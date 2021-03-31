
package com.linkage.module.gtms.stb.report.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.stb.report.dao.CheckFruitQueryDAO;


/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-4-10
 * @category com.linkage.module.lims.stb.report.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class CheckFruitQueryBIO
{

	// 日志
	private static Logger logger = LoggerFactory.getLogger(CheckFruitQueryBIO.class);
	private CheckFruitQueryDAO dao;
	private int maxPage_splitPage;
	/**
	 * 查询
	 * @param starttime    开始时间
	 * @param endtime      结束时间
	 * @param curPage_splitPage 
	 * @param num_splitPage
	 * @param user_id 	          业务账号
	 * @param mac		   MAC
	 * @return
	 */
	public List<Map> Query(String starttime, String endtime, int curPage_splitPage,
			int num_splitPage, String user_id, String mac)
	{
		logger.warn("CheckFruitQueryBIO======Query()===========>参数"+"starttime:"+starttime+"endtime:"+endtime+"user_id:"+user_id+"mac:"+mac);
		maxPage_splitPage = dao.getpaging(starttime, endtime, curPage_splitPage,
				num_splitPage, user_id, mac);
	List<Map> list=dao.Query(starttime, endtime, curPage_splitPage, num_splitPage, user_id,
				mac);
	logger.warn("list=========="+list);
	return list;
	}

	/*
	 * public int paging(String starttime, String endtime, int curPage_splitPage, int
	 * num_splitPage,String user_id,String mac) { return dao.getpaging(starttime, endtime,
	 * curPage_splitPage, num_splitPage, user_id, mac); }
	 */
	/**
	 * @param starttime     开始时间
	 * @param endtime       结束时间
	 * @param user_id       业务账号
	 * @param mac           MAC
	 * @return
	 */
	
	public List<Map> derive(String starttime, String endtime, String user_id, String mac)
	{
		logger.warn("derive( )===========>参数"+"starttime:"+starttime+"endtime:"+endtime+"user_id:"+user_id+"mac:"+mac);
		return dao.derive(starttime, endtime, user_id, mac);
	}

	public CheckFruitQueryDAO getDao()
	{
		return dao;
	}

	public void setDao(CheckFruitQueryDAO dao)
	{
		this.dao = dao;
	}

	public int getMaxPage_splitPage()
	{
		return maxPage_splitPage;
	}

	public void setMaxPage_splitPage(int maxPage_splitPage)
	{
		this.maxPage_splitPage = maxPage_splitPage;
	}
}
