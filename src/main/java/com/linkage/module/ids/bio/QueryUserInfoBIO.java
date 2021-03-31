package com.linkage.module.ids.bio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.ids.dao.QueryUserInfoDAO;
		
public class QueryUserInfoBIO
{
	private static Logger logger = LoggerFactory.getLogger(QueryUserInfoBIO.class);
	private QueryUserInfoDAO dao;
	private static final String indexName="ids";
	private static final String indexTypePon="ponstatus";
	private static final String indexTypeNet="netparam";
	private static final String indexTypeVoip="voicestatus";
	private static final String indexTypePonPort="poninfo";
	private static final String indexTypeLanPort="laninfo";

	/**
	 *      查询信息根据设备sn或者loid
			* @param deviceSerialNumber
			* @param gw_type
			* @return
	 */
public Map<String, String> getDeviceInfo(String paramType, String param) {
	   logger.warn("进入getDeviceInfo方法，paramType is:[{}],param is:[{}]",new Object[]{paramType,param});
		
		Map<String, String> deviceInfoMap = new HashMap<String, String>();
		List<HashMap<String,String>> devInfo=null;
		List<HashMap<String,String>> userInfo=null;
		//按设备sn
		if("device_serialnumber".equals(paramType)){
			devInfo= dao.getDeviceInfo(param);
				userInfo=dao.getDeviceHUserInfo(param);
		}
		//按Loid
		else if("loid".equals(paramType)){
			devInfo= dao.getDeviceInfoByLoid(param);
				userInfo=dao.getDeviceHUserInfoByLoid(param);
		}
        //设备信息
		if (null!=devInfo&&devInfo.size() > 0) {
			Map<String,String> oneDevInfo =  devInfo.get(0);
			String device_Id = StringUtil.getStringValue(oneDevInfo,"device_id","");
			String oui = StringUtil.getStringValue(oneDevInfo,"oui","");
			String device_serialnumber = StringUtil.getStringValue(oneDevInfo,"device_serialnumber","");
			String loopback_ip = StringUtil.getStringValue(oneDevInfo,"loopback_ip","");
			String vendor_add = StringUtil.getStringValue(oneDevInfo,"vendor_add","");
			String device_model = StringUtil.getStringValue(oneDevInfo,"device_model","");
			String hardwareversion = StringUtil.getStringValue(oneDevInfo,"hardwareversion","");
			String softwareversion = StringUtil.getStringValue(oneDevInfo,"softwareversion","");
            String cpe_mac=StringUtil.getStringValue(oneDevInfo,"cpe_mac","");
            String ip_model_type = StringUtil.getStringValue(oneDevInfo,"ip_model_type",""); 
			deviceInfoMap.put("device_Id", device_Id);
			deviceInfoMap.put("oui", oui);
			deviceInfoMap.put("device_serialnumber", device_serialnumber);
			deviceInfoMap.put("loopback_ip", loopback_ip);
			deviceInfoMap.put("vendor_add", vendor_add);
			deviceInfoMap.put("device_model", device_model);
			deviceInfoMap.put("hardwareversion", hardwareversion);
			deviceInfoMap.put("softwareversion", softwareversion);
			deviceInfoMap.put("register", "已注册");
			deviceInfoMap.put("cpe_mac", cpe_mac);
			deviceInfoMap.put("ip_model_type", ip_model_type);
		}
		//用户信息
   		if (null!=userInfo&&userInfo.size() > 0) {
   			Map<String,String> oneUserInfo =  userInfo.get(0);
			String user_id = StringUtil.getStringValue(oneUserInfo,"user_id","");
			String username = StringUtil.getStringValue(oneUserInfo,"username","");
			String linkman = StringUtil.getStringValue(oneUserInfo,"linkman","");
			String linkaddress = StringUtil.getStringValue(oneUserInfo,"linkaddress","");
			String credno = StringUtil.getStringValue(oneUserInfo,"credno","");
			String linkphone = StringUtil.getStringValue(oneUserInfo,"linkphone","");
			String device_port = StringUtil.getStringValue(oneUserInfo,"device_port","");
			String knname = StringUtil.getStringValue(oneUserInfo,"kdname","");	
			String macaddress = StringUtil.getStringValue(oneUserInfo,"macaddress","");
			String bandwidth = StringUtil.getStringValue(oneUserInfo,"bandwidth","");

			deviceInfoMap.put("user_id", user_id);
			deviceInfoMap.put("username", username);
			deviceInfoMap.put("linkman", linkman);
			deviceInfoMap.put("linkaddress", linkaddress);
			deviceInfoMap.put("credno", credno);
			deviceInfoMap.put("linkphone", linkphone);
			deviceInfoMap.put("device_port", device_port);
			deviceInfoMap.put("kdname", knname);
			deviceInfoMap.put("macaddress", macaddress);
			deviceInfoMap.put("bandwidth", bandwidth);
		}

        //设备状态
		Map<String,String> devMap=dao.queryDevCurrentStatus( indexName,indexTypePon, paramType, param);
		if(null!=devMap&&!devMap.isEmpty()){
			deviceInfoMap.putAll(devMap);
		}
		//宽带
		Map<String,String> netMap=dao.queryNetCurrentStatus( indexName,indexTypeNet, paramType, param);
		if(null!=netMap&&!netMap.isEmpty()){
			deviceInfoMap.putAll(netMap);
		}
		//语音
		Map<String,String> voipMap=dao.queryVoipCurrentStatus( indexName,indexTypeVoip, paramType, param);
		if(null!=voipMap&&!voipMap.isEmpty()){
			deviceInfoMap.putAll(voipMap);
		}
		//下载
		Map<String,String> httpMap=null;
		if("loid".equals(paramType)){
			httpMap=dao.getHttpInfoByLoid(param);
		}else{
			httpMap=dao.getHttpInfo(param);
		}
		if(null!=StringUtil.getStringValue(httpMap, "bom_time")&&null!=StringUtil.getStringValue(httpMap, "eom_time")){
			String downPert=this.getDownPert(StringUtil.getStringValue(httpMap, "bom_time"), StringUtil.getStringValue(httpMap, "eom_time"), StringUtil.getStringValue(httpMap, "total_bytes_rece"));
	        deviceInfoMap.put("downPert", downPert);
		}
        if(null!=StringUtil.getStringValue(httpMap, "test_time")){
        	 DateTimeUtil dateUtil = new DateTimeUtil(StringUtil.getLongValue(httpMap, "test_time")*1000);
        	 deviceInfoMap.put("test_time", dateUtil.getLongDate());
        }
        //图表数据
        List<Map<String,String>> charList=this.getChartData(paramType, param);
        JSONArray charJson = new JSONArray();
        charJson.addAll(charList);
        deviceInfoMap.put("chartData", charJson.toJSONString());
        //端口图表数据
        //pon口
        List<Map<String,String>> ponList=this.getPonData(indexTypePonPort, paramType, param);
        JSONArray ponJson = new JSONArray();
        ponJson.addAll(ponList);
        deviceInfoMap.put("ponData", ponJson.toJSONString());
        //lan口
        int lanNum;
        if("device_serialnumber".equals(paramType)){
        	lanNum= StringUtil.getIntValue(this.getLanNumByDevSn(param), "lan_num");
        }else{
        	lanNum= StringUtil.getIntValue(this.getLanNumByLoid(param), "lan_num");
        }
        List<Map<String,String>> lanList=this.getLanData(indexTypeLanPort, paramType, param);
        Map<String,List<Map<String,String>>> portMap=new HashMap<String,List<Map<String,String>>>();
        portMap.put("1", new ArrayList<Map<String,String>>());
        portMap.put("2", new ArrayList<Map<String,String>>());
        //终端有2个Lan口的或者是4个Lan口的
        if(4==lanNum){
        	portMap.put("3", new ArrayList<Map<String,String>>());
            portMap.put("4", new ArrayList<Map<String,String>>());
        }
        for(Map<String,String> lanMap:lanList){
        		portMap.get(StringUtil.getStringValue(lanMap,"lan_interface_config_id")).add(lanMap);
        }
        JSONArray lanJson = new JSONArray();
       
    	//lan1
    	lanJson.addAll(portMap.get("1"));
        deviceInfoMap.put("lan1Data", lanJson.toJSONString());
        //lan2
        lanJson.clear();;
    	lanJson.addAll(portMap.get("2"));
        deviceInfoMap.put("lan2Data", lanJson.toJSONString());
        
        if(4==lanNum){
    	//lan3
	    	lanJson.clear();
	    	lanJson.addAll(portMap.get("3"));
	        deviceInfoMap.put("lan3Data", lanJson.toJSONString());
	    //lan4
	    	lanJson.clear();
	    	lanJson.addAll(portMap.get("4"));
	        deviceInfoMap.put("lan4Data", lanJson.toJSONString());
        }
        return deviceInfoMap;
	}

			/**
			 *      先判断能不能找到多个设备
					* @return
			 */
		public String findOneDev(String sn)
		{
			List<HashMap<String,String>> list=dao.getDevQuantityBySn(sn);
			if(null==list||list.size()==0){
				return "0";
			}else if(1==list.size()){
				return StringUtil.getStringValue(list.get(0),"device_serialnumber");
			}else{
				return "2";
			}
				
		}
		/**
		 *       查有没有用户
				* @param trim
				* @return
		 */

		public boolean isUserExist(String loid)
		{
			if(null==dao.getUserByLoid(loid)){
				return false;
			}else{
				return true;
			}
				
		}

		/**
		 * 下载速率
		 * @param TransportStartTime 开始时间
		 * @param TransportEndTime 结束时间
		 * @param ReceiveByte 字节数
		 * @return
		 */
		private String getDownPert(String TransportStartTime, String TransportEndTime, String ReceiveByte){
			String ff="";
			String str ;
			float s1=0;
			String end ;
			float s2=0;
			if( !StringUtil.IsEmpty(TransportStartTime) && !StringUtil.IsEmpty(TransportEndTime)){
				
				if(TransportStartTime.contains(".")){
					str = (TransportStartTime.replace("T", " ")).substring(0,TransportStartTime.indexOf("."));
					s1=Float.parseFloat("0"+TransportStartTime.replace("T", " ").substring(TransportStartTime.indexOf("."),TransportStartTime.length()));
				}else{
					str = (TransportStartTime.replace("T", " ")).substring(0,19);
				}
				
				if(TransportEndTime.contains(".")){
					end = (TransportEndTime.replace("T", " ")).substring(0,TransportEndTime.indexOf("."));
					s2=Float.parseFloat("0"+TransportEndTime.replace("T", " ").substring(TransportEndTime.indexOf("."),TransportEndTime.length()));
				}else{
					end = (TransportEndTime.replace("T", " ")).substring(0,19);
				}
			DateTimeUtil start_time = new DateTimeUtil(str);
			DateTimeUtil end_time = new DateTimeUtil(end);
			
			long num = end_time.getLongTime()-start_time.getLongTime();
			BigDecimal b1 = new BigDecimal(s1);
			BigDecimal b2 = new BigDecimal(s2);
			float ss=b2.subtract(b1).setScale(6,BigDecimal.ROUND_HALF_UP).floatValue();
			
			float receiveByte = Float.parseFloat(ReceiveByte);
			if(num+ss>0){
				float result=(float) (receiveByte/1024/(num+ss));
				BigDecimal  b =new  BigDecimal(result);
				   //小数保留2位
				   ff=StringUtil.getStringValue(b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
			}
		}
			return StringUtil.getStringValue(ff);
		}

        /**
         *      获取图表数据
        		* @return
         */
		public List<Map<String,String>> getChartData(String paramType, String param)
		{
			DateTimeUtil dateTimeUtil = new DateTimeUtil();
			Long startTime=dateTimeUtil.getLastDayEnd(dateTimeUtil.getDate());
			Long endTime=startTime+24*3600-1;
		   return dao.getChartData( indexName, indexTypePon, startTime,  endTime,
					 paramType,  param);
		}
		/**
         *      获取pon口图表数据
        		* @return
         */
		public List<Map<String,String>> getPonData(String indexType,String paramType, String param)
		{
			DateTimeUtil dateTimeUtil = new DateTimeUtil();
			Long startTime=dateTimeUtil.getLastDayEnd(dateTimeUtil.getDate());
			Long endTime=startTime+24*3600-1;
		   return dao.getPonData( indexName, indexType, startTime,  endTime,
					 paramType,  param);
		}
		/**
         *      获取lan口图表数据
        		* @return
         */
		public List<Map<String,String>> getLanData(String indexType,String paramType, String param)
		{
			DateTimeUtil dateTimeUtil = new DateTimeUtil();
			Long startTime=dateTimeUtil.getLastDayEnd(dateTimeUtil.getDate());
			Long endTime=startTime+24*3600-1;
		   return dao.getLanData( indexName, indexType, startTime,  endTime,
					 paramType,  param);
		}
		/**
		 * 查询一个设备有几个lan口根据设备sn
		 */
		public Map<String,String> getLanNumByDevSn(String sn){
			return  dao.getLanNumByDevSn(sn);
		}
		/**
		 * 查询一个设备有几个lan口根据loid
		 */
		public Map<String,String> getLanNumByLoid(String loid){
			return dao.getLanNumByLoid(loid);
		}
		
	public QueryUserInfoDAO getDao()
	{
		return dao;
	}

	public void setDao(QueryUserInfoDAO dao)
	{
		this.dao = dao;
	}

	
}

	