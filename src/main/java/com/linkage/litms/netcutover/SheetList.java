/**
 * @(#)SheetList.java 2006-1-12
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.netcutover;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.Global;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.filter.SelectCityFilter;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.FormUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.AreaManager;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.system.dbimpl.AreaManageSyb;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 获取工单列表 报表类型 int
 * <UL>
 * <LI>工单表
 * <LI>错单表
 * <LI>97回单表
 * </UL>
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public class SheetList extends AbstractGetSheetList {
	private static final Logger LOG = LoggerFactory.getLogger(SheetList.class);
	private static final int DB_MYSQL=com.linkage.module.gwms.Global.DB_MYSQL;
	
	/**
	 * 获取工单列表数据SQL
	 */
	private String txtSQL = "select * from tablename where"
			+ " worksheet_receive_time>? and worksheet_receive_time<=?"
			+ " and servtype<>28 and producttype in (?) ? "
			+ " and device_id in(select res_id from tab_gw_res_area where area_id in(?)) "
			+ " order by worksheet_receive_time desc";
	/**
	 * 通过City_id过滤获取工单列表数据SQL
	 */
	private String GetSheetListByCityIDSQL = "select * from tablename where"
			+ " worksheet_receive_time>=? and worksheet_receive_time<?"
			+ " and servtype<>28 and producttype in (?) ? "
			// + " and device_id in(select res_id from tab_gw_res_area where
			// area_id in(?) and res_type=1) "
			+ " and system_id in(?) " + " order by worksheet_receive_time desc";
	/**
	 * 传递objWorkSheet参数(ObjectWorkSheet)
	 */
	private ObjectWorkSheet objWorkSheet;
	private long m_AreaId;
	/**
	 * Constractor:
	 * 
	 * @param time
	 *            时间 表示 1970 年1月1日 00:00:00 GMT 以后 time 秒的时间点 long
	 * @param type
	 *            报表类型 int
	 *            <UL>
	 *            <LI>工单表
	 *            <LI>错单表
	 *            <LI>97回单表
	 *            </UL>
	 * @param productType
	 *            业务类型 int
	 *            <UL>
	 *            <LI>ADSL
	 *            <LI>LAN
	 *            <LI>NETBAR
	 *            </UL>
	 * @param _objWorkSheet
	 *            JSP参数(ObjectWorkSheet).
	 */
	public SheetList(long time, int type, int productType, ObjectWorkSheet _objWorkSheet,
			long m_AreaId) {
		super(time, type, productType);
		this.objWorkSheet = _objWorkSheet;
		this.m_AreaId = m_AreaId;
	}
	/**
	 * Constractor:
	 * 
	 */
	public SheetList() {
	}
	/*
	 * 返回工单列表.
	 * 
	 * @return 返回Cursor类型数据
	 */
	public Cursor getSheetList() {
		String tabName = this.getTabName();
		txtSQL = txtSQL.replaceFirst("tablename", tabName);
		long lms = this.time * 1000;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = new Date(lms);
		String strStart = sdf.format(dt);
		dt = new Date(lms + 12 * 3600 * 1000);
		String strEnd = sdf.format(dt);
		// 构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(txtSQL);
		pSQL.setString(1, strStart);
		pSQL.setString(2, strEnd);
		pSQL.setStringExt(3, this.getProducttype(), false);
		if (null == objWorkSheet.getWorksheet_status()) {
			pSQL.setStringExt(4, "", false);
		}
		else {
			pSQL.setStringExt(4, " and worksheet_status in ("
					+ objWorkSheet.getWorksheet_status() + ")", false);
		}
		// pSQL.setStringExt(5, StringUtils.weave(objWorkSheet.getGather_id()),
		// false);
		// 获得本区域以及下属所有区域的编号
		AreaManager areaManager = new AreaManageSyb();
		List m_AreaIdList = areaManager.getLowerToFloorAreaIds(Integer.parseInt(String
				.valueOf(m_AreaId)));
		m_AreaIdList.add(String.valueOf(m_AreaId));
		pSQL.setStringExt(5, StringUtils.weaveNumber(m_AreaIdList), false);
		return DataSetBean.getCursor(pSQL.getSQL());
	}
	/*
	 * 返回工单列表.
	 * 
	 * @return 返回Cursor类型数据 通过city_id 过滤
	 */
	public Cursor getSheetList(String city_id) {
		String tabName = this.getTabName();
		GetSheetListByCityIDSQL = GetSheetListByCityIDSQL.replaceFirst("tablename",
				tabName);
		long lms = this.time * 1000;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = new Date(lms);
		String strStart = sdf.format(dt);
		dt = new Date(lms + 12 * 3600 * 1000);
		String strEnd = sdf.format(dt);
		// 构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(GetSheetListByCityIDSQL);
		pSQL.setString(1, strStart);
		pSQL.setString(2, strEnd);
		pSQL.setStringExt(3, this.getProducttype(), false);
		if (null == objWorkSheet.getWorksheet_status()) {
			pSQL.setStringExt(4, "", false);
		}
		else {
			pSQL.setStringExt(4, " and worksheet_status in ("
					+ objWorkSheet.getWorksheet_status() + ")", false);
		}
		// pSQL.setStringExt(5, StringUtils.weave(objWorkSheet.getGather_id()),
		// false);
		// // 获得本区域以及下属所有区域的编号
		// AreaManager areaManager = new AreaManageSyb();
		// List m_AreaIdList = areaManager.getLowerToFloorAreaIds(Integer
		// .parseInt(String.valueOf(m_AreaId)));
		// m_AreaIdList.add(String.valueOf(m_AreaId));
		pSQL.setStringExt(5, city_id, false);
		return DataSetBean.getCursor(pSQL.getSQL());
	}
	/*
	 * 返回工单列表. orverLoading by YYS 2006-09-24 @param city_id 过滤属地 @param
	 * ifcontainChild 是否向下级联 @return 返回Cursor类型数据
	 */
	public Cursor getSheetList(String city_id, String ifcontainChild) {
		String tabName = this.getTabName();
		GetSheetListByCityIDSQL = GetSheetListByCityIDSQL.replaceFirst("tablename",
				tabName);
		long lms = this.time * 1000;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = new Date(lms);
		String strStart = sdf.format(dt);
		// 将查询细度由原半天放大到一天
		// 时间跨度由原12小时更改为24小时
		// dt = new Date(lms + 12 * 3600 * 1000);
		dt = new Date(lms + 24 * 3600 * 1000);
		String strEnd = sdf.format(dt);
		// 构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(GetSheetListByCityIDSQL);
		pSQL.setString(1, strStart);
		pSQL.setString(2, strEnd);
		pSQL.setStringExt(3, this.getProducttype(), false);
		if (null == objWorkSheet.getWorksheet_status()) {
			pSQL.setStringExt(4, "", false);
		}
		else {
			pSQL.setStringExt(4, " and worksheet_status in ("
					+ objWorkSheet.getWorksheet_status() + ")", false);
		}
		// pSQL.setStringExt(5, StringUtils.weave(objWorkSheet.getGather_id()),
		// false);
		// 获得本区域以及下属所有区域的编号
		// AreaManager areaManager = new AreaManageSyb();
		// List m_AreaIdList = areaManager.getLowerToFloorAreaIds(Integer
		// .parseInt(String.valueOf(m_AreaId)));
		// m_AreaIdList.add(String.valueOf(m_AreaId));
		// pSQL.setStringExt(5, StringUtils.weaveNumber(m_AreaIdList), false);
		if (ifcontainChild.equals("1")) {
			SelectCityFilter scf = new SelectCityFilter();
			pSQL.setStringExt(5, scf.getAllSubCityIds(city_id, true), false);
		}
		else {
			pSQL.setStringExt(5, city_id, true);
		}
		return DataSetBean.getCursor(pSQL.getSQL());
	}
	/*
	 * 返回DDNS工单列表. by zhaixf 2008-03-18 
	 * @param city_id 过滤属地 
	 * @param ifcontainChild 是否向下级联 
	 * @return 返回Cursor类型数据
	 */
	public Cursor getDdnsSheetList(String city_id, String ifcontainChild) {
		// 域名工单列表数据SQL
		String GetDDNSSheetList = "select a.id,a.result_spec,a.type,a.receive_date,b.customer_name,b.city_id,"
				+ "c.name,c.value from tab_egw_bsn_open_original a, "
				+ " tab_customerinfo b, tab_egw_bsn_open_param c"
				+ " where a.receive_date>=? and a.receive_date<? and a.product_spec_id = 'GD9900062' "
				+ " and a.id = c.id " // wp_flag=0表示不执行workpro
				+ " and a.bnet_id = b.customer_id and b.city_id in (?)"
				+ " order by a.id desc";
		PrepareSQL psql = new PrepareSQL(GetDDNSSheetList);
    	psql.getSQL();
		return cursorDoForDDNSSheet(getSheetList(city_id, ifcontainChild,
				GetDDNSSheetList));
	}
	/*
	 * 返回Bnet工单列表. by zhaixf 2008-03-18 
	 * @param city_id 过滤属地 
	 * @param ifcontainChild 是否向下级联 
	 * @return 返回Cursor类型数据
	 */
	public Cursor getBnetSheetList(String city_id, String ifcontainChild) {
		// BNET工单列表数据SQL
		// select原始工单表的工单号，业务类型，操作类型，执行结果，接收时间，工单状态，用户名，属地，工单参数
		String GetBnetSheet = "select a.id,a.product_spec_id,a.type,a.receive_date,a.status,a.result,a.result_info,b.customer_name,b.city_id,c.name,c.value "
				+ " from tab_egw_bsn_open_original a, "
				+ " tab_customerinfo b, tab_egw_bsn_open_param c"
				+ " where a.receive_date>=? and a.receive_date<? "
				+ " and a.product_spec_id != 'GD9900062' and a.id = c.id " 
				+ " and a.bnet_id = b.customer_id and b.city_id in (?)"
				+ " order by a.id desc";
		PrepareSQL psql = new PrepareSQL(GetBnetSheet);
    	psql.getSQL();
		Cursor cursor = getSheetList(city_id, ifcontainChild, GetBnetSheet);
		//加入业务类型为3的纪录，
		GetBnetSheet = "select a.id,a.product_spec_id,a.type,a.receive_date,a.status,a.result,a.result_info,b.customer_name,b.city_id "
			+ " from tab_egw_bsn_open_original a, "
			+ " tab_customerinfo b "
			+ " where a.receive_date>=? and a.receive_date<? "
			+ " and a.product_spec_id != 'GD9900062' and a.type=3" 
			+ " and a.bnet_id = b.customer_id and b.city_id in (?)"
			+ " order by a.id desc";
		PrepareSQL psql2 = new PrepareSQL(GetBnetSheet);
    	psql2.getSQL();
		cursor.addCursor(getSheetList(city_id, ifcontainChild, GetBnetSheet));
		cursor.sort("id", "desc", cursor.STRING_TYPE);
		LOG.debug("---sort by id--");
		return cursorDoForResSheet(cursor);
	}
	/*
	 * 返回Bnet工单列表. by zhaixf 2008-03-18 
	 * @param city_id 过滤属地 
	 * @param ifcontainChild 是否向下级联 
	 * @param sheetListSql 工单视图sql 
	 * @return 返回Cursor类型数据
	 */
	private Cursor getSheetList(String city_id, String ifcontainChild, String sheetListSql) {
		long start = this.time * 1000;
		long end = start + 24 * 3600 * 1000;
		// 构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(sheetListSql);
		pSQL.setStringExt(1, String.valueOf(start), false);
		pSQL.setStringExt(2, String.valueOf(end), false);
		// 获得本区域以及下属所有区域的编号
		if (ifcontainChild.equals("1")) {
			SelectCityFilter scf = new SelectCityFilter();
			pSQL.setStringExt(3, scf.getAllSubCityIds(city_id, true), false);
		}
		else {
			pSQL.setStringExt(3, city_id, true);
		}
		return DataSetBean.getCursor(pSQL.getSQL());
	}
	/*
	 * ---------------------------------------------------------------
	 * 返回DDNS工单查询列表. by zhaixf 2008-03-18 
	 * @param request 
	 * @return 返回Cursor类型数据
	 * 
	 */
	public Cursor queryDdnsSheetList(HttpServletRequest request) {
		// select原始工单表的工单号，执行结果，执行时间，用户名，属地，工单参数(域名，账号，密码)
		//GD9900062为DDNS业务
		String queryDDNSSql = "select a.id,a.result_spec,a.type,a.receive_date,b.customer_name,b.city_id,c.name,c.value "
				+ "from tab_egw_bsn_open_original a, tab_customerinfo b, tab_egw_bsn_open_param c "
				+ " where a.receive_date>=? and a.receive_date<?  and a.product_spec_id = 'GD9900062'"
				+ " and a.id = c.id and a.bnet_id = b.customer_id ";
		PrepareSQL psql = new PrepareSQL(queryDDNSSql);
    	psql.getSQL();
		return cursorDoForDDNSSheet(querySheet(request, queryDDNSSql));
	}
	/*
	 * 返回Bss工单查询列表. by zhaixf 2008-09-
	 * @param request 
	 * @return 返回Cursor类型数据
	 */
	public Cursor queryBssSheetList(HttpServletRequest request) {

	    Cursor cursor = null;
	    String product_spec_id = request.getParameter("some_service");
	    String city_id = request.getParameter("city_id");
	    String oper_type =  request.getParameter("oper_type");//操作类型
	    String username = request.getParameter("username");
	    String startTime = request.getParameter("startTime");
	    String endTime = request.getParameter("endTime");
	    String start = request.getParameter("start");
	    String end = request.getParameter("end");
	    long strStart , strEnd;
	    if (start == null || start.equals("")) {
	    	strStart = new DateTimeUtil((new DateTimeUtil()).getDate()).getLongTime();//getLongTime()精确到秒
			strEnd = strStart + 24 * 3600;
	    }
	    else {
	    	start = start.replaceAll("-", "/");
	    	end = end.replaceAll("-", "/");
	    	strStart = (new Date(start + " " + startTime)).getTime();
	    	strEnd = (new Date(end + " " + endTime)).getTime();
	    }
	    
	    
	    String sql = "select * from tab_egw_bsn_open_original a where a.receive_date>=? and a.receive_date<=? ";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select id, city_id, username, product_spec_id, type, receive_date, result " +
					"from tab_egw_bsn_open_original a where a.receive_date>=? and a.receive_date<=? ";
		}
	    
	    if(oper_type != null && !oper_type.equals("-1") && !oper_type.isEmpty()){
		sql += " and a.type=" + oper_type;
	    }
	    
	    if(username != null && !username.isEmpty()){
			sql += " and a.username = '"+username+"'";
		}
	    
	    if(null != product_spec_id && !product_spec_id.isEmpty()){
	    	sql += " and a.product_spec_id = '" + product_spec_id + "'";
	    }
	    
	    if(city_id != null && !city_id.equals("-1") && !city_id.isEmpty()){
	    	List list = CityDAO.getAllNextCityIdsByCityPid(city_id);
	    	if(list != null && list.size() > 0){
	    		Iterator itor = list.iterator();
	    		Object cityId = "";
	    		sql += " and a.city_id in (";
		    	while(itor.hasNext()){
		    		sql += "'" + itor.next() + "',";
		    	}
		    	sql += "'')";
	    	}
	    	list = null;
	    }

	    sql += " order by a.receive_date desc";
	    
	    PrepareSQL pSQL = new PrepareSQL(sql);
		pSQL.setLong(1, strStart);
		pSQL.setLong(2, strEnd);
	    cursor = DataSetBean.getCursor(pSQL.getSQL());
	    return cursor;
	}
	
	/**
	 * 返回BSS工单视图
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2008-9-10
	 * @return Cursor
	 */
	public Cursor getBssSheetList(String city_id, String str_ifcontainChild){
		 long start = this.time * 1000;
		  long end = start + 24 * 3600 * 1000;
		     String sql = "select * from tab_egw_bsn_open_original a  where 1=1 ";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select result, id, city_id, username, product_spec_id, type, status, receive_date" +
					" from tab_egw_bsn_open_original a  where 1=1 ";
		}
		     
		     if(city_id != null && !city_id.equals("-1") && !city_id.isEmpty()){
		      if("1".equals(str_ifcontainChild)){
		       List list = CityDAO.getAllNextCityIdsByCityPid(city_id);
		       if(list != null && list.size() > 0){
		        Iterator itor = list.iterator();
		        Object cityId = "";
		        sql += " and  a.city_id in (";
		        while(itor.hasNext()){
		         sql += "'" + itor.next() + "',";
		        }
		        sql += "'')";
		       }
		       list = null;
		      }else{
		       sql += " and a.city_id = '" + city_id + "'";
		      }
		      
		     }
		     
		     sql += " and a.receive_date >= " + start + " and a.receive_date <= " + end;
		     
		     sql += " order by a.receive_date desc";
		     
		     PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
	    	Cursor cursor = DataSetBean.getCursor(sql);
		     return cursor;

	}
	
	/*
	 * @author zhaixf 2008-03-18 
	 * @param request 
	 * @param sql 查询sql语句
	 * @return 返回String类型数据，操作结果
	 */
	private Cursor querySheet(HttpServletRequest request, String querySql) {
		// 属地信息
		String city_id = request.getParameter("hid_city_id");
		String ifcontainChild = request.getParameter("ifcontainChild");
		// 获取用户信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		//第一次进来的时候,没有city_id的信息,则取用户的city_id
		if (city_id == null || city_id.equals(""))
			city_id = curUser.getCityId();
		LOG.debug("----city_id----" + city_id);
		String domainId = request.getParameter("domainId");
		String result_spec = request.getParameter("result_spec");
		String username = request.getParameter("username");
		String customername = request.getParameter("customername");
		String start = request.getParameter("start"); // 开始
		String startTime = request.getParameter("startTime");
		String end = request.getParameter("end"); // 结束
		String endTime = request.getParameter("endTime");
		long strStart , strEnd;
		if (start == null || start.equals("")) {
			strStart = new DateTimeUtil((new DateTimeUtil()).getDate()).getLongTime();//getLongTime()精确到秒
			strStart = strStart * 1000;
			strEnd = strStart + 24 * 3600 * 1000;
		}
		else {
			start = start.replaceAll("-", "/");
			end = end.replaceAll("-", "/");
			strStart = (new Date(start + " " + startTime)).getTime();
			strEnd = (new Date(end + " " + endTime)).getTime();
		}
		// select原始工单表的工单号，执行结果，用户名，属地，工单参数(域名，账号，密码)
		StringBuilder queryDDNSSheetList = new StringBuilder();
		//查询语句参数
		queryDDNSSheetList.append(querySql);
		//当第一次进来的时候ifcontainChild没有值，则取当前用户属地为city_id 并默认取所有子属地
		//或者当ifcontainChild=1的时候取，text中输入的属地，并包含子属地
		if (ifcontainChild == null || ifcontainChild.equals("")
				|| ifcontainChild.equals("1")) {
			queryDDNSSheetList.append(" and b.city_id in (");
			queryDDNSSheetList.append((new SelectCityFilter()).getAllSubCityIds(city_id,
					true));
			queryDDNSSheetList.append(")");
		}
		else {
			queryDDNSSheetList.append(" and b.city_id = '" + city_id + "'");
		}
		
		if (customername != null && !customername.equals("")) {
			queryDDNSSheetList.append(" and b.customer_name like '%");
			queryDDNSSheetList.append(customername);
			queryDDNSSheetList.append("%'");
		}
		
		if (username != null && !username.equals("")) {
			queryDDNSSheetList.append(" and b.username = '");
			queryDDNSSheetList.append(username);
			queryDDNSSheetList.append("'");
		}
		if (result_spec != null && !result_spec.equals("")) {
			if (result_spec.equals("0")) {
				queryDDNSSheetList.append(" and a.result_spec = 0 ");
			}
			else if (result_spec.equals("1"))
				queryDDNSSheetList.append(" and a.result_spec != 0 ");
		}
		if (domainId != null && !domainId.equals("")) {
			String sqlPrama = "select id from tab_egw_bsn_open_param where value like '%"
					+ domainId + "%'";
			// 先查出来工单参数
			PrepareSQL psql = new PrepareSQL(sqlPrama);
	    	psql.getSQL();
			Cursor cursor = DataSetBean.getCursor(sqlPrama);
			StringBuilder strList = new StringBuilder("('");
			if (cursor != null && cursor.getRecordSize() > 0) {
				int size = cursor.getRecordSize();
				
				for (int i = 0; i < size; i++) {
					strList.append(cursor.getRecord(i).get("id"));
					strList.append("','");
				}
			}
			strList.append("')");
			queryDDNSSheetList.append(" and a.id in ");
			queryDDNSSheetList.append(strList);
		}
		queryDDNSSheetList.append(" order by a.id desc");
		// 构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(queryDDNSSheetList.toString());
		pSQL.setStringExt(1, String.valueOf(strStart), false);
		pSQL.setStringExt(2, String.valueOf(strEnd), false);
		return DataSetBean.getCursor(pSQL.getSQL());
	}
	/*
	 * @author zhaixf 2008-03-18 
	 * @param resultType DDNS返回结果参数，ResultType类中有相关说明
	 * @return 返回String类型数据，操作结果
	 */
	private Cursor cursorDoForDDNSSheet(Cursor cursor) {
		Cursor fields = new Cursor();
		Map record = new HashMap() , result;
		// 对cursor按id进行分组，形成cursor数组
		if (cursor != null && cursor.getRecordSize() > 0) {
			Cursor[] cursorArray = cursor.group("id");
			int len = cursorArray.length;

			// 对每组的cursor进行处理，形成的形式为：{
			// {<o,v><o,v><o,v><o,v>}{<o,v><o,v><o,v><o,v>}... }
			int i = 0;
			while (len > i) {
				record = cursorArray[i].getNext();
				result = new HashMap();
				result.putAll(record);
				result.put("result_tail", getDDNSResultType((String) result
						.get("result_spec")));
				result.put("type", getBnetOperateType(result.get("type").toString()));
				// 对每组的map进行处理
				result.remove("name");
				result.remove("value");
				while (record != null) {
					result.put(record.get("name"), record.get("value"));
					record = cursorArray[i].getNext();
				}
				fields.add(result);
				result = null;
				++i;
			}
		}
		return fields;
	}

	/*
	 * 用于对返回的cursor进行处理，主要是对原始工单参数表的数据与原始工单表的数据组合到一起
	 * 返回DDNS工单列表. by zhaixf 2008-03-18 
	 * @param request 
	 * @return 返回Cursor类型数据
	 */
	private Cursor cursorDoForResSheet(Cursor cursor) {
		Cursor fields = new Cursor();
		Map record = new HashMap() , resultMap;
		// 对cursor按id进行分组，形成cursor数组
		if (cursor != null && cursor.getRecordSize() > 0) {
			Cursor[] cursorArray = cursor.group("id");
			int len = cursorArray.length;
			StringBuilder param = new StringBuilder();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 对每组的cursor进行处理，形成的形式为：{ //
			// {<o,v><o,v><o,v><o,v>}{<o,v><o,v><o,v><o,v>}... }
			int i = 0;
			//获取产品名称
			Map<String, String> productMap = getProductType();
			while (len > i) {
				record = cursorArray[i].getNext();
				resultMap = new HashMap();
				resultMap.putAll(record);
				resultMap.put("result_tail", resultMap.get("result_info")); 
				
				resultMap.put("product_spec_id", productMap.get(resultMap.get("product_spec_id").toString()));
				resultMap.put("type", getBnetOperateType(resultMap.get("type").toString()));
				if(resultMap.get("receive_date") != null && !resultMap.get("receive_date").toString().isEmpty()){
					resultMap.put("receive_date", sdf.format(new Date(Long.parseLong(resultMap.get("receive_date").toString())))); 
				}
				// 对每组的map进行处理 
				resultMap.remove("name");
				resultMap.remove("value");
				if(param != null && param.length() > 0)
					param.delete(0, param.length());
				while (record != null) {
					if(record.get("name") != null && !"".equals(record.get("name")))
						param.append(record.get("name") + ":" + record.get("value") + "<br>");
					
					record = cursorArray[i].getNext();
				}
				resultMap.put("param", param.toString());
				fields.add(resultMap);
				resultMap = null;
				++i;
			}
		}
		LOG.debug("------fields-------" + fields);
		return fields;
	}
	/*
	 * @author zhaixf 2008-03-18 
	 * @param resultType DDNS返回结果参数，ResultType类中有相关说明
	 * @return 返回String类型数据，操作结果
	 */
	private static String getDDNSResultType(String resultType) {
		if(resultType == null || resultType.equals("")){
			return "未找到对应结果说明";
		}
		switch (Integer.parseInt(resultType)) {
		case 0:
			return "成功";
		case 1:
			return "用户重复";
		case 2:
			return "根域为空";
		case 3:
			return "找不到对应的根域";
		case 4:
			return "域名己注册";
		case 5:
			return "插入数据库失败";
		case 6:
			return "DNS服务器注册该域名失败";
		default:
			return "未找到对应结果说明";
		}
	}
	
	/**
	 * 根据操作代码，返回操作类型说明
	 *   1：订购   2：修改  3：拆机
	 * SheetList.java
	 * @param typeId
	 * @return typeName
	 * String
	 */
	public static String getOperateType(String typeId){
		String typeName = Global.Oper_type_Id_Name_Map.get(typeId);
		return typeName == null ? "未知" : typeName;
	}
	
	/**
	 * 广州DDNS操作类型
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2008-9-10
	 * @return String
	 */
	public static String getBnetOperateType(String typeId){
		if("1".equals(typeId)){
			return "订购";
		}
		if("2".equals(typeId)){
			return "修改";
		}
		if("3".equals(typeId)){
			return "拆机";
		}
		return "未知";
	}
	/**
	 * 根据业务代码，返回业务类型
	 * SheetList.java
	 * @param productId
	 * @return
	 * String
	 */
	public static String getType(String product_spec_id){
	    Object obj = Global.Serv_type_Map.get(product_spec_id);
	    return obj == null ? "未知" : String.valueOf(obj);
	}
	
	/**
	 * 获取产品名称
	 * 
	 * @param 产品编号
	 * @author Jason(3412)
	 * @date 2008-9-10
	 * @return String
	 */
	public static Map<String,String> getProductType(){
		
		String strSQL = "select a.product_spec_id, b.serv_type_name " +
				" from gw_serv_type_product a,tab_gw_serv_type b " +
				" where a.serv_type_id=b.serv_type_id"; 
		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		Map<String,String> map = DataSetBean.getMap(strSQL);
		if(map != null && !map.isEmpty()){
			return map;
		}
		return null;
	}
	/**
	 * 生成设备型号下拉框
	 * @return
	 */
	public String getDeviceModelList(){
	    String sql = "select device_model_id,device_model from gw_device_model";
	    PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
	    Cursor cursor = DataSetBean.getCursor(sql);
	    return FormUtil.createListBox(cursor, "device_model_id", "device_model", false, "", "");
	}
	/**
	 * 业务类型
	 */
	public enum ProductSpecID {
		GD9900060, GD9900061, GD9900062, GD9900063, GD9900064, GD9900065, GD9900066, GD9900067, GD9900068, LC0000001, LC0000002, LC0000003;

		public String getDescription() {
			switch (this) {
				case GD9900060 :
					return "IPPBX设备";
				case GD9900061 :
					return "短信功能";
				case GD9900062 :
					return "动态DNS";
				case GD9900063 :
					return "远程办公";
				case GD9900064 :
					return "语音导航";
				case GD9900065 :
					return "语音邮箱";
				case GD9900066 :
					return "SIP分机";
				case GD9900067 :
					return "移动分机";
				case GD9900068 :
					return "电话一号通";
				case LC0000001 :
					return "网络传真";
				case LC0000002 :
					return "NGN业务";
				case LC0000003 :
					return "防火墙配置";
				default :
					return "未知量";
			}
		}
	}
	
	/**
	 * 
	 * johnson
	 * 2008-7-12
	 * 
	 * Map
	 */
	public static Map getResult(){
		Map map = new HashMap();
		map.put("0", "成功");
		map.put("1", "失败");
		return map;
	}
	/**
	 * 获取工单详细信息
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2008-9-10
	 * @return Map
	 */
	public static Map getDetailSheet(String id){
		
		String sql = "select * from tab_egw_bsn_open_original where id = '" + id + "'";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select bnet_id, bnet_account, type, customer_name, product_spec_id, result, result_info," +
					" receive_date, status, wp_flag, result_spec, result_spec_desc, oui, device_type, device_serialnumber," +
					" time, city_id, order_type, username, passwd, dev_sheet_id, sheet_para_desc from tab_egw_bsn_open_original where id = '" + id + "'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		return DataSetBean.getRecord(sql);
	}
	
	/*
	 * 返回DDNS工单查询列表. by zhaixf 2008-03-18 
	 * @param request 
	 * @return 返回Cursor类型数据
	 */
	public Cursor queryBnetSheetList(HttpServletRequest request) {
	    // select原始工单表的工单号，执行结果，用户名，属地，工单参数(域名，账号，密码)
		String queryBNETSql = "select a.id,a.product_spec_id,a.type,a.receive_date,"
				+ "a.status,a.result_info,a.result,b.customer_name,b.city_id,c.name,c.value "
				+ " from tab_egw_bsn_open_original a, tab_customerinfo b, tab_egw_bsn_open_param c "
				+ " where a.receive_date>=? and a.receive_date<? "
				+ " and a.product_spec_id != 'GD9900062' and a.id = c.id and a.bnet_id = b.customer_id ";
		PrepareSQL psql = new PrepareSQL(queryBNETSql);
    	psql.getSQL();
		Cursor cursor = querySheet(request, queryBNETSql);
		//对“拆机”类型没有参数的情况进行追加
		String strSql = "select a.id,a.product_spec_id,a.type,a.receive_date,"
			+ "a.status,a.result_info,a.result,b.customer_name,b.city_id "
			+ " from tab_egw_bsn_open_original a, tab_customerinfo b "
			+ " where a.receive_date>=? and a.receive_date<? "
			+ " and a.product_spec_id != 'GD9900062' and a.type=3 and a.bnet_id = b.customer_id ";
		PrepareSQL psql2 = new PrepareSQL(strSql);
    	psql2.getSQL();
		cursor.addCursor(querySheet(request, strSql));

	    return cursorDoForResSheet(cursor);

	}

}
