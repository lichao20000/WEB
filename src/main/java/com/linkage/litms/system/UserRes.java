/*
 * @(#)UserRes.java	1.00 1/21/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.system;

import java.io.Serializable;
import java.util.List;

import com.linkage.litms.system.dbimpl.DbUserRes;

/**
 * 保存登录用户各种信息的接口，如用户账号、用户资料、用户访问资源范围、用户访问功能模块权限等
 *
 * @author yuht
 * @version 1.00, 1/21/2006
 * @see DbUserRes
 * @since Liposs 2.1
 */
public interface UserRes extends Serializable{
    /**
     * 获取登录用户对象
     *
     * @return 用户对象
     */
    public User getUser();

    /**
     * 获取用户访问设备资源范围
     *
     * @return 设备资源范围
     */
    public List getUserDevRes();
    
    /**
     * 按照用户对象获取设备权限
     * @param user
     * @return List
     */
    public List getUserDevRes(User user);

    /**
     * 获取用户功能模块访问权限
     *
     * @return 功能模块访问权限
     */
    public List getUserPermissions();

    /**
     * 获取用户管理采集机列表
     *
     * @return 采集机列表
     */
    public List getUserProcesses();


    public List getUserVpnRes();

    /**
     * 获取用户属地
     *
     * @return 返回属地ID字符串
     */
    public String getCityId();



    /**
     * 获取用户所属域ID
     *
     * @return 返回长整形域ID
     */
    public long getAreaId();
}
