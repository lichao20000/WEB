<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");

			%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--

//--> 
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle"><span id=txtLoading style="font-size:14px;">请稍等······</span></td>
	</tr>
</table>
</center>
</div>
<FORM NAME="frm" target="childFrm" action="" method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">业务代码文件上传</div>
						</td>
						<td></td>
					</tr>
				</table>
				</td>
			</tr>
			<TR>
				<TD bgcolor=#999999 id="idBody">
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">

					<TR bgcolor="#ffffff">
						<TH colspan=2>上传业务代码文件</TH>
					</TR>
					<TR>
						<TD class=column>文件格式</TD>
						<TD class=column><select name="dengji" class="form_kuang">
							<option value="0">CSV</option>
							<!--<option value="1">其它格式</option> -->
						</select></TD>
					</TR>
					<TR bgcolor="#ffffff" colspan=2>
						<TD class=column>文件上传</TD>
						<TD class=column><iframe name=uploadFrm FRAMEBORDER=0 SCROLLING=NO
							src="FileUpload_Form.jsp?type=office" height="25" width=600></iframe>
						</TD>
					</TR>
					<tr bgcolor="#ffffff">
						<td colspan=2>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="2%">&nbsp;</td>
								<td colspan="2">文件格式：</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">1、</td>
								<td>文件中的第一行不入库，格式为：业务代码，业务编号（数字），适用配置模板ID（数字），设备型号ID（数字），关联城市列表（;城市ID;城市ID;......）</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">2、</td>
								<td>上面的字段中，只有关联城市列表允许不填，其它字段是必填项；城市列表不填的情况下，但设备型号ID后面还是要加逗号的</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">3、</td>
								<td>每行按照第一行的列名排对应的值，各值之间使用逗号相隔</td>
							</tr>
						</table>
						</td>
					</tr>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm name="childFrm"
			SRC="about:blank" STYLE="display:none"></IFRAME></TD>
	</TR>
</TABLE>
</FORM>
<%@ include file="../foot.jsp"%>
