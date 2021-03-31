/*
 * @(#)SkinUtils.java	1.00 1/21/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.system.dbimpl.DbUserRes;
import com.linkage.module.gwms.Global;

/**
 * LIPOSS 设定系统Skin的方法集合，为了系统外观，实用开发提供基础。
 * <ol>
 * <li> 设置和获取session方法
 * <ul>
 * <li>{@link #removeSession(HttpServletRequest, String)}
 * <li>{@link #getSession(HttpServletRequest, String)}
 * <li>{@link #setSession(HttpServletRequest, String, String)}
 * </ul>
 * <p>
 * <li> 设置和获取cookie方法
 * <ul>
 * <li>{@link #removeCookie(HttpServletResponse, String)}
 * <li>{@link #setCookie(HttpServletResponse, String, String)}
 * <li>{@link #setCookie(HttpServletResponse, String, String, int)}
 * <li>{@link #getCookie(HttpServletRequest, String)}
 * </ul>
 * </ol>
 * 
 * @author yuht
 * @version 1.00, 1/21/2006
 * @since Liposs 2.1
 */
public class SkinUtils {
	/**
	 * 删除session
	 * 
	 * @param req HttpServletRequest对象，JSP页面的request对象
	 * @param name session变量名称
	 */
	private static Logger logger = LoggerFactory.getLogger(SkinUtils.class);
	
	public static void removeSession(HttpServletRequest req, String name){
		HttpSession session = req.getSession();
		session.removeAttribute(name);
	}
	/**
	 * 获取session
	 * 
	 * @param req HttpServletRequest对象，JSP页面的request对象
	 * @param name session变量名称
	 * @return 返回Object类型，session存放对象
	 */
	public static Object getSession(HttpServletRequest req, String name){
		HttpSession session = req.getSession();
		return session.getAttribute(name);
	}
	/**
	 * 设置session
	 * 
	 * @param req HttpServletRequest对象，JSP页面的request对象
	 * @param name session变量名称
	 * @param value Object类型
	 */
	public static void setSession(HttpServletRequest req, String name, Object value){
		HttpSession session = req.getSession();

		// chenxj6 特殊用户控制 session 有效时间begin
		if (Global.JLDX.equals(Global.instAreaShortName)) {
			if(value instanceof UserRes){
				UserRes userRes = (UserRes)value;
				if ("curUser".equals(name) && "10000test".equals(userRes.getUser().getAccount())) {
					logger.warn("此用户session有效期为：12小时");
					session.setMaxInactiveInterval(12 * 60 * 60);
					session.setAttribute(name,value);
					return;
				} 
			}else{
				UserRes userRes = (UserRes)session.getAttribute("curUser");
				if(userRes!=null && "10000test".equals(userRes.getUser().getAccount())){
					logger.warn("此用户session有效期为：12小时");
					session.setMaxInactiveInterval(12 * 60 * 60);
					session.setAttribute(name,value);
					return;
				}
			}
			
//			session.setMaxInactiveInterval(1 * 60);
//			logger.warn("此用户session有效期为：60秒");
			
		}
		// chenxj6 特殊用户控制 session 有效时间end
		
		session.setAttribute(name,value);
	}
	/**
	 * 删除Cookie
	 * 
	 * @param rep HttpServletResponse对象，JSP页面的response对象
	 * @param name Cookie名称
	 */
	public static void removeCookie(HttpServletResponse rep, String name){
		Cookie cookie = new Cookie(name, "");

        cookie.setMaxAge(0);
        cookie.setPath("/");
        rep.addCookie(cookie);
	}
	/**
	 * 设置Cooike，默认保存时间为一个月
	 * 
	 * @param rep HttpServletResponse对象，JSP页面的response对象
	 * @param name Cookie名称
	 * @param value Cookie值
	 */
	public static void setCookie(HttpServletResponse rep, String name, String value){
		setCookie(rep,name,value,24*60*60*30);
	}
	/**
	 * 设置Cooike
	 * 
	 * @param rep HttpServletResponse对象，JSP页面的response对象
	 * @param name Cookie名称
	 * @param value Cookie值
	 * @param saveTime 保存时间长度
	 */
	public static void setCookie(HttpServletResponse rep, String name, String value, int saveTime){
        if (value == null) {
            value = "";
        }
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(saveTime);
        cookie.setPath("/");
        rep.addCookie(cookie);	
	}
	/**
	 * 获取Cookie
	 * 
	 * @param req HttpServletRequest对象，JSP页面的request对象
	 * @param name Cookie名称
	 * @return 返回Cookie对象
	 */
	public static Cookie getCookie(HttpServletRequest req, String name){
        Cookie cookies[] = req.getCookies();

        if(cookies == null || name == null || name.length() == 0) {
            return null;
        }

        for (int i = 0; i < cookies.length; i++) {
            if(cookies[i].getName().equals(name) ) {
                return cookies[i];
            }
        }
        return null;
	}
	
	/**
	 * 设置会话超时时间
	 * @param req
	 * @param time 单位为秒
	 */
	public static void setSessionTimeOut(HttpServletRequest req,int time)
	{
		HttpSession session = req.getSession();
		if(time<getSessionTimeOut(req))
		{
			session.setMaxInactiveInterval(time);
		}		
	}
	
	/**
	 * 返回会话超时时间
	 * @param req
	 * @return  返回会话超时时间，单位为秒
	 */
	public static int getSessionTimeOut(HttpServletRequest req)
	{
		int overTime = 0;
		HttpSession session = req.getSession();
		overTime = session.getMaxInactiveInterval();
		return overTime;
	}
}
