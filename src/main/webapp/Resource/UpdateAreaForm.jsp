<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.system.dbimpl.AreaSyb"%>
<jsp:useBean id="area" scope="request" class="com.linkage.litms.system.dbimpl.AreaSyb"/>
<jsp:useBean id="areaManage" scope="request" class="com.linkage.litms.system.dbimpl.AreaManageSyb"/>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

String action = request.getParameter("action");
String area_name = request.getParameter("area_name");
String remark = request.getParameter("area_desc");

String area_layer = request.getParameter("area_layer");
String area_rootid = request.getParameter("area_rootid");
String area_pid = request.getParameter("area_pid");
String area_id = request.getParameter("area_id");

out.println("<script language=javascript>");

//�������������򹹽�����������
if(action.equals("add")){//�������ڵ�	
	long acc_oid = user.getId();
	int m_AreaMaxId = area.getAreaMaxId();
	
	AreaSyb areaSyb = new AreaSyb(m_AreaMaxId,area_name,Integer.parseInt(area_pid),Integer.parseInt(area_rootid),Integer.parseInt(area_layer),Integer.parseInt(String.valueOf(acc_oid)),remark);
	int flag = areaSyb.insertIntoDbWithNameCheck();//��������
	areaSyb = null;
	//0 ok;1 ����;2 ���ݿ����
	if(flag==0){
		out.println("parent.createAreaRootItem('"+ area_name +"','"+ remark +"','"+ remark +"');");
	}
	
	out.println("parent.createAreaObjectResult("+ flag +");");
}else if(action.equals("del")){//ɾ���ڵ�

	int flag = areaManage.getAreaInfosOfAreaPId(area_id).getRecordSize();
	if(flag>0){

		out.println("parent.isAreaItemExist("+flag+");");
		out.println("</script>");
		return;
	}

	boolean flag1 = areaManage.delArea(area_id);
	if(flag1){
		out.println("parent.delAreaItem();");
	}
	out.println("parent.delAreaItemResult("+ flag1 +");");
}else if(action.equals("update")){//�޸Ľڵ�
	long acc_oid = user.getId();
	

	int flag = areaManage.refAreaInfoWithNameCheck(Integer.parseInt(area_id),area_name,Integer.parseInt(area_pid),Integer.parseInt(area_rootid),Integer.parseInt(area_layer),Integer.parseInt(String.valueOf(acc_oid)),remark);
	
	if(flag==0){
		out.println("parent.resetAreaInfo('"+ area_name +"','"+ remark +"');");
	}
	out.println("parent.updateAreaInfoResult("+ flag +");");
}else if(action.equals("addChild")){//����ӽڵ�
	
	long acc_oid = user.getId();
	int m_AreaMaxId = area.getAreaMaxId();
	
	AreaSyb areaSyb = new AreaSyb(m_AreaMaxId,area_name,Integer.parseInt(area_id),Integer.parseInt(area_rootid),Integer.parseInt(area_layer)+1,Integer.parseInt(String.valueOf(acc_oid)),remark);
	//0 ok;1 ����;2 ���ݿ����
	int flag = areaSyb.insertSubAreaWithNameCheck();
	areaSyb = null;
	
	if(flag==0){
		out.println("parent.createAreaChildItem('"+ area_name +"','"+ remark +"','"+ remark +"');");
	}
	
	out.println("parent.createAreaObjectResult("+ flag +");");
}
out.println("</script>");

%>