
package com.linkage.module.gwms.resource.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.impt.ExcelReader;
import com.linkage.litms.common.util.EqualsUtil;
import com.linkage.module.gwms.resource.bio.BatchMgcBIO;
import com.linkage.module.gwms.resource.obj.BatchMgcBean;

/**
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-9-26
 * @category com.linkage.module.gwms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchMgcACT
{

	private BatchMgcBIO batchMgcBIO;
	/**
	 * 导入文件,根据此文件解析用户账号
	 */
	private File file;
	private List<BatchMgcBean> failedList = null;

	public String execute()
	{
		return "success";
	}

	/**
	 * 下载导入模板 使用Struts2的StreamResult将本地文件导出
	 * 
	 * @return
	 */
	public String downloadTemplate()
	{
		return "toExport";
	}

	public String doUpload() throws IOException
	{
		ExcelReader<BatchMgcBean> reader = null;
		try
		{
			reader = new ExcelReader<BatchMgcBean>(file);
			reader.setBeanProperties(BatchMgcBean.class, BatchMgcBean.MGC_BEAN_PROPERTIES);
			reader.setTitleRow(0);
			BatchMgcBean titleBean = reader.getTitle();
			boolean validTemplate = EqualsUtil.checkBeanProperties(titleBean,
					BatchMgcBean.MGC_BEAN_PROPERTIES, BatchMgcBean.MGC_TITLES);
			if (!validTemplate)
			{
				failedList = new ArrayList<BatchMgcBean>(1);
				titleBean.setFailedLine(0);
				titleBean.setFailedCause("导入模板非法，请下载正确模板后再导入");
				failedList.add(titleBean);
			}
			else
			{
				failedList = new ArrayList<BatchMgcBean>();
				BatchMgcBean mgcBean = null;
				while (reader.hasNextRow())
				{
					mgcBean = reader.getNextRow();
					batchMgcBIO.analizeMgc(mgcBean);
					if (!StringUtil.IsEmpty(mgcBean.getFailedCause()))
					{
						// Excel row start with 0
						mgcBean.setFailedLine(reader.getCurrentRow() + 1);
						failedList.add(mgcBean);
					}
				}
			}
		}
		finally
		{
			if (reader != null)
			{
				reader.destory();
			}
		}
		return "success";
	}

	public InputStream getExportExcelStream() throws FileNotFoundException
	{
		String path = ServletActionContext.getServletContext().getRealPath("/");
		String separa = System.getProperty("file.separator");
		String filePath = path + "gwms" + separa + "resource" + separa
				+ "BatchMgcTemplate.xls";
		FileInputStream fio = new FileInputStream(filePath);
		return fio;
	}

	public void setBatchMgcBIO(BatchMgcBIO batchMgcBIO)
	{
		this.batchMgcBIO = batchMgcBIO;
	}

	public File getFile()
	{
		return file;
	}

	public void setFile(File file)
	{
		this.file = file;
	}

	public List<BatchMgcBean> getFailedList()
	{
		return failedList;
	}

	public void setFailedList(List<BatchMgcBean> failedList)
	{
		this.failedList = failedList;
	}
}
