package com.linkage.module.liposs.system.exception;

/**
 * 用户资源没有发现的异常
 *
 * @author 王志猛(工号) tel：5194
 * @version 1.0
 * @since 2008-8-14
 * @category com.linkage.module.bcms.system.exception 版权：南京联创科技 网管科技部
 *
 */
public class UserResNotFoundException extends Exception
{
	/**
	 *
	 */
	private static final long serialVersionUID = -3009889112468988804L;
	public UserResNotFoundException()
	{
		super();
	}
	public UserResNotFoundException(String msg)
	{
		super(msg);
	}
	public UserResNotFoundException(Throwable twa)
	{
		super(twa);
	}
	public UserResNotFoundException(Throwable twa, String msg)
	{
		super(msg, twa);
	}
}
