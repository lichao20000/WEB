package com.linkage.module.liposs.performance.bio;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.corba.interfacecontrol.FluxManagerInterface;
import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterface;
import com.linkage.module.liposs.performance.bio.snmpGather.FluxConfigInit;
import com.linkage.module.liposs.performance.bio.snmpGather.FluxPortInfo;
import com.linkage.module.liposs.performance.bio.snmpGather.PortJudgeAttr;
import com.linkage.module.liposs.performance.dao.ConfigFluxDao;

/**
 * @author wangping5221
 * @version 1.0
 * @since 2008-10-29
 * @category com.linkage.liposs.bio.performance 版权：南京联创科技 网管科技部
 */
public class FluxConfigMainThread implements Runnable
{

	// 日志类
	private static Logger log = LoggerFactory.getLogger(FluxConfigMainThread.class);
	private ThreadPool threadPool = null;
	// 流量配置得工具类
	private FluxConfigInit fluxInit = null;
	// 数据库操作类
	private ConfigFluxDao dao = null;
	// 设备链表
	private List<String> deviceList = null;
	// 告警参数表
	private Flux_Map_Instance alarmParam = null;
	// 端口列表
	private List<FluxPortInfo> portList = null;
	// 默认自动配置(1:自动配置，0：非自动配置)
	private int isauto = 1;
	// 默认总体配置（1：总体配置，0：非总体配置）
	private int total = 1;
	// 默认采集方式（snmp版本_计数器）
	private String gather_flag = "2_64";
	// 默认原始数据不入库（0:不入库，1：入库）
	private int intodb = 0;
	// 采集间隔时间（默认300s）
	private int polltime = 300;
	// 是否需要保存原有的告警配置，默认需要
	private boolean isKeep = true;

	/**
	 * 通过流量配置界面配置流量，支持批量/单个设备
	 * 
	 * @param threadPool
	 *            线程池
	 * @param deviceList
	 *            需要配置的设备id链表
	 * @param alarmParam
	 *            流量配置界面设置的告警参数
	 * @param fluxInit
	 *            流量配置的初始化类，为了获取oid或其它操作
	 * @param dao
	 *            数据库操作类
	 * @param portList
	 *            端口列表（只有在单个设备并选择非总体配置才有用）
	 * @param isauto
	 *            自动配置标志位（1:自动配置，0：非自动配置）
	 * @param total
	 *            总体配置标志位（1：总体配置，0：非总体配置）
	 * @param gather_flag
	 *            采集方式（snmp版本_计数器），只有在选择非自动配置才生效
	 * @param intodb
	 *            原始数据是否入库（0:不入库，1：入库）
	 * @param polltime
	 *            采集间隔时间
	 * @param isKeep
	 *            是否需要保存原有的告警配置（true：需要保留原有的端口告警配置，false：按照界面设置的告警参数来配置）
	 */
	public FluxConfigMainThread(ThreadPool threadPool, List<String> deviceList,
			Flux_Map_Instance alarmParam, FluxConfigInit fluxInit, ConfigFluxDao dao,
			List<FluxPortInfo> portList, int isauto, int total, String gather_flag,
			int intodb, int polltime, boolean isKeep)
	{
		log.debug("begin FluxConfigMainThread");
		this.threadPool = threadPool;
		this.deviceList = deviceList;
		this.alarmParam = alarmParam;
		this.fluxInit = fluxInit;
		this.dao = dao;
		this.portList = portList;
		this.isauto = isauto;
		this.total = total;
		this.gather_flag = gather_flag;
		this.intodb = intodb;
		this.isKeep = isKeep;
		this.polltime = polltime;
	}

	/**
	 * 默认配置（就是流量配置对外提供的接口，目前只给设备模板配置那边的总体配置使用）
	 * 
	 * @param threadPool
	 *            线程池
	 * @param deviceList
	 *            需要配置的设备id链表
	 * @param alarmParam
	 *            流量配置界面设置的告警参数
	 * @param dao
	 *            数据库操作类
	 */
	public FluxConfigMainThread(ThreadPool threadPool, List<String> deviceList,
			FluxConfigInit fluxInit, ConfigFluxDao dao)
	{
		this.threadPool = threadPool;
		this.deviceList = deviceList;
		this.alarmParam = new Flux_Map_Instance();
		this.fluxInit = fluxInit;
		this.dao = dao;
	}

	/**
	 * 对配置设备试采，确定采集方式，缓存原先配置的端口告警，把设备每个端口再拆分采集子任务进行配置
	 */
	public void run()
	{
		/**
		 * 重要参数的检查
		 */
		if (null == deviceList || 0 == deviceList.size())
		{
			log("参数错误，设备列表为空！");
			return;
		}
		if (deviceList.size() > 1 && total == 0)
		{
			log("流量批量配置，不能选择非总体配置！");
			return;
		}
		List<Map> deviceInfoList = dao.getDevInfo(deviceList);
		String serial = "";
		String device_id = "";
		List<PortJudgeAttr> oidList = null;
		Map<String, Flux_Map_Instance> deviceAlarmMap = null;
		Future temp = null;
		List<Future> taskResultList = new ArrayList<Future>();
		/**
		 * 遍历设备信息，进行采集
		 */
		for (Map deviceInfo : deviceInfoList)
		{
			serial = String.valueOf(deviceInfo.get("serial"));
			device_id = String.valueOf(deviceInfo.get("device_id"));
			log("begin 设备配置 device_id:"+device_id);
			/**
			 * 判断是否需要采集端口信息， 总体配置需要采集设备端口信息
			 */
			if (total == 1)
			{				
				portList=fluxInit.getDevicePortList(device_id,serial);
				// 端口过滤
				portList = fluxInit.filterPort(device_id, portList);
			}
			// 判断是否有端口
			if (portList.isEmpty())
			{
				log("设备id：" + device_id + " 配置失败！");
				deviceList.remove(device_id);
				continue;
			}
			log("设备device_id:"+device_id+"  端口数："+portList.size());
			/**
			 * 确定采集方式,自动配置需要试采，非自动配置根据用户选择的采集方式
			 */
			if (1 == isauto)
			{
				FluxPortInfo fpi = portList.get(0);
				String version=dao.getSnmpversion(device_id);
				/**
				 * 使用V2版本的64位计数器进行试采
				 */
				if (confirmGather(device_id, serial, fpi, version, "64"))
				{
					gather_flag = version+"_64";
				}
				/**
				 * 使用V1版本的32位计数器
				 */
				else if (confirmGather(device_id, serial, fpi, version, "32"))
				{
					gather_flag = version+"_32";
				}
				
				else
				{
					log("3个版本都试采不到端口性能数据 device_id:" + device_id);
					gather_flag = "";
					deviceList.remove(device_id);
					continue;
				}		
			}
			
			log("设备device_id:"+device_id+"  采集方式："+gather_flag);
			/**
			 * 删除设备配置信息前，缓存设备阈值告警
			 */
			if (this.isKeep)
			{
				deviceAlarmMap = dao.loadDeviceAlarm(device_id);
			}
			else
			{
				deviceAlarmMap = new HashMap<String, Flux_Map_Instance>();
			}
			if (!dao.delFluxConfig(device_id))
			{
				log("设备 device_id:" + device_id + "删除配置信息失败！");
				deviceList.remove(device_id);
				continue;
			}
			/**
			 * 遍历同一个设备下的各端口
			 */
			for (FluxPortInfo fpi : portList)
			{
				log("FluxConfigMainThread   =====>> portList的值===="+fpi.toString());
				Flux_Map_Instance flux_instance = this.alarmParam;
				if (deviceAlarmMap.containsKey(device_id + "||" + fpi.getGetway() + "||"
						+ fpi.getPort_info()))
				{
					flux_instance = deviceAlarmMap.get(device_id + "||" + fpi.getGetway()
							+ "||" + fpi.getPort_info());
				}
				FluxConfigChildThread task = new FluxConfigChildThread(flux_instance,
						this.dao, this.fluxInit, device_id, serial, this.gather_flag, fpi, this.intodb,
						this.polltime,this.total);
				temp = threadPool.submitLowerLevelTask(task);
				taskResultList.add(temp);
			}
		}// 设备遍历结束
		
		//clear
		this.dao=null;
		
		/**
		 * 等待子任务完成
		 */
		while (!isComplete(taskResultList))
		{
			try
			{
				// 等待5秒
				Thread.sleep(5000);
			}
			catch (InterruptedException e)
			{
				log("等待子任务失败！");
			}
		}
		String[] device_ids = new String[deviceList.size()];
		deviceList.toArray(device_ids);
		FluxManagerInterface.GetInstance().readDevices(device_ids);
		log("配置结束");
	}

	/**
	 * 判断指定线程任务是否执行完
	 * 
	 * @param taskResultList
	 * @return
	 */
	private boolean isComplete(List<Future> taskResultList)
	{
		Iterator<Future> it = taskResultList.iterator();
		Future temp;
		while (it.hasNext())
		{
			temp = it.next();
			if (!temp.isDone())
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * 根据指定版本试采指定设备端口得流入、流出，返回是否采集到
	 * 
	 * @param device_id
	 * @param serial
	 * @param fpi
	 * @param snmpVersion
	 * @param counterNum
	 * @return 只要流入速率、流出速率有一个可以采集到，则返回true，否则返回false
	 */
	private boolean confirmGather(String device_id, String serial, FluxPortInfo fpi,
			String snmpVersion, String counterNum)
	{
		log.debug("开始试采snmpVersion版本：" + snmpVersion + "    计数器：" + counterNum
				+ "    设备id：" + device_id + "    端口索引:" + fpi.getIfindex());
		/**
		 * 使用V2版本的64位计数器进行试采
		 */
		List<PortJudgeAttr> tempOidList = fluxInit.getOIDList(serial, snmpVersion,
				counterNum, "1");
		Performance.Data[] datalist = null;
		// 初始化OID没问题，并设备端口也存在
		if (tempOidList.size() > 2)
		{
			// 只要流入、流出字节数OID
			String[] oidArray = new String[2];
			oidArray[0] = ((PortJudgeAttr) tempOidList.get(0)).getValue() + "."
					+ fpi.getIfindex();
			oidArray[1] = ((PortJudgeAttr) tempOidList.get(1)).getValue() + "."
					+ fpi.getIfindex();
			// 采集数据
			datalist = SnmpGatherInterface.GetInstance().GetFillDataArrayIMDFull(device_id,
					"", oidArray, oidArray.length);
			if (null != datalist && 0 != datalist.length)
			{
				// 只有流入、流出字节数都没有采到的情况，认为这个版本这个计数器不使用
				if ((null != datalist[0].dataStr
						&& !"NODATA".equalsIgnoreCase(datalist[0].dataStr)
						&& !"".equalsIgnoreCase(datalist[0].dataStr.trim()) && !"error"
						.equalsIgnoreCase(datalist[0].dataStr))
						|| (null != datalist[1].dataStr
								&& !"NODATA".equalsIgnoreCase(datalist[1].dataStr)
								&& !"".equalsIgnoreCase(datalist[1].dataStr.trim()) && !"error"
								.equalsIgnoreCase(datalist[1].dataStr)))
				{
					log.debug("结束试采snmpVersion版本：" + snmpVersion + "    计数器：" + counterNum
							+ "    设备id：" + device_id + "    端口索引:" + fpi.getIfindex()
							+ " 成功");
					return true;
				}
			}
		}
		log.debug("结束试采snmpVersion版本：" + snmpVersion + "    计数器：" + counterNum
				+ "    设备id：" + device_id + "    端口索引:" + fpi.getIfindex() + " 失败");
		return false;
	}

	/**
	 * 记录日志
	 * 
	 * @param msg
	 */
	private void log(String msg)
	{
		log.warn(msg);
	}
}
