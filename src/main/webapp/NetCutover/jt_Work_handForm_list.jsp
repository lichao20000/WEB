<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />

<%
	request.setCharacterEncoding("GBK");
	String serv_type_id = request.getParameter("serv_type_id");
	if (serv_type_id == null || serv_type_id.equals("")) {
		serv_type_id = "0";
	}
	String operTypeList = HGWUserInfoAct.getOperTypeList(Integer.parseInt(serv_type_id));
%>
<script language="javascript">
var operTypeList = "<%=operTypeList%>";
//alert(operTypeList);
window.parent.document.forms[0].oper_type.outerHTML = operTypeList;

</script>
