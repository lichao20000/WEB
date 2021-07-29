/**
 * 
 */
package com.linkage.module.gwms.share.act;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.act
 * 
 */
public class FileUploadAction extends ActionSupport {

	//日志记录
	private static Logger logger = LoggerFactory
			.getLogger(FileUploadAction.class);

	
	private static final long serialVersionUID = 572146812454l;

	private static final int BUFFER_SIZE = 16 * 1024 * 1024;

	private File myFile;

	@SuppressWarnings("unused")
	private String contentType;

	@SuppressWarnings("unused")
	private String fileName;

	private String imageFileName;

	private String caption;

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

	// 流拷贝，要保证目标文件的父文件夹存在，否则抛出FileNotFoundException
	private static void copy(File src, File dst) {
		try {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(src),
						BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst),
						BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				while (in.read(buffer) > 0) {
					out.write(buffer);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		logger.warn("execute");
		//获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try{
			lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
		}catch(Exception e){
			logger.error(e.getMessage());
			lipossHome = null;
		}
		this.imageFileName = new Date().getTime() + "_" + fileName;
		File imageFile = new File(lipossHome + "/temp/" + imageFileName);
		logger.warn("myFile="+myFile+", imageFile="+imageFile);
		copy(myFile, imageFile);
		return SUCCESS;
	}
	
}
