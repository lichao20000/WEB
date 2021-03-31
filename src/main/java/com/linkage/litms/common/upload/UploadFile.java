package com.linkage.litms.common.upload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

/**
 * 文件上传用的servlet，不依赖于struts框架
 * @author 陈仲民（5243）
 * @since 2008-01-15
 * @version v1.0
 * @category 
 */
public class UploadFile extends HttpServlet implements Servlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8745961845626182256L;
	
	/**
	 * 实现get方法
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	/**
	 * 实现post方法
	 */
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//成功标志 0：失败  1：成功
		String flag = "0";
		// 获得上传文件的文件名
		String fileName = String.valueOf(new Date().getTime()) + ".doc";
		
		BufferedInputStream in=null;
		try {
			if (ServletFileUpload.isMultipartContent(request)) {
				
				
				// 创建该对象
				DiskFileItemFactory dff = new DiskFileItemFactory();
				// 指定在内存中缓存数据大小,单位为byte
				dff.setSizeThreshold(20 * 1024);
				// 创建该对象
				ServletFileUpload sfu = new ServletFileUpload(dff);
				// 指定一次上传多个文件的总尺寸
				sfu.setSizeMax(1024 * 1024 * 10);
				// 解析request
				FileItemIterator fii = sfu.getItemIterator(request);
				
				// 请求,并返回FileItemIterator集合
				while (fii.hasNext()) {
					// 从集合中获得一个文件流
					FileItemStream fis = fii.next();
					
					// 过滤掉表单中非文件域
					if (!fis.isFormField() && fis.getName().length() > 0) {
						
						//获取系统的绝对路径
						String lipossHome = "";
						String a = UploadFile.class.getResource("/").getPath();
						try{
							lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
						}
						catch(Exception e){
							e.printStackTrace();
							lipossHome = null;
						}
						
						// 获得文件输入流
						in = new BufferedInputStream(fis.openStream());
						// 获得文件输出流
						BufferedOutputStream out = new BufferedOutputStream(
								new FileOutputStream(new java.io.File(lipossHome + "/temp/"
										+ fileName)));
						// 开始把文件写到你指定的上传文件夹
						Streams.copy(in, out, true);
						
					}
				}
				
				flag = "1";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(in!=null){
					in.close();
				}
			}catch(Exception ie){
				ie.printStackTrace();
			}
			
		}
		
		response.sendRedirect("../Resource/FileUploadResult.jsp?flag="+flag+"&fileName="+fileName);
	}

}
