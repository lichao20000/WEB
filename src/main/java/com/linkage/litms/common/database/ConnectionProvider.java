/*
 * @(#)ConnectionProvider.java	1.00 1/5/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.common.database;

/**
 * 这个抽象类定义了提供连接的框架. 其它的类扩充这个抽象类建立数据源的实际连接.
 * 
 * @author yuht
 * @version 1.00, 1/5/2006
 * @see DefaultConnectionProvider
 * @since Liposs 2.1
 */
import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider{

	/**
	 * 假如这个连接驱动能够向外部连接池提供连接则返回 true. 
	 *
	 * @return true 假如这个连接对象返回给这个提供者是 pooled.
	 */
	public boolean isPooled();

	/**
	 * 返回一个数据库连接. 
	 *
	 * @return 返回一个 Connection 对象.
	 */
	public Connection getConnection() throws SQLException;

	/**
	 * 启动这个连接驱动.
	 */
	public void start();

	/**
	 * 使配置文件的改变立即生效.
	 */
	public void restart();

	/**
	 * 让连接驱动销毁.
	 */
	public void destroy();
}