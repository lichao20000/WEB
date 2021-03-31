<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<frameset cols="250px,*" frameBorder="3px" f id="bottom">
	<frame id="left" src="<s:url value="/hgwipMgSys/getMenu!getMenu.action"><s:param name="subnet_len"><s:property value="subnet_len"/></s:param></s:url>"
		frameborder="3px" scrolling="yes"></frame>
	<frame id="right" src="<s:url value=""/>" frameborder="0"></frame>
</frameset>
</html>