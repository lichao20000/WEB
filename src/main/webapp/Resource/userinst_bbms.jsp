<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="LipossGlobals" scope = "request" class = "com.linkage.litms.LipossGlobals"></jsp:useBean>
<%
	request.setCharacterEncoding("GBK");
	String title = "��ҵ�����ֹ���װ";
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
	//�Ƿ��ж�
	

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

//���ݸ�ѡ��ѡ��״̬����ȡ�豸id
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
//ȫѡ
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
		alert('���������١����6λ�����кŽ��в�ѯ!');
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
		alert('����ѡ��һ���豸');
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
		alert('����ѡ��һ���豸');
		return false;
	}
	if (document.frm.username.value == ""){
		alert('����д�û�����ʺ�/ר�ߺ�');
		return false;
	}
	var message = "��ȷ�ϣ��û��ʺţ�"+document.frm.username.value+"���豸���кţ�"+serialnumber;
	if (!confirm(message+'���Ƿ��������?')){
		return false;
	}
	document.frm.next.disabled = "true";
	document.frm.save_btn.disabled = "true";
}

function checkUsernameEmpty() {
	if (document.frm.username.value == ""){
		alert('����д�û�����ʺ�/ר�ߺ�');
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
										�ֳ���װ
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										���ڡ��豸���кš�����������<font color="red">���6λ</font>���кŽ��в�ѯ
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
										�豸���к� :
										<input type="text" class="bk" name=device_serialnumber
											value="">
										<input type="button" class="btn" name="sort" value=" �� ѯ "
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
									<!-- <TD COLSPAN=10 HEIGHT=30 bgcolor='#FFFFFF'>��������δ�󶨵��豸�����Ժ�...</TD> -->
									<TD COLSPAN=10 HEIGHT=30 bgcolor='#FFFFFF' >
									<img src="../images/attention_2.gif" width="15" height="12">
										���������к�<font color="red">���6λ</font>��ѯ
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
						<th colspan="5">�û������Ϣ</th>
					</tr>
					<tr bgcolor="#FFFFFF">
					<span><img src="../images/attention_2.gif" width="15" height="12">&nbsp;����˺ţ�·���û�����ר�ߺţ���̬IP�û���</span>
					<td class=column2 width="50%">�û�����ʺ�/ר�ߺţ�&nbsp;&nbsp;
						<input type="text" name="username" class="bk" value="<%=username%>" size="30">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" name="next" onclick="nextstep()" value=" ��һ�� ">&nbsp;&nbsp;
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
