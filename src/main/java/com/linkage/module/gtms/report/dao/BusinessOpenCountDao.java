package com.linkage.module.gtms.report.dao;

import java.util.List;
import java.util.Map;

public interface BusinessOpenCountDao {

	public List<Map> getAllSheetNum(String cityId, String starttime,
			String endtime, String selectTypeId);

	public List<Map> getUserList(String openStatus,String cityId, String parentCityId,String starttime,
			String endtime, int curPageSplitPage, int numSplitPage, String selectTypeId);

	public int getUserCount(String openStatus,String cityId,String parentCityId, String starttime,
			String endtime, int curPageSplitPage, int numSplitPage, String selectTypeId);

	public List<Map> getUserListExcel(String openStatus, String cityId,String parentCityId,
			String starttime, String endtime, String selectTypeId);

}

