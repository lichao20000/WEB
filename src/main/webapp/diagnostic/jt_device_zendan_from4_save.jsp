<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: �ʺ����ӡ��޸ġ�ɾ������
--%>
<html>
<%@ page import="java.util.*"%>
<jsp:useBean id="resetOrRebootAct" scope="request" class="com.linkage.litms.resource.ResetOrRebootAct"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	Map map = resetOrRebootAct.SetWayV1(request);

	String strlist = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
	strlist += "<tr>";
	strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
	strlist += "�豸����";
	strlist += "</td><td bgcolor='#FFFFFF' width='70%' nowrap>";
	strlist += "���ؽ��";
	strlist += "</td></tr>";
  	
	if(map==null) {
		strlist += "<tr class='blue_foot'>";
		strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
		strlist += "</td>";
		strlist += "<td bgcolor='#FFFFFF' width='70%' nowrap>";
		strlist += "��ȡֵʧ��";
		strlist += "</td></tr>";
	}else{
		Set set = map.keySet();
		Iterator iterator = set.iterator();
		String name = null;
		String value = null;	
		while(iterator.hasNext()){
			name = (String)iterator.next();
			value = (String)map.get(name);
//			if(value.equals("true")){
//				value = "�ָ��������óɹ���";
//			} else {
//				value = "�ָ���������ʧ�ܣ�";
//			}		
			strlist += "<tr class='blue_foot'>";
			strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
			strlist += name;
			strlist += ":</td><td bgcolor='#FFFFFF' width='70%'>";
			strlist += value;
			strlist += "</td></tr>";
		}
	}
	strlist += "</table>";
%>

<body>
<SPAN ID="child"><%=strlist%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_ping").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>
