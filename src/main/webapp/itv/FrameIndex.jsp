<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<%--
/**
 * ITV的首页
 * @author 王志猛(5194)
 * @version 1.0
 * @since 2008-8-14 下午02:42:22
 *
 * 版权：南京联创科技 网管科技部
 *
 */
 --%>
<html>
<head>
<title><%=LipossGlobals.getLipossName()%></title>
</head>
<frameset rows="88px,*" frameBorder="0" frameSpacing="0" id="indexMain">
	<frame id="indexTop" src="<s:url value="/gtms/itv/MenuManager!createModule.action"/>" frameborder="0" scrolling="no"></frame>
	<frameset cols="0px,*" id="indexBottom">
		<frame id="indexTree" src="" frameborder="0" scrolling="no"></frame>
		<frame id="indexContent" name="indexContent" src="<s:url value="/itv/index/index_welcome.jsp"/>" frameborder="0" scrolling="auto"></frame>
	</frameset>
</frameset>
</html>