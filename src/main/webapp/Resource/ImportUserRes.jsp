<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function CheckForm(){
	if(document.all("filename").value == ""){
		alert("�����ϴ�CVS�ļ���");
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
							�û���Դ
						</td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">
								�ϴ��û��ļ����û��ļ���������
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>����û��ļ�</B></TH>
				</TR>
							
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ѡ���ļ�</TD>
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
								<td>�ļ��еĵ�һ�в���⣬��ʽΪ������oui�豸���кš�����ʺš�������롢Adsl�󶨵绰����ͨʱ�䡢������пɴ����ʡ����г�ŵ���ʡ������û���������VlanID�š�VpiID�š�VciID�š��������ơ������ʶ��С����ʶ����ϵ�ˡ�ҵ�����͡���ϵ�绰���û����͡��û�״̬</td>
							</tr>
							<tr><td colspan="3" height="5"></td></tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">2��</td>
								<td>ÿ�а��յ�һ�е������Ŷ�Ӧ��ֵ����ֵ֮��ʹ�ö������</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">3��</td>
								<td>����ʺš�������롢VpiID�š�VciID�š��û����͡��û�״̬��ҵ������ Ϊ������</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">4��</td>
								<td>��ͨʱ��ĸ�ʽΪ��YYYY-MM-DD HH:MI:SS</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td width="2%">5��</td>
								<td>�û����͡��û�״̬�����˵����дid�������Ӧ��Ϣ��ģ�塣</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td colspan="2"><a href="./importUserTemplate.jsp">�������ģ��</a></td>
							</tr>
						</table>
					</TD>
				</TR>
				<TR >
					<TD colspan="4" align="right" class=foot>
						<INPUT TYPE="submit" name="save" value=" �� ��" class=btn>
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