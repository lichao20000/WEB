package com.linkage.module.liposs.performance.bio;

import com.linkage.module.liposs.system.moduleinterface.I_UseModuleCommon;
import com.linkage.module.liposs.system.moduleinterface.ModuleManager;
import com.linkage.system.systemlog.core.SystemLogBean;

/**
 * 【北京酒店网管 模板默认配置】流量配置
 * @author Administrator
 *
 */
public class DefaultConfigFlux extends ConfigFluxBio implements I_UseModuleCommon {
	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.liposs.performance.bio.I_UseModuleCommon#defaultConfig(com.linkage.module.liposs.performance.ModuleManager, com.linkage.system.systemlog.core.SystemLogBean)
	 */
	public boolean defaultConfig(ModuleManager mm, SystemLogBean slb) {
		setSlb(slb);
		return defaultSaveFlux(mm.getDevice_id());
	}
}
