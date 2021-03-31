
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 
 * @author 岩 (Ailk No.)
 * @version 1.0
 * @since 2016-4-18
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class DevVerificationDisListDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(DevVerificationDisListDAO.class);
	private int queryCount;

	/**
	 * 
	 * @author 岩 
	 * @date 2016-4-21
	 * @param city_id
	 *        属地
	 * @param starttime
	 *        开始时间
	 * @param endtime
	 *        结束时间
	 * @param is_match
	 *        核销是否一致
	 * @param use_type
	 *        核销方式
	 * @param curPage_splitPage
	 *        分页
	 * @param num_splitPage
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> queryDevVerification(String city_id, String starttime,
			String endtime, String is_match, String use_type, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryDevVerification()");
		PrepareSQL psql = new PrepareSQL();
		psql.append("select city_id,username,bill_no,itms_serial,spec_name_itms,term_serial,spec_name_term,use_type,is_match,use_time ");
		psql.append(" from  tab_gw_automaticlist a ");
		psql.append(" where 1=1");

		if ((!StringUtil.IsEmpty(city_id)) && (!"00".equals(city_id)))
		{
			psql.append("   and city_id in (select city_id from tab_city where city_id= '");
			psql.append(city_id);
			psql.append("' or ");
			psql.append("parent_id= '");
			psql.append(city_id);
			psql.append("')");
		}
		if ((!StringUtil.IsEmpty(is_match)) && (!"-1".equals(is_match)))
		{
			psql.append("   and a.is_match = ");
			psql.append(is_match);
		}
		if ((!StringUtil.IsEmpty(use_type)) && (!"-1".equals(use_type)))
		{
			psql.append("   and a.use_type = ");
			psql.append(use_type);
		}

		if (!StringUtil.IsEmpty(starttime))
		{
			psql.append("   and a.use_time >= ");
			psql.append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			psql.append("   and a.use_time <= ");
			psql.append(endtime);
		}
		psql.append(" order by a.use_time desc");
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 属地
				map.put("city_id", CityDAO.getCityName(StringUtil.getStringValue(rs.getString("city_id"))));
				// 用户LOID
				map.put("username", StringUtil.getStringValue(rs.getString("username")));
				
				// 工单号
				map.put("bill_no", StringUtil.getStringValue(rs.getString("bill_no")));
				
				// ITMS设备  
				map.put("itms_serial", StringUtil.getStringValue(rs.getString("itms_serial")));
				
				// ITMS设备规格
				map.put("spec_name_itms", StringUtil.getStringValue(rs.getString("spec_name_itms")));
				
				// 核销设备
				map.put("term_serial", StringUtil.getStringValue(rs.getString("term_serial")));
				
				// 核销设备规格
				map.put("spec_name_term", StringUtil.getStringValue(rs.getString("spec_name_term")));
				
				// 核销方式
				if ("1".equals(StringUtil.getStringValue(rs.getString("use_type"))))
				{
					map.put("use_type", "自动核销");
				}
				else
				{
					map.put("use_type", "手动核销");
				}
				
				// 核销是否一致
				if ("1".equals(StringUtil.getStringValue(rs.getString("is_match"))))
				{
					map.put("is_match", "是");
				}
				else
				{
					map.put("is_match", "否");
				}

				// 核销日期
				DateTimeUtil dt = new DateTimeUtil(StringUtil.getLongValue(rs
						.getString("use_time")) * 1000l);
				map.put("use_time", dt.getLongDate());

				return map;
			}
		});
		return list;
	}

	/**
	 * 
	 * @author 岩 
	 * @date 2016-4-21
	 * @param city_id
	 *        属地
	 * @param starttime
	 *        开始时间
	 * @param endtime
	 *        结束时间
	 * @param is_match
	 *        核销是否一致
	 * @param use_type
	 *        核销方式
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> parseExcel(String city_id, String starttime,
			String endtime, String is_match, String use_type)
	{
		logger.debug("queryDevVerification()");
		PrepareSQL psql = new PrepareSQL();
		psql.append("select city_id,username,bill_no,itms_serial,spec_name_itms,term_serial,spec_name_term,use_type,is_match,use_time, ");
		psql.append(" orgname,area,project_nature,inst_addr,group_name,install_person,device_sn_8,device_sn_31,device_spec_8,device_spec_31 ");
		psql.append(" from  tab_gw_automaticlist a ");
		psql.append(" where 1=1");

		if ((!StringUtil.IsEmpty(city_id)) && (!"00".equals(city_id)))
		{
			psql.append("   and city_id in (select city_id from tab_city where city_id= '");
			psql.append(city_id);
			psql.append("' or ");
			psql.append("parent_id= '");
			psql.append(city_id);
			psql.append("')");
		}
		if ((!StringUtil.IsEmpty(is_match)) && (!"-1".equals(is_match)))
		{
			psql.append("   and a.is_match = ");
			psql.append(is_match);
		}
		if ((!StringUtil.IsEmpty(use_type)) && (!"-1".equals(use_type)))
		{
			psql.append("   and a.use_type = ");
			psql.append(use_type);
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			psql.append("   and a.use_time >= ");
			psql.append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			psql.append("   and a.use_time <= ");
			psql.append(endtime);
		}
		psql.append(" order by a.use_time desc");
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 属地
				map.put("city_id", CityDAO.getCityName(StringUtil.getStringValue(rs.getString("city_id"))));
				// 用户LOID
				map.put("username", StringUtil.getStringValue(rs.getString("username")));
				
				// 工单号
				map.put("bill_no", StringUtil.getStringValue(rs.getString("bill_no")));
				
				// ITMS设备  
				map.put("itms_serial", StringUtil.getStringValue(rs.getString("itms_serial")));
				
				// ITMS设备规格
				map.put("spec_name_itms", StringUtil.getStringValue(rs.getString("spec_name_itms")));
				
				// 核销设备
				map.put("term_serial", StringUtil.getStringValue(rs.getString("term_serial")));
				
				// 核销设备规格
				map.put("spec_name_term", StringUtil.getStringValue(rs.getString("spec_name_term")));
				
				// 核销设备规格
				map.put("orgname", StringUtil.getStringValue(rs.getString("orgname")));
				
				// 核销设备规格
				map.put("area", StringUtil.getStringValue(rs.getString("area")));
				
				// 核销设备规格
				map.put("project_nature", StringUtil.getStringValue(rs.getString("project_nature")));
				
				// 核销设备规格
				map.put("inst_addr", StringUtil.getStringValue(rs.getString("inst_addr")));
				
				// 核销设备规格
				map.put("group_name", StringUtil.getStringValue(rs.getString("group_name")));
				
				// 核销设备规格
				map.put("install_person", StringUtil.getStringValue(rs.getString("install_person")));
				
				// 核销设备规格
				map.put("device_sn_8", StringUtil.getStringValue(rs.getString("device_sn_8")));
				
				// 核销设备规格
				map.put("device_spec_8", StringUtil.getStringValue(rs.getString("device_spec_8")));
				// 核销设备规格
				map.put("device_sn_31", StringUtil.getStringValue(rs.getString("device_sn_31")));
				// 核销设备规格
				map.put("device_spec_31", StringUtil.getStringValue(rs.getString("device_spec_31")));

				// 核销方式
				if ("1".equals(StringUtil.getStringValue(rs.getString("use_type"))))
				{
					map.put("use_type", "自动核销");
				}
				else
				{
					map.put("use_type", "人工核销");
				}
				
				// 核销是否一致
				if ("1".equals(StringUtil.getStringValue(rs.getString("is_match"))))
				{
					map.put("is_match", "是");
				}
				else
				{
					map.put("is_match", "否");
				}

				// 核销日期
				DateTimeUtil dt = new DateTimeUtil(StringUtil.getLongValue(rs
						.getString("use_time")) * 1000l);
				map.put("use_time", dt.getLongDate());

				return map;
			}
		});
		return list;
	}

	/**
	 * 
	 * @author 岩 
	 * @date 2016-4-21
	 * @param city_id
	 *        属地
	 * @param starttime
	 *        开始时间
	 * @param endtime
	 *        结束时间
	 * @param is_match
	 *        核销是否一致
	 * @param use_type
	 *        核销方式
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int countQueryDevVerification(String city_id, String starttime,
			String endtime, String is_match, String use_type,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("countQueryDevVerification()");
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(1) from  tab_gw_automaticlist a where 1=1 ");

		if ((!StringUtil.IsEmpty(city_id)) && (!"00".equals(city_id)))
		{
			psql.append("   and city_id in (select city_id from tab_city where city_id= '");
			psql.append(city_id);
			psql.append("' or ");
			psql.append("parent_id= '");
			psql.append(city_id);
			psql.append("')");
		}
		if ((!StringUtil.IsEmpty(is_match)) && (!"-1".equals(is_match)))
		{
			psql.append("   and a.is_match = ");
			psql.append(is_match);
		}
		if ((!StringUtil.IsEmpty(use_type)) && (!"-1".equals(use_type)))
		{
			psql.append("   and a.use_type = ");
			psql.append(use_type);
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			psql.append("   and a.use_time >= ");
			psql.append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			psql.append("   and a.use_time <= ");
			psql.append(endtime);
		}
		queryCount = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (queryCount % num_splitPage == 0)
		{
			maxPage = queryCount / num_splitPage;
		}
		else
		{
			maxPage = queryCount / num_splitPage + 1;
		}
		return maxPage;
	}

	public int getQueryCount()
	{
		return queryCount;
	}

	public List<Map<String, String>> getCityName()
	{
		return null;
	}
}
