package com.linkage.module.gtms.stb.resource.dao;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * wanghong5
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class StbEServerQueryDao extends SuperDAO
{
	private int queryCount;


	/**
	 * 查询工单信息
	 */
	public List<Map<String, String>> queryEServerList(int curPage_splitPage,
			int num_splitPage, String deviceMac,String servAccount,
			String grid,String opertor,long startTime, long endTime)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.bss_sheet_id,a.receive_date,a.username,a.result,a.sheet_context,");
		psql.append("a.returnt_context,a.type,b.from_id,b.mac,b.grid,b.opertor ");
		psql.append(getSql(deviceMac,servAccount,grid,opertor,startTime,endTime));
		psql.append("order by a.receive_date ");

		//导出列表，查询所有数据
		if(curPage_splitPage==-1 && num_splitPage==-1)
		{
			return jt.queryForList(psql.getSQL());
		}

		//页面分页展示
		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage+1,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("bss_sheet_id", rs.getString("bss_sheet_id"));
						map.put("receive_date", rs.getString("receive_date"));
						map.put("from_id",rs.getString("from_id"));
						map.put("username",rs.getString("username"));
						map.put("mac", rs.getString("mac"));
						map.put("sheet_context",rs.getString("sheet_context"));
						map.put("type",rs.getString("type"));
						map.put("grid", rs.getString("grid"));
						map.put("opertor",rs.getString("opertor"));
						map.put("result",rs.getString("result"));
						map.put("returnt_context",rs.getString("returnt_context"));

						return map;
					}
				});
		return list;
	}

	/**
	 * 获取总数并分页
	 */
	public int countEServerList(int num_splitPage,String deviceMac,String servAccount,
			String grid,String opertor,long startTime,long endTime)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) count_num ");
		psql.append(getSql(deviceMac,servAccount,grid,opertor,startTime,endTime));
		queryCount = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (queryCount % num_splitPage == 0){
			maxPage = queryCount / num_splitPage;
		}else{
			maxPage = queryCount / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 查询工单详细
	 */
	public Map<String, String> queryEServerInfo(String sheet_id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.username,a.returnt_context,a.city_id,");
		psql.append("a.receive_date,a.type,a.sheet_context,");
		psql.append("a.bss_sheet_id,b.from_id,b.mac,b.grid,b.opertor ");
		psql.append("from tab_bss_sheet a left join tab_eserver4ws b ");
		psql.append("on a.bss_sheet_id=b.sheet_id ");
		psql.append("where a.bss_sheet_id=? ");
		psql.setString(1,sheet_id);

		return jt.queryForMap(psql.getSQL());
	}

	/**
	 * 拼接sql
	 */
	private String getSql(String deviceMac,String servAccount,
			String grid,String opertor,long startTime,long endTime)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("from tab_bss_sheet a left join tab_eserver4ws b ");
		sb.append("on a.bss_sheet_id=b.sheet_id ");
		sb.append("where 1=1 ");

		if(!StringUtil.IsEmpty(servAccount)){
			sb.append("and a.username='"+servAccount+"' ");
		}
		if(startTime>0){
			sb.append("and a.receive_date>="+startTime+" ");
		}
		if(endTime>0){
			sb.append("and a.receive_date<"+endTime+" ");
		}
		if(!StringUtil.IsEmpty(deviceMac)){
			sb.append("and b.mac='"+deviceMac+"' ");
		}
		if(!StringUtil.IsEmpty(grid)){
			sb.append("and b.grid='"+grid+"' ");
		}
		if(!StringUtil.IsEmpty(opertor)){
			sb.append("and b.opertor='"+opertor+"' ");
		}

		return sb.toString();
	}

	public List<Map<String, String>> query(int curPage_splitPage, int num_splitPage, String mac, String servAccount, String status, long startTime, long endTime) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.cmd_id, a.mac, a.serv_account, a.acc_address, a.grid, a.opertor, a.status, a.oper_log, a.add_time, a.oper_time " +
				"from tab_bind_change_record a where 1=1 ");
		if(!StringUtil.IsEmpty(servAccount)){
			psql.append(" and a.serv_account='"+servAccount+"' ");
		}
		if(startTime>0){
			psql.append(" and a.add_time>="+startTime+" ");
		}
		if(endTime>0){
			psql.append(" and a.add_time<"+endTime+" ");
		}
		if(!StringUtil.IsEmpty(mac)){
			psql.append(" and a.mac='"+mac+"' ");
		}
		if(!StringUtil.IsEmpty(status)){
			psql.append(" and a.status='"+status+"' ");
		}
		psql.append(" order by  add_time desc");
		//页面分页展示
		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage+1,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						String cmdId = rs.getString("cmd_id");
						map.put("cmd_id", cmdId);
						if(cmdId.startsWith("FromGK"))
						{
							map.put("from_id", "FromGK");
						}
						map.put("mac", rs.getString("mac"));
						map.put("serv_account", rs.getString("serv_account"));
						map.put("acc_address", rs.getString("acc_address"));
						map.put("grid", rs.getString("grid"));
						map.put("opertor", rs.getString("opertor"));

						String status = rs.getString("status");
						map.put("status",status);
						if("0".equals(status))
						{
							map.put("status_desc","未审批");
						}
						else if("1".equals(status))
						{
							map.put("status_desc","通过");
						}
						else
						{
							map.put("status_desc","拒绝");
						}
						map.put("oper_log", rs.getString("oper_log"));

						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						long addTime = rs.getLong("add_time");
						map.put("add_time", df.format(addTime * 1000));
						long operTime = rs.getLong("oper_time");
						map.put("oper_time", df.format(operTime * 1000));

						return map;
					}
				});
		return list;
	}

	public String getGroupOid(long acc_oid)
	{
		String groupOid = "";
		PrepareSQL psql1 = new PrepareSQL();
		psql1.append("select group_oid from tab_acc_group where acc_oid = ? ");
		psql1.setLong(1,acc_oid);
		Map groupOidMap = DBOperation.getRecord(psql1.getSQL());
		if(null != groupOidMap && groupOidMap.size() > 0)
		{
			groupOid = StringUtil.getStringValue(groupOidMap.get("group_oid"));
		}
		return groupOid;
	}


	public int count(int num_splitPage, String mac, String servAccount, String status, long startTime, long endTime) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) from tab_bind_change_record a where 1=1 ");
		if(!StringUtil.IsEmpty(servAccount)){
			psql.append(" and a.serv_account='"+servAccount+"' ");
		}
		if(startTime>0){
			psql.append(" and a.add_time>="+startTime+" ");
		}
		if(endTime>0){
			psql.append(" and a.add_time<"+endTime+" ");
		}
		if(!StringUtil.IsEmpty(mac)){
			psql.append(" and a.mac='"+mac+"' ");
		}
		if(!StringUtil.IsEmpty(status)){
			psql.append(" and a.status='"+status+"' ");
		}

		queryCount = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (queryCount % num_splitPage == 0){
			maxPage = queryCount / num_splitPage;
		}else{
			maxPage = queryCount / num_splitPage + 1;
		}
		return maxPage;
	}

	public Map<String, String> queryDetail(String sheetId) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.cmd_id, a.mac, a.serv_account, a.acc_address, a.grid, a.opertor, a.status, a.oper_log, a.add_time, a.oper_time " +
				"from tab_bind_change_record a where cmd_id = ? ");
		psql.setString(1,sheetId);

		Map<String, String> map = new HashMap<String, String>();
		map  = jt.queryForMap(psql.getSQL());
		String cmdId = map.get("cmd_id");
		map.put("cmd_id", cmdId);
		if(cmdId.startsWith("FromGK"))
		{
			map.put("from_id", "FromGK");
		}
		map.put("mac", map.get("mac"));
		map.put("serv_account", map.get("serv_account"));
		map.put("acc_address", map.get("acc_address"));
		map.put("grid", map.get("grid"));
		map.put("opertor", map.get("opertor"));

		String status = StringUtil.getStringValue(map.get("status"));
		map.put("status",status);
		if("0".equals(status))
		{
			map.put("status_desc","未审批");
		}
		else if("1".equals(status))
		{
			map.put("status_desc","通过");
		}
		else
		{
			map.put("status_desc","拒绝");
		}
		String oper_log = StringUtil.getStringValue(map.get("oper_log"));
		oper_log = oper_log.replace(";","<br/>");
		map.put("oper_log",oper_log);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long addTime = StringUtil.getLongValue(map.get("add_time"));
		map.put("add_time", df.format(addTime * 1000));
		long operTime = StringUtil.getLongValue(map.get("oper_time"));
		map.put("oper_time", df.format(operTime * 1000));
		return map;
	}


	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}


	public String updateStbBindAccChgRecord(String sheetId,String status) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("update tab_bind_change_record set status = ?,oper_time = ? where cmd_id = ? ");
		psql.setString(1,status);
		psql.setLong(2,System.currentTimeMillis()/1000);
		psql.setString(3,sheetId);
		int result = DBOperation.executeUpdate(psql.getSQL());
		if(result == 1)
		{
			return "操作成功";
		}
		return "操作失败";
	}

	public Map getOldServAccInfo(String mac) {

		PrepareSQL psql1 = new PrepareSQL();
		psql1.append("select serv_account,city_id from stb_tab_customer where cpe_mac = ? ");
		psql1.setString(1,mac);
		Map result = DBOperation.getRecord(psql1.getSQL());

		return result;
	}

	public Map getCity(String servaccount) {
		PrepareSQL psql1 = new PrepareSQL();
		String regex = "(.{2})";
		psql1.append("select city_id from stb_tab_customer where serv_account = ? order by updatetime desc ");
		psql1.setString(1,servaccount);

		return DBOperation.getRecord(psql1.getSQL());
	}

	public void updateStbBindAccChgRecordOperLog(String sheetId, String result) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("update tab_bind_change_record set oper_log = ? ,oper_time = ? where cmd_id = ? ");
		psql.setString(1,result);
		psql.setLong(2,System.currentTimeMillis()/1000);
		psql.setString(3,sheetId);
		DBOperation.executeUpdate(psql.getSQL());
	}

    public boolean isbind(String mac) {
		String sql = "select serv_account from stb_tab_customer where cpe_mac=? ";
		PrepareSQL pSQL = new PrepareSQL(sql);
		pSQL.setString(1, mac);
		Map map = DBOperation.getRecord(pSQL.getSQL());
		if(null != map && !map.isEmpty())
		{
			return true;
		}
		return false;
	}
}
