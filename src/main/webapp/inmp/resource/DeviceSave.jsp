<%@ include file="../timelater.jsp"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%@ page import = "com.linkage.litms.common.database.DataSetBean" %>
<%@ page contentType="text/html;charset=GBK"%>
<%--
	zhaixf(3412) 2008-05-04
	req:�����豸�жϹ��ܣ�����豸�Ѵ����������
--%>
<%
	//��ҳ��ֻ��Ҫ�����������Ҫҳ���У�����ҳ��ֻ��Ҫ��
	request.setCharacterEncoding("GBK");
	String resultDevice = "";
	String action = request.getParameter("_action");
	if(action.equals("add")){
		String oui = request.getParameter("vendor_id");
		String device_serialnumber = request.getParameter("device_serialnumber");

		String sql = "select * from tab_gw_device where oui='"
					+ oui +"' and device_serialnumber='"
					+ device_serialnumber + "'";
		if(DataSetBean.getRecord(sql) != null){
			resultDevice = "<script language=\"javascript\">alert('���豸�Ѵ���,�������������豸!')</script>";
		}else{
			resultDevice = DeviceAct.getDeviceActMsg(request);
		}
	}else{
		resultDevice = DeviceAct.getDeviceActMsg(request);
	}
	
%>
<%=resultDevice%>