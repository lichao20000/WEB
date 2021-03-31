
package com.linkage.module.gtms.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-5-14
 * @category com.linkage.module.gtms.service.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class OperateByHandActionDAOImp extends SuperDAO implements OperateByHandActionDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(OperateByHandActionDAOImp.class);
	private Map<String, String> cityMap = null;

	private Map<String,String> operMap = new HashMap<String, String>();
	public OperateByHandActionDAOImp(){
		operMap.put("1", "开户");
		operMap.put("2", "变更");
		operMap.put("3", "销户");
		operMap.put("6", "更改账号");
	}


	@Override
	public List<Map> getOperateByHandInfo(long accOid,String starttime, String endtime,
			String city_id, String servTypeId, String username, String resultType,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getOperateByHandInfo({},{},{},{},{},{},{},{})", new Object[] {
				starttime, endtime, city_id, servTypeId, username, resultType,
				curPage_splitPage, num_splitPage });
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select b.device_serialnumber,t.id,t.username,t.city_id,t.oper_type,t.serv_type_id,t.result_id, ");
		sql.append("t.result_desc,t.oper_time,c.acc_loginname,t.occ_ip from tab_handsheet_log t ");
		sql.append("left join tab_hgwcustomer a on t.username=a.username ");
		sql.append("left join tab_gw_device b on a.device_id=b.device_id ");
		sql.append("left join tab_accounts c on t.occ_id=c.acc_oid where 1=1 ");
		if (accOid>0)
		{
			sql.append(" and (c.creator=").append(accOid).append(" or c.acc_oid=").append(accOid).append(" ) ");
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and t.oper_time>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and t.oper_time<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(servTypeId) && !"-1".equals(servTypeId))
		{
			sql.append(" and t.serv_type_id=").append(servTypeId);
		}
		if (!StringUtil.IsEmpty(username))
		{
			sql.append(" and c.acc_loginname='").append(username).append("'");
		}
		if (!StringUtil.IsEmpty(resultType) && !"-1".equals(resultType))
		{
			sql.append(" and t.result_id=").append(resultType);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				String cityName = null;
				map.put("id", rs.getString("id"));
				map.put("user_Name", rs.getString("username"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				String oper_type = operMap.get(rs.getString("oper_type"));
				map.put("oper_type", oper_type);
				cityName = (String) cityMap.get(rs.getString("city_id"));
				if (!StringUtil.IsEmpty(cityName))
				{
					map.put("city_name", cityName);
				}
				String servType = getServType(rs.getString("serv_type_id"));
				if (!StringUtil.IsEmpty(servType))
				{
					map.put("serv_type", servType);
				}
				String resultId = rs.getString("result_id");
				if (!StringUtil.IsEmpty(resultId))
				{
					if ("1".equals(resultId))
					{
						map.put("result_id", "成功");
					}
					if ("0".equals(resultId))
					{
						map.put("result_id", "失败");
					}
				}
				try
				{
					long opertime = StringUtil.getLongValue(rs.getString("oper_time")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(opertime);
					map.put("oper_time", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("oper_time", "");
				}
				catch (Exception e)
				{
					map.put("oper_time", "");
				}
				map.put("result_desc", rs.getString("result_desc"));
				map.put("acc_loginname", rs.getString("acc_loginname"));
				map.put("occ_ip", rs.getString("occ_ip"));
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	@Override
	public int countOperateByHandInfo(long accOid,String starttime, String endtime, String city_id,
			String servTypeId, String username, String resultType, int curPage_splitPage,
			int num_splitPage)
	{
		logger.debug("countOperateByHandInfo({},{},{},{},{},{},{},{})", new Object[] {
				starttime, endtime, city_id, servTypeId, username, resultType,
				curPage_splitPage, num_splitPage });
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select count(*) from tab_handsheet_log t ");
		sql.append("left join tab_hgwcustomer a on t.username=a.username ");
		sql.append("left join tab_gw_device b on a.device_id=b.device_id ");
		sql.append("left join tab_accounts c on t.occ_id=c.acc_oid where 1=1 ");
		if (accOid>0)
		{
			sql.append(" and c.creator=").append(accOid);
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and t.oper_time>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and t.oper_time<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(servTypeId) && !"-1".equals(servTypeId))
		{
			sql.append(" and t.serv_type_id=").append(servTypeId);
		}
		if (!StringUtil.IsEmpty(username))
		{
			sql.append(" and c.acc_loginname='").append(username).append("'");
		}
		if (!StringUtil.IsEmpty(resultType) && !"-1".equals(resultType))
		{
			sql.append(" and t.result_id=").append(resultType);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
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

	@Override
	public int countOperateByHandInfoExcel(String starttime, String endtime,
			String city_id, String servTypeId, String username, String resultType)
	{
		logger.debug("countOperateByHandInfoExcel({},{},{},{},{},{},{},{})",
				new Object[] { starttime, endtime, city_id, servTypeId, username,
						resultType });
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select count(*) from tab_handsheet_log t ");
		sql.append("left join tab_hgwcustomer a on t.username=a.username ");
		sql.append("left join tab_gw_device b on a.device_id=b.device_id ");
		sql.append("left join tab_accounts c on t.occ_id=c.acc_oid where 1=1 ");
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and t.oper_time>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and t.oper_time<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		// if (!StringUtil.IsEmpty(city_id))
		// {
		// sql.append(" and t.city_id='").append(city_id).append("'");
		// }
		if (!StringUtil.IsEmpty(servTypeId) && !"-1".equals(servTypeId))
		{
			sql.append(" and t.serv_type_id=").append(servTypeId);
		}
		if (!StringUtil.IsEmpty(username))
		{
			sql.append(" and c.acc_loginname='").append(username).append("'");
		}
		if (!StringUtil.IsEmpty(resultType) && !"-1".equals(resultType))
		{
			sql.append(" and t.result_id=").append(resultType);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		return total;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map> getOperateByHandInfoExcel(String starttime, String endtime,
			String city_id, String servTypeId, String username, String resultType)
	{
		logger.debug("getOperateByHandInfoExcel({},{},{},{},{},{},{},{})", new Object[] {
				starttime, endtime, city_id, servTypeId, username, resultType });
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select b.device_serialnumber,t.id,t.username,t.city_id,t.oper_type,t.serv_type_id,t.result_id, ");
		sql.append("t.result_desc,t.oper_time,c.acc_loginname,t.occ_ip from tab_handsheet_log t ");
		sql.append("left join tab_hgwcustomer a on t.username=a.username ");
		sql.append("left join tab_gw_device b on a.device_id=b.device_id ");
		sql.append("left join tab_accounts c on t.occ_id=c.acc_oid where 1=1 ");
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and t.oper_time>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and t.oper_time<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		// if (!StringUtil.IsEmpty(city_id))
		// {
		// sql.append(" and t.city_id='").append(city_id).append("'");
		// }
		if (!StringUtil.IsEmpty(servTypeId) && !"-1".equals(servTypeId))
		{
			sql.append(" and t.serv_type_id=").append(servTypeId);
		}
		if (!StringUtil.IsEmpty(username))
		{
			sql.append(" and c.acc_loginname='").append(username).append("'");
		}
		if (!StringUtil.IsEmpty(resultType) && !"-1".equals(resultType))
		{
			sql.append(" and t.result_id=").append(resultType);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				String id = (String) list.get(i).get("id");
				String name = (String) list.get(i).get("username");
				list.get(i).put("username", name);
				String cid = (String) list.get(i).get("city_id");
				list.get(i).put("city_id", cid);
				String city_name = StringUtil.getStringValue(cityMap.get(cid));
				if (!StringUtil.IsEmpty(city_name))
				{
					list.get(i).put("city_name", city_name);
				}
				else
				{
					list.get(i).put("city_name", "");
				}
				String serType = list.get(i).get("serv_type_id") + "";
				list.get(i).put("serv_type", getServType(serType));
				String result = list.get(i).get("result_id") + "";
				if (!StringUtil.IsEmpty(result))
				{
					if ("1".equals(result))
					{
						list.get(i).put("result_id", "成功");
					}
					if ("0".equals(result))
					{
						list.get(i).put("result_id", "失败");
					}
				}
				String resultDesc = (String) list.get(i).get("result_desc");
				list.get(i).put("result_desc", resultDesc);
				try
				{
					long opertime = StringUtil.getLongValue(list.get(i).get("oper_time")
							+ "") * 1000L;
					DateTimeUtil dt = new DateTimeUtil(opertime);
					list.get(i).put("oper_time", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					list.get(i).put("oper_time", "");
				}
				catch (Exception e)
				{
					list.get(i).put("oper_time", "");
				}
				String occip = (String) list.get(i).get("occ_ip");
				list.get(i).put("occ_ip", occip);
				String oper_type= list.get(i).get("oper_type")+"";
				String operType = operMap.get(oper_type);
				list.get(i).put("oper_type", operType);
				String acc_loginname = (String)list.get(i).get("acc_loginname");
				list.get(i).put("acc_loginname",acc_loginname);
				String ddevice_serialnumber = list.get(i).get("device_serialnumber") + "";
				list.get(i).put("device_serialnumber", ddevice_serialnumber);
			}
			cityMap = null;
		}

		return list;
	}

	private String getServType(String servTypeId)
	{
		String str = null;
		if (!StringUtil.IsEmpty(servTypeId))
		{
			int key = Integer.parseInt(servTypeId);
			switch (key)
			{
				case 20:
					str = "用户资料";
					break;
				case 10:
					str = "宽带业务";
					break;
				case 11:
					str = "IPTV业务";
					break;
				case 14:
					str = "VOIP业务";
					break;
				default:
					break;
			}
		}
		return str;
	}
}
