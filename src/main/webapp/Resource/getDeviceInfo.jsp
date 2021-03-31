<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
function showResult(request){
	$("_process").innerHTML="";
	eval(request.responseText);
}
function refresh(){
	window.location.href=window.location.href;
}

function getUserinstInfo(){
    var page="userinstInfo_xj.jsp?device_serialnumber="+document.frm.device_serialnumber.value
    		+"&refresh="+(new Date()).getTime();
	document.all("childFrm").src = page;
	document.frm.selDevice.value = "";
}

function goPage(offset){
	var page="userinstInfo_xj.jsp?device_serialnumber="+document.frm.device_serialnumber.value
    		+"&offset="+offset+"&refresh="+(new Date()).getTime();
	document.all("childFrm").src = page;
	document.frm.selDevice.value = "";
}
//-->
</SCRIPT>

<link rel="stylesheet" href="../css/listview.css" type="text/css">

<FORM NAME="frm" METHOD="post" ACTION="userinstSave.jsp"
	onsubmit="return CheckForm()">

	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD HEIGHT=20>
				&nbsp;
			</TD>
		</TR>
		<TR>
			<TD>
				<input type="hidden" name="status" value="All">
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
										请在“设备序列号”处输入序列号进行查询
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR bgcolor="#FFFFFF">
									<TH>家庭网关设备查询</TH>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD>
										设备序列号 :
										<input type="text" class="bk" name=device_serialnumber
											value="">
										<input type="button" class="btn" name="sort" value=" 查 询 "
											onclick="getUserinstInfo()">
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
			</TD>
		</TR>
	</TABLE>

</FORM>
