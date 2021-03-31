<%--
Author		: lizhaojun
Date		: 2007-4-20
Note		:
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.netcutover.ServiceAct"%>
<%@ page import="com.linkage.module.gwms.util.StringUtil"%>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<jsp:useBean id="faultCode" scope="request" class="com.linkage.module.gwms.Global"/>
<%
request.setCharacterEncoding("GBK");


//²éÑ¯Êý¾Ý¿â
sheetManage.toExcel(request, response);


%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<HTML>

<BODY>

<%@ include file="../head.jsp"%>

</BODY>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
</HTML>
