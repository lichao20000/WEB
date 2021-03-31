/**
 * @(#)UserNotFoundException.java	1.00 1/20/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.module.liposs.system.exception;
/**
 * 当用户信息不能载入或不存在时触发的异常类
 * 
 * @author yuht
 * @version 1.00, 1/20/2006
 * @since Liposs 2.1
 */
public class UserNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 缺省构造函数
	 */
	public UserNotFoundException() {
        super();
    }
	
	/**
	 * 带错误信息参数的构造函数
	 * 
	 * @param msg 错误信息
	 */
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
