<%--
	Ĭ��webtopo����ָ�����˲��
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
//����type�ж�ָ���ĸ�topo
String type = request.getParameter("type");
//Ĭ��topo��  1��������ͼ
if (type == null)
	type = "1";
int iType = Integer.parseInt(type);
//�õ�tab_accounts���еõ�parent_id��Ĭ��topo�㡣
//parent_idΪ��1/jinan/0,1/zibo/0,1/2345��
String parentid = user.getParentID();
//��parentidֵ��Ϊ��
//��ǰ�û���parentidΪ��ʱ������Ҫ�κδ���
if(parentid == null || parentid.equals("") || parentid.equals("null") || parentid.equals("-1")){
	switch(iType){
		case 1:
			parentid = "1/0";
			break;
		case 2:
			parentid = "2/0";
			break;
		case 3:
			parentid = "3/0";
			break;
		case 4:
			parentid = "4/0";
			break;
	}
}
String _page = "main_map.jsp";
String isvip = request.getParameter("isvip");
if(isvip != null && isvip.equals("true"))
	_page = "main_vip.jsp";

response.sendRedirect("./"+ _page +"?type="+ type +"&pid=" + parentid);
%>