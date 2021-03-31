package com.linkage.module.gwms.util.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.jms.MQConfigParser;
import com.linkage.commons.jms.MQPublisher;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.ResourceBindInterface;
import ResourceBind.BindInfo;
import ResourceBind.ResultInfo;
import ResourceBind.UnBindInfo;

/**
 * 向绑定模块发送消息公共类
 * @author jiafh (Ailk NO.)
 * @version 1.0
 * @since 2016-11-3
 * @category com.linkage.module.gwms.util.message
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class ResourceBindMQ implements ResourceBindInterface{
	
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ResourceBindMQ.class);
	
	/** 配置参数bean */
	private BindInfoMessage bindInfoMessage;
	
	private MQPublisher RESOURCE_BIND_PUBLISHER;

	/** 系统类型**/
	private String systemType;
	
	public ResourceBindMQ(){}
	
	public ResourceBindMQ(String gwType){
		
		this.systemType = gwType;
		if(Global.GW_TYPE_BBMS.equals(gwType)){
			RESOURCE_BIND_PUBLISHER = Global.RESOURCE_BIND_PUBLISHER_BBMS;
		}else if (Global.GW_TYPE_STB.equals(gwType)){
			RESOURCE_BIND_PUBLISHER = Global.RESOURCE_BIND_PUBLISHER_STB;
		} else{
			RESOURCE_BIND_PUBLISHER = Global.RESOURCE_BIND_PUBLISHER_ITMS;
		}
	}
	
	/**
	 * 绑定
	 * @param bindInfo
	 * @return
	 */
	public ResultInfo bind(BindInfo[] bindInfo){
		
		
		if(null == bindInfo || bindInfo.length == 0){
			logger.error("BindInfo[] bindInfo is null");
			return null;
		}
		
		bindInfoMessage = new BindInfoMessage();
		bindInfoMessage.setMethodName("bind");
		bindInfoMessage.setClientId("web");
		bindInfoMessage.setPriority("0");
		BindBean[] bindBeanArr = new BindBean[5];
		BindBean bindBean = new BindBean();
		for(int index=0;index < bindInfo.length;index++){
			bindBean.setAccName(bindInfo[index].accName);
			bindBean.setAccOid(bindInfo[index].accOid);
			bindBean.setDeviceId(bindInfo[index].deviceId);
			bindBean.setUserLine(bindInfo[index].userline);
			bindBean.setUserName(bindInfo[index].username);
			bindBeanArr[index] = bindBean;
		}
		bindInfoMessage.setBindBeanArr(bindBeanArr);
		
		ResultInfo resultInfo = new ResultInfo();
		try{
			RESOURCE_BIND_PUBLISHER.publishMQ(bindInfoMessage);
		}
		catch(Exception e)
		{
			try
			{
				initMQPool();
				RESOURCE_BIND_PUBLISHER.publishMQ(bindInfoMessage);
			}
			catch(Exception ex)
			{
				logger.error("rebind ResourceBind Error.\n{}", ex);
				return null;			
			}
		}
		resultInfo.resultId = new String[1];
		resultInfo.resultId[0] = "1";
		resultInfo.status = "1";
		logger.warn("bind: {}-{}",new Object[]{resultInfo.status,resultInfo.resultId});
		return resultInfo;
	}
	
	/**
	 * 解绑
	 * @param bindInfo
	 * @return
	 */
	public ResultInfo release(UnBindInfo[] unBindInfo){
		
		if(null == unBindInfo || unBindInfo.length == 0){
			logger.error("UnBindInfo[] unBindInfo is null");
			return null;
		}
		
		bindInfoMessage = new BindInfoMessage();
		bindInfoMessage.setMethodName("unBind");
		bindInfoMessage.setClientId("web");
		bindInfoMessage.setPriority("0");
		BindBean[] bindBeanArr = new BindBean[5];
		BindBean bindBean = new BindBean();
		for(int index=0;index < unBindInfo.length;index++){
			bindBean.setAccName(unBindInfo[index].accName);
			bindBean.setAccOid(unBindInfo[index].accOid);
			bindBean.setDeviceId(unBindInfo[index].deviceId);
			bindBean.setUserLine(unBindInfo[index].userline);
			bindBean.setUserId(unBindInfo[index].userId);
			bindBeanArr[index] = bindBean;
		}
		bindInfoMessage.setBindBeanArr(bindBeanArr);
		
		ResultInfo resultInfo = new ResultInfo();
		try{
			RESOURCE_BIND_PUBLISHER.publishMQ(bindInfoMessage);
		}
		catch(Exception e)
		{
			try
			{
				initMQPool();
				RESOURCE_BIND_PUBLISHER.publishMQ(bindInfoMessage);
			}
			catch(Exception ex)
			{
				logger.error("reUnbind ResourceBind Error.\n{}", ex);
				return null;			
			}
		}
		resultInfo.resultId = new String[1];
		resultInfo.resultId[0] = "1";
		resultInfo.status = "1";
		logger.warn("unbind: {}-{}",new Object[]{resultInfo.status,resultInfo.resultId});
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
	public String delUser(String userName){
			
		logger.debug("delUser({})", userName);
		bindInfoMessage = new BindInfoMessage();
		bindInfoMessage.setMethodName("userDelete");
		bindInfoMessage.setClientId("web");
		bindInfoMessage.setPriority("0");
		bindInfoMessage.setUserName(userName);
		
		try{
			RESOURCE_BIND_PUBLISHER.publishMQ(bindInfoMessage);
		}
		catch(Exception e)
		{
			try
			{
				initMQPool();
				RESOURCE_BIND_PUBLISHER.publishMQ(bindInfoMessage);
			}
			catch(Exception ex)
			{
				logger.error("delUser ResourceBind Error.\n{}", ex);
				return null;
			}
		}
		return "1";
	}
	
	/**
	 * 更新内存中的用户信息
	 * 
	 * @param userName 用户帐号
	 * @return 更新结果
	 */
	public String updateUser(String userName){
		return doTest("user","set", userName);
	}
	
	/**
	 * 更新内存中的设备信息
	 * 
	 * @param userName 设备ID
	 * @return 更新结果
	 */
	public String updateDevice(String deviceid){
		return doTest("device","set", deviceid);
	}
	
	/**
	 * 更新内存中的设备信息
	 * 
	 * @param device_id 设备ID
	 * @return 更新结果
	 */
	public String delDevice(String device_id){
		return doTest("device","del", device_id);
	}

	/**
	 * 獲取内存中的用户信息
	 * 
	 * @param userName 用户帐号
	 * @return 更新结果
	 */
	public String getUser(String username) {
		
		 return doTest("user","get", username);
	}

	/**
	 * 獲取内存中的设备信息
	 * 
	 * @param userName 设备ID
	 * @return 更新结果
	 */
	public String getDevice(String deviceid) {
		
		return doTest("device","get", deviceid);
	}
	
	/**
	 * 调用资源绑定模块
	 * 
	 * @param type user\device   user：用户数据，device：设备数据
	 * @param operate set\get\add\del  set：更新，get：获取，add：增加，del：删除
	 * @param parameter 待操作的具体参数  username或者device_id
	 * @return 操作结果
	 */
	public String doTest(String type,String operate,String parameter){
		logger.debug("doTest(" + type + "," + operate + "," + parameter + ")");
		bindInfoMessage = new BindInfoMessage();
		bindInfoMessage.setMethodName("test");
		bindInfoMessage.setClientId("web");
		bindInfoMessage.setPriority("0");
		bindInfoMessage.setType(type);
		bindInfoMessage.setOperate(operate);
		bindInfoMessage.setParameter(parameter);
		
		try{
			RESOURCE_BIND_PUBLISHER.publishMQ(bindInfoMessage);
		}
		catch(Exception e)
		{
			try
			{
				initMQPool();
				RESOURCE_BIND_PUBLISHER.publishMQ(bindInfoMessage);
			}
			catch(Exception ex)
			{
				logger.error("doTest ResourceBind Error.\n{}", ex);
				return null;
			}
		}
		
		return "1";
	}
	
	private void initMQPool(){
		
		if(Global.GW_TYPE_BBMS.equals(this.systemType)){
			Global.MQ_POOL_PUBLISHER_MAP_BBMS = MQConfigParser.getMQConfig(Global.G_MQPoolPath, "bbms");
			RESOURCE_BIND_PUBLISHER = new MQPublisher("res.Interface",Global.MQ_POOL_PUBLISHER_MAP_BBMS);
		}else if (Global.GW_TYPE_STB.equals(this.systemType)) {
		      Global.MQ_POOL_PUBLISHER_MAP_ITMS = MQConfigParser.getMQConfig(Global.G_MQPoolPath, "stb");
		      this.RESOURCE_BIND_PUBLISHER = new MQPublisher("res.InterfaceStb", Global.MQ_POOL_PUBLISHER_MAP_STB);
		}else{
			Global.MQ_POOL_PUBLISHER_MAP_ITMS = MQConfigParser.getMQConfig(Global.G_MQPoolPath, "itms");
			RESOURCE_BIND_PUBLISHER = new MQPublisher("res.Interface",Global.MQ_POOL_PUBLISHER_MAP_ITMS);
		}
	}
}
