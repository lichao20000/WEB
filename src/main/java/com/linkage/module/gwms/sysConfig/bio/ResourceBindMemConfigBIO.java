package com.linkage.module.gwms.sysConfig.bio;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.sysConfig.dao.ResourceBindMemConfigDAO;
import com.linkage.module.gwms.sysConfig.obj.User4HInfoOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ResourceBindMemConfigBIO {

	//日志记录
	private static Logger logger = LoggerFactory.getLogger(ResourceBindMemConfigBIO.class);
	
	ResourceBindMemConfigDAO dao = null;
	//private ResourceBindInterface G_ResourceBind = null;
	
	public List<Map> getUserInfo4H(String usernameType, String username) 
	{
		logger.warn("getUserInfo({},{})",  usernameType, username);
		long userId = 0;
		if (false == StringUtil.IsEmpty(username))
		{
			if ("1".equals(usernameType))
			{
				userId = dao.getUserBySN4H(username);
			}
			else if ("2".equals(usernameType))
			{
				userId = dao.getUserByServ4H(username, "10");
			}
			else if ("3".equals(usernameType))
			{
				userId = dao.getUserByServ4H(username, "11");
			}
			else if ("4".equals(usernameType))
			{
				userId = dao.getUserByVoip4H(username, null);
			}
			else if ("5".equals(usernameType))
			{
				userId = dao.getUserByVoip4H(null, username);
			}
			else
			{
				logger.warn("用户名类型不存在！");
				return null;
			}
			if (-1==userId)
			{
				logger.warn("用户不存在！");
				return null;
			}
		}
		List<Map> list = dao.getUserInfo4H(userId);
		return list;
	}
	
	public User4HInfoOBJ getUser4HDbDetail(String username){
		
		User4HInfoOBJ obj = new User4HInfoOBJ();
		
		List<Map> _list = dao.getUserDetail4H(username);
		
		if(null!=_list && _list.size()>0){
			Map _map = _list.get(0);
			obj.setUserId(StringUtil.getLongValue(_map,"user_id"));
			obj.setCityId(StringUtil.getStringValue(_map,"city_id"));
			obj.setUsername(StringUtil.getStringValue(_map,"username"));
			obj.setUserline(StringUtil.getIntValue(_map,"userline"));
			obj.setIsChkBind(StringUtil.getIntValue(_map,"is_chk_bind"));
			obj.setDeviceId(StringUtil.getStringValue(_map,"device_id"));
			obj.setOui(StringUtil.getStringValue(_map,"oui"));
			obj.setDeviceSerialnumber(StringUtil.getStringValue(_map,"device_serialnumber"));
			String typeId = StringUtil.getStringValue(_map,"type_id");
			if("1".equals(typeId)){
				obj.setType("e8-b");
			}else if("2".equals(typeId)){
				obj.setType("e8-c");
			}else{
				obj.setType("e8-c");
			}
		}
		return obj;
	}

	public String getUserBind4HDbDetail(String deviceId){
		
		List<Map> _list = dao.getUserBindDetail4H(deviceId);
		
		if(null!=_list && _list.size()>0){
			Map _map = _list.get(0);
			return StringUtil.getStringValue(_map,"username");
		}else{
			return null;
		}
	}
	
	public User4HInfoOBJ getUser4HMemDetail(String username, String gw_type){
		
		User4HInfoOBJ obj = null;
		
		//String _corbaUser = test("user", "get",username, gw_type);
		String _corbaUser = CreateObjectFactory.createResourceBind(gw_type).getUser(username);
		if(null==_corbaUser || "false".equals(_corbaUser) || "null".equals(_corbaUser)){
			return obj;
		}else{
			_corbaUser = _corbaUser.replace("com.linkage.bl.obj", "com.linkage.module.gwms.sysConfig.obj");
			XStream xstream = new XStream(new DomDriver());
			obj = (User4HInfoOBJ)xstream.fromXML(_corbaUser);
			return obj;
		}
	}
	
	public String getDevice4HMemDetail(String deviceId, String gw_type){
		
//		String _corbaUser = test("device", "get",deviceId, gw_type);
		String _corbaUser = CreateObjectFactory.createResourceBind(gw_type).getDevice(deviceId);
		if(null==_corbaUser || "false".equals(_corbaUser) || "null".equals(_corbaUser)){
			return null;
		}else{
			return _corbaUser;
		}
	}
	
	public static void main(String args[]){ 
//		User4HInfoOBJ obj = new User4HInfoOBJ();
//		obj.setUserId(111);
//		obj.setCityId("0100");
//		obj.setDeviceId("111");
//		obj.setDeviceSerialnumber("234324");
//		obj.setIsChkBind(1);
//		obj.setOui("12345");
//		obj.setType("e8-c");
//		obj.setUserline(5);
//		obj.setUsername("02512345678");
//		XStream xstream = new XStream(new DomDriver());
//		String _corbaUser = xstream.toXML(obj);
		String _corbaUser = "<com.linkage.bl.obj.User4HInfoOBJ>" + 
		  "<userId>141</userId>" + 
		  "<cityId>0006</cityId>" + 
		  "<username>14A9EA75DC62ECE0</username>" + 
		  "<userline>-1</userline>" + 
		  "<isChkBind>0</isChkBind>" + 
		  "<type>e8-c</type>" + 
		  "</com.linkage.bl.obj.User4HInfoOBJ>";
		String aa = _corbaUser.replace("com.linkage.bl.obj", "com.linkage.module.gwms.sysConfig.abj");
		
		System.out.println(aa);
		XStream xstream2 = new XStream(new DomDriver());
		Object _obj = xstream2.fromXML(aa);
		xstream2.aliasPackage("com.linkage.module.gwms.sysConfig", "com.linkage.module.gwms.sysConfig.abj");
		String ddd = xstream2.toXML(_obj);
		System.out.println(ddd);
//		XStream xstream3 = new XStream(new DomDriver());
//		com.linkage.module.gwms.sysConfig.User4HInfoOBJ obj2 = (com.linkage.module.gwms.sysConfig.User4HInfoOBJ)xstream3.fromXML(ddd);
//		System.out.println(obj2.getUsername());
	}
	
//	public String test(String type,String operate,String parameter, String gw_type){
//		
//		if (Global.GW_TYPE_ITMS.equals(gw_type)) {
//			this.G_ResourceBind = Global.G_ResourceBind_itms;
//		} else if (Global.GW_TYPE_BBMS.equals(gw_type)) {
//			this.G_ResourceBind = Global.G_ResourceBind_bbms;
//		} else {
//			this.G_ResourceBind = Global.G_ResourceBind_itms;
//		}
//		
//		if(null==G_ResourceBind){
//			String ior = this.getCorbaIor();
//			logger.warn("ior="+ior);
//			if(null==ior){
//				return "false";
//			}
//			org.omg.CORBA.Object objRef = null;
//			try
//			{
//				String[] args = null;
//				ORB PP_ORB = ORB.init(args, null);
//				objRef = PP_ORB.string_to_object(ior);
//				G_ResourceBind = BlManagerHelper.narrow(objRef);
//				return G_ResourceBind.test("web", 0, type,operate,parameter);
//			}
//			catch (Exception ex)
//			{
//				logger.error("rebind ResourceBind Error.\n{}", ex);
//			}
//		}else{
//			try
//			{
//				return G_ResourceBind.test("web", 0, type,operate,parameter);
//			}catch (Exception ex)
//			{
//				logger.error("rebind ResourceBind Error.\n{}", ex);
//				String ior = this.getCorbaIor();
//				logger.warn("ior="+ior);
//				if(null==ior){
//					return "false";
//				}
//				org.omg.CORBA.Object objRef = null;
//				try
//				{
//					String[] args = null;
//					ORB PP_ORB = ORB.init(args, null);
//					objRef = PP_ORB.string_to_object(ior);
//					G_ResourceBind = BlManagerHelper.narrow(objRef);
//					return G_ResourceBind.test("web", 0, type,operate,parameter);
//				}
//				catch (Exception ee)
//				{
//					logger.error("rebind ResourceBind Error.\n{}", ee);
//				}
//			}
//		}
//		return "false";
//	}
	
	
	public String getCorbaIor(){
		
		List _list = dao.getCorbaIor();
		
		if(null!=_list && _list.size()>0){
			Map _map = (Map)_list.get(0);
			return String.valueOf(_map.get("ior"));
		}else{
			return null;
		}
	}

	public ResourceBindMemConfigDAO getDao() {
		return dao;
	}

	public void setDao(ResourceBindMemConfigDAO dao) {
		this.dao = dao;
	}
	
}
