package com.linkage.module.gtms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class BusinessOpenCountDaoImpl extends SuperDAO implements
		BusinessOpenCountDao {
	private static Logger logger = LoggerFactory
			.getLogger(BusinessOpenCountDaoImpl.class);

	@SuppressWarnings("unchecked")
	public List<Map> getAllSheetNum(String cityId, String starttime,
			String endtime, String selectTypeId) {
		logger.debug("getAllSheetNum({})", new Object[] { cityId, starttime,
				endtime,selectTypeId });
		PrepareSQL sb = new PrepareSQL();
		if(selectTypeId==null || selectTypeId.trim().length()==0 || "-1".equals(selectTypeId.trim())){
			sb.append("select a.city_id , b.open_status ,count(*) as num  ");
			sb.append(" from tab_hgwcustomer a , hgwcust_serv_info b ");
			sb.append(" where a.user_id=b.user_id ");
			sb.append(" and b.completedate>=?");
			sb.append(" and b.completedate<=?");
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sb.append(" and a.city_id in (");
				sb.append(StringUtils.weave(cityIdList));
				sb.append(")");
				cityIdList = null;
			}
			sb.append(" group by a.city_id, b.open_status");
			sb.setInt(1, StringUtil.getIntegerValue(starttime));
			sb.setInt(2, StringUtil.getIntegerValue(endtime));
		}else{
			if("0".equals(selectTypeId.trim())){// TODO wait (more table related)
				sb.append("select a.city_id , b.open_status ,count(*) as num  ");
				sb.append(" from tab_hgwcustomer a , hgwcust_serv_info b , tab_gw_device c ");
				sb.append(" where a.user_id=b.user_id and a.device_id=c.device_id ");
				sb.append(" and c.complete_time>=? ");
				sb.append(" and c.complete_time<=? ");
				if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
					List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
					sb.append(" and a.city_id in (");
					sb.append(StringUtils.weave(cityIdList));
					sb.append(")");
					cityIdList = null;
				}
				sb.append(" group by a.city_id, b.open_status");
				sb.setInt(1, StringUtil.getIntegerValue(starttime));
				sb.setInt(2, StringUtil.getIntegerValue(endtime));
			}else if("1".equals(selectTypeId.trim())){// TODO wait (more table related)
				sb.append("select a.city_id , b.open_status ,count(*) as num  ");
				sb.append(" from tab_hgwcustomer a , hgwcust_serv_info b , tab_gw_device c ");
				sb.append(" where a.user_id=b.user_id and a.device_id=c.device_id ");
				sb.append(" and b.completedate>=? ");
				sb.append(" and b.completedate<=? ");
				sb.append(" and c.complete_time<? ");
				if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
					List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
					sb.append(" and a.city_id in (");
					sb.append(StringUtils.weave(cityIdList));
					sb.append(")");
					cityIdList = null;
				}
				sb.append(" group by a.city_id, b.open_status");
				sb.setInt(1, StringUtil.getIntegerValue(starttime));
				sb.setInt(2, StringUtil.getIntegerValue(endtime));
				sb.setInt(3, StringUtil.getIntegerValue(starttime));
			}
		}
		return jt.queryForList(sb.getSQL());
	}
	/**
	 * 用户业务列表
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getUserList(String openStatus, String cityId,String parentCityId,
			String starttime, String endtime, int curPageSplitPage,
			int numSplitPage, String selectTypeId) {
		logger.warn("getUserList({},{},{},{},{})", new Object[] { cityId,
				starttime, endtime, curPageSplitPage, numSplitPage });
		final Map<String,String> cityMap = CityDAO.getCityIdCityNameMap();

		PrepareSQL sb = new PrepareSQL();

		if(selectTypeId==null || selectTypeId.trim().length()==0 || "-1".equals(selectTypeId.trim())){
			sb.append(" select a.username,a.city_id,b.serv_type_id,b.open_status ");
			sb.append(" from tab_hgwcustomer a , hgwcust_serv_info b ");
			sb.append(" where a.user_id=b.user_id ");
			sb.append(" and b.completedate>=? ");
			sb.append(" and b.completedate<=? ");
			if("0".equals(openStatus)||"-1".equals(openStatus)||"1".equals(openStatus)){
				sb.append(" and b.open_status="+openStatus);
			}
			if("2".equals(openStatus)){
				sb.append(" and b.open_status in(-1,1)");
			}
			if("3".equals(openStatus)){
				sb.append(" and b.open_status in(-1,1,0)");
			}
			if (!StringUtil.IsEmpty(cityId)) {
				if(!parentCityId.equals(cityId))
				{
					List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
					sb.append(" and a.city_id in (");
					sb.append(StringUtils.weave(cityIdList));
					sb.append(")");
					cityIdList = null;
				}
				else
				{
					sb.append(" and a.city_id='");
					sb.append(cityId);
					sb.append("'");
				}
			}
			sb.append(" order by a.username ");
			sb.setInt(1, StringUtil.getIntegerValue(starttime));
			sb.setInt(2, StringUtil.getIntegerValue(endtime));
		}else{
			if("0".equals(selectTypeId.trim())){// TODO wait (more table related)
				sb.append(" select a.username,a.city_id,b.serv_type_id,b.open_status ");
				sb.append(" from tab_hgwcustomer a , hgwcust_serv_info b , tab_gw_device c ");
				sb.append(" where a.user_id=b.user_id and a.device_id=c.device_id ");
				sb.append(" and c.complete_time>=? ");
				sb.append(" and c.complete_time<=? ");
				if("0".equals(openStatus)||"-1".equals(openStatus)||"1".equals(openStatus)){
					sb.append(" and b.open_status="+openStatus);
				}
				if("2".equals(openStatus)){
					sb.append(" and b.open_status in(-1,1)");
				}
				if("3".equals(openStatus)){
					sb.append(" and b.open_status in(-1,1,0)");
				}
				if (!StringUtil.IsEmpty(cityId)) {
					if(!parentCityId.equals(cityId))
					{
						List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
						sb.append(" and a.city_id in (");
						sb.append(StringUtils.weave(cityIdList));
						sb.append(")");
						cityIdList = null;
					}
					else
					{
						sb.append(" and a.city_id='");
						sb.append(cityId);
						sb.append("'");
					}
				}
				sb.append(" order by a.username ");
				sb.setInt(1, StringUtil.getIntegerValue(starttime));
				sb.setInt(2, StringUtil.getIntegerValue(endtime));
			}else if("1".equals(selectTypeId.trim())){// TODO wait (more table related)
				sb.append(" select a.username,a.city_id,b.serv_type_id,b.open_status ");
				sb.append(" from tab_hgwcustomer a , hgwcust_serv_info b , tab_gw_device c ");
				sb.append(" where a.user_id=b.user_id and a.device_id=c.device_id ");
				sb.append(" and b.completedate>=? ");
				sb.append(" and b.completedate<=? ");
				sb.append(" and c.complete_time<? ");
				if("0".equals(openStatus)||"-1".equals(openStatus)||"1".equals(openStatus)){
					sb.append(" and b.open_status="+openStatus);
				}
				if("2".equals(openStatus)){
					sb.append(" and b.open_status in(-1,1)");
				}
				if("3".equals(openStatus)){
					sb.append(" and b.open_status in(-1,1,0)");
				}
				if (!StringUtil.IsEmpty(cityId)) {
					if(!parentCityId.equals(cityId))
					{
						List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
						sb.append(" and a.city_id in (");
						sb.append(StringUtils.weave(cityIdList));
						sb.append(")");
						cityIdList = null;
					}
					else
					{
						sb.append(" and a.city_id='");
						sb.append(cityId);
						sb.append("'");
					}
				}
				sb.append(" order by a.username ");
				sb.setInt(1, StringUtil.getIntegerValue(starttime));
				sb.setInt(2, StringUtil.getIntegerValue(endtime));
				sb.setInt(3, StringUtil.getIntegerValue(starttime));
			}
		}

		List<Map> list = querySP(sb.getSQL(), (curPageSplitPage - 1)
				* numSplitPage + 1, numSplitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				Map<String,String> resMap = new HashMap<String,String>();
				resMap.put("username", StringUtil.getStringValue(rs.getString("username")));
				String cityId = StringUtil.getStringValue(rs.getString("city_id"));
				resMap.put("cityName", StringUtil.getStringValue(cityMap.get(cityId)));
				String servTypeId = StringUtil.getStringValue(rs.getString("serv_type_id"));
				resMap.put("servTypeName", StringUtil.getStringValue(getServType().get(servTypeId)));
				resMap.put("openStatus", StringUtil.getStringValue(rs.getString("open_status")));
				return resMap;
			}

		});
		return list;
	}
	/**
	 * 业务类型ID与名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getServType(){

		logger.debug("getServType()");

		String sql = "select serv_type_id, serv_type_name from tab_gw_serv_type";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List<Map> list = jt.queryForList(sql);
		Map<String, String> servTypeMap = new HashMap<String, String>();
		for (Map map : list)
		{
			servTypeMap.put(StringUtil.getStringValue(map.get("serv_type_id")),
					StringUtil.getStringValue(map.get("serv_type_name")));
		}
		return servTypeMap;
	}
	/**
	 * 获取各个状态的用户总数
	 */
	public int getUserCount(String openStatus,String cityId,String parentCityId, String starttime,
			String endtime, int curPageSplitPage, int numSplitPage, String selectTypeId){
		PrepareSQL sb = new PrepareSQL();

		if(selectTypeId==null || selectTypeId.trim().length()==0 || "-1".equals(selectTypeId.trim())){
			sb.append(" select count(*) from tab_hgwcustomer a , hgwcust_serv_info b ");
			sb.append(" where a.user_id=b.user_id ");
			sb.append(" and b.completedate>=? ");
			sb.append(" and b.completedate<=? ");
			if("0".equals(openStatus)||"-1".equals(openStatus)||"1".equals(openStatus)){
				sb.append(" and b.open_status="+openStatus);
			}
			if("2".equals(openStatus)){
				sb.append(" and b.open_status in(-1,1)");
			}
			if("3".equals(openStatus)){
				sb.append(" and b.open_status in(-1,1,0)");
			}
			if (!StringUtil.IsEmpty(cityId)) {
				if(!parentCityId.equals(cityId))
				{
					List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
					sb.append(" and a.city_id in (");
					sb.append(StringUtils.weave(cityIdList));
					sb.append(")");
					cityIdList = null;
				}
				else
				{
					sb.append(" and a.city_id='");
					sb.append(cityId);
					sb.append("'");
				}
			}
			sb.setInt(1, StringUtil.getIntegerValue(starttime));
			sb.setInt(2, StringUtil.getIntegerValue(endtime));
		}else{
			if("0".equals(selectTypeId.trim())){// TODO wait (more table related)
				sb.append(" select count(*) from tab_hgwcustomer a , hgwcust_serv_info b , tab_gw_device c  ");
				sb.append(" where a.user_id=b.user_id and a.device_id=c.device_id ");
				sb.append(" and c.complete_time>=? ");
				sb.append(" and c.complete_time<=? ");
				if("0".equals(openStatus)||"-1".equals(openStatus)||"1".equals(openStatus)){
					sb.append(" and b.open_status="+openStatus);
				}
				if("2".equals(openStatus)){
					sb.append(" and b.open_status in(-1,1)");
				}
				if("3".equals(openStatus)){
					sb.append(" and b.open_status in(-1,1,0)");
				}
				if (!StringUtil.IsEmpty(cityId)) {
					if(!parentCityId.equals(cityId))
					{
						List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
						sb.append(" and a.city_id in (");
						sb.append(StringUtils.weave(cityIdList));
						sb.append(")");
						cityIdList = null;
					}
					else
					{
						sb.append(" and a.city_id='");
						sb.append(cityId);
						sb.append("'");
					}
				}
				sb.setInt(1, StringUtil.getIntegerValue(starttime));
				sb.setInt(2, StringUtil.getIntegerValue(endtime));
			}else if("1".equals(selectTypeId.trim())){// TODO wait (more table related)
				sb.append(" select count(*) from tab_hgwcustomer a , hgwcust_serv_info b , tab_gw_device c ");
				sb.append(" where a.user_id=b.user_id and a.device_id=c.device_id ");
				sb.append(" and b.completedate>=? ");
				sb.append(" and b.completedate<=? ");
				sb.append(" and c.complete_time<? ");
				if("0".equals(openStatus)||"-1".equals(openStatus)||"1".equals(openStatus)){
					sb.append(" and b.open_status="+openStatus);
				}
				if("2".equals(openStatus)){
					sb.append(" and b.open_status in(-1,1)");
				}
				if("3".equals(openStatus)){
					sb.append(" and b.open_status in(-1,1,0)");
				}
				if (!StringUtil.IsEmpty(cityId)) {
					if(!parentCityId.equals(cityId))
					{
						List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
						sb.append(" and a.city_id in (");
						sb.append(StringUtils.weave(cityIdList));
						sb.append(")");
						cityIdList = null;
					}
					else
					{
						sb.append(" and a.city_id='");
						sb.append(cityId);
						sb.append("'");
					}
				}
				sb.setInt(1, StringUtil.getIntegerValue(starttime));
				sb.setInt(2, StringUtil.getIntegerValue(endtime));
				sb.setInt(3, StringUtil.getIntegerValue(starttime));
			}
		}

		int total = jt.queryForInt(sb.getSQL());
		int maxPage = 1;
		if (total % numSplitPage == 0){
			maxPage = total / numSplitPage;
		}else{
			maxPage = total / numSplitPage + 1;
		}
		return maxPage;
	}
	@SuppressWarnings("unchecked")
	public List<Map> getUserListExcel(String openStatus, String cityId,String parentCityId,
			String starttime, String endtime, String selectTypeId) {
		logger.warn("getUserList({},{},{},{})", new Object[] { openStatus,cityId,
				starttime, endtime});
		final Map<String,String> cityMap = CityDAO.getCityIdCityNameMap();

		PrepareSQL sb = new PrepareSQL();

		if(selectTypeId==null || selectTypeId.trim().length()==0 || "-1".equals(selectTypeId.trim())){
			sb.append(" select a.username,a.city_id,b.serv_type_id,b.open_status ");
			sb.append(" from tab_hgwcustomer a , hgwcust_serv_info b ");
			sb.append(" where a.user_id=b.user_id ");
			sb.append(" and b.completedate>=? ");
			sb.append(" and b.completedate<=? ");
			if("0".equals(openStatus)||"-1".equals(openStatus)||"1".equals(openStatus)){
				sb.append(" and b.open_status="+openStatus);
			}
			if("2".equals(openStatus)){
				sb.append(" and b.open_status in(-1,1)");
			}
			if("3".equals(openStatus)){
				sb.append(" and b.open_status in(-1,1,0)");
			}
			if (!StringUtil.IsEmpty(cityId)) {
				if(!parentCityId.equals(cityId))
				{
					List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
					sb.append(" and a.city_id in (");
					sb.append(StringUtils.weave(cityIdList));
					sb.append(")");
					cityIdList = null;
				}
				else
				{
					sb.append(" and a.city_id='");
					sb.append(cityId);
					sb.append("'");
				}
			}
			sb.append(" order by a.username ");
			sb.setInt(1, StringUtil.getIntegerValue(starttime));
			sb.setInt(2, StringUtil.getIntegerValue(endtime));
		}else{
			if("0".equals(selectTypeId.trim())){// TODO wait (more table related)
				sb.append(" select a.username,a.city_id,b.serv_type_id,b.open_status ");
				sb.append(" from tab_hgwcustomer a , hgwcust_serv_info b, tab_gw_device c ");
				sb.append(" where a.user_id=b.user_id and a.device_id=c.device_id ");
				sb.append(" and c.complete_time>=? ");
				sb.append(" and c.complete_time<=? ");
				if("0".equals(openStatus)||"-1".equals(openStatus)||"1".equals(openStatus)){
					sb.append(" and b.open_status="+openStatus);
				}
				if("2".equals(openStatus)){
					sb.append(" and b.open_status in(-1,1)");
				}
				if("3".equals(openStatus)){
					sb.append(" and b.open_status in(-1,1,0)");
				}
				if (!StringUtil.IsEmpty(cityId)) {
					if(!parentCityId.equals(cityId))
					{
						List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
						sb.append(" and a.city_id in (");
						sb.append(StringUtils.weave(cityIdList));
						sb.append(")");
						cityIdList = null;
					}
					else
					{
						sb.append(" and a.city_id='");
						sb.append(cityId);
						sb.append("'");
					}
				}
				sb.append(" order by a.username ");
				sb.setInt(1, StringUtil.getIntegerValue(starttime));
				sb.setInt(2, StringUtil.getIntegerValue(endtime));
			}else if("1".equals(selectTypeId.trim())){// TODO wait (more table related)
				sb.append(" select a.username,a.city_id,b.serv_type_id,b.open_status ");
				sb.append(" from tab_hgwcustomer a , hgwcust_serv_info b, tab_gw_device c  ");
				sb.append(" where a.user_id=b.user_id and a.device_id=c.device_id ");
				sb.append(" and b.completedate>=? ");
				sb.append(" and b.completedate<=? ");
				sb.append(" and c.complete_time<? ");
				if("0".equals(openStatus)||"-1".equals(openStatus)||"1".equals(openStatus)){
					sb.append(" and b.open_status="+openStatus);
				}
				if("2".equals(openStatus)){
					sb.append(" and b.open_status in(-1,1)");
				}
				if("3".equals(openStatus)){
					sb.append(" and b.open_status in(-1,1,0)");
				}
				if (!StringUtil.IsEmpty(cityId)) {
					if(!parentCityId.equals(cityId))
					{
						List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
						sb.append(" and a.city_id in (");
						sb.append(StringUtils.weave(cityIdList));
						sb.append(")");
						cityIdList = null;
					}
					else
					{
						sb.append(" and a.city_id='");
						sb.append(cityId);
						sb.append("'");
					}
				}
				sb.append(" order by a.username ");
				sb.setInt(1, StringUtil.getIntegerValue(starttime));
				sb.setInt(2, StringUtil.getIntegerValue(endtime));
				sb.setInt(3, StringUtil.getIntegerValue(starttime));
			}
		}

		List<Map> list = jt.query(sb.getSQL(), new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				Map<String,String> resMap = new HashMap<String,String>();
				resMap.put("username", StringUtil.getStringValue(rs.getString("username")));
				String cityId = StringUtil.getStringValue(rs.getString("city_id"));
				resMap.put("cityName", StringUtil.getStringValue(cityMap.get(cityId)));
				String servTypeId = StringUtil.getStringValue(rs.getString("serv_type_id"));
				resMap.put("servTypeName", StringUtil.getStringValue(getServType().get(servTypeId)));
				String openStatus = StringUtil.getStringValue(rs.getString("open_status"));
				if(openStatus.equals("0")){
					resMap.put("openStatus", "未开通");
				}
				if(openStatus.equals("-1")){
					resMap.put("openStatus", "开通失败");
				}
				if(openStatus.equals("1")){
					resMap.put("openStatus", "开通成功");
				}
				return resMap;
			}

		});
		return list;
	}

}
