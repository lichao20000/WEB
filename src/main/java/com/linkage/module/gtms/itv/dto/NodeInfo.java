
package com.linkage.module.gtms.itv.dto;

/**
 * 菜单节点数据
 * 
 * @author 陈仲民(5243)
 * @version 1.0
 * @since 2009-12-23
 * @category com.linkage.module.fba.menu.dto<br>
 *           版权：南京联创科技 网管科技部
 */
public class NodeInfo
{
	
	/**
	 * 节点编号
	 */
	private String nodeId;
	
	/**
	 * 父节点
	 */
	private String nodeParentId;
	
	/**
	 * 节点名称
	 */
	private String nodeName;
	
	/**
	 * 节点参数
	 */
	private String urlParam;
	
	/**
	 * 层次
	 */
	private String layer = "0";
	
	/**
	 * 是否叶子节点 true：是 false：否
	 */
	private String isLeaf;
	
	/**
	 * 树型菜单数据编号
	 */
	private String treeId;
	
	public String getNodeId()
	{
		return nodeId;
	}
	
	public void setNodeId(String nodeId)
	{
		this.nodeId = nodeId;
	}
	
	public String getNodeName()
	{
		return nodeName;
	}
	
	public void setNodeName(String nodeName)
	{
		this.nodeName = nodeName;
	}
	
	public String getUrlParam()
	{
		return urlParam;
	}
	
	public void setUrlParam(String urlParam)
	{
		this.urlParam = urlParam;
	}
	
	public String getIsLeaf()
	{
		return isLeaf;
	}
	
	public void setIsLeaf(String isLeaf)
	{
		this.isLeaf = isLeaf;
	}
	
	public String getNodeParentId()
	{
		return nodeParentId;
	}
	
	public void setNodeParentId(String nodeParentId)
	{
		this.nodeParentId = nodeParentId;
	}
	
	public String getLayer()
	{
		return layer;
	}
	
	public void setLayer(String layer)
	{
		this.layer = layer;
	}

	
	public String getTreeId()
	{
		return nodeParentId + "_" + nodeId;
	}

	
	public void setTreeId(String treeId)
	{
		this.treeId = treeId;
	}
	
}
