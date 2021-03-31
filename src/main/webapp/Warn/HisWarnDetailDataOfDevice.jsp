<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>历史告警查询结果显示</title>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/edittable.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript">
       $(function(){
               var curPage = "<s:property value="curPage_splitPage"/>";
               var num = "<s:property value="num_splitPage"/>";
               var maxPage = "<s:property value="maxPage_splitPage"/>";
               var paramList = "<s:property value="paramList_splitPage"/>";

            //初始化翻页按钮
            $.initPage(
              "<s:url value="/Warn/NetWarnQuery!detailWarnInfoByDev.action"/>",
              "#toolbar",
              curPage,
              num,
              maxPage,
              paramList);
       });
       function showdesc(sno,subsno,year,time,week,gather_id){
       		var url="../webtopo/warnDetailInfo.action?serialNo="+sno+"&gatherId="+gather_id+"&createTime="+time+"&buttonflg=false";
				var otherpra="channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=no,status=0,toolbar=0";
				var l		= (document.body.clientWidth-750)/2;
				var t		= (document.body.clientHeight-600)/2;

				window.open(url,"","left=" + l + ",top=" + t + ",width=700,height=550," + otherpra);

           // var tablename = "event_raw_"+year+"_"+week;
           // page = "showWQWarn.jsp?serialno="+ sno +"&subserialno="+subsno+"&tablename="+tablename+"&start="+"<s:property value="starttime"/>"+"&end="+"<s:property value="endtime"/>"+"&ip="+ip;
           // window.open(page,"","left=20,top=20,width=650,height=300,resizable=yes,scrollbars=yes");
    }
    </script>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css/tablecss.css"/>" rel="stylesheet"
	type="text/css">

<style type="text/css">
tr {
	background-color: #ffffff;
}

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

a:link,
a:visited {
color: #999999;
text-decoration: none;
font:bold

}
a:active,
a:hover {
color:#00FF00;
text-decoration:none;
font:bold

</style>

</head>

<body>
<form name="frm" method="post" action="">
<script type="text/javascript" src="<s:property value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<table width="98%" align="center" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td height=20></td>
	</tr>
	<TR>
		<TD>
			<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<TR>
					<TD width="164" align="center" class="title_bigwhite">
						告警管理
					</TD>
					<td><img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12"> 历史告警统计</td>
				</TR>
			</TABLE>
		</TD>
	</TR>
</table>
<s:if test="WarnListDetail!=null && WarnListDetail.size()>0">
	<table id="outTable" width="98%" align="center" cellpadding="3"
		cellspacing="1" bgcolor="#666666">
		<thead>
			<tr>
				<th nowrap="nowrap">ID</th>
				<th nowrap="nowrap">属地</th>
				<th nowrap="nowrap">告警源</th>
				<th nowrap="nowrap">设备IP地址</th>
				<th nowrap="nowrap">创建时间</th>
				<th nowrap="nowrap">等级</th>
				<th nowrap="nowrap">设备名称</th>
				<th nowrap="nowrap">告警内容</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator var="warnlist" value="WarnListDetail">
				<tr>
					<td nowrap="nowrap"><s:property value="#warnlist.serialno" /></td>
					<td nowrap="nowrap"><s:property value="#warnlist.city_name" /></td>
					<td nowrap="nowrap"><s:property value="#warnlist.creatorname" /></td>
					<td nowrap="nowrap"><s:property value="#warnlist.sourceip" /></td>
					<td nowrap="nowrap"><s:property value="#warnlist.createtime" /></td>
					<td nowrap="nowrap"><s:property value="#warnlist.severity" /></td>
					<td><s:property value="#warnlist.sourcename" /></td>
					<td width="30%"><s:property value="#warnlist.displaystring" /></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
	<div id="toolbar" width="98%" align="right" style="margin-right: 10px; margin-top: 2px;"></div>
</s:if>
<s:else>WarnListDetail</s:else>
</form>
</body>
</html>
