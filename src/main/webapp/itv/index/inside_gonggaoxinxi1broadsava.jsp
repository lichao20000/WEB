
<%@page
	import="com.linkage.module.lims.system.common.database.DataSetBean"%>
<%@page
	import="com.linkage.module.lims.itv.businessprocess.util.StringUtils"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<%
	request.setCharacterEncoding("GBK");
	String strMsg = null;
	String strSQL = "";
	String strAction = request.getParameter("action");
	if (strAction.equals("delete"))
	{
		//删除操作
		String Str_title = request.getParameter("id");
		strSQL = "delete from tab_broad_info where id=" + Str_title;
	}
	else
	{
		String city_id_old = request.getParameter("city_id_old");
		String Str_title = request.getParameter("id");
		String Str_service_year = request.getParameter("service_year");
		String[] Str_kind = request.getParameterValues("kind");
		String Str_biaoti = request.getParameter("biaoti");
		String Str_content = request.getParameter("content");
		String Str_kinds = "";
		for (int i = 0; i < Str_kind.length; i++)
		{
			if (Str_kinds.equals(""))
		Str_kinds = Str_kind[i];
			else
		Str_kinds += "," + Str_kind[i];
		}
		if (strAction.equals("add"))
		{
			//增加操作
			//判断是否重复
			strSQL = "select  title from tab_broad_info where id=" + Str_title;
			if (DataSetBean.getRecord(strSQL) != null)
		strMsg = "标题\"" + Str_biaoti + "\"已经存在，请换一个标题。";
			else
			{
		long id = DataSetBean.getMaxId("tab_broad_info", "id") + 1;
		strSQL = "insert into tab_broad_info (id,title,content,titletype,city_id) values ("
				+ id
				+ ",'"
				+ Str_biaoti
				+ "','"
				+ Str_content
				+ "','"
				+ Str_service_year + "','" + Str_kinds + "')";
		strSQL = StringUtils.replace(strSQL, ",,", ",null,");
		strSQL = StringUtils.replace(strSQL, ",,", ",null,");
		strSQL = StringUtils.replace(strSQL, ",)", ",null)");
			}
		}
		else
		{
			//修改操作
			//判断是否重复
			strSQL = "select * from tab_broad_info where id=" + Str_title;
			if (DataSetBean.getRecord(strSQL) != null)
		strMsg = "标题\"" + Str_biaoti + "\"已经存在，请换一个标题。";
			else
			{
		long id = DataSetBean.getMaxId("tab_broad_info", "id") + 1;
		strSQL = "update tab_broad_info set id=" + id + ",title='"
				+ Str_biaoti + "',content='" + Str_content
				+ "',titletype='" + Str_service_year + "',city_id='"
				+ Str_kinds + "' where title='" + city_id_old + "'";
		strSQL = StringUtils.replace(strSQL, "=,", "=null,");
		strSQL = StringUtils.replace(strSQL, "= where", "=null where");
			}
		}
	}
	if (strMsg == null || !strSQL.equals(""))
	{
		int iCode = DataSetBean.executeUpdate(strSQL);
		if (iCode > 0)
		{
			strMsg = "手工公告信息操作成功！";
		}
		else
		{
			strMsg = "手工公告信息操作失败，请返回重试或稍后再试！";
		}
	}
%>

<TABLE class=querytable width="50%" align="center">
	<TR>
		<TH class="title_1">
			公告操作提示信息
		</TH>
	</TR>
	<TR height="50">
		<TD align="center" valign=middle>
			<font size="2"> 
			<%
			 out.println(strMsg);
			 %> 
 			</font>
		</TD>
	</TR>
	<TR>
		<TD class=foot align="right">
			<div class="right">
				<button NAME="cmdJump"
					onclick="javascript:document.location.href='inside_gonggaoxinxi1broad.jsp'">
					列 表
				</button>
				<button NAME="cmdBack" onclick="javascript:history.go(-1);">
					返 回
				</button>
			</div>
		</TD>
	</TR>
</TABLE>

