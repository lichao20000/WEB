<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.ArrayList"%>
<jsp:useBean id="VPNAct" scope="request" class="com.linkage.litms.webtopo.VPNAct"/>
<%
request.setCharacterEncoding("GBK");
String vpn_auto_id = request.getParameter("vpn_auto_id");
ArrayList list = new ArrayList();
list.clear();
list = VPNAct.getUnrelateVPNUsersCursor(request);

String strData = "";
String strBar = String.valueOf(list.get(0));
Cursor cursor = (Cursor)list.get(1);

Map fields = cursor.getNext();

if(fields == null){
	strData = "<TR><TD class=column COLSPAN=7 HEIGHT=30>���޿ɹ����û���Ϣ</TD></TR>";
}
else{
	int i=1;
	while(fields != null){
		String cust_level_name = (String)fields.get("cust_level_id");
		String cust_type_name = (String)fields.get("cust_type_name");
		String vpn_type_name = "";
		String topo_name = "";
		
		//�û��ȼ�
		if (cust_level_name.equals("1")) {
			cust_level_name = "��ʯ";
		} else if (cust_level_name.equals("1")) {
			cust_level_name = "�׽�";
		} else if (cust_level_name.equals("2")) {
			cust_level_name = "��";
		} else if (cust_level_name.equals("3")) {
			cust_level_name = "��";
		} else if (cust_level_name.equals("4")) {
			cust_level_name = "ͭ";
		}

		//�û�����
		if (Integer.parseInt((String)fields.get("vpn_type_id".toLowerCase())) == 1) {
			vpn_type_name = "����MPLS VPN�û�";
		} else if (Integer.parseInt((String)fields.get("vpn_type_id".toLowerCase())) == 2) {
			vpn_type_name = "����MPLS VPN�û�";
		}

		//��������
		if (Integer.parseInt((String)fields.get("topo_type".toLowerCase())) == 1) {
			topo_name = "��״";
		} else if (Integer.parseInt((String)fields.get("topo_type".toLowerCase())) == 2) {
			topo_name = "����";
		} else if (Integer.parseInt((String)fields.get("topo_type".toLowerCase())) == 3) {
			topo_name = "���";
		}

		strData += "<TR>";
		strData += "<TD class=column1>"+ fields.get("username") + "</TD>";
		strData += "<TD class=column2>"+ fields.get("vpn_name") + "</TD>";
		strData += "<TD class=column1>"+ cust_level_name + "</TD>";
		strData += "<TD class=column1>"+ cust_type_name + "</TD>";
		strData += "<TD class=column2>"+ vpn_type_name + "</TD>";
		strData += "<TD class=column2>"+ topo_name + "</TD>";
		strData += "<TD class=column2><input type='radio' name='relateRadio' value="+fields.get("vpn_id")+"></TD>";
		strData += "</TR>";
		i++;
		fields = cursor.getNext();
	}
	
	strData += "<TR><TD class=column COLSPAN=7 align=right>" + strBar + "<INPUT TYPE='hidden' name='vpn_auto_id' value="+vpn_auto_id+"></TD></TR>";	
}

fields = null;

list.clear();
list = null;
%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" id='myTable'>
			<TR>
				<TD bgcolor=#000000>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
						<TR>
							<TD bgcolor="#ffffff" colspan="7">���û�������δ������Ϣ����ѡ��ĳδ�����û���Ϣ��֮����������</TD>
						</TR>
						<TR> 
							<TH>�ͻ�����</TH>
							<TH>VPN����</TH>
							<TH>�ͻ��ȼ�</TH>
							<TH>�ͻ�����</TH>
							<TH>�û�����</TH>
							<TH>��������</TH>
							<TH>ѡ��</TH>
						</TR>
						<%=strData%>
						<TR><TD class=column COLSPAN=7 align=right><input type='button' value=' �� �� ' onclick='relateUser()'>&nbsp;&nbsp;
						<input type='button' value='�����û���Ϣ' onclick='addUser()'></TD></TR>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
</TD></TR>
</TABLE>
<script language="JavaScript">
<!--
//alert(parent.document.all("userinfo").innerHTML);
//alert(document.all.vpn_auto_id.value);
parent.document.all("userinfo").innerHTML=document.all.myTable.outerHTML;
//-->
</script>