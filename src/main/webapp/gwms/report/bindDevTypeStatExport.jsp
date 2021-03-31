<%@ page language="java" contentType="application/msexcel; charset=gbk" pageEncoding="gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%
//request.setCharacterEncoding("GBK");
//response.setContentType("Application/msexcel");
response.setHeader("Content-disposition","attachment; filename=bindDevStat.xls" );
System.out.println(session.getAttribute("bindDevTable"));

%>
<HTML>
<HEAD>
<TITLE>绑定终端版本统计</TITLE>
 <META HTTP-EQUIV="Content-Type" CONTENT="Application/msexcel; charset=gbk">
<style>
TD{
  FONT-FAMILY: "宋体", "Tahoma"; FONT-SIZE: 14px;
}
</style>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="../../Js/jquery.js"></script>
<script type="text/javascript" src="../../Js/jQuerySplitPage-linkage.js"></script>
</HEAD>
<body>
<table border=1 cellspacing=1 cellpadding=2 width="100%" id=userTable>
	<thead>
       <tr>
          <th width="10%"> 厂家</th>
          <th width="20%"> 型号</th>
          <th width="20%"> 硬件版本</th>
          <th width="20%"> 软件版本</th>
          <th width="10%"> 版本类型</th>
          <th width="10%"> 总量</th>
          <th width="10%"> 占比</th>
       </tr>
	</thead>
	<tbody id="userBody">
	   <%=session.getAttribute("bindDevTable")%>  
 	</tbody>
</table>
</body>		
<script LANGUAGE="JavaScript">

</script>