<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.system.*"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����ָ���������</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
	function do_query() {
		var width = 800;
		var height = 450;
		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
		if ("" == gwShare_fileName) {
			alert("�����ϴ��ļ���");
			return;
		}
		var url = "<s:url value="/gwms/resource/BatchReset!query.action"/>?gwShare_fileName=" + gwShare_fileName;
		$.post(url, {}, function(ajax) {
			if (ajax > 0) {
				alert("�ѽ�����̨����!");
				return;
			} else if (ajax == -5) {
				var url = "<s:url value="/gwms/resource/BatchReset!fileRow.action"/>";
				$.post(url, {}, function(ajax) {
					alert("�����ļ���������" + ajax + "��,�����µ���!");
				});
				//alert("�����ļ���������5000��,�����µ���!");
			} else if (ajax == -6) {
				alert("���յ��������Ѵ�����!");
			} else if (ajax == -9) {
				alert("�ļ���һ�б���Ϊ�豸���к�!");
			} else if(ajax ==-10000)
			{
				alert("����ʧ��!");
			}
			else if (ajax == -7) {
				var url = "<s:url value="/gwms/resource/BatchRestart!test.action"/>";
				$.post(url, {}, function(ajax) {
					alert("����ֻ�����ڵ���" + ajax + "������!");
				});
			}
		});
	}
	function getExcelTemplate(id) {
		var url = "<s:url value='/gwms/resource/BatchReset!downModle.action'/>"
				+ "?caseDownload=" + id;
		window.open(url);
		/* document.getElementById("Form").action = url;
		document.getElementById("Form").submit(); */
	}
</SCRIPT>
</head>
<form name="Form" action="" target="selectForm">
	<table>
		<tr>
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">�����ָ���������</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table class="querytable">
					<TR>
						<th colspan="4">�����ļ�</th>
					</tr>
					<TR>
						<TD colspan="2" class=column width="15%" align='right'>�ύ�ļ���</td>
						<td colspan="2" align="center">
							<div id="importUsername">
								<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO
									src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="20"
									width="100%"> </iframe>
								<input type="hidden" name=gwShare_fileName value="" />
							</div>
						</td>
					</TR>
					<TR>
						<TD colspan="2" class=column width="15%" align='right'>ע�����</TD>
						<TD colspan="2"><font color="#7f9db9">1����Ҫ������ļ���ʽ����Excel���ı��ļ�����xls��txt��ʽ
								�� <br> 2���ļ��ĵ�һ��Ϊ�����У������豸���кš��� <br>
								3���ļ�������󲻵ó�����ǧ��(����������)��
						</font></TD>
					</TR>
					<TR>
						<td colspan="4" align="right" class=foot><button onclick="do_query()" 
							name="gwShare_queryButton" >������豸�ָ��������ö���</button> &nbsp;&nbsp;&nbsp;
						<button onclick="getExcelTemplate('1')" >�������txtģ��</button>&nbsp;&nbsp;&nbsp;
						<button onclick="getExcelTemplate('0')" >�������xlsģ��</button>&nbsp;&nbsp;&nbsp;
							</td>
						</TR>
						</table>
			</td>
			
		</tr>
	</table>
</form>