<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManageIPTV"/>
<%
request.setCharacterEncoding("GBK");

//��ѯ���ݿ�
sheetManage.toExcel(request, response);

%>
<HTML>
<BODY>
<%@ include file="../head.jsp"%>
</BODY>
</HTML>
