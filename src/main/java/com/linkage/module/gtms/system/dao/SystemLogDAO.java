
package com.linkage.module.gtms.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.system.obj.LogOBJ;
import com.linkage.module.gtms.system.serv.LogManageServ;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author zhaixf(63412)
 * @version 1.0
 * @since 2010-12-6 下午08:10:16
 * @category com.linkage.module.ims.system.dao<br>
 * @copyright 南京联创科技 网管科技部
 */
public class SystemLogDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(SystemLogDAO.class);

	/**
	 * 查询日志的SQL语句
	 *
	 * @param logObj
	 * @return String SQL
	 */
	public String querySystemLogSql(LogOBJ logObj, boolean order)
	{
		logger.debug("querySystemLogSql({})", logObj);
		StringBuffer bufSql = new StringBuffer(// TODO wait (more table related)
				"select tr.log_time,tr.acc_oid,tr.host_name,tr.log_ip,tr.item_id,tr.oper_type_id,tr.oper_cont,tr.acc_loginname,"
						+ "tar.role_name,tc.city_name from tr_web_oper_log tr,tab_persons ta,tab_city tc,tab_role tar where 1=1 and tr.acc_oid = ta.per_acc_oid"
						+ " and ta.per_city = tc.city_id and tr.acc_oid = tar.acc_oid ");
		if (false == StringUtil.IsEmpty(logObj.getOperContent()))
		{
			bufSql.append(" and tr.oper_cont like '%").append(logObj.getOperContent())
					.append("%'");
		}
		if (false == StringUtil.IsEmpty(logObj.getIpAddr()))
		{
			bufSql.append(" and tr.log_ip = '").append(logObj.getIpAddr()).append("'");
		}
		if (false == StringUtil.IsEmpty(logObj.getAccount()))
		{
			bufSql.append(" and tr.acc_loginname like '%").append(logObj.getAccount())
					.append("%'");
		}
		if (false == StringUtil.IsEmpty(logObj.getHostname()))
		{
			bufSql.append(" and tr.host_name = '").append(logObj.getHostname())
					.append("'");
		}
		if (false == StringUtil.IsEmpty(logObj.getItemSelect()))
		{
			bufSql.append(" and tr.item_id ='").append(logObj.getItemSelect())
					.append("'");
		}
		if (false == StringUtil.IsEmpty(logObj.getOperType()))
		{
			bufSql.append(" and tr.oper_type_id =").append(
					StringUtil.getIntegerValue(logObj.getOperType()));
		}
		if (false == StringUtil.IsEmpty(logObj.getStarttime()))
		{
			bufSql.append(" and tr.log_time >= ").append(
					new DateTimeUtil(logObj.getStarttime()).getLongTime());
		}
		if (false == StringUtil.IsEmpty(logObj.getEndtime()))
		{
			bufSql.append(" and tr.log_time < ").append(
					new DateTimeUtil(logObj.getEndtime()).getLongTime());
		}
		if (null != logObj.getArea_id() && !"".equals(logObj.getArea_id().trim()))
		{// TODO wait (more table related)
			bufSql.append(" and tr.acc_loginname in (select a.acc_loginname from tab_acc_area aa,tab_accounts a,tab_area ar where 1=1 and aa.acc_oid = a.acc_oid and aa.area_id = ar.area_id and ar.area_id = ");
			bufSql.append(logObj.getArea_id().trim());
			bufSql.append(") ");
		}
		if (null != logObj.getRole_id() && !"".equals(logObj.getRole_id().trim()))
		{// TODO wait (more table related)
			bufSql.append(" and tr.acc_loginname in (select a.acc_loginname from tab_accounts a,tab_acc_role ar,tab_role r where r.role_id=ar.role_id and ar.acc_oid=a.acc_oid and r.role_id=");
			bufSql.append(logObj.getRole_id().trim());
			bufSql.append(") ");
		}
		if (order)
		{
			bufSql.append(" order by tr.log_time desc");
		}
		PrepareSQL psql = new PrepareSQL(bufSql.toString());
		return psql.toString();
	}

	/**
	 * 查询系统日志记录列表
	 *
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param logObj
	 * @return
	 */
	public List querySystemLogList(int curPage_splitPage, int num_splitPage, LogOBJ logObj)
	{
		logger.debug("querySystemLogList({}, {}, {})", new Object[] { curPage_splitPage,
				num_splitPage, logObj });
		if (null == logObj)
		{
			logObj = new LogOBJ();
		}
		List<Map> list = querySP(querySystemLogSql(logObj, true), (curPage_splitPage - 1)
				* num_splitPage, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				// LOG_TIME,ACC_OID,HOST_NAME,LOG_IP,ITEM_ID,OPER_TYPE_ID,OPER_CONT,ACC_LOGINNAME
				Map<String, String> map = new HashMap<String, String>();
				map.put("logtime", rs.getString("log_time"));
				map.put("account", rs.getString("acc_loginname"));
				map.put("hostname", rs.getString("host_name"));
				map.put("ipAddr", rs.getString("log_ip"));
				map.put("item", rs.getString("item_id"));
				map.put("operType",
						LogManageServ.LogOperMap.get(rs.getString("oper_type_id")));
				map.put("operContent", rs.getString("oper_cont"));
				map.put("acc_oid", rs.getString("acc_oid"));
				map.put("city_name", rs.getString("city_name"));
				map.put("role_name", rs.getString("role_name"));
				return map;
			}
		});
		return list;
	}

	/**
	 * 查询日志的总条数
	 *
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param logObj
	 * @return
	 */
	public int querySystemLogCount(int num_splitPage, LogOBJ logObj)
	{
		logger.debug("querySystemLogCount({}, {})", num_splitPage, logObj);
		if (null == logObj)
		{
			logger.warn("logObj is null");
			logObj = new LogOBJ();
		}
		String querySql = querySystemLogSql(logObj, false);
		querySql = querySql.substring(querySql.indexOf("from"));
		String countSql = "select count(*) as total ";
		int total = jt.queryForInt(new PrepareSQL(countSql + querySql).getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 导出用户使用菜单情况
	 *
	 * @param logObj
	 * @return
	 */
	public List<Map> excelLog(LogOBJ logObj)
	{
		logger.info("excelLog");
		if (null == logObj)
		{
			logger.warn("logObj is null");
			logObj = new LogOBJ();
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select acc_loginname,oper_cont,count(*) as usedCount from tr_web_oper_log where 1=1 ");
		if (null != logObj.getAccount() && !"".equals(logObj.getAccount().trim()))
		{
			sql.append(" and acc_loginname like '%").append(logObj.getAccount().trim())
					.append("%' ");
		}
		if (null != logObj.getItemSelect() && !"".equals(logObj.getItemSelect().trim()))
		{
			sql.append(" and item_id='").append(logObj.getItemSelect().trim())
					.append("' ");
		}
		if (null != logObj.getIpAddr() && !"".equals(logObj.getIpAddr().trim()))
		{
			sql.append(" and log_ip = '").append(logObj.getIpAddr().trim()).append("'");
		}
		if (null != logObj.getHostname() && !"".equals(logObj.getHostname().trim()))
		{
			sql.append(" and host_name = '").append(logObj.getHostname().trim())
					.append("'");
		}
		if (null != logObj.getStarttime() && !"".equals(logObj.getStarttime().trim()))
		{
			sql.append(" and log_time >= ").append(
					new DateTimeUtil(logObj.getStarttime().trim()).getLongTime());
		}
		if (null != logObj.getEndtime() && !"".equals(logObj.getEndtime().trim()))
		{
			sql.append(" and log_time < ").append(
					new DateTimeUtil(logObj.getEndtime().trim()).getLongTime());
		}
		if (null != logObj.getArea_id() && !"".equals(logObj.getArea_id().trim()))
		{// TODO wait (more table related)
			sql.append(" and acc_loginname in (select a.acc_loginname from tab_acc_area aa,tab_accounts a,tab_area ar where 1=1 and aa.acc_oid = a.acc_oid and aa.area_id = ar.area_id and ar.area_id = ");
			sql.append(logObj.getArea_id().trim());
			sql.append(") ");
		}
		if (null != logObj.getRole_id() && !"".equals(logObj.getRole_id().trim()))
		{// TODO wait (more table related)
			sql.append(" and acc_loginname in (select a.acc_loginname from tab_accounts a,tab_acc_role ar,tab_role r where r.role_id=ar.role_id and ar.acc_oid=a.acc_oid and r.role_id=");
			sql.append(logObj.getRole_id().trim());
			sql.append(") ");
		}
		sql.append(" group by oper_cont,acc_loginname");
		logger.warn(sql.toString());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> tempList = jt.queryForList(psql.getSQL());
		Map<String, String> userArea = getUserArea();
		Map<String, String> userRole = getUserRole();
		for (Map m : tempList)
		{
			m.put("area_name", userArea.get(m.get("acc_loginname")) == null ? ""
					: userArea.get(m.get("acc_loginname")));
			m.put("role_name", userRole.get(m.get("acc_loginname")) == null ? ""
					: userRole.get(m.get("acc_loginname")));
		}
		return tempList;
	}

	/**
	 * 统计使用的sql
	 *
	 * @param logObj
	 * @param order
	 * @return
	 */
	private String querySASLogSql(LogOBJ logObj, boolean order)
	{
		logger.debug("querySASLogSql({})", logObj);
		StringBuffer bufSql = new StringBuffer(
				"select acc_loginname,oper_cont,count(*) as usedCount from tr_web_oper_log where 1=1 ");
		if (false == StringUtil.IsEmpty(logObj.getOperContent()))
		{
			bufSql.append(" and oper_cont like '%").append(logObj.getOperContent())
					.append("%'");
		}
		if (false == StringUtil.IsEmpty(logObj.getIpAddr()))
		{
			bufSql.append(" and log_ip = '").append(logObj.getIpAddr()).append("'");
		}
		if (false == StringUtil.IsEmpty(logObj.getAccount()))
		{
			bufSql.append(" and acc_loginname like '%").append(logObj.getAccount())
					.append("%'");
		}
		if (false == StringUtil.IsEmpty(logObj.getHostname()))
		{
			bufSql.append(" and host_name = '").append(logObj.getHostname()).append("'");
		}
		if (false == StringUtil.IsEmpty(logObj.getItemSelect()))
		{
			bufSql.append(" and item_id ='").append(logObj.getItemSelect()).append("'");
		}
		if (false == StringUtil.IsEmpty(logObj.getOperType()))
		{
			bufSql.append(" and oper_type_id =").append(
					StringUtil.getIntegerValue(logObj.getOperType()));
		}
		if (false == StringUtil.IsEmpty(logObj.getStarttime()))
		{
			bufSql.append(" and log_time >= ").append(
					new DateTimeUtil(logObj.getStarttime()).getLongTime());
		}
		if (false == StringUtil.IsEmpty(logObj.getEndtime()))
		{
			bufSql.append(" and log_time < ").append(
					new DateTimeUtil(logObj.getEndtime()).getLongTime());
		}
		if (null != logObj.getArea_id() && !"".equals(logObj.getArea_id().trim()))
		{// TODO wait (more table related)
			bufSql.append(" and acc_loginname in (select a.acc_loginname from tab_acc_area aa,tab_accounts a,tab_area ar where 1=1 and aa.acc_oid = a.acc_oid and aa.area_id = ar.area_id and ar.area_id = ");
			bufSql.append(logObj.getArea_id().trim());
			bufSql.append(") ");
		}
		if (null != logObj.getRole_id() && !"".equals(logObj.getRole_id().trim()))
		{// TODO wait (more table related)
			bufSql.append(" and acc_loginname in (select a.acc_loginname from tab_accounts a,tab_acc_role ar,tab_role r where r.role_id=ar.role_id and ar.acc_oid=a.acc_oid and r.role_id=");
			bufSql.append(logObj.getRole_id().trim());
			bufSql.append(") ");
		}
		bufSql.append(" group by oper_cont,acc_loginname");
		if (order)
		{
			bufSql.append(" order by usedCount desc");
		}
		PrepareSQL psql = new PrepareSQL(bufSql.toString());
		return psql.toString();
	}

	/**
	 * 统计用户使用情况
	 *
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param logObj
	 * @return
	 */
	public List queryLogSAS(int curPage_splitPage, int num_splitPage, LogOBJ logObj)
	{
		logger.debug("queryLogSAS({}, {}, {})", new Object[] { curPage_splitPage,
				num_splitPage, logObj });
		if (null == logObj)
		{
			logger.warn("logObj is null");
			logObj = new LogOBJ();
		}
		Map<String, String> userArea = getUserArea();
		Map<String, String> userRole = getUserRole();
		List<Map> list = querySP(querySASLogSql(logObj, true), (curPage_splitPage - 1)
				* num_splitPage, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				// LOG_TIME,ACC_OID,HOST_NAME,LOG_IP,ITEM_ID,OPER_TYPE_ID,OPER_CONT,ACC_LOGINNAME
				Map<String, String> map = new HashMap<String, String>();
				map.put("acc_loginname", rs.getString("acc_loginname"));
				map.put("oper_cont", rs.getString("oper_cont"));
				map.put("usedCount", rs.getString("usedCount"));
				return map;
			}
		});
		for (Map m : list)
		{
			m.put("area_name", userArea.get(m.get("acc_loginname")) == null ? ""
					: userArea.get(m.get("acc_loginname")));
			m.put("role_name", userRole.get(m.get("acc_loginname")) == null ? ""
					: userRole.get(m.get("acc_loginname")));
		}
		return list;
	}

	/**
	 * 统计用户使用情况的总记录
	 *
	 * @param num_splitPage
	 * @param logObj
	 * @return
	 */
	public int querySASLogCount(int num_splitPage, LogOBJ logObj)
	{
		logger.debug("querySASLogCount({}, {})", num_splitPage, logObj);
		if (null == logObj)
		{
			logger.warn("logObj is null");
			logObj = new LogOBJ();
		}
		String querySql = querySASLogSql(logObj, false);
		querySql = querySql.substring(querySql.indexOf("from"));
		String countSql = "select count(*) as total ";
		int total = 0;
		try
		{
			List tempList = jt.queryForList(new PrepareSQL(countSql + querySql).getSQL());
			if (null != tempList && tempList.size() > 0)
			{
				total = tempList.size();
			}
		}
		catch (DataAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	private Map<String, String> getUserArea()
	{
		logger.debug("getUserArea");// TODO wait (more table related)
		String sql = "select a.acc_loginname,ar.area_name from tab_acc_area aa,tab_accounts a,tab_area ar where aa.acc_oid = a.acc_oid and aa.area_id = ar.area_id";
		List temp = jt.queryForList(new PrepareSQL(sql).getSQL());
		Map<String, String> map = new HashMap<String, String>();
		Map tempMap;
		if (null != temp && temp.size() > 0)
		{
			for (int i = 0; i < temp.size(); i++)
			{
				tempMap = (Map) temp.get(i);
				map.put(tempMap.get("acc_loginname").toString(), tempMap.get("area_name")
						.toString());
			}
		}
		return map;
	}

	private Map<String, String> getUserRole()
	{
		logger.debug("getUserArea");// TODO wait (more table related)
		String sql = "select a.acc_loginname,r.role_name from tab_accounts a,tab_acc_role ar,tab_role r where r.role_id=ar.role_id and ar.acc_oid=a.acc_oid";
		List temp = jt.queryForList(new PrepareSQL(sql).getSQL());
		Map<String, String> map = new HashMap<String, String>();
		Map tempMap;
		if (null != temp && temp.size() > 0)
		{
			for (int i = 0; i < temp.size(); i++)
			{
				tempMap = (Map) temp.get(i);
				map.put(tempMap.get("acc_loginname").toString(), tempMap.get("role_name")
						.toString());
			}
		}
		return map;
	}
}
