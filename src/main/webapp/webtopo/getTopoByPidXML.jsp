<%@page import="com.linkage.litms.common.util.GZIPHandler"%>
<%@page import="com.linkage.litms.webtopo.Scheduler"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.linkage.litms.webtopo.TopoGraphics"%>
<%@page import="com.linkage.litms.webtopo.VpnScheduler"%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	UserRes userRes = (UserRes) session.getAttribute("curUser");
	//String user_id = String.valueOf(userRes.getUser().getId());
	String areaid = "" + userRes.getUser().getAreaId();
	String str = null;
	/*
	if (userRes.getUser().getAccount().equals("admin"))
		areaid = "0";
	else
		areaid = Long.toString(userRes.getAreaId());
	 = Long.toString(userRes.getAreaId());*/

	//�ϲ�ڵ�
	String pid = request.getParameter("pid");
	//pid�������û����
	String topouser_id = request.getParameter("topouser_id");
	String topoType = request.getParameter("topoType");

	if (pid == null || pid.equalsIgnoreCase("NULL") || pid.equals("")) {
		pid = "";
		topouser_id = "-1";
	} else if (pid.indexOf("u") > 0) {
		topouser_id = "-1";
	}
	if (topouser_id == null || topouser_id.equalsIgnoreCase("NULL")) {
		topouser_id = "-1";
	}


	//
	byte[] data = null;

	//vpn topo view xml
	if (topoType != null && topoType.equals("5")) {
		VpnScheduler vpnscheduler = new VpnScheduler();
		data = vpnscheduler.getChildStreamData(areaid, topouser_id, pid);
	} else {
		Scheduler scheduler = new Scheduler();
		data = scheduler.getChildStreamData(areaid, pid);
	}

	if (data != null && data.length > 0) {
		str = GZIPHandler.Decompress(data);
	} else {
		str = "<?xml version=\"1.0\" encoding=\"gb2312\"?><WebTopo></WebTopo>";
	}
	out.println(str);
	//����webtopo_xml�ļ�,��������ͼ����ΪͼƬ---Add By shenkejian 2007-2-28
	//�ж�pid�Ƿ�Ϊ����
/*
	int num1 = 0, num2 = 0;
	num1 = pid.indexOf("/");
	num2 = pid.lastIndexOf("/");
	TopoGraphics topographics = new TopoGraphics();
	HashMap req_pid = new HashMap();
	//���num1��num2��ͬ��˵�������Ϊ����,Ŀǰ���������ݲ�������Ӧ��xml�ļ�
	if (num1 == num2) {
		//��ȡsession����
		HashMap req_pid_2 = (HashMap) session.getAttribute("req_pid");
		String str_segment = (String) req_pid_2.get("pid");
		//����ƴװpid,�������ͬһ�ӿ�,�Ͳ���Ҫ�޸����java����
		String segment_pid = "1/" + str_segment + "/0";
		+ "_webtopo_xml�ļ���");
		topographics.GenerateWebTopoXML(segment_pid, str);
	} else {
		String str_pid = pid.substring(num1 + 1, num2);
		//���������浽session,������������XML�ļ�ʱ����pid
		req_pid.put("pid", str_pid);
		session.setAttribute("req_pid", req_pid);
		topographics.GenerateWebTopoXML(pid, str);
	}
*/
%>
