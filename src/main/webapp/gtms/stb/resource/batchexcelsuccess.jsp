<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
	</head>
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					您当前的位置：批量导入业务帐号版本升级
				</TD>
			</TR>
		</TABLE>
		<br>
		<s:form action="stbSoftUpgrade!execUpload.action" method="post" enctype="multipart/form-data" name="batchexform">
			<input type="hidden" name="vendorPathId" value=""/>
			<table class="querytable" width="98%" align="center">
				<tr>
					<td colspan="4" class="title_1">
						批量导入升级任务结果
					</td>
				</tr>
				<tr>
					<td class="title_2" align="center" width="15%">
						导入结果
					</td>
					<td width="85%" colspan="3">
						<s:property value="ajax"/>
					</td>
				</tr>
			</table>
		</s:form>
	</body>