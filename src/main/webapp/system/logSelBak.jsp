<%--
�˵����֡�
Linkage Technology (NanJing) Co., Ltd
Copyright 2008-2012. All right reserved.
Author: Alex.Yan(yanhj@lianchuang.com)
Version: 1.0.0
Date: 2009-7-8
Desc: .
--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@page import="com.linkage.litms.system.dbimpl.LogItem"%>
<%@ include file="../timelater.jsp"%>

<%
long acc_oid = user.getId();

request.setCharacterEncoding("GBK");

int strMsg = LogItem.getInstance().bakLog(request);
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
	var msg = '<%=strMsg%>';
	if (msg > 0) {
		alert("���ݳɹ�");
		// ���±��ݱ��� 
		<%-- parent.frm.tab_name.value='log_' + '<%=acc_oid%>' + "_" + (new Date()).getTime(); --%>
	} else if(msg == -2){
		alert("���ݱ��Ѵ���, ����ı��ݱ���");
	}
	else if(msg == -3){
		alert("��������Ϊ��, ����Ҫ����");
	}
	else {
		alert("����ʧ��");
	}
//-->
</SCRIPT>