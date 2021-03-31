<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String vendor_id =request.getParameter("vendor_id");	
String vendor_name ="";
String vendor_add ="";
String telephone ="";
String staff_id ="";
String remark ="";
StbVendorAct act = new StbVendorAct();
Map fields = act.getSingleVendorInfo(request);
if(null!=fields)
{
	vendor_name = (String)fields.get("vendor_name");
	vendor_add = (String)fields.get("vendor_add");
	telephone = (String)fields.get("telephone");
	staff_id =(String)fields.get("staff_id");
	remark =(String)fields.get("remark");		
}

%>
<%@page import="com.linkage.litms.resource.StbVendorAct"%>
<%@page import="java.util.Map"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.document.frm.vendor_id.value='<%=vendor_id%>';
parent.document.frm.vendor_id.readOnly =true;
parent.document.frm.vendor_name.value='<%=vendor_name%>';
parent.document.frm.vendor_add.value='<%=vendor_add%>';
parent.document.frm.telephone.value='<%=telephone%>';
parent.document.frm.staff_id.value ='<%=staff_id%>';
parent.document.frm.remark.value ='<%=remark%>';
parent.document.frm.action.value='update';
parent.actLabel.innerHTML = '±à¼­';
parent.VendorLabel.innerHTML = '¡¼<%=vendor_id%>¡½';
//-->
</SCRIPT>