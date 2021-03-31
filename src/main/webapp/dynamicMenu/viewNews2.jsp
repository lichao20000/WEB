<%--
Author		: liuli
Date		: 2006-10-13
Desc		: 手工录入公告信息
--%>
<%
request.setCharacterEncoding("GBK");
String id = request.getParameter("id");
String sql = "select id,title,content from tab_broad_info where id="+ id +" and titletype=2" ;	
Map fields = DataSetBean.getRecord(sql);
%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>

<%@page import="com.linkage.litms.common.database.DataSetBean"%>
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
<%@ include file="../toolbar.jsp"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="30" bgcolor="#6483D5" background="../images/back.jpg"><font color="#ffffff"><span class="text">&nbsp;&nbsp;&nbsp;&nbsp;<%=user.getUserInfo().get("per_name")%>您好！ 您的当前位置：首页 &gt;&gt;</span></td>
  </tr>
  <tr>
    <td height="5" valign="top"></td> 
  </tr>
  <tr>
    <td valign="top" width="100%">		
    <table width="97%"  border="0" align="center" cellpadding="0" cellspacing="0">
		  <tr>
			<td background="../images/k_1.gif" width="5" height="5"></td>
			<td background="../images/k_2.gif"></td>
			<td background="../images/k_3.gif" width="5" height="5"></td>
		  </tr>
		  <tr>
		<td background="../images/k_7.gif"></td>               
         <TD vAlign=top align="center">
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
						<TR>			
							<TD vAlign=top>
							<TABLE cellSpacing=0 cellPadding=0 width=595 align=center border=0>
								<TBODY>
									<TR>
										<TD height="30">　</TD>
									</TR>
									<TR>
										<TD height="40">
										<p align="center"><b>
										<font color="#FF5400" size="3">
										<span class="header14"><%=fields.get("title")%> </span></font></b></TD>
									</TR>									
									<TR>
										<TD vAlign=top align="center">
										<table border="0" width="90%" cellspacing="0" cellpadding="0">
											<tr>
												<td>
												<p style="line-height: 200%">
												<%=fields.get("content")%>
												</td>
											</tr>
										</table>
										<table border="0" width="100%" cellspacing="0" cellpadding="0" height="50">
											<tr>
												<td height="80">
												<p align="center">
												<font color="#000000">&nbsp;
												<a href="javascript:close()">[关&nbsp;&nbsp;闭]</a></font>
												</td>
											</tr>
										</table></TD>
									</TR>
								</TBODY>
							</TABLE></TD>
						</TR>
					</TBODY>
				</TABLE>								
                </TD>
                  <td width="143" valign="top">&nbsp;</td>
                </tr>
              </table>
	          <br>
	          <br>
                  </td>
                </tr>
              </table>

		  <table border="0" width="100%" cellspacing="0" cellpadding="0">
	<tr>
		<td height="30" bgcolor="#6483D5">
		<p align="center"><font color="#ffffff">南京联创科技股份有限公司 版权所有 Copyright By  Linkage Technology Co.,Ltd. All rights reserved.2005-2007&nbsp;&nbsp;</font></td>
	</tr>
	</table>
<%@ include file="../foot.jsp"%>
