/**
 * @(#)HostDayData.java 2006-7-25
 * 
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.host;

import java.util.HashMap;
import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

/**
 * 江苏移动主机作业维护.
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 * @date 2006-7-25
 * 
 */
public class HostDayData {
	
	private String sqlSlmc = "select * from attribute_slmc where dxbh=? and sxbh=?";
	
	private String sqlDayData = "select dxbh,sxbh,slbh,slmc,csz,cysj from attribute_data_?"
			+ " where dxbh=? and sxbh=? and slbh=?"
			+ " and cysj>=?  and cysj<?";
	
	/**PrepareSQL*/
	private PrepareSQL pSQL = null;
	
	
	/**
	 * Constrator
	 */
	public HostDayData() {
		super();
		pSQL = new PrepareSQL();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 获取一对象的某一属性的实例列表.
	 * 
	 * @desc 江苏移动主机作业维护.
	 * 
	 * @param dxbh 对象编号（设备id）
	 * @param sxbh 属性编号.
	 * @return
	 */
	public Cursor getSlmc(String dxbh, String sxbh) {
		pSQL.setSQL(sqlSlmc);
		pSQL.setStringExt(1, dxbh, false);
		pSQL.setStringExt(2, sxbh, false);
		
		return DataSetBean.getCursor(pSQL.getSQL());
	}
	
	/**
	 * 获取一对象的某一属性的实例一天内的数据.
	 * 
	 * @param dxbh
	 * @param sxbh
	 * @param slbh
	 * @return
	 */
	public Cursor getDayData(String dxbh, String sxbh, String slbh, String dayTime, int start) {
		pSQL.setSQL(sqlDayData);
		String tabName = dayTime.substring(0, 4) + "_" 
			+ (dayTime.substring(5, 6).equals("0") ? dayTime.substring(6, 7) : dayTime.substring(5, 7));
		pSQL.setStringExt(1, tabName, false);
		pSQL.setStringExt(2, dxbh, false);
		pSQL.setStringExt(3, sxbh, false);
		pSQL.setStringExt(4, slbh, false);
		pSQL.setInt(5, start);
		pSQL.setInt(6, start + 3600*24);
		
		return DataSetBean.getCursor(pSQL.getSQL());		
	}
	
	/**
	 * 获取属性描述.
	 * 
	 * @return
	 */
	public static Map getSxsm() {
		Map map = new HashMap();
		String sql = "select dxbh,sxbh,sxsm from attribute_list a,object_def b where a.dxlx=b.dxlx";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if(null != fields) {
			while(null != fields) {
				String dxbh = (String)fields.get("dxbh");
				String sxbh = (String)fields.get("sxbh");
				String sxsm = (String)fields.get("sxsm");
				map.put(dxbh + "#" + sxbh, sxsm);
				
				fields = cursor.getNext();
			}
		}
		return map;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
