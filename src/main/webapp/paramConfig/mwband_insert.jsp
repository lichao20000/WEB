<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page
	import="java.util.Map,java.util.HashMap,com.linkage.litms.resource.FileSevice,com.linkage.litms.paramConfig.ParamInfoCORBA"%>
<jsp:useBean id="deviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%
	request.setCharacterEncoding("GBK");
	String device_id = request.getParameter("device_id");
	int Mode = 1;
	String TotalTerminalNumber = "";
	boolean STBRestrictEnable = false;
	String STBNumber = "";
	boolean CameraRestrictEnable = false;
	String CameraNumber = "";
	boolean ComputerRestrictEnable = false;
	String ComputerNumber = "";
	boolean PhoneRestrictEnable = false;
	String PhoneNumber = "";
	int canConn = -1;

	if (device_id != null) {

		String[] params = new String[2];

		ParamInfoCORBA paramInfoCorba = new ParamInfoCORBA();

		String[] params_name = new String[2];

		HashMap<String, String> deviceInfo = deviceAct.getDeviceInfo(device_id);
		String gather_id = (String) deviceInfo.get("gather_id");

		String ior = user.getIor("ACS_" + gather_id, "ACS_Poa_"
				+ gather_id);

		params_name[0] = "InternetGatewayDevice.Services.X_CT-COM_MWBAND.Mode";
		params_name[1] = "InternetGatewayDevice.Services.X_CT-COM_MWBAND.TotalTerminalNumber";

		Map<String, String> paraValueMap = paramInfoCorba.getParaValue_multi(params_name, device_id);

		if (null == paraValueMap) {
			canConn = 0;
		} else {
			canConn = 1;
			Map map = new HashMap();
			map.put("Mode", paraValueMap.get(params_name[0]));
			map.put("TotalTerminalNumber", paraValueMap.get(params_name[1]));

			//map.put("Mode", "" + 1);
			//map.put("TotalTerminalNumber", "" + 3);
			
			//是否限制同时接入公网的终端数量，取值范围(0，1，2)， 0：代表此业务不启用，1： 代表使用模式一，2：代表使 用模式二，缺省值：1
			//map.put("Mode", "" + 2);
			//模式一：同时接入公网的终端的最大数量，缺省值：4
			//map.put("TotalTerminalNumber", "" + 1);
			//模式二：是否限制同时接入公网的 STB终端数量，取值范围(true,false)， 缺省值： false
			//map.put("STBRestrictEnable", "" + "true");
			//同时接入公网的STB终端的最大数量 
			//map.put("STBNumber", "" + 2);
			//模式二：是否限制同时接入公网的 Camera 终端数量， 取值范围(true,false)，缺省值：false 
			//map.put("CameraRestrictEnable", "" + "false");
			//同时接入公网的 Camera 终端的最大数量
			//map.put("CameraNumber", "" + 3);
			//模式二：是否限制同时接入公网的 Computer终端数量，取值范围(true,false)，缺省值：false 
			//map.put("ComputerRestrictEnable", "" + "true");
			//同时接入公网的Computer终端的最大数量，缺省值：1
			//map.put("ComputerNumber", "" + 4);
			//模式二：是否限制同时接入公网的 Phone 终端数量，取值范围(true,false)，缺省值：false
			//map.put("PhoneRestrictEnable", "" + "false");
			//同时接入公网的 Phone终端的最大数量
			//map.put("PhoneNumber", "" + 5);

			Mode = Integer.parseInt((String) map.get("Mode"));
			TotalTerminalNumber = (String) map
					.get("TotalTerminalNumber");
			//int TotalTerminalNumber = Integer.parseInt((String)map.get("TotalTerminalNumber"));
			STBRestrictEnable = Boolean.parseBoolean((String) map
					.get("STBRestrictEnable"));
			STBNumber = (String) map.get("STBNumber");
			//int STBNumber = Integer.parseInt((String)map.get("STBNumber"));
			CameraRestrictEnable = Boolean.parseBoolean((String) map
					.get("CameraRestrictEnable"));
			CameraNumber = (String) map.get("CameraNumber");
			//int CameraNumber = Integer.parseInt((String)map.get("CameraNumber"));
			ComputerRestrictEnable = Boolean.parseBoolean((String) map
					.get("ComputerRestrictEnable"));
			ComputerNumber = (String) map.get("ComputerNumber");
			//int ComputerNumber = Integer.parseInt((String)map.get("ComputerNumber"));
			PhoneRestrictEnable = Boolean.parseBoolean((String) map
					.get("PhoneRestrictEnable"));
			PhoneNumber = (String) map.get("PhoneNumber");
			//int PhoneNumber = Integer.parseInt((String)map.get("PhoneNumber"));
		}

	}
%>

<table style="display:'<%=device_id != null ? "" : "none"%>'" width="98%" height="30" border="0" cellspacing="0" align="center"
	cellpadding="0" class="green_gargtd" id="myTable">
	<tr>
		<td>&nbsp;&nbsp;<img src="../images/attention_2.gif" width="15" height="12">
		配置结果
		</td>
		<td align=right>设备连接状态： <%
			if (canConn == 1) {
		%> <img
			src="../images/gaojin_green.gif" width="15" height="12"> <%
 	} else {
 %>
		<img src="../images/gaojin_red.gif" width="15" height="12"> <%
 	}
 %>
		</td>
	</tr>

	<tr>
		<td>
		<table style="display:'<%=canConn == 0 ? "" : "none"%>'" width="98%"
			border=0 cellspacing=1 cellpadding=0 align="center" bgcolor="#999999"
			class=text>
			<tr bgcolor="#ffffff">
				<td style="padding-left: 10px">获取失败，可能设备无法连接！</td>
			</tr>
		</table>

		<table style="display:'<%=canConn == 1 ? "" : "none"%>'" width="98%"
			border=0 cellspacing=1 cellpadding=0 align="center" bgcolor="#999999"
			class=text>
			<tr bgcolor="#ffffff">
				<td style="padding-left: 10px" width="250">是否限制同时接入公网的终端数量</td>
				<td style="padding-left: 10px"><%=Mode == 0 ? "不限制" : (Mode == 1 ? "模式一（限制所有类型终端总的数量）"
					: "模式二（每种终端类型单独设置）")%>
				</td>
			</tr>
			<tr style="display:'<%=Mode == 1 ? "" : "none"%>'" bgcolor="#ffffff">
				<td style="padding-left: 10px">同时接入公网的终端的最大数量</td>
				<td style="padding-left: 10px">
					<span id="mun_span_old"><%=TotalTerminalNumber%></span>&nbsp;
					<span id="mun_span_new"></span>&nbsp;
					<input name="compwGen" type="button" value="输入新值" class="btn_g" onclick="generateInput()">
					<input name="compwSet" type="button" value="重新设置" class="btn_g" onclick="setMaxUserNum()">
				</td>
						
			</tr>
			<%--
		   <tr style="display:'<%=Mode == 2 ? "" : "none"%>'" bgcolor="#ffffff" >
		      <td style="padding-left:10px">
				是否限制同时接入公网的 STB 终端数量
				</td>
				<td style="padding-left:10px">
					<%=STBRestrictEnable ? "限制" : "不限制"%>
				</td>
	       </tr>--%>
			<tr style="display:'<%=Mode == 2 ? "" : "none"%>'" bgcolor="#ffffff">
				<td style="padding-left: 10px">同时接入公网的 STB 终端的最大数量</td>
				<td style="padding-left: 10px"><%=STBRestrictEnable ? STBNumber : "不限"%>
				</td>
			</tr>
			<%--
		   <tr style="display:'<%=Mode == 2 ? "" : "none"%>'" bgcolor="#ffffff" >
		      <td style="padding-left:10px">
				是否限制同时接入公网的 Camera 终端数量
				</td>
				<td style="padding-left:10px">
					<%=CameraRestrictEnable ? "限制" : "不限制"%>
				</td>
	       </tr>--%>
			<tr style="display:'<%=Mode == 2 ? "" : "none"%>'" bgcolor="#ffffff">
				<td style="padding-left: 10px">同时接入公网的 Camera 终端的最大数量</td>
				<td style="padding-left: 10px"><%=CameraRestrictEnable ? CameraNumber : "不限"%>
				</td>
			</tr>
			<%--
		   <tr style="display:'<%=Mode == 2 ? "" : "none"%>'" bgcolor="#ffffff" >
		      <td style="padding-left:10px">
				是否限制同时接入公网的 Computer 终端数量
				</td>
				<td style="padding-left:10px">
					<%=ComputerRestrictEnable ? "限制" : "不限制"%>
				</td>
	       </tr>--%>
			<tr style="display:'<%=Mode == 2 ? "" : "none"%>'" bgcolor="#ffffff">
				<td style="padding-left: 10px">同时接入公网的 Computer 终端的最大数量</td>
				<td style="padding-left: 10px"><%=ComputerRestrictEnable ? ComputerNumber : "不限"%>
				</td>
			</tr>
			<%--
		   <tr style="display:'<%=Mode == 2 ? "" : "none"%>'" bgcolor="#ffffff" >
		      <td style="padding-left:10px">
				是否限制同时接入公网的 Phone 终端数量
				</td>
				<td style="padding-left:10px">
					<%=PhoneRestrictEnable ? "限制" : "不限制"%>
				</td>
	       </tr>--%>
			<tr style="display:'<%=Mode == 2 ? "" : "none"%>'" bgcolor="#ffffff">
				<td style="padding-left: 10px">同时接入公网的 Phone 终端的最大数量</td>
				<td style="padding-left: 10px"><%=PhoneRestrictEnable ? PhoneNumber : "不限"%>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>

<SCRIPT LANGUAGE="JavaScript">
<!--
parent.tableView.style.display="";
parent.tableView.innerHTML = document.all("myTable").innerHTML;

//-->
</SCRIPT>
