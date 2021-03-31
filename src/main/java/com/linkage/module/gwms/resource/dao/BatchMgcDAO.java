
package com.linkage.module.gwms.resource.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.resource.obj.BatchMgcBean;

/**
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-9-27
 * @category com.linkage.module.gwms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchMgcDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(BatchMgcDAO.class);

	// public Map<String, String> getAccessType()
	// {
	// PrepareSQL psql = new PrepareSQL("select type_name,type_id from gw_access_type");
	// final Map<String, String> resultMap = new HashMap<String, String>();
	// jt.query(psql.getSQL(), new RowCallbackHandler()
	// {
	//
	// @Override
	// public void processRow(ResultSet rs) throws SQLException
	// {
	// resultMap.put(rs.getString("type_name"), rs.getString("type_id"));
	// }
	// });
	// return resultMap;
	// }
	/**
	 * 根据MGC地址和端口信息，查询对应的表ID，如果不存在，则返回0
	 * 
	 * @param mgcBean
	 *            MGCBean，never null
	 * @return 如果对应的记录在表中存在，返回对应的表id，否则返回0
	 */
	@SuppressWarnings("unchecked")
	public long getExistSipId(BatchMgcBean mgcBean)
	{
		String sql = "select sip_id from tab_sip_info where prox_serv=? and prox_port=? and stand_prox_serv=? and stand_prox_port = ?"
				+ " and regi_serv=? and regi_port=? and stand_regi_serv=? and stand_regi_port=?"
				+ " and out_bound_proxy=? and out_bound_port=? and stand_out_bound_proxy=? and stand_out_bound_port=?";
		PrepareSQL pSql = new PrepareSQL(sql);
		int index = 0;
		String mainMgcIp = mgcBean.getMainMgcIp();
		int mainMgcPort = StringUtil.getIntegerValue(mgcBean.getMainMgcPort());
		String standMgcIp = mgcBean.getStandMgcIp();
		int standMgcPort = StringUtil.getIntegerValue(mgcBean.getStandMgcPort());
		pSql.setString(++index, mainMgcIp);
		pSql.setInt(++index, mainMgcPort);
		pSql.setString(++index, standMgcIp);
		pSql.setInt(++index, standMgcPort);
		pSql.setString(++index, mainMgcIp);
		pSql.setInt(++index, mainMgcPort);
		pSql.setString(++index, standMgcIp);
		pSql.setInt(++index, standMgcPort);
		pSql.setString(++index, mainMgcIp);
		pSql.setInt(++index, mainMgcPort);
		pSql.setString(++index, standMgcIp);
		pSql.setInt(++index, standMgcPort);
		List<Map<String, Object>> list = jt.queryForList(pSql.getSQL());
		long result = 0;
		if (list == null || list.isEmpty())
		{
			result = 0;
		}
		else
		{
			result = StringUtil.getLongValue(list.get(0), "sip_id");
		}
		logger.info("result sip_id is [{}]", result);
		return result;
	}

	public long getNextSipId()
	{
		String sql = "select max(sip_id) from tab_sip_info";
		long result = jt.queryForLong(new PrepareSQL(sql).getSQL()) + 1;
		logger.info("get next primary key from tab_sip_info is [{}]", result);
		return result;
	}

	public void addSip(long sipId, BatchMgcBean mgcBean)
	{
		String sql = "insert into tab_sip_info (sip_id,prox_serv,prox_port,stand_prox_serv,"
				+ " stand_prox_port,regi_serv,regi_port,stand_regi_serv,stand_regi_port,"
				+ "out_bound_proxy, out_bound_port, stand_out_bound_proxy, stand_out_bound_port)"
				+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PrepareSQL pSql = new PrepareSQL(sql);
		int index = 0;
		pSql.setLong(++index, sipId);
		String mainMgcIp = mgcBean.getMainMgcIp();
		int mainMgcPort = StringUtil.getIntegerValue(mgcBean.getMainMgcPort());
		String standMgcIp = mgcBean.getStandMgcIp();
		int standMgcPort = StringUtil.getIntegerValue(mgcBean.getStandMgcPort());
		pSql.setString(++index, mainMgcIp);
		pSql.setInt(++index, mainMgcPort);
		pSql.setString(++index, standMgcIp);
		pSql.setInt(++index, standMgcPort);
		pSql.setString(++index, mainMgcIp);
		pSql.setInt(++index, mainMgcPort);
		pSql.setString(++index, standMgcIp);
		pSql.setInt(++index, standMgcPort);
		pSql.setString(++index, mainMgcIp);
		pSql.setInt(++index, mainMgcPort);
		pSql.setString(++index, standMgcIp);
		pSql.setInt(++index, standMgcPort);
		int updateRows = jt.update(pSql.getSQL());
		logger.info("insert sip_id[{}] into tab_sip_info, result[{}]", sipId,
				updateRows > 0);
	}

	/**
	 * 根据用户名和voip账号查询已经存在的用户ID，如果不存在，返回0
	 * 
	 * @param mgcBean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public long getExistVoipUserId(BatchMgcBean mgcBean)
	{
		String sql = "select a.user_id from tab_hgwcustomer a,tab_voip_serv_param b"
				+ " where a.user_id=b.user_id and a.username=? and b.voip_phone=?";
		PrepareSQL pSql = new PrepareSQL(sql);
		int index = 0;
		pSql.setString(++index, mgcBean.getUsername());
		pSql.setString(++index, mgcBean.getVoipPhone());
		List<Map<String, Object>> list = jt.queryForList(pSql.getSQL());
		if (list == null || list.isEmpty())
		{
			logger.info(
					"username[{}] and voip_phone[{}] do not exist in tab_hgwcustomer",
					mgcBean.getUsername(), mgcBean.getVoipPhone());
			return 0;
		}
		long result = StringUtil.getLongValue(list.get(0), "user_id");
		logger.info(
				"username[{}] and voip_phone[{}] exist in tab_hgwcustomer, user_id is [{}]",
				new Object[] { mgcBean.getUsername(), mgcBean.getVoipPhone(), result });
		return result;
	}

	public void updateVoipSipId(long userId, long sipId, BatchMgcBean mgcBean)
	{
		String sql = "update tab_voip_serv_param set sip_id=? where voip_phone=? and user_id=?";
		PrepareSQL pSql = new PrepareSQL(sql);
		int index = 0;
		pSql.setLong(++index, sipId);
		pSql.setString(++index, mgcBean.getVoipPhone());
		pSql.setLong(++index, userId);
		int updateRows = jt.update(pSql.getSQL());
		if (logger.isInfoEnabled())
		{
			logger.info(
					"update tab_voip_serv_param where user_id=[{}] and voip_phone=[{}], result is [{}]",
					new Object[] { userId, mgcBean.getVoipPhone(), updateRows > 0 });
		}
	}

	/**
	 * 更新业务开通状态
	 */
	public void updateServOpenStatus(long userId)
	{
		String sql = "update hgwcust_serv_info set open_status=0 where user_id=? and serv_type_id=14 and serv_status in (1,2) and open_status!=0";
		PrepareSQL pSql = new PrepareSQL(sql);
		int index = 0;
		pSql.setLong(++index, userId);
		int updateRows = jt.update(pSql.getSQL());
		logger.info(
				"update hgwcust_serv_info open_status by user_id[{}], update rows[{}]",
				userId, updateRows);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserBindDevice(BatchMgcBean mgcBean)
	{
		String sql = "select b.user_id,a.device_id,a.gather_id,a.devicetype_id,a.oui,a.device_serialnumber,a.city_id "
				+ " from tab_gw_device a, tab_hgwcustomer b"
				+ " where a.device_id=b.device_id"
				+ " and b.username=? and user_state='1'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, mgcBean.getUsername());
		List<Map<String, Object>> list = jt.queryForList(psql.getSQL());
		if (list == null || list.isEmpty())
		{
			logger.info("username[{}] do not bind device", mgcBean.getUsername());
			return null;
		}
		return list.iterator().next();
	}
}
