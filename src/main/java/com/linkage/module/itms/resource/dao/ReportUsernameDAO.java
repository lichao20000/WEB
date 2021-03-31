
package com.linkage.module.itms.resource.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-11-28 下午04:54:27
 * @category com.linkage.module.itms.resource.dao
 * @copyright 南京联创科技 网管科技部
 */
public class ReportUsernameDAO extends SuperDAO
{

	public List<Map> list=new ArrayList<Map>();
	public List<Map> queryUsername( String idStr,String deviceSnStr)
	{
		
		String sql = "select * from dev_inform_user  where "
				+ "  device_id='"+idStr+"'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		list=jt.queryForList(sql);
		if(list.size()>0){
			list.get(0).put("device_serialnumber", deviceSnStr);
		}
		return list;
	}

	
}
