package com.linkage.module.gtms.itv.dto;

/**
 * 
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-07-30
 * @category 菜单pojobean，用来封装菜单的相关信息
 * 
 * 
 */
public class MenuItem
{
	private String name;// 菜单名称
	private String name_en;//英文菜单项
	private String id;// 菜单id
	private int layer;// 菜单层次,用来显示菜单的缩进控制/背景颜色（通过统一的样式控制）
	private String url;// 菜单的url地址
	private String pid;// 菜单的父id
	private boolean isleaf;// 是否是叶子
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public boolean isIsleaf()
	{
		return isleaf;
	}
	public void setIsleaf(boolean isleaf)
	{
		this.isleaf = isleaf;
	}
	public int getLayer()
	{
		return layer;
	}
	public void setLayer(int layer)
	{
		this.layer = layer;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getPid()
	{
		return pid;
	}
	public void setPid(String pid)
	{
		this.pid = pid;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getName_en()
	{
		return name_en;
	}
	public void setName_en(String name_en)
	{
		this.name_en = name_en;
	}
}
