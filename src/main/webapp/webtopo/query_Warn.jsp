<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<!-- ������������������make by ��־�� 5194  �澯��ѯ���档��ӦΪmain_contro.js�����quey_Warn()������ -->

<%
	//�û�Ȩ�޼�飬����ǷǷ���½�������¶�λ����½����
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

	//---------------���ñ����ȡ�ò�ѯ��id���������--------------------//
	request.setCharacterEncoding("GBK");
	//�豸id
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
		.print("�޷�ȡ����ϸ�澯��Ϣ....................<a href='JavaScript:window.close()'>����ر�</a>");
	}
	//�¼����ͣ���1δ����0��ʷ�澯
	String eventType = request.getParameter("type");

	//��ϱ����
	Calendar cal = Calendar.getInstance();
	int year = cal.get(Calendar.YEAR);
	int week = cal.get(Calendar.WEEK_OF_YEAR) + 1;//���ݿ�������ȵ�ǰ�������洢�Ķ���һ�ܣ���̬
	String tableName = "event_raw_" + year + "_" + week;
	//-----��ϲ�ѯsql��䣬ȡ�ý����¼����
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
	//�����ǰ�������޷�ȡ�ø澯����ϸ��Ϣ����ô�Ͳ�ȡǰһ�ܵļ�¼��

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
			.print("<script language='JavaScript' type='text/javascript'>window.alert('δȡ�ø澯���顣');window.colose();</script>");
			out
			.print("<script language='JavaScript' type='text/javascript'>window.close();</script>");
			return;
		}
	}
	String eventIndex = fields.get("eventno".toLowerCase()) == null ? ""
			: (String) fields.get("eventno".toLowerCase());//�¼����
	DateTimeUtil timeUtil = new DateTimeUtil(
			Long.parseLong((String) fields.get("createtime"
			.toLowerCase())) * 1000);
	String createTime = timeUtil.getLongDate();//�¼�����ʱ��
	String warnOwner = fields.get("gather_id".toLowerCase()) == null ? ""
			: (String) fields.get("gather_id".toLowerCase());//�澯ϵͳ
	String creater = fields.get("creatorname".toLowerCase()) == null ? ""
			: (String) fields.get("creatorname".toLowerCase());//����������

	String createrType = fields.get("gather_id".toLowerCase()) == null ? ""
			: (String) fields.get("gather_id".toLowerCase());//����������
	String deviceType = fields.get("devicetype".toLowerCase()) == null ? ""
			: (String) fields.get("devicetype".toLowerCase());//�豸�ͺ�
	String deviceCompany = fields.get("sourcename".toLowerCase()) == null ? ""
			: (String) fields.get("sourcename".toLowerCase());//�豸����
	String warnIp = fields.get("sourceip".toLowerCase()) == null ? ""
			: (String) fields.get("sourceip".toLowerCase());//�澯ip
	String warnStand = fields.get("severity".toLowerCase()) == null ? ""
			: (String) fields.get("severity".toLowerCase());//�澯����
	try
	{
		int s = Integer.parseInt(warnStand);
		warnStand = s == 3 ? "һ��澯" : (s == 4 ? "���ظ澯" : "��ͨ�澯");
	} catch (Exception e)
	{

	}
	String warnCity = fields.get("city".toLowerCase()) == null ? ""
			: (String) fields.get("city".toLowerCase());//�澯����
	String warnTitle = fields.get("displaytitle".toLowerCase()) == null ? ""
			: (String) fields.get("displaytitle".toLowerCase());//�澯����
	String warnContent = fields.get("displaystring".toLowerCase()) == null ? ""
			: (String) fields.get("displaystring".toLowerCase());//�澯����
	String activestatus = fields.get("activestatus".toLowerCase()) == null ? ""
			: (String) fields.get("activestatus".toLowerCase());//�澯״̬
	try
	{
		int s = Integer.parseInt(activestatus);
		switch (s)
		{

		case 1:
			activestatus = "ԭʼ�澯";
			break;
		case 2:
			activestatus = "���ڹ���";
			break;
		case 3:
			activestatus = "������ʾ";
			break;
		case 4:
			activestatus = "�Ѿ�ȷ��";
			break;
		case 5:
			activestatus = "�Ѿ�����";
			break;
		default:
			activestatus = "δ֪״̬";
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
<title>�澯����</title>
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
				<th colspan="4">��<%=eventIn%>���澯����</th>
			</tr>
			<tr>
				<td class="column1" width="100px">&nbsp;&nbsp;�澯���</td>
				<td bgcolor="#ffffff" width="200px"><%=eventIn%></td>
				<td class="column1" width="100px">&nbsp;&nbsp;����ʱ��</td>
				<td bgcolor="#ffffff" width="223px"><%=createTime%></td>
			</tr>
			<tr>
				<td class="column1">&nbsp;&nbsp;�澯ϵͳ</td>
				<td bgcolor="#ffffff"><%=warnOwner%></td>
				<td class="column1">&nbsp;&nbsp;�¼����</td>
				<td bgcolor="#ffffff"><%=eventIndex%></td>
			</tr>
			<tr>
				<td class="column1">&nbsp;&nbsp;����������</td>
				<td bgcolor="#ffffff"><%=creater%></td>
				<td class="column1">&nbsp;&nbsp;����������</td>
				<td bgcolor="#ffffff"><%=createrType%></td>
			</tr>
			<tr>
				<td class="column1">&nbsp;&nbsp;�豸�ͺ�</td>
				<td bgcolor="#ffffff"><%=deviceType%></td>
				<td class="column1">&nbsp;&nbsp;�豸����</td>
				<td bgcolor="#ffffff"><%=deviceCompany%></td>
			</tr>
			<tr>
				<td class="column1">&nbsp;&nbsp;�澯��ַ</td>
				<td bgcolor="#ffffff"><%=warnIp%></td>
				<td class="column1">&nbsp;&nbsp;�澯�ȼ�</td>
				<td bgcolor="#ffffff"><%=warnStand%></td>
			</tr>
			<tr>
				<td class="column1">&nbsp;&nbsp;�澯״̬</td>
				<td bgcolor="#ffffff"><%=activestatus%></td>
				<td class="column1">&nbsp;&nbsp;�澯����</td>
				<td bgcolor="#ffffff"><%=warnCity%></td>
			</tr>
			<tr>
				<td class="column1">&nbsp;&nbsp;�澯����</td>
				<td colspan="3" bgcolor="#ffffff"><%=warnTitle%></td>
			</tr>
			<tr>
				<td class="column1">&nbsp;&nbsp;�澯����</td>
				<td colspan="3" bgcolor="#ffffff"><%=warnContent%></td>
			</tr>
			<tr>
				<td class="blue_foot" align="right" colspan="4"><input
					type="button" value=" �� �� " class="jianbian"
					onclick="javascript:window.close()" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html>
