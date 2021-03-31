
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
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 按开始时间、结束时间、设备属地、用户业务类型、是否活跃统计
 * 总数统计所有用户，用户详表则只统计三个业务类型数据
 * @author wanghong5 2015-02-03
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class CountServReportDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(CountServReportDAO.class);
	
	/**
	 * 业务列表
	 */
	private List<Map<String,String>> specList;
	/**
	 * 所有用户列表
	 */
	private List<Map<String,String>> countList;
	/**
	 * 所有用户分类列表
	 */
	private List<Map<String,String>> countListByType;
	
	public CountServReportDAO(){
		
	}
	
	/**
	 * 获取类型列表
	 * @return  List<Map<String id,String name>>
	 */
	public List<Map<String, String>> getSpecList() 
	{
		String sql = "select id,spec_name from tab_bss_dev_port ";
		PrepareSQL psql = new PrepareSQL(sql);
		specList= jt.queryForList(psql.getSQL());
		
		Map map=new HashMap();
		String id="-1",spec_name="全部";
		map.put("id", id);
		map.put("spec_name", spec_name);
		specList.add(map);
		return specList;
	}
	
	/**
	 * 获取城市(市、区)对应的用户数    
	 * @param cityList
	 * @param specId
	 * @param starttimeCount
	 * @param endtimeCount
	 * @param is_active
	 * @return List<Map<String id, String num>> 
	 */
	public List<Map<String, String>> getCountAllList(String cityId,long starttimeCount,
			long endtimeCount,String specId,String is_active) 
	{
		logger.debug("CountServReportDAO.getCountAllList()--start");
		ArrayList<String> cityArray = CityDAO.getNextCityIdsByCityPid(cityId);
		String cityStr="'";
		if(cityArray != null && cityArray.size() > 0){
			cityStr += StringUtils.weave(cityArray,"','");
		}
		cityStr +="'";
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) num,a.city_id ");
		}else{
			sql.append("select count(1) num,a.city_id ");
		}
		sql.append("from tab_hgwcustomer a,gw_cust_user_dev_type b ");
		sql.append("where a.user_id = b.user_id ");
		if(!"00".equals(cityId)){
			sql.append(" and city_id in ( " +cityStr+" ) ");
		}
		if(!"-1".equals(specId)){
			sql.append(" and a.spec_id = " +specId);
		}
		if(!"-1".equals(is_active)){
			sql.append(" and a.is_active = "+is_active);
		}
		sql.append(" and a.dealdate>=" +starttimeCount+" and a.dealdate<=" +endtimeCount);
		sql.append(" and b.type_id='2' ");
		sql.append(" group by a.city_id ");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		countList= jt.queryForList(psql.getSQL());
		return countList;
	}
	
	/**
	 * 根据城市、用户类型获取用户报表
	 * @param starttimeCount
	 * @param endtimeCount
	 * @param cityId
	 * @param specId
	 * @param is_active
	 * @return List<Map<String num, String city_id,String ser_type_id>>
	 */
	public List<Map<String, String>> getCountAllListByType(String cityId,long starttimeCount,
			long endtimeCount,String specId,String is_active) 
	{
		logger.warn("CountServReportDAO.getCountAllListByType()--start");
		ArrayList<String> cityArray = CityDAO.getNextCityIdsByCityPid(cityId);
		String cityStr="'";
		if(cityArray != null && cityArray.size() > 0){
			cityStr += StringUtils.weave(cityArray,"','");
		}
		cityStr += "'";
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) num,");
		}else{
			sql.append("select count(1) num,");
		}
		sql.append("a.city_id city_id,c.serv_type_id serv_type_id "); 
		sql.append("from tab_hgwcustomer a,gw_cust_user_dev_type b,hgwcust_serv_info c ");	
		sql.append("where a.user_id=b.user_id and a.user_id=c.user_id ");
		if(!"00".equals(cityId)){
			sql.append(" and city_id in ( "+cityStr+" ) ");
		}
		if(!"-1".equals(is_active)){
			sql.append(" and a.is_active = "+is_active);
		}
		sql.append(" and a.dealdate>="+starttimeCount+" and a.dealdate<="+endtimeCount);
		if(!"-1".equals(specId)){
			sql.append(" and a.spec_id = "+specId);
		}
		sql.append(" and b.type_id = '2' ");
		sql.append(" and c.serv_type_id in (10,11,14) ");
		sql.append(" group by a.city_id,c.serv_type_id ");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		countListByType= jt.queryForList(psql.getSQL());
		return countListByType;
	}
	
	/**
	 * 获取用户详细报表
	 */
	public List<Map> getDetailsForPage(String city_id,long starttimeCount,long endtimeCount,
			 String specId, String is_active,String serv_type_id,
			int curPage_splitPage, int num_splitPage) 
	{
		logger.debug("CountServReportDAO.getDetailsForPage()--start");
		List<Map> list = new ArrayList<Map>();
		
		ArrayList<String> cityArray = CityDAO.getNextCityIdsByCityPid(city_id);
		String cityStr="'";
		if(cityArray != null && cityArray.size() > 0){
			cityStr += StringUtils.weave(cityArray,"','");
		}
		cityStr += "'";
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		//设备序列号device_serialnumbe  LOID a.username  受理时间dealdate  业务账号c.username 
		sql.append("select a.device_serialnumber deviceNum,a.username LOID," +
						"c.serv_type_id serv_type_id,c.username operationNum,a.dealdate dealdate "); 
		sql.append(" from tab_hgwcustomer a,gw_cust_user_dev_type b,hgwcust_serv_info c ");	
		sql.append(" where a.user_id = b.user_id and a.user_id = c.user_id ");
		if(!"00".equals(city_id)){
			sql.append(" and city_id in ( "+cityStr+" ) ");
		}
		if(!"-1".equals(is_active)){
			sql.append(" and a.is_active = "+is_active);
		}
		sql.append(" and b.type_id = '2' ");
		sql.append("and a.dealdate<="+endtimeCount +"and a.dealdate>="+starttimeCount+" ");
		if(!"-1".equals(specId)){
			sql.append("and a.spec_id = "+specId);
		}
		sql.append(" and c.serv_type_id in ( "+serv_type_id+" ) ");
				
		PrepareSQL psql = new PrepareSQL(sql.toString());
		 list=querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				 		num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1)throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				//设备序列号
				String deviceNum = rs.getString("deviceNum");
				//LOID
				String LOID = rs.getString("LOID");
				//业务名称
				String operationName="宽带业务";
				if("11".equals(rs.getString("serv_type_id"))){
					operationName ="IPTV业务";
				}else if("14".equals(rs.getString("serv_type_id"))){
					operationName ="VOIP业务";
				}
				//业务账号
				String operationNum = rs.getString("operationNum");
				// 将dealdate转换成时间
				try{
					long dealdate = StringUtil.getLongValue(rs.getString("dealdate"));
					DateTimeUtil dt = new DateTimeUtil(dealdate * 1000);
					map.put("dealdate", dt.getLongDate());
				}catch (Exception e){
					map.put("dealdate", "");
				}
				map.put("deviceNum", deviceNum);
				map.put("LOID", LOID);
				map.put("serv_type_id",rs.getString("serv_type_id"));
				map.put("operationName",operationName);
				map.put("operationNum", operationNum);
				
				return map;
			}
		});	
		return list;
	}
	
	/**
	 * 获取属地详细报表最大页数
	 */
	public int getDetailsCount(String city_id,long starttimeCount,long endtimeCount, 
			String specId, String is_active,
			String serv_type_id, int num_splitPage) 
	{
		 logger.debug("CountServReportDAO.getDetailsCount()--start");
		ArrayList<String> cityArray = CityDAO.getNextCityIdsByCityPid(city_id);
		String cityStr="'";
		if(cityArray != null && cityArray.size() > 0){
			cityStr += StringUtils.weave(cityArray,"','");
		}
		cityStr += "'";
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) "); 
		}else{
			sql.append("select count(1) "); 
		}
		sql.append(" from tab_hgwcustomer a,gw_cust_user_dev_type b,hgwcust_serv_info c ");	
		sql.append(" where a.user_id = b.user_id and a.user_id = c.user_id ");
		if(!"00".equals(city_id)){
			sql.append(" and city_id in ( "+cityStr+" ) ");
		}
		if(!"-1".equals(is_active)){
			sql.append(" and a.is_active = "+is_active);
		}
		sql.append("and a.dealdate<="+endtimeCount +"and a.dealdate>="+starttimeCount+" ");
		if(!"-1".equals(specId)){
			sql.append(" and a.spec_id = "+specId);
		}
		sql.append(" and c.serv_type_id in ( "+serv_type_id+" ) ");
		sql.append(" and b.type_id = '2' ");
		
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
	 * 导出excel报表
	 */
	public List<Map> getDetailsExcel(String city_id,long starttimeCount,long endtimeCount,
			 String specId, String is_active,String serv_type_id) 
	{
		 logger.debug("CountServReportDAO.getDetailsExcel()--start");
		ArrayList<String> cityArray = CityDAO.getNextCityIdsByCityPid(city_id);
		String cityStr="'";
		if(cityArray != null && cityArray.size() > 0){
			cityStr += StringUtils.weave(cityArray,"','");
		}
		cityStr += "'";
		
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		//设备序列号device_serialnumbe  LOID a.username  受理时间dealdate  业务账号c.username 
		sql.append("select a.device_serialnumber deviceNum,a.username LOID," +
				"c.serv_type_id serv_type_id,c.username operationNum,a.dealdate dealdate "); 
		sql.append(" from tab_hgwcustomer a,gw_cust_user_dev_type b,hgwcust_serv_info c ");	
		sql.append(" where a.user_id = b.user_id and a.user_id = c.user_id ");
		if(!"00".equals(city_id)){
			sql.append(" and city_id in ( "+cityStr+" ) ");
		}
		if(!"-1".equals(is_active)){
			sql.append(" and a.is_active = "+is_active);
		}
		sql.append(" and a.dealdate<= "+endtimeCount +" and a.dealdate>= "+starttimeCount+" ");
		if(!"-1".equals(specId)){
			sql.append(" and a.spec_id = "+specId);
		}
		sql.append(" and c.serv_type_id in ( "+serv_type_id+" ) ");
		sql.append(" and b.type_id = '2' ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	public List<Map<String, String>> getCountList() {
		return countList;
	}

	public void setCountList(List<Map<String, String>> countList) {
		this.countList = countList;
	}

	public List<Map<String, String>> getCountListByType() {
		return countListByType;
	}

	public void setCountListByType(List<Map<String, String>> countListByType) {
		this.countListByType = countListByType;
	}

	public void setSpecList(List<Map<String, String>> specList) {
		this.specList = specList;
	}
	
	
}
