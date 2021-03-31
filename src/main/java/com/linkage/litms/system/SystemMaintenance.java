package com.linkage.litms.system;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;

public class SystemMaintenance {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(SystemMaintenance.class);
	
	
    /**
     * 创建系统维护规则
     */
    private String INSERT_TAB_SYS_PLAN = "insert into tab_sys_plan (plan_id,acc_loginname,sys_item,execute_time,active,is_check,plan_desc) "
    									 + "values(?,?,?,?,?,?,?)";
    /**
     * 删除系统维护规则
     */
    private String DELETE_TAB_SYS_PLAN = "delete from tab_sys_plan where plan_id =?"; 
    
    
    /**
     * 更新系统维护规则
     */
    private String UPDATE_TAB_SYS_PLAN = "update tab_sys_plan set acc_loginname='?',sys_item=?,execute_time='?',active=?,is_check=?,plan_desc='?' where plan_id=?"; 
    
   
    
    
    private PrepareSQL pSQL = null;
    /**
     * 
     * @param request
     * @return
     */
    
    
    
    public  int insertSysPlan(HttpServletRequest request){
    	
    	String _action = request.getParameter("action");
    	String _plan_id = request.getParameter("plan_id");
    	logger.debug("_plan_id  =====>" + _plan_id);
    	String strSql = "";
    	if(_action.equals("delete")){
			  if(pSQL == null)
				pSQL = new PrepareSQL();
			  
    		  pSQL.setSQL(DELETE_TAB_SYS_PLAN);
    		  pSQL.setLong(1,Long.parseLong(_plan_id));
    		  strSql = pSQL.getSQL();
    	} else {
    		
    		String acc_loginname = request.getParameter("acc_loginname");
    		String sys_item = request.getParameter("sys_item");
    		String execute_time = request.getParameter("execute_time");
    		String active = request.getParameter("active");
    		String is_check = request.getParameter("is_check");
    		String plan_desc = request.getParameter("plan_desc");
    		if(_action.equals("add")){
    			if(pSQL == null)
    				pSQL = new PrepareSQL();
    			
	    		long plan_id = DataSetBean.getMaxId("tab_sys_plan", "plan_id");
	    		pSQL.setSQL(INSERT_TAB_SYS_PLAN);
	    		pSQL.setLong(1,plan_id);
	    		pSQL.setString(2,acc_loginname);
	    		pSQL.setLong(3,Long.parseLong(sys_item));
	    		pSQL.setString(4,execute_time);
	    		pSQL.setLong(5,Long.parseLong(active));
	    		pSQL.setLong(6,Long.parseLong(is_check));
	    		pSQL.setString(7,plan_desc);
	    		strSql = pSQL.getSQL();
        	} else {
//    			if(pSQL == null)
//    				pSQL = new PrepareSQL();
//    			
//        		pSQL.setSQL(UPDATE_TAB_SYS_PLAN);
//	    		pSQL.setString(1,acc_loginname);
//	    		pSQL.setLong(2,Long.parseLong(sys_item));
//	    		pSQL.setString(3,execute_time);
//	    		pSQL.setLong(4,Long.parseLong(active));
//	    		pSQL.setLong(5,Long.parseLong(is_check));
//	    		pSQL.setString(6,plan_desc);
//	    		pSQL.setLong(1,Long.parseLong(_plan_id));
	    		strSql = "update tab_sys_plan set acc_loginname='" + acc_loginname + "',sys_item=" + sys_item + ",execute_time='" +  execute_time
	    				+ "',active=" + active + ",is_check=" +  is_check + ",plan_desc='" + plan_desc + "' where plan_id=" + _plan_id;
        	}
    	}
    	PrepareSQL psql = new PrepareSQL(strSql);
        psql.getSQL();
    	return DataSetBean.executeUpdate(strSql);
    	   	
    }
    /**
     * 
     * @param request
     * @return
     */
    public String getPlanHtml(HttpServletRequest request){
    	String acc_loginname = request.getParameter("acc_loginname");
    	String active = request.getParameter("active");
    	String sys_item = request.getParameter("sys_item");
    	String is_check = request.getParameter("is_check");
    	String strSql = "select * from tab_sys_plan where 1=1 ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSql = "select sys_item, active, is_check, plan_id, acc_loginname, execute_time, plan_desc, plan_id" +
					" from tab_sys_plan where 1=1 ";
		}
    	if(acc_loginname!=null && !acc_loginname.equals("")){
    		strSql +=" and acc_loginname='" + acc_loginname + "'";   		
    	}
    	
    	if(active != null && !active.equals("")){
    		strSql +=" and active=" + active + " ";   
    	}
    	if(sys_item != null && !sys_item.equals("-1")){
    		strSql +=" and sys_item=" + sys_item + " ";   
    	}
    	
    	if(is_check != null && !is_check.equals("")){
    		strSql +=" and is_check=" + is_check + " ";   
    	}
    	
    	PrepareSQL psql = new PrepareSQL(strSql);
        psql.getSQL();
    	Cursor cursor = DataSetBean.getCursor(strSql);
    	Map fields = cursor.getNext();
    	String strHtml = "<TABLE border=0 cellspacing=1 cellpadding=2 width=\"95%\" align=center bgcolor=#999999>";
    	strHtml +="<TR bgcolor=#ffffff>";
    	strHtml +="<TH nowrap>编号</TH>";
    	strHtml +="<TH nowrap>制定人</TH>";
    	strHtml +="<TH nowrap>维护项目</TH>";
    	strHtml +="<TH nowrap>执行时间</TH>";
    	strHtml +="<TH nowrap>是否启用</TH>";
    	strHtml +="<TH nowrap>是否审核</TH>";
    	strHtml +="<TH nowrap>描述</TH>";
    	strHtml +="<TH nowrap>操作</TH>";
    	strHtml +="</TR>";
    	
    	if(fields == null){
    		strHtml +="<TR bgcolor=#ffffff>";
    		strHtml +="<TD colspan='8'>系统没有计划任务策略！</TD>";
    		strHtml +="</TR>";
    	} else {
    		String _sys_item  = "";
    		String _active = "";
    		String _is_check = "";
    		while(fields != null){
    			_sys_item = (String)fields.get("sys_item");
    			_active = (String)fields.get("active");
    			_is_check = (String)fields.get("is_check");
    			if(_sys_item.equals("0")){
    				_sys_item = "WEB服务器维护";
    			} else if(_sys_item.equals("1")){
    				_sys_item = "ACS维护";
    			} else {
    				_sys_item = "数据库维护";
    			}
    			
    			if(_active.equals("0")){
    				_active = "不启用";
    			} else {
    				_active = "启用";
    			}
    			
    			if(_is_check.equals("0")){
    				_is_check = "未审核";
    			} else {
    				_is_check = "审核";
    			}
    			strHtml += "<TR bgcolor=#ffffff>";
    			strHtml +="<TD>"+ fields.get("plan_id")+"</TD>";
    			strHtml +="<TD>"+ fields.get("acc_loginname")+"</TD>";
    			strHtml +="<TD>"+ _sys_item +"</TD>";
    			strHtml +="<TD>"+ fields.get("execute_time")+"</TD>";
    			strHtml +="<TD>"+ _active +"</TD>";
    			strHtml +="<TD>"+ _is_check +"</TD>";
    			strHtml +="<TD>"+ fields.get("plan_desc") +"</TD>";
    			strHtml +="<TD aleign='center'><A HREF='sysplan_edit.jsp?plan_id="+ (String)fields.get("plan_id")+ "')>编辑</A> | <A href=javascript:// onclick=delplan('"+(String)fields.get("plan_id")+"')>删除</A>";    			
    			fields = cursor.getNext();   			
    		}
    	}
    	return strHtml;
    }
    
    /**
     * 
     * @param request
     * @return
     */
    public Map getDetail(HttpServletRequest request){
    	String plan_id = request.getParameter("plan_id");
    	String sql = "select * from tab_sys_plan where plan_id=" + plan_id;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select plan_id, sys_item, execute_time, active, is_check, plan_desc from tab_sys_plan where plan_id=" + plan_id;
		}
    	PrepareSQL psql = new PrepareSQL(sql);
        psql.getSQL();
    	return DataSetBean.getRecord(sql);
    }
    
    public String getPlanlogHtml(HttpServletRequest request){
    	
    	String sys_item = request.getParameter("sys_item");
    	
        String start_time = request.getParameter("start_time") + " " + request.getParameter("start_ms");
        String end_time = request.getParameter("end_time") + " " +  request.getParameter("end_ms");
        
        DateTimeUtil dateTime = new DateTimeUtil(start_time);
        long s_time = dateTime.getLongTime();
        dateTime = new DateTimeUtil(end_time);
        long e_time = dateTime.getLongTime();
    	logger.debug("s_time :" + s_time + ";e_time :" + e_time);
    	String strSql = "select * from tab_sys_log where 1=1 and execute_time>" + s_time + " and execute_time<" + e_time;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSql = "select sys_item, execute_time, remark from tab_sys_log where 1=1 and execute_time>" + s_time + " and execute_time<" + e_time;
		}
    	if(sys_item!=null && !sys_item.equals("-1")){
    		strSql +=" and sys_item=" + sys_item + "";   		
    	}
    	  	
    	PrepareSQL psql = new PrepareSQL(strSql);
        psql.getSQL();
    	
    	Cursor cursor = DataSetBean.getCursor(strSql);
    	Map fields = cursor.getNext(); 
    	String strHtml = "<TABLE border=0 cellspacing=1 cellpadding=2 width=\"95%\" align=center bgcolor=#999999>";
    	strHtml +="<TR bgcolor=#ffffff>";
    	strHtml +="<TH nowrap>维护项目</TH>";
    	strHtml +="<TH nowrap>执行时间</TH>";
    	strHtml +="<TH nowrap>描述</TH>";
    	strHtml +="</TR>";
    	
    	if(fields == null){
    		strHtml +="<TR bgcolor=#ffffff>";
    		strHtml +="<TD colspan='3'>系统没有维护日志！</TD>";
    		strHtml +="</TR>";
    	} else {
    		String _sys_item  = "";
    		while(fields != null){
    			_sys_item = (String)fields.get("sys_item");
    			if(_sys_item.equals("0")){
    				_sys_item = "WEB服务器维护";
    			} else if(_sys_item.equals("1")){
    				_sys_item = "ACS维护";
    			} else {
    				_sys_item = "数据库维护";
    			}
    			strHtml += "<TR bgcolor=#ffffff>";
    			strHtml +="<TD>"+ _sys_item +"</TD>";
    			strHtml +="<TD>"+ StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",Long.parseLong((String)fields.get("execute_time")))+"</TD>";
    			strHtml +="<TD>"+ fields.get("remark") +"</TD>";    		
    			strHtml +="</TR>"; 
    			fields = cursor.getNext();   			
    		}
    	}
    	return strHtml;
    	
    }
    /**
     * 
     * @param request
     * @return
     */
    public int tabItemAct(HttpServletRequest request){
    	
    	String _sql = "";
    	String action = request.getParameter("action");
    	String tab_id = request.getParameter("tab_id");
    	if(action.equals("add")){
    		
    		String tab_name = request.getParameter("tab_name");
    		
    		String tmpSql = "select count(*) as num from tab_table_sys where tab_name ='" + tab_name + "'";
    		PrepareSQL psql = new PrepareSQL(tmpSql);
            psql.getSQL();
    		Map fields = DataSetBean.getRecord(tmpSql);
    		
    		if(Integer.parseInt((String)fields.get("num"))>0){
    			return 0;
    		}
    		
    		String tab_name_zh = request.getParameter("tab_name_zh");
    		String data_type = request.getParameter("data_type");
    		
    		long _tab_id = DataSetBean.getMaxId("tab_table_sys","tab_id");
    		
    		_sql = "insert into tab_table_sys(tab_id,tab_name,tab_name_zh,data_type) values ("
    				+ _tab_id + ",'" + tab_name + "','" +  tab_name_zh + "'," + data_type + ")";
    		
    	} else {
    		
    	}
    	
    	if(_sql != null && !"".equals(_sql)){
    		PrepareSQL psql = new PrepareSQL(_sql);
            psql.getSQL();
    		int code =  DataSetBean.executeUpdate(_sql);
    		if(code > 0){
    			return 1;
    		} else {
    			return -1;
    		}
    	} else {
    		return -1;
    	}
    	
    }
}
