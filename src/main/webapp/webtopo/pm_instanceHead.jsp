<%
	request.setCharacterEncoding("GBK"); 
	String device_id = request.getParameter("device_id");
	String expressionID = request.getParameter("expressionid");
	String deviceIP = request.getParameter("deviceIP");
	GetPm_Expreesion pmexp = new GetPm_Expreesion(request);
	String expName = pmexp.getpmName(expressionID);
	String instanceIndex = request.getParameter("instanceIndex"); 
	PM_InstanceInfo pminstance = new PM_InstanceInfo();
	HashMap map_instance = pminstance.setBaseInstance(request);
%>

<%@page import="com.linkage.litms.webtopo.GetPm_Expreesion"%>
<%@page import="com.linkage.litms.webtopo.PM_InstanceInfo"%>
<%@page import="java.util.HashMap"%>
<table width="100%"  border="0" cellspacing="0" cellpadding="0">
  <tr>
	<td bgcolor=#000000>
	   <table width="100%"  border="0" cellspacing="1" cellpadding="2">
	     <tr>
			<td align="center" class=column1>���ʽ����</td>
			<td align="left" class=column2>
				<%
					out.println("<input name=expressionName type=text value=" + expName + " readonly=true size=30>");
				%>
			</td>
			<td align="center" class=column1>�豸IP</td>
			<td align="left" class=column2>
				<%
					out.println("<input name=deviceIP type=text value=" + deviceIP + " readonly=true>");
				%>
			</td>
			</tr>
			<tr>
			<td align="center" class=column1>ʵ������</td>
			<td align="left" class=column2>
				<%
					out.println("<input name=instanceIndex type=text value=" + instanceIndex + " readonly=true size=30>");
				%>
			</td>
			<td align="center" class=column1>ԭʼ�����Ƿ����</td>
			<td align="left" class=column2>
			    <select name="ruku">
				<%
					String intodb = (String)map_instance.get("intodb");
					if (intodb.trim().equals("0")) {
						out.println("<option value=0 selected>��</option>");
						out.println("<option value=1>��</option>");
					}
					else {
						out.println("<option value=0>��</option>");
						out.println("<option value=1 selected>��</option>");
					}
				%>
				</select>
			</td>
			</tr>
			<tr>
			<td align="center" class=column1>�Ƿ�ɼ�</td>
			<td colspan="3" align="left" class=column2><select name="collect">
			<%
				String collect = (String)map_instance.get("collect");
				
				if (collect.trim().equals("0")) {
					out.println("<option value=0 selected>���ɼ�</option>");
					out.println("<option value=1>�ɼ�</option>");
				}
				else {
					out.println("<option value=0>���ɼ�</option>");
					out.println("<option value=1 selected>�ɼ�</option>");
				}
			%>
			  
			  
			</select></td>
		 </tr>
	</td>
  </tr>
</table>

<input type="hidden" name="device_id" value=<%= device_id %>>
<input type="hidden" name="expressionid" value=<%= expressionID %>>
<input type="hidden" name="indexid" value=<%= instanceIndex %>>
