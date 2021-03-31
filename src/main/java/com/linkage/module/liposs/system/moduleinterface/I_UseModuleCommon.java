package com.linkage.module.liposs.system.moduleinterface;

import com.linkage.system.systemlog.core.SystemLogBean;

/**
 * 模板默认配置通用接口
 * @author Administrator
 *
 */
public interface I_UseModuleCommon {
	/**
	 * 默认模板配置
	 * @param device_id：设备ID
	 * @param mm:ModuleManager
	 * @param slb：记录日志SESSION
	 * @return true：成功
	 *         false：失败
	 */
	boolean defaultConfig(ModuleManager mm,SystemLogBean slb);
}
