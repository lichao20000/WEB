/**
 * @(#)UserAlreadyExistsException.java  1.00 1/20/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.module.liposs.system.exception;

/**
 * 试图创建一个已经存在系统的新用户时触发的异常类
 * 
 * @author yuht
 * @version 1.00, 1/20/2006
 * @since Liposs 2.1
 */
public class RoleAlreadyExistsException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * 构造详细消息为 null 的新异常。
     */
    public RoleAlreadyExistsException() {
        super();
        // TODO 自动生成构造函数存根
    }

    /**
     * 构造带指定详细消息的新异常。
     * 
     * @param message
     *            详细消息。
     */
    public RoleAlreadyExistsException(String message) {
        super(message);
        // TODO 自动生成构造函数存根
    }

    /**
     * 根据指定的原因和 (cause==null ? null : cause.toString()) 的详细消息构造新异常 （它通常包含 cause
     * 的类和详细消息）。对于那些与其他可抛出异常 （例如，PrivilegedActionException）的包装器相同的异常，此构造方法很有用。
     * 
     * @param cause
     *            原因（允许使用 null 值，指出原因不存在或者是未知的。）
     */
    public RoleAlreadyExistsException(Throwable cause) {
        super(cause);
        // TODO 自动生成构造函数存根
    }

    /**
     * 构造带指定详细消息和原因的新异常。
     * <p>
     * 注意，与 cause 相关的详细消息不是 自动合并到这个异常的详细消息中的。
     * 
     * @param message
     *            详细消息
     * @param cause
     *            cause 原因（允许使用 null 值，指出原因不存在或者是未知的。）
     */
    public RoleAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
        // TODO 自动生成构造函数存根
    }

}
