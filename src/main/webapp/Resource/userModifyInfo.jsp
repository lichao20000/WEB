<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<jsp:useBean id="UserInstAct" scope="request" class="com.linkage.litms.resource.UserInstAct"/>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
request.setCharacterEncoding("GBK");

String username = request.getParameter("username");

String device_id = "";

String sql = "select a.*,b.devicetype_id,b.device_id from tab_user_dev a,tab_gw_device b where a.oui=b.oui and a.serialnumber=b.device_serialnumber and a.username='"
			+ username + "'";
// teledb
if (DBUtil.GetDB() == 3) {
	sql = "select a.oui, a.serialnumber, b.devicetype_id, b.device_id" +
			" from tab_user_dev a,tab_gw_device b where a.oui=b.oui and a.serialnumber=b.device_serialnumber and a.username='"
			+ username + "'";
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
psql.getSQL();
Cursor cursor = DataSetBean.getCursor(sql);
Map fields = cursor.getNext();

if (fields != null){
	device_id = (String)fields.get("device_id");
}

String device_id_old = "";
String sql_old = "select a.oui,a.device_serialnumber,b.devicetype_id,b.device_id from tab_hgwcustomer a,tab_gw_device b where a.device_id = b.device_id and a.username='"
			+ username + "' and a.user_state='1'";
Cursor cursor_old = DataSetBean.getCursor(sql_old);
Map fields_old = cursor_old.getNext();

if (fields_old != null){
	device_id_old = (String)fields_old.get("device_id");
}

String devicetype_id = null;
String devicemodel = null;
String softwareversion = null;

//��ȡ�豸��Ϣ
Map deviceTypeMap = UserInstAct.getDeviceTypeMap();
Map venderMap = DeviceAct.getOUIDevMap();
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--

//-->
</SCRIPT>

<link rel="stylesheet" href="../css/listview.css" type="text/css">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="userinstSave.jsp" onsubmit="return CheckForm()">
		<input type="hidden" name="username" value="<%=username%>">
		<input type="hidden" name="selDevice" value="<%=device_id%>">
		<input type="hidden" name="device_id_old" value="<%=device_id_old%>">
		<input type="hidden" name="strAction" value="modify">
		<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						�豸����ȷ��
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						ֻ�а�װe8-b�豸���û���Ҫ�����˽��棬�����û����������
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr><td bgcolor=#999999>
		<table width="100%" border=0 cellspacing=1 cellpadding=2 align="center">
			<TR>
				<td class=column2 colspan="2">�û�����ʺţ�</td>
				<td class=column2><%=username%></td>
				<td class=column2 colspan=2></td>
			</TR>
			<tr><td bgcolor="#FFFFFF" colspan="5" height="20"></td></tr>
		<%if (fields_old != null){ 
			devicetype_id = (String)fields_old.get("devicetype_id");
			String[] tmp = (String[])deviceTypeMap.get(devicetype_id);
			if (tmp != null && tmp.length==2) {
				devicemodel = tmp[0];
				softwareversion = tmp[1];			
			}
			else{
				devicemodel = "";
				softwareversion = "";
			}
			%>
			<TR>
				<TD class=column2 colspan="5">��ǰʹ�õ��豸��</TD>
			</TR>
			<TR>
				<TH nowrap colspan="2">�豸����</TH>
				<TH nowrap>�ͺ�</TH>
				<TH nowrap>����汾</TH>
				<TH nowrap>�豸���к�</TH>
			</TR>
			<tr>
				<td class=column2 width="30%" colspan=2><%=venderMap.get(fields_old.get("oui")) %></td>
				<td class=column2 width="20%"><%=devicemodel %></td>
				<td class=column2 width="15%"><%=softwareversion %></td>
				<td class=column2 width="35%"><%=(String)fields_old.get("device_serialnumber") %></td>
			</tr>
		<%}
		else{%>
			<tr>
				<td class=column2 colspan="5">�޷���ѯ������ʹ�õ��豸</td>
			</tr>
		<%} %>
			<tr><td bgcolor="#FFFFFF" colspan="5" height="20"></td></tr>
			<tr><td bgcolor="#FFFFFF" colspan="5"><font color="#FF0000">����������4λ���кŽ��в�ѯ</font></td></tr>
		<%if (fields != null){ 
			devicetype_id = (String)fields.get("devicetype_id");
			String tmp = (String)deviceTypeMap.get(devicetype_id);
		
			if(tmp != "" && tmp != null){
				String[] aa = tmp.split(",");
				devicemodel = aa[0];
				softwareversion = aa[1];
			}
			else{
				devicemodel = "";
				softwareversion = "";
			}
			%>
			<TR>
				<TD class=column2 colspan="5">����ѡ������豸��</TD>
			</TR>
			<TR>
				<TH nowrap>ѡ��</TH>
				<TH nowrap>�豸����</TH>
				<TH nowrap>�ͺ�</TH>
				<TH nowrap>����汾</TH>
				<TH nowrap>�豸���к�</TH>
			</TR>
			<tr>
				<td class=column2 width="5%"><input type=radio checked name=chkCheck value="<%=fields.get("device_id") %>"></td>
				<td class=column2 width="25%"><%=venderMap.get(fields.get("oui")) %></td>
				<td class=column2 width="20%"><%=devicemodel %></td>
				<td class=column2 width="10%"><%=softwareversion %></td>
				<td class=column2 width="35%"><%=(String)fields.get("serialnumber") %></td>
			</tr>
		<%} %>
			<TR style="display:none" id="serialnumber">
				<TD colspan="2" class=column2>����ѡ������豸��</TD>
				<TD class=column2>�豸���к�</TD>
				<TD colspan="2" class=column2>
					<input type="text" name=device_serialnumber value="">
					<input type="button" name="sort" value="��ѯ" onclick="checkSerialno()">
				</TD>
			</TR>
			<TR style="display:none" id="dispTr">
				<TD bgcolor=#ffffff colspan="5">
					<div id="userTable"></div>
				</TD>
			</TR>
			<TR style="display:none">
				<TD>
					<IFRAME ID="childFrm" SRC="" STYLE="display:none"></IFRAME>
				</TD>
			</TR>
			<tr><td bgcolor="#FFFFFF" colspan="5" height="20"></td></tr>
			<tr>
				<TD class=column2>����ԭ��</TD>
				<TD class=column2 colspan="2">
					<select name="fault_id" class=bk>
						<option value="-1">--��ѡ��--</option>
						<option value="1">�û���������</option>
						<option value="2">�豸���ֶ˿���</option>
						<option value="3">�豸�����ȱ��</option>
						<option value="4">����ͨ����ͨ���û���������</option>
						<option value="5">����ԭ��</option>
					</select>
				</TD>
				<td colspan="2" align="right" class=column2>
					<input type="submit" name="submit" value="ȷ�ϱ���">
				</td>
			</tr>
		</table>
		</td></tr>
		</TABLE>
	</FORM>	
</TD></TR>
</TABLE>
<SCRIPT LANGUAGE="JavaScript">
function getUserinstInfo(){
    var page="userinstInfo.jsp?status=All&device_serialnumber="+document.frm.device_serialnumber.value+"&refresh="+(new Date()).getTime();
	document.all("childFrm").src = page;
	document.all("serialnumber").style.display="";
	document.frm.selDevice.value = "";
}

function goPage(offset){
	var page="userinstInfo.jsp?status=All&offset="+offset+"&device_serialnumber="+document.frm.device_serialnumber.value+"&refresh="+(new Date()).getTime();
	document.all("childFrm").src = page;
	document.all("serialnumber").style.display="";
	document.frm.selDevice.value = "";
}

function CheckForm(){
	
	var obj = document.all("chkCheck");
	var serialnumber = '';
	
	if (obj == null){
		alert('����ѡ��һ��e8-b�豸�ٽ��б������!');
		return false;
	}
	
	if (typeof(obj.value) != 'undefined'){
		if (obj.checked){
			var tmp = obj.value.split('#');
			document.frm.selDevice.value = tmp[0];
			serialnumber = tmp[1];
		}
	}
	else{
		for (var i=0;i<obj.length;i++){
			if (obj[i].checked){
				var tmp = obj[i].value.split('#');
				document.frm.selDevice.value = tmp[0];
				serialnumber = tmp[1];
			}
		}
	}
	
	if (document.frm.fault_id.value == "" || document.frm.fault_id.value == "-1"){
		alert('��ѡ�����ԭ��');
		return false;
	}
	
	if (document.frm.selDevice.value == ""){
		alert('����ѡ��һ��e8-b�豸�ٽ��б������!');
		return false;
	}
	
	var message = "��ȷ�ϣ��û��ʺţ�<%=username%>���豸���кţ�"+serialnumber;
	if (!confirm(message+'���Ƿ��������?')){
		return false;
	}
	
}

function checkSerialno(){
	var tmp = document.frm.device_serialnumber.value;
	if (tmp.length < 4){
		alert('����������4λ���кŽ��в�ѯ!');
	}
	else{
		getUserinstInfo();
	}
}

<%if (fields == null){%>
	document.all("serialnumber").style.display="";
	document.frm.selDevice.value = "";
<%}%>
</SCRIPT>

<%@ include file="../foot.jsp"%>