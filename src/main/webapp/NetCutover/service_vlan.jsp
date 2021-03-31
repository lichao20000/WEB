<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function doAction(){
	var initcode = event.keyCode;
	if(initcode == 13){
		window.event.returnValue = false;
		var username = document.frm.account.value;
		if(username == ""){
		   document.frm.account.focus();
		   alert("请输入用户帐号!");
		   return false;
		} else {
				var page = "vlan_userInfo.jsp?username="+username + "&type=" + document.frm.type.value;
				document.all("childFrm").src = page;
		}
		
	}
}
function CheckForm(){
		
		if(Trim(document.frm.username.value) == ""){
			alert("请输入用户名称！");
			document.frm.username.focus();
			return false;
		}
		if(Trim(document.frm.pwd.value) == ""){
			alert("请输入用户密码！");
			document.frm.pwd.focus();
			return false;
		}
		if(Trim(document.frm.ssid1_username.value) == ""){
			alert("请输入SSID1用户名！");
			document.ssid1_username.pwd.focus();
			return false;
		}
		if(Trim(document.frm.ssid1_username.value) == ""){
			alert("请输入SSID1用户名！");
			document.ssid1_username.pwd.focus();
			return false;
		}
		if(Trim(document.frm.ssid1_username.value) == ""){
			alert("请输入SSID1用户名！");
			document.ssid1_username.pwd.focus();
			return false;
		}				
		if(Trim(document.frm.ssid1_username.value) == ""){
			alert("请输入SSID1用户名！");
			document.ssid1_username.pwd.focus();
			return false;
		}
		document.frm.action = "jt_Work_hand.jsp";
		document.frm.submit();
}

//-->
</SCRIPT>

<link rel="stylesheet" href="../css/listview.css" type="text/css">
<FORM name="frm" action=""  method="post">
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
											工单管理
										</div>
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										手工输入帐号，回车获取相关信息.&nbsp;&nbsp;
										注：带有
										<font color=red>*</font>必填
									</td>

								</tr>
							</table>
						</TD>
					</TR>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
								<TR>
									<TH colspan="4">手工设置VLAN</TH>
								</TR>							
								<TR bgcolor="#ffffff">
									<TD align="right" width="20%">
										业务类型
									</TD>
									<TD align="left" width="30%">
										<SELECT NAME="type" class=bk>
											<OPTION value="581">
												设置无线VLAN
											</OPTION>													
										</SELECT>
									</TD>
									<TD align="right" width="20%">
										用户帐号
									</TD>
									<TD width="30%">
										<input type="text" name="account"  class="bk" ONKEYDOWN="doAction(this);">&nbsp;&nbsp;
											<font color=red>*</font>
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

		<TR>
			<TD HEIGHT=20>
				&nbsp;
				<IFRAME ID=childFrm name="childFrm" STYLE="display:none"></IFRAME>
			</TD>
		</TR>
	</TABLE>
</form>
<%@ include file="../foot.jsp"%>
