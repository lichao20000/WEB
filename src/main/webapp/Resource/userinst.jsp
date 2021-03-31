<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="LipossGlobals" scope = "request" class = "com.linkage.litms.LipossGlobals"></jsp:useBean>
<%--
	zhaixf(3412) 2008-05-12
	req:NJLC_HG-BUG-ZHOUMF-20080508-001
--%>
<%
	request.setCharacterEncoding("GBK");
	
	String gw_type = request.getParameter("gw_type");

	if (gw_type == null || "".equals(gw_type)) {
		gw_type = "1";
	}

	String title = "手工安装";
	
	String username = request.getParameter("username");
	if (username == null){
		username = "";
	}
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
	//是否判断
	

function chkUser(){
		if(ischeck != null && ischeck == 1){
			var uname = document.frm.username.value;
			document.all.chkUserF.src = 'ChkInstUser.jsp?username='+uname
						+ '&gwType='+<%=gw_type%>
						+ '&time='+(new Date);
		}
}

function showResult(request){
	$("_process").innerHTML="";
	eval(request.responseText);
}

function refresh(){
	window.location.href=window.location.href;
}

//根据复选框选中状态，获取设备id
function getDeviceIDByCheck(_name){
	var arrObj = document.all(_name);
	var strDeviceIDs = "";
	if(typeof(arrObj.length) == "undefined"){
		if(arrObj.checked){
			strDeviceIDs = "&device_id=" + arrObj.value;
		}
	}else{
		for(var i=0;i<arrObj.length;i++){
			if(arrObj[i].checked){
				strDeviceIDs += "&device_id=" + arrObj[i].value;
			}
		}
	}
	return strDeviceIDs;
}
//全选
function SelectAll(_this,_name){
	var check = _this.checked;
	var chkArr = $A(document.getElementsByName(_name));
	chkArr.each(function(obj){
		obj.checked = check;
	});
}

function checkSerialno(){
	var tmp = document.frm.device_serialnumber.value;
	if (tmp.length < 4){
		alert('请输入至少4位序列号进行查询!');
	}
	else{
		getUserinstInfo();
	}
}

function getUserinstInfo(){

    var page="userinstInfo.jsp?status=All"
    		+"&device_serialnumber="+document.frm.device_serialnumber.value
    		+"&gw_type="+document.frm.gw_type.value
    		+"&refresh="+(new Date()).getTime();
	document.all("childFrm").src = page;
	document.frm.selDevice.value = "";
}


function CheckForm(){

	//chkUser();
	var obj = document.all("chkCheck");
	var serialnumber = '';
	var chk;
	
	if (obj == null){
		alert('请先选择一个设备');
		return false;
	}
	
	if (typeof(obj.value) != 'undefined'){
		if (obj.checked){
			var tmp = obj.value.split('#');
			document.frm.selDevice.value = tmp[0];
			serialnumber = tmp[1];
		}
	}
	else{
		for (var i=0;i<obj.length;i++){
			if (obj[i].checked){
				var tmp = obj[i].value.split('#');
				document.frm.selDevice.value = tmp[0];
				serialnumber = tmp[1];
			}
		}
	}
	
	document.frm.serialnumber.value = serialnumber;
	if (document.frm.selDevice.value == ""){
		alert('请先选择一个设备');
		return false;
	}
	if (document.frm.username.value == ""){
		alert('请填写用户帐号');
		return false;
	}
	
//	if(ischeck != null && ischeck == 1){
//		if(document.frm.ckuser.value == "0"){
//			alert("用户名不存在，请先添加用户");
//			return false;
//
//		}
//	}
	
	var message = "请确认！用户帐号："+document.frm.username.value+"，设备序列号："+serialnumber;
	if (!confirm(message+'！是否继续保存?')){
		return false;
	}
	
}

function goPage(offset){
	var page="userinstInfo.jsp?status=All"
			+"&device_serialnumber="+document.frm.device_serialnumber.value
			+"&gw_type="+document.frm.gw_type.value
    		+"&offset="+offset+"&refresh="+(new Date()).getTime();
	document.all("childFrm").src = page;
	document.frm.selDevice.value = "";
}

function DetailDevice(device_id){
	var strpage = "DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

//-->
</SCRIPT>

<link rel="stylesheet" href="../css/listview.css" type="text/css">
<%@ include file="../toolbar.jsp"%>

<FORM NAME="frm" METHOD="post" ACTION="userinstSave.jsp?gwType="+<%=gw_type%>
	onsubmit="return CheckForm()">

	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
		<TR>
			<TD HEIGHT=20>
			<input type = "hidden" name = "ckuser" value = "">
				&nbsp;
			</TD>
		</TR>
		<TR>
			<TD>

				<input type="hidden" name="gw_type" value="<%=gw_type%>">
				<input type="hidden" name="selDevice" value="">
				<input type="hidden" name="serialnumber"/>
				<TABLE width="95%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite">
										现场安装
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										请在“设备序列号”处输入至少4位号码进行查询
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR bgcolor="#FFFFFF">
									<TH colspan="2">
										<%=title%>
									</TH>
								</TR>
								<TR bgcolor="#FFFFFF">
									
									<TD align="right">
										设备序列号 :
										<input type="text" class="bk" name=device_serialnumber
											value="">
										<input type="button" class="btn" name="sort" value=" 查 询 "
											onclick="checkSerialno()">&nbsp;&nbsp;
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
					<TR style="display:none" id="dispTr">
						<TD bgcolor=#999999>
							<div id="userTable"></div>
						</TD>
					</TR>
					<TR style="display:none">
						<TD>
							<IFRAME ID="childFrm" SRC="" STYLE="display:none"></IFRAME>
						</TD>
					</TR>
				</TABLE>
				<br>
				<br>

			</TD>
		</TR>
		<TR>
			<TD>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					align="center">
					<tr bgcolor="#FFFFFF">
						<td width="10%" bgcolor=#FFFFFF>
							用户帐户:
						</td>
						<td width="25%" bgcolor=#FFFFFF>
							<input type="text" name="username" class="bk" value="<%=username%>">
						</td>
						<td bgcolor=#FFFFFF>
							
							<input type="submit" name="submit" value=" 保 存 ">
						</td>
					</tr>
				</table>
			</TD>
		</TR>
	</TABLE>

</FORM>
<iframe id="chkUserF" style = "display:none"></iframe>
<script language="javascript">
getUserinstInfo();
</script>

<br>
<br>
<%@ include file="../foot.jsp"%>
