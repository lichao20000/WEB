<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.software.*"%>
<%@ page import="ACS.*"%>
<jsp:useBean id="paramManage" scope="request" class="com.linkage.litms.paramConfig.ParamManage"/>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.linkage.litms.common.util.Base64" %>
<%@page import="com.linkage.litms.common.util.ToCode16" %>

<%
request.setCharacterEncoding("GBK");

UserRes current_user = (UserRes) session.getAttribute("curUser");
long user_id = current_user.getUser().getId();

String[] device_list = request.getParameter("deviceIds").split(",");

//�����ݿ��в�ѯ��òɼ��㣬�����Ǵ�ҳ����
DeviceAct act = new DeviceAct();
HashMap deviceInfo= act.getDeviceInfo(device_list[0]);
String strGatherId = (String)deviceInfo.get("gather_id");
String devicetype_id = (String)deviceInfo.get("devicetype_id");

String keyword = request.getParameter("keyword"); //�ؼ���
String filetype = request.getParameter("filetype"); //�ļ�����
String file_path = request.getParameter("hid_url_path");//�ļ�·��
String username = request.getParameter("username"); //�û���
String passwd = request.getParameter("passwd"); //����
String dir_id = request.getParameter("dir_id"); //Ŀ¼ID


String filename = request.getParameter("filename") + ".log"; //�ļ�����
//file_path = file_path + "index.jsp?fileName=" +filename+"&userName="+username+"&passwd="+passwd+"&dir_id="+dir_id;
//----######################modify by zhaixf-----------
file_path += "/FILE?";
file_path += ToCode16.encode(Base64.encode("dir_id="+dir_id));

String delay_time = request.getParameter("delay_time"); // ��ʱ

String execu_type = request.getParameter("excute_type");// ִ�з�ʽ

String gwType = request.getParameter("gwType");   //����IBMS��BBMS

boolean result = paramManage.doStrategy(user_id,device_list, execu_type, file_path, 
				username, passwd, delay_time, filename, 3 ,gwType);

%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
var flag = "<%= result%>";
if(flag == "true")
{
	alert("���ú�̨�ɹ�");
}
else
{
	alert("���ú�̨ʧ��");
}
</SCRIPT>
