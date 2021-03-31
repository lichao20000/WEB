<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ page import="com.linkage.commons.db.PrepareSQL" %>
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
	sql = "select a.oui, a.serialnumber, b.devicetype_id, b.device_id " +
			"from tab_user_dev a,tab_gw_device b where a.oui=b.oui and a.serialnumber=b.device_serialnumber and a.username='"
			+ username + "'";
}
com.linkage.commons.db.PrepareSQL psql = new PrepareSQL(sql);
psql.getSQL();

Cursor cursor = DataSetBean.getCursor(sql);
Map fields = cursor.getNext();

if (fields != null){
	device_id = (String)fields.get("device_id");
}

String devicetype_id = null;
String devicemodel = null;
String softwareversion = null;

//��ȡ�豸��Ϣ
Map deviceTypeMap = UserInstAct.getDeviceTypeMap();
Map venderMap = DeviceAct.getOUIDevMap();
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--

//-->
</SCRIPT>

<link rel="stylesheet" href="../css/listview.css" type="text/css">
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="userinstSave.jsp" onsubmit="return CheckForm()">
		<input type="hidden" name="username" value="<%=username%>">
		<input type="hidden" name="selDevice" value="<%=device_id%>">
		<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						�Զ���װ�豸ȷ��
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr><td bgcolor=#999999>
		<table width="100%" border=0 cellspacing=1 cellpadding=2 align="center">
		<%if (fields != null){ 
			devicetype_id = (String)fields.get("devicetype_id");
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
				<TH nowrap>ѡ��</TH>
				<TH>�豸����</TH>
				<TH>�ͺ�</TH>
				<TH>����汾</TH>
				<TH>�豸���к�</TH>
			</TR>
			<tr>
				<td class=column2 width="5%"><input type=radio checked name=chkCheck value="<%=fields.get("device_id") %>"></td>
				<td class=column2 width="25%"><%=venderMap.get(fields.get("oui")) %></td>
				<td class=column2 width="25%"><%=devicemodel %></td>
				<td class=column2 width="25%"><%=softwareversion %></td>
				<td class=column2 width="25%"><%=(String)fields.get("serialnumber") %></td>
			</tr>
		<%}%>
			<TR style="display:none" id="serialnumber">
				<TD colspan="2" class=column2>����ѡ������豸��</TD>
				<TD colspan="1" class=column2>�豸���к�</TD>
				<TD colspan="2" class=column2>
					<input type="text" name=device_serialnumber class="bk" value="">
					<input type="button" name="sort" value=" �� ѯ " onclick="getUserinstInfo()">
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
			<tr>
				<td  colspan="5" align="right" class=column2>
					<input type="submit" name="submit" value=" ȷ�ϱ��� ">
					<input type="button" name="back" onclick="history.go(-1)" value=" �� �� ">
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
	
	if (document.frm.selDevice.value == ""){
		if (confirm("��ǰû��ѡ���豸���Ƿ�ֱ�ӱ��棡")){
			return true;
		}
		else{
			return false;
		}
	}
	
	var message = "��ȷ�ϣ��û��ʺţ�"+document.frm.username.value+"���豸���кţ�"+serialnumber;
	if (!confirm(message+'���Ƿ��������?')){
		return false;
	}
}

<%if (fields == null){%>
	getUserinstInfo();
<%}%>
</SCRIPT>

<%@ include file="../foot.jsp"%>