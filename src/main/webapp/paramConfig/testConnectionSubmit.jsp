<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.*"%>
<%
request.setCharacterEncoding("GBK");


// ��������  �ɹ���1 ʧ�ܣ� 0
FileSevice  fileSevice = new  FileSevice();
int int_flag  = fileSevice.testConnection(request);

String flag = null;

if(int_flag == 1){
	flag = "���豸���ӳɹ���";
}else if (int_flag == 0){
	flag = "����δ֪���Ӵ���";
}
else if (int_flag == -1){
	flag = "�豸���Ӳ��ϣ�";
}
else if (int_flag == -2){
	flag = "�豸����Ϊ�գ�";
}
else if (int_flag == -3){
	flag = "�豸����������";
}
else if (int_flag == -4){
	flag = "δ֪����ԭ��";
}
else {
	flag = "����δ֪���Ӵ���";
}
//System.out.println("----"+flag);
%>
<%=flag%>