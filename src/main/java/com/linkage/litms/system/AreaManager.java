/*
 * 
 * 创建日期 2006-1-24
 * Administrator suzr
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.system;

import java.util.ArrayList;

public interface AreaManager {
	/**
	 * 创建区域对象
	 * 
	 * @param m_AreaId
	 * @param m_AreaName
	 * @param m_Area_Pid
	 * @param m_Area_RootId
	 * @param m_AreaLayer
	 * @param m_Acc_Oid
	 * @param m_Remark
	 * @return Area
	 */
	public Area createArea(int m_AreaId, String m_AreaName, int m_Area_Pid,
			int m_Area_RootId, int m_AreaLayer, int m_Acc_Oid, String m_Remark);

	/**
	 * 创建区域对象
	 * 
	 * @param m_AreaId
	 * @param m_AreaName
	 * @param m_Area_Pid
	 * @param m_Area_RootId
	 * @param m_AreaLayer
	 * @param m_Acc_Oid
	 * @param m_Remark
	 * @param m_AreaDevices
	 * @return Area
	 */
	public Area createArea(int m_AreaId, String m_AreaName, int m_Area_Pid,
			int m_Area_RootId, int m_AreaLayer, int m_Acc_Oid, String m_Remark,
			ArrayList m_AreaDevices);

	/**
	 * 根据区域对象删除区域对象
	 * 
	 * @param m_Area
	 * @return boolean
	 */
	public boolean delArea(Area m_Area);

	/**
	 * 根据区域ID删除区域对象
	 * 
	 * @param m_AreaId
	 * @return boolean
	 */
	public boolean delArea(int m_AreaId);

	/**
	 * 根据区域ID获得区域对象
	 * 
	 * @param m_AreaId
	 * @return Area
	 */
	public Area getArea(int m_AreaId);

	/**
	 * 根据区域名获得区域对象
	 * 
	 * @param m_AreaName
	 * @return Area
	 */
	public Area getArea(String m_AreaName);

	/**
	 * 刷新区域对象
	 * 
	 * @param m_AreaId
	 * @param m_AreaName
	 * @param m_Area_Pid
	 * @param m_Area_RootId
	 * @param m_AreaLayer
	 * @param m_Acc_Oid
	 * @param m_Remark
	 * @param m_AreaDevices
	 * @return boolean
	 */
	public boolean refAreaInfo(int m_AreaId, String m_AreaName, int m_Area_Pid,
			int m_Area_RootId, int m_AreaLayer, int m_Acc_Oid, String m_Remark,
			ArrayList m_AreaDevices);

	/**
	 * 刷新区域对象
	 * 
	 * @param m_AreaId
	 * @param m_AreaName
	 * @param m_Area_Pid
	 * @param m_Area_RootId
	 * @param m_AreaLayer
	 * @param m_Acc_Oid
	 * @param m_Remark
	 * @return boolean
	 */
	public boolean refAreaInfo(int m_AreaId, String m_AreaName, int m_Area_Pid,
			int m_Area_RootId, int m_AreaLayer, int m_Acc_Oid, String m_Remark);

	/**
	 * 刷新区域对象
	 * 
	 * @param m_Area
	 * @return boolean
	 */
	public boolean refAreaInfo(Area m_Area);

	/**
	 * 获得区域上层所有区域ID
	 * 
	 * @param m_AreaId
	 * @return ArrayList
	 */
	public ArrayList getUpperToTopAreaIds(int m_AreaId);

	/**
	 * 获得区域直接上层区域编号
	 * 
	 * @param m_AreaId
	 * @return int
	 */
	public int getUpperAreaIds(int m_AreaId);

	/**
	 * 获得区域下层所有区域ID
	 * 
	 * @param m_AreaId
	 * @return ArrayList
	 */
	public ArrayList getLowerToFloorAreaIds(int m_AreaId);

	/**
	 * 获得区域直接下层区域ID
	 * 
	 * @param m_AreaId
	 * @return
	 */
	public ArrayList getLowerAreaIds(int m_AreaId);
}
