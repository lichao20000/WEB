
package com.linkage.module.gwms.resource.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class BatchConfigNodeDAO extends SuperDAO
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(BatchConfigNodeDAO.class);
	UserRes currentUser = WebUtil.getCurrentUser();
	public void insertTask(long task_id,long accoid,int status)
	{
		logger.warn("insertTask start");
		String sql = "insert into gw_device_gather_task(task_id,add_time,account_id,status) values(?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setLong(1, task_id);
		psql.setLong(2, task_id);
		psql.setLong(3, accoid);
		psql.setLong(4, Long.valueOf(status));
		
		jt.execute(psql.getSQL());
	}
	
	public void insertDevice(long taskId,long add_time,String[] deviceId_array)
	{
		List<String> tempList = new ArrayList<String>();
		for (int i= 0;i<deviceId_array.length;i++)
		{
			PrepareSQL taskSql = new PrepareSQL("insert into gw_device_gather_batch (task_id, device_id, add_time) values(?,?,?)");
	    	
	    	taskSql.setLong(1, taskId);
	    	taskSql.setString(2, deviceId_array[i]);
	    	taskSql.setLong(3, add_time);
			tempList.add(taskSql.getSQL());
		}
		
		int[] ier = doBatch(tempList);
		if (ier != null && ier.length > 0) {
			logger.debug("批量入库成功");
		} else {
			logger.debug("批量入库失败");
		}
	}
	
	public void insertNode(long taskId,String[] nodeIds)
	{
		List<String> tempList = new ArrayList<String>();
		for (int i= 0;i<nodeIds.length;i++)
		{
			PrepareSQL taskSql = new PrepareSQL("insert into gw_device_gather_node (task_id, node) values(?,?)");
	    	
	    	taskSql.setLong(1, taskId);
	    	taskSql.setString(2, nodeIds[i]);
			tempList.add(taskSql.getSQL());
		}
		
		int[] ier = doBatch(tempList);
		if (ier != null && ier.length > 0) {
			logger.debug("批量入库成功");
		} else {
			logger.debug("批量入库失败");
		}
	}

    public List queryTaskList4XJ() 
    {
		List<Map> list = new ArrayList<Map>();
		PrepareSQL psql = new PrepareSQL();
		
		//TODO wait
		if(DBUtil.GetDB()==3)
		{
			psql.append("select c.task_id,c.city_id,c.gather_status,c.add_time,d.devSum ");
			psql.append("from tab_batch_gather_task c ");
			psql.append("left join (select count(1) devSum,city_id " +
									"from tab_gw_device t,tab_city t1 " +
									"where t.city_id=t1.city_id and t1.city_id<>'-1' group by city_id) d ");
			psql.append("on c.city_id = d.city_id ");
			logger.warn(currentUser.getCityId());
			if(!"00".equals(currentUser.getCityId())){
				psql.append(" where c.city_id = "+currentUser.getCityId().substring(0,4));
			}
			psql.append(" order by task_id ");
		}
		else
		{
			psql.append("select c.task_id,c.city_id,c.gather_status,c.add_time,d.devSum,e.city_name ");
			psql.append("from tab_batch_gather_task c ");
			psql.append("left join (select count(1) devSum, city_id ");
			psql.append("                 from (select a.device_id, a.city_id ");//地市
			psql.append("                         from (select t.*, t1.parent_id from tab_gw_device t left join tab_city t1 on t.city_id = t1.city_id) a ");
			psql.append("                        where a.city_id in ");
			psql.append("                              (select city_id from tab_city where parent_id = '00') ");
			psql.append("                       union all ");
			psql.append("                       select b.device_id, b.parent_id as city_id ");//区县
			psql.append("                         from (select t.*, t1.parent_id from tab_gw_device t left join tab_city t1 on t.city_id = t1.city_id) b ");
			psql.append("                        where b.parent_id in ");
			psql.append("                              (select city_id from tab_city where parent_id = '00')) ");
			psql.append("                group by city_id) d ");
			psql.append("      on c.city_id = d.city_id ");
			psql.append("    left join tab_city e ");
			psql.append("      on c.city_id = e.city_id ");
			logger.warn(currentUser.getCityId());
			if(!"00".equals(currentUser.getCityId())){
				psql.append(" where c.city_id = "+currentUser.getCityId().substring(0,4));
			}
			psql.append("   order by task_id ");
		}
		
		list = jt.queryForList(psql.getSQL());
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = list.get(i);
			long start_time = StringUtil.getLongValue(map.get("add_time"));
			if (start_time != 0) {
				map.put("add_time", new DateTimeUtil(start_time * 1000).getLongDate());
			} else {
				map.put("add_time", "");
			}
			int status = StringUtil.getIntegerValue(map.get("gather_status"));
			if (status==1) {
				map.put("result_desc", "已激活");
			} else {
				map.put("result_desc", "已停止");
			}
			
			if(DBUtil.GetDB()==3){
				map.put("city_name", CityDAO.getCityName(map.get("city_id")));
			}
		}

		return list;
    }

	public String changeTask(String task_id,int state) {
		DateTimeUtil dateUtil = new DateTimeUtil();
		long time = dateUtil.getLongTime();
		try {
			String sql = "update tab_batch_gather_task set gather_status = "+state+" ,add_time = "+time+" where task_id = "+task_id;
			PrepareSQL pSQL = new PrepareSQL(sql);
			return jt.update(pSQL.getSQL())+"";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public List getBatchDevList(String cityId, String startOpenDate, String endOpenDate) 
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append("select t.city_id,t2.area_name from tab_city t ");
		sql.append("left join tab_city_area t1  on t.city_id = t1.city_id  ");
		sql.append("left join tab_area t2 on t1.area_id = t2.area_id ");
		sql.append("where t.city_id='00' or t.parent_id='00' ");
		List<Map<String,String>> cityAreaList =jt.queryForList(sql.getSQL());
		Map<String,String> cityAreaMap = new HashMap<String, String>();
		for (int i = 0; i < cityAreaList.size() ; i++) {
			cityAreaMap.put(StringUtil.getStringValue(cityAreaList.get(i),"city_id"),StringUtil.getStringValue(cityAreaList.get(i),"area_name"));
		}
		PrepareSQL pSQL = new PrepareSQL();
		
		StringBuffer psql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			psql.append("select q.sum,q.area_id,q.area_name,");
		}else{
			psql.append("select /*+FIRST_ROWS*/ q.*,");
		}
		
		psql.append("q1.sum1, ROUND((q1.sum1/q.sum)*100,2) as devPer , q2.sum2, q3.sum3, q4.sum4, q5.city ");
		psql.append("from (select count(1) as sum, t.area_id, t1.area_name ");
		psql.append("		from tab_gw_device a ");
		psql.append("       left join tab_city_area t on t.city_id = a.city_id ");
		psql.append("       left join tab_area t1 on t.area_id = t1.area_id, tab_hgwcustomer b ");
		psql.append("       where a.device_id = b.device_id and b.user_state = '1' ");
		psql.append("       and t.area_id is not null and t.area_id !=1 ");
		psql.append("       group by t.area_id, t1.area_name ");
		psql.append("       order by t.area_id) q ");
		psql.append("left join (select count(1) as sum1, area_id ");
		psql.append("            from (select distinct device_id, area_id,gather_time,is_use_gbport from tab_gather_lan_info) ");
		psql.append("            where 1=1 ");
		if(!StringUtil.IsEmpty(startOpenDate)) {
			psql.append("        and gather_time >= "+new DateTimeUtil(startOpenDate).getLongTime());
		}
		if(!StringUtil.IsEmpty(endOpenDate)) {
			psql.append("        and gather_time <= "+new DateTimeUtil(endOpenDate).getLongTime());
		}
		psql.append("            group by area_id ) q1  on q.area_id = q1.area_id");
		psql.append("left join (select count(1) as sum2, area_id ");
		psql.append("            from tab_gather_wlan_info ");
		psql.append("            where total_associations != 0 ");
		if(!StringUtil.IsEmpty(startOpenDate)) {
			psql.append("        and gather_time >= "+new DateTimeUtil(startOpenDate).getLongTime());
		}
		if(!StringUtil.IsEmpty(endOpenDate)) {
			psql.append("        and gather_time <= "+new DateTimeUtil(endOpenDate).getLongTime());
		}
		psql.append("            group by area_id) q2  on q.area_id = q2.area_id ");
		psql.append("left join (select count(1) as sum3, area_id ");
		psql.append("            from (select distinct device_id, area_id,gather_time,is_use_gbport from tab_gather_lan_info)  ");
		psql.append("            where is_use_gbPort = 1 ");
		if(!StringUtil.IsEmpty(startOpenDate)) {
			psql.append("        and gather_time >= "+new DateTimeUtil(startOpenDate).getLongTime());
		}
		if(!StringUtil.IsEmpty(endOpenDate)) {
			psql.append("        and gather_time <= "+new DateTimeUtil(endOpenDate).getLongTime());
		}
		psql.append("            group by area_id) q3  on q.area_id = q3.area_id ");
		psql.append("left join (select count(1) as sum4, e.area_id ");
		psql.append("            from (select distinct device_id, area_id,gather_time,is_use_gbport from tab_gather_lan_info) e, tab_gather_wlan_info e1 ");
		psql.append("            where e1.device_id = e.device_id ");
		psql.append("            and e1.total_associations != 0 ");
		psql.append("            and e.is_use_gbport = 1 ");
		if(!StringUtil.IsEmpty(startOpenDate)) {
			psql.append("        and e.gather_time >= "+new DateTimeUtil(startOpenDate).getLongTime());
			psql.append("        and e1.gather_time >= "+new DateTimeUtil(startOpenDate).getLongTime());
		}
		if(!StringUtil.IsEmpty(endOpenDate)) {
			psql.append("        and e.gather_time <= "+new DateTimeUtil(endOpenDate).getLongTime());
			psql.append("        and e1.gather_time <= "+new DateTimeUtil(endOpenDate).getLongTime());
		}
		psql.append("            group by e.area_id) q4  on q.area_id = q4.area_id");
		psql.append("left join tmp_city_area_remark q5 on q5.area_id = q.area_id ");
		if (!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId) && !"00".equals(cityId)) {
				psql.append("where area_name='" + cityAreaMap.get(cityId) + "' ");
		}

		pSQL.setSQL(psql.toString());
		List<Map> list = jt.queryForList(pSQL.getSQL());
		Map totalMap = new HashMap();
		if(list.size()>0){
			int sum =0;
			int sum1 =0;
			int sum2 =0;
			int sum3 =0;
			int sum4 =0;
			for (Map map:list) {
				sum+= StringUtil.getIntegerValue(map.get("sum"),0);
				sum1+= StringUtil.getIntegerValue(map.get("sum1"),0);
				sum2+= StringUtil.getIntegerValue(map.get("sum2"),0);
				sum3+= StringUtil.getIntegerValue(map.get("sum3"),0);
				sum4+= StringUtil.getIntegerValue(map.get("sum4"),0);
			}
			totalMap.put("city","合计");
			totalMap.put("sum",sum);
			totalMap.put("sum1",sum1);
			totalMap.put("sum2",sum2);
			totalMap.put("sum3",sum3);
			totalMap.put("sum4",sum4);
			
			double perc=0;
			if(sum!=0){
				perc=(float)sum1/(float)sum*100.00;
			}
			totalMap.put("devper",String.format("%.2f",perc));
		}
		list.add(totalMap);
		return list;
	}

	public List getBatchWifiDevList(String cityId, String startOpenDate, String endOpenDate) 
	{
		PrepareSQL pSQL = new PrepareSQL();
		StringBuffer psql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			psql.append("select q.sum,q.area_id,q.area_name,");
		}else{
			psql.append("select /*+FIRST_ROWS*/ q.*,");
		}
		
		psql.append(" q1.sum1, ROUND((q1.sum1 / q.sum) * 100, 2) as devPer, q5.city ");
		psql.append("    from (select count(1) as sum, t.area_id, t1.area_name ");
		psql.append("            from tab_gw_device a ");
		psql.append("            left join tab_city_area t on t.city_id = a.city_id ");
		psql.append("            left join tab_area t1 on t.area_id = t1.area_id ");
		psql.append("            left join tab_device_version_attribute t2 on a.devicetype_id = t2.devicetype_id, tab_hgwcustomer b ");
		psql.append("           where a.device_id = b.device_id ");
		psql.append("             and b.user_state = '1' and t.area_id is not null ");
		psql.append("             and t.area_id !=1 and t2.wifi = 1 ");
		psql.append("           group by t.area_id, t1.area_name ");
		psql.append("           order by t.area_id) q ");
		psql.append("    left join (select count(1) as sum1, area_id ");
		psql.append("                 from tab_gather_wlan_info ");
		psql.append("                where total_associations != 0 ");
		if(!StringUtil.IsEmpty(startOpenDate)) {
			psql.append("                and gather_time >= "+new DateTimeUtil(startOpenDate).getLongTime());
		}
		if(!StringUtil.IsEmpty(endOpenDate)) {
			psql.append("                and gather_time <= "+new DateTimeUtil(endOpenDate).getLongTime());
		}
		psql.append("                group by area_id) q1 ");
		psql.append("      on q.area_id = q1.area_id ");
		psql.append("    left join tmp_city_area_remark q5 ");
		psql.append("      on q5.area_id = q.area_id ");

			if(!StringUtil.IsEmpty(cityId)&&!"-1".equals(cityId)&&!"00".equals(cityId)) {
				psql.append("     where q.area_id='"+Global.G_CityId_AreaId_Map.get(cityId)+"' ");
			}

		pSQL.setSQL(psql.toString());
		List<Map> list = jt.queryForList(pSQL.getSQL());
		Map totalMap = new HashMap(4);
		if(list.size()>0){
			int sum =0;
			int sum1 =0;

			for (Map map:list) {
				sum+= StringUtil.getIntegerValue(map.get("sum"),0);
				sum1+= StringUtil.getIntegerValue(map.get("sum1"),0);
			}
			totalMap.put("city","合计");
			totalMap.put("sum",sum);
			totalMap.put("sum1",sum1);
			double perc=0;
			if(sum!=0){
				perc=(float)sum1/(float)sum*100.00;
			}
			totalMap.put("devper",String.format("%.2f", perc));
		}
		list.add(totalMap);
		return list;
	}

	public List<Map<String, String>> downloadDevInfo(String cityId, String startOpenDate, String endOpenDate) {
		PrepareSQL pSQL = new PrepareSQL();
		StringBuffer psql = new StringBuffer();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		psql.append("select distinct t.city_id,t2.username loid,t2.username serusername,");
		psql.append("a.city_name,b.vendor_name,c.device_model ");
		psql.append("from tab_gather_wlan_info T, tab_gw_device T1 ");
		psql.append("left join tab_city a on t1.city_id = a.city_id ");
		psql.append("left join tab_vendor b on t1.vendor_id = b.VENDOR_ID ");
		psql.append("left join gw_device_model c on t1.device_model_id=c.device_model_id,tab_hgwcustomer T2 ");
		psql.append("left join hgwcust_serv_info T3 on (T2.user_id = T3.user_id and T3.serv_type_id = 10) ");
		psql.append("where t.device_id = t1.device_id ");
		psql.append("and t2.device_id = t1.device_id ");
		psql.append("and total_associations != 0 ");
		if(!"".equals(cityId)) {
			if(Global.G_CityId_AreaId_Map.get(cityId)!=null){
				cityId = Global.G_CityId_AreaId_Map.get(cityId);
			}
			psql.append("     and t.area_id =" +cityId);
		}

		psql.append(" and t.area_id >= 2 and t.area_id<=17 ");
		if(!StringUtil.IsEmpty(startOpenDate)) {
			psql.append("                and gather_time >= "+new DateTimeUtil(startOpenDate).getLongTime());
		}
		if(!StringUtil.IsEmpty(endOpenDate)) {
			psql.append("                and gather_time <= "+new DateTimeUtil(endOpenDate).getLongTime());
		}
		pSQL.setSQL(psql.toString());
		return jt.queryForList(pSQL.getSQL());
	}

	public Map queryCityIdByAreaId(String area_id) {
		PrepareSQL sql = new PrepareSQL();
		sql.setSQL("select a.city_id,b.city_name from tab_city_area a ");
		sql.append("inner join tab_city b on a.city_id=b.city_id ");

		if (!StringUtil.IsEmpty(area_id) && !"1".equals(area_id)) {
			sql.append("where a.area_id="+area_id+" and b.parent_id='00' ");
		}
		else
		{
			sql.append("where a.area_id="+area_id+" ");
		}
		return jt.queryForMap(sql.toString());
	}
}
