<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<% 
String gw_type = request.getParameter("gw_type");
%>
<SCRIPT LANGUAGE="JavaScript">

function CheckForm(){
	if(document.all("filename").value == ""){
		alert("请先上传CSV文件！");
		return false;
	}
	return true;
}
//-->
</SCRIPT>
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD valign=top>
	<FORM NAME="frm" METHOD="post" ACTION="ImportDeviceResSave.jsp" onsubmit="return CheckForm()">
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
							设备资源
						</td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">
								上传CSV文件到WEB服务器。
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>导入设备</B></TH>
				</TR>
							
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">选择文件</TD>
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
								<td>文件中的第一行不入库，格式为：设备名称，OUI，设备序列号，设备型号ID，电信维护密码</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">2、</td>
								<td>每行按照第一行的列名排对应的值，各值之间使用逗号相隔</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">3、</td>
								<td>其他设备字段都有默认值，如：设备的确认状态、设备最后更新时间、设备连接ACS默认用户名和密码等。</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">4、</td>
								<td>设备的型号ID可以从资源管理-->基础资源-->设备型号页面查看</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td colspan="2"><a href="./importDevTemplate.jsp">点击下载模板</a></td>
							</tr>
						</table>
					</TD>
				</TR>
				<TR >
					<TD colspan="4" align="right" class=foot>
						<INPUT TYPE="submit" name="save" value=" 保 存" class=btn>
						<INPUT TYPE="hidden" name="gw_type" value=<%= gw_type%>>
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
	
	</TABLE>
	<input type="hidden" name="filename" value="">
	</FORM>
	</TD>
</TR>
<TR><TD height="40"></TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>