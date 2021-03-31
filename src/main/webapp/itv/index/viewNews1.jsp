<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<%@page
	import="com.linkage.module.lims.system.common.database.DataSetBean"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.module.lims.system.UserRes"%>
<%@page import="com.linkage.module.lims.system.User"%>
<%
	UserRes curUser = (UserRes) session.getAttribute("curUser");
	String username = "";
	if (curUser != null)
	{
		User user = curUser.getUser();
		username = (String) user.getUserInfo().get("per_name");
	}
	request.setCharacterEncoding("GBK");
	String id = request.getParameter("id");
	String sql = "select id,title,content from tab_broad_info where id=" + id
			+ " and titletype='公告'";
	Map fields = DataSetBean.getRecord(sql);
%>
<%@ page contentType="text/html;charset=GBK"%>

<script language="JavaScript" type="text/JavaScript">
<!--
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function GoSameDevice3(pages)
{
	var page;
	page=pages;
	window.open(page,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}
//-->
</script>

<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			您当前的置：公告显示
		</TD>
	</TR>
</TABLE>
<br>
<table width="98%" class="querytable" align="center">

	<TR>
		<TD class="title_1">
			<%=fields.get("title")%>
		</TD>
	</TR>

	<tr>
		<td>
				<%=fields.get("content")%>
		</td>
	</tr>
	<tr>
		<td class="foot">
			<p align="center">
				<font color="#000000">&nbsp; <a href="javascript:close()">[关&nbsp;&nbsp;闭]</a>
				</font>
		</td>
	</tr>
</table>

<br>
<br>
<table width="98%" class="querytable" align="center">
	<tr>
		<td height="30" class="foot">
			<p align="center">
				<font color="bule">南京联创科技股份有限公司 版权所有 Copyright By Linkage
					Technology Co.,Ltd. All rights reserved.2010-2011&nbsp;&nbsp;</font>
		</td>
	</tr>
</table>
