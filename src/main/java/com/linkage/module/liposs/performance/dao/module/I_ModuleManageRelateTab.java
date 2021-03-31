package com.linkage.module.liposs.performance.dao.module;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.module.liposs.system.moduleinterface.ModuleManager;

public interface I_ModuleManageRelateTab {
	/**
	 * 获取关联表中所有数据
	 * @param mm
	 * @param jt
	 * @return
	 * 	<font color='red'>注：返回的List中Map的Key必须为：</font>
	 *  <font color='red'><br><b>use_column【使用的字段】</b>,<br><b>show_column【显示的字段】</b></font>
	 */
	List<Map> getRelateTabAllData(ModuleManager mm,JdbcTemplate jt);
	
	/**
	 * 获取<font color='red'>已配置</font>关联表中数据
	 * @param mm
	 * @param jt
	 * @return
	 * 	<font color='red'>注：返回的List中Map的Key必须为：</font>
	 *  <font color='red'><br><b>use_column【使用的字段】</b>,<br><b>show_column【显示的字段】</b></font>
	 */
	List<Map> getConfigRelateTabData(ModuleManager mm,JdbcTemplate jt);
	
	/**
	 * 获取<font color='red'>未配置</font>关联表中数据
	 * @param mm
	 * @param jt
	 * @return
	 * 	<font color='red'>注：返回的List中Map的Key必须为：</font>
	 *  <font color='red'><br><b>use_column【使用的字段】</b>,<br><b>show_column【显示的字段】</b></font>
	 */
	List<Map> getUnConfigRelateTabData(ModuleManager mm,JdbcTemplate jt);
}
