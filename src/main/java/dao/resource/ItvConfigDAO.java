package dao.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.dbimpl.AreaManageSyb;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2008-12-17
 */
public class ItvConfigDAO {

	private static final Logger LOG = LoggerFactory.getLogger(ItvConfigDAO.class);
	
	private JdbcTemplate jt;

	/**
	 * 入任务表
	 * @author gongsj
	 * @date 2010-3-19
	 * @param task_id
	 * @param task_name
	 * @param temp_id
	 * @param order_acc_oid
	 * @param order_time
	 * @param is_check
	 * @param check_acc_oid
	 * @param check_time
	 * @return
	 */
	public String inTaskTable(long task_id, String task_name, int temp_id,
			long order_acc_oid, long order_time, int is_check,
			long check_acc_oid, long check_time) {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into gw_conf_all_task (task_id,task_name,temp_id,order_acc_oid,order_time,is_check,check_acc_oid,check_time) values('")
		   .append(task_id).append("','").append(task_name).append("',").append(temp_id).append(",").append(order_acc_oid)
		   .append(",").append(order_time).append(",").append(is_check).append(",").append(check_acc_oid).append(",")
		   .append(check_time).append(")");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
        psql.getSQL();
		return sql.toString();
	}

	/**
	 * 入策略表
	 * @author gongsj
	 * @date 2010-3-19
	 * @param id
	 * @param acc_oid
	 * @param time
	 * @param type
	 * @param gather_id
	 * @param device_id
	 * @param oui
	 * @param serialnumber
	 * @param username
	 * @param sheet_param
	 * @param service_id
	 * @param task_id
	 * @param order_id
	 * @param sheetType
	 * @return
	 */
	public String inStrategyLogTable(long id, long acc_oid, long time, int type,
			String gather_id, String device_id, String oui,
			String serialnumber, String username, String sheet_param,
			String service_id, long task_id, int order_id, int sheetType, int tempId, int isLastOne, String sub_service_id) {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into gw_serv_strategy_log (id,acc_oid,time,type,gather_id,device_id,oui,device_serialnumber,username,sheet_para,")
		   .append("service_id,task_id,order_id, sheet_type, temp_id, is_last_one, sub_service_id) " + "values (").append(id).append(",").append(acc_oid).append(",")
		   .append(time).append(",").append(type).append(",'").append(gather_id).append("','").append(device_id).append("','")
		   .append(oui).append("','").append(serialnumber).append("','").append(username).append("','").append(sheet_param)
		   .append("',").append(service_id).append(",'").append(task_id).append("',").append(order_id).append(",").append(sheetType)
		   .append(",").append(tempId).append(",").append(isLastOne).append(",").append(sub_service_id).append(")");
		PrepareSQL psql = new PrepareSQL(sql.toString());
        psql.getSQL();
		return sql.toString();
	}

	/**
	 * 入或更新gw_dev_serv
	 * @author gongsj
	 * @date 2010-3-19
	 * @param id
	 * @param acc_oid
	 * @param time
	 * @param type
	 * @param gather_id
	 * @param device_id
	 * @param oui
	 * @param serialnumber
	 * @param username
	 * @param service_id
	 * @param task_id
	 * @param order_id
	 * @param sheetType
	 * @return
	 */
	public String inStrategyTable(long id, long acc_oid, long time, int type,
			String gather_id, String device_id, String oui,
			String serialnumber, String username, String sheet_param,
			String service_id, long task_id, int order_id, int sheetType, int tempId, int isLastOne, String sub_service_id) {
		
		StringBuilder sql = new StringBuilder();
		StringBuilder tempSql = new StringBuilder();
		tempSql.append("delete from gw_serv_strategy where device_id='").append(device_id).append("' and temp_id=").append(tempId);
		PrepareSQL psql = new PrepareSQL(tempSql.toString());
        psql.getSQL();
		if(1 != DBOperation.executeUpdate(tempSql.toString())) {
			LOG.warn("删除设备{}的iTV业务策略失败", device_id);
		}
		
		sql.append("insert into gw_serv_strategy (id,acc_oid,time,type,gather_id,device_id,oui,device_serialnumber,username,sheet_para,")
		   .append("service_id,task_id,order_id, sheet_type, temp_id, is_last_one,sub_service_id) " + "values (").append(id).append(",").append(acc_oid).append(",")
		   .append(time).append(",").append(type).append(",'").append(gather_id).append("','").append(device_id).append("','")
		   .append(oui).append("','").append(serialnumber).append("','").append(username).append("','").append(sheet_param)
		   .append("',").append(service_id).append(",'").append(task_id).append("',").append(order_id).append(",").append(sheetType)
		   .append(",").append(tempId).append(",").append(isLastOne).append(",").append(sub_service_id).append(")");
		
		tempSql = null;
		psql = new PrepareSQL(sql.toString());
        psql.getSQL();
		return sql.toString();
	}
	
	/**
	 * 入业务用户表
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-12-18
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String inServUserTable(String user_id, String serv_type_id,
			String username, String wan_type, String vpi, String vci,
			String port, long dealdate, long opendate, long updatetime) {
		
		StringBuilder strSelectSQL = new StringBuilder();
		strSelectSQL.append("select user_id from hgwcust_serv_info where user_id=")
		            .append(user_id).append(" and serv_type_id=").append(serv_type_id);
		
		LOG.debug("检查此用户在业务用户表中是否存在SQL：{}", strSelectSQL);
		List<Map<String,String>> tempRS = jt.queryForList(strSelectSQL.toString());
		StringBuilder strSQL = new StringBuilder();
		if(0<tempRS.size()){
			strSQL.append("update hgwcust_serv_info set wan_type=").append(wan_type)
			      .append(",vpiid='").append(vpi).append("',vciid=").append(vci).append(",bind_port='")
			      .append(port).append("',dealdate=").append(dealdate).append(",opendate=")
			      .append(opendate).append(",updatetime=").append(updatetime).append(" where user_id=")
			      .append(user_id).append(" and serv_type_id=").append(serv_type_id);
		}else{
			strSQL.append("insert into hgwcust_serv_info (user_id,serv_type_id,username,wan_type,vpiid,vciid,bind_port,dealdate,opendate,updatetime)")
				  .append(" values (").append(user_id).append(",").append(serv_type_id).append(",'").append(username).append("',")
				  .append(wan_type).append(",'").append(vpi).append("',").append(vci).append(",'").append(port).append("',")
				  .append(dealdate).append(",").append(opendate).append(",").append(updatetime).append(")");
		}
		
		strSelectSQL = null;
		tempRS = null;
		PrepareSQL psql = new PrepareSQL(strSQL.toString());
    	psql.getSQL();
		return strSQL.toString();
	}
	
	/**
	 * 域表操作
	 * 
	 * @author Jason(3412)
	 * @date 2008-12-17
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public static List<String> insertAreaTable(int res_type, String task_id,
			String curAreaId, boolean needDel) {
		List<String> sqlList = new ArrayList<String>();
		StringBuilder sql1 = new StringBuilder();
		StringBuilder sql2 = new StringBuilder();
		if (needDel) {
			PrepareSQL psql = new PrepareSQL(sql1.append("delete from tab_gw_res_area where res_id='").append(task_id).append("' and res_type=").append(res_type).toString());
	    	psql.getSQL();
			sqlList.add(sql1.append("delete from tab_gw_res_area where res_id='").append(task_id).append("' and res_type=").append(res_type).toString());
		}

		List list = AreaManageSyb.getAllareaPid(curAreaId);
		int listSize = list.size() - 1;
		while (listSize >= 0) {
			if (null == list.get(listSize) || list.get(listSize).equals("1")
					|| list.get(listSize).equals("0")) {
			} else {
				PrepareSQL psql = new PrepareSQL(sql2.append("insert into tab_gw_res_area(res_type,res_id,area_id) values(")
						 .append(res_type).append(",'").append(task_id).append("',").append(list.get(listSize))
						 .append(")").toString());
		    	psql.getSQL();
				sqlList.add(sql2.append("insert into tab_gw_res_area(res_type,res_id,area_id) values(")
								 .append(res_type).append(",'").append(task_id).append("',").append(list.get(listSize))
								 .append(")").toString());
			}
			--listSize;
		}
		return sqlList;
	}

	@SuppressWarnings("unchecked")
	public Map getServiceId() {
		String strSQL = "select convert(varchar(3),serv_type_id) +'|'+convert(varchar(3),oper_type_id) service, service_id from tab_service";
		
		//add by zhangcong@ 2011-06-21
		if(LipossGlobals.isOracle())
		{
//			strSQL = "select to_char(serv_type_id,'999') || '|' || to_char(oper_type_id,'999') service, service_id from tab_service";
			strSQL = "select (serv_type_id || '|' || oper_type_id) service, service_id from tab_service ";
		}

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select concat(serv_type_id, '|', oper_type_id) service, service_id from tab_service ";
		}
			
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return DataSetBean.getMap(strSQL);
	}

	/**
	 * 获取业务策略对应关系
	 * 
	 * @param temp_id 模板ID
	 * @author Jason(3412)
	 * @date 2008-12-17
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List getServStrary(int temp_id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select order_id,serv_type_id,oper_type_id from gw_conf_template_service where temp_id=").append(temp_id);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.queryForList(sql.toString());
	}

	/**
	 * 获取软件升级的目标型号，版本对应关系
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-12-17
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public Map getSoftUp(String temp_name) {
		StringBuilder sql = new StringBuilder();
		sql.append("select devicetype_id_old as device_model_id, devicetype_id from gw_soft_upgrade_temp a, gw_soft_upgrade_temp_map b where temp_name='")
		   .append(temp_name).append("' and a.temp_id=b.temp_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return DataSetBean.getMap(sql.toString());
	}

	/**
	 * 获取软件升级的目标型号，版本对应关系
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-12-17
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public Map getSoftUp() {
		StringBuilder sql = new StringBuilder();

		// oracle
		if(Global.DB_ORACLE == DBUtil.GetDB()){
			sql.append("select to_char(temp_id,'99999999') || '|' || to_char(devicetype_id_old,'9999'),devicetype_id from gw_soft_upgrade_temp_map");
		}
		// sysbase
		else if (Global.DB_SYBASE == DBUtil.GetDB()) {
			sql.append("select convert(varchar(8),temp_id)+'|'+ convert(varchar(4),devicetype_id_old),devicetype_id from gw_soft_upgrade_temp_map");
		}
		// teledb
		else if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql.append("select concat(CAST(temp_id AS CHAR(8)), '|', CAST(devicetype_id_old AS CHAR(4))), devicetype_id from gw_soft_upgrade_temp_map");
		}
		

		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return DataSetBean.getMap(sql.toString());
	}

	/**
	 * 获取软件升级目标版本模板
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-12-17
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public Map getSoftTemp() {
		String strSQL = "select temp_id,temp_name from gw_soft_upgrade_temp";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return DataSetBean.getMap(strSQL);
	}

	/**
	 * 获得策略类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map getStrategyType(String...typeIds) {
		
		StringBuffer strBuff = new StringBuffer();
		strBuff.append("select type_id,type_name from gw_strategy_type where ");
		
		for (int i = 0; i < typeIds.length; i++) {
			if (i == typeIds.length - 1) {
				strBuff.append(" type_id =").append(typeIds[i]);
			} else {
				strBuff.append(" type_id =").append(typeIds[i]).append(" or ");
			}
		}
		PrepareSQL psql = new PrepareSQL(strBuff.toString());
		psql.getSQL();
		return DataSetBean.getMap(strBuff.toString());
	}
	
	/**
	 * 根据devicetype_id获取软件升级的工单参数
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-12-17
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Map> getSoftFileInfo()
	{
		//修改 by 王森博
		String strSQL = "select devicetype_id,outter_url+'/'+server_dir+'/'+softwarefile_name as file_url";
		
		//add by zhangcong@ 2011-06-21
		if(LipossGlobals.isOracle())
		{
			strSQL = "select devicetype_id,outter_url || '/' || server_dir || '/' || softwarefile_name as file_url";
		}

		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select devicetype_id, concat(outter_url, '/', server_dir, '/', softwarefile_name) as file_url";
		}

		strSQL = strSQL + ",softwarefile_size,softwarefile_name"
		+ " from tab_software_file a, tab_file_server b where a.dir_id=b.dir_id"
		+ " and softwarefile_isexist=1";
		
		PrepareSQL psql = new PrepareSQL(strSQL);
		List list = jt.queryForList(psql.getSQL());
		Map<String, Map> map = new HashMap<String, Map>();
		for (int i = 0; i < list.size(); i++)
		{
			Map tmap = (Map) list.get(i);
			map.put(StringUtil.getStringValue(tmap.get("devicetype_id")), tmap);
		}
		return map;
	}

	/**
	 * 获取预处理的IOR
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getPreProcessIOR() {
		String ior = null;
		String iorSQL = "select ior from tab_ior where object_name='PreProcess' and object_poa='PreProcess_Poa'";
		PrepareSQL psql = new PrepareSQL(iorSQL);
		psql.getSQL();
		Map<String, String> iorMap = DataSetBean.getRecord(iorSQL);
		ior = iorMap.get("ior");
		return ior;
	}
	
	public boolean updateSQLList(ArrayList<String> sqlList) {
		int iCode[] = DataSetBean.doBatch(sqlList);
		if (iCode != null && iCode.length > 0) {
			LOG.debug("批量执行IPTV策略入库：  成功");
			return true;
			
		} else {
			LOG.debug("批量执行IPTV策略入库：  失败");
			return false;
		}

	}

	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}
}
