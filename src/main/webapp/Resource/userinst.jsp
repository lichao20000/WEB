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

	String title = "�ֹ���װ";
	
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
	if (tmp.length < 4){
		alert('����������4λ���кŽ��в�ѯ!');
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
		alert('����д�û��ʺ�');
		return false;
	}
	
//	if(ischeck != null && ischeck == 1){
//		if(document.frm.ckuser.value == "0"){
//			alert("�û��������ڣ���������û�");
//			return false;
//
//		}
//	}
	
	var message = "��ȷ�ϣ��û��ʺţ�"+document.frm.username.value+"���豸���кţ�"+serialnumber;
	if (!confirm(message+'���Ƿ��������?')){
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
										�ֳ���װ
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										���ڡ��豸���кš�����������4λ������в�ѯ
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
							�û��ʻ�:
						</td>
						<td width="25%" bgcolor=#FFFFFF>
							<input type="text" name="username" class="bk" value="<%=username%>">
						</td>
						<td bgcolor=#FFFFFF>
							
							<input type="submit" name="submit" value=" �� �� ">
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
