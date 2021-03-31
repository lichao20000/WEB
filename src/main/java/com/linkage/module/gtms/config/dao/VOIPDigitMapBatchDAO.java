package com.linkage.module.gtms.config.dao;

import java.util.List;
import java.util.Map;

public interface VOIPDigitMapBatchDAO {
	/**
	 * 查询所有的语音数图模版
	 */
	public List<Map> queryAllDigitMap(String flag);

    public 	String queryDigitMapById(String mapId, String flag);
    public List<Map> getDevice_id(String con,String condition);
    public List<Map> getDetailsForPage(String device_id,long starttimeCount,long endtimeCount,String openState,
			int curPage_splitPage, int num_splitPage);
    public int getDetailsCount(String device_id,long starttimeCount,long endtimeCount,
			String openState,int num_splitPage);
}
