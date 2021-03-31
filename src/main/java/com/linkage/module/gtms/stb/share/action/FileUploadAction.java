/**
 * 
 */
package com.linkage.module.gtms.stb.share.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
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

	private static final int BUFFER_SIZE = 1024 * 1024;

	private File myFile;

	@SuppressWarnings("unused")
	private String contentType;

	@SuppressWarnings("unused")
	private String fileName;

	private String imageFileName;

	private String caption;
	private InputStream exportTxtStream;

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

	private static void copy(File src, File dst) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src));
			out = new BufferedOutputStream(new FileOutputStream(dst));
			byte[] buffer = new byte[in.available()];
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
		this.imageFileName = new Date().getTime() + getExtention(fileName);
		File imageFile = new File(lipossHome + "/temp/" + imageFileName);
		copy(myFile, imageFile);
		return SUCCESS;
	}
	
	/**
	 * 下载txt模板
	 * @return
	 */
	public String downloadTemplate(){
         return "toExport";
	}

	public InputStream getExportTxtStream()
	{
	    FileInputStream fio = null;
	    String filePath = null;
		if(Global.HBLT.equals(Global.instAreaShortName)){
			filePath = LipossGlobals.getLipossHome() +File.separator+ "temp" + File.separator + "httpTestSpeedTemplate.txt";
		}else{
			logger.warn("不支持其他省份的非txt格式的文本");
		}
		logger.warn("getExportTxtStream txt[{}]",new Object[]{filePath});
		try {
			fio = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			logger.warn("读取文件异常", e);
		}
		return fio;
	}

	public void setExportTxtStream(InputStream exportTxtStream)
	{
		this.exportTxtStream = exportTxtStream;
	}
	
}
