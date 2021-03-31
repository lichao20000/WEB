package com.linkage.module.liposs.performance.dao;

import java.util.List;
import java.util.Map;


/**
 * 定义端口过滤规则操作数据库DAO的实现类
 * @author zhangsong(3704)
 * @since 2008-09-05
 * @category performance
 *
 */
public interface PortFilterRuleDAO {
	
	/**
	 * 获得所有设备厂商
	 * @return List list中存放Map key：company 
	 * 							vlaue：对应的值
	 */
	public List getAllCompany();
	
	/**
	 * 根据设备厂商获得所有设备型号
	 * 
	 * @return List List中存放Map key：device_name 
	 * 							vlaue：对应的值
	 */
	public List getAllDeviceModelByCompany(int companyId);
	
	/**
	 * 根据device_model 获得该设备型号对应的过滤规则
	 * @param device_model
	 * 
	 * @return Map key：字段名
	 * 			   value：字段值
	 */
	public Map getFilterRuleByDeviceModelType(String device_model,int type);
	
	/**
	 * 保存过滤规则
	 * @param device_model
	 * 			设备型号
	 * @param type
	 * 			过滤类型
	 * @param value
	 * 			过滤值
	 * 
	 * @return int 大于0，保存成功；小于0，保存失败。
	 */
	public int saveFilterRule(String device_model,int type,String value);
	
	
	
	/**
	 * 根据设备型号和过滤类型删除过滤规则
	 * @param device_model
	 * 			设备型号
	 * @param type
	 * 			过滤类型
	 * 
	 * @return int 大于0，删除成功；小于0，删除失败。
	 */
	public int delFilterRule(String device_model,int type);
	
	/**
	 * 获得规则记录数，用于分页
	 * @return
	 */
	public int getFilterNumber();
	
	
	/**
	 * 获得所有的过滤规则
	 * @return List 存放Map
	 */
	public List getAllFilter(int curPage_splitPage, int num_splitPage);
	
	
	/**
	 * 根据device_model、type、value获得规则信息
	 * @param device_model
	 * 			设备型号
	 * @param type
	 * 			过滤类型
	 * @param value
	 * 			过滤信息
	 * @return List 中存放Map
	 */
	public List getFilterRule(String device_model,int type,String value);
	
	/**
	 * 根据组合的sql语句获得规则列表
	 * @param sql
	 * @return List 存放Map
	 */
	public List getFilterRuleBySql(String sql);
	
	
}
