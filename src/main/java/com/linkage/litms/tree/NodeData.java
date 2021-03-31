package com.linkage.litms.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

public class NodeData {
	private static Logger m_logger = LoggerFactory.getLogger(NodeData.class);
	
	private PrepareSQL pSQL = null;
	/**分WEB时，报表子系统*/
	private final static String REPORT_CLUSTE_MODE = "3";
	/**分WEB时，开通子系统*/
	private final static String OPEN_CLUSTE_MODE = "2";
	
	/**
	 * 根据父目录节点、当前角色获取到子目录节点
	 */
	private String TreeRoleSQL = "select * from tab_system_tree where tree_parent_id=? and tree_id in(select tree_id from tab_tree_role where role_id=?) order by sequence";
	/*** 根据父目录节点、当前角色获取到子目录节点---开通子系统*/
	private String TreeRoleSQL4OPEN = "select * from tab_system_tree where tree_parent_id=? and tree_id not in (select item_id from tab_report_item where item_level=2) and tree_id in(select tree_id from tab_tree_role where role_id=?) order by sequence";
	/*** 根据父目录节点、当前角色获取到子目录节点---报表子系统*/
	private String TreeRoleSQL4REPORT = "select * from tab_system_tree where tree_parent_id=? and tree_id in (select item_id from tab_report_item where item_level=2) and tree_id in(select tree_id from tab_tree_role where role_id=?) order by sequence";
	/**
	 * 根据目录节点、角色获取到当前下属节点
	 */
//	private String ItemRoleSQL = "select * from tab_item where item_id in(select item_id from tab_tree_item where tree_id =?) and item_id in(select item_id from tab_item_role where role_id=?) order by item_name";
	private String ItemRoleSQL = "select * from tab_item where item_id in(select item_id from tab_tree_item where tree_id =?) and item_id in(select item_id from " + LipossGlobals.getLipossProperty("Systype") + " where role_id=?) order by item_name";
	/***根据目录节点、角色获取到当前下属节点---开通子系统*/
	private String ItemRoleSQL4OPEN = "select * from tab_item where item_id in(select item_id from tab_tree_item where tree_id =?) and item_id not in (select item_id from tab_report_item where item_level=3) and item_id in(select item_id from " + LipossGlobals.getLipossProperty("Systype") + " where role_id=?) order by item_name";
	/***根据目录节点、角色获取到当前下属节点---报表子系统*/
	private String ItemRoleSQL4RPORT = "select * from tab_item where item_id in (select a.item_id from tab_tree_item a,tab_report_item b where a.item_id=b.item_id and b.item_level=3 and a.tree_id =?) and item_id in (select item_id from  " + LipossGlobals.getLipossProperty("Systype") + " where role_id=?) order by item_name";

	/**
	 * 迭代获取节点
	 */
	private String IterSQL = "select * from tab_system_tree where tree_parent_id=? order by sequence";

	/**
	 * 获取所有系统目录节点
	 */
	private String TREE_NODE_ALL = "select * from tab_system_tree order by sequence";

	/**
	 * 获取指定系统目录下功能点
	 */
	private String ITEM_BY_TREE = "select * from tab_item where item_id in(select item_id from tab_tree_item where tree_id=?)  and item_visual='1' order by sequence";

	/**
	 * 根据用户角色获得用户所有功能节点权限
	 */
//	private String ITEMID_BY_ROLEID = "select c.*,b.tree_id from tab_tree_item b,tab_item c where b.item_id in(select item_id from tab_item_role where role_id=?) and c.item_id = b.item_id";
	private String ITEMID_BY_ROLEID = "select c.*,b.tree_id from tab_tree_item b,tab_item c where b.item_id in(select item_id from " + LipossGlobals.getLipossProperty("Systype") + " where role_id=?) and c.item_id = b.item_id";

	/**
	 * 角色与目录树之间关系
	 */
	private static Map role_nodes_map = null;

	public NodeData() {
		pSQL = new PrepareSQL();

		if (role_nodes_map == null) {
			role_nodes_map = new HashMap();
		}
	}
    /**
     * 
     *角色与目录树之间关系,对菜单改变之后需要清空静态变量
     */
	public synchronized static void clearRole_Nodes_Map(){
        if(role_nodes_map != null){
            role_nodes_map.clear();
            role_nodes_map = null;
        }
    }
	/**
	 * 获取节点信息
	 * 
	 * @param role_id
	 * @param lTreeId
	 * @param lItemId
	 * @return List
	 */
	public List getNodeInfo(String role_id, List lTreeId, List lItemId) {
		List list = new ArrayList();
		Node node = null;

		pSQL.setSQL(ITEMID_BY_ROLEID);
		pSQL.setInt(1, Integer.parseInt(role_id));
		Cursor Lcursor = DataSetBean.getCursor(pSQL.getSQL());
		Map item = Lcursor.getNext();

		while (item != null) {
			if (lItemId.contains(item.get("item_id"))) {
				node = new Node();
				node.setNode_parent_id((String) item.get("tree_id"));
				node.setNode_self_id((String) item.get("item_id"));
				node.setNode_text((String) item.get("item_name"));
				node.setNode_icon((String) item.get("item_icon"));
				node.setNode_url((String) item.get("item_url"));
				node.setNode_data((String) item.get("item_data"));

				list.add(node);

			}

			item = Lcursor.getNext();
		}

		//clear
		Lcursor = null;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			if(LipossGlobals.isGSDX()) {
				TREE_NODE_ALL = "select tree_parent_id, tree_id, tree_name " +
						"from tab_system_tree order by sequence";
			}else {
				TREE_NODE_ALL = "select tree_parent_id, tree_id, tree_name, tree_icon, tree_url, tree_data " +
						"from tab_system_tree order by sequence";
			}
		}

		com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(TREE_NODE_ALL);
		psql.getSQL();
		Lcursor = DataSetBean.getCursor(TREE_NODE_ALL);
		Map tree = Lcursor.getNext();

		while (tree != null) {
			if (lTreeId.contains(tree.get("tree_id"))) {
				node = new Node();
				node.setNode_parent_id((String) tree.get("tree_parent_id"));
				node.setNode_self_id((String) tree.get("tree_id"));
				node.setNode_text((String) tree.get("tree_name"));
				node.setNode_icon((String) tree.get("tree_icon"));
				node.setNode_url((String) tree.get("tree_url"));
				node.setNode_data((String) tree.get("tree_data"));

				list.add(node);
			}

			tree = Lcursor.getNext();
		}

		//clear
		Lcursor = null;
		tree = null;
		
		return list;
	}

	/**
	 * 
	 * @param node_parent_id
	 * @param role_id
	 * @return List
	 */
	public List getNodeList(String node_parent_id, String role_id) {
		if (role_nodes_map == null) {
			m_logger.warn("role_nodes_map is empty .");
			role_nodes_map = new HashMap();
		}

		if (role_nodes_map.get(role_id + ";" + node_parent_id) == null) {
			List list = getChildTree(node_parent_id, role_id);
			role_nodes_map.put(role_id + ";" + node_parent_id, list);

			m_logger.debug("new " + node_parent_id + " " + role_id
					+ " create nodes " + list.size() + ".");

			return list;
		}

		return (List) role_nodes_map.get(role_id + ";" + node_parent_id);
	}
	/**
	 * 
	 * @param node_parent_id
	 * @param role_id
	 * @return List
	 */
	public List getNodeList(String node_parent_id, String role_id,String clusteMode) {
		
		// 报表系统时,重新加载权限
		if (role_nodes_map == null || REPORT_CLUSTE_MODE.equals(clusteMode)) {
			m_logger.warn("role_nodes_map is empty .");
			role_nodes_map = new HashMap();
		}

		if (role_nodes_map.get(role_id + ";" + node_parent_id) == null || REPORT_CLUSTE_MODE.equals(clusteMode)) {
			List list = getChildTree(node_parent_id, role_id,clusteMode);
			
			role_nodes_map.put(role_id + ";" + node_parent_id, list);

			m_logger.debug("new " + node_parent_id + " " + role_id
					+ " create nodes " + list.size() + ".");

			return list;
		}
		
		return (List) role_nodes_map.get(role_id + ";" + node_parent_id);
	}
	/**
	 * 根据父目录节点、角色获取下属目录节点
	 * 
	 * @param tree_parent_id
	 * @param role_id
	 * @return List
	 */
	public List getChildTree(String tree_parent_id, String role_id) {
		List list = new ArrayList();
		Node node = null;

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			if(LipossGlobals.isGSDX()) {
				TreeRoleSQL = "select tree_id, tree_name " +
						" from tab_system_tree where tree_parent_id=? and tree_id in(select tree_id from tab_tree_role where role_id=?) order by sequence";
			}else {
				TreeRoleSQL = "select tree_id, tree_name, tree_icon, tree_url, tree_data " +
						" from tab_system_tree where tree_parent_id=? and tree_id in(select tree_id from tab_tree_role where role_id=?) order by sequence";
			}
		}
		pSQL.setSQL(TreeRoleSQL);
		pSQL.setString(1, tree_parent_id);
		pSQL.setInt(2, Integer.parseInt(role_id));

		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map tree = cursor.getNext();

		while (tree != null) {
			node = new Node();
			node.setNode_parent_id(tree_parent_id);
			node.setNode_self_id((String) tree.get("tree_id"));
			node.setNode_text((String) tree.get("tree_name"));
			node.setNode_icon((String) tree.get("tree_icon"));
			node.setNode_url((String) tree.get("tree_url"));
			node.setNode_data((String) tree.get("tree_data"));

			list.add(node);

			list.addAll(getChildTree((String) tree.get("tree_id"), role_id));

			tree = cursor.getNext();
		}

		// add item
		list.addAll(getChildItem(tree_parent_id, role_id));

		// clear
		tree = null;
		cursor = null;

		return list;
	}
	/**
	 * 根据父目录节点、角色获取下属目录节点
	 * 
	 * @param tree_parent_id
	 * @param role_id
	 * @return List
	 */
	public List getChildTree(String tree_parent_id, String role_id,String clusteMode) {
		List list = new ArrayList();
		Node node = null;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			if(LipossGlobals.isGSDX()) {
				TreeRoleSQL = "select tree_id, tree_name " +
						" from tab_system_tree where tree_parent_id=? and tree_id in(select tree_id from tab_tree_role where role_id=?) order by sequence";
			}else {
				TreeRoleSQL = "select tree_id, tree_name, tree_icon, tree_url, tree_data " +
						" from tab_system_tree where tree_parent_id=? and tree_id in(select tree_id from tab_tree_role where role_id=?) order by sequence";
			}
		}
		String strSQL = TreeRoleSQL;
		if(!StringUtil.IsEmpty(clusteMode))
		{
			if(OPEN_CLUSTE_MODE.equals(clusteMode))
			{
				// teledb
				if (DBUtil.GetDB() == Global.DB_MYSQL) {
					if(LipossGlobals.isGSDX()) {
						TreeRoleSQL4OPEN = "select tree_id, tree_name " +
								" from tab_system_tree where tree_parent_id=? and tree_id not in (select item_id from tab_report_item where item_level=2) and tree_id in(select tree_id from tab_tree_role where role_id=?) order by sequence";
					}else {
						TreeRoleSQL4OPEN = "select tree_id, tree_name, tree_icon, tree_url, tree_data " +
								" from tab_system_tree where tree_parent_id=? and tree_id not in (select item_id from tab_report_item where item_level=2) and tree_id in(select tree_id from tab_tree_role where role_id=?) order by sequence";
					}
				}
				strSQL = TreeRoleSQL4OPEN;
			}
			else if(REPORT_CLUSTE_MODE.equals(clusteMode))
			{
				// teledb
				if (DBUtil.GetDB() == Global.DB_MYSQL) {
					if(LipossGlobals.isGSDX()) {
						TreeRoleSQL4REPORT = "select tree_id, tree_name " +
								"from tab_system_tree where tree_parent_id=? and tree_id in (select item_id from tab_report_item where item_level=2) and tree_id in(select tree_id from tab_tree_role where role_id=?) order by sequence";
					}else {
						TreeRoleSQL4REPORT = "select tree_id, tree_name, tree_icon, tree_url, tree_data " +
								"from tab_system_tree where tree_parent_id=? and tree_id in (select item_id from tab_report_item where item_level=2) and tree_id in(select tree_id from tab_tree_role where role_id=?) order by sequence";
					}
				}
				strSQL = TreeRoleSQL4REPORT;
			}
		}
		pSQL.setSQL(TreeRoleSQL);
		pSQL.setString(1, tree_parent_id);
		pSQL.setInt(2, Integer.parseInt(role_id));

		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map tree = cursor.getNext();

		while (tree != null) {
			node = new Node();
			node.setNode_parent_id(tree_parent_id);
			node.setNode_self_id((String) tree.get("tree_id"));
			node.setNode_text((String) tree.get("tree_name"));
			node.setNode_icon((String) tree.get("tree_icon"));
			node.setNode_url((String) tree.get("tree_url"));
			node.setNode_data((String) tree.get("tree_data"));

			list.add(node);

			list.addAll(getChildTree((String) tree.get("tree_id"), role_id,clusteMode));

			tree = cursor.getNext();
		}

		// add item
		list.addAll(getChildItem(tree_parent_id, role_id,clusteMode));

		// clear
		tree = null;
		cursor = null;

		return list;
	}
	/**
	 * 获取到特定目录、角色下的功能节点
	 * 
	 * @param tree_id
	 * @param role_id
	 * @return List
	 */
	public List getChildItem(String tree_id, String role_id) {
		List list = new ArrayList();
		Node node = null;

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			if(LipossGlobals.isGSDX()) {
				ItemRoleSQL = "select item_id, item_name, item_url " +
						"from tab_item where item_id in(select item_id from tab_tree_item where tree_id =?) and item_id in(select item_id from " + LipossGlobals.getLipossProperty("Systype") + " where role_id=?) order by item_name";
			}else {
				ItemRoleSQL = "select item_id, item_name, item_icon, item_url, item_data " +
						"from tab_item where item_id in(select item_id from tab_tree_item where tree_id =?) and item_id in(select item_id from " + LipossGlobals.getLipossProperty("Systype") + " where role_id=?) order by item_name";
			}
		}
		pSQL.setSQL(ItemRoleSQL);
		pSQL.setString(1, tree_id);
		pSQL.setInt(2, Integer.parseInt(role_id));

		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map item = cursor.getNext();

		while (item != null) {
			node = new Node();
			node.setNode_parent_id(tree_id);
			node.setNode_self_id((String) item.get("item_id"));
			node.setNode_text((String) item.get("item_name"));
			node.setNode_icon((String) item.get("item_icon"));
			node.setNode_url((String) item.get("item_url"));
			node.setNode_data((String) item.get("item_data"));

			list.add(node);

			item = cursor.getNext();
		}

		// clear
		item = null;
		cursor = null;

		return list;
	}
	/**
	 * 获取到特定目录、角色下的功能节点
	 * 
	 * @param tree_id
	 * @param role_id
	 * @return List
	 */
	public List getChildItem(String tree_id, String role_id,String clusteMode) {
		List list = new ArrayList();
		Node node = null;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			if(LipossGlobals.isGSDX()) {
				ItemRoleSQL = "select item_id, item_name, item_url " +
						"from tab_item where item_id in(select item_id from tab_tree_item where tree_id =?) and item_id in(select item_id from " + LipossGlobals.getLipossProperty("Systype") + " where role_id=?) order by item_name";
			}else {
				ItemRoleSQL = "select item_id, item_name, item_icon, item_url, item_data " +
						"from tab_item where item_id in(select item_id from tab_tree_item where tree_id =?) and item_id in(select item_id from " + LipossGlobals.getLipossProperty("Systype") + " where role_id=?) order by item_name";
			}
		}
		String strSQL = ItemRoleSQL;
		if(!StringUtil.IsEmpty(clusteMode))
		{
			if(OPEN_CLUSTE_MODE.equals(clusteMode))
			{
				// teledb
				if (DBUtil.GetDB() == Global.DB_MYSQL) {
					if(LipossGlobals.isGSDX()) {
						ItemRoleSQL4OPEN = "select item_id, item_name, item_url " +
								" from tab_item where item_id in(select item_id from tab_tree_item where tree_id =?) and item_id not in (select item_id from tab_report_item where item_level=3) and item_id in(select item_id from " + LipossGlobals.getLipossProperty("Systype") + " where role_id=?) order by item_name";
					}else {
						ItemRoleSQL4OPEN = "select item_id, item_name, item_icon, item_url, item_data " +
								" from tab_item where item_id in(select item_id from tab_tree_item where tree_id =?) and item_id not in (select item_id from tab_report_item where item_level=3) and item_id in(select item_id from " + LipossGlobals.getLipossProperty("Systype") + " where role_id=?) order by item_name";
					}
				}
				strSQL = ItemRoleSQL4OPEN;
			}
			else if(REPORT_CLUSTE_MODE.equals(clusteMode))
			{
				// teledb
				if (DBUtil.GetDB() == Global.DB_MYSQL) {
					if(LipossGlobals.isGSDX()) {
						ItemRoleSQL4RPORT = "select item_id, item_name, item_url " +
								" from tab_item where item_id in (select a.item_id from tab_tree_item a,tab_report_item b where a.item_id=b.item_id and b.item_level=3 and a.tree_id =?) and item_id in (select item_id from  " + LipossGlobals.getLipossProperty("Systype") + " where role_id=?) order by item_name";
					}else {
						ItemRoleSQL4RPORT = "select item_id, item_name, item_icon, item_url, item_data " +
								" from tab_item where item_id in (select a.item_id from tab_tree_item a,tab_report_item b where a.item_id=b.item_id and b.item_level=3 and a.tree_id =?) and item_id in (select item_id from  " + LipossGlobals.getLipossProperty("Systype") + " where role_id=?) order by item_name";
					}
				}
				strSQL = ItemRoleSQL4RPORT;
			}
		}
		pSQL.setSQL(strSQL);
		pSQL.setString(1, tree_id);
		pSQL.setInt(2, Integer.parseInt(role_id));

		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map item = cursor.getNext();

		while (item != null) {
			node = new Node();
			node.setNode_parent_id(tree_id);
			node.setNode_self_id((String) item.get("item_id"));
			node.setNode_text((String) item.get("item_name"));
			node.setNode_icon((String) item.get("item_icon"));
			node.setNode_url((String) item.get("item_url"));
			node.setNode_data((String) item.get("item_data"));

			list.add(node);

			item = cursor.getNext();
		}

		// clear
		item = null;
		cursor = null;

		return list;
	}
	/**
	 * 获取目录及下属节点信息
	 * 
	 * @param node_parent_id
	 * @param lTreeId
	 * @param lItemId
	 * @param nodeList
	 * @return List
	 */
	public List getXMLTreeItem(String node_parent_id, List lTreeId, List lItemId) {
		Map tree = null;
		Node node = null;
		List nodeList = new ArrayList();
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			if(LipossGlobals.isGSDX()) {
				IterSQL = "select tree_id, tree_name " +
						"from tab_system_tree where tree_parent_id=? order by sequence";
			}else {
				IterSQL = "select tree_id, tree_name, tree_icon, tree_url, tree_data " +
						"from tab_system_tree where tree_parent_id=? order by sequence";
			}
		}

		pSQL.setSQL(IterSQL);
		pSQL.setString(1, node_parent_id);
		Cursor Lcursor = DataSetBean.getCursor(pSQL.getSQL());
		tree = Lcursor.getNext();

		while (tree != null) {
			if (lTreeId != null && lTreeId.contains(tree.get("tree_id"))) {
				node = new Node();
				node.setNode_parent_id(node_parent_id);
				node.setNode_self_id((String) tree.get("tree_id"));
				node.setNode_text((String) tree.get("tree_name"));
				node.setNode_icon((String) tree.get("tree_icon"));
				node.setNode_url((String) tree.get("tree_url"));
				node.setNode_data((String) tree.get("tree_data"));

				nodeList.add(node);
				nodeList.addAll(getXMLTreeItem(String.valueOf(tree
						.get("tree_id")), lTreeId, lItemId));

			}

			tree = Lcursor.getNext();
		}

		nodeList.addAll(getXmlItem(node_parent_id, lItemId));

		// clear
		tree = null;
		Lcursor = null;

		return nodeList;
	}

	/**
	 * 获取功能节点信息
	 * 
	 * @param tree_id
	 * @param lItemId
	 * @param nodeList
	 * @return List
	 */
	public List getXmlItem(String tree_id, List lItemId) {
		Map item = null;
		Node node = null;
		List nodeList = new ArrayList();

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			if(LipossGlobals.isGSDX()) {
				ITEM_BY_TREE = "select item_id, item_name " +
						" from tab_item where item_id in(select item_id from tab_tree_item where tree_id=?)  and item_visual='1' order by sequence";
			}else {
				ITEM_BY_TREE = "select item_id, item_name, item_icon, item_url, item_data " +
						" from tab_item where item_id in(select item_id from tab_tree_item where tree_id=?)  and item_visual='1' order by sequence";
			}
		}

		pSQL.setSQL(ITEM_BY_TREE);
		pSQL.setString(1, tree_id);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		item = cursor.getNext();

		while (item != null) {
			if (lItemId.contains(item.get("item_id"))) {
				node = new Node();
				node.setNode_parent_id(tree_id);
				node.setNode_self_id((String) item.get("item_id"));
				node.setNode_text((String) item.get("item_name"));
				node.setNode_icon((String) item.get("item_icon"));
				node.setNode_url((String) item.get("item_url"));
				node.setNode_data((String) item.get("item_data"));
				nodeList.add(node);
			}
		}

		// clear
		cursor = null;
		item = null;

		return nodeList;
	}
}
