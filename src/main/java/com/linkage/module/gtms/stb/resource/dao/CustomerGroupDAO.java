package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.system.utils.StringUtils;


public class CustomerGroupDAO extends SuperDAO {

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(CustomerGroupDAO.class);

	/**
	 * 查询数据
	 * @param groupName
	 * @return
	 */
	public List<HashMap<String, String>> queryDataList(String groupName) {

		StringBuffer sql = new StringBuffer("select group_id, group_name, remark from stb_customer_group a where 1 = 1");

		if (!StringUtil.IsEmpty(groupName)) {
			sql.append(" and a.group_name like '%" + groupName + "%' ");
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());
		return DBOperation.getRecords(psql.getSQL());
	}

	/**
	 * 查询数据
	 * @param groupName
	 * @return
	 */
	public Map<String, String> queryData(String groupId) {

		StringBuffer sql = new StringBuffer("select group_id, group_name, remark from stb_customer_group a where 1 = 1");

		if (!StringUtil.IsEmpty(groupId)) {
			sql.append(" and a.group_id = '" + groupId + "' ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return DBOperation.getRecord(psql.getSQL());
	}

	/**
	 * 查询记录数
	 * @param cityId
	 * @param deviceIp
	 * @return
	 */
	public int queryCount(String groupId) {
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select count(*) from stb_customer_group a where 1=1 ");
		if (!StringUtil.IsEmpty(groupId)) {
			pSql.append(" and a.group_id = '" + groupId + "' ");
		}
		return jt.queryForInt(pSql.getSQL());
	}

	/**
	 *
	 * @param groupId
	 * @param groupName
	 * @param remark
	 * @param operator
	 * @return
	 */
	public int insertData(String groupId, String groupName, String remark, String operator) {
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL("insert into stb_customer_group (group_id, group_name, remark, operator, add_time, update_time) values(?,?,?,?,?,?)");
		psql.setString(1, groupId);
		psql.setString(2, groupName);
		psql.setString(3, remark);
		psql.setString(4, operator);
		psql.setLong(5, new DateTimeUtil().getLongTime());
		psql.setLong(6, new DateTimeUtil().getLongTime());

		return DBOperation.executeUpdate(psql.getSQL());
	}

	/**
	 *
	 * @param groupId
	 * @param groupName
	 * @param remark
	 * @param operator
	 * @return
	 */
	public int updateData(String groupId, String groupName, String remark, String operator) {
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL("update stb_customer_group set group_name=?, remark=?, operator=?, update_time=? where group_id=?");
		psql.setString(1, groupName);
		psql.setString(2, remark);
		psql.setString(3, operator);
		psql.setLong(4, new DateTimeUtil().getLongTime());
		psql.setString(5, groupId);

		return DBOperation.executeUpdate(psql.getSQL());
	}

	/**
	 *
	 * @param groupId
	 * @return
	 */
	public int deleteData(String groupId) {
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL("delete from stb_customer_group where group_id=?");
		psql.setString(1, groupId);

		return DBOperation.executeUpdate(psql.getSQL());
	}
}
