<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.netcutover.*,java.util.Map,com.linkage.litms.common.database.Cursor"%>
<%
String _type = "";
String oper_type_id = request.getParameter("oper_type_id");
String serv_type_id = request.getParameter("serv_type_id");


String username = "";
String passwd = "";
String vpiid="";
String vciid="";
String device_id  = "";
String gather_id = "";
String devicetype_id = "";
String _oui = "";
String _device_serialnumber = "";
String e_id = "";
String e_username = "";
String e_passwd = "";
String wan_value_1 = "";
String wan_value_2 = "";
String wan_type = "�Ž�";
String wan_type_ = "";

//��̬IP������ʽ
String ipaddress = "";
String ipmask = "";
String gateway = "";
String adsl_ser = "";

Map fields = SheetManage.getUserInfo(request);

int flag = 0;
if(fields != null ){
	flag = 0;
	username = (String)fields.get("username");
	passwd = (String)fields.get("passwd");
	vpiid=  (String)fields.get("vpiid");
	vciid=  (String)fields.get("vciid");
	device_id  = (String)fields.get("device_id");
	gather_id = (String)fields.get("gather_id");
	devicetype_id = (String)fields.get("devicetype_id");
	_oui = (String)fields.get("oui");
	_device_serialnumber = (String)fields.get("device_serialnumber");
	e_id = (String)fields.get("e_id");
	e_username = (String)fields.get("e_username");
	e_passwd = (String)fields.get("e_passwd");
	wan_value_1 = (String)fields.get("wan_value_1");
	wan_value_2 = (String)fields.get("wan_value_2");
	wan_type_ = (String)fields.get("wan_type");
	if (wan_type_.equals("2")) {
		wan_type = "·��";
	} else if (wan_type_.equals("3")) {
		wan_type = "STATIC";
	} else if (wan_type_.equals("4")) {
		wan_type = "DHCP";
	}
	_type = SheetManage.getServiceIdBy(serv_type_id, oper_type_id, wan_type_);
	if(_type == null || _type.equals("")){
		_type = SheetManage.getServiceIdBy(serv_type_id, oper_type_id,"-1");
	}
	
	
	ipaddress = (String)fields.get("ipaddress");
	ipmask = (String)fields.get("ipmask");
	gateway = (String)fields.get("gateway");
	adsl_ser = (String)fields.get("adsl_ser");
}else{
	flag = -1;
}
%>
<link rel="stylesheet" href="../css/listview.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript">

</SCRIPT>
<div id="idBody">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" >
	  <TR><TD bgcolor=#999999>
		  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="idTable">
									<TR bgcolor="#ffffff">
										<TH colspan="4">
											�û�����Ϣ
										</TH>
									</TR>
									<TR bgcolor="#ffffff">
										<TD align="right">
											�û����ƣ�
										</TD>
										<TD colspan="">
											<input type="text" name="username"
												value="<%=username%>" class="bk" readOnly>
											&nbsp;&nbsp;
											<font color=red>*</font>
										</TD>
										<TD align="right">
											�û����룺
										</TD>
										<TD colspan="">
											<input type="text" name="passwd"
												value="<%=passwd%>" class="bk" readOnly>
											&nbsp;&nbsp;
											<font color=red>*</font>
										</TD>
									</TR>
									<TR bgcolor="#ffffff">
										<TD align="right">
											VPI/VCI��
										</TD>
										<TD colspan="">
											<input type="text" name="vpiid"
												value="<%=vpiid%>" class="bk" size='3' readOnly>&nbsp;/&nbsp;
												<input type="text" name="vciid" value="<%=vciid%>" class="bk" size='3' readOnly>
											&nbsp;<font color=red>*</font>
										</TD>
										<TD align="right">
											�������ͣ�
										</TD>
										<TD colspan="">
											&nbsp;<%=wan_type%>&nbsp;&nbsp;
										</TD>
									</TR>
									<TR bgcolor="#ffffff"  id = 'trip'>
									  <TD align="right">IP��ַ</TD>
									  <TD >
										<input type="text" name="ipaddress" value="<%=ipaddress%>" readOnly="readonly" class="bk">&nbsp;&nbsp;<font color=red>*</font>
									  </TD>
						
									  <TD align="right">����</TD>
									  <TD >
										<input type="text" name="ipmask" value="<%=ipmask%>" readOnly="readonly" class="bk">&nbsp;&nbsp;<font color=red>*</font>
									  </TD>
									</TR>
									<TR bgcolor="#ffffff" id = 'trgw'>
									  <TD align="right">����</TD>
									  <TD >
										<input type="text" name="gateway" value="<%=gateway%>" readOnly="readonly" class="bk">&nbsp;&nbsp;<font color=red>*</font>
									  </TD>
						
									  <TD align="right">DNS��ַ</TD>
									  <TD >
										<input type="text" name="adsl_ser" value="<%=adsl_ser%>" readOnly="readonly" class="bk">&nbsp;&nbsp;<font color=red>*</font>
									  </TD>
									</TR>
									
									<TR bgcolor="#ffffff">
										<TD align="right">
											�󶨶˿ڣ�
										</TD>
										<TD colspan="3">
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1">
											LAN1 &nbsp;&nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2">
											LAN2 &nbsp;&nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.3">
											LAN3 &nbsp;&nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.4">
											LAN4 &nbsp;&nbsp;
											<BR>
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.1">
											WLAN1 &nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.2">
											WLAN2 &nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.3">
											WLAN3 &nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.4">
											WLAN4 &nbsp;
											<BR>
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.X_CT-COM_VLAN.VLANConfig.1">
											VLAN1 &nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.X_CT-COM_VLAN.VLANConfig.2">
											VLAN2 &nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.X_CT-COM_VLAN.VLANConfig.3">
											VLAN3 &nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.X_CT-COM_VLAN.VLANConfig.4">
											VLAN4 &nbsp;
										</TD>
									</TR>
									<TR bgcolor="#ffffff">
										<TD align="right">
											���豸OUI-SN��
										</TD>
										<TD colspan="3">
											<input type="text" size="6" name="oui"
												value="<%=_oui%>" class="bk" readOnly>
											&nbsp;-&nbsp;
											<input type="text" size="40" name="device_serialnumber"
												value="<%=_device_serialnumber%>" class="bk"
												readOnly>
										</TD>
									</TR>
									<TR style='display:<%=_type.equals("5007") ? "" : "none"%>' bgcolor="#ffffff">
										<TD align="right">
											�����豸OUI-SN��
										</TD>
										<TD colspan="3">
											<input type="text" size="10" name="oui_new"
												value="" class="bk">
											&nbsp;-&nbsp;
											<input type="text" size="40" name="device_serialnumber_new"
												value="" class="bk">
										</TD>
									</TR>

									<TR bgcolor="#ffffff">
										<TH colspan="4">
											��ҵ����Ϣ
										</TH>
									</TR>
									<TR bgcolor="#ffffff">
										<TD align="right">
											��ҵID��
										</TD>
										<TD colspan="4">
											<input type="text" name="e_id"
												value="<%=e_id%>" class="bk" readOnly>
											&nbsp;&nbsp;
											<font color=red>*</font>
										</TD>
									</TR>
									<TR style='display:<%=_type.equals("5008") ? "" : "none"%>' bgcolor="#ffffff">
										<TD align="right">
											SSID1�û���:
										</TD>
										<TD colspan="">
											<input type="text" name="ssid1_username" value="bnet" class="bk"
												>
											&nbsp;&nbsp;
											<font color=red>*</font>
										</TD>
										<TD align="right">
											SSID1���룺
										</TD>
										<TD colspan="">
											<input type="text" name="ssid1_passwd" value="12345678" class="bk">
											&nbsp;&nbsp;
											<font color=red>*</font>
										</TD>
									</TR>
									<TR style='display:<%=_type.equals("5008") ? "" : "none"%>' bgcolor="#ffffff">
										<TD align="right">
											SSID2�û���:
										</TD>
										<TD colspan="">
											<input type="text" name="ssid2_username" value="bnet-guest" class="bk"
												>
											&nbsp;&nbsp;
											<font color=red>*</font>
										</TD>
										<TD align="right">
											SSID2���룺
										</TD>
										<TD colspan="">
											<input type="text" name="ssid2_passwd" value="12345678" class="bk"
												>
											&nbsp;&nbsp;
											<font color=red>*</font>
										</TD>
									</TR>
									<TR style='display:<%=_type.equals("5008") ? "" : "none"%>' bgcolor="#ffffff">
										<TD align="right">
											VLAN�󶨶˿ڣ�
										</TD>
										<TD colspan="3">
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1">
											LAN1 &nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2">
											LAN2 &nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.3">
											LAN3 &nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.4">
											LAN4 &nbsp;
											<BR>
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.1">
											WLAN1 &nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.2">
											WLAN2 &nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.3">
											WLAN3 &nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.4">
											WLAN4 &nbsp;
											<BR>
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.X_CT-COM_VLAN.VLANConfig.1">
											VLAN1 &nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.X_CT-COM_VLAN.VLANConfig.2">
											VLAN2 &nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.X_CT-COM_VLAN.VLANConfig.3">
											VLAN3 &nbsp;
											<input type="checkbox" id="LanInterface1"
												name="LanInterface1"
												value="InternetGatewayDevice.X_CT-COM_VLAN.VLANConfig.4">
											VLAN4 &nbsp;
											
										</TD>
									</TR>	
									<TR style='display:<%=_type.equals("5611") ? "" : "none"%>' bgcolor="#ffffff">
										<TD align="right">
											BossControl��
										</TD>
										<TD colspan="3">
											<input type="radio" name="boss_control" value="1" checked>����
											<input type="radio" name="boss_control" value="0">������
										</TD>
									</TR>
									
									<TR style='display:<%=_type.equals("5511") ? "" : "none"%>' bgcolor="#ffffff">
										<TD align="right">
											����ǽ��
										</TD>
										<TD colspan="3">
											<input type="checkbox" name="firewall" value="1" checked onClick="checkwall();">����&nbsp;&nbsp;&nbsp;&nbsp;
											<span id="wall" style="display:">
												����ǽ����
												<select name="wall_level" class="bk">
													<option value="L">--�ͼ�--</option>												
													<option value="M">--�м�--</option>
													<option value="H">--�߼�--</option>													
												</select>
											</span>
										</TD>
									</TR>
									<TR style='display:<%=_type.equals("5511") ? "" : "none"%>' bgcolor="#ffffff">
										<TD align="right">
											Զ�̵�¼UI���棺
										</TD>
										<TD colspan="3">
											<input type="checkbox" name="uiface" value="1" checked onClick="checkUI();">����&nbsp;&nbsp;&nbsp;&nbsp;
											<span id="ip" style="display:">
												����IP��<input type="text" name="allowip" class="bk"> &nbsp;&nbsp;
												����˿ڣ�<input type="text" name="allowport" value="80" class="bk"> (˵�������IP�м��ö��ŷָ���)
											</span>
											
										</TD>
									</TR>	
									<TR style='display:<%=_type.equals("5511") ? "" : "none"%>' bgcolor="#ffffff">
										<TD align="right">
											��������
										</TD>
										<TD colspan="3">
											<input type="checkbox" name="attack" value="1" checked >����
										</TD>
									</TR>
									<TR style='display:<%=_type.equals("5411") ? "" : "none"%>' bgcolor="#ffffff">
										<TD align="right">
											����ն���������
										</TD>
										<TD colspan="3">
											<input type="text" name="maxnum"  class="bk">&nbsp;( ע ������д���֡�)
										</TD>
									</TR>
									<TR style='display:<%=_type.equals("5411") ? "" : "none"%>' bgcolor="#ffffff">
										<TD align="right">
											���������ƣ�
										</TD>
										<TD colspan="3">
											<input type="checkbox" name="topbox" value="1" checked onclick="shownum('topbox');">����&nbsp;&nbsp;
											<span id="id_topbox">
												 ��������Ŀ�� <input type="text" name="topbox_num" class="bk" >&nbsp;( ע ������д���֡�)
											</span>
										</TD>
									</TR>																		
									<TR style='display:<%=_type.equals("5411") ? "" : "none"%>' bgcolor="#ffffff">
										<TD align="right">
											����ͷ���ƣ�
										</TD>
										<TD colspan="3">
											<input type="checkbox" name="photo" value="1" checked onclick="shownum('photo');">����&nbsp;&nbsp;
											<span id="id_photo">
												 ����ͷ��Ŀ�� <input type="text" name="photo_num" class="bk" >&nbsp;( ע ������д���֡�)
											</span>
										</TD>
									</TR>
									<TR style='display:<%=_type.equals("5411") ? "" : "none"%>' bgcolor="#ffffff">
										<TD align="right">
											�������ƣ�
										</TD>
										<TD colspan="3">
											<input type="checkbox" name="computer" value="1" checked onclick="shownum('computer');" >����&nbsp;&nbsp;
											<span id="id_computer">
												 &nbsp;&nbsp;&nbsp;������Ŀ�� <input type="text" name="computer_num" class="bk" >&nbsp;( ע ������д���֡�)
											</span>
										</TD>
									</TR>																		
									<TR style='display:<%=_type.equals("5411") ? "" : "none"%>' bgcolor="#ffffff">
										<TD align="right">
											�绰���ƣ�
										</TD>
										<TD colspan="3">
											<input type="checkbox" name="telephone" value="1" checked onclick="shownum('telephone');" >����&nbsp;&nbsp;
											<span id="id_telephone">
												 &nbsp;&nbsp;&nbsp;�绰��Ŀ�� <input type="text" name="telephone_num" class="bk" >&nbsp;( ע ������д���֡�)
											</span>
										</TD>
									</TR>
									<TR style='display:<%=_type.equals("5711") ? "" : "none"%>' bgcolor="#ffffff">
										<TD align="right">
											VPN���ã�
										</TD>
										<TD colspan="3">
											<input type="checkbox" name="vpn" value="1" checked>����&nbsp;&nbsp;
										</TD>
									</TR>
									<input type="hidden" name="device_id" value="<%=device_id%>">
									<input type="hidden" name="gather_id" value="<%=gather_id%>">
									<input type="hidden" name="devicetype_id" value="<%=devicetype_id%>">
									<input type="hidden" name="wan_value_1" value="<%=wan_value_1%>">
									<input type="hidden" name="wan_type_" value="<%=wan_type_%>">
									<TR bgcolor="#ffffff">										
										<TD align="right" colspan="4">
											<input type="button" name="button" value="���͹���" onclick="send('<%=_type%>')">
										</TD>
									</TR>

		  </TABLE>
	  </TD></TR>
	</TABLE>
</TD></TR>
</TABLE>
</div>
<SCRIPT LANGUAGE="JavaScript">
<!--
var _flag = "<%=flag%>";
if(_flag == -1){
	alert("��ǰϵͳ��û�д��ʺŻ����豸�����ڣ���ȷ�Ϻ��������룡");
}else{
	var wanTp = "<%=wan_type_%>";
	if(wanTp != "3"){
		document.all("trip").style.display = "none";
	
		document.all("trgw").style.display = "none";
	
	}else{
		document.all("trip").style.display = "";
	
		document.all("trgw").style.display = "";
	
	}
	parent.document.all("idBody").innerHTML = idBody.innerHTML;
}

//-->
</SCRIPT>

