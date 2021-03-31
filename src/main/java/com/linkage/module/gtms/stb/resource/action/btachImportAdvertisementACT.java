
package com.linkage.module.gtms.stb.resource.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.btachImportAdvertisementBIO;
import com.linkage.module.gwms.share.act.FileUploadAction;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-2-12
 * @category com.linkage.module.gtms.stb.resource.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class btachImportAdvertisementACT extends splitPageAction
{

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(btachImportAdvertisementACT.class);
	// 查询结束时间
	private String endTime;
	/** 任务id */
	private String taskId = "";
	/** 任务名 */
	private String taskName = "";
	/** 文件路径 */
	private String filePath = "";
	private String gwShare_fileName;
	private String bootFileName;
	private String startFileName;
	private String authFileName;
	private File bootFile;
	private File startFile;
	private File authFile;
	private String ajax;
	private String Invalid_time;
	private btachImportAdvertisementBIO bio;
	// 查询条件
	private String importQueryField = "servaccount";
	// 回传消息
	private String msg = null;
	/** 分组id */
	private String groupid = "";
	private String groupids = "";
	private String priority="";
	
	public String init()
	{
		DateTimeUtil dt = new DateTimeUtil();
		endTime = dt.getDate();
		dt = new DateTimeUtil(endTime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 240 * 3600 - 1) * 1000);
		endTime = dt.getLongDate();
		return "init";
	}
	
	public String batchImport()
	{
		
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		long currTime = new Date().getTime() / 1000L;
		long taskId = currTime;
		long addTime = currTime;
		if (null != gwShare_fileName)
		{
			gwShare_fileName.trim();
		}
		String fileName_ = gwShare_fileName.substring(gwShare_fileName.length() - 3,
				gwShare_fileName.length());
		if (!"xls".equals(fileName_) && !"txt".equals(fileName_))
		{
			this.msg = "上传的文件格式不正确！";
			return null;
		}
		List<Map<String,String>> dataList = null;
		try
		{
			if ("txt".equals(fileName_))
			{
				dataList = getImportDataByTXT(gwShare_fileName);
			}
			else
			{
				dataList = getImportDataByXLS(gwShare_fileName);
			}
		}
		catch (FileNotFoundException e)
		{
			logger.warn("{}文件没找到！", gwShare_fileName);
			this.msg = "文件没找到！";
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			logger.warn(e.getMessage());
			logger.warn("{}文件解析出错！", gwShare_fileName);
			this.msg = "文件解析出错！";
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn(e.getMessage());
			logger.warn("{}文件解析出错！", gwShare_fileName);
			this.msg = "文件解析出错！";
			return null;
		}
		logger.warn("文件解析dataList.size="+dataList.size());
		if(dataList.size() > 5000){
			logger.warn("文件解析dataList.size大于5000...");
			dataList.subList(0, 5000);
		}
		int tote=bio.insert(dataList, groupids,taskId);
		if(tote >0)
		{
			ajax=bio.OpenDeviceShowPicConfig(taskId, taskName, acc_oid, bootFile, startFile, authFile, bootFileName,startFileName,gwShare_fileName,authFileName,
					groupids,filePath,priority, addTime, Invalid_time);
		}else
		{
			ajax="定制失败!";
			return "ajax";
		}
		return "ajax";
	}

	/**
	 * 解析文件（xls格式）
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws IOException
	 * @throws BiffException
	 * @throws
	 */
	public List<Map<String,String>> getImportDataByXLS(String fileName) throws BiffException,
			IOException
	{
		logger.debug("getImportDataByXLS{}", fileName);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		File f = new File(getFilePath1() + fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		Sheet ws = null;
		// 总sheet数
		// int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m = 0; m < sheetNumber; m++)
		{
			ws = wwb.getSheet(m);
			// 当前页总记录行数和列数
			int rowCount = ws.getRows();
			// int columeCount = ws.getColumns();
			if (null != ws.getCell(0, 0).getContents())
			{
				String line = ws.getCell(0, 0).getContents().trim();
				/**
				 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以设备Id作为条件
				 */
				if (null != line && "业务账号".equals(line))
				{
					this.importQueryField = "serv_account";
				}
				else
				{
					this.importQueryField = "";
				}
			}
			// 取当前页所有值放入list中
			for (int i = 1; i < rowCount; i++)
			{
				String temp = ws.getCell(0, i).getContents();
				if (null != temp && !"".equals(temp))
				{
					if (!"".equals(ws.getCell(0, i).getContents().trim()))
					{
						/*Map<String, String> taskMap=bio.getDeviceInfo(ws.getCell(0, i).getContents().trim());
						logger.warn("taskMap.size="+taskMap.size());
						logger.info("taskMap.size="+taskMap.size());
						if (taskMap != null && !taskMap.isEmpty())
						{
							list.add(taskMap);
						}*/
						Map<String, String> taskMap=new HashMap<String, String>();
						taskMap.put("serv_account", ws.getCell(0, i).getContents().trim());
						list.add(taskMap);
					}
				}
				logger.warn("list.size="+list.size());
				logger.info("list.size="+list.size());
			}
		}
		f = null;
		return list;
	}
	/**
	 * 查询所有分组
	 * @return
	 */
	public String getGroupId()
	{
		ajax=bio.getGroupId();
		return "ajax";
	}
	public List<Map<String,String>> getImportDataByTXT(String fileName) throws FileNotFoundException,
			IOException
	{
		logger.debug("getImportDataByTXT{}", fileName);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath1() + fileName));
		String line = in.readLine();
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以设备id作为条件
		 */
		if (null != line && "设备序列号".equals(line))
		{
			this.importQueryField = "device_serialnumber";
		}
		else if (null != line && "设备ID".equals(line))
		{
			this.importQueryField = "device_id";
		}
		else
		{
			this.importQueryField = "";
		}
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null)
		{
			if (!"".equals(line.trim()))
			{
				/*Map<String, String> taskMap =bio.getDeviceInfo(line.trim());
				if (taskMap != null && !taskMap.isEmpty())
				{
					list.add(taskMap);
				}*/
				Map<String, String> taskMap=new HashMap<String, String>();
				taskMap.put("serv_account", line.trim());
				list.add(taskMap);
			}
		}
		in.close();
		in = null;
		return list;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public String getTaskName()
	{
		return taskName;
	}

	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}
	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath1()
	{
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try
		{
			lipossHome = java.net.URLDecoder.decode(
					a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
	}
	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public String getGwShare_fileName()
	{
		return gwShare_fileName;
	}

	public void setGwShare_fileName(String gwShare_fileName)
	{
		this.gwShare_fileName = gwShare_fileName;
	}

	public String getBootFileName()
	{
		return bootFileName;
	}

	public void setBootFileName(String bootFileName)
	{
		this.bootFileName = bootFileName;
	}

	public String getStartFileName()
	{
		return startFileName;
	}

	public void setStartFileName(String startFileName)
	{
		this.startFileName = startFileName;
	}

	public String getAuthFileName()
	{
		return authFileName;
	}

	public void setAuthFileName(String authFileName)
	{
		this.authFileName = authFileName;
	}

	public File getBootFile()
	{
		return bootFile;
	}

	public void setBootFile(File bootFile)
	{
		this.bootFile = bootFile;
	}

	public File getStartFile()
	{
		return startFile;
	}

	public void setStartFile(File startFile)
	{
		this.startFile = startFile;
	}

	public File getAuthFile()
	{
		return authFile;
	}

	public void setAuthFile(File authFile)
	{
		this.authFile = authFile;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	public btachImportAdvertisementBIO getBio()
	{
		return bio;
	}

	public void setBio(btachImportAdvertisementBIO bio)
	{
		this.bio = bio;
	}

	public String getImportQueryField()
	{
		return importQueryField;
	}

	public void setImportQueryField(String importQueryField)
	{
		this.importQueryField = importQueryField;
	}

	
	public String getGroupid()
	{
		return groupid;
	}

	
	public void setGroupid(String groupid)
	{
		this.groupid = groupid;
	}

	
	public String getGroupids()
	{
		return groupids;
	}

	
	public void setGroupids(String groupids)
	{
		this.groupids = groupids;
	}

	
	public String getInvalid_time()
	{
		return Invalid_time;
	}

	
	public void setInvalid_time(String invalid_time)
	{
		Invalid_time = invalid_time;
	}

	
	public String getPriority()
	{
		return priority;
	}

	
	public void setPriority(String priority)
	{
		this.priority = priority;
	}
	
}
