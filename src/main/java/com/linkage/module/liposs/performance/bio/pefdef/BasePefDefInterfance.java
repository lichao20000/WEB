package com.linkage.module.liposs.performance.bio.pefdef;

import java.util.List;
import java.util.Map;

import com.linkage.module.liposs.performance.bio.Pm_Map_Instance;
import com.linkage.module.liposs.performance.dao.I_configPmeeDao;

/**
 * 性能配置接口
 * 
 * @author Duangr
 * @version 1.0
 * @since 2008-6-30 10:22:46
 * @category PerDef
 */
public interface BasePefDefInterfance
{
	/**
	 * 性能配置所必须的7个参数
	 * 
	 * @param device_id
	 *            设备ID
	 * @param expressionId
	 *            性能表达式ID
	 * @param beforeSql
	 *            SQL前半段
	 * @param departAreaId
	 *            分域域表达式ID
	 * @param middleId
	 *            中转表达式ID
	 * @param descId
	 *            描述表达式ID
	 * @param pm
	 *            PM_instance
	 * @param domainId
	 *            domain表达式
	 * @param domainVRId
	 *            domain与VR关系表达式
	 * @param domainIpPoolId
	 *            domain与ip_pool关系表达式
	 * @param sqlList
	 *            入库语句
	 */
	void setBaseInfo(String device_id, String expressionId, String departAreaId,
			String middleId, String descId, String domainId, String domainVRId,
			String domainIpPoolId, I_configPmeeDao dao_,
			Map<String, Pm_Map_Instance> instanceConfigParam_, Pm_Map_Instance param_,
			List sqlList);
	/**
	 * 性能配置的主要流程
	 */
	int mainConfig();
	/**
	 * 生成配置的SQL列表
	 * 
	 * @return
	 */
	List<String> createSqlList();
}
