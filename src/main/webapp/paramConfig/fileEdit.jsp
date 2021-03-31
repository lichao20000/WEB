<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<jsp:useBean id="fileManage" scope="request" class="com.linkage.litms.filemanage.FileManage"/>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage" />
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
    //�汾�ļ���Ϣ      
    Map fields = (Map)fileManage.getConfigFileMap(request);
    
    Map area_nameMap = DeviceAct.getAreaIdMapName();
    String verconfile_id = "";
	String filename = "";
	String devicetype_id = "";
	String filesize = "";
	String filestatus ="";
	String server_name = "";
	String server_tomcat = "";
	String server_path = "";
	String remark = "";
	String area_name = "";
	String area_id = "";
	if(null != fields){
	    verconfile_id = (String)fields.get("verconfile_id");
		filename = (String)fields.get("verconfile_name");
		area_id = (String)fields.get("area_id");
		devicetype_id = (String)fields.get("devicetype_id");
		filesize = (String)fields.get("verconfile_size");
		filestatus = (String)fields.get("verconfile_status");
		server_name = (String)fields.get("server_name");
		//server_tomcat = (String)fields.get("tomcat_url");
		server_path = (String)fields.get("server_dir");
		remark = (String)fields.get("verconfile_description");
	}
	area_name = (String)area_nameMap.get(area_id);
	if(area_name == null){
		area_name = "";
	}
	//�豸�ͺ�������
	String deviceTypeList = versionManage.getDeviceTypeList(devicetype_id,false);
%>
<style>
NOBR.BT
{
	padding-right: 6px;
	padding-left: 6px;
	padding-top: 2px;
	border-top: #ffffff 1px solid;
	border-bottom: #ffffff 1px solid;
	border-right: #ffffff 1px solid;
	border-left: #ffffff 1px solid;
	cursor:hand;
}

NOBR.BTOver{
	padding-right: 6px;
	padding-left: 6px;
	padding-top: 2px;
	border-top: #316AC5 1px solid;
	border-bottom: #316AC5 1px solid;
	border-right: #316AC5 1px solid;
	border-left: #316AC5 1px solid;
	cursor:hand;
	background-color: #C1D2EE;
}
</style>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
function checkform()
{
	var filetype=0;
	var inst_area = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
	if("sx_lt"!=inst_area){
		for(var i=0;i< document.frm.filestatus.length;i++)
		{
			if(document.frm.filestatus[i].checked)
			{
				filetype=document.frm.filestatus[i].value;
				break;
			}
		}
		document.frm.file_status.value=filetype;
		
		if(document.frm.area_name.value == ""){
			alert("��ѡ�������");
			document.frm.area_name.focus();
			return false;		
		}
	}	
	
	if(!IsNull(document.frm.filename.value,"�ļ���")){
		document.frm.filename.focus();
		document.frm.filename.select();
		return false;
	}
	
	if(document.frm.devicetype_id.value==-1){
		alert("��ѡ���豸�ͺţ�");
		document.frm.devicetype_id.focus();
		return false;		
	}
}

function GoList(page)
{
	this.location = page;
}

function areaSelect()
{
	var page = "../system/AreaSelect.jsp?area_pid="+<%=user.getAreaId()%>+"&width=360";
	window.open(page,"","left=20,top=20,width=360,height=450,resizable=no,scrollbars=no");	
}
</SCRIPT>

<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD valign=top>
	<FORM NAME="frm" METHOD="post" ACTION="fileSave.jsp" onsubmit="return checkform()">
	<TABLE width="80%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR><TD HEIGHT=20>&nbsp;</TD></TR>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" class="title_bigwhite" align="center">
							�����ļ�����
						</td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">
							�޸��Ѿ����ڵ������ļ���
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH colspan="4" align="center">�޸������ļ�</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="10%" nowrap>����</TD>
					<TD width="50%">
						<INPUT TYPE="text" NAME="filename" class="bk" value="<%=filename%>" readOnly>&nbsp;
					</TD>
					<TD class=column align="right" width="10%" nowrap>�豸�ͺ�</TD>
					<TD width="30%"><%=deviceTypeList%></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >�ļ���С</TD>
					<ms:inArea areaCode="sx_lt" notInMode="false">
					<TD colspan="3">
						<input type="text" name="file_size" value="<%=filesize%>" class="bk" readOnly >&nbsp;&nbsp;Byte(�ֽ�)				
						<input type="hidden"  name="area_id" value="<%=area_id%>">
						<input type="hidden"  name="file_status" value="<%=filestatus%>">
					</TD>
					</ms:inArea>
					<ms:inArea areaCode="sx_lt" notInMode="true">
					<TD>
						<input type="text" name="file_size" value="<%=filesize%>" class="bk" readOnly >&nbsp;&nbsp;Byte(�ֽ�)				
					</TD>			
					<TD class=column align="right" >״̬</TD>
					<%
						String strCheck1 = "";
						String strCheck2 = "";
						if(filestatus.equals("1")){
							strCheck1 = "checked";
						}else{
							strCheck2 = "checked";
						}					
					%>
					<TD>
						<input type="radio"  name="filestatus" value="1" <%=strCheck1%>>�����
						<input type="radio"  name="filestatus" value="2" <%=strCheck2%>>δ���
						<input type="hidden" name="file_status" value="">
					</TD>
					</ms:inArea>
				</TR>
				<ms:inArea areaCode="sx_lt" notInMode="true">
				<TR bgcolor="#FFFFFF" >
					<TD class=column align="right" nowrap>������</TD>
					<TD colspan="3" nowrap>
						<input type="text"  name="area_name" class="bk" value="<%=area_name%>">
						<input type="hidden"  name="area_id" value="<%=area_id%>">
						<input type=hidden name="button1" value="ѡ����" onclick=areaSelect()>
						<nobr class="BT" onmouseover="this.className='BTOver'" onmouseout="this.className='BT'" onclick=areaSelect('s_area_name','s_area_id')><IMG SRC="../system/images/search.gif" WIDTH="15" HEIGHT="12" BORDER="0" ALT="ѡ����" valign="middle">&nbsp;ѡ��</nobr>
					</TD>
				</TR>
				</ms:inArea>
				<TR bgcolor="#FFFFFF" >
					<TD class=column align="right" nowrap>�洢·��</TD>
					<TD colspan="3" nowrap>
						<input type="text" size="80" name="filePath" class="bk" value="<%=server_name%>|<%=server_path%>" readOnly>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">����</TD>
					<TD colspan="3">
						<textarea name="remark" cols="70" class="bk" rows="4"><%=remark%></textarea>
					</TD>
				</TR>
				<TR >
					<TD colspan="4" align="right" class=foot>
						<INPUT TYPE="submit" name="save" value=" �� ��" class=btn>&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="action" value="update">
						<input TYPE="hidden" name="verconfile_id" value="<%=verconfile_id%>">
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
<%@ include file="../foot.jsp"%>
