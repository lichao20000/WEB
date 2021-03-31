
package com.linkage.module.gtms.stb.zeroconf.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gtms.stb.zeroconf.dto.ZeroConfStatisticsReportDTO;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.system.utils.StringUtils;

public class ZeroConfStatisticsReportDAO extends SuperDAO
{

	public static Logger log = Logger.getLogger(ZeroConfStatisticsReportDAO.class);

	@SuppressWarnings("rawtypes")
	private void SQLAppend(ZeroConfStatisticsReportDTO dto, StringBuffer sql)
	{
		if (null != dto.getBeginTime())
		{
			sql.append(" and a.openUserdate>=").append(dto.getBeginTime());
		}
		if (null != dto.getEndTime())
		{
			sql.append(" and a.openUserdate<=").append(dto.getEndTime());
		}
		if (false == StringUtil.IsEmpty(dto.getCityId()) && !"00".equals(dto.getCityId()))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(dto.getCityId());
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" group by a.city_id");
	}

	@SuppressWarnings("rawtypes")
	private void CusSQLAppend(ZeroConfStatisticsReportDTO dto, StringBuffer sql)
	{
		if (null != dto.getBeginTime())
		{
			sql.append(" and a.openUserdate>=").append(dto.getBeginTime());
		}
		if (null != dto.getEndTime())
		{
			sql.append(" and a.openUserdate<=").append(dto.getEndTime());
		}
		if (!StringUtil.IsEmpty(dto.getUserStatus()))
		{
			sql.append(" and a.user_status in( ").append(dto.getUserStatus())
					.append(" ) ");
		}
		if (null != dto.getCustAccessType())
		{
			sql.append(" and a.net_type in (").append(dto.getCustAccessType())
					.append(" ) ");
		}
		if (false == StringUtil.IsEmpty(dto.getCityId()) && !"00".equals(dto.getCityId()))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(dto.getCityId());
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
	}

	@SuppressWarnings("rawtypes")
	private void CusFailSQLAppend(ZeroConfStatisticsReportDTO dto, StringBuffer sql)
	{
		if (null != dto.getBeginTime())
		{
			sql.append(" and a.openUserdate>=").append(dto.getBeginTime());
		}
		if (null != dto.getEndTime())
		{
			sql.append(" and a.openUserdate<=").append(dto.getEndTime());
		}
		// if (!StringUtil.IsEmpty(dto.getUserStatus()))
		// {
		// sql.append(" and a.user_status in( ").append(dto.getUserStatus()).append(" ) ");
		// }
		if (null != dto.getCustAccessType())
		{
			sql.append(" and a.net_type in (").append(dto.getCustAccessType())
					.append(" ) ");
		}
		if (false == StringUtil.IsEmpty(dto.getCityId()) && !"00".equals(dto.getCityId()))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(dto.getCityId());
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
	}

	@SuppressWarnings("rawtypes")
	public Map getADSLAllNum(ZeroConfStatisticsReportDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(*) cnt from stb_tab_customer a where 1=1 and a.net_type=0 ");
		SQLAppend(dto, sql);
		log.info("ADSL总数：" + sql);
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public Map getADSLFail_1Num(ZeroConfStatisticsReportDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(*) cnt from stb_tab_customer a where a.user_status = -1 and a.net_type=0 ");
		SQLAppend(dto, sql);
		log.info("ADSL失败数：" + sql);
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public Map getADSLFailNum(ZeroConfStatisticsReportDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(*) cnt from stb_tab_customer a,stb_tab_gw_device b where a.user_status = 0 and a.serv_account = b.serv_account and a.net_type=0 ");
		SQLAppend(dto, sql);
		log.info("ADSL失败数：" + sql);
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public Map getADSLSuccessNum(ZeroConfStatisticsReportDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(*) cnt from stb_tab_customer a where a.user_status = 1 and a.net_type=0 ");
		SQLAppend(dto, sql);
		log.info("ADSL成功数：" + sql);
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public Map getLANAllNum(ZeroConfStatisticsReportDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(*) cnt from stb_tab_customer a where 1=1 and a.net_type=2 ");
		SQLAppend(dto, sql);
		log.info("LAN总数：" + sql);
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public Map getLANFail_1Num(ZeroConfStatisticsReportDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(*) cnt from stb_tab_customer a where a.user_status = -1  and a.net_type=2 ");
		SQLAppend(dto, sql);
		log.info("LAN失败数：" + sql);
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public Map getLANFailNum(ZeroConfStatisticsReportDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(*) cnt from stb_tab_customer a,stb_tab_gw_device b where a.user_status = 0 and a.serv_account = b.serv_account and a.net_type=2 ");
		SQLAppend(dto, sql);
		log.info("LAN失败数：" + sql);
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public Map getLANSuccessNum(ZeroConfStatisticsReportDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(*) cnt from stb_tab_customer a where a.user_status = 1 and a.net_type=2 ");
		SQLAppend(dto, sql);
		log.info("LAN成功数：" + sql);
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public Map getFTHHAllNum(ZeroConfStatisticsReportDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(*) cnt from stb_tab_customer a where 1=1 and a.net_type in (1,3) ");
		SQLAppend(dto, sql);
		log.info("FTHH总数：" + sql);
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public Map getFTHHFail_1Num(ZeroConfStatisticsReportDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.city_id,count(*) cnt from stb_tab_customer a where a.user_status = -1 and  a.net_type in(1,3)  ");
		SQLAppend(dto, sql);
		log.info("FTHH失败数：" + sql);
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public Map getFTHHFailNum(ZeroConfStatisticsReportDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.city_id,count(*) cnt from stb_tab_customer a,stb_tab_gw_device b where a.user_status = 0 and a.serv_account = b.serv_account and a.net_type in(1,3)  ");
		SQLAppend(dto, sql);
		log.info("FTHH失败数：" + sql.toString());
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public Map getFTHHSuccessNum(ZeroConfStatisticsReportDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(*) cnt from stb_tab_customer a where a.user_status = 1 and a.net_type in (1,3) ");
		SQLAppend(dto, sql);
		log.info("FTHH成功数：" + sql);
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> queryCustomerList(ZeroConfStatisticsReportDTO dto, int firstRecord,
			int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.customer_id,a.cust_account,a.serv_account,a.pppoe_user,a.city_id,a.addressing_type,b.device_id,b.device_serialnumber,a.openUserdate,a.user_status "
				+ " from stb_tab_customer a left join stb_tab_gw_device b on a.customer_id = b.customer_id  where 1=1 ");
		CusSQLAppend(dto, sql);
		if ("0".equals(dto.getUserStatus()))
		{
			sql.append(" and not exists (select 1 from stb_tab_gw_device c where a.serv_account = c.serv_account ) ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return querySP(psql.getSQL(), firstRecord, num_splitPage, new CustomerRowMapper());
	}

	public int countCustomer(ZeroConfStatisticsReportDTO dto)
	{
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select count(*) from stb_tab_customer a left join stb_tab_gw_device b on  a.customer_id = b.customer_id  where 1=1 ");
		CusSQLAppend(dto, sql);
		if ("0".equals(dto.getUserStatus()))
		{
			sql.append(" and not exists (select 1 from stb_tab_gw_device c where a.serv_account = c.serv_account ) ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForInt(psql.getSQL());
	}

	private static class CustomerRowMapper implements RowMapper
	{

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Map<String, String> result = new HashMap<String, String>();
			result.put("customerId", rs.getString("customer_id"));
			result.put("servAccount", rs.getString("serv_account"));
			result.put("pppoeUser", rs.getString("pppoe_user"));
			result.put("cust_account", rs.getString("cust_account"));
			result.put("cityName", CityDAO.getCityName(rs.getString("city_id")));
			result.put("addressingType", rs.getString("addressing_type"));
			result.put("deviceId", rs.getString("device_id"));
			result.put("deviceSN", rs.getString("device_serialnumber"));
			result.put("openUserdate",
					DateUtil.transTime(rs.getLong("openUserdate"), "yyyy-MM-dd HH:mm:ss"));
			result.put("userStatus", transUserStatus(rs.getString("user_status")));
			return result;
		}
	}

	private static String transUserStatus(String userStatus)
	{
		if ("-1".equals(userStatus))
		{
			return "失败";
		}
		else if ("1".equals(userStatus))
		{
			return "成功";
		}
		return "未做";
	}

	@SuppressWarnings("rawtypes")
	public Map getADSLNoNum(ZeroConfStatisticsReportDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id ,count(*) cnt from stb_tab_customer a where a.user_status = 0  and a.net_type=0 and not exists (select 1 from stb_tab_gw_device b where a.serv_account = b.serv_account ) ");
		SQLAppend(dto, sql);
		log.info("ADSL未做数" + sql);
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public Map getLANnoNum(ZeroConfStatisticsReportDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id ,count(*) cnt from stb_tab_customer a where a.user_status=0 and a.net_type=2 and not exists  (select 1 from stb_tab_gw_device b where a.serv_account = b.serv_account ) ");
		SQLAppend(dto, sql);
		log.info("LAN未做数" + sql);
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public Map getFTHHNoNum(ZeroConfStatisticsReportDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id ,count(*) cnt from stb_tab_customer a where a.user_status=0 and a.net_type in(1,3) and not exists (select 1 from stb_tab_gw_device b where a.serv_account = b.serv_account ) ");
		SQLAppend(dto, sql);
		log.info("FTHH未做数" + sql);
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> queryFailCustomerList(ZeroConfStatisticsReportDTO dto,
			int firstRecord, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select a.customer_id,a.cust_account,a.serv_account,a.pppoe_user,a.city_id,a.addressing_type,b.device_id,b.device_serialnumber,a.openUserdate,a.user_status "
				+ "from stb_tab_customer a left join stb_tab_gw_device b on  a.serv_account = b.serv_account where a.user_status = -1 ");
		CusFailSQLAppend(dto, sql);
		sql.append(" union all ");
		sql.append("select a.customer_id,a.cust_account,a.serv_account,a.pppoe_user,a.city_id,a.addressing_type,b.device_id,b.device_serialnumber,a.openUserdate,a.user_status ");
		sql.append("from stb_tab_customer a, stb_tab_gw_device b where a.user_status = 0  ");
		CusFailSQLAppend(dto, sql);
		sql.append(" and a.serv_account = b.serv_account");
		log.info("失败详细列表" + sql);
		return querySP(sql.toString(), firstRecord, num_splitPage,
				new CustomerRowMapper());
	}

	public int countFailCustomer(ZeroConfStatisticsReportDTO dto)
	{
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select sum(t.cnt) from ( select count(*) cnt from stb_tab_customer a left join stb_tab_gw_device b on a.serv_account = b.serv_account where a.user_status = -1 ");
		CusFailSQLAppend(dto, sql);
		sql.append(" union all ");
		sql.append(" select count(*) cnt from stb_tab_customer a, stb_tab_gw_device b where a.user_status = 0 ");
		CusFailSQLAppend(dto, sql);
		sql.append(" and a.serv_account = b.serv_account) t ");
		log.info("失败详细列表" + sql);
		return jt.queryForInt(sql.toString());
	}
}
