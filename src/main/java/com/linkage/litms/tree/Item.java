package com.linkage.litms.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.JdbcUtils;
import com.linkage.litms.common.util.RandomGUID;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;

public class Item {
	private static Logger m_logger = LoggerFactory.getLogger(Item.class);
	
	/**
	 * 首先当item_sequence = 0为零时，从数据库读取最大值；若item_sequence != 0，则向下自增1
	 */
	private static long item_sequence = 0;

	private static long tree_item_sequence = 0;

	private static PrepareSQL pSQL = null;

	/**
	 * 列表可见功能点
	 */
	private String ITEM_LIST = "select item_id,item_name from tab_item where item_visual='1' order by sequence";

	/**
	 * 获得指定系统目录下节点
	 */
	private String ITEM_SELECTED = "select item_id from tab_tree_item where tree_id=?  order by sequence";

	/**
	 * 删除指定系统目录下功能节点
	 */
	private String TREE_ITEM_DELETE = "delete from tab_tree_item where tree_id=?";

	/**
	 * 功能点增加
	 */
	private String ITEM_ADD = "insert into tab_item (sequence,item_id,item_name,item_url,item_desc,item_visual) values (?,?,?,?,?,?)";
	/**
     *添加功能点时，关联角色 
	 */
//  private String ITEM_ADD_ROLE = "insert into tab_item_role(item_id,role_id) values(?,?)";
	private String ITEM_ADD_ROLE = "insert into " + LipossGlobals.getLipossProperty("Systype") + "(item_id,role_id) values(?,?)";
	/**
	 * 功能点删除
	 */
	private String ITEM_DELETE = "delete from tab_item where item_id=?";
    /**
     * 功能点删除 角色关联删除
     */
//  private String ITEM_DELETE_ROLE = "delete from tab_item_role where item_id=?";
	private String ITEM_DELETE_ROLE = "delete from " + LipossGlobals.getLipossProperty("Systype") + " where item_id=?";
	/**
	 * 修改功能点
	 */
	private String ITEM_UPDATE = "update tab_item set item_name=?,item_url=?,item_desc=?,item_visual=? where item_id=?";

	/**
	 * 批量增加系统目录下关联功能点
	 */
	private String TREE_ITEMS_SAVE = "insert into tab_tree_item (sequence,tree_id,item_id) values (?,?,?)";

	/**
	 * 按照名称检索功能节点
	 */
	private String ITEM_QUERY_BY_ITEMNAME = "select * from tab_item where item_name like ? and item_visual='1' order by sequence";

	/**
	 * 根据功能节点编号获得功能节点详细信息
	 */
	private String ITEM_INFO_BY_ITEMID = "select * from tab_item where item_id=?";

	/**
	 * 剔出已经被选中功能节点以外的功能节点
	 */
	private String ITEMID_OUTOF_SELECTED = "select item_id from tab_item where item_id not in (select distinct item_id from tab_tree_item) order by sequence";

	public Item() {
		if (item_sequence == 0) {
			item_sequence = DataSetBean.getMaxId("tab_item", "sequence");
		}

		if (pSQL == null) {
			pSQL = new PrepareSQL();
		}
	}

	/**
	 * 剔出已经被选中功能节点以外的功能节点
	 * 
	 * @return List
	 */
	public List getOutOfItemSelected() {
		PrepareSQL psql = new PrepareSQL(ITEMID_OUTOF_SELECTED);
        psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(ITEMID_OUTOF_SELECTED);
		Map field = cursor.getNext();
		List list = new ArrayList();

		while (field != null) {
			list.add(field.get("item_id"));
			field = cursor.getNext();
		}

		// clear
		field = null;
		cursor = null;

		return list;
	}

	/**
	 * 根据功能节点编号获得功能节点详细信息
	 * 
	 * @param item_id
	 * @return Map
	 */
	public Map getItemInfoByItemId(String item_id) {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			ITEM_INFO_BY_ITEMID = "select item_name, item_url, item_id from tab_item where item_id=?";
		}
		pSQL.setSQL(ITEM_INFO_BY_ITEMID);
		pSQL.setString(1, item_id);
		Map field = DataSetBean.getRecord(pSQL.getSQL());

		if (field != null) {
			// clear

			return field;
		}

		return null;
	}

	/**
	 * 按照名称检索功能节点
	 * 
	 * @param item_name
	 * @return List
	 */
	public List getItemListByItemName(String item_name) {
		PrepareSQL psql = new PrepareSQL(ITEM_QUERY_BY_ITEMNAME);
        psql.getSQL();
		return JdbcUtils.query(ITEM_QUERY_BY_ITEMNAME, "%" + item_name + "%");
	}

	/**
	 * 获得功能点列表
	 * 
	 * @return List
	 */
	public List getItemList() {
		PrepareSQL psql = new PrepareSQL(ITEM_LIST);
        psql.getSQL();
		return JdbcUtils.query(ITEM_LIST);
	}

	/**
	 * 获得指定系统目录下节点列表
	 * 
	 * @param tree_id
	 * @return List
	 */
	public List getItemListByTreeId(String tree_id) {
		List ITEM_LIST = new ArrayList();
		pSQL.setSQL(ITEM_SELECTED);
		pSQL.setString(1, tree_id);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map field = cursor.getNext();
		while (field != null) {
			ITEM_LIST.add(field.get("item_id"));

			field = cursor.getNext();
		}

		//clear
		field = null;
		cursor = null;
		
		return ITEM_LIST;
	}

	/**
	 * 新增功能点
	 * 
	 * @param request
	 * @return boolean
	 */
	public boolean itemAdd(HttpServletRequest request) {
		String item_id = new RandomGUID().toString();
		String item_name = request.getParameter("item_name");
		String item_url = request.getParameter("item_url");
		String item_desc = request.getParameter("item_desc");
		String item_visual = request.getParameter("item_visual");
        HttpSession session = request.getSession();
        UserRes curUser = (UserRes)session.getAttribute("curUser");
        User user = curUser.getUser();
        long role_id = user.getRoleId();
		m_logger.debug(item_desc);

		// int codes = JdbcUtils.update(ITEM_ADD, new String[] {
		// String.valueOf(++item_sequence), item_id, item_name, item_url,
		// item_desc, item_visual });
		/*
		int codes = JdbcUtils.executeUpdate(ITEM_ADD, new String[][] {
				{ String.valueOf(++item_sequence), "int" },
				{ item_id, "string" }, { item_name, "string" },
				{ item_url, "string" }, { item_desc, "string" },
				{ item_visual, "string" } });
		
        codes = JdbcUtils.executeUpdate(ITEM_ADD_ROLE, new String[][] {
                { item_id, "string" }, { item_name, "string" },
                { "" + role_id, "int" } });
        */
        String[] sqlArr = new String[2];
        PrepareSQL pSQL = new PrepareSQL();
        pSQL.setSQL(ITEM_ADD);
        pSQL.setLong(1, ++item_sequence);
        pSQL.setString(2, item_id);
        pSQL.setString(3,item_name);
        pSQL.setString(4,item_url);
        pSQL.setString(5,item_desc);
        pSQL.setString(6,item_visual);
        sqlArr[0] = pSQL.getSQL();
        
        pSQL.setSQL(ITEM_ADD_ROLE);
        pSQL.setString(1, item_id);
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
	 * 删除功能点
	 * 
	 * @param request
	 * @return boolean
	 */
	public boolean itemDelete(HttpServletRequest request) {
		boolean flag = false;

		String item_id = request.getParameter("item_id");
		PrepareSQL psql = new PrepareSQL(ITEM_DELETE);
        psql.getSQL();
		int codes = JdbcUtils.update(ITEM_DELETE, item_id);
		psql = new PrepareSQL(ITEM_DELETE_ROLE);
        psql.getSQL();
        codes = JdbcUtils.update(ITEM_DELETE_ROLE, item_id);
		if (codes > 0) {
			flag = true;
		}

		return flag;
	}

	/**
	 * 更新功能点
	 * 
	 * @param request
	 * @return boolean
	 */
	public boolean itemUpdate(HttpServletRequest request) {
		boolean flag = false;

		String item_id = request.getParameter("item_id");
		String item_name = request.getParameter("item_name");
		String item_url = request.getParameter("item_url");
		String item_desc = request.getParameter("item_desc");
		String item_visual = request.getParameter("item_visual");
		PrepareSQL psql = new PrepareSQL(ITEM_UPDATE);
        psql.getSQL();
		int codes = JdbcUtils.update(ITEM_UPDATE, new String[] { item_name,
				item_url, item_desc, item_visual, item_id });

		if (codes > 0) {
			flag = true;
		}

		return flag;
	}

	/**
	 * 批量保存系统目录节点关联功能点
	 * 
	 * @param tree_id
	 * @param item_id
	 * @return boolean
	 */
	public boolean saveItems(String tree_id, String[] item_id) {
		if (item_id == null) {
			return true;
		}

		int h = item_id.length;
		String[][][] item = new String[h][3][2];

		for (int k = 0; k < h; k++) {
			item[k][0][0] = String.valueOf(++tree_item_sequence);
			item[k][0][1] = "int";

			item[k][1][0] = tree_id;
			item[k][1][1] = "String";

			item[k][2][0] = item_id[k];
			item[k][2][1] = "String";
		}
		PrepareSQL psql = new PrepareSQL(TREE_ITEMS_SAVE);
        psql.getSQL();
		int[] codes = JdbcUtils.batch(TREE_ITEMS_SAVE, item);

		return (codes != null) ? true : false;
	}
	
	
	/**
	 * 新增私有报表功能点 by liuwei 2007-4-27
	 * 
	 * @param request
	 * @return boolean
	 */
	public String reportItemAdd(String itemName, String itemUrl,
					String itemDesc, String itemVisual,long role_id)
	{
		boolean flag = false;
		String itemId = "";
		String item_id = new RandomGUID().toString();
		String item_name = itemName;
		String item_url = itemUrl;
		String item_desc = itemDesc;
		String item_visual = itemVisual;		

		String itemSeq = String.valueOf(++item_sequence);

		m_logger.debug(item_desc);
		
		ArrayList sqlList = new ArrayList();
		PrepareSQL pSQL = new PrepareSQL(ITEM_ADD);
		pSQL.setStringExt(1,itemSeq,false);
		pSQL.setStringExt(2,item_id,true);
		pSQL.setStringExt(3,item_name,true);
		pSQL.setStringExt(4,item_url,true);
		pSQL.setStringExt(5,item_desc,true);
		pSQL.setStringExt(6,item_visual,true);
		
		sqlList.add(pSQL.getSQL());
		
		pSQL.setSQL(ITEM_ADD_ROLE);
		pSQL.setString(1,item_id);
		pSQL.setLong(2,role_id);
		
		sqlList.add(pSQL.getSQL());
		
		int[] codes = DataSetBean.doBatch(sqlList);
		
		if(null!=codes&&codes.length>0)
		{
			itemId=item_id;
		}	
		return itemId;
	}

	/**
	 * 删除指定系统目录下功能节点
	 * 
	 * @param tree_id
	 * @return boolean
	 */
	public boolean delItems(String tree_id) {
		PrepareSQL psql = new PrepareSQL(TREE_ITEM_DELETE);
        psql.getSQL();
		return JdbcUtils.update(TREE_ITEM_DELETE, tree_id) > 0 ? true : false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
