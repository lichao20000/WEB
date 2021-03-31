<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*,java.util.Map,com.linkage.litms.common.util.*,java.util.ArrayList,com.linkage.litms.common.filter.*" %>

<%
	request.setCharacterEncoding("GBK");
    String searchType = request.getParameter("searchType");
	String city_id = request.getParameter("city_id");	
	String username = request.getParameter("username1");
	String strList = "";
	 Cursor cur = null;
	if(searchType.equals("1")){	    
		if(city_id == null){
			city_id = user.getCityId();
		}
		//获取下级所有属地
        SelectCityFilter scf = new SelectCityFilter();              
        String citys = scf.getAllSubCityIds(city_id,true);
		String sql="select a.customer_id,a.username from tab_egwcustomer a, tab_customerinfo b where a.customer_id=b.customer_id and  a.customer_id='"+username+"' and b.city_id in(" + citys + ")";
		cur = DataSetBean.getCursor(sql);
	}
	    strList = FormUtil.createListBox(cur, "username","username",false,"","");
%>
<html>
<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("idusercount").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>