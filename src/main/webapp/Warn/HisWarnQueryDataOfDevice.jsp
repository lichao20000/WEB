<%@ page contentType="text/html; charset=gbk" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>历史告警查询结果显示</title>
<%--
            /**
             * 查询每台设备的告警条数 北京企业网关测试
             *
             * @author suixz(5253)
             * @version 1.0
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
              "<s:url value="/Warn/NetWarnQuery!queryWarnInfoByDev.action"/>",
              "#toolbar",
              curPage,
              num,
              maxPage,
              paramList);
       });
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

}


</style>
<script type="text/javascript">
	function detailWarnInfo(severity_id, sourcename_name, starttime, endtime, gatherid, maxnum) {
		//alert(gatherid);
       	//alert("<s:url value="/Warn/NetWarnQuery!detailWarnInfoByDev.action"/>?severity_id="+severity_id +"&sourcename_name="+sourcename_name+"&starttime="+starttime+"&endtime="+endtime+"&gatherid="+gatherid+"&maxPage_splitPage="+maxPage_splitPage);
		var city_id = document.frm.city_id.value;
//		alert(city_id);
       	window.open("<s:url value="/Warn/NetWarnQuery!detailWarnInfoByDev.action"/>?severity_id="+severity_id +"&city_id="+city_id+"&sourcename_name="+sourcename_name+"&starttime="+starttime+"&endtime="+endtime+"&gatherid="+gatherid+"&maxnum="+maxnum,"","status=yes,toolbar=no,menubar=no,location=no");
    }
</script>

</head>

<body>
<form name="frm" method="post" action="">
<INPUT NAME="city_id" TYPE="hidden"  value="<s:property value='city_id'/>">
<script type="text/javascript" src="<s:property value="/Js/jQuerySplitPage-linkage.js"/>"></script>
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
</table>
<br>
<s:if test="WarnList!=null && WarnList.size()>0">
	<table id="outTable" width="98%" align="center" cellpadding="3"
		cellspacing="1" bgcolor="#666666">
		<thead>
			<tr>
				<th rowspan="2" width="30%">设备名称</th>
				<th nowrap="nowrap" rowspan="2">告警总条数</th>
				<th colspan="3" nowrap="nowrap">
					告警信息
				</th>
			</tr>
			<tr>
				<th nowrap="nowrap">告警等级</th>
				<th nowrap="nowrap">告警条数</th>
				<th nowrap="nowrap">已处理条数</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator var="warnlist" value="WarnList">
				<s:if test="#warnlist.num!=0">
				<tr>
					<td rowspan="6"><s:property value="#warnlist.sourcename" /></td>
					<td rowspan="6"><s:property value="#warnlist.num" /></td>
					<td>严重告警</td>
					<s:if test="#warnlist.level5==0">
						<td><s:property value="#warnlist.level5" /></td>
					</s:if>
					<s:else>
						<td><a href="#" onclick="detailWarnInfo('5','<s:property value="#warnlist.sourcename"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="gatherid"/>','<s:property value="#warnlist.level5" />')"><s:property value="#warnlist.level5" /></a></td>
					</s:else>
					<td><s:property value="#warnlist.level5deal" /></td>
				</tr>
				<tr>
					<td>主要告警</td>
					<s:if test="#warnlist.level4==0">
						<td><s:property value="#warnlist.level4" /></td>
					</s:if>
					<s:else>
						<td><a href="#" onclick="detailWarnInfo('4','<s:property value="#warnlist.sourcename"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="gatherid"/>','<s:property value="#warnlist.level4" />')"><s:property value="#warnlist.level4" /></a></td>
					</s:else>
					<td><s:property value="#warnlist.level4deal" /></td>
				</tr>
				<tr>
					<td>次要告警</td>
					<s:if test="#warnlist.level3==0">
						<td><s:property value="#warnlist.level3" /></td>
					</s:if>
					<s:else>
						<td><a href="#" onclick="detailWarnInfo('3','<s:property value="#warnlist.sourcename"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="gatherid"/>','<s:property value="#warnlist.level3" />')"><s:property value="#warnlist.level3" /></a></td>
					</s:else>
					<td><s:property value="#warnlist.level3deal" /></td>
				</tr>
				<tr>
					<td>警告告警</td>
					<s:if test="#warnlist.level2==0">
						<td><s:property value="#warnlist.level2" /></td>
					</s:if>
					<s:else>
						<td><a href="#" onclick="detailWarnInfo('2','<s:property value="#warnlist.sourcename"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="gatherid"/>','<s:property value="#warnlist.level2" />')"><s:property value="#warnlist.level2" /></a></td>
					</s:else>
					<td><s:property value="#warnlist.level2deal" /></td>
				</tr>
				<tr>
					<td>事件告警</td>
					<s:if test="#warnlist.level1==0">
						<td><s:property value="#warnlist.level1" /></td>
					</s:if>
					<s:else>
						<td><a href="#" onclick="detailWarnInfo('1','<s:property value="#warnlist.sourcename"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="gatherid"/>','<s:property value="#warnlist.level1" />')"><s:property value="#warnlist.level1" /></a></td>
					</s:else>
					<td><s:property value="#warnlist.level1deal" /></td>
				</tr>
				<tr>
					<td>清除告警</td>
					<s:if test="#warnlist.level0==0">
						<td><s:property value="#warnlist.level0" /></td>
					</s:if>
					<s:else>
						<td><a href="#" onclick="detailWarnInfo('0','<s:property value="#warnlist.sourcename"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="gatherid"/>','<s:property value="#warnlist.level0" />')"><s:property value="#warnlist.level0" /></a></td>
					</s:else>
					<td><s:property value="#warnlist.level0deal" /></td>
				</tr>
				</s:if>
			</s:iterator>
		</tbody>
	</table>
	<div id="toolbar" width="98%" align="right" style="margin-right: 10px; margin-top: 2px;"></div>
</s:if>
<div width="98%" align="right"
	style="margin-right: 10px; margin-top: 2px;"><a
	href="<s:url value="/Warn/NetWarnQuery.action"/>">返回查询</a>&nbsp;&nbsp;
</div>
</form>
</body>
</html>
