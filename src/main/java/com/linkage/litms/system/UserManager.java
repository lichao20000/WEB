/*
 * @(#)UserManager.java	1.00 1/18/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.system;

import java.util.Map;

import com.linkage.litms.common.database.Cursor;

/**
 * 系统集中管理用户接口，包括建立用户，删除用户，查找用户等操作
 * 
 * @author yuht
 * @version 1.00, 1/18/2006
 * @see User
 * @since Liposs 2.1
 */
public interface UserManager {
	/**
	 * 根据参数创建用户
	 * 
	 * @param account
	 *            用户账号
	 * @param passwd
	 *            用户密码
	 * @param creator
	 *            用户创建者
	 * @param role_id
	 *            用户所属角色ID
	 * @param area_id
	 *            用户管理域列表
	 * @return 返回用户对象
	 * @throws UserAlreadyExistsException
	 *             假如用户已经存在系统中
	 */
	public User createUser(String account, String passwd, long creator,
			long role_id, long area_id) throws UserAlreadyExistsException;

	/**
	 * 根据参数创建用户和用户资料
	 * 
	 * @param account
	 *            用户账号
	 * @param passwd
	 *            用户密码
	 * @param creator
	 *            用户创建者
	 * @param _map
	 *            用户资料Map
	 * @param role_id
	 *            用户所属角色ID
	 * @param area_id
	 *            用户管理域ID
	 * @param _map
	 *            用户资料信息
	 * @return 返回用户对象
	 * @throws UserAlreadyExistsException
	 *             假如用户已经存在系统中
	 * 
	 */
	public User createUser(String account, String passwd, long creator,
			long role_id, long area_id, Map _map)
			throws UserAlreadyExistsException;

	/**
	 * 根据用户账号ID获取用户对象
	 * 
	 * @param acc_oid
	 *            户账号ID
	 * @return 用户对象
	 * @throws UserNotFoundException
	 *             假如用户不存在
	 */
	public User getUser(long acc_oid) throws UserNotFoundException;

	/**
	 * 根据用户账号和用户区域获取用户对象
	 * 
	 * @param account
	 *            用户账号
	 * @param area_id
	 *            用户区域
	 * @return 用户对象
	 * @throws UserNotFoundException
	 *             假如用户不存在
	 */
	public User getUser(String account, long area_id)
			throws UserNotFoundException;

	/**
	 * 根据用户账号和用户区域获取用户对象(江苏电信 itms与智能网管单点登录用)
	 * 
	 * @param account
	 *            用户账号
	 * @param area_id
	 *            用户区域
	 * @param cas
	 *            是否单点登录
	 * @return 用户对象
	 * @throws UserNotFoundException
	 *             假如用户不存在
	 */
	public User getUser(String account, long area_id, boolean cas)
			throws UserNotFoundException;

	/**
	 * 根据用户对象删除用户
	 * 
	 * @param user
	 *            用户对象
	 */
	public void deleteUser(User user);

	/**
	 * 根据用户账号ID号删除用户
	 * 
	 * @param acc_oid
	 *            用户账号ID
	 */
	public void deleteUser(long acc_oid);

	/**
	 * 获取用户列表，游标开始位置是<code>startIndex</code>,取记录长度为<code>numResults</code>
	 * 
	 * @param startIndex
	 *            游标开始位置
	 * @param numResults
	 *            记录长度
	 * @return
	 */
	public Cursor users(int startIndex, int numResults);

	/**
	 * 根据 查询条件获取用户列表
	 * 
	 * @param _map
	 *            查询条件
	 * @return
	 */
	public Cursor users(Map _map);

}
