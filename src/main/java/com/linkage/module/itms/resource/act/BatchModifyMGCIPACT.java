
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

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.itms.resource.bio.BatchModifyMGCIPBIO;

/**
 * @author yinlei3 (Ailk No.73167)
 * @version 1.0
 * @since 2016年3月21日
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchModifyMGCIPACT implements ServletRequestAware, ServletResponseAware,
		Serializable
{

	/** 序列化*/
	private static final long serialVersionUID = 1L;
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(BatchModifyMGCIPACT.class);
	/** HttpServletResponse */
	private HttpServletResponse response;
	/** 文件路径 */
	private File file = null;
	/** 执行结果 0:错误 1:正确 */
	private String excuteResult;;
	/** 服务类 */
	private BatchModifyMGCIPBIO bio;
	/** 文件名后缀 */
	private final String XLS = ".xls";

	/**
	 * 初始化导入界面
	 */
	public String execute()
	{
		logger.debug("BatchModifyMGCIPACT==>execute()");
		return "init";
	}

	/**
	 * 下载模板文件
	 * 
	 * @return
	 */
	public String download()
	{
		logger.debug("BatchModifyMGCIPACT==>download()");
		// 文件路径
		String filePath = LipossGlobals.getLipossHome() + File.separator + "temp"
				+ File.separator + "BatchModifyMGCITemplate.xls";
		bio.download(filePath, response);
		return null;
	}

	/**
	 * 上传解析文件
	 * 
	 * @return 上传解析结果
	 */
	public String uploadAndParse()
	{
		logger.debug("BatchModifyMGCIPACT==>uploadAndParse()");
		// 保存的路径
		String storePath = LipossGlobals.getLipossHome() + File.separator + "temp";
		// 防止多人同时上传相同文件名文件
		String fileName = new DateTimeUtil().getYYYYMMDDHHMMSS() + XLS;
		// 上传文件结果
		String fileResult = bio.saveFile(file, storePath, fileName);
		// 上传文件成功, 解析文件
		if ("1".equals(fileResult))
		{
			try
			{
				excuteResult = bio.parseExcel(storePath + File.separator + fileName);
			}
			catch (FileNotFoundException e)
			{
				logger.error("文件不存在" + e);
				excuteResult = "文件不存在, 请重试";
			}
			catch (IOException e)
			{
				logger.error("读写异常" + e);
				excuteResult = "解析异常,请重试";
			}
		}
		else
		{
			excuteResult = "上传文件出错,请重试!";
		}
		return "init";
	}

	public void setBio(BatchModifyMGCIPBIO bio)
	{
		this.bio = bio;
	}

	public String getExcuteResult()
	{
		return excuteResult;
	}

	public File getFile()
	{
		return file;
	}

	public void setFile(File file)
	{
		this.file = file;
	}

	public void setExcuteResult(String excuteResult)
	{
		this.excuteResult = excuteResult;
	}

	@Override
	public void setServletResponse(HttpServletResponse response)
	{
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request)
	{
	}
}
