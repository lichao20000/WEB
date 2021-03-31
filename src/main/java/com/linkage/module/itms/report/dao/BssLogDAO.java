package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;

public class BssLogDAO extends SuperDAO {

	private static Logger logger = LoggerFactory
			.getLogger(BssLogDAO.class);

	private void SQLAppend(String loid, String bussinessacount,
			String startOpenDate1, String operationuser, String bssaccount,
			StringBuffer sql) {
		if (!StringUtil.IsEmpty(loid)) {
			sql.append(" and c.username='").append(loid + "'");
		}
		if (!StringUtil.IsEmpty(bussinessacount)) {
			sql.append(" and c.serv_type_id=").append(bussinessacount);
		} else {
			sql.append(" and c.serv_type_id in(10,11,14)");
		}
		if (!StringUtil.IsEmpty(bssaccount)) {
			sql.append(" and c.serv_account='").append(bssaccount + "'");
		}
		if (!StringUtil.IsEmpty(startOpenDate1)) {
			sql.append(" and c.oper_time>=").append(startOpenDate1);
		}
		if (!StringUtil.IsEmpty(operationuser)) {
			sql.append(" and d.acc_loginname>='").append(operationuser + "'");
		}
	}

	@SuppressWarnings("rawtypes")
	public List<Map> bsslogList(String loid, String bussinessacount,
			String startOpenDate1, String operationuser, String bssaccount,
			int curPage_splitPage, int num_splitPage) 
	{
		logger.debug("IdsAlarmInfoDAO=>getIdsarmInfoList()");
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		sql.append("select c.username,c.serv_type_id,c.serv_account,c.oper_type,d.acc_loginname,c.occ_ip,c.oper_time ");
		sql.append("from tab_hgwcustomer a,tab_handsheet_log c,tab_accounts d ");
		sql.append("where a.username=c.username and c.occ_id=d.acc_oid ");
		SQLAppend(loid, bussinessacount, startOpenDate1, operationuser,
				bssaccount, sql);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", rs.getString("username"));
				String serv_type_id = rs.getString("serv_type_id");
				if ("10".equals(serv_type_id)) {
					serv_type_id = "宽带";
				} else if ("11".equals(serv_type_id)) {
					serv_type_id = "IPTV";
				} else if ("14".equals(serv_type_id)) {
					serv_type_id = "VOIP";
				}
				map.put("serv_type_id", serv_type_id);
				map.put("serv_account", rs.getString("serv_account"));
				String oper_type = rs.getString("oper_type");
				if ("1".equals(oper_type)) {
					oper_type = "开户";
				} else if ("2".equals(oper_type)) {
					oper_type = "暂停";
				} else if ("3".equals(oper_type)) {
					oper_type = "销户";
				}
				map.put("oper_type", oper_type);
				map.put("acc_loginname", rs.getString("acc_loginname"));
				map.put("occ_ip", rs.getString("occ_ip"));
				try {
					long oper_time = StringUtil.getLongValue(rs
							.getString("oper_time"));
					DateTimeUtil dt = new DateTimeUtil(oper_time * 1000);
					map.put("oper_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("oper_time", "");
				} catch (Exception e) {
					map.put("oper_time", "");
				}
				return map;
			}
		});
		return list;
	}

	public int countbsslogList(String loid, String bussinessacount,
			String startOpenDate1, String operationuser, String bssaccount,
			int curPage_splitPage, int num_splitPage) 
	{
		logger.debug("IdsAlarmInfoDAO=>getIdsarmInfoList()");
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_hgwcustomer a,tab_handsheet_log c,tab_accounts d ");
		sql.append("where a.username = c.username and c.occ_id=d.acc_oid ");
		SQLAppend(loid, bussinessacount, startOpenDate1, operationuser,
				bssaccount, sql);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> getcountbsslogExcel(String loid, String bussinessacount,
			String startOpenDate1, String operationuser, String bssaccount) 
	{
		logger.info("IdsAlarmInfoDAO=>getIdsarmInfoListExcel()");
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		sql.append("select c.username,c.serv_type_id,c.serv_account,c.oper_type,d.acc_loginname,c.occ_ip,c.oper_time "
				+ "from tab_hgwcustomer a,tab_handsheet_log c,tab_accounts d  where a.username = c.username and c.occ_id=d.acc_oid ");
		SQLAppend(loid, bussinessacount, startOpenDate1, operationuser,
				bssaccount, sql);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("username",
						StringUtil.getStringValue(list.get(i).get("username")));
				String serv_type_id = StringUtil.getStringValue(list.get(i)
						.get("serv_type_id"));
				if ("10".equals(serv_type_id)) {
					serv_type_id = "宽带";
				} else if ("11".equals(serv_type_id)) {
					serv_type_id = "IPTV";
				} else if ("14".equals(serv_type_id)) {
					serv_type_id = "VOIP";
				}
				list.get(i).put("serv_type_id", serv_type_id);
				list.get(i).put(
						"serv_account",
						StringUtil.getStringValue(list.get(i).get(
								"serv_account")));
				String oper_type = StringUtil.getStringValue(list.get(i).get(
						"oper_type"));
				if ("1".equals(oper_type)) {
					oper_type = "开户";
				} else if ("2".equals(oper_type)) {
					oper_type = "暂停";
				} else if ("3".equals(oper_type)) {
					oper_type = "销户";
				}
				list.get(i).put("oper_type", oper_type);
				list.get(i).put(
						"acc_loginname",
						StringUtil.getStringValue(list.get(i).get(
								"acc_loginname")));
				list.get(i).put("occ_ip",
						StringUtil.getStringValue(list.get(i).get("occ_ip")));
				try {
					long oper_time = StringUtil.getLongValue(list.get(i).get(
							"oper_time")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(oper_time);
					list.get(i).put("oper_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					list.get(i).put("oper_time", "");
				} catch (Exception e) {
					list.get(i).put("oper_time", "");
				}
			}
		}
		return list;
	}

}
