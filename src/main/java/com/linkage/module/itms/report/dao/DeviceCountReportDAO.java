
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

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 统计设备数量
 * 
 * @author liyl10
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class DeviceCountReportDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(DeviceCountReportDAO.class);
	
	public List getDeviceNum(long starttime,long endtime,String cityId,String accessstyle)
	{
		logger.debug("DeviceCountReportDAO().getDeviceNum()");
		List<Map> tmplist = new ArrayList<Map>();
		List list = new ArrayList();
		
		StringBuffer sql = new StringBuffer("select city_id,access_style_id,count(access_style_id) as num ");
		sql.append("from tab_hgwcustomer where device_id is not null ");
		if (null != accessstyle && !"null".equals(accessstyle) && !"0".equals(accessstyle)) {
			sql.append(" and access_style_id =" + accessstyle + " ");
		}else{
			sql.append(" and access_style_id in (1,2,3,4) ");
		}
		if (null != cityId && !"null".equals(cityId)
				&& !"-1".equals(cityId)) {
			ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			String cityStr = cityId + "','" + StringUtils.weave(cityArray,"','");
			sql.append(" and city_id in ('" + cityStr + "')");
		}
		sql.append(" and opendate > " + starttime);
		sql.append(" and opendate < " + endtime);
		sql.append(" group by access_style_id,city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		tmplist = jt.queryForList(psql.getSQL());
		if(!tmplist.isEmpty()){
			for(int i = 0; i < tmplist.size(); i++){
				Map tmp = (Map)tmplist.get(i);
				String city_id = StringUtil.getStringValue(tmp,"city_id");
				String city_name = CityDAO.getCityName(city_id);
				tmp.put("city_name", city_name);
				String access_style_id = StringUtil.getStringValue(tmp,"access_style_id");
				String access_style_name = "ADSL";
				if("1".equals(access_style_id)){
					access_style_name = "ADSL";
				}else if("2".equals(access_style_id)){
					access_style_name = "LAN";
				}else if("3".equals(access_style_id)){
					access_style_name = "EPON";
				}else if("4".equals(access_style_id)){
					access_style_name = "GPON";
				}
				tmp.put("access_style_name", access_style_name);
				list.add(tmp);
			}
		}
		return list;
	}
	
	public List<Map<String,String>> getDetail(String cityId,String accesstype,long starttime,long endtime)
	{
		logger.debug("DeviceCountReportDAO().getDetail({},{},{},{})",
				new Object[]{cityId,accesstype,starttime,endtime});
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		String cityStr = cityId;
		ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
		if(cityArray != null && cityArray.size() > 0){
			cityStr = "','" + StringUtils.weave(cityArray,"','");
		}
		
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		StringBuffer sql = new StringBuffer("select a.device_id,a.username as loid,a.device_serialnumber,a.opendate,");
		sql.append("a.city_id,b.vendor_id,b.device_model_id,b.devicetype_id,b.device_type,c.username ");
		sql.append("from (select device_id,city_id,user_id,username,opendate,device_serialnumber ");
			sql.append("from tab_hgwcustomer where city_id in ('" + cityStr + "') ");
			sql.append("and access_style_id = " + accesstype + " ");
		sql.append("and device_id is not null ) a,tab_gw_device b,hgwcust_serv_info c ");
		sql.append("where a.device_id = b.device_id and a.user_id = c.user_id and c.serv_type_id = 10 ");
		sql.append("and a.opendate > " + starttime + " ");
		sql.append("and a.opendate < " + endtime );
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		list = jt.queryForList(psql.getSQL());
		
		return list;
	}
	
	
	public List getDetailForPage(int curPage_splitPage,int num_splitPage,
			String cityId,String accesstype,long starttime,long endtime)
	{
		logger.debug("DeviceCountReportDAO().getDetailForPage({},{},{},{},{},{})",
				new Object[]{curPage_splitPage,num_splitPage,cityId,accesstype,starttime,endtime});
		String cityStr = cityId;
		ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
		if(cityArray != null && cityArray.size() > 0){
			cityStr = StringUtils.weave(cityArray,"','");
		}
		if("0".equals(accesstype)){
			accesstype = "1,2,3,4";
		}
		
		StringBuffer sql = new StringBuffer("select c.loid,c.username,c.city_id,c.serv_type_id," +
				"d.device_serialnumber,d.device_id,");
		sql.append("d.complete_time,d.vendor_id,d.device_model_id,d.devicetype_id,d.device_type from ");
		
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("(select loid,device_id,access_style_id,city_id,opendate,username,serv_type_id from ");
			sql.append("(select a.username as loid,a.device_id,a.access_style_id,a.city_id,a.opendate,b.username,b.serv_type_id,");
			//substring_index(group_concat(b.user_id order by null),',',1)
			sql.append("substring_index(group_concat(b.user_id order by null),',',1) ");
			sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id = b.user_id)) c,");
			sql.append("tab_gw_device d where c.device_id = d.device_id ");
		}else{
			sql.append("(select * from ");
			sql.append("(select a.username as loid,a.device_id,a.access_style_id,a.city_id,a.opendate,b.username,b.serv_type_id,");
			sql.append("ROW_NUMBER() OVER(PARTITION BY b.user_id ORDER BY NULL) RN ");
			sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id = b.user_id)) c,");
			sql.append("tab_gw_device d where c.device_id = d.device_id and c.RN = 1 ");
		}
		
		sql.append("and c.access_style_id in (" + accesstype + ") ");
		sql.append("and c.city_id in ('" + cityStr + "') ");
		sql.append("and c.opendate > " + starttime + " and c.opendate < " + endtime + " ");
		sql.append("order by c.city_id");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		return querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						
						Map<String, String> map = new HashMap<String, String>();
						
						map.put("device_id", rs.getString("device_id"));
						map.put("city_id", rs.getString("city_id"));
						map.put("vendor_id", rs.getString("vendor_id"));
						map.put("device_model_id", rs.getString("device_model_id"));
						map.put("devicetype_id", rs.getString("devicetype_id"));
						map.put("complete_time", rs.getString("complete_time"));
						map.put("loid", rs.getString("loid"));
						map.put("device_type", rs.getString("device_type"));
						map.put("username", rs.getString("username"));
						map.put("device_serialnumber", rs.getString("device_serialnumber"));
						
						return map;
					}
				});
	}
	
	public List getDetailForPageBakNew(int curPage_splitPage,int num_splitPage,
			String cityId,String accesstype,long starttime,long endtime)
	{
		logger.debug("DeviceCountReportDAO().getDetailForPage({},{},{},{},{},{})",
				new Object[]{curPage_splitPage,num_splitPage,cityId,accesstype,starttime,endtime});
		String cityStr = cityId;
		ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
		if(cityArray != null && cityArray.size() > 0){
			cityStr = StringUtils.weave(cityArray,"','");
		}
		
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		StringBuffer sql = new StringBuffer("select c.loid,c.username,c.city_id,c.serv_type_id,d.device_serialnumber,d.device_id,");
		sql.append("d.complete_time,d.vendor_id,d.device_model_id,d.devicetype_id,d.device_type from ");
		
		if(DBUtil.GetDB()==3){
			sql.append("(select loid,device_id,access_style_id,city_id,opendate,username,serv_type_id,rn from ");
		}else{
			sql.append("(select * from ");
		}
		
		sql.append("(select a.username as loid,a.device_id,a.access_style_id,a.city_id,a.opendate,b.username,b.serv_type_id,");
		sql.append("ROW_NUMBER() OVER(PARTITION BY b.user_id ORDER BY NULL) RN ");
//		sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id = b.user_id) where RN = 1) c,");
		sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id = b.user_id)) c,");
		sql.append("tab_gw_device d where c.device_id = d.device_id and ");
		sql.append("c.access_style_id = " + accesstype + " ");
		sql.append("and c.city_id in ('" + cityStr + "') ");
		sql.append("and c.opendate > " + starttime + " ");
		sql.append("and c.opendate < " + endtime + " ");
		sql.append("order by c.city_id");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						
						Map<String, String> map = new HashMap<String, String>();
						
						map.put("device_id", rs.getString("device_id"));
						map.put("city_id", rs.getString("city_id"));
						map.put("vendor_id", rs.getString("vendor_id"));
						map.put("device_model_id", rs.getString("device_model_id"));
						map.put("devicetype_id", rs.getString("devicetype_id"));
						map.put("complete_time", rs.getString("complete_time"));
						map.put("loid", rs.getString("loid"));
						map.put("device_type", rs.getString("device_type"));
						map.put("username", rs.getString("username"));
						map.put("device_serialnumber", rs.getString("device_serialnumber"));
						
						return map;
					}
				});
	}
	
	public List getDetailForPageBak(int curPage_splitPage,int num_splitPage,
			String cityId,String accesstype,long starttime,long endtime)
	{
		logger.warn("DeviceCountReportDAO().getDetailForPage({},{},{},{},{},{})",
				new Object[]{curPage_splitPage,num_splitPage,cityId,accesstype,starttime,endtime});
		String cityStr = cityId;
		ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
		if(cityArray != null && cityArray.size() > 0){
			cityStr = "','" + StringUtils.weave(cityArray,"','");
		}
		
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		StringBuffer sql = new StringBuffer("select a.device_id,a.username as loid,a.device_serialnumber,b.complete_time,");
		sql.append("a.city_id,b.vendor_id,b.device_model_id,b.devicetype_id,b.device_type,c.username from ");
		sql.append("(select device_id,city_id,user_id,username,opendate,device_serialnumber ");
		sql.append("from tab_hgwcustomer where ");
		sql.append("city_id in ('" + cityStr + "') ");
		sql.append("and access_style_id = " + accesstype + " ");
		sql.append("and device_id is not null ) a,tab_gw_device b,hgwcust_serv_info c ");
		sql.append("where a.device_id = b.device_id and a.user_id = c.user_id and c.serv_type_id = 10 ");
		sql.append("and a.opendate > " + starttime + " ");
		sql.append("and a.opendate < " + endtime );
		sql.append(" order by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		return querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						
						Map<String, String> map = new HashMap<String, String>();
						
						map.put("device_id", rs.getString("device_id"));
						map.put("city_id", rs.getString("city_id"));
						map.put("vendor_id", rs.getString("vendor_id"));
						map.put("device_model_id", rs.getString("device_model_id"));
						map.put("devicetype_id", rs.getString("devicetype_id"));
						map.put("complete_time", rs.getString("complete_time"));
						map.put("loid", rs.getString("loid"));
						map.put("username", rs.getString("username"));
						map.put("device_type", rs.getString("device_type"));
						map.put("device_serialnumber", rs.getString("device_serialnumber"));
						
						return map;
					}
				});
	}
	
	
	public int getDetailCount(int num_splitPage,String cityId,String accesstype,long starttime,long endtime)
	{
		logger.warn("DeviceCountReportDAO().getDetailCount({},{},{},{})",
				new Object[]{num_splitPage,cityId,accesstype,starttime,endtime});
		String cityStr = cityId;
		ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
		if(cityArray != null && cityArray.size() > 0){
			cityStr = "','" + StringUtils.weave(cityArray,"','");
		}
		if("0".equals(accesstype)){
			accesstype = "1,2,3,4";
		}
		
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) from ");
			sql.append("(select loid,device_id,access_style_id,city_id,opendate,username,serv_type_id,rn ");
		}else{
			sql.append("select count(1) from (select * ");
		}
		
		sql.append("from (select a.username as loid,a.device_id,a.access_style_id,a.city_id,a.opendate,b.username,b.serv_type_id,");
		sql.append("ROW_NUMBER() OVER(PARTITION BY b.user_id ORDER BY NULL) RN ");
//		sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id = b.user_id) where RN = 1) c,");
		sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id = b.user_id)) c,");
		sql.append("tab_gw_device d where c.device_id = d.device_id and c.RN = 1 and ");
		sql.append("c.access_style_id in (" + accesstype + ") ");
		sql.append("and c.city_id in ('" + cityStr + "') ");
		sql.append("and c.opendate > " + starttime + " ");
		sql.append("and c.opendate < " + endtime + " ");
		sql.append("order by c.city_id");
		
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
	
	
	public int getDetailCountBak(int num_splitPage,String cityId,String accesstype,long starttime,long endtime){
		logger.warn("DeviceCountReportDAO().getDetailCount({},{},{},{})",new Object[]{num_splitPage,cityId,accesstype,starttime,endtime});
		String cityStr = cityId;
		ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
		if(cityArray != null && cityArray.size() > 0){
			cityStr = "','" + StringUtils.weave(cityArray,"','");
		}
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) from ");
		}else{
			sql.append("select count(1) from ");
		}
		
		sql.append("(select device_id,city_id,user_id,username,opendate,device_serialnumber ");
		sql.append("from tab_hgwcustomer where ");
		sql.append("city_id in ('" + cityStr + "') ");
		sql.append("and access_style_id = " + accesstype + " ");
		sql.append("and device_id is not null ) a,tab_gw_device b,hgwcust_serv_info c ");
		sql.append("where a.device_id = b.device_id and a.user_id = c.user_id and c.serv_type_id = 10 ");
		sql.append("and a.opendate > " + starttime + " ");
		sql.append("and a.opendate < " + endtime );
		
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
	
	/**
	 * 加载所有厂商信息到map中
	 * @return
	 */
	public Map<String,String> getVendorMap(){
		logger.warn("DeviceCountReportDAO().getVendorMap()");
		String sql = "select vendor_id,vendor_name,vendor_add from tab_vendor order by vendor_add";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		
		Map<String,String> map = new HashMap<String,String>();
		if(!list.isEmpty()){
			for(Map<String,String> tmp : list){
				String vendor_add = (String) StringUtil.getStringValue(tmp.get("vendor_add"));
				if (vendor_add != null && !"".equals(vendor_add)) {
					map.put(StringUtil.getStringValue(tmp.get("vendor_id")), vendor_add + "("
							+ StringUtil.getStringValue(tmp.get("vendor_id")) + ")");
				} else {
					map.put(
							StringUtil.getStringValue(tmp.get("vendor_id")),
							StringUtil.getStringValue(tmp.get("vendor_name")) + "("
									+ StringUtil.getStringValue(tmp.get("vendor_id")) + ")");
				}
			}
		}
		return map;
	}
	
	/**
	 * 加载所有型号信息到map中
	 * @return
	 */
	public Map<String,String> getModelMap(){
		logger.debug("DeviceCountReportDAO().getModelMap()");
		String sql = "select device_model_id,device_model from gw_device_model order by device_model_id";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		
		Map<String,String> map = new HashMap<String,String>();
		if(!list.isEmpty()){
			for(Map<String,String> tmp : list){
				String device_model_id = StringUtil.getStringValue(tmp,"device_model_id");
				String device_model = StringUtil.getStringValue(tmp,"device_model");
				map.put(device_model_id, device_model);
			}
		}
		return map;
	}
	
	/**
	 * 加载所有版本信息到map中
	 * @return
	 */
	public Map<String,String> getVersionMap(){
		logger.debug("DeviceCountReportDAO().getVersionMap()");
		String sql = "select devicetype_id,softwareversion from tab_devicetype_info order by devicetype_id";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		
		Map<String,String> map = new HashMap<String,String>();
		if(!list.isEmpty()){
			for(Map<String,String> tmp : list){
				String devicetype_id = StringUtil.getStringValue(tmp,"devicetype_id");
				String softwareversion = StringUtil.getStringValue(tmp,"softwareversion");
				map.put(devicetype_id, softwareversion);
			}
		}
		return map;
	}


	public Map<String, String> getDualStack() 
	{
		StringBuffer psql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			psql.append("select manualsucc,manualundo,manualfail,automaticsucc,automaticundo,");
			psql.append("automaticfail,nottianyi,tianyi,dualstacksucc,");
			psql.append("dualstackundo,dualstackfail,total from tab_dualStack ");
		}else{
			psql.append("select * from tab_dualStack ");
		}
		
		return DBOperation.getRecord(psql.toString());
	}
}
