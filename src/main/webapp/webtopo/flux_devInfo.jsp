<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	Flux_Config flux_con = new Flux_Config();
	flux_con.setRequest(request);
	HashMap result = flux_con.getDevResource_IP();
	String device_id = null;
	String loopback_ip = null;
	String device_name = null;
	String device_model = null;
	String snmp_ro_community = null;
%>
<%@ include file="../head.jsp"%>

<DIV id="idLayer">
	<table width="100%"  border="0" cellspacing="1" cellpadding="2">
		<tr>
			<td class="column" align="center">设备内部ID</td>
			<td class="column" align="center">设备IP</td>
			<td class="column" align="center">设备名称</td>
			<td class="column" align="center">设备类型</td>
			<td class="column" align="center">读口令</td>
			<td class="column" align="center">选择&nbsp;&nbsp;<input name="select_all" type="checkbox">(全选)</td>
		</tr>
		<%
				if (result != null) {
					device_id = (String)result.get("device_id");
					loopback_ip = (String)result.get("loopback_ip");
					device_name = (String)result.get("device_name");
					device_model = (String)result.get("device_model");
					snmp_ro_community = (String)result.get("snmp_ro_community");
					out.println("<tr>");
					out.println("<td class=\"column\" align=\"left\">" + device_id + "</td>");
					out.println("<td class=\"column\" align=\"left\">" + loopback_ip + "</td>");
					out.println("<td class=\"column\" align=\"left\">" + device_name + "</td>");
					out.println("<td class=\"column\" align=\"left\">" + device_model + "</td>");
					out.println("<td class=\"column\" align=\"left\">" + snmp_ro_community + "</td>");
					out.println("<td class=\"column\" align=\"center\"><input name=\"select_one\" type=\"checkbox\" value=\"" + device_id + "/" + device_name + "/" + loopback_ip +"\"></td>");
					out.println("</tr>");
				}
				else {
					out.println("<tr><td class=\"column\" colspan=\"6\" align=\"center\">查 询 无 此 设 备！</td></tr>");
				}
		%>
	</table>
</DIV>

<SCRIPT LANGUAGE="JavaScript">
<!--
if(typeof(parent.idLayerView1) == "object"){
	parent.idLayerView1.innerHTML = idLayer.innerHTML;
}
//-->
</SCRIPT>