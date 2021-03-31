
package com.linkage.module.itms.resource.act;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.itms.resource.bio.DevVendorDataUploadBIO;

/**
 * @author yinlei3 (73167)
 * @version 1.0
 * @since 2015年10月15日
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DevVendorDataUploadACT implements ServletRequestAware, ServletResponseAware,
		Serializable
{

	/** 序列化 */
	private static final long serialVersionUID = 1L;
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(DevVendorDataUploadBIO.class);
	/** 新增-文件路径 **/
	private File filePath = null;
	/** 新增-文件名称 **/
	private String fileName = null;
	/** 软件升级记录查询 用service */
	private DevVendorDataUploadBIO bio;
	/** HttpServletResponse */
	private HttpServletResponse response;
	/** case选择 */
	private String caseDownload;
	/**
	 * 执行结果 0:错误 1:正确
	 */
	private String excuteResult;;

	/**
	 * 上传解析文件
	 * 
	 * @return 上传解析结果
	 */
	public String uploadAndParse()
	{
		// 保存的路径
		String storePath = LipossGlobals.getLipossProperty("uploaddir");
		// 上传文件结果
		String fileResult = bio.saveFile(filePath, storePath, fileName);
		// 上传文件成功, 解析文件
		if ("1".equals(fileResult))
		{
			try
			{
				if (fileName.endsWith("txt"))
				{
					excuteResult = bio.parseTxt(storePath + File.separator + fileName);
				}
				else
				{
					excuteResult = bio.parseExcel(storePath + File.separator + fileName);
				}
			}
			catch (FileNotFoundException e)
			{
				logger.error("文件没找到" + e);
				excuteResult = "0#文件没找到, 请重试";
			}
			catch (IOException e)
			{
				logger.error("读写异常" + e);
				excuteResult = "0#解析异常,请重试";
			}
		}
		else
		{
			excuteResult = "0#上传文件出错,请重试!";
		}
		return "uploadAndParse";
	}

	/**
	 * 下载模板文件
	 * 
	 * @return
	 */
	public String download()
	{
		if (!StringUtil.IsEmpty(caseDownload))
		{
			// 文件路径
			String storePath = LipossGlobals.getLipossProperty("uploaddir");
			// 下载xls模板
			if ("0".equals(caseDownload))
			{
				bio.download(storePath + LipossGlobals.getLipossProperty("downloadXLS"),
						response);
			}
			// 下载txt模板
			else
			{
				bio.download(storePath + LipossGlobals.getLipossProperty("downloadTXT"),
						response);
			}
			return null;
		}
		return null;
	}

	public File getFilePath()
	{
		return filePath;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFilePath(File filePath)
	{
		this.filePath = filePath;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public void setBio(DevVendorDataUploadBIO bio)
	{
		this.bio = bio;
	}

	public String getExcuteResult()
	{
		return excuteResult;
	}

	public void setExcuteResult(String excuteResult)
	{
		this.excuteResult = excuteResult;
	}

	public String getCaseDownload()
	{
		return caseDownload;
	}

	public void setCaseDownload(String caseDownload)
	{
		this.caseDownload = caseDownload;
	}

	public void setServletResponse(HttpServletResponse response)
	{
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request)
	{
	}
}
