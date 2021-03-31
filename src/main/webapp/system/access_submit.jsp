<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>

<%
String access_flag = (String)request.getParameter("isAccess");
String gather_id = (String)request.getParameter("gather_id");

String sql = "update tab_auth set access_flag = '" + access_flag + "' where gather_id = '" + gather_id + "'";
int flag = DataSetBean.executeUpdate(sql);


// 开始调用corba接口
com.linkage.litms.common.corba.AccessClient access = new com.linkage.litms.common.corba.AccessClient();


int sheetObj = 0;  //调用接口返回的值
try{
	sheetObj = access.DoRPC(access_flag);
}catch(Exception e){
	sheetObj = access.reDoRPC(access_flag);
	e.printStackTrace();
}

String strData = "";

if (sheetObj == 1){
	strData += "window.alert('认证方式更新成功');";
}
else{
	strData += "window.alert('认证方式更新失败');";
}
%>

<script language="javascript">

<%=strData%>

window.location.replace("access.jsp");

</script>

