<%@page import="com.opensymphony.xwork2.util.logging.LoggerFactory"%>
<%@page import="com.opensymphony.xwork2.util.logging.Logger"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.BufferedOutputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="com.linkage.commons.ftp.FtpClient"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%
		String fileName = request.getParameter("nameDowmload");
		String ip = LipossGlobals.getLipossProperty("nodeftp.ip");
		String username = LipossGlobals.getLipossProperty("nodeftp.username");
		String password = LipossGlobals.getLipossProperty("nodeftp.password");
		//FtpClient ftpClient = new FtpClient(ip, username, password);
		FtpClient ftpClient = new FtpClient(ip, username, password);
		File ftpLocalPath = new File(LipossGlobals.getLipossProperty("nodeftp.localDir"));
		if(!ftpLocalPath.exists()){
			ftpLocalPath.mkdir();
		}
		// ä¸è½½å°æ¬å°çæä»¶
		 String targetFilePath = LipossGlobals.getLipossProperty("nodeftp.localDir")
				+ fileName;
		String path = LipossGlobals.getLipossProperty("nodeftp.ftpDir");
		if(path == null){
			path = "";
		}
		String filePath1 = path + fileName;
		if (ftpClient.connect()) {
			// å¼å§ä¸è½½æä»¶
			// downFilePath:http://192.168.2.20:30779/FileServer/FILE/SOFT/test5.txt
			// targetFilePath:/FileServer/FILE/SOFT/test5.txt
			ftpClient.get(filePath1, targetFilePath, false);
			// ä¸è½½æå,éæ¾è¿æ¥
			ftpClient.disconnect();
		} else {
			return;
		}
		response.setContentType("application/x-download");

		File file = new File(targetFilePath);
		// åå¾æä»¶å
		String filename = file.getName();
		response.addHeader("Content-Disposition", "attachment;filename="
				+ new String(filename.getBytes("GBK")));
		java.io.OutputStream outp = null;
		java.io.FileInputStream in = null;
		File localFile = new File(targetFilePath);
	    try{
			outp = response.getOutputStream();
			in = new java.io.FileInputStream(targetFilePath);
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
	    	localFile.delete();
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