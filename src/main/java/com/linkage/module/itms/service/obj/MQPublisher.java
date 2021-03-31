package com.linkage.module.itms.service.obj;

import javax.jms.DeliveryMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.jms.TopicMsgPublisher;
import com.linkage.commons.jms.obj.ActiveMQConfig;
import com.linkage.commons.jms.obj.KafkaConfig;
import com.linkage.commons.xml.Bean2XML;
import com.linkage.module.gwms.Global;

public class MQPublisher {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(MQPublisher.class);
	
	/** 是否生效 **/
	private String enable;
	
	/** 广播URL **/
	private String url;
	
	/** 广播topic **/
	private String topic;
	
	/** msg publisher **/
	private TopicMsgPublisher publisher;
	
	public MQPublisher(String enable, String url, String topic)
	{
		logger.warn("MQAbsPublisher({},{},{})", new Object[]{enable, url, topic});
		this.enable = enable;
		this.url = url;
		this.topic = topic;
		this.publisher = new TopicMsgPublisher(this.url);
		this.getPublisher().registTopic(this.topic, DeliveryMode.PERSISTENT);
	}
	
	public MQPublisher(String topic){
		if ("activeMQ".equals(Global.MQ_POOL_PUBLISHER_MAP.get(topic).getType())){
			ActiveMQConfig activeMQ = (ActiveMQConfig) Global.MQ_POOL_PUBLISHER_MAP.get(topic);
			this.enable = activeMQ.getEnable();
			this.url = activeMQ.getUrl();
			this.topic = topic;		
			this.publisher = new TopicMsgPublisher(this.url);		
			this.getPublisher().registTopic(this.topic, DeliveryMode.PERSISTENT);
		}
		else if("kafka".equals(Global.MQ_POOL_PUBLISHER_MAP.get(topic).getType())){
			KafkaConfig kafka = (KafkaConfig) Global.MQ_POOL_PUBLISHER_MAP.get(topic);
			this.topic = topic;
			this.publisher = new TopicMsgPublisher(kafka);
		}
		
	}
	
	/**
	 * 发布MQ消息
	 * @param obj
	 */
	public void publishMQ(Object obj){
		
		logger.debug("publishMQ()");
		
		Bean2XML bean2XML = new Bean2XML();
		String xml = bean2XML.getXML(obj);
		logger.warn("send MQ({}):{}", this.topic, xml);
		this.getPublisher().publish(this.topic, xml);
	}
	
	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public TopicMsgPublisher getPublisher() {
		logger.debug("getPublisher()");
		return publisher;
	}

	public void setPublisher(TopicMsgPublisher publisher) {
		this.publisher = publisher;
	}
	
}
