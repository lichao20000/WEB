<%@page language="java" contentType="application/x-msdownload" pageEncoding="GBK"%>

<%
	String fileName = request.getParameter("fileName");
	String filePath = request.getParameter("filePath");

	response.setContentType("application/x-download");
	
	String fileDownload = filePath + fileName ;  // 要下载文件的物理路径
	String fileDisplay = fileName ;  //  显示给客户的文件名
	
	fileDisplay = java.net.URLEncoder.encode(fileDisplay,"UTF-8");
	
	response.addHeader("Content-Disposition","attachment;filename=" + fileDisplay);

	java.io.OutputStream outp = null;
	java.io.FileInputStream in = null;
	
    try{
		outp = response.getOutputStream();
		in = new java.io.FileInputStream(fileDownload);

        byte[] b = new byte[1024];
        
        int i = 0;

        while((i = in.read(b)) > 0){
            outp.write(b, 0, i);
        }
        
		outp.flush();
    }catch(Exception e){
        System.out.println("Error!");
        e.printStackTrace();
    }finally{
        if(in != null){
			in.close();
			in = null;
        }
        if(outp != null){
            outp.close();
            outp = null;
        }
    }
%>

