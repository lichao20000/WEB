<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.text.*,java.util.*"%>
<%@page import="com.linkage.litms.netcutover.SheetList"%>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>

<%@page import="com.linkage.litms.Global"%><html>
<head>
<%
	request.setCharacterEncoding("GBK");
	SheetList sheet = new SheetList();
	Cursor cursor = sheet.queryBssSheetList(request);
	Map fields = cursor.getNext();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	String[] arrStyle = new String[11];
	arrStyle[0] = "class=trOutyellow onmouseover='this.className=\"trOutyellow\"' onmouseout='this.className=\"trOutyellow\"'";
	arrStyle[1] = "class=trOutblue onmouseover='this.className=\"trOverblue\"' onmouseout='this.className=\"trOverblue\"'";
	arrStyle[2] = "class=trOut  onmouseover='this.className=\"trOver\"' onmouseout='this.className=\"trOut\"'";
	arrStyle[3] = "class=trOutred onmouseover='this.className=\"trOutred\"' onmouseout='this.className=\"trOutred\"'";
	arrStyle[4] = "class=trOutred onmouseover='this.className=\"trOutred\"' onmouseout='this.className=\"trOutred\"'";
	arrStyle[5] = "class=trOutblue onmouseover='this.className=\"trOverblue\"' onmouseout='this.className=\"trOverblue\"'";
	arrStyle[6] = "class=trOutred onmouseover='this.className=\"trOutred\"' onmouseout='this.className=\"trOutred\"'";
	arrStyle[7] = "class=trOutchense onmouseover='this.className=\"trOverchense\"' onmouseout='this.className=\"trOutchense\"'";
	arrStyle[8] = "class=trOutyellow onmouseover='this.className=\"trOutyellow\"' onmouseout='this.className=\"trOutyellow\"'";
	arrStyle[9] = "class=trOutshougong onmouseover='this.className=\"trOvershougong\"' onmouseout='this.className=\"trOutshougong\"'";
	arrStyle[10] = "class=trOutshougong onmouseover='this.className=\"trOvershougong\"' onmouseout='this.className=\"trOutshougong\"'";

%>
<title>工单查询结果</title>
</head>
<body>
<div id="showList">
<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
	<TR>
		<TD HEIGHT=40>&nbsp;</TD>
	</TR>
	<TR>
		<TD bgcolor=#999999>
		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<tr>
				<th width="" nowrap>工单编号</th>
				<th width="" nowrap>属地</th>
			<!--  	<th width="" nowrap>客户名称</th>-->
				<th width="" nowrap>用户账号</th>
				<th width="" nowrap>业务类型</th>
				<th width="" nowrap>操作类型</th>
				<th width="" nowrap>来单时间</th>
				<th nowrap>执行结果</th>
			</tr>
			<%
				if (fields != null) {
					String tmp;
					int iStatus = 0;
					while (fields != null) {
						out.println("<tr value=" + fields.get("id")
								+ " ondblclick=doDbClick(this) " 
								+ arrStyle[iStatus]
								+ " title='双击会显示工单详细信息'>");
						out.println("<td class=column1><nobr>" + fields.get("id")
								+ "</nobr></td>");
						out.println("<td class=column1><nobr>"
								+ CityDAO.getCityIdCityNameMap().get((String) fields
										.get("city_id")) + "</nobr></td>");
					//	out.println("<td class=column1><nobr>"
						//		+ fields.get("bnet_account") + "</nobr></td>");
						out.println("<td class=column1><nobr>"
								+ fields.get("username")
								+ "</nobr></td>");
					
						out.println("<td class=column1><nobr>"
								+ SheetList.getType((String) fields
												.get("product_spec_id")) + "</nobr></td>");
						out.println("<td class=column1><nobr>"
								+ SheetList.getOperateType((String) fields
										.get("type")) + "</nobr></td>");
						out.println("<td class=column1><nobr>"
								+ sdf.format(new Date(Long
										.parseLong((String) fields
												.get("receive_date"))))
								+ "</nobr></td>");
						tmp = (String) SheetList.getResult().get(
								fields.get("result"));
						if (tmp == null || tmp.equals("null"))
							tmp = "";
						out.println("<td class=column1><nobr>" + tmp
								+ "</nobr></td>");
						out.println("</tr>");
						fields = cursor.getNext();
					}
				} else {
					out
							.println("<tr bgcolor='#ffffff' ><td align=center colspan=10>没有工单记录</td></tr>");
				}
			%>
		</TABLE>
		</TD>
	</TR>


</TABLE>
</div>
</body>
<SCRIPT LANGUAGE="JavaScript">
<!--
	parent.document.all("childList").innerHTML = document.all("showList").innerHTML;
//-->
</SCRIPT>
</html>