<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<jsp:useBean id="areaManage" scope="request" class="com.linkage.litms.system.dbimpl.AreaManageSyb"/>

<%
request.setCharacterEncoding("GBK");
//��ȡ����������Ϣ
Cursor cursor = areaManage.getAreaInfoAll();

long m_AreaId = user.getAreaId();
//��ȡӵ�в���Ȩ���������
List lAreaIdSelected = null;
if(user.getAccount().equals("admin")){//���ж���ʾ
	lAreaIdSelected = areaManage.getAreaIdAll();
}else{
	lAreaIdSelected = areaManage.getLowerToFloorAreaIds(Integer.parseInt(String.valueOf(m_AreaId)));
}

//�������Ա������
lAreaIdSelected.add(String.valueOf(m_AreaId));

String target = "";
String sqlvalue="";
Map fields = cursor.getNext();
out.clear();
out.println("<?xml version=\"1.0\" encoding=\"gb2312\" ?>");
out.println("<Tree>");
out.println("<TreeView isManage=\"true\" title=\"�������\" />");

//ͳ����Ͳ���,��ò��ƫ��
int lower_layer = 10000;
while(fields != null){
	//��Ȩ������ʾ
	if(lAreaIdSelected.contains(fields.get("area_id"))){
		lower_layer = (lower_layer<Integer.parseInt(String.valueOf(fields.get("area_layer"))))?lower_layer:Integer.parseInt(String.valueOf(fields.get("area_layer")));
	
	}
	
	fields = cursor.getNext();
}

lower_layer = (lower_layer==1)?0:(lower_layer-1);

//��λ
cursor.Reset();
fields = cursor.getNext();

//������ʾ��Ŀ¼��
boolean flag = false;

int layer = 0;

while(fields != null){
	//��Ȩ������ʾ
	if(lAreaIdSelected.contains(fields.get("area_id"))){
		layer = Integer.parseInt(String.valueOf(fields.get("area_layer")));
		layer = layer - lower_layer;		
		out.println("<TreeNode title=\""+fields.get("area_name")+"\" href=\""+sqlvalue
				+"\" id=\""+fields.get("area_id")+"\" remark=\""+fields.get("remark")+"\" target=\""+target
				+"\" pid=\""+(flag?fields.get("area_pid"):"0")+"\" layer=\""+layer+"\" ishas=\"1\" />");
		
		flag = true;
	}
	
	fields = cursor.getNext();
}

out.println("</Tree>");

//clear
lAreaIdSelected.clear();
lAreaIdSelected = null;
fields = null;
cursor = null;
%>
