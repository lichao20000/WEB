<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<jsp:useBean id="areaManage" scope="request" class="com.linkage.litms.system.dbimpl.AreaManageSyb"/>

<%
request.setCharacterEncoding("GBK");
//获取所有区域信息
Cursor cursor = areaManage.getAreaInfoAll();

long m_AreaId = user.getAreaId();
//获取拥有操作权限区域编码
List lAreaIdSelected = null;
if(user.getAccount().equals("admin")){//所有都显示
	lAreaIdSelected = areaManage.getAreaIdAll();
}else{
	lAreaIdSelected = areaManage.getLowerToFloorAreaIds(Integer.parseInt(String.valueOf(m_AreaId)));
}

//加入操作员区域本身
lAreaIdSelected.add(String.valueOf(m_AreaId));

String target = "";
String sqlvalue="";
Map fields = cursor.getNext();
out.clear();
out.println("<?xml version=\"1.0\" encoding=\"gb2312\" ?>");
out.println("<Tree>");
out.println("<TreeView isManage=\"true\" title=\"区域管理\" />");

//统计最低层数,获得层次偏移
int lower_layer = 10000;
while(fields != null){
	//有权限则显示
	if(lAreaIdSelected.contains(fields.get("area_id"))){
		lower_layer = (lower_layer<Integer.parseInt(String.valueOf(fields.get("area_layer"))))?lower_layer:Integer.parseInt(String.valueOf(fields.get("area_layer")));
	
	}
	
	fields = cursor.getNext();
}

lower_layer = (lower_layer==1)?0:(lower_layer-1);

//复位
cursor.Reset();
fields = cursor.getNext();

//来用显示根目录用
boolean flag = false;

int layer = 0;

while(fields != null){
	//有权限则显示
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
