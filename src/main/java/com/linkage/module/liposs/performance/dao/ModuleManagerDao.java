package com.linkage.module.liposs.performance.dao;

import java.util.List;
import java.util.Map;

import com.linkage.module.liposs.system.moduleinterface.ModuleManager;
import com.linkage.system.systemlog.core.SystemLogBean;

public interface ModuleManagerDao {
	/**
	 * 获取厂商列表
	 * @return List&lt;Map&gt;
	 *    key:vendor_id,vendor_name
	 */
	List<Map> getVendorList();
	
	/**
	 * 根据厂商ID获取设备型号ID
	 * @param vendor_id:厂商ID
	 * @return
	 * 	List&lt;Map&gt;
	 *  key:serial,device_name
	 */
	List<Map> getSerialListByVendorID(String vendor_id);
	
	/**
	 * 获取配置指标列表
	 * @return
	 *  List&lt;Map&gt;
	 */
	List<Map>getConfigTypeList();
	
	/**
	 * 获取关联表数据
	 * @param mm
	 * @return
	 */
	List<Map>getRelateExpression(ModuleManager mm);
	
	/**
	 * 获取已配置设备型号列表
	 * @param serial：设备型号
	 * @return
	 */
	int getConfigNumBySerial(String serial);
	
	/**
	 * 保存模板
	 * @param template_name:模板名字
	 * @param serial：设备型号
	 * @param atrrvalue：configtype，attr：指标属性值
	 * @param configtype：配置指标
	 * @param slb:日志记录类
	 * @return true配置成功
	 *        false：配置失败
	 */
	boolean saveTemplate(String template_name,String serial,String[][]atrrvalue,String[]configtype,SystemLogBean slb);
	
	/**
	 * 获取已配置模板信息
	 * @return
	 */
	List<Map>getConfigTemplateInfo();
	
	/**
	 * 获取已配置关联表数据
	 * @param mm:
	 * 			<br>
	 * 				<font color="red">必须字段:<br>
	 * 					vendor_id:厂商ID<br>
	 * 				    configtype：配置指标<br>
	 * 				    serial：设备型号
	 *				</font>
	 * @param isconfig:是否获取已配置
	 *          true:获取已配置的
	 *         false：获取未配置的
	 * @return
	 */
	List<Map>getConfigRelateExpression(ModuleManager mm,boolean isconfig);
	
	/**
	 * 删除模板
	 * @param serial
	 * @param slb:日志类
	 * @return
	 */
	boolean delTemplate(String serial,SystemLogBean slb);
	
	/**
	 * 根据设备ID获取模板信息
	 * @param device_id
	 * @return
	 * 		key:template_name:模板名称<br>
	 *      key:id:           对应的applicationContext配置文件中的ID<br>
	 *      key:configtype:   配置指标<br>
	 *      key:url:          配置项的URL地址【context】<br>
	 *      key:vendor_id     厂商ID
	 *      key:serial        设备型号ID
	 */
	List<Map>getTemplateInfoByDevID(String device_id);
	
	/**
	 * 获取设备基本信息
	 * @param device_id:设备ID
	 * @return
	 */
	Map<String,String> getDeviceInfo(String device_id);
	
	/**
	 * 获取ID等基本信息
	 * @param configtype
	 * @return
	 */
	List<Map> getIDByConfigtype(String configtype);
	
	/**
	 * 获取关联表数据
	 * @param configtype
	 * @param serial
	 * @return
	 */
	List<Map<String,Integer>> getAttrValue(String configtype,String serial);
}
