
package com.linkage.module.gwms.dao;

import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


/**
 * 实现动态数据源
 * 
 * @author zhangsm (AILK No.)
 * @version 1.0
 * @since 2013-12-20
 * @category com.linkage.module.gwms.dao
 * @copyright AILK NBS-Network Mgt. RD Dept.
 */
public class DynamicDataSource extends AbstractRoutingDataSource
{

	@Override
	protected Object determineCurrentLookupKey()
	{
		return DataSourceContextHolder.getDBType();
	}
	public Logger getParentLogger() throws SQLFeatureNotSupportedException
	{
		throw new SQLFeatureNotSupportedException("this method is not impelement,if you need use it ,please impelement it first!");
	}
}
