package com.linkage.litms.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.confTaskView.StrategyConfigInterfaceAction;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DbUtils;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.filter.SelectCityFilter;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.paramConfig.ParamInfoCORBA;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.system.dbimpl.AreaManageSyb;
import com.linkage.litms.webtopo.MCControlManager;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 现场安装
 * 
 * @author zhaixf(3412);Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Aug 24, 2009
 * @see
 * @since 1.0
 * @desc 2008-04-22 JSDX_ITMS-BUG-YHJ-20080421-001
 */
@SuppressWarnings( { "unchecked", "unused" })
public class UserInstAct {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(UserInstAct.class);

	private Cursor cursor = null;

	private Map fields = null;

	private int offset;

	private int MaxLine;

	private int total;

	private int curPage;

	private int totalPage;

	private String query;

	private HttpServletRequest requestTmp = null;

	long curTime1 = (new Date()).getTime() / 1000;

	private long userid = 0;

	// 现场安装及新增用户都把用户线路置1
	// iposs为空；BSS为0
	public static int ITMSUSERLINE = 1;

	/**
	 * 查询未确认的设备列表 modify by zhaixf
	 * 
	 * @param request
	 * @return
	 */
	public List getDeviceInfoList(HttpServletRequest request) {
		logger.debug("getDeviceInfoList(request)");

		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();
		String sql = "select city_id,parent_id from tab_city";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map map = DataSetBean.getMap(sql);

		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice;

		// 用户类型 1：家庭网关 2：企业网关
		String gw_type = request.getParameter("gw_type");

		sqlDevice = "select a.* from tab_gw_device a  where a.device_status=0 ";

		if ("1".equals(gw_type)) {
			sqlDevice += " and a.gw_type=1";
		} else if ("2".equals(gw_type)) {
			sqlDevice += " and a.gw_type=2";
		}

		String device_serialnumber = request
				.getParameter("device_serialnumber");
		if (device_serialnumber != null && !device_serialnumber.equals("")) {
			if(device_serialnumber.length()>5){
				sqlDevice += " and a.dev_sub_sn ='" + device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length()) + "'";
			}
			sqlDevice += " and a.device_serialnumber like '%"
					+ device_serialnumber + "'";
		}

		String pcityId = (String) map.get(city_id);
		if (!pcityId.equals("-1")) {
			sqlDevice += " and a.city_id in('" + pcityId + "','" + city_id
					+ "','00') ";
		}

		// 查找设备条件
		String stroffset = request.getParameter("offset");

		psql = new PrepareSQL(sqlDevice);
		psql.getSQL();

		String strBar = "";
		int pagelen = 10;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);

		initPage(sqlDevice, offset, pagelen);
		cursor = DataSetBean.getCursor(sqlDevice, offset, pagelen);

		strBar = getPageBar();

		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/*
	 * 查询没有用户关联的设备列表(现场安装) modify by zhaixf
	 */
	public List getAllDeviceInfoList(HttpServletRequest request) {

		String city_id = request.getParameter("city_id");
		String device_serialnumber = request
				.getParameter("device_serialnumber");

		if (city_id == null || city_id.equals("")) {
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			city_id = curUser.getCityId();
		}

		String sql = "select city_id,parent_id from tab_city";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map map = DataSetBean.getMap(sql);

		ArrayList list = new ArrayList();
		list.clear();

		// String sqlDevice =
		// "select a.* from tab_gw_device a where a.device_status != -1 ";

		/*
		 * String tabName = ""; if (gw_type != null && "1".equals(gw_type)) {
		 * sqlDevice += " and a.gw_type=1 "; tabName = "tab_hgwcustomer";
		 * 
		 * }else if (gw_type != null && "2".equals(gw_type)) { sqlDevice += "
		 * and a.gw_type=2 "; tabName = "tab_egwcustomer"; }else { tabName =
		 * "tab_hgwcustomer"; }
		 */

		// 用户类型 1：家庭网关 2：企业网关
		String gw_type = request.getParameter("gw_type");

		String sqlDevice = "select a.* from tab_gw_device a where a.cpe_allocatedstatus=0 ";

		if (gw_type != null && "1".equals(gw_type)) {
			sqlDevice += " and a.gw_type=1 ";

		} else if (gw_type != null && "2".equals(gw_type)) {
			sqlDevice += " and a.gw_type=2 ";
		}

		if (device_serialnumber != null && !device_serialnumber.equals("")) {
			if(device_serialnumber.length()>5){
				sqlDevice += " and a.dev_sub_sn ='" + device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length()) + "'";
			}
			sqlDevice += " and a.device_serialnumber like '%"
					+ device_serialnumber + "'";
		}
		if (!LipossGlobals.isXJDX()) {
			String pcityId = (String) map.get(city_id);
			if (!pcityId.equals("-1")) {
				sqlDevice += " and a.city_id in('" + pcityId + "','" + city_id
						+ "','00') ";
			}
		}
		/*
		 * sqlDevice += " and not exists (select b.oui from " + tabName + " b ";
		 * sqlDevice += "where b.user_state = '1' and a.oui = b.oui and
		 * a.device_serialnumber = b.device_serialnumber)" ;
		 */
		// 查找设备条件
		String stroffset = request.getParameter("offset");

		psql = new PrepareSQL(sqlDevice);
		psql.getSQL();

		String strBar = "";
		int pagelen = 10;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);

		initPage(sqlDevice, offset, pagelen);
		cursor = DataSetBean.getCursor(sqlDevice, offset, pagelen);

		strBar = getPageBar();

		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/**
	 * 
	 * UserInstAct.java
	 * 
	 * @param request
	 * @return Map
	 */
	public Map getDeviceUserInfo(HttpServletRequest request) {

		Map map = new HashMap();

		String gw_type = request.getParameter("gw_type");
		String device_serialnumber = request
				.getParameter("device_serialnumber");
		StringBuilder strbuildSql = new StringBuilder();
		String tabName = " tab_hgwcustomer";
		strbuildSql
				.append("select username, city_id, device_serialnumber from ");

		if (gw_type != null && "2".equals(gw_type)) {
			tabName = " tab_egwcustomer";
		}
		strbuildSql.append(tabName);

		if (device_serialnumber != null && !device_serialnumber.equals("")) {
			strbuildSql.append(" where device_serialnumber like '%");
			strbuildSql.append(device_serialnumber);
			strbuildSql.append("'");
		}
		PrepareSQL psql = new PrepareSQL(strbuildSql.toString());
		psql.getSQL();
		return DataSetBean.getRecord(strbuildSql.toString());
	}

	/**
	 * 企业网关查询所有未绑定的设备
	 * 
	 * @param request
	 * @return
	 */
	public List getAllDeviceInfoList_bbms(HttpServletRequest request) {

		String city_id = request.getParameter("city_id");
		String device_serialnumber = request
				.getParameter("device_serialnumber");

		if (city_id == null || city_id.equals("")) {
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			city_id = curUser.getCityId();
		}

		String sql = "select city_id,parent_id from tab_city";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map map = DataSetBean.getMap(sql);

		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice = "select a.* from tab_gw_device a where a.cpe_allocatedstatus=0 and a.gw_type=2 and a.device_status in(0,1) ";

		if (device_serialnumber != null && !device_serialnumber.equals("")) {
			if(device_serialnumber.length()>5){
				sqlDevice += " and a.dev_sub_sn ='" + device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length()) + "'";
			}
			sqlDevice += " and a.device_serialnumber like '%"
					+ device_serialnumber + "'";
		}

		// if(!LipossGlobals.isXJDX()){
		// String pcityId = (String) map.get(city_id);
		// if (!pcityId.equals("-1")){
		// sqlDevice += " and a.city_id in('"+pcityId+"','"+city_id+"','00') ";
		// }
		// }
		// 查找设备条件
		String stroffset = request.getParameter("offset");

		psql = new PrepareSQL(sqlDevice);
		psql.getSQL();

		String strBar = "";
		int pagelen = 10;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);

		initPage(sqlDevice, offset, pagelen);
		cursor = DataSetBean.getCursor(sqlDevice, offset, pagelen);

		strBar = getPageBar();

		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/**
	 * 
	 * @param username
	 * @param cityId
	 * @return
	 */
	public Cursor getCustomerInfo_bbms(String username, String cityId) {

		// String sql = "select * from tab_customerinfo where
		// customer_id=(select customer_id from tab_egwcustomer where
		// username='"
		// + username + "' and user_state='1')";

		String sql = "select b.* from tab_egwcustomer a, tab_customerinfo b "
				+ " where a.customer_id=b.customer_id and a.username='"
				+ username + "' and a.user_state='1'";
		if (!"00".equals(cityId)) {
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql += " and b.city_id in ("
					+ StringUtils.weave(list)
					+ ",'')";
			list = null;
		}

		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		return cursor;
	}

	/**
	 * 检查该用户是否已绑定其它设备 TODO
	 * 
	 * @return
	 */
	public String checkUser(String username, String cityId) {
		String value = null;
		String sqlUser = "select a.oui,a.device_serialnumber from tab_egwcustomer a, tab_customerinfo b "
				+ "where a.customer_id = b.customer_id and a.username='"
				+ username + "' and a.user_state in ('1','2')";

		if (!"00".equals(cityId)) {
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sqlUser += " and b.city_id in ("
					+ StringUtils.weave(list)
					+ ",'')";
			list = null;
		}

		PrepareSQL psql = new PrepareSQL(sqlUser);
		psql.getSQL();

		// 查看用户是否绑定了设备,如果绑定了设备返回绑定信息
		Map fields = DataSetBean.getRecord(sqlUser);

		if (fields != null) {
			String _oui = (String) fields.get("oui");
			String _device_serialnumber = (String) fields
					.get("device_serialnumber");

			if (_oui != null && _device_serialnumber != null
					&& !"".equals(_oui) && !"".equals(_device_serialnumber)) {
				value = _oui + "|" + _device_serialnumber;
			}
		}
		return value;
	}

	/**
	 * 企业网关查询未绑定设备
	 * 
	 * @param request
	 * @return
	 */
	public List getDeviceInfoList_bbms(HttpServletRequest request) {

		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();
		String sql = "select city_id,parent_id from tab_city";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map map = DataSetBean.getMap(sql);

		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice;

		// 用户类型 1：家庭网关 2：企业网关
		String gw_type = request.getParameter("gw_type");

		sqlDevice = "select a.* from tab_gw_device a  where a.device_status=0 and a.gw_type=2 and a.device_status in(0,1) ";

		String device_serialnumber = request
				.getParameter("device_serialnumber");
		if (device_serialnumber != null && !device_serialnumber.equals("")) {
			if(device_serialnumber.length()>5){
				sqlDevice += " and a.dev_sub_sn ='" + device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length()) + "'";
			}
			sqlDevice += " and a.device_serialnumber like '%"
					+ device_serialnumber + "'";
		}

		// String pcityId = (String) map.get(city_id);
		// if (!pcityId.equals("-1")){
		// sqlDevice += " and a.city_id in('"+pcityId+"','"+city_id+"','00') ";
		// }

		// 查找设备条件
		String stroffset = request.getParameter("offset");

		psql = new PrepareSQL(sqlDevice);
		psql.getSQL();

		String strBar = "";
		int pagelen = 10;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);

		initPage(sqlDevice, offset, pagelen);
		cursor = DataSetBean.getCursor(sqlDevice, offset, pagelen);

		strBar = getPageBar();

		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/**
	 * 现场安装配置的数据处理
	 * 
	 * @param request
	 * @return
	 */
	public String saveConfig(HttpServletRequest request) {
		logger.debug("saveConfig(request)");

		String batchSQL = "";
		requestTmp = request;
		String gather_id = "-1";
		String city_id = "";
		String gw_type = "1";
		long area_id = 0;
		int retMsg = 0;
		String oui = "";
		String device_serialnumber = "";
		String devicetype_id = "";
		String passwd = "";

		String device_id = request.getParameter("selDevice");
		String username = request.getParameter("username");
		String serialnumber = request.getParameter("serialnumber");

		// 获取当前操作人员的信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");

		city_id = curUser.getCityId();
		area_id = curUser.getAreaId();

		String dealStaff = curUser.getUser().getAccount();

		logger.warn("现场安装信息:device_id={},username={},city_id={},dealStaff={}",
				new Object[] { device_id, username, city_id, dealStaff });
		if (device_id == null || "".equals(device_id)) {
			logger.warn("没有选择需要安装的设备，请重新操作！");

			return "没有选择需要安装的设备，请重新操作！";
		}
		PrepareSQL psql = new PrepareSQL("select gather_id,oui,device_serialnumber,gw_type,devicetype_id from tab_gw_device where device_id = '"
				+ device_id + "' ");
		psql.getSQL();
		// 查询设备信息
		cursor = DataSetBean
				.getCursor("select gather_id,oui,device_serialnumber,gw_type,devicetype_id from tab_gw_device where device_id = '"
						+ device_id + "' ");

		fields = cursor.getNext();
		if (fields != null) {
			gather_id = (String) fields.get("gather_id");
			oui = (String) fields.get("oui");
			device_serialnumber = (String) fields.get("device_serialnumber");
			gw_type = (String) fields.get("gw_type");
			devicetype_id = (String) fields.get("devicetype_id");
		} else {
			logger.warn("无法查询到指定的新设备:{}", device_id);

			return "无法查询到指定的新设备！";
		}

//		String tab_name = "tab_hgwcustomer";
//		if (gw_type.equals("2")) {
//			tab_name = "tab_egwcustomer";
//		}
		String tab_name = "tab_hgwcustomer";
		if(false == LipossGlobals.IsITMS()){
			tab_name = "tab_egwcustomer";
		}
		psql = new PrepareSQL("select oui,device_serialnumber,city_id,device_id from "
				+ tab_name + " where username='" + username
				+ "' and user_state in ('1','2')");
		psql.getSQL();
		// 查看用户是否绑定了设备,如果绑定了设备返回绑定信息
		fields = DataSetBean
				.getRecord("select oui,device_serialnumber,city_id,device_id from "
						+ tab_name + " where username='" + username
						+ "' and user_state in ('1','2')");
		if (fields != null) {

			String _oui = (String) fields.get("oui");
			String _device_serialnumber = (String) fields
					.get("device_serialnumber");

			// 用户属地
			city_id = String.valueOf(fields.get("city_id"));

//			if (_oui != null && _device_serialnumber != null
//					&& !"".equals(_oui) && !"".equals(_device_serialnumber)) {
			if (false == "".equals(fields.get("device_id"))) {
				
				logger.warn("该用户已绑定设备，请重试！");

				return "该用户已绑定设备，请重试！";

			}

		}

		// 判断该设备是否已绑定用户
		String count = "";
//		cursor = DataSetBean.getCursor("select count(1) as total from "
//				+ tab_name + "  where oui = '" + oui
//				+ "' and device_serialnumber = '" + device_serialnumber
//				+ "' and user_state='1'");
		psql = new PrepareSQL("select count(1) as total from "
				+ tab_name + " where device_id = '" + device_id
				+ "' and user_state='1'");
		psql.getSQL();
		cursor = DataSetBean.getCursor("select count(1) as total from "
				+ tab_name + " where device_id = '" + device_id
				+ "' and user_state='1'");
		fields = cursor.getNext();
		if (fields != null) {
			count = (String) fields.get("total");
			if (count != null && !"".equals(count) && !"0".equals(count)) {
				logger.warn("该设备已绑定用户，请重新选择设备:{}", device_id);

				return "该设备已绑定用户，请重新选择设备！";
			}
		}

		// 表tab_userinst的新数据
		/*
		 * retMsg = addInstInfo(gather_id,device_id,username,dealStaff,1); if
		 * (retMsg != 1){ UserinstLog.log(request,
		 * "不同设备不能绑定同一个帐号，请重试！username==" + username); return
		 * "不同设备不能绑定同一个帐号，请重试！"; }
		 */
		batchSQL += getAddInstInfo(gather_id, device_id, username, dealStaff, 1);

		// 更新tab_gw_device中的device_status、city_id
		/* updDeviceInfo("1",city_id,device_id); */
		batchSQL += getUpdDeviceInfo("1", city_id, device_id);

		// 判断该帐号是否存在
		psql = new PrepareSQL("select count(1) as total from "
				+ tab_name + "  where username = '" + username
				+ "' and user_state='1'");
		psql.getSQL();
		cursor = DataSetBean.getCursor("select count(1) as total from "
				+ tab_name + "  where username = '" + username
				+ "' and user_state='1'");
		fields = cursor.getNext();
		String total = "1";
		if (fields != null) {
			total = (String) fields.get("total");
		}

		// 用户表tab_hgwcustomer中新增数据
		if (!"0".equals(total)) {
			if ("1".equals(gw_type)) {
				// retMsg =
				// updHGWInfo(gather_id,username,oui,device_serialnumber,city_id,"1");
				batchSQL += getUpdHGWInfo(gather_id, username, oui,
						device_serialnumber, city_id, "1", device_id);
			} else {
				retMsg = updEGWInfo(gather_id, username, oui,
						device_serialnumber, city_id, "1");
			}
		} else {
			if ("1".equals(gw_type)) {
				// retMsg =
				// addHGWInfo(gather_id,username,oui,device_serialnumber,city_id,"1");
				batchSQL += getAddHGWInfo(gather_id, username, oui,
						device_serialnumber, city_id, "1", device_id);
				batchSQL += getAddHgwcustServInfo(username);
				userid = 0;
			} else {
				retMsg = addEGWInfo(gather_id, username, oui,
						device_serialnumber, city_id, "1", device_id);
			}
		}

		/*
		 * if (retMsg != 1){ UserinstLog.log(request, "新增用户数据失败，请重试！username==" +
		 * username); return "新增用户数据失败，请重试！"; }
		 */

		// 处理表tab_gw_res_area中的数据，更新域操作
		// addAreaRes(device_id,area_id);
		batchSQL += insertAreaTable(device_id, String.valueOf(area_id), true);
		psql = new PrepareSQL(batchSQL);
		psql.getSQL();
		int[] iCodes = DataSetBean.doBatch(batchSQL);
		if (iCodes == null || iCodes.length <= 0) {
			logger.warn("现场安装设备失败,入库失败");

			return "现场安装设备失败！";
		}

		/**
		 * 2009-07-30 添加
		 * 
		 */
		StrategyConfigInterfaceAction action = new StrategyConfigInterfaceAction();

		String msg = action.itmsExecute(curUser.getUser().getId(), username,
				device_id, request);

		logger.warn("现场安装设备成功");
		return "现场安装设备成功！";
	}

	/**
	 * 企业网关现场安装配置的数据处理
	 * 
	 */
	public String saveConfig_bbms(HttpServletRequest request) {

		requestTmp = request;
		String gather_id = "-1";
		// String city_id = "";
		long area_id = 0;
		int retMsg = 0;
		String returnMsg = null;
		String oui = "";
		String device_serialnumber = "";
		String devicetype_id = "";
		String passwd = "";
		ArrayList<String> sqlList = new ArrayList<String>();
		String device_id = request.getParameter("selDevice");
		String username = request.getParameter("username");
		String serialnumber = request.getParameter("serialnumber");
		String customer_id = request.getParameter("customer_id");
		String city_id = request.getParameter("city_id");
		String service_id = null;
		String gw_type = null;
		// 获取当前操作人员的信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");

		logger.debug("CITY_ID:{}", city_id);
		if (null == city_id || "".equals(city_id) || "null".equals(city_id)) {
			city_id = curUser.getCityId();
		}
		//
		PrepareSQL psql = new PrepareSQL("select area_id from tab_city_area where city_id='"
				+ city_id + "'");
		psql.getSQL();
		Map areaIDMap = DataSetBean
				.getRecord("select area_id from tab_city_area where city_id='"
						+ city_id + "'");
		if (null == areaIDMap || areaIDMap.size() == 0
				|| "".equals((String) areaIDMap.get("area_id"))) {
			area_id = curUser.getAreaId();
		} else {
			area_id = Long.parseLong((String) areaIDMap.get("area_id"));
		}
		logger.debug("area_id=", area_id);

		String dealStaff = curUser.getUser().getAccount();

		logger.warn("现场安装信息:device_id={},username={},city_id={},dealStaff={}",
				new Object[] { device_id, username, city_id, dealStaff });

		if (device_id == null || "".equals(device_id)) {
			logger.warn("没有选择需要安装的设备，请重新操作！");

			return "没有选择需要安装的设备，请重新操作！";
		}

		// 查询设备信息
		psql = new PrepareSQL("select gather_id,oui,device_serialnumber,gw_type,devicetype_id from tab_gw_device where device_id = '"
				+ device_id + "' ");
		psql.getSQL();
		cursor = DataSetBean
				.getCursor("select gather_id,oui,device_serialnumber,gw_type,devicetype_id from tab_gw_device where device_id = '"
						+ device_id + "' ");
		fields = cursor.getNext();
		if (fields != null) {
			gather_id = (String) fields.get("gather_id");
			oui = (String) fields.get("oui");
			device_serialnumber = (String) fields.get("device_serialnumber");
			devicetype_id = (String) fields.get("devicetype_id");
			gw_type = (String) fields.get("gw_type");
		} else {
			logger.warn("无法查询到指定的新设备！device_id={}", device_id);
			return "无法查询到指定的新设备！";
		}
		// // 查询service_id
		// fields = DataSetBean
		// .getRecord("select service_id from tab_service where serv_type_id =
		// (select serv_type_id from tab_egwcustomer where username='"
		// + username
		// + "' and user_state in ('1','2')) and oper_type_id=1 and wan_type=2
		// and flag=2");
		// if (fields != null) {
		// service_id = (String) fields.get("service_id");
		// } else {
		// UserinstLog.log(request, "无法查询到tab_service中的service_id");
		// return "无法查询到tab_service中的service_id！";
		// }

		// 判断该设备是否已绑定用户
		String count = "";
		psql = new PrepareSQL("select count(1) as total from tab_egwcustomer where oui = '"
				+ oui
				+ "' and device_serialnumber = '"
				+ device_serialnumber + "' and user_state in ('1','2')");
		psql.getSQL();
		cursor = DataSetBean
				.getCursor("select count(1) as total from tab_egwcustomer where oui = '"
						+ oui
						+ "' and device_serialnumber = '"
						+ device_serialnumber + "' and user_state in ('1','2')");
		fields = cursor.getNext();
		if (fields != null) {
			count = (String) fields.get("total");
			if (count != null && !"".equals(count) && !"0".equals(count)) {
				logger.warn("该设备已绑定其它用户！device_id={}", device_id);

				return "该设备已绑定其它用户，数据出现异常，请联系管理员！";
			}
		}

		// 插入现场安装表
		String sql = "insert into tab_userinst(gather_id,device_id,username,dealstaff,dealtime,user_flag) values('"
				+ gather_id
				+ "','"
				+ device_id
				+ "','"
				+ username
				+ "','"
				+ dealStaff + "'," + (new Date()).getTime() / 1000 + ",1)";
		psql = new PrepareSQL(sql);
		psql.getSQL();
		sqlList.add(sql);

		// 更新tab_gw_device中的device_status、city_id
		sql = "update tab_gw_device set device_status = 1"
				+ ",cpe_allocatedstatus=1 , city_id = '" + city_id
				+ "', customer_id='" + customer_id + "' where device_id = '"
				+ device_id + "'";
		psql = new PrepareSQL(sql);
		psql.getSQL();
		sqlList.add(sql);

		// 判断该帐号是否存在
		psql = new PrepareSQL("select count(1) as total from tab_egwcustomer where username = '"
				+ username + "' and user_state='1'");
		psql.getSQL();
		cursor = DataSetBean
				.getCursor("select count(1) as total from tab_egwcustomer where username = '"
						+ username + "' and user_state='1'");
		fields = cursor.getNext();
		String total = "1";
		if (fields != null) {
			total = (String) fields.get("total");
		}
		// 用户表tab_hgwcustomer中新增数据
		if (!"0".equals(total)) {
			sql = "update tab_egwcustomer set oui='"
					+ oui
					+ "',device_serialnumber='"
					+ device_serialnumber
					+ "', device_id='"
					+ device_id
					+ "' ,updatetime="
					+ curTime1
					+ " from tab_egwcustomer a, tab_customerinfo b where a.customer_id=b.customer_id and a.username='"
					+ username
					+ "' and a.user_state in ('1','2') and b.city_id='"
					+ city_id + "'";

			// and city_id='" + city_id + "'";没有该字段
			psql = new PrepareSQL(sql);
			psql.getSQL();
			sqlList.add(sql);
		} else {
			// 获取新的用户记录ID
			// long userid = DataSetBean.getMaxId("tab_hgwcustomer", "user_id");
			long userid = getMaxUserId();
			long curTime = (new Date()).getTime() / 1000;
			sql = "insert into tab_egwcustomer(user_id,username,device_id, oui,device_serialnumber,serv_type_id,cust_type_id,user_state,customer_id,open_status,device_slot, opendate, onlinedate, updatetime) values("
					+ userid
					+ ",'"
					+ username
					+ "','"
					+ device_id
					+ "','"
					+ oui
					+ "','"
					+ device_serialnumber
					+ "',10,2,'1','"
					+ customer_id
					+ "',1,0,"
					+ curTime
					+ ","
					+ curTime
					+ ","
					+ curTime + ") ";
			psql = new PrepareSQL(sql);
			psql.getSQL();
			sqlList.add(sql);
		}
		// 处理表tab_gw_res_area中的数据，更新域操作
		ArrayList<String> AllsqlList = addAreaRes_bbms(device_id, area_id,
				sqlList);

		// 开始操作数据
		int[] retCode = DataSetBean.doBatch(AllsqlList);
		if (null == retCode) {
			logger.warn("执行SQL失败，请重试");
			returnMsg = "执行失败，请重试！";
			// 清空List
			AllsqlList.clear();
			AllsqlList = null;
			return returnMsg;
		}
		// 清空List
		AllsqlList.clear();
		AllsqlList = null;

		/*
		 * //调用中间件接口 if (LipossGlobals.getMidWare()) { MidWareManager
		 * midWareManager = new MidWareManager(request); String area_msg =
		 * midWareManager
		 * .getDevArea(String.valueOf(area_id),device_serialnumber,"");
		 * UserinstLog.log(request,area_msg); }
		 */
		// 通知后台设备状态变更
		User user = curUser.getUser();
		MCControlManager mc = new MCControlManager(user.getAccount(), user
				.getPasswd());
		String[] device_array = { device_id };

		retMsg = mc.reloadDeviceAraeInfo(device_array);
		if (retMsg != 0) {
			logger.warn("通知后台CORBA失败");
		} else {
			logger.warn("通知后台CORBA成功");
		}
		logger.warn("设备绑定成功");
		returnMsg = "设备绑定成功！";
		if (null == gw_type) {
			// 如果该设备的gw_type在数据库中为空，则把它设置为2(企业网关设备)
			gw_type = "2";
		}
		/*
		 * Map businessMap = new HashMap(); businessMap.put("curUser", curUser);
		 * businessMap.put("device_id", device_id);
		 * businessMap.put("service_id", service_id);
		 * businessMap.put("devicetype_id", devicetype_id);
		 * businessMap.put("gather_id", gather_id); businessMap.put("gw_type",
		 * gw_type); //执行开户工单，调用工单接口 String result =
		 * internetOpen(username,businessMap);
		 */

		// returnMsg = returnMsg + "|" + result;
		// }
		/**
		 * 修改于2009/03/16 qixueqi
		 * 
		 * 删除以前的工单下发关系，删除以前的用户多终端下发 添加新的下发 来自于StrategyConfigInterfaceAction
		 * 
		 */
		// String isDoSetMaxUsers =
		// LipossGlobals.getLipossProperty("doMaxUsers");
		//		
		// if ("1".equals(isDoSetMaxUsers)) {
		// doSetMaxUsers(device_id, username, request);
		// }
		StrategyConfigInterfaceAction action = new StrategyConfigInterfaceAction();

		String msg = action.bbmsExecute(curUser.getUser().getId(), username,
				device_id, request);

		logger.warn(msg);

		return returnMsg;
	}

	/**
	 * 设置最大用户数
	 * 
	 * @param device_id
	 * @param username
	 * @param request
	 */
	private void doSetMaxUsers(String device_id, String username,
			HttpServletRequest request) {
		logger.debug("doSetMaxUsers({}, {}, request)", device_id, username);
		PrepareSQL psql = new PrepareSQL("select max_user_number from tab_egwcustomer where username='"
				+ username + "' and user_state in ('1','2')");
		psql.getSQL();
		Map<String, String> field = DataSetBean
				.getRecord("select max_user_number from tab_egwcustomer where username='"
						+ username + "' and user_state in ('1','2')");

		String maxUserNumber = null;
		if (null == field) {
			// 未取到用户数
			maxUserNumber = "1";
		} else {
			maxUserNumber = field.get("max_user_number");
			if (null == maxUserNumber || "null".equals(maxUserNumber)
					|| "".equals(maxUserNumber)) {
				maxUserNumber = "1";
			}
		}

		logger.warn("[{}] 开始设置最大用户数:{},{}", new Object[] { device_id, username,
				maxUserNumber });

		ParamInfoCORBA paramInfoCorba = new ParamInfoCORBA();

		String[] params_name = new String[2];
		String[] params_value = new String[2];
		String[] para_type_id = new String[2];

		params_name[0] = "InternetGatewayDevice.Services.X_CT-COM_MWBAND.Mode";
		params_value[0] = "1";
		para_type_id[0] = "2";

		params_name[1] = "InternetGatewayDevice.Services.X_CT-COM_MWBAND.TotalTerminalNumber";
		params_value[1] = maxUserNumber;
		para_type_id[1] = "2";

		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();

		DeviceAct act = new DeviceAct();
		HashMap<String, String> deviceInfo = act.getDeviceInfo(device_id);
		String gather_id = (String) deviceInfo.get("gather_id");

		String ior = user.getIor("ACS_" + gather_id, "ACS_Poa_" + gather_id);

		boolean booleanMaxUserNum = paramInfoCorba.setParamValue_multi(
				params_name, params_value, para_type_id, device_id);

		logger.debug("booleanMaxUserNum:", booleanMaxUserNum);

		if (booleanMaxUserNum) {
			insertOpenMaxUserNum(device_id, maxUserNumber);
		}
	}

	/**
	 * 
	 * @param device_id
	 * @param total_number
	 */
	private void insertOpenMaxUserNum(String device_id, String total_number) {
		String gather_time = new DateTimeUtil().getLongTime() + "";

		String istSQL = "insert into gw_mwband(device_id, mode, gather_time, total_number) values ('"
				+ device_id + "',1, " + gather_time + ", " + total_number + ")";
		PrepareSQL psql = new PrepareSQL(istSQL);
		psql.getSQL();

		int flag = DataSetBean.executeUpdate(istSQL);

		if (flag > 0) {
			logger.debug("入库成功!");
		} else {
			logger.debug("入库失败");
		}
	}

	/**
	 * 设备修障的数据处理(itms)
	 * 
	 * @param request
	 * @return
	 */
	public String userModifyConfig(HttpServletRequest request) {
		logger.debug("userModifyConfig(request)");

		requestTmp = request;
		String gather_id = "-1";
		long area_id = 0;
		String oui = "";
		String device_serialnumber = "";
		String oui_old = "";
		String device_serialnumber_old = "";
		String city_id = "";
		int retMsg = 0;
		String device_id = request.getParameter("selDevice");
		String username = request.getParameter("username");
		String device_id_old = request.getParameter("device_id_old");
		String fault_id = request.getParameter("fault_id");

		// 获取当前操作人员的信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");

		city_id = curUser.getCityId();
		area_id = curUser.getAreaId();

		String dealStaff = curUser.getUser().getAccount();
		logger.warn("[{}] 设备修障信息:"
				+ "device_id_old={}, username={}, city_id={},"
				+ "dealStaff={}, fault_id={}", new Object[] { device_id,
				device_id_old, username, city_id, dealStaff, fault_id });

		// 根据city_id查询表获取area_id
		area_id = getAreaByCity(city_id);

		if (device_id == null || "".equals(device_id)) {
			// return modifyNodevice(request);
			logger.warn("没有选择需要安装的设备，请重新操作！");
			return "没有选择需要安装的设备，请重新操作！";
		}

		if (device_id.equals(device_id_old)) {
			logger.warn("新设备和原有设备相同，不需要修改信息。");
			return "新设备和原有设备相同，不需要修改信息。";
		}

		// 查询新设备信息
		PrepareSQL psql = new PrepareSQL("select gather_id,oui,device_serialnumber from tab_gw_device where device_id = '"
				+ device_id + "' ");
		psql.getSQL();
		cursor = DataSetBean
				.getCursor("select gather_id,oui,device_serialnumber from tab_gw_device where device_id = '"
						+ device_id + "' ");
		fields = cursor.getNext();
		if (fields != null) {
			gather_id = (String) fields.get("gather_id");
			oui = (String) fields.get("oui");
			device_serialnumber = (String) fields.get("device_serialnumber");
		} else {
			logger.warn("无法查询到指定的新设备！device_id={}", device_id);
			return "无法查询到指定的新设备！";
		}

		// 查询原有设备的信息
		psql = new PrepareSQL("select gather_id,oui,device_serialnumber from tab_gw_device where device_id = '"
				+ device_id_old + "' ");
		psql.getSQL();
		cursor = DataSetBean
				.getCursor("select gather_id,oui,device_serialnumber from tab_gw_device where device_id = '"
						+ device_id_old + "' ");
		fields = cursor.getNext();
		if (fields != null) {
			oui_old = (String) fields.get("oui");
			device_serialnumber_old = (String) fields
					.get("device_serialnumber");
		} else {
			oui_old = "";
			device_serialnumber_old = "";
		}

		// 判断该设备是否已绑定用户
		String count = "";
		psql = new PrepareSQL("select count(1) as total from tab_hgwcustomer where oui = '"
				+ oui
				+ "' and device_serialnumber = '"
				+ device_serialnumber + "' and user_state='1'");
		psql.getSQL();
		cursor = DataSetBean
				.getCursor("select count(1) as total from tab_hgwcustomer where oui = '"
						+ oui
						+ "' and device_serialnumber = '"
						+ device_serialnumber + "' and user_state='1'");
		fields = cursor.getNext();
		if (fields != null) {
			count = (String) fields.get("total");
			if (count != null && !"".equals(count) && !"0".equals(count)) {
				logger.warn("该设备已绑定用户，请重新选择设备！device_id={}", device_id);
				return "该设备已绑定用户，请重新选择设备！";
			}
		}

		// 表tab_userinst的新数据
		// retMsg = addInstInfo(gather_id,device_id,username,dealStaff);

		// 更新tab_gw_device中的device_status、city_id
		updDeviceInfo("1", city_id, device_id);

		// 用户表tab_hgwcustomer中新增数据
		retMsg = modifyHGWInfo(username, oui, device_serialnumber, city_id,
				device_id, device_id_old);
		if (retMsg != 1) {
			logger.warn("不同设备不能绑定同一个帐号，请重试！");
			return "不同设备不能绑定同一个帐号，请重试！";
		}

		// 处理表tab_gw_res_area中的数据，更新域操作
		addAreaRes(device_id, area_id);
		addAreaRes(device_id_old, 0);

		// 保存故障原因
		saveFault(username, device_id_old, fault_id, dealStaff, "");

		/*
		 * // 获取当前操作人员的信息,默认取admin Map user = getUserInfo(); // 通知后台设备状态变更
		 * MCControlManager mc = new MCControlManager((String) user
		 * .get("acc_loginname"), (String) user.get("acc_password")); String[]
		 * device_array = { device_id, device_id_old }; retMsg =
		 * mc.reloadDeviceAraeInfo(device_array);
		 * 
		 * if (retMsg != 0) { logger.warn("通知后台CORBA失败。"); return
		 * "通知后台CORBA失败。"; }
		 */

		logger.warn("设备修障成功！\n");
		return "设备修障成功!";
	}

	/**
	 * 现场安装配置的数据处理(iposs)
	 * 
	 */
	public String ipossSaveConfig(HttpServletRequest request) {

		String batchSQL = "";
		requestTmp = request;
		String gather_id = "-1";
		long area_id = 0;
		String oui = "";
		String device_serialnumber = "";
		int retMsg = 0;
		String gw_type = "1";

		String device_id = request.getParameter("selDevice");
		String username = request.getParameter("username");
		String city_id = request.getParameter("city_id");
		String dealStaff = request.getParameter("dealStaff");

		logger.warn("现场安装信息: device_id=" + device_id + "; username=" + username
				+ "; city_id=" + city_id + "; dealStaff=" + dealStaff);

		// 根据city_id查询表获取area_id
		area_id = getAreaByCity(city_id);

		logger.warn("根据属地获取到的域ID area_id：" + area_id + "  ; city_id :"
				+ city_id);

		if (device_id == null || "".equals(device_id)) {
			// return saveNodevice(request);
			logger.warn("没有选择需要安装的设备，请重新操作！");
			return "没有选择需要安装的设备，请重新操作！";
		}

		// 查询设备信息
		PrepareSQL psql = new PrepareSQL("select gather_id,oui,device_serialnumber,gw_type from tab_gw_device where device_id = '"
				+ device_id + "' ");
		psql.getSQL();
		cursor = DataSetBean
				.getCursor("select gather_id,oui,device_serialnumber,gw_type from tab_gw_device where device_id = '"
						+ device_id + "' ");
		fields = cursor.getNext();
		if (fields != null) {
			gather_id = (String) fields.get("gather_id");
			oui = (String) fields.get("oui");
			device_serialnumber = (String) fields.get("device_serialnumber");
			gw_type = (String) fields.get("gw_type");
		} else {
			logger.warn("无法查询到指定的新设备！device_id==" + device_id);
			return "无法查询到指定的新设备！";
		}

		// 判断是那种用户类型 1：家庭网关 2：企业网关
		String tab_name = "tab_hgwcustomer";
		if (gw_type.equals("2")) {
			tab_name = "tab_egwcustomer";
		}

		// 查看用户是否绑定了设备,如果绑定了设备返回绑定信息
		psql = new PrepareSQL("select oui,device_serialnumber,city_id,device_id from "
				+ tab_name + " where username='" + username
				+ "' and user_state in ('1','2')");
		psql.getSQL();
		fields = DataSetBean
				.getRecord("select oui,device_serialnumber,city_id,device_id from "
						+ tab_name + " where username='" + username
						+ "' and user_state in ('1','2')");
		if (fields != null) {

			String _oui = (String) fields.get("oui");
			String _device_serialnumber = (String) fields
					.get("device_serialnumber");

			// 用户属地
			city_id = String.valueOf(fields.get("city_id"));

//			if (_oui != null && _device_serialnumber != null
//					&& !"".equals(_oui) && !"".equals(_device_serialnumber)) {
			if (false == "".equals(fields.get("device_id"))) {
				logger.warn("该用户已绑定设备，请重试！");
				return "该用户已绑定设备，请重试！";

			}

		}

		// 判断该设备是否已绑定用户
		String tmpName = "";
		psql = new PrepareSQL("select username as total from "
				+ tab_name + " where oui='" + oui
				+ "' and device_serialnumber='" + device_serialnumber
				+ "' and user_state='1'");
		psql.getSQL();
		cursor = DataSetBean.getCursor("select username as total from "
				+ tab_name + " where oui='" + oui
				+ "' and device_serialnumber='" + device_serialnumber
				+ "' and user_state='1'");
		fields = cursor.getNext();
		if (fields != null) {
			tmpName = (String) fields.get("total");
			if (tmpName != null && !"".equals(tmpName)
					&& username.equals(tmpName)) {
				logger.warn("该帐号和设备对应关系已存在，现场安装设备成功！\n");
				return "现场安装设备成功！";
			} else {
				logger.warn("该设备已绑定用户，请重新选择设备！device_id==" + device_id);
				return "该设备已绑定用户，请重新选择设备！";
			}
		}

		// 表tab_userinst的新数据
		/*
		 * retMsg = addInstInfo(gather_id,device_id,username,dealStaff,1); if
		 * (retMsg != 1){ UserinstLog.log(request,
		 * "不同设备不能绑定同一个帐号，请重试！username==" + username); return
		 * "不同设备不能绑定同一个帐号，请重试！"; }
		 */
		batchSQL += getAddInstInfo(gather_id, device_id, username, dealStaff, 1);

		// 更新tab_gw_device中的device_status、city_id
		// updDeviceInfo("1",city_id,device_id);
		batchSQL += getUpdDeviceInfo("1", city_id, device_id);

		String sql = "select count(1) as total from "
				+ tab_name + " where username = '" + username
				+ "' and user_state='1'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select count(*) as total from "
					+ tab_name + " where username = '" + username
					+ "' and user_state='1'";
		}
		// 判断该帐号是否存在
		psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		fields = cursor.getNext();
		String total = "1";
		if (fields != null) {
			total = (String) fields.get("total");
		}
		// 用户表tab_hgwcustomer中新增数据或更新用户信息
		if (!"0".equals(total)) {
			if ("1".equals(gw_type)) {
				// retMsg =
				// updHGWInfo(gather_id,username,oui,device_serialnumber,city_id,"1");
				batchSQL += getUpdHGWInfo(gather_id, username, oui,
						device_serialnumber, city_id, "1", device_id);
			} else {
				retMsg = updEGWInfo(gather_id, username, oui,
						device_serialnumber, city_id, "1");
			}
		} else {
			if ("1".equals(gw_type)) {
				// retMsg =
				// addHGWInfo(gather_id,username,oui,device_serialnumber,city_id,"1");
				batchSQL += getAddHGWInfo(gather_id, username, oui,
						device_serialnumber, city_id, "1", device_id);
				batchSQL += getAddHgwcustServInfo(username);
				userid = 0;
			} else {
				retMsg = addEGWInfo(gather_id, username, oui,
						device_serialnumber, city_id, "1", device_id);
			}
		}

		// 处理表tab_gw_res_area中的数据，更新域操作
		logger.warn("处理表tab_gw_res_area　调用addAreaRes ：:" + area_id);
		// addAreaRes(device_id,area_id);

		batchSQL += insertAreaTable(device_id, String.valueOf(area_id), true);
		psql = new PrepareSQL(batchSQL);
		psql.getSQL();
		int[] iCodes = DataSetBean.doBatch(batchSQL);
		if (iCodes == null || iCodes.length <= 0) {
			return "现场安装设备失败！";
		}

		// 获取当前操作人员的信息,默认取admin
		// Map user = getUserInfo();
		// //通知后台设备状态变更
		// MCControlManager mc=new
		// MCControlManager((String)user.get("acc_loginname"),(String)user.get("acc_password"));
		// String[] device_array = {device_id};
		// retMsg = mc.reloadDeviceAraeInfo(device_array);
		//		
		// if (retMsg != 0){
		// UserinstLog.log(request, "通知后台CORBA失败。");
		// return "通知后台CORBA失败。";
		// }

		// 绑定完成后，即下发业务工单
		new DoBusinessAct().doOpenBusiness(gw_type, device_id, gather_id, oui,
				device_serialnumber, username);

		/**
		 * 2009-09-24 添加
		 * 
		 */
		StrategyConfigInterfaceAction action = new StrategyConfigInterfaceAction();

		String msg = action.itmsExecute(1, username,
				device_id, request);
		
		logger.warn("现场安装设备成功！\n");
		return "现场安装设备成功！";
	}

	/**
	 * 设备修障的数据处理(iposs)
	 * 
	 */
	public String ipossModifyConfig(HttpServletRequest request) {

		requestTmp = request;
		String gather_id = "-1";
		long area_id = 0;
		String oui = "";
		String device_serialnumber = "";
		String oui_old = "";
		String device_serialnumber_old = "";
		int retMsg = 0;
		String gw_type = "1";

		String device_id = request.getParameter("selDevice");
		String username = request.getParameter("username");
		String city_id = request.getParameter("city_id");
		String dealStaff = request.getParameter("dealStaff");
		String dealStaffid = request.getParameter("dealStaffid");
		String device_id_old = request.getParameter("device_id_old");
		String fault_id = request.getParameter("fault_id");

		logger.warn("设备修障信息: device_id=" + device_id + "; device_id_old="
				+ device_id_old + "; username=" + username + "; city_id="
				+ city_id + "; dealStaff=" + dealStaff + "; fault_id="
				+ fault_id);

		// 根据city_id查询表获取area_id
		area_id = getAreaByCity(city_id);

		if (device_id == null || "".equals(device_id)) {
			// return modifyNodevice(request);
			logger.warn("没有选择需要安装的设备，请重新操作！");
			return "没有选择需要安装的设备，请重新操作！";
		}

		if (device_id.equals(device_id_old)) {
			logger.warn("新设备和原有设备相同，不需要修改信息。");
			return "新设备和原有设备相同，不需要修改信息。";
		}

		// 查询新设备信息
		PrepareSQL psql = new PrepareSQL("select gather_id,oui,device_serialnumber,gw_type from tab_gw_device where device_id = '"
				+ device_id + "' ");
		psql.getSQL();
		cursor = DataSetBean
				.getCursor("select gather_id,oui,device_serialnumber,gw_type from tab_gw_device where device_id = '"
						+ device_id + "' ");
		fields = cursor.getNext();
		if (fields != null) {
			gather_id = (String) fields.get("gather_id");
			oui = (String) fields.get("oui");
			device_serialnumber = (String) fields.get("device_serialnumber");
			gw_type = (String) fields.get("gw_type");
		} else {
			logger.warn("无法查询到指定的新设备！device_id==" + device_id);
			return "无法查询到指定的新设备！";
		}

		// 查询原有设备的信息
		psql = new PrepareSQL("select gather_id,oui,device_serialnumber from tab_gw_device where device_id = '"
				+ device_id_old + "' ");
		psql.getSQL();
		cursor = DataSetBean
				.getCursor("select gather_id,oui,device_serialnumber from tab_gw_device where device_id = '"
						+ device_id_old + "' ");
		fields = cursor.getNext();
		if (fields != null) {
			oui_old = (String) fields.get("oui");
			device_serialnumber_old = (String) fields
					.get("device_serialnumber");
			String updateGwDevice = "update tab_gw_device set device_status=0,cpe_allocatedstatus=0 where device_id='"
					+ device_id_old + "'";
			psql = new PrepareSQL(updateGwDevice);
			psql.getSQL();
			DataSetBean.executeUpdate(updateGwDevice);
		} else {
			oui_old = "";
			device_serialnumber_old = "";
		}

		// 判断是那种用户类型 1：家庭网关 2：企业网关
		String tab_name = "tab_hgwcustomer";
		if (gw_type.equals("2")) {
			tab_name = "tab_egwcustomer";
		}

		// 判断该设备是否已绑定用户
		String count = "";

		String sql = "select count(1) as total from "
				+ tab_name + " where oui = '" + oui
				+ "' and device_serialnumber = '" + device_serialnumber
				+ "' and user_state='1'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select count(*) as total from "
					+ tab_name + " where oui = '" + oui
					+ "' and device_serialnumber = '" + device_serialnumber
					+ "' and user_state='1'";
		}
		psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		fields = cursor.getNext();
		if (fields != null) {
			count = (String) fields.get("total");
			if (count != null && !"".equals(count) && !"0".equals(count)) {
				logger.warn("该设备已绑定用户，请重新选择设备！device_id==" + device_id);
				return "该设备已绑定用户，请重新选择设备！";
			}
		}

		String total = "1";
		// 查看用户是否绑定了设备,如果绑定了设备返回绑定信息
		psql = new PrepareSQL("select user_id,oui,device_serialnumber,city_id from "
				+ tab_name
				+ " where username='"
				+ username
				+ "' and user_state in ('1','2')");
		psql.getSQL();
		fields = DataSetBean
				.getRecord("select user_id,oui,device_serialnumber,city_id from "
						+ tab_name
						+ " where username='"
						+ username
						+ "' and user_state in ('1','2')");
		if (fields != null) {

			// 用户属地
			city_id = String.valueOf(fields.get("city_id"));
			total = "1";
		} else {
			total = "0";
		}

		// 判断该帐号是否存在
		// cursor = DataSetBean.getCursor("select count(1) as total from " +
		// tab_name
		// + " where username = '" + username + "' and user_state='1'");
		// fields = cursor.getNext();
		//
		// if (fields != null) {
		// total = (String) fields.get("total");
		// }

		// 用户表tab_hgwcustomer中新增数据或更新用户信息

		if (!"0".equals(total)) {
			if ("1".equals(gw_type)) {
				retMsg = modifyHGWInfo(username, oui, device_serialnumber,
						city_id, device_id, device_id_old);
			} 
//			else {
//				retMsg = modifyEGWInfo(gather_id, username, oui,
//						device_serialnumber, city_id, oui_old,
//						device_serialnumber_old);
//			}
		} else {

			if ("1".equals(gw_type)) {
				retMsg = addHGWInfo(gather_id, username, oui,
						device_serialnumber, city_id, "1", device_id);
			} else {
				retMsg = addEGWInfo(gather_id, username, oui,
						device_serialnumber, city_id, "1", device_id);
			}
		}

		int user_flag = 1;

		if (oui_old != null && !oui_old.equals("")
				&& device_serialnumber_old != null
				&& !device_serialnumber_old.equals("")) {

			user_flag = 1;
		} else {
			user_flag = 2;
		}

		// itms没有数据则认为是新装机，增加现场安装记录
		addInstInfo(gather_id, device_id, username, dealStaff, user_flag);

		if (retMsg != 1) {
			logger.warn("设备绑定帐号失败，请重试！");
			return "设备绑定帐号失败，请重试！";
		}

		// 更新tab_gw_device中的device_status、city_id
		updDeviceInfo("1", city_id, device_id);

		// 处理表tab_gw_res_area中的数据，更新域操作
		addAreaRes(device_id, area_id);
		addAreaRes(device_id_old, 0);

		// 保存故障原因
		saveFault(username, device_id_old, fault_id, dealStaff, dealStaffid);

		// 获取当前操作人员的信息,默认取admin
		// Map user = getUserInfo();
		// //通知后台设备状态变更
		// MCControlManager mc=new
		// MCControlManager((String)user.get("acc_loginname"),(String)user.get("acc_password"));
		// String[] device_array = {device_id,device_id_old};
		// retMsg = mc.reloadDeviceAraeInfo(device_array);
		//		
		// if (retMsg != 0){
		// UserinstLog.log(request, "通知后台CORBA失败。");
		// return "通知后台CORBA失败。";
		// }

		// 绑定完成后，即下发业务工单
		new DoBusinessAct().doOpenBusiness(gw_type, device_id, gather_id, oui,
				device_serialnumber, username);

		/**
		 * 2009-09-24 添加
		 * 
		 */
		StrategyConfigInterfaceAction action = new StrategyConfigInterfaceAction();

		String msg = action.itmsExecute(1, username,
				device_id, request);
		
		logger.warn("设备修障成功！\n");
		return "设备修障成功!";
	}

	/**
	 * 初始化页面信息
	 * 
	 * @param query
	 * @param start
	 * @param len
	 */
	public void initPage(String query, int start, int len) {
		offset = start;
		MaxLine = len;
		this.query = query;
		total = getTotal();
		totalPage = (int) Math.ceil((double) total / MaxLine);
		curPage = (int) Math.floor((double) offset / MaxLine + 1);
	}

	/**
	 * 获取所有记录条数
	 * 
	 * @return
	 */
	private int getTotal() {
		String query_pos;

		int begin, end;
		if (query.toUpperCase().indexOf("UNION ALL") == -1) {
			begin = query.toUpperCase().indexOf(" FROM ");
			end = query.toUpperCase().indexOf(" GROUP ");
			if (end == -1)
				end = query.toUpperCase().indexOf("ORDER");
			if (end == -1)
				end = query.length();

			query_pos = query.substring(begin, end).trim();

			String strSQL = "select count(1) As total " + query_pos;
			logger.debug(strSQL);
			HashMap fields = DataSetBean.getRecord(strSQL);
			if (fields != null) {
				logger.debug("Record Count: " + fields.get("total"));
				return (Integer.parseInt((String) fields.get("total")));
			} else
				return 0;
		} else {
			Cursor cursor = DataSetBean.getCursor(this.query);
			return (cursor.getRecordSize());

		}
	}

	/**
	 * 生成分页导航栏
	 * 
	 * @return 返回导航栏HTML代码
	 */
	public String getPageBar() {
		String strHTML = "";
		String strColor = "#535353";
		int first, next, prev, last;

		first = 1;
		next = offset + MaxLine;
		prev = offset - MaxLine;
		last = (totalPage - 1) * MaxLine + 1;

		if (offset > MaxLine)
			strHTML += "<A HREF='#' onclick=goPage('" + first
					+ "')>首页</A>&nbsp;";
		else
			strHTML += "<FONT COLOR=" + strColor + ">首页</FONT>&nbsp;";

		if (prev > 0)
			strHTML += "<A HREF='#' onclick=goPage('" + prev
					+ "')>前页</A>&nbsp;";
		else
			strHTML += "<FONT COLOR=" + strColor + ">前页</FONT>&nbsp;";

		if (next <= total)
			strHTML += "<A HREF='#' onclick=goPage('" + next
					+ "')>后页</A>&nbsp;";
		else
			strHTML += "<FONT COLOR=" + strColor + ">后页</FONT>&nbsp;";

		if (totalPage != 0 && curPage < totalPage)
			strHTML += "<A HREF='#' onclick=goPage('" + last
					+ "')>尾页</A>&nbsp;";
		else
			strHTML += "<FONT COLOR=" + strColor + ">尾页</FONT>&nbsp;";

		strHTML += "  页次：<b>" + curPage + "</b>/<b>" + totalPage + "</b>页 <b>";
		strHTML += MaxLine + "</b>条/页 共<b>" + total + "</b>条";

		return strHTML;
	}

	/**
	 * 将当前area_id及所有父节点都录入表tab_gw_res_area
	 * 
	 */
	private void addAreaRes(String device_id, long area_id) {

		String sqlDelete = "delete from tab_gw_res_area where res_id = ? and res_type = 1";
		String sql = "select * from tab_area where area_id = ?";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select area_pid from tab_area where area_id = ?";
		}
		String sql1 = "insert into tab_gw_res_area(res_type,res_id,area_id) values(1,?,?) ";

		// 删除表tab_gw_res_area中已有的设备数据
		PrepareSQL pSQLDelete = new PrepareSQL(sqlDelete);
		pSQLDelete.setString(1, device_id);
		DataSetBean.executeUpdate(pSQLDelete.getSQL());

		logger.warn("开始入域表 area_id 是 ：{}", area_id);
		// 当area_id不是最上层时继续循环
		while (area_id != 0 && area_id != -1 && area_id != 1) {

			logger.warn("循环取到的 area_id 是 ：", area_id);
			// 向表tab_gw_res_area中插入数据
			PrepareSQL pSQL1 = new PrepareSQL(sql1);
			pSQL1.setString(1, device_id);
			pSQL1.setLong(2, area_id);
			DataSetBean.executeUpdate(pSQL1.getSQL());

			// 记录更新域权限的sql
			logger.debug(pSQL1.getSQL());

			// 查询该区域的父ID
			PrepareSQL pSQL = new PrepareSQL(sql);
			pSQL.setLong(1, area_id);
			cursor = DataSetBean.getCursor(pSQL.getSQL());
			fields = cursor.getNext();
			if (fields != null) {
				String tmp = (String) fields.get("area_pid");
				if (tmp != null && !"".equals(tmp)) {
					area_id = Long.parseLong(tmp);
				} else {
					area_id = 0;
				}
			} else {
				area_id = 0;
			}
		}
	}

	/**
	 * 将当前area_id及所有父节点都录入表tab_gw_res_area
	 * 
	 */
	private ArrayList<String> addAreaRes_bbms(String device_id, long area_id,
			ArrayList<String> sqlList) {

		String sqlDelete = "delete from tab_gw_res_area where res_id = ? and res_type = 1";
		String sql = "select * from tab_area where area_id = ?";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select area_pid from tab_area where area_id = ?";
		}
		String sql1 = "insert into tab_gw_res_area(res_type,res_id,area_id) values(1,?,?) ";

		// 删除表tab_gw_res_area中已有的设备数据
		PrepareSQL pSQLDelete = new PrepareSQL(sqlDelete);
		pSQLDelete.setString(1, device_id);
		DataSetBean.executeUpdate(pSQLDelete.getSQL());

		logger.debug("开始入域表 area_id 是 ：" + area_id);
		// 当area_id不是最上层时继续循环
		while (area_id != 0 && area_id != -1 && area_id != 1) {

			logger.debug("循环取到的 area_id 是 ：" + area_id);
			// 向表tab_gw_res_area中插入数据
			PrepareSQL pSQL1 = new PrepareSQL(sql1);
			pSQL1.setString(1, device_id);
			pSQL1.setLong(2, area_id);
			// DataSetBean.executeUpdate(pSQL1.getSQL());
			sqlList.add(pSQL1.getSQL());
			// 记录更新域权限的sql
			logger.debug(pSQL1.getSQL());

			// 查询该区域的父ID
			PrepareSQL pSQL = new PrepareSQL(sql);
			pSQL.setLong(1, area_id);
			cursor = DataSetBean.getCursor(pSQL.getSQL());
			fields = cursor.getNext();
			if (fields != null) {
				String tmp = (String) fields.get("area_pid");
				if (tmp != null && !"".equals(tmp)) {
					area_id = Long.parseLong(tmp);
				} else {
					area_id = 0;
				}
			} else {
				area_id = 0;
			}
		}

		return sqlList;
	}

	/**
	 * 获取设备信息
	 * 
	 * @return
	 */
	public Map getDeviceTypeMap() {
		// Map deviceTypeMap = new HashMap();
		// String devicetype_id = null;
		// String devicemodel = null;
		// String softwareversion = null;
		//
		// Cursor cursorTmp = DataSetBean.getCursor("select * from
		// tab_devicetype_info");
		// Map fieldTmp = cursorTmp.getNext();
		// while (fieldTmp != null) {
		// devicemodel = (String) fieldTmp.get("device_model");
		// softwareversion = (String) fieldTmp.get("softwareversion");
		// devicetype_id = (String) fieldTmp.get("devicetype_id");
		// deviceTypeMap.put(devicetype_id, devicemodel + "," +
		// softwareversion);
		// fieldTmp = cursorTmp.getNext();
		// }
		//
		// return deviceTypeMap;
		return UnfirmDeviceAct.getDeviceTypeMap();
	}

	private String getAddInstInfo(String gather_id, String device_id,
			String username, String dealStaff, int user_flag) {
		String sql = "insert into tab_userinst(gather_id,device_id,username,dealstaff,dealtime,user_flag) values('"
				+ gather_id
				+ "','"
				+ device_id
				+ "','"
				+ username
				+ "','"
				+ dealStaff
				+ "',"
				+ (new Date()).getTime()
				/ 1000
				+ ","
				+ user_flag + ")";
		sql += ";";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return sql;
	}

	/**
	 * 向表tab_userinst新增数据
	 * 
	 * @return 操作的记录条数
	 */
	private int addInstInfo(String gather_id, String device_id,
			String username, String dealStaff, int user_flag) {
		int flag = 0;

		String sql = "insert into tab_userinst(gather_id,device_id,username,dealstaff,dealtime,user_flag) values('"
				+ gather_id
				+ "','"
				+ device_id
				+ "','"
				+ username
				+ "','"
				+ dealStaff
				+ "',"
				+ (new Date()).getTime()
				/ 1000
				+ ","
				+ user_flag + ")";
		flag = DataSetBean.executeUpdate(sql);

		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();

		return flag;
	}

	/**
	 * 操作现场安装数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private int operateInstInfo() {
		return 0;
	}

	/**
	 * 更新表tab_gw_device的数据
	 * 
	 * @return 操作的记录条数
	 * 
	 */
	private int updDeviceInfo(String device_status, String city_id,
			String device_id) {
		int flag = 0;

		String sql = "update tab_gw_device set device_status = "
				+ device_status + ",cpe_allocatedstatus=1 , city_id = '"
				+ city_id + "' where device_id = '" + device_id + "'";
		flag = DataSetBean.executeUpdate(sql);

		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();

		return flag;
	}

	/**
	 * 增加用户业务表数据sql
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-9-1
	 * @return String
	 */
	private String getAddHgwcustServInfo(String username) {
		long curTime = (new Date()).getTime() / 1000;
		String sql = "insert into hgwcust_serv_info (user_id,username,serv_type_id,dealdate,opendate,updatetime)"
				+ " values ("
				+ userid
				+ ",'"
				+ username
				+ "',10,"
				+ curTime
				+ "," + curTime + "," + curTime + ");";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return sql;
	}

	/**
	 * 获取添加家庭网关用户表的sql语句
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-9-1
	 * @return String
	 */
	private String getAddHGWInfo(String gather_id, String username, String oui,
			String device_serialnumber, String city_id, String user_state, String deviceId) {

		// 获取新的用户记录ID
		// long userid = DataSetBean.getMaxId("tab_hgwcustomer", "user_id");
		userid = getMaxUserId();

		long curTime = (new Date()).getTime() / 1000;

		String sql = "insert into tab_hgwcustomer"
				+ "(user_id,gather_id,username,oui,device_serialnumber,"
				+ "serv_type_id,city_id,cust_type_id,user_state,open_status,"
				+ "device_slot, opendate, onlinedate, updatetime,device_id) values("
				+ userid
				+ ",'"
				+ gather_id
				+ "','"
				+ username
				+ "','"
				+ oui
				+ "','"
				+ device_serialnumber
				+ "',10,'"
				+ city_id
				+ "',2,'"
				+ user_state
				+ "',1,0,"
				+ curTime
				+ ","
				+ curTime
				+ ","
				+ curTime + ",'" + deviceId + "') ";

		sql += ";";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return sql;
	}

	/**
	 * 向表tab_hgwcustomer新增数据
	 * 
	 * @return 操作的记录条数
	 * 
	 */
	private int addHGWInfo(String gather_id, String username, String oui,
			String device_serialnumber, String city_id, String user_state, String deviceId) {
		int flag = 0;
		// 获取新的用户记录ID
		// long userid = DataSetBean.getMaxId("tab_hgwcustomer", "user_id");
		long userid = getMaxUserId();

		long curTime = (new Date()).getTime() / 1000;

		String sql = "insert into tab_hgwcustomer(user_id,gather_id,username,oui,device_serialnumber,"
				+ "serv_type_id,city_id,cust_type_id,user_state,open_status,"
				+ "device_slot, opendate, onlinedate, updatetime,device_id) values("
				+ userid
				+ ",'"
				+ gather_id
				+ "','"
				+ username
				+ "','"
				+ oui
				+ "','"
				+ device_serialnumber
				+ "',10,'"
				+ city_id
				+ "',2,'"
				+ user_state
				+ "',1,0,"
				+ curTime
				+ ","
				+ curTime
				+ ","
				+ curTime + ",'" + deviceId + "') ";
		flag = DataSetBean.executeUpdate(sql);

		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();

		return flag;
	}

	/**
	 * 向表tab_hgwcustomer新增数据
	 * 
	 * @return 操作的记录条数
	 * 
	 */
	private int addEGWInfo(String gather_id, String username, String oui,
			String device_serialnumber, String city_id, String user_state, String deviceId) {
		int flag = 0;
		// 获取新的用户记录ID
		// long userid = DataSetBean.getMaxId("tab_hgwcustomer", "user_id");
		long userid = getMaxUserId();

		long curTime = (new Date()).getTime() / 1000;
		String sql = "insert into tab_egwcustomer(user_id,gather_id,username,oui,device_serialnumber,"
				+ "serv_type_id,city_id,cust_type_id,user_state,open_status,"
				+ "device_slot, opendate, onlinedate, updatetime,device_id) values("
				+ userid
				+ ",'"
				+ gather_id
				+ "','"
				+ username
				+ "','"
				+ oui
				+ "','"
				+ device_serialnumber
				+ "',10,'"
				+ city_id
				+ "',2,'"
				+ user_state
				+ "',1,0,"
				+ curTime
				+ ","
				+ curTime
				+ ","
				+ curTime + ",'" + deviceId + "') ";
		flag = DataSetBean.executeUpdate(sql);

		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();

		return flag;
	}
	
	/**
	 * 更改用户
	 * @param gather_id
	 * @param username
	 * @param oui
	 * @param device_serialnumber
	 * @param city_id
	 * @param user_state
	 * @param device_id
	 * @return
	 */
	private String getUpdHGWInfo(String gather_id, String username, String oui,
			String device_serialnumber, String city_id, String user_state, String device_id) {
		long curTime = (new Date()).getTime() / 1000;
		String sql = "update tab_hgwcustomer set gather_id='" + gather_id
				+ "',city_id='" + city_id + "',oui='" + oui
				+ "',device_serialnumber='" + device_serialnumber
				+ "',device_id='" + device_id
				+ "',updatetime=" + curTime + " where username='" + username
				+ "' and user_state='" + user_state + "'";
		sql += ";";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return sql;
	}

	/**
	 * 向表tab_hgwcustomer新增数据
	 * 
	 * @return 操作的记录条数
	 * 
	 */
	private int updHGWInfo(String gather_id, String username, String oui,
			String device_serialnumber, String city_id, String user_state) {
		int flag = 0;
		long curTime = (new Date()).getTime() / 1000;
		String sql = "update tab_hgwcustomer set gather_id='" + gather_id
				+ "',city_id='" + city_id + "',oui='" + oui
				+ "',device_serialnumber='" + device_serialnumber
				+ "',updatetime=" + curTime + " where username='" + username
				+ "' and user_state='" + user_state + "'";
		flag = DataSetBean.executeUpdate(sql);

		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();

		return flag;
	}

	/**
	 * 向表tab_egwcustomer更新数据
	 * 
	 * @return 操作的记录条数
	 * 
	 */
	private int updEGWInfo(String gather_id, String username, String oui,
			String device_serialnumber, String city_id, String user_state) {
		int flag = 0;

		String sql = "update tab_egwcustomer set gather_id='" + gather_id
				+ "',city_id='" + city_id + "',oui='" + oui
				+ "',device_serialnumber='" + device_serialnumber
				+ "',device_serialnumber='" + device_serialnumber
				+ "',updatetime=" + curTime1 + " where username='" + username
				+ "' and user_state='" + user_state + "'";
		flag = DataSetBean.executeUpdate(sql);

		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();

		return flag;
	}

	/**
	 * 修障时进行的操作，现将原有数据的user_state改为4，再新增新的用户数据
	 * 
	 * @return 操作的记录条数
	 * 
	 */
	private int modifyHGWInfo(String username, String oui,
			String device_serialnumber, String city_id, String device_id,
			String device_id_old) {
		int flag = 0;
		String sql = "";
		// 获取新的用户记录ID

		if (device_id_old != null && !"".equals(device_id_old)) {

			// long userid = DataSetBean.getMaxId("tab_hgwcustomer", "user_id");
			long userid = getMaxUserId();

			// 新增用户数据，将原有信息输入新的记录
			sql = "insert into tab_hgwcustomer"
					+ "(user_id,gather_id,username,passwd,city_id,cotno,bill_type_id,next_bill_type_id,"
					+ "cust_type_id,user_type_id,bindtype,virtualnum,numcharacter,access_style_id,aut_flag,"
					+ "service_set,realname,sex,cred_type_id,credno,address,office_id,zone_id,access_kind_id,"
					+ "trade_id,licenceregno,occupation_id,education_id,vipcardno,contractno,linkman,linkman_credno,"
					+ "linkphone,linkaddress,mobile,email,agent,agent_credno,agentphone,adsl_res,adsl_card,adsl_dev,"
					+ "adsl_ser,isrepair,bandwidth,ipaddress,overipnum,ipmask,gateway,macaddress,device_id,device_ip,"
					+ "device_shelf,device_frame,device_slot,device_port,basdevice_id,basdevice_ip,basdevice_shelf,"
					+ "basdevice_frame,basdevice_slot,basdevice_port,vlanid,workid,user_state,opendate,onlinedate,"
					+ "pausedate,closedate,updatetime,staff_id,remark,phonenumber,cableid,bwlevel,vpiid,vciid,adsl_hl,"
					+ "userline,dslamserialno,movedate,dealdate,opmode,maxattdnrate,upwidth,oui,device_serialnumber,"
					+ "serv_type_id,max_user_number,wan_value_1,wan_value_2,open_status) "
					+ "select "
					+ userid
					+ ",gather_id"
					+ ",'"
					+ username
					+ "',passwd,'"
					+ city_id
					+ "',cotno,bill_type_id,next_bill_type_id,"
					+ "cust_type_id,user_type_id,bindtype,virtualnum,numcharacter,access_style_id,aut_flag,"
					+ "service_set,realname,sex,cred_type_id,credno,address,office_id,zone_id,access_kind_id,"
					+ "trade_id,licenceregno,occupation_id,education_id,vipcardno,contractno,linkman,linkman_credno,"
					+ "linkphone,linkaddress,mobile,email,agent,agent_credno,agentphone,adsl_res,adsl_card,adsl_dev,"
					+ "adsl_ser,isrepair,bandwidth,ipaddress,overipnum,ipmask,gateway,macaddress,'"
					+ device_id
					+ "',device_ip,"
					+ "device_shelf,device_frame,device_slot,device_port,basdevice_id,basdevice_ip,basdevice_shelf,"
					+ "basdevice_frame,basdevice_slot,basdevice_port,vlanid,workid,'1',opendate,onlinedate,"
					+ "pausedate,closedate,updatetime,staff_id,remark,phonenumber,cableid,bwlevel,vpiid,vciid,adsl_hl,"
					+ "userline,dslamserialno,movedate,dealdate,opmode,maxattdnrate,upwidth,'"
					+ oui
					+ "','"
					+ device_serialnumber
					+ "',"
					+ "10,max_user_number,wan_value_1,wan_value_2,open_status from tab_hgwcustomer where device_id='"
					+ device_id_old
					+ "' and username = '"
					+ username
					+ "' and user_state='1'";
			flag = DataSetBean.executeUpdate(sql);

			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();

			// 将原有数据的user_state更新为4
			sql = "update tab_hgwcustomer set user_state = '4',updatetime="
					+ curTime1 + " where (device_id = '" + device_id_old
					+ "' or device_id is null) and username = '" + username
					+ "'";
			DataSetBean.executeUpdate(sql);

			psql = new PrepareSQL(sql);
			psql.getSQL();
		} else {
			PrepareSQL psql = new PrepareSQL("update tab_hgwcustomer set oui ='" + oui
					+ "', device_serialnumber='" + device_serialnumber
					+ "', device_id='" + device_id
					+ "' where username='" + username
					+ "' and user_state in ('1','2')");
			psql.getSQL();
			flag = DataSetBean
					.executeUpdate("update tab_hgwcustomer set oui ='" + oui
							+ "', device_serialnumber='" + device_serialnumber
							+ "', device_id='" + device_id
							+ "' where username='" + username
							+ "' and user_state in ('1','2')");
		}

		return flag;
	}

	/**
	 * 修障时进行的操作，现将原有数据的user_state改为4，再新增新的用户数据
	 * 
	 * @return 操作的记录条数
	 * 
	 */
//	private int modifyEGWInfo(String gather_id, String username, String oui,
//			String device_serialnumber, String city_id, String oui_old,
//			String device_serialnumber_old) {
//		int flag = 0;
//		String sql = "";
//		// 获取新的用户记录ID
//		long userid = DataSetBean.getMaxId("tab_egwcustomer", "user_id");
//
//		// 新增用户数据，将原有信息输入新的记录
//		sql = "insert into tab_egwcustomer"
//				+ "(user_id,gather_id,username,passwd,city_id,cotno,bill_type_id,next_bill_type_id,"
//				+ "cust_type_id,user_type_id,bindtype,virtualnum,numcharacter,access_style_id,aut_flag,"
//				+ "service_set,realname,sex,cred_type_id,credno,address,office_id,zone_id,access_kind_id,"
//				+ "trade_id,licenceregno,occupation_id,education_id,vipcardno,contractno,linkman,linkman_credno,"
//				+ "linkphone,linkaddress,mobile,email,agent,agent_credno,agentphone,adsl_res,adsl_card,adsl_dev,"
//				+ "adsl_ser,isrepair,bandwidth,ipaddress,overipnum,ipmask,gateway,macaddress,device_id,device_ip,"
//				+ "device_shelf,device_frame,device_slot,device_port,basdevice_id,basdevice_ip,basdevice_shelf,"
//				+ "basdevice_frame,basdevice_slot,basdevice_port,vlanid,workid,user_state,opendate,onlinedate,"
//				+ "pausedate,closedate,updatetime,staff_id,remark,phonenumber,cableid,bwlevel,vpiid,vciid,adsl_hl,"
//				+ "userline,dslamserialno,movedate,dealdate,opmode,maxattdnrate,upwidth,oui,device_serialnumber,"
//				+ "serv_type_id,max_user_number,wan_value_1,wan_value_2,open_status) "
//				+ "select "
//				+ userid
//				+ ",'"
//				+ gather_id
//				+ "','"
//				+ username
//				+ "',passwd,'"
//				+ city_id
//				+ "',cotno,bill_type_id,next_bill_type_id,"
//				+ "cust_type_id,user_type_id,bindtype,virtualnum,numcharacter,access_style_id,aut_flag,"
//				+ "service_set,realname,sex,cred_type_id,credno,address,office_id,zone_id,access_kind_id,"
//				+ "trade_id,licenceregno,occupation_id,education_id,vipcardno,contractno,linkman,linkman_credno,"
//				+ "linkphone,linkaddress,mobile,email,agent,agent_credno,agentphone,adsl_res,adsl_card,adsl_dev,"
//				+ "adsl_ser,isrepair,bandwidth,ipaddress,overipnum,ipmask,gateway,macaddress,device_id,device_ip,"
//				+ "device_shelf,device_frame,device_slot,device_port,basdevice_id,basdevice_ip,basdevice_shelf,"
//				+ "basdevice_frame,basdevice_slot,basdevice_port,vlanid,workid,'1',opendate,onlinedate,"
//				+ "pausedate,closedate,updatetime,staff_id,remark,phonenumber,cableid,bwlevel,vpiid,vciid,adsl_hl,"
//				+ "userline,dslamserialno,movedate,dealdate,opmode,maxattdnrate,upwidth,'"
//				+ oui
//				+ "','"
//				+ device_serialnumber
//				+ "',"
//				+ "10,max_user_number,wan_value_1,wan_value_2,open_status from tab_egwcustomer where oui = '"
//				+ oui_old
//				+ "' and device_serialnumber = '"
//				+ device_serialnumber_old
//				+ "' and username = '"
//				+ username
//				+ "' and user_state='1'";
//		flag = DataSetBean.executeUpdate(sql);
//
//		logger.debug(sql);
//
//		// 将原有数据的user_state更新为4
//		sql = "update tab_egwcustomer set user_state = '4' ,updatetime="
//				+ curTime1 + " where (oui = '" + oui_old
//				+ "' or oui is null) and (device_serialnumber = '"
//				+ device_serialnumber_old
//				+ "' or device_serialnumber is null) and username = '"
//				+ username + "'";
//		DataSetBean.executeUpdate(sql);
//
//		logger.debug(sql);
//
//		return flag;
//	}

	/**
	 * 获取用户admin的信息，用于Corba接口
	 * 
	 * @return 用户信息
	 * 
	 */
	private Map getUserInfo() {
		Cursor userCursor = null;
		Map userFields = null;
		PrepareSQL psql = new PrepareSQL("select acc_loginname,acc_password from tab_accounts where acc_loginname = 'admin'");
		psql.getSQL();
		userCursor = DataSetBean
				.getCursor("select acc_loginname,acc_password from tab_accounts where acc_loginname = 'admin'");
		userFields = userCursor.getNext();

		return userFields;

	}

	/**
	 * 现场安装时没有选择设备时的操作，只保存现场安装表和用户表
	 * 
	 * @return 错误信息
	 * 
	 */
//	private String saveNodevice(HttpServletRequest request) {
//		int retMsg = 0;
//		String username = request.getParameter("username");
//		String city_id = request.getParameter("city_id");
//		String dealStaff = request.getParameter("dealStaff");
//
//		// 表tab_userinst的新数据
//		retMsg = addInstInfo("", null, username, dealStaff, 1);
//		if (retMsg != 1) {
//			return "新增用户数据失败，请重试！";
//		}
//
//		// 判断该帐号是否存在
//		cursor = DataSetBean
//				.getCursor("select count(1) as total from tab_hgwcustomer where username = '"
//						+ username + "' and user_state='1'");
//		fields = cursor.getNext();
//		String total = "1";
//		if (fields != null) {
//			total = (String) fields.get("total");
//		}
//
//		// 用户表tab_hgwcustomer中新增数据
//		if (!"0".equals(total)) {
//			retMsg = updHGWInfo("-1", username, "", "", city_id, "1");
//		} else {
//			retMsg = addHGWInfo("-1", username, "", "", city_id, "1");
//		}
//
//		if (retMsg != 1) {
//			return "新增用户数据失败，请重试！";
//		}
//
//		return "用户信息保存成功!";
//	}

	/**
	 * 现场安装时没有选择设备时的操作，只保存现场安装表和用户表
	 * 
	 * @return 错误信息
	 * 
	 */
	private String modifyNodevice(HttpServletRequest request) {
		int retMsg = 0;
		String username = request.getParameter("username");
		String city_id = request.getParameter("city_id");
		String device_id = "";
		String oui_old = "";
		String device_id_old = request.getParameter("device_id_old");

		// 查询原有设备的信息
		// cursor = DataSetBean
		// .getCursor("select gather_id,oui,device_serialnumber from
		// tab_gw_device where device_id = '"
		// + device_id_old + "' ");
		// fields = cursor.getNext();
		// if (fields != null) {
		// oui_old = (String) fields.get("oui");
		// device_serialnumber_old = (String) fields
		// .get("device_serialnumber");
		// } else {
		// oui_old = "";
		// device_serialnumber_old = "";
		// }

		// 用户表tab_hgwcustomer中新增数据
		retMsg = modifyHGWInfo(username, "", "", city_id, device_id,
				device_id_old);
		if (retMsg != 1) {
			return "新增用户数据失败，请重试！";
		}

		return "用户信息保存成功!";
	}

	/**
	 * 保存修障时的选择的错误原因
	 * 
	 * @return
	 * 
	 */
	private void saveFault(String username, String device_id, String fault_id,
			String dealStaff, String dealStaffid) {

		String sql = "insert into tab_devicefault(username,device_id,fault_id,faulttime,dealstaff,dealstaffid) values('"
				+ username
				+ "','"
				+ device_id
				+ "','"
				+ fault_id
				+ "',"
				+ (new Date()).getTime()
				/ 1000
				+ ",'"
				+ dealStaff
				+ "','"
				+ dealStaffid + "')";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		DataSetBean.executeUpdate(sql);
		
	}

	private long getAreaByCity(String city_id) {
		long area_id = 1;
		String tmp = "";
		PrepareSQL psql = new PrepareSQL("select area_id from tab_city_area where city_id = '"
				+ city_id + "'");
		psql.getSQL();
		cursor = DataSetBean
				.getCursor("select area_id from tab_city_area where city_id = '"
						+ city_id + "'");
		fields = cursor.getNext();
		if (fields != null) {
			tmp = (String) fields.get("area_id");
		}

		if (tmp != null && !"".equals(tmp)) {
			area_id = Long.parseLong(tmp);
		}

		logger.debug("area_id={}", area_id);
		return area_id;
	}

	/**
	 * modify by zhaixf 增加分页功能
	 * 
	 * @param request
	 * @return
	 */
	public String getUserInstState(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb
				.append("<table width='100%'  border=0 cellspacing=1 cellpadding=1 bgcolor='#999999'>");
		// 输出表头
		sb.append("<TH>安装地点</TH>");
		sb.append("<TH>操作人</TH>");
		sb.append("<TH>安装数量</TH>");
		sb.append("<TH>详细数据</TH>");

		String sql = "select city_id,parent_id from tab_city";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map map = DataSetBean.getMap(sql);
		// 获取参数
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		String city_id = request.getParameter("city_id");

		// 获取秒数
		long ls_time = 0l;
		long le_time = 0l;

		DateTimeUtil dateTimeUtil = new DateTimeUtil(start_time);
		ls_time = dateTimeUtil.getLongTime();
		dateTimeUtil = new DateTimeUtil(end_time);
		le_time = dateTimeUtil.getLongTime();

		dateTimeUtil = null;

		String stroffset = request.getParameter("offset");
		int pagelen = 50;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);

		PrepareSQL pSQL = new PrepareSQL();
		String sqlUserInstState = "select a.dealstaff,count(a.device_id) num,b.city_id from tab_userinst a,tab_gw_device b where a.device_id=b.device_id and a.device_id is not null and a.dealtime>=? and a.dealtime<=? ";

		// 选择了采集点
		if (!"-1".equals(map.get(city_id))) {
			SelectCityFilter scf = new SelectCityFilter();
			sqlUserInstState += (" and b.city_id in ("
					+ scf.getAllSubCityIds(city_id, true) + ")");
		}

		sqlUserInstState += " group by a.dealstaff,b.city_id";

		// 形成SQL语句
		pSQL.setSQL(sqlUserInstState);
		pSQL.setLong(1, ls_time);
		pSQL.setLong(2, le_time);
		logger.debug(pSQL.getSQL());
		// QueryPage qryp = new QueryPage();
		initPageExt(pSQL.getSQL(), offset, pagelen);
		String strBar = getPageBar();
		// cursor = DataSetBean.getCursor(pSQL.getSQL());

		cursor = DataSetBean.getCursor(pSQL.getSQL(), offset, pagelen);
		// cursor = DataSetBean.getCursor(pSQL.getSQL());

		// 无数据时
		if (cursor.getRecordSize() == 0) {
			sb.append("<tr bgcolor=#ffffff><td colspan=4>没有统计数据.</td></tr>");
		} else {
			// 获取采集点Map
			Map cityIdMap = CityDAO.getCityIdCityNameMap();

			String dealstaff = null;
			String cityDesc = null;
			while ((fields = cursor.getNext()) != null) {
				sb.append("<tr bgcolor=#ffffff>");

				dealstaff = (String) fields.get("dealstaff");
				city_id = (String) fields.get("city_id");
				// gather_id = gather_id.trim();
				cityDesc = (String) cityIdMap.get(city_id);
				if (cityDesc == null)
					cityDesc = "-";
				sb.append("<td>").append(cityDesc).append("</td>");
				sb.append("<td>").append(dealstaff).append("</td>");
				sb.append("<td>").append(fields.get("num")).append("</td>");
				// 将参数放在属性中，在界面上通过js来遍历
				sb.append("<td><span _type=\"detail\" _dealstaff='" + dealstaff
						+ "' _city_id='" + city_id + "'></span></td>");

				sb.append("</tr>");
			}

			cityIdMap = null;
		}

		cursor = null;
		fields = null;
		sb
				.append("<tr bgcolor=#ffffff><td colspan=4 class=green_foot align=right>");
		sb.append(strBar);
		sb.append("</td></tr>");
		sb.append("</table>");
		return sb.toString();
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public String getDetailUserInst(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb
				.append("<table width=\"100%\"  border=0 cellspacing=1 cellpadding=1 bgcolor=\"#999999\">");
		// 输出表头
		sb.append("<TH>安装地点</TH>");
		sb.append("<TH>操作人</TH>");
		sb.append("<TH>设备序列号</TH>");
		sb.append("<TH>用户账号</TH>");
		sb.append("<TH>安装时间</TH>");
		// 获取参数
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		String city_id = request.getParameter("city_id");
		String dealstaff = request.getParameter("dealstaff");
		// 获取秒数
		long ls_time = 0l;
		long le_time = 0l;

		DateTimeUtil dateTimeUtil = new DateTimeUtil(start_time);
		ls_time = dateTimeUtil.getLongTime();
		dateTimeUtil = new DateTimeUtil(end_time);
		le_time = dateTimeUtil.getLongTime();

		dateTimeUtil = null;

		PrepareSQL pSQL = new PrepareSQL();
		String sql = "select b.city_id,a.dealstaff,a.dealtime,b.oui,b.device_serialnumber,a.username from"
				+ " tab_userinst a,tab_gw_device b"
				+ " where a.device_id=b.device_id and b.city_id=? "
				+ " and a.dealtime>=? and a.dealtime<=? and a.dealstaff=? "
				+ " order by a.dealtime ";
		pSQL.setSQL(sql);

		// 形成SQL语句
		pSQL.setString(1, city_id);
		pSQL.setLong(2, ls_time);
		pSQL.setLong(3, le_time);
		pSQL.setString(4, dealstaff);

		cursor = DataSetBean.getCursor(pSQL.getSQL());

		// 无数据时
		if (cursor.getRecordSize() == 0) {
			sb.append("<tr bgcolor=#ffffff><td colspan=5>没有详细数据.</td></tr>");
		} else {
			// 获取采集点Map
			Map cityIdMap = CityDAO.getCityIdCityNameMap();
			String cityDesc = null;

			long sec = 0l;
			while ((fields = cursor.getNext()) != null) {
				sb.append("<tr bgcolor=#ffffff>");

				city_id = (String) fields.get("city_id");
				cityDesc = (String) cityIdMap.get(city_id);
				if (cityDesc == null)
					cityDesc = city_id;

				sb.append("<td>").append(cityDesc).append("</td>");
				sb.append("<td>").append(fields.get("dealstaff")).append(
						"</td>");
				sb.append("<td>").append(fields.get("oui") + "-").append(
						fields.get("device_serialnumber")).append("</td>");
				sb.append("<td>").append(fields.get("username"))
						.append("</td>");
				sec = Long.parseLong((String) fields.get("dealtime"));
				sb.append("<td>").append(
						StringUtils.getDateTimeStr("yyyy-MM-dd HH:mm:ss",
								sec * 1000)).append("</td>");
				sb.append("</tr>");
			}
			cityIdMap = null;
		}

		cursor = null;
		fields = null;
		sb
				.append("<tr bgcolor=#ffffff><td colspan=5 class=green_foot align=right><input type=button value=\"关闭\" onclick=CloseDetail()></td></tr>");
		sb.append("</table>");
		return sb.toString();
	}

	/**
	 * 增加现场安装查询页面－>直接显示出其详细信息，并进行分页处理。
	 * 
	 * @author zhaixf
	 * 
	 */
	public String getDetailUserInstState(HttpServletRequest request) {

		StringBuffer sb = new StringBuffer();
		sb
				.append("<table width=\"100%\"  border=0 cellspacing=1 cellpadding=1 bgcolor=\"#999999\">");
		// 输出表头
		sb.append("<TH>安装地点</TH>");
		sb.append("<TH>操作人</TH>");
		sb.append("<TH>设备序列号</TH>");
		sb.append("<TH>用户账号</TH>");
		sb.append("<TH>安装时间</TH>");

		// 获取参数
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		String city_id = request.getParameter("city_id");
		String username = request.getParameter("username");
		String device_serialnumber = request
				.getParameter("device_serialnumber");

		String sql = "select city_id,parent_id from tab_city";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map map = DataSetBean.getMap(sql);
		// 获取秒数
		long ls_time = 0l;
		long le_time = 0l;

		DateTimeUtil dateTimeUtil = new DateTimeUtil(start_time);
		ls_time = dateTimeUtil.getLongTime();
		dateTimeUtil = new DateTimeUtil(end_time);
		le_time = dateTimeUtil.getLongTime();

		dateTimeUtil = null;
		String stroffset = request.getParameter("offset");
		int pagelen = 50;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);

		PrepareSQL pSQL = new PrepareSQL();
		StringBuilder sqlUserInstState = new StringBuilder();
		sqlUserInstState
				.append("select b.city_id , a.dealstaff, b.oui, b.device_serialnumber, a.username, a.dealtime  from tab_userinst a, tab_gw_device b  where a.device_id=b.device_id and a.device_id is not null and a.dealtime>=? and a.dealtime<=? ");

		// 选择了采集点
		if (!"-1".equals(map.get(city_id))) {
			SelectCityFilter scf = new SelectCityFilter();
			sqlUserInstState.append(" and b.city_id in ("
					+ scf.getAllSubCityIds(city_id, true) + ")");
		}
		// 按用户账号查询
		if (username != null && username.length() > 0 && !("".equals(username))) {
			sqlUserInstState.append(" and a.username like '%");
			sqlUserInstState.append(username);
			sqlUserInstState.append("%'");
		}
		// 按设备序列号查询
		if (device_serialnumber != null && device_serialnumber.length() > 0
				&& !("".equals(device_serialnumber))) {
			if(device_serialnumber.length()>5){
				sqlUserInstState.append(" and b.dev_sub_sn ='").append(device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length())).append("'");
			}
			sqlUserInstState.append(" and b.device_serialnumber like '%");
			sqlUserInstState.append(device_serialnumber);
			sqlUserInstState.append("'");
		}

		// 形成SQL语句
		pSQL.setSQL(sqlUserInstState.toString());
		pSQL.setLong(1, ls_time);
		pSQL.setLong(2, le_time);
		logger.debug(pSQL.getSQL());

		// QueryPage qryp = new QueryPage();
		// qryp.initPage(pSQL.getSQL(), offset, pagelen);

		initPageExt(pSQL.getSQL(), offset, pagelen);
		String strBar = getPageBar();
		cursor = DataSetBean.getCursor(pSQL.getSQL(), offset, pagelen);
		// 无数据时
		if (cursor.getRecordSize() == 0) {
			sb.append("<tr bgcolor=#ffffff><td colspan=5>没有统计数据.</td></tr>");
		} else {
			// 获取采集点Map
			Map cityIdMap = CityDAO.getCityIdCityNameMap();

			String dealstaff = null;
			String cityDesc = null;
			long sec = 0l;
			while ((fields = cursor.getNext()) != null) {
				sb.append("<tr bgcolor=#ffffff>");

				dealstaff = (String) fields.get("dealstaff");
				city_id = (String) fields.get("city_id");
				// gather_id = gather_id.trim();
				cityDesc = (String) cityIdMap.get(city_id);
				if (cityDesc == null)
					cityDesc = "-";
				sb.append("<td>").append(cityDesc).append("</td>");
				sb.append("<td>").append(dealstaff).append("</td>");
				sb.append("<td>").append(fields.get("oui") + "-").append(
						fields.get("device_serialnumber")).append("</td>");
				sb.append("<td>").append(fields.get("username"))
						.append("</td>");
				sec = Long.parseLong((String) fields.get("dealtime"));
				sb.append("<td>").append(
						StringUtils.getDateTimeStr("yyyy-MM-dd HH:mm:ss",
								sec * 1000)).append("</td>");
				// 将参数放在属性中，在界面上通过js来遍历
				// sb.append("<td><span _type=\"detail\" _dealstaff='" +
				// dealstaff + "' _city_id='"+ city_id + "'></span></td>");

				sb.append("</tr>");
			}

			cityIdMap = null;
		}

		cursor = null;
		fields = null;
		sb
				.append("<tr bgcolor=#ffffff><td colspan=5 class=green_foot align = right>");
		sb.append(strBar);
		sb.append("</td></tr>");
		sb.append("</table>");
		return sb.toString();
	}

	/**
	 * 现场安装查询页面－>详细信息excel导出。
	 * 
	 * @author zhaixf
	 * 
	 */
	public String getDetailUserInstExcel(HttpServletRequest request) {

		StringBuffer sb = new StringBuffer();
		sb
				.append("<table width=\"100%\"  border=0 cellspacing=1 cellpadding=1 bgcolor=\"#999999\">");
		// 输出表头
		sb.append("<TH>安装地点</TH>");
		sb.append("<TH>操作人</TH>");
		sb.append("<TH>设备序列号</TH>");
		sb.append("<TH>用户账号</TH>");
		sb.append("<TH>安装时间</TH>");

		// 获取参数
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		String city_id = request.getParameter("city_id");
		String username = request.getParameter("username");
		String device_serialnumber = request
				.getParameter("device_serialnumber");

		String sql = "select city_id,parent_id from tab_city";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map map = DataSetBean.getMap(sql);
		// 获取秒数
		long ls_time = 0l;
		long le_time = 0l;

		DateTimeUtil dateTimeUtil = new DateTimeUtil(start_time);
		ls_time = dateTimeUtil.getLongTime();
		dateTimeUtil = new DateTimeUtil(end_time);
		le_time = dateTimeUtil.getLongTime();

		dateTimeUtil = null;

		PrepareSQL pSQL = new PrepareSQL();
		StringBuilder sqlUserInstState = new StringBuilder();
		sqlUserInstState
				.append(" select b.city_id , a.dealstaff, b.oui, b.device_serialnumber, a.username, a.dealtime  from tab_userinst a, tab_gw_device b  where a.device_id=b.device_id and a.device_id is not null and a.dealtime>=? and a.dealtime<=? ");

		// 选择属地
		if (!"-1".equals(map.get(city_id))) {
			SelectCityFilter scf = new SelectCityFilter();
			sqlUserInstState.append(" and b.city_id in ("
					+ scf.getAllSubCityIds(city_id, true) + ")");
		}
		// 按用户账号查询
		if (username != null && username.length() > 0 && !("".equals(username))) {
			sqlUserInstState.append(" and a.username like '%");
			sqlUserInstState.append(username);
			sqlUserInstState.append("%'");
		}
		// 按设备序列号查询
		if (device_serialnumber != null && device_serialnumber.length() > 0
				&& !("".equals(device_serialnumber))) {
			if(device_serialnumber.length()>5){
				sqlUserInstState.append(" and b.dev_sub_sn ='").append(device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length())).append("'");
			}
			sqlUserInstState.append(" and b.device_serialnumber like '%");
			sqlUserInstState.append(device_serialnumber);
			sqlUserInstState.append("'");
		}

		// 形成SQL语句
		pSQL.setSQL(sqlUserInstState.toString());
		pSQL.setLong(1, ls_time);
		pSQL.setLong(2, le_time);
		logger.debug(pSQL.getSQL());

		cursor = DataSetBean.getCursor(pSQL.getSQL());
		// 无数据时
		if (cursor.getRecordSize() == 0) {
			sb.append("<tr bgcolor=#ffffff><td colspan=5>没有统计数据.</td></tr>");
		} else {
			// 获取采集点Map
			Map cityIdMap = CityDAO.getCityIdCityNameMap();

			String dealstaff = null;
			String cityDesc = null;
			long sec = 0l;
			while ((fields = cursor.getNext()) != null) {
				sb.append("<tr bgcolor=#ffffff>");

				dealstaff = (String) fields.get("dealstaff");
				city_id = (String) fields.get("city_id");
				// gather_id = gather_id.trim();
				cityDesc = (String) cityIdMap.get(city_id);
				if (cityDesc == null)
					cityDesc = "-";
				sb.append("<td>").append(cityDesc).append("</td>");
				sb.append("<td>").append(dealstaff).append("</td>");
				sb.append("<td>").append(fields.get("oui") + "-").append(
						fields.get("device_serialnumber")).append("</td>");
				sb.append("<td>").append(fields.get("username"))
						.append("</td>");
				sec = Long.parseLong((String) fields.get("dealtime"));
				sb.append("<td>").append(
						StringUtils.getDateTimeStr("yyyy-MM-dd HH:mm:ss",
								sec * 1000)).append("</td>");
				sb.append("</tr>");
			}

			cityIdMap = null;
		}

		cursor = null;
		fields = null;
		sb.append("</table>");
		return sb.toString();
	}

	/**
	 * @param city_id
	 * @return flag 江苏电信特殊处理，只有苏州（0200）泰州（0800）才验证通过
	 */
	@SuppressWarnings("unused")
	private int checkCity(String city_id) {
		int flag = 0;
		String parent_id = "";

		String sql = "select parent_id from tab_city where city_id = '"
				+ city_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();

		if (fields != null) {
			parent_id = (String) fields.get("parent_id");
		}

		if ("0200".equals(city_id) || "0800".equals(city_id)) {
			flag = 1;
		} else if ("0200".equals(parent_id) || "0800".equals(parent_id)) {
			flag = 1;
		}

		return flag;
	}

	/**
	 * 根据用户名获取用户密码信息（企业网关）
	 * 
	 * @param username
	 * @return
	 */
	private String getPasswdByUsername(String username) {
		String passwd = "";

		String sql = "select passwd from tab_egwcustomer where username = '"
				+ username + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();

		if (fields != null) {
			passwd = (String) fields.get("passwd");
		}

		return passwd;
	}


	/**
	 * 根据采集点获取ior信息
	 * 
	 * @param gather_id
	 * @param user
	 * @return
	 */
	public String getIor(String gather_id, User user) {

		return user.getIor("ACS_" + gather_id, "ACS_Poa_" + gather_id);
	}

	/**
	 * 根据用户帐号判断需要的设备类型
	 * 
	 * @param username
	 *            宽带帐号
	 * @return 1：家庭网关 2：企业网关 ""：两种都可能活=或都不可能
	 */
	public String getDeviceType(String username) {

		String gw_type = "";
		String isHGW = "";
		String isEGW = "";

		String sql1 = "select * from tab_hgwcustomer where username = '"
				+ username + "' and user_state='1'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql1 = "select username from tab_hgwcustomer where username = '"
					+ username + "' and user_state='1'";
		}
		// 判断该帐号是否家庭网关
		PrepareSQL psql = new PrepareSQL(sql1);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql1);
		fields = cursor.getNext();
		if (fields != null) {
			isHGW = "1";
		}

		String sql2 = "select * from tab_egwcustomer where username = '"
				+ username + "' and user_state='1'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql2 = "select username from tab_egwcustomer where username = '"
					+ username + "' and user_state='1'";
		}
		// 判断该帐号是否企业网关
		psql = new PrepareSQL(sql2);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql2);
		fields = cursor.getNext();
		if (fields != null) {
			isEGW = "1";
		}

		if ("1".equals(isHGW) && !"1".equals(isEGW)) {
			gw_type = "1";
		} else if (!"1".equals(isHGW) && "1".equals(isEGW)) {
			gw_type = "2";
		}

		return gw_type;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public String releaseAct(HttpServletRequest request) {

		String msg = "";

		String shortName = Global.instAreaShortName;

		ArrayList sqlArr = new ArrayList();
		String gw_type = request.getParameter("gw_type");

		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		String city_id = user.getCityId();

		String deviceIDS = request.getParameter("deviceIDS");
		String userNameS = request.getParameter("userNameS");

		// 更新设备资源表,将设备的属地更新成解绑用户的属地.
		String sqlCity = "select city_id,parent_id from tab_city";
		PrepareSQL psql = new PrepareSQL(sqlCity);
		psql.getSQL();
		Map map = DataSetBean.getMap(sqlCity);
		String pcityId = (String) map.get(city_id);
		// 本身和其父属地都不是省中心
		String sql;
		if (!pcityId.equals("00") && !city_id.equals("00")) {
			if ("2".equals(gw_type)) {
				// 企业网关用户
				// sql = "update tab_gw_device set city_id='" + pcityId
				// + "', cpe_allocatedstatus=0,customer_id='' where
				// device_serialnumber in ("
				// + deviceSNS + ")";
				sql = "update tab_gw_device set city_id='00', cpe_allocatedstatus=0,customer_id=null where device_id in ("
						+ deviceIDS + ")";

			} else {
				if (Global.XJDX.equals(shortName)) {
					// 更新用户资源表,如果是新疆电信，则不更改属地
					sql = "update tab_gw_device set cpe_allocatedstatus=0 where device_id in ("
							+ deviceIDS + ")";
				} else {
					// 更新用户资源表,将用户的OUI和设备序列号更新成空
					sql = "update tab_gw_device set city_id='"
							+ pcityId
							+ "', cpe_allocatedstatus=0 where device_id in ("
							+ deviceIDS + ")";
				}

			}
		} else {
			if ("2".equals(gw_type)) {
				// 企业网关用户
				sql = "update tab_gw_device set city_id='00', cpe_allocatedstatus=0,customer_id=null where device_id in ("
						+ deviceIDS + ")";
			} else {
				if (Global.XJDX.equals(shortName)) {
					// 更新用户资源表,如果是新疆电信，则不更改属地
					sql = "update tab_gw_device set cpe_allocatedstatus=0 where device_id in ("
							+ deviceIDS + ")";
				} else {
					// 更新用户资源表,将用户的OUI和设备序列号更新成空
					sql = "update tab_gw_device set city_id='"
							+ city_id
							+ "', cpe_allocatedstatus=0 where device_id in ("
							+ deviceIDS + ")";
				}

			}
		}
		// String sql = "update tab_gw_device set city_id='" + city_id +
		// "' where device_id in (" + deviceIDS + ")";

		psql = new PrepareSQL(sql);
		psql.getSQL();

		sqlArr.add(sql);

		if ("2".equals(gw_type)) {
			// 企业网关用户
			sql = "update tab_egwcustomer set oui=null,device_serialnumber=null,device_id=null where username in ("
					+ userNameS + ")";
		} else {
			// 更新用户资源表,将用户的OUI和设备序列号更新成空
			sql = "update tab_hgwcustomer set oui=null,device_serialnumber=null,device_id=null where username in ("
					+ userNameS + ") and user_state in ('1','2') ";
		}

		psql = new PrepareSQL(sql);
		psql.getSQL();

		sqlArr.add(sql);

		// 从域资源表中删除
		sql = "delete from tab_gw_res_area where res_id in (select device_id from tab_gw_device where device_id in("
				+ deviceIDS + ")) and res_type =1 and area_id in (select area_id from tab_city_area where city_id='"+city_id+"')";

		psql = new PrepareSQL(sql);
		psql.getSQL();

		sqlArr.add(sql);

		int[] flag = DataSetBean.doBatch(sqlArr);

		if ("2".equals(gw_type)) {
			// User user = curUser.getUser();
			MCControlManager mc = new MCControlManager(user.getAccount(), user
					.getPasswd());

			String[] device_array = deviceIDS.split(",");
			for (int i = 0; i < device_array.length; i++)
			{
				device_array[i] = device_array[i].replaceAll("'", "");
			}
			int retMsg = mc.reloadDeviceAraeInfo(device_array);

			if (retMsg != 0) {
				logger.debug("解绑--通知后台CORBA失败(调用MC时)!");
				// UserinstLog.log(request, "。");
				// returnMsg = "通知后台失败。";
			} else {
				logger.debug("解绑--通知后台CORBA成功(调用MC时)!");

				// UserinstLog.log(request, "解绑--通知后台CORBA成功(调用MC时)。");
			}
		}
		if (flag != null && flag.length > 0) {
			msg = "用户设备解绑成功!";
		} else {
			msg = "用户设备解绑失败!";
		}

		return msg;

	}

	/**
	 * 初始化页面信息
	 */
	public void initPageExt(String query, int start, int len) {
		offset = start;
		MaxLine = len;
		this.query = query;
		total = getTotalExt();
		totalPage = (int) Math.ceil((double) total / MaxLine);
		curPage = (int) Math.floor((double) offset / MaxLine + 1);
	}

	/**
	 * 获取所有记录条数
	 */
	private int getTotalExt() {

		Cursor cursor = DataSetBean.getCursor(this.query);
		return (cursor.getRecordSize());

	}

	/**
	 * 
	 * @param device_status
	 * @param city_id
	 * @param device_id
	 * @return
	 */
	private String getUpdDeviceInfo(String device_status, String city_id,
			String device_id) {

		String sql = "update tab_gw_device set device_status = "
				+ device_status + ",cpe_allocatedstatus=1 , city_id = '"
				+ city_id + "' where device_id = '" + device_id + "'";
		sql += ";";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return sql;
	}

	/**
	 * 域表操作
	 * 
	 * @param
	 * @author zhaixf
	 * @date 2008-8-8
	 * @return String
	 */
	public static String insertAreaTable(String device_id, String curAreaId,
			boolean needDel) {
		String strSql = "";
		if (needDel) {
			strSql = "delete from tab_gw_res_area where res_id='" + device_id
					+ "'" + " and res_type=1;";
		}

		List list = AreaManageSyb.getAllareaPid(curAreaId);
		int listSize = list.size() - 1;
		while (listSize >= 0) {
			if (null == list.get(listSize) || list.get(listSize).equals("1")
					|| list.get(listSize).equals("0")) {
			} else {
				strSql += "insert into tab_gw_res_area(res_type,res_id,area_id) values(1,'"
						+ device_id + "'," + list.get(listSize) + ");";
			}
			--listSize;
		}
		PrepareSQL psql = new PrepareSQL(strSql);
		psql.getSQL();
		return strSql;
	}

	public long getMaxUserId() {
		if(DBUtil.GetDB() == Global.DB_ORACLE || DBUtil.GetDB() == Global.DB_SYBASE) {
			return getMaxUserIdOld();
		}
		return DbUtils.getUnusedID("sql_tab_hgwcustomer", 1);
	}
	
	/**
	 * 
	 * @return
	 */
	public long getMaxUserIdOld() {
		long userid = -1L;
		String str_userId = "";
		String callPro = "maxHgwUserIdProc 1";
		PrepareSQL psql = new PrepareSQL(callPro);
		psql.getSQL();
		Map map = DataSetBean.getRecord(callPro);
		if (null != map && !map.isEmpty()) {
			str_userId = map.values().toArray()[0].toString();
		} else {
			userid = DataSetBean.getMaxId("tab_hgwcustomer", "user_id");
			str_userId = String.valueOf(userid);
		}
		return Long.valueOf(str_userId);
	}
}
