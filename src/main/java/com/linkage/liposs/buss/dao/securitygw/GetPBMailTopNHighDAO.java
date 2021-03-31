package com.linkage.liposs.buss.dao.securitygw;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 接口:获取员工浏览mail的topN信息
 * 
 * @author suixz(5253)
 * @version 1.0
 * @category securitygw
 */
public interface GetPBMailTopNHighDAO
{
	List<Map> getMailTopNData(String deviceId, String startTime, String endTime)
			throws ParseException;
}
