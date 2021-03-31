<%@ page import="java.util.*,java.io.*,java.net.*;"%>
<%@page language="java" contentType="application/x-msdownload" pageEncoding="gb2312"%>
<%
	//关于文件下载时采用文件流输出的方式处理：
	//加上response.reset()，并且所有的％>后面不要换行，包括最后一个；
	request.setCharacterEncoding("GBK");
	String file_path = request.getParameter("file_path");
	String filename = file_path.substring(file_path.lastIndexOf("/")+1, file_path.length());
	response.reset();//可以加也可以不加
	response.setContentType("application/x-download");
	String filedisplay = filename;
	filedisplay = URLEncoder.encode(filedisplay, "GBK");
	response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);

	OutputStream outp = null;
	FileInputStream in = null;
	try {
		outp = response.getOutputStream();
		in = new FileInputStream(file_path);

		byte[] b = new byte[1024];
		int i = 0;

		while ((i = in.read(b)) > 0) {
			outp.write(b, 0, i);
		}
		outp.flush();
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		if (in != null) {
			in.close();
			in = null;
		}
		if (outp != null) {
			outp.close();
			outp = null;
		}
	}
%>