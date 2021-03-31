package dao.report;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DBAdapter;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author Jason(3412)
 * @date 2009-4-28
 */
public class StrategyConfigStatDAO {

	/** log */
	private static final Logger LOG = LoggerFactory
			.getLogger(StrategyConfigStatDAO.class);

	private JdbcTemplate jt;

	
	/**
	 * 按属地统计业务用户下发执行业务的情况
	 * 	
	 * @param 业务类型，属地，开始时间(受理)，结束时间(受理)
	 * @author Jason(3412)
	 * @date 2009-9-8
	 * @return List<city_id,业务开户用户总数,未下发执行数,下发执行成功数,下发执行失败数>
	 */
	public List statServUserByCity(int servTypeId, String cityPid, long startTime, long endTime){
		LOG.debug("statServUserByCity({},{},{},{})", new Object[]{servTypeId,cityPid,startTime,endTime});
		List rlist = null;
		StringBuffer sqlBuf = new StringBuffer();
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlBuf.append("select b.city_id, count(*) as total,")
					.append("count (case when a.status=0 and a.result_id=0 then 1 else null end) as undo,")
					.append("count (case when a.status=0 and a.result_id!=0 then 1 else null end) as unsuccess,")
					.append("count (case when a.result_id=1 then 1 else null end) as success,")
					.append("count (case when a.status!=0 and a.result_id!=1 then 1 else null end) as fail ")
					.append("from gw_serv_strategy a, tab_gw_device b where a.device_id=b.device_id and a.temp_id=1 and a.is_last_one=1");
		}
		else {
			sqlBuf.append("select b.city_id, count(1) as total,")
					.append("count (case when a.status=0 and a.result_id=0 then 1 else null end) as undo,")
					.append("count (case when a.status=0 and a.result_id!=0 then 1 else null end) as unsuccess,")
					.append("count (case when a.result_id=1 then 1 else null end) as success,")
					.append("count (case when a.status!=0 and a.result_id!=1 then 1 else null end) as fail ")
					.append("from gw_serv_strategy a, tab_gw_device b where a.device_id=b.device_id and a.temp_id=1 and a.is_last_one=1");
		}
//		StringBuffer sqlBuf = new StringBuffer("select b.city_id, count(1) as total,"
//				+ " count(case when a.open_status=0 then 1 else null end) as undo,"
//				+ " count(case when a.open_status=1 then 1 else null end) as success,"
//				+ " count(case when a.open_status=-1 then 1 else null end) as fail"
//				+ " from hgwcust_serv_info a , tab_hgwcustomer b"
//				+ " where a.serv_type_id=? and a.serv_status=1 and a.user_id=b.user_id");
		if(0 != startTime){
			sqlBuf.append(" and a.time>=" + startTime);
		}
		if(0 != endTime){
			sqlBuf.append(" and a.time<" + endTime);
		}	
		Map<String, String> cityMap = CityDAO.getCityIdPidMap();
		if(null == cityMap.get(cityPid) || "-1".equals(cityMap.get(cityPid))){
			//如果为省中心，不需要加属地限制。
		}else{
			List list = CityDAO.getAllNextCityIdsByCityPid(cityPid);
			sqlBuf.append(" and b.city_id in (" + StringUtils.weave(list) + ")");
			list = null;
		}
		cityMap = null;
		sqlBuf.append(" group by b.city_id");
		
		PrepareSQL psql = new PrepareSQL(sqlBuf.toString());
//		psql.setInt(1, servTypeId);
		
		rlist = jt.queryForList(psql.getSQL());
		return rlist;
	}

	
	/**
	 * 获取业务用户列表，
	 * 
	 * @param exeStatus：1表示执行成功，0表示未执行，-1表示执行失败，100全部
	 * @author Jason(3412)
	 * @date 2009-5-6
	 * @return void
	 */
	public ArrayList queryServUserListData(String servTypeId, String cityId,
			String exeStatus, long startTime, long endTime) {
		LOG.debug("serviceId, startTime, endTime, exeStatus");
		LOG.debug(servTypeId + "_" + startTime + "_" + endTime + "_" + exeStatus);
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select a.username, b.city_id, b.device_serialnumber, d.vendor_name, c.hardwareversion, c.softwareversion, e.device_model,");
		
		//add by zhangcong@ 2011-06-21

		// oracle
		if(Global.DB_ORACLE == DBUtil.GetDB()){
			sqlBuf.append("to_char(to_date('1970-01-01 00:00:00','yyyy-mm-dd hh24:mi:ss') + time/24/60/60,'YYYY/MM/DD') as time ");
		}
		// sysbase
		else if (Global.DB_SYBASE == DBUtil.GetDB()) {
			sqlBuf.append("convert(char,dateadd(ss,a.time,'1970-01-01 00:00:00'),111) as time ");
		}
		// teledb
		else if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlBuf.append("FROM_UNIXTIME(a.time,'%Y/%m/%d') as time ");
		}

		sqlBuf.append("from gw_serv_strategy a, tab_gw_device b, tab_devicetype_info c, tab_vendor d, gw_device_model e ")
			  .append("where a.device_id=b.device_id and b.devicetype_id=c.devicetype_id and c.vendor_id=d.vendor_id and c.device_model_id=e.device_model_id ")
			  .append("and a.temp_id=1");

//		//String strSQL = "select b.username,a.oui,a.device_serialnumber,a.time from gw_serv_strategy a"
//		StringBuffer sqlBuf = new StringBuffer("select b.username,b.city_id,b.oui,b.device_serialnumber,"
//				+ " convert(char,dateadd(ss,a.dealdate,'1970-01-01 00:00:00'),111) as time"
//				+ " from hgwcust_serv_info a, tab_hgwcustomer b "
//				+ " where a.serv_type_id=? and a.serv_status=1 and a.user_id=b.user_id ");
		if(0 != startTime){
			sqlBuf.append(" and a.time>=" + startTime);
		}
		if(0 != endTime){
			sqlBuf.append(" and a.time<" + endTime);
		}	
		Map<String, String> cityMap = CityDAO.getCityIdPidMap();
		if(null == cityMap.get(cityId) || "-1".equals(cityMap.get(cityId))){
			//如果为省中心，不需要加属地限制。
		}else{
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sqlBuf.append(" and b.city_id in (" + StringUtils.weave(list) + ")");
			list = null;
		}
		cityMap = null;
		//执行状态
		if ("1".equals(exeStatus)) {
			// 成功
			sqlBuf.append(" and a.result_id=1");
		} else if ("0".equals(exeStatus)) {
			// 未执行
			sqlBuf.append(" and a.status=0 and a.result_id=0");
		} else if ("-1".equals(exeStatus)) {
			// 失败
			sqlBuf.append(" and a.status!=0 and a.result_id!=1");
		} else if ("2".equals(exeStatus)) {
			// 暂未成功，下次再做
			sqlBuf.append(" and a.status=0 and a.result_id!=0");
		} else{
			//全部
		}
		PrepareSQL psql = new PrepareSQL(sqlBuf.toString());
		psql.setStringExt(1, servTypeId, false);
		return DataSetBean.executeQuery(psql.getSQL(), null);
	}
	
	
	
	
	
	
	
	
	/**
	 * 获取统计结果
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-5-5
	 * @return String
	 */
	private String strategyStatSQL(int serviceId, String cityPid) {
		String strSQL = "select c.city_id,c.city_name," + com.linkage.litms.common.util.DbUtil.getNullFunction("d.total", "0") + " total,"
			+ " " + com.linkage.litms.common.util.DbUtil.getNullFunction("d.success", "0") + " success," + com.linkage.litms.common.util.DbUtil.getNullFunction("d.undo", "0") + " undo,"
			+ " (" + com.linkage.litms.common.util.DbUtil.getNullFunction("d.total", "0") + "-" + com.linkage.litms.common.util.DbUtil.getNullFunction("d.undo", "0") + "-" + com.linkage.litms.common.util.DbUtil.getNullFunction("d.success", "0") + ") fail"	
			+ " from tab_city c left join (select b.city_id,count(1) total, "
				+ " count(case when a.result_id=1 then 1 else null end) success, "
				+ " count(case when a.status=0 then 1 else null end) undo "
				// + ", ? as service_id "
				+ " from #tmp_gw_serv_strategy a, tab_gw_device b where a.service_id=? "
				+ " and a.device_id=b.device_id "
				+ " group by b.city_id) d on c.city_id=d.city_id ";
		
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setInt(1, serviceId);
		Map<String, String> cityMap = CityDAO.getCityIdPidMap();
		if(null == cityMap.get(cityPid) || "-1".equals(cityMap.get(cityPid))){
			//如果为省中心，不需要加属地限制。
		}else{
			List list = CityDAO.getAllNextCityIdsByCityPid(cityPid);
			psql.append(" where c.city_id in (" + StringUtils.weave(list) + ")");
			list = null;
		}
		cityMap = null;
		return psql.getSQL();
	}

	/**
	 * 建临时表，过滤策略表中设备相同的记录
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-5-5
	 * @return String
	 */
	private String createTmpTabelSQL(int serviceId, long startTime, long endTime) {
		LOG.debug("serviceId, startTime, endTime");
		LOG.debug(serviceId + "_" + startTime + "_" + endTime);
		String strSQL = "select a.*"
				+ " into #tmp_gw_serv_strategy "
				+ " from gw_serv_strategy a, (select device_serialnumber,oui,max(time) maxtime "
				+ " from gw_serv_strategy group by device_serialnumber,oui) b"
				+ " where a.time = b.maxtime and a.device_id=b.device_id "
				+ " and a.service_id=? ";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select a.id, a.acc_oid, a.time, a.type, a.gather_id, a.device_id, a.oui, a.device_serialnumber, a.username, a.sheet_para, " +
					"a.service_id, a.task_id, a.order_id, a.sheet_type, a.temp_id, a.is_last_one, a.sub_service_id "
					+ " into #tmp_gw_serv_strategy "
					+ " from gw_serv_strategy a, (select device_serialnumber,oui,max(time) maxtime "
					+ " from gw_serv_strategy group by device_serialnumber,oui) b"
					+ " where a.time = b.maxtime and a.device_id=b.device_id "
					+ " and a.service_id=? ";
		}

		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setInt(1, serviceId);
		if (0 != startTime) {
			psql.appendAndNumber("a.time", PrepareSQL.BIGGEREQUEAL, String.valueOf(startTime));
		}
		if (0 != endTime) {
			psql.appendAndNumber("a.time", PrepareSQL.SMALLER, String.valueOf(endTime));
		}
		return psql.getSQL();
	}

	/**
	 * drop掉临时表
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-5-5
	 * @return String
	 */
	private String dropTmpTableSQL() {
		String strSQL = "drop table #tmp_gw_serv_strategy";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return strSQL;
	}

	/**
	 * 策略配置统计
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-5-5
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getStatStrategyResult(int serviceId,
			String cityPid, long startTime, long endTime) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData metadata;
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		HashMap<String, String> resultMap = null;
		String value = null;
		try {
			conn = DBAdapter.getJDBCConnection();
			stmt = conn.createStatement();
			// 创建临时表
			stmt.executeUpdate(createTmpTabelSQL(serviceId, startTime,
							endTime));
			LOG.debug(createTmpTabelSQL(serviceId, startTime, endTime));
			// 查询
			rs = stmt.executeQuery(strategyStatSQL(serviceId, cityPid));
			LOG.debug(strategyStatSQL(serviceId, cityPid));
			while (rs.next()) {
				metadata = rs.getMetaData();
				resultMap = new HashMap<String, String>();
				for (int i = 1; i <= metadata.getColumnCount(); i++) {
					value = rs.getString(metadata.getColumnLabel(i));
					if (value == null)
						value = "";
					resultMap.put(metadata.getColumnLabel(i).toLowerCase(),
							value.trim());
				}
				resultList.add(resultMap);
			}
			// drop临时表
			stmt.executeUpdate(dropTmpTableSQL());
			
		} catch (SQLException sqle) {
			LOG.error("executeQuery is error: getStatStrategyResult");
			LOG.error("{}", sqle);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
				LOG.error("close Statement object error");
			}
			try {
				conn.close();
				conn = null;
			} catch (Exception e) {
				LOG.error("close connection error in ");
			}
		}
		LOG.debug(resultList.toString());
		return resultList;
	}

	/**
	 * 获取策略配置设备列表，
	 * 
	 * @param exeStatus：1表示执行成功，0表示未执行，-1表示执行失败，100全部
	 * @author Jason(3412)
	 * @date 2009-5-6
	 * @return void
	 */
	public ArrayList queryDevListData(String serviceId, String cityId,
			String exeStatus, long startTime, long endTime) {
		LOG.debug("serviceId, startTime, endTime, exeStatus");
		LOG.debug(serviceId + "_" + startTime + "_" + endTime + "_" + exeStatus);
		//String strSQL = "select b.username,a.oui,a.device_serialnumber,a.time from gw_serv_strategy a"
		
		String strSQL = "select b.username,a.oui,a.device_serialnumber,convert(char,dateadd(ss,a.time,'1970-01-01 00:00:00'),111) as time";
		
		//add by zhangcong@ 2011-06-21
		if(LipossGlobals.isOracle())
		{
			strSQL = "select b.username,a.oui,a.device_serialnumber,to_char(to_date('1970-01-01 00:00:00','yyyy-mm-dd hh24:mi:ss') + time/24/60/60,'YYYY/MM/DD') as time";
		}

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select b.username,a.oui,a.device_serialnumber,FROM_UNIXTIME(a.time, '%Y/%m/%d') as time";
		}
		strSQL = strSQL + " from gw_serv_strategy a"
				+ " left join ? on a.device_id=b.device_id";
				//+ " a.service_id=? ";
				//+ " and city_id in (?) ";
		// + "[and a.status=0|a.result_id=1|(a.status!=0 and a.result_id!=1)]";
		String tabname = null;
		if (1 == LipossGlobals.SystemType()) {
			// ITMS
			tabname = " tab_hgwcustomer b ";
		} else {
			// BBMS
			tabname = " tab_egwcustomer b ";
		}
		List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
		String citysSql = StringUtils.weave(list);
		list = null;
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setStringExt(1, tabname, false);
		if(2 == LipossGlobals.SystemType()){
			psql.append(" left join tab_customerinfo c on b.customer_id=c.customer_id ");
		}
		psql.append(" where a.service_id=" + serviceId);
		psql.append(" and city_id in (" + citysSql + ")");
		//psql.setStringExt(2, serviceId, false);
		//psql.setStringExt(3, citysSql, false);
		//执行状态
		if ("1".equals(exeStatus)) {
			// 成功
			psql.appendAndNumber("a.result_id", PrepareSQL.EQUEAL, "1");
		} else if ("0".equals(exeStatus)) {
			// 未执行
			psql.appendAndNumber("a.status", PrepareSQL.EQUEAL, "0");
		} else if ("-1".equals(exeStatus)) {
			// 失败
			psql.append(" and (a.status!=0 and a.result_id!=1)");;
		}
		//配置时间
		if(0 != startTime){
			psql.appendAndNumber("a.time", PrepareSQL.BIGGEREQUEAL, String.valueOf(startTime));
		}
		if(0 != endTime){
			psql.appendAndNumber("a.time", PrepareSQL.SMALLER, String.valueOf(endTime));
		}
		return DataSetBean.executeQuery(psql.getSQL(), null);
	}

	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}
}
