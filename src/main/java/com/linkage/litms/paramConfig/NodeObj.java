package com.linkage.litms.paramConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * node object.
 * 
 * @author Gongsj (gongsj@lianchuang.com), Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Dec 23, 2008
 * @see
 * @since 1.0
 */
public class NodeObj {
	
	/** log */
	private static Logger logger = LoggerFactory.getLogger(NodeObj.class);
	/** 节点名字 */
	private String name = null;
	/** 节点值 */
	private String value = null;
	/** 是否更改, 1 修改；0 未改 */
	private String isModified = "0";
	/** 节点类型, 1 String; 2 int;3 unsignedInt;4 boolean. */
	private String paramType = "1";

	/**
	 * get name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * get isModified
	 * 
	 * @return
	 */
	public String getIsModified() {
		if (isModified == null || isModified.length() == 0) {
			isModified = "0";
		}
		return isModified;
	}

	public void setIsModified(String isModified) {
		this.isModified = isModified;
	}

	/**
	 * get param type
	 * 
	 * @return
	 */
	public String getParamType() {
		if (paramType == null || paramType.length() == 0) {
			paramType = "1";
		}
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	
	/**
	 * return object.
	 */
	public String toString() {
		logger.debug("toString()");

		return name;
	}
}
