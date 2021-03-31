<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%
request.setCharacterEncoding("GBK");
String device_model_id = request.getParameter("device_model_id");
String getSnmpSecuritySQL = "select * from sgw_model_security_template where device_model_id='" + device_model_id + "'";
// teledb
if (DBUtil.GetDB() == 3) {
	getSnmpSecuritySQL = "select snmp_version, security_username, engine_id, context_name, security_level, auth_protocol," +
			" auth_passwd, privacy_protocol, privacy_passwd, snmp_r_passwd, snmp_w_passwd" +
			" from sgw_model_security_template where device_model_id='" + device_model_id + "'";
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(getSnmpSecuritySQL);
psql.getSQL();
Cursor cursor = DataSetBean.getCursor(getSnmpSecuritySQL);
Map fields = cursor.getNext();
String snmp_version = "v2";//协议版本 v1,v2,v3
String security_username="SMEGWMANAGER";//鉴权用户
String engine_id = "";//SNMP引擎
String context_name = ""; // ContextName
int security_level = 2; //安全级别,1:noAuthNoPriv   2:AuthNoPriv   3:AuthPriv , 默认2
String auth_protocol = "MD5";//鉴权协议
String auth_passwd = "TelecomSNMP";//鉴权密钥,默认TelecomSNMP
String privacy_protocol = "DES";//加解密协议  DES、IDEA、AES128、AES192、AES256
String privacy_passwd = "";//私钥

String snmp_r_passwd = "";// 读口令
String snmp_w_passwd = "";// 写口令
String temp = "";


if(fields!=null){
	snmp_version = (String)fields.get("snmp_version");
	security_username = (String)fields.get("security_username");
	engine_id = (String)fields.get("engine_id");
	context_name = (String)fields.get("context_name");
	security_level = Integer.parseInt((String)fields.get("security_level"));
	temp = (String)fields.get("auth_protocol");
	if(temp.trim().length()>0)
		auth_protocol = temp;
	auth_passwd = (String)fields.get("auth_passwd");
	temp = (String)fields.get("privacy_protocol");
	if(temp.trim().length()>0)
		privacy_protocol = temp;
	privacy_passwd = (String)fields.get("privacy_passwd");
	// 读写口令
	temp = (String)fields.get("snmp_r_passwd");
	if(temp.trim().length()>0)
		snmp_r_passwd = temp;
	temp = (String)fields.get("snmp_w_passwd");
	if(temp.trim().length()>0)
		snmp_w_passwd = temp;
}
//----------手工测试-------------
/*
snmp_version="v3";
security_username = "TEST";
engine_id = "test";
context_name="test";
security_level=1;
auth_protocol="SHA-1";
auth_passwd="duangr";
privacy_protocol="AES128";
privacy_passwd="";
*/
//-----------------------


%>
<HTML>
<BODY BGCOLOR="#FFFFFF">
<FORM name=frm >
</FORM>
<SCRIPT LANGUAGE="JavaScript">
<!--
var snmp_version = "<%=snmp_version %>";
var security_username = "<%=security_username %>";
var engine_id = "<%=engine_id %>";
var context_name = "<%=context_name %>";
var security_level = "<%=security_level %>";
var auth_protocol = "<%=auth_protocol %>";
var auth_passwd = "<%=auth_passwd %>";
var privacy_protocol = "<%=privacy_protocol %>";
var privacy_passwd = "<%=privacy_passwd %>";
var snmp_r_passwd = "<%=snmp_r_passwd %>";
var snmp_w_passwd = "<%=snmp_w_passwd %>";

parent.setVersion(snmp_version);
parent.frm.security_username.value=security_username;
parent.frm.engine_id.value=engine_id;
parent.frm.context_name.value=context_name;
parent.frm.security_level.value=security_level;
parent.setStates(security_level);
parent.frm.auth_protocol.value=auth_protocol;
parent.frm.auth_passwd.value=auth_passwd;
parent.frm.privacy_protocol.value=privacy_protocol;
parent.frm.privacy_passwd.value=privacy_passwd;

parent.frm.snmp_r_passwd.value=snmp_r_passwd;
parent.frm.snmp_w_passwd.value=snmp_w_passwd;

if(snmp_version=="v3"){
	parent.snmpVersionChange(true);
}else{
	parent.snmpVersionChange(false);
}
//-->
</SCRIPT>

</BODY>
</HTML>
