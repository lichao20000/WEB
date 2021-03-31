
package com.linkage.module.gtms.itv.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.litms.common.database.PrepareSQL;
import org.apache.log4j.Logger;

import com.linkage.module.gtms.itv.dto.Module;
import com.linkage.module.gtms.itv.dto.NodeInfo;
import com.linkage.module.gtms.itv.dto.SystemTree;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 菜单管理数据库接口
 *
 * @author 陈仲民(5243)
 * @version 1.0
 * @since 2009-12-22
 * @category com.linkage.module.fba.menu.dao<br>
 *           版权：南京联创科技 网管科技部
 */
public class MenuManagerDAOImp extends SuperDAO implements MenuManagerDAO
{

	/**
	 * 记日志
	 */
	private static Logger log = Logger.getLogger(MenuManagerDAOImp.class);

	/**
	 * 角色和模块的对应关系
	 */
	private Map<String, List<Module>> roleModule = new HashMap<String, List<Module>>();

	/**
	 * 角色和节点的对应关系
	 */
	private Map<String, SystemTree> roleNode = new HashMap<String, SystemTree>();

	/*
	 * (non-Javadoc)
	 *
	 * @see com.linkage.module.fba.menu.dao.MenuManagerDAO#initAll()
	 */
	public void initAll()
	{
		// 所有角色
		List<String> roleList = getAllRole();
		for (String roleId : roleList)
		{
			initModule(roleId);

			// 处理所有模块的菜单信息
			List<Module> tmpList = roleModule.get(roleId);
			for (Module module : tmpList)
			{
				initNode(roleId, module.getModuleId(), module.getModuleName());
			}
		}
	}

	/**
	 * 查询所有的角色信息
	 *
	 * @return
	 */
	private List<String> getAllRole()
	{
		// 查询所有角色
		String sql = "select role_id from tab_role";
		PrepareSQL psql = new PrepareSQL(sql);
		List list = jt.queryForList(psql.getSQL());

		// 生成返回角色信息
		List<String> roleList = new ArrayList<String>();
		if (list != null && list.size() > 0)
		{
			int len = list.size();
			for (int i = 0; i < len; i++)
			{
				Map map = (Map) list.get(i);
				if (map != null)
				{
					roleList.add(String.valueOf(map.get("role_id")));
				}
			}
		}
		return roleList;
	}

	/**
	 * 初始化指定角色的模块列表
	 *
	 * @param roleId
	 */
	private void initModule(String roleId)
	{
		// 查询角色所属的模块
		String sql = "select module_id,module_name,module_url from tab_module where module_id in(select tree_id from tab_tree_role where role_id="
				+ roleId + ") order by sequence";
		log.info("查询模块列表==" + sql);
		List list = jt.queryForList(sql);

		// 生成模块数据列表
		List<Module> moduleList = new ArrayList<Module>();
		if (list != null && list.size() > 0)
		{
			int len = list.size();
			for (int i = 0; i < len; i++)
			{
				Map map = (Map) list.get(i);

				Module module = new Module();
				module.setModuleId(String.valueOf(map.get("module_id")));
				module.setModuleName(String.valueOf(map.get("module_name")));
				module.setModuleUrl(String.valueOf(map.get("module_url")));
				moduleList.add(module);
			}
		}

		roleModule.put(roleId, moduleList);
	}

	/**
	 * 初始化指定角色的节点列表
	 *
	 * @param roleId
	 */
	private void initNode(String roleId, String moduleId, String moduleName)
	{
		SystemTree tree = new SystemTree();
		tree.setNodelList(getALlSubNode(roleId, moduleId, 0));
		tree.setModuleName(moduleName);
		tree.setModuleId(moduleId);

		roleNode.put(roleId + "_" + moduleId, tree);
	}

	/**
	 * 获取模块所有下级的节点信息
	 *
	 * @param roleId
	 *            角色
	 * @param treeId
	 *            模块编号
	 * @return
	 */
	private List<NodeInfo> getALlSubNode(String roleId, String treeId, int layer)
	{
		List<NodeInfo> nodeList = new ArrayList<NodeInfo>();

		// 首先查询目录
		String sql = "select tree_id,tree_name from tab_system_tree where tree_parent_id='"
				+ treeId
				+ "' and tree_id in (select tree_id from tab_tree_role where role_id="
				+ roleId + ") order by sequence";
		log.info("查询目录==" + sql);
		List treeList = jt.queryForList(sql);

		// 遍历目录，再继续读取下级菜单
		if (treeList != null && treeList.size() > 0)
		{
			int len = treeList.size();
			for (int i = 0; i < len; i++)
			{
				Map treeMap = (Map) treeList.get(i);
				if (treeMap != null)
				{
					NodeInfo node = new NodeInfo();
					node.setNodeId(String.valueOf(treeMap.get("tree_id")));
					node.setNodeParentId(treeId);
					node.setLayer(String.valueOf(layer));
					node.setNodeName(String.valueOf(treeMap.get("tree_name")));
					node.setIsLeaf("false");
					nodeList.add(node);

					log.info("========" + String.valueOf(treeMap.get("tree_name")));

					// 下级节点
					int tmp = layer+1;
					nodeList.addAll(getALlSubNode(roleId, String.valueOf(treeMap
							.get("tree_id")), tmp));
				}
			}
		}

		// 其次查询菜单节点 // TODO wait (more table related)
		sql = "select item_id,item_name,item_url from tab_item "
				+ " where item_id in (select item_id from tab_tree_item where tree_id='"
				+ treeId
				+ "') and item_id in (select item_id from tab_item_role where role_id="
				+ roleId + ")  order by item_desc";
		log.info("查询菜单节点==" + sql);
		List itemList = jt.queryForList(sql);

		// 遍历节点
		if (itemList != null && itemList.size() > 0)
		{
			int len = itemList.size();
			for (int i = 0; i < len; i++)
			{
				Map itemMap = (Map) itemList.get(i);
				if (itemMap != null)
				{
					NodeInfo node = new NodeInfo();
					node.setNodeId(String.valueOf(itemMap.get("item_id")));
					node.setNodeParentId(treeId);
					node.setLayer(String.valueOf(layer));
					node.setNodeName(String.valueOf(itemMap.get("item_name")));
					node.setIsLeaf("true");
					node.setUrlParam(String.valueOf(itemMap.get("item_url")));
					nodeList.add(node);
				}
			}
		}

		return nodeList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.linkage.module.fba.menu.dao.MenuManagerDAO#getModuleList(long)
	 */
	public List<Module> getModuleList(long roleId)
	{
		List<Module> list = roleModule.get(String.valueOf(roleId));

		// 如果模块列表为空，则重新查询一次
		if (list == null)
		{
			initModule(String.valueOf(roleId));
			list = roleModule.get(String.valueOf(roleId));
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.linkage.module.fba.menu.dao.MenuManagerDAO#getSystemTree(long,
	 *      java.lang.String, java.lang.String)
	 */
	public SystemTree getSystemTree(long roleId, String moduleId, String moduleName)
	{
		/**
		 * 注释此段代码，是为了在角色关联菜单之后不需要重启服务即可访问
		 */
//		SystemTree tree = roleNode.get(String.valueOf(roleId) + "_" + moduleId);
		SystemTree tree = null;

		// 如果菜单信息为空，则重新查询一次
		if (tree == null)
		{
			initNode(String.valueOf(roleId), moduleId, moduleName);
			tree = roleNode.get(String.valueOf(roleId) + "_" + moduleId);
		}

		return tree;
	}
}
