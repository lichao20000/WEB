package com.linkage.module.gtms.stb.resource.serv;

import java.util.List;
import java.util.Map;

import com.linkage.module.gtms.stb.resource.dao.BSSStatisticsDAO;
import com.linkage.module.gtms.stb.resource.dto.BSSStatisticsDTO;

public interface BSSStatisticsBIO {

	/**
	 * 根据数值 获取操作类型
	 * 
	 * @param type
	 *            1 ==>开机 ,2==>移机,3==>销户,其他数字==>无
	 * @return
	 */
	String getOperation(int type);

	/**
	 * 根据map对象获取BSS工单实体
	 * 
	 * @param map
	 */
	BSSStatisticsDTO getDtoByMap(Map<String, Object> map);

	/**
	 * 将时间字符串转为时间数字， 如果next为true，则得到下一天的时间数字，如 1999-12-02===>944064000+24*60*60
	 * 如果为false则得到当天的时间数字,如 1999-12-02===>944064000
	 * 
	 * @param time
	 * @param next
	 * @return
	 */
	Long formatTime(String time, boolean next);

	/**
	 * 过滤bss map ,将不存的属性值设为 ""
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> fiterBSSMap(Map<String, Object> map);

	/**
	 * 根据零配置BSS工单实体查寻想关的工单数据
	 * 
	 * @param dto
	 * @return
	 */
	List<Map> exportBSS(BSSStatisticsDTO dto,int firstResult,int pageSize);

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
	List<BSSStatisticsDTO> queryPageData(BSSStatisticsDTO dto,int firstResult,int pageSize);

	/**
	 * 将list集合转为bss工单集合
	 */
	List<BSSStatisticsDTO> getBSSDtos(List<Map> list);
	
	void setbSSStatisticsDao(BSSStatisticsDAO bSSStatisticsDao);
}
