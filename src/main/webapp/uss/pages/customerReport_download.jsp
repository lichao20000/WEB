<%@ page import="java.util.*,java.io.*,java.net.*;"%>
<%@page language="java" contentType="application/x-msdownload" pageEncoding="gb2312"%>
<%
	//�����ļ�����ʱ�����ļ�������ķ�ʽ����
	//����response.reset()���������еģ�>���治Ҫ���У��������һ����
	request.setCharacterEncoding("GBK");
	String file_path = request.getParameter("file_path");
	String filename = file_path.substring(file_path.lastIndexOf("/")+1, file_path.length());
	response.reset();//���Լ�Ҳ���Բ���
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