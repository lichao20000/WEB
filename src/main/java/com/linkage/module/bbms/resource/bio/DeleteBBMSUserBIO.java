
package com.linkage.module.bbms.resource.bio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.bbms.resource.dao.DeleteBBMSUserDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 云南未使用定制终端用户删除
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class DeleteBBMSUserBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(DeleteBBMSUserBIO.class);
	private String msg = null;
	private DeleteBBMSUserDAO dao;
	// 不存在用户列表
	private List notExistUserList = new ArrayList();
	// 绑定设备用户列表
	private List bindUserList = new ArrayList();
	// 可以删除用户列表
	private List canDelUserList = new ArrayList();
	private String userIds = "";

	@SuppressWarnings("unchecked")
	public boolean getUserList(String fileName, String cityId)
	{
		logger.debug("getUserList({},{})", fileName, cityId);
		if (fileName.length() < 4)
		{
			this.msg = "上传的文件名不正确！";
			return false;
		}
		String fileName_ = fileName.substring(fileName.length() - 3, fileName.length());
		logger.debug("fileName_;{}", fileName_);
		if (!"xls".equals(fileName_) && !"txt".equals(fileName_)
				&& !"csv".equals(fileName_))
		{
			this.msg = "上传的文件格式不正确！";
			return false;
		}
		List<String> dataList = null;
		try
		{
			if ("txt".equals(fileName_))
			{
				dataList = getImportDataByTXT(fileName);
			}
			else
			{
				dataList = getImportDataByXLS(fileName);
			}
		}
		catch (FileNotFoundException e)
		{
			logger.warn("{}文件没找到！", fileName);
			this.msg = "文件没找到！";
			return false;
		}
		catch (IOException e)
		{
			logger.warn("{}文件解析出错！", fileName);
			this.msg = "文件解析出错！";
			return false;
		}
		catch (Exception e)
		{
			logger.warn("{}文件解析出错！", fileName);
			this.msg = "文件解析出错！";
			return false;
		}
		Map<String, Map> map = null;
		if (dataList == null)
		{
			return false;
		}
		if (dataList.size() < 1)
		{
			this.msg = "文件未解析到合法数据！";
			return false;
		}
		if (dataList.size() > 100)
		{
			this.msg = "文件数据超过100！";
			return false;
		}
		else
		{
			map = dao.queryUserByImportUsername(cityId, dataList);
			for (String username : dataList)
			{
				Map tmap = map.get(username);
				if (tmap == null)
				{
					tmap = new HashMap();
					tmap.put("username", username);
					notExistUserList.add(tmap);
				}
				else
				{
					if (true == StringUtil.IsEmpty(StringUtil.getStringValue(tmap
							.get("device_id"))))
					{
						canDelUserList.add(tmap);
						userIds = userIds + tmap.get("user_id") + ",";
					}
					else
					{
						bindUserList.add(tmap);
					}
				}
			}
		}
		return true;
	}

	/**
	 * 解析文件（txt格式）
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws FileNotFoundException
	 *             IOException
	 * @author wangsenbo
	 * @date Jul 26, 2010
	 * @param
	 * @return List<String>
	 */
	public List<String> getImportDataByTXT(String fileName) throws FileNotFoundException,
			IOException
	{
		logger.debug("getImportDataByTXT{}", fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath() + fileName));
		String line = in.readLine();
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到其他时， 则返回null
		 */
		if (null == line || !"用户账号".equals(line))
		{
			this.msg = "查询条件出错！";
			return null;
		}
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null)
		{
			if (!"".equals(line.trim()))
			{
				list.add(line.trim());
			}
		}
		in.close();
		in = null;
		// 处理完毕时，则删掉文件
		File f = new File(getFilePath() + fileName);
		f = null;
		return list;
	}

	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath()
	{
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try
		{
			lipossHome = java.net.URLDecoder.decode(a.substring(0, a
					.lastIndexOf("WEB-INF") - 1), "UTF-8");
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
	}

	/**
	 * 解析文件（xls格式、csv格式）
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws IOException
	 * @throws BiffException
	 * @throws
	 */
	public List<String> getImportDataByXLS(String fileName) throws BiffException,
			IOException
	{
		logger.debug("getImportDataByXLS{}", fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath() + fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		;
		Sheet ws = null;
		// 总sheet数
		// int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m = 0; m < sheetNumber; m++)
		{
			ws = wwb.getSheet(m);
			// 当前页总记录行数和列数
			int rowCount = ws.getRows();
			int columeCount = ws.getColumns();
			// 用户账号所在列
			int columenum = -1;
			/**
			 * 判断导入文件查询的条件，默认为用户账号，当匹配到其他时， 则返回null
			 */
			for (int j = 0; j < columeCount; j++)
			{
				String line = ws.getCell(j, 0).getContents().trim();
				if (null != line && "用户账号".equals(line))
				{
					columenum = j;
					break;
				}
			}
			if (columenum == -1)
			{
//				this.msg = "查询条件出错！";
//				return null;
				columenum = 0;
			}
			// 取当前页所有值放入list中
			for (int i = 1; i < rowCount; i++)
			{
				String temp = ws.getCell(columenum, i).getContents();
				if (null != temp && !"".equals(temp))
				{
					if (!"".equals(ws.getCell(columenum, i).getContents().trim()))
					{
						list.add(ws.getCell(columenum, i).getContents().trim());
					}
				}
			}
		}
		f = null;
		return list;
	}

	/**
	 * @return the msg
	 */
	public String getMsg()
	{
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	/**
	 * @return the dao
	 */
	public DeleteBBMSUserDAO getDao()
	{
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(DeleteBBMSUserDAO dao)
	{
		this.dao = dao;
	}

	/**
	 * @return the notExistUserList
	 */
	public List getNotExistUserList()
	{
		return notExistUserList;
	}

	/**
	 * @param notExistUserList
	 *            the notExistUserList to set
	 */
	public void setNotExistUserList(List notExistUserList)
	{
		this.notExistUserList = notExistUserList;
	}

	/**
	 * @return the bindUserList
	 */
	public List getBindUserList()
	{
		return bindUserList;
	}

	/**
	 * @param bindUserList
	 *            the bindUserList to set
	 */
	public void setBindUserList(List bindUserList)
	{
		this.bindUserList = bindUserList;
	}

	/**
	 * @return the canDelUserList
	 */
	public List getCanDelUserList()
	{
		return canDelUserList;
	}

	/**
	 * @param canDelUserList
	 *            the canDelUserList to set
	 */
	public void setCanDelUserList(List canDelUserList)
	{
		this.canDelUserList = canDelUserList;
	}

	/**
	 * 删除用户
	 * 
	 * @author wangsenbo
	 * @date Jul 28, 2010
	 * @param
	 * @return String
	 */
	public String deleteUser(String userIds)
	{
		List list = new ArrayList();
		String[] userIdArray = userIds.split(",");
		for (int i = 0; i < userIdArray.length; i++)
		{
			list.add(userIdArray[i]);
		}
		return dao.deleteUser(list);
	}

	/**
	 * @return the userIds
	 */
	public String getUserIds()
	{
		return userIds;
	}

	/**
	 * @param userIds
	 *            the userIds to set
	 */
	public void setUserIds(String userIds)
	{
		this.userIds = userIds;
	}
}
