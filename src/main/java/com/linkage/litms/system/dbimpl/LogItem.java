
package com.linkage.litms.system.dbimpl;

import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.litms.common.util.DesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.Encoder;
import com.linkage.litms.common.util.LoginUtil;
import com.linkage.litms.system.ModuleLog;
import com.linkage.litms.system.SystemMaintenance;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.tree.Tree;
import com.linkage.litms.webtopo.warn.MyFile;
import com.linkage.module.gwms.Global;

/**
 * 记录数据库中的tab_item中的信息，供记录记录日志表或查询日志表使用
 * 
 * @author wp
 */
@SuppressWarnings("all")
public class LogItem
{

	/** logger */
	private static Logger loger = LoggerFactory.getLogger(LogItem.class);
	private HashMap allItems = new HashMap(10);
	private static String itemAll = "select * from tab_item where item_visual='1'";
	private static String writeItemLogSql = "insert into tab_oper_log("
			+ "acc_oid,acc_login_ip,operationlog_type,operation_time,operation_name"
			+ ",operation_object,operation_content,operation_device"
			+ ",operation_result,result_id) values(?,?,?,?,?,?,?,?,?,?)";
	// private static String selectItemsByUserID ="select distinct * from
	// tab_item where item_id in(select item_id from tab_item_role where role_id
	// in(select role_id from tab_acc_role where acc_oid =?))";
	private static String selectItemsByUserID = "select distinct * from tab_item where"
			+ " item_id in(select item_id from "
			+ LipossGlobals.getLipossProperty("Systype")
			+ " where role_id in(select role_id from tab_acc_role where acc_oid =?))";
	/** 记录登录失败日志 */
	private String writeLoginLogSql = "insert into errorlogin("
			+ "acc_loginname,acc_login_ip,time,errorlogin_desc) values(?,?,?,?)";
	// 日志自动备份定时器
	private Timer logTimer = null;
	// 日志LogItem的单态实例
	private static LogItem instance = null;
	// 日志备份间隔时间
	private static long period = 24 * 60 * 60 * 1000;
	// 查询某个时间段的日志信息
	private String selectLogSQL = "select a.*,b.acc_loginname"
			+ " from tab_oper_log a,tab_accounts b where"
			+ " a.acc_oid = b.acc_oid and operation_time>=? and operation_time<?"
			+ " order by b.acc_loginname ";
	// 日志备份目录
	private static String path = LipossGlobals.getLipossHome() + File.separator
			+ "LOGBAK";
	private Map<String, String> errordescMap = new HashMap<String, String>()
	{

		private static final long serialVersionUID = 1L;
		{
			put("1", "认证失败");
			put("2", "密码过期");
			put("3", "处于锁定时间段内");
		}
	};

	public static LogItem getInstance()
	{
		if (null == instance)
		{
			instance = new LogItem();
		}
		return instance;
	}

	/**
	 * 构造函数
	 */
	private LogItem()
	{
		loger.debug("LogItem()");
		// 初始化定时器
		logTimer = new Timer();
		DateTimeUtil now = new DateTimeUtil();
		DateTimeUtil day = new DateTimeUtil(now.getDate() + " 01:00:00");
		day = new DateTimeUtil(day.getNextDateTime("day", 1));
		now = null;
		try
		{
			logTimer.schedule(new LogBak(), day.getDateTime(), period);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// 初始化数型结构
		initItem();
	}

	/**
	 * 获取某个模块下的各功能点的访问量
	 * 
	 * @param request
	 * @return
	 */
	public HashMap getModuleItemLog(HttpServletRequest request)
	{
		loger.debug("getModuleItemLog(request)");
		String module_name = request.getParameter("module_name");
		// 模块下的功能点MAP
		HashMap itemModuleMap = new HashMap();
		Tree tree = new Tree();
		tree.getModuleItem(module_name, itemModuleMap);
		// 模块下的功能点访问量MAP
		HashMap itemLogMap = new HashMap();
		// 没有查询对应的功能点信息，那么返回0长度的map
		if (0 == itemModuleMap.size())
		{
			return itemLogMap;
		}
		/**
		 * 准备SQL语句
		 */
		Iterator it = itemModuleMap.keySet().iterator();
		String itemStr = null;
		loger.debug("item_size={}", itemModuleMap.size());
		while (it.hasNext())
		{
			if (null == itemStr)
			{
				itemStr = "'" + (String) it.next() + "'";
			}
			else
			{
				itemStr += ",'" + (String) it.next() + "'";
			}
		}
		// 统计字段
		String column = "operation_name";
		// 吉林联通统计 operation_object
		if (Global.JLLT.equals(Global.instAreaShortName))
		{
			column = "operation_object";
		}
		String start_day = request.getParameter("start_day");
		String end_day = request.getParameter("end_day");
		String logSQL = "select " + column + " ,count(*) num from tab_oper_log where 1=1";
		if (null != start_day)
		{
			logSQL += " and operation_time>=" + new DateTimeUtil(start_day).getLongTime();
		}
		if (null != end_day)
		{
			logSQL += " and operation_time<" + new DateTimeUtil(end_day).getLongTime();
		}
		logSQL += " and " + column + " in(" + itemStr + ") group by " + column
				+ " order by num desc";
		// 查询数据库
		PrepareSQL psql = new PrepareSQL(logSQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(logSQL);
		Map fields = cursor.getNext();
		String item_name = "";
		String number = "";
		while (null != fields)
		{
			item_name = (String) fields.get(column);
			number = (String) fields.get("num");
			itemLogMap.put(item_name, number);
			fields = cursor.getNext();
		}
		return itemLogMap;
	}

	/**
	 * 获取日志统计信息
	 * 
	 * @return
	 */
	public HashMap getModuleLog(HttpServletRequest request)
	{
		loger.debug("getModuleLog(request)");
		HashMap moduleLogMap = new HashMap();
		String start_day = request.getParameter("start_day");
		String end_day = request.getParameter("end_day");
		// 遍历获取item_name和module_name的对应关系
		HashMap moduleMap = new HashMap();
		Tree tree = new Tree();
		tree.getSystemRootTree(moduleMap);
		// 统计字段
		String column = "operation_name";
		// 吉林联通统计 operation_object
		if (Global.JLLT.equals(Global.instAreaShortName))
		{
			column = "operation_object";
		}
		String logSQL = "select " + column + ",count(*) num from tab_oper_log where "
				+ column + "!='未知'";
		if (null != start_day && !"".equals(start_day))
		{
			logSQL += " and operation_time>=" + new DateTimeUtil(start_day).getLongTime();
		}
		if (null != end_day && !"".equals(end_day))
		{
			logSQL += " and operation_time<" + new DateTimeUtil(end_day).getLongTime();
		}
		logSQL += " group by " + column;
		PrepareSQL psql = new PrepareSQL(logSQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(logSQL);
		Map fields = cursor.getNext();
		// 没有记录的情况下，直接返回
		if (null == fields)
		{
			return moduleLogMap;
		}
		loger.debug("item_logSIZE:{}", cursor.getRecordSize());
		String operation_name = "";
		long number = 0;
		String module_name = "";
		ModuleLog moduleLog = null;
		// 遍历日志记录进行日志统计
		while (null != fields)
		{
			operation_name = (String) fields.get(column);
			number = Long.parseLong((String) fields.get("num"));
			// 根据功能点名字，没有找到匹配的模块，则跳到下层循环
			if (null == moduleMap.get(operation_name))
			{
				fields = cursor.getNext();
				continue;
			}
			module_name = (String) moduleMap.get(operation_name);
			// moduleLogMap已经有了这个模块的信息，那就只需要更新了
			if (moduleLogMap.containsKey(module_name))
			{
				moduleLog = (ModuleLog) moduleLogMap.get(module_name);
			}
			else
			{
				moduleLog = new ModuleLog(module_name);
			}
			// 更新统计数
			moduleLog.update(operation_name, number);
			moduleLogMap.put(module_name, moduleLog);
			fields = cursor.getNext();
		}
		// clear
		fields = null;
		cursor = null;
		moduleMap = null;
		return moduleLogMap;
	}

	/**
	 * @author Alex.Yan (yanhj@lianchuang.com)
	 * @version 2.0, Jul 28, 2009
	 * @see
	 * @since 1.0
	 */
	public class LogBak extends TimerTask
	{

		public void run()
		{
			DateTimeUtil now = new DateTimeUtil();
			now = new DateTimeUtil(now.getDate() + " 00:00:00");
			// 今天的0点
			long endTime = now.getLongTime();
			// 昨天的0点
			long startTime = endTime - 24 * 60 * 60;
			String fileName = "WEB_" + new DateTimeUtil(startTime * 1000).getShortDate();
			now = null;
			fileName = path + File.separator + fileName;
			// 不存在创建文件，存在删除重创
			MyFile.clearFile(fileName);
			BufferedWriter xmlWriter = MyFile.openAppendWriter(fileName);
			String content = "用户名|客户端IP|日志类型|操作时间|操作名称|操作对象|操作内容|操作终端|操作结果";
			MyFile.WriteXml(xmlWriter, content);
			// 查询数据库
			PrepareSQL pSQL = new PrepareSQL();
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL)
			{
				selectLogSQL = "select a.*,b.acc_loginname"
						+ " from tab_oper_log a,tab_accounts b where"
						+ " a.acc_oid = b.acc_oid and operation_time>=? and operation_time<?"
						+ " order by b.acc_loginname ";
			}
			pSQL.setSQL(selectLogSQL);
			pSQL.setLong(1, startTime);
			pSQL.setLong(2, endTime);
			Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
			Map fields = cursor.getNext();
			StringBuffer sb = null;
			int logType = 1;
			String logTypeStr = "";
			while (null != fields)
			{
				sb = new StringBuffer(500);
				sb.append((String) fields.get("acc_loginname") + "|");
				sb.append((String) fields.get("acc_login_ip") + "|");
				logType = Integer.parseInt((String) fields.get("operationlog_type"));
				switch (logType)
				{
					case 1:
						logTypeStr = "操作日志";
						break;
					case 2:
						logTypeStr = "系统日志";
						break;
					case 3:
						logTypeStr = "安全日志";
						break;
					default:
						logTypeStr = "操作日志";
				}
				sb.append(logTypeStr + "|");
				sb.append(new DateTimeUtil(
						Long.parseLong((String) fields.get("operation_time")) * 1000)
								.getLongDate()
						+ "|");
				sb.append((String) fields.get("operation_name") + "|");
				sb.append((String) fields.get("operation_object") + "|");
				sb.append((String) fields.get("operation_content") + "|");
				sb.append((String) fields.get("operation_device") + "|");
				sb.append((String) fields.get("operation_result"));
				content = sb.toString();
				// 把行内容写日志
				MyFile.WriteXml(xmlWriter, content);
				fields = cursor.getNext();
			}
			MyFile.closeWriter(xmlWriter);
		}
	}

	/**
	 * 初始化itemHashMap，获取系统中所有的功能点
	 */
	private synchronized void initItem()
	{
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL)
		{
			itemAll = "select item_name, item_url from tab_item where item_visual='1'";
		}
		com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(
				itemAll);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(itemAll);
		Map fields = cursor.getNext();
		String itemName = "";
		String itemUrl = "";
		if (null != fields)
		{
			allItems.clear();
			while (null != fields)
			{
				itemName = (String) fields.get("item_name");
				itemUrl = (String) fields.get("item_url");
				if (!"".equals(itemName.trim()) && !"".equals(itemUrl.trim()))
				{
					if (-1 != itemUrl.indexOf("?"))
					{
						itemUrl = itemUrl.substring(0, itemUrl.indexOf("?"));
					}
					allItems.put(itemUrl.trim(), itemName.trim());
				}
				fields = cursor.getNext();
			}
		}
		else
		{
			loger.warn("NO Item!");
		}
	}

	/**
	 * 获取系统中所有功能点
	 * 
	 * @return
	 */
	public HashMap getallItems()
	{
		if (0 == allItems.size())
		{
			initItem();
		}
		return allItems;
	}

	/**
	 * 根据URL获取Item名称，作为日志记录表中的操作名称
	 * 
	 * @param url
	 */
	public String getItemName(String url)
	{
		// item链表为空或根据URL没有从链表中找到对应的功能点，从数据库重新装载一次功能节点
		if (0 == allItems.size() || !allItems.containsKey(url))
		{
			initItem();
		}
		Object itemNameOb = allItems.get(url);
		String itemName = "未知";
		if (null != itemNameOb)
		{
			itemName = (String) itemNameOb;
		}
		return itemName;
	}

	/**
	 * 用户Web上记录用户日志
	 * 
	 * @param request
	 * @param type
	 *            日志类型
	 * @param device
	 *            设备
	 * @param content
	 *            操作内容
	 * @param result
	 *            结果
	 */
	public void writeItemLog(HttpServletRequest request, int type, String device,
			String content, String result)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		String operation_name = getItemName(request.getServletPath());
		if (operation_name == null || "".equals(operation_name)
				|| "未知".equals(operation_name))
		{
			return;
		}
		// 20200716 区分家庭网关gw_type=1和企业网关gw_type=2
		String gw_type = request.getParameter("gw_type");
		if (!StringUtil.IsEmpty(gw_type))
		{
			if (gw_type.equals("1") && operation_name.contains("企业"))
			{
				operation_name = operation_name.replace("企业", "家庭");
			}
			if (gw_type.equals("2") && operation_name.contains("家庭"))
			{
				operation_name = operation_name.replace("家庭", "企业");
			}
		}
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(writeItemLogSql);
		pSQL.setLong(1, user.getId());
		pSQL.setString(2, request.getRemoteHost());
		pSQL.setInt(3, type);
		pSQL.setLong(4, new DateTimeUtil().getLongTime());
		if (1 == type)
		{
			pSQL.setString(5, "1");
		}
		else if (2 == type)
		{
			pSQL.setString(5, "0");
		}
		else if (3 == type)
		{
			pSQL.setString(5, "0");
		}
		else
		{
			pSQL.setString(5, "0");
		}
		pSQL.setString(6, operation_name);
		// 空字符串或null就填入null
		if (null == content || "".equals(content))
		{
			content = null;
			pSQL.setStringExt(7, content, false);
		}
		else
		{
			try
			{
				content = Encoder.toGB(content);
			}
			catch (Exception e)
			{
				content = "";
			}
			pSQL.setString(7, content);
		}
		if (null == device || "".equals(device))
		{
			device = null;
			pSQL.setStringExt(8, device, false);
		}
		else
		{
			pSQL.setString(8, device);
		}
		try
		{
			result = Encoder.toGB(result);
		}
		catch (Exception e)
		{
			result = "成功";
		}
		pSQL.setString(9, result);
		if ("失败".equals(result))
		{
			pSQL.setInt(10, 0);
		}
		else
		{
			pSQL.setInt(10, 1);
		}
		DataSetBean.executeUpdate(pSQL.getSQL());
	}

	/**
	 * 登录日志
	 * 
	 * @param request
	 * @param resultId
	 */
	public void addLoginLog(HttpServletRequest request, int resultId)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(writeItemLogSql);
		if (curUser != null)
		{
			User user = curUser.getUser();
			pSQL.setLong(1, user.getId());
		}
		else
		{
			pSQL.setLong(1, 0);
		}
		pSQL.setString(2, request.getRemoteHost());
		pSQL.setInt(3, 1);
		pSQL.setLong(4, new DateTimeUtil().getLongTime());
		pSQL.setString(5, "4");
		pSQL.setString(6, "WEB");
		pSQL.setString(7, "系统登录");
		pSQL.setString(8, "WEB");
		if (resultId == 1)
		{
			pSQL.setString(9, "成功");
		}
		else
		{
			pSQL.setString(9, "失败");
		}
		pSQL.setInt(10, resultId);
		DataSetBean.executeUpdate(pSQL.getSQL());
	}

	/**
	 * 用户Web上记录用户日志
	 * 
	 * @param request
	 * @param type
	 * @param device
	 * @param content
	 * @param result
	 * @param operation_name
	 */
	public void writeItemLog_other(HttpServletRequest request, int type, String device,
			String content, String result, String operation_name)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(writeItemLogSql);
		pSQL.setLong(1, user.getId());
		pSQL.setString(2, request.getRemoteHost());
		pSQL.setInt(3, type);
		pSQL.setLong(4, new DateTimeUtil().getLongTime());
		pSQL.setString(5, operation_name);
		pSQL.setString(6, request.getServletPath());
		// 空字符串或null就填入null
		if (null == content || "".equals(content))
		{
			content = null;
			pSQL.setStringExt(7, content, false);
		}
		else
		{
			try
			{
				content = Encoder.toGB(content);
			}
			catch (Exception e)
			{
				content = "";
			}
			pSQL.setString(7, content);
		}
		if (null == device || "".equals(device))
		{
			device = null;
			pSQL.setStringExt(8, device, false);
		}
		else
		{
			pSQL.setString(8, device);
		}
		try
		{
			result = Encoder.toGB(result);
		}
		catch (Exception e)
		{
			result = "成功";
		}
		pSQL.setString(9, result);
		if ("失败".equals(result))
		{
			pSQL.setInt(10, 1);
		}
		else
		{
			pSQL.setInt(10, 0);
		}
		DataSetBean.executeUpdate(pSQL.getSQL());
	}

	/**
	 * 记录登录错误日志
	 * 
	 * @param request
	 * @param desc
	 */
	public void writeLoginLog(HttpServletRequest request, String desc)
	{
		try
		{
			desc = Encoder.toGB(desc);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			desc = "";
		}
		String acc_loginname = LoginUtil.getAccount(request);
		String ip = request.getRemoteHost();
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(writeLoginLogSql);
		pSQL.setString(1, acc_loginname);
		pSQL.setString(2, ip);
		pSQL.setLong(3, new DateTimeUtil().getLongTime());
		pSQL.setString(4, desc);
		DataSetBean.executeUpdate(pSQL.getSQL());
		addLoginLog(request, 0);
	}

	/**
	 * 查询登录错误日志
	 * 
	 * @param request
	 * @return
	 */
	public ArrayList getLoginLog(HttpServletRequest request)
	{
		loger.debug("getLoginLog(request)");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long acc_oid = user.getId();
		ArrayList list = new ArrayList();
		list.clear();
		String stroffset = request.getParameter("offset");
		int offset;
		int pagelen = 20;
		if (stroffset == null)
		{
			offset = 1;
		}
		else
		{
			offset = Integer.parseInt(stroffset);
		}
		// CQDX-REQ-ITMS-20190516-YZJ-001（登录日志查询功能优化）
		String userName = request.getParameter("username");
		String startTime = request.getParameter("time_start");
		String endTime = request.getParameter("time_end");
		StringBuffer search = new StringBuffer();
		/**
		 * 查询登录日志
		 */
		String sql = "select a.* from errorlogin a,tab_accounts b where"
				+ " a.acc_loginname=b.acc_loginname";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL)
		{
			sql = "select a.acc_loginname, a.acc_login_ip, a.time, a.errorlogin_desc "
					+ " from errorlogin a,tab_accounts b where"
					+ " a.acc_loginname=b.acc_loginname";
		}
		if (user.isAdmin() == false)
		{
			sql += " and (b.creator=" + acc_oid + " or b.acc_oid=" + acc_oid + ")";
		}
		// CQDX-REQ-ITMS-20190516-YZJ-001（登录日志查询功能优化）
		String strBar = null;
		if (Global.CQDX.equals(Global.instAreaShortName))
		{
			if (!StringUtil.IsEmpty(userName))
			{
				sql += " and a.ACC_LOGINNAME = '" + userName + "'";
				search.append("&username=").append(userName);
			}
			if (!StringUtil.IsEmpty(startTime))
			{
				sql += " and a.TIME >= " + new DateTimeUtil(startTime).getLongTime();
				search.append("&time_start=").append(startTime);
			}
			if (!StringUtil.IsEmpty(endTime))
			{
				sql += " and a.TIME <= " + new DateTimeUtil(endTime).getLongTime();
				search.append("&time_end=").append(endTime);
			}
			sql += " order by a.TIME desc";
		}
		QueryPage qryp = new QueryPage();
		qryp.initPage(sql, offset, pagelen);
		if (Global.CQDX.equals(Global.instAreaShortName))
		{
			strBar = qryp.getPageBar(search.toString());
		}
		else
		{
			strBar = qryp.getPageBar();
		}
		list.add(strBar);
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql, offset, pagelen);
		list.add(cursor);
		return list;
	}

	/**
	 * 查询登录错误和成功的日志
	 * 
	 * @param request
	 * @return
	 */
	public ArrayList<Object> getLoginLog_nxdx(HttpServletRequest request)
	{
		loger.debug("getLoginLog_nxdx(request)");
		ArrayList<Object> list = new ArrayList<Object>();
		list.clear();
		String stroffset = request.getParameter("offset");
		int offset;
		int pagelen = 20;
		if (stroffset == null)
		{
			offset = 1;
		}
		else
		{
			offset = Integer.parseInt(stroffset);
		}
		String search = getSqlInfo(request).get(0);
		String sql = getSqlInfo(request).get(1);
		QueryPage qryp = new QueryPage();
		qryp.initPage(sql, offset, pagelen);
		String strBar = qryp.getPageBar(search);
		list.add(strBar);
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql, offset, pagelen);
		list.add(cursor);
		return list;
	}

	/**
	 * 获取sql等查询信息
	 * 
	 * @param request
	 * @return
	 */
	private ArrayList<String> getSqlInfo(HttpServletRequest request)
	{
		ArrayList<String> list = new ArrayList<String>();
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long acc_oid = user.getId();
		// 获取查询条件
		String userName = request.getParameter("username");
		String startTime = request.getParameter("time_start");
		String endTime = request.getParameter("time_end");
		int result = StringUtil.getIntegerValue(request.getParameter("result"));
		String errorcode = request.getParameter("errordesc");
		String errordesc = null;
		if (!"-1".equals(errorcode))
		{
			errordesc = errordescMap.get(errorcode);
		}
		StringBuffer search = new StringBuffer();
		StringBuffer sqlbf = new StringBuffer();
		// 失败日志的sql
		StringBuffer sqlFail = new StringBuffer();
		sqlFail.append(
				"select a.* from errorlogin a, tab_accounts b where a.acc_loginname = b.acc_loginname ");
		// 成功日志的sql
		StringBuffer sqlSucc = new StringBuffer();
		sqlSucc.append(
				"select d.acc_loginname, c.acc_login_ip, c.operation_time as time, c.operation_result as errorlogin_desc from");
		sqlSucc.append(
				" tab_oper_log c, tab_accounts d where c.acc_oid = d.acc_oid and c.operation_content='系统登录' and c.result_id = 1");
		if (!user.isAdmin())
		{
			sqlFail.append(" and (b.creator = ").append(acc_oid)
					.append(" or b.acc_oid = ").append(acc_oid).append(") ");
			sqlSucc.append(" and (d.creator = ").append(acc_oid)
					.append(" or c.acc_oid = ").append(acc_oid).append(") ");
		}
		if (!StringUtil.IsEmpty(userName))
		{
			sqlFail.append(" and a.acc_loginname = '").append(userName).append("'");
			sqlSucc.append(" and d.acc_loginname = '").append(userName).append("'");
			search.append("&username=").append(userName);
		}
		if (!StringUtil.IsEmpty(errordesc))
		{
			sqlFail.append(" and a.errorlogin_desc = '").append(errordesc).append("'");
			search.append("&errordesc=").append(errorcode);
		}
		if (!StringUtil.IsEmpty(startTime))
		{
			sqlFail.append(" and a.time >= ")
					.append(new DateTimeUtil(startTime).getLongTime());
			sqlSucc.append(" and c.operation_time >= ")
					.append(new DateTimeUtil(startTime).getLongTime());
			search.append("&time_start=").append(startTime);
		}
		if (!StringUtil.IsEmpty(endTime))
		{
			sqlFail.append(" and a.time <= ")
					.append(new DateTimeUtil(endTime).getLongTime());
			sqlSucc.append(" and c.operation_time <= ")
					.append(new DateTimeUtil(endTime).getLongTime());
			search.append("&time_end=").append(endTime);
		}
		switch (result)
		{
			case -1:
				sqlbf.append("select e.* from ( ").append(sqlFail).append(" union ")
						.append(sqlSucc).append(" ) e order by e.time desc");
				search.append("&result=").append(result);
				break;
			case 1:
				sqlbf.append("select e.* from ( ").append(sqlFail)
						.append(" ) e order by e.time desc");
				search.append("&result=").append(result);
				break;
			case 2:
				sqlbf.append("select e.* from ( ").append(sqlSucc)
						.append(" ) e order by e.time desc");
				search.append("&result=").append(result);
				break;
			default:
				break;
		}
		list.add(search.toString());
		list.add(sqlbf.toString());
		return list;
	}

	/**
	 * 登录日志导出
	 * 
	 * @param request
	 * @return
	 */
	public ArrayList<Cursor> getLoginLogExport(HttpServletRequest request)
	{
		loger.debug("getLoginLogExport(request)");
		ArrayList<Cursor> list = new ArrayList<Cursor>();
		String sql = getSqlInfo(request).get(1);
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		list.add(cursor);
		return list;
	}

	/**
	 * 根据查询表单中提交的数据查询对应的日志信息
	 * 
	 * @param request
	 * @return
	 */
	public ArrayList getLogs(HttpServletRequest request)
	{
		loger.debug("getLogs(request)");
		ArrayList list = new ArrayList();
		list.clear();
		String stroffset = request.getParameter("offset");
		String device_oui = request.getParameter("device_oui");
		String device_serialnumber = request.getParameter("device_serialnumber");
		int pagelen = 30;
		int offset;
		String selectDetailLogSQL = "";
		String start_day = request.getParameter("start_day");
		String start_time = request.getParameter("start_time");
		String end_day = request.getParameter("end_day");
		String end_time = request.getParameter("end_time");
		String param = "";
		if (stroffset == null)
		{
			offset = 1;
		}
		else
		{
			offset = Integer.parseInt(stroffset);
		}
		/**
		 * 构造sql语句
		 */
		param += "&start_day=" + start_day + "&start_time=" + start_time + "&end_day="
				+ end_day + "&end_time=" + end_time;
		String startdateStr = start_day + " " + start_time;
		String enddateStr = end_day + " " + end_time;
		long startTime = new DateTimeUtil(startdateStr).getLongTime();
		long endTime = new DateTimeUtil(enddateStr).getLongTime();
		if (null == device_serialnumber || "".equals(device_serialnumber.trim()))
		{
			selectDetailLogSQL = "select a.*,b.acc_loginname from tab_oper_log a,tab_accounts b where a.acc_oid = b.acc_oid and a.operation_time >="
					+ startTime + " and a.operation_time<=" + endTime;
		}
		// 操作终端的查询
		else
		{
			param += "&device_oui=" + device_oui + "&device_serialnumber="
					+ device_serialnumber;
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			User user = curUser.getUser();
			if (user.isAdmin())
			{
				selectDetailLogSQL = "select a.acc_login_ip,a.operationlog_type,a.operation_time,a.operation_name,a.operation_object,a.operation_content,a.result_id,c.oui,c.device_serialnumber,a.operation_device,a.operation_result,b.acc_loginname from tab_oper_log a,tab_accounts b, tab_gw_device c where c.device_id =a.operation_device and a.acc_oid = b.acc_oid and a.operation_time >="
						+ startTime + " and a.operation_time<=" + endTime;
			}
			else
			{
				selectDetailLogSQL = "select a.acc_login_ip,a.operationlog_type,a.operation_time,a.operation_name,a.operation_object,a.operation_content,a.result_id,c.oui,c.device_serialnumber,a.operation_device,a.operation_result,b.acc_loginname from tab_oper_log a,tab_accounts b, tab_gw_device c, tab_gw_res_area d  where c.device_id =a.operation_device and c.device_id=d.res_id and d.res_type=1 and d.area_id="
						+ user.getAreaId()
						+ "and a.acc_oid = b.acc_oid and a.operation_time >=" + startTime
						+ " and a.operation_time<=" + endTime;
			}
			if (device_serialnumber.trim().length() > 5)
			{
				selectDetailLogSQL += " and c.dev_sub_sn ='"
						+ device_serialnumber.substring(device_serialnumber.length() - 6,
								device_serialnumber.length())
						+ "'";
			}
			selectDetailLogSQL += " and c.device_serialnumber like '%"
					+ device_serialnumber.trim() + "'";
		}
		String acc_oid = request.getParameter("userSelect");
		String operation_name = request.getParameter("operation_nameSelect");
		String operation_object = request.getParameter("operation_objectSelect");
		int ordertype = Integer.parseInt(request.getParameter("ordertype"));
		int logtype = Integer.parseInt(request.getParameter("logtype"));
		if (null != acc_oid && !"-1".equals(acc_oid))
		{
			param += "&userSelect=" + acc_oid;
			selectDetailLogSQL += " and a.acc_oid =" + acc_oid;
		}
		if (null != operation_name && !"-1".equals(operation_name))
		{
			param += "&operation_nameSelect=" + operation_name;
			selectDetailLogSQL += " and a.operation_name='" + operation_name + "'";
		}
		if (null != operation_object && !"-1".equals(operation_object))
		{
			param += "&operation_objectSelect=" + operation_object;
			selectDetailLogSQL += " and a.operation_object='" + operation_object + "'";
		}
		param += "&logtype=" + logtype;
		selectDetailLogSQL += " and a.operationlog_type=" + logtype;
		param += "&ordertype=" + ordertype;
		if (1 == ordertype)
		{
			selectDetailLogSQL += " order by a.acc_oid asc,a.operation_time asc";
		}
		else
		{
			selectDetailLogSQL += " order by a.acc_oid desc,a.operation_time desc";
		}
		QueryPage qryp = new QueryPage();
		qryp.initPage(selectDetailLogSQL, offset, pagelen);
		String strBar = qryp.getPageBar(param);
		list.add(strBar);
		PrepareSQL psql = new PrepareSQL(selectDetailLogSQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(selectDetailLogSQL, offset, pagelen);
		list.add(cursor);
		return list;
	}

	/**
	 * 根据查询表单中提交的数据查询对应的日志信息
	 * 
	 * @param req
	 * @return
	 */
	public int bakLog(HttpServletRequest request)
	{
		loger.debug("bakLog(request)");
		int flag = -1;
		String device_serialnumber = request.getParameter("device_serialnumber");
		String selectDetailLogSQL = "";
		String start_day = request.getParameter("start_day");
		String start_time = request.getParameter("start_time");
		String end_day = request.getParameter("end_day");
		String end_time = request.getParameter("end_time");
		String tab_name = request.getParameter("tab_name");
		SystemMaintenance bak = new SystemMaintenance();
		flag = bak.tabItemAct(request);
		if (flag == 0)
		{// 备份表已存在
			return -2;
		}
		else if (flag < 0)
		{
			return flag;
		}
		/**
		 * 构造sql语句
		 */
		String startdateStr = start_day + " " + start_time;
		String enddateStr = end_day + " " + end_time;
		long startTime = new DateTimeUtil(startdateStr).getLongTime();
		long endTime = new DateTimeUtil(enddateStr).getLongTime();
		if (null == device_serialnumber || "".equals(device_serialnumber.trim()))
		{
			if (DBUtil.GetDB() == Global.DB_ORACLE)
			{// oracle
				selectDetailLogSQL = "create table " + tab_name
						+ " as select a.*,b.acc_loginname "
						+ " from tab_oper_log a,tab_accounts b where a.acc_oid = b.acc_oid and a.operation_time >="
						+ startTime + " and a.operation_time<=" + endTime;
			}
			else if (DBUtil.GetDB() == Global.DB_SYBASE)
			{// sybase
				selectDetailLogSQL = "select a.*,b.acc_loginname" + " into " + tab_name
						+ " from tab_oper_log a,tab_accounts b where a.acc_oid = b.acc_oid and a.operation_time >="
						+ startTime + " and a.operation_time<=" + endTime;
			}
			else if (DBUtil.GetDB() == Global.DB_MYSQL)
			{// UDAL
				selectDetailLogSQL = "select a.acc_oid, a.acc_login_ip, a.operationlog_type, a.operation_time, "
						+ " a.operation_object, a.operation_content, a.operation_device, a.operation_result, "
						+ "	a.result_id, a.log_sub_type, b.acc_loginname "
						+ " from tab_oper_log a,tab_accounts b where a.acc_oid = b.acc_oid and a.operation_time >="
						+ startTime + " and a.operation_time<=" + endTime;
				// sql补充
				selectDetailLogSQL = sqlAppend(selectDetailLogSQL, request);
				// 备份
				int result = bakData(tab_name, selectDetailLogSQL);
				
				return result;
			}
		}
		// 操作终端的查询
		else
		{
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			User user = curUser.getUser();
			if (user.isAdmin())
			{
				if (DBUtil.GetDB() == Global.DB_ORACLE)
				{// oracle
					selectDetailLogSQL = "create table " + tab_name
							+ " select a.acc_login_ip,a.operationlog_type,a.operation_time,a.operation_name,a.operation_object,a.operation_content,c.oui,c.device_serialnumber,a.operation_device,a.operation_result,b.acc_loginname "
							+ " from tab_oper_log a,tab_accounts b, tab_gw_device c where c.device_id =a.operation_device and a.acc_oid = b.acc_oid and a.operation_time >="
							+ startTime + " and a.operation_time<=" + endTime;
				}
				else if (DBUtil.GetDB() == Global.DB_SYBASE)
				{// sybase
					selectDetailLogSQL = "select a.acc_login_ip,a.operationlog_type,a.operation_time,a.operation_name,a.operation_object,a.operation_content,c.oui,c.device_serialnumber,a.operation_device,a.operation_result,b.acc_loginname"
							+ " into " + tab_name
							+ " from tab_oper_log a,tab_accounts b, tab_gw_device c where c.device_id =a.operation_device and a.acc_oid = b.acc_oid and a.operation_time >="
							+ startTime + " and a.operation_time<=" + endTime;
				}
				else if (DBUtil.GetDB() == Global.DB_MYSQL)
				{// UDAL
					selectDetailLogSQL = " select a.acc_login_ip, a.operationlog_type, a.operation_time, a.operation_name, "
							+ "a.operation_object, a.operation_content, c.oui, c.device_serialnumber, a.operation_device, "
							+ "a.operation_result, b.acc_loginname "
							+ " from tab_oper_log a,tab_accounts b, tab_gw_device c "
							+ " where c.device_id =a.operation_device and a.acc_oid = b.acc_oid and a.operation_time >= "
							+ startTime + " and a.operation_time <= " + endTime;
					// sql补充
					selectDetailLogSQL = sqlAppend(selectDetailLogSQL, request);
					// 备份
					int result = bakData(tab_name, selectDetailLogSQL);
					
					return result;
				}
			}
			else
			{
				if (DBUtil.GetDB() == Global.DB_ORACLE)
				{// oracle
					selectDetailLogSQL = "create table " + tab_name
							+ " select a.acc_login_ip,a.operationlog_type,a.operation_time,a.operation_name,a.operation_object,a.operation_content,c.oui,c.device_serialnumber,a.operation_device,a.operation_result,b.acc_loginname "
							+ " from tab_oper_log a,tab_accounts b, tab_gw_device c, tab_gw_res_area d  where c.device_id =a.operation_device and c.device_id=d.res_id and d.res_type=1 and d.area_id="
							+ user.getAreaId()
							+ "and a.acc_oid = b.acc_oid and a.operation_time >="
							+ startTime + " and a.operation_time<=" + endTime;
				}
				else if (DBUtil.GetDB() == Global.DB_SYBASE)
				{// sybase
					selectDetailLogSQL = "select a.acc_login_ip,a.operationlog_type,a.operation_time,a.operation_name,a.operation_object,a.operation_content,c.oui,c.device_serialnumber,a.operation_device,a.operation_result,b.acc_loginname"
							+ " into " + tab_name
							+ " from tab_oper_log a,tab_accounts b, tab_gw_device c, tab_gw_res_area d  where c.device_id =a.operation_device and c.device_id=d.res_id and d.res_type=1 and d.area_id="
							+ user.getAreaId()
							+ "and a.acc_oid = b.acc_oid and a.operation_time >="
							+ startTime + " and a.operation_time<=" + endTime;
				}
				else if (DBUtil.GetDB() == Global.DB_MYSQL)
				{// UDAL
					selectDetailLogSQL = "select a.acc_login_ip, a.operationlog_type, a.operation_time, a.operation_name, "
							+ " a.operation_object, a.operation_content, c.oui, c.device_serialnumber, a.operation_device, "
							+ " a.operation_result, b.acc_loginname "
							+ " from tab_oper_log a, tab_accounts b, tab_gw_device c, tab_gw_res_area d  "
							+ " where c.device_id =a.operation_device and c.device_id=d.res_id and d.res_type=1 and d.area_id="
							+ user.getAreaId()
							+ "and a.acc_oid = b.acc_oid and a.operation_time >= "
							+ startTime + " and a.operation_time <= " + endTime;
					// sql补充
					selectDetailLogSQL = sqlAppend(selectDetailLogSQL, request);
					// 备份
					int result = bakData(tab_name, selectDetailLogSQL);
					
					return result;
				}
			}
			if (device_serialnumber.trim().length() > 5)
			{
				selectDetailLogSQL += " and c.dev_sub_sn ='"
						+ device_serialnumber.substring(device_serialnumber.length() - 6,
								device_serialnumber.length())
						+ "'";
			}
			selectDetailLogSQL += " and c.device_serialnumber like '%"
					+ device_serialnumber.trim() + "'";
		}
		
		// sql补充
		selectDetailLogSQL = sqlAppend(selectDetailLogSQL, request);
		
		PrepareSQL psql = new PrepareSQL(selectDetailLogSQL);
		psql.getSQL();
		flag = DataSetBean.executeUpdate(selectDetailLogSQL);
		return flag;
	}
	
	/**
	 * 数据备份
	 * @param tab_name
	 * @param selectDetailLogSQL
	 * @return
	 */
	private int bakData(String tab_name, String selectDetailLogSQL)
	{
		// 创建备份表
		int createResult = createBakTable(tab_name);
		loger.warn("createResult==>{}", createResult);
		
		if (createResult < 0)
		{
			return createResult;
		}
		
		PrepareSQL psql = new PrepareSQL(selectDetailLogSQL);
		ArrayList bakDataList = DataSetBean.executeQuery(psql.getSQL(), null);
		
		if (bakDataList == null || bakDataList.isEmpty())
		{
			return -3;
		}
		
		// 备份数据
		int[] insertResultArr = insertData(tab_name, bakDataList);
		
		loger.warn("insertResultArr==>{}",insertResultArr.length);
		
		for (int i = 0; i < insertResultArr.length; i++)
		{
			loger.warn("insertResultArr[i]==>{}",insertResultArr[i]);
		}
		
		if(insertResultArr == null || insertResultArr.length == 0) {
			return -4;
		}
		
		return 1;
	}

	/**
	 * sql补充
	 * @param selectDetailLogSQL
	 * @param request
	 * @return
	 */
	public String sqlAppend(String selectDetailLogSQL, HttpServletRequest request) {
		
		String acc_oid = request.getParameter("userSelect");
		String operation_name = request.getParameter("operation_nameSelect");
		String operation_object = request.getParameter("operation_objectSelect");
		int logtype = Integer.parseInt(request.getParameter("logtype"));
		if (null != acc_oid && !"-1".equals(acc_oid))
		{
			selectDetailLogSQL += " and a.acc_oid =" + acc_oid;
		}
		if (null != operation_name && !"-1".equals(operation_name))
		{
			selectDetailLogSQL += " and a.operation_name='" + operation_name + "'";
		}
		if (null != operation_object && !"-1".equals(operation_object))
		{
			selectDetailLogSQL += " and a.operation_object='" + operation_object + "'";
		}
		selectDetailLogSQL += " and a.operationlog_type=" + logtype;
		
		return selectDetailLogSQL;
	}

	/**
	 * 备份数据
	 * @param tab_name
	 * @param bakDataList
	 */
	private int[] insertData(String tab_name, ArrayList bakDataList) {
		
		String insertSql = "";
		PrepareSQL insertSqlPs = new PrepareSQL();
		String[] insertSqlArr = new String[bakDataList.size()];
		Map map = null;
		
		for (int i = 0; i < bakDataList.size(); i++) {
			
			if(bakDataList.get(i) == null) {
				continue;
			}
			
			try {
				map = (Map)bakDataList.get(i);
			} catch (Exception e) {
				continue;
			}
			
			insertSql = "insert into " + tab_name 
					+ "(acc_oid, acc_login_ip, operationlog_type, operation_time, operation_object, operation_name, "
					+ "operation_content, operation_device, operation_result, result_id, log_sub_type, acc_loginname, "
					+ "oui, device_serialnumber) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			insertSqlPs.setSQL(insertSql);
			insertSqlPs.setInt(1, StringUtil.getIntValue(map, "acc_oid"));
			insertSqlPs.setString(2, getStringValue(map, "acc_login_ip"));
			insertSqlPs.setInt(3, StringUtil.getIntValue(map, "operationlog_type"));
			insertSqlPs.setInt(4, StringUtil.getIntValue(map, "operation_time"));
			insertSqlPs.setString(5, getStringValue(map, "operation_name"));
			insertSqlPs.setString(6, getStringValue(map, "operation_object"));
			insertSqlPs.setString(7, getStringValue(map, "operation_content"));
			insertSqlPs.setString(8, getStringValue(map, "operation_device"));
			insertSqlPs.setString(9, getStringValue(map, "operation_result"));
			insertSqlPs.setInt(10, StringUtil.getIntValue(map, "result_id"));
			insertSqlPs.setInt(11, StringUtil.getIntValue(map, "log_sub_type"));
			insertSqlPs.setString(12, getStringValue(map, "acc_loginname"));
			insertSqlPs.setString(13, getStringValue(map, "oui"));
			insertSqlPs.setString(14, getStringValue(map, "device_serialnumber"));
			
			insertSqlArr[i] = insertSqlPs.getSQL();
		}
		
		int[] doBatch = DataSetBean.doBatch(insertSqlArr);
		
		return doBatch;
	}

	// 从map获取数据
	public static String getStringValue(Map map, String columName)
	{
		String stringValue = StringUtil.getStringValue(map, columName);
		if (stringValue == null)
		{
			return "";
		}
		return stringValue;
	}
	

	/**
	 * 创建备份表
	 * 
	 * @param tab_name
	 * @return
	 */
	private int createBakTable(String tab_name)
	{
		String sql = "CREATE TABLE `" + tab_name + "`  ("
				+ "  `acc_oid`				decimal(10,0)		NOT NULL DEFAULT  1,"
				+ "  `acc_login_ip`			varchar(20)         NOT NULL DEFAULT NULL,"
				+ "  `operationlog_type`  	decimal(1,0)		NOT NULL DEFAULT  1,"
				+ "  `operation_time`     	decimal(10,0)       NULL DEFAULT NULL,"
				+ "  `operation_name`     	varchar(50)       	NULL DEFAULT NULL,"
				+ "  `operation_object`   	varchar(50)         NULL DEFAULT NULL,"
				+ "  `operation_content`  	text        	 	NULL DEFAULT NULL,"
				+ "  `operation_device`   	varchar(50)         NULL DEFAULT NULL,"
				+ "  `operation_result`   	varchar(50)         NULL DEFAULT NULL,"
				+ "  `result_id`          	decimal(1,0)        NULL DEFAULT  1,"
				+ "  `log_sub_type`       	decimal(1,0)        NULL DEFAULT NULL,"
				+ "  `acc_loginname`      	varchar(80)         NULL DEFAULT NULL,"
				+ "  `oui`      		  	varchar(6)          NULL DEFAULT NULL,"
				+ "  `device_serialnumber`  varchar(64)         NULL DEFAULT NULL"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='BakTable'";
		PrepareSQL psql = new PrepareSQL(sql);
		return DataSetBean.executeUpdate(psql.getSQL());
	}

	/**
	 * 获取某个用户的所有日志
	 * 
	 * @param request
	 * @return
	 */
	public ArrayList getLogByUserName(HttpServletRequest request) throws Exception
	{
		loger.debug("getLogByUserName(request)");
		DesUtils des = new DesUtils();
		ArrayList list = new ArrayList();
		list.clear();
		String stroffset = request.getParameter("offset");
		String acc_oid = des
				.decrypt(StringUtil.getStringValue(request.getParameter("acc_oid")));
		int pagelen = 30;
		int offset;
		if (stroffset == null)
		{
			offset = 1;
		}
		else
		{
			offset = Integer.parseInt(stroffset);
		}
		DateTimeUtil now = new DateTimeUtil();
		now = new DateTimeUtil(now.getDate());
		String sql = "select a.*,b.acc_loginname from tab_oper_log a,tab_accounts b where a.acc_oid = b.acc_oid and a.acc_oid="
				+ acc_oid + " and operation_time>=" + now.getLongTime()
				+ " order by operation_time desc";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL)
		{
			sql = "select a.operation_time, a.operationlog_type, a.operation_name, a.operation_object, a.operation_content, "
					+ " a.operation_device, a.operation_result, b.acc_loginname "
					+ " from tab_oper_log a,tab_accounts b where a.acc_oid = b.acc_oid and a.acc_oid="
					+ acc_oid + " and operation_time>=" + now.getLongTime()
					+ " order by operation_time desc";
		}
		// 分页
		QueryPage qryp = new QueryPage();
		qryp.initPage(sql, offset, pagelen);
		String strBar = qryp.getPageBar("&acc_oid=" + acc_oid);
		list.add(strBar);
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql, offset, pagelen);
		list.add(cursor);
		return list;
	}

	/**
	 * 根据用户ID获取功能节点的信息
	 * 
	 * @param user_id
	 * @return
	 */
	public Cursor getItemInfosByUserID(long user_id)
	{
		loger.debug("getItemInfosByUserID({})", user_id);
		Cursor cursor = null;
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(selectItemsByUserID);
		pSQL.setLong(1, user_id);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		return cursor;
	}
}
