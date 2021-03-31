<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>原始手工工单查询</title>
<%
	/**
	 * 原始手工工单查询
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2013-05-15
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		var username = $("input[@name='username']").val().trim();
		/* var ACCOUNT_NAME = $("input[@name='ACCOUNT_NAME']").val().trim();
		var SERIAL_NUMBER = $("input[@name='SERIAL_NUMBER']").val().trim(); */
		if(username==""){
			alert("请输入查询条件");
			return false;
		}
		document.selectForm.submit();
	}
	
	//** iframe自动适应页面 **//
	//输入你希望根据页面高度自动调整高度的iframe的名称的列表
	//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
	//定义iframe的ID
	var iframeids = [ "dataForm" ]

	//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
	var iframehide = "yes"

	function dyniframesize() {
		var dyniframe = new Array()
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//自动调整iframe高度
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block"
					//如果用户的浏览器是NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//如果用户的浏览器是IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
				}
			}
			//根据设定的参数来处理不支持iframe的浏览器的显示问题
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block"
			}
		}
	}

	$(function() {
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/itms/service/operateByHistoryCQOldQuery!getOperateByHistoryInfo.action'/>"
		target="dataForm">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								原始工单查询</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> 查询原始工单信息情况</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">原始工单查询</th>
						</TR>
						<TR>
								<!-- <SELECT  name="usernameType">
										<option value="1" selected="selected">
											LOID
										</option>
										<option value="2">
											宽带账号
											
										</option>
									</SELECT> -->
								<TD width='15%' class=column align="right" >LOID</TD>
							<TD width="85%" colspan="3"><input type="text" name="username" size="20"
								maxlength="30" class=bk /></TD>
						 	<!-- <TD width='15%' class=column align="right" >宽带账号</TD>
							<TD width="35%"><INPUT type="text" name="ACCOUNT_NAME" maxlength=20 class=bk
								value=""></TD> -->
						</TR>
						
						<!-- <TR>
							<TD width='15%' class=column align="right" >设备序列号</TD>
							<TD width="85%" colspan="3"><INPUT type="text" name="SERIAL_NUMBER" maxlength=20 class=bk
								value=""></TD>
						</TR> -->
						
						
						<%-- <TR>
							<TD class=column width="15%" align='right'>开始时间</TD>
							<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
										&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
							</TD>
							<TD class=column width="15%" align='right'>结束时间</TD>
							<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
										&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
							</TD>
						</TR> --%>

						
						<TR>
							<td colspan="4" align="right" class=foot>
								<input type="hidden" name="gw_type" id="gw_type" value="1" />
								<button onclick="javaScript:query();" >&nbsp;查&nbsp;&nbsp;询&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr>
				<td><iframe id="dataForm" name="dataForm" height="0"
						frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>
</body>
</html>