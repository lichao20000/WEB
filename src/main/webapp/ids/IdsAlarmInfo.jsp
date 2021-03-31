<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>预检预修告警信息</title>
<%
	/**
	 *  预检预修告警信息
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2014-02-18
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		document.selectForm.submit();
	}

	function ToExcel() {
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/ids/IdsAlarmInfo!getIdsarmInfoListExcel.action' />";
		mainForm.submit();
		mainForm.action = "<s:url value='/ids/IdsAlarmInfo!getIdsarmInfoList.action' />";
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

<body >
	<form id="form" name="selectForm"
		action="<s:url value='/ids/IdsAlarmInfo!getIdsarmInfoList.action'/>"
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
								预检预修告警信息</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">预检预修告警信息设计查询条件</th>
						</tr>

						<TR>
							<TD class=column width="15%" align='right'>开始时间</TD>
							<TD width="35%"><input type="text" name="startOpenDate"
								readonly class=bk value="<s:property value="startOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../images/dateButton.png" width="15" height="12" border="0"
								alt="选择"></TD>
							<TD class=column width="15%" align='right'>结束时间</TD>
							<TD width="35%"><input type="text" name="endOpenDate"
								readonly class=bk value="<s:property value="endOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../images/dateButton.png" width="15" height="12" border="0"
								alt="选择"></TD>
						</TR>
						<TR>
							<TD class="column" width='15%' align="right">设备序列号</TD>
							<TD width="35%"><input type="text" name="device_id"
								size="20" maxlength="30" class=bk /></TD>

							<TD class="column" width='15%' align="right">LOID</TD>
							<TD width='35%' align="left"><input type="text" name="loid"
								size="20" maxlength="30" class=bk /></TD>
						</TR>

						<TR>

							<TD class="column" width='15%' align="right">告警次数</TD>
							<TD width="35%"><input type="text" name="alarm_count"
								size="20" maxlength="30" class=bk /></TD>
							<TD class="column" width='15%' align="right">告警代码</td>
							<TD width="35%"><select name="alarm_code" class="bk">
									<option value="" selected="selected">==请选择==</option>
									<%--
									<option value="1001">语音注册失败</option>
									<option value="1002">光路劣化</option>
									<option value="1003">PPPOE拨号失败</option>
									<option value="1004">环境感知告警</option>
									<option value="1005">网络质量告警</option>
									<option value="1006">网络质量关联</option>--%>
									<option value="500001">语音注册失败</option>
									<option value="500002">光路劣化</option>
									<option value="500003">PPPOE拨号失败</option>
									<option value="500004">终端环境</option>
									<option value="500005">网络质量</option>
									<option value="500006">网络综合质量</option>
									<option value="500007">光路劣化关联</option>
							</select></TD>
						</TR>
						<TR>

							<TD class=column width="15%" align='right'>告警类别</TD>
							<td width='35%' align="left"><SELECT name="alarm_type"
								class="bk">
									<%--<option value="-1" selected="selected">==请选择==</option> --%>
									<option value="1" selected="selected">==当前告警==</option>
									<option value="2">==历史告警==</option>
							</SELECT></td>
							<TD class="column" width='15%' align="right">告警解除</TD>
							<TD width='35%' align="left"><SELECT name="is_release"
								class="bk">
									<option value="-1" selected="selected">==请选择==</option>
									<option value="1">==是==</option>
									<option value="0">==否==</option>
							</SELECT></TD>
						</TR>
						<!-- <TR>
							<TD class="column" width='15%' align="right">告警预解除</TD>
							<TD width="35%"><SELECT name="is_pre_release" class="bk">
									<option value="-1" selected="selected">==请选择==</option>
									<option value="1">==是==</option>
									<option value="0">==否==</option>
							</SELECT></TD>
							<TD class="column" width='15%' align="right">是否派单</TD>
							<TD width='35%' align="left"><SELECT name="is_send_sheet"
								class="bk">
									<option value="-1" selected="selected">==请选择==</option>
									<option value="1">==是==</option>
									<option value="0">==否==</option>
							</SELECT></TD>

						</TR> -->
						<TR>
							<TD class=column width="15%" align='right'>区域</TD>
							<TD colspan="3" width="35%"><s:select list="cityList"
									name="city_id" headerKey="-1" headerValue="请选择属地"
									listKey="city_id" listValue="city_name" cssClass="bk"></s:select></TD>
						</TR>

						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;查&nbsp询&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td >
					<div style="overflow:scroll;">
					<iframe id="dataForm" name="dataForm" height="0" 
						frameborder="0"  width="100%" src="" style="overflow:scroll;overflow-x:hidden"></iframe>
					</div>
				</td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>