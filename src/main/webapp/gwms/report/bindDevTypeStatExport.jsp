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
<TITLE>���ն˰汾ͳ��</TITLE>
 <META HTTP-EQUIV="Content-Type" CONTENT="Application/msexcel; charset=gbk">
<style>
TD{
  FONT-FAMILY: "����", "Tahoma"; FONT-SIZE: 14px;
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
          <th width="10%"> ����</th>
          <th width="20%"> �ͺ�</th>
          <th width="20%"> Ӳ���汾</th>
          <th width="20%"> ����汾</th>
          <th width="10%"> �汾����</th>
          <th width="10%"> ����</th>
          <th width="10%"> ռ��</th>
       </tr>
	</thead>
	<tbody id="userBody">
	   <%=session.getAttribute("bindDevTable")%>  
 	</tbody>
</table>
</body>		
<script LANGUAGE="JavaScript">

</script>