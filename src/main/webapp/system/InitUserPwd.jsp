<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.commons.util.EncryptionUtil"%>
<%@ page import="com.linkage.litms.common.util.Base64"%>
<%@ page import="com.linkage.litms.common.util.MD5"%>
<%
request.setCharacterEncoding("GBK");
String acc_oid = request.getParameter("acc_oid");
String strMsg = "��ʼ��Ĭ������ʧ��!";
//AHDX_ITMS-REQ-20180423YQW-001��WEB��¼������ܴ洢���죩 
String psw = "123qwe!@#";
if (LipossGlobals.inArea("ah_dx")) {
	psw = MD5.getMD5(Base64.encode("system@517"));
}
if (LipossGlobals.inArea("nx_dx")||LipossGlobals.inArea("hn_lt") ) {
	// NXDX-REQ-ITMS-20190617-LX-001(�����ݿ�����û�������м��ܱ���)
	psw = MD5.getMD5(Base64.encode(request.getParameter("psw")));
}
if(LipossGlobals.inArea("jl_lt")){
	psw ="Rms@201912";
}
if(LipossGlobals.inArea("hb_lt")){
	psw ="Rms123456";
}
int iCode = DataSetBean.executeUpdate("update tab_accounts set acc_password='" + psw + "' where acc_oid=" + acc_oid);
if(iCode>0){
	strMsg = "��ʼ��Ĭ������ɹ���";
}
%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.commons.util.EncryptionUtil"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
alert("<%=strMsg%>");
this.location = "UserList.jsp";
//-->
</SCRIPT>