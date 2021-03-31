
package com.linkage.litms.init;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.jms.MQConfigParser;
import com.linkage.commons.jms.MQPublisher;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.intelspeaker.verconfigfile.InitIntelSpeaker;
import com.linkage.module.intelspeaker.verconfigfile.IntelSpeakerGlobal;

public class SentRouterVerMQServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(SentRouterVerMQServlet.class);

	public void init() throws ServletException
	{
		super.init();
		logger.warn("初始化监听");
		IntelSpeakerGlobal.G_MQPoolPath =  LipossGlobals.getLipossHome()+ File.separator + "WEB-INF"
				+ File.separator +"MQPool.xml";
		IntelSpeakerGlobal.MQ_POOL_PUBLISHER_MAP = MQConfigParser.getMQConfig(IntelSpeakerGlobal.G_MQPoolPath, "publisher");
		IntelSpeakerGlobal.COMMON_PUBLISHER = new MQPublisher("term.newestinfo",IntelSpeakerGlobal.MQ_POOL_PUBLISHER_MAP);
		//初始化智能音箱相关配置
		new InitIntelSpeaker().init();
		
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
