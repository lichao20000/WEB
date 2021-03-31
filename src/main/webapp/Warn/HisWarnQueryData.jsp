<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>历史告警查询结果显示</title>
<%--
            /**
             * 历史告警查询结果页面
             *
             * @author 贲友朋(5260) email:benyp@lianchuang.com
             * @version 1.0
             * @since 2008-1-3
             * @category
             */
        --%>
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
              "<s:url value="/Warn/NetWarnQuery!Query.action"/>",
              "#toolbar",
              curPage,
              num,
              maxPage,
              paramList);
       });
       function showdesc(sno,subsno,year,time,week,gather_id){
       		var url="../webtopo/warnDetailInfo.action?serialNo="+sno+"&subSerialNo="+subsno+"&gatherId="+gather_id+"&createTime="+time+"&buttonflg=false";
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
</style>

</head>

<body>
<form name="frm" method="post" action="NetWarnQuery!Query.action">
<script type="text/javascript" src="<s:property value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<table width="98%" align="center" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td height=20>&nbsp;</td>
	</tr>
	<TR  style="display:none">
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
<table width="98%" align="center" cellpadding="3" cellspacing="1"
	bgcolor="#999999">
    <s:if test="WarnList==null || WarnList.size()==0">
        <tr>
            <s:iterator var="titlemap" value="TitleList">
                <th><s:property value="#titlemap.severity"/></th>
            </s:iterator>
        </tr>
        <tr>
            <td colspan="<s:property value="num"/>">查询无数据,请<a href="<s:url value="/Warn/NetWarnQuery.action"/>">返  回</a>重新查询</td>
        </tr>
    </s:if>
    <s:else>
        <tr>
            <s:iterator var="warnnumlist" value="WarnNumList">
                <th><s:property value="#warnnumlist.title"/></th>
            </s:iterator>
        </tr>
        <tr>
            <s:iterator var="warnlist" value="WarnNumList">
                <td><s:property value="#warnlist.num"/></td>
            </s:iterator>
        </tr>
    </s:else>
</table>
<br>
<s:if test="WarnList!=null && WarnList.size()>0">
	<s:url var="all_data" value="/Warn/NetWarnQuery!ExportAll.action">
		<s:param name="gatherid" value="gatherid" />
		<s:param name="grade" value="grade" />
		<s:param name="starttime" value="starttime" />
		<s:param name="endtime" value="endtime" />
		<s:param name="ip" value="ip" />
		<s:param name="dev_name" value="dev_name" />
		<s:param name="keyword" value="keyword" />
	</s:url>
	<div width="98%" align="right"
		style="margin-right: 10px; margin-top: 2px;"><a
		href="javascript://" onclick="initialize('outTable',0,0)">Excel</a>&nbsp;&nbsp;
	<s:a href="%{all_data}">Excel&nbsp;&nbsp;</s:a></div>
	<table id="outTable" width="98%" align="center" cellpadding="3"
		cellspacing="1" bgcolor="#999999">
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
			<s:iterator var="warnlist" value="WarnList">
				<tr>
					<td nowrap="nowrap"><a href="javascript://"
						onclick="showdesc('<s:property value="#warnlist.serialno"/>',
                                                                      '<s:property value="#warnlist.subserialno"/>',
                                                                      '<s:property value="#warnlist.year"/>',
                                                                      '<s:property value="#warnlist.ctime"/>',
                                                                      '<s:property value="#warnlist.week"/>',
                                                                      '<s:property value="#warnlist.gather_id"/>'
                                                                      )"
						title='查看该告警的详细信息'><s:property value="#warnlist.serialno" /></a></td>
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
<div width="98%" align="right"
	style="margin-right: 10px; margin-top: 2px;"><a
	href="<s:url value="/Warn/NetWarnQuery.action"/>">返回</a>&nbsp;&nbsp;
</div>
</form>
</body>
</html>
