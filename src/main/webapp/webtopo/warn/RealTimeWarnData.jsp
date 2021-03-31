<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>实时告警牌</title>
<%--
	/**
		 * 业务平台-实时告警牌
		 *
		 * @author 贲友朋(5260) email:benyp@lianchuang.com
		 * @version 1.0
		 * @since 2008-03-07
		 * @category BUSSTopo
		 */
--%>
<LINK REL="stylesheet" HREF="../css/coolmenu.css" TYPE="text/css">
<LINK REL="stylesheet" HREF="../css/listview.css" TYPE="text/css">
<style type="text/css">
/*用于导出excel*/
td.column {
	background-color: #F4F4FF;
	color: #000000;
}

td.head {
	background-color: buttonface;
	border-left: solid #ffffff 1.5px;
	border-top: solid #ffffff 1.5px;
	border-right: solid #808080 1.8px;
	border-bottom: solid #808080 1.8px;
	color: #000000;
}

	/*用于表格排序的图片*/
	table.tablesorter thead tr .header {
		background-image: url("<s:url value="/images/bg.gif "/>");
		background-color: #bdd4ff;
		background-repeat: no-repeat;
		background-position: right;
		cursor: pointer;
	}

	table.tablesorter thead tr .headerSortUp {
		background-image: url("<s:url value="/images/desc.gif "/>");
	}

	table.tablesorter thead tr .headerSortDown {
		background-image: url("<s:url value="/images/asc.gif "/>");
	}
th{
	white-space: nowrap;
}
.content{
	overflow: hidden;
	text-overflow: ellipsis;
	height: 14px;
	cursor: hand;
}
/*告警内容样式*/
.e_c{
	width:23%;
}
</style>
</head>
<body>

	<input type="hidden" name="gather" value="<s:property value="gather_val"/>">
	<input type="hidden" name="columnID" value="<s:property value="columnID"/>">
    <table align="center" name="warn_data" id="data" cellpadding=0 border=0
    cellspacing=1 bgcolor=#333333 class="tablesorter" width="98%">
		<thead id="thead">
           <s:property value="table_head" escapeHtml="false"/>
        </thead>
		<tbody id="tbody">
			<s:property value="table_body" escapeHtml="false"/>
		</tbody>
    </table>

</form>
</body>
</html>
