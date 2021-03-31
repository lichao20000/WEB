<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.Map"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>采集</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/faulttreatment/slide.js"/>"></script>
<link href="<s:url value="/css3/css.css"/>" rel="stylesheet"
	type="text/css" />
	<script type="text/javascript">
	$(document).ready(function(){
		var messageCaiji = '<%=request.getParameter("messageCaiji")%>';
		var caijiArray = messageCaiji.split(",");
		var device_id = caijiArray[0];
		var city_id = caijiArray[1];
		var url = '<s:url value='/inmp/config/preConfigDiag!businessInfo.action'/>'; 
		$.post(url,{
				deviceId:device_id,
				cityId:city_id,
				gw_type:1
		    },function(ajax){
		    	$("body").css("background-color","#FFF");
		    	$(".it_main").html("");
		    	$(".it_main").append(ajax);
		});
	});
	</script>
</head>
<body style="background: #CCC;width="95%";float: right;">
	<div class="it_main" style="text-align: center;" >
		<div>
		<img src="../../images/loadingfault.gif" />
		</div>
		<div style="font-size:15px;color:#0066FF;">
		<span>正在诊断，请稍等....</span>
		</div>
	</div>
</body>
</html>
