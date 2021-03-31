package com.linkage.module.liposs.performance.bio.snmpGather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import Performance.Data;

import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterface;
import com.linkage.module.liposs.performance.bio.Flux_Map_Instance;
import com.linkage.module.liposs.performance.bio.pefdef.PerDefByDomainAndDescId;
import com.linkage.system.utils.DateTimeUtil;

public class ReadFluxConfigPortInfo
{
	// 日志类
	private static Logger log = LoggerFactory.getLogger(ReadFluxConfigPortInfo.class);
	/**
	 * 根据oid采集这个设备得端口信息
	 * 
	 * @param device_id
	 *            设备id
	 * @param oid
	 * @param isPortIP
	 *            是否端口IPOID 考虑到采集出来数据格式不一样 true：是端口ip得oid；false：不是端口IP得oid
	 * @return 设备端口信息，key:端口索引 value：oid对应得端口指标
	 */
	private static Map<String, String> getPortInfo(String device_id, String oid, boolean isPortIP)
	{
		Map<String, String> portMap = new HashMap<String, String>();
		/**
		 * 调用后台接口获取采集数据
		 */
		org.omg.CORBA.IntHolder dataType = new org.omg.CORBA.IntHolder();
		Data[] dataList = SnmpGatherInterface.GetInstance().GetDataListIMDFull(dataType,
				device_id, "", oid);
		if (null == dataList || 0 == dataList.length)
		{
			return portMap;
		}
		
		/**
		 * 遍历采集结果
		 */
		for (Data data : dataList)
		{
			if (isValid(data.index)&&isValid(data.dataStr))
			{
				/**
				 * 端口IP得情况下，后台采集出来得索引是端口IP，值是端口索引
				 * 其它OID，是索引是端口索引，值是端口IP
				 */
				if(isPortIP)
				{
					log.warn(".......当为端口IP时......");
					log.warn(" data.dataStr="+data.dataStr+"    data.index="+data.index);
					portMap.put(data.dataStr.trim(), data.index.trim());
					log.warn("portMap.toString()="+portMap.toString());
				}
				else
				{
					portMap.put(data.index.trim(), data.dataStr.trim());
				}
			}
		}
		
		return portMap;
	}
	
	/**
	 * 校验是否有效
	 * @param value
	 * @return
	 */
	private static boolean isValid(String value)
	{
		if (null == value ||"".equals(value.trim())
				||"error".equalsIgnoreCase(value.trim())
				||"nodata".equalsIgnoreCase(value.trim()))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * 根据告警参数统计设置了多少个阈值
	 * @param alarmInstance
	 * @return
	 */
	public static int getConfignumByAlarm(Flux_Map_Instance alarmInstance)
	{
		int countNum = 0;
		// 端口流入带宽利用率阈值一操作符
		if(alarmInstance.getIfinoct_maxtype()!= 0)
		{
			countNum ++;
		}
		// 端口流出带宽利用率阈值一操作符
		if(alarmInstance.getIfoutoct_maxtype()!= 0 )
		{
			countNum ++;
		}
		// 端口流入丢包率阈值
		if(alarmInstance.getIfindiscardspps_max()>= 0)
		{
			countNum ++;
		}
		// 端口流出丢包率阈值
		if(alarmInstance.getIfoutdiscardspps_max()>= 0)
		{
			countNum ++;
		}
		// 端口流入错包率阈值
		if(alarmInstance.getIfinerrorspps_max()>= 0)
		{
			countNum ++;
		}
		// 端口流出错包率阈值
		if(alarmInstance.getIfouterrorspps_max()>= 0)
		{
			countNum ++;
		}
		// 端口流入利用率阈值二操作符
		if(alarmInstance.getIfinoct_mintype()!= 0)
		{
			countNum ++;
		}
		// 端口流出利用率阈值二操作符
		if(alarmInstance.getIfoutoct_mintype()!= 0)
		{
			countNum ++;
		}
		// 动态阈值一操作符
		if(alarmInstance.getOvermax()!= 0)
		{
			countNum ++;
		}
		// 动态阈值二操作符
		if(alarmInstance.getOvermin()!= 0)
		{
			countNum ++;
		}
		// 判断是否配置流入突变告警操作
		if(alarmInstance.getIntbflag()!= 0)
		{
			countNum ++;
		}
		// 是否配置流出突变告警操作
		if(alarmInstance.getOuttbflag()!= 0)
		{
			countNum ++;
		}
		return countNum;
	}
	
	
	/**
	 * 采集设备端口信息
	 * @param device_id
	 * @param oidList  采集设备端口信息得oid 即oid_type:2
	 * @return
	 */
	public static ArrayList<FluxPortInfo> getDeviceInfo(String device_id,List<PortJudgeAttr> oidList)
	{
		log.debug("begin getDeviceInfo oidList_size:"+oidList.size()+"   "+ new DateTimeUtil().getLongDate());
		ArrayList<FluxPortInfo> portList = new ArrayList<FluxPortInfo>();
		if(oidList==null || oidList.isEmpty()){
			log.error("采集设备端口信息error：OIDList is  null ");
			return portList;
		}
		/**
		 * oid是固定初始化好得，并且顺序也是固定排好，依次是端口索引、端口描述、端口名字、端口别名、端口IP
		 */
		//采集端口索引
		String oid=oidList.get(0).getValue();
		Map<String,String> ifindexMap =getPortInfo(device_id,oid,false);
		if(ifindexMap.size()==0)
		{
			log.debug("device_id:"+device_id+"  没有采集到端口！");
			log.warn("device_id:"+device_id+"  没有采集到端口！");
			return portList;
		}else{
			log.warn("device_id:"+device_id+"  采集到端口！");
			log.warn("ifindexMap:"+ifindexMap.toString());
		}
		
		//采集端口描述
		oid=oidList.get(1).getValue();
		Map<String,String> ifdescrMap =getPortInfo(device_id,oid,false);
		
		//采集端口名字
		oid=oidList.get(2).getValue();
		Map<String,String> ifnameMap =getPortInfo(device_id,oid,false);
		
		//采集端口别名
		oid=oidList.get(3).getValue();
		Map<String,String> ifnamedefinedMap =getPortInfo(device_id,oid,false);
		
		//采集端口IP
		oid=oidList.get(4).getValue();
		Map<String,String> ifportipMap =getPortInfo(device_id,oid,true);
		
		//整理端口信息
		Iterator<String> ifindexIt=ifindexMap.keySet().iterator();
		String ifindex="";
		while(ifindexIt.hasNext())
		{
			ifindex=ifindexIt.next();
			FluxPortInfo fpi = new FluxPortInfo();
			fpi.setIfindex(ifindex);
			if(ifdescrMap.containsKey(ifindex))
			{
				fpi.setIfdescr(ifdescrMap.get(ifindex));
			}	
			if(ifnameMap.containsKey(ifindex))
			{
				fpi.setIfname(ifnameMap.get(ifindex));
			}
			if(ifnamedefinedMap.containsKey(ifindex))
			{
				fpi.setIfnamedefined(ifnamedefinedMap.get(ifindex));
			}
			if(ifportipMap.containsKey(ifindex))
			{
				fpi.setIfportip(ifportipMap.get(ifindex));
			}
			
			portList.add(fpi);
		}
		
		//clear
		ifindexIt=null;
		ifindexMap=null;
		ifdescrMap=null;
		ifnameMap=null;
		ifnamedefinedMap=null;
		ifportipMap=null;
		
		log.debug("end getDeviceInfo port_size:"+portList.size()+"   "+ new DateTimeUtil().getLongDate());
		return portList;
	}
}