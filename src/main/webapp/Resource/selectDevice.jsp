<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.resource.DeviceSearch"%>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%@ include file="../timelater.jsp"%>
<%
	request.setCharacterEncoding("GBK");
	//�������ļ���ȡ��������Ϣ������Ϣ�ڹ��̼���ʱ�Ѷ����ڴ棬����������Ϊapplication
	Map city_Map = CityDAO.getCityIdCityNameMap();
	String curCity_ID = curUser.getCityId();
	Map venderMap = DeviceAct.getOUIDevMap();
	String strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>Ϊ�˱�֤����ѯ�������Ч�ԣ�����������6λ���豸���к�</TD></TR>";
	Cursor cursor = new Cursor();
	String device_serialnumber = request.getParameter("device_serialnumber");
	device_serialnumber = (device_serialnumber == null? "" : device_serialnumber);
	Map fields = null;
	if(device_serialnumber != null && !"".equals(device_serialnumber)){
		cursor = DeviceSearch.queryHGWDevice(device_serialnumber, curCity_ID);
		fields = cursor.getNext();
	
		if (fields == null) {
			strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>û�в�ѯ���豸��Դ</TD></TR>";
		} else {
			String device_id = null;
	
			String city_id = null;
			String city_name = null;
			String vender = null;
			String oui = null;
			String serialnumber = null;
			String cpe_allocatedstatus = null;
			strData = "";
		    while (fields != null) {
				device_id = (String)fields.get("device_id");
				city_id = (String)fields.get("city_id");
				city_name = (String)city_Map.get(city_id);
				city_name = city_name == null ? "&nbsp;" : city_name;
				vender = (String)venderMap.get(fields.get("vender_id"));
				oui = (String)fields.get("oui");
				serialnumber = (String)fields.get("device_serialnumber");
				cpe_allocatedstatus = (String)fields.get("cpe_allocatedstatus");
				
				strData += "<TR>";
				strData += "<TD class=column2 align='right'>" + device_id + "</TD>";
				strData += "<TD class=column2 align='right'>" + vender + "</TD>";
				strData += "<TD class=column2 align='right'>" + city_name + "</TD>";
				strData += "<TD class=column2 align='right'>" + fields.get("oui") + "-" + serialnumber + "</TD>";
				strData += "<TD class=column2 align='center'>" + (cpe_allocatedstatus.equals("1")? "��" : "��" )+ "</TD>";
				strData += "<TD class=column2  align='center' nowrap><a href='#' onclick=selDevice('" + oui + "','" + serialnumber + "','"+device_id+"')>ѡ��</a></TD>";
				strData += "</TR>";
		        fields = cursor.getNext();
		    }
		    strData += "<TR><TD class=column2 colspan=6 align=right></TD></TR>";
		}
	}

%>
<html>
<head>
<script language="javascript">

function checkForm()
{
	var sno = document.frm.device_serialnumber.value;
	if(sno.length < 6){
		alert("�����������豸���к����6λ");
		document.frm.device_serialnumber.focus();
		return false;
	}
	return true;
}
</script>
</head>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>

<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="frm" METHOD="post" onsubmit="return checkForm();" action="" target="_self">
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr>
						<td align="right" width="40%">
						�豸���к�
						</td>
						<td width="20%">
							<input type="text" name="device_serialnumber" class='bk' value="<%=device_serialnumber%>">
							&nbsp;&nbsp;
						</td>
						<td width="40%">
							<input type="submit" value="�� ѯ" class='btn' >
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td bgcolor=#999999>
			</td>
		</tr>
			<TR>
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
						<TR>
							<TH>�豸����</TH>
							<TH>�豸����</TH>
							<TH>����</TH>
							<TH>�豸���к�</TH>
							<TH>�Ƿ��</TH>
							<TH>����</TH>
						</TR>
						<%=strData%>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</FORM>
</TD></TR>
</TABLE>
</body>
<%@ include file="../foot.jsp"%>
<script language="javascript">

function selDevice(oui,serialnumber,device_id)
{
	//window.returnValue = oui + '/' + serialnumber + '/' + device_id;
	
	window.opener.document.frm.oui.value = oui;
	window.opener.document.frm.vender.value = serialnumber;
	window.opener.document.frm.device_id.value = device_id;
	window.close();
	window.opener.focus();
}

</script>
</html>