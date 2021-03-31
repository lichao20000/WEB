package dao.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class PerformanceReportDAO
{
	private static Logger log = LoggerFactory.getLogger(PerformanceReportDAO.class);
	
	// jdbc模板
	private JdbcTemplate jt;
	//
	private Map<String, String> devNameMap = new HashMap<String, String>();
	//表名
	private String table = "";
	//开始时间
	private long start = 0;
	//结束时间
	private long end = 0;
	/**
	 * 初始化数据库连接
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}
	/**
	 * 查询属地列表
	 * @param city_id
	 * @return
	 */
	public List getCityList(String city_id)
	{
		String sql = "select * from tab_city where city_id='" + city_id + "' or parent_id='" + city_id + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select city_id, parent_id, city_name from tab_city where city_id='" + city_id + "' or parent_id='" + city_id + "'";
		}
		
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	/**
	 * 查询属地下的所有的设备
	 * @param city_id
	 * @return
	 */
	public String getDevByCity(String city_id)
	{
		String html = "";
		//获取所有下级属地
		List clist = CityDAO.getAllNextCityIdsByCityPid(city_id);
		String cityStr = StringUtils.weave(clist);
		clist = null;
		//查询设备
		String sql = "select * from tab_gw_device where city_id in (" + cityStr + ")";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select device_id, device_name from tab_gw_device where city_id in (" + cityStr + ")";
		}

		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List list = jt.queryForList(sql);
		if (list != null && list.size() > 0){
			Map map = null;
			for (int i=0;i<list.size();i++){
				map = (Map)list.get(i);
				html += "<input type='checkbox' name='devList' value='" 
					+ String.valueOf(map.get("device_id")) + "'>" + String.valueOf(map.get("device_name"))
					+ "</br>";
			}
		}
		return html;
	}
	/**
	 * 统计性能报表
	 * @param day
	 * @param reportType
	 * @param devList
	 * @return
	 */
	public String getPerReportData(String day, String[] reportType, String[] devList, String type)
	{
		String html = "";
		//获取设备名称
		getDevNameMap();
		//按类别统计报表
		if (reportType != null && reportType.length > 0){
			for (int i=0;i<reportType.length;i++){
				if ("cpu".equals(reportType[i])){
					html += getCPUReport(day, devList, type);
				}
				else if ("mem".equals(reportType[i])){
					html += getMemReport(day, devList, type);
				}
				else if ("wanPort".equals(reportType[i])){
					html += getWANReport(day, devList, type);
				}
			}
		}
		
		return html;
	}
	/**
	 * 统计cpu性能
	 * @param day
	 * @param devList
	 * @param type
	 * @return
	 */
	private String getCPUReport(String day, String[] devList, String type)
	{
		String html = "<table border=0 cellspacing=1 cellpadding=2 width='100%' align='center' bgcolor='#000000'>";
		html += "<tr bgcolor='#FFFFFF'><th width='20%'>设备名称</th><th width='20%'>采集时间</th><th width='20%'>CPU使用最大值(%)</th><th width='20%'>CPU使用最小值(%)</th><th width='20%'>平均值(%)</th></tr>";
		String sql = "";
		//根据时间生成表名和时间范围
		getTableInfo(day, type);
		//生成sql
		sql = "select a.device_id,c.* from pm_map_instance a,pm_expression b," + table + " c " 
			+ "where a.device_id in ('" + StringUtils.weave(devList,"','") + "') and a.expressionid=b.expressionid and b.class1=1 "
			+ "and c.id=a.id and c.gathertime>" + start + " and c.gathertime<" + end;

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select a.device_id,c.gathertime,c.maxvalue,c.minvalue,c.avgvalue from pm_map_instance a,pm_expression b," + table + " c "
					+ "where a.device_id in ('" + StringUtils.weave(devList,"','") + "') and a.expressionid=b.expressionid and b.class1=1 "
					+ "and c.id=a.id and c.gathertime>" + start + " and c.gathertime<" + end;
		}

		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List list = jt.queryForList(sql);
		//显示数据
		int len = list.size();
		if (list != null && len > 0){
			Map map = null;
			for (int i=0;i<len;i++){
				map = (Map)list.get(i);
				if (map != null){
					html += "<tr bgcolor='#FFFFFF'>";
					html += "<td>" + devNameMap.get(String.valueOf(map.get("device_id"))) + "</td>";
					html += "<td>" + new DateTimeUtil(Long.valueOf(String.valueOf(map.get("gathertime")))*1000).getLongDate() + "</td>";
					html += "<td>" + String.valueOf(map.get("maxvalue")) + "</td>";
					html += "<td>" + String.valueOf(map.get("minvalue")) + "</td>";
					html += "<td>" + String.valueOf(map.get("avgvalue")) + "</td>";
					html += "</tr>";
				}
			}
		}
		else{
			html += "<tr bgcolor='#FFFFFF'><td colspan='5'>没有设备CPU性能数据</td></tr>";
		}
		html += "</table><br>";
		return html;
	}
	/**
	 * 统计内存性能
	 * @param day
	 * @param devList
	 * @param type
	 * @return
	 */
	private String getMemReport(String day, String[] devList, String type)
	{
		String html = "<table border=0 cellspacing=1 cellpadding=2 width='100%' align='center' bgcolor='#000000'>";
		html += "<tr bgcolor='#FFFFFF'><th width='20%'>设备名称</th><th width='20%'>采集时间</th><th width='20%'>内存使用最大值(MB)</th><th width='20%'>内存使用最小值(MB)</th><th width='20%'>平均值(MB)</th></tr>";
		String sql = "";
		//根据时间生成表名和时间范围
		getTableInfo(day, type);
		//生成sql
		sql = "select a.device_id,c.* from pm_map_instance a,pm_expression b," + table + " c " 
			+ "where a.device_id in ('" + StringUtils.weave(devList,"','") + "') and a.expressionid=b.expressionid and b.class1=2 "
			+ "and c.id=a.id and c.gathertime>" + start + " and c.gathertime<" + end;

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select a.device_id,c.gathertime,c.maxvalue,c.minvalue,c.avgvalue from pm_map_instance a,pm_expression b," + table + " c "
					+ "where a.device_id in ('" + StringUtils.weave(devList,"','") + "') and a.expressionid=b.expressionid and b.class1=2 "
					+ "and c.id=a.id and c.gathertime>" + start + " and c.gathertime<" + end;
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List list = jt.queryForList(sql);
		//显示数据
		int len = list.size();
		if (list != null && len > 0){
			Map map = null;
			for (int i=0;i<len;i++){
				map = (Map)list.get(i);
				if (map != null){
					html += "<tr bgcolor='#FFFFFF'>";
					html += "<td>" + devNameMap.get(String.valueOf(map.get("device_id"))) + "</td>";
					html += "<td>" + new DateTimeUtil(Long.valueOf(String.valueOf(map.get("gathertime")))*1000).getLongDate() + "</td>";
					html += "<td>" + String.valueOf(map.get("maxvalue")) + "</td>";
					html += "<td>" + String.valueOf(map.get("minvalue")) + "</td>";
					html += "<td>" + String.valueOf(map.get("avgvalue")) + "</td>";
					html += "</tr>";
				}
			}
		}
		else{
			html += "<tr bgcolor='#FFFFFF'><td colspan='5'>没有设备内存性能数据</td></tr>";
		}
		html += "</table><br>";
		return html;
	}
	/**
	 * 统计wan端口流量性能
	 * @param day
	 * @param devList
	 * @param type
	 * @return
	 */
	private String getWANReport(String day, String[] devList, String type)
	{
		String html = "<table border=0 cellspacing=1 cellpadding=2 width='100%' align='center' bgcolor='#000000'>";
		html += "<tr bgcolor='#FFFFFF'><th width='20%'>设备名称/端口名称</th><th width='20%'>采集时间</th><th width='15%'>流入字节数</th><th width='15%'>流出字节数</th><th width='15%'>流入速率</th><th width='15%'>流出速率</th></tr>";
		String sql = "";
		//根据时间生成表名和时间范围
		DateTimeUtil dt = new DateTimeUtil(day);
		//月
		if ("2".equals(type)){
			table = "flux_month_stat_" + dt.getYear();
			end = dt.getLongTime();
			start = end - 30*24*60*60;
		}
		//周
		else if ("1".equals(type)){
			table = "flux_week_stat_" + dt.getYear();
			end = dt.getLongTime();
			start = end - 7*24*60*60;
		}
		//日
		else{
			table = "flux_day_stat_" + dt.getYear() + "_" + dt.getMonth();
			end = dt.getLongTime();
			start = end - 24*60*60;
		}

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql += "select b.port_info,d.device_id,d.collecttime,d.ifinoctets,d.ifoutoctets,d.ifinoctetsbps,d.ifoutoctetsbps " +
					" from tab_port_name_map a,flux_interfacedeviceport b,tab_gw_device c," + table + " d "
					+ "where a.port_desc like '%WAN%' and a.port_name=b.ifdescr and a.device_model_id=c.device_model_id "
					+ "and b.device_id=c.device_id  and c.device_id in ('" + StringUtils.weave(devList,"','") + "') and d.device_id=c.device_id "
					+ "and d.port_info=b.port_info and d.getway=b.getway and d.collecttime>" + start + " and d.collecttime<" + end;
		}
		else {
			sql += "select b.port_info,d.* from tab_port_name_map a,flux_interfacedeviceport b,tab_gw_device c," + table + " d "
					+ "where a.port_desc like '%WAN%' and a.port_name=b.ifdescr and a.device_model_id=c.device_model_id "
					+ "and b.device_id=c.device_id  and c.device_id in ('" + StringUtils.weave(devList,"','") + "') and d.device_id=c.device_id "
					+ "and d.port_info=b.port_info and d.getway=b.getway and d.collecttime>" + start + " and d.collecttime<" + end;
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List list = jt.queryForList(sql);
		//显示数据
		int len = list.size();
		if (list != null && len > 0){
			Map map = null;
			for (int i=0;i<len;i++){
				map = (Map)list.get(i);
				if (map != null){
					html += "<tr bgcolor='#FFFFFF'>";
					html += "<td>" + devNameMap.get(String.valueOf(map.get("device_id"))) + "/" + String.valueOf(map.get("port_info")) + "</td>";
					html += "<td>" + new DateTimeUtil(Long.valueOf(String.valueOf(map.get("collecttime")))*1000).getLongDate() + "</td>";
					html += "<td>" + String.valueOf(map.get("ifinoctets")) + "</td>";
					html += "<td>" + String.valueOf(map.get("ifoutoctets")) + "</td>";
					html += "<td>" + String.valueOf(map.get("ifinoctetsbps")) + "</td>";
					html += "<td>" + String.valueOf(map.get("ifoutoctetsbps")) + "</td>";
					html += "</tr>";
				}
			}
		}
		else{
			html += "<tr bgcolor='#FFFFFF'><td colspan='6'>没有设备端口性能数据</td></tr>";
		}
		html += "</table><br>";
		return html;
	}
	/**
	 * 获取设备名称map
	 */
	private void getDevNameMap()
	{
		String sql = "select device_id,device_name from tab_gw_device";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List list = jt.queryForList(sql);
		int len = list.size();
		if (list != null && len > 0){
			Map map = null;
			for (int i=0;i<len;i++){
				map = (Map)list.get(i);
				devNameMap.put(String.valueOf(map.get("device_id")), String.valueOf(map.get("device_name")));
			}
		}
	}
	/**
	 * 生成表名和查询数据
	 * @param day
	 * @param type
	 */
	private void getTableInfo(String day, String type)
	{
		DateTimeUtil dt = new DateTimeUtil(day);
		//月
		if ("2".equals(type)){
			table = "pm_month_stats_" + dt.getYear();
			end = dt.getLongTime();
			start = end - 30*24*60*60;
		}
		//周
		else if ("1".equals(type)){
			table = "pm_week_stats_" + dt.getYear();
			end = dt.getLongTime();
			start = end - 7*24*60*60;
		}
		//日
		else{
			table = "pm_day_stats_" + dt.getYear();
			end = dt.getLongTime();
			start = end - 24*60*60;
		}
	}
}
