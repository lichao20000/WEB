<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�ֻ���Ϣ����</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
function insert()
{
	var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
	var url="<s:url value='/gwms/blocTest/BatchAdditionPhone!insert.action'/> ";
	if(""==gwShare_fileName){
		alert("�����ϴ��ļ�!");
		return;
	}
	$.post(url, {
		gwShare_fileName:gwShare_fileName
	}, function(ajax) {
		if (ajax > 0) {
			alert("����ɹ�!");
			return;
		}else if(ajax=='-100'){
			alert("����ʧ��!�ļ�������൥�ε������������ݣ�");
		}else{
			alert("����ʧ��!");
		}
	});
}
</SCRIPT>

</head>
<form name="Form" action="" target="dataForm">
<TABLE width="100%" class="querytable" align="center">
	<tr>
		<td colspan="4" class="title_1" id="">�ύ�ļ�</td>
	</tr>
	<tr id="gwShare_tr31" bgcolor="#FFFFFF"  >
		<td align="right" width="15%">ע������</td>
		<td colspan="3" width="85%">
			<div id="importUsername">
				<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="20" width="100%">
				</iframe>
				<input type="hidden" name=gwShare_fileName value=""/>
			</div>
		</td>
	</tr>
	<tr id="gwShare_tr32" >
		<td CLASS="green_foot" align="right">ע������</td>
		<td colspan="3" CLASS="green_foot">
			1����Ҫ������ļ���ʽ���ֻ��ƶ��ն�ΪExcel�ļ�����xls��
		 	<br>
			2���ն��ļ�Ϊtxt�ļ�,txt��ʽ�� 
			<br>
			3���ļ�������Ҫ̫�࣬����Ӱ�����ܡ�
			<br>
		</td>
	</tr>
	<tr >
		<td colspan="4" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:insert();" name="gwShare_queryButton" style="CURSOR:hand"  >����</button>
			</div>
		</td>
	</tr>
</TABLE>
</form>