
package com.linkage.module.ids.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.dao.NetWorkQualityTestDAO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-11-1
 * @category com.linkage.module.ids.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class NetWorkQualityTestBIO
{

	private static Logger logger = LoggerFactory
			.getLogger(NetWorkQualityTestBIO.class);
	private NetWorkQualityTestDAO dao;
	
	public List<Map> netWorkQualityInfo(String start_time, String end_time,
			String avg_delay, String appear_count, String loss_pp, int curPage_splitPage,
			int num_splitPage)
	{
		return dao.netWorkQualityInfo(start_time, end_time, avg_delay, appear_count, loss_pp, curPage_splitPage, num_splitPage);
	}
	
	public int countNetWorkQualityInfo(String start_time, String end_time,
			String avg_delay, String appear_count, String loss_pp,int curPage_splitPage,
			int num_splitPage)
	{
		return dao.countnetWorkQualityInfo(start_time, end_time, avg_delay, appear_count, loss_pp, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> netWorkQualityExcel(String start_time, String end_time,
			String avg_delay, String appear_count, String loss_pp)
	{
		return dao.netWorkQualityInfoExcel(start_time, end_time, avg_delay, appear_count, loss_pp);
	}

	
	

	/**
	 * 查询网络质量检测分析情况
	 * @param start_time 开始时间
	 * @param end_time 结束时间
	 * @param avg_delay 延迟
	 * @param appear_count 出现次数
	 * @param curPage_splitPage 当前页
	 * @param num_splitPage 当前页个数
	 * @return
	 */
	public List<Map> netWorkQualityTestInfo(String start_time, String end_time,String device_sn, int curPage_splitPage,
			int num_splitPage)
	{
		return dao.netWorkQualityTestInfo(start_time, end_time, device_sn,
				curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 统计网络质量检测分析最大记录数
	 * @param start_time
	 * @param end_time
	 * @param avg_delay
	 * @param appear_count
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int countNetWorkQualityTestInfo(String start_time, String end_time, String device_sn,int curPage_splitPage,
			int num_splitPage)
	{
		return dao.countNetWorkQualityTestInfo(start_time, end_time, device_sn,curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 导出功能
	 * @param start_time
	 * @param end_time
	 * @param avg_delay
	 * @param appear_count
	 * @return
	 */
	public List<Map> netWorkQualityTestExcel(String start_time, String end_time, String device_sn)
	{
		return dao.netWorkQualityTestExcel(start_time, end_time, device_sn);
	}

	public NetWorkQualityTestDAO getDao()
	{
		return dao;
	}

	public void setDao(NetWorkQualityTestDAO dao)
	{
		this.dao = dao;
	}
}
