<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>

<%
String strDevice = request.getParameter("device_id");
String[] device_list = strDevice.split(",");
String device_id = "";
String type= request.getParameter("type");
String html = "";
//-----------------------
//SecurityUserName ��Ȩ�û�  445
String SecurityUserName = request.getParameter("SecurityUserName");
//AuthPasswd ��Ȩ��Կ  446
String AuthPasswd = request.getParameter("AuthPasswd");
//PrivacyPasswd ˽Կ  447
String PrivacyPasswd = request.getParameter("PrivacyPasswd");
//SNMPVersion Э��汾  448
String SNMPVersion = request.getParameter("SNMPVersion");
//ReadOnlyCommunity ������  449
String ReadOnlyCommunity = request.getParameter("ReadOnlyCommunity");
//ReadWriteCommunity д����  450
String ReadWriteCommunity = request.getParameter("ReadWriteCommunity");
//TrapCommunity Trap����  451
String TrapCommunity = request.getParameter("TrapCommunity");
//-----------------------
//--------------------

String[] DHCPName = new String[4];
String[] value = new String[4];
String[] theDesc = new String[4];
String[] theSQLDesc = new String[4];
//----------Ҫ��������
//SNMPVersion Э��汾 448
//����������1 2 3   1����v1 ; 2 ���� v2 ; 3 ���� v3 
value[0] = SNMPVersion.equals("3")?"1":"0";
DHCPName[0] = "448";
theDesc[0] = "Э��汾";
theSQLDesc[0] = "snmp_version";
if("3".equals(SNMPVersion)){
	//V3
	//SecurityUserName ��Ȩ�û�  445
	value[1] = SecurityUserName;
	DHCPName[1] = "445";
	theDesc[1] = "��Ȩ�û�";
	theSQLDesc[1] = "security_username";
	//AuthPasswd ��Ȩ��Կ  446
	value[2] = AuthPasswd;
	DHCPName[2] = "446";
	theDesc[2] = "��Ȩ��Կ";
	theSQLDesc[2] = "auth_passwd";
	//PrivacyPasswd ˽Կ  447
	value[3] = PrivacyPasswd;
	DHCPName[3] = "447";
	theDesc[3] = "˽Կ";
	theSQLDesc[3] = "privacy_passwd";
}else{
	// V1 / V2
	//ReadOnlyCommunity ������  449
	value[1] = ReadOnlyCommunity;
	DHCPName[1] = "449";
	theDesc[1] = "������";
	theSQLDesc[1] = "snmp_r_passwd";
	//ReadWriteCommunity д����  450
	value[2] = ReadWriteCommunity;
	DHCPName[2] = "450";
	theDesc[2] = "д����";
	theSQLDesc[2] = "snmp_w_passwd";
	//TrapCommunity Trap����  451
	value[3] = TrapCommunity;
	DHCPName[3] = "451";
	theDesc[3] = "Trap����";
	theSQLDesc[3] = "";
}
//--------------------
//--------------------

//----------

String[] typeArr = new String[4];
typeArr[0] = "1";
typeArr[1] = "1";
typeArr[2] = "1";
typeArr[3] = "1";
///����
String theName = "SNMP��������";

///
ArrayList sqlList = new ArrayList();
String mySql = "";
String myTempSql = "";
// �Ƿ���Ҫ���ı�־
boolean theFlag = false;

String msg = "����"+theName+"�����Ϣ��";


if ("2".equals(type)){
	html += "���豸��֧����SNMPЭ������"+theName+"��Ϣ<br>";
}
else{
	for (int i=0;i<device_list.length;i++){
		
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			// ��Temp SQL���
			myTempSql = "";
			// SNMPVersion Э��汾 448
			DHCPName[0] = ConfigDevice.getParaArr(DHCPName[0], device_id);
			// SecurityUserName ��Ȩ�û�  445  /  ReadOnlyCommunity ������  449
			DHCPName[1] = ConfigDevice.getParaArr(DHCPName[1], device_id);
			//
			DHCPName[2] = ConfigDevice.getParaArr(DHCPName[2], device_id);
			//
			DHCPName[3] = ConfigDevice.getParaArr(DHCPName[3], device_id);
			
			if("".equals(DHCPName[0])||"".equals(DHCPName[0])){
				html += "�豸��֧��"+theName;
				msg += "����" + fields.get("device_name") + "��֧��"+theName+"��";
				break;
			}
			
			int[] ret = ConfigDevice.setDevInfo_tr069(device_id, curUser.getUser(), DHCPName, value, typeArr);
			// duangr Test
			//int[] ret = new int[]{1,1,1,1};
				
			if (ret != null && ret.length > 0){
				theFlag = false;
				for(int j=0;j<4;j++){
					if (ret[j] == 1){
						html += "����" + fields.get("device_name") + "��"+theName+"["+theDesc[j]+"]�ɹ�...<br>";
						msg += "����" + fields.get("device_name") + "��"+theName+"["+theDesc[j]+"]�ɹ���ֵΪ" + value[j] + "��";
						if(!theSQLDesc[j].equals("")){
							if(theFlag)
								myTempSql +=",";
							theFlag = true;
							if(j==0){
								//SNMPVersion Э��汾
								myTempSql += " "+theSQLDesc[j]+"='v"+ SNMPVersion+"' ";
							}else
								myTempSql += " "+theSQLDesc[j]+"='"+ value[j]+"' ";
						}
					}
					else{
						html += "����" + fields.get("device_name") + "��"+theName+"["+theDesc[j]+"]ʧ��...<br>";
						msg += "����" + fields.get("device_name") + "��"+theName+"["+theDesc[j]+"]ʧ�ܣ�ֵΪ" + value[j] + "��";
					}
				}
				
				if(theFlag){
					mySql = "update sgw_security set "+myTempSql+" where device_id='"+device_id+"'";
					sqlList.add(mySql); 
				}
			}
			else{
				html += "����" + fields.get("device_name") + "��"+theName+"["+theDesc[0]+"]["+theDesc[1]+"]["+theDesc[2]+"]["+theDesc[3]+"]ʧ��...<br>";
				msg += "����" + fields.get("device_name") + "��"+theName+"["+theDesc[0]+"]["+theDesc[1]+"]["+theDesc[2]+"]["+theDesc[3]+"]ʧ�ܣ�ֵΪ" + value[0]+" / "+value[1] +" / "+value[2]+" / "+value[3] + "��";
			}
		}
	}
	
	//��ʼ������
	int sqlLen = sqlList.size();
	// duangr Test
	DataSetBean.doBatch(sqlList);
   	//int iCode[] = DataSetBean.doBatch(sqlList);
}

msg = Encoder.toISO(msg);
//����־
//LogItem.getInstance().writeItemLog_other(request, 1, device_id, msg, Encoder.toISO("�ɹ�"), "DNS����");


%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
