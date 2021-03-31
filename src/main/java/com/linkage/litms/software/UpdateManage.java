package com.linkage.litms.software;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.DBUtil;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.Candidate.SheetDefault;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;

public class UpdateManage {
	ArrayList SqlList = new ArrayList();
	String devicetype_id = null;
	ArrayList List = new ArrayList();
	
	/**
	 * 设备自动升级策略
	 */
	private String autoUpdate_InsertSQL = "insert into tab_device_autoupdate(task_id,task_name,task_type,username,task_time,type,is_check,is_over) values(?,?,?,?,?,?,?,?)";

	/**
	 * 策略工单对应表
	 */
	private String strategyupdate_sheet_InsertSQL = "insert into tab_strategyupdate_sheet(task_id,device_id,sheet_id,gather_id,sheet_serial,sheet_status,excute_count) values(?,?,?,?,?,0,0)";
	
	/**
	 * 策略工单对应表
	 */
	private String task_result_InsertSQL = "insert into tab_device_task_result(task_id,device_id,ex_result) values(?,?,0)";
	
	/**
	 * 根据工单id，来查询对应的工单信息
	 */
	private String sheetInfo_SQL = "select * from tab_sheet where sheet_id in(?)";
	
	/**
	 * 搜索这些设备中没有完成的某个类型的工单数目
	 */
	private String isAllowConfig_SQL="select count(*) number from tab_device_autoupdate a,tab_strategyupdate_sheet b where a.task_id = b.task_id and a.is_check!=2 and a.task_type=? and b.device_id in(?) and b.sheet_status=0";

	

	
	
	
	/**
	 * 配置对应业务的参数
	 * @param sheetID
	 * @param service_id
	 * @param param
	 */
	private void setParam(String sheetID,String service_id,String[] param){

		String strSql = "";
		
		/**
         * modify by qixq
         * time:2009/03/26 16:45
         */
        // 根据业务和设备型号获取模板，然后根据模板获取命令id
        String sql = SheetDefault.getTempSql(service_id, devicetype_id);
        Cursor tmpCursor = DataSetBean.getCursor(sql);  
        //如果没取到模板，则用默认模板devicetype_id = 0
        if(null == tmpCursor || tmpCursor.getRecordSize() <= 0){
                tmpCursor = DataSetBean.getCursor(SheetDefault.getTempSql(service_id, "0"));
        }
        
//		String sql = "select b.tc_serial,c.have_defvalue,c.def_value,c.para_type_id from tab_servicecode a,tab_template_cmd b,tab_template_cmd_para c  where a.template_id = b.template_id and  a.service_id = "
//			+ service_id + " and a.devicetype_id = " + devicetype_id  + " and b.tc_serial = c.tc_serial order by b.rpc_order,c.rpc_order";
//		logger.debug("然后根据模板获取命令===>:" + sql);
//		Cursor tmpCursor = DataSetBean.getCursor(sql);
		Map fields = tmpCursor.getNext();
		
		String _have_defvalue = "";
		String _tc_serial = "";
		String _para_type_id = "";
		String _def_value = "";
		if (null != fields) {
			int m = 1,n = 0;
			while(null != fields){
				_have_defvalue = (String)fields.get("have_defvalue");
				_tc_serial = (String)fields.get("tc_serial");
				_para_type_id = (String)fields.get("para_type_id");
				if(_have_defvalue.equals("0")){
					_def_value = param[n];
					n++;
				} else {
					_def_value = (String)fields.get("def_value");
				}
				strSql = "insert into tab_sheet_para(sheet_id,tc_serial,para_serial,def_value,para_type_id) values("
						+ "'"
						+ sheetID
						+ "',"
						+ _tc_serial
						+ ","
						+ m
						+ ","
						+ "'" + _def_value 
						+ "',"
						+ _para_type_id 
						+ ")";
				strSql = strSql.replaceAll(",,", ",null,");
				strSql = strSql.replaceAll(",\\)", ",null)");  
				m++;
				PrepareSQL psql = new PrepareSQL(strSql);
				psql.getSQL();
				SqlList.add(strSql);
				fields = tmpCursor.getNext();
			}
		}
	}
	
	
	/**
	 * 处理升级策略任务工单对应表数据
	 * @param task_id
	 * @param device_id
	 * @param sheet_id
	 * @param strGatherId
	 * @param service_id
	 */
	private void initInsertSQL(long task_id,String device_id,String sheet_id,String strGatherId,String service_id){
		PrepareSQL pSQL = new PrepareSQL();
		
		//升级策略任务工单对应表
		pSQL.setSQL(strategyupdate_sheet_InsertSQL);
		pSQL.setLong(1, task_id);
		pSQL.setString(2, device_id);
		pSQL.setString(3, sheet_id);
		pSQL.setString(4, strGatherId);
		
		//上传配置文件
		if ("2".equals(service_id)){
			pSQL.setInt(5, 1);
		}
		//软件简单升级
		else if ("5".equals(service_id)){
			pSQL.setInt(5, 2);
		}
		//下发配置文件
		else if ("1".equals(service_id)){
			pSQL.setInt(5, 3);
		}
		else {
			pSQL.setInt(5, 0);
		}
		
		List.add(pSQL.getSQL());
		
	}
	
	/**
	 * 设备任务执行结果表
	 * @param task_id
	 * @param device_id
	 */
	private void setTaskResult(long task_id,String device_id){
		PrepareSQL pSQL = new PrepareSQL();
		
		//设备任务执行结果表 tab_device_task_result
		pSQL.setSQL(task_result_InsertSQL);
		pSQL.setLong(1, task_id);
		pSQL.setString(2, device_id);
		
		List.add(pSQL.getSQL());
	}
	
	/**
	 * 拼装新下发的工单的sql
	 * 
	 * @author lizhaojun
	 * @param task_id
	 * @return
	 */
	public String getSql(String task_id) {
		String sql = "";
		
		if (task_id != null){
			sql = "select a.*,b.task_name,b.task_type,b.task_time,b.type,b.is_over from tab_device_task_result a,tab_device_autoupdate b where a.task_id = b.task_id and a.task_id = " + task_id;
		}
		else{
			sql = "select a.*,b.task_name,b.task_type,b.task_time,b.type,b.is_over from tab_device_task_result a,tab_device_autoupdate b where a.task_id = b.task_id";
		}
		
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return sql;
	}
	
	/**
	 * 拼装所有工单的sql
	 * 
	 * @author lizhaojun
	 * @param request
	 * @return
	 */
	public String getSqlAll(HttpServletRequest request) {
		String sql = "";
		String str_lms = request.getParameter("start");
		
		//开始时间
		long startTime = 0;
		if (str_lms != null && !"".equals(str_lms)){
			startTime = Long.parseLong(str_lms);
		}
		//结束时间是开始时间加一天
		Date dt = new Date(startTime + 24 * 3600);
		long endTime = dt.getTime();
		
		//是否完成
		String filter = request.getParameter("filter");
		
		sql = "select a.*,b.task_name,b.task_type,b.task_time,b.type,b.is_over from tab_device_task_result a,tab_device_autoupdate b where a.task_id = b.task_id ";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select a.device_id, a.ex_result, a.task_id, b.task_name, b.task_type, b.task_time, b.type, b.is_over " +
					"from tab_device_task_result a,tab_device_autoupdate b where a.task_id = b.task_id ";
		}
		if (startTime != 0){
			sql += " and b.task_time > " + startTime + " and b.task_time < " + endTime;
		}
		
		if (filter != null && !"-1".equals(filter)){
			sql += " and b.is_over = " + filter;
		}
		
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return sql;
	}
	
	/**
	 * 任务执行结果的描述信息
	 * @return
	 */
	public Map getResultDesc(){
		Map resultMap = new HashMap();
		
		String sql = "select * from tab_service_disc where service_id = 6";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select ex_result, result_disc from tab_service_disc where service_id = 6";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		
		while (fields != null){
			resultMap.put(fields.get("ex_result"), fields.get("result_disc"));
			fields = cursor.getNext();
		}
		
		return resultMap;
	}
	
	public Map getDeviceInfo(){
		Map deviceInfo = new HashMap();
		
//		String sql = "select a.*,b.device_model from tab_gw_device a,tab_devicetype_info b where a.device_status = 1 and a.devicetype_id = b.devicetype_id";
		String sql = "select a.*,b.device_model from tab_gw_device a,gw_device_model b where a.device_status = 1 and a.device_model_id = b.device_model_id";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select a.oui, a.device_serialnumber, a.device_id, b.device_model " +
					"from tab_gw_device a,gw_device_model b where a.device_status = 1 and a.device_model_id = b.device_model_id";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		
		String oui = "";
		String device_serialnumber = "";
		String device_model = "";
		String device_id = "";
		while (fields != null){
			oui = (String)fields.get("oui");
			device_serialnumber = (String)fields.get("device_serialnumber");
			device_model = (String)fields.get("device_model");
			device_id = (String)fields.get("device_id");
			
			deviceInfo.put(device_id, oui+"#"+device_serialnumber+"#"+device_model);
			
			fields = cursor.getNext();
		}
		
		return deviceInfo;
		
	}
	
	/**
	 * 返回正在处理的工单列表. lizhaojun
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return 返回Cursor类型数据
	 */
	public Cursor excSheetList(String tmpSql) {
		Cursor cursor = null;
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(tmpSql);
		return cursor;

	}
	
	/**
	 * 获取自动升级任务的详细信息
	 * @param request
	 * @return
	 */
	public ArrayList getUpdateTaskDetail(HttpServletRequest request)
	{
		ArrayList list = new ArrayList();
		String task_id = request.getParameter("task_id");
		String success = request.getParameter("successPercent");
		float percent =0f;
		if(success==null)
		{
			String tab_autoupdate_sheetSQL = "select count(*) number from tab_strategyupdate_sheet where task_id ="+task_id;
			PrepareSQL psql = new PrepareSQL(tab_autoupdate_sheetSQL);
			psql.getSQL();
			HashMap totalCountMap = DataSetBean.getRecord(tab_autoupdate_sheetSQL);
			String totalCount ="0";
			if(totalCountMap!=null)
			{
				totalCount = (String)totalCountMap.get("number");
			}
			
			//策略任务有对应的工单
			if(!"0".equals(totalCount))
			{
				String sheet_success_SQL = "select count(*) number from tab_strategyupdate_sheet a,tab_sheet_report b where a.sheet_id=b.sheet_id and b.fault_code =1 and a.task_id ="+task_id;
				psql = new PrepareSQL(sheet_success_SQL);
				psql.getSQL();
				HashMap successCountMap = DataSetBean.getRecord(sheet_success_SQL);
				String successCount = "0";
				if(successCountMap!=null)
				{
					successCount = (String)successCountMap.get("number");
					percent = Float.parseFloat(successCount)/Float.parseFloat(totalCount);
					success = StringUtils.formatNumber(String.valueOf(percent*100),2);
				}
				else
				{
					success ="0";
				}
			}
			else
			{
				success ="0";
			}
		}
		String sql = "select a.oui,a.device_serialnumber,b.sheet_id,c.exec_status,c.username,c.exec_desc,c.fault_code,c.fault_desc,c.start_time,c.end_time"
			+"  from tab_gw_device a inner join tab_strategyupdate_sheet b  on a.device_id=b.device_id left join tab_sheet_report c on b.sheet_id = c.sheet_id"
			+" where b.task_id="+task_id;
		
		String oui = request.getParameter("oui");
		String device_serialnumber = request.getParameter("device_serialnumber");
		
		if (oui != null){
			sql += " and a.oui = '" + oui + "' and a.device_serialnumber = '" + device_serialnumber + "'";
		}
		
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		//分页
		String stroffset = request.getParameter("offset");
		int pagelen = 30;
		int offset;
		if (stroffset == null)
		{
			offset = 1;
		}
		else
		{
			offset = Integer.parseInt(stroffset);
		}		
		QueryPage qryp = new QueryPage();	
		qryp.initPage(sql, offset, pagelen);
		String strBar = qryp.getPageBar("&task_id=" + task_id+"&successPercent="+success);
		list.add(strBar);
		Cursor cursor = DataSetBean.getCursor(sql);
		list.add(cursor);
		list.add(success);
		return list;		
	}
	
	/**
	 * 这个策略是否允许配置
	 * @param request
	 * @return   true:允许配置  false:不允许配置
	 */
	public boolean isAllowConfig(HttpServletRequest request)
	{
		String[] device_list = request.getParameterValues("device_id");
		String auto_type = request.getParameter("auto_type");
		String device_idStr = null;
		if(null==device_list||0==device_list.length||null==auto_type||"".equals(auto_type))
		{
			return false;
		}
		
		//为sql语句的device_id作准备
		for(int i=0;i<device_list.length;i++)
		{
			if(null==device_idStr)
			{
				device_idStr = "'"+device_list[i]+"'";
			}
			else
			{
				device_idStr+=",'"+device_list[i]+"'";
			}
		}
		
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(isAllowConfig_SQL);
		pSQL.setStringExt(1,auto_type,false);
		pSQL.setStringExt(2,device_idStr,false);
		
		HashMap record = DataSetBean.getRecord(pSQL.getSQL());
		if(null==record)
		{
			return false;
		}
		
		//如果没有查到记录,则允许配置
		if(Integer.parseInt((String)record.get("number"))==0)
		{
			return true;
		}
		
		return false;
		
	}
}
