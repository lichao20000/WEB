
package com.linkage.litms.init;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.ailk.tr069.devrpc.thread.AcsDevRpcThread;
import com.linkage.module.gwms.Global;

public class AcsRpcMQServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	


	public void init() throws ServletException
	{
		
		startAcsRPCThread("itms");
		startAcsRPCThread("bbms");
		startAcsRPCThread("stb");
//		/** ITMS 监听 */
//		if ("1".equals(LipossGlobals.getLipossProperty("mq.itms.itmsEnable"))
//				&& "1".equals(LipossGlobals
//						.getLipossProperty("mq.itms.mqDevRPC.enab"))) {
//			
//			logger.warn("AcsDevRpcThread_ITMS启动");
//			AcsDevRpcThread aamdt_itms = null;
//			logger.warn("messageServerType" + LipossGlobals.getLipossProperty("mq.itms.mqDevRPC.messageServerType"));
//			if("2".equals(LipossGlobals.getLipossProperty("mq.itms.mqDevRPC.messageServerType"))){
//				Map<String,String> kafkaParamMap = new HashMap<String, String>();
//				kafkaParamMap.put("zooKeeper", LipossGlobals.getLipossProperty("kafka.itms.kafkaDevRPC.zooKeeper"));
//				kafkaParamMap.put("groupId", LipossGlobals.getLipossProperty("kafka.itms.kafkaDevRPC.groupId"));
//				kafkaParamMap.put("sessionTimeout", LipossGlobals.getLipossProperty("kafka.itms.kafkaDevRPC.sessionTimeout"));
//				kafkaParamMap.put("syncTime", LipossGlobals.getLipossProperty("kafka.itms.kafkaDevRPC.syncTime"));
//				kafkaParamMap.put("topic", LipossGlobals.getLipossProperty("kafka.itms.kafkaDevRPC.topic"));
//				kafkaParamMap.put("autoCommitInterval", LipossGlobals.getLipossProperty("kafka.itms.kafkaDevRPC.autoCommitInterval"));
//				aamdt_itms = new AcsDevRpcThread(Global.getPrefixName(Global.GW_TYPE_ITMS)+Global.SYSTEM_ACS,
//						kafkaParamMap, LipossGlobals.getClientId());
//			}
//			else{
//				aamdt_itms = new AcsDevRpcThread(
//						Global.getPrefixName(Global.GW_TYPE_ITMS)+Global.SYSTEM_ACS, 
//						LipossGlobals.getLipossProperty("mq.itms.mqDevRPC.url"),
//						"dev.rpc", LipossGlobals.getClientId());
//			}	
//			aamdt_itms.start();
//			
//		} else {
//			logger.warn("AcsDevRpcThread_ITMS 不启动");
//		}
//		
//		
//		/** BBMS 监听 */
//		if ("1".equals(LipossGlobals.getLipossProperty("mq.bbms.bbmsEnable"))
//				&& "1".equals(LipossGlobals
//						.getLipossProperty("mq.bbms.mqDevRPC.enab"))) {
//			
//			logger.warn("AcsDevRpcThread_BBMS启动");
//			
//			AcsDevRpcThread aamdt_bbms = null;
//			if("2".equals(LipossGlobals.getLipossProperty("mq.bbs.mqDevRPC.messageServerType"))){
//				Map<String,String> kafkaParamMap = new HashMap<String, String>();
//				kafkaParamMap.put("zooKeeper", LipossGlobals.getLipossProperty("kafka.bbs.kafkaDevRPC.zooKeeper"));
//				kafkaParamMap.put("groupId", LipossGlobals.getLipossProperty("kafka.bbs.kafkaDevRPC.groupId"));
//				kafkaParamMap.put("sessionTimeout", LipossGlobals.getLipossProperty("kafka.bbs.kafkaDevRPC.sessionTimeout"));
//				kafkaParamMap.put("syncTime", LipossGlobals.getLipossProperty("kafka.bbs.kafkaDevRPC.syncTime"));
//				kafkaParamMap.put("topic", LipossGlobals.getLipossProperty("kafka.bbs.kafkaDevRPC.topic"));
//				kafkaParamMap.put("autoCommitInterval", LipossGlobals.getLipossProperty("kafka.bbs.kafkaDevRPC.autoCommitInterval"));
//				aamdt_bbms = new AcsDevRpcThread(Global.getPrefixName(Global.GW_TYPE_BBMS)+Global.SYSTEM_ACS,
//						kafkaParamMap, LipossGlobals.getClientId());
//			}
//			else{
//				aamdt_bbms = new AcsDevRpcThread(
//						Global.getPrefixName(Global.GW_TYPE_BBMS)+Global.SYSTEM_ACS,
//						LipossGlobals.getLipossProperty("mq.bbms.mqDevRPC.url"),
//						"dev.rpc", LipossGlobals.getClientId());
//			}	
//			aamdt_bbms.start();
//			
//		} else {
//			logger.warn("AcsDevRpcThread_BBMS 不启动");
//		}
//		
//		
//		/** STB 监听 */
//		if ("1".equals(LipossGlobals.getLipossProperty("mq.stb.stbEnable"))
//				&& "1".equals(LipossGlobals
//						.getLipossProperty("mq.stb.mqDevRPC.enab"))) {
//			
//			logger.warn("AcsDevRpcThread_STB 启动");
//			
//			AcsDevRpcThread aamdt_stb = null;
//			if("2".equals(LipossGlobals.getLipossProperty("mq.stb.mqDevRPC.messageServerType"))){
//				Map<String,String> kafkaParamMap = new HashMap<String, String>();
//				kafkaParamMap.put("zooKeeper", LipossGlobals.getLipossProperty("kafka.stb.kafkaDevRPC.zooKeeper"));
//				kafkaParamMap.put("groupId", LipossGlobals.getLipossProperty("kafka.stb.kafkaDevRPC.groupId"));
//				kafkaParamMap.put("sessionTimeout", LipossGlobals.getLipossProperty("kafka.stb.kafkaDevRPC.sessionTimeout"));
//				kafkaParamMap.put("syncTime", LipossGlobals.getLipossProperty("kafka.stb.kafkaDevRPC.syncTime"));
//				kafkaParamMap.put("topic", LipossGlobals.getLipossProperty("kafka.stb.kafkaDevRPC.topic"));
//				kafkaParamMap.put("autoCommitInterval", LipossGlobals.getLipossProperty("kafka.stb.kafkaDevRPC.autoCommitInterval"));
//				aamdt_stb = new AcsDevRpcThread(Global.getPrefixName(Global.GW_TYPE_STB)+Global.SYSTEM_ACS,
//						kafkaParamMap, LipossGlobals.getClientId());
//			}
//			else{
//				aamdt_stb = new AcsDevRpcThread(
//						Global.getPrefixName(Global.GW_TYPE_STB)+Global.SYSTEM_ACS,
//						LipossGlobals.getLipossProperty("mq.stb.mqDevRPC.url"),
//						"dev.rpc", LipossGlobals.getClientId());
//			}	
//			aamdt_stb.start();
//			
//		} else {
//			logger.warn("AcsDevRpcThread_STB 不启动");
//		}
//		
	}

	
	public void startAcsRPCThread(String systemType)
	{
		String systemKey = null;
		if ("itms".equals(systemType))
		{
			systemKey = Global.getPrefixName(Global.GW_TYPE_ITMS) + Global.SYSTEM_ACS;
		}
		else if("bbms".equals(systemType))
		{
			systemKey = Global.getPrefixName(Global.GW_TYPE_BBMS) + Global.SYSTEM_ACS;
		}
		else if("stb".equals(systemType))
		{
			systemKey = Global.getPrefixName(Global.GW_TYPE_STB) + Global.SYSTEM_ACS;
		}
		
		AcsDevRpcThread thread = AcsDevRpcThread.getInstance(systemKey,systemType, Global.G_MQPoolPath);
		if(null != thread)
		{
			thread.start();
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doGet(request, response);
	}
}
