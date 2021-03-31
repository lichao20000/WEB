package com.linkage.module.gwms.report.dao;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TerminalNoMatchReportDAO extends SuperDAO
{
	Logger logger = LoggerFactory.getLogger(TerminalNoMatchReportDAO.class);
	private Map<String, String> cityMap = null;
	private HashMap<String, String> vendorMap = null;
	private HashMap<String, String> deviceModelMap = null;
	private long registerTime = 1388505600l;
	private long reportTime = 1606752000l;
	private long bindTime = 1609430400l;
	
	public List<Map> queryCityId(String area_id)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append("select a.city_id from tab_city_area a ");
		sql.append("inner join tab_city b on a.city_id=b.city_id ");
		
		if (!StringUtil.IsEmpty(area_id) && !"1".equals(area_id)) {
			sql.append("where a.area_id="+area_id+" and b.parent_id='00' ");
		}else {
			sql.append("where a.area_id="+area_id+" ");
		}
		return jt.queryForList(sql.getSQL());
	}
	
	/**
	 * 已废弃，新疆已换为两个版本代替
	 * @param cityId
	 * @return
	 */
	public List<Map> getTyCount(String cityId)
	{
		PrepareSQL sql = new PrepareSQL();
		List list;
		if(DBUtil.GetDB()==Global.DB_MYSQL)
		{
			sql.setSQL("select ifnull(a.city_id, '-1') city_id, ");
			sql.append("sum(a.t0num) t0num, ");
			sql.append("sum(a.daynomatchnum) daynomatchnum, ");
			sql.append("sum(a.allnomatchnum) allnomatchnum, ");
			sql.append("sum(a.scrapdevnum) scrapdevnum, ");
			sql.append("sum(a.alladdnomatchnum) alladdnomatchnum, ");
			sql.append("sum(a.old_t0num) oldT0, ");
			sql.append("sum(a.old_onUse_num) oldNum, ");
			sql.append("sum(a.old_t0num - a.old_onUse_num) old_complete_num ");
			sql.append("from tab_xjdx_nomatchreport a ");
			
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
				sql.append("where a.city_id='"+cityId+"' ");
			}
			sql.append("group by a.city_id");
			
			list=jt.queryForList(sql.getSQL());
			if(list!=null && !list.isEmpty()){
				int t0num=0;
				int daynomatchnum=0;
				int allnomatchnum=0;
				int scrapdevnum=0;
				int alladdnomatchnum=0;
				int oldT0=0;
				int oldNum=0;
				int old_complete_num=0;
				for(int i=0;i<list.size();i++){
					Map m=(Map)list.get(i);
					
					String city_id=StringUtil.getStringValue(m, "city_id");
					
					t0num+=StringUtil.getIntValue(m, "t0num");
					daynomatchnum+=StringUtil.getIntValue(m, "daynomatchnum");
					allnomatchnum+=StringUtil.getIntValue(m, "allnomatchnum");
					scrapdevnum+=StringUtil.getIntValue(m, "scrapdevnum");
					alladdnomatchnum+=StringUtil.getIntValue(m, "alladdnomatchnum");
					oldT0+=StringUtil.getIntValue(m, "oldT0");
					oldNum+=StringUtil.getIntValue(m, "oldNum");
					old_complete_num+=StringUtil.getIntValue(m, "old_complete_num");
					
					if("-1".equals(city_id)){
						continue;
					}
					m.put("city_name", CityDAO.getCityName(city_id));
				}
				
				Map map=new HashMap();
				map.put("city_id","-1");
				map.put("city_name","合计");
				map.put("t0num",t0num);
				map.put("daynomatchnum",daynomatchnum);
				map.put("allnomatchnum",allnomatchnum);
				map.put("scrapdevnum",scrapdevnum);
				map.put("alladdnomatchnum",alladdnomatchnum);
				map.put("oldT0",oldT0);
				map.put("oldNum",oldNum);
				map.put("old_complete_num",old_complete_num);
				
				list.add(map);
			}
		}
		else
		{
			sql.setSQL("select b.*,nvl(c.city_name, '合计') city_name  from (select nvl(a.city_id, '-1') city_id, ");
			sql.append("sum(a.t0num) t0num, ");
			sql.append("sum(a.daynomatchnum) daynomatchnum, ");
			sql.append("sum(a.allnomatchnum) allnomatchnum, ");
			sql.append("sum(a.scrapdevnum) scrapdevnum, ");
			sql.append("sum(a.alladdnomatchnum) alladdnomatchnum, ");
			sql.append("sum(a.old_t0num) oldT0, ");
			sql.append("sum(a.old_onUse_num) oldNum ,");
			sql.append("sum(a.old_t0num - a.old_onUse_num) old_complete_num ");
			sql.append("from tab_xjdx_nomatchreport a ");
			sql.append("group by rollup(a.city_id)) b ");
			sql.append("left join tab_city c on b.city_id=c.city_id ");
			
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
				sql.append("where b.city_id='"+cityId+"' ");
			}
			list=jt.queryForList(sql.getSQL());
		}
		return list;
	}
	
	/**
	 * 终端不匹配报表
	 * @param cityId
	 * @return
	 */
    public List<Map> getDevMisMtch(String cityId)
    {
    	PrepareSQL sql = new PrepareSQL();
    	List list;
    	if(DBUtil.GetDB()==Global.DB_MYSQL)
    	{
    		sql.append("select ifnull(a.city_id, '-1') city_id, ");
    		sql.append("sum(a.t0num) t0num, ");
    		sql.append("sum(a.daynomatchnum) daynomatchnum, ");
    		sql.append("sum(a.allnomatchnum) allnomatchnum, ");
     		sql.append("sum(a.alladdnomatchnum) alladdnomatchnum ");
    		sql.append("from tab_xjdx_nomatchreport a ");
    		
    		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
    			sql.append("where a.city_id='"+cityId+"' ");
    		}
    		sql.append("group by a.city_id ");
    		
    		list=jt.queryForList(sql.getSQL());
    		if(list!=null && !list.isEmpty()){
				int t0num=0;
				int daynomatchnum=0;
				int allnomatchnum=0;
				for(int i=0;i<list.size();i++){
					Map m=(Map)list.get(i);
					
					String city_id=StringUtil.getStringValue(m, "city_id");
					
					t0num+=StringUtil.getIntValue(m, "t0num");
					daynomatchnum+=StringUtil.getIntValue(m, "daynomatchnum");
					allnomatchnum+=StringUtil.getIntValue(m, "allnomatchnum");
					
					if("-1".equals(city_id)){
						continue;
					}
					m.put("city_name", CityDAO.getCityName(city_id));
				}
				
				Map map=new HashMap();
				map.put("city_id","-1");
				map.put("city_name","合计");
				map.put("t0num",t0num);
				map.put("daynomatchnum",daynomatchnum);
				map.put("allnomatchnum",allnomatchnum);
				
				list.add(map);
			}
    	}
    	else
    	{
    		sql.append("select b.*,nvl(c.city_name, '合计') city_name  from (select nvl(a.city_id, '-1') city_id, ");
    		sql.append("sum(a.t0num) t0num, ");
    		sql.append("sum(a.daynomatchnum) daynomatchnum, ");
    		sql.append("sum(a.allnomatchnum) allnomatchnum, ");
     		sql.append("sum(a.alladdnomatchnum) alladdnomatchnum ");
    		sql.append("from tab_xjdx_nomatchreport a ");
    		sql.append("group by rollup(a.city_id)) b ");
    		sql.append("left join tab_city c on b.city_id=c.city_id ");
    		
    		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
    			sql.append("where b.city_id='"+cityId+"' ");
    		}
    		list=jt.queryForList(sql.getSQL());
    	}
		
		return list;
	
    }
    
    /**
	 * 老旧终端更换
	 * @param cityId
	 * @return
	 */
    public List<Map> getOldDev(String cityId)
    {
    	PrepareSQL sql = new PrepareSQL();
    	List list;
    	if(DBUtil.GetDB()==Global.DB_MYSQL)
    	{
    		sql.setSQL("select nvl(a.city_id, '-1') city_id, ");
    		sql.append("sum(a.sec_t0) sec_t0, ");
    		sql.append("sum(a.sec_nocplt_num) sec_nocplt_num, ");
    		sql.append("sum(a.thrd_t0) thrd_t0, ");
    		sql.append("sum(a.thrd_nocplt_num) thrd_nocplt_num, ");
    		sql.append("sum(a.scrapdevnum) scrapdevnum, ");
     		sql.append("sum(a.old_t0num) first_oldt0, ");
    		sql.append("sum(a.old_onUse_num) first_oldnum ,");
    		sql.append("sum(a.old_t0num - a.old_onUse_num) first_old_complete_num, ");
    		sql.append("sum(a.old_day_complt_num) first_day_complete_num ");
    		sql.append("from tab_xjdx_nomatchreport a ");
    		
    		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
    			sql.append("where b.city_id='"+cityId+"' ");
    		}
    		sql.append("group by a.city_id ");
    		
    		list=jt.queryForList(sql.getSQL());
    		if(list!=null && !list.isEmpty()){
				int sec_t0=0;
				int sec_nocplt_num=0;
				int thrd_t0=0;
				int scrapdevnum=0;
				int first_oldt0=0;
				int first_oldnum=0;
				int first_old_complete_num=0;
				int first_day_complete_num=0;
				for(int i=0;i<list.size();i++){
					Map m=(Map)list.get(i);
					
					String city_id=StringUtil.getStringValue(m, "city_id");
					
					sec_t0+=StringUtil.getIntValue(m, "sec_t0");
					sec_nocplt_num+=StringUtil.getIntValue(m, "sec_nocplt_num");
					thrd_t0+=StringUtil.getIntValue(m, "thrd_t0");
					scrapdevnum+=StringUtil.getIntValue(m, "scrapdevnum");
					first_oldt0+=StringUtil.getIntValue(m, "first_oldt0");
					first_oldnum+=StringUtil.getIntValue(m, "first_oldnum");
					first_old_complete_num+=StringUtil.getIntValue(m, "first_old_complete_num");
					first_day_complete_num+=StringUtil.getIntValue(m, "first_day_complete_num");
					
					if("-1".equals(city_id)){
						continue;
					}
					m.put("city_name", CityDAO.getCityName(city_id));
				}
				
				Map map=new HashMap();
				map.put("city_id","-1");
				map.put("city_name","合计");
				map.put("sec_t0",sec_t0);
				map.put("sec_nocplt_num",sec_nocplt_num);
				map.put("thrd_t0",thrd_t0);
				map.put("scrapdevnum",scrapdevnum);
				map.put("first_oldt0",first_oldt0);
				map.put("first_oldnum",first_oldnum);
				map.put("first_old_complete_num",first_old_complete_num);
				map.put("first_day_complete_num",first_day_complete_num);
				
				list.add(map);
			}
    	}
    	else
    	{
    		sql.setSQL("select b.*,nvl(c.city_name, '合计') city_name  from (select nvl(a.city_id, '-1') city_id, ");
    		sql.append("sum(a.sec_t0) sec_t0, ");
    		sql.append("sum(a.sec_nocplt_num) sec_nocplt_num, ");
    		sql.append("sum(a.thrd_t0) thrd_t0, ");
    		sql.append("sum(a.thrd_nocplt_num) thrd_nocplt_num, ");
    		sql.append("sum(a.scrapdevnum) scrapdevnum, ");
     		sql.append("sum(a.old_t0num) first_oldt0, ");
    		sql.append("sum(a.old_onUse_num) first_oldnum ,");
    		sql.append("sum(a.old_t0num - a.old_onUse_num) first_old_complete_num, ");
    		sql.append("sum(a.old_day_complt_num) first_day_complete_num ");
    		sql.append("from tab_xjdx_nomatchreport a ");
    		sql.append("group by rollup(a.city_id)) b ");
    		sql.append("left join tab_city c on b.city_id=c.city_id ");
    		
    		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
    			sql.append("where b.city_id='"+cityId+"' ");
    		}
    		list=jt.queryForList(sql.getSQL());
    	}
    	
    	return list;
    }
	
	public List<Map> queryDetailList(String cityId,int curPage_splitPage, int num_splitPage)
	{
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		StringBuffer sql = new StringBuffer();
		sql.append("select d.device_id,d.city_id,c.username loid,b.username,d.device_name,d.vendor_id,d.device_model_id,c.binddate from hgwcust_serv_info b  ");
		sql.append("inner join tab_hgwcustomer c on b.user_id=c.user_id and b.serv_type_id=10 ");
		sql.append("inner join tab_gw_device d on c.device_id=d.device_id ");
		sql.append("inner join tab_net_serv_param e on b.user_id=e.user_id and b.username=e.username  ");
		sql.append("inner join tab_device_version_attribute f on d.devicetype_id=f.devicetype_id ");
		sql.append("where d.gw_type=1  ");
		sql.append("and c.binddate>=1609430400  ");
		sql.append("and e.down_bandwidth is not null  ");
		sql.append("and e.serv_type_id=10 ");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("and cast(SUBSTRING(e.down_bandwidth,2,INSTR (e.down_bandwidth, 'M', 1, 1) -1) as signed)>=200 ");
		}else{
			sql.append("and to_number(SUBSTR(e.down_bandwidth,1,INSTR (e.down_bandwidth, 'M', 1, 1) -1))>=200 ");
		}
		sql.append("and f.device_version_type<2 ");
		if ("-1".equals(cityId)) {
			sql.append("and d.city_id <>'00' ");
		}else {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append("and d.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}
				else{
					map.put("city_name", "");
				}
				map.put("loid", rs.getString("loid"));
				map.put("username", rs.getString("username"));
				map.put("device_name", rs.getString("device_name"));
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)){
					map.put("vendor_add", vendor_add);
				}
				else{
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap
						.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model))
				{
					map.put("device_model", device_model);
				}
				else
				{
					map.put("device_model", "");
				}
				long binddate = StringUtil.getLongValue(rs.getString("binddate"))*1000L;
				DateTimeUtil dt = new DateTimeUtil(binddate);
				map.put("binddate", dt.getLongDate());
				return map;
			}
		});
		
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
	}
	
	public int queryCountDetailList(String cityId,int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select count(*) from hgwcust_serv_info b  ");
		}else{
			sql.append("select count(1) from hgwcust_serv_info b  ");
		}
		
		sql.append("inner join tab_hgwcustomer c on b.user_id=c.user_id and b.serv_type_id=10 ");
		sql.append("inner join tab_gw_device d on c.device_id=d.device_id ");
		sql.append("inner join tab_net_serv_param e on b.user_id=e.user_id and b.username=e.username  ");
		sql.append("inner join tab_device_version_attribute f on d.devicetype_id=f.devicetype_id ");
		sql.append("where d.gw_type=1  ");
		sql.append("and c.binddate>=1609430400  ");
		sql.append("and e.down_bandwidth is not null  ");
		sql.append("and e.serv_type_id=10 ");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("and cast(SUBSTRING(e.down_bandwidth,2,INSTR (e.down_bandwidth, 'M', 1, 1) -1) as signed)>=200 ");
		}else{
			sql.append("and to_number(SUBSTR(e.down_bandwidth,1,INSTR (e.down_bandwidth, 'M', 1, 1) -1))>=200 ");
		}
		
		sql.append("and f.device_version_type<2 ");
		if ("-1".equals(cityId)) {
			sql.append("and d.city_id <>'00' ");
		}else {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append("and d.city_id in (" + StringUtils.weave(citylist) + ") ");
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
	
	public List<Map> queryDetailListExcel(String cityId)
	{
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		StringBuffer sql = new StringBuffer();
		sql.append("select d.device_id,d.city_id,c.username loid,b.username,d.device_name,d.vendor_id,d.device_model_id,c.binddate from hgwcust_serv_info b  ");
		sql.append("inner join tab_hgwcustomer c on b.user_id=c.user_id and b.serv_type_id=10 ");
		sql.append("inner join tab_gw_device d on c.device_id=d.device_id ");
		sql.append("inner join tab_net_serv_param e on b.user_id=e.user_id and b.username=e.username  ");
		sql.append("inner join tab_device_version_attribute f on d.devicetype_id=f.devicetype_id ");
		sql.append("where d.gw_type=1  ");
		sql.append("and c.binddate>=1609430400  ");
		sql.append("and e.down_bandwidth is not null  ");
		sql.append("and e.serv_type_id=10 ");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("and cast(SUBSTRING(e.down_bandwidth,2,INSTR (e.down_bandwidth, 'M', 1, 1) -1) as signed)>=200 ");
		}else{
			sql.append("and to_number(SUBSTR(e.down_bandwidth,1,INSTR (e.down_bandwidth, 'M', 1, 1) -1))>=200 ");
		}
		
		sql.append("and f.device_version_type<2 ");
		if ("-1".equals(cityId)) {
			sql.append("and d.city_id <>'00' ");
		}else {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append("and d.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}
				else{
					map.put("city_name", "");
				}
				map.put("loid", rs.getString("loid"));
				map.put("username", rs.getString("username"));
				map.put("device_name", rs.getString("device_name"));
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)){
					map.put("vendor_add", vendor_add);
				}
				else{
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap
						.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model)){
					map.put("device_model", device_model);
				}else{
					map.put("device_model", "");
				}
				long binddate = StringUtil.getLongValue(rs.getString("binddate"))*1000L;
				DateTimeUtil dt = new DateTimeUtil(binddate);
				map.put("binddate", dt.getLongDate());
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
	}
	
	/**
	 * 终端不匹配详情
	 * @param cityId
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryNoMatchDetailList(String cityId,int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select a.city_id,a.pppoe_name,e.loid,e.vendor_id,e.device_model_id,");
			sql.append("e.device_id,e.gigabit_port,e.device_version_type ");
		}else{
			sql.append("select a.city_id,a.pppoe_name,e.* ");
		}
		
		sql.append("from tab_rate_mismatch a ");
		sql.append("left join (select c.username loid,d.vendor_id,d.device_model_id,d.device_id,f.gigabit_port,f.device_version_type,b.user_id,b.username from  hgwcust_serv_info b ");
		sql.append("inner join tab_hgwcustomer c on b.user_id = c.user_id ");
		sql.append("inner join tab_gw_device d  on c.device_id = d.device_id ");
		sql.append("left join tab_device_version_attribute f on d.devicetype_id = f.devicetype_id ");
		sql.append("where  d.gw_type=1 and b.serv_type_id = 10) e on a.pppoe_name = e.username ");
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append("where a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				String city_id = StringUtil.getStringValue(rs.getString("city_id"));
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}
				else{
					map.put("city_name", "");
				}
				map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
				map.put("pppoe_name", StringUtil.getStringValue(rs.getString("pppoe_name")));
				String vendor_id = StringUtil.getStringValue(rs.getString("vendor_id"));
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)){
					map.put("vendor_add", vendor_add);
				}
				else{
					map.put("vendor_add", "");
				}
				String device_model_id = StringUtil.getStringValue(rs.getString("device_model_id"));
				String device_model = StringUtil.getStringValue(deviceModelMap
						.get(device_model_id));
				if (!StringUtil.IsEmpty(device_model))
				{
					map.put("device_model", device_model);
				}
				else
				{
					map.put("device_model", "");
				}
				
				if(StringUtil.IsEmpty(StringUtil.getStringValue(rs.getString("device_id")))){
					map.put("remark", "已拆机");
				}else{
					map.put("remark", "");
				}
				
				String gigabit_port = StringUtil.getStringValue(rs.getString("gigabit_port"));
				if ("1".equals(gigabit_port)) {
					map.put("gigabit_port", "是");
				}else {
					map.put("gigabit_port", "否");
				}
				
				String device_version_type = StringUtil.getStringValue(rs.getString("device_version_type"));
				if ("2".equals(device_version_type) || "3".equals(device_version_type) || "5".equals(device_version_type)) {
					map.put("device_version_type", "是");
				}else {
					map.put("device_version_type", "否");
				}
				
				return map;
			}
		});
		
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
	}
	
	
	/**
	 * 终端不匹日完成量详情
	 * @param cityId
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> getNoMatchDayDetailList(String cityId,int curPage_splitPage, int num_splitPage)
	{
		Calendar calendar1 = Calendar.getInstance();
	    calendar1.set(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH)-1,23,59,59);
	    long dataEnd = calendar1.getTime().getTime()/1000;
	    calendar1 = Calendar.getInstance();
	    calendar1.set(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH)-1,0,0,0);
	    long starttime = calendar1.getTime().getTime()/1000;
	    
	    StringBuffer sb = new StringBuffer();
		sb.append("select a.city_id,a.pppoe_name, c.username loid,d.vendor_id,d.device_model_id,d.device_id,");
		sb.append("f.gigabit_port,f.device_version_type,c.username from  ");
		sb.append("tab_rate_mismatch a ");
		sb.append("inner join hgwcust_serv_info b on a.pppoe_name=b.username ");
		sb.append("inner join tab_hgwcustomer c on b.user_id=c.user_id and b.serv_type_id=10 ");
		sb.append("inner join tab_gw_device d on c.device_id=d.device_id  ");
		sb.append("inner join tab_device_version_attribute f on d.devicetype_id=f.devicetype_id ");
		sb.append("where ");
		sb.append("f.device_version_type>=2 ");
		sb.append("and d.gw_type=1 and c.binddate <"+dataEnd+" ");
		sb.append("and c.binddate > "+starttime+" ");
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append("and a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		sb.append("union all ");
		sb.append("select a.city_id,a.pppoe_name,a.loid, d.vendor_id,d.device_model_id,d.device_id,");
		sb.append("f.gigabit_port,f.device_version_type,c.username from ");
		sb.append("tab_rate_mismatch a ");
		sb.append("left join tab_hgwcustomer c on a.loid=c.username ");
		sb.append("left join tab_gw_device d on c.device_id=d.device_id  ");
		sb.append("left join tab_device_version_attribute f on d.devicetype_id=f.devicetype_id ");
		sb.append("where not exists( ");
		sb.append("select 1 from hgwcust_serv_info b ");
		sb.append("inner join tab_hgwcustomer c on b.user_id=c.user_id and b.serv_type_id=10 ");
		sb.append("inner join tab_gw_device d on c.device_id=d.device_id  ");
		sb.append("where a.pppoe_name=b.username  ");
		sb.append("and d.gw_type=1 and c.binddate <"+dataEnd+" ");
		sb.append("and c.binddate > "+starttime+" ");
		sb.append(") ");
		sb.append("and c.binddate <"+dataEnd+" ");
		sb.append("and c.binddate > "+starttime+" ");
		
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append("and a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		
		/*sb.append("union all ");
		sb.append("select a.city_id,a.pppoe_name,a.loid, d.vendor_id,d.device_model_id,d.device_id,");
		sb.append("f.gigabit_port,f.device_version_type,c.username from ");
		sb.append("tab_rate_mismatch a ");
		sb.append("left join tab_hgwcustomer c on a.loid=c.username ");
		sb.append("left join tab_gw_device d on c.device_id=d.device_id  ");
		sb.append("left join tab_device_version_attribute f on d.devicetype_id=f.devicetype_id ");
		sb.append("where exists( ");
		sb.append("select 1 from bind_log b ");
		sb.append("where a.loid=b.username  ");
		sb.append("and b.binddate <"+dataEnd+" ");
		sb.append("and b.binddate > "+starttime+" ");
		sb.append(") ");
		//sb.append("and d.gw_type=1 and c.binddate <"+dataEnd+" ");
		//sb.append("and c.binddate > "+starttime+" ");
		
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append("and a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}*/
		PrepareSQL psql = new PrepareSQL(sb.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				String city_id = StringUtil.getStringValue(rs.getString("city_id"));
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}
				else{
					map.put("city_name", "");
				}
				map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
				map.put("pppoe_name", StringUtil.getStringValue(rs.getString("pppoe_name")));
				String vendor_id = StringUtil.getStringValue(rs.getString("vendor_id"));
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)){
					map.put("vendor_add", vendor_add);
				}
				else{
					map.put("vendor_add", "");
				}
				String device_model_id = StringUtil.getStringValue(rs.getString("device_model_id"));
				String device_model = StringUtil.getStringValue(deviceModelMap
						.get(device_model_id));
				if (!StringUtil.IsEmpty(device_model))
				{
					map.put("device_model", device_model);
				}
				else
				{
					map.put("device_model", "");
				}
				
				
				if(StringUtil.IsEmpty(StringUtil.getStringValue(rs.getString("device_id"))) || 
						StringUtil.IsEmpty(StringUtil.getStringValue(rs.getString("username"))) ){
					map.put("remark", "已拆机");
				}else{
					map.put("remark", "");
				}
				
				String gigabit_port = StringUtil.getStringValue(rs.getString("gigabit_port"));
				if ("1".equals(gigabit_port)) {
					map.put("gigabit_port", "是");
				}else {
					map.put("gigabit_port", "否");
				}
				
				String device_version_type = StringUtil.getStringValue(rs.getString("device_version_type"));
				if ("2".equals(device_version_type) || "3".equals(device_version_type) || "5".equals(device_version_type)) {
					map.put("device_version_type", "是");
				}else {
					map.put("device_version_type", "否");
				}
				
				return map;
			}
		});
		
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
	}
	
	
	
	public int queryNoMatchCountDetailList(String cityId,int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select count(*) from tab_rate_mismatch a ");
		}else{
			sql.append("select count(1) from tab_rate_mismatch a ");
		}
		
		sql.append("left join (select c.username loid,d.vendor_id,d.device_model_id,f.gigabit_port,f.device_version_type,b.user_id,b.username from  hgwcust_serv_info b ");
		sql.append("inner join tab_hgwcustomer c on b.user_id = c.user_id ");
		sql.append("inner join tab_gw_device d  on c.device_id = d.device_id ");
		sql.append("left join tab_device_version_attribute f on d.devicetype_id = f.devicetype_id ");
		sql.append("where  d.gw_type=1 and b.serv_type_id = 10) e on a.pppoe_name = e.username ");
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append("where a.city_id in (" + StringUtils.weave(citylist) + ") ");
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
	
	public int queryNoMatchDayDetailCount(String cityId,int curPage_splitPage, int num_splitPage)
	{
		Calendar calendar1 = Calendar.getInstance();
	    calendar1.set(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH)-1,23,59,59);
	    long dataEnd = calendar1.getTime().getTime()/1000;
	    calendar1 = Calendar.getInstance();
	    calendar1.set(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH)-1,0,0,0);
	    long starttime = calendar1.getTime().getTime()/1000;
	    
	    StringBuffer sb = new StringBuffer();
		sb.append("select count(*) from  ");
		sb.append("tab_rate_mismatch a ");
		sb.append("inner join hgwcust_serv_info b on a.pppoe_name=b.username ");
		sb.append("inner join tab_hgwcustomer c on b.user_id=c.user_id and b.serv_type_id=10 ");
		sb.append("inner join tab_gw_device d on c.device_id=d.device_id  ");
		sb.append("inner join tab_device_version_attribute f on d.devicetype_id=f.devicetype_id ");
		sb.append("where ");
		sb.append("f.device_version_type>=2 ");
		sb.append("and d.gw_type=1 and c.binddate <"+dataEnd+" ");
		sb.append("and c.binddate > "+starttime+" ");
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append("and a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		PrepareSQL psql = new PrepareSQL(sb.toString());
		int total1 = jt.queryForInt(psql.getSQL());
		
		sb = new StringBuffer();
		sb.append("select count(*) from ");
		sb.append("tab_rate_mismatch a ");
		sb.append("left join tab_hgwcustomer c on a.loid=c.username ");
		sb.append("left join tab_gw_device d on c.device_id=d.device_id  ");
		sb.append("left join tab_device_version_attribute f on d.devicetype_id=f.devicetype_id ");
		sb.append("where not exists( ");
		sb.append("select 1 from hgwcust_serv_info b ");
		sb.append("inner join tab_hgwcustomer c on b.user_id=c.user_id and b.serv_type_id=10 ");
		sb.append("inner join tab_gw_device d on c.device_id=d.device_id  ");
		sb.append("where a.pppoe_name=b.username  ");
		sb.append("and d.gw_type=1 and c.binddate <"+dataEnd+" ");
		sb.append("and c.binddate > "+starttime+" ");
		sb.append(") ");
		sb.append("and c.binddate <"+dataEnd+" ");
		sb.append("and c.binddate > "+starttime+" ");
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append("and a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		
		psql = new PrepareSQL(sb.toString());
		int total2 = jt.queryForInt(psql.getSQL());
		
		/*sb = new StringBuffer();
		sb.append("select count(*) from ");
		sb.append("tab_rate_mismatch a ");
		sb.append("left join tab_hgwcustomer c on a.loid=c.username ");
		sb.append("left join tab_gw_device d on c.device_id=d.device_id  ");
		sb.append("left join tab_device_version_attribute f on d.devicetype_id=f.devicetype_id ");
		sb.append("where exists( ");
		sb.append("select 1 from bind_log b ");
		sb.append("where a.loid=b.username  ");
		sb.append("and b.binddate <"+dataEnd+" ");
		sb.append("and b.binddate > "+starttime+" ");
		sb.append(") ");
		//sb.append("and d.gw_type=1");// and c.binddate <"+dataEnd+" ");
		//sb.append("and c.binddate > "+starttime+" ");
		
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append("and a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		psql = new PrepareSQL(sb.toString());
		int total3 = jt.queryForInt(psql.getSQL());
		
		int total = total1 + total2 + total3;*/
		int total = total1 + total2;
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
	
	
	/**
	 * 终端不匹配导出
	 * @param cityId
	 * @return
	 */
	public List<Map> queryNoMatchDetailListExcel(String cityId)
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select a.city_id,a.pppoe_name,e.loid,e.vendor_id,");
			sql.append("e.device_model_id,e.device_id,e.gigabit_port,e.device_version_type ");
		}else{
			sql.append("select a.city_id,a.pppoe_name,e.* ");
		}
		
		sql.append("from tab_rate_mismatch a ");
		sql.append("left join (select c.username loid,d.vendor_id,d.device_model_id,d.device_id,f.gigabit_port,f.device_version_type,b.user_id,b.username from  hgwcust_serv_info b ");
		sql.append("inner join tab_hgwcustomer c on b.user_id = c.user_id ");
		sql.append("inner join tab_gw_device d  on c.device_id = d.device_id ");
		sql.append("left join tab_device_version_attribute f on d.devicetype_id = f.devicetype_id ");
		sql.append("where  d.gw_type=1 and b.serv_type_id = 10) e on a.pppoe_name = e.username  ");
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append("where a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				String city_id = StringUtil.getStringValue(rs.getString("city_id"));
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}
				else{
					map.put("city_name", "");
				}
				map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
				map.put("pppoe_name", StringUtil.getStringValue(rs.getString("pppoe_name")));
				String vendor_id = StringUtil.getStringValue(rs.getString("vendor_id"));
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)){
					map.put("vendor_add", vendor_add);
				}
				else{
					map.put("vendor_add", "");
				}
				String device_model_id = StringUtil.getStringValue(rs.getString("device_model_id"));
				String device_model = StringUtil.getStringValue(deviceModelMap
						.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model))
				{
					map.put("device_model", device_model);
				}
				else
				{
					map.put("device_model", "");
				}
				
				if(StringUtil.IsEmpty(StringUtil.getStringValue(rs.getString("device_id")))){
					map.put("remark", "已拆机");
				}else{
					map.put("remark", "");
				}
				
				String gigabit_port = StringUtil.getStringValue(rs.getString("gigabit_port"));
				if ("1".equals(gigabit_port)) {
					map.put("gigabit_port", "是");
				}else {
					map.put("gigabit_port", "否");
				}
				
				String device_version_type = StringUtil.getStringValue(rs.getString("device_version_type"));
				if ("2".equals(device_version_type) || "3".equals(device_version_type) || "5".equals(device_version_type)) {
					map.put("device_version_type", "是");
				}else {
					map.put("device_version_type", "否");
				}
				
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
	}
	
	/**
	 * 终端不匹配日完成量导出
	 * @param cityId
	 * @return
	 */
	public List<Map> getNoMatchDayCompltExcel(String cityId)
	{
		Calendar calendar1 = Calendar.getInstance();
	    calendar1.set(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH)-1,23,59,59);
	    long dataEnd = calendar1.getTime().getTime()/1000;
	    calendar1 = Calendar.getInstance();
	    calendar1.set(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH)-1,0,0,0);
	    long starttime = calendar1.getTime().getTime()/1000;
	    
	    StringBuffer sb = new StringBuffer();
	    sb.append("select a.city_id,a.pppoe_name, c.username loid,d.vendor_id,d.device_model_id,d.device_id,");
		sb.append("f.gigabit_port,f.device_version_type,c.username from  ");
		sb.append("tab_rate_mismatch a ");
		sb.append("inner join hgwcust_serv_info b on a.pppoe_name=b.username ");
		sb.append("inner join tab_hgwcustomer c on b.user_id=c.user_id and b.serv_type_id=10 ");
		sb.append("inner join tab_gw_device d on c.device_id=d.device_id  ");
		sb.append("inner join tab_device_version_attribute f on d.devicetype_id=f.devicetype_id ");
		sb.append("where ");
		sb.append("f.device_version_type>=2 ");
		sb.append("and d.gw_type=1 and c.binddate <"+dataEnd+" ");
		sb.append("and c.binddate > "+starttime+" ");
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append("and a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		sb.append("union all ");
		sb.append("select a.city_id,a.pppoe_name,a.loid, d.vendor_id,d.device_model_id,d.device_id,");
		sb.append("f.gigabit_port,f.device_version_type,c.username from ");
		sb.append("tab_rate_mismatch a ");
		sb.append("left join tab_hgwcustomer c on a.loid=c.username ");
		sb.append("left join tab_gw_device d on c.device_id=d.device_id  ");
		sb.append("left join tab_device_version_attribute f on d.devicetype_id=f.devicetype_id ");
		sb.append("where not exists( ");
		sb.append("select 1 from hgwcust_serv_info b ");
		sb.append("inner join tab_hgwcustomer c on b.user_id=c.user_id and b.serv_type_id=10 ");
		sb.append("inner join tab_gw_device d on c.device_id=d.device_id  ");
		sb.append("where a.pppoe_name=b.username  ");
		sb.append("and d.gw_type=1 and c.binddate <"+dataEnd+" ");
		sb.append("and c.binddate > "+starttime+" ");
		sb.append(") ");
		sb.append("and c.binddate <"+dataEnd+" ");
		sb.append("and c.binddate > "+starttime+" "); 
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append("and a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		
		/*sb.append("union all ");
		sb.append("select a.city_id,a.pppoe_name,a.loid, d.vendor_id,d.device_model_id,d.device_id,");
		sb.append("f.gigabit_port,f.device_version_type,c.username from ");
		sb.append("tab_rate_mismatch a ");
		sb.append("left join tab_hgwcustomer c on a.loid=c.username ");
		sb.append("left join tab_gw_device d on c.device_id=d.device_id  ");
		sb.append("left join tab_device_version_attribute f on d.devicetype_id=f.devicetype_id ");
		sb.append("where exists( ");
		sb.append("select 1 from bind_log b ");
		sb.append("where a.loid=b.username  ");
		sb.append("and b.binddate <"+dataEnd+" ");
		sb.append("and b.binddate > "+starttime+" ");
		sb.append(") ");
		//sb.append("and d.gw_type=1 ");//and c.binddate <"+dataEnd+" ");
		//sb.append("and c.binddate > "+starttime+" ");
		
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append("and a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}*/
		
		PrepareSQL psql = new PrepareSQL(sb.toString());
		
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				String city_id = StringUtil.getStringValue(rs.getString("city_id"));
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}
				else{
					map.put("city_name", "");
				}
				map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
				map.put("pppoe_name", StringUtil.getStringValue(rs.getString("pppoe_name")));
				String vendor_id = StringUtil.getStringValue(rs.getString("vendor_id"));
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)){
					map.put("vendor_add", vendor_add);
				}
				else{
					map.put("vendor_add", "");
				}
				String device_model_id = StringUtil.getStringValue(rs.getString("device_model_id"));
				String device_model = StringUtil.getStringValue(deviceModelMap
						.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model))
				{
					map.put("device_model", device_model);
				}
				else
				{
					map.put("device_model", "");
				}
				
				if(StringUtil.IsEmpty(StringUtil.getStringValue(rs.getString("device_id"))) || 
						StringUtil.IsEmpty(StringUtil.getStringValue(rs.getString("username")))){
					map.put("remark", "已拆机");
				}else{
					map.put("remark", "");
				}
				
				String gigabit_port = StringUtil.getStringValue(rs.getString("gigabit_port"));
				if ("1".equals(gigabit_port)) {
					map.put("gigabit_port", "是");
				}else {
					map.put("gigabit_port", "否");
				}
				
				String device_version_type = StringUtil.getStringValue(rs.getString("device_version_type"));
				if ("2".equals(device_version_type) || "3".equals(device_version_type) || "5".equals(device_version_type)) {
					map.put("device_version_type", "是");
				}else {
					map.put("device_version_type", "否");
				}
				
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
	}
	
	
	public List<Map> querySrcapDevDetailList(String cityId,int curPage_splitPage, int num_splitPage)
	{
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,c.username loid, d.username,b.vendor_id,b.device_model_id,c.binddate from tab_gw_device_scrap a ");
		sql.append("inner join tab_gw_device  b on a.device_id =b.device_id ");
		sql.append("inner join tab_hgwcustomer c on b.device_id=c.device_id ");
		sql.append("inner join hgwcust_serv_info d on d.user_id=c.user_id ");
		sql.append("where a.status=1 and d.serv_type_id=10 and b.gw_type=1 ");
		
		sql.append("and c.binddate >= " + bindTime + " ");
		
		if (!"-1".equals(cityId)) {
			if ("1010".equals(cityId)) {
				List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append("and b.city_id in (" + StringUtils.weave(citylist) +",'00'"+ ") ");
			}else {
				List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append("and b.city_id in (" + StringUtils.weave(citylist) + ") ");
			}
			
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}
				else{
					map.put("city_name", "");
				}
				map.put("loid", rs.getString("loid"));
				map.put("username", rs.getString("username"));
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)){
					map.put("vendor_add", vendor_add);
				}
				else{
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap
						.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model))
				{
					map.put("device_model", device_model);
				}
				else
				{
					map.put("device_model", "");
				}
				map.put("isold", "是");
				long binddate = StringUtil.getLongValue(rs.getString("binddate"))*1000L;
				DateTimeUtil dt = new DateTimeUtil(binddate);
				map.put("binddate", dt.getLongDate());
				
				return map;
			}
		});
		
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
	}
	
	
	public int querySrcapDevCountDetailList(String cityId,int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select count(*) from tab_gw_device_scrap a ");
		}else{
			sql.append("select count(1) from tab_gw_device_scrap a ");
		}
		
		sql.append("inner join tab_gw_device  b on a.device_id =b.device_id ");
		sql.append("inner join tab_hgwcustomer c on b.device_id=c.device_id ");
		sql.append("inner join hgwcust_serv_info d on d.user_id=c.user_id ");
		sql.append("where a.status=1 and d.serv_type_id=10 and b.gw_type=1 ");
		
		sql.append("and c.binddate >= " + bindTime + " ");

		if (!"-1".equals(cityId)) {
			if ("1010".equals(cityId)) {
				List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append("and b.city_id in (" + StringUtils.weave(citylist) +",'00'"+ ") ");
			}else {
				List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append("and b.city_id in (" + StringUtils.weave(citylist) + ") ");
			}
			
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
	
	public List<Map> querySrcapDetailListExcel(String cityId)
	{
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,c.username loid, d.username,b.vendor_id,b.device_model_id,c.binddate from tab_gw_device_scrap a ");
		sql.append("inner join tab_gw_device  b on a.device_id =b.device_id ");
		sql.append("inner join tab_hgwcustomer c on b.device_id=c.device_id ");
		sql.append("inner join hgwcust_serv_info d on d.user_id=c.user_id ");
		sql.append("where a.status=1 and d.serv_type_id=10 and b.gw_type=1 ");
		
		sql.append("and c.binddate >= " + bindTime + " ");

		if (!"-1".equals(cityId)) {
			if ("1010".equals(cityId)) {
				List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append("and b.city_id in (" + StringUtils.weave(citylist) +",'00'"+ ") ");
			}else {
				List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append("and b.city_id in (" + StringUtils.weave(citylist) + ") ");
			}
			
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}
				else{
					map.put("city_name", "");
				}
				map.put("loid", rs.getString("loid"));
				map.put("username", rs.getString("username"));
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)){
					map.put("vendor_add", vendor_add);
				}
				else{
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap
						.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model))
				{
					map.put("device_model", device_model);
				}
				else
				{
					map.put("device_model", "");
				}
				map.put("isold", "是");
				long binddate = StringUtil.getLongValue(rs.getString("binddate"))*1000L;
				DateTimeUtil dt = new DateTimeUtil(binddate);
				map.put("binddate", dt.getLongDate());
				
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
	}


	/**
	 * 根据地州获取存在的老旧终端使用数量
	 * @param cityId
	 * @return
	 */
	public int getOldUserDevCount(String cityId)
	{
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		
		sql.append("from tab_gw_device_scrap a, tab_hgwcustomer b,tab_gw_device c,gw_devicestatus d , gw_device_model g");
		sql.append(" where a.device_id = b.device_id and b.device_id=c.device_id and c.device_id=d.device_id ");
		sql.append(" and c.device_model_id=g.device_model_id and g.device_model <> 'PT921G' ");
        
		//第一批老旧终端更新T0值，T0值是2014年1月1日以前入网的终端，2020年12月1日以后上报过信息的老旧终端 未完成量更新start
		sql.append(" and  c.complete_time < " + registerTime + " ");
        sql.append(" and  d.last_time >= " + reportTime + " ");
        
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and  a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		return jt.queryForInt(sql.getSQL());
	}
	
	
	/**
	 * 完成量总量
	 * @param cityId
	 * @return
	 */
	public int getOldCompleteDevCount(String cityId)
	{
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append(" select count(*) ");
		}else{
			sql.append(" select count(1) ");
		}
		
		/*sql.setSQL(" select count(1) ");
		sql.append(" from tab_gw_device_scrap a ,tab_hgwcustomer b,hgwcust_serv_info c,tab_gw_device d,gw_devicestatus e");
		sql.append(" where a.loid=b.username and b.user_id=c.user_id and c.serv_type_id=10");
		sql.append(" and b.device_id=d.device_id and d.device_id=e.device_id");
		sql.append(" and not exists (select 1 from tab_hgwcustomer e where a.device_id=e.device_id)");*/
		
		sql.append(" from ");
		sql.append(" tab_gw_device_scrap a ,");
		sql.append(" tab_hgwcustomer b left join hgwcust_serv_info c on b.user_id=c.user_id and c.serv_type_id=10,");
		sql.append(" tab_gw_device d,");
		sql.append(" gw_devicestatus e, ");
		sql.append(" gw_device_model g ");
		sql.append(" where a.loid=b.username ");
		sql.append(" and a.device_id=d.device_id and d.device_id=e.device_id and d.device_model_id=g.device_model_id and g.device_model <> 'PT921G'");
		sql.append(" and not exists (select 1 from tab_hgwcustomer s where a.device_id=s.device_id)");
		
        //第一批老旧终端更新T0值，T0值是2014年1月1日以前入网的终端，2020年12月1日以后上报过信息的老旧终端 未完成量更新start
		sql.append(" and  d.complete_time < " + registerTime + " ");
        sql.append(" and  e.last_time >= " + reportTime + " ");
        
        
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and  a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}		
		return jt.queryForInt(sql.getSQL());
	}
	
	/**
	 * 第一批日完成量
	 * @param cityId
	 * @return
	 */
	public int getOldDayCompleteDevCount(String cityId)
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		long startTime = cal.getTimeInMillis() / 1000;
		
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND,59);
		long endTime = cal.getTimeInMillis() / 1000;
		
		PrepareSQL sql = new PrepareSQL();
		sql.append(" select count(*) ");
		sql.append(" from ");
		sql.append(" tab_gw_device_scrap a left join tab_hgwcustomer b on a.loid = b.username");
		sql.append(" left join hgwcust_serv_info c on b.user_id=c.user_id and c.serv_type_id=10,");
		sql.append(" tab_gw_device d,");
		sql.append(" gw_devicestatus e, ");
		sql.append(" gw_device_model g, ");
		sql.append(" bind_log t ");
		sql.append(" where ");
		sql.append(" a.device_id = d.device_id and d.device_id=e.device_id and d.device_model_id=g.device_model_id and g.device_model <> 'PT921G'");
		sql.append(" and a.device_id = t.device_id ");
		sql.append(" and t.binddate <= "+endTime);
		sql.append(" and t.binddate >= "+startTime);
		//第一批老旧终端更新T0值，T0值是2014年1月1日以前入网的终端，2020年12月1日以后上报过信息的老旧终端 未完成量更新
		sql.append(" and d.complete_time < "+registerTime);
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and  a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		 
		 return jt.queryForInt(sql.getSQL());
	}
	
	/**
	 * pt921g终端未完成数量t0统计数据
	 * @param cityId
	 * @return
	 */
	public int queryPt921gCount(String cityId)
	{
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append(" select count(*) ");
		}else{
			sql.append(" select count(1) ");
		}
		
		sql.append("from tab_gw_device_scrap a " +
				"left join tab_hgwcustomer b on a.loid=b.username " +
				"left join hgwcust_serv_info c on b.user_id=c.user_id and c.serv_type_id=10,");
		sql.append("tab_gw_device d ");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("left join tab_hgwcustomer tt on d.customer_id=cast(tt.user_id as char), ");
		}else{
			sql.append("left join tab_hgwcustomer tt on d.customer_id=to_char(tt.user_id), ");
		}
		
		sql.append("gw_devicestatus f,");
		sql.append("gw_device_model g ");
		sql.append("where a.device_id=d.device_id and d.device_id=f.device_id ");
		sql.append("and d.device_model_id=g.device_model_id and g.device_model = 'PT921G' ");
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and  a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}		
		return jt.queryForInt(sql.getSQL());
	}
	
	/**
	 * 第三批终端未完成量统计数量
	 * @param cityId
	 * @return
	 */
	public int queryThrdCount(String cityId)
	{
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append(" select count(*) ");
		}else{
			sql.append(" select count(1) ");
		}
		
		sql.append("from tab_gw_device d ");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("left join tab_hgwcustomer b on cast(b.user_id as char) = d.customer_id,");
		}else{
			sql.append("left join tab_hgwcustomer b on to_char(b.user_id) = d.customer_id,");
		}
		sql.append("gw_devicestatus f, ");
		sql.append("tab_device_model_scrap g, ");
		sql.append("gw_device_model k ");
		sql.append("where d.device_id=f.device_id and d.device_id=g.device_id and d.device_model_id=k.device_model_id ");
		sql.append("and g.status=1 ");
		
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and  d.city_id in (" + StringUtils.weave(citylist) + ") ");
		}		
		return jt.queryForInt(sql.getSQL());
	}

	/**
	 * 根据地州分页获取存在的老旧终端deviceId列表
	 * @param cityId
	 * @return
	 */
    public List<Map> getOldUseDevByCityId(String cityId,int curPage_splitPage, int num_splitPage)
    {
    /**	if(DBUtil.GetDB()==Global.DB_MYSQL){
    		//TODO wait
    	}else{
    		
    	} */
		PrepareSQL sql = new PrepareSQL();
		sql.append("select a.device_id,a.loid,a.username,c.vendor_id,c.device_model_id,b.city_id,d.last_time " +
				"from tab_gw_device_scrap a, tab_hgwcustomer b,tab_gw_device c,gw_devicestatus d,gw_device_model g  ");
		sql.append(" where a.device_id = b.device_id and b.device_id=c.device_id and c.device_id=d.device_id ");
		sql.append(" and c.device_model_id=g.device_model_id and g.device_model <> 'PT921G' ");
		
		//第一批老旧终端更新T0值，T0值是2014年1月1日以前入网的终端，2020年12月1日以后上报过信息的老旧终端 未完成量更新start
		sql.append(" and  c.complete_time < " + registerTime + " ");
        sql.append(" and  d.last_time >= " + reportTime + " ");
        
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and  a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		List<Map> list = querySP(sql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("cityName", city_name);
				}
				else{
					map.put("cityName", "");
				}
				map.put("loid", rs.getString("loid"));
				map.put("username", rs.getString("username"));
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)){
					map.put("vendorName", vendor_add);
				}
				else{
					map.put("vendorName", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model))
				{
					map.put("deviceModelName", device_model);
				}
				else
				{
					map.put("deviceModelName", "");
				}
				long binddate = StringUtil.getLongValue(rs.getString("last_time"))*1000L;
				DateTimeUtil dt = new DateTimeUtil(binddate);
				map.put("lastOnlineTime", dt.getLongDate());
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
    }
    
	public List<Map> queryOldDevDetailExcel(String cityId)
	{
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		 PrepareSQL sql = new PrepareSQL();
		sql.append("select a.device_id,a.loid,a.username,c.vendor_id,c.device_model_id,b.city_id,d.last_time  " +
				"from tab_gw_device_scrap a, tab_hgwcustomer b,tab_gw_device c,gw_devicestatus d ,gw_device_model g ");
		sql.append(" where a.device_id = b.device_id and b.device_id=c.device_id and c.device_id=d.device_id ");
		sql.append(" and c.device_model_id=g.device_model_id and g.device_model <> 'PT921G' ");
		
		//第一批老旧终端更新T0值，T0值是2014年1月1日以前入网的终端，2020年12月1日以后上报过信息的老旧终端 未完成量更新start
		sql.append(" and  c.complete_time < " + registerTime + " ");
        sql.append(" and  d.last_time >= " + reportTime + " ");
		        
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and  a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		List<Map> list = jt.query(sql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("cityName", city_name);
				}
				else{
					map.put("cityName", "");
				}
				map.put("loid", rs.getString("loid"));
				map.put("username", rs.getString("username"));
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)){
					map.put("vendorName", vendor_add);
				}
				else{
					map.put("vendorName", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model))
				{
					map.put("deviceModelName", device_model);
				}
				else
				{
					map.put("deviceModelName", "");
				}
				long binddate = StringUtil.getLongValue(rs.getString("last_time"))*1000L;
				DateTimeUtil dt = new DateTimeUtil(binddate);
				map.put("lastOnlineTime", dt.getLongDate());
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
	}
	
	
	public List<Map> queryOldCompleteDevDetailExcel(String cityId)
	{
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
    	PrepareSQL sql = new PrepareSQL();
		/*sql.setSQL(" select b.city_id,b.device_id,b.username loid,c.username,d.vendor_id,d.device_model_id,b.city_id,e.last_time ");
		sql.append(" from tab_gw_device_scrap a ,tab_hgwcustomer b,hgwcust_serv_info c,tab_gw_device d,gw_devicestatus e");
		sql.append(" where a.loid=b.username and b.user_id=c.user_id and c.serv_type_id=10");
		sql.append(" and b.device_id=d.device_id and d.device_id=e.device_id");
		sql.append(" and not exists (select 1 from tab_hgwcustomer e where a.device_id=e.device_id)");*/
    	sql.append(" select b.city_id,b.device_id,b.username loid,c.username,d.vendor_id,d.device_model_id,b.city_id,e.last_time");
		sql.append(" from ");
		sql.append(" tab_gw_device_scrap a ,");
		sql.append(" tab_hgwcustomer b left join hgwcust_serv_info c on b.user_id=c.user_id and c.serv_type_id=10,");
		sql.append(" tab_gw_device d,");
		sql.append(" gw_devicestatus e, ");
		sql.append(" gw_device_model g ");
		sql.append(" where a.loid=b.username ");
		sql.append(" and a.device_id=d.device_id and d.device_id=e.device_id and d.device_model_id=g.device_model_id and g.device_model <> 'PT921G'");
		sql.append(" and not exists (select 1 from tab_hgwcustomer s where a.device_id=s.device_id)");
		
        //第一批老旧终端更新T0值，T0值是2014年1月1日以前入网的终端，2020年12月1日以后上报过信息的老旧终端 未完成量更新start
		sql.append(" and  d.complete_time < " + registerTime + " ");
        sql.append(" and  e.last_time >= " + reportTime + " ");
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and  a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}		
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		List<Map> list = jt.query(sql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("cityName", city_name);
				}
				else{
					map.put("cityName", "");
				}
				map.put("loid", rs.getString("loid"));
				map.put("username", rs.getString("username"));
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)){
					map.put("vendorName", vendor_add);
				}
				else{
					map.put("vendorName", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model))
				{
					map.put("deviceModelName", device_model);
				}
				else
				{
					map.put("deviceModelName", "");
				}
				long binddate = StringUtil.getLongValue(rs.getString("last_time"))*1000L;
				DateTimeUtil dt = new DateTimeUtil(binddate);
				map.put("lastOnlineTime", dt.getLongDate());
				
				String deviceId = StringUtil.getStringValue(rs.getString("device_id"));
				if(StringUtil.IsEmpty(deviceId)){
					map.put("remark", "已拆机");
				}else{
					map.put("remark", "");
				}
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
    
	}
    
   	public List<Map> queryOldDayCompleteDevDetailExcel(String cityId)
   	{
    	Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		long startTime = cal.getTimeInMillis() / 1000;
		
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND,59);
		long endTime = cal.getTimeInMillis() / 1000;
		
		PrepareSQL sql = new PrepareSQL();
		sql.append(" select a.city_id city_id_orign,a.device_id orign_dev,a.loid orign_loid,d.vendor_id vendor_id_orign,d.device_model_id device_model_id_orign,e.last_time last_time_orign,");
		sql.append(" tt.city_id,b.device_id,b.username loid,c.username, tt.vendor_id,tt.device_model_id,bb.last_time ");
		sql.append(" from ");
		sql.append(" tab_gw_device_scrap a left join tab_hgwcustomer b on a.loid = b.username");
		sql.append(" left join hgwcust_serv_info c on b.user_id=c.user_id and c.serv_type_id=10");
		sql.append(" left join tab_gw_device tt on b.device_id=tt.device_id");
		sql.append(" left join gw_devicestatus bb on tt.device_id=bb.device_id,");
		sql.append(" tab_gw_device d,");
		sql.append(" gw_devicestatus e, ");
		sql.append(" gw_device_model g, ");
		sql.append(" bind_log t ");
		sql.append(" where ");
		sql.append(" a.device_id = d.device_id and d.device_id=e.device_id and d.device_model_id=g.device_model_id and g.device_model <> 'PT921G'");
		sql.append(" and a.device_id = t.device_id ");
		sql.append(" and t.binddate <= "+endTime);
		sql.append(" and t.binddate >= "+startTime);
		
		//第一批老旧终端更新T0值，T0值是2014年1月1日以前入网的终端，2020年12月1日以后上报过信息的老旧终端 未完成量更新
		sql.append(" and d.complete_time < "+registerTime);
		
   		if (!"-1".equals(cityId)) {
   			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
   			sql.append(" and  a.city_id in (" + StringUtils.weave(citylist) + ")  ");
   		}
   		cityMap = CityDAO.getCityIdCityNameMap();
   		vendorMap = VendorModelVersionDAO.getVendorMap();
   		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
   		List<Map> list = jt.query(sql.getSQL(), new RowMapper()
   		{
   			public Object mapRow(ResultSet rs, int arg1) throws SQLException
   			{
   				Map<String, String> map = new HashMap<String, String>();
   				
   				String kdusername = StringUtil.getStringValue(rs.getString("username"));
				//当前tab_hgwcustomer中的loid
				String loid = StringUtil.getStringValue(rs.getString("loid"));
				//报废loid
				String orign_loid = StringUtil.getStringValue(rs.getString("orign_loid"));
				//报废属地
				String cityId = StringUtil.getStringValue(rs.getString("city_id_orign"));
				//报废型号
				String device_model_id = StringUtil.getStringValue(rs.getString("device_model_id_orign"));
				//报废最新上线实际
				long last_time = StringUtil.getLongValue(rs.getString("last_time_orign"));
				//报废厂家
				String vendor_id = StringUtil.getStringValue(rs.getString("vendor_id_orign"));
				//可能更换设备id
				String devId = StringUtil.getStringValue(rs.getString("device_id"));
				//报废设备id
				String orign_devId = StringUtil.getStringValue(rs.getString("orign_dev"));
				//备注
				String remark = "" ;
				//1、查看报废表中的LOID是否已拆机，即不存在
				if(StringUtil.IsEmpty(loid)){
					remark = "已拆机";
				}else{
					//2、查看是否已更换机顶盒
					if(!StringUtil.IsEmpty(devId) && !devId.equals(orign_devId)){
						cityId = StringUtil.getStringValue(rs.getString("city_id"));
						device_model_id = StringUtil.getStringValue(rs.getString("device_model_id"));
						vendor_id = StringUtil.getStringValue(rs.getString("vendor_id"));
						last_time = StringUtil.getLongValue(rs.getString("last_time"));
					}else{
						//3、如果宽带账号不存在，则备注拆机
						if(StringUtil.IsEmpty(kdusername)){
							remark = "已拆机";
						}
					}
				}
				
				//4、已拆机的值显示老的LOID和属地
				if(!StringUtil.IsEmpty(remark)){
					device_model_id = "";
					vendor_id = "";
					last_time = -1;
				}
				map.put("loid", orign_loid);
				map.put("city_id", cityId);
				
				String city_name = StringUtil.getStringValue(cityMap.get(cityId));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("cityName", city_name);
				}
				else{
					map.put("cityName", "");
				}
				map.put("username", kdusername);
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)){
					map.put("vendorName", vendor_add);
				}
				else{
					map.put("vendorName", "");
				}
				String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model))
				{
					map.put("deviceModelName", device_model);
				}
				else
				{
					map.put("deviceModelName", "");
				}
				if(last_time != -1){
					long binddate = last_time * 1000L;
					DateTimeUtil dt = new DateTimeUtil(binddate);
					map.put("lastOnlineTime", dt.getLongDate()); 
				}else{
					map.put("lastOnlineTime", ""); 
				}
				map.put("remark", remark);
   				return map;
   			}
   		});
   		cityMap = null;
   		vendorMap = null;
   		deviceModelMap = null;
   		return list;
   	}
    
    public List<Map> getOldCompleteDevByCityId(String cityId, int curPage_splitPage, int num_splitPage)
    {
    /**	if(DBUtil.GetDB()==Global.DB_MYSQL){
    		//TODO wait
    	}else{
    		
    	} */
		PrepareSQL sql = new PrepareSQL();
		/*sql.append(" select b.city_id,b.device_id,b.username loid,c.username,d.vendor_id,d.device_model_id,b.city_id,e.last_time");
		sql.append(" from tab_gw_device_scrap a ,tab_hgwcustomer b,hgwcust_serv_info c,tab_gw_device d,gw_devicestatus e");
		sql.append(" where a.loid=b.username and b.user_id=c.user_id and c.serv_type_id=10");
		sql.append(" and b.device_id=d.device_id and d.device_id=e.device_id");
		sql.append(" and not exists (select 1 from tab_hgwcustomer e where a.device_id=e.device_id)");*/
		
		sql.append(" select b.city_id,b.device_id,b.username loid,c.username,d.vendor_id,d.device_model_id,b.city_id,e.last_time");
		sql.append(" from ");
		sql.append(" tab_gw_device_scrap a ,");
		sql.append(" tab_hgwcustomer b left join hgwcust_serv_info c on b.user_id=c.user_id and c.serv_type_id=10,");
		sql.append(" tab_gw_device d,");
		sql.append(" gw_devicestatus e, ");
		sql.append(" gw_device_model g ");
		sql.append(" where a.loid=b.username ");
		sql.append(" and a.device_id=d.device_id and d.device_id=e.device_id and d.device_model_id=g.device_model_id and g.device_model <> 'PT921G'");
		sql.append(" and not exists (select 1 from tab_hgwcustomer s where a.device_id=s.device_id)");
		
		//第一批老旧终端更新T0值，T0值是2014年1月1日以前入网的终端，2020年12月1日以后上报过信息的老旧终端 未完成量更新start
		sql.append(" and  d.complete_time < " + registerTime + " ");
        sql.append(" and  e.last_time >= " + reportTime + " ");
        
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and  a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}		
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		List<Map> list = querySP(sql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("cityName", city_name);
				}
				else{
					map.put("cityName", "");
				}
				map.put("loid", rs.getString("loid"));
				map.put("username", rs.getString("username"));
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)){
					map.put("vendorName", vendor_add);
				}
				else{
					map.put("vendorName", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model))
				{
					map.put("deviceModelName", device_model);
				}
				else
				{
					map.put("deviceModelName", "");
				}
				long binddate = StringUtil.getLongValue(rs.getString("last_time"))*1000L;
				DateTimeUtil dt = new DateTimeUtil(binddate);
				map.put("lastOnlineTime", dt.getLongDate());
				
				String deviceId = StringUtil.getStringValue(rs.getString("device_id"));
				if(StringUtil.IsEmpty(deviceId)){
					map.put("remark", "已拆机");
				}else{
					map.put("remark", "");
				}
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
    }
    
    /**
     * 第一批日完成量统计
     * @param cityId
     * @param curPage_splitPage
     * @param num_splitPage
     * @return
     */
    public List<Map> getOldDayCompleteDevByCityId(String cityId, int curPage_splitPage, int num_splitPage)
    {
    	Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		long startTime = cal.getTimeInMillis() / 1000;
		
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND,59);
		long endTime = cal.getTimeInMillis() / 1000;
		
		PrepareSQL sql = new PrepareSQL();
		//sql.append(" select a.city_id,b.device_id,a.loid,c.username,d.vendor_id,d.device_model_id,e.last_time ");
		sql.append(" select a.city_id city_id_orign,a.device_id orign_dev,a.loid orign_loid,d.vendor_id vendor_id_orign,d.device_model_id device_model_id_orign,e.last_time last_time_orign,");
		sql.append(" tt.city_id,b.device_id,b.username loid,c.username, tt.vendor_id,tt.device_model_id,bb.last_time ");
		sql.append(" from ");
		sql.append(" tab_gw_device_scrap a left join tab_hgwcustomer b on a.loid = b.username");
		sql.append(" left join hgwcust_serv_info c on b.user_id=c.user_id and c.serv_type_id=10");
		sql.append(" left join tab_gw_device tt on b.device_id=tt.device_id");
		sql.append(" left join gw_devicestatus bb on tt.device_id=bb.device_id,");
		sql.append(" tab_gw_device d,");
		sql.append(" gw_devicestatus e, ");
		sql.append(" gw_device_model g, ");
		sql.append(" bind_log t ");
		sql.append(" where ");
		sql.append(" a.device_id = d.device_id and d.device_id=e.device_id and d.device_model_id=g.device_model_id and g.device_model <> 'PT921G'");
		sql.append(" and a.device_id = t.device_id ");
		sql.append(" and t.binddate <= "+endTime);
		sql.append(" and t.binddate >= "+startTime);
		
		//第一批老旧终端更新T0值，T0值是2014年1月1日以前入网的终端，2020年12月1日以后上报过信息的老旧终端 未完成量更新
		sql.append(" and d.complete_time < "+registerTime);
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and  a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}

		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		List<Map> list = querySP(sql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				String kdusername = StringUtil.getStringValue(rs.getString("username"));
				//当前tab_hgwcustomer中的loid
				String loid = StringUtil.getStringValue(rs.getString("loid"));
				//报废loid
				String orign_loid = StringUtil.getStringValue(rs.getString("orign_loid"));
				//报废属地
				String cityId = StringUtil.getStringValue(rs.getString("city_id_orign"));
				//报废型号
				String device_model_id = StringUtil.getStringValue(rs.getString("device_model_id_orign"));
				//报废最新上线实际
				long last_time = StringUtil.getLongValue(rs.getString("last_time_orign"));
				//报废厂家
				String vendor_id = StringUtil.getStringValue(rs.getString("vendor_id_orign"));
				//可能更换设备id
				String devId = StringUtil.getStringValue(rs.getString("device_id"));
				//报废设备id
				String orign_devId = StringUtil.getStringValue(rs.getString("orign_dev"));
				//备注
				String remark = "" ;
				//1、查看报废表中的LOID是否已拆机，即不存在
				if(StringUtil.IsEmpty(loid)){
					remark = "已拆机";
				}else{
					//2、查看是否已更换机顶盒
					if(!StringUtil.IsEmpty(devId) && !devId.equals(orign_devId)){
						cityId = StringUtil.getStringValue(rs.getString("city_id"));
						device_model_id = StringUtil.getStringValue(rs.getString("device_model_id"));
						vendor_id = StringUtil.getStringValue(rs.getString("vendor_id"));
						last_time = StringUtil.getLongValue(rs.getString("last_time"));
					}else{
						//3、如果宽带账号不存在，则备注拆机
						if(StringUtil.IsEmpty(kdusername)){
							remark = "已拆机";
						}
					}
				}
				
				//4、已拆机的值显示老的LOID和属地
				if(!StringUtil.IsEmpty(remark)){
					device_model_id = "";
					vendor_id = "";
					last_time = -1;
				}
				map.put("loid", orign_loid);
				map.put("city_id", cityId);
				
				String city_name = StringUtil.getStringValue(cityMap.get(cityId));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("cityName", city_name);
				}
				else{
					map.put("cityName", "");
				}
				map.put("username", kdusername);
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)){
					map.put("vendorName", vendor_add);
				}
				else{
					map.put("vendorName", "");
				}
				
				if(!StringUtil.IsEmpty(device_model_id)){
					String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
					if (!StringUtil.IsEmpty(device_model))
					{
						map.put("deviceModelName", device_model);
					}else
					{
						map.put("deviceModelName", "");
					}
				}else{
					map.put("deviceModelName", "");
				}
				
				if(last_time != -1){
					long binddate = last_time * 1000L;
					DateTimeUtil dt = new DateTimeUtil(binddate);
					map.put("lastOnlineTime", dt.getLongDate()); 
				}else{
					map.put("lastOnlineTime", ""); 
				}
				map.put("remark", remark);
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
    }
    
    /**
	 * pt921g未完成量统计
	 * @param cityId
	 * @return
	 */
    public List<Map> queryPt921gByCityId(String cityId,int curPage_splitPage, int num_splitPage)
    {
    /**	if(DBUtil.GetDB()==Global.DB_MYSQL){
    		//TODO wait
    	}else{
    		
    	} */
		PrepareSQL sql = new PrepareSQL();
		/*sql.append("select ");
		sql.append("b.device_id,b.username loid,c.username,d.vendor_id,g.device_model,b.city_id,f.last_time ");
		sql.append("from tab_gw_device_scrap a,");
		sql.append("tab_hgwcustomer b left join hgwcust_serv_info c on b.user_id=c.user_id and c.serv_type_id=10, ");
		sql.append("tab_gw_device d,");
		sql.append("gw_devicestatus f,");
		sql.append("gw_device_model g ");
		sql.append("where a.loid=b.username and a.device_id=d.device_id and d.device_id=f.device_id ");
		sql.append("and d.device_model_id=g.device_model_id and g.device_model = 'PT921G' ");*/
		sql.append("select ");
		sql.append("tt.device_id,a.loid,c.username,d.vendor_id,g.device_model,d.city_id,f.last_time ");
		sql.append("from tab_gw_device_scrap a " +
				"left join tab_hgwcustomer b on a.loid=b.username " +
				"left join hgwcust_serv_info c on b.user_id=c.user_id and c.serv_type_id=10,");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("tab_gw_device d left join tab_hgwcustomer tt on d.customer_id=cast(tt.user_id as char), ");
    	}else{
    		sql.append("tab_gw_device d left join tab_hgwcustomer tt on d.customer_id=to_char(tt.user_id), ");
    	}
		
		sql.append("gw_devicestatus f,gw_device_model g ");
		sql.append("where a.device_id=d.device_id and d.device_id=f.device_id ");
		sql.append("and d.device_model_id=g.device_model_id and g.device_model = 'PT921G' ");
		
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and  a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		List<Map> list = querySP(sql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("cityName", city_name);
				}
				else{
					map.put("cityName", "");
				}
				
				String loid = StringUtil.getStringValue(rs.getString("loid"));
				String username = StringUtil.getStringValue(rs.getString("username"));
				map.put("loid", loid);
				map.put("username", username);
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (!StringUtil.IsEmpty(vendor_add)){
					map.put("vendorName", vendor_add);
				}
				else{
					map.put("vendorName", "");
				}
				
				String device_model = StringUtil.getStringValue(rs.getString("device_model"));
				map.put("deviceModelName", device_model);
				long binddate = StringUtil.getLongValue(rs.getString("last_time"))*1000L;
				DateTimeUtil dt = new DateTimeUtil(binddate);
				map.put("lastOnlineTime", dt.getLongDate());
				
				String deviceId = StringUtil.getStringValue(rs.getObject("device_id"));
				if(StringUtil.IsEmpty(deviceId)){
					map.put("remark","已拆机");
				}else{
					map.put("remark","");
				}
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
    }
	
	/**
	 * 第三批终端未完成量统计
	 * @param cityId
	 * @return
	 */
    public List<Map> queryThrdByCityId(String cityId,int curPage_splitPage, int num_splitPage)
    {
    /**	if(DBUtil.GetDB()==Global.DB_MYSQL){
    		//TODO wait
    	}else{
    		
    	} */
		PrepareSQL sql = new PrepareSQL();
		/*sql.append("select ");
		sql.append("b.device_id,b.username loid,c.username,d.vendor_id,g.device_model,d.city_id,f.last_time ");
		sql.append("from ");
		sql.append("tab_gw_device d left join tab_hgwcustomer b on to_char(b.user_id) = d.customer_id ");
		sql.append("left join hgwcust_serv_info c on b.user_id=c.user_id and c.serv_type_id=10,");
		sql.append("gw_devicestatus f, ");
		sql.append("tab_device_model_scrap g ");
		sql.append("where d.device_id=f.device_id and d.device_model_id=g.device_model_id");*/
		sql.append("select ");
		sql.append("b.device_id,g.loid,g.username,d.vendor_id,k.device_model,d.city_id,f.last_time ");
		sql.append("from tab_gw_device d ");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("left join tab_hgwcustomer b on cast(b.user_id as char) = d.customer_id,");
    	}else{
    		sql.append("left join tab_hgwcustomer b on to_char(b.user_id) = d.customer_id,");
    	}
		sql.append("gw_devicestatus f,tab_device_model_scrap g,gw_device_model k ");
		sql.append("where d.device_id=f.device_id and d.device_id=g.device_id and d.device_model_id=k.device_model_id ");
		sql.append("and g.status=1 ");
		
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and  d.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		List<Map> list = querySP(sql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("cityName", city_name);
				}
				else{
					map.put("cityName", "");
				}
				
				String loid = StringUtil.getStringValue(rs.getString("loid"));
				String username = StringUtil.getStringValue(rs.getString("username"));
				map.put("loid", loid);
				map.put("username",username);
				
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (!StringUtil.IsEmpty(vendor_add)){
					map.put("vendorName", vendor_add);
				}
				else{
					map.put("vendorName", "");
				}
				String device_model = StringUtil.getStringValue(rs.getString("device_model"));
				map.put("deviceModelName", device_model);
				
				long binddate = StringUtil.getLongValue(rs.getString("last_time"))*1000L;
				DateTimeUtil dt = new DateTimeUtil(binddate);
				map.put("lastOnlineTime", dt.getLongDate());
				
				String deviceId = StringUtil.getStringValue(rs.getString("device_id"));
				if(StringUtil.IsEmpty(deviceId)){
					map.put("remark", "已拆机");
				}else{
					map.put("remark", "");
				}
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		return list;
	}
	
	/**
	 * pt921g详情导出
	 * @param cityId
	 * @return
	 */
    public List<Map> getPt921gDetailExcel(String cityId)
    {
	 /**  	if(DBUtil.GetDB()==Global.DB_MYSQL){
	   		//TODO wait
	   	}else{
	   		
	   	} */
	   PrepareSQL sql = new PrepareSQL();
	   sql.append("select ");
		sql.append("tt.device_id,a.loid,c.username,d.vendor_id,g.device_model,d.city_id,f.last_time ");
		sql.append("from tab_gw_device_scrap a " +
				"left join tab_hgwcustomer b on a.loid=b.username " +
				"left join hgwcust_serv_info c on b.user_id=c.user_id and c.serv_type_id=10,");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("tab_gw_device d left join tab_hgwcustomer tt on d.customer_id=cast(tt.user_id as char), ");
	   	}else{
	   		sql.append("tab_gw_device d left join tab_hgwcustomer tt on d.customer_id=to_char(tt.user_id), ");
	   	}
		
		sql.append("gw_devicestatus f,gw_device_model g ");
		sql.append("where a.device_id=d.device_id and d.device_id=f.device_id ");
		sql.append("and d.device_model_id=g.device_model_id and g.device_model = 'PT921G' ");
		
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and  a.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		List<Map> list = jt.query(sql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("cityName", city_name);
				}
				else{
					map.put("cityName", "");
				}
				
				String loid = StringUtil.getStringValue(rs.getString("loid"));
				String username = StringUtil.getStringValue(rs.getString("username"));
				map.put("loid", loid);
				map.put("username", username);
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (!StringUtil.IsEmpty(vendor_add)){
					map.put("vendorName", vendor_add);
				}
				else{
					map.put("vendorName", "");
				}
				
				String device_model = StringUtil.getStringValue(rs.getString("device_model"));
				map.put("deviceModelName", device_model);
				long binddate = StringUtil.getLongValue(rs.getString("last_time"))*1000L;
				DateTimeUtil dt = new DateTimeUtil(binddate);
				map.put("lastOnlineTime", dt.getLongDate());
				
				String deviceId = StringUtil.getStringValue(rs.getString("device_id"));
				if(StringUtil.IsEmpty(deviceId)){
					map.put("remark","已拆机");
				}else{
					map.put("remark","");
				}
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
    }
   
   /**
	 * 第三批终端未完成量统计
	 * @param cityId
	 * @return
	 */
   public List<Map> getThrdDetailExcel(String cityId)
   {
	 /**  if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		PrepareSQL sql = new PrepareSQL();
		sql.append("select ");
		sql.append("b.device_id,g.loid,g.username,d.vendor_id,k.device_model,d.city_id,f.last_time ");
		sql.append("from tab_gw_device d ");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("left join tab_hgwcustomer b on cast(b.user_id as char) = d.customer_id,");
		}else{
			sql.append("left join tab_hgwcustomer b on to_char(b.user_id) = d.customer_id,");
		}
		sql.append("gw_devicestatus f,tab_device_model_scrap g,gw_device_model k ");
		sql.append("where d.device_id=f.device_id and d.device_id=g.device_id and d.device_model_id=k.device_model_id ");
		sql.append("and g.status=1 ");
		
		if (!"-1".equals(cityId)) {
			List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and  d.city_id in (" + StringUtils.weave(citylist) + ") ");
		}
		
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		List<Map> list = jt.query(sql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("cityName", city_name);
				}
				else{
					map.put("cityName", "");
				}
				
				String loid = StringUtil.getStringValue(rs.getString("loid"));
				String username = StringUtil.getStringValue(rs.getString("username"));
				map.put("loid", loid);
				map.put("username",username);
				
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (!StringUtil.IsEmpty(vendor_add)){
					map.put("vendorName", vendor_add);
				}
				else{
					map.put("vendorName", "");
				}
				String device_model = StringUtil.getStringValue(rs.getString("device_model"));
				map.put("deviceModelName", device_model);
				
				long binddate = StringUtil.getLongValue(rs.getString("last_time"))*1000L;
				DateTimeUtil dt = new DateTimeUtil(binddate);
				map.put("lastOnlineTime", dt.getLongDate());
				
				String deviceId = StringUtil.getStringValue(rs.getString("device_id"));
				if(StringUtil.IsEmpty(deviceId)){
					map.put("remark", "已拆机");
				}else{
					map.put("remark", "");
				}
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		return list;
   }
}
