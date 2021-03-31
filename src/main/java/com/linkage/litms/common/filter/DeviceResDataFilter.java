package com.linkage.litms.common.filter;

import java.util.List;
import java.util.Map;


import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.core.DataFilter;
import com.linkage.litms.system.UserRes;

public class DeviceResDataFilter implements DataFilter {

	// 需要过滤的数据游标
	private Cursor cursor = null;

	// 过滤匹配字段名称
	private String fieldname = null;

	/**
	 * 
	 * @param _cursor
	 * @param _fieldname
	 */
	public DeviceResDataFilter(Cursor _cursor, String _fieldname) {
		this.cursor = _cursor;
		this.fieldname = _fieldname;
	}

	public Object doFilter(UserRes userRes) {
		Cursor result = new Cursor();
		
//		m_logger.debug("cursor = "+cursor.getRecordSize());
		Map fields = cursor.getNext();
		String tmp;
		List list = userRes.getUserDevRes(userRes.getUser());
//		m_logger.debug("UserDevRes = "+list.size());
		while (fields != null) {
			tmp = (String) fields.get(fieldname.toLowerCase());
			if (list.contains(tmp)) {
//				m_logger.debug(fields);
				result.add(fields);
			}
			
			fields = cursor.getNext();
		}
		
		//clear
		
		
		return result;
	}

}
