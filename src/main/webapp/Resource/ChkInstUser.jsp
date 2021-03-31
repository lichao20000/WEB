<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%@page import="java.util.Map"%>

<%
	String username = request.getParameter("username");
	String gwType = request.getParameter("gwType");
	String tabName = "", chku = "";
	if(gwType != null && "2".equals(gwType)){
		tabName = "tab_egwcustomer";
	}else{
		tabName = "tab_hgwcustomer";
	}
	String sql = "select username from " + tabName 
				+ " where username='" + username
				+ "'";
	Map map = DataSetBean.getRecord(sql);
	if(map != null && map.get("username") != null && !"".equals(map.get("username"))){
		chku = "1";
	}else{
		chku = "0";
	}

%>

<script language="javascript">

	var uname = "<%=chku%>";
	parent.frm.ckuser.value = uname;

</script>
