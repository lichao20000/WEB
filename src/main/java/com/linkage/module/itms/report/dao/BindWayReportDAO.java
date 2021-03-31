
package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 考核所有的用户
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class BindWayReportDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(BindWayReportDAO.class);
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;
	private Map<String, String> userTypeMap = null;
	private Map<String, String> bindTypeMap = null;

	/**
	 * 通过属地查询总开户数
	 * 
	 * @author wangsenbo
	 * @param cityId
	 * @param endtime1
	 * @param starttime1
	 * @date Mar 1, 2010
	 * @param
	 * @return Map
	 */
	public Map countOpened(String starttime1, String endtime1, String cityId,
			String access_style_id,String userType,String is_active)
	{
		logger.debug("countOpened({},{},{},{},{})", 
				new Object[] { starttime1, endtime1, cityId, access_style_id, userType });
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();

		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id ,count(*) as total ");
		}else{
			sql.append("select a.city_id ,count(1) as total ");
		}
		
		if(LipossGlobals.inArea(Global.JSDX)){
			sql.append("from tab_hgwcustomer_report a,gw_cust_user_dev_type b where a.user_id=b.user_id ");
		}else{
			sql.append("from tab_hgwcustomer a,gw_cust_user_dev_type b  where a.user_id=b.user_id ");
		}
		
		if(false == StringUtil.IsEmpty(userType) && "1".equals(userType)){  // 1 表示 E8-B  按活跃用户统计
			logger.warn("userType===="+userType);
			sql.append(" and b.type_id='").append(userType).append("'");
			sql.append(" and a.user_type_id= '2'   and a.is_active=1");      //BSS工单        只统计工单用户      
			logger.warn("aaaa");
			if (false == StringUtil.IsEmpty(starttime1)){
				sql.append(" and a.dealdate >= ").append(starttime1);   // 受理时间
			}
			if (false == StringUtil.IsEmpty(endtime1)){
				sql.append(" and a.dealdate <= ").append(endtime1);     // 受理时间
			}
		}else if(false == StringUtil.IsEmpty(userType) && "2".equals(userType)){  // 2 表示 E8-C 按受理时间统计
			sql.append("and b.type_id='").append(userType).append("'");
			if (false == StringUtil.IsEmpty(starttime1)){
				sql.append(" and a.onlinedate >= ").append(starttime1);  // 竣工时间
			}
			if (false == StringUtil.IsEmpty(endtime1)){
				sql.append(" and a.onlinedate <= ").append(endtime1);    // 竣工时间
			}
			if(false == StringUtil.IsEmpty(is_active) && !"".equals(is_active)){
				sql.append(" and a.is_active = ").append(is_active);
			}
		}
		
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(
					")");
			cityIdList = null;
		}
		sql.append(" and a.stat_bind_enab = 1 ");

		if(false == StringUtil.IsEmpty(access_style_id)){
			sql.append(" and a.access_style_id=").append(access_style_id);	
		}
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty()){
			for (int i = 0; i < list.size(); i++){
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}
	
	/**
	 * 根据属地统计 BSS同步用户数
	 * 此方法与countOpened 相同，只是查询条件 a.user_type_id=4  BSS同步
	 * @param starttime1
	 * @param endtime1
	 * @param cityId
	 * @param object
	 * @param usertype
	 * @return
	 */
	public Map countSyn(String starttime1, String endtime1, String cityId,
			String access_style_id, String userType,String is_active) 
	{
		logger.debug("countSyn({},{},{},{},{})", 
				new Object[] { starttime1, endtime1, cityId, access_style_id, userType });
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id,count(*) as total ");
		}else{
			sql.append("select a.city_id,count(1) as total ");
		}
		if(LipossGlobals.inArea(Global.JSDX)){
			sql.append("from tab_hgwcustomer_report a,gw_cust_user_dev_type b where a.user_id=b.user_id ");
		}else{
			sql.append("from tab_hgwcustomer a,gw_cust_user_dev_type b where a.user_id=b.user_id ");
		}
		
		if(false == StringUtil.IsEmpty(userType) && "1".equals(userType)){  // 1 表示 E8-B  按活跃用户统计
			logger.warn("userType=="+userType);
			sql.append(" and a.is_active=1 and b.type_id='").append(userType).append("'");
			if (false == StringUtil.IsEmpty(starttime1)){
				sql.append(" and a.dealdate >= ").append(starttime1);   // 受理时间时间
			}
			if (false == StringUtil.IsEmpty(endtime1)){
				sql.append(" and a.dealdate <= ").append(endtime1);     // 受理时间
			}
		}else if(false == StringUtil.IsEmpty(userType) && "2".equals(userType)){  // 2 表示 E8-C 
			sql.append("and b.type_id='").append(userType).append("'");
			if (false == StringUtil.IsEmpty(starttime1)){
				sql.append(" and a.onlinedate >= ").append(starttime1);  // 竣工时间
			}
			if (false == StringUtil.IsEmpty(endtime1)){
				sql.append(" and a.onlinedate <= ").append(endtime1);    // 竣工时间
			}
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(
					")");
			cityIdList = null;
		}
		sql.append(" and a.stat_bind_enab = 1 ");             //提高用户绑定率的需求
		sql.append(" and  a.user_type_id= '4'");                 //BSS用户工单
		if(false == StringUtil.IsEmpty(access_style_id)){
			sql.append(" and a.access_style_id=").append(access_style_id);	
		}
		if(false == StringUtil.IsEmpty(is_active) && !"".equals(is_active)){
			sql.append(" and a.is_active = ").append(is_active);
		}
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty()){
			for (int i = 0; i < list.size(); i++){
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}
	
	/**
	 * MAC比对新建用户
	 * 
	 * @author wangsenbo
	 * @param cityId
	 * @param endtime1
	 * @param starttime1
	 * @date Mar 1, 2010
	 * @param
	 * @return Map
	 */
	public Map countMac(String starttime1, String endtime1, String cityId,String userType)
	{
		logger.debug("countMac({},{},{},{})", new Object[] { starttime1, endtime1, cityId,userType });
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id ,count(*) as total ");
		}else{
			sql.append("select a.city_id ,count(1) as total ");
		}
		sql.append("from tab_hgwcustomer_report a");
		if(false == StringUtil.IsEmpty(userType) && !"0".equals(userType))
		{
			sql.append(",gw_cust_user_dev_type b ");
			sql.append("where a.user_type_id='5' and a.user_id=b.user_id and b.type_id='"+userType+"'");
		}
		else
		{
			sql.append(" where a.user_type_id='5'");
		}
		
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(
					")");
			cityIdList = null;
		}
		sql.append(" and a.stat_bind_enab = 1 ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.onlinedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.onlinedate<=").append(endtime1);
		}
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}

	/**
	 * 绑定方式统计
	 * 
	 * @author wangsenbo
	 * @date Mar 2, 2010
	 * @param
	 * @return List
	 */
	public List countBindWay(String starttime1, String endtime1, String cityId,String userType)
	{
		logger.debug("countMac({},{},{})", new Object[] { starttime1, endtime1, cityId });
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id,a.userline,a.is_chk_bind,count(*) as total ");
		}else{
			sql.append("select a.city_id,a.userline,a.is_chk_bind,count(1) as total ");
		}
		sql.append("from tab_hgwcustomer_report a ");
		if(false == StringUtil.IsEmpty(userType) && !"0".equals(userType))
		{
			sql.append(",gw_cust_user_dev_type b ");
			sql.append("where a.device_id!=null and a.device_id!='' ");
			sql.append("and a.user_id=b.user_id and b.type_id='"+userType+"'");
		}
		else
		{
			sql.append(" where a.device_id!=null and a.device_id!='' ");
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(
					")");
			cityIdList = null;
		}
		sql.append(" and a.stat_bind_enab = 1 ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.onlinedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.onlinedate<=").append(endtime1);
		}
		sql.append(" group by a.city_id,a.userline,a.is_chk_bind");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 统计用户列表用户数
	 * 
	 * @author wangsenbo
	 * @date Mar 2, 2010
	 * @param
	 * @return int
	 */
	public int getHgwCount(String starttime1, String endtime1, String cityId,
			String userTypeId, String userline, String isChkBind, int curPage_splitPage,
			int num_splitPage,String access_style_id,String userType,String is_active)
	{
		logger.debug("getHgwList({},{},{},{},{},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, userTypeId, userline, isChkBind, curPage_splitPage,
				num_splitPage, access_style_id, userType });
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_hgwcustomer_report a,gw_cust_user_dev_type b ");
		sql.append("where a.user_id=b.user_id ");
		if(false == StringUtil.IsEmpty(userType) && "1".equals(userType)){  // 1 表示 E8-B  按竣工时间
			sql.append("and a.is_active=1 ");
			sql.append("and b.type_id='").append(userType).append("'");
			if (false == StringUtil.IsEmpty(starttime1)){
				sql.append(" and a.dealdate >= ").append(starttime1);   // 竣工时间
			}
			if (false == StringUtil.IsEmpty(endtime1)){
				sql.append(" and a.dealdate <= ").append(endtime1);     // 竣工时间
			}
		}else if(false == StringUtil.IsEmpty(userType) && "2".equals(userType)){  // 2 表示 E8-C 按受理时间
			sql.append("and b.type_id='").append(userType).append("'");
			if (false == StringUtil.IsEmpty(starttime1)){
				sql.append(" and a.onlinedate >= ").append(starttime1);  // 竣工时间
			}
			if (false == StringUtil.IsEmpty(endtime1)){
				sql.append(" and a.onlinedate <= ").append(endtime1);    //竣工时间
			}
		}
		
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(
					")");
			cityIdList = null;
		}
		sql.append(" and a.stat_bind_enab = 1 ");
		if(false == StringUtil.IsEmpty(access_style_id)){
			sql.append(" and a.access_style_id=").append(access_style_id);	
		}
		if (false == StringUtil.IsEmpty(userTypeId)){
			sql.append(" and a.user_type_id='").append(userTypeId).append("'");
		}
		if (false == StringUtil.IsEmpty(userline)){
			if ("all".equals(userline)){
				List<Map<String, String>> bindtypelist = getAllBindWay();
				List userlines = new ArrayList();
				for (Map<String, String> bmap : bindtypelist)
				{
					userlines.add(bmap.get("userline"));
				}
				sql.append(" and a.device_id!=null and a.device_id!='' and a.userline in (").append(
						StringUtils.weaveNumber(userlines)).append(")");
				bindtypelist = null;
				userlines = null;
			}else{
				sql.append(" and a.device_id!=null and a.device_id!='' ");
				sql.append("and a.userline in ("+userline+")");
			}
		}
		if (false == StringUtil.IsEmpty(isChkBind)){
			sql.append(" and a.is_chk_bind=").append(isChkBind);
		}
		if(false == StringUtil.IsEmpty(is_active) && !"".equals(is_active)){
			sql.append(" and a.is_active = ").append(is_active);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 获取用户列表
	 * 
	 * @author wangsenbo
	 * @date Mar 2, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getHgwList(String starttime1, String endtime1, String cityId,
			String userTypeId, String userline, String isChkBind, int curPage_splitPage,
			int num_splitPage,String access_style_id,String userType,String is_active)
	{
		logger.debug("getHgwList({},{},{},{},{},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, userTypeId, userline, isChkBind, curPage_splitPage,
				num_splitPage, access_style_id,userType });
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,a.username,a.user_id,a.user_type_id,a.oui,");
		sql.append("a.device_serialnumber,a.opendate,a.opmode,a.userline ");
		sql.append("from tab_hgwcustomer_report a,gw_cust_user_dev_type b ");
		sql.append("where a.user_id=b.user_id ");
		if(false == StringUtil.IsEmpty(userType) && "1".equals(userType)){  // 1 表示 E8-B  按活跃用户
			sql.append("and a.is_active=1 ");
			sql.append("and b.type_id='").append(userType).append("'");
			if (false == StringUtil.IsEmpty(starttime1)){
				sql.append(" and a.dealdate >= ").append(starttime1);   // 受理时间
			}
			if (false == StringUtil.IsEmpty(endtime1)){
				sql.append(" and a.dealdate <= ").append(endtime1);     // 受理时间
			}
		}else if(false == StringUtil.IsEmpty(userType) && "2".equals(userType)){  // 2 表示 E8-C 按受理时间
			sql.append("and b.type_id='").append(userType).append("'");
			if (false == StringUtil.IsEmpty(starttime1)){
				sql.append(" and a.onlinedate >= ").append(starttime1);  // 受理时间
			}
			if (false == StringUtil.IsEmpty(endtime1)){
				sql.append(" and a.onlinedate <= ").append(endtime1);    // 受理时间
			}
		}
		
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		sql.append(" and a.stat_bind_enab = 1 ");
		if(false == StringUtil.IsEmpty(access_style_id)){
			sql.append(" and a.access_style_id=").append(access_style_id);	
		}
		if (false == StringUtil.IsEmpty(userTypeId)){
			sql.append(" and a.user_type_id='").append(userTypeId).append("'");  
		}
		if (false == StringUtil.IsEmpty(userline)){
			if ("all".equals(userline)){
				List<Map<String, String>> bindtypelist = getAllBindWay();
				List userlines = new ArrayList();
				for (Map<String, String> bmap : bindtypelist){
					userlines.add(bmap.get("userline"));
				}
				sql.append(" and a.device_id!=null and a.device_id!='' and a.userline in (").append(
						StringUtils.weaveNumber(userlines)).append(")");
				bindtypelist = null;
				userlines = null;
			}else{
				sql.append(" and a.device_id!=null and a.device_id!='' ");
				sql.append("and a.userline in (").append(userline).append(")");
			}
		}
		if (false == StringUtil.IsEmpty(isChkBind)){
			sql.append(" and a.is_chk_bind=").append(isChkBind);
		}
		if(false == StringUtil.IsEmpty(is_active) && !"".equals(is_active)){
			sql.append(" and a.is_active = ").append(is_active);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		userTypeMap = getUserType();
		bindTypeMap = getBindType();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				map.put("user_id", rs.getString("user_id"));
				String user_type_id = rs.getString("user_type_id");
				String tmp = "手工添加";
				if (false == StringUtil.IsEmpty(user_type_id)){
					tmp = userTypeMap.get(user_type_id);
					if (true == StringUtil.IsEmpty(tmp)){
						tmp = "其他";
					}
				}
				map.put("user_type", tmp);
				String oui = rs.getString("oui") == null ? "" : rs.getString("oui");
				String device_serialnumber = rs.getString("device_serialnumber") == null ? ""
						: rs.getString("device_serialnumber");
				map.put("device", oui + "-" + device_serialnumber);
				// 将opendate转换成时间
				try{
					long opendate = StringUtil.getLongValue(rs.getString("opendate"));
					DateTimeUtil dt = new DateTimeUtil(opendate * 1000);
					map.put("opendate", dt.getLongDate());
				}catch (NumberFormatException e){
					map.put("opendate", "");
				}catch (Exception e){
					map.put("opendate", "");
				}
				map.put("opmode", rs.getString("opmode"));
				// 绑定方式
				String bindtype = "-";
				String userline = rs.getString("userline");
				if (false == StringUtil.IsEmpty(userline)){
					bindtype = bindTypeMap.get(userline);
					if (true == StringUtil.IsEmpty(bindtype)){
						bindtype = "-";
					}
				}
				map.put("bindtype", bindtype);
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	/**
	 * 用户信息导出
	 * 
	 * @author wangsenbo
	 * @date Mar 2, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getHgwExcel(String starttime1, String endtime1, String cityId,
			String userTypeId, String userline, String isChkBind,String access_style_id,String userType,String is_active)
	{
		logger.debug("getHgwExcel({},{},{},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, userTypeId, userline, isChkBind,access_style_id, userType });
		StringBuffer sql = new StringBuffer();
		
		sql.append("select a.city_id,a.username,a.user_id,a.user_type_id,a.oui,a.device_serialnumber,a.opendate," +
				   "       a.opmode,a.userline " +
				   "  from tab_hgwcustomer_report a ,gw_cust_user_dev_type b  " +
				   " where a.user_id=b.user_id ");
		
		if(false == StringUtil.IsEmpty(userType) && "1".equals(userType)){  // 1 表示 E8-B  按竣工时间统计
			sql.append("and a.is_active=1 ");
			sql.append("and b.type_id='").append(userType).append("'");
			if (false == StringUtil.IsEmpty(starttime1)){
				sql.append(" and a.dealdate >= ").append(starttime1);   // 竣工时间
			}
			if (false == StringUtil.IsEmpty(endtime1)){
				sql.append(" and a.dealdate <= ").append(endtime1);     // 竣工时间
			}
		}else if(false == StringUtil.IsEmpty(userType) && "2".equals(userType)){  // 2 表示 E8-C 按受理时间统计
			sql.append("and b.type_id='").append(userType).append("'");
			if (false == StringUtil.IsEmpty(starttime1)){
				sql.append(" and a.onlinedate >= ").append(starttime1);  // 受理时间
			}
			if (false == StringUtil.IsEmpty(endtime1)){
				sql.append(" and a.onlinedate <= ").append(endtime1);    // 受理时间
			}
		}
		
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(
					")");
			cityIdList = null;
		}
		sql.append(" and a.stat_bind_enab = 1 ");
		if(false == StringUtil.IsEmpty(access_style_id)){
			sql.append(" and a.access_style_id=").append(access_style_id);	
		}
		if (false == StringUtil.IsEmpty(userTypeId)){
			sql.append(" and a.user_type_id='").append(userTypeId).append("'");
		}
		if (false == StringUtil.IsEmpty(userline)){
			if ("all".equals(userline)){
				List<Map<String, String>> bindtypelist = getAllBindWay();
				List userlines = new ArrayList();
				for (Map<String, String> bmap : bindtypelist){
					userlines.add(bmap.get("userline"));
				}
				sql.append(" and a.device_id!=null and a.device_id!='' and a.userline in (").append(
						StringUtils.weaveNumber(userlines)).append(")");
				bindtypelist = null;
				userlines = null;
			}else{
				sql.append(" and a.device_id!=null and a.device_id!='' and a.userline in (").append(userline).append(")");
			}
		}
		if (false == StringUtil.IsEmpty(isChkBind)){
			sql.append(" and a.is_chk_bind=").append(isChkBind);
		}
				if(false == StringUtil.IsEmpty(is_active) && !"".equals(is_active)){
					sql.append(" and a.is_active = ").append(is_active);
				}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		userTypeMap = getUserType();
		bindTypeMap = getBindType();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				map.put("user_id", rs.getString("user_id"));
				String user_type_id = rs.getString("user_type_id");
				String tmp = "手工添加";
				if (false == StringUtil.IsEmpty(user_type_id)){
					tmp = userTypeMap.get(user_type_id);
					if (true == StringUtil.IsEmpty(tmp)){
						tmp = "其他";
					}
				}
				map.put("user_type", tmp);
				String oui = rs.getString("oui") == null ? "" : rs.getString("oui");
				String device_serialnumber = rs.getString("device_serialnumber") == null ? ""
						: rs.getString("device_serialnumber");
				map.put("device", oui + "-" + device_serialnumber);
				// 将opendate转换成时间
				try{
					long opendate = StringUtil.getLongValue(rs.getString("opendate"));
					DateTimeUtil dt = new DateTimeUtil(opendate * 1000);
					map.put("opendate", dt.getLongDate());
				}catch (NumberFormatException e){
					map.put("opendate", "");
				}catch (Exception e){
					map.put("opendate", "");
				}
				String opmode = "";
				if ("1".equals(rs.getString("opmode"))){
					opmode = "已竣工";
				}else{
					opmode = "未竣工";
				}
				map.put("opmode", opmode);
				// 绑定方式
				String bindtype = "-";
				String userline = rs.getString("userline");
				if (false == StringUtil.IsEmpty(userline)){
					bindtype = bindTypeMap.get(userline);
					if (true == StringUtil.IsEmpty(bindtype)){
						bindtype = "-";
					}
				}
				map.put("bindtype", bindtype);
				return map;
			}
		});
		cityMap = null;
		userTypeMap = null;
		bindTypeMap = null;
		return list;
	}

	/**
	 * 查询所有绑定方式
	 * 
	 * @author wangsenbo
	 * @date May 18, 2010
	 * @param
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getAllBindWay()
	{
		String sql = "select bind_type_id as userline,type_name from bind_type";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 统计所有绑定方式的用户数
	 * 
	 * @param starttime1
	 * @param endtime1
	 * @param cityId
	 * @param userType
	 * @return
	 */
	public List<Map<String, String>> countAllBindWay(String starttime1, String endtime1, String cityId,String userType,String is_active)
	{
		logger.debug("countAllBindWay({},{},{})", new Object[] { starttime1, endtime1,
				cityId });
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id,a.userline,count(*) as total ");
		}else{
			sql.append("select a.city_id,a.userline,count(1) as total ");
		}
		if(LipossGlobals.inArea(Global.JSDX)){
			sql.append("from tab_hgwcustomer_report a ,gw_cust_user_dev_type b ");
		}else{
			sql.append("from tab_hgwcustomer a ,gw_cust_user_dev_type b ");
		}
		sql.append("where a.user_id=b.user_id ");
		
		if(false == StringUtil.IsEmpty(userType) && "1".equals(userType)){  // 1 表示 E8-B  按活跃用户统计
			logger.warn("userType=="+userType);
			sql.append(" and b.type_id='").append(userType).append("'");
			sql.append(" and a.user_type_id = '2' and a.is_active=1");
			if (false == StringUtil.IsEmpty(starttime1)){
				sql.append(" and a.dealdate >= ").append(starttime1);   // 受理时间
			}
			if (false == StringUtil.IsEmpty(endtime1)){
				sql.append(" and a.dealdate <= ").append(endtime1);     // 受理时间
			}
			
			if (LipossGlobals.isOracle()) {
				sql.append(" and a.device_id is not null ");
			}else {
				sql.append(" and a.device_id != null and a.device_id != '' ");
			}
		}else if(false == StringUtil.IsEmpty(userType) && "2".equals(userType)){  // 2 表示 E8-C 按受理时间统计
			sql.append("and b.type_id='").append(userType).append("'");
			if (false == StringUtil.IsEmpty(starttime1)){
				sql.append(" and a.onlinedate >= ").append(starttime1);  // 竣工时间
			}
			if (false == StringUtil.IsEmpty(endtime1)){
				sql.append(" and a.onlinedate <= ").append(endtime1);    // 竣工时间
			}
			
			if (LipossGlobals.isOracle()) {
				sql.append(" and a.device_id is not null ");
			}else{
				sql.append(" and a.device_id != null and a.device_id != '' ");
			}
		}
		
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(
					")");
			cityIdList = null;
		}
				if(false == StringUtil.IsEmpty(is_active) && !"".equals(is_active)){
					sql.append(" and a.is_active = ").append(is_active);
				}
		sql.append(" and a.stat_bind_enab = 1 ");		
		sql.append(" group by a.city_id,a.userline");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	public Map<String, String> getUserType()
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select user_type_id,type_name from user_type");
		}else{
			psql.append("select * from user_type");
		}
		List<Map> list = jt.queryForList(psql.getSQL());
		Map userTypeMap = new HashMap<String, String>();
		for (Map map : list)
		{
			userTypeMap.put(StringUtil.getStringValue(map.get("user_type_id")),
					StringUtil.getStringValue(map.get("type_name")));
		}
		return userTypeMap;
	}

	public Map<String, String> getBindType()
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select bind_type_id,type_name from bind_type");
		}else{
			psql.append("select * from bind_type");
		}
		List<Map> list = jt.queryForList(psql.getSQL());
		Map<String, String> bindTypeMap = new HashMap<String, String>();
		for (Map map : list)
		{
			bindTypeMap.put(StringUtil.getStringValue(map.get("bind_type_id")),
					StringUtil.getStringValue(map.get("type_name")));
		}
		return bindTypeMap;
	}

}
