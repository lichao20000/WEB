<%--
FileName	: devDesc_add.jsp
Author		: liuli
Date		: 2006��6��29��
Desc		: �°汾����.
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
	// actLabel.innerHTML = '�༭';
}

function delWarn(){
	if(confirm("���Ҫɾ����ҵ����\n��������ɾ���Ĳ��ָܻ�������")){
		return true;
	}
	else{
		return false;
	}
}

function CheckForm(){
	var obj = document.frm;
	if(!IsNull(obj.lg_name.value,'ҵ������')){
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
						<div align="center" class="title_bigwhite">�û�����ҵ��</div>
						</td>
						<td><img src="../images/attention_2.gif" width="15" height="12">�û�����ҵ���б���Ϣ</td>
					</tr>
				</table>
				</td>
			</tr>
          <tr>         
            <td><TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
                <TR> 
                  <TD bgcolor=#999999> <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                      <TR> 
                      <TH>ҵ����</TH>
                      <TH>ҵ������</TH>
                      <TH>ҵ������</TH>
                      <TH>����</TH>
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
						<Td colspan="4" align="center"  class=column>�û�����ҵ����Ϣ</Td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="right">ҵ������:</TD>
						<TD colspan=3>
						<INPUT TYPE="hidden" NAME="lg_id" value="">						
						<INPUT TYPE="text" NAME="lg_name" maxlength=20 class=bk size=20 >&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="right" >ҵ������:</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="lg_desc"  maxlength=50 class=bk size=40></TD>
					</TR>
					<TR >
						<TD colspan="4" align="right"  class=column>
							
							<INPUT TYPE="submit" value=" �� �� " class=btn>&nbsp;
							<INPUT TYPE="hidden" name="action" value="add">
							&nbsp;
							<INPUT TYPE="reset" value=" �� д " class=btn>
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
