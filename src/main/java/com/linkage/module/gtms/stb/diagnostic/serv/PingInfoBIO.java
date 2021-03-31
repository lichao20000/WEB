package com.linkage.module.gtms.stb.diagnostic.serv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.resource.FileSevice;
import com.linkage.module.gtms.stb.cao.PingCAO;
import com.linkage.module.gtms.stb.cao.TraceRouteCAO;
import com.linkage.module.gtms.stb.diagnostic.dao.PingInfoDAO;
import com.linkage.module.gtms.stb.obj.tr069.PingOBJ;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.corba.ACSCorba;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.act
 * 
 */
@SuppressWarnings("rawtypes")
public class PingInfoBIO 
{
	private static Logger logger = LoggerFactory.getLogger(PingInfoBIO.class);
	
	private static final String ROOTPARA = "Device.LAN.TraceRouteDiagnostics.RouteHops.";
	private static String instArea=Global.instAreaShortName;
	private PingInfoDAO dao;

    public List<Map<String,String>> queryIpMap() {
    	return dao.queryIpMap();
    }

    public List<Map<String,String>> pingForList(PingOBJ pingObj, String gw_type)
	{
		logger.debug("pingForList()");
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> pingOBJMap = null;
		PingCAO pingCAO = new PingCAO(gw_type);
		//1、pingiTV综合网管    云外
		//pingObj.setPingAddr("58.223.80.57");
		//2、pingiTV综合网管    云内
		pingObj.setPingAddr("180.100.134.11");
		pingCAO.setPingObj(pingObj);
		logger.warn("开始pingiTV综合网管云内(180.100.134.11)");
		pingCAO.ping();
		pingOBJMap = new HashMap<String, String>();
		pingOBJMap.put("hostName", "iTV综合网管("+pingObj.getPingAddr()+")");
		pingOBJMap.put("succNum", StringUtil.getStringValue(pingObj.getSuccNum()));
		pingOBJMap.put("failNum", StringUtil.getStringValue(pingObj.getFailNum()));
		pingOBJMap.put("delayAvg", StringUtil.getStringValue(pingObj.getDelayAvg()));
		pingOBJMap.put("delayMin", StringUtil.getStringValue(pingObj.getDelayMin()));
		pingOBJMap.put("delayMax", StringUtil.getStringValue(pingObj.getDelayMax()));
		pingOBJMap.put("faultCode", StringUtil.getStringValue(pingObj.getFaultCode()));
		list.add(pingOBJMap);
		logger.warn("pingiTV综合网管云内(180.100.134.11)返回码："+pingObj.getFaultCode());
		if(!isPingSucc(pingObj.getFaultCode()))
		{
			return list;
		}
		//2、一平台主EPG
		pingObj.setPingAddr("58.223.107.136");
		pingCAO.setPingObj(pingObj);
		logger.warn("开始ping一平台主EPG(58.223.107.136)");
		pingCAO.ping();
		pingOBJMap = new HashMap<String, String>();
		pingOBJMap.put("hostName", "一平台主EPG("+pingObj.getPingAddr()+")");
		pingOBJMap.put("succNum", StringUtil.getStringValue(pingObj.getSuccNum()));
		pingOBJMap.put("failNum", StringUtil.getStringValue(pingObj.getFailNum()));
		pingOBJMap.put("delayAvg", StringUtil.getStringValue(pingObj.getDelayAvg()));
		pingOBJMap.put("delayMin", StringUtil.getStringValue(pingObj.getDelayMin()));
		pingOBJMap.put("delayMax", StringUtil.getStringValue(pingObj.getDelayMax()));
		pingOBJMap.put("faultCode", StringUtil.getStringValue(pingObj.getFaultCode()));
		list.add(pingOBJMap);
		logger.warn("ping一平台主EPG(58.223.107.136)返回码："+pingObj.getFaultCode());
		if(!isPingSucc(pingObj.getFaultCode()))
		{
			return list;
		}
		//3、二平台主EPG
		pingObj.setPingAddr("58.223.143.14");
		pingCAO.setPingObj(pingObj);
		logger.warn("开始ping二平台主EPG(58.223.143.14)");
		pingCAO.ping();
		pingOBJMap = new HashMap<String, String>();
		pingOBJMap.put("hostName", "二平台主EPG("+pingObj.getPingAddr()+")");
		pingOBJMap.put("succNum", StringUtil.getStringValue(pingObj.getSuccNum()));
		pingOBJMap.put("failNum", StringUtil.getStringValue(pingObj.getFailNum()));
		pingOBJMap.put("delayAvg", StringUtil.getStringValue(pingObj.getDelayAvg()));
		pingOBJMap.put("delayMin", StringUtil.getStringValue(pingObj.getDelayMin()));
		pingOBJMap.put("delayMax", StringUtil.getStringValue(pingObj.getDelayMax()));
		pingOBJMap.put("faultCode", StringUtil.getStringValue(pingObj.getFaultCode()));
		list.add(pingOBJMap);
		logger.warn("ping二平台主EPG(58.223.143.14)返回码："+pingObj.getFaultCode());
		if(!isPingSucc(pingObj.getFaultCode()))
		{
			return list;
		}
		//4、业务管理平台
		pingObj.setPingAddr("58.223.251.139");
		pingCAO.setPingObj(pingObj);
		logger.warn("开始ping业务管理平台(58.223.251.139)");
		pingCAO.ping();
		pingOBJMap = new HashMap<String, String>();
		pingOBJMap.put("hostName", "业务管理平台("+pingObj.getPingAddr()+")");
		pingOBJMap.put("succNum", StringUtil.getStringValue(pingObj.getSuccNum()));
		pingOBJMap.put("failNum", StringUtil.getStringValue(pingObj.getFailNum()));
		pingOBJMap.put("delayAvg", StringUtil.getStringValue(pingObj.getDelayAvg()));
		pingOBJMap.put("delayMin", StringUtil.getStringValue(pingObj.getDelayMin()));
		pingOBJMap.put("delayMax", StringUtil.getStringValue(pingObj.getDelayMax()));
		pingOBJMap.put("faultCode", StringUtil.getStringValue(pingObj.getFaultCode()));
		list.add(pingOBJMap);
		logger.warn("ping业务管理平台(58.223.251.139)返回码："+pingObj.getFaultCode());
		return list;
	}
	
	public boolean isPingSucc(int faultCode)
	{
		if(faultCode == -1 || faultCode == -7 || faultCode == -6 ||faultCode == -9)
		{
			return false;
		}
		
		return true;
	}
	
	public PingOBJ pingTest(PingOBJ pingObj, String gw_type)
	{
		logger.debug("pingTest()");
		
		if(Global.SXLT.equals(instArea))
		{
			Map devMap=dao.getDevName(pingObj.getDeviceId());
			//文件服务器ip根据设备属地变化
			if("172.0.10.172".equals(pingObj.getPingAddr()))
			{
				String cityId = CityDAO.getLocationCityIdByCityId(StringUtil.getStringValue(devMap,"city_id"));
				String ip=getCityIP(pingObj.getDeviceId(),cityId);
				if(!StringUtil.IsEmpty(ip)){
					pingObj.setPingAddr(ip);	
				}
				
				cityId=null;
				ip=null;
			}
			
			devMap=null;
		}
		
		PingCAO pingCAO = new PingCAO(gw_type);
		pingCAO.setPingObj(pingObj);
		pingCAO.ping();
		return pingObj;
	}
	
	public String paramConfig(String servName, String servPasswd, String gw_type, String deviceId)
	{
		logger.debug("paramConfig()");
		ACSCorba acscorba= new ACSCorba(gw_type);
		ArrayList<ParameValueOBJ> objList = new ArrayList<ParameValueOBJ>();
		ParameValueOBJ obj1 = new ParameValueOBJ("Device.X_CU_STB.AuthServiceInfo.UserID",servName,"1"); 
		ParameValueOBJ obj2 = new ParameValueOBJ("Device.X_CU_STB.AuthServiceInfo.UserIDPassword",servPasswd,"1"); 
		objList.add(obj1);
		objList.add(obj2);
		int res = acscorba.setValue(deviceId, objList);
		logger.warn("下发servName[{}]servPasswd[{}]device[{}]参数结果{}",new Object[]{servName,servPasswd,deviceId,res});
		 if(res==1||res==0){
			 StbSystemCmdBIO cmdio = new StbSystemCmdBIO();
			 cmdio.reboot(deviceId, gw_type);
			 return "参数设置成功，正在重启设备。";
		 }else if(res == -7){
			 return "系统参数错误";
		 }else if(res == -6){
			 return "设备正被操作";
		 }else if(res == -1){
			 return "设备连接失败";
		 }else if(res == -9){
			 return "系统内部错误";
		 }else{
			 return "TR069错误";
		 }
	}
	
	
	public PingOBJ traceRoute(PingOBJ pingObj, String gw_type)
	{
		logger.debug("traceRoute({})" , new Object[]{pingObj});
//		TraceRouteCAO pingCAO = new TraceRouteCAO();
//		pingCAO.setPingObj(pingObj);
//		
//		pingCAO.pingACS();
//		// 如果调用ACS成功
//		if(pingObj.isSuccess())
//		{
//			// 进一步调用采集模块
//			pingCAO.pingSuperGather(pingObj);
//			// 采集成功，查询数据库数据
//			if(pingObj.isSuccess())
//			{
//				dao.queryTraceRouteInfo(pingObj);
//			}
//		}
//		return pingObj;
		TraceRouteCAO pingCAO = new TraceRouteCAO(gw_type);
	    pingCAO.setPingObj(pingObj);
	    pingCAO.pingACS();
	    return pingObj;
	}
	
	public PingOBJ traceRouteTest(PingOBJ pingObj, String gw_type)
	{
		logger.warn("traceRoute({})" , new Object[]{pingObj});
		
		Map devMap=null;
		if(Global.SXLT.equals(instArea))
		{
			devMap=dao.getDevName(pingObj.getDeviceId());
			//文件服务器ip根据设备属地变化
			if("172.0.10.172".equals(pingObj.getPingAddr()))
			{
				String cityId = CityDAO.getLocationCityIdByCityId(StringUtil.getStringValue(devMap,"city_id"));
				String ip=getCityIP(pingObj.getDeviceId(),cityId);
				if(!StringUtil.IsEmpty(ip)){
					pingObj.setPingAddr(ip);	
				}
				cityId=null;
				ip=null;
			}
		}
		
		TraceRouteCAO pingCAO = new TraceRouteCAO(gw_type);
		pingCAO.setPingObj(pingObj);
		pingCAO.pingACS();
		
		PingOBJ obj=pingCAO.getPingObj();
		if(Global.SXLT.equals(instArea)){
			obj.setOui(StringUtil.getStringValue(devMap, "oui"));
			obj.setSn(StringUtil.getStringValue(devMap, "device_serialnumber"));
		}
		
		return obj;
	}
	
	/**
	 * 获取地市ip
	 */
	private String getCityIP(String device_id,String cityId)
	{
		Map ipMap=dao.getCityIp(cityId);
		if(ipMap!=null && !ipMap.isEmpty()){
			logger.warn("[{}] [{}]-[{}]",device_id,cityId,StringUtil.getStringValue(ipMap,"ip"));
			return StringUtil.getStringValue(ipMap,"ip");
		}
		return null;
	}
	
	/**
	 * 山西联通特别要求
	 */
	public List<Map<String,String>> getRouteHopsData(String device_id,String gw_type)
	{
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		
		ACSCorba corba=new ACSCorba(gw_type);
		List<String> iList = corba.getIList(device_id, ROOTPARA);
		
		if(iList!=null && !iList.isEmpty())
		{
			iList=getSort(iList);
			logger.warn("[{}] getRouteHopsData.iList:{}",device_id,iList);
			FileSevice fileSevice = new FileSevice();
			@SuppressWarnings("unchecked")
			Map<String,String> expertMap = fileSevice.getSuggested(5);
			int ex_bias = StringUtil.getIntegerValue(expertMap.get("ex_bias"));
			String ex_regular = expertMap.get("ex_regular");
			
			for(String i:iList)
			{
				String[] gather_nodes=new String[1];
				gather_nodes[0]=ROOTPARA+i+".HopHost";
				/*gather_nodes[1]=ROOTPARA+i+".HopHostAddress";
				gather_nodes[2]=ROOTPARA+i+".HopErrorCode";
				gather_nodes[3]=ROOTPARA+i+".HopRTTimes";*/
				
				ArrayList<ParameValueOBJ> valuesList = corba.getValue(device_id, gather_nodes);
				if(null != valuesList && valuesList.size()!=0)
				{
					Map<String,String> nodeMap = new HashMap<String, String>();
					nodeMap.put("ex_bias", ex_bias+"");
					nodeMap.put("id", i+"");
					
					for(int j = 0; j < valuesList.size(); j++)
					{
						String name=valuesList.get(j).getName();
						String value=valuesList.get(j).getValue();
						
						if(name.endsWith("HopHost")){
							name="hopHost";
						}/*else if(name.endsWith("HopHostAddress")){
							name="hopHostAddress";
						}else if(name.endsWith("HopErrorCode")){
							name="hopErrorCode";
						}else if(name.endsWith("HopRTTimes")){
							name="hopRTTimes";
							
							String ex_desc=judgeIntValue(StringUtil.getDoubleValue(ex_bias), 
													StringUtil.getDoubleValue(value), ex_regular, 0);
							nodeMap.put("ex_desc", ex_desc);
						}*/
						
						nodeMap.put(name, value);
						name=null;
						value=null;
					}
					list.add(nodeMap);
				}
				
				gather_nodes=null;
			}
		}else{
			logger.warn("[{}] 获取iList为空或失败",device_id);
		}
		
		return list;
	}
	
	/**
	 * 节点i排序
	 */
	private List<String> getSort(List<String> iList)
	{
		List<String> list=new ArrayList<String>();
		
		int[] arr=new int[iList.size()];
		for(int i=0;i<iList.size();i++){
			arr[i]=StringUtil.getIntegerValue(iList.get(i));
		}
		Arrays.sort(arr);
		
		for(int x:arr){
			list.add(StringUtil.getStringValue(x));
		}
		
		return list;
	}
	
	/**
	 * 判断值
	 * @param biasValue  参考值
	 * @param factValue  实际值
	 * @param regular    判断符号
	 * @param orValue    正负波动值
	 */
	private String judgeIntValue(double biasValue, double factValue, String regular, double orValue)
	{
		if("<".equals(regular))
		{
			return factValue < biasValue ? "正常" : "异常";
		}
		else if(">".equals(regular))
		{
			return factValue > biasValue ? "正常" : "异常";
		}
		else if("=".equals(regular))
		{
			return factValue == biasValue ? "正常" : "异常";
		}
		else if("<>".equals(regular))
		{
			return (biasValue - orValue < factValue && factValue < biasValue + orValue) ? "正常" : "异常";
		}else{
			return "";
		}
	}

	
	/**
	 * 根据city_id查询IP类别，IP名称
	 */
 	public String getIpTypeByCityId(String cityId) 
 	{
		List list = dao.getIpTypeByCityId(cityId);
		
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++)
		{
			Map map = (Map) list.get(i);
			if (i > 0){
				bf.append("#");
			}
			bf.append(StringUtil.getStringValue(map, "ip_type", ""));
			bf.append("$");
			bf.append(StringUtil.getStringValue(map, "ip_name", ""));
		}
		return bf.toString();
	}
	
	
	/**
	 * 根据属地ID，IP类别 检索相应的IP地址
	 */
	public String getIpByIpType(String cityId, String ipType) 
	{
		List list = dao.getIpByIpType(cityId, ipType);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++)
		{
			Map map = (Map) list.get(i);
			if (i > 0)
			{
				bf.append("#");
			}
			bf.append(StringUtil.getStringValue(map, "ip", ""));
			bf.append("$");
			bf.append(StringUtil.getStringValue(map, "ip", ""));
		}
		return bf.toString();
	}
	
	

	public PingInfoDAO getDao() {
		return dao;
	}

	public void setDao(PingInfoDAO dao) {
		this.dao = dao;
	}
	
	
	
}
