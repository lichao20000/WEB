<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%@ page import="com.linkage.litms.common.util.FormUtil" %>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%-- 
 * 创建日期 2007-3-6
 * Administrator liuli
--%>
<%
request.setCharacterEncoding("GBK");
String strSQL  = "";
String strv_id = request.getParameter("resource_type_id");
String Mycheckbox1 = request.getParameter("Mycheckbox1");
String strChildList = "";
//----------------------属地过滤 add by liuli 2007-3-23---------------
HttpSession session1 = request.getSession();
UserRes curUser1 = (UserRes) session1.getAttribute("curUser");
String city_id1 = curUser1.getCityId();
String m_CityIdsStr = StringUtils.weave(CityDAO.getAllNextCityIdsByCityPid(city_id1));
//-------------------------------------------------------------------
Cursor cursor;
strSQL = "select distinct a.resource_type_id, a.resource_name,b.device_model from tab_resourcetype a , tab_deviceresource b where a.resource_type_id=b.resource_type_id ";
 if(!user.isAdmin()){
		List m_ProcessesList = curUser.getUserProcesses();
		for(int i=0;i<m_ProcessesList.size();i++){
			String Gether_id = (String)m_ProcessesList.get(i);			 
			strSQL += " and b.gather_id like '%" + Gether_id+"%' ";
		}
      }


if(strv_id!=null)
	strSQL += " and a.resource_type_id=" + strv_id + "";
    cursor = DataSetBean.getCursor(strSQL);
    strChildList = FormUtil.createListBox(cursor, "device_model" ,"device_model", true, "", "");  

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> 取得告警属性 </TITLE>
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="dolphin">
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb_2312-80">
<META NAME="Description" CONTENT="取得子串">
</HEAD>

<BODY BGCOLOR="#FFFFFF">
<FORM name=frm >
<SPAN ID="child"><%=strChildList%></SPAN>
</FORM>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.document.all("devicelist").innerHTML = child.innerHTML;
//-->
</SCRIPT>

</BODY>
</HTML>