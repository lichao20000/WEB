<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<link href="<s:url value="/css3/css_s.css"/>" rel="stylesheet"
	type="text/css" />
<meta http-equiv="x-ua-compatible" content="IE=8" >
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script> 
<script type="text/javascript" src="<s:url value="/Js/slide.js"/>"></script>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../../Js/echarts-all.js"></script>
<script type="text/javascript">
	function searchTypeSel(searchType){
		if(searchType.value == -1){
			alert("��ѡ���ѯ��ʽ");
		}
		var url = "<s:url value='/itms/resource/devRepairTestInfo!qualifiedRateType.action'/>";
		$.post(url, {
			searchType:searchType.value
		}, function(ajax) {
			$("div[@id='it_main']").html("");
			$("div[@id='it_main']").append(ajax);
		});
	}
</script>
<title>�����ն˺ϸ���ͳ��</title>
</head>
<body>
	<div class="it_main" id="it_main">
		<h1 class="it_webtt">�����ն˺ϸ���ͳ��</h1>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="mainSearch">
			<tr>
			    <td class="tit" align="left" width="1%" style="text-align: left;">��ѯ��ʽ </td>
			    <td align="center" width="17%" style="text-align: left;">
			    	<select class="gj_select" name="searchType" onchange="searchTypeSel(this);">
			    		<option value="-1">��ѡ���ѯ��ʽ</option>
			    		<option value="1">��ά�޳���ͳ��</option>
			    		<option value="2">������ͳ��</option>
			    	</select>
			    </td>
			</tr>
		</table>	
	</div>
</body>
</html>