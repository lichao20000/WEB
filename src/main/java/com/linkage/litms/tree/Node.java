package com.linkage.litms.tree;

public class Node {
	/**
	 * 父节点id
	 */
	private String node_parent_id = null;
	/**
	 * 节点id
	 */
	private String node_self_id = null;
	/**
	 * 节点说明
	 */
	private String node_text = null;
	/**
	 * 节点链接
	 */
	private String node_url = null;
	/**
	 * 节点链接参数
	 */
	private String node_data = null;
	/**
	 * 节点图标
	 */
	private String node_icon  = null;
	
	
	public Node(){

	}


	/**
	 * @return the node_data
	 */
	public String getNode_data() {
		return node_data;
	}


	/**
	 * @param node_data the node_data to set
	 */
	public void setNode_data(String node_data) {
		this.node_data = node_data;
	}


	/**
	 * @return the node_icon
	 */
	public String getNode_icon() {
		return node_icon;
	}


	/**
	 * @param node_icon the node_icon to set
	 */
	public void setNode_icon(String node_icon) {
		this.node_icon = node_icon;
	}


	/**
	 * @return the node_parent_id
	 */
	public String getNode_parent_id() {
		return node_parent_id;
	}


	/**
	 * @param node_parent_id the node_parent_id to set
	 */
	public void setNode_parent_id(String node_parent_id) {
		this.node_parent_id = node_parent_id;
	}


	/**
	 * @return the node_self_id
	 */
	public String getNode_self_id() {
		return node_self_id;
	}


	/**
	 * @param node_self_id the node_self_id to set
	 */
	public void setNode_self_id(String node_self_id) {
		this.node_self_id = node_self_id;
	}


	/**
	 * @return the node_text
	 */
	public String getNode_text() {
		return node_text;
	}


	/**
	 * @param node_text the node_text to set
	 */
	public void setNode_text(String node_text) {
		this.node_text = node_text;
	}


	/**
	 * @return the node_url
	 */
	public String getNode_url() {
		return node_url;
	}


	/**
	 * @param node_url the node_url to set
	 */
	public void setNode_url(String node_url) {
		this.node_url = node_url;
	}
	
	
}
