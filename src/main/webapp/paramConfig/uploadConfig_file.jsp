<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<jsp:useBean id="fileManage" scope="request" class="com.linkage.litms.filemanage.FileManage"/>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage" />

<%
	String deviceTypeList = versionManage.getDeviceTypeList("",false);
    int fileType =2;
    Cursor cursor = (Cursor)fileManage.getCursor(fileType);
    Map fields = cursor.getNext();
%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
	function checkform(){
		var filetype = 0;
		for(var i=0;i< document.frm.filestatus.length;i++)
		{
			if(document.frm.filestatus[i].checked)
			{
				filetype=document.frm.filestatus[i].value;
				break;
			}
		}
		document.frm.file_status.value=filetype;
		if(document.frm.devicetype_id.value == -1){
			alert("��ѡ���豸�ͺţ�");
			document.frm.devicetype_id.focus();
			return false;
		}
		if(!IsNull(document.frm.filename.value,"�ļ���")){
			document.frm.filename.focus();
			document.frm.filename.select();
			return false;
		}
	}
	function GoList(page){
		this.location = page;
	}
</SCRIPT>

<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD valign=top>
	<FORM NAME="frm" METHOD="post" ACTION="fileSave.jsp" onsubmit="return checkform()">
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
							����ʵ������
						</td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">
								�ϴ������ļ��������ļ���������
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>�ϴ������ļ�</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="15%" nowrap>����</TD>
					<TD class=column  width="35%">
						<INPUT TYPE="text" NAME="filename" class="bk" >&nbsp;
					</TD>
					<TD class=column align="right" width="15%">״̬</TD>
					<TD class=column width="35%">
						
						<input type="radio" name="filestatus" value="1" checked>�����
						<input type="radio" name="filestatus" value="2">δ���
						<input type="hidden" name="file_status" value="">
					</TD>					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >�ļ���С</TD>
					<TD class=column >
						<input type="text" name="file_size" value="" class="bk" readOnly >&nbsp;&nbsp;Byte(�ֽ�)				
					</TD>
					<TD class=column align="right" >�豸�ͺ�</TD>	
					<TD><%=deviceTypeList%>
					</TD>								
				</TR>
				<TR bgcolor="#FFFFFF" >
					<TD class=column align="right" nowrap>�洢·��</TD>
					<TD class=column colspan="3" nowrap>
						<select name="fileserver" class="bk">
						<%
							if(fields == null){
						%>
								<option value="-1">==����û�м�¼==</option>
						<%
							}else{
								while(fields != null){
						%>
									<option value="<%=fields.get("tomcat_url")%>|<%=fields.get("server_dir")%>|<%=fields.get("dir_id")%>"><%=fields.get("server_name")%>|<%=fields.get("tomcat_url")%>/<%=fields.get("server_dir")%></option>
						
						<%			
									fields = cursor.getNext();
								}
							}
						%>
						
						</select>&nbsp;&nbsp;
					</TD>


				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >�û���</TD>
					<TD class=column >
						<input type="text"  name="serUser" class="bk" value="">
					</TD>
					<TD class=column align="right" >����</TD>	
					<TD class=column >
						<input type="password" name="serPass" class="bk" value="">
					</TD>								
				</TR>				
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">ѡ���ļ�</TD>
					<TD colspan="3" class=column >
						<iframe name=uploadFrm FRAMEBORDER=0 SCROLLING=NO src="FileUpload.jsp?type=office" height="25" width=600>
						</iframe>
					</TD>
				</TR>


				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">����</TD>
					<TD class=column colspan="3">
						<textarea name="remark" cols="70" class="bk" rows="4"></textarea>
					</TD>
				</TR>

				<TR >
					<TD colspan="4" align="right" class=foot>
						<INPUT TYPE="submit" name="save" value=" �� ��" class=btn disabled>&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="action" value="add">
						<INPUT TYPE="button" name="reset" value=" �� д " class=btn onclick="GoList('uploadConfig_file.jsp')" >
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
<%@ include file="../foot.jsp"%>
