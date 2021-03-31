<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*,java.util.Map,com.linkage.litms.common.util.*,java.util.ArrayList,com.linkage.litms.common.filter.*" %>

<%
	request.setCharacterEncoding("GBK");
	
	String searchType = request.getParameter("searchType");
	String city_id = request.getParameter("city_id");	
	String user_name = request.getParameter("user_name");
	String loopback_ip = request.getParameter("loopback_ip");
	String strList = "";
	Cursor cur = null;
	if(searchType.equals("1")){
		if(city_id == null){
			city_id = user.getCityId();
		}
		//获取下级所有属地
        SelectCityFilter scf = new SelectCityFilter();  
            
        String citys = scf.getAllSubCityIds(city_id,true);
		
		cur = DataSetBean.getCursor("select user_id,username from cus_radiuscustomer where city_id in(" + citys + ")");
		
	} else if(searchType.equals("0")){
	
		cur = DataSetBean.getCursor("select user_id,username from cus_radiuscustomer where username = '" + user_name + "'");
	} else {		
		cur = DataSetBean.getCursor("select a.username from cus_radiuscustomer a,tab_deviceresource b where a.device_id = b.device_id and b.loopback_ip like '" + loopback_ip + "%'");		
	}
	
	strList = FormUtil.createListBox(cur, "username","username",false,"","");
%>
<html>
<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("iduser").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>