<%@page import="com.linkage.litms.webtopo.TopoGraphics"%>
<%@page import="com.linkage.litms.common.util.GZIPHandler"%>
<%@page import="com.linkage.litms.webtopo.Scheduler"%>
<%@page import="com.linkage.litms.system.UserRes"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.linkage.litms.webtopo.VpnScheduler"%>
<%
//@ include file="../timelater.jsp"
%>
<%@ page contentType="text/xml;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	UserRes userRes = (UserRes) session.getAttribute("curUser");
	String areaid = "" + userRes.getUser().getAreaId();
	/*
	if (userRes.getUser().getAccount().equals("admin"))
		areaid = "0";
	else
		areaid = Long.toString(userRes.getAreaId());
	*/
	String pid = request.getParameter("pid");
	String topoType = request.getParameter("topoType");
	String topouser_id = request.getParameter("topouser_id");

	pid = (pid == null || pid.equalsIgnoreCase("NULL") || pid.equals("")) ? "" : pid;
	if (topouser_id == null)
		topouser_id = "-1";


	//
	byte[] data = null;
	String str = null;

	//vpn topo view xml
	if (topoType != null && topoType.equals("5")) {
		VpnScheduler vpnscheduler = new VpnScheduler();
		data = vpnscheduler.getSameStreamData(areaid, topouser_id, pid);
	} else {
		Scheduler scheduler = new Scheduler();
		data = scheduler.getSameStreamData(areaid, pid);
	}

	if (data != null && data.length > 0) {
		str = GZIPHandler.Decompress(data);

	} else {
		str = "<?xml version=\"1.0\" encoding=\"gb2312\"?><WebTopo></WebTopo>";
	}
	out.println(str);
/*
	//用于"返回上一层"后拓扑图数据的XML文件生成
	HashMap req_pid_2 = (HashMap) session.getAttribute("req_pid");
	String str_segment = (String) req_pid_2.get("pid");
	//重新拼装pid
	String segment_pid = "1/" + str_segment + "/0";
	//调用接口
	TopoGraphics topographics = new TopoGraphics();
	topographics.GenerateWebTopoXML(segment_pid, str);
*/	
%>
