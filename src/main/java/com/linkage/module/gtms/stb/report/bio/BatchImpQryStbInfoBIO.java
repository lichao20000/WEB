/**
 * 
 */
package com.linkage.module.gtms.stb.report.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.report.dao.BatchImpQryStbInfoDAO;
import com.linkage.module.gwms.Global;

/**
 * 工单报表
 * fan'j'm
 * @author HP (AILK No.)
 * @version 1.0
 * @since 2018-4-20
 * @category com.linkage.module.gtms.stb.report.bio
 * @copyright AILK NBS-Network Mgt. RD Dept.
 */
public class BatchImpQryStbInfoBIO {
	
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BatchImpQryStbInfoBIO.class);
	
	private BatchImpQryStbInfoDAO dao;
	
	
	
	private int maxPage_splitPage;

	/**
	 * 清空，导入数据
	 * @param dataList
	 */
	public void insertDate(List<String> dataList) throws Exception
	{
		dao.truncate();
		logger.warn("truncate table tab_temp_import over");
		
		dao.insertDate(dataList);
		logger.warn("insert table tab_temp_import over");
	}

	/**
	 * 查询数量
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param importQueryField
	 * @return
	 */
	public int getCount(int curPage_splitPage, int num_splitPage,String importQueryField)
	{
		int total =  dao.getCount(importQueryField);
		logger.warn("total="+total);
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	/**
	 * 查询数量
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param importQueryField
	 * @return
	 */
	public int getCountItms(int curPage_splitPage, int num_splitPage,String importQueryField)
	{
		int total =  dao.getCountItms(importQueryField);
		logger.warn("total="+total);
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	/**
	 * 查询设备信息 分页
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param importQueryField
	 * @return
	 */
	public List<Map> getResultPage(int curPage_splitPage, int num_splitPage,String importQueryField)
	{
		logger.warn("curPage_splitPage="+curPage_splitPage+", num_splitPage="+num_splitPage+", importQueryField="+importQueryField);
		List<Map> list = dao.getResultPage(curPage_splitPage, num_splitPage, importQueryField);
		deal(list);
		return list;
	}
	
	
	/**
	 * 查询设备信息 分页
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param importQueryField
	 * @return
	 */
	public List<Map> getResultPageItms(int curPage_splitPage, int num_splitPage,String importQueryField)
	{
		logger.warn("curPage_splitPage="+curPage_splitPage+", num_splitPage="+num_splitPage+", importQueryField="+importQueryField);
		List<Map> list = dao.getResultPageItms(curPage_splitPage, num_splitPage, importQueryField);
		deal(list);
		return list;
	}
	
	/**
	 * 查询设备信息
	 * @param importQueryField
	 * @return
	 */
	public List<Map> getResult(String importQueryField)
	{
		List<Map> list = dao.getResult(importQueryField);
		deal(list);
		return list;
	}
	
	/**
	 * 查询设备信息
	 * @param importQueryField
	 * @return
	 */
	public List<Map> getResultItms(String importQueryField)
	{
		List<Map> list = dao.getResultItms(importQueryField);
		deal(list);
		return list;
	}
	
	/*public int getMaxPage_splitPage() {
		return maxPage_splitPage;
	}

	public void setMaxPage_splitPage(int maxPage_splitPage) {
		this.maxPage_splitPage = maxPage_splitPage;
	}*/

	private void deal(List<Map> list)
	{
		if(null!=list && list.size()>0){
			for(int i=0;i<list.size();i++){
				Map<String,String> map = list.get(i);
				String city_id= map.get("city_id");
				long time = StringUtil.getLongValue(map.get("cpe_currentupdatetime"));
				map.put("city_id", StringUtil.IsEmpty(Global.G_CityId_CityName_Map.get(city_id))?"00":Global.G_CityId_CityName_Map.get(city_id));
				map.put("cpe_currentupdatetime", new DateTimeUtil(time*1000).getYYYY_MM_DD_HH_mm_ss());
			}
		}
	}

	public BatchImpQryStbInfoDAO getDao() {
		return dao;
	}

	public void setDao(BatchImpQryStbInfoDAO dao) {
		this.dao = dao;
	}

}
