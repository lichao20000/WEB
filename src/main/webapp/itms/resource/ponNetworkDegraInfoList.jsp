<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>PON综合网管劣化光路报表</title>
<%
	/**
	 * e8-c业务查询
	 * 
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2010-09-08
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>综合网管劣化光路信息 </caption>
	<thead>
		<tr>
			<th>区域</th>
			<th>E8C终端总数</th>
			<th>E8C终端光路劣化数</th>
			<th>劣化光路占比</th>
			<th>光功率小于30DB记录数</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="ponlist!=null">
			<s:if test="ponlist.size()>0">
				<s:iterator value="ponlist">
						<tr>
							<td>
								<s:property value="area_name" />
							</td>
							<td>
								<s:property value="e8cValue" />
							</td>
							<td>
							<a href="javascript:openPon('<s:property value="city_id"/>')">
								<s:property value="ponValue" />
							</a>
							</td>
							<td>
								<s:property value="pert" />
							</td>
							<td>
							<a href="javascript:openLessPon('<s:property value="city_id"/>')">
								<s:property value="lessPonValue" />
							</a>
							</td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=5>系统中没有查询出需要的信息!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=5>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="5" align="right"><IMG SRC="/itms/images/excel.gif"
				BORDER="0" ALT="导出列表" style="cursor: hand"  onclick="javaScript:ToExcel();">
			</td>
		</tr>
		<tr>
			<td colspan="5" align="left">
				<b>
					备注：劣化光路数：出现次数大于5次（包含）;&nbsp;&nbsp;&nbsp;光功率小于30DB记录数:出现次数大于5次（包含），并且光功率小于30DB;
				</b>
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>