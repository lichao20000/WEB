<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="paramManage" scope="request" class="com.linkage.litms.paramConfig.ParamManage"/>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage"/>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%@page import="java.util.HashMap"%>
<%
request.setCharacterEncoding("GBK");

UserRes current_user = (UserRes) session.getAttribute("curUser");
long user_id = current_user.getUser().getId();

String[] device_list = request.getParameterValues("device_id");
//�����ݿ��в�ѯ��òɼ��㣬�����Ǵ�ҳ����
DeviceAct act = new DeviceAct();
HashMap deviceInfo=null;
if("4".equals(request.getParameter("gw_type"))){
	deviceInfo= act.getStbDeviceInfo(device_list[0]);
}else{
	deviceInfo= act.getDeviceInfo(device_list[0]);
}
String strGatherId = (String)deviceInfo.get("gather_id");
String devicetype_id = (String)deviceInfo.get("devicetype_id");

String execu_type = request.getParameter("excute_type");
/**
if("2".equals(execu_type))
{
	boolean operResult =versionManage.isAllowConfig(request);
	if(!operResult)
	{
		response.sendRedirect("./configrecover.jsp?operResult=notAllow");
		return;
	}
}
**/
//String strGatherId = request.getParameter("gather_id");


//----------------------------���ûָ���������-----------------------------------------------

String keyword = request.getParameter("keyword"); //�ؼ���
String filetype = request.getParameter("filetype"); //�ļ�����
String[] file_path2 = request.getParameter("file_path").split("\\|");
String file_path = file_path2[0]; //�ļ�·��
String username = request.getParameter("username"); //�û���
String passwd = request.getParameter("passwd"); //����
String file_size = request.getParameter("file_size"); //�ļ���С
String filename = request.getParameter("filename"); //�ļ�����
String delay_time = request.getParameter("delay_time"); //��ʱ
String sucess_url = request.getParameter("sucess_url"); //�ɹ�URL
String fail_url = request.getParameter("fail_url"); //ʧ��URL
String gw_type = request.getParameter("gw_type");  //����ITMS��BBMS  add zhangsb3

boolean result = paramManage.doStrategy(user_id,device_list, "0", file_path, 
				username, passwd, delay_time, filename, 2,gw_type);

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
