/**
 * 
 */
package com.linkage.module.bbms.diag.dao;

import javax.sql.DataSource;

import com.linkage.module.gwms.dao.SuperDAO;

import dao.util.JdbcTemplateExtend;

/**
 * @author chenjie
 * @date   2011-8-5
 */
public class OneKeyDiagDAO extends SuperDAO{
	private JdbcTemplateExtend jt;
	
	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}
}
