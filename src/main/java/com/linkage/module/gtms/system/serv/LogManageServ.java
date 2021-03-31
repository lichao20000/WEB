
package com.linkage.module.gtms.system.serv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.system.dao.ItemDAO;
import com.linkage.module.gtms.system.dao.SystemLogDAO;
import com.linkage.module.gtms.system.obj.LogOBJ;

/**
 * 日志管理业务处理
 * 
 * @author Jason(3412)
 * @date 2010-12-4
 */
public class LogManageServ
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(LogManageServ.class);
	// 菜单DAO
	private ItemDAO itemDao;
	// 系统日志DAO
	private SystemLogDAO logDao;
	public static HashMap<String, String> LogOperMap = new HashMap<String, String>();
	static
	{
		LogOperMap.put("1", "查看");
		LogOperMap.put("2", "增加");
		LogOperMap.put("3", "修改");
		LogOperMap.put("4", "删除");
	}

	/**
	 * 获取日志中菜单的列表
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2010-12-4
	 * @return List
	 */
	public List queryItemList()
	{
		logger.debug("queryItemList()");
		List itemList = null;
		itemList = itemDao.getItemList();
		return itemList;
	}

	/**
	 * 按条件分页查询日志记录
	 * 
	 * @param curPage
	 * @param numPerPage
	 * @param logObj
	 * @return
	 */
	public List queryLog(int curPage, int numPerPage, LogOBJ logObj)
	{
		logger.debug("queryLog({})", logObj);
		List list = logDao.querySystemLogList(curPage, numPerPage, logObj);
		if (list != null)
		{
			Map<String, String> itemMap = itemDao.getItemIdNameMap();
			for (Object obj : list)
			{
				Map map = (Map) obj;
				String itemName = StringUtil.getStringValue(map.get("item"));
				map.put("item", itemMap.get(itemName));
				long logTime = StringUtil.getLongValue(map.get("logtime"));
				map.put("logtime", new DateTimeUtil(logTime * 1000).getLongDate());
			}
		}
		return list;
	}

	/**
	 * 按条件查询日志的总条数
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param monitorId
	 * @param cityId
	 * @param venueName
	 * @return
	 */
	public int queryLogCount(int curPage_splitPage, int num_splitPage, LogOBJ logObj)
	{
		logger.debug("queryLogCount({},{},{})", new Object[] { curPage_splitPage,
				num_splitPage, logObj });
		return logDao.querySystemLogCount(num_splitPage, logObj);
	}

	/**
	 * 日志导出Excel
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2010-12-4
	 * @return String
	 */
	public List<Map> excelLog(LogOBJ logObj)
	{
		logger.debug("excelLog()");
		return logDao.excelLog(logObj);
	}

	public void setLogDao(SystemLogDAO logDao)
	{
		this.logDao = logDao;
	}

	public void setItemDao(ItemDAO itemDao)
	{
		this.itemDao = itemDao;
	}

	public int querySASLogCount(int num_splitPage, LogOBJ logObj)
	{
		logger.debug("querySASLogCount");
		return logDao.querySASLogCount(num_splitPage, logObj);
	}

	public List queryLogSAS(int curPage_splitPage, int num_splitPage, LogOBJ logObj)
	{
		logger.debug("queryLogSAS");
		return logDao.queryLogSAS(curPage_splitPage, num_splitPage, logObj);
	}
}
