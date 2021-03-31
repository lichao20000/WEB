package com.linkage.liposs.buss.dao.securitygw;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 获取员工web浏览topN报表信息
 * 
 * @author suixz(5253) 2008-5-6
 * @version 1.0
 * @category securitygw
 */
public interface GetWebTopNHighQueryDAO
{
	List<Map> getWebTopNHighQuery(String deviceId, String startTime, String endTime,
			int topN)throws ParseException;
}
