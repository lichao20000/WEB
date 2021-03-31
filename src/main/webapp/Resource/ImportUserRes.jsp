<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function CheckForm(){
	if(document.all("filename").value == ""){
		alert("请先上传CVS文件！");
		return false;
	}
	return true;
}
//-->
</SCRIPT>
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD valign=top>
	<FORM NAME="frm" METHOD="post" ACTION="ImportUserResSave.jsp" onsubmit="return CheckForm()">
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD HEIGHT=20>
				&nbsp;
			</TD>
		</TR>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							用户资源
						</td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">
								上传用户文件到用户文件服务器。
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>添加用户文件</B></TH>
				</TR>
							
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;选择文件</TD>
					<TD class=column colspan="3" >
						<iframe name=uploadFrm FRAMEBORDER=0 SCROLLING=NO src="FileUploadForm.jsp?type=office" height="25" width=600>
						</iframe>
					</TD>
				</TR>
				<TR>
					<TD  colspan="4" align="center" bgcolor=#ffffff>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="2%">&nbsp;</td>
								<td colspan="2">文件格式：</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">1、</td>
								<td>文件中的第一行不入库，格式为：厂商oui设备序列号、宽带帐号、宽带密码、Adsl绑定电话、开通时间、最大下行可达速率、上行承诺速率、允许用户上网数、VlanID号、VpiID号、VciID号、属地名称、局向标识、小区标识、联系人、业务类型、联系电话、用户类型、用户状态</td>
							</tr>
							<tr><td colspan="3" height="5"></td></tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">2、</td>
								<td>每行按照第一行的列名排对应的值，各值之间使用逗号相隔</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">3、</td>
								<td>宽带帐号、宽带密码、VpiID号、VciID号、用户类型、用户状态、业务类型 为必填项</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">4、</td>
								<td>开通时间的格式为：YYYY-MM-DD HH:MI:SS</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">5、</td>
								<td>用户类型、用户状态请根据说明填写id，具体对应信息见模板。</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td colspan="2"><a href="./importUserTemplate.jsp">点击下载模板</a></td>
							</tr>
						</table>
					</TD>
				</TR>
				<TR >
					<TD colspan="4" align="right" class=foot>
						<INPUT TYPE="submit" name="save" value=" 保 存" class=btn>
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
		<TR><TD></TD></TR>
	</TABLE>
	<input type="hidden" name="filename" value="">
	</FORM>
	</TD>
</TR>
</TABLE>
<br>
<br>
<%@ include file="../foot.jsp"%>