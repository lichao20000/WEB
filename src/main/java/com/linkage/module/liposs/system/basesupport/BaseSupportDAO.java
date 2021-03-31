package com.linkage.module.liposs.system.basesupport;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.litms.common.util.JdbcTemplateExtend;

/**
 * DAO的基类，所有的DAO都应该继承该类
 * 
 * @author 王志猛(工号) tel：13701409234
 * @version 1.0
 * @since 2008-8-14
 * @category com.linkage.module.bcms.system 版权：南京联创科技 网管科技部
 * 
 */
public class BaseSupportDAO
{
	protected DataSource dao = null;
	protected JdbcTemplate jt = null;
	protected JdbcTemplateExtend jte = null;

	/**
	 * 设置数据源
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao)
	{
		this.dao = dao;
		jt = new JdbcTemplate(dao);
		jte = new JdbcTemplateExtend(dao);
	}
}
