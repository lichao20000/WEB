<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String vendor_id ="";
String device_model_id =""; 
String device_model = "";
String SpecVersion ="";
String HardwareVersion ="";
String SoftwareVersion ="";
String area_id ="";
String area_desc = "";
String devicetype_id =request.getParameter("devicetype_id");	
DeviceAct act = new DeviceAct();
Map fields = act.getDeviceTypeInfoByID(request);
Map areaMap = act.getAreaIdMapName();

if(null!=fields)
{
	//vendor_id = (String)fields.get("oui");
	vendor_id = (String)fields.get("vendor_id");
	//manufacturer = (String)fields.get("vendor_name");
	device_model_id = (String)fields.get("device_model_id");
	//device_model = (String)fields.get("device_model");
	SpecVersion = (String)fields.get("specversion");
	HardwareVersion =(String)fields.get("hardwareversion");
	SoftwareVersion =(String)fields.get("softwareversion");
	area_id =(String)fields.get("area_id");		
}

if (area_id == null || "".equals(area_id)){
	area_id = "-1";
}
else{
	area_desc = (String)areaMap.get(area_id);
}

%>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.document.frm.devicetype_id.value='<%=devicetype_id%>';
parent.document.frm.vendor_id.value='<%=vendor_id%>';

parent.showChild("vendor_id",'<%=device_model_id%>');

parent.document.frm.SpecVersion.value='<%=SpecVersion%>';
parent.document.frm.HardwareVersion.value ='<%=HardwareVersion%>';
parent.document.frm.SoftwareVersion.value ='<%=SoftwareVersion%>';
parent.document.frm.area_id.value ='<%=area_id%>';
parent.document.frm.area_name.value ='<%=area_desc%>';
parent.document.frm.action.value='update';
parent.actLabel.innerHTML = '±à¼­';
parent.DeviceTypeLabel.innerHTML = '¡¼<%=devicetype_id%>¡½';

//-->
</SCRIPT>