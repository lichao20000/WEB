package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class BindReportDAO extends SuperDAO {

	private static Logger logger = LoggerFactory
			.getLogger(BindReportDAO.class);

	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;

	/**
	 * 统计未绑定用户数据量
	 * 
	 * @param starttime1
	 * @param endtime1
	 * @param cityId
	 * @param usertype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getOperUnOkNum(String starttime1,
			String endtime1, String cityId, String usertype ,String gw_type) {

		logger.debug("getOperNoNum({},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, usertype });

		Map<String, String> map = new HashMap<String, String>();

		StringBuffer sql = new StringBuffer();
		//当系统为家庭网关时
		if("1".equals(gw_type)){
			if(DBUtil.GetDB()==3){
				sql.append("select a.city_id,count(*) cnt ");
			}else{
				sql.append("select a.city_id,count(1) cnt ");
			}
				sql.append("from tab_hgwcustomer a,gw_cust_user_dev_type b ");
				sql.append("where a.user_id=b.user_id ");
				if ("1".equals(usertype)) { // E8-b
					sql.append(" and b.type_id='1'");
					sql.append(" and  a.stat_bind_enab = 1");
					if (false == StringUtil.IsEmpty(starttime1)) {
						sql.append(" and a.dealdate>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1)) {
						sql.append(" and a.dealdate<=").append(endtime1);
					}
					sql.append(" and a.is_active=1 ");
				} else if ("2".equals(usertype)) {
					sql.append(" and b.type_id='2'"); // E8-c
					if (false == StringUtil.IsEmpty(starttime1)) {
						sql.append(" and a.dealdate>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1)) {
						sql.append(" and a.dealdate<=").append(endtime1);
					}
				} else {
					logger.error("用户终端类型不正确！");
					return map;
				}
		
				if (LipossGlobals.isOracle()) {
					sql.append("  and a.device_id is null and a.user_state in ('1','2')");
				}else {
					sql.append("  and (a.device_id = null or a.device_id = '')  and a.user_state in ('1','2')");
				}
		
				if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
					List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
					sql.append(" and a.city_id in (").append(
							StringUtils.weave(cityIdList)).append(")");
					cityIdList = null;
				}
				sql.append("  group by a.city_id");
			}
			//当系统为企业网关时,不区分终端设备的类型
			else if("2".equals(gw_type))
			{
				if(DBUtil.GetDB()==3){
					sql.append("select b.city_id,count(*) cnt ");
				}else{
					sql.append("select b.city_id,count(1) cnt ");
				}
				sql.append("from tab_egwcustomer a,tab_customerinfo b ");
				sql.append("where a.customer_id = b.customer_id ");
				sql.append("and a.user_state in ('1','2') " );
				
				if (LipossGlobals.isOracle()) {
					sql.append(" and a.device_id is null " );
				}else {
					sql.append(" and (a.device_id = null or a.device_id = '')" );
				}
				
				if (!StringUtil.IsEmpty(starttime1)) {
					sql.append(" and a.opendate>=").append(starttime1);
				}
				if (!StringUtil.IsEmpty(endtime1)) {
					sql.append(" and a.opendate<=").append(endtime1);
				}
				if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
					List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
					sql.append(" and b.city_id in (").append(
							StringUtils.weave(cityIdList)).append(")");
					cityIdList = null;
				}
				sql.append("  group by b.city_id");
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	/**
	 * 统计已绑定的用户数据量
	 * 
	 * @param starttime1
	 * @param endtime1
	 * @param cityId
	 * @param usertype
	 * @return
	 */
	public Map<String, String> getOperOkNum(String starttime1, String endtime1,
			String cityId, String usertype, String gw_type) 
	{
		logger.debug("getOperOkNum({},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, usertype });

		Map<String, String> map = new HashMap<String, String>();

		StringBuffer sql = new StringBuffer();
		//当为家庭网关时
		if("1".equals(gw_type)){
			if(DBUtil.GetDB()==3){
				sql.append(" select a.city_id,count(*) cnt ");
			}else{
				sql.append(" select a.city_id,count(1) cnt ");
			}
			sql.append(" from tab_hgwcustomer a,gw_cust_user_dev_type b  ");
			sql.append(" where a.user_id=b.user_id ");
	
			if ("1".equals(usertype)) { // E8-b
				sql.append(" and b.type_id='1'");
				sql.append(" and  a.stat_bind_enab = 1");
				if (false == StringUtil.IsEmpty(starttime1)) {
					sql.append(" and a.dealdate>=").append(starttime1);
				}
				if (false == StringUtil.IsEmpty(endtime1)) {
					sql.append(" and a.dealdate<=").append(endtime1);
				}
				sql.append(" and a.is_active=1 ");
			} else if ("2".equals(usertype)) {
				sql.append(" and b.type_id='2'"); // E8-c
				if (false == StringUtil.IsEmpty(starttime1)) {
					sql.append(" and a.dealdate>=").append(starttime1);
				}
				if (false == StringUtil.IsEmpty(endtime1)) {
					sql.append(" and a.dealdate<=").append(endtime1);
				}
			} else {
				logger.error("用户终端类型不正确！");
				return map;
			}
			
			if (LipossGlobals.isOracle()) {
				sql.append("  and a.device_id is not null and a.user_state in ('1','2')");
			}else {
				sql.append("  and a.device_id != null  and a.device_id != ''  and a.user_state in ('1','2')");
			}
			
	
			if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and a.city_id in (").append(
						StringUtils.weave(cityIdList)).append(")");
				cityIdList = null;
			}
	
			sql.append("  group by a.city_id");
		}  //当系统为企业网关时,不区分终端设备的类型
		else if("2".equals(gw_type))
		{                
			if(DBUtil.GetDB()==3){
				sql.append("select b.city_id,count(*) cnt ");
			}else{
				sql.append("select b.city_id,count(1) cnt ");
			}
			sql.append("from tab_egwcustomer a ,tab_customerinfo b ");
			sql.append("where a.customer_id = b.customer_id ");
			sql.append("and a.user_state in ('1','2') " );
			
			if (LipossGlobals.isOracle()) {
				sql.append(" and a.device_id is not null" );
			}else {
				sql.append(" and a.device_id != null  and a.device_id != ''" );
			}
			
			if (!StringUtil.IsEmpty(starttime1)) {
				sql.append(" and a.opendate>=").append(starttime1);
			}
			if (!StringUtil.IsEmpty(endtime1)) {
				sql.append(" and a.opendate<=").append(endtime1);
			}
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and b.city_id in (").append(
						StringUtils.weave(cityIdList)).append(")");
				cityIdList = null;
			}
			sql.append("  group by b.city_id");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	/**
	 * 已绑定用户信息 未绑定用户信息 列表展示
	 * 
	 * @param starttime1
	 * @param endtime1
	 * @param cityId
	 * @param bind_flag
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param usertype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getUserList(String starttime1, String endtime1,
			String cityId, String bind_flag, int curPage_splitPage,
			int num_splitPage, String usertype,String gw_type) {

		logger.debug("getUserList({},{},{},{},{},{},{})", new Object[] {
				starttime1, endtime1, cityId, bind_flag, curPage_splitPage,
				num_splitPage, usertype });

		StringBuffer sql = new StringBuffer();
		if("1".equals(gw_type)){
				sql.append("select a.city_id, a.user_id, a.username, a.dealdate, a.onlinedate "
						+ "  from tab_hgwcustomer a, gw_cust_user_dev_type b  "
						+ " where a.user_id = b.user_id ");

				if ("1".equals(usertype)) { // E8-b

				sql.append(" and b.type_id = '1' "); // E8-b
				sql.append(" and a.user_state in ('1','2') "
						+ " and a.stat_bind_enab = 1 ");
	
				if ("1".equals(bind_flag)) { // 已绑定用户
					if (LipossGlobals.isOracle()) {
						sql.append(" and a.device_id is not null ");
					}else {
						sql.append(" and a.device_id != null ");
						sql.append(" and a.device_id != '' ");
					}
				} else if ("0".equals(bind_flag)) { // 未绑定用户
					if (LipossGlobals.isOracle()) {
						sql.append(" and a.device_id is null  ");
					}else {
						sql.append(" and (a.device_id = null or a.device_id = '')  ");
					}
				}
				if (false == StringUtil.IsEmpty(starttime1)) {
					sql.append(" and a.dealdate >= ").append(starttime1);
				}
				if (false == StringUtil.IsEmpty(endtime1)) {
					sql.append(" and a.dealdate <= ").append(endtime1);
				}
				sql.append(" and a.is_active=1 ");
			} else if ("2".equals(usertype)) { // E8-c
	
				sql.append(" and b.type_id='2'"); // E8-c
				sql.append(" and a.user_state in ('1','2') ");
	
				if ("1".equals(bind_flag)) { // 已绑定用户
					if (LipossGlobals.isOracle()) {
						sql.append(" and a.device_id is not null ");
					}else {
						sql.append(" and a.device_id != null and a.device_id != '' ");
					}
				} else if ("0".equals(bind_flag)) { // 未绑定用户
					if (LipossGlobals.isOracle()) {
						sql.append(" and a.device_id is null ");
					}else {
						sql.append(" and (a.device_id = null or a.device_id = '')  ");
					}
				}
				if (false == StringUtil.IsEmpty(starttime1)) {
					sql.append(" and a.dealdate >= ").append(starttime1);
				}
				if (false == StringUtil.IsEmpty(endtime1)) {
					sql.append(" and a.dealdate <= ").append(endtime1);
				}
			} else {
				logger.error("用户终端类型不正确！");
				return null;
			}
	
			if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and a.city_id in (").append(
						StringUtils.weave(cityIdList)).append(")");
				cityIdList = null;
			}
		}else if("2".equals(gw_type)){
			sql.append("select b.city_id, a.user_id, a.username, a.dealdate, a.onlinedate ,a.opendate"
					+ "  from tab_egwcustomer a, tab_customerinfo b  "
					+ " where a.customer_id = b.customer_id ");
			sql.append(" and a.user_state in ('1','2')");
			if ("1".equals(bind_flag)) { // 已绑定用户
				if (LipossGlobals.isOracle()) {
					sql.append(" and a.device_id is not null ");
				}else {
					sql.append(" and a.device_id != null ");
					sql.append(" and a.device_id != '' ");
				}
			} else if ("0".equals(bind_flag)) { // 未绑定用户
				if (LipossGlobals.isOracle()) {
					sql.append(" and a.device_id is null  ");
				}else{
					sql.append(" and (a.device_id = null or a.device_id = '')  ");
				}
			}
			if (false == StringUtil.IsEmpty(starttime1)) {
				sql.append(" and a.opendate >= ").append(starttime1);
			}
			if (false == StringUtil.IsEmpty(endtime1)) {
				sql.append(" and a.opendate <= ").append(endtime1);
			}
			if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and b.city_id in (").append(
						StringUtils.weave(cityIdList)).append(")");
				cityIdList = null;
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());

		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap
						.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				} else {
					map.put("city_name", "");
				}
				map.put("user_id", rs.getString("user_id"));

				// 将dealdate转换成时间
				try {
					long dealdate = StringUtil.getLongValue(rs
							.getString("dealdate")); // 受理时间
					if(0==dealdate){
						map.put("dealdate", "");
					}else{
						DateTimeUtil dDate = new DateTimeUtil(dealdate * 1000);
						map.put("dealdate", dDate.getDate());
					}
				} catch (NumberFormatException e) {
					map.put("dealdate", "");
				} catch (Exception e) {
					map.put("dealdate", "");
				}

				// 将onlinedate转换成时间
				try {
					long onlinedate = StringUtil.getLongValue(rs
							.getString("onlinedate")); // 竣工时间
					if( 0==onlinedate){
						map.put("onlinedate", "");
					}else{
						DateTimeUtil oDate = new DateTimeUtil(onlinedate * 1000);
						map.put("onlinedate", oDate.getDate());
					}
				} catch (NumberFormatException e) {
					map.put("onlinedate", "");
				} catch (Exception e) {
					map.put("onlinedate", "");
				}
				// 将opendate转换成时间
				try {
					long opendate = StringUtil.getLongValue(rs
							.getString("opendate")); //开户时间
					if(0==opendate){
						map.put("opendate", "");
					}else{
						DateTimeUtil oDate = new DateTimeUtil(opendate * 1000);
						map.put("opendate", oDate.getDate());
					}
					
				} catch (NumberFormatException e) {
					map.put("opendate", "");
				} catch (Exception e) {
					map.put("opendate", "");
				}
				return map;
			}
		});
		cityMap = null;
		return list;

	}

	/**
	 * 统计 用户总数 （已绑定用户、未绑定用户）
	 * 
	 * @param starttime1
	 * @param endtime1
	 * @param cityId
	 * @param bind_flag
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param usertype
	 * @return
	 */
	public int getUsersCount(String starttime1, String endtime1, String cityId,
			String bind_flag, int curPage_splitPage, int num_splitPage,
			String usertype ,String gw_type) {
		logger.debug("getUsersCount({},{},{},{},{},{},{})", new Object[] {
				starttime1, endtime1, cityId, bind_flag, curPage_splitPage,
				num_splitPage, usertype });

		StringBuffer sql = new StringBuffer();
		if("1".equals(gw_type)){
			if(DBUtil.GetDB()==3){
				sql.append("select count(*) ");
			}else{
				sql.append("select count(1) ");
			}
			sql.append("from tab_hgwcustomer a, gw_cust_user_dev_type b  "
					+ " where a.user_id = b.user_id ");
	
			if ("1".equals(usertype)) { // E8-b
	
				sql.append(" and b.type_id = '1' "); // E8-b
				sql.append(" and a.user_state in ('1','2') "
						+ " and a.stat_bind_enab = 1 ");
	
				if ("1".equals(bind_flag)) { // 已绑定用户
					if (LipossGlobals.isOracle()) {
						sql.append(" and a.device_id is not null ");
					}else{
						sql.append(" and a.device_id != null ");
						sql.append(" and a.device_id != '' ");
					}
				} else if ("0".equals(bind_flag)) { // 未绑定用户
					if (LipossGlobals.isOracle()) {
						sql.append(" and a.device_id is null ");
					}else{
						sql.append(" and (a.device_id = null or a.device_id = '')  ");
					}
				}
				if (false == StringUtil.IsEmpty(starttime1)) {
					sql.append(" and a.dealdate >= ").append(starttime1);
				}
				if (false == StringUtil.IsEmpty(endtime1)) {
					sql.append(" and a.dealdate <= ").append(endtime1);
				}
				sql.append(" and a.is_active=1 ");
			} else if ("2".equals(usertype)) { // E8-c
	
				sql.append(" and b.type_id='2'"); // E8-c
				sql.append(" and a.user_state in ('1','2') ");
	
				if ("1".equals(bind_flag)) { // 已绑定用户
					if (LipossGlobals.isOracle()) {
						sql.append(" and a.device_id is not null ");
					}else{
						sql.append(" and a.device_id != null and a.device_id != '' ");
					}
				} else if ("0".equals(bind_flag)) { // 未绑定用户
					if (LipossGlobals.isOracle()) {
						sql.append(" and a.device_id is null ");
					}else{
						sql.append(" and (a.device_id = null or a.device_id = '')  ");
					}
				}
				if (false == StringUtil.IsEmpty(starttime1)) {
					sql.append(" and a.dealdate >= ").append(starttime1);
				}
				if (false == StringUtil.IsEmpty(endtime1)) {
					sql.append(" and a.dealdate <= ").append(endtime1);
				}
			} else {
				logger.error("用户终端类型不正确！");
				return 0;
			}
	
			if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and a.city_id in (").append(
						StringUtils.weave(cityIdList)).append(")");
				cityIdList = null;
			}
		}
		else if("2".equals(gw_type))
		{
			if(DBUtil.GetDB()==3){
				sql.append("select count(*) ");
			}else{
				sql.append("select count(1) ");
			}
			sql.append("from tab_egwcustomer a, tab_customerinfo b  "
					+ " where a.customer_id = b.customer_id ");
			sql.append(" and a.user_state in ('1','2')");
			if ("1".equals(bind_flag)) { // 已绑定用户
				if (LipossGlobals.isOracle()) {
					sql.append(" and a.device_id is not null ");
				}else{
					sql.append(" and a.device_id != null ");
					sql.append(" and a.device_id != '' ");
				}
			} else if ("0".equals(bind_flag)) { // 未绑定用户
				if (LipossGlobals.isOracle()) {
					sql.append(" and a.device_id is null ");
				}else{
					sql.append(" and (a.device_id = null or a.device_id = '')  ");
				}
			}
			if (false == StringUtil.IsEmpty(starttime1)) {
				sql.append(" and a.opendate >= ").append(starttime1);
			}
			if (false == StringUtil.IsEmpty(endtime1)) {
				sql.append(" and a.opendate <= ").append(endtime1);
			}
			if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and b.city_id in (").append(
						StringUtils.weave(cityIdList)).append(")");
				cityIdList = null;
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
}
