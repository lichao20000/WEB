<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.webtopo.*"%>
<%@ page import="com.linkage.litms.common.database.*"%>
<%@ page import="java.util.*"%>
<%   
	String did = request.getParameter("device_id");
	String type = request.getParameter("type");
	if(null==type)
	{
		type = "";
	}
	String query = "select count(*) as num from pm_map where device_id='" + did + "'";
	String query1 = "select a.isok,a.remark,b.name,b.descr,a.expressionid  from pm_map a,pm_expression b  where a.device_id='" + did
   			 + "' and a.expressionid=b.expressionid order by a.isok desc";
    HashMap map = DataSetBean.getRecord(query);	
	String id = null;
	String name = null;
	String descr = null;
	String isok = null;
	String isokdesc = null;
	
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
function the_Name() {
	
}
//-->
</SCRIPT>
<form name="expressionFrm">
<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor=#000000>
	<tr>
		<th>�豸IP</th>
		<th>���ʽ����</th>
		<th>���ʽ����</th>
		<th>����״̬</th>
		<th>����</th>
	</tr>
	<div id="expressionparent">
	<%
	if (map != null) {
        Cursor cursor_instence = DataSetBean.getCursor(query1);
        Map myMap = cursor_instence.getNext();
		while(myMap != null){
			id=(String)myMap.get("expressionid");
			name=(String)myMap.get("name");
			descr=(String)myMap.get("descr");
			isok = (String)myMap.get("isok");
			if(isok.equals("-1")){
				isokdesc =  "δ����";
			} else if(isok.equals("1")){
				//��ʼ���ɹ�
				isokdesc =  "�ɹ�";
			} else{
				//��ʼ��ʧ��
				isokdesc =  "ʧ��";
			}
	%>
	<tr id="expressionid<%=id %>">
		<td class=column1 align="center"><%=(String)map_ip.get("loopback_ip")%></td>
		<td class=column1><%=name %></td>
		<td class=column1><%=descr %></td>
		<td class=column1 align="center"><%=isokdesc %></td>
		<td class=column1 align="center"><a href="./pm_map_instance_info.jsp?device_id=<%=did %>&expressionid=<%=id %>&name=<%=name %>&type=<%=type %>" target="_blank">����</a>&nbsp;&nbsp;&nbsp;<a href="./pm_map_instance_delete.jsp?device_id=<%=did %>&expressionid=<%=id %>&deltype=all&type=<%=type %>" target="childFrm">ɾ��</a></td>
	</tr>
	<%
			myMap = cursor_instence.getNext();	
		}
    }
	%>
	</div>
	<tr>
		<td colspan="5" class=green_foot align="center">&nbsp;&nbsp;
		</td>
	</tr>
</table>
</form>