<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%
	request.setCharacterEncoding("GBK");
	String gw_type = request.getParameter("gw_type");
%>

<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">

function query(){

	var device_serialnumber = document.frm.device_serialnumber.value.trim();
	
    if(device_serialnumber.length < 6){
            alert("请输入至少最后6位设备序列号 !");
            document.frm.device_serialnumber.focus();
            return;
    }
    var deviceSelect_url = "queryDeviceByAllCity!getDeviceByAllCityInBBMS.action";
	
	showMsgDlg();
	
	//作为首次进入加载
	$.post(deviceSelect_url,{
		device_serialnumber:device_serialnumber
	},function(ajax){
		$("div[@id='idBody']").html("");
		$("div[@id='idBody']").append(ajax);
		
		closeMsgDlg();
	});
	
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

</SCRIPT>

<link rel="stylesheet" href="../css/listview.css" type="text/css">

<div id="PendingMessage"
        style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
	<center>
		<table border="0">
			<tr>
				<td valign="middle"><img src="../images/cursor_hourglas.gif" border="0" WIDTH="30" HEIGHT="30"></td>
				<td>&nbsp;&nbsp;</td>
 				<td valign="middle">
 					<span id=txtLoading style="font-size:14px;font-family: 宋体">正在查询，请稍等...</span>
 				</td>
 			</tr>
 		</table>
 	</center>
</div>
<FORM name="frm" action="" method="post">
	<input type="hidden" name="gw_type" value="">
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD HEIGHT="20">
				&nbsp;
			</TD> 
		</TR>
		<TR>
			<TD>
				<TABLE width="98%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<div align="center" class="title_bigwhite">
											设备检索
										</div>
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										查询设备是否已绑（全省范围内查询）
									</td>
								</tr>
							</table>
						</TD>
					</TR>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR>
									<TH colspan="3">设备检索</TH>
								</TR>
								<TR bgcolor="#ffffff">
									<TD align="right" width="20%">
										设备序列号: 
									</TD>
									<TD align="left" width="50%">
										<input type="text" name="device_serialnumber" class="bk" size="30">&nbsp;&nbsp;<font color="red">*</font>&nbsp;&nbsp;(请输入至少最后6位.)
									</TD>
									<TD align="center" >
										<input type="button" class="btn" name="button" value=" 查 询 " style="display:" onclick="query();">&nbsp;
									</TD>
								</TR>
								<TR>
									<TD colspan="4" height="20" bgcolor="#ffffff">
											&nbsp;
									</TD>
								</TR>
							</TABLE>
							<div id="idBody">
							</div>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</FORM>
<%@ include file="../foot.jsp"%>