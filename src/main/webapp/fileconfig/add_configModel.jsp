<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
%>
<SCRIPT LANGUAGE="JavaScript">
function config_model()
{
	var winattr="center:yes;dialogWidth:800px;dialogHeight:550px;"
	var StringPara="",pos1=0,pos2=0;
	StringPara=window.showModalDialog("./configmodel.jsp","命令参数配置",winattr);
	if(StringPara!=null)
	{
		if(StringPara!="")
		document.frm.cmd_parameter.value=StringPara;
	}
}
</SCRIPT>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%> 
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD valign=top>
	<FORM NAME="frm" METHOD="post" ACTION="UserSave.jsp" onsubmit="return CheckForm()">
	<TABLE width="80%" border=0 cellspacing=0 cellpadding=0 align="center">
			
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>创建配置模板</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="10%" nowrap>模板名称</TD>
					<TD width="50%">
						<INPUT TYPE="text" NAME="device_type" class="bk">&nbsp;<font color="#FF0000">*</font>
					</TD>
					<TD class=column align="right" width="10%" nowrap>设备型号</TD>
					<TD width="30%">
						&nbsp;&nbsp;<select name="file_type">
								<option value="0">版本文件</option>
								<option value="1">配置模板文件</option>
						</select>&nbsp;&nbsp;&nbsp;
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">命令参数配置</TD>
					<TD colspan="3" >
						<input type="text" name="cmd_parameter" value="">
						&nbsp;&nbsp;<input type="button" name="upload" value="配置..." onClick="config_model();">
					</TD>
				</TR>

				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">模板描述</TD>
					<TD colspan="3">
						<textarea name="remark" cols="70" rows="4"></textarea>
					</TD>
				</TR>			
				
				<TR height="23">
					<TD colspan="4" align="right" class=foot>
						<INPUT TYPE="submit" value=" 保 存" class=btn>&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="action" value="add">
						<INPUT TYPE="reset" value=" 重 写 " class=btn>
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
		<TR height="23" nowrap><TD></TD></TR>
	</TABLE>
	</FORM>
	</TD>
</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
