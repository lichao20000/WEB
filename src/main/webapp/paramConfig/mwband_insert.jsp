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
			
			//�Ƿ�����ͬʱ���빫�����ն�������ȡֵ��Χ(0��1��2)�� 0�������ҵ�����ã�1�� ����ʹ��ģʽһ��2������ʹ ��ģʽ����ȱʡֵ��1
			//map.put("Mode", "" + 2);
			//ģʽһ��ͬʱ���빫�����ն˵����������ȱʡֵ��4
			//map.put("TotalTerminalNumber", "" + 1);
			//ģʽ�����Ƿ�����ͬʱ���빫���� STB�ն�������ȡֵ��Χ(true,false)�� ȱʡֵ�� false
			//map.put("STBRestrictEnable", "" + "true");
			//ͬʱ���빫����STB�ն˵�������� 
			//map.put("STBNumber", "" + 2);
			//ģʽ�����Ƿ�����ͬʱ���빫���� Camera �ն������� ȡֵ��Χ(true,false)��ȱʡֵ��false 
			//map.put("CameraRestrictEnable", "" + "false");
			//ͬʱ���빫���� Camera �ն˵��������
			//map.put("CameraNumber", "" + 3);
			//ģʽ�����Ƿ�����ͬʱ���빫���� Computer�ն�������ȡֵ��Χ(true,false)��ȱʡֵ��false 
			//map.put("ComputerRestrictEnable", "" + "true");
			//ͬʱ���빫����Computer�ն˵����������ȱʡֵ��1
			//map.put("ComputerNumber", "" + 4);
			//ģʽ�����Ƿ�����ͬʱ���빫���� Phone �ն�������ȡֵ��Χ(true,false)��ȱʡֵ��false
			//map.put("PhoneRestrictEnable", "" + "false");
			//ͬʱ���빫���� Phone�ն˵��������
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
		���ý��
		</td>
		<td align=right>�豸����״̬�� <%
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
				<td style="padding-left: 10px">��ȡʧ�ܣ������豸�޷����ӣ�</td>
			</tr>
		</table>

		<table style="display:'<%=canConn == 1 ? "" : "none"%>'" width="98%"
			border=0 cellspacing=1 cellpadding=0 align="center" bgcolor="#999999"
			class=text>
			<tr bgcolor="#ffffff">
				<td style="padding-left: 10px" width="250">�Ƿ�����ͬʱ���빫�����ն�����</td>
				<td style="padding-left: 10px"><%=Mode == 0 ? "������" : (Mode == 1 ? "ģʽһ���������������ն��ܵ�������"
					: "ģʽ����ÿ���ն����͵������ã�")%>
				</td>
			</tr>
			<tr style="display:'<%=Mode == 1 ? "" : "none"%>'" bgcolor="#ffffff">
				<td style="padding-left: 10px">ͬʱ���빫�����ն˵��������</td>
				<td style="padding-left: 10px">
					<span id="mun_span_old"><%=TotalTerminalNumber%></span>&nbsp;
					<span id="mun_span_new"></span>&nbsp;
					<input name="compwGen" type="button" value="������ֵ" class="btn_g" onclick="generateInput()">
					<input name="compwSet" type="button" value="��������" class="btn_g" onclick="setMaxUserNum()">
				</td>
						
			</tr>
			<%--
		   <tr style="display:'<%=Mode == 2 ? "" : "none"%>'" bgcolor="#ffffff" >
		      <td style="padding-left:10px">
				�Ƿ�����ͬʱ���빫���� STB �ն�����
				</td>
				<td style="padding-left:10px">
					<%=STBRestrictEnable ? "����" : "������"%>
				</td>
	       </tr>--%>
			<tr style="display:'<%=Mode == 2 ? "" : "none"%>'" bgcolor="#ffffff">
				<td style="padding-left: 10px">ͬʱ���빫���� STB �ն˵��������</td>
				<td style="padding-left: 10px"><%=STBRestrictEnable ? STBNumber : "����"%>
				</td>
			</tr>
			<%--
		   <tr style="display:'<%=Mode == 2 ? "" : "none"%>'" bgcolor="#ffffff" >
		      <td style="padding-left:10px">
				�Ƿ�����ͬʱ���빫���� Camera �ն�����
				</td>
				<td style="padding-left:10px">
					<%=CameraRestrictEnable ? "����" : "������"%>
				</td>
	       </tr>--%>
			<tr style="display:'<%=Mode == 2 ? "" : "none"%>'" bgcolor="#ffffff">
				<td style="padding-left: 10px">ͬʱ���빫���� Camera �ն˵��������</td>
				<td style="padding-left: 10px"><%=CameraRestrictEnable ? CameraNumber : "����"%>
				</td>
			</tr>
			<%--
		   <tr style="display:'<%=Mode == 2 ? "" : "none"%>'" bgcolor="#ffffff" >
		      <td style="padding-left:10px">
				�Ƿ�����ͬʱ���빫���� Computer �ն�����
				</td>
				<td style="padding-left:10px">
					<%=ComputerRestrictEnable ? "����" : "������"%>
				</td>
	       </tr>--%>
			<tr style="display:'<%=Mode == 2 ? "" : "none"%>'" bgcolor="#ffffff">
				<td style="padding-left: 10px">ͬʱ���빫���� Computer �ն˵��������</td>
				<td style="padding-left: 10px"><%=ComputerRestrictEnable ? ComputerNumber : "����"%>
				</td>
			</tr>
			<%--
		   <tr style="display:'<%=Mode == 2 ? "" : "none"%>'" bgcolor="#ffffff" >
		      <td style="padding-left:10px">
				�Ƿ�����ͬʱ���빫���� Phone �ն�����
				</td>
				<td style="padding-left:10px">
					<%=PhoneRestrictEnable ? "����" : "������"%>
				</td>
	       </tr>--%>
			<tr style="display:'<%=Mode == 2 ? "" : "none"%>'" bgcolor="#ffffff">
				<td style="padding-left: 10px">ͬʱ���빫���� Phone �ն˵��������</td>
				<td style="padding-left: 10px"><%=PhoneRestrictEnable ? PhoneNumber : "����"%>
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
