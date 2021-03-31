<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="LipossGlobals" scope = "request" class = "com.linkage.litms.LipossGlobals"></jsp:useBean>
<%
	request.setCharacterEncoding("GBK");
	String title = "企业网关手工安装";
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
			document.all.chkUserF.src = "ChkInstUser_bbms.jsp?username=" +uname +"&time="+ new Date().getTime();
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
	if (tmp.length < 6){
		alert('请输入至少“最后6位”序列号进行查询!');
	}
	else{
		getUserinstInfo();
	}
}

function getUserinstInfo(){

    var page="userinstInfo_bbms.jsp?status=All"
    		+"&device_serialnumber="+document.frm.device_serialnumber.value
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
		alert('请填写用户宽带帐号/专线号');
		return false;
	}
	var message = "请确认！用户帐号："+document.frm.username.value+"，设备序列号："+serialnumber;
	if (!confirm(message+'！是否继续保存?')){
		return false;
	}
	document.frm.next.disabled = "true";
	document.frm.save_btn.disabled = "true";
}

function checkUsernameEmpty() {
	if (document.frm.username.value == ""){
		alert('请填写用户宽带帐号/专线号');
		document.frm.username.focus();
		return false;
	}
	return true;
}
function nextstep(){
	if (!checkUsernameEmpty()) {
		return;
	}
	var page="userinst_bbms_customerinfo.jsp?username="+document.frm.username.value+"&refresh="+new Date().getTime();
	document.all("childFrm1").src = page;
}

function goPage(offset){
	var page="userinstInfo_bbms.jsp?status=All"
			+"&device_serialnumber="+document.frm.device_serialnumber.value
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
  
<FORM NAME="frm" METHOD="post" ACTION="userinstSave_bbms.jsp" onsubmit="return CheckForm()">

	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%">
		<TR>
			<TD HEIGHT=20>
			<input type = "hidden" name = "ckuser" value = "">
				&nbsp;
			</TD>
		</TR>
		<TR>
			<TD>
				<input type="hidden" name="selDevice" value="">
				<input type="hidden" name="serialnumber"/>
				<TABLE width="98%" border=0 cellspacing=0 cellpadding=0
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
										请在“设备序列号”处输入至少<font color="red">最后6位</font>序列号进行查询
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
					<TR>
						<TD bgcolor="ffffff" height="5"></TD>
					</TR>
					<TR style="display:" id="dispTr">
						<TD  bgcolor="#999999">
							<div id="userTable">
							
							<TABLE border=0 cellspacing=1 cellpadding=2 width='100%'>
								<TR>
									<!-- <TD COLSPAN=10 HEIGHT=30 bgcolor='#FFFFFF'>正在载入未绑定的设备，请稍候...</TD> -->
									<TD COLSPAN=10 HEIGHT=30 bgcolor='#FFFFFF' >
									<img src="../images/attention_2.gif" width="15" height="12">
										请输入序列号<font color="red">最后6位</font>查询
									</TD>
								</TR>
							</TABLE>
							</div>
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
				<table width="98%" border=0 cellspacing=1 cellpadding=2
					align="center" bgcolor="#999999">
					<tr>
						<th colspan="5">用户相关信息</th>
					</tr>
					<tr bgcolor="#FFFFFF">
					<span><img src="../images/attention_2.gif" width="15" height="12">&nbsp;宽带账号（路由用户）；专线号（静态IP用户）</span>
					<td class=column2 width="50%">用户宽带帐号/专线号：&nbsp;&nbsp;
						<input type="text" name="username" class="bk" value="<%=username%>" size="30">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" name="next" onclick="nextstep()" value=" 下一步 ">&nbsp;&nbsp;
					</td>
					</tr>
				</table>
			</TD>
		</TR>
		<TR>
			<TD HEIGHT=20>
			</TD>
		</TR>
		<TR style="display:none" id="customerinfo_str">
			<TD bgcolor=#ffffff colspan="5" bgcolor="#999999">
				<div id="customerinfo_table"></div>
			</TD>
		</TR>
		<TR style="display:none">
			<TD>
				<IFRAME ID="childFrm1" SRC="" STYLE="display:none"></IFRAME>
			</TD>
		</TR>
		
	</TABLE>

</FORM>
<iframe id="chkUserF" style = "display:none"></iframe>
<script language="javascript">
//document.all.dispTr.style.display="";
//document.all.userTable.innerHTML = "";
//getUserinstInfo();
</script>

<br>
<br>
<%@ include file="../foot.jsp"%>
