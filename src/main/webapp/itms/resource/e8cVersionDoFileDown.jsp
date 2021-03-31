<%@page import="com.linkage.module.itms.resource.bio.E8cVersionQueryBIO"%>
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
		String fileName = request.getParameter("fileName");
		String filePath = request.getParameter("filePath");
		//ftp到服务器上取数据
		String ip = LipossGlobals.getLipossProperty("ftp.ip");
		String username = LipossGlobals.getLipossProperty("ftp.username");
		String password = LipossGlobals.getLipossProperty("ftp.password");
		//FtpClient ftpClient = new FtpClient(ip, username, password);
		FtpClient ftpClient = new FtpClient(ip, username, password);
		File ftpLocalPath = new File(LipossGlobals.getLipossProperty("ftp.localDir"));
		if(!ftpLocalPath.exists()){
			ftpLocalPath.mkdir();
		}
		// 下载到本地的文件
		 String targetFilePath = LipossGlobals.getLipossProperty("ftp.localDir")
				+ fileName;
		String path = LipossGlobals.getLipossProperty("ftp.ftpDir");
		if(path == null){
			path = "";
		}
		String filePath1 = path + fileName;
		if (ftpClient.connect()) {
			// 开始下载文件
			// downFilePath:http://192.168.2.20:30779/FileServer/FILE/SOFT/test5.txt
			// targetFilePath:/FileServer/FILE/SOFT/test5.txt
			ftpClient.get(filePath1, targetFilePath, false);
			// 下载成功,释放连接
			ftpClient.disconnect();
		} else {
			return;
		}
		response.setContentType("application/x-download");
		
		File file = new File(targetFilePath);
		// 取得文件名
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