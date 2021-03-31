package com.linkage.module.gtms.config.serv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.config.dao.OttConfigDAO;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;

/**
 * 
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since 2014-4-1
 * @category com.linkage.module.gtms.config.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class OttConfigServImpl implements OttConfigServ
{
	private static Logger logger = LoggerFactory.getLogger(OttConfigServImpl.class);
	private  OttConfigDAO dao;

	/**
	    * 各个返回值 
	    * 1没有宽带业务
	    * 0该用户不存在                
	    * -1已开通itv业务
	    * 
	    */
	@Override
	public String getServUserInfo(String searchType, String username, String gwType)
	{
		logger.debug("getServUserInfo({},{})",username,gwType);
		 String flag ="1";
		  List<HashMap<String,String>> list =   dao.isExists(searchType,username,gwType);
		  if(null!=list && list.size()>0)
		  {
			  
			 for(HashMap<String,String> data : list)
			 {
				 //开通了itv，不做
				 if("11".equals(data.get("serv_type_id")))
				 {
					 flag = "-1";
					 break;
				 }
				 if("2".equals(data.get("spec_id")))
				 {
					 flag = "-2";
					 break;
				 }
				 if("10".equals(data.get("serv_type_id")))
				 {
					 flag = list.get(0).get("user_id");
				 }
			 }
		  }
		  else
		  {
			  flag = "0";
		  }
		  logger.warn("用户返回值flag========="+flag);
		  return flag;
	}

	/**
	 * -1:失败
	 * 0 :成功
	 */
	@Override
	public String openOtt(String username, String userId, String gw_type)
	{
		String deviceId = null;
		String deviceType = null;
		String bindPort = null;
		String result = "0";
		Map<String,String> userInfo = dao.getUserInfo(userId);
		boolean haveOtt = dao.isOpenOtt(userId);
		if(!haveOtt && null != userInfo)//没有开通OTT，且有宽带业务，才可以开通
		{
			
			deviceType = userInfo.get("spec_id");//3,13,14
			bindPort = StringUtil.getStringValue(userInfo,"bind_port","");
			deviceId = userInfo.get("device_id");
			String port = "";
			
			//修改宽带绑定端口
			if("3".equals(deviceType) || "13".equals(deviceType) || "14".equals(deviceType))
			{
				port = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2";
			}
			else
			{
				port = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2,InternetGatewayDevice.LANDevice.1.WLANConfiguration.2";
			}
			if(bindPort.length()>0)
			{
				bindPort += ","+port;
			}
			else
			{
				bindPort = port;
			}
			dao.updateNetBindPort(userId, bindPort);
			//新增一条数据
			dao.openOttServ(StringUtil.getLongValue(userId), userInfo, port);
			
			//调用配置模块下发业务
			 // 预读调用对象
			 PreServInfoOBJ preInfoObj = new PreServInfoOBJ(userId,deviceId, userInfo.get("oui"), userInfo.get("device_serialnumber"), "10", "1");
            if (1 != CreateObjectFactory.createPreProcess(gw_type) .processServiceInterface(CreateObjectFactory.createPreProcess().GetPPBindUserList(preInfoObj))) 
            {
            	logger.warn("通知后台失败");
            	result = "-1";
            }
		}
		
		return result;
	}

	@Override
	public String closeOtt(String username, String userId, String gw_type)
	{
		String deviceId = null;
		String deviceType = null;
		String bindPort = "";
		String result = "0";
		Map<String,String> userInfo = dao.getUserInfo(userId);
//		boolean haveOtt = dao.isOpenOtt(userId);
//		if(haveOtt)//没有开通OTT，且有宽带业务，才可以开通
//		{
			
			deviceType = userInfo.get("spec_id");//3,13,14
//			bindPort = StringUtil.getStringValue(userInfo,"bind_port","");
			deviceId = userInfo.get("device_id");
			//修改宽带绑定端口
			if("3".equals(deviceType) || "13".equals(deviceType) || "14".equals(deviceType))
			{
				bindPort = StringUtil.getStringValue(userInfo,"bind_port","").replace(",InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2", "");
				
			}
			else
			{
				bindPort = StringUtil.getStringValue(userInfo,"bind_port","").replace(",InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2,InternetGatewayDevice.LANDevice.1.WLANConfiguration.2", "");
			}
			dao.updateNetBindPort(userId, bindPort);
			//删除
			logger.warn("delete Ottserv:" + dao.deleteOttServ(userId));
			
			//调用配置模块下发业务
			 // 预读调用对象
			 PreServInfoOBJ preInfoObj = new PreServInfoOBJ(userId,deviceId, userInfo.get("oui"), userInfo.get("device_serialnumber"), "10", "1");
            if (1 != CreateObjectFactory.createPreProcess(gw_type) .processServiceInterface(CreateObjectFactory.createPreProcess().GetPPBindUserList(preInfoObj))) 
            {
            	logger.warn("通知后台失败");
            	result = "-1";
            }
//		}
		
		return result;
	}

	
	public OttConfigDAO getDao()
	{
		return dao;
	}

	
	public void setDao(OttConfigDAO dao)
	{
		this.dao = dao;
	}

	
}
