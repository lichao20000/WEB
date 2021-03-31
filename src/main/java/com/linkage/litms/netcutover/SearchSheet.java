/**
 * @(#)SearchSheetList.java 2006-1-17
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.netcutover;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.filter.SelectCityFilter;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.system.dbimpl.DbUserRes;
import com.linkage.module.gwms.Global;

/**
 * 查询工单信息.
 * <UL>
 * <LI>菜单：工单查询:根据JSP参数查取指定工单并分页显示.
 * <LI>查指定工单信息
 * <LI>查指定97原始工单信息
 * </UL>
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public class SearchSheet extends AbstractGetSheetList {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(SearchSheet.class);

	/** 获取工单列表数据SQL */
	private String txtSQL = "select * from ? where "
			+ " worksheet_receive_time>=? and worksheet_receive_time<=?"
			+ " and servtype<>28 and producttype in (3,31,5)"
			+ " and gather_id in (?)";

	/** sql:获取唯一工单(worksheet_id,gather_id) */
	private String sqlWorkSheetReport = "select * from ? where"
			+ " worksheet_id=? and gather_id in (?)";

	/** sql:获取唯一工单(sheet_id,system_id,gather_id) */
	private String sqlWorkSheetReport2 = "select * from ? where"
			+ " sheet_id=? and system_id=? and gather_id in (?)";

	/** sql:获取唯一97原始工单(work97id,productid,gather_id) */
	private String sql97original = "select * from ? where"
			+ " work97id=? and productid=? and gather_id in (?)";

	/** sql:获取唯一97原始工单(work97id,work97areaid,gather_id) */
	private String sql97original2 = "select * from ? where"
			+ " work97id=? and work97areaid=? and gather_id in (?)";

	/** sql:批量工单 */
	private String sqlBWS = "select * FROM ? where ?(product_id,1,3)='bws' and worksheet_receive_time>? and worksheet_receive_time<? and gather_id in (?) ";

    /** sql:批量工单查询 2006-8-5 wf 添加  */
	private String sqlBWS_chose = "select * FROM ? where ?(product_id,1,3)='bws' and worksheet_receive_time>? and worksheet_receive_time<? and gather_id in (?) and device_id in (select res_id from tab_gw_res_area where area_id= ? and res_type=1) ";

	/** sql:批量工单 用于打印 */
	private String sqlBWSPrint = "select * FROM ? where ?(product_id,1,3)='bws' and worksheet_receive_time>=? and worksheet_receive_time<=? and gather_id in (?) ";

	/** WorkSheet对象 */
	private ObjectWorkSheet workSheet;

	/** 页面最大记录数 */
	private int pagelen = 15;

	/** 页面偏移 */
	private String offset;

	/** 分页栏 */
	private String strBar;

	private HttpServletRequest request;

	/** JSP:session */
	private HttpSession session;

	private UserRes curUser;

	private List gather_id;

	/** PrepareSQL */
	private PrepareSQL pSQL = null;

	String worksheetID = "";

	String sheetID = "";

	String productID = "";

	String source = "";

	String receive_time = "";

	String systemID = "";

	String tabName = "gw_worksheet_report_";

	String tabName97 = "tab_97work_original_";
	
	public String tabName_1 = "";
	public String tabName97_1 = "";
    
    //----------------------属地过滤 add by YYS 2006-9-26 ---------------
    String city_id = "";//过滤属地编号
    
    String ifcontainChild = "";//是否向下级联
    //-------------------------------------------------------------------

	public SearchSheet() {

	}

	/**
     * modify by YYS 2006-09-27
     * @param request
	 */
    public void setRequest(HttpServletRequest request) {
		this.request = request;
		session = request.getSession();
		curUser = (UserRes) session.getAttribute("curUser");
		gather_id = curUser.getUserProcesses();

		worksheetID = request.getParameter("worksheet_id");
		sheetID = request.getParameter("sheet_id");
		productID = request.getParameter("product_id");
		source = request.getParameter("source");
		receive_time = request.getParameter("receive_time");
		systemID = request.getParameter("system_id");
        
		//----------------------属地过滤 add by YYS 2006-9-26 ---------------
        city_id = request.getParameter("hid_city_id");
        ifcontainChild = request.getParameter("ifcontainChild");
        //-------------------------------------------------------------------

		if (receive_time != null && receive_time.length() > 7) {
			tabName += receive_time.substring(0, 4)
					+ receive_time.substring(5, 7);
			tabName97 += receive_time.substring(0, 4)
					+ receive_time.substring(5, 7);
			tabName_1 ="snmp_worksheet_report_"+ receive_time.substring(0, 4)
			+ receive_time.substring(5, 7);
	         tabName97_1 = "tab_97work_original_"+ receive_time.substring(0, 4)
			+ receive_time.substring(5, 7);
		}
	}

	/**
	 * @param start
	 * @param end
	 * @param type
	 * @param productType
	 * @param workSheet
	 *            WorkSheet
	 */
	public SearchSheet(long time, int type, int productType,
			ObjectWorkSheet workSheet) {
		super(time, type, productType);
		this.workSheet = workSheet;
	}

	/**
	 * Constractor: 获取表名列表.
	 * 
	 * @param time
	 *            时间 表示 1970 年1月1日 00:00:00 GMT 以后 time 秒的时间点 长整形
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
	 * @param obj97orginal
	 *            工单对象 Object97orginal
	 */
	public SearchSheet(long time, int type, int productType,
			Object97orginal obj97orginal) {
		super(time, type, productType);
	}

	/**
	 * 获取唯一工单(worksheet_id,gather_id)
	 * 
	 * @return 唯一工单 Map
	 */
	public Map getWorkSheetRecord() {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlWorkSheetReport = "select username, worksheet_source, system_id, sheet_id, product_id, worksheet_receive_time," +
					" producttype, worksheet_exec_time, servtype, worksheet_start_time, worksheet_end_time, worksheet_priority," +
					" worksheet_desc, worksheet_error_desc " +
					" from ? where"
					+ " worksheet_id=? and gather_id in (?)";
		}
		// 构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(sqlWorkSheetReport);
		pSQL.setStringExt(1, tabName, false);
		pSQL.setString(2, worksheetID);
		pSQL.setStringExt(3, StringUtils.weave(gather_id), false);

		return DataSetBean.getRecord(pSQL.getSQL());
	}

	/**
	 * 获取唯一工单(sheet_id,system_id,gather_id)
	 * 
	 * @return 唯一工单 Map
	 */
	public Map getWorkSheetRecord2() {
		// 构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(sqlWorkSheetReport2);
		pSQL.setStringExt(1, tabName, false);
		pSQL.setString(2, sheetID);
		pSQL.setString(3, systemID);
		pSQL.setStringExt(4, StringUtils.weave(gather_id), false);

		return DataSetBean.getRecord(pSQL.getSQL());
	}

	/**
	 * 获取唯一97原始工单(work97id,productid,gather_id)
	 * 
	 * @return
	 */
	public Map get97orginalRecord() {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql97original = "select office_id, zone_id, adslbindphone, dslam_ban_old_devicecode, dslam_ban_old_ip, " +
					"dslam_ban_old_shelf, dslam_ban_old_frame, dslam_ban_old_slot, dslam_ban_old_port, dslam_ban_devicecode, " +
					"dslam_ban_ip, dslam_ban_shelf, dslam_ban_frame, dslam_ban_slot, dslam_ban_port, maxdownstreamrate, " +
					"maxupstreamrate, oper_accounts, deviceencode from ? where"
					+ " work97id=? and productid=? and gather_id in (?)";
		}

		// 构造预处理SQL
		pSQL = new PrepareSQL(sql97original);
		pSQL.setStringExt(1, tabName97, false);
		pSQL.setString(2, sheetID);
		pSQL.setString(3, productID);
		pSQL.setStringExt(4, StringUtils.weave(gather_id), false);

		return DataSetBean.getRecord(pSQL.getSQL());
	}

	/**
	 * 获取唯一97原始工单(work97id,productid,gather_id)
	 * 
	 * @return
	 */
	public Map get97orginalRecord2() {
		// 构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(sql97original2);
		pSQL.setStringExt(1, tabName97, false);
		pSQL.setString(2, sheetID);
		pSQL.setString(3, systemID);
		pSQL.setStringExt(4, StringUtils.weave(gather_id), false);

		return DataSetBean.getRecord(pSQL.getSQL());
	}

	/**
	 * 根据是JSP页面查询工单列表.
	 * 
	 * @see com.linkage.liposs.netcutover.AbstractGetSheetList#getSheetList()
	 */
	public Cursor getSheetList() {
		String tabName = this.getTabName();
		txtSQL = txtSQL.replaceFirst("tablename", tabName);

		if (!workSheet.getUsername().equals("")) { // Usernam
			txtSQL += " and  username='" + workSheet.getUsername() + "'";
		}
		if (!workSheet.getSheet_id().equals("")) { // sheet_id
			txtSQL += " and sheet_id='" + workSheet.getSheet_id() + "'";
		}
		if (workSheet.getServtype() != -1) { // sheet_id
			txtSQL += " and servtype=" + workSheet.getServtype();
		}
		if (workSheet.getWorksheet_id().length() > 0) { // worksheet_id
			txtSQL += " and worksheet_id='" + workSheet.getWorksheet_id() + "'";
		}
		if (workSheet.getProduct_id().length() > 0) { // product_id
			txtSQL += " and product_id='" + workSheet.getWorksheet_id() + "'";
		}
		// worksheet_status
		if (workSheet.getWorksheet_status().equals("11")) { // 手工激活
			txtSQL += " and worksheet_status=2 and worksheet_error_no<>0";
		} else if (!workSheet.getWorksheet_status().equals("-1")) {
			txtSQL += " and worksheet_status="
					+ workSheet.getWorksheet_status();
		}
		// worksheet_source
		if (workSheet.getWorksheet_source() != -1) {
			txtSQL += " and worksheet_source="
					+ workSheet.getWorksheet_source();
		}
		// system_id
		if (!workSheet.getSystem_id().equals("-1")) {
			txtSQL += " and system_id='" + workSheet.getSystem_id() + "'";
		}
		// deviceencode苏州13位设备编码处理
		if (workSheet.getDeviceencode().length() == 13) {
			txtSQL += " and deviceencode='" + workSheet.getDeviceencode() + "'";
		}
		txtSQL += " ORDER BY worksheet_receive_time desc";

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
		pSQL.setStringExt(3, StringUtils.weave(gather_id), true);

		// 页面偏移
		int tmpOffset;
		if (offset == null)
			tmpOffset = 1;
		else
			tmpOffset = Integer.parseInt(offset);

		QueryPage gryp = new QueryPage();
		gryp.initPage(pSQL.getSQL(), tmpOffset, pagelen);
		strBar = gryp.getPageBar();

		return DataSetBean.getCursor(pSQL.getSQL(), tmpOffset, pagelen);
	}

	/**
	 * modify by YYS 2006-09-26
	 * 
	 * @param request
	 * @return
	 */
	public Cursor getSearchList(HttpServletRequest request) {

		String strSQL = null;
		String tmpRequest = "";
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes)session.getAttribute("curUser");
		// 若为检索状态
		if (request.getParameter("submit") != null) {
			
            //----------------------属地过滤 add by YYS 2006-9-26 ---------------
            String cityid = request.getParameter("hid_city_id");
            logger.debug("cityid111：===>" + cityid);
            String ifcontainChild = request.getParameter("ifcontainChild");            
            tmpRequest = "&hid_city_id=" + cityid + "&ifcontainChild=" + ifcontainChild;
            
            if(ifcontainChild.equals("1")) {
                SelectCityFilter scf = new SelectCityFilter();
                cityid = scf.getAllSubCityIds(cityid, true);
            }else {
                cityid = "'"+cityid+"'";
            }
            logger.debug("cityid222：===>" + cityid);
            //-------------------------------------------------------------------
            
			String start = request.getParameter("start"); // 开始
			String startTime = request.getParameter("startTime");
			String end = request.getParameter("end"); // 结束
			String endTime = request.getParameter("endTime");
			String worksheet_status = request.getParameter("worksheet_status"); // 工单状态
			String sheet_id = request.getParameter("sheet_id");
			//String product_id = request.getParameter("product_id");
			// add by  xiaoxf 实现lan 查询  需要同时更新  AdslWork_Search.jsp
			String operation = request.getParameter("operation"); // 业务类型
			  logger.debug("operation：===>" + operation);
			String worksheet_source = request.getParameter("worksheet_source"); // 来自何处

			String phone = request.getParameter("phone"); // 电话号码
			String username = request.getParameter("username");
			if (worksheet_source.equals("0")) {
				strSQL = "select worksheet_status,worksheet_id,worksheet_exec_time,sheet_id,"
					+ "product_id,worksheet_receive_time,username,worksheet_end_time,"
					+ "producttype,servtype,worksheet_error_desc FROM tableName where"
					+ " producttype="+operation;
			} else if (worksheet_source.equals("1")) {
				strSQL = "select worksheet_status,worksheet_id,worksheet_exec_time,sheet_id,"
					+ "product_id,worksheet_receive_time,username,worksheet_end_time,"
					+ "producttype,servtype,worksheet_error_desc FROM tableName where" + " producttype="+operation+"1";
			} else {
				strSQL = "select worksheet_status,worksheet_id,worksheet_exec_time,sheet_id,"
					+ "product_id,worksheet_receive_time,username,worksheet_end_time,"
					+ "producttype,servtype,worksheet_error_desc FROM tableName where"
					+ " (producttype="+operation+"1 or producttype="+operation+")";
			}
			 logger.debug("operation：===>" + operation);
			tmpRequest += "&start=" + start
					+ "&startTime=" + startTime + "&end=" + end + "&endTime="
					+ endTime + "&worksheet_status=" + worksheet_status
					+ "&sheet_id=" + sheet_id + "&operation=" + operation
					+ "&worksheet_source=" + worksheet_source + "&phone="
					+ phone + "&username=" + username;

			String servtype = request.getParameter("servtype");
			String worksheet_id = request.getParameter("worksheet_id");
			
			strSQL += (sheet_id.length() > 0) ? (" and sheet_id='" + sheet_id + "'")
					: "";
			strSQL += (start.length() > 0) ? (" and worksheet_receive_time between '"
					+ start + " " + startTime + "'")
					: "";
			strSQL += (end.length() > 0) ? (" and '" + end + " " + endTime + "'")
					: "";
			if (worksheet_status.equals("11")) {
				strSQL += " and worksheet_status=2 and worksheet_error_no<>0";
			} else {
				strSQL += (!worksheet_status.equals("null")) ? (" and worksheet_status=" + worksheet_status)
						: "";
			}
			strSQL += (username.length() > 0) ? (" and username='" + username + "' ")
					: "";
			strSQL += (!servtype.equals("-1")) ? (" and servtype=" + servtype)
					: "";
			strSQL += (worksheet_id.length() > 0) ? (" and worksheet_id='"
					+ worksheet_id + "' ") : "";
			
			//苏州13位设备编码
			boolean bIsSZ = SelectCityFilter.isSZ(curUser.getCityId());
			if (bIsSZ) {
				String portCode = request.getParameter("portCode");
				if(null != portCode) {
					portCode = portCode.trim(); 
					if (portCode.length() == 13) {
						strSQL += " and deviceencode ='" + portCode + "'";
					}
				}
			}

			// 表示本次检索条件是手工添加的
			if (cityid != null && !"-1".equals(cityid)) {
				strSQL += " and system_id in (" + cityid + ") ";
			}
		}

		strSQL += " and servtype<>28 ORDER BY worksheet_receive_time desc";

		strSQL = strSQL.replaceFirst("tableName", "snmp_worksheet_report_"
				+ (String) request.getParameter("tableTime"));

		tmpRequest += "&tableTime="
				+ (String) request.getParameter("tableTime") + "&portCode="
				+ (String) request.getParameter("portCode");

		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		return DataSetBean.getCursor(strSQL);
	}

	public Cursor getBWSData() {
		String lms_sendtime = request.getParameter("lms_sendtime");
		String sendtime = request.getParameter("day");
		String username = request.getParameter("username");
		String servtype = request.getParameter("servtype");
		String worksheet_status = request.getParameter("worksheet_status");
		String offset = request.getParameter("offset");
		String tmpRequest = "&lms_sendtime=" + lms_sendtime + "&day="
				+ sendtime + "&username=" + username + "&servtype=" + servtype
				+ "&worksheet_status=" + worksheet_status;

		String start_sendtime = sendtime + " 00:00:00";
		String end_sendtime = sendtime + " 23:59:59";

		HttpSession session = request.getSession();
		DbUserRes dbUserRes = (DbUserRes) session.getAttribute("curUser");
		List gather_id = dbUserRes.getUserProcesses();

		if (username != null && !username.equals("")) {
			sqlBWS += " and username='" + username + "'";
		}

		if (servtype != null && !servtype.equals("-1")) {
			sqlBWS += " and servtype=" + servtype;
		}

		if (worksheet_status != null && !worksheet_status.equals("-1")) {
			sqlBWS += " and worksheet_status=" + worksheet_status;
		}

		sqlBWS += "order by worksheet_receive_time desc,sheet_id";

		String year = sendtime.substring(0, 4);
		String month = sendtime.substring(5, 7);
		if (sendtime.substring(6, 7).equals("-"))
			month = "0" + sendtime.substring(5, 6);
		PrepareSQL pSQL = new PrepareSQL(sqlBWS);
		pSQL.setStringExt(1, "snmp_worksheet_report_" + year + month, false);
		pSQL.setStringExt(2, StringUtils.getSubstrFun(), false);
		pSQL.setString(3, start_sendtime);
		pSQL.setString(4, end_sendtime);
		pSQL.setStringExt(5, StringUtils.weave(gather_id), false);
		// pSQL.setStringExt(5,"order by sheet_id,worksheet_receive_time
		// desc",false);
		// 页面偏移
		int tmpOffset;
		if (offset == null)
			tmpOffset = 1;
		else
			tmpOffset = Integer.parseInt(offset);

		QueryPage gryp = new QueryPage();
		gryp.initPage(pSQL.getSQL(), tmpOffset, pagelen);
		strBar = gryp.getPageBar(tmpRequest);

		return DataSetBean.getCursor(pSQL.getSQL(), tmpOffset, pagelen);
	}

    /**
     * 选择工单并对不同的区域进行筛选 modify by YYS 2006-09-27
     * @author wangfeng 2006-8-5 
     * @param  userRes
     * @return 查询的结果集
     */
	public Cursor getBWSDataAfterChose(UserRes userRes) {
		String lms_sendtime = request.getParameter("lms_sendtime");
		String sendtime = request.getParameter("day");
		String username = request.getParameter("username");
		String servtype = request.getParameter("servtype");
		String worksheet_status = request.getParameter("worksheet_status");
		String offset = request.getParameter("offset");
		String tmpRequest = "&lms_sendtime=" + lms_sendtime + "&day="
				+ sendtime + "&username=" + username + "&servtype=" + servtype
				+ "&worksheet_status=" + worksheet_status + "&ifcontainChild=" + ifcontainChild
				+ "&hid_city_id="+city_id;

		String start_sendtime = sendtime + " 00:00:00";
		String end_sendtime = sendtime + " 23:59:59";

		HttpSession session = request.getSession();
		DbUserRes dbUserRes = (DbUserRes) session.getAttribute("curUser");
		List gather_id = dbUserRes.getUserProcesses();

        //----------------------属地过滤 add by YYS 2006-9-27 ---------------
        if(ifcontainChild.equals("1")) {
            SelectCityFilter scf = new SelectCityFilter();
            city_id = scf.getAllSubCityIds(city_id, true);
        }else {
            city_id = "'"+city_id+"'";
        }
        
        sqlBWS_chose += " and system_id in("+city_id+") ";
        //-------------------------------------------------------------------
        
        
        if (username != null && !username.equals("")) {
			sqlBWS_chose += " and username='" + username + "'";
		}

		if (servtype != null && !servtype.equals("-1")) {
			sqlBWS_chose += " and servtype=" + servtype;
		}

		if (worksheet_status != null && !worksheet_status.equals("-1")) {
			sqlBWS_chose += " and worksheet_status=" + worksheet_status;
		}

		sqlBWS_chose += " order by worksheet_receive_time desc,sheet_id";

		String year = sendtime.substring(0, 4);
		String month = sendtime.substring(5, 7);
		if (sendtime.substring(6, 7).equals("-"))
			month = "0" + sendtime.substring(5, 6);
		PrepareSQL pSQL = new PrepareSQL(sqlBWS_chose);
		pSQL.setStringExt(1, "snmp_worksheet_report_" + year + month, false);
		pSQL.setStringExt(2, StringUtils.getSubstrFun(), false);
		pSQL.setString(3, start_sendtime);
		pSQL.setString(4, end_sendtime);
		pSQL.setStringExt(5, StringUtils.weave(gather_id), false);
		pSQL.setLong(6, userRes.getAreaId()); 
		int tmpOffset;
		if (offset == null)
			tmpOffset = 1;
		else
			tmpOffset = Integer.parseInt(offset);

		QueryPage gryp = new QueryPage();
		gryp.initPage(pSQL.getSQL(), tmpOffset, pagelen);
		strBar = gryp.getPageBar(tmpRequest);

		return DataSetBean.getCursor(pSQL.getSQL(), tmpOffset, pagelen);
	}


	/**
	 * 导出EXCEL调用得方法 by liuw 2006-6-12
	 * 
	 * @return
	 */
	public Cursor getBWSDataPrint() {
		String lms_sendtime = request.getParameter("lms_sendtime");
		String sendtime = request.getParameter("day");
		String username = request.getParameter("username");
		String servtype = request.getParameter("servtype");
		String worksheet_status = request.getParameter("worksheet_status");
		String start_sendtime = lms_sendtime;
		String end_sendtime = lms_sendtime;

		HttpSession session = request.getSession();
		DbUserRes dbUserRes = (DbUserRes) session.getAttribute("curUser");
		List gather_id = dbUserRes.getUserProcesses();

		if (username != null && !username.equals("")) {
			sqlBWSPrint += " and username='" + username + "'";
		}

		if (servtype != null && !servtype.equals("-1")) {
			sqlBWSPrint += " and servtype=" + servtype;
		}

		if (worksheet_status != null && !worksheet_status.equals("-1")) {
			sqlBWSPrint += " and worksheet_status=" + worksheet_status;
		}

		sqlBWSPrint += "order by worksheet_receive_time desc,sheet_id";

		String year = sendtime.substring(0, 4);
		String month = sendtime.substring(5, 7);
		if (sendtime.substring(6, 7).equals("-"))
			month = "0" + sendtime.substring(5, 6);
		PrepareSQL pSQL = new PrepareSQL(sqlBWSPrint);
		pSQL.setStringExt(1, "snmp_worksheet_report_" + year + month, false);
		pSQL.setStringExt(2, StringUtils.getSubstrFun(), false);
		pSQL.setString(3, start_sendtime);
		pSQL.setString(4, end_sendtime);
		pSQL.setStringExt(5, StringUtils.weave(gather_id), false);


		return DataSetBean.getCursor(pSQL.getSQL());
	}

	/**
	 * 获取JSP页面分页栏,需先调用getSheetList（）方法.
	 * 
	 * @return strBar String
	 */
	public String getStrBar() {
		return strBar;
	}

	/**
	 * 检测未发送成功工单.
	 * 
	 * @return
	 */
	public Cursor getBWSSheetChecked() {
		String sendtime = request.getParameter("sendtime");
		String start_sendtime = sendtime + " 00:00:00";
		String end_sendtime = sendtime + " 23:59:59";

		String year = sendtime.substring(0, 4);
		String month = sendtime.substring(5, 7);
		if (sendtime.substring(6, 7).equals("-"))
			month = "0" + sendtime.substring(5, 6);

		PrepareSQL pSQL = new PrepareSQL(sqlBWS);
		pSQL.setStringExt(1, "snmp_worksheet_report_" + year + month, false);
		pSQL.setStringExt(2, StringUtils.getSubstrFun(), false);
		pSQL.setString(3, start_sendtime);
		pSQL.setString(4, end_sendtime);
		pSQL.setStringExt(5, StringUtils.weave(gather_id), false);

		return DataSetBean.getCursor(pSQL.getSQL());
	}
}