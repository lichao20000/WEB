<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="areaManage" scope="request" class="com.linkage.litms.system.dbimpl.AreaManageSyb"/>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
request.setCharacterEncoding("GBK");
String gather_id = request.getParameter("gather_id");
String devicetype_id = request.getParameter("devicetype_id");
String area_layer = request.getParameter("area_layer");
String area_pid = request.getParameter("area_pid");
String area_id=request.getParameter("area_id");
String city_id=request.getParameter("city_id");
String ifcontainChild=request.getParameter("ifcontainChild");



//获得父区域范围内设备
Cursor pcursor = null;
//资源类型 1 代表设备
String res_type = "1";


List m_ProcessesList = new ArrayList();
if(!"-1".equals(gather_id))
{
	m_ProcessesList.add(gather_id);
}
String cur_area_id = "" + user.getAreaId();

//modified by w5221 admin.com用户可选择配置的设备是tab_gw_device中的去掉所选择域下的设备
if(user.isAdmin()){
	pcursor = areaManage.getOtherAllDevices(res_type,area_id,m_ProcessesList,devicetype_id,city_id,ifcontainChild);
}else{
	int m_db_i = StringUtils.getDB();
	if(m_db_i == 1) { //sybase
		pcursor = areaManage.getToConfigDevice(res_type,area_id,m_ProcessesList,cur_area_id, devicetype_id,city_id,ifcontainChild);
	} else if (m_db_i == 2) { //oracle
		pcursor = areaManage.getOtherDevicesOfAreaIdNew(res_type,area_id,m_ProcessesList,cur_area_id, devicetype_id,city_id,ifcontainChild);
	}
}

Map pfield = pcursor.getNext();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%> 
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<%@page import="java.util.Map"%>
<HTML>
<HEAD>
<TITLE></TITLE>

</HEAD>

<BODY BGCOLOR="#FFFFFF">
<FORM name=frm >
<SPAN ID="child">
  <select name="source" size="6" style="width:300;height:260" onDblClick="addInfoMath()" multiple>
    <%while(pfield != null){%>
    <option value="<%=pfield.get("device_id")%>"><%=(pfield.get("oui"))+"/"+(pfield.get("device_serialnumber"))%></option>
    <% 
    	pfield = pcursor.getNext();
    }%>
  </select>
</SPAN>
</FORM>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.sourceDevices.innerHTML = child.innerHTML;

//-->
</SCRIPT>

</BODY>
</HTML>
<%
m_ProcessesList.clear();
m_ProcessesList = null;
pfield = null;
pcursor = null;
%>