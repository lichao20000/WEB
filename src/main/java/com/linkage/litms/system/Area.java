/*
 * 
 * 创建日期 2006-1-24
 * Administrator suzr
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.system;

import java.util.ArrayList;
import java.util.Map;

public interface Area {
    /**
     * 获得区域ID
     * 
     * @return int
     */
    public int getAreaId();

    /**
     * 获得区域名称
     * 
     * @return String
     */
    public String getAreaName();

    /**
     * 获取区域父ID
     * 
     * @return int
     */
    public int getAreaPid();

    /**
     * 获取区域根ID
     * 
     * @return int
     */
    public int getAreaRootId();

    /**
     * 获取区域层次
     * 
     * @return int
     */
    public int getAreaLayer();

    /**
     * 获取操作员ID
     * 
     * @return int
     */
    public int getAccOid();

    /**
     * 获取注释
     * 
     * @return String
     */
    public String getRemark();

    /**
     * 设定区域ID
     * 
     * @param m_AreaId
     */
    public void setAreaId(int m_AreaId);

    /**
     * 设置区域名称
     * 
     * @param m_AreaName
     */
    public void setAreaName(String m_AreaName);

    /**
     * 设置区域父ID
     * 
     * @param m_Area_Pid
     */
    public void setAreaPid(int m_Area_Pid);

    /**
     * 设置区域层次
     * 
     * @param m_AreaLayer
     */
    public void setAreaLayer(int m_AreaLayer);

    /**
     * 设置操作员ID
     * 
     * @param m_Acc_Oid
     */
    public void setAccOid(int m_Acc_Oid);

    /**
     * 设置区域注释
     * 
     * @param m_remark
     */
    public void setRemark(String m_remark);

    /**
     * 设置区域信息
     * 
     * @param m_AreaInfo
     */
    public void setAreaInfo(Map m_AreaInfo);

    /**
     * 获取区域信息
     * 
     * @return
     */
    public Map getAreaInfo();

    /**
     * 设置区域管理设备范围
     * 
     * @param m_AreaDevices
     */
    public void setAreaDevices(ArrayList m_AreaDevices);

    /**
     * 获取区域管理设备范围
     * 
     * @return ArrayList
     */
    public ArrayList getAreaDevices();
}
