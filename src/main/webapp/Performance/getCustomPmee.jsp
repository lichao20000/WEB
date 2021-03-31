<%
/**
 ***********************************************************************
 *
 *					根据厂商ID获取该用户定制的性能实例
 *
 ***********************************************************************
 */
%>
<%@ page contentType="text/html;charset=GBK"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<script src="../Js/jquery.js"></script>
<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.Cursor" %>
<%@page import="com.linkage.litms.common.database.DataSetBean" %>
<%@page import="java.util.Map"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%
	String vendor_id=request.getParameter("vendor_id");
	long acc_oid=curUser.getUser().getId();
	String sql="select * from gw_pmee_custom where oui in('"+vendor_id+"') and acc_oid="+acc_oid;
	// teledb
	if (DBUtil.GetDB() == 3) {
		sql="select expressionid, custom_desc from gw_pmee_custom where oui in('"+vendor_id+"') and acc_oid="+acc_oid;
	}
	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
	psql.getSQL();
	Cursor cursor=DataSetBean.getCursor(sql);
	Map field=cursor.getNext();
	String data="<table><tr>";
	int n=1;
	while(field!=null){
		data+="<td><input type='checkbox' checked name='chk' value='"+field.get("expressionid")+"-/-"+field.get("custom_desc")+"'>"+field.get("custom_desc")+"</td>";
		if(n%3==0) data+="</tr><tr>";
		n++;
		field=cursor.getNext();
	}
	data+="</tr></table>";
	out.println(data);
%>