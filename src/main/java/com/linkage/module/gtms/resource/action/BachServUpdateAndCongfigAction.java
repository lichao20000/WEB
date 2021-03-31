package com.linkage.module.gtms.resource.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.resource.serv.BachServConfigBio;

/**
 * 
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since 2014-3-24
 * @category com.linkage.module.gtms.resource.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BachServUpdateAndCongfigAction implements ServletRequestAware

{
private static Logger logger = LoggerFactory.getLogger(BachServUpdateAndCongfigAction.class);
	
	/** 需要导入的文件（Excel2003 或者 TXT文本文件） */
	private File file = null;
	
	/** 只解析前rowNum行数据 */
	private int rowNum = 0;
	
	/** 回参 **/
	private String retResult = null;
	
	/** 所导入文件的文件名 */
	private String fileName = null;
	
	/** request取登陆帐号使用 */
	private HttpServletRequest request;
	
	/** 网关标志 1：家庭网关  2：企业网关 */
	private String gw_type = null;
	
	/** 上传的文件类型 */
	private String fileType = null;
	
	private BachServConfigBio bio = null ;

	/**
	 * 解析导入的文件（Excel2003 或则 TXT文本）
	 */
	public String readUploadFile() {
		
		logger.debug("ImportDeviceInitImp==>readUploadFile()");
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		
		//写文件
		this.WriteFile(curUser);
		
		retResult = bio.readUploadFile(this.file, this.rowNum, curUser, gw_type, fileType, fileName);
		
		return "init";
	}
	
	/**
	 * 下载Excel模板
	 * @return
	 */
	public String downloadTemplate(){
		logger.debug("ImportDeviceInitImp==>downloadTemplate()");
		return "toExport";
	}
	
	
	
	/**
	 * 读取Excel2003模板文件
	 * @return
	 */
	public InputStream getExportExcelStream() {
		
		logger.debug("ImportDeviceInitImp==>getExportExcelStream()");
		
		FileInputStream fio = null;
		
		String filePath = LipossGlobals.getLipossHome() +File.separator+ "temp" + File.separator + "batchServTemplate.xls";
		
		try {
			fio = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			logger.warn("读取文件异常", e);
		}
		return fio;
	}
	
	private void WriteFile(UserRes curUser)
	{
		
		//目录
        File localPath = new File(LipossGlobals.G_ServerHome + "/temp");
        if("2".equals(gw_type))
        {
        	localPath = new File(LipossGlobals.G_ServerHome + "/temp/bbms");
        }
        String fileNamestr = curUser.getUser().getAccount()+"_"+ new DateTimeUtil().getTime() +fileName;
        if(!localPath.exists())
        {
            if(!localPath.mkdirs())
            {
                logger.error("缓存文件目录:{}创建失败！", localPath);
            }
            
        }
        File localFile = new File(localPath +"/"+ fileNamestr);
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try
        {
            fis = new FileInputStream(file);
            fos = new FileOutputStream(localFile);
            
            int ch = fis.read();
            while(ch != -1)
            {
                fos.write((char)ch);
                fos.flush();
                ch = fis.read();
            }
            fos.flush();
        }
        catch (FileNotFoundException e1)
        {
            //本地文件不存在
            logger.error("本地文件不存在！",e1);
        }
        catch (IOException e)
        {
            //读取文件失败
            logger.error("读取本地文件失败！",e);
        }finally
        {
        	 try {
        		 if(fos!=null){
        			 fos.close();
        			 fos=null;
        		 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             try {
            	 if(fis!=null){
            		 fis.close();
            		 fis=null;
            	 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        logger.info("WEB文件{}写入成功",localFile.getAbsolutePath());
		
	}

	/**
	 * 初始化导入界面
	 */
	public String execute() {
		
		logger.debug("ImportDeviceInitImp==>execute()");
		return "init";
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
		
	}

	
	public File getFile()
	{
		return file;
	}

	
	public void setFile(File file)
	{
		this.file = file;
	}

	
	public int getRowNum()
	{
		return rowNum;
	}

	
	public void setRowNum(int rowNum)
	{
		this.rowNum = rowNum;
	}

	
	public String getRetResult()
	{
		return retResult;
	}

	
	public void setRetResult(String retResult)
	{
		this.retResult = retResult;
	}

	
	public String getFileName()
	{
		return fileName;
	}

	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	
	public HttpServletRequest getRequest()
	{
		return request;
	}

	
	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	
	public String getGw_type()
	{
		return gw_type;
	}

	
	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}

	
	public String getFileType()
	{
		return fileType;
	}

	
	public void setFileType(String fileType)
	{
		this.fileType = fileType;
	}

	
	public BachServConfigBio getBio()
	{
		return bio;
	}

	
	public void setBio(BachServConfigBio bio)
	{
		this.bio = bio;
	}
	
}
