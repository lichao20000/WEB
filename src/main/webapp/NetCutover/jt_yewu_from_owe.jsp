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
	if(confirm("真的要删除该链路组吗？\n本操作所删除的不能恢复！！！")){
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
						<div align="center" class="title_bigwhite">系统默认业务列表</div>
						</td>
						<td><img src="../images/attention_2.gif" width="15" height="12">系统默认业务列表</td>
					</tr>
				</table>
				</td>
			</tr>
          <tr>         
            <td><TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
                <TR> 
                  <TD bgcolor=#999999> <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                      <TR> 
                      <TH nowrap>业务编号</TH>
                      <TH nowrap>业务名称</TH>
                      <TH nowrap>业务描述</TH>
                      <TH nowrap>操作</TH>
                      </TR>
                      <%=strData1%> </TABLE></TD>
                </TR>
              </TABLE></td>
          </tr>
        </table>
	</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
