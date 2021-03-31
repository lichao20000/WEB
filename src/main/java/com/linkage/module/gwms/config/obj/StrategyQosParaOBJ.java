package com.linkage.module.gwms.config.obj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author Jason(3412)
 * @date 2009-7-24
 */
public class StrategyQosParaOBJ extends SuperDAO {

	private static Logger logger = LoggerFactory
			.getLogger(StrategyQosParaOBJ.class);

	//策略ID
	private long id;
	//子模板顺序
	private int subOrder;
	//type顺序
	private int typeOrder;
	//子模板ID
	private int subId;
	//type id
	private int typeId;
	//配置值type_name
	private String typeName;
	//配置值type_max
	private String typeMax;
	//配置值type_min
	private String typeMin;
	//配置值type_Prot
	private String typeProt;
	//配置值队列
	private int queueId;
	//参数值,配置值参数的值,如：ssid名称
	private String paraValue;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSubOrder() {
		return subOrder;
	}

	public void setSubOrder(int subOrder) {
		this.subOrder = subOrder;
	}

	public int getTypeOrder() {
		return typeOrder;
	}

	public void setTypeOrder(int typeOrder) {
		this.typeOrder = typeOrder;
	}

	public int getSubId() {
		return subId;
	}

	public void setSubId(int subId) {
		this.subId = subId;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeMax() {
		return typeMax;
	}

	public void setTypeMax(String typeMax) {
		this.typeMax = typeMax;
	}

	public String getTypeMin() {
		return typeMin;
	}

	public void setTypeMin(String typeMin) {
		this.typeMin = typeMin;
	}

	public String getTypeProt() {
		return typeProt;
	}

	public void setTypeProt(String typeProt) {
		this.typeProt = typeProt;
	}

	public void setQueueId(int queueId) {
		this.queueId = queueId;
	}

	public int getQueueId() {
		return queueId;
	}

	public String getParaValue() {
		return paraValue;
	}

	public void setParaValue(String paraValue) {
		this.paraValue = paraValue;
	}

	public String toString(){
		logger.debug("toString()");
		return this.id + " " + this.typeName + " " + this.typeMax + " " + this.paraValue;
	}
}
