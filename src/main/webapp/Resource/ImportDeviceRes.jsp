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
		alert("�����ϴ�CSV�ļ���");
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
							�豸��Դ
						</td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">
								�ϴ�CSV�ļ���WEB��������
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>�����豸</B></TH>
				</TR>
							
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">ѡ���ļ�</TD>
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
								<td colspan="2">�ļ���ʽ��</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">1��</td>
								<td>�ļ��еĵ�һ�в���⣬��ʽΪ���豸���ƣ�OUI���豸���кţ��豸�ͺ�ID������ά������</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">2��</td>
								<td>ÿ�а��յ�һ�е������Ŷ�Ӧ��ֵ����ֵ֮��ʹ�ö������</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">3��</td>
								<td>�����豸�ֶζ���Ĭ��ֵ���磺�豸��ȷ��״̬���豸������ʱ�䡢�豸����ACSĬ���û���������ȡ�</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">4��</td>
								<td>�豸���ͺ�ID���Դ���Դ����-->������Դ-->�豸�ͺ�ҳ��鿴</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td colspan="2"><a href="./importDevTemplate.jsp">�������ģ��</a></td>
							</tr>
						</table>
					</TD>
				</TR>
				<TR >
					<TD colspan="4" align="right" class=foot>
						<INPUT TYPE="submit" name="save" value=" �� ��" class=btn>
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