<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>用户信息管理</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">

$(function() {
	gwShare_change("city", "-1");
});
function gwShare_change(type, selectvalue) {
	switch (type) {
	case "city":
		var url = "<s:url value='/gtms/stb/resource/userMessage!getCityNextChild.action'/>";
		$.post(url, {}, function(ajax) {
			gwShare_parseMessage(ajax, $("select[@name='gwShare_cityId']"),
					selectvalue);
			$("select[@name='cityId']").html(
					"<option value='-1'>==请先选择属地==</option>");
		});
		break;
	case "cityid":
		var url = "<s:url value='/gtms/stb/resource/userMessage!getCityNext.action'/>";
		var cityId = $("select[@name='gwShare_cityId']").val();
		if ("-1" == cityId) {
			$("select[@name='gwShare_cityId']").html(
					"<option value='-1'>==请先选择属地==</option>");
			break;
		}
		$.post(url, {
			citynext : cityId
		}, function(ajax) {
			gwShare_parseMessage(ajax, $("select[@name='citynext']"),
					selectvalue);
			$("select[@name='cityId']").html(
					"<option value='-1'>==请先选择属地==</option>");
		});
		break;
	default:
		alert("未知查询选项！");
		break;
	}
}
function gwShare_parseMessage(ajax, field, selectvalue) {
	var flag = true;
	if ("" == ajax) {
		return;
	}
	var lineData = ajax.split("#");
	if (!typeof (lineData) || !typeof (lineData.length)) {
		return false;
	}
	field.html("");
	option = "<option value='-1' selected>==请选择==</option>";
	field.append(option);
	for ( var i = 0; i < lineData.length; i++) {
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if (selectvalue == xValue) {
			flag = false;
			//根据每组value和text标记的值创建一个option对象
			option = "<option value='"+xValue+"' selected>==" + xText
					+ "==</option>";
		} else {
			//根据每组value和text标记的值创建一个option对象
			option = "<option value='"+xValue+"'>==" + xText
					+ "==</option>";
		}
		try {
			field.append(option);
		} catch (e) {
			alert("设备型号检索失败！");
		}
	}
	if (flag) {
		field.attr("value", "-1");
	}
}
function add()
{
		var gwShare_cityId=$.trim($("select[@name='gwShare_cityId']").val());
		var citynext=$.trim($("select[@name='citynext']").val());
		
		if(gwShare_cityId == "-1"){
		    alert("请选择属地");
		    return false;
		}
		var platformType=$.trim($("select[@name='platformType']").val());
		if(""==platformType||"-1"==platformType)
		{
		alert("请选择业务平台!");
		return false;
		}
		var userGroupID=$.trim($("select[@name='userGroupID']").val());
		if(""==userGroupID||"-1"==userGroupID)
		{
		alert("请选择用户组!");
		return false;
		}
		var stbpwd=$.trim($("input[@name='stbpwd']").val());
		/* if(""==stbpwd)
		{
		alert("接入密码不能为空!");
		return false;
		} */
		var stbuser=$.trim($("input[@name='stbuser']").val());
		/* if(""==stbuser)
		{
		alert("接入账号不能为空!");
		return false;
		} */
		var stbuptyle=$.trim($("select[@name='stbuptyle']").val());
		if(""==stbuptyle||"-1"==stbuptyle)
		{
		alert("请选择上行方式!");
		return false;
		}
		var stbaccessStyle=$.trim($("select[@name='stbaccessStyle']").val());
		if(""==stbaccessStyle||"-1"==stbaccessStyle)
		{
		alert("请选择接入方式!");
		return false;
		}
		var iptvBindPhone=$.trim($("input[@name='iptvBindPhone']").val());
		var dealDate=$.trim($("input[@name='endtime']").val());
		var MAC=$.trim($("input[@name='MAC']").val());
		var servaccount=$.trim($("input[@name='servaccount']").val());
		if(""==servaccount)
		{
		alert("请填写业务账号!");
		return false;
		}
		var servpwd=$.trim($("input[@name='servpwd']").val());
		if(""==servpwd)
		{
		alert("请填写业务密码!");
		return false;
		}
		var url = "<s:url value='/gtms/stb/resource/userMessage!add.action'/>";
		$.post(url, {
			iptvBindPhone : iptvBindPhone,
			endtime : dealDate,
			citynext : citynext,
			gwShare_cityId:gwShare_cityId,
			platformType:platformType,
			userGroupID:userGroupID,
			stbuser:stbuser,
			stbpwd:stbpwd,
			MAC:MAC,
			stbaccessStyle:stbaccessStyle,
			servaccount:servaccount,
			servpwd:servpwd,
			stbuptyle:stbuptyle
		}, function(ajax) {
			if (ajax == "成功") {
				alert("添加成功!");
			} else if(ajax == "Mac为空或校验失败")
				{
				alert("mac地址只能由A-F字母或数字组成!");
				}else {
					alert(ajax);
				}
			});
}

</script>
</head>

<body>
	<form name="frm" id="frm" method="post" action="" target="dataForm">

		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">添加用户信息</th>
						</tr>
						<!-- <TR bgcolor="#FFFFFF">
							<TD class=column align="right">鉴权账号</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="authUser"
								maxlength=30 class=bk size=20>&nbsp;<font
								color="#FF0000">*</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">鉴权密码</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="authPwd" maxlength=30
								class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
						</TR> -->
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">受理时间</TD>
							<TD width="35%"><input type="text" name="endtime" readonly
								value="<s:property value='endtime'/>"> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" /></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD align="right" class=column width="15%">平台类型：</TD>
							<TD width="35%" nowrap><s:select list="platformTypeList" name="platformType"
									headerKey="-1" headerValue="请选择平台类型" listKey="platform_id"
									listValue="platform_name" cssClass="bk"></s:select>&nbsp; <font
								color="red"> *</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD align="right" class=column width="15%">用户分组：</TD>
							<TD width="35%" nowrap><s:select list="userGroupIDList" name="userGroupID"
									headerKey="-1" headerValue="请选择用户分组" listKey="group_id"
									listValue="group_name" cssClass="bk"></s:select>&nbsp; <font
								color="red"> *</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">手机号码</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="iptvBindPhone"
								maxlength=30 class=bk size=20></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column width="15%" align='right'>属地</TD>
							<TD align="left" width="35%"><select name="gwShare_cityId"
								class="bk" onchange="gwShare_change('cityid','-1')">
									<option value="-1">==请选择==</option>
							</select> &nbsp; <font
								color="red"> *</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column width="15%" align='right'>下级属地</TD>
							<TD align="left" width="35%"><select name="citynext"
								class="bk">
									<option value="-1">请先选择属地</option>
							</select></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">业务账号</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="servaccount"
								maxlength=30 class=bk size=20>&nbsp;<font
								color="#FF0000">*</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">业务密码</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="servpwd" maxlength=30
								class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">MAC地址</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="MAC" maxlength=30
								class=bk size=20>&nbsp;&nbsp;&nbsp;&nbsp;<font color="#FF0000">MAC地址只能由A-F字母或数字组成</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">接入方式</TD>
							<TD colspan=3><select name="stbaccessStyle" class="bk">
									<option value="-1">==请选择==</option>
									<option value="1"  selected = "selected">DHCP</option>
									<option value="2">Static</option>
									<option value="3">PPPoE</option>
									<option value="4">IPoE</option>
							</select>&nbsp;<font color="#FF0000">*</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">接入账号</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="stbuser"
								maxlength=30 class=bk size=20></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">接入密码</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="stbpwd" maxlength=30
								class=bk size=20></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">上行方式</TD>
							<TD colspan=3><select name="stbuptyle" class="bk">
									<option value="-1">==请选择==</option>
									<option value="FTTH" selected = "selected">FTTH</option>
									<option value="FTTB">FTTB</option>
									<option value="LAN">LAN</option>
									<option value="HGW">HGW</option>
							</select>&nbsp;<font color="#FF0000">*</font></TD>
						</TR>
						<TR>
						<td colspan="4" align="right" class="foot" width="100%">
									<button onclick="add()">&nbsp;添 加&nbsp;</button>
									<button onclick="javascript:window.close();">&nbsp;返 回&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
