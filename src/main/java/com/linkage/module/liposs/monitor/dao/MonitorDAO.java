/*
 * ASIAINFO-LINKAGE TECHNOLOGY (NANJING) CO.,LTD. Copyright 1996-2012,  All rights reserved.
 * 文件名     :MonitorDAO.java
 * 创建人     :ZhangCong(zhangcong@asiainfo-linkage.com)
 * 创建日期:2015年12月2日
 */
package com.linkage.module.liposs.monitor.dao;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * [一句话功能简述]
 * </p>
 * <p>
 * [功能详细描述]
 * </p>
 * @author ZhangCong(zhangcong@asiainfo-linkage.com)
 * @version [revision], 2015年12月2日
 * @sinse webapp
 */
@SuppressWarnings("rawtypes")
public interface MonitorDAO
{
    public List<Map> getAllMonitorHostList();
    
    public List<Map> getMonitorTypeListByHostID(String host_id);
    
    public List<Map> queryDataByConditions(Map<String, Object> queryConditions);
    
    public Map getMonitorHostAndTypeByHostIDAndTypeID(String host_id,String type_id);
    
    public Map getCurrActiveCount(String host_id, String type_id,String tableName);
    
    public Map getCurrWaitCount(String host_id,String parent_id,String tableName);

	public List<Map> getHostProgressType(String hostId);

	public Map getCurrHostProgress(String hostId, String progressType,
			String tabMonitorProgress);

	public List<Map> getProgressHistoryList(String hostId, String progressType,
			String start, String end, List<String> tableNameList,
			int curPageSplitPage, int numSplitPage);

	public int getProgressHistoryCount(String hostId, String progressType,
			String start, String end, List<String> tableNameList);

	public Map getHostName(String hostId);

	public Map getHostProgressType(String hostId, String progressType);
	
	public Map getHostIpTime(String hostId);
	
	public List<Map> getHostIpLog(String hostId,String process_name);
}
