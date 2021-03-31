
package com.linkage.module.itms.report.act;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.itms.report.bio.ExportUserReportBIO;

/**
 * @author hanzezheng (Ailk No.)
 * @version 1.0
 * @since 2015-1-8
 * @category com.linkage.module.itms.report.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
@SuppressWarnings("rawtypes")
public class ExportUserACT extends splitPageAction implements ServletRequestAware,
		SessionAware
{

	/**
	 * serial
	 */
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ExportUserACT.class);
	private ExportUserReportBIO exportBIO;
	HttpServletRequest request = null;
	// session
	private Map<String, Object> session;
	private List<Map> userList = null;
	private String fileNames = null;
	// 导出数据
	private List<Map> data;
	// 导出文件名
	private String fileName;
	// 导出文件列
	private String[] column;
	// 导出文件列标题
	private String[] title;

	public String init()
	{
		return "init";
	}

	public String queryUserList()
	{
		logger.debug("queryUserList()");
		userList = exportBIO.getUserList(fileNames,curPage_splitPage, num_splitPage);
		maxPage_splitPage = exportBIO.getUserCount(fileNames,
				curPage_splitPage, num_splitPage);
		return "userList";
	}

	public String goPage()
	{
		logger.debug("queryUserList()");
		userList = exportBIO.getUserList(fileNames,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = exportBIO.getUserCount(fileNames,
				curPage_splitPage, num_splitPage);
		return "userList";
	}

	public String getUserInfoExcel()
	{
		logger.debug("getHgwExcel2()");
		fileName = "用户列表";
		title = new String[] { "原文件内容", "属  地", "LOID", "宽带账号", "itv账号", "语音账号" };
		column = new String[] { "content", "city_name", "loid", "net_account",
				"itv_account", "voip_account" };
		data = exportBIO.getUseExcel(session.get("fileNames").toString());
		return "excel";
	}

	public ExportUserReportBIO getExportBIO()
	{
		return exportBIO;
	}

	public void setExportBIO(ExportUserReportBIO exportBIO)
	{
		this.exportBIO = exportBIO;
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public List<Map> getUserList()
	{
		return userList;
	}

	public void setUserList(List<Map> userList)
	{
		this.userList = userList;
	}

	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public Map<String, Object> getSession()
	{
		return session;
	}

	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}

	public String getFileNames()
	{
		return fileNames;
	}

	public void setFileNames(String fileNames)
	{
		this.fileNames = fileNames;
	}

	public List<Map> getData()
	{
		return data;
	}

	public void setData(List<Map> data)
	{
		this.data = data;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String[] getColumn()
	{
		return column;
	}

	public void setColumn(String[] column)
	{
		this.column = column;
	}

	public String[] getTitle()
	{
		return title;
	}

	public void setTitle(String[] title)
	{
		this.title = title;
	}
}
