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
///����
String theName = "��������";
//SecurityUserName ��Ȩ�û�  445
String SecurityUserName = "";
//AuthPasswd ��Ȩ��Կ  446
String AuthPasswd = "";

//PrivacyPasswd ˽Կ  447
String PrivacyPasswd = "";

//SNMPVersion Э��汾  448
String SNMPVersion = "";
//ReadOnlyCommunity ������  449
String ReadOnlyCommunity = "";
//ReadWriteCommunity д����  450
String ReadWriteCommunity = "";
//TrapCommunity Trap����  451
String TrapCommunity = "";


///
//------------------------------------
if ("2".equals(type)){
	html += "���豸��֧����SNMPЭ������"+theName+"��Ϣ";
}
else{
	String[] DHCPName = new String[7];
	//��Ȩ�û�
	//DHCPName[0] = "InternetGatewayDevice.X_CT-COM_SNMP.SecurityUserName";
	//��Ȩ��Կ
	//DHCPName[1] = "InternetGatewayDevice.X_CT-COM_SNMP.AuthPasswd";
	
	//��������ѭ�����豸�϶���ͬһ�ͺŵ�
	for (int i=0;i<device_list.length;i++){
		
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			//��Ȩ�û�  445
			DHCPName[0] = ConfigDevice.getParaArr("445", device_id);
			//��Ȩ��Կ  446
			DHCPName[1] = ConfigDevice.getParaArr("446", device_id);
			//PrivacyPasswd ˽Կ  447
			DHCPName[2] = ConfigDevice.getParaArr("447", device_id);
			//SNMPVersion Э��汾  448
			DHCPName[3] = ConfigDevice.getParaArr("448", device_id);
			//ReadOnlyCommunity ������  449
			DHCPName[4] = ConfigDevice.getParaArr("449", device_id);
			//ReadWriteCommunity д����  450
			DHCPName[5] = ConfigDevice.getParaArr("450", device_id);
			//TrapCommunity Trap����  451
			DHCPName[6] = ConfigDevice.getParaArr("451", device_id);
			
			
			if("".equals(DHCPName[3])){
				html += "�豸��֧��TR069"+theName;
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
				html += "��ȡ����ֵʧ�ܣ�����ACS�����Ƿ���ȷ<br>";
			}
			else{
				//��Ȩ�û�
				SecurityUserName = (String)paraMap.get("0");
				//��Ȩ��Կ
				AuthPasswd = (String)paraMap.get("1");

				//PrivacyPasswd ˽Կ  447
				PrivacyPasswd = (String)paraMap.get("2");

				//SNMPVersion Э��汾  448
				SNMPVersion = (String)paraMap.get("3");
				//ReadOnlyCommunity ������  449
				ReadOnlyCommunity = (String)paraMap.get("4");
				//ReadWriteCommunity д����  450
				ReadWriteCommunity = (String)paraMap.get("5");
				//TrapCommunity Trap����  451
				TrapCommunity = (String)paraMap.get("6");
				
				
				if (SNMPVersion != null){
					html += "��ȡ"+theName+"[Э��汾]�ɹ�...<br>";
					html += fields.get("device_name") + "��"+theName+"[Э��汾]Ϊ��" + SNMPVersion+"<br>";
				}
				else{
					html += "��ȡ"+theName+"[Э��汾]ʧ��...<br>";
					SecurityUserName = "";
					continue;
				}
				
				if(SNMPVersion.equals("0")){
					// V1 / V2
					if (ReadOnlyCommunity != null){
						html += "��ȡ"+theName+"[������]�ɹ�...<br>";
						html += fields.get("device_name") + "��"+theName+"[������]Ϊ��" + ReadOnlyCommunity+"<br>";
					}
					else{
						html += "��ȡ"+theName+"[������]ʧ��...<br>";
						ReadOnlyCommunity = "";
					}
					if (ReadWriteCommunity != null){
						html += "��ȡ"+theName+"[д����]�ɹ�...<br>";
						html += fields.get("device_name") + "��"+theName+"[д����]Ϊ��" + ReadWriteCommunity+"<br>";
					}
					else{
						html += "��ȡ"+theName+"[д����]ʧ��...<br>";
						ReadWriteCommunity = "";
					}
					if (TrapCommunity != null){
						html += "��ȡ"+theName+"[Trap����]�ɹ�...<br>";
						html += fields.get("device_name") + "��"+theName+"[Trap����]Ϊ��" + TrapCommunity+"<br>";
					}
					else{
						html += "��ȡ"+theName+"[Trap����]ʧ��...<br>";
						TrapCommunity = "";
					}
					
				}else if(SNMPVersion.equals("1")){
					// V3
					if (SecurityUserName != null){
						html += "��ȡ"+theName+"[��Ȩ�û�]�ɹ�...<br>";
						html += fields.get("device_name") + "��"+theName+"[��Ȩ�û�]Ϊ��" + SecurityUserName+"<br>";
					}
					else{
						html += "��ȡ"+theName+"[��Ȩ�û�]ʧ��...<br>";
						SecurityUserName = "";
					}
					if (AuthPasswd != null){
						html += "��ȡ"+theName+"[��Ȩ��Կ]�ɹ�...<br>";
						html += fields.get("device_name") + "��"+theName+"[��Ȩ��Կ]Ϊ��" + AuthPasswd+"<br>";
					}
					else{
						html += "��ȡ"+theName+"[��Ȩ��Կ]ʧ��...<br>";
						AuthPasswd = "";
					}
					if (PrivacyPasswd != null){
						html += "��ȡ"+theName+"[˽Կ]�ɹ�...<br>";
						html += fields.get("device_name") + "��"+theName+"[˽Կ]Ϊ��" + PrivacyPasswd+"<br>";
					}
					else{
						html += "��ȡ"+theName+"[˽Կ]ʧ��...<br>";
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
