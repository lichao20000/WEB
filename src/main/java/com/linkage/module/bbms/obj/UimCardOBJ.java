/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */

package com.linkage.module.bbms.obj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * table:uim_card.
 * 
 * @author wangsenbo (wangsenbo@lianchuang.com)
 * @date 2009-10-12
 */
public class UimCardOBJ
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(UimCardOBJ.class);
	/** uim卡ID */
	private String uimCardId = null;
	/** IMSI */
	private String uimCardImsi = null;
	/** 网关ID */
	private String deviceId = null;
	/** 描述 */
	private String uimCardDesc = null;
	/** 绑定标志 */
	private int bindStat = 0;
	/** 注册时间 */
	private long completeTime = 0;
	/** 绑定时间 */
	private long bindTime = 0;
	/** 更新时间 */
	private long updateTime = 0;
	/** 供电电压 */
	private long voltage = 0;
	/** 备注 */
	private String remark = null;

	/**
	 * @return the uimCardId
	 */
	public String getUimCardId()
	{
		logger.debug("getUimCardId()");
		return uimCardId;
	}

	/**
	 * @param uimCardId
	 *            the uimCardId to set
	 */
	public void setUimCardId(String uimCardId)
	{
		logger.debug("setUimCardId({})", uimCardId);
		this.uimCardId = uimCardId;
	}

	/**
	 * @return the uimCardImsi
	 */
	public String getUimCardImsi()
	{
		logger.debug("getUimCardImsi()");
		return uimCardImsi;
	}

	/**
	 * @param uimCardImsi
	 *            the uimCardImsi to set
	 */
	public void setUimCardImsi(String uimCardImsi)
	{
		logger.debug("setUimCardImsi({})", uimCardImsi);
		this.uimCardImsi = uimCardImsi;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId()
	{
		logger.debug("getDeviceId()");
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId)
	{
		logger.debug("setDeviceId({})", deviceId);
		this.deviceId = deviceId;
	}

	/**
	 * @return the uimCardDesc
	 */
	public String getUimCardDesc()
	{
		logger.debug("getUimCardDesc()");
		return uimCardDesc;
	}

	/**
	 * @param uimCardDesc
	 *            the uimCardDesc to set
	 */
	public void setUimCardDesc(String uimCardDesc)
	{
		logger.debug("setUimCardDesc({})", uimCardDesc);
		this.uimCardDesc = uimCardDesc;
	}

	/**
	 * @return the bindStat
	 */
	public int getBindStat()
	{
		logger.debug("getBindStat()");
		return bindStat;
	}

	/**
	 * @param bindStat
	 *            the bindStat to set
	 */
	public void setBindStat(int bindStat)
	{
		logger.debug("setBindStat({})", bindStat);
		this.bindStat = bindStat;
	}

	/**
	 * @return the completeTime
	 */
	public long getCompleteTime()
	{
		logger.debug("getCompleteTime()");
		return completeTime;
	}

	/**
	 * @param completeTime
	 *            the completeTime to set
	 */
	public void setCompleteTime(long completeTime)
	{
		logger.debug("setCompleteTime({})", completeTime);
		this.completeTime = completeTime;
	}

	/**
	 * @return the bindTime
	 */
	public long getBindTime()
	{
		logger.debug("getBindTime()");
		return bindTime;
	}

	/**
	 * @param bindTime
	 *            the bindTime to set
	 */
	public void setBindTime(long bindTime)
	{
		logger.debug("setBindTime({})", bindTime);
		this.bindTime = bindTime;
	}

	/**
	 * @return the updateTime
	 */
	public long getUpdateTime()
	{
		logger.debug("getUpdateTime()");
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(long updateTime)
	{
		logger.debug("setUpdateTime({})", updateTime);
		this.updateTime = updateTime;
	}

	/**
	 * @return the voltage
	 */
	public long getVoltage()
	{
		logger.debug("getVoltage()");
		return voltage;
	}

	/**
	 * @param voltage
	 *            the voltage to set
	 */
	public void setVoltage(long voltage)
	{
		logger.debug("setVoltage({})", voltage);
		this.voltage = voltage;
	}

	/**
	 * @return the remark
	 */
	public String getRemark()
	{
		logger.debug("getRemark()");
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark)
	{
		logger.debug("setRemark({})", remark);
		this.remark = remark;
	}

	/** string to object */
	public String toString()
	{
		return "uim卡[" + uimCardId + "," + deviceId + "]";
	}
}
