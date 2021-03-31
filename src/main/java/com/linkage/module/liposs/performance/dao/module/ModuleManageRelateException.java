package com.linkage.module.liposs.performance.dao.module;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.module.liposs.system.moduleinterface.ModuleManager;

/**
 * 异常处理
 * @author Administrator
 *
 */
public class ModuleManageRelateException implements I_ModuleManageRelateTab {
	private static Logger log = LoggerFactory.getLogger(ModuleManageRelateException.class);
	public List<Map> getRelateTabAllData(ModuleManager mm, JdbcTemplate jt) {
		log.error("【北京酒店网管 模板管理】获取关联表所有数据失败，原因："+mm.getName()+"没有定义实体类！");
		throw new NullPointerException(mm.getName()+"没有定义实体类！");
	}
	public List<Map> getConfigRelateTabData(ModuleManager mm, JdbcTemplate jt) {
		log.error("【北京酒店网管 模板管理】获取已配置关联表所有数据失败，原因："+mm.getName()+"没有定义实体类！");
		throw new NullPointerException(mm.getName()+"没有定义实体类！");
	}
	public List<Map> getUnConfigRelateTabData(ModuleManager mm, JdbcTemplate jt) {
		log.error("【北京酒店网管 模板管理】获取未配置关联表所有数据失败，原因："+mm.getName()+"没有定义实体类！");
		throw new NullPointerException(mm.getName()+"没有定义实体类！");
	}

}
