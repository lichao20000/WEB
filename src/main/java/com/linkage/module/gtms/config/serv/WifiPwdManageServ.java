package com.linkage.module.gtms.config.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.config.dao.WifiPwdManageDAO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.ACSCorba;

public class WifiPwdManageServ 
{
	private static Logger logger = LoggerFactory.getLogger(WifiPwdManageServ.class);
	private WifiPwdManageDAO dao;
	
	
	/**
	 * 各个返回值 1该业务用户存在 0该用户不存在 -1该业务用户不存在,但是用户存在 -2用户未绑定设备
	 */
	@SuppressWarnings("rawtypes")
	public String getServUserInfo(String username) 
	{
		String flag = "";
		String userId = "";
		String deviceId = "";
		String usernameChoose = "";
		Map map = null;
		List list = dao.isExists(username);
		if (null != list) 
		{
			flag = "0";
			if (list.size() == 1) 
			{
				map = (Map) list.get(0);
				userId = StringUtil.getStringValue(map.get("user_id"));
				deviceId = StringUtil.getStringValue(map.get("device_id"));
				usernameChoose = StringUtil.getStringValue(map.get("device_serialnumber"));
				
				if (StringUtil.IsEmpty(userId)) {
					flag="-1";
				}else if (StringUtil.IsEmpty(deviceId)) {
					flag="-2";
				}else{
					flag="1,<span style='display:inline-block;width:132px'>";
					if(Global.NXDX.equals(Global.instAreaShortName)){
						flag+="<input type='radio' class=jianbian name='device_serialnumber' value='"
								+deviceId+"' />"+usernameChoose+"</span>";
					}else{
						flag+="<input type='radio' class=jianbian name='device_serialnumber' value='"
								+usernameChoose+"' />"+usernameChoose+"</span>";
					}
				}
			}
			else if (list.size() > 1) 
			{
				StringBuffer flagtParam = new StringBuffer();
				flagtParam.append("2,");
				for(int i =0;i<list.size();i++)
				{
					map = (Map) list.get(i);
					if(Global.NXDX.equals(Global.instAreaShortName)){
						usernameChoose = StringUtil.getStringValue(map.get("device_serialnumber"));
						flagtParam.append("<span style='display:inline-block;width:132px'>")
									.append("<input type='radio' class=jianbian name='device_serialnumber' value='"
												+StringUtil.getStringValue(map.get("device_id"))+"' />"+usernameChoose+"</span>");
					}else{
						usernameChoose = StringUtil.getStringValue(map.get("username"));
						flagtParam.append("<span style='display:inline-block;width:132px'>")
									.append("<input type='radio' class=jianbian name='device_serialnumber' value='"
												+usernameChoose+"' />"+usernameChoose+"</span>");
					}
				}
				flag = flagtParam.toString();
			}
		}
		return flag;
	}

	@SuppressWarnings({ "rawtypes", "unchecked"})
	public String getAllInfo1(String device_serialnumber,List<Map<String,String>> res) 
	{
		String deviceId = dao.getDeviceId(device_serialnumber);
		if(StringUtil.IsEmpty(deviceId)){
			return "-9|||设备不存在";
		}
		
		ACSCorba acsCorba = new ACSCorba(Global.GW_TYPE_ITMS);
		int flag = acsCorba.testConnection(deviceId, Global.GW_TYPE_ITMS);
		logger.warn("设备[{}]在线状态结果statusResult : {}" ,deviceId,flag);
		if ((1 != flag)){
			return "-1|||设备不在线或者设备正在被操作，请稍后再试";
		}
		
		logger.warn("查询家庭网关,device_id={},设备在线，可以进行采集操作",new Object[] {deviceId });
		/*WIFI名称和密码相关节点：
			SSID：InternetGatewayDevice.LANDevice.1.WLANConfiguration.j.SSID
			4G密码：InternetGatewayDevice.LANDevice.1.WLANConfiguration.j.PreSharedKey.k.PreSharedKey
			5G密码：InternetGatewayDevice.LANDevice.1.WLANConfiguration.j.PreSharedKey.k.PreSharedKey
		*/
		String lanPath = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.";
	    List<String> iList = acsCorba.getIList(deviceId, lanPath);
		if(null == iList || iList.isEmpty()){
			logger.warn("[{}]获取iList失败，返回", deviceId);
			return "-2|||获取设备节点失败，请稍后再试";
		}else{
			logger.warn("[{}]获取iList成功，iList.size={}", deviceId,iList.size());
		}
		
		for(String i : iList)
		{
			HashMap map = new HashMap();
			String[] gatherPath = null;
			gatherPath = new String[]{"InternetGatewayDevice.LANDevice.1.WLANConfiguration."+i+".SSID"};
			ArrayList<ParameValueOBJ> SSIDobjLlist = acsCorba.getValue(deviceId, gatherPath);
			for(ParameValueOBJ pvobj : SSIDobjLlist)
			{
				if(pvobj.getName().contains("SSID")){
					map.put("ssidname", pvobj.getValue());
				}
				String kpath = "InternetGatewayDevice.LANDevice.1.WLANConfiguration."+i+".PreSharedKey.";
				List<String> kList = acsCorba.getIList(deviceId, kpath);
				String pwdPath = ".PreSharedKey";
				if (null == iList || iList.isEmpty()){
					logger.warn("[{}]获取iList失败，返回", deviceId);
				}else{
					String[] gatherkPath = null;
					gatherkPath = new String[kList.size()];
					for(String k : kList){
						for(int t = 0; t<kList.size() ;t++){
							gatherkPath[t] = kpath+ k + pwdPath;
						}
					}
					ArrayList<ParameValueOBJ> PreSharedKeyobjKlist = acsCorba.getValue(deviceId, gatherkPath);
					if(null != PreSharedKeyobjKlist && PreSharedKeyobjKlist.size()>0){
						for(ParameValueOBJ keypvobj : PreSharedKeyobjKlist)
						{
							if(PreSharedKeyobjKlist.size()>2)
							{
								if(!StringUtil.IsEmpty(StringUtil.getStringValue(map, "PreSharedKeyPath"))){
									map.put("PreSharedKeyPath",StringUtil.getStringValue(map, "PreSharedKeyPath")+"|||"+keypvobj.getName());
								}else{
									map.put("PreSharedKeyPath",keypvobj.getName());
								}
								
								if(!StringUtil.IsEmpty(StringUtil.getStringValue(map, "PreSharedKeyvalue"))){
									map.put("PreSharedKeyvalue",StringUtil.getStringValue(map, "PreSharedKeyvalue")+"|||"+keypvobj.getValue());
								}else{
									map.put("PreSharedKeyvalue",keypvobj.getValue());
								}
							}
							else
							{
								map.put("PreSharedKeyPath",keypvobj.getName());
								map.put("PreSharedKeyvalue",keypvobj.getValue());
							}
						}
					}
				}
			}
			map.put("deviceId",deviceId);
			res.add(map);
		}
		for (Map mapres : res) {
			logger.warn("map结果:{}",mapres);
		}
		return "0|||成功";
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public String getAllInfo(String device_serialnumber,List<Map<String,String>> res) 
	{
		logger.warn("getAllInfo({})",device_serialnumber);
		String deviceId;
		if(Global.NXDX.equals(Global.instAreaShortName)){
			deviceId=device_serialnumber;
		}else{
			deviceId = dao.getDeviceId(device_serialnumber);
		}
		
		if(StringUtil.IsEmpty(deviceId)){
			return "-9|||设备不存在";
		}
		
		//拿到deivceID获取下挂节点值
		ACSCorba acsCorba = new ACSCorba(Global.GW_TYPE_ITMS);
		int flag = acsCorba.testConnection(deviceId, Global.GW_TYPE_ITMS);
		logger.warn("设备[{}]在线状态结果statusResult : {}" ,deviceId,flag);
		if((1 != flag)){
			return "-1|||设备不在线或者设备正在被操作，请稍后再试";
		}
		
		logger.warn("查询家庭网关,device_id={},设备在线，可以进行采集操作",new Object[] {deviceId });
		/*WIFI名称和密码相关节点：
			SSID：InternetGatewayDevice.LANDevice.1.WLANConfiguration.j.SSID
			4G密码：InternetGatewayDevice.LANDevice.1.WLANConfiguration.j.PreSharedKey.k.PreSharedKey
			5G密码：InternetGatewayDevice.LANDevice.1.WLANConfiguration.j.PreSharedKey.k.PreSharedKey
		*/
		String lanPath = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.";
	    List<String> iList = acsCorba.getIList(deviceId, lanPath);
		if (null == iList || iList.isEmpty()){
			logger.warn("[{}]获取iList失败，返回", deviceId);
			return "-2|||获取设备节点失败，请稍后再试";
		}else{
			logger.warn("[{}]获取iList成功，iList.size={}", deviceId,iList.size());
		}
		
		StringBuffer buf = new StringBuffer();
		boolean flg = false;
		for(String i : iList)
		{
			HashMap map = new HashMap();
			/*String[] gatherPath = null;
			gatherPath = new String[]{"InternetGatewayDevice.LANDevice.1.WLANConfiguration."+i+".SSID"};
			ArrayList<ParameValueOBJ> SSIDobjLlist = acsCorba.getValue(deviceId, gatherPath);
			for(ParameValueOBJ pvobj : SSIDobjLlist){
				if(pvobj.getName().contains("SSID")){
					map.put("ssidname", pvobj.getValue());
				}*/
			if(flg){
				buf.append("|||");
			}
			flg = true;
			buf.append("InternetGatewayDevice.LANDevice.1.WLANConfiguration."+i+".SSID");
			String kpath = "InternetGatewayDevice.LANDevice.1.WLANConfiguration."+i+".PreSharedKey.";
			List<String> kList = acsCorba.getIList(deviceId, kpath);
			String pwdPath = ".PreSharedKey";
			if (null == iList || iList.isEmpty()){
				logger.warn("[{}]获取iList失败，返回", deviceId);
			}else{
				/*String[] gatherkPath = null;
				gatherkPath = new String[kList.size()];*/
				for(String k : kList){
					for(int t = 0; t<kList.size() ;t++){
						//gatherkPath[t] = kpath+ k + pwdPath;
						buf.append("|||"+kpath+ k + pwdPath);
					}
				}
			}
		}
		
		logger.warn("所有节点:{}",buf.toString());
		String[] split = buf.toString().split("\\|\\|\\|");
		ArrayList<ParameValueOBJ> PreSharedKeyobjKlist = acsCorba.getValue(deviceId, split);
		if(null != PreSharedKeyobjKlist && PreSharedKeyobjKlist.size()>0){
			//先判断SSID
			Map<String,Map<String,String>> allmap = new HashMap<String,Map<String,String>>();
			for(ParameValueOBJ keypvobj : PreSharedKeyobjKlist)
			{
				// 分离出节点名
				String beforpath = "";
				String value = keypvobj.getName();
				logger.warn("keypvobj.getName()：{}",keypvobj.getName());
				if(keypvobj.getName().contains("SSID")){
					beforpath = value.substring(0, value.lastIndexOf("."));
				}else{
					String bu = value.substring(0, value.lastIndexOf("."));
					String  bu1  =  bu.substring(0, bu.lastIndexOf("."));
					beforpath = bu1.substring(0,bu1.lastIndexOf("."));
				}
				if(!StringUtil.IsEmpty(StringUtil.getStringValue(allmap, beforpath))){
					Map<String, String> ijkmap = allmap.get(beforpath);
					if(keypvobj.getName().contains("SSID")){
						ijkmap.put("ssidname", keypvobj.getValue());
					}
					if(keypvobj.getName().contains("PreSharedKey")){
						if(!StringUtil.IsEmpty(StringUtil.getStringValue(ijkmap, "PreSharedKeyPath"))){
							ijkmap.put("PreSharedKeyPath",StringUtil.getStringValue(ijkmap, "PreSharedKeyPath")+"|||"+keypvobj.getName());
						}else{
							ijkmap.put("PreSharedKeyPath",keypvobj.getName());
						}
						if(!StringUtil.IsEmpty(StringUtil.getStringValue(ijkmap, "PreSharedKeyvalue"))){
							ijkmap.put("PreSharedKeyvalue",StringUtil.getStringValue(ijkmap, "PreSharedKeyvalue")+"|||"+keypvobj.getValue());
						}else{
							ijkmap.put("PreSharedKeyvalue",keypvobj.getValue());
						}
					}
				}else{
					Map val = new HashMap();
					if(keypvobj.getName().contains("SSID")){
						val.put("ssidname", keypvobj.getValue());
					}
					if(keypvobj.getName().contains("PreSharedKey")){
						val.put("PreSharedKeyPath",keypvobj.getName());
						val.put("PreSharedKeyvalue",keypvobj.getValue());
					}
					val.put("deviceId", deviceId);
					allmap.put(beforpath,val);
				}
			}
			
			Set<String> keySet = allmap.keySet();
			for (String string : keySet) {
				res.add(allmap.get(string));
			}
			logger.warn("res结果:{}",res);
			return "0|||成功";
		}else{
			return "0|||获取失败请重新查询获取";
		}
	}

	public String updateWifiPwd(String wifipath, String wifipwd,String deviceId) 
	{
		ACSCorba acsCorba = new ACSCorba(Global.GW_TYPE_ITMS);
		ArrayList<ParameValueOBJ> paramList = new ArrayList<ParameValueOBJ>();
		String result = "";
		if(!StringUtil.IsEmpty(wifipath))
		{
			logger.warn("设备:{},修改wifi密码修改节点:{},修改值:{}",deviceId,wifipath,wifipwd);
			String[] split = wifipath.split("\\|\\|\\|");
			for (String string : split) {
				ParameValueOBJ parameValueOBJ = new ParameValueOBJ();
				parameValueOBJ
				.setName(string);
				parameValueOBJ.setValue(wifipwd);
				parameValueOBJ.setType("1");
				paramList.add(parameValueOBJ);
			}
		}
		
		int saveResult = acsCorba.setValue(deviceId, paramList);
		logger.warn("设备:{},修改wifi密码结果:{}",deviceId,saveResult);
		switch (saveResult) 
		{
			case 0 | 1:
				result = "成功";
				break;
			case -1:
				result = "设备连接失败";
				break;
			case -6:
				result = "设备正被操作";
				break;
			case -7:
				result = "系统参数错误";
				break;
			case -9:
				result = "系统内部错误";
				break;
			default:
				result = "其它:TR069错误";
				break;
		}
		return result;
	}

	
	public WifiPwdManageDAO getDao() {
		return dao;
	}

	public void setDao(WifiPwdManageDAO dao) {
		this.dao = dao;
	}
	
}
