<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.webtopo.TopoGraphics"%>
<%@page import="com.linkage.litms.system.UserRes"%>
<%@page import="com.linkage.litms.common.util.GZIPHandler"%>
<%@page import="com.linkage.litms.webtopo.Scheduler"%>
<%--导出拓扑图为图片-下载--%>
<%@ page import="java.io.*"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");

	String pid = request.getParameter("pid");
	String topoType = request.getParameter("topoType"); 
	String selDev = request.getParameter("selDev");
	
	UserRes userRes = (UserRes) session.getAttribute("curUser");
	String areaid = null;
	//if (userRes.getUser().getAccount().equals("admin"))
	//	areaid = "0";
	//else
	areaid = Long.toString(userRes.getAreaId());
	
	byte[] data = null;
	String str = null;

	//获取topo数据
	Scheduler scheduler = new Scheduler();
	if (selDev != null && !"".equals(selDev)){
		String[] devArr = selDev.split("-");
		data = scheduler.getStreamDataByIds(pid, devArr);
	}
	else{
		if (pid == null || "".equals(pid) || "null".equals(pid)){
			data = scheduler.getSameStreamData(areaid, "1/0");
		}
		else{
			data = scheduler.getChildStreamData(areaid, pid);
		}
	}
	

	if (data != null && data.length > 0) {
		str = GZIPHandler.Decompress(data);
	} else {
		str = "<?xml version=\"1.0\" encoding=\"gb2312\"?><WebTopo></WebTopo>";
	}
	
	//调用接口
	TopoGraphics topographics = new TopoGraphics();
	topographics.generateWebTopoXML(pid, str);
	topographics.initImageData(topographics.getxmlPath());
	List list = topographics.createImage(); 
	
	//String filename = pid;
	String filename=String.valueOf(list.get(3));
	String dirName = application.getRealPath("webtopo/webtopodata/images");
	java.io.File strFile = null;
	String strPath = dirName
			+ System.getProperties().getProperty("file.separator")
			+ filename;
	list = null;
	try {
		strFile = new java.io.File(strPath);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	if (strFile != null && strFile.exists() && strFile.isFile()) {
		long filelength = strFile.length();
		OutputStream outputStream = response.getOutputStream();
		InputStream inputStream = new FileInputStream(strPath);
		//设置输出的格式 
		response.reset();
		response.setContentType("application/x-msdownload");
		response.setContentLength((int) filelength);
		response.addHeader("Content-Disposition",
		"attachment; filename=\"topo_img.png\"");
		//循环取出流中的数据 
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = inputStream.read(b)) != -1) {
			outputStream.write(b, 0, len);
		}
		outputStream.flush();
		outputStream.close();
		inputStream.close();
		outputStream = null;
	}
%>

