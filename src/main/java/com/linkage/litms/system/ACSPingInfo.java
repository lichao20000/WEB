package com.linkage.litms.system;

import java.util.Date;

public class ACSPingInfo 
{
    /**
     * ACS名称
     */
	private String acsName ="";
	
	/**
	 * 采集点
	 */
	private String gather_id ="";
	
	/**
	 * ACS的IP地址
	 */
	private String acsIp="";
	
	/**
	 * ACS的ping状态
	 */
	private boolean acsStatus = false;	
	
	/**
	 * 时间
	 */
	private long time=0L;
	
	
	
	public ACSPingInfo(String _gatherId,String _acsIp,boolean _acsStatus)
	{
		gather_id = _gatherId;
		acsName = "ACS_"+gather_id;
		acsIp = _acsIp;
		acsStatus =_acsStatus;
		time = new Date().getTime()/1000;
	}	
	
	
	/**
	 * 返回ACS名称
	 * @return
	 */
	public String getACSName()
	{
		return acsName;
	}
	
	/**
	 * 返回ACSIP
	 * @return
	 */
	public String getACSIp()
	{
		return acsIp;
	}
	
	/**
	 * 返回ACS状态
	 * @return
	 */
	public boolean getACSStatus()
	{
		return acsStatus;
	}
	
	/**
	 * 返回ACSping时间
	 * @return
	 */
	public long getACSTime()
	{
		return time;
	}
	
	/**
	 * 获取采集点
	 * @return
	 */
	public String getGather_id()
	{
		return gather_id;
	}
	
	/**
	 * 更新acsping时间
	 * @param _time
	 */
	public void setACSTime(long _time)
	{
		time= _time;
	}
	
	/**
	 * 设置ACSIP
	 * @param _acsIP
	 */
	public void setACSIp(String _acsIP)
	{
		acsIp = _acsIP;
	}
	
	/**
	 * 设置ACS状态
	 * @param _acsStatus
	 */
	public void setACSStatus(boolean _acsStatus)
	{
		acsStatus = _acsStatus;
	}
	
	
	/**
	 * 设置采集点
	 * @param gatherID
	 */
	public void setGatherID(String gatherID)
	{
		gather_id=gatherID;
	}

}
