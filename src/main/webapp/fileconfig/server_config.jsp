<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<jsp:useBean id="serverManage" scope="request" class="com.linkage.litms.filemanage.ServerManage"/>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<style>
/* .mybutton{
	cursor: pointer;
    border-radius: 4px;
    border: 1px solid #a6a9a8;
    background-color: #f2f3f4;
} */
</style>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

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

// �༭����
var action = "add";

// ���
function addfile(){
	action = "add";
	$("#editType").html("����ļ�������");
	$("#editTable").show();
}
// �༭
function edit(dir){
	action = "edit";
	$("#editType").html("�༭�ļ�������");
	dir = eval("(" + dir + ")");
	// id
	document.editfrm.dir_id.value = dir.dir_id;
	// ����������
	document.editfrm.server_name.value = dir.server_name;
	// �ɼ���
	document.editfrm.gather_id.value = dir.gather_id;
	// �û�����URL	
	document.editfrm.inner_url.value = dir.inner_url;
	// �豸����URL	
	document.editfrm.outter_url.value = dir.outter_url;
	// ���·��
	document.editfrm.server_dir.value = dir.server_dir;
	// �ļ�����������
	document.editfrm.file_type.value = dir.file_type;
	// �û���	
	document.editfrm.access_user.value = dir.access_user;
	// ����
	document.editfrm.access_passwd.value = dir.access_passwd;
	
	$("#editTable").show();
}

// ����
function save(){
	// У��
	if(!checkform()){
		return;
	}
	$.ajax({
		type:"Post",
		url:"serverSave.jsp?action="+action,
		data: $("#editfrm").serialize(),
		dataType:'text',
		success:function(data){
			data = data.trim();
			if(data.indexOf('�ɹ�') !== -1){
				$("#editTable").hide();
				queryfile();
			}else{
				alert(data);
			}
		},
		error:function(e){
			alert("�������쳣");
			console.info("e",e);
		}
	});
}

// ɾ��
function del(dir_id){
	action = "delete";
	if(confirm("���Ҫɾ�����ļ���������\n��������ɾ���Ĳ��ָܻ�������")){
		$.ajax({
			type:"Post",
			url:"serverSave.jsp",
			data:{
				action: 'delete',
				dir_id: dir_id
			},
			dataType:'text',
			success:function(data){
				data = data.trim();
				if(data.indexOf('�ɹ�') !== -1){
					queryfile();
				}else{
					alert(data);
				}
			},
			error:function(e){
				alert("�������쳣");
				console.info("e",e);
			}
		});
		
	}else{
		return false;
	}
}

function checkform(){
	
	if(!IsNull(document.editfrm.server_name.value,"��������")){
		document.editfrm.server_name.focus();
		document.editfrm.server_name.select();
		return false;
	}
	if(document.editfrm.gather_id.value==-1){
		alert("��ѡ��ɼ��㣡");
		document.editfrm.gather_id.focus();
		return false;		
	}	
	if(!IsNull(document.editfrm.inner_url.value,"������IP")){
		document.editfrm.inner_url.focus();
		document.editfrm.inner_url.select();
		return false;
	}
	if(!IsNull(document.editfrm.server_dir.value,"���������·��")){
		document.editfrm.server_dir.focus();
		document.editfrm.server_dir.select();
		return false;
	}
	if(!IsNull(document.editfrm.outter_url.value,"WEB������URL")){
		document.editfrm.outter_url.focus();
		document.editfrm.outter_url.select();
		return false;
	}	
	if(document.editfrm.file_type.value==-1){
		alert("��ѡ���ļ����������ͣ�");
		document.editfrm.file_type.focus();
		return false;		
	}
	if(!IsNull(document.editfrm.access_user.value,"�û���")){
		document.editfrm.access_user.focus();
		document.editfrm.access_user.select();
		return false;
	}
	if(!IsNull(document.editfrm.access_passwd.value,"����")){
		document.editfrm.access_passwd.focus();
		document.editfrm.access_passwd.select();
		return false;
	}
	return true;
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
						<INPUT TYPE="text" name="server_name" class="bk" value="">
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
						<INPUT TYPE="button" value="�� ��" class=jianbian onclick="addfile()"/>&nbsp;&nbsp;
						<INPUT TYPE="button" value="�� ѯ " class=jianbian onclick="queryfile()"/>
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

<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0" id="editTable" style="display: none">
	<TR>
		<TD valign=top>
			<FORM NAME="editfrm" METHOD="post" id="editfrm">
				<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR>
								<TH  colspan="4" align="center"><B id="editType">����ļ�������</B></TH>
							</TR>
							<!-- �ļ�������id -->
							<INPUT TYPE="hidden" NAME="dir_id" value=""/>
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right" width="10%" nowrap>����</TD>
								<TD width="40%">
									<INPUT TYPE="text" NAME="server_name" class="bk" style="width: 225px">
									&nbsp<font color="#FF0000">*</font>
								</TD>
								<TD class=column align="right" width="10%" nowrap>�ɼ���</TD>
								<TD width="40%">
										<%=gatherList%>&nbsp;<font color="#FF0000">*</font>
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right" >�û�����URL</TD>
								<TD colspan="3">
									<input type="text" name="inner_url" value="" size="85" class="bk" style="width: 465px">
									&nbsp;<font color="#FF0000">*</font>(����http://192.168.28.192:9090/FileServer,���url�� ; ����)			
								</TD>				
							</TR>
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right" >�豸����URL</TD>
								<TD colspan="3">
									<input type="text" name="outter_url" size="85" value="" class="bk" style="width: 465px">
									&nbsp;<font color="#FF0000">*</font>(����http://218.94.131.205:9090/FileServer,���url�� ; ����)			
								</TD>	
							</TR>	
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right" >���·��</TD>
								<TD>
									<input type="text" name="server_dir" value="" class="bk" style="width: 225px">
									&nbsp;<font color="#FF0000">*</font>(����FILE/CONFIG)	
								</TD>
								<TD class=column nowrap align="right">�ļ�����������</TD>
								<TD>
									<select name="file_type" class="bk" style="width: 225px">
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
									<input type="text" name="access_user" value="" class="bk" style="width: 225px">
									&nbsp;<font color="#FF0000">*</font>			
								</TD>				
								<TD class=column align="right" >����</TD>
								<TD>
									<input type="password" name="access_passwd" value="" class="bk" style="width: 225px">
									&nbsp;<font color="#FF0000">*</font>	
								</TD>
							</TR>
			
							<TR>
								<TD colspan="4" align="right" class=green_foot>
									<INPUT TYPE="button" onclick="save()" value="�� �� " class=jianbian>&nbsp;&nbsp;
									<INPUT TYPE="reset" name="reset" value="�� д " class=jianbian>
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

<script>
queryfile();
</script>

<%@ include file="../foot.jsp"%>
