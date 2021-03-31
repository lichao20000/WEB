<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>手工配置业务端口</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		var loid = $("input[@name='loid']").val();
		if ("" == loid) {
			alert("请输入LOID");
			loid.focus();
			return;
		}
		$("tr[@id='trData']").show();
		$("button[@name='button']").attr("disabled", true);
		var url = "<s:url value='/itms/service/manuallyconfigureportbusiness!initLoid.action'/>";
		$.post(url, {
			loid : loid
		}, function(ajax) {
			if("" != ajax){
				$("div[@id='QueryData']").html("");
				$("div[@id='QueryData']").append(ajax);
				$("button[@name='button']").attr("disabled", false);
			}else{
				$("div[@id='QueryData']").html("");
				$("button[@name='button']").attr("disabled", false);
				alert("用户不存在或者用户没有绑定设备");
				loid.focus();
			}
		});
	}

</script>
</head>

<body>
	<form id="form" name="selectForm" action="">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								手工配置业务端口</td>
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
							<th colspan="4">手工配置业务端口</th>
						</tr>
						<TR>
							<TD class=column align="right" width="25%">LOID</TD>
							<TD width="25%"><INPUT type="text" name="loid" maxlength=20 class=bk
								value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right"  width="25%"></TD>
							<TD width="25%"></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()" id="button" name="button">&nbsp;查询&nbsp;</button>
								<input type="hidden" name="cityId" id="cityId" value="00" />
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr id="trData" style="display: none">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
						正在统计，请稍等....</div>
				</td>
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
<%@ include file="../../../foot.jsp"%>