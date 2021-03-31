package com.linkage.module.itms.resource.obj;

import java.util.ArrayList;

public class TreeNode
{
	private String name = "";
	private String path = "";
	private String nocheck = "true";
	private String checked = "false";
	private String open = "true";
	//type: type of node. 1,string; 2,int; 3,unsignedInt; 4,boolean .
	private String type = "";
	private String value = "";
	private String priority = "10"; 
	//readonly:is readonly. 0,none; 1,read; 2,read and write.
	private String readonly = "";
	private ArrayList<TreeNode> children = new ArrayList<TreeNode>();
	
	
	
	public TreeNode()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public TreeNode(String name, String nocheck, String open, ArrayList<TreeNode> children)
	{
		super();
		this.name = name;
		this.nocheck = nocheck;
		this.open = open;
		this.children = children;
	}

	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getNocheck()
	{
		return nocheck;
	}
	
	public void setNocheck(String nocheck)
	{
		this.nocheck = nocheck;
	}
	
	public String getOpen()
	{
		return open;
	}
	
	public void setOpen(String open)
	{
		this.open = open;
	}
	
	
	public ArrayList<TreeNode> getChildren()
	{
		return children;
	}

	public String getType()
	{
		return type;
	}

	
	public void setType(String type)
	{
		this.type = type;
	}

	
	public String getValue()
	{
		return value;
	}

	
	public void setValue(String value)
	{
		this.value = value;
	}

	
	public String getPriority()
	{
		return priority;
	}

	
	public void setPriority(String priority)
	{
		this.priority = priority;
	}

	
	public void setChildren(ArrayList<TreeNode> children)
	{
		this.children = children;
	}

	
	public String getPath()
	{
		return path;
	}

	
	public void setPath(String path)
	{
		this.path = path;
	}

	
	public String getChecked()
	{
		return checked;
	}

	
	public void setChecked(String checked)
	{
		this.checked = checked;
	}

	
	public String getReadonly()
	{
		return readonly;
	}

	
	public void setReadonly(String readonly)
	{
		this.readonly = readonly;
	}
	
	
}
