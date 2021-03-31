<%--
菜单名字。
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
		alert("备份成功");
		// 更新备份表名 
		<%-- parent.frm.tab_name.value='log_' + '<%=acc_oid%>' + "_" + (new Date()).getTime(); --%>
	} else if(msg == -2){
		alert("备份表已存在, 请更改备份表名");
	}
	else if(msg == -3){
		alert("备份数据为空, 不需要备份");
	}
	else {
		alert("操作失败");
	}
//-->
</SCRIPT>