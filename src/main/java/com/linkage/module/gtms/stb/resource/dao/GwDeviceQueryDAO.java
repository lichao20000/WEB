/**
 *
 */

package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DbUtil;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.system.utils.StringUtils;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.dao
 */
public class GwDeviceQueryDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(GwDeviceQueryDAO.class);

	/**
	 * 编辑设备表
	 */
	public int editDevice(String deviceId, String cityId, String cpeMac, String deviceIp)
	{
		if (StringUtil.IsEmpty(deviceId))
		{
			return 0;
		}
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(" update stb_tab_gw_device set city_id=?,cpe_mac=?,loopback_ip=? where device_id=? ");
		pSQL.setString(1, cityId);
		pSQL.setString(2, cpeMac);
		pSQL.setString(3, deviceIp);
		pSQL.setString(4, deviceId);
		return jt.update(pSQL.getSQL());
	}

	/**
	 * 设置设备表的状态
	 *
	 * @param deviceId
	 * @return
	 */
	public int queryDeviceByOnce(String deviceId)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceByOnce(deviceId:{},cityId;{})",
				deviceId);
		if (StringUtil.IsEmpty(deviceId))
		{
			return 0;
		}
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(" update stb_tab_gw_device set device_status=-1 where ");
		pSQL.append("device_id='");
		pSQL.append(deviceId);
		pSQL.append("'");
		return jt.update(pSQL.getSQL());
	}

	/**
	 * 单查设备表
	 *
	 * @param deviceId
	 * @param cityId
	 * @return
	 */
	public List queryDeviceByOnce(String deviceId, String cityId, boolean noChildCity)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceByOnce(deviceId:{},cityId;{})",
				deviceId, cityId);
		if (StringUtil.IsEmpty(deviceId))
		{
			return new ArrayList();
		}
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select device_id,cpe_allocatedstatus,customer_id,oui,device_serialnumber from stb_tab_gw_device where 1=1 ");
		if (noChildCity)
		{
			pSQL.append(" and city_id='");
			pSQL.append(cityId);
			pSQL.append("' ");
		}
		else
		{
			if (!CityDAO.isAdmin(cityId))
			{
				ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				pSQL.append(" and city_id in ('" + StringUtils.weave(cityArray, "','")
						+ "')");
				cityArray = null;
			}
		}
		pSQL.appendAndString("device_id", PrepareSQL.EQUEAL, deviceId);
		return jt.queryForList(pSQL.getSQL());
	}

	/**
	 * 查询设备详细信息，根据权限过滤
	 *
	 * @param deviceId
	 * @param areaId
	 * @return
	 */
	public List queryDeviceDetail(String deviceId, String cityId, boolean noChildCity)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceDetail(deviceId:{})", deviceId);
		PrepareSQL pSQL = new PrepareSQL();
		if (Global.HNLT.equals(Global.instAreaShortName))
		{// TODO wait (more table related)
			pSQL.setSQL("select a.complete_time, a.cpe_currentupdatetime, a.city_id, a.status, a.cpe_allocatedstatus, a.customer_id," +
					" b.vendor_add,c.device_model,d.softwareversion,d.specversion,d.hardwareversion " +
					"from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d " +
					"where  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		}
		else
		{// TODO wait (more table related)
			pSQL.setSQL("select a.complete_time, a.cpe_currentupdatetime, a.city_id, a.status, a.cpe_allocatedstatus, a.customer_id, " +
					" b.vendor_add,c.device_model,d.softwareversion,d.specversion,d.hardwareversion,e.last_time,e.oper_time,e.online_status " +
					"from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e " +
					"where a.device_id=e.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		}
		if (noChildCity)
		{
			pSQL.append(" and a.city_id='");
			pSQL.append(cityId);
			pSQL.append("' ");
		}
		else
		{
			if (!CityDAO.isAdmin(cityId))
			{
				ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','")
						+ "')");
				cityArray = null;
			}
		}
		pSQL.appendAndString("a.device_id", PrepareSQL.EQUEAL, deviceId);
		return jt.queryForList(pSQL.getSQL());
	}

	/**
	 * 查询历史配置信息
	 *
	 * @param deviceId
	 * @return
	 */
	public List queryBaseConfigInfo(String deviceId)
	{
		logger.debug("queryBaseConfigInfo({})" + deviceId);
		String sql = "select gather_time, address_type from stb_lan_recent where device_id='" + deviceId + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 查询stb_x_serv_info最新记录表
	 *
	 * @param deviceId
	 * @return
	 */
	public List getX_CTC_IPTV_ServiceInfoRecent(String deviceId)
	{
		String sql = "select pppoe_id, pppoe_pwd, user_id, user_pwd, auth_url, iptv_dhcp_username, iptv_dhcp_password " +
				" from stb_x_serv_info_recent where device_id='" + deviceId + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 查询stb_user_itfs最新记录表
	 *
	 * @param device_id
	 * @return
	 */
	public List getUserInterfaceRecent(String deviceId)
	{
		String sql = "select auto_update_serv from stb_user_itfs_recent where device_id='" + deviceId + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 屏幕显示与视频输出制式
	 *
	 * @param deviceId
	 * @return
	 */
	public List getCmpstVideoAndAspRatio(String deviceId)
	{
		String sql = "select composite_video_standard, aspect_ratio from stb_capa where device_id='" + deviceId + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 查询用户信息R
	 *
	 * @param customerId
	 * @return 修改：姚重亮 日期：10/14/9:52 修改内容：查询结果中增加业务账号、业务密码两个字段
	 */
	@SuppressWarnings("unchecked")
	public Map queryDeviceUser(long customerId)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceUser(customerId;{})", customerId);
		PrepareSQL pSQL = new PrepareSQL();
		if (Global.XJDX.equals(Global.instAreaShortName))
		{
			pSQL.setSQL("select cust_account,cust_name,serv_account,serv_pwd,addressing_type,is_tel_dev from stb_tab_customer  where customer_id=?");
		}
		else
		{
			pSQL.setSQL("select cust_account,cust_name,serv_account,serv_pwd,addressing_type from stb_tab_customer  where customer_id=?");
		}
		pSQL.setLong(1, customerId);
		List list = jt.queryForList(pSQL.getSQL());
		if (list == null || list.size() < 1)
		{
			return new HashMap();
		}
		else
		{
			return (Map) list.get(0);
		}
	}

	/**
	 * 查询设备列表(根据设备序列号和设备IP用or以及like来匹配)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDeviceByFieldOr(int curPage_splitPage, int num_splitPage,
			String cityId, String queryParam, boolean noChildCity)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceByFieldOr(areaId:{},queryParam:{})",
				cityId, queryParam);
		PrepareSQL pSQL = new PrepareSQL();
		// TODO wait (more table related)
		pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.loopback_ip, a.city_id, a.serv_account, a.inform_stat, " +
				" b.vendor_add,c.device_model,d.softwareversion " +
				" from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d " +
				" where a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (!StringUtil.IsEmpty(queryParam))
		{
			pSQL.append(" and (a.device_serialnumber like '%");
			pSQL.append(queryParam);
			pSQL.append("%' or a.loopback_ip = '");
			pSQL.append(queryParam);
			pSQL.append("') ");
		}
		if (noChildCity)
		{
			pSQL.append(" and a.city_id='");
			pSQL.append(cityId);
			pSQL.append("' ");
		}
		else
		{
			if (!CityDAO.isAdmin(cityId))
			{
				ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','")
						+ "')");
				cityArray = null;
			}
		}
		return querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage,
				num_splitPage, new RowMapper()
				{

					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						return resultSet2Map(map, rs);
					}
				});
	}

	/**
	 * 查询设备列表(条数)(根据设备序列号和设备IP用or以及like来匹配)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public int queryDeviceByFieldOrCount(String cityId, String queryParam,
			boolean noChildCity)
	{
		logger.debug(
				"GwDeviceQueryDAO=>queryDeviceByFieldOrCount(areaId:{},queryParam:{})",
				cityId, queryParam);
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select count(*) from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d where a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (!StringUtil.IsEmpty(queryParam))
		{
			pSQL.append(" and (a.device_serialnumber like '%");
			pSQL.append(queryParam);
			pSQL.append("%' or a.loopback_ip = '");
			pSQL.append(queryParam);
			pSQL.append("') ");
		}
		if (noChildCity)
		{
			pSQL.append(" and a.city_id='");
			pSQL.append(cityId);
			pSQL.append("' ");
		}
		else
		{
			if (!CityDAO.isAdmin(cityId))
			{
				ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','")
						+ "')");
				cityArray = null;
			}
		}
		return jt.queryForInt(pSQL.getSQL());
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDevice(int curPage_splitPage, int num_splitPage, long areaId,
			String cityId, String vendorId, String deviceModelId, String devicetypeId,
			String bindType, String deviceSerialnumber, String loopbackIp,
			String username, String servAccount, String startLastTime,
			String endLastTime, boolean noChildCity, String completeStartTime,
			String completeEndTime)
	{
		logger.debug("GwDeviceQueryDAO=>queryDevice()");
		PrepareSQL pSQL = new PrepareSQL();
		boolean needAppendCustomer = false;
		if (StringUtil.IsEmpty(username) && StringUtil.IsEmpty(servAccount))
		{// TODO wait (more table related)
			pSQL.setSQL("select x.*, e.cust_account,e.serv_account servAccount " +
					" from (select a.device_id, a.oui, a.device_serialnumber, a.loopback_ip, a.city_id, a.serv_account, a.inform_stat, a.customer_id, " +
					"	b.vendor_add,c.device_model,d.softwareversion,f.online_status,f.last_time " +
					" 	from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus f " +
					" 	where a.device_id=f.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
			needAppendCustomer = true;
		}
		else
		{
			if (!StringUtil.IsEmpty(username) && !StringUtil.IsEmpty(servAccount))
			{// TODO wait (more table related)
				pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.loopback_ip, a.city_id, a.serv_account, a.inform_stat, " +
						"b.vendor_add,c.device_model,d.softwareversion,e.cust_account,a.serv_account,f.online_status,f.last_time " +
						"from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_tab_customer e,stb_gw_devicestatus f " +
						"where a.customer_id=e.customer_id and a.device_id=f.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
				pSQL.append(PrepareSQL.AND, "e.cust_account", PrepareSQL.EQUEAL,
						username, false);
				pSQL.append(PrepareSQL.AND, "a.serv_account", PrepareSQL.EQUEAL,
						servAccount, false);
			}
			else if (!StringUtil.IsEmpty(username))
			{// TODO wait (more table related)
				pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.loopback_ip, a.city_id, a.serv_account, a.inform_stat, " +
						"b.vendor_add,c.device_model,d.softwareversion,e.cust_account,a.serv_account,f.online_status,f.last_time " +
						"from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_tab_customer e,stb_gw_devicestatus f " +
						"where a.customer_id=e.customer_id and a.device_id=f.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
				pSQL.append(PrepareSQL.AND, "e.cust_account", PrepareSQL.EQUEAL,
						username, false);
			}
			else if (!StringUtil.IsEmpty(servAccount))
			{// TODO wait (more table related)
				pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.loopback_ip, a.city_id, a.serv_account, a.inform_stat, " +
						"b.vendor_add,c.device_model,d.softwareversion,a.serv_account,f.online_status,f.last_time " +
						"from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus f " +
						"where a.device_id=f.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
				pSQL.append(PrepareSQL.AND, "a.serv_account", PrepareSQL.EQUEAL,
						servAccount, false);
			}
		}
		if (!StringUtil.IsEmpty(startLastTime))
		{
			pSQL.append(" and f.last_time > ");
			pSQL.append(startLastTime);
		}
		if (!StringUtil.IsEmpty(endLastTime))
		{
			pSQL.append(" and f.last_time < ");
			pSQL.append(endLastTime);
		}
		if (noChildCity)
		{
			pSQL.append(" and a.city_id='");
			pSQL.append(cityId);
			pSQL.append("' ");
		}
		else
		{
			if (!CityDAO.isAdmin(cityId))
			{
				ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','")
						+ "')");
				cityArray = null;
			}
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId))
		{
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId))
		{
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
		}
		if (!StringUtil.IsEmpty(devicetypeId) && !"-1".equals(devicetypeId))
		{
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
		}
		if (!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType))
		{
			pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
		}
		if (!StringUtil.IsEmpty(deviceSerialnumber))
		{
			if (deviceSerialnumber.length() > 5)
			{
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("'");
			}
			pSQL.append(" and a.device_serialnumber like '%");
			pSQL.append(deviceSerialnumber);
			pSQL.append("' ");
		}
		if (!StringUtil.IsEmpty(loopbackIp) && !"-1".equals(loopbackIp))
		{
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, loopbackIp,
					false);
		}
		if (!"0".equals(completeStartTime))
		{
			pSQL.append(PrepareSQL.AND, "a.complete_time", PrepareSQL.BIGGEREQUEAL,
					completeStartTime, true);
		}
		if (!"0".equals(completeEndTime))
		{
			pSQL.append(PrepareSQL.AND, "a.complete_time", PrepareSQL.SMALLERQUEAL,
					completeEndTime, true);
		}
		if (needAppendCustomer)
		{
			pSQL.append(") x left join stb_tab_customer e on x.customer_id=e.customer_id");
		}
		return querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage,
				num_splitPage, new RowMapper()
				{

					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						// 修改,因获取stb_tab_customer中的账号而不是设备表中
						map.put("servAccount", rs.getString("servAccount"));
						return resultSet2Map(map, rs);
					}
				});
	}

	/**
	 * 查询设备列表(根据设备序列号和设备IP用or以及like来匹配)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDeviceByFieldOr(String gw_type, long areaId, String queryParam,
			String cityId)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceByFieldOr(areaId:{},queryParam:{})",
				areaId, queryParam);
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select a.device_id, b.vendor_add,c.device_model,d.softwareversion " +
				"from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d " +
				"where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (!StringUtil.IsEmpty(queryParam))
		{
			pSQL.append(" and (a.device_serialnumber like '%");
			pSQL.append(queryParam);
			pSQL.append("%' or a.loopback_ip like '%");
			pSQL.append(queryParam);
			pSQL.append("%') ");
		}
		if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId) && !"-1".equals(cityId)
				&& !"00".equals(cityId))
		{
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','")
					+ "')");
			cityArray = null;
		}
		if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type))
		{
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		pSQL.append(" order by a.complete_time");
		return jt.query(pSQL.toString(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(关联设备状态表查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDeviceByLikeStatus(String gw_type, long areaId, String cityId,
			String vendorId, String deviceModelId, String devicetypeId, String bindType,
			String deviceSerialnumber, String deviceIp, String onlineStatus)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatus()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select a.device_id,b.vendor_add,c.device_model,d.softwareversion " +
				"from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e " +
				"where a.device_status =1 and  a.device_id=e.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus))
		{
			pSQL.appendAndNumber("e.online_status", PrepareSQL.EQUEAL, onlineStatus);
		}
		if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId) && !"-1".equals(cityId)
				&& !"00".equals(cityId))
		{
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','")
					+ "')");
			cityArray = null;
		}
		if (!StringUtil.IsEmpty(vendorId) && !"null".equals(vendorId)
				&& !"-1".equals(vendorId))
		{
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"null".equals(deviceModelId)
				&& !"-1".equals(deviceModelId))
		{
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
		}
		if (!StringUtil.IsEmpty(devicetypeId) && !"null".equals(devicetypeId)
				&& !"-1".equals(devicetypeId))
		{
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
		}
		if (!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType))
		{
			pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
		}
		if (!StringUtil.IsEmpty(deviceSerialnumber) && !"-1".equals(deviceSerialnumber))
		{
			if (deviceSerialnumber.length() > 5)
			{
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE,
					deviceSerialnumber, false);
		}
		if (!StringUtil.IsEmpty(deviceIp) && !"-1".equals(deviceIp))
		{
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp,
					false);
		}
		if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type))
		{
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		pSQL.append(" order by a.complete_time");
		return jt.query(pSQL.toString(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDevice(String gw_type, long areaId, String cityId, String username)
	{
		logger.debug("GwDeviceQueryDAO=>queryDevice()");
		PrepareSQL pSQL = new PrepareSQL();
		String tableName = "tab_hgwcustomer";
		if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type))
		{
			tableName = "tab_egwcustomer";
		}// TODO wait (more table related)
		pSQL.setSQL("select a.device_id,b.vendor_add,c.device_model,d.softwareversion from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,");
		pSQL.append(tableName);
		pSQL.append(" e where a.device_id=e.device_id and a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId) && !"-1".equals(cityId)
				&& !"00".equals(cityId))
		{
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','")
					+ "')");
			cityArray = null;
		}
		if (!StringUtil.IsEmpty(username))
		{
			pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, username);
		}
		if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type))
		{
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		pSQL.append(" order by a.complete_time");
		return jt.query(pSQL.toString(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDevice(String gw_type, long areaId, String cityId, String vendorId,
			String deviceModelId, String devicetypeId, String bindType,
			String deviceSerialnumber, String deviceIp)
	{
		logger.debug("GwDeviceQueryDAO=>queryDevice()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select a.device_id,b.vendor_add,c.device_model,d.softwareversion " +
				"from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d " +
				"where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId) && !"-1".equals(cityId)
				&& !"00".equals(cityId))
		{
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','")
					+ "')");
			cityArray = null;
		}
		if (!StringUtil.IsEmpty(vendorId) && !"null".equals(vendorId)
				&& !"-1".equals(vendorId))
		{
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"null".equals(deviceModelId)
				&& !"-1".equals(deviceModelId))
		{
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
		}
		if (!StringUtil.IsEmpty(devicetypeId) && !"null".equals(devicetypeId)
				&& !"-1".equals(devicetypeId))
		{
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
		}
		if (!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType))
		{
			pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
		}
		if (!StringUtil.IsEmpty(deviceSerialnumber) && !"-1".equals(deviceSerialnumber))
		{
			if (deviceSerialnumber.length() > 5)
			{
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE,
					deviceSerialnumber, false);
		}
		if (!StringUtil.IsEmpty(deviceIp) && !"-1".equals(deviceIp))
		{
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp,
					false);
		}
		if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type))
		{
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		pSQL.append(" order by a.complete_time");
		return jt.query(pSQL.toString(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List<Map> queryZeroDevice(String cityId, String deviceSerialnumber,
			String loopbackIp, String username, String servAccount, boolean noChildCity,
			String startLastTime1, String endLastTime1, String addressingType,
			String status, String cpeMac, String failReason, String device_status,String loopbackIpSix)
	{
		return queryZeroDevice(-1, -1, cityId, deviceSerialnumber, loopbackIp, username,
				servAccount, noChildCity, startLastTime1, endLastTime1, addressingType,
				status, cpeMac, failReason, device_status, loopbackIpSix);
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List<Map> queryZeroDevice(int curPage_splitPage, int num_splitPage,
			String cityId, String sn, String loopbackIp, String username,
			String servAccount, boolean noChildCity, String startLastTime1,
			String endLastTime1, String addressingType, String status, String cpeMac,
			String failReason, String device_status,String loopbackIpSix)
	{
		PrepareSQL pSql = new PrepareSQL();
		if (Global.HNLT.equals(Global.instAreaShortName))
		{
			pSql.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.loopback_ip, a.city_id, a.complete_time, a.status, a.gw_type, " +
					" b.addressing_type,b.serv_account as hnlt_serv,b.cust_account"
					+ " from stb_tab_gw_device a left join stb_tab_customer b on a.customer_id=b.customer_id"
					+ " where 1=1");
		}
		else
		{
			pSql.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.loopback_ip, a.city_id, a.complete_time, a.status, a.gw_type, " +
					"b.addressing_type,a.serv_account,b.cust_account,b.pppoe_user"
					+ " from stb_tab_gw_device a left join stb_tab_customer b on a.customer_id=b.customer_id"
					+ " where 1=1");
		}
		if (!StringUtil.IsEmpty(failReason) && !"-1".equals(failReason))
		{// TODO wait (more table related)
			pSql.append(" and exists (select 1 from stb_tab_zeroconfig_fail c where b.customer_id = c.customer_id"
					+ " and c.fail_reason_id= " + failReason + ")");
		}
		if (!StringUtil.IsEmpty(username))
		{
			if (Global.NXDX.equals(Global.instAreaShortName))
			{
				pSql.append(" and b.pppoe_user='" + username + "'");
			}
			else
			{
				pSql.append(" and b.cust_account='" + username + "'");
			}
		}
		if (!StringUtil.IsEmpty(servAccount))
		{
			if (Global.HNLT.equals(Global.instAreaShortName))
			{
				pSql.append(" and b.serv_account = '" + servAccount + "'");
			}
			else
			{
				pSql.append(" and a.serv_account = '" + servAccount + "'");
			}
		}
		else
		{
			// 宁夏需求 NXDX-ITMS-20180730-LX-001（机顶盒查询统计改造）
			if (Global.NXDX.equals(Global.instAreaShortName))
			{
				pSql.append(" and a.serv_account is not null");
			}
			else
			{
				pSql.append("  ");
			}
		}
		if (!StringUtil.IsEmpty(sn))
		{
			if (sn.length() > 5)
			{
				pSql.append(" and a.dev_sub_sn ='");
				pSql.append(sn.substring(sn.length() - 6, sn.length()));
				pSql.append("'");
			}
			pSql.append(" and a.device_serialnumber like '%");
			pSql.append(sn);
			pSql.append("' ");
		}
		if (noChildCity)
		{
			pSql.append(" and a.city_id='");
			pSql.append(cityId);
			pSql.append("' ");
		}
		else
		{
			if (!CityDAO.isAdmin(cityId))
			{
				ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				pSql.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','")
						+ "')");
				cityArray = null;
			}
		}
		if (!StringUtil.IsEmpty(loopbackIp) && !"-1".equals(loopbackIp))
		{
			pSql.append(" and a.loopback_ip = '" + loopbackIp + "' ");
		}
		if (!StringUtil.IsEmpty(loopbackIpSix) && !"-1".equals(loopbackIpSix))
		{
			pSql.append(" and a.loopback_ip_six = '" + loopbackIpSix + "' ");
		}
		if (!StringUtil.IsEmpty(addressingType) && !"-1".equals(addressingType))
		{
			pSql.append(" and b.addressing_type = '" + addressingType + "'");
		}
		if (!StringUtil.IsEmpty(startLastTime1) && !"0".equals(startLastTime1))
		{
			pSql.append(" and a.complete_time >= " + startLastTime1);
		}
		if (!StringUtil.IsEmpty(endLastTime1) && !"0".equals(endLastTime1))
		{
			pSql.append(" and a.complete_time <= " + endLastTime1);
		}
		if (!StringUtil.IsEmpty(status) && !"-1".equals(status))
		{
			pSql.append(" and a.status = " + status);
		}
		if (!StringUtil.IsEmpty(cpeMac))
		{
			pSql.append(" and a.cpe_mac = '" + cpeMac + "'");
		}
		if (Global.HBLT.equals(Global.instAreaShortName)
				&& !device_status.equals("-1"))
		{
			pSql.append(" and a.device_status = '" + device_status + "'");
		}
		if (-1 != curPage_splitPage)
		{
			return querySP(pSql.getSQL(), (curPage_splitPage - 1) * num_splitPage,
					num_splitPage, new RowMapper()
					{

						List<Map> vendorList = queryAllVendor();
						List<Map> modelList = queryAllModel();
						List<Map> typeInfoList = queryAllTypeInfo();

						public Object mapRow(ResultSet rs, int arg1) throws SQLException
						{
							Map<String, String> map = new HashMap<String, String>();
							return resultSet2Map(map, rs, true, vendorList, modelList,
									typeInfoList);
						}
					});
		}
		else
		{
			return jt.query(pSql.getSQL(), new RowMapper()
			{

				List<Map> vendorList = queryAllVendor();
				List<Map> modelList = queryAllModel();
				List<Map> typeInfoList = queryAllTypeInfo();

				public Object mapRow(ResultSet rs, int arg1) throws SQLException
				{
					Map<String, String> map = new HashMap<String, String>();
					return resultSet2Map(map, rs, true, vendorList, modelList,
							typeInfoList);
				}
			});
		}
	}

	/**
	 * 查询设备列表(条数)(根据设备序列号和设备IP用or以及like来匹配)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public int queryZeroDeviceCount(String cityId, String username, String servAccount,
			String deviceSerialnumber, String loopbackIp, boolean noChildCity,
			String startLastTime1, String endLastTime1, String addressingType,
			String status, String cpeMac, String failReason, String device_status,String loopbackIpSix)
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select count(*) from stb_tab_gw_device a left join stb_tab_customer b on a.customer_id=b.customer_id where 1=1 ");
		if (!StringUtil.IsEmpty(failReason) && !"-1".equals(failReason))
		{// TODO wait (more table related)
			pSql.append(" and exists (select 1 from stb_tab_zeroconfig_fail c where b.customer_id = c.customer_id"
					+ " and c.fail_reason_id= " + failReason + ")");
		}
		if (!StringUtil.IsEmpty(username))
		{
			if (Global.NXDX.equals(Global.instAreaShortName))
			{
				pSql.append(" and b.pppoe_user= '" + username + "'");
			}
			else
			{
				pSql.append(" and b.cust_account= '" + username + "'");
			}
		}
		if (!StringUtil.IsEmpty(servAccount))
		{
			pSql.append(" and a.serv_account = '" + servAccount + "'");
		}
		if (!StringUtil.IsEmpty(deviceSerialnumber))
		{
			if (deviceSerialnumber.length() > 5)
			{
				pSql.append(" and a.dev_sub_sn ='");
				pSql.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSql.append("'");
			}
			pSql.append(" and a.device_serialnumber like '%");
			pSql.append(deviceSerialnumber);
			pSql.append("' ");
		}
		if (noChildCity)
		{
			pSql.append(" and a.city_id='");
			pSql.append(cityId);
			pSql.append("' ");
		}
		else
		{
			if (!CityDAO.isAdmin(cityId))
			{
				ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				pSql.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','")
						+ "')");
				cityArray = null;
			}
		}
		if (!StringUtil.IsEmpty(loopbackIp) && !"-1".equals(loopbackIp))
		{
			pSql.append(" and a.loopback_ip = '" + loopbackIp + "' ");
		}
		if (!StringUtil.IsEmpty(loopbackIpSix) && !"-1".equals(loopbackIpSix))
		{
			pSql.append(" and a.loopback_ip_six = '" + loopbackIpSix + "' ");
		}
		if (!StringUtil.IsEmpty(addressingType) && !"-1".equals(addressingType))
		{
			pSql.append(" and b.addressing_type = '" + addressingType + "'");
		}
		if (!StringUtil.IsEmpty(startLastTime1) && !"0".equals(startLastTime1))
		{
			pSql.append(" and a.complete_time >= " + startLastTime1);
		}
		if (!StringUtil.IsEmpty(endLastTime1) && !"0".equals(endLastTime1))
		{
			pSql.append(" and a.complete_time <= " + endLastTime1);
		}
		if (!StringUtil.IsEmpty(status) && !"-1".equals(status))
		{
			pSql.append(" and a.status = " + status);
		}
		if (!StringUtil.IsEmpty(cpeMac))
		{
			pSql.append(" and a.cpe_mac = '" + cpeMac + "'");
		}
		if (Global.HBLT.equals(Global.instAreaShortName)
				&& !device_status.equals("-1"))
		{
			pSql.append(" and a.device_status = '" + device_status + "'");
		}
		return jt.queryForInt(pSql.getSQL());
	}

	/**
	 * 查询所有厂商
	 *
	 * @return
	 */
	private List<Map> queryAllVendor()
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select vendor_id, vendor_add from stb_tab_vendor");
		return querySP(pSql.getSQL(), 0, 1000);
	}

	/**
	 * 转换厂商显示
	 *
	 * @param vendorList
	 * @param vendorID
	 * @return
	 */
	public String getVendorNameByVendorID(List<Map> vendorList, String vendorID)
	{
		if (null == vendorList || StringUtil.IsEmpty(vendorID)
				|| "null".equals(vendorID.trim()))
		{
			return vendorID;
		}
		for (Map map : vendorList)
		{
			if (vendorID.equals(StringUtil.getStringValue(map, "vendor_id")))
			{
				// return StringUtil.getStringValue(map, "vendor_add") + "(" +
				// StringUtil.getStringValue(map, "vendor_name") + ")";
				return StringUtil.getStringValue(map, "vendor_add");
			}
		}
		return vendorID;
	}

	/**
	 * 查询所有型号
	 *
	 * @return
	 */
	private List<Map> queryAllModel()
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select device_model_id, device_model from stb_gw_device_model");
		return querySP(pSql.getSQL(), 0, 10000);
	}

	/**
	 * 转换型号显示
	 *
	 * @param modelList
	 * @param modelID
	 * @return
	 */
	public String getModelNameByModelID(List<Map> modelList, String modelID)
	{
		if (null == modelList || StringUtil.IsEmpty(modelID)
				|| "null".equals(modelID.trim()))
		{
			return modelID;
		}
		for (Map map : modelList)
		{
			if (modelID.equals(StringUtil.getStringValue(map, "device_model_id")))
			{
				return StringUtil.getStringValue(map, "device_model");
			}
		}
		return modelID;
	}

	/**
	 * 查询所有设备类型
	 *
	 * @return
	 */
	private List<Map> queryAllTypeInfo()
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select devicetype_id, softwareversion from stb_tab_devicetype_info");
		return querySP(pSql.getSQL(), 0, 10000);
	}

	/**
	 * 转换软件版本
	 *
	 * @param typeInfoList
	 * @param devicetypeID
	 * @return
	 */
	public String getSoftVersionByTypeInfoID(List<Map> typeInfoList, String devicetypeID)
	{
		if (null == typeInfoList || StringUtil.IsEmpty(devicetypeID)
				|| "null".equals(devicetypeID.trim()))
		{
			return devicetypeID;
		}
		for (Map map : typeInfoList)
		{
			if (devicetypeID.equals(StringUtil.getStringValue(map, "devicetype_id")))
			{
				return StringUtil.getStringValue(map, "softwareversion");
			}
		}
		return devicetypeID;
	}

	/**
	 * 机顶盒信息导出
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> queryDeviceForExport(long areaId, String cityId, String vendorId,
			String deviceModelId, String devicetypeId, String bindType,
			String deviceSerialnumber, String deviceIp, String username,
			String servAccount, String startLastTime, String endLastTime,
			boolean noChildCity, String completeStartTime, String completeEndTime)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceForExport()");
		PrepareSQL pSQL = new PrepareSQL();
		boolean needAppendCustomer = false;
		if (StringUtil.IsEmpty(username) && StringUtil.IsEmpty(servAccount))
		{// TODO wait (more table related)
			pSQL.setSQL("select x.vendor_add,x.device_model,x.softwareversion,x.city_name,x.device_serialnumber,x.loopback_ip,e.serv_account,x.last_time " +
					"from (select a.device_serialnumber, a.loopback_ip, a.customer_id" +
					"	g.city_name,b.vendor_add,c.device_model,d.softwareversion,f.online_status,f.last_time " +
					"	from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus f  ,tab_city g " +
					"	where a.city_id=g.city_id and  a.device_id=f.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id " +
					"	and a.devicetype_id=d.devicetype_id ");
			needAppendCustomer = true;
		}
		else
		{
			if (!StringUtil.IsEmpty(username) && !StringUtil.IsEmpty(servAccount))
			{// TODO wait (more table related)
				pSQL.setSQL("select b.vendor_add,c.device_model,d.softwareversion,g.city_name,a.device_serialnumber,a.loopback_ip,a.serv_account,x.last_time " +
						" from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_tab_customer e,stb_gw_devicestatus f  ," +
						" tab_city g where a.city_id=g.city_id and  a.customer_id=e.customer_id and a.device_id=f.device_id and a.vendor_id=b.vendor_id " +
						" and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
				pSQL.append(PrepareSQL.AND, "e.cust_account", PrepareSQL.EQUEAL,
						username, false);
				pSQL.append(PrepareSQL.AND, "a.serv_account", PrepareSQL.EQUEAL,
						servAccount, false);
			}
			else if (!StringUtil.IsEmpty(username))
			{// TODO wait (more table related)
				pSQL.setSQL("select b.vendor_add,c.device_model,d.softwareversion,g.city_name,a.device_serialnumber,a.loopback_ip,a.serv_account,x.last_time " +
						" from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_tab_customer e,stb_gw_devicestatus f  ," +
						" tab_city g where a.city_id=g.city_id and  a.customer_id=e.customer_id and a.device_id=f.device_id and a.vendor_id=b.vendor_id " +
						" and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
				pSQL.append(PrepareSQL.AND, "e.cust_account", PrepareSQL.EQUEAL,
						username, false);
			}
			else if (!StringUtil.IsEmpty(servAccount))
			{// TODO wait (more table related)
				pSQL.setSQL("select b.vendor_add,c.device_model,d.softwareversion,g.city_name,a.device_serialnumber,a.loopback_ip,a.serv_account,x.last_time " +
						" from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus f ,tab_city g " +
						" where a.city_id=g.city_id and a.device_id=f.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id " +
						" and a.devicetype_id=d.devicetype_id ");
				pSQL.append(PrepareSQL.AND, "a.serv_account", PrepareSQL.EQUEAL,
						servAccount, false);
			}
		}
		if (!StringUtil.IsEmpty(startLastTime))
		{
			pSQL.append(" and f.last_time > ");
			pSQL.append(startLastTime);
		}
		if (!StringUtil.IsEmpty(endLastTime))
		{
			pSQL.append(" and f.last_time < ");
			pSQL.append(endLastTime);
		}
		if (noChildCity)
		{
			pSQL.append(" and a.city_id='");
			pSQL.append(cityId);
			pSQL.append("' ");
		}
		else
		{
			if (!CityDAO.isAdmin(cityId))
			{
				ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','")
						+ "')");
				cityArray = null;
			}
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId))
		{
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId))
		{
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
		}
		if (!StringUtil.IsEmpty(devicetypeId) && !"-1".equals(devicetypeId))
		{
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
		}
		if (!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType))
		{
			pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
		}
		if (!StringUtil.IsEmpty(deviceSerialnumber))
		{
			if (deviceSerialnumber.length() > 5)
			{
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("'");
			}
			pSQL.append(" and a.device_serialnumber like '%");
			pSQL.append(deviceSerialnumber);
			pSQL.append("' ");
		}
		if (!StringUtil.IsEmpty(deviceIp) && !"-1".equals(deviceIp))
		{
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp,
					false);
		}
		if (!"0".equals(completeStartTime))
		{
			pSQL.append(PrepareSQL.AND, "a.complete_time", PrepareSQL.BIGGEREQUEAL,
					completeStartTime, true);
		}
		if (!"0".equals(completeEndTime))
		{
			pSQL.append(PrepareSQL.AND, "a.complete_time", PrepareSQL.SMALLERQUEAL,
					completeEndTime, true);
		}
		if (needAppendCustomer)
		{
			pSQL.append(") x left join stb_tab_customer e on x.customer_id=e.customer_id");
		}
		return jt.query(pSQL.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map map = new HashMap();
				map.put("vendor_add",
						StringUtil.getStringValue(rs.getString("vendor_add")));
				map.put("device_model",
						StringUtil.getStringValue(rs.getString("device_model")));
				map.put("softwareversion",
						StringUtil.getStringValue(rs.getString("softwareversion")));
				map.put("city_name", StringUtil.getStringValue(rs.getString("city_name")));
				map.put("device_serialnumber",
						StringUtil.getStringValue(rs.getString("device_serialnumber")));
				map.put("loopback_ip",
						StringUtil.getStringValue(rs.getString("loopback_ip")));
				map.put("serv_account",
						StringUtil.getStringValue(rs.getString("serv_account")));
				// 将dealdate转换成时间
				try
				{
					long dealdate = StringUtil.getLongValue(rs.getInt("last_time"));
					com.linkage.litms.common.util.DateTimeUtil dt = new com.linkage.litms.common.util.DateTimeUtil(
							dealdate * 1000);
					map.put("last_time", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("last_time", "");
				}
				catch (Exception e)
				{
					map.put("last_time", "");
				}
				return map;
			}
		});
	}

	/**
	 * 查询设备列表(条数)(根据设备序列号和设备IP用or以及like来匹配)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public int queryDeviceCount(long areaId, String cityId, String vendorId,
			String deviceModelId, String devicetypeId, String bindType,
			String deviceSerialnumber, String deviceIp, boolean noChildCity,
			String completeStartTime, String completeEndTime, String startLastTime,
			String endLastTime)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceCount()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select count(*) from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d ,stb_gw_devicestatus f where a.device_id=f.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (noChildCity)
		{
			pSQL.append(" and a.city_id='");
			pSQL.append(cityId);
			pSQL.append("' ");
		}
		else
		{
			if (!CityDAO.isAdmin(cityId))
			{
				ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','")
						+ "')");
				cityArray = null;
			}
		}
		if (!StringUtil.IsEmpty(startLastTime))
		{
			pSQL.append(" and f.last_time > ");
			pSQL.append(startLastTime);
		}
		if (!StringUtil.IsEmpty(endLastTime))
		{
			pSQL.append(" and f.last_time < ");
			pSQL.append(endLastTime);
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId))
		{
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId))
		{
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
		}
		if (!StringUtil.IsEmpty(devicetypeId) && !"-1".equals(devicetypeId))
		{
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
		}
		if (!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType))
		{
			pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
		}
		if (!StringUtil.IsEmpty(deviceSerialnumber))
		{
			if (deviceSerialnumber.length() > 5)
			{
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("'");
			}
			pSQL.append(" and a.device_serialnumber like '%");
			pSQL.append(deviceSerialnumber);
			pSQL.append("' ");
		}
		if (!StringUtil.IsEmpty(deviceIp) && !"-1".equals(deviceIp))
		{
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp,
					false);
		}
		if (!"0".equals(completeStartTime))
		{
			pSQL.append(PrepareSQL.AND, "a.complete_time", PrepareSQL.BIGGEREQUEAL,
					completeStartTime, true);
		}
		if (!"0".equals(completeEndTime))
		{
			pSQL.append(PrepareSQL.AND, "a.complete_time", PrepareSQL.SMALLERQUEAL,
					completeEndTime, true);
		}
		return jt.queryForInt(pSQL.getSQL());
	}

	/**
	 * 查询设备列表(关联设备状态表查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDeviceByLikeStatus(int curPage_splitPage, int num_splitPage,
			long areaId, String cityId, String vendorId, String deviceModelId,
			String devicetypeId, String bindType, String deviceSerialnumber,
			String deviceIp, String onlineStatus, boolean noChildCity)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatus()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select a.device_id,b.vendor_add,c.device_model,d.softwareversion from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e where a.device_id=e.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (noChildCity)
		{
			pSQL.append(" and a.city_id='");
			pSQL.append(cityId);
			pSQL.append("' ");
		}
		else
		{
			if (!CityDAO.isAdmin(cityId))
			{
				ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','")
						+ "')");
				cityArray = null;
			}
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId))
		{
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId))
		{
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
		}
		if (!StringUtil.IsEmpty(devicetypeId) && !"-1".equals(devicetypeId))
		{
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
		}
		if (!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType))
		{
			pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
		}
		if (!StringUtil.IsEmpty(deviceSerialnumber))
		{
			if (deviceSerialnumber.length() > 5)
			{
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("'");
			}
			pSQL.append(" and a.device_serialnumber like '%");
			pSQL.append(deviceSerialnumber);
			pSQL.append("' ");
		}
		if (!StringUtil.IsEmpty(deviceIp) && !"-1".equals(deviceIp))
		{
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp,
					false);
		}
		return querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage,
				num_splitPage, new RowMapper()
				{

					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						return resultSet2Map(map, rs);
					}
				});
	}

	/**
	 * 查询设备列表(关联设备状态表查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public int queryDeviceByLikeStatusCount(long areaId, String cityId, String vendorId,
			String deviceModelId, String devicetypeId, String bindType,
			String deviceSerialnumber, String deviceIp, String onlineStatus,
			boolean noChildCity)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatus()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select count(*) from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e where a.device_id=e.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (noChildCity)
		{
			pSQL.append(" and a.city_id='");
			pSQL.append(cityId);
			pSQL.append("' ");
		}
		else
		{
			if (!CityDAO.isAdmin(cityId))
			{
				ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','")
						+ "')");
				cityArray = null;
			}
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId))
		{
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId))
		{
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
		}
		if (!StringUtil.IsEmpty(devicetypeId) && !"-1".equals(devicetypeId))
		{
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
		}
		if (!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType))
		{
			pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
		}
		if (!StringUtil.IsEmpty(deviceSerialnumber))
		{
			if (deviceSerialnumber.length() > 5)
			{
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("'");
			}
			pSQL.append(" and a.device_serialnumber like '%");
			pSQL.append(deviceSerialnumber);
			pSQL.append("' ");
		}
		if (!StringUtil.IsEmpty(deviceIp) && !"-1".equals(deviceIp))
		{
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp,
					false);
		}
		return jt.queryForInt(pSQL.getSQL());
	}

	/**
	 * 查询设备列表(关联用户表查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDeviceByUsername(int curPage_splitPage, int num_splitPage,
			long areaId, String cityId, String vendorId, String deviceModelId,
			String devicetypeId, String bindType, String deviceSerialnumber,
			String deviceIp, String username, String servAccount, boolean noChildCity)
	{
		logger.debug("GwDeviceQueryDAO=>queryDevice()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select  a.device_id, a.oui, a.device_serialnumber, a.loopback_ip, a.city_id, a.complete_time, a.status, a.gw_type, " +
				"b.vendor_add,c.device_model,d.softwareversion,e.cust_account,e.serv_account from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_tab_customer e where a.customer_id=e.customer_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (noChildCity)
		{
			pSQL.append(" and a.city_id='");
			pSQL.append(cityId);
			pSQL.append("' ");
		}
		else
		{
			if (!CityDAO.isAdmin(cityId))
			{
				ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','")
						+ "')");
				cityArray = null;
			}
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId))
		{
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId))
		{
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
		}
		if (!StringUtil.IsEmpty(devicetypeId) && !"-1".equals(devicetypeId))
		{
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
		}
		if (!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType))
		{
			pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
		}
		if (!StringUtil.IsEmpty(deviceSerialnumber))
		{
			if (deviceSerialnumber.length() > 5)
			{
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("'");
			}
			pSQL.append(" and a.device_serialnumber like '%");
			pSQL.append(deviceSerialnumber);
			pSQL.append("' ");
		}
		if (!StringUtil.IsEmpty(deviceIp))
		{
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp,
					false);
		}
		if (!StringUtil.IsEmpty(username))
		{
			pSQL.append(PrepareSQL.AND, "e.cust_account", PrepareSQL.EQUEAL, username,
					false);
		}
		if (!StringUtil.IsEmpty(servAccount))
		{
			pSQL.append(PrepareSQL.AND, "e.serv_account", PrepareSQL.EQUEAL, servAccount,
					false);
		}
		return querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage,
				num_splitPage, new RowMapper()
				{

					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						return resultSet2Map(map, rs);
					}
				});
	}

	/**
	 * 查询设备列表(条数)(关联用户表查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public int queryDeviceByUsernameCount(long areaId, String cityId, String vendorId,
			String deviceModelId, String devicetypeId, String bindType,
			String deviceSerialnumber, String deviceIp, String username,
			String servAccount, boolean noChildCity, String startLastTime,
			String endLastTime)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceCount()");
		PrepareSQL pSQL = new PrepareSQL();
		// pSQL.setSQL("select count(*) from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_tab_customer e where a.customer_id=e.customer_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (StringUtil.IsEmpty(username) && StringUtil.IsEmpty(servAccount))
		{// TODO wait (more table related)
			pSQL.setSQL("select count(*) from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d," +
					" stb_gw_devicestatus f where a.device_id=f.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id " +
					" and a.devicetype_id=d.devicetype_id ");
		}
		else
		{
			if (!StringUtil.IsEmpty(username) && !StringUtil.IsEmpty(servAccount))
			{// TODO wait (more table related)
				pSQL.setSQL("select count(*) from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_tab_customer e,stb_gw_devicestatus f where a.customer_id=e.customer_id and a.device_id=f.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
				pSQL.append(PrepareSQL.AND, "e.cust_account", PrepareSQL.EQUEAL,
						username, false);
				pSQL.append(PrepareSQL.AND, "a.serv_account", PrepareSQL.EQUEAL,
						servAccount, false);
			}
			else if (!StringUtil.IsEmpty(username))
			{// TODO wait (more table related)
				pSQL.setSQL("select count(*) from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_tab_customer e,stb_gw_devicestatus f where a.customer_id=e.customer_id and a.device_id=f.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
				pSQL.append(PrepareSQL.AND, "e.cust_account", PrepareSQL.EQUEAL,
						username, false);
			}
			else if (!StringUtil.IsEmpty(servAccount))
			{
				pSQL.setSQL("select count(*) from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus f where a.device_id=f.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
				pSQL.append(PrepareSQL.AND, "a.serv_account", PrepareSQL.EQUEAL,
						servAccount, false);
			}
		}
		if (!StringUtil.IsEmpty(startLastTime))
		{
			pSQL.append(" and f.last_time > ");
			pSQL.append(startLastTime);
		}
		if (!StringUtil.IsEmpty(endLastTime))
		{
			pSQL.append(" and f.last_time < ");
			pSQL.append(endLastTime);
		}
		if (noChildCity)
		{
			pSQL.append(" and a.city_id='");
			pSQL.append(cityId);
			pSQL.append("' ");
		}
		else
		{
			if (!CityDAO.isAdmin(cityId))
			{
				ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','")
						+ "')");
				cityArray = null;
			}
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId))
		{
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId))
		{
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
		}
		if (!StringUtil.IsEmpty(devicetypeId) && !"-1".equals(devicetypeId))
		{
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
		}
		if (!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType))
		{
			pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
		}
		if (!StringUtil.IsEmpty(deviceSerialnumber))
		{
			if (deviceSerialnumber.length() > 5)
			{
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("'");
			}
			pSQL.append(" and a.device_serialnumber like '%");
			pSQL.append(deviceSerialnumber);
			pSQL.append("' ");
		}
		if (!StringUtil.IsEmpty(deviceIp))
		{
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp,
					false);
		}
		return jt.queryForInt(pSQL.getSQL());
	}

	/**
	 * 获取状态List
	 *
	 * @return
	 */
	public List<Map<String, String>> getStatusList()
	{
		List<Map<String, String>> statusList = new ArrayList<Map<String, String>>();
		if(Global.NXDX.equals(Global.instAreaShortName))
		{
			// 宁夏定制 NXDX-ITMS-20180730-LX-001（机顶盒查询统计改造）
			Map<String, String> status = new HashMap<String, String>();
			status.put("status_id", "0");
			status.put("status_name", "手工配置");
			Map<String, String> status1 = new HashMap<String, String>();
			status1.put("status_id", "9");
			status1.put("status_name", "零配置");
			statusList.add(status);
			statusList.add(status1);
		}
		else if (Global.SXLT.equals(Global.instAreaShortName))
		{
			// 2020/05/08山西联通机顶盒状态只保留0和9
			Map<String, String> status = new HashMap<String, String>();
			status.put("status_id", "0");
			status.put("status_name", "新机顶盒");
			Map<String, String> status1 = new HashMap<String, String>();
			status1.put("status_id", "9");
			status1.put("status_name", "自动配置中");
			statusList.add(status);
			statusList.add(status1);
		}
		else
		{
			// 有10种状态
			for (int i = 0; i < 10; i++)
			{
				String key = "" + i;
				Map<String, String> status = new HashMap<String, String>();
				status.put("status_id", key);
				status.put("status_name", parseStatusByCode(key));
				statusList.add(status);
			}
		}
		return statusList;
	}

	/**
	 * 解析设备状态字段
	 *
	 * @param status
	 * @return
	 */
	public String parseStatusByCode(String status)
	{
		// 为空则返回空
		if (StringUtil.IsEmpty(status) || "null".equals(status.trim()))
		{
			return "";
		}
		int code = Integer.parseInt(status);
		String str = null;
		switch (code)
		{
			case 0:
				str = "新机顶盒";
				break;
			case 1:
				str = "老机顶盒";
				break;
			case 2:
				str = "维修中";
				break;
			case 3:
				str = "vip用户";
				break;
			case 4:
				str = "升版失败";
				break;
			case 5:
				str = "串号配置";
				break;
			case 6:
				str = "零配置失败";
				break;
			case 7:
				str = "移机";
				break;
			case 8:
				str = "销户";
				break;
			case 9:
				str = "自动配置中";
				break;
		}
		return str;
	}

	/**
	 * 数据转换
	 *
	 * @param map
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public Map<String, String> resultSet2Map(Map<String, String> map, ResultSet rs)
	{
		return resultSet2Map(map, rs, false, null, null, null);
	}

	/**
	 * 数据转换 (废弃)
	 *
	 * @param map
	 * @param rs
	 * @param isZero
	 *            零配置查询方式不一样，需要配置
	 * @return
	 * @throws SQLException
	 */
	public Map<String, String> resultSet2MapNX(Map<String, String> map,
			Map<String, String> rs, boolean isZero, List<Map> vendorList,
			List<Map> modelList, List<Map> typeInfoList)
	{
		map.put("device_id", rs.get("device_id"));
		map.put("oui", rs.get("oui"));
		map.put("device_serialnumber", rs.get("device_serialnumber"));
		map.put("device_name", rs.get("device_name"));
		map.put("city_id", rs.get("city_id"));
		map.put("city_name", CityDAO.getCityIdCityNameMap().get(rs.get("city_id")));
		map.put("office_id", rs.get("office_id"));
		map.put("complete_time", new DateTimeUtil(
				Long.valueOf(rs.get("complete_time")) * 1000).getYYYY_MM_DD_HH_mm_ss());
		map.put("zone_id", rs.get("zone_id"));
		map.put("buy_time", new DateTimeUtil(Long.valueOf(rs.get("buy_time")) * 1000)
				.getYYYY_MM_DD_HH_mm_ss());
		map.put("staff_id", rs.get("staff_id"));
		map.put("remark", rs.get("remark"));
		map.put("loopback_ip", rs.get("loopback_ip"));
		map.put("interface_id", rs.get("interface_id"));
		map.put("device_status", rs.get("device_status"));
		map.put("gather_id", rs.get("gather_id"));
		map.put("devicetype_id", rs.get("devicetype_id"));
		// 零配置不关联
		if (isZero)
		{
			map.put("softwareversion",
					getSoftVersionByTypeInfoID(typeInfoList, rs.get("devicetype_id")));
		}
		else
		{
			map.put("softwareversion", rs.get("softwareversion"));
		}
		map.put("maxenvelopes", rs.get("maxenvelopes"));
		map.put("addressing_type", rs.get("addressing_type"));
		map.put("status", parseStatusByCode(rs.get("status")));
		map.put("cr_port", rs.get("cr_port"));
		map.put("cr_path", rs.get("cr_path"));
		map.put("cpe_mac", rs.get("cpe_mac"));
		map.put("cpe_currentupdatetime",
				new DateTimeUtil(Long.valueOf(rs.get("cpe_currentupdatetime")) * 1000)
						.getYYYY_MM_DD_HH_mm_ss());
		map.put("cpe_allocatedstatus", rs.get("cpe_allocatedstatus"));
		map.put("cpe_username", rs.get("cpe_username"));
		map.put("cpe_passwd", rs.get("cpe_passwd"));
		map.put("acs_username", rs.get("acs_username"));
		map.put("acs_passwd", rs.get("acs_passwd"));
		map.put("device_type", rs.get("device_type"));
		map.put("x_com_username", rs.get("x_com_username"));
		map.put("x_com_passwd", rs.get("x_com_passwd"));
		map.put("gw_type", rs.get("gw_type"));
		map.put("device_model_id", rs.get("device_model_id"));
		// 零配置不关联表
		if (isZero)
		{
			map.put("device_model",
					getModelNameByModelID(modelList, rs.get("device_model_id")));
		}
		else
		{
			map.put("device_model", rs.get("device_model"));
		}
		map.put("customer_id", rs.get("customer_id"));
		map.put("device_url", rs.get("device_url"));
		map.put("x_com_passwd_old", rs.get("x_com_passwd_old"));
		map.put("vendor_id", rs.get("vendor_id"));
		// 零配置不关联表
		if (isZero)
		{
			map.put("vendor_add",
					getVendorNameByVendorID(vendorList, rs.get("vendor_id")));
		}
		else
		{
			map.put("vendor_add", rs.get("vendor_add"));
		}
		map.put("bind_time", new DateTimeUtil(Long.valueOf(rs.get("bind_time")) * 1000)
				.getYYYY_MM_DD_HH_mm_ss());
		map.put("cust_account", rs.get("cust_account"));
		if (Global.HNLT.equals(Global.instAreaShortName))
		{
			map.put("serv_account", rs.get("hnlt_serv"));
		}
		else
		{
			map.put("serv_account", rs.get("serv_account"));
			map.put("pppoe_user", rs.get("pppoe_user"));
		}
		map.put("inform_stat", "1".equals(rs.get("inform_stat")) ? "可管理" : "不可管理");
		map.put("online_status", "1".equals(rs.get("online_status")) ? "在线" : "不在线");
		// 将dealdate转换成时间
		try
		{
			long dealdate = StringUtil.getLongValue(rs.get("last_time"));
			com.linkage.litms.common.util.DateTimeUtil dt = new com.linkage.litms.common.util.DateTimeUtil(
					dealdate * 1000);
			map.put("last_time", dt.getLongDate());
		}
		catch (NumberFormatException e)
		{
			map.put("last_time", "");
		}
		catch (Exception e)
		{
			map.put("last_time", "");
		}
		return map;
	}

	/**
	 * 删除指定的设备并通知ACS
	 *
	 * @param deviceId
	 *            :设备ID
	 * @author Jason(3412)
	 * @date 2010-11-9
	 * @return int
	 */
	public int deleteDevice(String deviceId)
	{
		if (StringUtil.IsEmpty(deviceId))
		{
			return 0;
		}
		String[] sqlArr = new String[2];
		if ("1".equals(LipossGlobals.getLipossProperty("isDelTr111Table")))
		{
			sqlArr = new String[3];
		}
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(" delete from stb_gw_devicestatus where device_id =?");
		pSQL.setString(1, deviceId);
		sqlArr[0] = pSQL.getSQL();
		pSQL.setSQL("delete from stb_tab_gw_device where device_id =?");
		pSQL.setString(1, deviceId);
		sqlArr[1] = pSQL.getSQL();
		if ("1".equals(LipossGlobals.getLipossProperty("isDelTr111Table")))
		{
			pSQL.setSQL(" delete from stb_tab_device_addressinfo where device_id =?");
			pSQL.setString(1, deviceId);
			sqlArr[2] = pSQL.getSQL();
		}
		return jt.batchUpdate(sqlArr)[1];
	}

	public List<Map> querygwZeroDetail(String device_id)
	{
		PrepareSQL pSql = new PrepareSQL();
		if (LipossGlobals.isOracle())
		{
			pSql.append("select a.serv_account,b.fail_time,b.return_value,c.reason_desc from stb_tab_gw_device a,stb_tab_zeroconfig_fail b, " +
					"stb_tab_zeroconfig_reason c where a.device_id=to_char(b.device_id) and b.fail_reason_id= c.reason_id and b.device_id= ");
		}
		else if (DBUtil.GetDB() == 3)
		{// TODO wait (more table related)
			pSql.append("select a.serv_account,b.fail_time,b.return_value,c.reason_desc from stb_tab_gw_device a,stb_tab_zeroconfig_fail b, " +
					"stb_tab_zeroconfig_reason c where a.device_id=cast(b.device_id as char) and b.fail_reason_id= c.reason_id and b.device_id= ");
		}
		else
		{
			pSql.append("select a.serv_account,b.fail_time,b.return_value,c.reason_desc from stb_tab_gw_device a,stb_tab_zeroconfig_fail b, " +
					"stb_tab_zeroconfig_reason c where a.device_id=convert(varchar,b.device_id) and b.fail_reason_id= c.reason_id and b.device_id= ");
		}
		pSql.append(device_id);
		return jt.query(pSql.getSQL(), new ZerogwRowMapper());
	}

	/**
	 * 江西电信零配置信息
	 *
	 * @param device_id
	 * @return
	 */
	public List<Map> querygwZeroDetailTo3(String device_id)
	{
		PrepareSQL pSql = new PrepareSQL();
		if (LipossGlobals.isOracle())
		{
			pSql.append("select * from (select a.serv_account,a.city_id,a.device_serialnumber,b.buss_id,b.start_time," +
					" b.fail_time,b.bind_way,b.fail_reason_id,b.return_value,c.reason_desc " +
					" from stb_tab_gw_device a, stb_tab_zeroconfig_fail b, stb_tab_zeroconfig_reason c " +
					" where a.device_id=to_char(b.device_id) and b.fail_reason_id= c.reason_id and b.device_id= ");
			pSql.append(device_id);
			pSql.append(" order by b.fail_time desc) where rownum <=3");
		}
		else if (DBUtil.GetDB() == 3)
		{// TODO wait (more table related)
			pSql.append("select a.serv_account,a.city_id,a.device_serialnumber,b.buss_id,b.start_time," +
					" b.fail_time,b.bind_way,b.fail_reason_id,b.return_value,c.reason_desc " +
					" from stb_tab_gw_device a, stb_tab_zeroconfig_fail b, stb_tab_zeroconfig_reason c " +
					" where a.device_id=cast(b.device_id as char) and b.fail_reason_id= c.reason_id and b.device_id= ");
			pSql.append(device_id);
			pSql.append(" order by b.fail_time desc limit 3");
		}
		else
		{
			pSql.append("select top 3 a.serv_account,a.city_id,a.device_serialnumber,b.buss_id,b.start_time," +
					" b.fail_time,b.bind_way,b.fail_reason_id,b.return_value,c.reason_desc " +
					" from stb_tab_gw_device a,stb_tab_zeroconfig_fail b, stb_tab_zeroconfig_reason c " +
					" where a.device_id=convert(varchar,b.device_id) and b.fail_reason_id= c.reason_id and b.device_id= ");
			pSql.append(device_id);
			pSql.append(" order by b.fail_time desc");
		}
		return jt.query(pSql.getSQL(), new ZerogwRowMapperPage());
	}

	public List<Map> querygwZeroDetailPage(int curPageSplitPage, int numSplitPage,
			String deviceId)
	{
		PrepareSQL pSql = new PrepareSQL();

		if (LipossGlobals.isOracle())
		{
			pSql.append("select a.serv_account,a.city_id,a.device_serialnumber,b.buss_id,b.start_time,b.fail_time,b.bind_way," +
					" b.fail_reason_id,b.return_value,c.reason_desc " +
					" from stb_tab_gw_device a,stb_tab_zeroconfig_fail b, stb_tab_zeroconfig_reason c " +
					" where a.device_id=to_char(b.device_id) and b.fail_reason_id= c.reason_id and b.device_id= ");
		}
		else if (DBUtil.GetDB() == 3)
		{// TODO wait (more table related)
			pSql.append("select a.serv_account,a.city_id,a.device_serialnumber,b.buss_id,b.start_time,b.fail_time,b.bind_way," +
					" b.fail_reason_id,b.return_value,c.reason_desc " +
					" from stb_tab_gw_device a,stb_tab_zeroconfig_fail b, stb_tab_zeroconfig_reason c " +
					" where a.device_id=cast(b.device_id as char) and b.fail_reason_id= c.reason_id and b.device_id= ");
		}
		else
		{
			pSql.append("select a.serv_account,a.city_id,a.device_serialnumber,b.buss_id,b.start_time,b.fail_time,b.bind_way," +
					" b.fail_reason_id,b.return_value,c.reason_desc " +
					" from stb_tab_gw_device a,stb_tab_zeroconfig_fail b, stb_tab_zeroconfig_reason c " +
					" where a.device_id=convert(varchar,b.device_id) and b.fail_reason_id= c.reason_id and b.device_id= ");
		}
		pSql.append(deviceId);
		pSql.append(" order by b.fail_time desc");
		if (-1 != curPageSplitPage)
		{
			return querySP(pSql.getSQL(), (curPageSplitPage - 1) * numSplitPage,
					numSplitPage, new ZerogwRowMapperPage());
		}
		else
		{
			return jt.query(pSql.getSQL(), new ZerogwRowMapperPage());
		}
	}

	public int querygwZeroDetailCount(String deviceId)
	{
		PrepareSQL pSql = new PrepareSQL();
		if (LipossGlobals.isOracle())
		{
			pSql.append("select count(*) from stb_tab_gw_device a,stb_tab_zeroconfig_fail b,"
					+ "stb_tab_zeroconfig_reason c where a.device_id=to_char(b.device_id) and b.fail_reason_id= c.reason_id and b.device_id= ");
		}
		else if (DBUtil.GetDB() == 3)
		{// TODO wait (more table related)
			pSql.append("select count(*) from stb_tab_gw_device a,stb_tab_zeroconfig_fail b,"
					+ "stb_tab_zeroconfig_reason c where a.device_id=cast(b.device_id as char) and b.fail_reason_id= c.reason_id and b.device_id= ");
		}
		else
		{
			pSql.append("select count(*) from stb_tab_gw_device a,stb_tab_zeroconfig_fail b,"
					+ "stb_tab_zeroconfig_reason c where a.device_id=convert(varchar,b.device_id) and b.fail_reason_id= c.reason_id and b.device_id= ");
		}
		pSql.append(deviceId);
		return jt.queryForInt(pSql.getSQL());
	}

	private static class ZerogwRowMapper implements RowMapper
	{

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Map<String, String> result = new HashMap<String, String>();
			result.put("serv_account", rs.getString("serv_account"));
			result.put("fail_time",
					DateUtil.transTime(rs.getLong("fail_time"), "yyyy-MM-dd HH:mm:ss"));
			result.put("return_value", rs.getString("return_value"));
			result.put("reason_desc", rs.getString("reason_desc"));
			return result;
		}
	}

	private static class ZerogwRowMapperPage implements RowMapper
	{

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Map<String, String> result = new HashMap<String, String>();
			result.put("serv_account", rs.getString("serv_account"));
			result.put("city_id", rs.getString("city_id"));
			result.put("device_serialnumber", rs.getString("device_serialnumber"));
			result.put("buss_id", rs.getString("buss_id"));
			result.put("start_time",
					DateUtil.transTime(rs.getLong("start_time"), "yyyy-MM-dd HH:mm:ss"));
			result.put("fail_time",
					DateUtil.transTime(rs.getLong("fail_time"), "yyyy-MM-dd HH:mm:ss"));
			result.put("bind_way", rs.getString("bind_way"));
			result.put("fail_reason_id", rs.getString("fail_reason_id"));
			result.put("return_value", rs.getString("return_value"));
			result.put("reason_desc", rs.getString("reason_desc"));
			return result;
		}
	}

	public List<Map> querygwWorkDetail(String device_id)
	{// TODO wait (more table related)
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("select a.serv_account,a.pppoe_user,a.cust_account,a.browser_url1,a.browser_url2,a.cpe_mac,");
		pSql.append("a.addressing_type,c.username from stb_tab_customer a ");
		pSql.append("left join hgwcust_serv_info b on (a.pppoe_user=b.username and b.serv_type_id=11) ");
		pSql.append("left join tab_hgwcustomer c on b.user_id=c.user_id ,stb_tab_gw_device d ");
		pSql.append("where a.customer_id = d.customer_id ");
		if (!StringUtil.IsEmpty(device_id))
		{
			pSql.append("and d.device_id= '" + device_id + "'");
		}
		return jt.query(pSql.getSQL(), new WorkRowMapper());
	}

	private static class WorkRowMapper implements RowMapper
	{

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Map<String, String> result = new HashMap<String, String>();
			result.put("serv_account", rs.getString("serv_account"));
			result.put("pppoe_user", rs.getString("pppoe_user"));
			result.put("username", rs.getString("username"));
			result.put("browser_url1", rs.getString("browser_url1"));
			result.put("browser_url2", rs.getString("browser_url2"));
			result.put("cpe_mac", rs.getString("cpe_mac"));
			result.put("addressing_type", rs.getString("addressing_type"));
			return result;
		}
	}

	public List<Map> queryFailReason()
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("select reason_id,reason_desc from stb_tab_zeroconfig_reason");
		return jt.query(pSql.getSQL(), new FailReasonMapper());
	}

	private static class FailReasonMapper implements RowMapper
	{

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Map<String, String> result = new HashMap<String, String>();
			result.put("reason_id", rs.getString("reason_id"));
			result.put("reason_desc", rs.getString("reason_desc"));
			return result;
		}
	}

	/**
	 * 数据转换
	 *
	 * @param map
	 * @param rs
	 * @param isZero
	 *            零配置查询方式不一样，需要配置
	 * @return
	 * @throws SQLException
	 */
	public Map<String, String> resultSet2Map(Map<String, String> map, ResultSet rs,
			boolean isZero, List<Map> vendorList, List<Map> modelList,
			List<Map> typeInfoList)
	{
		try
		{
			map.put("device_id", rs.getString("device_id"));
			map.put("oui", rs.getString("oui"));
			map.put("device_serialnumber", rs.getString("device_serialnumber"));
			map.put("device_name", rs.getString("device_name"));
			map.put("city_id", rs.getString("city_id"));
			map.put("city_name",
					CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
			map.put("office_id", rs.getString("office_id"));
			map.put("complete_time", new DateTimeUtil(rs.getLong("complete_time") * 1000)
					.getYYYY_MM_DD_HH_mm_ss());
			map.put("zone_id", rs.getString("zone_id"));
			map.put("buy_time", new DateTimeUtil(rs.getLong("buy_time") * 1000)
					.getYYYY_MM_DD_HH_mm_ss());
			map.put("staff_id", rs.getString("staff_id"));
			map.put("remark", rs.getString("remark"));
			map.put("loopback_ip", rs.getString("loopback_ip"));
			if (Global.JXDX.equals(Global.instAreaShortName))
			{
				map.put("loopback_ip_six", rs.getString("loopback_ip_six"));
			}
			map.put("interface_id", rs.getString("interface_id"));
			map.put("device_status", rs.getString("device_status"));
			map.put("gather_id", rs.getString("gather_id"));
			map.put("devicetype_id", rs.getString("devicetype_id"));
			// 零配置不关联
			if (isZero)
			{
				map.put("softwareversion",
						getSoftVersionByTypeInfoID(typeInfoList,
								rs.getString("devicetype_id")));
			}
			else
			{
				map.put("softwareversion", rs.getString("softwareversion"));
			}
			map.put("maxenvelopes", rs.getString("maxenvelopes"));
			try
			{
				map.put("addressing_type", rs.getString("addressing_type"));
				map.put("status", parseStatusByCode(rs.getString("status")));
			}
			catch (SQLException e)
			{
			}
			map.put("cr_port", rs.getString("cr_port"));
			map.put("cr_path", rs.getString("cr_path"));
			map.put("cpe_mac", rs.getString("cpe_mac"));
			map.put("cpe_currentupdatetime",
					new DateTimeUtil(rs.getLong("cpe_currentupdatetime") * 1000)
							.getYYYY_MM_DD_HH_mm_ss());
			map.put("cpe_allocatedstatus", rs.getString("cpe_allocatedstatus"));
			map.put("cpe_username", rs.getString("cpe_username"));
			map.put("cpe_passwd", rs.getString("cpe_passwd"));
			map.put("acs_username", rs.getString("acs_username"));
			map.put("acs_passwd", rs.getString("acs_passwd"));
			map.put("device_type", rs.getString("device_type"));
			map.put("x_com_username", rs.getString("x_com_username"));
			map.put("x_com_passwd", rs.getString("x_com_passwd"));
			map.put("gw_type", rs.getString("gw_type"));
			map.put("device_model_id", rs.getString("device_model_id"));
			// 零配置不关联表
			if (isZero)
			{
				map.put("device_model",
						getModelNameByModelID(modelList, rs.getString("device_model_id")));
			}
			else
			{
				map.put("device_model", rs.getString("device_model"));
			}
			map.put("customer_id", rs.getString("customer_id"));
			map.put("device_url", rs.getString("device_url"));
			map.put("x_com_passwd_old", rs.getString("x_com_passwd_old"));
			map.put("vendor_id", rs.getString("vendor_id"));
			// 零配置不关联表
			if (isZero)
			{
				map.put("vendor_add",
						getVendorNameByVendorID(vendorList, rs.getString("vendor_id")));
			}
			else
			{
				map.put("vendor_add", rs.getString("vendor_add"));
			}
			map.put("bind_time", new DateTimeUtil(rs.getLong("bind_time") * 1000)
					.getYYYY_MM_DD_HH_mm_ss());
			map.put("cust_account", rs.getString("cust_account"));
			if (Global.HNLT.equals(Global.instAreaShortName))
			{
				map.put("serv_account", rs.getString("hnlt_serv"));
			}
			else
			{
				map.put("serv_account", rs.getString("serv_account"));
				map.put("pppoe_user", rs.getString("pppoe_user"));
			}
			map.put("inform_stat", "1".equals(rs.getString("inform_stat")) ? "可管理"
					: "不可管理");
			try
			{
				map.put("online_status", "1".equals(rs.getString("online_status")) ? "在线"
						: "不在线");
			}
			catch (SQLException ignore)
			{
				// 由于展示列表页面也是调用该方法，没有online_status方法，导致报错。忽略异常
			}
			// 将dealdate转换成时间
			try
			{
				long dealdate = StringUtil.getLongValue(rs.getString("last_time"));
				com.linkage.litms.common.util.DateTimeUtil dt = new com.linkage.litms.common.util.DateTimeUtil(
						dealdate * 1000);
				map.put("last_time", dt.getLongDate());
			}
			catch (NumberFormatException e)
			{
				map.put("last_time", "");
			}
			catch (Exception e)
			{
				map.put("last_time", "");
			}
		}
		catch (SQLException e)
		{
			logger.error(e.getMessage(), e);
		}
		return map;
	}
}
