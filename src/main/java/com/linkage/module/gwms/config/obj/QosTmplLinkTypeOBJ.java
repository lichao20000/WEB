package com.linkage.module.gwms.config.obj;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.util.StringUtil;

/**
 * qos_tmpl_link_type
 * 
 * @author alex(yanhj@)
 * @date 2009-6-17
 */
public class QosTmplLinkTypeOBJ {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(QosTmplLinkTypeOBJ.class);

	/** 模板ID */
	private String tmplId;

	/** 子模板顺序 */
	private String subOrder;

	/** 类型顺序 */
	private String typeOrder;

	/** 类型ID */
	private String typeId;

	/** 类型名称 */
	private String typeName;

	/** 最大值 */
	private String typeMax;

	/** 最小值 */
	private String typeMin;

	/** 协议 */
	private String typeProt;

	/**
	 * constructor
	 */
	public QosTmplLinkTypeOBJ() {
	}

	/**
	 * constructor
	 */
	@SuppressWarnings("unchecked")
	public QosTmplLinkTypeOBJ(Map map) {

		if (map == null) {
			logger.warn("map is null");

			return;
		}

		this.tmplId = StringUtil.getStringValue(map, "tmpl_id");
		this.subOrder = StringUtil.getStringValue(map, "sub_order");
		this.typeOrder = StringUtil.getStringValue(map, "type_order");
		this.typeId = StringUtil.getStringValue(map, "type_id");
		this.typeName = StringUtil.getStringValue(map, "type_name");
		this.typeMax = StringUtil.getStringValue(map, "type_max");
		this.typeMin = StringUtil.getStringValue(map, "type_min");
		this.typeProt = StringUtil.getStringValue(map, "type_prot");
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
	 * @return the typeOrder
	 */
	public String getTypeOrder() {
		logger.debug("getTypeOrder()");

		return typeOrder;
	}

	/**
	 * get:
	 * 
	 * @return the typeId
	 */
	public String getTypeId() {
		logger.debug("getTypeId()");

		return typeId;
	}

	/**
	 * get:
	 * 
	 * @return the typeName
	 */
	public String getTypeName() {
		logger.debug("getTypeName()");

		return typeName;
	}

	/**
	 * get:
	 * 
	 * @return the typeMax
	 */
	public String getTypeMax() {
		logger.debug("getTypeMax()");

		return typeMax;
	}

	/**
	 * get:
	 * 
	 * @return the typeMin
	 */
	public String getTypeMin() {
		logger.debug("getTypeMin()");

		return typeMin;
	}

	/**
	 * get:
	 * 
	 * @return the typeProt
	 */
	public String getTypeProt() {
		logger.debug("getTypeProt()");

		return typeProt;
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
	 * @param typeOrder
	 *            the typeOrder to set
	 */
	public void setTypeOrder(String typeOrder) {
		logger.debug("setTypeOrder({})", typeOrder);

		this.typeOrder = typeOrder;
	}

	/**
	 * set:
	 * 
	 * @param typeId
	 *            the typeId to set
	 */
	public void setTypeId(String typeId) {
		logger.debug("setTypeId({})", typeId);

		this.typeId = typeId;
	}

	/**
	 * set:
	 * 
	 * @param typeName
	 *            the typeName to set
	 */
	public void setTypeName(String typeName) {
		logger.debug("setTypeName({})", typeName);

		this.typeName = typeName;
	}

	/**
	 * set:
	 * 
	 * @param typeMax
	 *            the typeMax to set
	 */
	public void setTypeMax(String typeMax) {
		logger.debug("setTypeMax({})", typeMax);

		this.typeMax = typeMax;
	}

	/**
	 * set:
	 * 
	 * @param typeMin
	 *            the typeMin to set
	 */
	public void setTypeMin(String typeMin) {
		logger.debug("setTypeMin({})", typeMin);

		this.typeMin = typeMin;
	}

	/**
	 * set:
	 * 
	 * @param typeProt
	 *            the typeProt to set
	 */
	public void setTypeProt(String typeProt) {
		logger.debug("setTypeProt({})", typeProt);

		this.typeProt = typeProt;
	}

	/**
	 * string to object.
	 */
	public String toString() {
		logger.debug("toString()");

		return "tmplId=" + tmplId + "|subOrder=" + this.subOrder
				+ "|typeOrder=" + this.typeOrder + "|typeName=" + this.typeName;
	}
}
