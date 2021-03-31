
package com.linkage.module.gtms.stb.resource.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.ImportMacInitBIO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-6-4
 * @category com.linkage.module.lims.stb.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class ImportMacInitACT
{

	private static Logger logger = LoggerFactory.getLogger(ImportMacInitACT.class);
	/** 需要导入的文件（Excel2003 或者 TXT文本文件） */
	private File file = null;
	/** 只解析前rowNum行数据 */
	private int rowNum = 0;
	/** 回参 **/
	private String retResult = null;
	/** 所导入文件的文件名 */
	private String fileName = null;
	/** 上传的文件类型 */
	private String fileType = null;
	private ImportMacInitBIO bio = null;

	/**
	 * 初始化导入界面
	 */
	public String execute()
	{
		logger.debug("ImportMacInitACT==>execute()");
		return "init";
	}

	/**
	 * 下载Excel模板
	 * 
	 * @return
	 */
	public String downloadTemplate()
	{
		return "toExport";
	}

	/**
	 * 解析导入的文件（Excel2003 或则 TXT文本）
	 */
	public String readUploadFile()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		String staff_id = "";
		String city_id = "";
		if (null != curUser)
		{
			staff_id = curUser.getUser().getAccount();
			city_id = curUser.getUser().getCityId();
		}
		retResult = bio.readUploadFile(this.file, this.rowNum, fileType, fileName,
				staff_id, city_id);
		return "init";
	}

	/**
	 * 读取Excel2003模板文件
	 * 
	 * @return
	 */
	public InputStream getExportExcelStream()
	{
		FileInputStream fio = null;
		String path = ServletActionContext.getServletContext().getRealPath("/");
		String separa = System.getProperty("file.separator");
		String filePath = path + "gtms" + separa + "stb" + separa + "resource" + separa
				+ "batchImportMacTemplate.xls";
		try
		{
			fio = new FileInputStream(filePath);
		}
		catch (FileNotFoundException e)
		{
			logger.warn("读取文件异常", e);
		}
		return fio;
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

	public String getFileType()
	{
		return fileType;
	}

	public void setFileType(String fileType)
	{
		this.fileType = fileType;
	}

	public ImportMacInitBIO getBio()
	{
		return bio;
	}

	public void setBio(ImportMacInitBIO bio)
	{
		this.bio = bio;
	}
}
