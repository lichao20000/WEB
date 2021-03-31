
package com.linkage.module.liposs.performance.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Performance.Data;

import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterface;
import com.linkage.module.liposs.performance.bio.snmpGather.FluxConfigInit;
import com.linkage.module.liposs.performance.bio.snmpGather.FluxPortInfo;
import com.linkage.module.liposs.performance.bio.snmpGather.PortJudgeAttr;
import com.linkage.module.liposs.performance.dao.ConfigFluxDao;

/**
 * @author 王萍（5221）
 * @version 1.0
 * @since 2008-10-30
 * @category com.linkage.liposs.bio.performance<br>
 * @copyright 版权：南京联创科技 网管科技部
 */
public class FluxConfigChildThread implements Runnable
{

	// 日志类
	private static Logger log = LoggerFactory.getLogger(FluxConfigChildThread.class);
	// 流量配置得工具类
	private FluxConfigInit fluxInit = null;
	// 数据库操作类
	private ConfigFluxDao dao = null;
	// 默认原始数据不入库（0:不入库，1：入库）
	private int intodb = 0;
	// 采集间隔时间（默认300s）
	private int polltime = 300;
	// 设备id
	private String device_id = "";
	// 设备型号id
	private String serial = "";
	// 默认采集方式（snmp版本_计数器）
	private String gather_flag = "2_64";
	// 默认总体配置（1：总体配置，0：非总体配置）
	private int total = 1;
	// 端口信息
	private FluxPortInfo fpi = null;
	// 告警参数
	private Flux_Map_Instance flux_instance = null;

	/**
	 * 设备单个端口的配置
	 * 
	 * @param flux_instance
	 *            告警参数
	 * @param dao
	 *            数据库操作类
	 * @param fluxInit
	 *            流量配置的初始化类，为了获取oid或其它操作
	 * @param device_id
	 *            设备ID
	 * @param serial
	 *            设备型号id
	 * @param gather_flag
	 *            采集方式（snmp版本_计数器）
	 * @param port
	 *            端口信息
	 * @param intodb
	 *            原始数据是否入库（0:不入库，1：入库）
	 * @param polltime
	 *            采集间隔时间
	 */
	public FluxConfigChildThread(Flux_Map_Instance flux_instance, ConfigFluxDao dao,
			FluxConfigInit fluxInit, String device_id, String serial, String gather_flag,
			FluxPortInfo port, int intodb, int polltime,int total)
	{
		this.flux_instance = flux_instance;
		this.dao = dao;
		this.fluxInit = fluxInit;
		this.device_id = device_id;
		this.serial = serial;
		this.gather_flag = gather_flag;
		this.fpi = port;
		this.intodb = intodb;
		this.polltime = polltime;
		this.total=total;
	}

	
	/**
	 * 每个端口采集流入、流出速率等指标，端口类型、高速带宽等指标
	 */
	public void run()
	{
		log.warn("FluxConfigChildThread ===begin 配置 device_id:"+device_id+"   gather_flag:"+gather_flag+"    ifindex:"+fpi.getIfindex());
		// 版本
		String snmpversion = gather_flag.split("_")[0];
		// 计数器
		String counternum = gather_flag.split("_")[1];
		// oid中型号id
		String model = "";
		// 采集参数
		String getarg = "";
		// 端口类型
		int iftype = 0;
		// 端口最大传输单元
		long ifmtu = 0;
		// 端口速率
		long ifspeed = 0;
		// 高速端口速率
		long ifhighspeed = 0;
		// 实际带宽
		long if_real_speed = 0;
		/**
		 * 采集性能指标，在采集不到流入或流出的端口不予配置
		 */
		Data[] datalist = null;
		List<PortJudgeAttr> tempList = fluxInit.getOIDList(this.serial, snmpversion,
				counternum, "1");
		List<String> perfList = new ArrayList<String>();
		if (tempList.size() < 2)
		{
			log.warn("没有获取到性能列表的oid，device_id:" + device_id + "  ifindex:"
					+ fpi.getIfindex() + "  snmpversion:" + snmpversion
					+ "   counternum:" + counternum + " oidtype:1");
			return;
		}
		model = tempList.get(0).getModel();
		//总体配置
		if(1==this.total)
		{
			getarg = fluxInit.getGetarg(tempList);
		}
		else
		{
			getarg=fpi.getCollectParm();
		}
		
		for (PortJudgeAttr oidObj : tempList)
		{
			perfList.add(oidObj.getValue() + "." + fpi.getIfindex());
		}
		String[] oidArray = new String[perfList.size()];
		perfList.toArray(oidArray);
		datalist = SnmpGatherInterface.GetInstance().GetFillDataArrayIMDFull(device_id,
				"", oidArray, oidArray.length);
		if (null == datalist || 0 == datalist.length)
		{
			log.warn("设备端口性能数据没有采集到 device_id:" + device_id + "   ifindex:"+ fpi.getIfindex());
			return;
		}
		// 检查流入、流出速率能不能采集到
		if (!fluxInit.checkString(datalist[0].dataStr)
				||! fluxInit.checkString(datalist[1].dataStr))
		{
			log.warn("流入或流出字节数没有采到数据 device_id:" + device_id + "   ifindex:"+ fpi.getIfindex());
			return;
		}
		// clear
		tempList = null;
		perfList.clear();
		oidArray = null;
		/**
		 * 采集端口类型、端口带宽等指标
		 */
		tempList = fluxInit.getOIDList(this.serial, snmpversion, counternum, "3");
		for (PortJudgeAttr oidObj : tempList)
		{
			perfList.add(oidObj.getValue() + "." + fpi.getIfindex());
		}
		oidArray = new String[perfList.size()];
		perfList.toArray(oidArray);
		datalist = SnmpGatherInterface.GetInstance().GetDataArrayAllIMDFull(device_id,
				"", oidArray, oidArray.length);
		for (int i = 0; i < datalist.length; i++)
		{
			// 端口类型
			if (0 == i && fluxInit.checkString(datalist[i].dataStr))
			{
				iftype = Integer.parseInt(datalist[i].dataStr.trim());
			}
			// 端口最大传输单元
			else if (1 == i && fluxInit.checkString(datalist[i].dataStr))
			{
				ifmtu = Long.parseLong(datalist[i].dataStr.trim());
			}
			// 端口速率
			else if (2 == i && fluxInit.checkString(datalist[i].dataStr))
			{
				ifspeed = Long.parseLong(datalist[i].dataStr.trim());
			}
			// 高速端口速率
			else if (3 == i && fluxInit.checkString(datalist[i].dataStr))
			{
				ifhighspeed = Long.parseLong(datalist[i].dataStr.trim());
			}
		}
		// 根据端口类型过滤端口
		if (fluxInit.isFilterPortByPortType(device_id, iftype))
		{
			log("端口被端口过滤规则过滤掉了 device_id:" + device_id + "   ifindex:"+ fpi.getIfindex() + "  iftype:" + iftype);
			return;
		}
		// 高速端口速率不为空，就×1M，否则取端口速率
		if (ifhighspeed == 0)
		{
			ifhighspeed = ifspeed;
		}
		else
		{
			ifhighspeed = ifhighspeed * 1000000;
		}
		//由于商务领航的带宽无法采集，因此采用用户表的带宽
		// 高速端口速率不为0,做下数据转换
		/**if (ifhighspeed != 0)
		{
			if_real_speed = fluxInit.ParseRealSpeed(ifhighspeed);
		}**/
		List bandwidthList = dao.getBandwidth(device_id);
		if(null==bandwidthList ||bandwidthList.size()<1){
			if_real_speed = (long)2*1024;
		}else{
			Map mapBandwidth = (Map) bandwidthList.get(0);
			if(null==mapBandwidth.get("bandwidth")){
				if_real_speed = (long)2*1024;
			}else{
				if_real_speed = Long.parseLong(String.valueOf(mapBandwidth.get("bandwidth")))*1024;
			}
		}
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("device_id", device_id);
		param.put("snmpversion", snmpversion);
		param.put("model", model);
		param.put("counternum", counternum);
		param.put("polltime", String.valueOf(polltime));
		param.put("gatherflag", "1");
		param.put("ifconfigflag", "1");
		param.put("intodb", String.valueOf(intodb));
		param.put("getarg", getarg);
		param.put("iftype", String.valueOf(iftype));
		param.put("ifmtu", String.valueOf(ifmtu));
		param.put("ifspeed", String.valueOf(ifspeed));
		param.put("ifhighspeed", String.valueOf(ifhighspeed));
		param.put("if_real_speed", String.valueOf(if_real_speed));
		dao.configDevicePortFlux(param, fpi, flux_instance);
	}

	/**
	 * 记录日志
	 * @param msg
	 */
	private void log(String msg)
	{
		log.debug(msg);		
	}
}
