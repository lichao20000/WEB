/*
 * @(#)Authorization.java	1.00 1/21/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.system;


/**
 * 系统注册用户登录认证授权接口
 * 
 * @author yuht
 * @version 1.00, 1/21/2006
 * @since Liposs 2.1
 */
public interface Authorization {
	/**
	 * 用户登录认证
	 * 
	 * @return 登录成功返回true，反之false
	 */
	public boolean login();
	/**
	 * 用户退出处理
	 *
	 */
	public void logout();
}
