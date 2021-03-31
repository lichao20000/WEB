package com.linkage.module.gwms.util.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import PreProcess.OneToMany;
import PreProcess.UserInfo;

import com.linkage.commons.jms.MQConfigParser;
import com.linkage.commons.jms.MQPublisher;
import com.linkage.commons.jms.TopicMsgPublisher;
import com.linkage.commons.jms.obj.KafkaConfig;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.PreProcessInterface;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;

/**
 * 发送给配置模块消息公用类
 * 
 * @author jiafh (Ailk NO.)
 * @version 1.0
 * @since 2016-11-3
 * @category com.linkage.module.gwms.util.message
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 * 
 */
public class PreProcessMQ implements PreProcessInterface {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(PreProcessMQ.class);

	/** 配置参数bean */
	private PreProcessMessage preProcessMessage;
	
	/**配置模块发送消息服务端*/
	private MQPublisher PROCESS_PUBLISHER;
	
	/**系统类型*/
	private String systemType;
	
	private String suffix;
	
	public PreProcessMQ(){
	}

	public PreProcessMQ(String gwType){
		this.systemType = gwType;
		if(Global.GW_TYPE_BBMS.equals(gwType)){
			PROCESS_PUBLISHER = Global.PROCESS_PUBLISHER_BBMS;
		}
		else if (Global.GW_TYPE_STB.equals(gwType))
	    {
			this.suffix = "Stb";
		    PROCESS_PUBLISHER = Global.PROCESS_PUBLISHER_STB;
		}
		else
		{
			this.suffix = "";
			PROCESS_PUBLISHER = Global.PROCESS_PUBLISHER_ITMS;
		}
	}
	
	/**
	 * 生成新的策略.
	 * 
	 * @param idArr
	 *            the id of strategy.
	 */
	@Override
	public boolean processOOBatch(String[] idArr) {

		boolean flag = false;
		if (idArr == null || idArr.length == 0) {
			logger.error("idArr == null");
			return flag;
		}
		preProcessMessage = new PreProcessMessage();
		preProcessMessage.setMethodName("processOOBatch");
		preProcessMessage.setStrategyIdArr(idArr);
		try {
			PROCESS_PUBLISHER.publishMQ("cm.strategy"+suffix,preProcessMessage);
			flag = true;
		} catch (Exception e) {
			logger.warn("Send MQ PreProcess Error:{}.", e.getMessage());
			try {
				initMQPool();
				PROCESS_PUBLISHER.publishMQ("cm.strategy"+suffix,preProcessMessage);
				flag = true;
			} catch (RuntimeException e1) {
				logger.error("Send MQ PreProcess Error:{}", e1.getMessage());
			}
		}
		return flag;
	}
	
	
	
	/**
	 * 生成新的策略.
	 * 
	 * @param idArr
	 *            the id of strategy.
	 */
	@Override
	public boolean processOOBatch_stb(String[] idArr, String serviceId) {

		boolean flag = false;
		if (idArr == null || idArr.length == 0) {
			logger.error("idArr == null");
			return flag;
		}
		preProcessMessage = new PreProcessMessage();
		preProcessMessage.setMethodName("processOOBatch");
		preProcessMessage.setStrategyIdArr(idArr);
		preProcessMessage.setServiceId(serviceId);
		try {
			this.publisher(serviceId, preProcessMessage);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("Send MQ PreProcess Error:{}.", e.getMessage());
			try {
				initMQPool();
				this.publisher(serviceId, preProcessMessage);
				flag = true;
			} catch (RuntimeException e1) {
				logger.error("Send MQ PreProcess Error:{}", e1.getMessage());
			}
		}
		return flag;
	}
	
	
	

	/**
	 * 生成新的策略.
	 * 
	 * @param idArr
	 *            the id of strategy.
	 */
	@Override
	public boolean processOOBatch(String id) {

		if (id == null) {
			logger.error("id == null");
			return false;
		}
		return processOOBatch(new String[] { id });
	}

	/**
	 * 默认业务生成新的策略.
	 * 
	 * @param idArr
	 *            the id of strategy.
	 */
	@Override
	public boolean processOMBatch4DefaultService(OneToMany[] objArr) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 默认业务生成新的策略.
	 * 
	 * @param idArr
	 *            the id of strategy.
	 */
	@Override
	public boolean processOMBatch4DefaultService(OneToMany obj) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 绑定设备后通知PP.
	 * 
	 * @param userInfoArr
	 * @return <li>1:成功</li> <li>-1:参数为空</li> <li>-2:绑定失败</li>
	 */
	@Override
	public int processServiceInterface(UserInfo userInfo) {

		if (userInfo == null) {
			logger.error("userInfo == null");
			return -1;
		}
		return processServiceInterface(new UserInfo[] { userInfo });
	}

	/**
	 * 绑定设备后通知PP.(STB专用)
	 * 
	 * @param userInfoArr
	 * @return <li>1:成功</li> <li>-1:参数为空</li> <li>-2:绑定失败</li>
	 */
	@Override
	public int processServiceInterface_STB(String xmlString) {
		this.preProcessMessage = new PreProcessMessage();
	    this.preProcessMessage.setMethodName("processServiceInterface");
	    this.preProcessMessage.setParamXMLStr(xmlString);
	    try {
	      this.PROCESS_PUBLISHER.publishMQ("cm.servStb", this.preProcessMessage);
	    } catch (Exception e) {
	      logger.warn("Send MQ PreProcess Error:{}.", e.getMessage());
	      try {
	        initMQPool();
	        this.PROCESS_PUBLISHER.publishMQ("cm.servStb", this.preProcessMessage);
	      } catch (RuntimeException e1) {
	        logger.error("Send MQ PreProcess Error:{}.", e1);
	        return -2;
	      }
	    }
	    return 1;
	}

	/**
	 * 绑定设备后通知PP.
	 * 
	 * @param userInfoArr
	 * @return <li>1:成功</li> <li>-1:参数为空</li> <li>-2:绑定失败</li>
	 */
	@Override
	public int processServiceInterface(UserInfo[] userInfoArr) {

		if (userInfoArr == null || userInfoArr.length == 0) {
			logger.error("userInfoArr == null");
			return -1;
		}

		UserBean[] userBeanArr = new UserBean[5];
		UserBean userBean = new UserBean();
		for (int index = 0; index < userInfoArr.length; index++) {
			userBean.setDeviceId(userInfoArr[index].deviceId);
			userBean.setDeviceSn(userInfoArr[index].deviceSn);
			userBean.setGatherId(userInfoArr[index].gatherId);
			userBean.setOperTypeId(userInfoArr[index].operTypeId);
			userBean.setOui(userInfoArr[index].oui);
			userBean.setServTypeId(userInfoArr[index].servTypeId);
			userBean.setUserId(userInfoArr[index].userId);
			userBeanArr[index] = userBean;
		}
		preProcessMessage = new PreProcessMessage();
		preProcessMessage.setMethodName("processServiceInterface");
		preProcessMessage.setUserBeanArr(userBeanArr);

		try {
			PROCESS_PUBLISHER.publishMQ("cm.serv"+suffix,preProcessMessage);
		} catch (Exception e) {
			logger.warn("Send MQ PreProcess Error:{}.", e.getMessage());
			try {
				initMQPool();
				PROCESS_PUBLISHER.publishMQ("cm.serv"+suffix,preProcessMessage);
			} catch (RuntimeException e1) {
				logger.error("Send MQ PreProcess Error:{}.", e1);
				return -2;
			}
		}
		return 1;
	}

	/**
	 * get userInfo from params
	 * 
	 * @param length
	 * @return
	 */
	@Override
	public UserInfo GetPPBindUserList(PreServInfoOBJ preInfoObj) {
		logger.debug("GetScheduleSQLList({})", preInfoObj);
		UserInfo uinfo = new UserInfo();
		uinfo.userId = StringUtil.getStringValue(preInfoObj.getUserId());
		uinfo.deviceId = StringUtil.getStringValue(preInfoObj.getDeviceId());
		uinfo.oui = StringUtil.getStringValue(preInfoObj.getOui());
		uinfo.deviceSn = StringUtil.getStringValue(preInfoObj.getDeviceSn());
		uinfo.gatherId = StringUtil.getStringValue(preInfoObj.getGatherId());
		uinfo.servTypeId = StringUtil
				.getStringValue(preInfoObj.getServTypeId());
		uinfo.operTypeId = StringUtil
				.getStringValue(preInfoObj.getOperTypeId());
		return uinfo;
	}

	/**
	 * 长短定时器配置
	 * 
	 * @param userInfoArr
	 * @return <li>1:成功</li> <li>-1:参数为空</li> <li>-2:失败</li>
	 */
	@Override
	public int processDeviceStrategy(String[] deviceIds, String serviceId,
			String[] paramArr) {

		if (deviceIds == null) {
			logger.error("deviceIds == null");
			return -1;
		}
		preProcessMessage = new PreProcessMessage();
		preProcessMessage.setMethodName("processDeviceStrategy");
		preProcessMessage.setDeviceIdIdArr(deviceIds);
		preProcessMessage.setServiceId(serviceId);
		preProcessMessage.setParamArr(paramArr);

		try {
			this.publisher(serviceId, preProcessMessage);
		} catch (Exception e) {
			logger.warn("Send MQ PreProcess Error:{}.", e.getMessage());
			try {
				// 调用配置模块和绑定模块主题
				initMQPool();
				this.publisher(serviceId, preProcessMessage);
			} catch (RuntimeException e1) {
				logger.error("Send MQ PreProcess Error:{}.", e1);
				return -2;
			}
		}
		return 1;
	}
	
	
	/**
	 * 长短定时器配置
	 * 
	 * @param userInfoArr
	 * @return <li>1:成功</li> <li>-1:参数为空</li> <li>-2:失败</li>
	 */
	@Override
	public int processDeviceStrategy(String[] deviceIds, String serviceId,
			String[] paramArr,String taskId) {

		if (deviceIds == null) {
			logger.error("deviceIds == null");
			return -1;
		}
		preProcessMessage = new PreProcessMessage();
		preProcessMessage.setMethodName("processDeviceStrategy");
		preProcessMessage.setDeviceIdIdArr(deviceIds);
		preProcessMessage.setServiceId(serviceId);
		preProcessMessage.setParamArr(paramArr);
		preProcessMessage.setTaskId(taskId);

		try {
			this.publisher(serviceId, preProcessMessage);
		} catch (Exception e) {
			logger.warn("Send MQ PreProcess Error:{}.", e.getMessage());
			try {
				// 调用配置模块和绑定模块主题
				initMQPool();
				this.publisher(serviceId, preProcessMessage);
			} catch (RuntimeException e1) {
				logger.error("Send MQ PreProcess Error:{}.", e1);
				return -2;
			}
		}
		return 1;
	}
	
	/*@Override
	public int processSetStrategyMem(String[] deviceIds,String serviceId)
	{
		if (deviceIds == null) {
			logger.error("deviceIds == null");
			return -1;
		}
		preProcessMessage = new PreProcessMessage();
		preProcessMessage.setMethodName("processDeviceStrategy");
		preProcessMessage.setDeviceIdIdArr(deviceIds);
		preProcessMessage.setServiceId(serviceId);

		try {
			this.publisher(serviceId, preProcessMessage);
		} catch (Exception e) {
			logger.warn("Send MQ PreProcess Error:{}.", e.getMessage());
			try {
				// 调用配置模块和绑定模块主题
				initMQPool();
				this.publisher(serviceId, preProcessMessage);
			} catch (RuntimeException e1) {
				logger.error("Send MQ PreProcess Error:{}.", e1);
				return -2;
			}
		}
		return 1;
	}*/

	/**
	 * 更新缓存中策略信息
	 * @param deviceIdArr
	 * @param alias
	 * @return
	 */
	@Override
	public int processSetStrategyMem(String[] deviceIdArr,String alias) {

		if (deviceIdArr == null || deviceIdArr.length == 0) {
			logger.error("deviceIdArr == null");
			return -1;
		}
		preProcessMessage = new PreProcessMessage();
		preProcessMessage.setMethodName("processSetStrategyMem");
		preProcessMessage.setDeviceIdIdArr(deviceIdArr);
		try {
			logger.warn("更新缓存 processSetStrategyMem, topic="+"cm." + alias +suffix);
			PROCESS_PUBLISHER.publishMQ("cm." + alias +suffix,preProcessMessage);
		} catch (Exception e) {
			logger.warn("Send MQ PreProcess Error:{}.", e.getMessage());
			try {
				initMQPool();
				PROCESS_PUBLISHER.publishMQ("cm." + alias +suffix,preProcessMessage);
				return -2;
			} catch (RuntimeException e1) {
				logger.error("Send MQ PreProcess Error:{}", e1.getMessage());
			}
		}
		return 1;
	}
	/**
	 * 根据serviceId获取MQ对象
	 * 
	 * @param serviceId
	 * @return
	 */
	private void publisher(String serviceId,PreProcessMessage preProcess) {String servServiceIds = "";
	    String softServiceIds = "";
	    String batchServiceIds = "";
	    String picServiceIds = "";
	    if (Global.GW_TYPE_STB.equals(this.systemType)) {
	      servServiceIds = LipossGlobals.getLipossProperty("stb_strategy_tabname.serv.serviceId");
	      softServiceIds = LipossGlobals.getLipossProperty("stb_strategy_tabname.soft.serviceId");
	      picServiceIds = LipossGlobals.getLipossProperty("stb_strategy_tabname.pic.serviceId");
	    } else {
	      servServiceIds = LipossGlobals.getLipossProperty("strategy_tabname.serv.serviceId");
	      softServiceIds = LipossGlobals.getLipossProperty("strategy_tabname.soft.serviceId");
	      batchServiceIds = LipossGlobals.getLipossProperty("strategy_tabname.batch.serviceId");
	    }
	    String[] servServiceIdArr = StringUtil.IsEmpty(servServiceIds) ? new String[]{""} : servServiceIds.split(",");
	    Arrays.sort(servServiceIdArr);
	
	    String[] softServiceIdArr = StringUtil.IsEmpty(softServiceIds) ? new String[]{""} : softServiceIds.split(",");
	    Arrays.sort(softServiceIdArr);
	
	    String[] batchServiceIdArr = StringUtil.IsEmpty(batchServiceIds) ? new String[]{""} : batchServiceIds.split(",");
	    Arrays.sort(batchServiceIdArr);
	    String[] picServiceIdArr = StringUtil.IsEmpty(picServiceIds) ? new String[]{""} : picServiceIds.split(",");
	    Arrays.sort(picServiceIdArr);
	
	    if (Arrays.binarySearch(servServiceIdArr, serviceId) >= 0){
	    	 this.PROCESS_PUBLISHER.publishMQ("cm.serv" + suffix, preProcess);
	    }else if (Arrays.binarySearch(softServiceIdArr, serviceId) >= 0){
	    	 this.PROCESS_PUBLISHER.publishMQ("cm.soft" + suffix, preProcess);
	    }else if ((Global.GW_TYPE_STB.equals(this.systemType)) && Arrays.binarySearch(picServiceIdArr, serviceId) >= 0){
	    	 this.PROCESS_PUBLISHER.publishMQ("cm.pic" + suffix, preProcess);
	    }else if ((!Global.GW_TYPE_STB.equals(this.systemType)) && (Arrays.binarySearch(batchServiceIdArr, serviceId) >= 0)){
	    	this.PROCESS_PUBLISHER.publishMQ("cm.batch", preProcess);
	    }else{
	    	this.PROCESS_PUBLISHER.publishMQ("cm.strategy" + suffix, preProcess);
	    }
	}
	
	private void initMQPool(){
		logger.warn("initMQPool...................................");
				
		// 调用配置模块模块主题
		List<String> suffixList = new ArrayList<String>();
		suffixList.add(".serv");
		suffixList.add(".soft");
		suffixList.add(".batch");
		suffixList.add(".strategy");
		List<String> suffixListStb = new ArrayList<String>();
		suffixList.add(".servStb");
		suffixList.add(".softStb");
		suffixList.add(".strategyStb");
		
		if(Global.GW_TYPE_BBMS.equals(this.systemType )){
			Global.MQ_POOL_PUBLISHER_MAP_BBMS = MQConfigParser.getMQConfig(Global.G_MQPoolPath, "bbms");
			PROCESS_PUBLISHER = new MQPublisher("cm",suffixList,Global.MQ_POOL_PUBLISHER_MAP_BBMS);
		}
		else if (Global.GW_TYPE_STB.equals(this.systemType))
	    {
		    Global.MQ_POOL_PUBLISHER_MAP_STB = MQConfigParser.getMQConfig(Global.G_MQPoolPath, "stb");
		    PROCESS_PUBLISHER = new MQPublisher("cm", suffixListStb, Global.MQ_POOL_PUBLISHER_MAP_STB);
	    }
		else
		{
			Global.MQ_POOL_PUBLISHER_MAP_ITMS = MQConfigParser.getMQConfig(Global.G_MQPoolPath, "itms");
			logger.warn("Global.G_MQPoolPath="+Global.G_MQPoolPath);
			logger.warn("Global.MQ_POOL_PUBLISHER_MAP_ITMS="+Global.MQ_POOL_PUBLISHER_MAP_ITMS);
			logger.warn("Global.MQ_POOL_PUBLISHER_MAP_ITMS.get('cm')"+Global.MQ_POOL_PUBLISHER_MAP_ITMS.get("cm"));
			logger.warn("type"+Global.MQ_POOL_PUBLISHER_MAP_ITMS.get("cm").getType());
			if ("kafka".equals(Global.MQ_POOL_PUBLISHER_MAP_ITMS.get("cm").getType())) {
				KafkaConfig kafka = (KafkaConfig) Global.MQ_POOL_PUBLISHER_MAP_ITMS.get("cm");
				Object aa = new TopicMsgPublisher(kafka);
				logger.warn("aa="+aa);
			}
			
			
			PROCESS_PUBLISHER = new MQPublisher("cm",suffixList,Global.MQ_POOL_PUBLISHER_MAP_ITMS);
			logger.warn("PROCESS_PUBLISHER.getPublisher="+PROCESS_PUBLISHER.getPublisher());
		}
	}

	public static void main(String[] args) {
		String picServiceIds=null;
		 String[] picServiceIdArr = StringUtil.IsEmpty(picServiceIds) ? new String[]{""} : picServiceIds.split(",");
		  Arrays.sort(picServiceIdArr);
		  Arrays.binarySearch(picServiceIdArr, "1");
	}
}
