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
	<tr><td colspan="4" class="title_1" id="gwShare_thTitle">�� �� �� ��</td></tr>
	<tr id="gwShare_tr31" bgcolor="#FFFFFF">
		<td align="right" width="15%">�ύ�ļ�</td>
		<td colspan="3" width="85%">
			<div id="importUsername">
				<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="20" width="100%">
				</iframe>
				<input type="hidden" name=gwShare_fileName value=""/>
			</div>
		</td>
	</tr>
	<tr id="gwShare_tr32">
		<td CLASS="green_foot" align="right">ע������</td>
		<td colspan="3" CLASS="green_foot">
		1����Ҫ������ļ���ʽ����Excel����xls��ʽ ��
		 <br>
		2���ļ��ĵ�һ��Ϊ�����У�������š�����SN������MAC�������ն��ͺš�������ע����
		 <br>
		3��Excel�ļ�ֻ��ȡ2��3��4��5������
		 <br>
		4���ļ�������Ҫ����5000�У�����Ӱ�����ܡ�
		</td>
	</tr>
	<tr >
		<td colspan="4" align="right" class="foot" width="100%">
		</td>
	</tr>
</TABLE>
</form>