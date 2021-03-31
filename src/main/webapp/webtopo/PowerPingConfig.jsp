<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.common.database.Cursor, com.linkage.litms.common.database.DataSetBean,java.util.*"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%@ include file="../timelater.jsp"%>
<SCRIPT LANGUAGE="JavaScript" id="idParentFun">
</SCRIPT>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
	request.setCharacterEncoding("GBK");
//	String strIP = request.getParameter("strIP");

	String strData = "";
	String warnmode = "";
	String warnlevel = "";
	String timeoutwarnlevel = "";
	String isstat = "";
	String groupid = "";
//	HashMap isstatMap=new HashMap();

	//在工单表中查找该设备相关工单
	// 0 不需要统计
    // 1 需要统计
//    isstatMap.clear();
//    isstatMap.put("0", "不需要");
//    isstatMap.put("1", "需要");
	String strSql = "select * from pping_group_conf order by groupid";
	// teledb
	if (DBUtil.GetDB() == 3) {
		strSql = "select groupid, warnmode, warnlevel, timeoutwarnlevel, isstat, groupdesc, timeinterval, delaythreshold, timeout" +
				" from pping_group_conf order by groupid";
	}
	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSql);
	psql.getSQL();
	Cursor cursor = DataSetBean.getCursor(strSql);
	Map fields = cursor.getNext();
	if(fields == null){
		strData = "<TR ><TD COLSPAN=10 HEIGHT=30 bgcolor=#FFFFFF>还没有配置PowerPing!</TD></TR>";
	}
	else{
		while(fields != null){
			groupid = (String)fields.get("groupid");
			warnmode = (String)fields.get("warnmode");
			warnlevel = (String)fields.get("warnlevel");
			timeoutwarnlevel = (String)fields.get("timeoutwarnlevel");
			isstat = (String)fields.get("isstat");
			if ("0".equals(warnmode)) {
				warnmode = "只发一次";
			} else if ("1".equals(warnmode)) {
				warnmode = "连续发送";
			} else {
				warnmode = "未知";
			}
			if ("0".equals(warnlevel)) {
				warnlevel = "清除告警";
			} else if ("1".equals(warnlevel)) {
				warnlevel = "事件告警";
			} else if ("2".equals(warnlevel)) {
				warnlevel = "警告告警";
			} else if ("3".equals(warnlevel)) {
				warnlevel = "次要告警";
			} else if ("4".equals(warnlevel)) {
				warnlevel = "主要告警";
			} else if ("5".equals(warnlevel)) {
				warnlevel = "严重告警";
			} else {
				warnlevel = "未知";
			}
			if ("0".equals(timeoutwarnlevel)) {
				timeoutwarnlevel = "清除告警";
			} else if ("1".equals(timeoutwarnlevel)) {
				timeoutwarnlevel = "事件告警";
			} else if ("2".equals(timeoutwarnlevel)) {
				timeoutwarnlevel = "警告告警";
			} else if ("3".equals(timeoutwarnlevel)) {
				timeoutwarnlevel = "次要告警";
			} else if ("4".equals(timeoutwarnlevel)) {
				timeoutwarnlevel = "主要告警";
			} else if ("5".equals(timeoutwarnlevel)) {
				timeoutwarnlevel = "严重告警";
			} else {
				timeoutwarnlevel = "未知";
			}
			if ("0".equals(isstat)) {
				isstat = "不需要";
			} else if ("1".equals(isstat)) {
				isstat = "需要";
			}
			strData += "<TR bgcolor=#FFFFFF>";
			strData += "<TD class=column1>"+ groupid+ " </TD>";
			strData += "<TD class=column2>"+ (String)fields.get("groupdesc") + "</TD>";
			strData += "<TD class=column1>"+ (String)fields.get("timeinterval") + "</TD>";
			strData += "<TD class=column2>"+ (String)fields.get("delaythreshold") + "</TD>";
			strData += "<TD class=column1>"+ (String)fields.get("timeout") + "</TD>";
			strData += "<TD class=column2>"+ isstat + "</TD>";
			strData += "<TD class=column1>"+ warnmode + "</TD>";
			strData += "<TD class=column2>"+ warnlevel + "</TD>";
			strData += "<TD class=column1>"+ timeoutwarnlevel + "</TD>";
			strData += "<TD class=column2><input type='radio' name='operationRadio' value="+groupid+"></TD>";
			strData += "</TR>";
			fields = cursor.getNext();
		}
	}

%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	
	
//-->
</SCRIPT>

<TABLE width="90%" border="0" cellspacing="0" cellpadding="0" align="center" id="idLayerTable">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<TR>
		<TD valign="top" >
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" bgcolor="#000000">
			<TR class="blue_title"> 
			  <TD >ip组ID</TD>
			  <TD >组描述</TD>
			  <TD >检测时间间隔</TD>
			  <TD >时延门限值</TD>
			  <TD >等待超时值</TD>
			  <TD >是否需要统计</TD>
			  <TD >发告警模式</TD>
			  <TD >时延告警等级</TD>
			  <TD >超时告警等级</TD>
			  <TD >操作</TD>
			</TR>
			 <%=strData%>
			 </TABLE>
		</TD>
	</TR>
	<BR>
	<TR>
		<TD bgcolor=#FFFFFF align=center>
			<INPUT TYPE="button" value=" 增加组 " class="jianbian"  onclick="addgroup()">&nbsp;&nbsp;
			<INPUT TYPE="button" value=" 编辑组 " class="jianbian"  onclick="operationBtn('1')">&nbsp;&nbsp;
			<INPUT TYPE="button" value=" 删除组 " class="jianbian" onclick="operationBtn('2')">&nbsp;&nbsp;
			<INPUT TYPE="button" value="查看IP包设置" class="jianbian"  onclick="operationBtn('3')">&nbsp;&nbsp;
			<INPUT TYPE="button" value="查看IP列表" class="jianbian"  onclick="operationBtn('4')">&nbsp;&nbsp;
			<INPUT TYPE="button" value=" 关 闭 " class="jianbian"  onclick="javascript:window.close()">&nbsp;&nbsp;
		</TD>
	</TR>
</TABLE>
<SCRIPT LANGUAGE="JavaScript">
	parent.maxspeed.innerHTML = idLayerTable.innerHTML;	
</SCRIPT>