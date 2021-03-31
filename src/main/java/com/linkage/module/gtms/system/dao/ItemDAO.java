
package com.linkage.module.gtms.system.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 菜单数据表操作类
 * 
 * @author Jason(3412)
 * @date 2010-12-6
 */
public class ItemDAO extends SuperDAO
{

	/**
	 * 列表可见功能点
	 */
	private String itemSql = "select item_id,item_name from tab_item where item_visual='1' order by sequence";

	/**
	 * 获得功能点列表
	 * 
	 * @return List
	 */
	public List getItemList()
	{
		List itmeIdNameList = jt.queryForList(new PrepareSQL(itemSql).getSQL());
		return itmeIdNameList;
	}

	/**
	 * 获得功能点Map
	 * 
	 * @return List
	 */
	public Map<String, String> getItemIdNameMap()
	{
		Map<String, String> itemIdNameMap = new HashMap<String, String>();
		List itemList = getItemList();
		if (null != itemList)
		{
			for (Object obj : itemList)
			{
				Map tmpMap = (Map) obj;
				itemIdNameMap.put(StringUtil.getStringValue(tmpMap.get("item_id")),
						StringUtil.getStringValue(tmpMap.get("item_name")));
			}
		}
		return itemIdNameMap;
	}
	
	public List<String> getItemByRole(long roleId)
	{
		PrepareSQL sql = new PrepareSQL("select item_id from tab_item_role where role_id="+roleId);
		List itmeIdNameList = jt.queryForList(sql.getSQL());
		return itmeIdNameList;
	}
}
