/**
 * @(#)CommonMap.java 2006-1-11
 * 
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.netcutover;

import java.util.HashMap;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.FormUtil;
import com.linkage.module.gwms.Global;

/**
 * 开通系统中常用到的Map
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public final class CommonMap {

	/**
	 * 获取工单状态Map
	 * 
	 * @return 工单状态Map
	 */
	public static Map getStatusTypeMap() {
		Map statusTypeMap = new HashMap();
		statusTypeMap.clear();
		statusTypeMap.put("0", "处理成功");
		statusTypeMap.put("1", "处理失败");

		return statusTypeMap;
	}

	/**
	 * 获取工单执行结果 描述Map
	 * 
	 * @return 工单执行结果 描述Map
	 */
	public static Map getErrStatusTypeMap() {
		Map errStatusTypeMap = new HashMap();
		errStatusTypeMap.clear();
		errStatusTypeMap.put("1", "正在处理");
		errStatusTypeMap.put("2", "已处理，成功");
		errStatusTypeMap.put("3", "已处理，失败");
		errStatusTypeMap.put("4", "参数错误");
		errStatusTypeMap.put("5", "手工激活后正在处理");
		errStatusTypeMap.put("6", "处理失败");
		errStatusTypeMap.put("7", "处理失败");
		errStatusTypeMap.put("8", "处理失败");
		errStatusTypeMap.put("9", "手工执行成功");
		errStatusTypeMap.put("10", "手工执行失败");

		return errStatusTypeMap;
	}

	/**
	 * 获取工单类型Map
	 * 
	 * @return 工单类型Map
	 */
	public static Map getProductTypeMap() {
		Map productTypeMap = new HashMap();
		String sql = "select * from tab_gw_serv_type";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select serv_type_id, serv_type_name from tab_gw_serv_type";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if(fields != null ){
			while(fields != null){								
				productTypeMap.put((String)fields.get("serv_type_id"), (String)fields.get("serv_type_name"));
				fields = cursor.getNext();
			}
			
		}
//		productTypeMap.clear();
//		productTypeMap.put("1", "客户");
//		productTypeMap.put("2", "窄带");
//		productTypeMap.put("3", "ADSL");
//		productTypeMap.put("31", "ADSL批量工单");
//		productTypeMap.put("4", "LAN");
//		productTypeMap.put("41", "LAN批量工单");
//		productTypeMap.put("5", "VDSL");
//		productTypeMap.put("6", "网吧");
//        productTypeMap.put("7", "IPTV/BARS");
//        productTypeMap.put("8", "IPTV/DSLAM");
//        productTypeMap.put("71", "IPTV/BARS批量工单");
//        productTypeMap.put("81", "IPTV/DSLAM批量工单");
//		productTypeMap.put("700", "网络传真");
//		productTypeMap.put("701", "VPN");
//		productTypeMap.put("702", "短信");
//		productTypeMap.put("703", "电话移动办公");
//		productTypeMap.put("704", "语音留言");
//		productTypeMap.put("705", "软电话");
//		productTypeMap.put("706", "电话会议");
//		productTypeMap.put("707", "IVR");
//        productTypeMap.put("708", "NGN");
		return productTypeMap;
	}
	/**
	 * 生成业务类型下拉框
	 * @param name
	 * @param cast
	 * @param hasChild
	 * @param pos
	 * @param aliasName
	 * @return
	 */
	public static String getProductSelectOption(String name,String cast,boolean hasChild,String pos,String aliasName){
		PrepareSQL psql = new PrepareSQL("select serv_type_id,serv_type_name from tab_gw_serv_type");
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor("select serv_type_id,serv_type_name from tab_gw_serv_type");
	    return FormUtil.createListBox(cursor, name, cast, hasChild, pos, aliasName);
	}
	/**
	 * 获取工单服务类型Map
	 * 
	 * @return 工单服务类型Map
	 */
	public static Map getServTypeMap() {
		Map servTypeMap = new HashMap();
		String sql = "select * from tab_gw_oper_type";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select oper_type_id, oper_type_name from tab_gw_oper_type";
		}
		
		Cursor cursor = DataSetBean.getCursor(sql);
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Map fields = cursor.getNext();
		if(fields != null ){
			while(fields != null){				
				
				servTypeMap.put((String)fields.get("oper_type_id"), (String)fields.get("oper_type_name"));
				fields = cursor.getNext();
			}
			
		}
		
		
//		servTypeMap.clear();
//		servTypeMap.put("1", "打开");
//		servTypeMap.put("2", "暂停");
//		servTypeMap.put("3", "销户");
//		servTypeMap.put("4", "复机");
//		servTypeMap.put("5", "绑定属性修改");
//		servTypeMap.put("6", "信息修改");
//		servTypeMap.put("21", "开户");
//		servTypeMap.put("22", "销户");
//		servTypeMap.put("23", "暂停");
//		servTypeMap.put("24", "更改速率");
//		servTypeMap.put("25", "移机");
//		servTypeMap.put("26", "复机");
//		servTypeMap.put("29", "激活");
//		servTypeMap.put("32", "移机开户");
//		servTypeMap.put("33", "移机销户");
//		servTypeMap.put("121", "开通Adsl和IPTV");
//		servTypeMap.put("221", "开通IPTV");
//		servTypeMap.put("321","ADSL开户");
//		servTypeMap.put("122","ADSL和IPTV销户");
//		servTypeMap.put("222","IPTV销户");
//		servTypeMap.put("322","ADSL销户");
//		servTypeMap.put("123","ADSL和IPTV暂停");
//		servTypeMap.put("223","IPTV暂停");
//		servTypeMap.put("323","ADSL暂停");
//		servTypeMap.put("126","ADSL和IPTV复机");
//		servTypeMap.put("226","IPTV复机");
//		servTypeMap.put("326","ADSL复机");
//		servTypeMap.put("50","使能个数激活");
//		servTypeMap.put("51","使能无限激活");
//		servTypeMap.put("52","使能关闭");
		
		return servTypeMap;
	}
	
	/*
	public static void main (String[] args) {
		String test = "('hello',)";
	}
	*/
}
