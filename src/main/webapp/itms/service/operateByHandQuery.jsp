<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>手工工单日志查询</title>
<%
	/**
	 * ITMS手工工单日志查询
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

		document.selectForm.submit();
	}

	function ToExcel() {
		var starttime = $("input[@name='startOpenDate']").val();
		var endtime = $("input[@name='endOpenDate']").val();
		var city_id = $("select[@name='city_id']").val();
		var servType = $("select[@name='servType']").val();
		var username = Trim($("input[@name='username']").val());
		var resultType = $("select[@name='resultType']").val();

		var url = "<s:url value='/gtms/service/operateByHandQuery!getOperateByHandInfoCount.action'/>";
		$.post(url, {starttime : starttime,
					 endtime : endtime,
					 city_id : city_id,
					 servType : servType,
					 username : username,
					 resultType : resultType
					},
						function(ajax) {
							var total = parseInt(ajax);
							if (ajax > 100000) {
								alert("数据量太大不支持导出 ");
								return;
							} else {
								
								var mainForm = document
										.getElementById("selectForm");
								mainForm.action = "<s:url value='/gtms/service/operateByHandQuery!getOperateByHandInfoExcel.action'/>"
								mainForm.submit();
								mainForm.action = "<s:url value='/gtms/service/operateByHandQuery!getOperateByHandInfo.action'/>"
							}
						});
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
		action="<s:url value='/gtms/service/operateByHandQuery!getOperateByHandInfo.action'/>"
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
								手工工单操作日志</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> 时间为受理时间</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">业务一次下发成功率统计</th>
						</tr>

						<TR>
							<TD class=column width="15%" align='right'>开始时间</TD>
							<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
							</TD>
							<TD class=column width="15%" align='right'>结束时间</TD>
							<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
							</TD>
						</TR>


						<TR>

							<TD class=column width="15%" align='right'>属地</TD>
							<TD width="35%"><s:select list="cityList" name="city_id"
									headerKey="-1" headerValue="请选择属地" listKey="city_id"
									listValue="city_name" cssClass="bk"></s:select></TD>

							<TD class="column" width='15%' align="right">工单类型</td>
							<td width='35%' align="left"><select name="servType"
								class="bk">
									<option value="-1"
										<s:property value='"-1".equals(servType)?"selected":""'/>>
										==请选择==</option>
									<option value="20"
										<s:property value='"20".equals(servType)?"selected":""'/>>
										==用户资料==</option>
									<option value="10"
										<s:property value='"10".equals(servType)?"selected":""'/>>
										==宽带业务==</option>
									<option value="11"
										<s:property value='"11".equals(servType)?"selected":""'/>>
										==IPTV业务==</option>
									<option value="14"
										<s:property value='"14".equals(servType)?"selected":""'/>>
										==VOIP业务==</option>
							</select></TD>

						</TR>
						<TR>
							<TD class="column" width='15%' align="right">操作人</TD>
							<TD width="35%"><input type="text" name="username" size="20"
								maxlength="30" class=bk /></TD>

							<TD class="column" width='15%' align="right">执行结果</TD>
							<TD width='35%' align="left" id="allOrderType"><select
								name="resultType" class="bk">
									<option value="-1"
										<s:property value='"-1".equals(resultType)?"selected":""'/>>
										==请选择==</option>
									<option value="1"
										<s:property value='"1".equals(resultType)?"selected":""'/>>
										==成功==</option>
									<option value="0"
										<s:property value='"0".equals(resultType)?"selected":""'/>>
										==失败==</option>
							</select></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;统计&nbsp;</button>
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