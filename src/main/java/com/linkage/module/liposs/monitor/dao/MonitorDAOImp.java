/*
 * ASIAINFO-LINKAGE TECHNOLOGY (NANJING) CO.,LTD. Copyright 1996-2012,  All rights reserved.
 * 文件名     :MonitorDAOlmp.java
 * 创建人     :ZhangCong(zhangcong@asiainfo-linkage.com)
 * 创建日期:2015年12月2日
 */
package com.linkage.module.liposs.monitor.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.ColumnMapRowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.liposs.system.basesupport.BaseSupportDAO;
import com.linkage.system.utils.database.DBUtil;

import dao.util.SplitPageResultSetExtractor;

/**
 * @author ZhangCong(zhangcong@asiainfo-linkage.com)
 * @version [revision], 2015年12月2日
 * @sinse webapp
 */
@SuppressWarnings({ "rawtypes", "unchecked"})
public class MonitorDAOImp extends BaseSupportDAO implements MonitorDAO
{
    private static final Logger LOG = Logger.getLogger(MonitorDAOImp.class);

    @Override
    public List<Map> getAllMonitorHostList()
    {
        String sql = "select hostid,hostname,hostdesc";
        sql += " from tab_monitor_host order by hostid";
        LOG.info(sql);

        List<Map> list = jt.queryForList(sql);
        LOG.info(list);
        return list;
    }

    @Override
    public List<Map> getMonitorTypeListByHostID(String host_id)
    {
        String sql = "select hostid,typeid,typedesc,typevalve,typeperoid";
        sql += " from tab_monitor_host_type";
        sql += " where hostid = " + host_id;
        LOG.info(sql);

        List<Map> list = jt.queryForList(sql);
        LOG.info(list);
        return list;
    }

    @Override
    public Map getMonitorHostAndTypeByHostIDAndTypeID(String host_id, String type_id)
    {
        String sql = "select b.hostid,b.typeid,a.hostdesc,b.typedesc,b.typevalve,b.typeperoid";
        sql += " from tab_monitor_host a,tab_monitor_host_type b";
        sql += " where a.hostid = b.hostid and b.hostid = " + host_id + "and b.typeid = " + type_id;
        LOG.info(sql);

        List<Map> list = jt.queryForList(sql);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public List<Map> queryDataByConditions(Map<String, Object> queryConditions)
    {
        String whereSql = queryConditions.get("whereSql") + "";
        String tableName = queryConditions.get("tableName") + "";

        String sql = "select gathertime,monitorvalue";
        sql += " from " + tableName;
        sql += whereSql;
        LOG.warn(sql);
        return jt.queryForList(sql);
    }

	public Map getCurrActiveCount(String host_id, String type_id,String tableName)
	{
    	LOG.warn("DBUtil.tableIsExist(tableName, jt)=" + DBUtil.tableIsExist(tableName, jt));
    	if(DBUtil.tableIsExist(tableName, jt) == 0){
    		return null;
    	}
    	String sql = "select * from " +
				"(select gathertime, monitorvalue from " + tableName+ " where typeid = "+ type_id +
				" and hostid = " + host_id + " order by gathertime desc) where rownum = 1 ";
    	if(DBUtil.getDbType() == 1){
    		sql = "select top 1 gathertime, monitorvalue from " + tableName+ " where typeid = "+ type_id +
					" and hostid = " + host_id + " order by gathertime desc";
    	}
    	else if(DBUtil.getDbType() == 3)
    	{// mysql
			sql = "select gathertime, monitorvalue from " + tableName+ " where typeid = "+ type_id +
					" and hostid = " + host_id + " order by gathertime desc limit 1";
		}
    	try {
    		PrepareSQL psql = new PrepareSQL(sql);
			return jt.queryForMap(psql.getSQL());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }

	public Map getCurrWaitCount(String host_id,String parent_id,String tableName)
	{
    	if(DBUtil.tableIsExist(tableName, jt) == 0){
    		return null;
    	}
    	String sql = "select * from (select gathertime, monitorvalue from " + tableName+ " where hostid = "
    			+ host_id + " and parentid = " + parent_id + " order by gathertime desc) where rownum = 1 ";
    	if(DBUtil.getDbType() == 1){
    		sql = "select top 1 gathertime, monitorvalue from " + tableName+ " where hostid = " + host_id
    				+ " and parentid = " + parent_id + " order by gathertime desc";
    	}
    	else if(DBUtil.getDbType() == 3)
    	{// mysql
			sql = "select gathertime, monitorvalue from " + tableName+ " where hostid = " + host_id
					+ " and parentid = " + parent_id + " order by gathertime desc limit 1";
		}
    	try {
			PrepareSQL psql = new PrepareSQL(sql);
			return jt.queryForMap(psql.getSQL());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }

	public List<Map> getHostProgressType(String hostId)
	{
    	String sql = "select progressid, progresstype from tab_monitor_host_progress where hostid=" + hostId;
		PrepareSQL psql = new PrepareSQL(sql);
    	return jt.queryForList(psql.getSQL());
    }

    public Map getHostProgressType(String hostId,String progressId)
    {
    	String sql = "select progresstype from tab_monitor_host_progress where hostid="
    					+ hostId + " and progressid=" + progressId;
		PrepareSQL psql = new PrepareSQL(sql);
    	return jt.queryForMap(psql.getSQL());
    }

    public Map getHostName(String hostId)
    {
        String sql = "select hostid,hostname,hostdesc from tab_monitor_host where hostid=" + hostId;
		PrepareSQL psql = new PrepareSQL(sql);
        return jt.queryForMap(psql.getSQL());
    }

	public Map getCurrHostProgress(String hostId,String progressType,String tableName)
	{
    	if(DBUtil.tableIsExist(tableName, jt) == 0){
    		return null;
    	}
    	String sql = "select * from (select gathertime, monitorvalue from " + tableName+ " where hostid = " + hostId
    			+ " and typeid = " + progressType + " order by gathertime desc) where rownum = 1 ";
    	if(DBUtil.getDbType() == 1){
    		sql = "select top 1 gathertime, monitorvalue from " + tableName+ " where hostid = " + hostId
    				+ " and typeid = " + progressType + " order by gathertime desc";
    	}
    	else if(DBUtil.getDbType() == 3)
    	{// mysql
			sql = "select gathertime, monitorvalue from " + tableName+ " where hostid = " + hostId
					+ " and typeid = " + progressType + " order by gathertime desc limit 1";
		}
    	try {
			PrepareSQL psql = new PrepareSQL(sql);
			return jt.queryForMap(psql.getSQL());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }

    /**
     * 获取指定时间进程历史信息
     * @param hostId
     * @param progressType
     * @param start
     * @param end
     * @param tableNameList
     * @param curPageSplitPage
     * @param numSplitPage
     * @return
     */
	public List<Map> getProgressHistoryList(String hostId,String progressType,String start,String end,
			List<String> tableNameList,int curPageSplitPage,int numSplitPage)
	{
    	if(null == tableNameList || tableNameList.isEmpty()){
    		LOG.error("不存在需要查询的天表");
    		return null;
    	}
    	if(tableNameList.size() == 1){
    		if(DBUtil.tableIsExist(tableNameList.get(0), jt) == 0){
    			LOG.warn("表" + tableNameList.get(0) + "不存在");
        		return null;
        	}
    		String sql = "select gathertime, monitorvalue from " + tableNameList.get(0)+ " where hostid = "
    					+ hostId + " and typeid = " + progressType + "and gathertime >=" + start
    					+ "and gathertime <=" + end + " order by gathertime desc";
    		int startRow = (curPageSplitPage - 1) * numSplitPage + 1;
    		if(com.linkage.commons.db.DBUtil.GetDB() == Global.DB_MYSQL) {
    			if(startRow < 1) {
    				startRow = 1;
    			}
    			StringBuilder sb = new StringBuilder(sql);
    			sb.append(" limit ").append(startRow -1).append(",").append(numSplitPage);
    			sql = sb.toString();
    			startRow = 1;
    		}
    		try {
				PrepareSQL psql = new PrepareSQL(sql);
    			return (List) jt.query(psql.getSQL(), new SplitPageResultSetExtractor(new ColumnMapRowMapper(), startRow ,numSplitPage));
    		} catch (Exception e) {
    			e.printStackTrace();
    			return null;
    		}
    	}

    	List<Map> progressHistoryList = new ArrayList<Map>();
    	for(int index= tableNameList.size() -1;index>=0;index--){
    		if(DBUtil.tableIsExist(tableNameList.get(0), jt) == 0){
    			LOG.warn("表" + tableNameList.get(index) + "不存在");
    			break;
        	}
    		String sql = "";
    		if(index == 0){
    			sql = "select gathertime, monitorvalue from " + tableNameList.get(0)+ " where hostid = " + hostId
    					+ " and typeid = " + progressType + "and gathertime >=" + start + " order by gathertime desc";
    		}else if(index == tableNameList.size() -1){
    			sql = "select gathertime, monitorvalue from " + tableNameList.get(0)+ " where hostid = " + hostId
    					+ " and typeid = " + progressType + "and gathertime <=" + end + " order by gathertime desc";
    		}else{
    			sql = "select gathertime, monitorvalue from " + tableNameList.get(0)+ " where hostid = " + hostId
    					+ " and typeid = " + progressType + " order by gathertime desc";
    		}
    		try {
				PrepareSQL psql = new PrepareSQL(sql);
    			List<Map> progressList = jt.queryForList(psql.getSQL());
    			progressHistoryList.addAll(progressList);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}

    	if(progressHistoryList.size() <= numSplitPage){
    		return progressHistoryList;
    	}

    	List<Map> currPageHistoryList = new ArrayList<Map>();
    	for(int index = (curPageSplitPage - 1) * numSplitPage;index < curPageSplitPage * numSplitPage;index++){
    		currPageHistoryList.add(progressHistoryList.get(index));
    	}

		return currPageHistoryList;
    }

    /**
     * 获取指定时间进程信息总数
     * @param hostId
     * @param progressType
     * @param start
     * @param end
     * @param tableNameList
     * @return
     */
    public int getProgressHistoryCount(String hostId,String progressType,String start,String end,
    		List<String> tableNameList)
    {
    	if(null == tableNameList || tableNameList.isEmpty()){
    		LOG.error("不存在需要查询的天表");
    		return 0;
    	}
    	if(tableNameList.size() == 1){
    		if(DBUtil.tableIsExist(tableNameList.get(0), jt) == 0){
    			LOG.warn("表" + tableNameList.get(0) + "不存在");
        		return 0;
        	}
    		String sql = "select count(*) from " + tableNameList.get(0)+ " where hostid = " + hostId
    				+ " and typeid = " + progressType + "and gathertime >=" + start
    				+ "and gathertime <=" + end + " order by gathertime desc";
    		try {
				PrepareSQL psql = new PrepareSQL(sql);
    			return jt.queryForInt(psql.getSQL());
    		} catch (Exception e) {
    			e.printStackTrace();
    			return 0;
    		}
    	}

    	int count = 0;
    	for(int index= tableNameList.size() -1;index>=0;index--){
    		if(DBUtil.tableIsExist(tableNameList.get(0), jt) == 0){
    			LOG.warn("表" + tableNameList.get(index) + "不存在");
    			break;
        	}
    		String sql = "";
    		if(index == 0){
    			sql = "select count(*) from " + tableNameList.get(0)+ " where hostid = " + hostId
    					+ " and typeid = " + progressType + "and gathertime >=" + start + " order by gathertime desc";
    		}else if(index == tableNameList.size() -1){
    			sql = "select count(*) from " + tableNameList.get(0)+ " where hostid = " + hostId
    					+ " and typeid = " + progressType + "and gathertime <=" + end + " order by gathertime desc";
    		}else{
    			sql = "select count(*) from " + tableNameList.get(0)+ " where hostid = " + hostId
    					+ " and typeid = " + progressType + " order by gathertime desc";
    		}
    		try {
				PrepareSQL psql = new PrepareSQL(sql);
    			count += jt.queryForInt(psql.getSQL());
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	return count;
    }

	public Map getHostIpTime(String hostId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.* from (");
		psql.append("select host_time from tab_ntp_time ");
		psql.append("where host_id=? and host_time>0 order by host_time desc");
		psql.append(") a where rownum<2");
		psql.setInt(1,StringUtil.getIntegerValue(hostId));

		Map m=null;
		try{
			m=jt.queryForMap(psql.getSQL());
			return m;
		}catch(Exception e){
			return null;
		}
    }

	public List<Map> getHostIpLog(String hostId,String process_name)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select log from tab_server_log ");
		psql.append("where host_id=? and process_name=? order by id ");
		psql.setInt(1,StringUtil.getIntegerValue(hostId));
		psql.setString(2,process_name);
		return jt.queryForList(psql.getSQL());
    }



}
