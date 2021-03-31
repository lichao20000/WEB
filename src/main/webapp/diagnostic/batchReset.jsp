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
<title>批量恢复出厂设置</title>
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
			alert("请先上传文件！");
			return;
		}
		var url = "<s:url value="/gwms/resource/BatchReset!query.action"/>?gwShare_fileName=" + gwShare_fileName;
		$.post(url, {}, function(ajax) {
			if (ajax > 0) {
				alert("已交给后台处理!");
				return;
			} else if (ajax == -5) {
				var url = "<s:url value="/gwms/resource/BatchReset!fileRow.action"/>";
				$.post(url, {}, function(ajax) {
					alert("导入文件行数超过" + ajax + "行,请重新导入!");
				});
				//alert("导入文件行数超过5000行,请重新导入!");
			} else if (ajax == -6) {
				alert("今日导入数量已达上限!");
			} else if (ajax == -9) {
				alert("文件第一行必须为设备序列号!");
			} else if(ajax ==-10000)
			{
				alert("导入失败!");
			}
			else if (ajax == -7) {
				var url = "<s:url value="/gwms/resource/BatchRestart!test.action"/>";
				$.post(url, {}, function(ajax) {
					alert("今日只可以在导入" + ajax + "条数据!");
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
						<td width="162" align="center" class="title_bigwhite">批量恢复出厂设置</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table class="querytable">
					<TR>
						<th colspan="4">导入文件</th>
					</tr>
					<TR>
						<TD colspan="2" class=column width="15%" align='right'>提交文件：</td>
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
						<TD colspan="2" class=column width="15%" align='right'>注意事项：</TD>
						<TD colspan="2"><font color="#7f9db9">1、需要导入的文件格式限于Excel、文本文件，即xls、txt格式
								。 <br> 2、文件的第一行为标题行，即【设备序列号】。 <br>
								3、文件行数最大不得超过五千行(包括标题行)。
						</font></TD>
					</TR>
					<TR>
						<td colspan="4" align="right" class=foot><button onclick="do_query()" 
							name="gwShare_queryButton" >添加至设备恢复出厂设置队列</button> &nbsp;&nbsp;&nbsp;
						<button onclick="getExcelTemplate('1')" >点击下载txt模板</button>&nbsp;&nbsp;&nbsp;
						<button onclick="getExcelTemplate('0')" >点击下载xls模板</button>&nbsp;&nbsp;&nbsp;
							</td>
						</TR>
						</table>
			</td>
			
		</tr>
	</table>
</form>