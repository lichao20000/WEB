<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.*"%>
<jsp:useBean id="serverManage" scope="request" class="com.linkage.litms.filemanage.ServerManage"/>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
	String gatherList =  deviceAct.getGatherList(session,"","",false);
%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
	function checkform(){

		if(!IsNull(document.frm.server_name.value,"��������")){
			document.frm.server_name.focus();
			document.frm.server_name.select();
			return false;
		}
		if(document.frm.gather_id.value==-1){
			alert("��ѡ��ɼ��㣡");
			document.frm.gather_id.focus();
			return false;		
		}	
		if(!IsNull(document.frm.inner_url.value,"�û�����URL")){
			document.frm.inner_url.focus();
			document.frm.inner_url.select();
			return false;
		}
		if(!IsNull(document.frm.outter_url.value,"�豸����URL")){
			document.frm.outter_url.focus();
			document.frm.outter_url.select();
			return false;
		}
		if(!IsNull(document.frm.server_dir.value,"���������·��")){
			document.frm.server_dir.focus();
			document.frm.server_dir.select();
			return false;
		}
		
		if(document.frm.file_type.value==-1){
			alert("��ѡ���ļ����������ͣ�");
			document.frm.file_type.focus();
			return false;		
		}
		
		if(!IsNull(document.frm.access_user.value,"�û���")){
			document.frm.access_user.focus();
			document.frm.access_user.select();
			return false;
		}
		
		if(!IsNull(document.frm.access_passwd.value,"����")){
			document.frm.access_passwd.focus();
			document.frm.access_passwd.select();
			return false;
		}
	}
</SCRIPT>
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD valign=top>
	<FORM NAME="frm" METHOD="post" ACTION="serverSave.jsp" onsubmit="return checkform()">
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
						�����ֹ������ļ���������·�����ļ�·����Ȼ�󴴽��ļ���������
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>����ļ�������</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="10%" nowrap>����</TD>
					<TD width="40%">
						<INPUT TYPE="text" NAME="server_name" class="bk" >&nbsp<font color="#FF0000">*</font>
					</TD>
					<TD class=column align="right" width="10%" nowrap>�ɼ���</TD>
					<TD width="40%">
							<%=gatherList%>&nbsp;<font color="#FF0000">*</font>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >�û�����URL</TD>
					<TD colspan="3">
						<input type="text" name="inner_url" value="" size="85" class="bk">&nbsp;<font color="#FF0000">*</font>(����http://192.168.28.192:9090/FileServer,���url�� ; ����)			
					</TD>				
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >�豸����URL</TD>
					<TD colspan="3">
						<input type="text" name="outter_url" size="85" value="" class="bk">&nbsp;<font color="#FF0000">*</font>(����http://218.94.131.205:9090/FileServer,���url�� ; ����)			
					</TD>	
				</TR>	
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >���·��</TD>
					<TD>
						<input type="text" name="server_dir" value="" class="bk">&nbsp;<font color="#FF0000">*</font>(����FILE/CONFIG)	
					</TD>
					<TD class=column nowrap align="right">�ļ�����������</TD>
					<TD>
						<select name="file_type" class="bk">
								 <option value="-1">--��ѡ��--</option>
								<option value="1">�汾�ļ�</option>
								<option value="2">�����ļ�</option>
								<option value="3">��־�ļ�</option>								
						</select>&nbsp<font color="#FF0000">*</font>
					</TD>
				</TR>

				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >�û���</TD>
					<TD>
						<input type="text" name="access_user" value="" class="bk">&nbsp;<font color="#FF0000">*</font>			
					</TD>				
					<TD class=column align="right" >����</TD>
					<TD>
						<input type="password" name="access_passwd" value="" class="bk">&nbsp;<font color="#FF0000">*</font>	
					</TD>
				</TR>

				<TR>
					<TD colspan="4" align="right" class=green_foot>
						<INPUT TYPE="submit" name="save" value=" �� �� " class=btn>&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="action" value="add">
						<INPUT TYPE="reset" name="reset" value=" �� д " class=btn>
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
		<TR><TD></TD></TR>
	</TABLE>
	</FORM>
	</TD>
</TR>
</TABLE>
<BR><BR>
<%@ include file="../foot.jsp"%>
