<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.TimeZone"%>
<%@ page import="java.util.Calendar"%>			  	  
<%
	request.setCharacterEncoding("GBK");
	String alarmStr = request.getParameter("alarmStr");
  String gatherId=request.getParameter("gatherId");
	int    num=alarmStr.indexOf("@"); 
  String alarmId=alarmStr.substring(0,num);
%>
<%
     Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone("GMT+8:00"));      
     int      year=calendar.get(Calendar.YEAR);    
     int      weekNo= calendar.get(Calendar.WEEK_OF_YEAR)+1;
	   String   postfix, tableName, sql;
	   Map      hashtable;
	   int      k=0;
     while( true){   
          postfix="_"+String.valueOf(year)+"_"+String.valueOf(weekNo);//����event_raw��׺
          tableName="event_raw"+postfix; //���ɱ���
          sql = "select  *  from  "+tableName+"  where serialno='"+alarmId +"'  and subserialno='0'  and gather_id='"+gatherId+"'";
		 // teledb
		 if (DBUtil.GetDB() == 3) {
			 sql = "select displaystring, devicetype, sourceip, creatortype, gather_id, sourcename, createtime " +
					 "from  "+tableName+"  where serialno='"+alarmId +"'  and subserialno='0'  and gather_id='"+gatherId+"'";
		 }
		 PrepareSQL psql = new PrepareSQL(sql);
		 psql.getSQL();
          hashtable = DataSetBean.getRecord(sql);
	     if( hashtable!=null)
		    break;
	     else{	 
     	    if( ++k==5) //�������Ŀ����5 ʱ�� �˳���ѯ�澯
            	break ;
        	weekNo=weekNo-1;//���ż�1        	
        	if( weekNo==0) 
        	{
        	   year=year-1;                   //����һ���еĵ�һ�ܣ��Ǿͱ�����ݼ�һ                                                                   
             calendar.set(year,11,30);      //�����������ڸ�ʽ
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
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<script language="javascript"><!--
	function  trim(str)
 {
   for(var  i  =  0  ;  i<str.length  &&  str.charAt(i)==" "  ;  i++  )  ;
   for(var  j  =str.length;  j>0  &&  str.charAt(j-1)==" "  ;  j--)  ;
   if(i>j)  return  "";  
   return  str.substring(i,j);  
 }
function checkForm(){
    var obj=document.frm;
    var str=obj.subject.value;
        str=trim(str);
    if(str.length==0)  {
	    alert("����澯����");
	    obj.subject.focus();
		  return false;
	   }	
	  str=obj.warnReason.value;
    str=trim(str);
    if(str.length==0)  {
	    alert("����澯ԭ��");
	    obj.warnReason.focus();
		  return false;
	   }
	  str=obj.warnResove.value;
    str=trim(str);
    if(str.length==0)  {
	    alert("����澯�������");
	    obj.warnResove.focus();
		  return false;
	  }
		return true;  
}
//--></script>
</head>
<body>
<br>
<img src="../system/images/attention.gif"> <font color="red">ע�⣺ֻ���ύ�澯������Ϣ����ȷ�ϸ澯</font>
<form name="frm" method="POST"action="../Knowledge/ackSave.jsp?ack_CreateTime=<%=time%>&alarmString=<%=alarmStr%>&tableName=<%=tableName%>&alarmId=<%=alarmId%>" onsubmit="javascript:return checkForm();">
<table align="center" border=0 cellspacing=1 cellpadding=2 width="70%" bgcolor="#000000">
	<tr>
		<td class=column width=30% align=right>����:<font color="#FF0000">*</font></td>
		<td class=column><textarea rows="2" name="subject" cols="85"><%=subject%></textarea></td>
	</tr>
	
	<tr>
		<td class=column align=right>�澯ԭ��:<font color="#FF0000">*</font></td>
		<td class=column><textarea rows="3" name="warnReason" cols="85"><%=warnReason%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>�������:<font color="#FF0000">*</font></td>
		<td class=column><textarea rows="4" name="warnResove" cols="85"><%=warnResove%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>���ݣ�</td>
		<td class=column><textarea rows="3" name="content" cols="85" readonly ><%=content%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>�澯����ʱ�䣺</td>
		<td class=column><textarea rows="1" name="createTime" cols="85" readonly  ><%=createTime%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>�豸���ͣ�</td>
		<td class=column><textarea rows="1" name="deviceType" cols="85" readonly  ><%=deviceType%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>�澯�豸���ƣ�</td>
		<td class=column><textarea rows="1" name="sourceName" cols="85" readonly ><%=sourceName%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>�澯����������:</td>
		<td class=column><textarea rows="1" name="creatorType" cols="85" readonly ><%=creatorType%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>�豸IP��</td>
		<td class=column><textarea rows="1" name="sourceIp" cols="85" readonly ><%=sourceIp%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>���أ�</td>
		<td class=column><textarea rows="1" name="gatherId" cols="85" readonly ><%=gather_Id%></textarea></td>
	</tr>
	<tr>
            <td colspan="2" class=column>
            <table align="center" width=50%><tr align=center><td>
            <input type="submit" value="��  ��" name="B1" class=jianbian style="width:60px">
            </td>
            <td>
            <input type="reset" value="��  ��" name="B2" class=jianbian style="width:60px">
            </td></tr></table>
            </td>
	</tr>
</table>
</form>
<br><br>
</body>
</html>