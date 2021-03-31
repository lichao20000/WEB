/*
 * @(#)User.java	1.00 1/17/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.system;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户接口,定义用户的基本操作
 *
 * @author yuht
 * @version 1.00, 1/17/2006
 * @since Liposs 2.1
 */
public interface User extends Serializable{
	/**
	 * 设置用户账号
	 *
	 * @param account
	 *            账号字符串
	 */
	public void setAccount(String account);

	/**
	 * 设置用户密码
	 *
	 * @param passwd
	 *            密码字符串
	 */
	public void setPasswd(String passwd);

	/**
	 * 设置用户创建者账号ID
	 *
	 * @param creator
	 *            创建者账号ID
	 */
	public void setCreator(long creator);
	
	/**
	 * 设置子用户密码最小长度
	 * @param length
	 */
	public void setMinPwdLength(String length);
	

	/**
	 * 设置用户创建时间
	 *
	 * @param creationDate
	 *            用户建立时间，日期型
	 */
	public void setCreationDate(Date creationDate);

	/**
	 * 设置用户最后一次登录时间
	 *
	 * @param acc_last_login
	 *            登录时间，日期型
	 */
	public void setLastLoginDate(Date acc_last_login);

	/**
	 * 设置用户自定义主页
	 *
	 * @param init_page
	 *            JSP URL字符串
	 */
	public void setInitPage(String init_page);
    /**
     * 设置默认Topo层
     * 对应到tab_accounts表中parentid字段
     */
    public int setParentID(String parentid);
    /**
     * 得到用户帐号表中parentid值
     * @param parentid
     */
    public String getParentID();

	/**
	 * 获得用户属地编号
	 *
	 * @return String
	 */
	public String getCityId();

        public String getCustomId();

	/**
	 * 获取用户的ID号
	 *
	 * @return 返回用户ID，整形值
	 */
	public long getId();

	/**
	 * 获取用户账号
	 *
	 * @return 返回用户账号字符串
	 */
	public String getAccount();

	/**
	 * 获取用户密码
	 *
	 * @return 返回用户密码字符串
	 */
	public String getPasswd();

	/**
	 * 获取用户的创建者
	 *
	 * @return 用户的创建者账号ID
	 */
	public long getCreator();
	
	/**
	 * 获取创建用户的密码最小长度
	 * @return
	 */
	public String getMinPwdLength();	

	/**
	 * 获取用户创建时间
	 *
	 * @return 用户创建时间，Java.uitl.Date类型
	 */
	public Date getCreationDate();

	/**
	 * 获取用户最后一次登录系统时间
	 *
	 * @return 登录系统时间，Java.uitl.Date类型
	 */
	public Date getLastLoginDate();

	/**
	 * 获取用户自定义主页URL
	 *
	 * @return JSP URL字符串
	 */
	public String getInitPage();

	/**
	 * 获取用户管理域ID列表
	 *
	 * @return 返回整形数组，数组存放域ID
	 */
	public long getAreaId();
	
	/**
	 * 获取ior所有信息
	 *
	 * @return 返回字符串
	 */
	public String getIor(String object_name,String object_poaname);

	/**
	 * 获取用户所属角色ID
	 *
	 * @return 返回整形角色ID
	 */
	public long getRoleId();
	/**
	 * 获取用户所属角色ID数组
	 *
	 * @return 返回整形角色ID数组
	 */
	public long[] getRole_Id();


	/**
	 * 设置用户所能管理域列表
	 *
	 * @param arr_area
	 *            存放域ID数组
	 */
	public void setAreaId(long area_id);

	/**
	 * 设置用户所属角色
	 *
	 * @param role_id
	 *            存放角色ID
	 */
	public void setRole(long role_id);

	/**
	 * 获取用户资料信息
	 *
	 * @return 返回用户资料信息，存放到Map中
	 */
	public Map getUserInfo();

	/**
	 * 设置用户资料信息
	 *
	 * @param map
	 *            用户资料信息
	 */
	public void setUserInfo(Map map);
	
	/**
	 * 获取用户角色信息
	 * 
	 * @param list
	 *            用户角色信息
	 * add wangfeng 2006-10-22
	 */
	public List getRole_list();
    /**
     * 判断用户是否是管理员
     * @return true/false
     */
    public boolean isAdmin();
}
