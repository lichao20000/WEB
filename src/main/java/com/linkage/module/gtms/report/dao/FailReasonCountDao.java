package com.linkage.module.gtms.report.dao;

import java.util.Map;

public interface FailReasonCountDao {

	public Map<String,Map<String,Integer>> getFailReasonCount(String cityId, String starttime, String endtime);

	public Map<String, String> getFailCode();

}
