package com.linkage.litms.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.JdbcUtils;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;

public class Tree {
	private static Logger m_logger = LoggerFactory.getLogger(Tree.class);
	
	private static PrepareSQL pSQL = null;

	/**
	 * 首先当tree_sequence = 0为零时，从数据库读取最大值；若tree_sequence != 0，则向下自增1
	 */
	private static long tree_sequence = 0;

	/**
	 * 获取所有系统目录节点
	 */
	private String TREE_NODE_ALL = "select tree_id,tree_name from tab_system_tree";

	/**
	 * 创建目录节点
	 */
	private String CREATE_TREE_NODE = "insert into tab_system_tree (sequence,tree_id,tree_name,tree_parent_id,tree_desc) values (?,?,?,?,?)";
    /** 目录权限 */
    private String CREATE_TREE_NODE_ROLE = "insert into tab_tree_role (sequence,tree_id,role_id) values(0,?,?)";

	/**
	 * 更新目录节点
	 */
	private String UPDATE_TREE_NODE = "update tab_system_tree set tree_name=?,tree_parent_id=?,tree_desc=? where tree_id=?";

	/**
	 * 迭代获取节点
	 */
	private String IterSQL = "select tree_id, tree_name from tab_system_tree where tree_parent_id=? order by sequence";
    
    private String IterSQL_ROLE = "select tree_id, tree_name from tab_system_tree where tree_parent_id=? and tree_id in(select tree_id from tab_tree_role where role_id=?) order by sequence";

	/**
	 * 获取指定系统目录下功能点
	 */
	private String ITEM_BY_TREE = "select * from tab_item where item_id in(select item_id from tab_tree_item where tree_id=?)  and item_visual='1' order by sequence";

//  private String ITEM_BY_TREE_ROLE = "select * from tab_item where item_id in(select item_id from tab_tree_item where tree_id=?) and item_id in(select item_id from tab_item_role where role_id=?) and item_visual='1' order by sequence";
	private String ITEM_BY_TREE_ROLE = "select * from tab_item where item_id in(select item_id from tab_tree_item where tree_id=?) and item_id in(select item_id from " + LipossGlobals.getLipossProperty("Systype") + " where role_id=?) and item_visual='1' order by sequence";
	/**
	 * 获取节点细节
	 */
	private String TREE_DETAIL = "select * from tab_system_tree where tree_id=?";

	/**
	 * 删除掉指定目录节点
	 */
	private String TREE_NODES_DELETE = "delete from tab_system_tree where tree_id =?";

	/**
	 * 删除掉指定模块节点
	 */
	private String MODULE_NODES_DELETE = "delete from tab_module where module_id=?";

	/**
	 * 在删除系统目录同时删除目录下面配之对应权限功能点
	 */
//	private String ITEM_DELETE_BY_TREEID = "delete from tab_item_role where item_id in (select item_id from tab_tree_item where tree_id=?)";
	private String ITEM_DELETE_BY_TREEID = "delete from " + LipossGlobals.getLipossProperty("Systype") + " where item_id in (select item_id from tab_tree_item where tree_id=?)";

	/**
	 * 删除指定目录下功能节点
	 */
	private String TREE_ITEM_DELETE_BY_TREEID = "delete from tab_tree_item where tree_id=?";

	/**
	 * 在删除系统目录同时删除目录下面配之对应权限目录
	 */
	private String TREE_DELETE_BY_TREEID = "delete from tab_tree_role where tree_id = ?";

	/**
	 * 判断指定节点是否有下属节点
	 */
	private String TREES_EXIST = "select tree_id from tab_system_tree where tree_parent_id=?";

	/**
	 * 完成拖动操作
	 */
	private String DROPPROCESSOR = "update tab_system_tree set tree_parent_id=? where tree_id=?";

	/**
	 * 根据目录编号获得下面所有层的目录编号（不包含自身）
	 */
	private String FLOORTREE_BY_TREEID = "select tree_id from tab_system_tree where tree_id=?";

	/**
	 * 根据目录编号判断指定节点是否存在于此目录下面
	 */
	private String ITEMID_UNDEROF_TREEID = "select count(1) as num from tab_tree_item where item_id in(?) and tree_id=?";

	/**
	 * 根据用户角色获得用户所有功能节点权限
	 */
//	private String ITEMID_BY_ROLEID = "select item_id from tab_item_role where role_id=?";
	private String ITEMID_BY_ROLEID = "select item_id from " + LipossGlobals.getLipossProperty("Systype") + " where role_id=?";

	/**
	 * 根据用户角色获得用户所有系统目录权限
	 */
	private String TREEID_BY_ROLEID = "select tree_id from tab_tree_role where role_id=?";

	/**
	 * 根据下层目录编号获得上层目录编号
	 */
	private String UPPERTREEID_BY_TREEID = "select tree_parent_id from tab_system_tree where tree_id in(@@) and tree_parent_id!='0'";

	/**
	 * 获得顶层主菜单
	 */
	// private String ROOT_MENU = "select tree_id from tab_system_tree where
	// tree_parent_id='0' order by sequence";
	/**
	 * 根据系统指定目录编码获得上层目录名称
	 */
	private String PARENT_TREENAME_BY_TREEID = "select tree_parent_id,tree_name from tab_system_tree where tree_id =? ";

	/**
	 * 所有角色
	 */
	private String ALLROLES = "select role_id from tab_role";

	/**
	 * 获取系统模块列表
	 */
	private String MODULE_LIST = "select * from tab_module order by sequence";

    private String MODULE_LIST_ROLE = "select * from tab_module where module_id in (select tree_id from tab_tree_role where role_id=?) order by sequence";
    
    private String REPORTMODULE_LIST_ROLE="select * from tab_module where module_id in (select tree_id from tab_tree_role where role_id=?) and module_name like '%报表%'";
    
    /**
     * 根据模块名获取模块ID
     */
    private String MODULE_ID = "select module_id from tab_module where module_name=?";
	/**
	 * 角色\顶节点与树结构哈希映射
	 */
	private static Map TREE_XML_BY_ROLE_TREEID_MAP = null;

	public Tree() {
		if (tree_sequence == 0) {
			tree_sequence = DataSetBean.getMaxId("tab_system_tree", "sequence");
		}

		if (pSQL == null) {
			pSQL = new PrepareSQL();
		}
	}

	/**
	 * 获得顶层主菜单
	 * 
	 * @return List
	 */
	public List getRootMenu() {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			MODULE_LIST = "select module_id from tab_module order by sequence";
		}
		PrepareSQL psql = new PrepareSQL(MODULE_LIST);
	    psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(MODULE_LIST);
		Map field = cursor.getNext();

		List temp_list = new ArrayList();

		while (field != null) {
			temp_list.add(field.get("module_id"));

			field = cursor.getNext();
		}

		// clear
		field = null;
		cursor = null;

		return temp_list;
	}

	/**
	 * 根据角色编号获得用户第一层主菜单项
	 * 
	 * @param role_id
	 * @return List
	 */
	public List getRootMenuOfRole(String role_id) {
		// 根据用户角色获取用户系统目录权限
		List lTreeId = this.getTreeIdListByRoleId(role_id);
		// 根据已经获取系统目录权限获取上溯所有目录权限
		lTreeId = this.getUpperTreeIdListByTreeIdList(lTreeId);
		// 获得根目录
		List lTreeMenu = this.getRootMenu();
		int p = 0;
		while (p < lTreeMenu.size()) {
			if (!lTreeId.contains(lTreeMenu.get(p))) {
				lTreeMenu.remove(p);
			} else {
				p++;
			}
		}

		// clear
		lTreeId.clear();
		lTreeId = null;
		m_logger.debug("lTreeMenu = " + lTreeMenu.size());
		// 返回过滤之后的根目录
		return lTreeMenu;
	}

	/**
	 * 根据哈希关系获取到用户对应的目录树
	 * 
	 * @param tree_parent_id
	 * @param role_id
	 * @param flag
	 * @return String
	 */
	public String getTreeXmlByRoleTreeMap(String module_id, String role_id,
			boolean flag) {
		String xml = null;
		String TreeXML = null;

		try {
			if (TREE_XML_BY_ROLE_TREEID_MAP == null
					|| TREE_XML_BY_ROLE_TREEID_MAP.isEmpty()) {
				TREE_XML_BY_ROLE_TREEID_MAP = new HashMap();

				String temp_module_id = null;
				String temp_role_id = null;

				Cursor roles_cursor = null;
				Map field = null;

				// all modules
				List module_list = getRootMenu();
				roles_cursor = DataSetBean.getCursor(ALLROLES);
				for (int k = 0; k < module_list.size(); k++) {
					temp_module_id = (String) module_list.get(k);
					field = roles_cursor.getNext();

					while (field != null) {
						temp_role_id = (String) field.get("role_id");

						TreeXML = getDroitXmlByRoleId(temp_module_id,
								temp_role_id, flag);
						// System.out
						// .println("===================================================================");
						TREE_XML_BY_ROLE_TREEID_MAP.put(temp_role_id + ";"
								+ temp_module_id, TreeXML);

						field = roles_cursor.getNext();
					}

					roles_cursor.Reset();
				}

				// clear
				roles_cursor = null;
				module_list.clear();
				module_list = null;
			}

			// + TREE_XML_BY_ROLE_TREEID_MAP.size());
			// else other
			if (TREE_XML_BY_ROLE_TREEID_MAP.get(role_id + ";" + module_id) != null) {
				m_logger.debug("old xml");
				xml = (String) TREE_XML_BY_ROLE_TREEID_MAP.get(role_id + ";"
						+ module_id);
			} else {
				xml = TreeXML = getDroitXmlByRoleId(module_id, role_id, flag);
				TREE_XML_BY_ROLE_TREEID_MAP.put(role_id + ";" + module_id,
						TreeXML);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return xml;
	}

	/**
	 * 根据指定起始节点和用户角色构造用户权限树
	 * 
	 * @param tree_parent_id
	 * @param role_id
	 * @return String
	 */
	public String getDroitXmlByRoleId(String tree_parent_id, String role_id,
			boolean flag) {
		// 根据用户角色获取用户系统目录权限
		List lTreeId = this.getTreeIdListByRoleId(role_id);
		// 根据已经获取系统目录权限获取上溯所有目录权限
		lTreeId = this.getUpperTreeIdListByTreeIdList(lTreeId);
		// 根据用户角色获取所有用户功能节点操作权限
		List lItemId = this.getItemIdListByRoleId(role_id);

		// 从起始目录位置开始构造权限结构树
		String XMLTREE = getXMLTreeItem(tree_parent_id, lTreeId, lItemId, flag);

		// clear
		lTreeId = null;
		lItemId = null;

		return XMLTREE;
	}

	/**
	 * 循环递归获得系统目录XML文档内容
	 * 
	 * @param tree_parent_id
	 *            系统目录节点
	 * @param lTreeId
	 *            有权限访问的系统目录节点链表
	 * @param lItemId
	 *            有权限访问的功能点链表
	 * @param flag
	 *            是否增加超链接
	 * @return String
	 */
	public String getXMLTreeItem(String tree_parent_id, List lTreeId,
			List lItemId, boolean flag) {
		String IterXML = "";
		Map tree = null;

		// List Ltree = JdbcUtils.query(IterSQL, tree_parent_id);
		pSQL.setSQL(IterSQL);
		pSQL.setString(1, tree_parent_id);
		Cursor Lcursor = DataSetBean.getCursor(pSQL.getSQL());
		tree = Lcursor.getNext();

		while (tree != null) {
			// if (Lcursor != null && Lcursor.getRecordSize() > 0) {
			// for (int k = 0; k < Ltree.size(); k++) {
			// tree = (Map) Ltree.get(k);

			if (lTreeId != null && lTreeId.contains(tree.get("tree_id"))) {
				IterXML += "<item id='" + tree.get("tree_id") + "' text='"
						+ tree.get("tree_name") + "'>";
				// IterXML += "<userdata name='file'>alpha.html</userdata>";

				IterXML += getXMLTreeItem(String.valueOf(tree.get("tree_id")),
						lTreeId, lItemId, flag);
				// IterXML +=
				// getXmlItem(String.valueOf(tree.get("tree_id")),
				// lItemId, flag);

				IterXML += "</item>";
			}

			tree = Lcursor.getNext();
		}

		IterXML += getXmlItem(tree_parent_id, lItemId, flag);

		// clear
		tree = null;
		Lcursor = null;

		return IterXML;
	}

	public List getTreeIds(String tree_parent_id) {
		List Ltree = new ArrayList();
		pSQL.setSQL(IterSQL);
		pSQL.setString(1, tree_parent_id);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map field = cursor.getNext();
		while (field != null) {
			Ltree.add(field);

			field = cursor.getNext();
		}

		return Ltree;
	}

	/**
	 * 根据下层一组目录编号获得所有上层目录编号（包含自身）
	 * 
	 * @param lTreeId
	 * @return List
	 */
	public List getUpperTreeIdListByTreeIdList(List lTreeId) {
		String query = "";
		List list = new ArrayList();

		if (lTreeId!= null && lTreeId.size() > 0) {
			query = "?";
		} else {
			return list;
		}

		int len = lTreeId.size();

		for (int k = 1; k < len; k++) {
			query += ",?";
		}

		query = UPPERTREEID_BY_TREEID.replaceAll("@@", query);
		pSQL.setSQL(query);
		
		for(int k=0;k<len;k++){
			pSQL.setString(k+1, (String)lTreeId.get(k));
		}
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map field = cursor.getNext();
		
		while (field != null) {
			list.add(field.get("tree_parent_id"));

			field = cursor.getNext();
		}

		if(!list.isEmpty()){
			lTreeId.addAll(getUpperTreeIdListByTreeIdList(list));
		}

		//clear
		cursor = null;
		field = null;
		
		// 剔除重复元素
		lTreeId = new ArrayList(new HashSet(lTreeId));

		return lTreeId;
	}

	/**
	 * 根据用户角色获得用户所有系统目录权限
	 * 
	 * @param role_id
	 * @return List
	 */
	public List getTreeIdListByRoleId(String role_id) {
		pSQL.setSQL(TREEID_BY_ROLEID);
		pSQL.setInt(1, Integer.parseInt(role_id));
		List list = new ArrayList();

		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map field = cursor.getNext();

		if (cursor != null) {
			// 把值从链表所有坐标位提取出来并重新放入同样位置
			while (field != null) {
				list.add((String) field.get("tree_id"));

				field = cursor.getNext();
			}
		}

		// clear
		cursor = null;
		field = null;

		return list;
	}

	/**
	 * 根据用户角色获得用户所有功能节点权限
	 * 
	 * @param role_id
	 * @return List
	 */
	public List getItemIdListByRoleId(String role_id) {
		List list = new ArrayList();

		pSQL.setSQL(ITEMID_BY_ROLEID);
		pSQL.setInt(1, Integer.parseInt(role_id));

		// 把值从链表所有坐标位提取出来并重新放入同样位置
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map mItem = cursor.getNext();
		while (mItem != null) {
			list.add(mItem.get("item_id"));

			mItem = cursor.getNext();
		}

		// clear
		mItem = null;
		cursor = null;

		return list;
	}

	/**
	 * 根据目录编号判断指定节点是否存在于此目录下面
	 * 
	 * @param lItemId
	 * @return boolean
	 */
	public boolean isExistItemOfTreeId(List lItemId) {
		boolean flag = false;

		String query = "?";

		int len = lItemId.size();

		for (int k = 1; k < len; k++) {
			query += ",?";
		}

		ITEMID_UNDEROF_TREEID.replaceFirst("\\?", query);
		PrepareSQL psql = new PrepareSQL(ITEMID_UNDEROF_TREEID);
	    psql.getSQL();
		List list = JdbcUtils.query(ITEMID_UNDEROF_TREEID, lItemId.toArray());

		if (!((Map) list.get(0)).get("num").equals("0")) {
			flag = true;
		}

		// clear
		query = null;
		list = null;

		return flag;
	}
	

	/**
	 * 根据目录编号获得下面所有层的目录编号（不包含自身）
	 * 
	 * @param tree_id
	 * @return List
	 */
	public List getFloorTreeIdByTreeId(String tree_id) {
		PrepareSQL psql = new PrepareSQL(FLOORTREE_BY_TREEID);
	    psql.getSQL();
		List list = JdbcUtils.query(FLOORTREE_BY_TREEID, tree_id);

		if (list != null) {
			int len = list.size();

			// 把值从链表所有坐标位提取出来并重新放入同样位置
			Map mItem = null;
			for (int h = 0; h < len; h++) {
				mItem = (Map) list.get(h);

				list.set(h, mItem.get("tree_id"));
			}

			// clear
			mItem = null;

			for (int k = 0; k < len; k++) {
				list
						.addAll(getFloorTreeIdByTreeId(String.valueOf(list
								.get(k))));
			}
		}

		return list;
	}

	/**
	 * 获取系统目录所有节点
	 * 
	 * @return List
	 */
	public List getTreeAll() {
		
        //List list = JdbcUtils.query(TREE_NODE_ALL);
		PrepareSQL psql = new PrepareSQL(TREE_NODE_ALL);
	    psql.getSQL();
	    Cursor cursor = DataSetBean.getCursor(TREE_NODE_ALL);
        Map fields = cursor.getNext();
        List TreeIdList = new ArrayList();
        while(fields != null){
            TreeIdList.add(fields.get("tree_id"));
            fields = cursor.getNext();
        }
		/*
		for (int k = 0; k < list.size(); k++) {
			TreeIdList.add(((Map) list.get(k)).get("tree_id"));
		}
        */

		return TreeIdList;
	}

	/**
	 * 获得所有系统目录节点对照
	 * 
	 * @return Map
	 */
	public Map getTreeMap() {
		PrepareSQL psql = new PrepareSQL(TREE_NODE_ALL);
	    psql.getSQL();
		List list = JdbcUtils.query(TREE_NODE_ALL);

		Map mTree = new HashMap();

		for (int k = 0; k < list.size(); k++) {
			mTree.put(((Map) list.get(k)).get("tree_id"), ((Map) list.get(k))
					.get("tree_name"));
		}

		// clear
		list.clear();
		list = null;

		return mTree;
	}

	/**
	 * 从系统模块起获取系统目录
	 * 
	 * @return String
	 */
	public String getSystemRootTreeXML() {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			MODULE_LIST = "select module_id, module_name from tab_module order by sequence";
		}
		PrepareSQL psql = new PrepareSQL(MODULE_LIST);
	    psql.getSQL();
		List lModule = JdbcUtils.query(MODULE_LIST);

		String IterXML = "";
		Map module = null;

		for (int k = 0; k < lModule.size(); k++) {
			module = (Map) lModule.get(k);

			IterXML += "<item id='" + module.get("module_id") + "' text='"
					+ module.get("module_name") + "'>";
			IterXML += getXMLTree(String.valueOf(module.get("module_id")));
			IterXML += "</item>";
		}

		return IterXML;
	}
	
	
	/**
	 * 根据角色展示树型结构
	 * @param role_id
	 * @return
	 */
	public String getSystemRootTreeXML(String role_id)
	{
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			MODULE_LIST_ROLE = "select module_id, module_name " +
					" from tab_module where module_id in (select tree_id from tab_tree_role where role_id=?) order by sequence";
		}

		PrepareSQL psql = new PrepareSQL(MODULE_LIST_ROLE.replaceFirst(
			"\\?", role_id));
		psql.getSQL();
		Cursor lModule = DataSetBean.getCursor(MODULE_LIST_ROLE.replaceFirst(
						"\\?", role_id));
		String IterXML = "";
		Map module = lModule.getNext();

		while (module != null)
		{
			IterXML += "<item id='" + module.get("module_id") + "' text='"
							+ module.get("module_name") + "'>";
			IterXML += getXMLTree(String.valueOf(module.get("module_id")),
							role_id);
			IterXML += "</item>";
			module = lModule.getNext();
		}
		
		lModule = null;
		module = null;
		
		return IterXML;
 
	}
	
	
	/**
	 * 返回报表
	 * @param role_id
	 * @return
	 */
	public String getReportModuleTreeXML(String role_id)
	{
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			REPORTMODULE_LIST_ROLE="select module_id, module_name " +
					" from tab_module where module_id in (select tree_id from tab_tree_role where role_id=?) and module_name like '%报表%'";
		}
		pSQL.setSQL(REPORTMODULE_LIST_ROLE);
		pSQL.setStringExt(1,role_id,false);
		Cursor lModule =DataSetBean.getCursor(pSQL.getSQL());
		String IterXML = "";
		Map module = lModule.getNext();

		if (module != null)
		{
			IterXML += "<item id='" + module.get("module_id") + "' text='"
							+ module.get("module_name") + "'>";
			IterXML += getXMLTree(String.valueOf(module.get("module_id")),
							role_id);
			IterXML += "</item>";			
		}
		
		lModule = null;
		module = null;
		
		return IterXML;		
	}

	/**
	 * 循环递归获得XML文档内容
	 * 
	 * @param tree_parent_id
	 * @return String
	 */
	public String getXMLTree(String tree_parent_id) {
		String IterXML = "";
		PrepareSQL psql = new PrepareSQL(IterSQL);
		psql.getSQL();
		List lTree = JdbcUtils.query(IterSQL, tree_parent_id);

		Map tree = null;

		for (int k = 0; k < lTree.size(); k++) {
			tree = (Map) lTree.get(k);
			IterXML += "<item id='" + tree.get("tree_id") + "' text='"
					+ tree.get("tree_name") + "'>";

			// IterXML += "<userdata name='file'>alpha.html</userdata>";

			IterXML += getXMLTree(String.valueOf(tree.get("tree_id")));
			IterXML += "</item>";
		}

		// clear
		lTree = null;

		return IterXML;
	}
	
	
	/**
	 * 查询某个节点下的某个角色下的目录
	 * @param tree_parent_id
	 * @param role_id
	 * @return
	 */
	public String getXMLTree(String tree_parent_id,String role_id)
	{
		String IterXML = "";
		pSQL.setSQL(IterSQL_ROLE);
		pSQL.setString(1, tree_parent_id);
		pSQL.setStringExt(2,role_id,false);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map field = cursor.getNext();
		while (field != null)
		{
			IterXML += "<item id='" + field.get("tree_id") + "' text='"
							+ field.get("tree_name") + "'>";
			IterXML += getXMLTree(String.valueOf(field.get("tree_id")));
			IterXML += "</item>";
			field = cursor.getNext();
		}
		field = null;
		cursor = null;
		return IterXML;
	}

	/**
	 * 从系统模块起获取系统目录以及功能节点
	 * 需要根据角色来过滤
	 * @return String
	 */
	public String getSystemRootTreeItemXML(String role_id) {
        long current = System.currentTimeMillis();
        // List lModule = JdbcUtils.query(MODULE_LIST_ROLE.replaceFirst("\\?",role_id));
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			MODULE_LIST_ROLE = "select module_id, module_name " +
					" from tab_module where module_id in (select tree_id from tab_tree_role where role_id=?) order by sequence";
		}
        PrepareSQL psql = new PrepareSQL(MODULE_LIST_ROLE.replaceFirst("\\?", role_id));
		psql.getSQL();
        Cursor lModule = DataSetBean.getCursor(MODULE_LIST_ROLE.replaceFirst("\\?", role_id));
        String IterXML = "";
        Map module = lModule.getNext();

        while (module != null) {
            IterXML += "<item id='" + module.get("module_id") + "' text='"
                    + module.get("module_name") + "'>";
            IterXML += getXMLTreeItem(String.valueOf(module.get("module_id")),
                    role_id);
            IterXML += "</item>";
            module = lModule.getNext();
        }
        /*
         * for (int k = 0; k < lModule.size(); k++) { module = (Map)
         * lModule.get(k); // m_logger.debug(module.get("module_name")); IterXML += "<item
         * id='" + module.get("module_id") + "' text='" +
         * module.get("module_name") + "'>"; IterXML +=
         * getXMLTreeItem(String.valueOf(module.get("module_id")),role_id);
         * IterXML += "</item>"; }
         */
        lModule = null;
        module = null;
        m_logger.debug("树形节点生成时间：" + (System.currentTimeMillis() - current) + "ms");
        return IterXML;
    }
	
	/**
	 * 根据module_name来获取模块下的功能点
	 * @param module_name
	 * @param itemModuleMap
	 */
	public void getModuleItem(String module_name,HashMap itemModuleMap)
	{
		m_logger.debug("begin getModuleItem time:"+new DateTimeUtil().getLongDate());
		if(null==itemModuleMap)
		{
			itemModuleMap = new HashMap();
		}
		if(null==module_name||"".equals(module_name))
		{
			return;
		}
		PrepareSQL psql = new PrepareSQL(MODULE_ID.replaceFirst("\\?","'"+module_name+"'"));
		psql.getSQL();
		HashMap record = DataSetBean.getRecord(MODULE_ID.replaceFirst("\\?","'"+module_name+"'"));
		if(null==record)
		{
			return;
		}
		
		String module_id = (String)record.get("module_id");
		//获取模块下的所有功能点
		getChildTreeItem(module_id,module_name,itemModuleMap);
		
		m_logger.debug("end getModuleItem time:"+new DateTimeUtil().getLongDate());
	}
	
	/**
	 * 从模块起遍历获取功能点
	 * @param itemModuleMap
	 */
	public void getSystemRootTree(HashMap itemModuleMap)
	{
		m_logger.debug("begin time:"+new DateTimeUtil().getLongDate());
		if(null==itemModuleMap)
		{
			itemModuleMap = new HashMap();
		}
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			MODULE_LIST = "select module_id, module_name from tab_module order by sequence";
		}
		PrepareSQL psql = new PrepareSQL(MODULE_LIST);
		psql.getSQL();
		Cursor lModule = DataSetBean.getCursor(MODULE_LIST);
		Map module = lModule.getNext();
		String module_name="";
		String module_id="";
		while(null!=module)
		{
			module_id = (String)module.get("module_id");
			module_name = (String)module.get("module_name");
			getChildTreeItem(module_id,module_name,itemModuleMap);			
			module = lModule.getNext();
		}	
		
		//clear
		module = null;
		lModule = null;
		
		m_logger.debug("item_size:"+itemModuleMap.size());		
		m_logger.debug("end time:"+new DateTimeUtil().getLongDate());
	}
	
	/**
	 * 获取tree_id下的所有功能点
	 * @param tree_id
	 * @param module_name
	 */
	public void getChildTreeItem(String tree_parent_id,String module_name,HashMap itemModuleMap)
	{
		if(null==itemModuleMap)
		{
			itemModuleMap = new HashMap();			
		}
		PrepareSQL psql = new PrepareSQL(IterSQL.replaceFirst("\\?","'"+tree_parent_id+"'"));
		psql.getSQL();
		Cursor Ltree = DataSetBean.getCursor(IterSQL.replaceFirst("\\?","'"+tree_parent_id+"'"));
		Map  tree = Ltree.getNext();
		String tree_id ="";
		while(null!=tree)
		{
			tree_id = (String)tree.get("tree_id");
			getChildTreeItem(tree_id,module_name,itemModuleMap);
			
			tree = Ltree.getNext();
		}
		
		//获取tree_parent_id下的功能点
		getItem(tree_parent_id,module_name,itemModuleMap);
		
		//clear
		tree = null;
		Ltree = null;
		
	}
	
	/**
	 * tree_parent_id下的一节子功能点
	 * @param tree_parent_id
	 * @param module_name
	 * @param itemModuleMap
	 */
	public void getItem(String tree_parent_id,String module_name,HashMap itemModuleMap)
	{
		if(null==itemModuleMap)
		{
			itemModuleMap = new HashMap();			
		}
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			ITEM_BY_TREE = "select item_name " +
					"from tab_item where item_id in(select item_id from tab_tree_item where tree_id=?)  and item_visual='1' order by sequence";
		}

		PrepareSQL psql = new PrepareSQL(ITEM_BY_TREE.replaceFirst("\\?","'"+tree_parent_id+"'"));
		psql.getSQL();
		Cursor Litem = DataSetBean.getCursor(ITEM_BY_TREE.replaceFirst("\\?","'"+tree_parent_id+"'"));
		Map item = Litem.getNext();
		String item_name="";
		while(null!=item)
		{
			item_name = (String)item.get("item_name");
			itemModuleMap.put(item_name,module_name);
			
			item = Litem.getNext();			
		}
		
		//clear
		item = null;
		Litem = null;
	}

	/**
	 * 循环递归获得系统目录XML文档内容
	 * 
	 * @param tree_parent_id
	 *            系统目录节点
	 * @return String
	 */
	public String getXMLTreeItem(String tree_parent_id) {
		String IterXML = "";
		PrepareSQL psql = new PrepareSQL(IterSQL);
		psql.getSQL();
		List Ltree = JdbcUtils.query(IterSQL, tree_parent_id);
		Map tree = null;
		for (int k = 0; k < Ltree.size(); k++) {
			tree = (Map) Ltree.get(k);
			IterXML += "<item id='" + tree.get("tree_id") + "' text='"
					+ tree.get("tree_name") + "'>";
			// IterXML += "<userdata name='file'>alpha.html</userdata>";

			IterXML += getXMLTreeItem(String.valueOf(tree.get("tree_id")));
			//IterXML += getXmlItem(String.valueOf(tree.get("tree_id")));

			IterXML += "</item>";
		}

		// item
		// m_logger.debug("||||||||||||||||||||"+getXmlItem(tree_parent_id));
		IterXML += getXmlItem(tree_parent_id);

		return IterXML;
	}
	
	
	

	/**
	 * 循环递归获得系统目录XML文档内容
	 * 
	 * @param tree_parent_id
	 *            系统目录节点
	 * @param role_id
	 *            系统角色
	 * @return String
	 */
	public String getXMLTreeItem(String tree_parent_id, String role_id) {
		String IterXML = "";
        
		//List Ltree = JdbcUtils.query(IterSQL_ROLE, new String[][]{{tree_parent_id,"String"},{role_id,"int"}});
        pSQL.setSQL(IterSQL_ROLE);
        pSQL.setString(1, tree_parent_id);
        pSQL.setStringExt(2, role_id, false);
        Cursor Ltree = DataSetBean.getCursor(pSQL.getSQL());
		Map tree = Ltree.getNext();
        while(tree != null){
            IterXML += "<item id='" + tree.get("tree_id") + "' text='" + tree.get("tree_name") + "'>";
            // IterXML += "<userdata name='file'>alpha.html</userdata>";
        
            IterXML += getXMLTreeItem(String.valueOf(tree.get("tree_id")),
                    role_id);
            //IterXML += getXmlItem(String.valueOf(tree.get("tree_id")),role_id);
        
            IterXML += "</item>";
            tree = Ltree.getNext();
        }
        /*
		for (int k = 0; k < Ltree.size(); k++) {
			tree = (Map) Ltree.get(k);
			IterXML += "<item id='" + tree.get("tree_id") + "' text='"
					+ tree.get("tree_name") + "'>";
			// IterXML += "<userdata name='file'>alpha.html</userdata>";

			IterXML += getXMLTreeItem(String.valueOf(tree.get("tree_id")),
					role_id);
			//IterXML += getXmlItem(String.valueOf(tree.get("tree_id")),role_id);

			IterXML += "</item>";
		}
        */
        
        IterXML += getXmlItem(tree_parent_id,role_id);
        Ltree = null;
        tree = null;
		return IterXML;
	}

	/**
	 * 根据目录节点获取下面功能节点（限定范围内）
	 * 
	 * @param tree_id
	 *            系统目录节点
	 * @param lItemId
	 *            限定范围
	 * @return String
	 */
	public String getXmlItem(String tree_id, List lItemId, boolean flag) {
		String ItemXML = "";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			ITEM_BY_TREE = "select item_id, item_name, item_url " +
					"from tab_item where item_id in(select item_id from tab_tree_item where tree_id=?)  and item_visual='1' order by sequence";
		}

		PrepareSQL psql = new PrepareSQL(ITEM_BY_TREE);
		psql.getSQL();
		List LItem = JdbcUtils.query(ITEM_BY_TREE, tree_id);
		Map item = null;

		for (int k = 0; k < LItem.size(); k++) {
			item = (Map) LItem.get(k);

			if (lItemId.contains(item.get("item_id"))) {
				ItemXML += "<item id='" + item.get("item_id") + "' text='"
						+ item.get("item_name") + "'>";
				ItemXML += flag ? "<userdata name='file'>"
						+ item.get("item_url") + "</userdata>" : "";
				ItemXML += "</item>";
			}
		}

		// clear
		LItem = null;

		return ItemXML;
	}

	/**
	 * 根据目录节点获取下面功能节点
	 * 
	 * @param tree_id
	 *            系统目录节点
	 * @return String
	 */
	public String getXmlItem(String tree_id) {
		String ItemXML = "";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			ITEM_BY_TREE = "select item_id, item_name " +
					"from tab_item where item_id in(select item_id from tab_tree_item where tree_id=?)  and item_visual='1' order by sequence";
		}
		PrepareSQL psql = new PrepareSQL(ITEM_BY_TREE);
		psql.getSQL();
		List LItem = JdbcUtils.query(ITEM_BY_TREE, tree_id);
        
		Map item = null;
		for (int k = 0; k < LItem.size(); k++) {
			item = (Map) LItem.get(k);
			ItemXML += "<item id='" + item.get("item_id") + "' text='"
					+ item.get("item_name") + "'>";
			// ItemXML += "<userdata name='file'>alpha.html</userdata>";
			ItemXML += "</item>";
		}

		return ItemXML;
	}

    /**
     * 根据目录节点和role_id获取下面功能节点
     * 
     * @param tree_id
     *            系统目录节点
     * @return String
     */
    public String getXmlItem(String tree_id,String role_id){
        String ItemXML = "";

        //List LItem = JdbcUtils.query(ITEM_BY_TREE_ROLE, new String[][] {{tree_id,"String"},{role_id,"int"}});
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			ITEM_BY_TREE_ROLE = "select item_id, item_name " +
					" from tab_item where item_id in(select item_id from tab_tree_item where tree_id=?) and item_id in(select item_id from " + LipossGlobals.getLipossProperty("Systype") + " where role_id=?) and item_visual='1' order by sequence";
		}
        pSQL.setSQL(ITEM_BY_TREE_ROLE);
        pSQL.setString(1, tree_id);
        pSQL.setStringExt(2, role_id, false);
        Cursor LItem = DataSetBean.getCursor(pSQL.getSQL());
        
        Map item = LItem.getNext(); 
        while(item != null){
            ItemXML += "<item id='" + item.get("item_id") + "' text='" + item.get("item_name") + "'>";
            ItemXML += "</item>";
            item = LItem.getNext();
        }
        /*
        for (int k = 0; k < LItem.size(); k++) {
            item = (Map) LItem.get(k);
            ItemXML += "<item id='" + item.get("item_id") + "' text='"
                    + item.get("item_name") + "'>";
            ItemXML += "</item>";
        }
        */
        LItem = null;
        item = null;
        return ItemXML;
    }
	/**
	 * 获得指定节点细节
	 * 
	 * @param tree_id
	 * @return String
	 */
	public String loadNodeXMLDetail(String tree_id) {
		String XML = "";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			TREE_DETAIL = "select tree_name, tree_parent_id, tree_desc from tab_system_tree where tree_id=?";
		}

		PrepareSQL psql = new PrepareSQL(TREE_DETAIL);
		psql.getSQL();
		List Ltree = JdbcUtils.query(TREE_DETAIL, tree_id);
		Map tree = null;

		// 从系统模块表中读取系统模块信息
		Module module = new Module();
		module = module.getModuleInfo(tree_id);

		if (module != null) {

			XML += "<name>" + module.getModule_name() + "</name>";
			XML += "<parent_id>0</parent_id>";
			XML += "<desc>" + module.getModule_desc() + "</desc>";
		}

		// 从系统目录表中读取系统目录信息
		if (Ltree.size() > 0) {
			tree = (Map) Ltree.get(0);

			XML += "<name>" + tree.get("tree_name") + "</name>";
			XML += "<parent_id>" + tree.get("tree_parent_id") + "</parent_id>";
			XML += "<desc>" + tree.get("tree_desc") + "</desc>";
		}

		// clear
		module = null;
		Ltree = null;
		tree = null;

		return XML;
	}

	/**
	 * 创建系统目录节点
	 * 
	 * @param tree_id
	 * @param tree_name
	 * @param tree_parent_id
	 * @param tree_desc
	 * @return boolean
	 */
	public boolean createTreeNode(String tree_id, String tree_name,
			String tree_parent_id, String tree_desc,long role_id) {
		// int codes = JdbcUtils.update(CREATE_TREE_NODE, new String[] {
		// String.valueOf(++tree_sequence), tree_id, tree_name,
		// tree_parent_id, tree_desc });
	    /*
		int codes = JdbcUtils.executeUpdate(CREATE_TREE_NODE, new String[][] {
				{ String.valueOf(++tree_sequence), "int" },
				{ tree_id, "String" }, { tree_name, "String" },
				{ tree_parent_id, "String" }, { tree_desc, "String" } });
        */
        String[] sqlArr = new String[2];
        PrepareSQL pSQL = new PrepareSQL();
        pSQL.setSQL(CREATE_TREE_NODE);
        pSQL.setLong(1, ++tree_sequence);
        pSQL.setString(2, tree_id);
        pSQL.setString(3,tree_name);
        pSQL.setString(4,tree_parent_id);
        pSQL.setString(5,tree_desc);
        sqlArr[0] = pSQL.getSQL();
        
        pSQL.setSQL(CREATE_TREE_NODE_ROLE);
        pSQL.setString(1, tree_id);
        pSQL.setLong(2, role_id);
        sqlArr[1] = pSQL.getSQL();
        pSQL = null;
        try{
            int[] codes = DataSetBean.doBatch(sqlArr);
            return (codes != null && codes[0] > 0);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
	}

	/**
	 * 更新系统目录节点细节信息
	 * 
	 * @param tree_id
	 * @param tree_name
	 * @param tree_parent_id
	 * @param tree_desc
	 * @return boolean
	 */
	public boolean updateTreeNode(String tree_id, String tree_name,
			String tree_parent_id, String tree_desc) {
		PrepareSQL psql = new PrepareSQL(UPDATE_TREE_NODE);
		psql.getSQL();
		int codes = JdbcUtils.update(UPDATE_TREE_NODE, new String[] {
				tree_name, tree_parent_id, tree_desc, tree_id });
		// m_logger.debug("" + codes);
		return codes > 0 ? true : false;
	}

	/**
	 * 删除当前节点以及下属节点
	 * 
	 * @param tree_id
	 * @return boolean
	 */
	public boolean deleteTreeNode(String tree_id) {
		// 删除权限表中系统目录下功能节点
		PrepareSQL psql = new PrepareSQL(ITEM_DELETE_BY_TREEID);
		psql.getSQL();
		JdbcUtils.update(ITEM_DELETE_BY_TREEID, tree_id);

		// 删除权限表中系统目录节点
		psql = new PrepareSQL(TREE_DELETE_BY_TREEID);
		psql.getSQL();
		JdbcUtils.update(TREE_DELETE_BY_TREEID, tree_id);

		// 删除目录与功能节点关联
		psql = new PrepareSQL(TREE_ITEM_DELETE_BY_TREEID);
		psql.getSQL();
		JdbcUtils.update(TREE_ITEM_DELETE_BY_TREEID, tree_id);

		// 删除掉指定模块节点
		psql = new PrepareSQL(MODULE_NODES_DELETE);
		psql.getSQL();
		JdbcUtils.update(MODULE_NODES_DELETE, tree_id);

		// 删除系统目录节点
		psql = new PrepareSQL(TREE_NODES_DELETE);
		psql.getSQL();
		JdbcUtils.update(TREE_NODES_DELETE, tree_id);
		psql = new PrepareSQL(TREES_EXIST);
		psql.getSQL();
		List list_tree = JdbcUtils.query(TREES_EXIST, tree_id);
		if (list_tree.size() > 0) {
			int h = list_tree.size();
			for (int k = 0; k < h; k++) {
				deleteTreeNode(String.valueOf(list_tree.get(k)));
			}
		}

		return true;
	}

	/**
	 * 完成拖动操作的节点变更
	 * 
	 * @param tree_parent_id
	 * @param tree_id 
	 * @return boolean
	 */
	public boolean dropProcessor(String tree_parent_id, String tree_id) {
		PrepareSQL psql = new PrepareSQL(DROPPROCESSOR);
		psql.getSQL();
		int codes = JdbcUtils.update(DROPPROCESSOR, new String[] {
				tree_parent_id, tree_id });

		return codes > 0 ? true : false;
	}

	/**
	 * 根据系统指定目录编码逐层获得上层目录名称
	 * 
	 * @param tree_id
	 * @return String
	 */
	public String getFullParentPathByTreeId(String tree_id) {
		PrepareSQL psql = new PrepareSQL(PARENT_TREENAME_BY_TREEID);
		psql.getSQL();
		List list = JdbcUtils.query(PARENT_TREENAME_BY_TREEID, tree_id);

		// 系统目录路径
		String path = "";

		if (list != null && list.size() > 0) {
			tree_id = String.valueOf(((Map) list.get(0)).get("tree_parent_id"));
			String tree_name = String.valueOf(((Map) list.get(0))
					.get("tree_name"));

			// 继续调用执行
			path = getFullParentPathByTreeId(tree_id) + "->" + tree_name;

			tree_name = null;
		}

		// clear
		list = null;

		return path;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

}
