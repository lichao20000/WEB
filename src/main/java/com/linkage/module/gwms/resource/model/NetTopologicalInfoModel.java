
package com.linkage.module.gwms.resource.model;

import java.util.List;

/**
 * 获取家庭网络拓扑接口返回模型
 * 
 * @author yages
 */
public class NetTopologicalInfoModel
{

	private int status;
	private int Result;
	private GwInfoModel gw_info; // 光猫信息
	private List<TopoInfoModel> topo_info; // 拓扑信息
	private String FailReason; // FailReason
	private long timestamp; // 时间戳

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getResult()
	{
		return Result;
	}

	public void setResult(int result)
	{
		Result = result;
	}

	public GwInfoModel getGw_info()
	{
		return gw_info;
	}

	public void setGw_info(GwInfoModel gw_info)
	{
		this.gw_info = gw_info;
	}

	public List<TopoInfoModel> getTopo_info()
	{
		return topo_info;
	}

	public void setTopo_info(List<TopoInfoModel> topo_info)
	{
		this.topo_info = topo_info;
	}

	public String getFailReason()
	{
		return FailReason;
	}

	public void setFailReason(String failReason)
	{
		FailReason = failReason;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}
}
