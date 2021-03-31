/**
 * 
 */

package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;


@SuppressWarnings("rawtypes")
public class DepartManageDAO extends SuperDAO {

	/**
	 * 查询
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param nameSearch
	 * @return
	 */
	public List<Map> queryDepartList(int curPage_splitPage, int num_splitPage, String startTime, String endTime, String nameSearch) {
		StringBuffer sb = new StringBuffer();
		
		// teledb
		if (DBUtil.GetDB() == 3) {
			sb.append(" select a.depart_id, a.depart_name, a.depart_time, a.depart_desc, a.acc_oid, b.acc_loginname from tab_department a left join ");
		}
		else {
			sb.append(" select a.*, b.acc_loginname  from tab_department a left join ");
		}
		sb.append(" tab_accounts b on a.acc_oid = b.acc_oid ");
		sb.append(" where 1 = 1");
		
		if (!StringUtil.IsEmpty(startTime)) {
			sb.append(" and a.depart_time >= ").append(new DateTimeUtil(startTime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endTime)) {
			sb.append(" and a.depart_time <= ").append(new DateTimeUtil(endTime).getLongTime());
		}
		if (!StringUtil.IsEmpty(nameSearch)) {
			sb.append(" and a.depart_name like").append("'%").append(nameSearch).append("%'");
		}
		
		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("depart_id", rs.getString("depart_id"));
				map.put("depart_name", rs.getString("depart_name"));
				try {
					long departtime = StringUtil.getLongValue(rs.getString("depart_time")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(departtime);
					map.put("depart_time", dt.getLongDate());
				}
				catch (NumberFormatException e) {
					map.put("depart_time", "");
				}
				catch (Exception e) {
					map.put("depart_time", "");
				}
				map.put("depart_desc", rs.getString("depart_desc"));
				map.put("acc_oid", rs.getString("acc_oid"));
				map.put("acc_loginname", rs.getString("acc_loginname"));
				return map;
			}
		});
		return list;
	}
	
	/**
	 * 查询总数
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param nameSearch
	 * @return
	 */
	public int getDepartListCount(int curPage_splitPage, int num_splitPage, String startTime, String endTime, String nameSearch) {
		StringBuffer sb = new StringBuffer();
		
		// teledb
		if (DBUtil.GetDB() == 3) {
			sb.append(" select count(*) from tab_department ");
		}
		else {
			sb.append(" select count(1) from tab_department ");
		}
		sb.append(" where 1 = 1");
		
		if (!StringUtil.IsEmpty(startTime)) {
			sb.append(" and depart_time >= ").append(new DateTimeUtil(startTime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endTime)) {
			sb.append(" and depart_time <= ").append(new DateTimeUtil(endTime).getLongTime());
		}
		if (!StringUtil.IsEmpty(nameSearch)) {
			sb.append(" and depart_name like").append("'%").append(nameSearch).append("%'");
		}
		PrepareSQL psql = new PrepareSQL(sb.toString());
		int total = jt.queryForInt(psql.getSQL());

		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		}
		else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	/**
	 * 增加部门
	 * @param deptname
	 * @param deptdesc
	 * @param acc_oid
	 * @return
	 */
	public int addDepartInfo(String deptname, String deptdesc, Long acc_oid) {
		String sql = "insert into tab_department(depart_id, depart_name, depart_time, depart_desc, acc_oid) "
					+ "values(?, ?, ?, ?, ?)";
	
		long max_id = DataSetBean.getMaxId("tab_department", "depart_id");
		PrepareSQL psql = new PrepareSQL(sql);
	
		psql.setLong(1, max_id);
		psql.setString(2, deptname);
		psql.setLong(3, new Date().getTime() / 1000);
		psql.setString(4, deptdesc);
		psql.setLong(5, acc_oid);
		
		return jt.update(psql.getSQL());
	}
	
	/**
	 * 查询相同部门
	 * @param deptname
	 * @return
	 */
	public int queryDepartName(String deptname, boolean isAdd, Long departid) {
		String sql = null;
		PrepareSQL psql = null;
		if (isAdd) {
			sql = "select count(1) from tab_department where depart_name = ?";
			psql = new PrepareSQL(sql);
			psql.setString(1, deptname);
		}
		else {
			sql = "select count(1) from tab_department where depart_name = ? and depart_id != ?";
			psql = new PrepareSQL(sql);
			psql.setString(1, deptname);
			psql.setLong(2, departid);
		}
		return jt.queryForInt(psql.getSQL());
	}

	/**
	 * 删除部门
	 * @param departid
	 * @return
	 */
	public int deleteDepart(Long departid) {
		String sql = "delete from tab_department where depart_id = ?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setLong(1, departid);
		return jt.update(psql.getSQL());
	}
	
	/**
	 * 修改部门
	 * @param deptname
	 * @param deptdesc
	 * @param acc_oid
	 * @param departid
	 * @return
	 */
	public int editDepartInfo(String deptname, String deptdesc, Long acc_oid, Long departid) {
		String sql = "update tab_department set depart_name = ?, depart_time = ?, depart_desc = ?, acc_oid = ? where depart_id = ? ";
	
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, deptname);
		psql.setLong(2, new Date().getTime() / 1000);
		psql.setString(3, deptdesc);
		psql.setLong(4, acc_oid);
		psql.setLong(5, departid);
		return jt.update(psql.getSQL());
	}
}
