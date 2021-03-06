<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="../timelater.jsp"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.TimeZone"%>
<%@ page import="java.util.Calendar"%>
<%
     String alarmStr =request.getParameter("alarmStr");
     String gatherId=request.getParameter("gatherId");
     Calendar 	calendar = Calendar.getInstance( TimeZone.getTimeZone("GMT+8:00"));      
     int year=calendar.get(Calendar.YEAR);    
     int weekNo= calendar.get(Calendar.WEEK_OF_YEAR)+1;
	 String postfix, tableName, sql;
	 Map hashtable;
	 int  k=0;
     while( true){   
          postfix="_"+String.valueOf(year)+"_"+String.valueOf(weekNo);//生成event_raw后缀
          tableName="event_raw"+postfix; //生成表名
          sql = "select  *  from  "+tableName+"  where serialno='"+alarmStr +"'  and subserialno='0'  and gather_id='"+gatherId+"'";
		 // teledb
		 if (DBUtil.GetDB() == 3) {
			 sql = "select displaystring, devicetype, sourceip, creatortype, gather_id, sourcename, createtime " +
					 "from  "+tableName+"  where serialno='"+alarmStr +"'  and subserialno='0'  and gather_id='"+gatherId+"'";
		 }
		 PrepareSQL psql = new PrepareSQL(sql);
		 psql.getSQL();
          hashtable = DataSetBean.getRecord(sql);
	     if( hashtable!=null)
		    break;
	     else{	 
     	    if( ++k==5) //当查表数目超过5 时， 退出查询告警
            	break ;
        	weekNo=weekNo-1;//表单号减1        	
        	if( weekNo==0) 
        	{
        	   year=year-1;                   //若是一年中的第一周，那就报表年份减一                                                                   
             calendar.set(year,11,30);      //重新设置日期格式
	          weekNo= calendar.get(Calendar.WEEK_OF_YEAR)+1;    
	        }                           
	     } 
	  }               
 %>    
       
 <%    
	 String subject,warnReason,warnResove;	
	 String content ,createTime,deviceType, sourceIp,creatorType,gather_Id,sourceName;
	 long time=0;
	 if(hashtable!=null){
	   content=(String)hashtable.get("displaystring");
	   deviceType=(String)hashtable.get("devicetype");
	   sourceIp=(String)hashtable.get("sourceip");
	   creatorType=(String)hashtable.get("creatortype");
	   gather_Id=(String)hashtable.get("gather_id");
	   sourceName=(String)hashtable.get("sourcename");
	   try{     
                time = Long.parseLong((String)hashtable.get("createtime"))*1000;
				Date date = new Date(time);
                createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
           }
       catch(Exception e){ 
		   createTime="no read data";
            }
	  }
	 else{
		 content="not read data";
		 deviceType="not read data";
	     sourceIp="not read data";
		 creatorType="not read data";
	     gather_Id="not read data";
		 sourceName="not read data";
		 createTime="not read data";
	  }
	 subject = "";
	 warnReason = "";
	 warnResove = "";
%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ page import="com.linkage.commons.db.PrepareSQL" %>
<html>
<head>
<meta http-equiv="Content-Language" content="zh-cn">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>知识库</title>
<script language="javascript"><!--
function checkForm(){
    var obj=document.frm;
    if(obj.subject.value==""){
        alert("请输入主题。");
        return false;
    }
    else if(obj.content.value==""){
        alert("请输入内容。");
        return false;
    }
    else
        return true;
}

//--></script>
</head>
<body>
<br>
<form name="frm" method="POST" action="ackSave.jsp?ack_CreateTime=<%=time%>" onsubmit="javascript:return checkForm();">
<table align="center" border=0 cellspacing=1 cellpadding=2 width="70%" bgcolor="#000000">

	<tr>
		<td class=column width=30% align=right> 主题 :</td>
		<td class=column>
                        <textarea rows="2" name="subject" cols="85"><%=subject%></textarea></td>
	</tr>
	
	<tr>
		<td class=column align=right>告警原因：</td>
		<td class=column>
                        <textarea rows="3" name="warnReason" cols="85"><%=warnReason%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>解决方法：</td>
		<td class=column>
                        <textarea rows="4" name="warnResove" cols="85"><%=warnResove%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>内容：</td>
		<td class=column>
                  <textarea rows="3" name="content" cols="85" readonly ><%=content%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>告警创建时间：</td>
		<td class=column>
           <textarea rows="1" name="createTime" cols="85" readonly  ><%=createTime%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>设备类型：</td>
		<td class=column>
           <textarea rows="1" name="deviceType" cols="85" readonly  ><%=deviceType%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>告警设备名称：</td>
		<td class=column>
           <textarea rows="1" name="sourceName" cols="85" readonly ><%=sourceName%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>告警创建者类型:</td>
		<td class=column>
          <textarea rows="1" name="creatorType" cols="85" readonly ><%=creatorType%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>设备IP：</td>
		<td class=column>
           <textarea rows="1" name="sourceIp" cols="85" readonly ><%=sourceIp%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>属地：</td>
		<td class=column>
          <textarea rows="1" name="gatherId" cols="85" readonly ><%=gather_Id%></textarea></td>
	</tr>
	<tr>
            <td colspan="2" class=column>
            <table align="center" width=50%><tr align=center><td>
            <input type="submit" value="提  交" name="B1" class=jianbian style="width:60px">
            </td>
            <td>
            <input type="reset" value="重  置" name="B2" class=jianbian style="width:60px">
            </td></tr></table>
            </td>
	</tr>

</table>
</form>
<br><br>
<%@include file="../foot.jsp"%>