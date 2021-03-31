<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<form name="gwShare_selectForm">

<TABLE width="100%" class="querytable" align="center">
	<tr><td colspan="4" class="title_1" id="gwShare_thTitle">批 量 导 入</td></tr>
	<tr id="gwShare_tr31" bgcolor="#FFFFFF">
		<td align="right" width="15%">提交文件</td>
		<td colspan="3" width="85%">
			<div id="importUsername">
				<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="20" width="100%">
				</iframe>
				<input type="hidden" name=gwShare_fileName value=""/>
			</div>
		</td>
	</tr>
	<tr id="gwShare_tr32">
		<td CLASS="green_foot" align="right">注意事项</td>
		<td colspan="3" CLASS="green_foot">
		1、需要导入的文件格式限于Excel，即xls格式 。
		 <br>
		2、文件的第一行为标题行，即【箱号】、【SN】、【MAC】、【终端型号】、【备注】。
		 <br>
		3、Excel文件只读取2、3、4、5列数据
		 <br>
		4、文件行数不要超过5000行，以免影响性能。
		</td>
	</tr>
	<tr >
		<td colspan="4" align="right" class="foot" width="100%">
		</td>
	</tr>
</TABLE>
</form>