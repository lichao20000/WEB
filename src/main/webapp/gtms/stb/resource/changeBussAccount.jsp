<%@page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />

<title>用户业务查询</title>
<script type="text/javascript"
	src="<s:url value="/Js/CheckFormForm.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	function CheckForm(){
		var _dealdate = $.trim($("input[@name='dealdate']").val());
		var _userType = $.trim($("select[@name='userType']").val());
		var _loidMsg = $.trim($("input[@name='loidMsg']").val());
		var _cityId = $.trim($("select[@name='cityId']").val());
		var _oldBussAccount = $.trim($("input[@name='oldBussAccount']").val());
		var _newBussAccount = $.trim($("input[@name='newBussAccount']").val());

		//业务受理时间
		if (!IsNull(_dealdate, "业务受理时间")) {
			$("input[@name='dealdate']").focus();
			return false;
		}

		

		//属地
		if('' == _cityId || '-1' == _cityId){
		alert("请选择属地");
		$("select[@name='cityId']").focus();
		return false;
	}
		//老业务账号
		if (!IsNull(_oldBussAccount, "老业务账号")) {
			$("input[@name='oldBussAccount']").focus();
			return false;
		}
		//新业务账号
		if (!IsNull(_newBussAccount, "新业务账号")) {
			$("input[@name='newBussAccount']").focus();
			return false;
		}


	document.queryStbForm.submit();
	}
</script>
</head>
<body>
	<form id="queryStbForm" name="queryStbForm" method="POST"
		action="<s:url value='/gtms/stb/resource/stbSimulateSheet!sendSheet.action'/>">
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0">

			<TR>
				<TD>
					<table id="tblQuery" width="100%" class="querytable">
						<TR>
							<TD bgcolor=#999999>
						<thead>
							<tr>
								<input type="hidden" name="servTypeId"
							value='<s:property value="servTypeId" />'> <input
							type="hidden" name="operateType"
							value='<s:property value="operateType" />'>
								<td colspan="4" class="title_1">修改业务账号工单</td>
							</tr>
						</thead>
						<TR>
							<TD class="title_2">业务受理时间</TD>
							<TD width="30%"><input type="text" name="dealdate"
								value='<s:property value="dealdate" />' readonly class=bk>
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.queryStbForm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="13" height="12"
								border="0" alt="选择">&nbsp; <font color="#FF0000">*</font></TD>
							<TD class="title_2">客户类型</TD>
							<TD width="30%"><select name="userType" class="bk">
									<option selected value="3">==机顶盒==</option>
							</select>&nbsp; <font color="#FF0000">*</font></TD>
						</tr>

						<tr>
							<TD class="title_2">LOID</TD>
							<TD><INPUT TYPE='text' name='loidMsg' class=bk value=""></TD>
							<TD class=column align="right" width="20%">属地</TD>
							<TD width="30%"><s:select list="cityList" name="cityId"
									headerKey="-1" headerValue="请选择属地" listKey="city_id"
									listValue="city_name" value="cityId" cssClass="bk"></s:select>
								&nbsp; <font color="#FF0000">*</font></TD>
						</tr>
						<tr>

							<TD class="title_2" align="right" nowrap>老业务账号</TD>
							<TD><INPUT TYPE="text" NAME="oldBussAccount" maxlength=20
								class=bk value="">&nbsp; <font color="#FF0000">*</font></TD>
									<TD class="title_2" align="right" nowrap>新业务账号</TD>
							<TD><INPUT TYPE="text" NAME="newBussAccount" maxlength=20
								class=bk value="">&nbsp; <font color="#FF0000">*</font></TD>
						</tr>

						<TR>
							<TD colspan="4" height="35px" align="right" class="foot"><button
									onclick="CheckForm()">&nbsp;发送工单&nbsp;</button></TD>
						</TR>
						</TD>
						</TR>
					</table>
				</TD>
			</TR>
		</table>
	</form>
</body>
</html>
