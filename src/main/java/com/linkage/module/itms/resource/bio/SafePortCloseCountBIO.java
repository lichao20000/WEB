
package com.linkage.module.itms.resource.bio;

import java.util.List;
import java.util.Map;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.itms.resource.dao.SafePortCloseCountDAO;

/**
 * CQDX-REQ-ITMS-20190225-YJ-001(安全端口关闭统计)-修改(1)
 *
 */
public class SafePortCloseCountBIO
{

	private SafePortCloseCountDAO dao;
	
	
	/** 重庆21和23端口关闭统计*/
	public List<Map> portCloseListByCity() {
		return dao.portCloseListByCity();
	}
	/** 重庆21和23端口关闭统计列表导出*/
	public List<Map> reportCountExcel() {
		return dao.reportCountExcel();
	}
	
	/**
	 * 根据条件查询设备列表
	 * @param city_id 属地id
	 * @param type 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryDevList(String city_id, String type, int curPage_splitPage, int num_splitPage){
		return dao.queryDevList(city_id, type, curPage_splitPage, num_splitPage);
	}
	
	
	
	/**
	 * 设备列表最大页数
	 * @param countNum
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int queryDevCount(String countNum, int curPage_splitPage, int num_splitPage) {
		int total = StringUtil.getIntegerValue(countNum);
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public SafePortCloseCountDAO getDao() {
		return dao;
	}

	public void setDao(SafePortCloseCountDAO dao) {
		this.dao = dao;
	}
	
}
