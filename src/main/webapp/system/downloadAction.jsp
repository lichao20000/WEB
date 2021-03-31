<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.BufferedOutputStream"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ include file="../timelater.jsp"%>
<%
	BufferedInputStream bis = null; 
	BufferedOutputStream bos = null;
	try {
		String filename = request.getParameter("filename");
		String path = request.getParameter("path");
		if("hn_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")))
		{
			String userAgent = request.getHeader("User-Agent");
			String file_name;
			if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {  
				file_name = java.net.URLEncoder.encode(filename, "UTF-8");// is ie 
			} else {  
				file_name = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
			}
			response.setContentType("application/x-msdownload;charset=utf-8");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", -1);
			response.addHeader( "Cache-Control", "no-store");
			response.addHeader( "Cache-Control", "must-revalidate");
			response.setHeader("Content-disposition","attachment; filename="+ file_name);
		}
		else
		{
			filename = new String(filename.getBytes("iso8859-1"), "gb2312");
			path = new String(path.getBytes("iso8859-1"), "gb2312");
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-disposition",
			"attachment; filename="+ new String(filename.getBytes("gb2312"),"iso8859-1"));
		}
		
		bis = new BufferedInputStream(new FileInputStream(path + "/" + filename));
		bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		if (bis != null)
			bis.close();
		if (bos != null)
			bos.close();
	}
%>
