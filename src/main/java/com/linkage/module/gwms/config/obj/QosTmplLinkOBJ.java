package com.linkage.module.gwms.config.obj;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.util.StringUtil;

/**
 * qos_tmpl_link
 * 
 * @author alex(yanhj@)
 * @date 2009-6-17
 */
public class QosTmplLinkOBJ {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(QosTmplLinkOBJ.class);

	/** 模板ID */
	private String tmplId;

	/** 子模板顺序 */
	private String subOrder;

	/** 子模板ID */
	private String subId;

	/** 队列ID */
	private String queueId;

	/** APP名字 */
	private String appName;

	/** DSCP值 */
	private String classDscp;

	/** 8021p值 */
	private String class802;

	/**
	 * constructor
	 */
	public QosTmplLinkOBJ() {
	}

	/**
	 * constructor
	 */
	@SuppressWarnings("unchecked")
	public QosTmplLinkOBJ(Map map) {

		if (map == null) {
			logger.warn("map is null");

			return;
		}

		this.tmplId = StringUtil.getStringValue(map, "tmpl_id");
		this.subOrder = StringUtil.getStringValue(map, "sub_order");
		this.subId = StringUtil.getStringValue(map, "sub_id");
		this.queueId = StringUtil.getStringValue(map, "queue_id");
		this.appName = StringUtil.getStringValue(map, "app_name");
		this.classDscp = StringUtil.getStringValue(map, "class_dscp");
		this.class802 = StringUtil.getStringValue(map, "class_802");
	}

	/**
	 * get:
	 * 
	 * @return the tmplId
	 */
	public String getTmplId() {
		logger.debug("getTmplId()");

		return tmplId;
	}

	/**
	 * get:
	 * 
	 * @return the subOrder
	 */
	public String getSubOrder() {
		logger.debug("getSubOrder()");

		return subOrder;
	}

	/**
	 * get:
	 * 
	 * @return the subId
	 */
	public String getSubId() {
		logger.debug("getSubId()");

		return subId;
	}

	/**
	 * get:
	 * 
	 * @return the queueId
	 */
	public String getQueueId() {
		logger.debug("getQueueId()");

		return queueId;
	}

	/**
	 * get:
	 * 
	 * @return the appName
	 */
	public String getAppName() {
		logger.debug("getAppName()");

		return appName;
	}

	/**
	 * get:
	 * 
	 * @return the classDscp
	 */
	public String getClassDscp() {
		logger.debug("getClassDscp()");

		return classDscp;
	}

	/**
	 * get:
	 * 
	 * @return the class802
	 */
	public String getClass802() {
		logger.debug("getClass802()");

		return class802;
	}

	/**
	 * set:
	 * 
	 * @param tmplId
	 *            the tmplId to set
	 */
	public void setTmplId(String tmplId) {
		logger.debug("setTmplId({})", tmplId);

		this.tmplId = tmplId;
	}

	/**
	 * set:
	 * 
	 * @param subOrder
	 *            the subOrder to set
	 */
	public void setSubOrder(String subOrder) {
		logger.debug("setSubOrder({})", subOrder);

		this.subOrder = subOrder;
	}

	/**
	 * set:
	 * 
	 * @param subId
	 *            the subId to set
	 */
	public void setSubId(String subId) {
		logger.debug("setSubId({})", subId);

		this.subId = subId;
	}

	/**
	 * set:
	 * 
	 * @param queueId
	 *            the queueId to set
	 */
	public void setQueueId(String queueId) {
		logger.debug("setQueueId({})", queueId);

		this.queueId = queueId;
	}

	/**
	 * set:
	 * 
	 * @param appName
	 *            the appName to set
	 */
	public void setAppName(String appName) {
		logger.debug("setAppName({})", appName);

		this.appName = appName;
	}

	/**
	 * set:
	 * 
	 * @param classDscp
	 *            the classDscp to set
	 */
	public void setClassDscp(String classDscp) {
		logger.debug("setClassDscp({})", classDscp);

		this.classDscp = classDscp;
	}

	/**
	 * set:
	 * 
	 * @param class802
	 *            the class802 to set
	 */
	public void setClass802(String class802) {
		logger.debug("setClass802({})", class802);

		this.class802 = class802;
	}

	/**
	 * string to object.
	 */
	public String toString() {
		logger.debug("toString()");

		return "tmplId=" + tmplId + "|subOrder=" + this.subOrder + "|subId="
				+ this.subId + "|queueId=" + this.queueId;
	}
}
