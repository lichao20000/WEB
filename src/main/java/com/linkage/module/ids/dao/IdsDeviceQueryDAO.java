
package com.linkage.module.ids.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-17
 * @category com.linkage.module.ids.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class IdsDeviceQueryDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(IdsDeviceQueryDAO.class);
	
	// 查询多个最多只能查询这么多条
	private int maxNum = 10000;

	/**
	 * 根据入参去查询到设备账号
	 * @param
	 * @param idsShare_queryField
	 * @param idsShare_queryParam
	 * @param city_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryList(String idsShare_queryField,
			String idsShare_queryParam, String city_id) {
		// TODO Auto-generated method stub
		logger.debug("IdsDeviceQueryDAO=>queryList()");
		String table_customer = "tab_hgwcustomer";
		String gw_type = " and gw_type = 1 ";
		String strSQL  ="";
		//queryField==6 代表设备序列号，
		if ("6".equals(idsShare_queryField))
		{
			strSQL = "select a.device_id, a.device_serialnumber, a.oui, a.cpe_allocatedstatus, a.city_id,a.x_com_passwd,a.devicetype_id, b.user_id, b.username, " +
					"b.userline, b.updatetime from tab_gw_device a left join " + table_customer + " b on a.device_id=b.device_id  where a.dev_sub_sn='"
					+ idsShare_queryParam.substring(idsShare_queryParam.length() - 6)
					+ "' and a.device_serialnumber like '%" + idsShare_queryParam + "'" + gw_type;;
		}
		//loid
		else if("2".equals(idsShare_queryField)){
			strSQL = "select b.device_serialnumber,b.oui, b.cpe_allocatedstatus,c.user_id, b.device_id, b.x_com_passwd, b.city_id, b.devicetype_id " +
					" from  tab_gw_device b ,tab_hgwcustomer c where b.device_id=c.device_id and c.username ='"+idsShare_queryParam+"'";
		}
		else{
			strSQL = "select b.device_serialnumber,b.oui, b.cpe_allocatedstatus,c.user_id, b.device_id, b.x_com_passwd, b.city_id, b.devicetype_id " +
					" from  tab_gw_device b ,hgwcust_serv_info c where b.customer_id=c.user_id and c.serv_type_id=10 and  c.username ='"+idsShare_queryParam+"'";
		}
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(strSQL);
		return jt.queryForList(pSQL.getSQL());
	}

	public List<Map<String, String>> queryListForSDDX(String idsShare_queryField, String idsShare_queryParam) {
		// TODO Auto-generated method stub
		String strSQL  ="";
		// loid
		if("2".equals(idsShare_queryField)){
			strSQL = "select b.device_serialnumber,b.oui, b.cpe_allocatedstatus,c.user_id, b.device_id, b.x_com_passwd, b.city_id, b.devicetype_id " +
					" from  tab_gw_device b ,tab_hgwcustomer c where b.device_id=c.device_id and c.username ='"+idsShare_queryParam+"'";
		}
		// 宽带账号
		else if("1".equals(idsShare_queryField)){
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				strSQL = "select t.user_id,select b.device_serialnumber,b.oui,b.cpe_allocatedstatus,b.device_id,b.x_com_passwd,b.city_id,b.devicetype_id "
						+ "from ("
						+ "select a.device_id,c.user_id from tab_hgwcustomer a,hgwcust_serv_info c "
						+ "where a.user_id=c.user_id and c.serv_type_id=10 and c.username='"+idsShare_queryParam+"'"
						+ ") t,tab_gw_device b where t.device_id=b.device_id ";
			}else{
				strSQL = "select b.device_serialnumber,b.oui,b.cpe_allocatedstatus,c.user_id,b.device_id,b.x_com_passwd,b.city_id,b.devicetype_id "
						+ "from tab_gw_device b,tab_hgwcustomer a,hgwcust_serv_info c where a.user_id=c.user_id and a.device_id=b.device_id "
						+ "and c.serv_type_id=10 and c.username='"+idsShare_queryParam+"'";
			}
		}
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(strSQL);
		return jt.queryForList(pSQL.getSQL());
	}
	
	public Map<String, String> queryPPPOE(String idsShare_queryParam) {
		logger.debug("IdsDeviceQueryDAO=>queryPPPOE()");
		String strSQL  ="";
	
		strSQL = "select a.city_id, b.username,b.user_id from tab_gw_device a, hgwcust_serv_info b where a.customer_id = b.user_id and b.serv_type_id=10 and a.dev_sub_sn='"
					+ idsShare_queryParam.substring(idsShare_queryParam.length() - 6)
					+ "' and a.device_serialnumber like '%" + idsShare_queryParam + "' and a.gw_type = 1";
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(strSQL);
		return DBOperation.getRecord(pSQL.getSQL());
	}

	public HashMap<String, String> getPPPOERate(String userId, String city_id) {

		String sql = "select a.device_serialnumber,a.pppoe_name,a.account_suffix,a.rate from "
				+ " tab_speed_dev_rate a  where a.user_id=?  order by a.rate desc ";
		
		// 用父地市id查询，如果父id为 00 则用当前cityid查询
		String queryCityId = Global.G_City_Pcity_Map.get(city_id);
		if ("00".equals(queryCityId) || "00".equals(city_id)) {
			queryCityId = city_id;
		}
		PrepareSQL pSql = new PrepareSQL(sql);
		pSql.setLong(1, StringUtil.getLongValue(userId));
		int rate = 0;
		int num = 0;
		List<HashMap<String, String>> list = DBOperation.getRecords(pSql.getSQL());
		// 先获取最大速率，然后再根据最大速率或者多个测速账号密码，随机从多个账号里面选取一个去测速
		if(null != list && !list.isEmpty()) {
			rate = StringUtil.getIntegerValue(list.get(0).get("rate"));
			sql = "select b.net_account,b.net_password,b.test_rate from tab_speed_net b where b.test_rate = ? and b.city_id=?";
			pSql = new PrepareSQL(sql);
			pSql.setInt(1, rate);
			pSql.setString(2, queryCityId);
			List<HashMap<String, String>> returnList = DBOperation.getRecords(pSql.getSQL());
			if(null != returnList && !returnList.isEmpty()) {
				num = (int)(returnList.size() * Math.random());
				return returnList.get(num);
			}
			else {
				return null;
			}
		}
		else{
			return null;
		}
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 * 
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	
//	public Map<String,String> getDefaultdiag(int default_type_id){
//		PrepareSQL psql = new PrepareSQL("select * from tab_diag_default where default_type_id=?");
//		psql.setInt(1, default_type_id);
//		Map map = jt.queryForMap(psql.getSQL());
//		return map;
//	}
	
//	public List queryDevice(String gw_type, long areaId, String deviceSerialnumber,
//			String deviceIp)
//	{
//		logger.debug("GwDeviceQueryDAO=>queryDevice()");
//		PrepareSQL pSQL = new PrepareSQL();
//		pSQL.setSQL("select a.*,b.vendor_add,c.device_model,d.softwareversion from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
//		if (null != deviceSerialnumber && !"".equals(deviceSerialnumber)
//				&& !"-1".equals(deviceSerialnumber))
//		{
//			if (deviceSerialnumber.length() > 5)
//			{
//				pSQL.append(" and a.dev_sub_sn ='");
//				pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6,
//						deviceSerialnumber.length()));
//				pSQL.append("' ");
//			}
//			pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE,
//					deviceSerialnumber, false);
//		}
//		if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp))
//		{
//			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp,
//					false);
//		}
//		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type))
//		{
//			pSQL.append(" and a.gw_type = " + gw_type);
//		}
//		pSQL.append(" order by a.complete_time");
//		return jt.query(pSQL.toString(), new RowMapper()
//		{
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException
//			{
//				Map<String, String> map = new HashMap<String, String>();
//				return resultSet2Map(map, rs);
//			}
//		});
//	}

//	/**
//	 * 查询设备列表(单独根据设备表的条件查询)
//	 * 
//	 * @param areaId
//	 * @param queryParam
//	 * @return
//	 */
//	public List queryDevice(String gw_type, long areaId, String username)
//	{
//		logger.debug("GwDeviceQueryDAO=>queryDevice()");
//		PrepareSQL pSQL = new PrepareSQL();
//		String tableName = "tab_hgwcustomer";
//		if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type))
//		{
//			tableName = "tab_egwcustomer";
//		}
//		pSQL.setSQL("select a.*,b.vendor_add,c.device_model,d.softwareversion from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,");
//		pSQL.append(tableName);
//		pSQL.append(" e where a.device_id=e.device_id and a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
//		if (null != username && !"".equals(username))
//		{
//			pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, username);
//		}
//		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type))
//		{
//			pSQL.append(" and a.gw_type = " + gw_type);
//		}
//		pSQL.append(" order by a.complete_time");
//		return jt.query(pSQL.toString(), new RowMapper()
//		{
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException
//			{
//				Map<String, String> map = new HashMap<String, String>();
//				return resultSet2Map(map, rs);
//			}
//		});
//	}
//
//	public List queryDevice(String gw_type, String voipPhoneNum)
//	{
//		logger.debug("queryDevice({})", new Object[] { voipPhoneNum });
//		PrepareSQL psql = new PrepareSQL();
//		psql.append("select c.*, d.vendor_add, e.device_model, f.softwareversion ");
//		psql.append("  from tab_hgwcustomer a, tab_voip_serv_param b, tab_gw_device c, tab_vendor d, gw_device_model e, tab_devicetype_info f ");
//		psql.append(" where 1 = 1 ");
//		psql.append("   and a.user_id = b.user_id");
//		psql.append("   and a.device_id = c.device_id");
//		psql.append("   and c.vendor_id = d.vendor_id");
//		psql.append("   and c.device_model_id = e.device_model_id ");
//		psql.append("   and c.devicetype_id = f.devicetype_id");
//		psql.append("   and c.device_status = 1"); // 设备已确认
//		// 根据VOIP电话号码
//		if (null != voipPhoneNum && !"".equals(voipPhoneNum))
//		{
//			psql.append("   and b.voip_phone = '" + voipPhoneNum + "'");
//		}
//		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type))
//		{
//			psql.append(" and c.gw_type = " + gw_type);
//		}
//		return jt.query(psql.getSQL(), new RowMapper()
//		{
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException
//			{
//				Map<String, String> map = new HashMap<String, String>();
//				return resultSet2Map(map, rs);
//			}
//		});
//	}
//
//	/**
//	 * 查询设备页面增加按照宽带账号查询(不分页)
//	 * 
//	 * @param gw_type
//	 * @param cityId
//	 * @param kdname
//	 * @author chenjie
//	 * @date 2012-4-18
//	 * @return
//	 */
//	public List queryDeviceByKdname(String gw_type, String kdname)
//	{
//		logger.debug("queryDeviceByKdname({},{},{})", new Object[] { gw_type, kdname });
//		PrepareSQL psql = new PrepareSQL();
//		psql.append("select c.*, d.vendor_add, e.device_model, f.softwareversion ");
//		psql.append(" from tab_hgwcustomer a, hgwcust_serv_info b, tab_gw_device c, tab_vendor d, ");
//		psql.append(" gw_device_model e, tab_devicetype_info f");
//		psql.append(" where a.device_id=c.device_id and a.user_id = b.user_id ");
//		psql.append("   and c.vendor_id = d.vendor_id");
//		psql.append("   and c.device_model_id = e.device_model_id ");
//		psql.append("   and c.devicetype_id = f.devicetype_id");
//		psql.append("   and c.device_status = 1"); // 设备已确认
//		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type))
//		{
//			psql.append(" and c.gw_type = " + gw_type);
//		}
//		// 宽带账号
//		if (!StringUtil.IsEmpty(kdname))
//		{
//			psql.append(" and b.serv_type_id = 10 and b.username='" + kdname + "'");
//		}
//		return jt.query(psql.getSQL(), new RowMapper()
//		{
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException
//			{
//				Map<String, String> map = new HashMap<String, String>();
//				return resultSet2Map(map, rs);
//			}
//		});
//	}
//
//	/**
//	 * 查询设备列表(根据设备序列号和设备IP用or以及like来匹配)
//	 * 
//	 * @param areaId
//	 * @param queryParam
//	 * @return
//	 */
//	public List queryDeviceByFieldOr(String gw_type, long areaId, String queryParam)
//	{
//		logger.debug("GwDeviceQueryDAO=>queryDeviceByFieldOr(areaId:{},queryParam:{})",
//				areaId, queryParam);
//		PrepareSQL pSQL = new PrepareSQL();
//		pSQL.setSQL("select a.*,b.vendor_add,c.device_model,d.softwareversion from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
//		if (null != queryParam && !"".equals(queryParam))
//		{
//			pSQL.append(" and (a.device_serialnumber like '%");
//			pSQL.append(queryParam);
//			pSQL.append("%' or a.loopback_ip like '%");
//			pSQL.append(queryParam);
//			pSQL.append("%') ");
//		}
//		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type))
//		{
//			pSQL.append(" and a.gw_type = " + gw_type);
//		}
//		pSQL.append(" order by a.complete_time");
//		return jt.query(pSQL.toString(), new RowMapper()
//		{
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException
//			{
//				Map<String, String> map = new HashMap<String, String>();
//				return resultSet2Map(map, rs);
//			}
//		});
//	}
//
//	/**
//	 * 查询设备列表(条数)(根据设备序列号和设备IP用or以及like来匹配)
//	 * 
//	 * @param areaId
//	 * @param queryParam
//	 * @return
//	 */
//	public int queryDeviceCount(String gw_type, long areaId, String deviceSerialnumber,
//			String deviceIp)
//	{
//		logger.debug("GwDeviceQueryDAO=>queryDeviceCount()");
//		PrepareSQL pSQL = new PrepareSQL();
//		pSQL.setSQL("select count(1) from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
//		if (null != deviceSerialnumber && !"".equals(deviceSerialnumber)
//				&& !"-1".equals(deviceSerialnumber))
//		{
//			if (deviceSerialnumber.length() > 5)
//			{
//				pSQL.append(" and a.dev_sub_sn ='");
//				pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6,
//						deviceSerialnumber.length()));
//				pSQL.append("' ");
//			}
//			pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE,
//					deviceSerialnumber, false);
//		}
//		if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp))
//		{
//			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp,
//					false);
//		}
//		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type))
//		{
//			pSQL.append(" and a.gw_type = " + gw_type);
//		}
//		return jt.queryForInt(pSQL.toString());
//	}
//
//	/**
//	 * 查询设备列表(单独根据设备表的条件查询)
//	 * 
//	 * @param areaId
//	 * @param queryParam
//	 * @return
//	 */
//	public int queryDeviceCount(String gw_type, long areaId, String username)
//	{
//		logger.debug("GwDeviceQueryDAO=>queryDeviceCount()");
//		PrepareSQL pSQL = new PrepareSQL();
//		String tableName = "tab_hgwcustomer";
//		if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type))
//		{
//			tableName = "tab_egwcustomer";
//		}
//		pSQL.setSQL("select count(1) from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,");
//		pSQL.append(tableName);
//		pSQL.append(" e where a.device_id=e.device_id and a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
//		if (null != username && !"".equals(username))
//		{
//			pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, username);
//		}
//		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type))
//		{
//			pSQL.append(" and a.gw_type = " + gw_type);
//		}
//		return jt.queryForInt(pSQL.toString());
//	}
//
//	/**
//	 * 宁夏 按VOIP电话号码查询设备 根据VOIP电话号码以及属地统计有多少设备 add by zhangchy 2012-02-23
//	 * 
//	 * @param city_id
//	 * @param voipPhoneNum
//	 * @return
//	 */
//	public int queryDeviceCount(String gw_type, String voipPhoneNum)
//	{
//		logger.debug("queryDeviceCount({})", new Object[] { voipPhoneNum });
//		PrepareSQL psql = new PrepareSQL();
//		psql.append("select count(1) ");
//		psql.append("  from tab_hgwcustomer a, tab_voip_serv_param b, tab_gw_device c, tab_vendor d, gw_device_model e, tab_devicetype_info f ");
//		psql.append(" where 1 = 1 ");
//		psql.append("   and a.user_id = b.user_id");
//		psql.append("   and a.device_id = c.device_id");
//		psql.append("   and c.vendor_id = d.vendor_id");
//		psql.append("   and c.device_model_id = e.device_model_id ");
//		psql.append("   and c.devicetype_id = f.devicetype_id");
//		psql.append("   and c.device_status = 1"); // 设备已确认
//		// 根据VOIP电话号码
//		if (null != voipPhoneNum && !"".equals(voipPhoneNum))
//		{
//			psql.append("   and b.voip_phone = '" + voipPhoneNum + "'");
//		}
//		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type))
//		{
//			psql.append(" and c.gw_type = " + gw_type);
//		}
//		return jt.queryForInt(psql.getSQL());
//	}
//
//	/**
//	 * 查询设备页面增加按照宽带账号统计总数
//	 * 
//	 * @param gw_type
//	 * @param cityId
//	 * @param kdname
//	 * @author chenjie
//	 * @date 2012-4-18
//	 * @return
//	 */
//	public int queryDeviceCountByKdname(String gw_type, String kdname)
//	{
//		logger.debug("queryDeviceByKdname({},{})", new Object[] { gw_type, kdname });
//		PrepareSQL psql = new PrepareSQL();
//		psql.append("select count(1)");
//		psql.append(" from tab_hgwcustomer a, hgwcust_serv_info b, tab_gw_device c, tab_vendor d, ");
//		psql.append(" gw_device_model e, tab_devicetype_info f");
//		psql.append(" where a.device_id=c.device_id and a.user_id = b.user_id ");
//		psql.append("   and c.vendor_id = d.vendor_id");
//		psql.append("   and c.device_model_id = e.device_model_id ");
//		psql.append("   and c.devicetype_id = f.devicetype_id");
//		psql.append("   and c.device_status = 1"); // 设备已确认
//		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type))
//		{
//			psql.append(" and c.gw_type = " + gw_type);
//		}
//		// 宽带账号
//		if (!StringUtil.IsEmpty(kdname))
//		{
//			psql.append(" and b.serv_type_id = 10 and b.username='" + kdname + "'");
//		}
//		return jt.queryForInt(psql.getSQL());
//	}
//
//	/**
//	 * 查询设备列表(条数)(根据设备序列号和设备IP用or以及like来匹配)
//	 * 
//	 * @param areaId
//	 * @param queryParam
//	 * @return
//	 */
//	public int queryDeviceByFieldOrCount(String gw_type, long areaId, String queryParam)
//	{
//		logger.debug(
//				"GwDeviceQueryDAO=>queryDeviceByFieldOrCount(areaId:{},queryParam:{})",
//				areaId, queryParam);
//		PrepareSQL pSQL = new PrepareSQL();
//		pSQL.setSQL("select count(1) from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
//		if (null != queryParam && !"".equals(queryParam))
//		{
//			pSQL.append(" and (a.device_serialnumber like '%");
//			pSQL.append(queryParam);
//			pSQL.append("%' or a.loopback_ip like '%");
//			pSQL.append(queryParam);
//			pSQL.append("%') ");
//		}
//		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type))
//		{
//			pSQL.append(" and a.gw_type = " + gw_type);
//		}
//		return jt.queryForInt(pSQL.toString());
//	}
//
//	/**
//	 * 查询设备页面增加按照宽带账号查询(分页)
//	 * 
//	 * @param gw_type
//	 * @param cityId
//	 * @param kdname
//	 * @param curPage_splitPage
//	 * @param num_splitPage
//	 * @author chenjie
//	 * @date 2012-4-18
//	 * @return
//	 */
//	public List queryDeviceByKdname(String gw_type, String cityId, String kdname,
//			int curPage_splitPage, int num_splitPage)
//	{
//		logger.debug("queryDeviceByKdname({},{},{})", new Object[] { gw_type, cityId,
//				kdname });
//		PrepareSQL psql = new PrepareSQL();
//		psql.append("select");
//		psql.append(" c.*, d.vendor_add, e.device_model, f.softwareversion ");
//		psql.append(" from tab_hgwcustomer a, hgwcust_serv_info b, tab_gw_device c, tab_vendor d, ");
//		psql.append(" gw_device_model e, tab_devicetype_info f");
//		psql.append(" where a.device_id=c.device_id and a.user_id = b.user_id ");
//		psql.append("   and c.vendor_id = d.vendor_id");
//		psql.append("   and c.device_model_id = e.device_model_id ");
//		psql.append("   and c.devicetype_id = f.devicetype_id");
//		psql.append("   and c.device_status = 1"); // 设备已确认
//		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
//				&& !"-1".equals(cityId) && !"00".equals(cityId))
//		{
//			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
//			psql.append("   and c.city_id in ('" + StringUtils.weave(cityArray, "','")
//					+ "')");
//			cityArray = null;
//		}
//		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type))
//		{
//			psql.append(" and c.gw_type = " + gw_type);
//		}
//		// 宽带账号
//		if (!StringUtil.IsEmpty(kdname))
//		{
//			psql.append(" and b.serv_type_id = 10 and b.username='" + kdname + "'");
//		}
//		return querySP(psql.toString(), (curPage_splitPage - 1) * num_splitPage,
//				num_splitPage, new RowMapper()
//				{
//
//					public Object mapRow(ResultSet rs, int arg1) throws SQLException
//					{
//						Map<String, String> map = new HashMap<String, String>();
//						return resultSet2Map(map, rs);
//					}
//				});
//	}
//	
//	
//	
//	
//	/**
//	 * 查询设备列表(单独根据设备表的条件查询)
//	 * @param areaId
//	 * @param queryParam
//	 * @return
//	 */
//	public List queryDevice(String gw_type,int curPage_splitPage,int num_splitPage,long areaId,String deviceSerialnumber,String deviceIp){
//		logger.debug("GwDeviceQueryDAO=>queryDevice()");
//		PrepareSQL pSQL = new PrepareSQL();
//		if (DBUtil.GetDB() == 2) {
//			pSQL.append("select top " + maxNum );
//		}
//		else
//		{
//			pSQL.append("select");
//		}
//		pSQL.append(" a.*,b.vendor_add,c.device_model,d.softwareversion from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
//		if(null!=deviceSerialnumber && !"".equals(deviceSerialnumber) && !"-1".equals(deviceSerialnumber)){
//			if(deviceSerialnumber.length()>5){
//				pSQL.append(" and a.dev_sub_sn ='");
//				pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length()-6, deviceSerialnumber.length()));
//				pSQL.append("' ");
//			}
//			pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSerialnumber, false);
//		}
//		if(null!=deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)){
//			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp, false);
//		}
////		if( 1!=areaId ) {
////			pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
////			pSQL.append(String.valueOf(areaId));
////			pSQL.append(") ");
////		}
//		 if (null !=gw_type && !"".equals(gw_type)&& !"null".equals(gw_type) ) {
//				pSQL.append(" and a.gw_type = " + gw_type );
//			}
//		 if (DBUtil.GetDB() == 1) {
//				pSQL.append(" and rownum< " + maxNum );
//			}
//		pSQL.append(" order by a.complete_time");
//		return querySP(pSQL.toString(), (curPage_splitPage - 1) * num_splitPage,num_splitPage, new RowMapper() {
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				Map<String, String> map = new HashMap<String, String>();
//				return resultSet2Map(map, rs);
//			}
//		});
//	}
//	
//	
//	/**
//	 * 查询设备列表(单独根据设备表的条件查询)
//	 * @param areaId
//	 * @param queryParam
//	 * @return
//	 */
//	public List queryDevice(String gw_type,int curPage_splitPage,int num_splitPage,long areaId,String username){
//		logger.debug("GwDeviceQueryDAO=>queryDevice()");
//		PrepareSQL pSQL = new PrepareSQL();
//		String tableName = "tab_hgwcustomer";
//		if(!StringUtil.IsEmpty(gw_type)&&"2".equals(gw_type)){
//			tableName = "tab_egwcustomer";
//		}
//		if (DBUtil.GetDB() == 2) {
//		pSQL.append("select top " + maxNum );
//		}
//		else
//		{
//			pSQL.append("select");
//		}
//		pSQL.append(" a.*,b.vendor_add,c.device_model,d.softwareversion from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,");
//		pSQL.setSQL("select top " + maxNum + " a.*,b.vendor_add,c.device_model,d.softwareversion from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,");
//		pSQL.append(tableName);
//		pSQL.append(" e where a.device_id=e.device_id and a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
//		if(null!=username && !"".equals(username)){
//			pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, username);
//		}
//		
//		 if (null !=gw_type && !"".equals(gw_type)&& !"null".equals(gw_type) ) {
//				pSQL.append(" and a.gw_type = " + gw_type );
//			}
//		 if (DBUtil.GetDB() == 1) {
//				pSQL.append(" and rownum< " + maxNum );
//		}
//		pSQL.append(" order by a.complete_time");
//		return querySP(pSQL.toString(), (curPage_splitPage - 1) * num_splitPage,num_splitPage, new RowMapper() {
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				Map<String, String> map = new HashMap<String, String>();
//				return resultSet2Map(map, rs);
//			}
//		});
//	}
//	
//	
//	/**
//	 * 宁夏 按VOIP电话号码查询设备
//	 * 
//	 * 分页
//	 * 
//	 * add by zhangchy 2012-02-23
//	 * 
//	 * @param city_id
//	 * @param voipPhoneNum
//	 * @return
//	 */
//	public List queryDevice(String gw_type,int curPage_splitPage,int num_splitPage,String voipPhoneNum){
//		
//		logger.debug("queryDevice({},{},{})", new Object[]{curPage_splitPage, num_splitPage, voipPhoneNum});
//		
//		PrepareSQL psql = new PrepareSQL();
//		if (DBUtil.GetDB() == 2) {
//			psql.append("select top " + maxNum );
//		}
//		else
//		{
//			psql.append("select");
//		}
//		psql.append(" c.*, d.vendor_add, e.device_model, f.softwareversion ");
//		psql.append("  from tab_hgwcustomer a, tab_voip_serv_param b, tab_gw_device c, tab_vendor d, gw_device_model e, tab_devicetype_info f ");
//		psql.append(" where 1 = 1 ");
//		psql.append("   and a.user_id = b.user_id");
//		psql.append("   and a.device_id = c.device_id");
//		psql.append("   and c.vendor_id = d.vendor_id");
//		psql.append("   and c.device_model_id = e.device_model_id ");
//		psql.append("   and c.devicetype_id = f.devicetype_id");
//		psql.append("   and c.device_status = 1"); // 设备已确认
//		
//		// 根据VOIP电话号码
//		if (null != voipPhoneNum && !"".equals(voipPhoneNum)) {
//			psql.append("   and b.voip_phone = '" + voipPhoneNum + "'");
//		}
//		
//		 if (null !=gw_type && !"".equals(gw_type)&& !"null".equals(gw_type) ) {
//				psql.append(" and c.gw_type = " + gw_type );
//			}
//		 if (DBUtil.GetDB() == 1) {
//			 psql.append(" and rownum< " + maxNum );
//			}
//		return querySP(psql.toString(), (curPage_splitPage - 1) * num_splitPage,num_splitPage, new RowMapper() {
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				Map<String, String> map = new HashMap<String, String>();
//				return resultSet2Map(map, rs);
//			}
//		});
//	}
//	
//	
//	/**
//	 * 查询设备页面增加按照宽带账号查询(分页)
//	 * @param gw_type
//	 * @param cityId
//	 * @param kdname
//	 * @param curPage_splitPage
//	 * @param num_splitPage
//	 * 
//	 * @author chenjie
//	 * @date 2012-4-18
//	 * @return
//	 */
//	public List queryDeviceByKdname(String gw_type,String kdname, int curPage_splitPage, int num_splitPage)
//	{
//		logger.debug("queryDeviceByKdname({},{})", new Object[]{gw_type, kdname});
//		PrepareSQL psql = new PrepareSQL();
//		if (DBUtil.GetDB() == 2) {
//			psql.append("select top " + maxNum );
//		}
//		else
//		{
//			psql.append("select");
//		}
//		psql.append(" c.*, d.vendor_add, e.device_model, f.softwareversion ");
//		psql.append(" from tab_hgwcustomer a, hgwcust_serv_info b, tab_gw_device c, tab_vendor d, ");
//		psql.append(" gw_device_model e, tab_devicetype_info f");
//		psql.append(" where a.device_id=c.device_id and a.user_id = b.user_id ");
//		psql.append("   and c.vendor_id = d.vendor_id");
//		psql.append("   and c.device_model_id = e.device_model_id ");
//		psql.append("   and c.devicetype_id = f.devicetype_id");
//		psql.append("   and c.device_status = 1"); // 设备已确认
//		if(null !=gw_type && !"".equals(gw_type)&& !"null".equals(gw_type) ) {
//				psql.append(" and c.gw_type = " + gw_type );
//		}
//		// 宽带账号
//		if(!StringUtil.IsEmpty(kdname))
//		{
//			psql.append(" and b.serv_type_id = 10 and b.username='" + kdname + "'");
//		}
//		 if (DBUtil.GetDB() == 1) {
//			 psql.append(" and rownum< " + maxNum );
//			}
//		return querySP(psql.toString(), (curPage_splitPage - 1) * num_splitPage,num_splitPage, new RowMapper() {
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				Map<String, String> map = new HashMap<String, String>();
//				return resultSet2Map(map, rs);
//			}
//		});
//	}
//	
//	/**
//	 * 查询设备列表(根据设备序列号和设备IP用or以及like来匹配)
//	 * @param areaId
//	 * @param queryParam
//	 * @return
//	 */
//	public List queryDeviceByFieldOr(String gw_type,int curPage_splitPage,int num_splitPage,long areaId,String queryParam){
//		logger.debug("GwDeviceQueryDAO=>queryDeviceByFieldOr(areaId:{},queryParam:{})",areaId,queryParam);
//		PrepareSQL pSQL = new PrepareSQL();
//		if (DBUtil.GetDB() == 2) {
//			pSQL.append("select top " + maxNum );
//		}
//		else
//		{
//			pSQL.append("select");
//		}
//		pSQL.append(" a.*,b.vendor_add,c.device_model,d.softwareversion from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
//		if(null!=queryParam && !"".equals(queryParam)){
//			pSQL.append(" and (a.device_serialnumber like '%");
//			pSQL.append(queryParam);
//			pSQL.append("%' or a.loopback_ip like '%");
//			pSQL.append(queryParam);
//			pSQL.append("%') ");
//		}
//		 if (null !=gw_type && !"".equals(gw_type)&& !"null".equals(gw_type) ) {
//				pSQL.append(" and a.gw_type = " + gw_type );
//			}
//		 if (DBUtil.GetDB() == 1) {
//				pSQL.append(" and rownum< " + maxNum );
//			}
//		pSQL.append(" order by a.complete_time");
//		return querySP(pSQL.toString(), (curPage_splitPage - 1) * num_splitPage ,num_splitPage, new RowMapper() {
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				Map<String, String> map = new HashMap<String, String>();
//				return resultSet2Map(map, rs);
//			}
//		});
//	}
//	
//	
//	
//	
//	
//	
//	
//
//	/**
//	 * 数据转换
//	 * 
//	 * @param map
//	 * @param rs
//	 * @return
//	 * @throws SQLException
//	 */
//	public Map<String, String> resultSet2Map(Map<String, String> map, ResultSet rs)
//	{
//		try
//		{
//			map.put("device_id", rs.getString("device_id"));
//			map.put("oui", rs.getString("oui"));
//			map.put("device_serialnumber", rs.getString("device_serialnumber"));
//			map.put("device_name", rs.getString("device_name"));
//			map.put("city_id", rs.getString("city_id"));
//			map.put("city_name",
//					CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
//			map.put("office_id", rs.getString("office_id"));
//			map.put("complete_time", new DateTimeUtil(rs.getLong("complete_time") * 1000)
//					.getYYYY_MM_DD_HH_mm_ss());
//			map.put("zone_id", rs.getString("zone_id"));
//			map.put("buy_time", new DateTimeUtil(rs.getLong("buy_time") * 1000)
//					.getYYYY_MM_DD_HH_mm_ss());
//			map.put("staff_id", rs.getString("staff_id"));
//			map.put("remark", rs.getString("remark"));
//			map.put("loopback_ip", rs.getString("loopback_ip"));
//			map.put("interface_id", rs.getString("interface_id"));
//			map.put("device_status", rs.getString("device_status"));
//			map.put("gather_id", rs.getString("gather_id"));
//			map.put("devicetype_id", rs.getString("devicetype_id"));
//			map.put("softwareversion", rs.getString("softwareversion"));
//			map.put("maxenvelopes", rs.getString("maxenvelopes"));
//			map.put("cr_port", rs.getString("cr_port"));
//			map.put("cr_path", rs.getString("cr_path"));
//			map.put("cpe_mac", rs.getString("cpe_mac"));
//			map.put("cpe_currentupdatetime",
//					new DateTimeUtil(rs.getLong("cpe_currentupdatetime") * 1000)
//							.getYYYY_MM_DD_HH_mm_ss());
//			map.put("cpe_allocatedstatus", rs.getString("cpe_allocatedstatus"));
//			map.put("cpe_username", rs.getString("cpe_username"));
//			map.put("cpe_passwd", rs.getString("cpe_passwd"));
//			map.put("acs_username", rs.getString("acs_username"));
//			map.put("acs_passwd", rs.getString("acs_passwd"));
//			map.put("device_type", rs.getString("device_type"));
//			map.put("x_com_username", rs.getString("x_com_username"));
//			map.put("x_com_passwd", rs.getString("x_com_passwd"));
//			map.put("gw_type", rs.getString("gw_type"));
//			map.put("device_model_id", rs.getString("device_model_id"));
//			map.put("device_model", rs.getString("device_model"));
//			map.put("customer_id", rs.getString("customer_id"));
//			map.put("device_url", rs.getString("device_url"));
//			map.put("x_com_passwd_old", rs.getString("x_com_passwd_old"));
//			map.put("vendor_id", rs.getString("vendor_id"));
//			map.put("vendor_add", rs.getString("vendor_add"));
//		}
//		catch (SQLException e)
//		{
//			logger.error(e.getMessage());
//		}
//		return map;
//	}
	
	/**
	 * 获取设备信息
	 * @param idsShare_queryField
	 * @param idsShare_queryParam
	 * @param city_id
	 * @return
	 */
	public List<Map<String, String>> queryDevList(String idsShare_queryField,String idsShare_queryParam, String city_id)
	{
		//TODO wait
		String strSQL  = "select a.device_id, c.username loid,f.username,f.wan_type," +
				"a.device_serialnumber,a.oui,a.cpe_allocatedstatus,a.city_id,d.downlink,b.is_speedTest " 
				 +"from tab_gw_device a "
				 +"left join tab_device_version_attribute b on a.devicetype_id=b.devicetype_id,"
				 +"tab_hgwcustomer c,tab_netacc_spead d,hgwcust_serv_info f ";
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			strSQL+="where a.customer_id=cast(c.user_id as char) ";
		}else{
			strSQL+="where a.customer_id=to_char(c.user_id) ";
		}
		strSQL+="and c.user_id=f.user_id and d.username=f.username and f.serv_type_id=10 ";
		//queryField==6 代表设备序列号，
		if ("6".equals(idsShare_queryField))
		{
			strSQL += "and a.dev_sub_sn='"+ idsShare_queryParam.substring(idsShare_queryParam.length() - 6)
					 +"' and a.device_serialnumber like '%" + idsShare_queryParam + "'";
		}
		//loid
		else if("2".equals(idsShare_queryField)){
			strSQL += "and c.username ='"+idsShare_queryParam+"'";
		}
		else{
			//宽带账号
			strSQL += "and f.username ='"+idsShare_queryParam+"'";
		}
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(strSQL);
		return jt.queryForList(pSQL.getSQL());
	}
	
	public Map<String,String> getTestUser(int downlink){
		String sql = "select userName,password from tab_http_test_user where testRate=?";
		PrepareSQL pSQL = new PrepareSQL(sql);
		pSQL.setInt(1, downlink);
		return DBOperation.getRecord(pSQL.getSQL());
	}
}
