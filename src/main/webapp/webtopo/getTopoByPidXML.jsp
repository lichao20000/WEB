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

	//上层节点
	String pid = request.getParameter("pid");
	//pid所属的用户编号
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
	//生成webtopo_xml文件,用于拓扑图保存为图片---Add By shenkejian 2007-2-28
	//判断pid是否为网段
/*
	int num1 = 0, num2 = 0;
	num1 = pid.indexOf("/");
	num2 = pid.lastIndexOf("/");
	TopoGraphics topographics = new TopoGraphics();
	HashMap req_pid = new HashMap();
	//如果num1和num2相同，说明进入的为网段,目前对网段数据不生成相应的xml文件
	if (num1 == num2) {
		//获取session内容
		HashMap req_pid_2 = (HashMap) session.getAttribute("req_pid");
		String str_segment = (String) req_pid_2.get("pid");
		//重新拼装pid,方便调用同一接口,就不需要修改相关java方法
		String segment_pid = "1/" + str_segment + "/0";
		+ "_webtopo_xml文件！");
		topographics.GenerateWebTopoXML(segment_pid, str);
	} else {
		String str_pid = pid.substring(num1 + 1, num2);
		//将变量保存到session,用于生成网段XML文件时传送pid
		req_pid.put("pid", str_pid);
		session.setAttribute("req_pid", req_pid);
		topographics.GenerateWebTopoXML(pid, str);
	}
*/
%>
