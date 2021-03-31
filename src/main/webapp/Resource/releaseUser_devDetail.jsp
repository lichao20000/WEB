<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.Map,com.linkage.litms.common.database.*,com.linkage.litms.common.util.*"%>

<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%
request.setCharacterEncoding("GBK");

Map citymap = CommonMap.getCityMap();
String device_serialnumber = request.getParameter("device_serialnumber");
String gw_type = request.getParameter("gw_type");
UserRes userRes = (UserRes) request.getSession().getAttribute("curUser");
String cityId = userRes.getCityId();
String sql = "";

if ("2".equals(gw_type)) {
	//直接根据用户表中信息判断此用户是否已绑定，而不是关联设备表查询 GSJ
	sql = "select a.username, a.device_serialnumber, a.device_id, b.city_id from tab_egwcustomer a , tab_customerinfo b where a.customer_id=b.customer_id and a.user_state in ('1','2') ";

	if(!user.isAdmin()){
		sql += " and b.city_id in (" + StringUtils.weave(CityDAO.getAllNextCityIdsByCityPid(cityId)) + ",'')";
	}
	sql +=" and a.device_serialnumber like '%" + device_serialnumber + "'";
	
} else {
	sql = "select a.username, a.device_serialnumber, a.device_id, b.city_id from tab_hgwcustomer a, tab_gw_device b where a.user_state in ('1','2') "
		  + " and a.device_id=b.device_id ";
	if(!user.isAdmin()){
		sql += " and b.city_id in (" + StringUtils.weave(CityDAO.getAllNextCityIdsByCityPid(cityId)) + ",'')";
	}
	sql +=" and b.device_serialnumber like '%" + device_serialnumber + "'";
}



//String sql = "select a.* ,b.username from tab_gw_device a, " + tablename +" b";
//if(!user.isAdmin()){
//	sql +=",tab_gw_res_area c ";
//}
//sql +=" where a.cpe_allocatedstatus=1 and b.user_state in ('1','2') ";
			
//if(!user.isAdmin()){
//	sql +=" and a.device_id=c.res_id and c.res_type=1 and c.area_id=" + user.getAreaId();
//}	

//sql += " and a.oui=b.oui and a.device_serialnumber = b.device_serialnumber and a.gw_type=" + gw_type;

//sql +=" and a.device_serialnumber like '%" + device_serialnumber + "'  order by a.device_serialnumber";



Cursor cur = DataSetBean.getCursor(sql);

Map fields = cur.getNext();

String flag = "1";

if(fields == null){
	flag = "-1";
%>
<SCRIPT LANGUAGE="JavaScript">	
	alert("没有设备和用户的绑定关系!");
</SCRIPT>

<%
} 
%>
<link rel="stylesheet" href="../css/listview.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript">

</SCRIPT>
<div id="idBody">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" >
	  <TR><TD bgcolor=#999999>
		  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="idTable">
			<TR bgcolor="#ffffff">
			  <TH align="right" width="5%">选择</TH>
			  <TH align="right" width="20%">属地</TH>
			  <TH align="right" width="25%">绑定用户</TH>
			  <TH align="right" width="50%">设备序列号</TH>	
			</TR>
			<% 
			if(fields != null){		
				while(fields != null){	
			%>
			<TR bgcolor="#ffffff">
			  <TD width="10%" align="center"><input type="checkbox" name="userDev" id="userDev" username="<%=fields.get("username")%>"  value="<%=fields.get("device_id")%>"></TD>			  
			  <TD width="20%" align="center"><%=citymap.get((String)fields.get("city_id"))%></TD>
			  <TD width="25%" align="center"><%=fields.get("username")%></TD>
			  <TD width="45%" align="center"><%=fields.get("device_serialnumber")%></TD>	
			</TR>			
			<%
					fields = cur.getNext();
				}			
			}
			 %>
			<TR bgcolor="#ffffff">
			  <TD width="" align="right" colspan="4"><input type="button" name="userDev" value=" 解 绑 " onclick="doRelease('<%= gw_type%>');"></TD>		  
			</TR>			
		  </TABLE>
	  </TD></TR>
	</TABLE>
</TD></TR>
</TABLE>
</div>
<SCRIPT LANGUAGE="JavaScript">
<!--
var flag = "<%=flag%>";
if(flag == 1){
	parent.document.all("idBody").innerHTML = idBody.innerHTML;
}
//-->
</SCRIPT>
