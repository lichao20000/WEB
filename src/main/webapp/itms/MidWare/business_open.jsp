<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>

<script language="JavaScript">

function save(){

	var deviceModel = $.trim($("input[@name='deviceModel']").val());
	var cityId = $.trim($("input[@name='cityId']").val());
	var oui = $.trim($("input[@name='oui']").val());
	var deviceSn = $.trim($("input[@name='deviceSn']").val());
	var account = $.trim($("input[@name='account']").val());
	var password = $.trim($("input[@name='password']").val());
    var b_type = $.trim($("select[@name='b_type']").val());
    var op_type = $.trim($("select[@name='op_type']").val());
    var username = $.trim($("input[@name='username']").val());
    var deviceId = $.trim($("input[@name='deviceId']").val());
	if(oui==""){
		alert("请输入设备厂商OUI！");
		$("input[@name='oui']").focus();
		return false;
	}
	if(deviceSn==""){
		alert("请输入设备序列号！");
		$("input[@name='deviceSn']").focus();
		return false;
	}
	if(op_type=="B1"||op_type=="B4"){
		if(account==""){
			alert("请输入业务账号！");
			$("input[@name='account']").focus();
			return false;
		}
		if(username==""){
			alert("请输入用户账号！");
			$("input[@name='username']").focus();
			return false;
		}
		if(password==""){
			alert("请输入密码！");
			$("input[@name='password']").focus();
			return false;
		}
	}
	
	if(b_type==""||b_type=="-1"){
		alert("请选择业务类型！");
		return false;
	}
	if(op_type==""||op_type=="-1"){
		alert("请选择操作类型！");
		return false;
	}
	var url = '<s:url value='/itms/midware/businessOpen!save.action'/>'; 
	$.post(url,{
		deviceModel:deviceModel,
		cityId:cityId,
		oui:oui,
		deviceSn:deviceSn,
		account:encodeURIComponent(account),
		password:password,
		b_type:encodeURIComponent(b_type),
		op_type:encodeURIComponent(op_type),
		username:encodeURIComponent(username),
		deviceId:deviceId
	},function(ajax){	
	    alert(ajax);
	});
}

function changeOp_type(){
	var op_type = $.trim($("select[@name='op_type']").val());
	if(op_type=="B1"||op_type=="B4"){
		$("tr[@id='close']").show();
	}else{
		$("tr[@id='close']").hide();
	}
	
}

</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						业务工单管理
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12">

					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<input type="hidden" name="cityId" value="<s:property value="cityId"/>">
				<input type="hidden" name="deviceId" value="<s:property value="deviceId"/>">
				<input type="hidden" name="deviceModel" value="<s:property value="deviceModel"/>">
				
				<table class="querytable">
					<tr>
						<th colspan=4>
							业务开通
						</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							设备厂商OUI
						</td>
						<td>
							<input type="text" name="oui" value="<s:property value="oui"/>">
							<font color="red">&nbsp;*</font>
						</td>
						<td class=column width="15%">
							设备序列号
						</td>
						<td>
							<input type="text" name="deviceSn"
								value="<s:property value="deviceSn"/>">
							<font color="red">&nbsp;*</font>
						</td>
					</tr>

					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							业务类型
						</td>
						<td>
							<select name="b_type" class="bk">
								<option value="-1" selected>
									==请选择==
								</option>
								<option value="1">
									MediaFtp
								</option>
								<option value="8">
									UPLOAD
								</option>
								<option value="6">
									Rss
								</option>
								<option value="21">
									爱音乐
								</option>
								<option value="21">
									网络电台
								</option>
							</select>
							<font color="red">&nbsp;*</font>
						</td>

						<td class=column width="15%">
							操作类型
						</td>
						<td>
							<select name="op_type" onchange="changeOp_type();">
								<option value="-1" selected>
									==请选择==
								</option>
								<option value="B1">
									业务开通
								</option>
								<option value="B2">
									业务暂停
								</option>
								<option value="B3">
									业务恢复
								</option>
								<option value="B4">
									业务参数修改
								</option>
								<option value="B5">
									业务关闭
								</option>
								<option value="B6">
									业务模块升级
								</option>
							</select>
							<font color="red">&nbsp;*</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff id="close">
						<td class=column width="15%">
							业务账号
						</td>
						<td>
							<input type="text" name="account"
								value="<s:property value="account"/>">
							<font color="red">&nbsp;*</font>
						</td>
						<td class=column width="15%">
							宽带账号
						</td>
						<td>
							<input type="text" name="username"
								value="<s:property value="username"/>">
							<font color="red">&nbsp;*</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff id="close">
						<td class=column width="15%">
							密码
						</td>
						<td colspan="3">
							<input type="text" name="password" value="">
							<font color="red">&nbsp;*</font>
						</td>


					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="save()">
								&nbsp;发 送&nbsp;
							</button>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<button onclick="reset()">
								&nbsp;重 置&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
