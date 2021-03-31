/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */

package com.linkage.module.bbms.obj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * table:data_card.
 * 
 * @author wangsenbo (wangsenbo@lianchuang.com)
 * @date 2009-10-12
 */
public class DataCardOBJ
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(DataCardOBJ.class);
	/** 数据卡ID */
	private String dataCardId = null;
	/** ESN */
	private String dataCardEsn = null;
	/** 网关ID */
	private String deviceId = null;
	/** 厂商 */
	private int vendorId = 0;
	/** 型号 */
	private int modelId = 0;
	/** 硬件版本 */
	private int hardId = 0;
	/** 固件版本 */
	private int firmId = 0;
	/** 描述 */
	private String dataCardDesc = null;
	/** 绑定标志 */
	private int bindStat = 0;
	/** 注册时间 */
	private long completeTime = 0;
	/** 绑定时间 */
	private long bindTime = 0;
	/** 更新时间 */
	private long updateTime = 0;
	/** 备注 */
	private String remark = null;
	/** 工作模式 */
	private String workMode = null;

	/**
	 * @return the dataCardId
	 */
	public String getDataCardId()
	{
		logger.debug("getDataCardId()");
		return dataCardId;
	}

	/**
	 * @param dataCardId
	 *            the dataCardId to set
	 */
	public void setDataCardId(String dataCardId)
	{
		logger.debug("setDataCardId({})", dataCardId);
		this.dataCardId = dataCardId;
	}

	/**
	 * @return the dataCardEsn
	 */
	public String getDataCardEsn()
	{
		logger.debug("getDataCardEsn()");
		return dataCardEsn;
	}

	/**
	 * @param dataCardEsn
	 *            the dataCardEsn to set
	 */
	public void setDataCardEsn(String dataCardEsn)
	{
		logger.debug("setDataCardEsn({})", dataCardEsn);
		this.dataCardEsn = dataCardEsn;
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
	 * @return the vendorId
	 */
	public int getVendorId()
	{
		logger.debug("getVendorId()");
		return vendorId;
	}

	/**
	 * @param vendorId
	 *            the vendorId to set
	 */
	public void setVendorId(int vendorId)
	{
		logger.debug("setVendorId({})", vendorId);
		this.vendorId = vendorId;
	}

	/**
	 * @return the modelId
	 */
	public int getModelId()
	{
		logger.debug("getModelId()");
		return modelId;
	}

	/**
	 * @param modelId
	 *            the modelId to set
	 */
	public void setModelId(int modelId)
	{
		logger.debug("setModelId({})", modelId);
		this.modelId = modelId;
	}

	/**
	 * @return the hardId
	 */
	public int getHardId()
	{
		logger.debug("getHardId()");
		return hardId;
	}

	/**
	 * @param hardId
	 *            the hardId to set
	 */
	public void setHardId(int hardId)
	{
		logger.debug("setHardId({})", hardId);
		this.hardId = hardId;
	}

	/**
	 * @return the firmId
	 */
	public int getFirmId()
	{
		logger.debug("getFirmId()");
		return firmId;
	}

	/**
	 * @param firmId
	 *            the firmId to set
	 */
	public void setFirmId(int firmId)
	{
		logger.debug("setFirmId({})", firmId);
		this.firmId = firmId;
	}

	/**
	 * @return the dataCardDesc
	 */
	public String getDataCardDesc()
	{
		logger.debug("getDataCardDesc()");
		return dataCardDesc;
	}

	/**
	 * @param dataCardDesc
	 *            the dataCardDesc to set
	 */
	public void setDataCardDesc(String dataCardDesc)
	{
		logger.debug("setDataCardDesc({})", dataCardDesc);
		this.dataCardDesc = dataCardDesc;
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
		logger.debug("toString()");
		return "数据卡[" + dataCardId + "," + deviceId + "]";
	}

	/**
	 * @return the workMode
	 */
	public String getWorkMode()
	{
		logger.debug("getWorkMode()");
		return workMode;
	}

	/**
	 * @param workMode
	 *            the workMode to set
	 */
	public void setWorkMode(String workMode)
	{
		logger.debug("setWorkMode({})", workMode);
		this.workMode = workMode;
	}
}
