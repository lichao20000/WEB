package com.linkage.module.gtms.stb.resource.dao;

import java.util.List;
import java.util.Map;

import com.linkage.module.gtms.stb.resource.dto.BSSStatisticsDTO;

public interface BSSStatisticsDAO {

	/**
	 * 根据零配置BSS工单实体查寻想关的工单的sql语句
	 */
	String getSQL(BSSStatisticsDTO dto);
	
	/**
	 * 根据零配置BSS工单实体查寻想关的工单的条数
	 */
	int queryTotalNum(BSSStatisticsDTO dto);
	
	/**
	 * 查寻BSS工单分页数据
	 * @param dto
	 * @param firstResult  第几条数据
	 * @param pageSize  每页显示多少条数据
	 * @return
	 */
	List<Map> queryPageData(BSSStatisticsDTO dto,int firstResult,int pageSize);
}
