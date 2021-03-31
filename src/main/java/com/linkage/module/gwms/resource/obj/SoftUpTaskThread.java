package com.linkage.module.gwms.resource.obj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.jms.ITopicMessageHandle;
import com.linkage.commons.jms.TopicMsgListener;
import com.linkage.litms.LipossGlobals;

public class SoftUpTaskThread extends Thread implements ITopicMessageHandle {

private static Logger logger = LoggerFactory.getLogger(SoftUpTaskThread.class);
	
	//	 new Listener
	final TopicMsgListener topicListener = new TopicMsgListener(LipossGlobals
			.getLipossProperty("mqSoftUpTask.url"));
	
	public void run()
	{
		logger.debug("sevinfo mq url:({})", LipossGlobals
				.getLipossProperty("mqSoftUpTask.topic"));
		
		// set handle
		topicListener.setMessageHandle(this);
		
		// Regist
		topicListener.listenTopic(LipossGlobals.getLipossProperty("mqSoftUpTask.topic"));
		
		logger.debug("servinfo start listen");
	}
	
	public void handTopicMessage(String _topic, String _message) {
		
		/**
		// 此段代码是模拟接收MQ消息，用于测试
		logger.debug("handTopicMessage({},{})", _topic, _message);
		if(_topic.equals(LipossGlobals.getLipossProperty("mqSoftUpTask.topic")))
		{
			logger.debug("deal message, topic:[{}]", _topic);
			
			XML2Bean x2b = new XML2Bean(_message);
			
			SoftUp softUp = (SoftUp)x2b.getBean("SoftUp", SoftUp.class);
			
			// 没有数据直接返回
			if(null != softUp && false == StringUtil.IsEmpty(softUp.getTask().getClientId())){
				logger.warn("===softUp.getTask()=="+softUp.getTask()+"=====");
				logger.warn("===softUp.getTask().getClientId()=="+softUp.getTask().getClientId()+"=====");
				logger.warn("===softUp.getTask().getTaskId()=="+softUp.getTask().getTaskId()+"=====");
				logger.warn("===softUp.getTask().getAction()=="+softUp.getTask().getAction()+"=====");
			}
		}
		else
		{
			logger.error("unknown topic({})", _topic);
		}
		*/
	}
}
