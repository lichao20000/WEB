package com.linkage.module.liposs.performance.bio;

import com.linkage.system.systemlog.core.SystemLogBean;

/**
 * 性能配置BIO的接口方法
 * @author Administrator
 * @author BENYP(5260) E-MAIL:BENYP@LIANCHUANG.COM
 * @version 1.0
 * @since 2008-09-25
 * *************************************************修改记录****************************************************
 * 序号      时间     修改人         需求&BUG单号        修改的方法            修改内容                备注
 * -----------------------------------------------------------------------------------------------------------
 *  1   2008-10-17  BENYP(5260)     无            增加ChangeInterval   修改采集时间间隔            贵州要求
 * -----------------------------------------------------------------------------------------------------------
 *  2   2008-10-22  BENYP(5260)     无            ConfigPmeeDB()、     增加日志管理               北京酒店网管要求有日志管理
 *  											  DelPmeeExpression()、                         需要上日志模块，同时要启动
 *                                                EditPmeeExpression()、                        日志的SERVLET，
 *                                                SaveWarn()、                                  否则会报异常
 *                                                ChangeInterval() 
 * ------------------------------------------------------------------------------------------------------------
 *  3                                               
 * ***********************************************************************************************************
 */
public interface I_configPmeeBio {
	/**
	 * 获取已经配置的性能表达式
	 * @param device_id
	 * @param expressionid
	 * @return ajax 返回已经配置了的性能表达式名称
	 */
	String getConfigExp(String device_id,String expressionid);
	/**
	 * 性能配置入库
	 * @param device_id：设备ID【以，分隔】
	 * @param expressionid：性能表达式ID【以，分隔】
	 * @param pm:pm_map_instance实体类
	 * @return
	 */
	boolean ConfigPmeeDB(String device_id,String expressionid,Pm_Map_Instance pm,SystemLogBean slb);
	
	/**
	 * 删除已经配置的实例
	 * @param device_id:设备ID
	 * @param expressionid：性能表达式ID
	 * @param id：实例ID
	 * @param type：类型 all：删除该性能表达式的所有实例
	 *                  one：删除该ID的性能配置
	 * @return true:删除成功
	 *         false:删除失败
	 */
	boolean DelPmeeExpression(String device_id,String expressionid,String id,String type,SystemLogBean slb);
	
	/**
	 * 编辑已配置的实例
	 * @param device_id:设备ID
	 * @param expressionid：性能表达式ID
	 * @param id：实例ID
	 * @param pm：Pm_instance用于存放告警配置
	 * @return
	 */
	boolean EditPmeeExpression(String device_id,String expressionid,String id,Pm_Map_Instance pm,SystemLogBean slb);
	
	/**
	 * 保存告警配置
	 * @param device_id:设备ID
	 * @param expression_id：性能表达式ID
	 * @param pm：告警信息
	 * @param slb SystemLogBean
	 * @return
	 */
	boolean SaveWarn(String device_id,String expression_id,Pm_Map_Instance pm,SystemLogBean slb);
	/**
	 * 修改采集事件间隔
	 * @param interval:采集事件间隔
	 * @param expressionid:性能表达式ID
	 * @param device_id:设备ID
	 * @return
	 */
	boolean ChangeInterval(String interval,String expressionid,String device_id,SystemLogBean slb);
	
}
