<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@ page import="com.linkage.litms.common.util.StringUtils"%>
<%@ page import="com.linkage.litms.performance.GeneralNetPerf"%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<%
   String hidstart= request.getParameter("hidstart");
   Cursor cursor = GeneralNetPerf.getAllDeviceClassData(request);
   Map fields = cursor.getNext();
   String device_id="";
   String device_name="";
   String loopback_ip="";
   String class1="";   
   String unit="";
   String descr="";
   int index=0;
   if(null!=fields)
   {
	   int num =cursor.getRecordSize();
	   out.println("parent.doMrtg('"+num+"');");
	   while(null!=fields)
	   {
		   device_id =(String)fields.get("device_id");
		   device_name=(String)fields.get("device_name");
		   loopback_ip=(String)fields.get("loopback_ip");
		   class1=(String)fields.get("class1");
		   unit=(String)fields.get("unit");
		   descr=(String)fields.get("descr");
		   out.println("parent.CreateMrgtAct('"+index+"','"+device_id+"','"+device_name+"','"+loopback_ip+"','"+class1+"','"+hidstart+"','"+descr+"','"+unit+"');");
		   
		   index++;
		   fields = cursor.getNext();		   
	   }
   }
   else
   {
	   //out.println("parent.doMrtg('1');");
	   out.println("parent.showNOData();");	   
   } 
   
   //clear
   fields =null;
   cursor = null;
%>
</SCRIPT>