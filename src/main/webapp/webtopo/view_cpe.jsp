<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.vipms.ConfigCPE" %>
<jsp:useBean id="bcusa" scope="request" class="com.linkage.litms.vipms.action.BaseCustomerAct"/>
<jsp:useBean id="bcira" scope="request" class="com.linkage.litms.vipms.action.BaseCircuitAct"/>
<jsp:useBean id="bna" scope="request" class="com.linkage.litms.vipms.action.BaseNodeAct"/>
<jsp:useBean id="bsa" scope="request" class="com.linkage.litms.vipms.action.BaseServerAct"/>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");

DeviceResourceInfo deviceInfo = new DeviceResourceInfo();
Cursor cursorinfo = deviceInfo.getDeviceResource(device_id);
Map myMap = cursorinfo.getNext();
String deviceName= (String)myMap.get("device_name");
String loopbackip= (String)myMap.get("loopback_ip");


String strData = "";

//��ҳ��
ConfigCPE cpe = new ConfigCPE(request);
Cursor cursor = cpe.getCPECursor();
Map fields = cursor.getNext();

if(fields == null){
	strData = "<TR ><TD COLSPAN=6 HEIGHT=30 bgcolor=#FFFFFF>���豸û������cpe��Ϣ</TD></TR>";
}
else{	
	String customname="";
	while(fields != null){
		customname=bcusa.getCustomerName((String)fields.get("customid"));
		strData += "<TR bgcolor=#FFFFFF>";

		strData += "<TD >"+ (String)fields.get("port_id") + "</TD>";
		strData += "<TD >"+ customname + "</TD>";
		strData += "<TD >"+ bsa.getServerName((String)fields.get("serviceid")) + "</TD>";
		strData += "<TD >"+ bna.getNodeName((String)fields.get("nodeid")) + "</TD>";
		strData += "<TD >"+ bcira.getCircuitName((String)fields.get("circuitid")) + "</TD>";
		
		strData += "<TD align=center> <A HREF=\"javascript:View('"+(String)fields.get("circuitid")+"');\">��·ͼ</A></TD>";
		strData += "</TR>";		
		
		fields = cursor.getNext();
	}
	
}


%>
<%@ include file="../head.jsp"%>
<TITLE>�����õ�·��Ϣ</TITLE>

<SCRIPT LANGUAGE="JavaScript" src="../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function View(circuitid)
	{
		location.href = "http://132.228.1.102:9080/tswebapp/test1.jsp?id=" + circuitid;
	}

//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">

<TR><TD>
	<FORM NAME="frm" METHOD="post">
        <table width="100%" height="30" border="0" align="center" cellpadding="0" cellspacing="0"  class="blue_gargtd">
          <tr> 
            <td width="162"><div align="center" class="title_bigwhite">��·ͼ�鿴</div></td>
            <td><img src="../images/attention_2.gif" width="15" height="12">&nbsp;<%=deviceName%>[<%=loopbackip%>]ҵ���·��ϸ��Ϣ</td>
           
          </tr>
        </table>
        <TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR class="blue_title"> 
				  <TD>�˿ڱ�ʶ</TD>
                  <TD>�ͻ�</TD>
                  <TD>ҵ��</TD>
                  <TD>����</TD>
				  <TD>��·</TD>
                  <TD>����</TD>
                </TR>
                <%=strData%> </TABLE>
			</TD>
		</TR>
	</TABLE>
	
	</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
<%@ include file="../openfoot.jsp"%>
