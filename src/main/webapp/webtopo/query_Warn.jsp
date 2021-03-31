<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<!-- －－－－－－－－－make by 王志猛 5194  告警查询界面。对应为main_contro.js里面得quey_Warn()方法。 -->

<%
	//用户权限检查，如果是非法登陆，则重新定位到登陆界面
	UserRes userRes = (UserRes) session.getAttribute("curUser");
	String model = LipossGlobals.getLipossProperty("SetupModel");
	if (model == null)
	{
		model = "";
	} else
	{
		model = "/" + model;
	}
	if (userRes == null)
	{
		response.sendRedirect(request.getRequestURI().substring(0,
		request.getRequestURI().indexOf("/", 2))
		+ model + "/login.jsp");
		return;
	}

	//---------------设置编码和取得查询得id号码和类型--------------------//
	request.setCharacterEncoding("GBK");
	//设备id
	String eventIn = request.getParameter("warnIndex");
	int len = 0;
	String devicecoding = null;

	if (eventIn != null)
	{
		len = eventIn.split("/").length;
		devicecoding = len == 2 ? null : eventIn.split("/")[2];
		eventIn = eventIn.substring(0, 4);
	} else
	{
		out
		.print("无法取得详细告警信息....................<a href='JavaScript:window.close()'>点击关闭</a>");
	}
	//事件类型－－1未处理，0历史告警
	String eventType = request.getParameter("type");

	//组合表格名
	Calendar cal = Calendar.getInstance();
	int year = cal.get(Calendar.YEAR);
	int week = cal.get(Calendar.WEEK_OF_YEAR) + 1;//数据库的周数比当前的周数存储的多了一周，变态
	String tableName = "event_raw_" + year + "_" + week;
	//-----组合查询sql语句，取得结果记录集合
	String sql = "select * from " + tableName + " where serialno = '"
			+ eventIn + "' and subserialno = " + eventType;
	// teledb
	if (DBUtil.GetDB() == 3) {
		sql = "select eventno, createtime, gather_id, creatorname, gather_id, devicetype, sourcename, sourceip, " +
				"severity, city, displaytitle, displaystring, activestatus" +
				" from " + tableName + " where serialno = '"
				+ eventIn + "' and subserialno = " + eventType;
	}

	sql = len == 2 ? sql + "and devicecoding is null" : sql
			+ " and devicecoding = '" + devicecoding + "'";
	HashMap fields = (HashMap) DataSetBean.getRecord(sql);
	//如果当前的周数无法取得告警的详细信息，那么就查取前一周的记录。

	if (fields == null)
	{
		tableName = "event_raw_" + year + "_" + (week - 1);
		sql = "select * from " + tableName + " where serialno = '"
		+ eventIn + "' and subserialno= " + eventType;
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql = "select eventno, createtime, gather_id, creatorname, gather_id, devicetype, sourcename, sourceip, " +
					"severity, city, displaytitle, displaystring, activestatus" +
					" from " + tableName + " where serialno = '"
					+ eventIn + "' and subserialno= " + eventType;
		}
		sql = len == 2 ? sql + "and devicecoding is null" : sql
		+ " and devicecoding = '" + devicecoding + "'";
		com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
		psql.getSQL();
		fields = (HashMap) DataSetBean.getRecord(sql);
		if (fields == null)
		{
			out
			.print("<script language='JavaScript' type='text/javascript'>window.alert('未取得告警详情。');window.colose();</script>");
			out
			.print("<script language='JavaScript' type='text/javascript'>window.close();</script>");
			return;
		}
	}
	String eventIndex = fields.get("eventno".toLowerCase()) == null ? ""
			: (String) fields.get("eventno".toLowerCase());//事件编号
	DateTimeUtil timeUtil = new DateTimeUtil(
			Long.parseLong((String) fields.get("createtime"
			.toLowerCase())) * 1000);
	String createTime = timeUtil.getLongDate();//事件创立时间
	String warnOwner = fields.get("gather_id".toLowerCase()) == null ? ""
			: (String) fields.get("gather_id".toLowerCase());//告警系统
	String creater = fields.get("creatorname".toLowerCase()) == null ? ""
			: (String) fields.get("creatorname".toLowerCase());//创建者名称

	String createrType = fields.get("gather_id".toLowerCase()) == null ? ""
			: (String) fields.get("gather_id".toLowerCase());//创建者类型
	String deviceType = fields.get("devicetype".toLowerCase()) == null ? ""
			: (String) fields.get("devicetype".toLowerCase());//设备型号
	String deviceCompany = fields.get("sourcename".toLowerCase()) == null ? ""
			: (String) fields.get("sourcename".toLowerCase());//设备名称
	String warnIp = fields.get("sourceip".toLowerCase()) == null ? ""
			: (String) fields.get("sourceip".toLowerCase());//告警ip
	String warnStand = fields.get("severity".toLowerCase()) == null ? ""
			: (String) fields.get("severity".toLowerCase());//告警级别
	try
	{
		int s = Integer.parseInt(warnStand);
		warnStand = s == 3 ? "一般告警" : (s == 4 ? "严重告警" : "普通告警");
	} catch (Exception e)
	{

	}
	String warnCity = fields.get("city".toLowerCase()) == null ? ""
			: (String) fields.get("city".toLowerCase());//告警属地
	String warnTitle = fields.get("displaytitle".toLowerCase()) == null ? ""
			: (String) fields.get("displaytitle".toLowerCase());//告警标题
	String warnContent = fields.get("displaystring".toLowerCase()) == null ? ""
			: (String) fields.get("displaystring".toLowerCase());//告警内容
	String activestatus = fields.get("activestatus".toLowerCase()) == null ? ""
			: (String) fields.get("activestatus".toLowerCase());//告警状态
	try
	{
		int s = Integer.parseInt(activestatus);
		switch (s)
		{

		case 1:
			activestatus = "原始告警";
			break;
		case 2:
			activestatus = "正在关联";
			break;
		case 3:
			activestatus = "正在显示";
			break;
		case 4:
			activestatus = "已经确认";
			break;
		case 5:
			activestatus = "已经处理";
			break;
		default:
			activestatus = "未知状态";
			break;
		}
	} catch (Exception e)
	{

	}
%>

<%@page import="java.util.Calendar"%> 

<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="java.util.HashMap"%>


<%@page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@page import="com.linkage.litms.system.UserRes"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<LINK REL="stylesheet" HREF="../css/css_blue.css" TYPE="text/css">
<title>告警详情</title>
</head>
<body>
<table boder=0 cellspacing=0 cellpadding=0 width="98%" height="100%"
	align="center">
	<tr>
		<td height="5"></td>
	</tr>
	<tr>
		<td bgcolor="#000000">
		<table border=0 cellspacing=1 cellpadding=2 width="100%" height="100%">
			<tr class="blue_title">
				<th colspan="4">〖<%=eventIn%>〗告警详情</th>
			</tr>
			<tr>
				<td class="column1" width="100px">&nbsp;&nbsp;告警编号</td>
				<td bgcolor="#ffffff" width="200px"><%=eventIn%></td>
				<td class="column1" width="100px">&nbsp;&nbsp;创建时间</td>
				<td bgcolor="#ffffff" width="223px"><%=createTime%></td>
			</tr>
			<tr>
				<td class="column1">&nbsp;&nbsp;告警系统</td>
				<td bgcolor="#ffffff"><%=warnOwner%></td>
				<td class="column1">&nbsp;&nbsp;事件编号</td>
				<td bgcolor="#ffffff"><%=eventIndex%></td>
			</tr>
			<tr>
				<td class="column1">&nbsp;&nbsp;创建者名称</td>
				<td bgcolor="#ffffff"><%=creater%></td>
				<td class="column1">&nbsp;&nbsp;创建者类型</td>
				<td bgcolor="#ffffff"><%=createrType%></td>
			</tr>
			<tr>
				<td class="column1">&nbsp;&nbsp;设备型号</td>
				<td bgcolor="#ffffff"><%=deviceType%></td>
				<td class="column1">&nbsp;&nbsp;设备名称</td>
				<td bgcolor="#ffffff"><%=deviceCompany%></td>
			</tr>
			<tr>
				<td class="column1">&nbsp;&nbsp;告警地址</td>
				<td bgcolor="#ffffff"><%=warnIp%></td>
				<td class="column1">&nbsp;&nbsp;告警等级</td>
				<td bgcolor="#ffffff"><%=warnStand%></td>
			</tr>
			<tr>
				<td class="column1">&nbsp;&nbsp;告警状态</td>
				<td bgcolor="#ffffff"><%=activestatus%></td>
				<td class="column1">&nbsp;&nbsp;告警属地</td>
				<td bgcolor="#ffffff"><%=warnCity%></td>
			</tr>
			<tr>
				<td class="column1">&nbsp;&nbsp;告警标题</td>
				<td colspan="3" bgcolor="#ffffff"><%=warnTitle%></td>
			</tr>
			<tr>
				<td class="column1">&nbsp;&nbsp;告警内容</td>
				<td colspan="3" bgcolor="#ffffff"><%=warnContent%></td>
			</tr>
			<tr>
				<td class="blue_foot" align="right" colspan="4"><input
					type="button" value=" 关 闭 " class="jianbian"
					onclick="javascript:window.close()" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html>
