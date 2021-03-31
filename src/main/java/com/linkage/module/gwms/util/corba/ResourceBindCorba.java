/**
 * 
 */
package com.linkage.module.gwms.util.corba;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ResourceBind.BindInfo;
import ResourceBind.BlManager;
import ResourceBind.ResultInfo;
import ResourceBind.UnBindInfo;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.init.bio.AppInitBIO;
import com.linkage.module.gwms.util.ResourceBindInterface;

/**
 * @author chenjie(67371)
 * @date 2011-7-7
 * 
 * 资源绑定模块corab工具类
 */
public class ResourceBindCorba implements ResourceBindInterface{
	
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ResourceBindCorba.class);
	
	private BlManager G_ResourceBind = null;
	
	private String gw_type;
	
	public ResourceBindCorba(){
	}
	
	public ResourceBindCorba(String gw_type){
		this.gw_type = gw_type;
		init();
	}

	private void init()
	{
		if (Global.GW_TYPE_ITMS.equals(gw_type))
		{
			G_ResourceBind = Global.G_ResourceBind_itms;
		}
		else if (Global.GW_TYPE_STB.equals(gw_type))
		{
			G_ResourceBind = Global.G_ResourceBind_stb;
		}
		else
		{
			G_ResourceBind = Global.G_ResourceBind_bbms;
		}
	}
	
	/**
	 * 绑定
	 * @param bindInfo
	 * @return
	 */
	public ResultInfo bind(BindInfo[] bindInfo)
	{
		logger.debug("bind({})", bindInfo);
		ResultInfo resultInfo = null;
		try{
			resultInfo = G_ResourceBind.bind("web", 0, bindInfo);
			logger.warn("bind: {}-{}",new Object[]{resultInfo.status,resultInfo.resultId});
		}
		catch(Exception e)
		{
			logger.error("ResourceBind Error.\n{}, rebind!", e);
			AppInitBIO.initResourceBind(this.gw_type);
			init();
			try
			{
				resultInfo = G_ResourceBind.bind("web", 0, bindInfo);
			}
			catch(Exception ex)
			{
				logger.error("rebind ResourceBind Error.\n{}", ex);
			}
		}
		return resultInfo;
	}
	
	/**
	 * 解绑
	 * @param bindInfo
	 * @return
	 */
	public ResultInfo release(UnBindInfo[] unBindInfo)
	{
		logger.debug("release({})", unBindInfo);
		ResultInfo resultInfo = null;
		try{
			resultInfo = G_ResourceBind.unBind("web", 0, unBindInfo);
			logger.warn("unbind: {}-{}",new Object[]{resultInfo.status,resultInfo.resultId});
		}
		catch(Exception e)
		{
			logger.error("ResourceBind Error.\n{}, rebind!", e);
			AppInitBIO.initResourceBind(this.gw_type);
			init();
			try
			{
				resultInfo = G_ResourceBind.unBind("web", 0, unBindInfo);
			}
			catch(Exception ex)
			{
				logger.error("rebind ResourceBind Error.\n{}", ex);
			}
		}
		return resultInfo;
	}
	
	/**
	 * 删除用户
	 * <br>
	 * 资源绑定模块无返回结果，所以只要不报错，则认为成功(1)
	 * 
	 * @param userName 用户帐号
	 * @return 删除结果
	 */
	public String delUser(String userName)
	{
		logger.debug("delUser({})", userName);
		try{
			G_ResourceBind.userDelete("web", 0, userName);
			return "1";
		}
		catch(Exception e)
		{
			logger.error("ResourceBind Error.\n{}, rebind!", e);
			AppInitBIO.initResourceBind(this.gw_type);
			init();
			try
			{
				G_ResourceBind.userDelete("web", 0, userName);
				return "1";
			}
			catch(Exception ex)
			{
				logger.error("rebind ResourceBind Error.\n{}", ex);
				return null;
			}
		}
	}
	
	/**
	 * 更新内存中的用户信息
	 * 
	 * @param userName 用户帐号
	 * @return 更新结果
	 */
	public String updateUser(String userName)
	{
		return doTest("user","set", userName);
	}
	
	/**
	 * 獲取内存中的用户信息
	 * 
	 * @param userName 用户帐号
	 * @return 更新结果
	 */
	public String getUser(String userName)
	{
		return doTest("user","get", userName);
	}
	
	/**
	 * 更新内存中的设备信息
	 * 
	 * @param userName 设备ID
	 * @return 更新结果
	 */
	public String updateDevice(String deviceid)
	{
		return doTest("device","set", deviceid);
	}
	
	/**
	 * 獲取内存中的设备信息
	 * 
	 * @param userName 设备ID
	 * @return 更新结果
	 */
	public String getDevice(String deviceid)
	{
		return doTest("device","get", deviceid);
	}
	
	/**
	 * 更新内存中的设备信息
	 * 
	 * @param device_id 设备ID
	 * @return 更新结果
	 */
	public String delDevice(String device_id)
	{
		return doTest("device","del", device_id);
	}
	
	/**
	 * 调用资源绑定模块
	 * 
	 * @param type user\device   user：用户数据，device：设备数据
	 * @param operate set\get\add\del  set：更新，get：获取，add：增加，del：删除
	 * @param parameter 待操作的具体参数  username或者device_id
	 * @return 操作结果
	 */
	public String doTest(String type,String operate,String parameter)
	{
		logger.debug("doTest(" + type + "," + operate + "," + parameter + ")");
		try{
			G_ResourceBind.test("web", 0,type,operate, parameter);
			return "1";
		}
		catch(Exception e)
		{
			logger.error("updateUser ResourceBind Error.\n{}, rebind!", e);
			AppInitBIO.initResourceBind(this.gw_type);
			init();
			try
			{
				G_ResourceBind.test("web", 0,type,operate, parameter);
				return "1";
			}
			catch(Exception ex)
			{
				logger.error("updateUser ResourceBind Error.\n{}", ex);
				return null;
			}
		}
	}
}
