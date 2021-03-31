<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.commons.db.DBUtil"%>
<%@ page import="com.linkage.module.gwms.util.StringUtil"%>
<%
	request.setCharacterEncoding("GBK");
	QueryPage qryp = new QueryPage();
	//放动态表名的固定部分
	ArrayList list = new ArrayList();
	//获取所有的动态表名的固定部分
	String strSQL = "";
	Cursor cursor = null;
	Map fields = null;
	String stroffset = request.getParameter("offset");
	String objName = request.getParameter("objName");
	String queryName = request.getParameter("queryName");
	queryName = (queryName == null || queryName.compareTo("null") == 0) ? ""
			: queryName;
	//不是点击的下一页
	if (stroffset == null)
	{
		session.putValue("qName", queryName);
		session.putValue("oName", objName);
	}
	//如果是点击下一页
	else
	{
		queryName = (String) session.getValue("qName");
		objName = (String) session.getValue("oName");
	}
	strSQL = "select name from sysobjects where type='U' and name like '%"
			+ queryName + "%' order by name";
	if (DBUtil.GetDB() == 1)
	{// oracle
		strSQL = "select table_name as name from user_tables where table_name like '%"
		+ StringUtil.getUpperCase(queryName) + "%' order by table_name";
	}
	else if (DBUtil.GetDB() == 2)
	{// sybase
		strSQL = "select name from sysobjects where type='U' and name like '%"
		+ queryName + "%' order by name";
	}
	//out.println(strSQL);
	String strData = "";
	int pagelen = 15;
	int offset;
	if (stroffset == null)
		offset = 1;
	else
		offset = Integer.parseInt(stroffset);
	qryp.initPage(strSQL, offset, pagelen);
	String strBar = qryp.getPageBar();
	cursor = DataSetBean.getCursor(strSQL, offset, pagelen);
	fields = cursor.getNext();
	if (fields == null)
	{
		strData = "<TR ><TD COLSPAN=5 HEIGHT=30 class=column>系统没有表信息</TD></TR>";
	}
	else
	{
		int i = 1;
		while (fields != null)
		{
			strData += "<TR>";
			strData += "<TD class=column1 align=left>" + i + "</TD>";
			strData += "<TD class=column1 align=left>" + fields.get("name")
			+ "</TD>";
			strData += "<TD class=column2 align=center><A HREF=\"javascript:Edit('"
			+ fields.get("name") + "');\">选择</A></TD>";
			strData += "</TR>";
			i++;
			fields = cursor.getNext();
		}
		strData += "<TR BGCOLOR=#FFFFFF><TD COLSPAN=5 align=right>" + strBar
		+ "</TD></TR>";
	}
%>
<%@ include file="../head.jsp"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.QueryPage"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	
	function queryTable()
	{
		var page="tab_select.jsp?objName="+frm.objName.value+"&queryName="+frm.queryName.value;
		window.navigate(page);	
		
	}

	function Edit(_tab_name)
	{
		
		var obj=eval("opener."+frm.objName.value);
		obj.value=_tab_name;
		window.close();		
	}
//-->
</SCRIPT>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="tab_backupSave.jsp" onsubmit="return CheckForm()">
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TD bgcolor="#ffffff" colspan="5" >系统表选择</TD>
					</TR>
					<TR>
						<TD	class=column colspan=5>
							表名<input type="text" name="queryName" class=bk value=<%=queryName%>>
							<input type="hidden" name="objName" value="<%=objName%>">
							&nbsp;&nbsp;
							<input type="button" name="query" value=" 查 询 " class=btn onclick="javascript:queryTable();">
						</TD>


					</TR>
					<TR>
						<TH nowrap>序号</TH>						
						<TH nowrap>表名</TH>
						<TH nowrap>操作</TH>
					</TR>
					<%=strData%>					
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	
	
	</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
