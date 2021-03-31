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
//SecurityUserName 鉴权用户  445
String SecurityUserName = request.getParameter("SecurityUserName");
//AuthPasswd 鉴权密钥  446
String AuthPasswd = request.getParameter("AuthPasswd");
//PrivacyPasswd 私钥  447
String PrivacyPasswd = request.getParameter("PrivacyPasswd");
//SNMPVersion 协议版本  448
String SNMPVersion = request.getParameter("SNMPVersion");
//ReadOnlyCommunity 读口令  449
String ReadOnlyCommunity = request.getParameter("ReadOnlyCommunity");
//ReadWriteCommunity 写口令  450
String ReadWriteCommunity = request.getParameter("ReadWriteCommunity");
//TrapCommunity Trap口令  451
String TrapCommunity = request.getParameter("TrapCommunity");
//-----------------------
//--------------------

String[] DHCPName = new String[4];
String[] value = new String[4];
String[] theDesc = new String[4];
String[] theSQLDesc = new String[4];
//----------要入库的数据
//SNMPVersion 协议版本 448
//传过来的是1 2 3   1代表v1 ; 2 代表 v2 ; 3 代表 v3 
value[0] = SNMPVersion.equals("3")?"1":"0";
DHCPName[0] = "448";
theDesc[0] = "协议版本";
theSQLDesc[0] = "snmp_version";
if("3".equals(SNMPVersion)){
	//V3
	//SecurityUserName 鉴权用户  445
	value[1] = SecurityUserName;
	DHCPName[1] = "445";
	theDesc[1] = "鉴权用户";
	theSQLDesc[1] = "security_username";
	//AuthPasswd 鉴权密钥  446
	value[2] = AuthPasswd;
	DHCPName[2] = "446";
	theDesc[2] = "鉴权密钥";
	theSQLDesc[2] = "auth_passwd";
	//PrivacyPasswd 私钥  447
	value[3] = PrivacyPasswd;
	DHCPName[3] = "447";
	theDesc[3] = "私钥";
	theSQLDesc[3] = "privacy_passwd";
}else{
	// V1 / V2
	//ReadOnlyCommunity 读口令  449
	value[1] = ReadOnlyCommunity;
	DHCPName[1] = "449";
	theDesc[1] = "读口令";
	theSQLDesc[1] = "snmp_r_passwd";
	//ReadWriteCommunity 写口令  450
	value[2] = ReadWriteCommunity;
	DHCPName[2] = "450";
	theDesc[2] = "写口令";
	theSQLDesc[2] = "snmp_w_passwd";
	//TrapCommunity Trap口令  451
	value[3] = TrapCommunity;
	DHCPName[3] = "451";
	theDesc[3] = "Trap口令";
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
///描述
String theName = "SNMP访问配置";

///
ArrayList sqlList = new ArrayList();
String mySql = "";
String myTempSql = "";
// 是否需要入库的标志
boolean theFlag = false;

String msg = "设置"+theName+"相关信息：";


if ("2".equals(type)){
	html += "该设备不支持以SNMP协议配置"+theName+"信息<br>";
}
else{
	for (int i=0;i<device_list.length;i++){
		
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			// 将Temp SQL清空
			myTempSql = "";
			// SNMPVersion 协议版本 448
			DHCPName[0] = ConfigDevice.getParaArr(DHCPName[0], device_id);
			// SecurityUserName 鉴权用户  445  /  ReadOnlyCommunity 读口令  449
			DHCPName[1] = ConfigDevice.getParaArr(DHCPName[1], device_id);
			//
			DHCPName[2] = ConfigDevice.getParaArr(DHCPName[2], device_id);
			//
			DHCPName[3] = ConfigDevice.getParaArr(DHCPName[3], device_id);
			
			if("".equals(DHCPName[0])||"".equals(DHCPName[0])){
				html += "设备不支持"+theName;
				msg += "设置" + fields.get("device_name") + "不支持"+theName+"。";
				break;
			}
			
			int[] ret = ConfigDevice.setDevInfo_tr069(device_id, curUser.getUser(), DHCPName, value, typeArr);
			// duangr Test
			//int[] ret = new int[]{1,1,1,1};
				
			if (ret != null && ret.length > 0){
				theFlag = false;
				for(int j=0;j<4;j++){
					if (ret[j] == 1){
						html += "配置" + fields.get("device_name") + "的"+theName+"["+theDesc[j]+"]成功...<br>";
						msg += "设置" + fields.get("device_name") + "的"+theName+"["+theDesc[j]+"]成功，值为" + value[j] + "。";
						if(!theSQLDesc[j].equals("")){
							if(theFlag)
								myTempSql +=",";
							theFlag = true;
							if(j==0){
								//SNMPVersion 协议版本
								myTempSql += " "+theSQLDesc[j]+"='v"+ SNMPVersion+"' ";
							}else
								myTempSql += " "+theSQLDesc[j]+"='"+ value[j]+"' ";
						}
					}
					else{
						html += "配置" + fields.get("device_name") + "的"+theName+"["+theDesc[j]+"]失败...<br>";
						msg += "设置" + fields.get("device_name") + "的"+theName+"["+theDesc[j]+"]失败，值为" + value[j] + "。";
					}
				}
				
				if(theFlag){
					mySql = "update sgw_security set "+myTempSql+" where device_id='"+device_id+"'";
					sqlList.add(mySql); 
				}
			}
			else{
				html += "配置" + fields.get("device_name") + "的"+theName+"["+theDesc[0]+"]["+theDesc[1]+"]["+theDesc[2]+"]["+theDesc[3]+"]失败...<br>";
				msg += "设置" + fields.get("device_name") + "的"+theName+"["+theDesc[0]+"]["+theDesc[1]+"]["+theDesc[2]+"]["+theDesc[3]+"]失败，值为" + value[0]+" / "+value[1] +" / "+value[2]+" / "+value[3] + "。";
			}
		}
	}
	
	//开始入库操作
	int sqlLen = sqlList.size();
	// duangr Test
	DataSetBean.doBatch(sqlList);
   	//int iCode[] = DataSetBean.doBatch(sqlList);
}

msg = Encoder.toISO(msg);
//记日志
//LogItem.getInstance().writeItemLog_other(request, 1, device_id, msg, Encoder.toISO("成功"), "DNS配置");


%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
