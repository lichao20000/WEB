<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.*"%>
<%
request.setCharacterEncoding("GBK");
// ��������  �ɹ���1 ʧ�ܣ� 0
FileSevice  fileSevice = new  FileSevice();
int int_flag  = fileSevice.testConnection(request);

%>
<script language="JavaScript">
<!--
var flag = "<%=int_flag%>";
function init(){
	if(flag == 1){
		alert("���豸���ӳɹ���");
	}else if (flag == 0){
		alert("����δ֪���Ӵ���");
	}
	else if (flag == -1){
		alert("�豸���Ӳ��ϣ�");
	}
	else if (flag == -2){
		alert("�豸����Ϊ�գ�");
	}
	else if (flag == -3){
		alert("�豸����������");
	}
	else if (flag == -4){
		alert("δ֪����ԭ��");
	}
	else {
		alert("����δ֪���Ӵ���");
	}
}
//-->
</script>
<html>

</html>
<script language="JavaScript">
init();
</script>