
package com.linkage.module.gtms.itv.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.litms.common.database.PrepareSQL;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.module.gtms.itv.dto.MenuItem;

/**
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-07-30
 * @category 菜单初始化类，用来在服务器刚刚启动的时候进行装载
 */
public class MenuInit
{

	private DataSource dao;// 数据源
	private Map<String, ArrayList<MenuItem>> allRoleMenu = new HashMap<String, ArrayList<MenuItem>>();// 全部角色的菜单
	private ArrayList<MenuItem> allMenu = new ArrayList<MenuItem>();// 全部菜单的集合
	private boolean init = true;// 是否第一次初始化
	private JdbcTemplate jt;// spring的jdbc模版类

	/**
	 * 用户菜单初始化方法，提取tab_module的记录作为树的根，只有在服务器启动后，首个访问web的用户进行初始化，或者调用reloadMenu方法才会导致该方法被调用
	 *
	 * @see #reloadMenu()
	 */
	synchronized private void init()
	{
		if (!init)
		{
			return;
		}
		jt = new JdbcTemplate(dao);
		String getMoudle = "select module_id,module_name,module_name_en from tab_module order by sequence";
		PrepareSQL psql = new PrepareSQL(getMoudle);
		List<Map<String, String>> module = jt.queryForList(psql.getSQL());
		for (Map<String, String> map : module)
		{
			int layer = 0;
			MenuItem item = new MenuItem();
			item.setId(map.get("module_id"));
			item.setName(map.get("module_name"));
			item.setName_en(map.get("module_name_en"));
			item.setLayer(layer);
			item.setIsleaf(false);
			item.setPid("0");
			item.setUrl(null);
			allMenu.add(item);
			getItem(map.get("module_id"), layer);
		}
		init = false;
		initRoleMenu();
	}

	/**
	 * 插入数据源
	 *
	 * @param dao
	 *            数据源
	 */
	public void setDao(DataSource dao)
	{
		this.dao = dao;
	}

	/**
	 * 根据用户的角色id来获取对应的菜单项
	 *
	 * @param role_id
	 *            角色id
	 * @return 用户的列表list，里面存放的MenuItem的对象
	 */
	public ArrayList<MenuItem> getMenuByRole(String role_id)
	{
		if (init)
		{
			init();
		}
		return allRoleMenu.get(role_id);
	}

	/**
	 * 重新装载菜单<br>
	 * <b>该方法提供给在菜单管理模块中修改了部分菜单配置，需要对内存中的菜单重新装载使用，只有在需要的时候重新装载，该方法会导致内存中用户菜单map更新
	 */
	public void reloadMenu()
	{
		allRoleMenu.clear();
		allMenu.clear();
		init = true;
		init();
	}

	/**
	 * 初始化各个角色的菜单列表
	 */
	private void initRoleMenu()
	{
		List<Map> list = jt.queryForList("select role_id from tab_role");
		for (Map map : list)
		{
			int role_id = Integer.parseInt(map.get("role_id").toString());
			PrepareSQL psql = new PrepareSQL("select tree_id from tab_tree_role where role_id=" + role_id);
			List<Map<String, String>> tree_role = jt.queryForList(psql.getSQL());
			PrepareSQL psql1 = new PrepareSQL("select item_id from tab_item_role where role_id=" + role_id);
			List<Map<String, String>> item_role = jt.queryForList(psql1.getSQL());
			ArrayList<MenuItem> roleMenu = new ArrayList<MenuItem>();
			for (MenuItem temp : allMenu)
			{
				Map<String, String> tree = new HashMap<String, String>();
				tree.put("tree_id", temp.getId());
				Map<String, String> item = new HashMap<String, String>();
				item.put("item_id", temp.getId());
				if (tree_role.contains(tree) || item_role.contains(item))
				{
					roleMenu.add(temp);
				}
			}
			allRoleMenu.put(map.get("role_id").toString(), roleMenu);
		}
	}

	/**
	 * 迭代树的方法，内部使用，将所有的功能点和树机型迭代展示
	 *
	 * @param tree_id
	 * @param layer
	 */
	private void getItem(String tree_id, int layer)
	{
		jt = new JdbcTemplate(dao);
		String getItem = "select a.item_id as id,a.item_name as name,a.item_name_en as name_en ,a.item_url as url ,a.item_desc from tab_item a,tab_tree_item b where b.tree_id='"
				+ tree_id + "' and a.item_id=b.item_id order by item_desc";
		PrepareSQL psql1 = new PrepareSQL(getItem);
		List<Map<String, String>> subitem = jt.queryForList(psql1.getSQL());
		++layer;
		for (Map<String, String> map : subitem)
		{
			MenuItem item = new MenuItem();
			item.setId(map.get("id"));
			item.setName(map.get("name"));
			item.setName_en(map.get("name_en"));
			item.setUrl(map.get("url"));
			item.setLayer(layer);
			item.setIsleaf(true);
			item.setPid(tree_id);
			allMenu.add(item);
		}
		String getSubTree = "select tree_id as id ,tree_name as name,tree_name_en as name_en from tab_system_tree where tree_parent_id ='"
				+ tree_id + "'";
		PrepareSQL psql2 = new PrepareSQL(getSubTree);
		List<Map<String, String>> subtree = jt.queryForList(psql2.getSQL());
		for (Map<String, String> map : subtree)
		{
			MenuItem item = new MenuItem();
			item.setId(map.get("id"));
			item.setName(map.get("name"));
			item.setName_en(map.get("name_en"));
			item.setUrl(null);
			item.setLayer(layer);
			item.setIsleaf(false);
			item.setPid(tree_id);
			allMenu.add(item);
			getItem(map.get("id"), layer);
		}
	}

	/**
	 * 查询导航条信息
	 *
	 * @param menuId
	 * @return
	 */
	public String initBar(String menuId)
	{
		String str = "";

		if (jt == null){
			jt = new JdbcTemplate(dao);
		}

		// 查询节点名称
		String sql = "select item_name from tab_item where item_id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		Map map = jt.queryForMap(psql.getSQL(), new Object[] { menuId },
				new int[] { java.sql.Types.VARCHAR });
		if (map != null)
		{
			str = String.valueOf(map.get("item_name"));
		}

		// 查询节点的父目录
		sql = "select tree_id from tab_tree_item where item_id=?";
		PrepareSQL psql1 = new PrepareSQL(sql);
		List list = jt.queryForList(psql1.getSQL(), new Object[] { menuId },
				new int[] { java.sql.Types.VARCHAR });
		if (list != null && list.size() > 0)
		{
			// 正常情况下只会有一条记录
			Map treeMap = (Map) list.get(0);
			if (treeMap != null)
			{
				// 拼装父目录信息
				str = getTreeInfo(String.valueOf(treeMap.get("tree_id"))) + str;
			}
		}

		return str;
	}

	/**
	 * 根据目录编号查询导航信息
	 *
	 * @param tree_id
	 *            目录编号
	 * @return 导航信息
	 */
	private String getTreeInfo(String tree_id)
	{
		String tree_name = "";
		String tree_parent_id = "";
		// 查询目录信息
		String sql = "select tree_name,tree_parent_id from tab_system_tree where tree_id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		List list = jt.queryForList(psql.getSQL(), new Object[] { tree_id },
				new int[] { java.sql.Types.VARCHAR });

		if (list != null && list.size() > 0)
		{
			// 正常情况下只会有一条记录
			Map map = (Map) list.get(0);

			tree_name = String.valueOf(map.get("tree_name"));
			tree_parent_id = String.valueOf(map.get("tree_parent_id"));

			// 存在父目录时，调用方法继续查询
			if (tree_parent_id != null && !"".equals(tree_parent_id)
					&& !"0".equals(tree_parent_id))
			{
				return getTreeInfo(tree_parent_id) + tree_name + ">>";
			}
			else
			{
				return tree_name + ">>";
			}
		}
		else
		{
			return "";
		}
	}
}
