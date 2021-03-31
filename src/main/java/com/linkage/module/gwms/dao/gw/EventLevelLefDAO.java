
package com.linkage.module.gwms.dao.gw;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

public class EventLevelLefDAO extends SuperDAO
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(EventLevelLefDAO.class);

	/**
	 * 获得告警信息的map
	 * 
	 * @param
	 * @author wangsenbo
	 * @date 2009-10-26
	 * @return Map 没有数据返回null
	 */
	public Map getWarnLevel()
	{
		logger.debug("getWarnLevel()");
		Map warnMap = null;
		String sql;
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql = "select event_level_id,event_level_name from event_level_def";
		}else{
			sql = "select * from event_level_def";
		}
		
		PrepareSQL psql = new PrepareSQL(sql);
		logger.debug(psql.getSQL());
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		Map fields = cursor.getNext();
		while (fields != null)
		{
			if (warnMap == null)
			{
				warnMap = new HashMap<String, String>();
			}
			String event_level_id = StringUtil.getStringValue(fields
					.get("event_level_id"));
			String event_level_name = StringUtil.getStringValue(fields
					.get("event_level_name"));
			warnMap.put(event_level_id, event_level_name);
			fields = cursor.getNext();
		}
		cursor = null;
		return warnMap;
	}
}
