<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>BSS模拟工单</title>

<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/CheckFormForm.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<script type="text/javascript">

	//显示工单参数信息
	function sendSheet(){
		
		var url = "<s:url value='/gtms/service/sendRouterOpenSheet!sendSheet.action'/>";
		var netUserName = "";
		
		var _operateType = $("select[@name='operateType']");  // 操作类型
		var _cityId = $("select[@name='cityId']");            // 属地
		
		var _servTypeId = $("input[@name='servTypeId']");     // 业务类型
		var _netUsername = $("input[@name='netUsername']");   // 宽带帐号
		var _netPassword = $("input[@name='netPassword']");   // 宽带密码
		
		if ('-1' == _operateType.val()) {
			alert("请选择操作类型！");
			_operateType.focus();
			return false;
		}
		if ('-1' == _cityId.val()) {
			alert("请选择属地！");
			_cityId.focus();
			return false;
		}
		
		// 3 表示销户   1 表示开户
		if('3' == _operateType.val()){
			var _netUsername2 = $("input[@name='netUsername2']");
			if (!IsNull(_netUsername2.val(), "宽带帐号")) {
				_netUsername2.focus();
				return false;
			} else {
				netUserName = _netUsername2.val();
			}
		} else {
			if (!IsNull(_netUsername.val(), "宽带帐号")) {
				_netUsername.focus();
				return false;
			} else {
				netUserName = _netUsername.val();
			}
		}
		
		// 3 表示销户   1 表示开户
		if('3' != _operateType.val()){ 
			if (!IsNull(_netPassword.val(), "宽带密码")) {
				_netPassword.focus();
				return false;
			}
		}
		
		$.post(url,{
			servTypeId:_servTypeId.val(),
			operateType:_operateType.val(),
			cityId:_cityId.val(),
			netUsername:netUserName,
			netPassword:_netPassword.val()
		},function(ajax){
			//alert(ajax);
			var msg = ajax.split("|||");
			if("0" == msg[0]){
				alert("成功！");
			} else {
				alert(msg[2]);  // 成功信息：0|||成功代码    失败信息：1|||失败代码|||失败描述
			}
		});
		
	}
	
	function hiddenElements(){
		var _operateType = $("select[@name='operateType']");	// 操作类型
		
		if(_operateType.val() == "-1" || _operateType.val() == "1"){
			$("tr[@id='tr01']").css("display","");
			$("tr[@id='tr02']").css("display","none");
			
			$("input[@name='netUsername']").val("");
			$("input[@name='netPassword']").val("");
			$("input[@name='netUsername2']").val("");
		}else{
			$("tr[@id='tr01']").css("display","none");
			$("tr[@id='tr02']").css("display","");
			
			$("input[@name='netUsername']").val("");
			$("input[@name='netPassword']").val("");
			$("input[@name='netUsername2']").val("");
		}
	}
</script>
</head>

<body onLoad="hiddenElements()">
<form name="mainfrm" action="" target="dataForm">
<input name="servTypeId" value="50" type="hidden">
<table>
	<tr>
		<td HEIGHT=20>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<table class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">路由业务工单</td>
				<td><img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12" /> 路由业务工单</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table class="querytable">

			<TR>
				<th colspan="4">模拟radius发工单</th>
			</tr>

			<TR bgcolor="#FFFFFF">
				<TD class=column align="right" width="20%">操作类型</TD>
				<TD width="30%"><select name="operateType" class="bk" onChange="hiddenElements()">
				    <!-- option value="-1">==请选择操作类型==</option-->
					<option value="1">==开户==</option>
					<option value="3">==销户==</option>
				</select>&nbsp; <font color="#FF0000">* </font></TD>
				
				<TD class=column align="right" width="20%">属地</TD>
				<TD width="30%"><s:select list="cityList" name="cityId"
					headerKey="-1" headerValue="请选择属地" listKey="city_id"
					listValue="city_name" value="cityId" cssClass="bk"></s:select>
				&nbsp; <font color="#FF0000">*</font></TD>
			</TR>
			
			<TR id="tr01" bgcolor="#FFFFFF" style="display:none">
				<TD class=column align="right" width="20%">宽带账号</TD>
				<TD width="30%"><INPUT TYPE="text" NAME="netUsername" maxlength=50 class="bk"
					value="">&nbsp; <font color="#FF0000">* </font></TD>
						
				<TD class=column align="right" width="20%" >宽带密码</TD>
				<TD width="30%" ><INPUT TYPE="text" NAME="netPassword" maxlength=50 class="bk"
					value="">&nbsp; <font color="#FF0000">* </font></TD>
			</TR>
			
			<TR id="tr02" bgcolor="#FFFFFF" style="display:none">
				<TD class=column align="right" width="20%">宽带账号</TD>
				<TD width="30%"><INPUT TYPE="text" NAME="netUsername2" maxlength=50 class="bk"
					value="">&nbsp; <font color="#FF0000">* </font></TD>
						
				<TD class=column align="right" width="20%" ></TD>
				<TD width="30%" ></TD>
			</TR>
			
			<TR>
				<td colspan="4" align="right" class=foot>
				<button onclick="sendSheet()">&nbsp;发送工单&nbsp;</button>
				</td>
			</TR>
		</table>
		</td>
	</tr>
</table>
<br>
</form>
</body>
</html>