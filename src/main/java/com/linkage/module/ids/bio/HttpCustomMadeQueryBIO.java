
package com.linkage.module.ids.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.dao.HttpCustomMadeQueryDAO;

/**
 * @author yinlei3 (73167)
 * @version 1.0
 * @since 2015年6月12日
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class HttpCustomMadeQueryBIO
{

	/** 日志 */
	private static Logger logger = LoggerFactory
			.getLogger(HttpCustomMadeQueryBIO.class);
	/** HTTP定制查询用dao */
	private HttpCustomMadeQueryDAO dao;

	/**
	 * 任务查询
	 * 
	 * @param name
	 *            任务名称
	 * @param acc_loginname
	 *            定制人
	 * @param starttime
	 *            开始时间
	 * @param endtime
	 *            结束时间
	 * @param curPage_splitPage
	 *            当前页
	 * @param num_splitPage
	 *            每页条数
	 * @return 任务集合
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> queryTask(String name, String acc_loginname, String starttime,
			String endtime, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("HttpCustomMadeQueryBIO==>queryTask({},{},{},{})", new Object[] {
				name, acc_loginname, starttime, endtime, });
		return dao.queryTask(name, acc_loginname, starttime, endtime, curPage_splitPage,
				num_splitPage);
	}

	/**
	 * 任务查询(分页)
	 * 
	 * @param name
	 *            任务名称
	 * @param acc_loginname
	 *            定制人
	 * @param starttime
	 *            开始时间
	 * @param endtime
	 *            结束时间
	 * @param curPage_splitPage
	 *            当前页
	 * @param num_splitPage
	 *            每页条数
	 * @return 总页数
	 */
	public int queryTaskCount(String name, String acc_loginname, String starttime,
			String endtime, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("HttpCustomMadeQueryBIO==>queryTaskCount({},{},{},{})",
				new Object[] { name, acc_loginname, starttime, endtime, });
		return dao.queryTaskCount(name, acc_loginname, starttime, endtime,
				curPage_splitPage, num_splitPage);
	}

	/**
	 * 获得设备列表
	 * 
	 * @param taskId
	 *            任务Id
	 * @param curPage_splitPage
	 *            当前页
	 * @param num_splitPage
	 *            每页条数
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getTaskDevList(String taskId, int curPage_splitPage,
			int num_splitPage)
	{
		logger.debug("HttpCustomMadeQueryBIO==>getTaskDevList({})",
				new Object[] { taskId });
		return dao.getTaskDevList(taskId, curPage_splitPage, num_splitPage);
	}

	/**
	 * 获得设备列表(分页)
	 * 
	 * @param taskId
	 *            任务Id
	 * @param curPage_splitPage
	 *            当前页
	 * @param num_splitPage
	 *            每页条数
	 * @return 总页数
	 */
	public int getTaskCount(String taskId, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("HttpCustomMadeQueryBIO==>getTaskCount({})", new Object[] { taskId });
		return dao.getTaskDevCount(taskId, curPage_splitPage, num_splitPage);
	}

	public void setDao(HttpCustomMadeQueryDAO dao)
	{
		this.dao = dao;
	}
}
