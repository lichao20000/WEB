/*
 * @(#)GeneralNetPerf.java	1.00 12/31/2005
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.performance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.filter.SelectCityFilter;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;

/**
 * 继承AbstractNetPerf抽象类，实现其接口GeneralData的方法。<br>
 * 取出网络性能的文本数据和图形数据。
 * 
 * @author Dolphin
 * @version 1.00, 1/10/2006
 * @see AbstractNetPerf
 * @since Liposs 2.1
 */

public class GeneralNetPerf extends AbstractNetPerf {
	
	// 获取网络性能查询报表的文本数据SQL
	private String txtSQL = "select * from ? where gathertime>=? and "
			+ "gathertime<? and id in(?)";

	// 获取网络性能原始性能数据
	private String deviceRawDataSQL = "select id,avg(value) as avgvalue,max(value) as maxvalue,min(value) as minvalue from ? where gathertime>=? and "
			+ "gathertime<? and id in(?) group by id";

	// 获取网络性能小时性能数据
	private String deviceHourDataSQL = "select id,avg(avgvalue) as avgvalue,max(maxvalue) as maxvalue,min(minvalue) as minvalue from ? where gathertime>=? and "
			+ "gathertime<? and id in(?) group by id";

	// 获取在线用户数网络性能小时性能数据
	private String deviceClass7HourDataSQL = "select max(b.maxvalue) as maxvalue,min(b.minvalue) as minvalue,avg(b.avgvalue) as avgvalue "
			+ "from (select gathertime,sum(maxvalue) as maxvalue,sum(minvalue) as minvalue,sum(avgvalue) as avgvalue from ? "
			+ "where gathertime>=? and gathertime<? and id in(?) "
			+ "group by gathertime) b";

	// 获取所有设备的网络性能查询报表的文本数据SQL
	// 通过时间 报表类型 表达式类型 属地 及用户域信息
	private String allDeviceTxtSQL = "select b.device_id,e.device_name,e.loopback_ip,a.expressionid,a.descr,a.unit,c.id,c.indexid,c.descr as indexDesc,f.maxvalue,f.minvalue,f.avgvalue,a.class1 "
		+ "from pm_expression a,pm_map b,pm_map_instance c,tab_gw_device e,? f "
		+ "where a.class2=1 and a.class1 in (?) "
		+ "and a.expressionid  = b.expressionid and b.isok=1 "
		+ "and a.expressionid  = c.expressionid and b.device_id = c.device_id "
		+ "and c.id = f.id and f.gathertime>=? and f.gathertime<? "		
		+ "and b.device_id = e.device_id and e.device_id in(?) "		
		+ "order by e.loopback_ip,b.device_id,a.class1,c.indexid";

	// 获取网络性能查询报表的图形数据SQL
	private String chartSQL = "select * from ? where gathertime>=? and "
			+ "gathertime<? and id in(?)";

	// 获取所有设备的网络性能查询报表的文本数据SQL
	// 通过时间 报表类型 表达式类型 属地 及用户域信息
	private String allDeviceChartSQL = "select b.device_id,a.expressionid,a.descr,a.unit,c.id,c.indexid,c.descr as indexDesc,gathertime,? "
			+ "from pm_expression a,pm_map b,pm_map_instance c,? f "
			+ "where a.class2=1 and a.class1=? "
			+ "and a.expressionid  = b.expressionid and b.isok=1 "
			+ "and a.expressionid  = c.expressionid and b.device_id = c.device_id "
			+ "and c.id = f.id and f.gathertime>=? and f.gathertime<? "
			+ "and b.device_id =? "			
			+ "order by b.device_id,c.id,c.indexid";

	// 网络性能采集实例号
	private String[] arrId;

	// 设备厂商SQL
//	private static String strSQLDeviceVendor = "select vendor_id,vendor_name from tab_vendor where vendor_id in (select distinct oui from tab_gw_device where oui != '' and oui != null)";
	private static String strSQLDeviceVendor = "select vendor_id,vendor_name from tab_vendor";
	
	private static String strDeviceType = "select distinct(device_model) as device_model from tab_gw_device where vendor_id=? and city_id in(?) order by device_model";

	private static String strDeviceIP3 = "select distinct device_id,loopback_ip,device_name from "
			+ "tab_gw_device where device_model=? and gather_id in(?) and device_id in"
			+ "(select device_id from pm_map) and city_id in (?) order by loopback_ip";

	/**
	 * 查询出所有设备的指定的性能指标数据
	 */
	private static String allDeviceClassSql = "select distinct b.device_id,a.class1,c.loopback_ip,c.device_name,a.unit,a.descr "
		+ "from pm_expression a, pm_map b,tab_gw_device c where a.class2=1 and a.class1 in (?) and a.expressionid  = b.expressionid and b.isok=1 "
		+"and b.device_id = c.device_id and c.device_id in(?) order by b.device_id,a.class1";

	/**
	 * 构造函数，设置网络性能报表参数
	 * 
	 * @param start
	 *            开始时间 表示 1970 年1月1日 00:00:00 GMT 以后 time 秒的时间点 长整形
	 * @param end
	 *            结束时间 表示 1970 年1月1日 00:00:00 GMT 以后 time 秒的时间点 长整形
	 * @param type
	 *            报表类型 整形
	 *            <UL>
	 *            <LI>1 代表小时报表
	 *            <LI>2 代表日报表
	 *            <LI>3 代表周报表
	 *            <LI>4 代表月报表
	 *            <LI>5 代表年报表
	 *            </UL>
	 * @param arrId
	 *            查询条件，网络性能采集实例号数组
	 */
	public GeneralNetPerf(long start, long end, int type, String[] arrId) {
		super(start, end, type);
		this.arrId = arrId;
	}

	/**
	 * 构造函数，设置网络性能报表参数
	 * 
	 * @param start
	 *            开始时间 表示 1970 年1月1日 00:00:00 GMT 以后 time 秒的时间点 长整形
	 * @param end
	 *            结束时间 表示 1970 年1月1日 00:00:00 GMT 以后 time 秒的时间点 长整形
	 * @param type
	 *            报表类型 整形
	 *            <UL>
	 *            <LI>1 代表小时报表
	 *            <LI>2 代表日报表
	 *            <LI>3 代表周报表
	 *            <LI>4 代表月报表
	 *            <LI>5 代表年报表
	 *            </UL>
	 */
	public GeneralNetPerf(long start, long end, int type) {
		super(start, end, type);
	}

	/**
	 * 将索引号转化为"'"+id+"'"
	 * 
	 * @return
	 */
	public String[] getArrId() {
		String tmp[] = new String[arrId.length];
		for (int i = 0; i < arrId.length; i++) {
			// if (i == 0) {
			tmp[i] = "'" + arrId[i] + "'";
			// } else {
			// tmp[i] = ",'" + arrId[i] + "'";
			// }
		}

		return tmp;
	}

	/*
	 * public GeneralNetPerf(HttpServletRequest req) { try { String s_start,
	 * s_end, s_type; s_start = req.getParameter("hidstart"); s_end =
	 * req.getParameter("hidend"); s_type = req.getParameter("type");
	 * 
	 * this.start = Long.parseLong(s_start); this.end = Long.parseLong(s_end);
	 * this.type = Integer.parseInt(s_type); this.arrId =
	 * req.getParameterValues("iid"); } catch (Exception e) {
	 * log.error("GeneralNetPerf Bean 获取视图参数出错："+e.getMessage()); } }
	 */

	public Cursor getGeneralTxtData() {
		String tblName = this.getTxtTblName();
		// txtSQL = txtSQL.replaceFirst("{tablename}", tblName);
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			txtSQL = "select id, maxvalue, minvalue, avgvalue from ? where gathertime>=? and "
					+ "gathertime<? and id in(?)";
		}

		// 构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(txtSQL);
		pSQL.setStringExt(1, tblName, false);
		pSQL.setLong(2, start);
		pSQL.setLong(3, end);
		pSQL.setStringExt(4, StringUtils.weave(this.getArrId()), false);


		return DataSetBean.getCursor(pSQL.getSQL());
	}

	/**
	 * 在原始数据中获取设备当天性能的最大最小平均值 type = 0
	 * 
	 * @return HashMap key:id value:maxvalue+"#"+minvalue+"#"+avgvalue
	 */
	public HashMap getDevRawTxtData() {
		HashMap _m = new HashMap();
		_m.clear();

		String tblName = this.getTxtTblName();
		// 构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(deviceRawDataSQL);
		pSQL.setStringExt(1, tblName, false);
		pSQL.setLong(2, start);
		pSQL.setLong(3, end);
		pSQL.setStringExt(4, StringUtils.weave(this.getArrId()), false);


		Cursor _c = DataSetBean.getCursor(pSQL.getSQL());


		if (_c != null && _c.getRecordSize() > 0) {
			Map fields = _c.getNext();
			String _k = "";
			String _avgV = "";
			String _maxV = "";
			String _minV = "";
			while (fields != null) {
				_k = (String) fields.get("id");
				_avgV = StringUtils.formatNumber((String) fields
						.get("avgvalue"), 2);
				_maxV = StringUtils.formatNumber((String) fields
						.get("maxvalue"), 2);
				_minV = StringUtils.formatNumber((String) fields
						.get("minvalue"), 2);
				_m.put(_k, _maxV + "#" + _minV + "#" + _avgV);
				// _m.put:"+_k+"#"+_maxV+"#"+_minV+"#"+_avgV);

				fields = _c.getNext();
			}
		}


		return _m;
	}

	/**
	 * 在小时报表数据中获取设备当天性能的最大最小平均值 type = 1
	 * 
	 * @return HashMap key:id value:maxvalue+"#"+minvalue+"#"+avgvalue
	 */
	public HashMap getDevHourTxtData() {
		HashMap _m = new HashMap();
		_m.clear();

		String tblName = this.getTxtTblName();
		// 构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(deviceHourDataSQL);
		pSQL.setStringExt(1, tblName, false);
		pSQL.setLong(2, start);
		pSQL.setLong(3, end);
		pSQL.setStringExt(4, StringUtils.weave(this.getArrId()), false);


		Cursor _c = DataSetBean.getCursor(pSQL.getSQL());


		if (_c != null && _c.getRecordSize() > 0) {
			Map fields = _c.getNext();
			String _k = "";
			String _avgV = "";
			String _maxV = "";
			String _minV = "";
			while (fields != null) {
				_k = (String) fields.get("id");
				_avgV = StringUtils.formatNumber((String) fields
						.get("avgvalue"), 2);
				_maxV = StringUtils.formatNumber((String) fields
						.get("maxvalue"), 2);
				_minV = StringUtils.formatNumber((String) fields
						.get("minvalue"), 2);
				_m.put(_k, _maxV + "#" + _minV + "#" + _avgV);
				// _m.put:"+_k+"#"+_maxV+"#"+_minV+"#"+_avgV);

				fields = _c.getNext();
			}
		}


		return _m;
	}

	/**
	 * 在小时报表数据中获取设备当天的所有实例的在线用户数性能的最大最小平均值 type = 1
	 * 
	 * @return HashMap key:id value:maxvalue+"#"+minvalue+"#"+avgvalue
	 */
	public String getDevClass7Data(String ids) {

		String[] _s = ids.split(",");
		for (int i = 0; i < _s.length; i++) {
			if (i == 0)
				ids = "'" + _s[0] + "'";
			else
				ids += ",'" + _s[i] + "'";
		}

		String tblName = this.getTxtTblName();
		// 构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(deviceClass7HourDataSQL);
		pSQL.setStringExt(1, tblName, false);
		pSQL.setLong(2, start);
		pSQL.setLong(3, end);
		pSQL.setStringExt(4, ids, false);


		HashMap _m = DataSetBean.getMap(pSQL.getSQL());


		if (_m != null && _m.size() > 0) {
			String _avgV = StringUtils.formatNumber(
					(String) _m.get("avgvalue"), 2);
			String _maxV = StringUtils.formatNumber(
					(String) _m.get("maxvalue"), 2);
			String _minV = StringUtils.formatNumber(
					(String) _m.get("minvalue"), 2);
			return _maxV + "#" + _minV + "#" + _avgV;

		}


		return " " + "#" + " " + "#" + " ";
	}

	public static  Cursor getAllDeviceClassData(HttpServletRequest request) {
		//用户选中得设备
		String device_id_str="";
		String device_ids_str =request.getParameter("device_ids");
		String[] device_ids= device_ids_str.split(",");
		for(int i=0;i<device_ids.length;i++)
		{
			if("".equals(device_id_str))
			{
				device_id_str="'"+device_ids[i]+"'";
			}
			else
			{
				device_id_str+=",'"+device_ids[i]+"'";
			}
		}
		//用户选中得性能表达式
		String class1=request.getParameter("typeList");
		
		//构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(allDeviceClassSql);
		pSQL.setStringExt(1,class1,false);
		pSQL.setStringExt(2,device_id_str,false);
		return DataSetBean.getCursor(pSQL.getSQL());
	}	
	

	public Cursor getAllDeviceGeneralTxtData(HttpServletRequest request) {
		String tblName = this.getTxtTblName();
		//用户选中得设备
		String device_id_str="";
		String device_ids_str =request.getParameter("device_ids");
		String[] device_ids= device_ids_str.split(",");
		for(int i=0;i<device_ids.length;i++)
		{
			if("".equals(device_id_str))
			{
				device_id_str="'"+device_ids[i]+"'";
			}
			else
			{
				device_id_str+=",'"+device_ids[i]+"'";
			}
		}
		//用户选中得性能表达式
		String class1=request.getParameter("typeList");
		
		//构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(allDeviceTxtSQL);
		pSQL.setStringExt(1, tblName, false);
		pSQL.setStringExt(2, class1, false);
		pSQL.setLong(3,start);
		pSQL.setLong(4,end);
		pSQL.setStringExt(5,device_id_str,false);

		/*String city_id = request.getParameter("city_id");// 所要过滤的属地编号
		String ifcontainChild = request.getParameter("ifcontainChild");// 是否包含所有下属属地
		String userIp = request.getParameter("userIp");// 用户输入IP
		// class1 性能表达式类型
		// 1 CPU利用率 2 内存利用率 3 地址池利用率 4 温度 5 电源 6 风扇 7 在线用户数 0 其它
		String class1 = request.getParameter("typeList");

		HttpSession session = request.getSession();
		DbUserRes dbUserRes = (DbUserRes) session.getAttribute("curUser");
		long area_id = dbUserRes.getAreaId();

		// 构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(allDeviceTxtSQL);
		pSQL.setStringExt(1, tblName, false);
		pSQL.setStringExt(2, class1, false);

		// 当为性能类型（class1）为 3 地址池利用率 或 7 在线用户数 时
		// 只查看所有性能实例的总体值 (indexid=-1)
		if (class1.equals("3") || class1.equals("7"))
			pSQL.setStringExt(3, "and c.indexid='-1'", false);
		else
			pSQL.setStringExt(3, "", false);

		pSQL.setLong(4, start);
		pSQL.setLong(5, end);
		pSQL.setStringExt(6, " ", false);

		// 属地过虑信息拼接
		if (ifcontainChild.equals("1")) {
			SelectCityFilter scf = new SelectCityFilter();
			pSQL.setStringExt(7, scf.getAllSubCityIds(city_id, true), false);
		} else {
			pSQL.setStringExt(7, city_id, true);
		}


		// 当用户指定IP时 拼接ip条件
		if (userIp != null && !userIp.equalsIgnoreCase(""))
			pSQL.setStringExt(8, "and e.loopback_ip in ('" + userIp + "')",
					false);
		else
			pSQL.setStringExt(8, "", false);

		*/
		 

		return DataSetBean.getCursor(pSQL.getSQL());
	}

	/**
	 * 获得图形数据
	 * 
	 * @return Cursor
	 */
	public Cursor getGeneralChartData() {
		String tblName = this.getChartTblName();
		// chartSQL = chartSQL.replaceFirst("{tablename}", tblName);
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			chartSQL = "select id, value, gathertime, maxvalue, minvalue, avgvalue  from ? where gathertime>=? and "
					+ "gathertime<? and id in(?)";
		}
		// 构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(chartSQL);
		pSQL.setStringExt(1, tblName, false);
		pSQL.setLong(2, start);
		pSQL.setLong(3, end);
		pSQL.setStringExt(4, StringUtils.weave(this.getArrId()), false);

		
		return DataSetBean.getCursor(pSQL.getSQL());
	}

	/**
	 * 获得图形数据 每个设备一个cursor 可遍历设备编号字符串ids 对每个设备生成Cursor
	 * 
	 * @return Cursor
	 */
	public Cursor[] getAllDeviceGeneralChartData(HttpServletRequest request) {

		String tblName = this.getChartTblName();
		String cols = this.getChartTblColume();
		String class1 = request.getParameter("class1");
		String strDevices = request.getParameter("device_id");
		
		PrepareSQL pSQL = new PrepareSQL(allDeviceChartSQL);
		String[] strDevice = strDevices.split(",");
		Cursor[] _c = new Cursor[strDevice.length];
		for (int i = 0; i < strDevice.length; i++) {
			pSQL.setStringExt(1, cols, false);
			pSQL.setStringExt(2, tblName, false);
			pSQL.setStringExt(3, class1, false);
			pSQL.setLong(4, start);
			pSQL.setLong(5, end);
			pSQL.setStringExt(6, strDevice[i], true);
			//pSQL.setStringExt(7, devIndexs, false);

			_c[i] = DataSetBean.getCursor(pSQL.getSQL());
		}

		return _c;
	}

	/**
	 * 过滤设备厂商
	 * 
	 * 
	 * @return Cursor
	 */
	public static Cursor getDeviceVendor() {
		PrepareSQL pSQL = new PrepareSQL(strSQLDeviceVendor);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		return cursor;
	}

	/**
	 * 过滤设备类型 Overloading by liuw FOR山东需求
	 * 
	 * @param strVendor
	 * @param city_id
	 * @param ifcontainChild
	 * @return
	 */
	public static Cursor getDeviceType(String strVendor, String city_id,
			String ifcontainChild) {

		PrepareSQL pSQL;
		Cursor cursor = new Cursor();
		pSQL = new PrepareSQL(strDeviceType);
		pSQL.setString(1, strVendor);

		if (ifcontainChild.equals("1")) {
			SelectCityFilter scf = new SelectCityFilter();
			pSQL.setStringExt(2, scf.getAllSubCityIds(city_id, true), false);
		} else {
			pSQL.setString(2, city_id);
		}

		cursor = DataSetBean.getCursor(pSQL.getSQL());


		return cursor;
	}

	/**
	 * 过滤设备名称/IP Overloading by liuw FOR山东需求
	 * 
	 * @param strType
	 * @param city_id
	 * @param ifcontainChild
	 * @return
	 */
	public static String getDeviceNameByType(String strType, String city_id,
			String ifcontainChild, List gather_id) {
		String htmlStr = "";

		PrepareSQL pSQL;
		Cursor cursor = new Cursor();
		pSQL = new PrepareSQL(strDeviceIP3);
		pSQL.setString(1, strType);
		pSQL.setStringExt(2, StringUtils.weave(gather_id), false);

		if (ifcontainChild.equals("1")) {
			SelectCityFilter scf = new SelectCityFilter();
			pSQL.setStringExt(3, scf.getAllSubCityIds(city_id, true), false);
		} else {
			pSQL.setString(3, city_id);
		}
		cursor = DataSetBean.getCursor(pSQL.getSQL());


		Map fields = cursor.getNext();
		if (fields != null) {
			while (fields != null) {
				htmlStr += "<input type='checkbox' name='dev_id' value='"
						+ fields.get("loopback_ip") + "'>"
						+ fields.get("device_name") + "/"
						+ fields.get("loopback_ip") + "<br>";
				fields = cursor.getNext();
			}
		} else {
			htmlStr += "没有可以选择的设备";
		}

		return htmlStr;
	}

	/**
	 * 根据用户帐号查询设备
	 * 
	 * @param username
	 *            用户帐号
	 * @param gather_id
	 *            采集点列表
	 * @return
	 */
	public static String getDeviceByUser(String username, List gather_id) {
		String htmlStr = "";

		String sql = "select b.loopback_ip,b.device_name from cus_radiuscustomer a,tab_gw_device b where a.device_id=b.device_id and a.username='"
				+ username
				+ "' and b.gather_id in ("
				+ StringUtils.weave(gather_id)
				+ ") and b.device_id in (select device_id from pm_map)";

		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();

		if (fields != null) {
			while (fields != null) {
				htmlStr += "<input type='checkbox' name='dev_id1' value='"
						+ fields.get("loopback_ip") + "'>"
						+ fields.get("device_name") + "/"
						+ fields.get("loopback_ip") + "<br>";
				fields = cursor.getNext();
			}
		} else {
			htmlStr += "没有可以选择的设备";
		}

		return htmlStr;
	}

}
