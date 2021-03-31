<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.linkage.litms.webtopo.*"%>
<%
	GetPm_Expreesion pmexp = new GetPm_Expreesion(request);
	Cursor cursor = pmexp.getPm_exp();
	Map feilds = cursor.getNext();
	String strExp = request.getParameter("expressionid");
	
	if (strExp==null) {
		strExp = "0";
	}
	DeviceResourceInfo deviceInfo = new DeviceResourceInfo();
	Cursor cursor_ip = deviceInfo.getDeviceResource(request.getParameter("device_id"));
	Map map_ip = cursor_ip.getNext();
	String key_exp = null;
	String value_exp = null;
%>		  

<SCRIPT LANGUAGE="JavaScript">
<!--
function Pm_Name() {
	var name = frm.exp_name.options[frm.exp_name.selectedIndex].text;
	frm.expression_Name.value = name;
}
//-->
</SCRIPT>

<table width="100%"  border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td bgcolor=#000000>
			 <table width="100%"  border="0" cellspacing="1" cellpadding="2">
				  <tr>
					<td class=column1 align=center width="100">性能表达式</td>
					<td class=column2 align=left width="200">
					<select name="exp_name" onchange="Pm_Name()">
					<option value=0>请选择此设备的性能表达式</option>
					<%
						while (feilds != null) {
							key_exp = (String)feilds.get("expressionid");
							value_exp = (String)feilds.get("name");
							
							if (key_exp.trim().equals(strExp.trim())) {
								out.println("<option value=" + key_exp + " selected>" + value_exp + "</option>");
							}
							else {
								out.println("<option value=" + key_exp + ">" + value_exp + "</option>");
							}
							feilds = cursor.getNext();
						}
					%>
					</select>
					</td>
					<td class=column1 align=center width="100">设备IP</td>
					<td class=column2 align=left width="100"><%= (String)map_ip.get("loopback_ip")%></td>
				  </tr>
				  <tr>
					<td class=column1 align=center width="100">采样间隔</td>
					<td class=column2 align=left><input name="samp_distance" type="text" value="300" style="width:100"></td>
					<td class=column1 align=center width="100">原始数据是否入库</td>
					<td class=column2 align=left><select name="ruku">
					  <option value="0">否</option>
					  <option value="1">是</option>
					</select>					  
					</td>
				  </tr>
			</table>
		</td>
	</tr>
</table>
<input type="hidden" name="expression_Name">
<input type="hidden" name="device_id" value=<%= device_id %>>
<input type="hidden" name="dev_serial" value<%= dev_serial %>>