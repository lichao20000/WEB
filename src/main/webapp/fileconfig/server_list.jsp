<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="serverManage" scope="request" class="com.linkage.litms.filemanage.ServerManage"/>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%
	String gatherList =  deviceAct.getGatherList(session,"","",false);
%>
<SCRIPT LANGUAGE="JavaScript">
function queryfile()
{
	document.all("userTable").innerHTML="<TABLE border=0 cellspacing=1 cellpadding=2 width='100%'><tr><td bgcolor='#ffffff'>����ͳ��,���Ժ�......</td></tr></table>";
	var page="server_listdata.jsp?server_name="+ document.frm.server_name.value+"&gather_id=" + document.frm.gather_id.value
	+"&serverIp="+document.frm.serverIp.value+"&server_type="+document.frm.server_type.value;
	document.all("childFrm").src = page;
}
function delWarn(){
	if(confirm("���Ҫɾ�����ļ���������\n��������ɾ���Ĳ��ָܻ�������")){
		return true;
	}
	else{
		return false;
	}
}
</SCRIPT>

<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD valign=top>
	<FORM NAME="frm" METHOD="post">
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
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
						�ļ�������
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
							&nbsp;&nbsp;���������ơ�������URL��ѯ֧��ģ����ѯ
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>�ļ���������ѯ</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="15%" nowrap>����������</TD>
					<TD width="35%" class=column>
						<INPUT TYPE="text" name="server_name" class="bk">
					</TD>
					<TD class=column align="right" width="15%" nowrap>�ɼ���</TD>
					<TD width="35%" class=column>
						<%=gatherList%>
					</TD>
				</TR>

				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">������URL</TD>
					<TD class=column>
						<input type="text" name="serverIp" class="bk" value="">							
					</TD>
					<TD class=column align="right">�ļ�����</TD>
					<TD class=column>
							<select name="server_type" class="bk">
								<option value="-1">--��ѡ��--</option>
								<option value="1">�汾�ļ�</option>
								<option value="2">�����ļ�</option>	
								<option value="3">��־�ļ�</option>
							</select>				
					</TD>
				</TR>		
				<TR height="23">
					<TD colspan="4" align="right" class=green_foot>
						<INPUT TYPE="button" value=" �� ѯ " class=btn onclick="queryfile()">
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
		<TR height="23" nowrap><TD></TD></TR>
		<TR style="display:none" id="dispTr">
			<TD><span id="userTable"></span></TD>
		</TR>
		<TR>
			<TD HEIGHT=20>&nbsp; 
				<IFRAME ID="childFrm" SRC="" STYLE="display:none">
				</IFRAME>
			</TD>
			<TD HEIGHT=20>&nbsp; 
				<IFRAME ID="childFrm1" SRC="" STYLE="display:none">
				</IFRAME>
			</TD>
		</TR>		
	</TABLE>
	</FORM>
	</TD>
</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
