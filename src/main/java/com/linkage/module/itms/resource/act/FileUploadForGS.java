package com.linkage.module.itms.resource.act;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年12月20日
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class FileUploadForGS extends ActionSupport
{
	    //日志记录
		private static Logger logger = LoggerFactory.getLogger(FileUploadForGS.class);

		
		private static final long serialVersionUID = 572146812454l;

		private static final int BUFFER_SIZE = 16 * 1024 * 1024;

		private File myFile;

		@SuppressWarnings("unused")
		private String contentType;

		@SuppressWarnings("unused")
		private String fileName;

		private String imageFileName;
		private String taskFile = "";

		private String caption;
		
		// 流拷贝，要保证目标文件的父文件夹存在，否则抛出FileNotFoundException
		private static void copy(File src, File dst) {
				InputStream in = null;
				OutputStream out = null;
				try {
					in = new BufferedInputStream(new FileInputStream(src));
					out = new BufferedOutputStream(new FileOutputStream(dst));
					
					int file_size = (int) src.length();
					byte[] buffer = new byte[file_size];
					while (in.read(buffer) > 0) {
						out.write(buffer);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					if (null != in) {
						try {
							in.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (null != out) {
						try {
							out.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
		}

		private static String getExtention(String fileName) {
			int pos = fileName.lastIndexOf(".");
			if(pos>-1){
				return fileName.substring(pos);
			}else{
				return "";
			}
		}

		@Override
		public String execute() {
			//获取系统的绝对路径
			String lipossHome = "";
			String a = FileUploadAction.class.getResource("/").getPath();
			try{
				lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
			}catch(Exception e){
				logger.error(e.getMessage());
				lipossHome = null;
			}
			//this.imageFileName = new Date().getTime() + "_" + fileName;
			this.imageFileName = fileName;
			if(LipossGlobals.inArea(Global.SXLT)){
				taskFile = "yes";
			}
			String path = LipossGlobals.getLipossProperty("work_memory_path");
			if(StringUtil.IsEmpty(path)){
				path = "/temp/";
			}
			File imageFile = new File(lipossHome + path + fileName);
			copy(myFile, imageFile);
			return SUCCESS;
		}	
		
		public String downloadTemplete(){
			return "excelTemplete";
		}

		public void setMyFileContentType(String contentType) {
			this.contentType = contentType;
		}

		public void setMyFileFileName(String fileName) {
			this.fileName = fileName;
		}

		public void setMyFile(File myFile) {
			this.myFile = myFile;
		}

		public String getImageFileName() {
			return imageFileName;
		}

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}
		
		/**
		 * @return the fileName
		 */
		public String getFileName() {
			return fileName;
		}

		public String getTaskFile()
		{
			return taskFile;
		}

		public void setTaskFile(String taskFile)
		{
			this.taskFile = taskFile;
		}

}
