<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.commons.util.EncryptionUtil"%>
<%@ page import="com.linkage.litms.common.util.Base64"%>
<%@ page import="com.linkage.litms.common.util.MD5"%>
<%
request.setCharacterEncoding("GBK");
String acc_oid = request.getParameter("acc_oid");
String strMsg = "初始化默认密码失败!";
//AHDX_ITMS-REQ-20180423YQW-001（WEB登录密码加密存储改造） 
String psw = "123qwe!@#";
if (LipossGlobals.inArea("ah_dx")) {
	psw = MD5.getMD5(Base64.encode("system@517"));
}
if (LipossGlobals.inArea("nx_dx")||LipossGlobals.inArea("hn_lt") ) {
	// NXDX-REQ-ITMS-20190617-LX-001(对数据库里的用户密码进行加密保存)
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
	strMsg = "初始化默认密码成功！";
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