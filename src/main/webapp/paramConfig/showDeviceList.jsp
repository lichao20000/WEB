<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage"/>
<%
	request.setCharacterEncoding("GBK");

	String devicetype_id = request.getParameter("devicetype_id");
	//�Ƿ���Ҫ���豸ID���й���·��
	String needFilter = request.getParameter("needFilter");
	if (null == needFilter) {
		needFilter = "false";
	}
	boolean needFilter_b = Boolean.valueOf(needFilter).booleanValue();
	
	//�����ļ��Ƿ�ӱ����ϴ��Ļ��Ǵ��豸�ϱ��������� isFromLocal=trueΪ�����ϴ� isFromLocal=falseΪ�豸�ϱ���
	String isFromLocalStr = request.getParameter("isFromLocal");
	if (null == isFromLocalStr) {
		isFromLocalStr = "false";
	}
	boolean isFromLocal = Boolean.valueOf(isFromLocalStr).booleanValue();
	
	String strList= null;
	
	//��������
	String gw_type = request.getParameter("gw_type");
	strList = versionManage.getDeviceHtmlUseDeviceVersion(request, needFilter_b);
	String inst_area = LipossGlobals.getLipossProperty("InstArea.ShortName");
	String file_path = "";
	if(inst_area.equals("nx_dx"))
	{
		 file_path = versionManage.getFilePath_3("file_path",devicetype_id,"",isFromLocal,gw_type);
		
	}
	else
	{
		 file_path = versionManage.getFilePath_2("file_path",devicetype_id,"",isFromLocal);
	} 
	//String file_path = versionManage.getFilePath_3("file_path",devicetype_id,"",isFromLocal,gw_type);
	
%>
<html>
<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_device").innerHTML = child.innerHTML;
	//if(!<%=needFilter_b%>){
		parent.document.all("filepath").innerHTML = "<%=file_path%>";
	//}
</SCRIPT>
</body>
</html>