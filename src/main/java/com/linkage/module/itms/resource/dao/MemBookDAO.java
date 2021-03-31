package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.iq80.leveldb.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年12月18日
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class MemBookDAO  extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(MemBookDAO.class);
	private HashMap<String,String> vendorMap = null;  //type = 1
	private HashMap<String,String> specTypeMap = null; //type = 2
	private HashMap<String,String> workOptsMap = null; //type = 3
	private HashMap<String,String> receptionMap = null; //type = 4
	
	
	public List<HashMap<String,String>> getMap(int type){
		String sql = "select value_id ,value from tab_memorandum_type where type=?";
	    PrepareSQL pSQL = new PrepareSQL(sql);
	    pSQL.setInt(1, type);
		return DBOperation.getRecords(pSQL.getSQL());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map> qryWorkList(String busNo,String vendor,String spec,String spec_model,String hardware,String software,String workOpts
			,String connPerson,String connPhone,String reception,String startTime,String endTime, 
			int curPage_splitPage,int num_splitPage){
		
		String sql = "select * from tab_memorandum where work_status !=-1";
		if(!StringUtil.IsEmpty(busNo)){
			sql = sql + " and bus_no=" + busNo;
		}
		if(!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)){
			sql = sql + " and vendor_name='" + vendor +"'";
		}
		if(!StringUtil.IsEmpty(spec) && !"-1".equals(spec)){
			sql = sql + " and spec_type ='" + spec +"'";
		}
		if(!StringUtil.IsEmpty(spec_model)){
			sql = sql + " and spec_model='" + spec_model +"'";
		}
		if(!StringUtil.IsEmpty(hardware)){
			sql = sql + " and hardwareversion='"+hardware + "'";
		}
		if(!StringUtil.IsEmpty(software)){
			sql = sql + " and softwareversion='"+software + "'";
		}
		if(!StringUtil.IsEmpty(workOpts) && !"-1".equals(workOpts)){
			sql = sql + " and work_opts='"+workOpts + "'";
		}
		if(!StringUtil.IsEmpty(connPerson)){
			sql = sql + " and conn_person='"+connPerson+"'";
		}
		if(!StringUtil.IsEmpty(connPhone)){
			sql = sql + " and conn_phone='"+connPhone + "'";
		}
		if(!StringUtil.IsEmpty(reception) && !"-1".equals(reception)){
			sql = sql + " and reception='"+reception+"'";
		}
		if(!StringUtil.IsEmpty(startTime)){
			sql = sql + " and start_time >="+startTime;
		}
		if(!StringUtil.IsEmpty(endTime)){
			sql = sql + " and end_time<="+endTime;
		}
		
	    sql = sql + " order by start_time desc";
	    PrepareSQL pSQL = new PrepareSQL(sql);
		 if(-1 != curPage_splitPage){
		    	return querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
		    			num_splitPage,new RowMapper() {
					  public Object mapRow(ResultSet rs, int arg1) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						try {
							
							String start_time = rs.getString("start_time");
							String end_time = rs.getString("end_time");
							
							map.put("busNo", rs.getString("bus_no"));
							map.put("spec_model", rs.getString("spec_model"));
							map.put("hardwareversion", rs.getString("hardwareversion"));
							map.put("softwareversion", rs.getString("softwareversion"));
							map.put("workContent", rs.getString("work_content"));
							map.put("phone", rs.getString("conn_phone"));
							map.put("connPerson", rs.getString("conn_person"));
							map.put("status", getStatusType(StringUtil.getIntegerValue(rs.getString("status"))));
							map.put("startTime", new DateTimeUtil(Long.valueOf(start_time) * 1000).getYYYY_MM_DD_HH_mm_ss());
							if(!StringUtil.IsEmpty(end_time) && !"0".equals(end_time)){
								map.put("endTime", new DateTimeUtil(Long.valueOf(end_time) * 1000).getYYYY_MM_DD_HH_mm_ss());
							}else{
								map.put("endTime", "");
							}
							map.put("commit", rs.getString("remark"));
							map.put("fileNmae", rs.getString("file_name"));
							
							map.put("vendor_name", rs.getString("vendor_name"));
							map.put("spec_type", rs.getString("spec_type"));
							map.put("workOpts", rs.getString("work_opts"));
							map.put("reseption", rs.getString("reception"));
						} catch (SQLException e) {
							logger.error(e.getMessage());
						}
						return map; 
					}
				});
		    }else{
		    	return jt.query(pSQL.getSQL(), new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						try {
							String start_time = rs.getString("start_time");
							String end_time = rs.getString("end_time");
							
							map.put("busNo", rs.getString("bus_no"));
							map.put("spec_model", rs.getString("spec_model"));
							map.put("hardwareversion", rs.getString("hardwareversion"));
							map.put("softwareversion", rs.getString("softwareversion"));
							map.put("workContent", rs.getString("work_content"));
							map.put("phone", rs.getString("conn_phone"));
							map.put("connPerson", rs.getString("conn_person"));
							map.put("status", getStatusType(StringUtil.getIntegerValue(rs.getString("status"))));
							map.put("startTime", new DateTimeUtil(Long.valueOf(start_time) * 1000).getYYYY_MM_DD_HH_mm_ss());
							if(!StringUtil.IsEmpty(end_time) && !"0".equals(end_time)){
								map.put("endTime", new DateTimeUtil(Long.valueOf(end_time) * 1000).getYYYY_MM_DD_HH_mm_ss());
							}else{
								map.put("endTime", "");
							}
							map.put("commit", rs.getString("remark"));
							map.put("fileNmae", rs.getString("file_name"));
							
							map.put("vendor_name", rs.getString("vendor"));
							map.put("spec_type", rs.getString("spec"));
							map.put("workOpts", rs.getString("work_opts"));
							map.put("reseption", rs.getString("reception"));

						} catch (SQLException e) {
							logger.error(e.getMessage());
						}
						return map;  
					}
				});
		    }
	}
	
	public String getStatusType(int status){
	  String ret = "";
	  if(status == 1){
		  ret = "进行中";
	  }else
		  if(status == 2){
			  ret = "已完成";
		  }else{
			  ret = "未完成";
		  }
	  return ret;
	}
	public int qryWorkListCount(String busNo,String vendor,String spec,String spec_model,String hardware,String software,String workOpts
			,String connPerson,String connPhone,String reception,String startTime,String endTime){
		String sql = "select count(1) num from tab_memorandum where work_status !=-1";
		if(!StringUtil.IsEmpty(busNo)){
			sql = sql + " and bus_no=" + busNo;
		}
		if(!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)){
			sql = sql + " and vendor_name ='" + vendor + "'";
		}
		if(!StringUtil.IsEmpty(spec)  && !"-1".equals(spec)){
			sql = sql + " and spec_type ='" + spec + "'";
		}
		if(!StringUtil.IsEmpty(spec_model)){
			sql = sql + " and spec_model='" + spec_model + "'";
		}
		if(!StringUtil.IsEmpty(hardware)){
			sql = sql + " and hardwareversion='"+hardware + "'";
		}
		if(!StringUtil.IsEmpty(software)){
			sql = sql + " and softwareversion='"+software + "'";
		}
		if(!StringUtil.IsEmpty(workOpts) && !"-1".equals(workOpts)){
			sql = sql + " and work_opts='"+workOpts + "'";
		}
		if(!StringUtil.IsEmpty(connPerson)){
			sql = sql + " and conn_person='"+connPerson + "'";
		}
		if(!StringUtil.IsEmpty(connPhone)){
			sql = sql + " and conn_phone='"+connPhone + "'";
		}
		if(!StringUtil.IsEmpty(reception) && !"-1".equals(reception)){
			sql = sql + " and reception='"+reception + "'";
		}
		if(!StringUtil.IsEmpty(startTime)){
			sql = sql + " and start_time>="+startTime;
		}
		if(!StringUtil.IsEmpty(endTime)){
			sql = sql + " and end_time<="+endTime;
		}
		
	    sql = sql + " order by start_time desc";
	    PrepareSQL pSQL = new PrepareSQL(sql);
	    
	    try
		{
			ArrayList<HashMap<String,String>> rets = DBOperation.getRecords(pSQL.getSQL());
			if(null != rets && !rets.isEmpty()){
				return StringUtil.getIntegerValue(rets.get(0).get("num"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	    return 0;
	}
	
	public List<HashMap<String,String>> qryWorkExcel(String busNo,String vendor,String spec,String spec_model,String hardware,String software,String workOpts
			,String connPerson,String connPhone,String reception,String startTime,String endTime){
		String sql = "select bus_no,vendor_name, spec_type,spec_model,hardwareversion,softwareversion,"
				+ "work_opts,WORK_CONTENT,CONN_PERSON,CONN_PHONE,RECEPTION,REMARK,FILE_NAME,"
				+ " (case when START_TIME !=0 then TO_CHAR(START_TIME/ (60 * 60 * 24) +TO_DATE('1970-01-01 08:00:00', 'YYYY-MM-DD HH:MI:SS'), 'YYYY-MM-DD HH:MI:SS') end) st,"
				+ " (case when END_TIME !=0 then TO_CHAR(END_TIME/ (60 * 60 * 24) +TO_DATE('1970-01-01 08:00:00', 'YYYY-MM-DD HH:MI:SS'), 'YYYY-MM-DD HH:MI:SS') end) end,"
				+ " (case status when 1 then '进行中' when 2 then '已完成' when 0 then '未完成' end) work_status"
				+ " from tab_memorandum where work_status !=-1";

		// teledb
		if (DBUtil.GetDB() == 3) {
			sql = "select bus_no,vendor_name, spec_type,spec_model,hardwareversion,softwareversion,"
					+ "work_opts,WORK_CONTENT,CONN_PERSON,CONN_PHONE,RECEPTION,REMARK,FILE_NAME,"
					+ " (case when START_TIME !=0 then date_format(START_TIME/ (60 * 60 * 24) +str_to_date('1970-01-01 08:00:00', '%Y-%m-%d %H:%i:%s'), '%Y-%m-%d %H:%i:%s') end) st,"
					+ " (case when END_TIME !=0 then date_format(END_TIME/ (60 * 60 * 24) +str_to_date('1970-01-01 08:00:00', '%Y-%m-%d %H:%i:%s'), '%Y-%m-%d %H:%i:%s') end) end,"
					+ " (case status when 1 then '进行中' when 2 then '已完成' when 0 then '未完成' end) work_status"
					+ " from tab_memorandum where work_status !=-1";
		}
		if(!StringUtil.IsEmpty(busNo)){
			sql = sql + " and bus_no=" + busNo;
		}
		if(!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)){
			sql = sql + " and vendor_name='" + vendor +"'";
		}
		if(!StringUtil.IsEmpty(spec) && !"-1".equals(spec)){
			sql = sql + " and spec_type ='" + spec +"'";
		}
		if(!StringUtil.IsEmpty(spec_model)){
			sql = sql + " and spec_model='" + spec_model +"'";
		}
		if(!StringUtil.IsEmpty(hardware)){
			sql = sql + " and hardwareversion='"+hardware +"'";
		}
		if(!StringUtil.IsEmpty(software)){
			sql = sql + " and softwareversion='"+software +"'";
		}
		if(!StringUtil.IsEmpty(workOpts) && !"-1".equals(workOpts)){
			sql = sql + " and work_opts='"+workOpts +"'";
		}
		if(!StringUtil.IsEmpty(connPerson)){
			sql = sql + " and conn_person='"+connPerson +"'";
		}
		if(!StringUtil.IsEmpty(connPhone)){
			sql = sql + " and conn_phone='"+connPhone +"'";
		}
		if(!StringUtil.IsEmpty(reception) && !"-1".equals(reception)){
			sql = sql + " and reception='"+reception +"'";
		}
		if(!StringUtil.IsEmpty(startTime)){
			sql = sql + " and start_time>="+startTime;
		}
		if(!StringUtil.IsEmpty(endTime)){
			sql = sql + " and end_time<="+endTime;
		}
		
	    sql = sql + " order by start_time desc";
	    PrepareSQL pSQL = new PrepareSQL(sql);
	    ArrayList<HashMap<String,String>> rets = null;
	    try
		{
			rets = DBOperation.getRecords(pSQL.getSQL());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	    return rets;
	}
	
	public String addMemorandumInfo(String busNo,String vendor,String spec,String spec_model,String hardware,String software,String workOpts
			,String workContent,String connPerson,String connPhone,String reception,String startTime,
			String endTime,int status,String remark,String fileName){
		
		//查看文件名是否重复
		if(checkPicIn(fileName)){
			return "文件名已存在,请重新添加";
		}
		String sql = addMemorandum(busNo, vendor, spec, spec_model, hardware, software, workOpts, workContent, connPerson, connPhone, reception, startTime, endTime, status, remark, fileName);
		try
		{
			int res = DBOperation.executeUpdate(sql);
			if(res > 0){
				return "新增成功";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "新增失败";
		}
		return "新增失败";
	}
	
	public boolean checkPicIn(String fileName){
		String sql = "select count(1) num from tab_memorandum where WORK_STATUS !=-1 and file_name=?";
		PrepareSQL pSQL = new PrepareSQL(sql);
		pSQL.setString(1, fileName);
		
		int num = 0;
		try
		{
			 num  = StringUtil.getIntegerValue(DBOperation.getRecord(pSQL.getSQL()).get("num"));
		}
		catch (Exception e)
		{
			
			logger.error("checkPicIn => err : {}",e.getMessage());
			return false;
		}
		if(num > 0){
			return true;
		}
		return false;
	}
	
	public String addMemorandum(String busNo,String vendor,String spec,String spec_model,String hardware,String software,String workOpts
			,String workContent,String connPerson,String connPhone,String reception,String startTime,
			String endTime,int status,String remark,String fileName){
		
		String sql = "insert into tab_memorandum values(?,?,?,?,?,   ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?)";
		PrepareSQL pSQL = new PrepareSQL(sql);
		pSQL.setLong(1,StringUtil.getLongValue(busNo));
		
		if(StringUtil.IsEmpty(vendor) || "-1".equals(vendor)){
			vendor = "";
		}
		pSQL.setString(2,vendor);
		
		if(StringUtil.IsEmpty(spec) || "-1".equals(spec)){
			spec = "";
		}
		pSQL.setString(3,spec);
		pSQL.setString(4,StringUtil.IsEmpty(spec_model) ? "":spec_model);
		pSQL.setString(5,StringUtil.IsEmpty(hardware) ? "":hardware);
		pSQL.setString(6,StringUtil.IsEmpty(software) ? "":software);
		
		if(StringUtil.IsEmpty(workOpts) || "-1".equals(workOpts)){
			workOpts = "";
		}
		pSQL.setString(7,workOpts);
		pSQL.setString(8,StringUtil.IsEmpty(workContent) ? "":workContent);
		pSQL.setString(9,StringUtil.IsEmpty(connPerson) ? "":connPerson);
		pSQL.setString(10,StringUtil.IsEmpty(connPhone) ? "":connPhone);
		if(StringUtil.IsEmpty(reception) || "-1".equals(reception)){
			reception = "";
		}
		pSQL.setString(11,reception);
		if(!StringUtil.IsEmpty(startTime)){
			pSQL.setLong(12,StringUtil.getLongValue(startTime));
		}else{
			pSQL.setLong(12,0l);
		}
		
		if(!StringUtil.IsEmpty(endTime)){
			pSQL.setLong(13,StringUtil.getLongValue(endTime));
		}else{
			pSQL.setLong(13,0l);
		}
		pSQL.setInt(14,status);
		pSQL.setString(15,remark);
		pSQL.setString(16,fileName);
		pSQL.setInt(17,1);  //默认正常状态
		
		long currMills = System.currentTimeMillis()/1000;
		pSQL.setLong(18,currMills);   
		pSQL.setLong(19,currMills);   
		return pSQL.getSQL();
	}
	
	public String updateMemorandum(String busNo,String vendor,String spec,String spec_model,String hardware,String software,String workOpts
			,String workContent,String connPerson,String connPhone,String reception,String startTime,
			String endTime,int status,String remark,String fileName){
		
		//logger.warn("{},{},{},{}",new Object[]{vendor,spec,workOpts,reception});
		long currMils = System.currentTimeMillis()/1000;
		String sql = "update tab_memorandum set status = "+status +",current_time="+currMils;
		 
		if(!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)){
			sql = sql + ", vendor_name='" + vendor + "'";
		}
		if(!StringUtil.IsEmpty(spec) && !"-1".equals(spec)){
			sql = sql + " , spec_type='" + spec + "'";
		}
		if(!StringUtil.IsEmpty(spec_model)){
			sql = sql + " , spec_model='" + spec_model + "'";
		}
		if(!StringUtil.IsEmpty(hardware)){
			sql = sql + " , hardwareversion='" + hardware + "'";
		}
		if(!StringUtil.IsEmpty(software)){
			sql = sql + " , softwareversion='" + software + "'";
		}
		if(!StringUtil.IsEmpty(workOpts) && !"-1".equals(workOpts)){
			sql = sql + " , work_opts='" + workOpts + "'";
		}
		if(!StringUtil.IsEmpty(workContent)){
			sql = sql + ", work_content='" + workContent + "'";
		}
		if(!StringUtil.IsEmpty(connPerson)){
			sql = sql + " , conn_person='" + connPerson + "'";
		}
		if(!StringUtil.IsEmpty(connPhone)){
			sql = sql + ", conn_phone='" + connPhone + "'";
		}
		if(!StringUtil.IsEmpty(reception) && !"-1".equals(reception)){
			sql = sql + ", reception='" + reception + "'";
		}
		if(!StringUtil.IsEmpty(startTime)){
			sql = sql + ", start_time=" + startTime;
		}
		if(!StringUtil.IsEmpty(endTime)){
			sql = sql + ", end_time=" + endTime;
		}
		
		if(!StringUtil.IsEmpty(remark)){
			sql = sql + ", remark='" + remark + "'";
		}
		if(!StringUtil.IsEmpty(fileName)){
			sql = sql + ",file_name='" + fileName + "'";
		}
		
		sql = sql + " where bus_no = " + busNo; 
		PrepareSQL pSQL = new PrepareSQL(sql);
		try
		{
			int res = DBOperation.executeUpdate(pSQL.getSQL());
			if(res > 0){
				return "更新成功";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "更新失败";
		}
		return "更新失败";
	}
	
	public String deletMem(String busNo){
		
		//String sql = "delete from tab_memorandum where bus_no = ?";
		long currTime = System.currentTimeMillis()/1000;
		String sql = "update tab_memorandum set WORK_STATUS=-1,current_time=? where bus_no =?";
		PrepareSQL pSQL = new PrepareSQL(sql);
		pSQL.setLong(1, currTime);
		pSQL.setString(2, busNo);
		try
		{
			int rts = DBOperation.executeUpdate(pSQL.getSQL());
			if(rts > 0){
				return "删除成功";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "删除失败";
		}
		return "删除成功";
	}
	
	//通过厂商id获取终端类型
    public List getSpecType(String vendor_id){
		String sql = "select value_id,value from tab_memorandum_type where type=? and parent_id =?";
		PrepareSQL pSQL = new PrepareSQL(sql);
		pSQL.setInt(1, 2);
		pSQL.setString(2, vendor_id);
		
		ArrayList<HashMap<String,String>> specMapList = null;
		try
		{
			specMapList = DBOperation.getRecords(pSQL.getSQL());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return specMapList; 
	}
    
    public static void main(String args[]){
    	/*String vendor = "-1";
    	if(!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)){
			System.out.print("123");
		}*/
    	
    	System.out.println(System.currentTimeMillis());
    	System.out.println(System.currentTimeMillis() / 1000);
    	System.out.println(Math.round(Math.random() * 10L));
    	System.out.println(Math.round(Math.random() * 10000L));
    }
	
}
