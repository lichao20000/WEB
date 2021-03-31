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
	<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
</head>

<body>
	<form name="frm" id="frm" method="post"
		action=""
		target="dataForm">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">
						<TR>
							<th colspan="4">修改用户信息</th>
						</tr>
						<s:if test="date!=null">
				<s:if test="date.size()>0">
					<s:iterator value="date">
					
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">开户时间</TD>
							<TD width="35%"  colspan="3" >
							<input type="hidden" name="stbuptyle1" value="<s:property value="stbuptyle1" />" />
							<input type="hidden" name="stbaccessStyle1" value="<s:property value="stbaccessStyle1" />" />
							<input type="text" name="endtime" readonly
								value="<s:property value="dealDate"/>"> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" /></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">业务平台</TD>
							<TD colspan=3><select name="platformType" class="bk" disabled="false">
								<option value="<s:property value="belong"/>" selected="selected"></option> &nbsp; <font
								color="red"> *</font>
								</select></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">用户分组</TD>
							<TD colspan=3>
									<select name="userGroupID" class="bk" disabled="false">
							<option value="<s:property value='groupid'/>" ></option> &nbsp; <font
								color="red"> *</font>
								</select></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">手机号码</TD>
							<TD colspan="3" ><input type="text" disabled="false" id="iptvBindPhone" name="iptvBindPhone" class="bk" value="<s:property value="iptvBindPhone"/>"></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column width="15%" align='right'>属地</TD>
							<TD width="35%"  colspan="3" >
							<select name="gwShare_cityId" class="bk" onchange="change_select('cityid','-1','2')">
									<option value="<s:property value="parentcity" />">==请选择==</option>
							</select> &nbsp; <font color="red"> *</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column width="15%" align='right'>下级属地</TD>
							<TD width="35%"  colspan="3" >
							<select name="citynext" class="bk">
									<option value="<s:property value="citynext" />">请先选择属地</option>
							</select></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">业务账号</TD>
							<TD colspan=3><input type="text" id="servaccount" name="servaccount" disabled="disabled" class="bk" value="<s:property value="servaccount" />">&nbsp;<font
								color="#FF0000" >*</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">业务密码</TD>
							<TD colspan=3><input type="text" id="servpwd" name="servpwd" class="bk" value="<s:property value="servpwd" />">&nbsp;<font
								color="#FF0000" >*</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">MAC地址</TD>
							<TD colspan=3><input type="text" id="MAC" disabled="disabled" name="MAC" class="bk" value="<s:property value="MAC" />"></TD>
						</TR>
						<%-- <TR bgcolor="#FFFFFF">
							<TD class=column align="right">接入方式</TD>
							<TD colspan=3><select name="stbaccessStyle" class="bk">
									<option value="-1>">==请选择==</option>
									<option value="1">DHCP</option>
									<option value="2">Static</option>
									<option value="3">PPPoE</option>
									<option value="4">IPoE</option>
							</select>&nbsp;<font color="#FF0000">*</font></TD>
						</TR> --%>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">接入账号</TD>
							<TD colspan=2><input type="text" id="stbuser" name="stbuser" class="bk" value="<s:property value="stbuser" />"></TD>
						    <s:if test='stbuser!=null || (stbpwd!=null && stbpwd!=" ")'>
						    	<td colspan=1><button onclick="cleanAccountPwd()">&nbsp;清除接入账号和密码&nbsp;</button></td>
						    </s:if>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">接入密码</TD>
							<TD colspan=3><input type="text" id="stbpwd" name="stbpwd" class="bk" value="<s:property value="stbpwd" />"></TD>
						</TR>
					<%-- 	<TR bgcolor="#FFFFFF">
							<TD class=column align="right">上行方式</TD>
							<TD colspan=3><select name="stbuptyle" class="bk">
									<option value="-1">==请选择==</option>
									<option value="1">FTTH</option>
									<option value="2">FTTB</option>
									<option value="3">LAN</option>
									<option value="4">HGW</option>
							</select>&nbsp;<font color="#FF0000">*</font></TD>
						</TR> --%>
						<TR>
							<td colspan="4" align="right" class="foot" width="100%">
									<button onclick="add()">&nbsp;保 存&nbsp;</button>
									<button onclick="javascript:window.close();">&nbsp;返 回&nbsp;</button>
							</td>
						</TR>
						</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=4>系统没有该用户的业务信息!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=4>系统没有此用户!</td>
				</tr>
			</s:else>
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
		</table>
		<br>
	</form>
</body>
<script type="text/javascript">	
	$(function(){
		/* var stbaccessStyle1=$("input[@name='stbaccessStyle1']").val();
		var stbuptyle1=$("input[@name='stbuptyle1']").val();
		$("select[@name='stbaccessStyle']").attr("value",stbaccessStyle1)
		$("select[@name='stbuptyle']").attr("value",stbuptyle1) */
		var cityId = $.trim($("select[@name='gwShare_cityId']").val());
		var citynext = $.trim($("select[@name='citynext']").val());
		var platformType = $.trim($("select[@name='platformType']").val());
		var userGroupID = $.trim($("select[@name='userGroupID']").val());
		change_select("city",cityId,"1");
		change_select("cityid",citynext,"1");
		change_select("platform",platformType,"1");
		change_select("userGroupID",userGroupID,"1");
	});
	function change_select(type,selectvalue,isChange){
	switch (type){
	case "city":
		var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getCityNextChild.action"/>";
		$.post(url,{
		},function(ajax){
			gwShare_parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
			$("select[@name='citynext']").html("<option value='-1'>==请先选择属地==</option>");
		});
		break;
	case "cityid":
		var url = "<s:url value='/gtms/stb/resource/userMessage!getCityNext.action'/>";
		var cityId = $("select[@name='gwShare_cityId']").val();
		if ("-1" == cityId) {
			$("select[@name='citynext']").html("<option value='-1'>==请先选择属地==</option>");
			break;
		}
		$.post(url, {
			citynext : cityId
		}, function(ajax) {
			gwShare_parseMessage(ajax, $("select[@name='citynext']"),selectvalue);
		});
		break;
		case "platform":
			var url = "<s:url value='/gtms/stb/resource/userMessage!getplatformType.action'/>";
			var platformType = $("select[@name='platformType']").val();
			/* if ("-1" == platformType) {
				$("select[@name='platformType']").html("<option value='-1'>==请先选择业务平台==</option>");
				break;
			} */
			$.post(url, {
			}, function(ajax) {
				gwShare_parseMessage(ajax, $("select[@name='platformType']"),selectvalue);
			});
			break;
		case "userGroupID":
			var url = "<s:url value='/gtms/stb/resource/userMessage!getuserGroupID.action'/>";
			var userGroupID = $("select[@name='userGroupID']").val();
			/* if ("-1" == userGroupID) {
				$("select[@name='citynext']").html("<option value='-1'>==请先用户分组==</option>");
				break;
			} */
			$.post(url, {
			}, function(ajax) {
				gwShare_parseMessage(ajax, $("select[@name='userGroupID']"),selectvalue);
			});
			break;
		default:
			alert("未知查询选项！");
			break;
	}	
}
	//解析查询设备型号返回值的方法
	function gwShare_parseMessage(ajax,field,selectvalue){
		var flag = true;
		if(""==ajax){
			return;
		}
		var lineData = ajax.split("#");
		if(!typeof(lineData) || !typeof(lineData.length)){
			return false;
		} 
		field.html("");
		/* option = "<option value='-1' selected>==请选择==</option>";
		field.append(option); */
		for(var i=0;i<lineData.length;i++){
			var oneElement = lineData[i].split("$");
			var xValue = oneElement[0];
			var xText = oneElement[1];
			if(selectvalue==xValue){
				flag = false;
				//根据每组value和text标记的值创建一个option对象
				option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
			}else{
				//根据每组value和text标记的值创建一个option对象
				option = "<option value='"+xValue+"'>=="+xText+"==</option>";
			}
			try{
				field.append(option);
			}catch(e){
				alert("设备型号检索失败！");
			}
		}
		if(flag){
			field.attr("value","-1");
		}
	}
	function cleanAccountPwd(){
		//是否确认清除接入账号和密码（该操作不影响机顶盒中已经存在的数据）
		if(!confirm("是否确认清除接入账号和密码（该操作不影响机顶盒中已经存在的数据）？")){
			return false;
		}
		var servaccount=$.trim($("input[@name='servaccount']").val());
		var url = "<s:url value='/gtms/stb/resource/userMessage!cleanAccountPwd.action'/>";
		$.post(url, {
			servaccount : servaccount
		}, function(ajax) {
			if (ajax == "成功") {
				alert("清空接入账号和密码成功!");
				window.location.reload();
			}else {
					alert(ajax);
				}
		});
	}
	
	function add() {
		var gwShare_cityId=$.trim($("select[@name='gwShare_cityId']").val());
		var citynext=$.trim($("select[@name='citynext']").val());
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
		var stbuser=$.trim($("input[@name='stbuser']").val());
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
		var url = "<s:url value='/gtms/stb/resource/userMessage!updateuserMessage.action'/>";
		$.post(url, {
			iptvBindPhone : iptvBindPhone,
			endtime : dealDate,
			platformType:platformType,
			userGroupID:userGroupID,
			stbuser:stbuser,
			stbpwd:stbpwd,
			MAC:MAC,
			servaccount:servaccount,
			servpwd:servpwd,
			gwShare_cityId:gwShare_cityId,
		    citynext:citynext
		}, function(ajax) {
			if (ajax == "成功") {
				alert("修改成功!");
				window.close();
			}else if(ajax == "Mac为空或校验失败")
				{
				alert("mac地址只能由A-F字母或数字组成!");
				}else {
					alert(ajax);
				}
		});
	}
</script>
</html>
