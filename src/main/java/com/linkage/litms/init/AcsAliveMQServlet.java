
package com.linkage.litms.init;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.ailk.tr069.acsalive.thread.AcsAliveMessageDealThread;
import com.linkage.module.gwms.Global;

public class AcsAliveMQServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException
	{
		super.init();
		
		startAcsAliveThread("itms");
		startAcsAliveThread("bbms");
		startAcsAliveThread("stb");
		
//		/** ITMS 监听 */
//		if ("1".equals(LipossGlobals.getLipossProperty("mq.itms.itmsEnable"))
//				&& "1".equals(LipossGlobals.getLipossProperty("mq.itms.mqACSAlive.enab")))
//		{
//			logger.warn("AcsAliveMessageDealThread_ITMS 启动");
//			AcsAliveMessageDealThread aamdt_itms = null;
//			logger.warn("messageServerType"
//					+ LipossGlobals
//							.getLipossProperty("mq.itms.mqACSAlive.messageServerType"));
//			if ("2".equals(LipossGlobals
//					.getLipossProperty("mq.itms.mqACSAlive.messageServerType")))
//			{
//				Map<String, String> kafkaParamMap = new HashMap<String, String>();
//				kafkaParamMap.put("zooKeeper", LipossGlobals
//						.getLipossProperty("kafka.itms.kafkaACSAlive.zooKeeper"));
//				kafkaParamMap.put("groupId", LipossGlobals
//						.getLipossProperty("kafka.itms.kafkaACSAlive.groupId"));
//				kafkaParamMap.put("sessionTimeout", LipossGlobals
//						.getLipossProperty("kafka.itms.kafkaACSAlive.sessionTimeout"));
//				kafkaParamMap.put("syncTime", LipossGlobals
//						.getLipossProperty("kafka.itms.kafkaACSAlive.syncTime"));
//				kafkaParamMap
//						.put("topic", LipossGlobals
//								.getLipossProperty("kafka.itms.kafkaACSAlive.topic"));
//				kafkaParamMap
//						.put("autoCommitInterval",
//								LipossGlobals
//										.getLipossProperty("kafka.itms.kafkaACSAlive.autoCommitInterval"));
//				aamdt_itms = new AcsAliveMessageDealThread(
//						Global.getPrefixName(Global.GW_TYPE_ITMS) + Global.SYSTEM_ACS,
//						kafkaParamMap);
//			}
//			else
//			{
//				aamdt_itms = new AcsAliveMessageDealThread(
//						Global.getPrefixName(Global.GW_TYPE_ITMS) + Global.SYSTEM_ACS, // Global.SYSTEM_ITMS_PREFIX
//																						// +
//						LipossGlobals.getLipossProperty("mq.itms.mqACSAlive.url"),
//						"acs.alive");
//			}
//			aamdt_itms.start();
//		}
//		else
//		{
//			logger.warn("AcsAliveMessageDealThread_ITMS 不启动");
//		}
//		/** BBMS监听 */
//		if ("1".equals(LipossGlobals.getLipossProperty("mq.bbms.bbmsEnable"))
//				&& "1".equals(LipossGlobals.getLipossProperty("mq.bbms.mqACSAlive.enab")))
//		{
//			logger.warn("AcsAliveMessageDealThread_BBS 启动");
//			AcsAliveMessageDealThread aamdt_bbms = null;
//			if ("2".equals(LipossGlobals
//					.getLipossProperty("mq.bbs.mqACSAlive.messageServerType")))
//			{
//				Map<String, String> kafkaParamMap = new HashMap<String, String>();
//				kafkaParamMap.put("zooKeeper", LipossGlobals
//						.getLipossProperty("kafka.bbs.kafkaACSAlive.zooKeeper"));
//				kafkaParamMap.put("groupId", LipossGlobals
//						.getLipossProperty("kafka.bbs.kafkaACSAlive.groupId"));
//				kafkaParamMap.put("sessionTimeout", LipossGlobals
//						.getLipossProperty("kafka.bbs.kafkaACSAlive.sessionTimeout"));
//				kafkaParamMap.put("syncTime", LipossGlobals
//						.getLipossProperty("kafka.bbs.kafkaACSAlive.syncTime"));
//				kafkaParamMap.put("topic",
//						LipossGlobals.getLipossProperty("kafka.bbs.kafkaACSAlive.topic"));
//				kafkaParamMap.put("autoCommitInterval", LipossGlobals
//						.getLipossProperty("kafka.bbs.kafkaACSAlive.autoCommitInterval"));
//				aamdt_bbms = new AcsAliveMessageDealThread(
//						Global.getPrefixName(Global.GW_TYPE_BBMS) + Global.SYSTEM_ACS,
//						kafkaParamMap);
//			}
//			else
//			{
//				aamdt_bbms = new AcsAliveMessageDealThread(
//						Global.getPrefixName(Global.GW_TYPE_BBMS) + Global.SYSTEM_ACS,
//						LipossGlobals.getLipossProperty("mq.bbms.mqACSAlive.url"),
//						"acs.alive");
//			}
//			aamdt_bbms.start();
//		}
//		else
//		{
//			logger.warn("AcsAliveMessageDealThread_BBMS 不启动");
//		}
//		/** STB监听 */
//		if ("1".equals(LipossGlobals.getLipossProperty("mq.stb.stbEnable"))
//				&& "1".equals(LipossGlobals.getLipossProperty("mq.stb.mqACSAlive.enab")))
//		{
//			logger.warn("AcsAliveMessageDealThread_STB 启动");
//			AcsAliveMessageDealThread aamdt_stb = null;
//			if ("2".equals(LipossGlobals
//					.getLipossProperty("mq.stb.mqACSAlive.messageServerType")))
//			{
//				Map<String, String> kafkaParamMap = new HashMap<String, String>();
//				kafkaParamMap.put("zooKeeper", LipossGlobals
//						.getLipossProperty("kafka.stb.kafkaACSAlive.zooKeeper"));
//				kafkaParamMap.put("groupId", LipossGlobals
//						.getLipossProperty("kafka.stb.kafkaACSAlive.groupId"));
//				kafkaParamMap.put("sessionTimeout", LipossGlobals
//						.getLipossProperty("kafka.stb.kafkaACSAlive.sessionTimeout"));
//				kafkaParamMap.put("syncTime", LipossGlobals
//						.getLipossProperty("kafka.stb.kafkaACSAlive.syncTime"));
//				kafkaParamMap.put("topic",
//						LipossGlobals.getLipossProperty("kafka.stb.kafkaACSAlive.topic"));
//				kafkaParamMap.put("autoCommitInterval", LipossGlobals
//						.getLipossProperty("kafka.stb.kafkaACSAlive.autoCommitInterval"));
//				aamdt_stb = new AcsAliveMessageDealThread(
//						Global.getPrefixName(Global.GW_TYPE_STB) + Global.SYSTEM_ACS,
//						kafkaParamMap);
//			}
//			else
//			{
//				aamdt_stb = new AcsAliveMessageDealThread(
//						Global.getPrefixName(Global.GW_TYPE_STB) + Global.SYSTEM_ACS,
//						LipossGlobals.getLipossProperty("mq.stb.mqACSAlive.url"),
//						"acs.alive");
//			}
//			aamdt_stb.start();
//		}
//		else
//		{
//			logger.warn("AcsAliveMessageDealThread_STB 不启动");
//		}
		
		
	}
	
	
	public void startAcsAliveThread(String systemType)
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
		
		AcsAliveMessageDealThread thread = AcsAliveMessageDealThread.getInstance(systemKey,systemType, Global.G_MQPoolPath);
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
