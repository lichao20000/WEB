package com.linkage.module.gwms.config.obj;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.util.StringUtil;

/**
 * qos_tmpl
 * 
 * @author alex(yanhj@)
 * @date 2009-6-17
 */
public class QosTmplOBJ {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(QosTmplOBJ.class);

	/** 模板ID */
	private String tmplId;

	/** 时间 */
	private String tmplTime;

	/** 模板名字 */
	private String tmplName;

	/** 类型 */
	private String tmplType;

	/** 模式 */
	private String tmplMode;

	/** 是否启用 */
	private String tmplEnab;

	/** Plan */
	private String tmplPlan;

	/** 带宽 */
	private String bandwidth;

	/** 强制带宽 */
	private String enabForceWeight;

	/** DSCP */
	private String enabDscp;

	/** 802-1_P */
	private String enab802;

	/** 管理员ID */
	private String accOid;

	/** 描述 */
	private String tmplDesc;

	/**
	 * constructor
	 */
	public QosTmplOBJ() {
	}

	/**
	 * constructor
	 */
	@SuppressWarnings("unchecked")
	public QosTmplOBJ(Map map) {

		if (map == null) {
			logger.warn("map is null");

			return;
		}

		this.tmplId = StringUtil.getStringValue(map, "tmpl_id");
		this.tmplName = StringUtil.getStringValue(map, "tmpl_name");
		this.tmplTime = StringUtil.getStringValue(map, "tmpl_time");
		this.tmplType = StringUtil.getStringValue(map, "tmpl_type");
		this.tmplMode = StringUtil.getStringValue(map, "tmpl_mode");
		this.tmplEnab = StringUtil.getStringValue(map, "tmpl_enab");
		this.bandwidth = StringUtil.getStringValue(map, "bandwidth");
		this.tmplPlan = StringUtil.getStringValue(map, "tmpl_plan");
		this.enabForceWeight = StringUtil.getStringValue(map,
				"enab_force_weight");
		this.enabDscp = StringUtil.getStringValue(map, "enab_dscp");
		this.enab802 = StringUtil.getStringValue(map, "enab_802");
		this.accOid = StringUtil.getStringValue(map, "acc_oid");
		this.tmplDesc = StringUtil.getStringValue(map, "tmpl_desc");
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
	 * @return the tmplTime
	 */
	public String getTmplTime() {
		logger.debug("getTmplTime()");

		return tmplTime;
	}

	/**
	 * get:
	 * 
	 * @return the tmplName
	 */
	public String getTmplName() {
		logger.debug("getTmplName()");

		return tmplName;
	}

	/**
	 * get:
	 * 
	 * @return the tmplType
	 */
	public String getTmplType() {
		logger.debug("getTmplType()");

		return tmplType;
	}

	/**
	 * get:
	 * 
	 * @return the tmplMode
	 */
	public String getTmplMode() {
		logger.debug("getTmplMode()");

		return tmplMode;
	}

	/**
	 * get:
	 * 
	 * @return the tmplEnab
	 */
	public String getTmplEnab() {
		logger.debug("getTmplEnab()");

		return tmplEnab;
	}

	/**
	 * get:
	 * 
	 * @return the tmplPlan
	 */
	public String getTmplPlan() {
		logger.debug("getTmplPlan()");

		return tmplPlan;
	}

	/**
	 * get:
	 * 
	 * @return the bandwidth
	 */
	public String getBandwidth() {
		logger.debug("getBandwidth()");

		return bandwidth;
	}

	/**
	 * get:
	 * 
	 * @return the enabForceWeight
	 */
	public String getEnabForceWeight() {
		logger.debug("getEnabForceWeight()");

		return enabForceWeight;
	}

	/**
	 * get:
	 * 
	 * @return the enabDscp
	 */
	public String getEnabDscp() {
		logger.debug("getEnabDscp()");

		return enabDscp;
	}

	/**
	 * get:
	 * 
	 * @return the enab802
	 */
	public String getEnab802() {
		logger.debug("getEnab802()");

		return enab802;
	}

	/**
	 * get:
	 * 
	 * @return the accOid
	 */
	public String getAccOid() {
		logger.debug("getAccOid()");

		return accOid;
	}

	/**
	 * get:
	 * 
	 * @return the tmplDesc
	 */
	public String getTmplDesc() {
		logger.debug("getTmplDesc()");

		return tmplDesc;
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
	 * @param tmplTime
	 *            the tmplTime to set
	 */
	public void setTmplTime(String tmplTime) {
		logger.debug("setTmplTime({})", tmplTime);

		this.tmplTime = tmplTime;
	}

	/**
	 * set:
	 * 
	 * @param tmplName
	 *            the tmplName to set
	 */
	public void setTmplName(String tmplName) {
		logger.debug("setTmplName({})", tmplName);

		this.tmplName = tmplName;
	}

	/**
	 * set:
	 * 
	 * @param tmplType
	 *            the tmplType to set
	 */
	public void setTmplType(String tmplType) {
		logger.debug("setTmplType({})", tmplType);

		this.tmplType = tmplType;
	}

	/**
	 * set:
	 * 
	 * @param tmplMode
	 *            the tmplMode to set
	 */
	public void setTmplMode(String tmplMode) {
		logger.debug("setTmplMode({})", tmplMode);

		this.tmplMode = tmplMode;
	}

	/**
	 * set:
	 * 
	 * @param tmplEnab
	 *            the tmplEnab to set
	 */
	public void setTmplEnab(String tmplEnab) {
		logger.debug("setTmplEnab({})", tmplEnab);

		this.tmplEnab = tmplEnab;
	}

	/**
	 * set:
	 * 
	 * @param tmplPlan
	 *            the tmplPlan to set
	 */
	public void setTmplPlan(String tmplPlan) {
		logger.debug("setTmplPlan({})", tmplPlan);

		this.tmplPlan = tmplPlan;
	}

	/**
	 * set:
	 * 
	 * @param bandwidth
	 *            the bandwidth to set
	 */
	public void setBandwidth(String bandwidth) {
		logger.debug("setBandwidth({})", bandwidth);

		this.bandwidth = bandwidth;
	}

	/**
	 * set:
	 * 
	 * @param enabForceWeight
	 *            the enabForceWeight to set
	 */
	public void setEnabForceWeight(String enabForceWeight) {
		logger.debug("setEnabForceWeight({})", enabForceWeight);

		this.enabForceWeight = enabForceWeight;
	}

	/**
	 * set:
	 * 
	 * @param enabDscp
	 *            the enabDscp to set
	 */
	public void setEnabDscp(String enabDscp) {
		logger.debug("setEnabDscp({})", enabDscp);

		this.enabDscp = enabDscp;
	}

	/**
	 * set:
	 * 
	 * @param enab802
	 *            the enab802 to set
	 */
	public void setEnab802(String enab802) {
		logger.debug("setEnab802({})", enab802);

		this.enab802 = enab802;
	}

	/**
	 * set:
	 * 
	 * @param accOid
	 *            the accOid to set
	 */
	public void setAccOid(String accOid) {
		logger.debug("setAccOid({})", accOid);

		this.accOid = accOid;
	}

	/**
	 * set:
	 * 
	 * @param tmplDesc
	 *            the tmplDesc to set
	 */
	public void setTmplDesc(String tmplDesc) {
		logger.debug("setTmplDesc({})", tmplDesc);

		this.tmplDesc = tmplDesc;
	}

	/**
	 * string to object.
	 */
	public String toString() {
		logger.debug("toString()");

		return "tmplId=" + tmplId + "|enable=" + this.tmplEnab + "|mode="
				+ this.tmplMode + "|plan=" + this.tmplPlan;
	}
}
