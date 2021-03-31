<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page import="java.util.Map,java.util.HashMap,com.linkage.litms.resource.FileSevice,com.linkage.litms.paramConfig.ParamInfoCORBA"%>

<%
request.setCharacterEncoding("GBK");

%>

<style type="text/css">
<!--
.btn_g {
	border: 1px solid #999999
}
//
-->
</style>

<jsp:include page="/share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="radio"/>
	<jsp:param name="jsFunctionName" value=""/>
</jsp:include>

<SCRIPT LANGUAGE="JavaScript">

function getmwbandInfo() {
	
	if (CheckForm()) {
		var device_id;
		var oSelect = document.all("device_id");
		for(var i=0; i<oSelect.length; i++) {
			if(oSelect[i].checked) {
				device_id = oSelect[i].value;
			}
		}
		if(oSelect.checked){
			device_id = oSelect.value;
		}
		page = "mwband_insert.jsp?device_id=" + device_id + "&gwType="+<%=request.getParameter("gw_type")%>;
		document.all("childFrm3").src = page;
	}
	
}
var i = 0;

function generateInput() {
	//alert(munValue);
	var munValue = document.all.mun_span_old.innerHTML;
	if (i % 2 == 0) {
		//document.all.mun_span_old.innerHTML="";
		document.all.mun_span_new.innerHTML = "<input type='text' value='' name='userNumInput'>";   
	} else {
		document.all.mun_span_old.innerHTML=munValue;
		document.all.mun_span_new.innerHTML = "";  
	}
	//i++;
}

function setInput(newValue) {
	document.all.mun_span_old.innerHTML=newValue;
	document.all.mun_span_new.innerHTML = "";  
	//i++;
}

function setMaxUserNum() {
	var newMunValue = document.all.userNumInput.value;
	//alert(newMunValue);

	var obj = document.all.userNumInput;
	if (obj.value == undefined || obj.value.trim() == "") {
		alert(" 请先输入新值 ");
	} else {
		showMsgDlg();
		url = "mwband_set.jsp?device_id="+document.all("device_id").value+"&newMunValue="+newMunValue+"&tt="+new Date().getTime();
		//alert(url);
		//send_request_setPW(url);
		document.all("childFrm4").src = url;
		disableBtns();
	}
	
}

function send_request_setPW(url) {
    request.open("GET", url, true);
    request.onreadystatechange = updatePageSetPW;
    request.send(null);
}

function updatePageSetPW() {
 	if (request.readyState == 4) {
        if (request.status == 200) {
        	closeMsgDlg();
        	//alert(request.responseText.trim());
			if (request.responseText.trim() == "-2") {
				alert("数据出错，请检查程序！");
				document.all.mun_span_new.innerHTML = "";
			} else if (request.responseText.trim() == "-1") {
				alert("设置失败,请确保设备连接正常！");
				document.all.mun_span_new.innerHTML = "";
			} else if(request.responseText.trim() == "0") {
				alert("设置成功，但数据库更新失败，请联系管理员！");
			} else if(request.responseText.trim() == "1") {
				alert("设置成功！");
				document.all.mun_span_old.innerHTML = document.all.mun_span_new.value.trim();
	        	document.all.mun_span_new.innerHTML = "";
			} else {
				alert("未知错误！");
				document.all.mun_span_new.innerHTML = "";
			}
			inspireBtns();
        } else {
        	closeMsgDlg();
            alert("出错！(status is " + request.status + ")");
            inspireBtns();
        }
 	}
}

function showMsgDlg(){
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
}
function closeMsgDlg(){
	PendingMessage.style.display="none";
}

function disableBtns() {
	document.all.compwGen.disabled = true;
	document.all.compwSet.disabled = true;
}

function inspireBtns() {
	document.all.compwGen.disabled = false;
	document.all.compwSet.disabled = false;
}

function CheckForm(){
	if(!deviceSelectedCheck()){
		alert("请选择设备！");
		return false;
	}
	return true;
}

</SCRIPT>

<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;
	border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif" border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle">
			<span id=txtLoading	style="font-size:12px;font-family: 宋体">正在设置新值，请稍等…</span>
		</td>
	</tr>
</table>
</center>
</div>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm">
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									多终端上网参数
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									多终端上网参数。
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<TR>
						<TH colspan="4" align="center">
							设备查询
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF" >
						<td colspan="4">
							<div id="selectDevice"><span>加载中....</span></div>
						</td>
					</TR>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">												
													<INPUT TYPE="button" value=" 获 取 " class=btn onclick="getmwbandInfo()">
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>

			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			<div id="tableView"></div>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm3 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm4 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>

<%@ include file="../foot.jsp"%>