package com.linkage.module.liposs.performance.dao;

import java.util.List;
import java.util.Map;

import com.linkage.module.liposs.performance.bio.Pm_Map_Instance;

/**
 * 性能配置DAO的接口方法
 * @author Administrator
 * @author BENYP(5260) E-MAIL:BENYP@LIANCHUANG.COM
 * @version 1.0
 * @since 2008-09-25
 * *************************************************修改记录****************************************************
 * 序号      时间     修改人         需求&BUG单号        修改的方法          修改内容                备注
 * -----------------------------------------------------------------------------------------------------------
 *  1   2008-10-17  BENYP(5260)     无            getExpressList()  增加参数expressionid和flg  为了和模板管理融合
 *  ----------------------------------------------------------------------------------------------------------
 *  2   2008-10-17  BENYP(5260)     无            增加ChangeInterval 修改采集时间间隔            贵州要求
 *  ----------------------------------------------------------------------------------------------------------
 *  3
 * ***********************************************************************************************************
 */
public interface I_configPmeeDao {
	
	/**
	 * 获取单个设备的基本信息
	 * @param device_id:设备ID
	 * @return Map<String,String> device_name,loopback_ip,vendor_id
	 */
	Map getDevInfo(String device_id);
	/**
	 * 根据单个厂商ID获取性能表达式列表
	 * @param vendor_id:厂商ID
	 * @param expressionid:已经配置的性能表达式ID
	 * @param flg: true:查询已经定制的性能表达式
	 *             false：查询未定制的性能表达式
	 * @return List<Map<String,String>> 
	 **************************************修改记录*****************************************************************
     * 序号     时间     修改人           需求&BUG单号          			  修改内容                  备注
     *  1  2008-10-17 BENYP(5260) 					    融合北京酒店网管模板配置的需求，增加查询已经定制、未定制的性能表达式
     *  2                          
     *************************************************************************************************************
	 */
	List<Map> getExpressList(String vendor_id,String expresionid,boolean flg);
	/**
	 * 查看多个设备配置结果
	 * @param device_id:设备ID间用
	 * @return
	 */
	List<Map> getConfigResultList(String device_id);
	/**
	 * 获取批量设备的厂商ID
	 * @param device_id:设备ID间用
	 * @return
	 */
	String getVendorID(String device_id);
	/**
	 * 获取已经配置的实例信息
	 * @param device_id:设备ID间用
	 * @param expressionid:性能表达式，各个性能表达式间用，分隔【e.g：2,4】
	 * @return
	 */
	List<Map<String,String>> getConfigedExpInfo(String device_id,String expressionid);
	/**
	 * 删除已经配置的实例
	 * @param device_id:单个设备ID
	 * @param expressionid：单个性能表达式ID
	 * @param id：实例ID
	 * @param type：类型 all：删除该性能表达式的所有实例
	 *                  one：删除该ID的性能配置
	 * @return true:删除成功
	 *         false:删除失败
	 */
	boolean DelPmeeExpression(String device_id,String expressionid,String id,String type);
	/**
	 * 编辑单个实例
	 * @param sql：已经组装好的SQL
	 * @return
	 */
	boolean EditPmeeExpID(String sql);
	
	
	/**
	 * 判断指定设备指定性能表达式，是否配置性能
	 * @param device_id  设备资源表中的设备ID
	 * @param expression_id  pm_expression表中的性能表达式id
	 * @return true:配置了性能   false:没有配置性能
	 */
	boolean isConfigPmee(String device_id,String expression_id);
	
	
	/**
	 * 装载指定设备指定性能表达式的各性能实例的参数设置
	 * @param device_id 设备资源表中的设备ID
	 * @param expression_id pm_expression表中的性能表达式id
	 * @return  map中的key:indexid(实例索引) value：对应Pm_Map_Instance对象 
	 */
	Map<String,Pm_Map_Instance> getPmeeConfigParam(String device_id,String expression_id);
	
	
	
	/**
	 * 删除指定设备指定性能表达式的配置信息
	 * @param device_id  设备资源表中的设备ID
	 * @param expression_id  pm_expression表中的性能表达式id
	 * @return  true：删除成功    false:删除失败
	 */
	boolean  deleteConfig(String device_id,String expression_id);
	
	
	/**
	 * 性能配置流程中操作配置表的sql
	 * @param sqlList  sql链表(update/delete/insert语句)
	 * @return
	 */
	boolean pmeeConfig(List<String> sqlList);
	
	
	/**
	 * 这个性能指标是否需要总体统计
	 * @param expression_id
	 * @return true:需要总体统计  false:不需要
	 */
	boolean isPopulationState(String expression_id);	
	
		
	
	/**
	 * 通过表达式ID获取此表达式的基本信息<br>
	 * 包括2id,表达式分类,是否分域
	 * 
	 * @param expressionId
	 * @return
	 *            <li>Arr[0] = expression2id
	 *            <li>Arr[1] = class2
	 *            <li>Arr[2] = class3
	 */
	String[] getBasicInfoByExpressionId(String expressionId);
	
	
	/**
	 * 判断该设备型号的设备是否需要使用描述OID来采集描述信息,<br>
	 * 需求点来源:JSDX_JS-REQ-20080516-SJ-001<br>
	 * 在设备配置性能时，对华为MA5200G和NE40这两个型号特殊处理，凡是这两个型号的设备在采集端口描述时不用相应的索引转描述的oid，而直接用slot(索引-1)的方式得到端口描述
	 * <p>
	 * 目前不需要描述OID的设备型号
	 * </p>
	 * <li>MA5200G
	 * <li>NE40
	 * 
	 * @param device_id
	 *            设备ID
	 * @return 是以上型号返回true,否则返回false
	 */
	boolean isDeviceModelNotNeedDesc(String device_id);
	
	/**
	 * 获取指定性能指标对应的性能oid、偏差oid
	 * @param expression_id 性能表达式id
	 * @return
	 */
	List<String> getOIDList(String expression_id);
	
	
	/**
	 * 获取指定性能指标的equation
	 * @param expression_id
	 * @return
	 */
	String getEquation(String expression_id);
	
	/**
	 * 获取厂商列表
	 * @return
	 */
	List<Map<String,String>> getVendorList();
	/**
	 * 获取属地及下属属地
	 * @param city_id:用户自身的CityID
	 * @return
	 */
	List<Map<String,String>> getCityList(String city_id);
	/**
	 * 根据厂商获取设备型号
	 * @param vendor_id
	 * @return
	 */
	List<Map<String,String>> getDeviceModelByVendor(String vendor_id);
	
	/**
	 * 根据属地、厂商、设备型号查询设备
	 * @param city_id
	 * @param vendor_id
	 * @param device_model
	 * @param area_id
	 * @param isadmin
	 * 
	 * 在原有基础上增加了String gw_type
	 * 
	 * @return
	 */
	List<Map<String,String>> getDeviceList(String gw_type, String city_id,String vendor_id,String device_model,long area_id,boolean isadmin);
	
	/**
	 * 根据设备名称或设备IP查询设备
	 * @param device_name:设备名称
	 * @param loopback_ip：设备IP
	 * @param area_id：用户自身的域ID
	 * @param isadmin:是否是ADMIN用户
	 * 
	 * 在原有基础上增加了String gw_type
	 * 
	 * @return
	 */
	List<Map<String,String>> getDevListByName(String gw_type, String device_name,String loopback_ip,long area_id,boolean isadmin);
	
	/**
	 * 修改采集事件间隔
	 * @param interval:采集事件间隔
	 * @param expressionid:性能表达式ID
	 * @param device_id:设备ID
	 * @return
	 */
	boolean ChangeInterval(String interval,String expressionid,String device_id);
	
}
