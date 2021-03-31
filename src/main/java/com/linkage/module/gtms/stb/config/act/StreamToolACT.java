
package com.linkage.module.gtms.stb.config.act;

import com.ailk.tr069.devrpc.dao.corba.AcsCorbaDAO;
import com.linkage.module.gtms.stb.config.bio.StreamToolBIO;
import com.linkage.module.gwms.Global;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamToolACT
{

	private static Logger logger = LoggerFactory.getLogger(StreamToolACT.class);
	private StreamToolBIO streamToolBIO;
	private String deviceId;
	private List streamList;
	private String gw_type = Global.GW_TYPE_STB;
	private String ajax;

	public String openStream()
	{
		logger.debug("openStream()");
		int r = new AcsCorbaDAO(Global.getPrefixName(gw_type)
				+ Global.SYSTEM_ACS).chgInfo(1, 1, this.deviceId);
		logger.warn("-------------" + r);
		if (1 == r)
			this.ajax = "开启成功";
		else
			this.ajax = "开启失败";
		logger.warn("-------------" + this.ajax);
		return "ajax";
	}

	public String closeStream()
	{
		logger.debug("closeStream()");
		int r = new AcsCorbaDAO(Global.getPrefixName(gw_type)
				+ Global.SYSTEM_ACS).chgInfo(0, 1, this.deviceId);
		if (1 == r)
			this.ajax = "关闭成功";
		else
			this.ajax = "关闭失败";
		return "ajax";
	}

	public String clearStream()
	{
		logger.debug("clearStream()");
		int r = this.streamToolBIO.clearAcsStream(this.deviceId);
		if (r >= 0)
			this.ajax = "清除成功";
		else
			this.ajax = "清除失败";
		return "ajax";
	}

	public String showInterStream()
	{
		logger.debug("showInterStream()");
		this.streamList = this.streamToolBIO.getAcsStream(this.deviceId);
		return "showStream";
	}

	public String getDeviceId()
	{
		return this.deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public List getStreamList()
	{
		return this.streamList;
	}

	public void setStreamList(List streamList)
	{
		this.streamList = streamList;
	}

	public String getAjax()
	{
		return this.ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public StreamToolBIO getStreamToolBIO()
	{
		return this.streamToolBIO;
	}

	public void setStreamToolBIO(StreamToolBIO streamToolBIO)
	{
		this.streamToolBIO = streamToolBIO;
	}
}