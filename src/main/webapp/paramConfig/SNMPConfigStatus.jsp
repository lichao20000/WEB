<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>

<%
//String DNSServers = "";
String strDevice = request.getParameter("device_id");
String[] device_list = strDevice.split(",");
String device_id = "";
String type= request.getParameter("type");
String html = "";
//------------------------------------
///描述
String theName = "访问配置";
//SecurityUserName 鉴权用户  445
String SecurityUserName = "";
//AuthPasswd 鉴权密钥  446
String AuthPasswd = "";

//PrivacyPasswd 私钥  447
String PrivacyPasswd = "";

//SNMPVersion 协议版本  448
String SNMPVersion = "";
//ReadOnlyCommunity 读口令  449
String ReadOnlyCommunity = "";
//ReadWriteCommunity 写口令  450
String ReadWriteCommunity = "";
//TrapCommunity Trap口令  451
String TrapCommunity = "";


///
//------------------------------------
if ("2".equals(type)){
	html += "该设备不支持以SNMP协议配置"+theName+"信息";
}
else{
	String[] DHCPName = new String[7];
	//鉴权用户
	//DHCPName[0] = "InternetGatewayDevice.X_CT-COM_SNMP.SecurityUserName";
	//鉴权密钥
	//DHCPName[1] = "InternetGatewayDevice.X_CT-COM_SNMP.AuthPasswd";
	
	//进入下面循环的设备肯定是同一型号的
	for (int i=0;i<device_list.length;i++){
		
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			//鉴权用户  445
			DHCPName[0] = ConfigDevice.getParaArr("445", device_id);
			//鉴权密钥  446
			DHCPName[1] = ConfigDevice.getParaArr("446", device_id);
			//PrivacyPasswd 私钥  447
			DHCPName[2] = ConfigDevice.getParaArr("447", device_id);
			//SNMPVersion 协议版本  448
			DHCPName[3] = ConfigDevice.getParaArr("448", device_id);
			//ReadOnlyCommunity 读口令  449
			DHCPName[4] = ConfigDevice.getParaArr("449", device_id);
			//ReadWriteCommunity 写口令  450
			DHCPName[5] = ConfigDevice.getParaArr("450", device_id);
			//TrapCommunity Trap口令  451
			DHCPName[6] = ConfigDevice.getParaArr("451", device_id);
			
			
			if("".equals(DHCPName[3])){
				html += "设备不支持TR069"+theName;
				break;
			}
			
			Map paraMap = ConfigDevice.getDevInfo_tr069(device_id, curUser.getUser(), DHCPName);
			// duangr Test Begin
			/*
			Map paraMap = new HashMap();
			for(int theint = 0;theint<7;theint++){
				paraMap.put(""+theint,""+theint);
			}
			paraMap.put("3","0");
			*/
			// duangr Test Over
			
			if (paraMap == null){
				html += "获取参数值失败，请检查ACS配置是否正确<br>";
			}
			else{
				//鉴权用户
				SecurityUserName = (String)paraMap.get("0");
				//鉴权密钥
				AuthPasswd = (String)paraMap.get("1");

				//PrivacyPasswd 私钥  447
				PrivacyPasswd = (String)paraMap.get("2");

				//SNMPVersion 协议版本  448
				SNMPVersion = (String)paraMap.get("3");
				//ReadOnlyCommunity 读口令  449
				ReadOnlyCommunity = (String)paraMap.get("4");
				//ReadWriteCommunity 写口令  450
				ReadWriteCommunity = (String)paraMap.get("5");
				//TrapCommunity Trap口令  451
				TrapCommunity = (String)paraMap.get("6");
				
				
				if (SNMPVersion != null){
					html += "获取"+theName+"[协议版本]成功...<br>";
					html += fields.get("device_name") + "的"+theName+"[协议版本]为：" + SNMPVersion+"<br>";
				}
				else{
					html += "获取"+theName+"[协议版本]失败...<br>";
					SecurityUserName = "";
					continue;
				}
				
				if(SNMPVersion.equals("0")){
					// V1 / V2
					if (ReadOnlyCommunity != null){
						html += "获取"+theName+"[读口令]成功...<br>";
						html += fields.get("device_name") + "的"+theName+"[读口令]为：" + ReadOnlyCommunity+"<br>";
					}
					else{
						html += "获取"+theName+"[读口令]失败...<br>";
						ReadOnlyCommunity = "";
					}
					if (ReadWriteCommunity != null){
						html += "获取"+theName+"[写口令]成功...<br>";
						html += fields.get("device_name") + "的"+theName+"[写口令]为：" + ReadWriteCommunity+"<br>";
					}
					else{
						html += "获取"+theName+"[写口令]失败...<br>";
						ReadWriteCommunity = "";
					}
					if (TrapCommunity != null){
						html += "获取"+theName+"[Trap口令]成功...<br>";
						html += fields.get("device_name") + "的"+theName+"[Trap口令]为：" + TrapCommunity+"<br>";
					}
					else{
						html += "获取"+theName+"[Trap口令]失败...<br>";
						TrapCommunity = "";
					}
					
				}else if(SNMPVersion.equals("1")){
					// V3
					if (SecurityUserName != null){
						html += "获取"+theName+"[鉴权用户]成功...<br>";
						html += fields.get("device_name") + "的"+theName+"[鉴权用户]为：" + SecurityUserName+"<br>";
					}
					else{
						html += "获取"+theName+"[鉴权用户]失败...<br>";
						SecurityUserName = "";
					}
					if (AuthPasswd != null){
						html += "获取"+theName+"[鉴权密钥]成功...<br>";
						html += fields.get("device_name") + "的"+theName+"[鉴权密钥]为：" + AuthPasswd+"<br>";
					}
					else{
						html += "获取"+theName+"[鉴权密钥]失败...<br>";
						AuthPasswd = "";
					}
					if (PrivacyPasswd != null){
						html += "获取"+theName+"[私钥]成功...<br>";
						html += fields.get("device_name") + "的"+theName+"[私钥]为：" + PrivacyPasswd+"<br>";
					}
					else{
						html += "获取"+theName+"[私钥]失败...<br>";
						PrivacyPasswd = "";
					}
					
				}
				
				
			}
		}
	}
	
	
}

%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
