<%--
vlan����
Author: ��ɭ��
Version: 1.0.0
Date: 2009-10-29
--%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>ҵ�����ò���ִ�н��</title>
		<link rel="stylesheet" href="<s:url value='/css/inmp/css/css_blue.css'/>"
			type="text/css">
		<link rel="stylesheet" href="<s:url value='/css/inmp/css/css_green.css'/>"
			type="text/css">

	</head>

	<body>

		<table border=0 cellspacing=1 cellpadding=1 width="100%"
			align="center" bgcolor="#999999">
			<TR>
				<TH colspan="5">
					<span style="float: right"><a href="#"
						onclick="refresh('<s:property value="servStrategyMap.strategyId" />')"><img
								src="<s:url value="../../images/inmp/refresh.png" />" border="0" alt="ˢ��"></a>&nbsp;&nbsp; </span> �������

				</TH>
			</TR>
			<tr class="green_title2">
				<td width="15%">
					����ID
				</td>
				<td width="15%">
					����״̬
				</td>
				<td width="15%">
					����ִ�н��
				</td>
				<td width="27.5%">
					��ʼʱ��
				</td>
				<td width="27.5%">
					����ʱ��
				</td>
			</tr>

			<tr bgcolor="#FFFFFF" height="25">
				<s:if test="servStrategyMap!=null">
					<td align="center">
						<s:property value="servStrategyMap.strategyId" />
					</td>
					<td align="center">
						<s:property value="servStrategyMap.status" />
					</td>
					<td align="center">
						<s:property value="servStrategyMap.result" />
					</td>
					<td align="center">
						<s:property value="servStrategyMap.startTime" />
					</td>
					<td align="center">
						<s:property value="servStrategyMap.endTime" />
					</td>
				</s:if>
				<s:else>
					<td colspan="5">
						<font color="red">û�д˲��Ե���Ϣ</font>
					</td>
				</s:else>
			</tr>


		</table>

	</body>
</html>

<SCRIPT LANGUAGE="JavaScript">

function refresh(strategyId){
	var url = "<s:url value='/inmp/servStrategy/ServStrategy!getStrategy.action'/>";
	$.post(url,{
		strategyId:strategyId
	},function(ajax){
	  	$("div[@id='div_strategy']").html("");
		$("div[@id='div_strategy']").append(ajax);
	});	
	document.getElementById("tr002").style.display = "";
	//parent.dyniframesize();
}
</SCRIPT>