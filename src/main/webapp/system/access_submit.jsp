<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>

<%
String access_flag = (String)request.getParameter("isAccess");
String gather_id = (String)request.getParameter("gather_id");

String sql = "update tab_auth set access_flag = '" + access_flag + "' where gather_id = '" + gather_id + "'";
int flag = DataSetBean.executeUpdate(sql);


// ��ʼ����corba�ӿ�
com.linkage.litms.common.corba.AccessClient access = new com.linkage.litms.common.corba.AccessClient();


int sheetObj = 0;  //���ýӿڷ��ص�ֵ
try{
	sheetObj = access.DoRPC(access_flag);
}catch(Exception e){
	sheetObj = access.reDoRPC(access_flag);
	e.printStackTrace();
}

String strData = "";

if (sheetObj == 1){
	strData += "window.alert('��֤��ʽ���³ɹ�');";
}
else{
	strData += "window.alert('��֤��ʽ����ʧ��');";
}
%>

<script language="javascript">

<%=strData%>

window.location.replace("access.jsp");

</script>

