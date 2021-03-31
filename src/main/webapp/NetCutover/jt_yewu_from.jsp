<%--
FileName	: devDesc_add.jsp
Author		: liuli
Date		: 2006年6月29日
Desc		: 新版本流量.
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.FileSevice"/>
<%
request.setCharacterEncoding("gb2312");
String strData1 = DeviceAct.getHtml1(request);
String strData = DeviceAct.getHtml(request);
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--

function Edit(_lg_id,_lg_name,_lg_desc){
	frm.lg_id.value=_lg_id;
	frm.lg_name.value=_lg_name;
	frm.lg_desc.value=_lg_desc;
	document.frm.action.value="edit";
	// actLabel.innerHTML = '编辑';
}

function delWarn(){
	if(confirm("真的要删除该业务吗？\n本操作所删除的不能恢复！！！")){
		return true;
	}
	else{
		return false;
	}
}

function CheckForm(){
	var obj = document.frm;
	if(!IsNull(obj.lg_name.value,'业务名称')){
		obj.lg_name.focus();
		obj.lg_name.select();
		return false;
	}
	else{
		return true;
	}	
}

//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="jt_sevice_from_save.jsp" onsubmit="return CheckForm()">
        <table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">         
          <tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">用户新增业务</div>
						</td>
						<td><img src="../images/attention_2.gif" width="15" height="12">用户新增业务列表信息</td>
					</tr>
				</table>
				</td>
			</tr>
          <tr>         
            <td><TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
                <TR> 
                  <TD bgcolor=#999999> <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                      <TR> 
                      <TH>业务编号</TH>
                      <TH>业务名称</TH>
                      <TH>业务描述</TH>
                      <TH>操作</TH>
                      </TR>
                      <%=strData%> </TABLE></TD>
                </TR>
              </TABLE></td>
          </tr>
        </table>
        <BR>
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<Td colspan="4" align="center"  class=column>用户新增业务信息</Td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="right">业务名称:</TD>
						<TD colspan=3>
						<INPUT TYPE="hidden" NAME="lg_id" value="">						
						<INPUT TYPE="text" NAME="lg_name" maxlength=20 class=bk size=20 >&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="right" >业务描述:</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="lg_desc"  maxlength=50 class=bk size=40></TD>
					</TR>
					<TR >
						<TD colspan="4" align="right"  class=column>
							
							<INPUT TYPE="submit" value=" 保 存 " class=btn>&nbsp;
							<INPUT TYPE="hidden" name="action" value="add">
							&nbsp;
							<INPUT TYPE="reset" value=" 重 写 " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
