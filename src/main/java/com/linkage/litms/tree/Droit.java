package com.linkage.litms.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
import com.linkage.module.gwms.Global;

public class Droit {
	private static Logger logger = LoggerFactory.getLogger(Droit.class);
	
	/**
	 * 删除角色系统目录权限表中权限
	 */
	private String DROIT_TREE_DELETE = "delete from tab_tree_role where role_id=?";

	/**
	 * 删除角色功能点权限表中权限
	 */
//	private String DROIT_ITEM_DELETE = "delete from tab_item_role where role_id=?";
	private String DROIT_ITEM_DELETE = "delete from " + LipossGlobals.getLipossProperty("Systype") + " where role_id=?";
	/**
	 * 删除角色表中对象
	 */
	private String DROIT_ROLE_DELETE = "delete from tab_role where role_id=?";
    
    /**
     * 删除账户角色表中对象
     */
    private String DROIT_ACC_ROLE_DELETE = "delete from tab_acc_role where role_id=?";

	/**
	 * 保存角色系统目录权限表中权限
	 */
	private String DROIT_TREE_SAVE = "insert into tab_tree_role (tree_id,role_id) values (?,?)";

	/**
	 * 保存角色功能点权限表中权限
	 */
//	private String DROIT_ITEM_SAVE = "insert into tab_item_role (item_id,role_id) values (?,?)";
	private String DROIT_ITEM_SAVE = "insert into " + LipossGlobals.getLipossProperty("Systype") + " (item_id,role_id) values (?,?)";

	/**
	 * 获取所有角色对象
	 */
	private String ROLES_SELECT = "select role_id ,role_name from tab_role";
    
    /**
     * 获取所有角色对象，但不包含当前用户角色
     */
    private String ROLES_SELECT_EXP = "select role_id ,role_name from tab_role where role_id !=";
    

	/**
	 * 获取指定角色下所有功能点
	 */
//	private String ITEMS_BY_ROLEID = "select item_id from tab_item_role where role_id=?";
    private String ITEMS_BY_ROLEID = "select item_id from " + LipossGlobals.getLipossProperty("Systype") + " where role_id=?";

	/**
	 * 根据角色名称判断角色是否已经存在
	 */
	private String ISEXIST_OF_ROLENAME = "select role_id from tab_role where role_name=?";

	/**
	 * 判断更新角色名称是否与已有角色名称重复
	 */
	private String ISEXIST_OF_OTHERROLENAME = "select role_id from tab_role where role_name=? and role_name!=?";

	/**
	 * 新增角色对象信息
	 */
	private String CREATE_NEWROLE = "insert into tab_role (role_id,role_name,role_desc,role_pid,acc_oid) values (?,?,?,?,?)";

	/**
	 * 更新指定角色信息
	 */
	private String UPDATE_ROLEINFO = "update tab_role set role_name=?,role_desc=? where role_id=?";
	/**
     *插入快捷菜单sql 
	 */
    private String INSERT_SHORTCUT = "insert into tab_shortcut_item (acc_oid,item_id) values(?,?)";
    private String SELECT_SHORTCUT = "select * from tab_item where item_id in(select item_id from tab_shortcut_item where acc_oid=?)";
    
	public Droit() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 更新指定角色信息
	 * 
	 * @param role_name
	 * @param role_desc
	 * @param role_pid
	 * @param acc_oid
	 * @param role_id
	 * @return boolean
	 */
	public boolean updateRoleInfo(String role_name, String role_desc,
			String role_pid, String acc_oid, String role_id) {
		PrepareSQL pSQL = new PrepareSQL();

		pSQL.setSQL(UPDATE_ROLEINFO);
		pSQL.setString(1, role_name);
		pSQL.setString(2, role_desc);
		/* 2008-3-4 Zhaof
		//pSQL.setInt(3, Integer.parseInt(role_pid));
		//pSQL.setInt(4, Integer.parseInt(acc_oid));
		 * 修改角色时不更新role_pid和acc_oid
		 */
		pSQL.setInt(3, Integer.parseInt(role_id));

		return (JdbcUtils.update(pSQL.getSQL()) > 0) ? true : false;
	}

	/**
	 * 新增角色对象信息
	 * 
	 * @param role_id
	 * @param role_name
	 * @param role_desc
	 * @param role_pid
	 * @param acc_oid
	 * @return boolean
	 */
	public boolean createNewRole(String role_id, String role_name,
			String role_desc, String role_pid, String acc_oid) {
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(CREATE_NEWROLE);
		pSQL.setInt(1, Integer.parseInt(role_id));
		pSQL.setString(2, role_name);
		pSQL.setString(3, role_desc);
		pSQL.setInt(4, Integer.parseInt(role_pid));
		pSQL.setInt(5, Integer.parseInt(acc_oid));

		return (JdbcUtils.update(pSQL.getSQL()) > 0) ? true : false;
	}

	/**
	 * 根据角色名称判断角色是否已经存在
	 * 
	 * @param role_name
	 * @return boolean
	 */
	public boolean isExistRoleName(String role_name) {
		PrepareSQL psql = new PrepareSQL(ISEXIST_OF_ROLENAME);
        psql.getSQL();
		List list = JdbcUtils.query(ISEXIST_OF_ROLENAME, role_name);

		if (list != null && list.size() > 0) {
			list.clear();
			list = null;

			return true;
		}

		list = null;

		return false;
	}

	/**
	 * 根据角色名称判断角色是否已经存在
	 * 
	 * @param role_name
	 * @return boolean
	 */
	public boolean isExistOtherRoleName(String role_name, String old_role_name) {
		PrepareSQL psql = new PrepareSQL(ISEXIST_OF_OTHERROLENAME);
        psql.getSQL();
		List list = JdbcUtils.query(ISEXIST_OF_OTHERROLENAME, new String[] {
				role_name, old_role_name });

		if (list != null && list.size() > 0) {
			list.clear();
			list = null;

			return true;
		}

		list = null;

		return false;
	}

	/**
	 * 删除角色表中对象
	 * 
	 * @param role_id
	 * @return boolean
	 */
	public boolean delRoleDroitByRoleId(String role_id) {
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(DROIT_ROLE_DELETE);
		pSQL.setInt(1, Integer.parseInt(role_id));

		return JdbcUtils.update(pSQL.getSQL()) > 0 ? true : false;
	}
    
    /**
     * 删除账户角色表中对象
     * 
     * @param role_id
     * @return boolean
     */
    public boolean delAcc_RoleDroitByRoleId(String role_id) {
        PrepareSQL pSQL = new PrepareSQL();
        pSQL.setSQL(DROIT_ACC_ROLE_DELETE);
        pSQL.setInt(1, Integer.parseInt(role_id));

        return JdbcUtils.update(pSQL.getSQL()) > 0 ? true : false;
    }

	/**
	 * 删除角色系统权限表中权限
	 * 
	 * @param role_id
	 * @return boolean
	 */
	public boolean delTreeDroitByRoleId(String role_id) {
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(DROIT_TREE_DELETE);
		pSQL.setInt(1, Integer.parseInt(role_id));

		return JdbcUtils.update(pSQL.getSQL()) > 0 ? true : false;
	}

	/**
	 * 删除角色功能点权限表中权限
	 * 
	 * @param role_id
	 * @return boolean
	 */
	public boolean delItemDroitByRoleId(String role_id) {
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(DROIT_ITEM_DELETE);
		pSQL.setInt(1, Integer.parseInt(role_id));

		return JdbcUtils.update(pSQL.getSQL()) > 0 ? true : false;
	}

	/**
	 * 保存角色系统目录权限表中权限
	 * 
	 * @param tree_id
	 * @param role_id
	 * @return boolean
	 */
	public boolean saveTreeDroit(List list, String role_id) {
		if (list == null || list.size() == 0) {
			return false;
		}

		Object[] tree_id = list.toArray();

		int h = tree_id.length;
//		Object[][] tree_id_query = new String[h][2];
		
		//logger.debug(h);
		// 转换成检索SQL
		String[] sql = new String[h];
		PrepareSQL pSQL = new PrepareSQL();

		// 调整数组对象长度为36位；并追加参数
		for (int k = 0; k < h; k++) {
			if (tree_id[k].toString().length() > 36) {
				tree_id[k] = tree_id[k].toString().substring(
						tree_id[k].toString().length() - 36);
			}

//			tree_id_query[k][0] = tree_id[k];
//			tree_id_query[k][1] = role_id;

			// create sql query
			pSQL.setSQL(DROIT_TREE_SAVE);
			pSQL.setString(1, tree_id[k].toString());
			pSQL.setInt(2, Integer.parseInt(role_id));
			sql[k] = pSQL.getSQL();
		}

		// int[] codes = JdbcUtils.batch(DROIT_TREE_SAVE, tree_id_query);
		int[] codes = JdbcUtils.batchUpdate(sql);

		// clear
//		tree_id_query = null;
		sql = null;

		return (codes != null && codes[0] > 0) ? true : false;
	}

	/**
	 * 保存角色功能点权限表中权限
	 * 
	 * @param item_id
	 * @param role_id
	 * @return boolean
	 */
	public boolean saveItemDroit(List list, String role_id) {
		if (list == null || list.size() == 0) {
			return false;
		}		

		Object[] item_id = list.toArray();		

		int h = item_id.length;
		
		logger.debug("saveItemDroit_size:"+h);
//		Object[][] item_id_query = new String[h][2];

		// 转换成检索SQL
		String[] sql = new String[h];
		PrepareSQL pSQL = new PrepareSQL();

		// 调整数组对象长度为36位；并追加参数
		for (int k = 0; k < h; k++) {
			if (item_id[k].toString().length() > 36) {
				item_id[k] = item_id[k].toString().substring(
						item_id[k].toString().length() - 36);
			}

//			item_id_query[k][0] = item_id[k];
//			item_id_query[k][1] = role_id;

			// create sql query
			pSQL.setSQL(DROIT_ITEM_SAVE);
			pSQL.setString(1, item_id[k].toString());
			pSQL.setInt(2, Integer.parseInt(role_id));
			sql[k] = pSQL.getSQL();
		}

		// int[] codes = JdbcUtils.batch(DROIT_ITEM_SAVE, item_id_query);
		//int[] codes = JdbcUtils.batchUpdate(sql);
		int[] codes = DataSetBean.doBatch(sql);

		// clear
//		item_id_query = null;
		sql = null;

		return (codes != null && codes[0] > 0) ? true : false;
	}

	/**
	 * 获取指定角色下功能点列表
	 * 
	 * @param m_RoleId
	 * @return List
	 */
	public List getItemIdListByRoleId(String m_RoleId) {
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(ITEMS_BY_ROLEID);
		pSQL.setInt(1, Integer.parseInt(m_RoleId));
		return JdbcUtils.query(pSQL.getSQL());
	}

	/**
	 * 获取所有角色对象
	 * 
	 * @return List
	 */
	public List getAllRoles() {
		PrepareSQL psql = new PrepareSQL(ROLES_SELECT);
        psql.getSQL();
		return JdbcUtils.query(ROLES_SELECT);
	}
    
    public List getAllRolesExpSelf(long role_id){
    	PrepareSQL psql = new PrepareSQL(ROLES_SELECT_EXP);
        psql.getSQL();
        return JdbcUtils.query(ROLES_SELECT_EXP + role_id);
    }
	/**
     * 快捷菜单保存入口
     * @param itemCollection
     * @param acc_oid
     * @return
	 */
    public boolean saveShortCut(Collection itemCollection,String acc_oid){
        Iterator iterator = itemCollection.iterator();
        String item_id = null;
        PrepareSQL pSQL = new PrepareSQL(INSERT_SHORTCUT);
        ArrayList listSQL = new ArrayList();
        listSQL.add("delete from tab_shortcut_item where acc_oid=" + acc_oid);
        while(iterator.hasNext()){
            item_id = String.valueOf(iterator.next());
            pSQL.setStringExt(1, acc_oid, false);
            pSQL.setString(2,item_id);
            listSQL.add(pSQL.getSQL());
        }
        int[] codes = null;
        if(listSQL.size() > 0){
            codes = DataSetBean.doBatch(listSQL);
        }
        return (codes != null && codes[0] > 0) ? true : false;
    }
    /**
     * 根据acc_oid获取快捷菜单功能点
     * @param acc_oid
     */
    public List getShortCutItemList(long acc_oid){
        List list = new ArrayList();
        Node node = null;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			if(LipossGlobals.isGSDX()) {
				SELECT_SHORTCUT = "select item_id, item_name, item_url " +
						" from tab_item where item_id in(select item_id from tab_shortcut_item where acc_oid=?)";
			}else {
				SELECT_SHORTCUT = "select item_id, item_name, item_icon, item_url, item_data " +
						" from tab_item where item_id in(select item_id from tab_shortcut_item where acc_oid=?)";
			}
		}

        PrepareSQL pSQL = new PrepareSQL(SELECT_SHORTCUT);
        pSQL.setLong(1, acc_oid);
        Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
        Map item = null;
        while((item = cursor.getNext()) != null){
            node = new Node();
            node.setNode_parent_id("" + acc_oid);
            node.setNode_self_id((String) item.get("item_id"));
            node.setNode_text((String) item.get("item_name"));
            node.setNode_icon((String) item.get("item_icon"));
            node.setNode_url((String) item.get("item_url"));
            node.setNode_data((String) item.get("item_data"));
            
            list.add(node);
        }
        return list;
    }
    
    
    /**  begin add by chenjie67371 2011-4-6  **/
    
    public List<String> delItemDroitByRoleId_List(String role_id) {
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(DROIT_ITEM_DELETE);
		pSQL.setInt(1, Integer.parseInt(role_id));

		List<String> result = new ArrayList<String>();
		result.add(pSQL.getSQL());
		return result;
	}
    
    public List<String> delTreeDroitByRoleId_List(String role_id) {
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(DROIT_TREE_DELETE);
		pSQL.setInt(1, Integer.parseInt(role_id));

		List<String> result = new ArrayList<String>();
		result.add(pSQL.getSQL());
		return result;
	}
    
    public List<String> saveItemDroit_List(List list, String role_id) {
		if (list == null || list.size() == 0) {
			return null;
		}		

		Object[] item_id = list.toArray();		

		int h = item_id.length;
		
		logger.debug("saveItemDroit_size:"+h);
//		Object[][] item_id_query = new String[h][2];

		// 转换成检索SQL
		//String[] sql = new String[h];
		List<String> result = new ArrayList<String>();
		PrepareSQL pSQL = new PrepareSQL();

		// 调整数组对象长度为36位；并追加参数
		for (int k = 0; k < h; k++) {
			if (item_id[k].toString().length() > 36) {
				item_id[k] = item_id[k].toString().substring(
						item_id[k].toString().length() - 36);
			}

			// create sql query
			pSQL.setSQL(DROIT_ITEM_SAVE);
			pSQL.setString(1, item_id[k].toString());
			pSQL.setInt(2, Integer.parseInt(role_id));
			result.add(pSQL.getSQL());
		}

		return result;
	}
    
    public List<String> saveTreeDroit_List(List list, String role_id) {
		if (list == null || list.size() == 0) {
			return null;
		}

		Object[] tree_id = list.toArray();

		int h = tree_id.length;
//		Object[][] tree_id_query = new String[h][2];
		
		//logger.debug(h);
		// 转换成检索SQL
		// String[] sql = new String[h];
		List<String> result = new ArrayList<String>();
		PrepareSQL pSQL = new PrepareSQL();

		// 调整数组对象长度为36位；并追加参数
		for (int k = 0; k < h; k++) {
			if (tree_id[k].toString().length() > 36) {
				tree_id[k] = tree_id[k].toString().substring(
						tree_id[k].toString().length() - 36);
			}

			// create sql query
			pSQL.setSQL(DROIT_TREE_SAVE);
			pSQL.setString(1, tree_id[k].toString());
			pSQL.setInt(2, Integer.parseInt(role_id));
			result.add(pSQL.getSQL());
		}

		return result;
	}
    
    /**
     * 获取当前角色的所有的Item
     * @param role_id
     */
    public List<String> getCurrentItemByRoleId(String role_id)
    {
    	String str = "select item_id from " + LipossGlobals.getLipossProperty("Systype");
    	List<String> list = new ArrayList<String>();
    	
    	PrepareSQL pSQL = new PrepareSQL(str);
        //pSQL.setLong(1, acc_oid);
        Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
        Map item = null;
        while((item = cursor.getNext()) != null){
            list.add((String)item.get("item_id"));
        }
        return list;
    }
    
    /**
     * 获取当前角色的所有的Tree
     * @param role_id
     */
    public List<String> getCurrentTreeByRoleId(String role_id)
    {
    	String str = "select tree_id from tab_tree_role" ;
    	List<String> list = new ArrayList<String>();
    	
    	PrepareSQL pSQL = new PrepareSQL(str);
        //pSQL.setLong(1, acc_oid);
        Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
        Map item = null;
        while((item = cursor.getNext()) != null){
            list.add((String)item.get("tree_id"));
        }
        return list;
    }
    
    /**
     * 比较当前的Item和用户编辑以后的Item有什么区别，获取减少的部分，也可以用来比较tree
     * @return
     */
    public List<String> getReduceItemTree(List<String> old, List<String> current)
    {
    	List<String> list = new ArrayList<String>();
    	for(int i = 0; i < old.size(); i++)
    	{
    		String old_id = old.get(i);
    		// 编辑后不包含的tree
    		if(!current.contains(old_id))
    		{
    			list.add(old_id);
    		}
    	}
    	return list;
    }
    
    /**
     * 获取某个角色下属的所有的子角色（包括子子角色）
     * @param rold_id
     * @return
     */
    public List<String> getAllChildRoleIds(String rold_id)
    {
    	// 第一层
		List<String> childList = getChildRoleIds(rold_id);
		
		List<String> result = new ArrayList<String>();
		String temp = null;
		 
		// 需要递归得到所有的子节点和子子节点
		for(int i=0; i<childList.size(); i++)
		{
			temp = childList.get(i);
			result.add(temp);
			
			// 如果没有子子节点了
			//List<String> childList_ = getChildRoleIds(temp);
			if(getChildRoleIds(temp).size() == 0)
			{
				//
			}
			else
			{
				result.addAll(getAllChildRoleIds(temp));
			}
		}
		
		return result;
    }
    
    /**
     * 获取某个角色下的所有的子角色（不包括子子角色）
     * @return
     */
    public List<String> getChildRoleIds(String role_id)
    {
    	String str = "select role_id from tab_role where role_pid=" + role_id;
    	List<String> list = new ArrayList<String>();
    		
    	PrepareSQL pSQL = new PrepareSQL(str);
        Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
        Map item = null;
        while((item = cursor.getNext()) != null){
        	list.add((String)item.get("role_id"));
        }
        return list;
    }
    
    /**
     * 更新角色的树
     * 
     * @param role_id  角色ID
     * @param itemList item更新集合
     * @param treeList tree更新集合
     * @return
     */
    public boolean updateRoleItemAndTree(String role_id, List itemList, List treeList)
    {
    	// 最终sql集合
    	ArrayList listSql = new ArrayList();
    	
    	// 获取在库中的Tree
    	List<String> oldItem = getCurrentItemByRoleId(role_id);
    	
    	// 获取在库中的Item
    	List<String> oldTree = getCurrentTreeByRoleId(role_id);
    	
    	// 当前的item和tree 与 库中的进行比较
    	List<String> reduceItemList = getReduceItemTree(oldItem, itemList);
    	List<String> reduceTreeList = getReduceItemTree(oldTree, treeList);
    	
    	// 如果有减少的内容，对子子节点进行操作
    	if(reduceItemList.size() != 0 || reduceTreeList.size() != 0 )
    	{
    		listSql.addAll(dealChildRole(reduceItemList, reduceTreeList, role_id));
    	}
    	
		// 如果没有减少的内容，不需要对子角色进行任何操作
		listSql.addAll(delItemDroitByRoleId_List(role_id));
		listSql.addAll(delTreeDroitByRoleId_List(role_id));
		
		listSql.addAll(saveItemDroit_List(itemList,role_id));
		listSql.addAll(saveTreeDroit_List(treeList,role_id));
    	
    	int[] codes = DataSetBean.doBatch(listSql);
		return (codes != null && codes[0] > 0) ? true : false;
    }
    
    /**
     * 更新子角色内容,子角色相应减少菜单
     * @param reduceItemList
     * @param reduceTreeList
     */
    public List<String> dealChildRole(List<String> reduceItemList, List<String> reduceTreeList, String role_id)
    {
    	List<String> list = new ArrayList<String>();
    	
    	// 获取子子节点
    	List<String> childList = getAllChildRoleIds(role_id);
    	
    	// 没有子节点，不需要操作
    	if(childList.size() == 0)
    	{
    		return list;
    	}
    	
    	String sql = "";
    	for(int i = 0; i < childList.size(); i++)
    	{
    		for(int j = 0; j < reduceItemList.size(); j++)
    		{
    			sql = "delete from " + LipossGlobals.getLipossProperty("Systype") + " where item_id='" + reduceItemList.get(j) + "' and role_id=" + childList.get(i);
    			list.add(sql);
    		}
    		
    		for(int k = 0; k < reduceTreeList.size(); k++)
    		{
    			sql = "delete from tab_tree_role" + " where tree_id='" + reduceTreeList.get(k) + "' and role_id=" + childList.get(i);
    			list.add(sql);
    		}
    	}
    	return list;
    }
    
    /**  end add by chenjie67371 2011-4-6  **/

    /**
     * add by qixueqi 2011-4-8
     */
    public boolean hasUserByRole(String roleId){
    	logger.debug("hasUserByRole({})",roleId);
    	String sql = "select count(1) from tab_role where role_pid=" + roleId;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select count(*) from tab_role where role_pid=" + roleId;
		}
    	PrepareSQL pSQL = new PrepareSQL(sql);
        Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
        if(null!=cursor && cursor.getRecordSize()>0){
        	return true;
        }else{
        	return false;
        }
    }
    
    /**
     * add by qixueqi 2011-4-8
     */
    public boolean hasChildByRole(String roleId){
    	logger.debug("hasUserByRole({})",roleId);
		String sql = "select count(1) as num from tab_role where role_pid=" + roleId;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select count(*) as num from tab_role where role_pid=" + roleId;
		}

    	PrepareSQL pSQL = new PrepareSQL(sql);
        Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
        Map item = null;
        int _num = 0 ;
        while((item = cursor.getNext()) != null){
        	_num = Integer.parseInt(item.get("num").toString());
        }
        if(_num>0){
        	return true;
        }else{
        	return false;
        }
    }
    
}
