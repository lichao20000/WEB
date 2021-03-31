package com.linkage.liposs.buss.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 安全网关的item POJO类，负责存储菜单的信息
 * 
 * @author 王志猛(5194) tel：13701409234
 * @version 1.0
 * @since 2008-3-31
 * @category com.linkage.liposs.buss.menu 版权：南京联创科技 网管科技部
 * 
 */
public class SercurityItem
{
	private String area_id;// 域id
	private String area_name;// 域名字
	private String area_pid;// 域的父id
	private boolean hasSGW;// 是否有安全网关设备
	private List<Map> SGWList;// 域的机房列表，其中机房的数目为当前域的所有机房减去当前域的子域说包含的子域
	private boolean hasChild;// 是否有子域
	private List<SercurityItem> ChildArea;// 该域的子域
	private SercurityItem ParentArea;// 该域的父域
	public String getArea_id()
	{
		return area_id;
	}
	public void setArea_id(String area_id)
	{
		this.area_id = area_id;
	}
	public String getArea_name()
	{
		return area_name;
	}
	public void setArea_name(String area_name)
	{
		this.area_name = area_name;
	}
	public String getArea_pid()
	{
		return area_pid;
	}
	public void setArea_pid(String area_pid)
	{
		this.area_pid = area_pid;
	}
	public List<Map> getSGWList()
	{
		SGWList = SGWList == null ? new ArrayList<Map>() : SGWList;
		return SGWList;
	}
	public void setSGWList(List<Map> sgwList)
	{
		this.SGWList = sgwList;
	}
	public boolean isHasChild()
	{
		hasChild = ChildArea == null ? false : (ChildArea.size() != 0);
		return hasChild;
	}
	public List<SercurityItem> getChildArea()
	{
		ChildArea = ChildArea == null ? new ArrayList<SercurityItem>() : ChildArea;
		return ChildArea;
	}
	public void setChildArea(List<SercurityItem> childArea)
	{
		ChildArea = childArea;
	}
	public boolean isHasSGW()
	{
		hasSGW = SGWList == null ? false : SGWList.size() != 0;
		return hasSGW;
	}
	public SercurityItem getParentArea()
	{
		return ParentArea;
	}
	public void setParentArea(SercurityItem parentArea)
	{
		ParentArea = parentArea;
	}
	public boolean equals(Object obj)
	{
		if (obj == null)
			{
				return false;
			}
		else
			{
				return this.area_id.equals(((SercurityItem) obj).area_id);
			}
	}
	public int hashCode(){
		return area_id.hashCode()+60;
	}
	public String toString()
	{
		return "域id/" + getArea_id() + " 域名/" + getArea_name() + " 域的父id/"
				+ getArea_pid() + " 子域长度/" + getChildArea().size() + " 安全设备长度/"
				+ getSGWList().size();
	}
}
