<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title> </title>
<%
	/**
		 *  
		 * 
		 * @author qixueqi(4174)
		 * @version 1.0
		 * @since 2010-5-21
		 * @category
		 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});

	function openw(cityId,bindStartTime,bindEndTime,operType){
		var page="<s:url value='/gwms/resource/bindLogView!startQuery.action'/>?"
			+ "cityId=" + cityId 
			+ "&bindStartTime=" + bindStartTime
			+ "&bindEndTime=" + bindEndTime
			+ "&operType=" + operType;
		window.open(page,"","left=50,top=50,height=400,width=650,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
</script>

</head>

<body>
	<table class="listtable">
		<caption>
			统计结果
		</caption>
		<thead>
			<tr>
				<th>属地</th>
				<th>安装数</th>
				<th>解绑数</th>
				<th>修障数</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="rsList">
				<tr bgcolor="#FFFFFF">
					<td class=column1>
						<a href="<s:url value='/gwms/resource/bindLogCount!startQuery.action'/>?cityId=<s:property value="city_id"/>&bindStartTime=<s:property value="bindStartTime"/>&bindEndTime=<s:property value="bindEndTime"/>"><s:property value="city_name" /></a>
					</td>
					<td class=column1>
						<a href="javascript:openw('<s:property value="city_id"/>','<s:property value="bindStartTime"/>','<s:property value="bindEndTime"/>','1');"><s:property value="bind"/></a>
					</td>
					<td class=column1>
						<a href="javascript:openw('<s:property value="city_id"/>','<s:property value="bindStartTime"/>','<s:property value="bindEndTime"/>','2');"><s:property value="bindless"/></a>
					</td>
					<td class=column1>
						<a href="javascript:openw('<s:property value="city_id"/>','<s:property value="bindStartTime"/>','<s:property value="bindEndTime"/>','3');"><s:property value="modify"/></a>
					</td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</body>
</html>