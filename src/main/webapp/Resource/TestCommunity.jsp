<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.corba.WebCorba"%>
<%@page import="Performance.PerformanceManager"%>
<%@page import="Performance.Data"%>

<%
request.setCharacterEncoding("GBK");

String ip = request.getParameter("ip");
String gather_id = request.getParameter("gather_id");
String snmp_ro_community = request.getParameter("snmp_ro_community");
String snmp_rw_community = request.getParameter("snmp_rw_community");


Data ro[] = null;
Data rw[] = null;
WebCorba corba = null;



PerformanceManager performanceManager = null;

try
{

corba = new WebCorba("SnmpGather", gather_id, "1");

performanceManager = (PerformanceManager) corba.getIDLCorba("SnmpGather");

if(null!=performanceManager){
	try{
		ro = performanceManager.VerifyReadCommunity(ip,snmp_ro_community);
		if(null!=snmp_rw_community&&!"".equals(snmp_rw_community))
			rw = performanceManager.VerifyReadCommunity(ip,snmp_rw_community);
	}
	catch(Exception e){
		try{	
			corba.refreshCorba("c");
			performanceManager = (PerformanceManager)corba.getIDLCorba("SnmpGather");
			ro = performanceManager.VerifyReadCommunity(ip,snmp_ro_community);
			if(null!=snmp_rw_community&&!"".equals(snmp_rw_community))
				rw = performanceManager.VerifyReadCommunity(ip,snmp_rw_community);
		}catch(Exception e1){
			e1.printStackTrace();
		}
	}
  }
}
catch(Exception e)
{
	e.printStackTrace();
}
if(null!=snmp_rw_community&&!"".equals(snmp_rw_community)){
	if (ro!=null&&null!=rw){
		//1:¶ÁÐ´¿ÚÁî½Ô²»¿É¶Á;2¶Á¿ÚÁî×¼È·,Ð´¿ÚÁî²»¿É¶Á;3Ð´¿ÚÁî¿É¶Á,¶Á¿ÚÁî´íÎó;4¶ÁÐ´¿ÚÁî½Ô¿É¶Á
		if("NODATA".equals(ro[0].dataStr)&&"NODATA".equals(rw[0].dataStr))
			out.println("1");
		else if(!"".equals(ro[0].dataStr)&&"NODATA".equals(rw[0].dataStr))
			out.println("2");
		else if(!"".equals(rw[0].dataStr)&&"NODATA".equals(ro[0].dataStr))
			out.println("3");
		else if(!"".equals(rw[0].dataStr)&&!"".equals(ro[0].dataStr))
			out.println("4");
	}else out.println("fail");
}else{
	if (ro!=null){
		//5:¶Á¿Ú¿É¶Á;6¶Á¿ÚÁî´íÎó
		if("NODATA".equals(ro[0].dataStr))
			out.println("6");
		else if(!"".equals(ro[0].dataStr))
			out.println("5");
	}else out.println("fail");
}


%>

