<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>

<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>

<%

Map venderMap = DeviceAct.getOUIDevMap();
Map deviceTypeMap = DeviceAct.getDeviceTypeMap();

Cursor cursor = DeviceAct.getDeviceUnit(request);
Map fields = cursor.getNext();
%>
<DIV id="idView">
<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
	<TR>
		<TH nowrap>�豸����</TH>
		<TH nowrap>�ͺ�</TH>
		<TH nowrap>�豸���к�</TH>
		<TH nowrap>�鿴</TH>
	</TR>
	<%
	 if(null==fields)
	 {
		 out.println("<TR align=left><TD class=column colspan=4>ϵͳû���豸��Ϣ!</TD></TR>");
	 }
	 else
	 {
	 	String tmp = "";
		 while(null!=fields)
		 {
			 
			 tmp = (String)fields.get("devicetype_id");
			 String[] aa = (String[])deviceTypeMap.get(tmp);
			 if (aa != null && aa.length==2) {
				 tmp = aa[0];		
			 }
			 out.println("<TR align=center>");
			 out.println("<TD class=column>" + venderMap.get((String)fields.get("vendor_id")) + "("+ (String)fields.get("vendor_id")+")"+"</TD>");
			 out.println("<TD class=column>" + tmp +"</TD>");
			 out.println("<TD class=column>"+ fields.get("device_serialnumber")+"</TD>");
			 
			 out.println("<TD class=column><a href=\"javaScript:showService('" + (String)fields.get("oui") +"','" + fields.get("device_serialnumber") + "','" + fields.get("gw_type")+ "')\">�鿴��ͨҵ��</a></TD>");
			 out.println("</TR>");
			 
			 fields = cursor.getNext();
		 }
	 }
	
	//CLEAR
	fields = null;
	cursor = null;
	%>
</TABLE>
</DIV>
<script language="JavaScript">
parent.idLayer.innerHTML=idView.innerHTML;
parent.document.all("line").style.display ="";
</script>
	


