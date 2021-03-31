<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * 该jsp首页是为了安全网关使用的
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2008-03-26
 * @category security
 * 
 * 
 */
--%>
<html>
	<frameset id="main" rows="73px,*" id="top" frameBorder="0"
		frameSpacing="0">
		<frame id="top" noResize src="<s:url value="/model_vip/Top!securitygw.action"/>"
			scrolling="no"></frame>
		<frameset cols="215px,15px,*" frameBorder="0" frameSpacing="0"
			id="bottom">
			<frame id="leftbottom"
				src="<s:url value="/model_vip/SGWMenu.action"/>" frameborder="0"
				scrolling="yes"></frame>
			<frame id="ect" noResize
				src="<s:url value="/model_vip/index/ect.jsp"/>" scrolling="no"
				frameborder="0"></frame>
			<frame id="rightbottom" noResize
				src="<s:url value="/model_vip/Goto.action"/>"
				frameborder="0"></frame>
		</frameset>
	</frameset>
</html>