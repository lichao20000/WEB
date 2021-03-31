<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<form name="frm" action="<s:url value="/wlan/hotspot.action"/>">
<frameset rows="30%,70%" border="0">
	<frame name="searchForm" src="<s:url value="/bbms/CustomerInfo!queryForm.action"/>">
	<frame name="dataForm" src="<s:url value="/bbms/CustomerInfo!queryData.action"/>">
</frameset>
</form>
</html>
