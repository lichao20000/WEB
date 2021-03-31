<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<%--
	/**
	 * 规范版本查询页面
	 *
	 * @author 姓名(工号) Tel:电话
	 * @version 1.0
	 * @since ${date} ${time}
	 *
	 * <br>版权：南京联创科技 网管科技部
	 *
	 */
 --%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>宁夏FTTH用户绑定率统计</title>

		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
	</head>
	<body>
		<table class="listtable">
			<caption>
				统计结果
			</caption>
			<thead>
				<tr>
					<th> 属地 </th>
					<th> 绑定用户数 </th>
					<th> 工单用户数 </th>
					<th> 绑定率 </th>
				</tr>
			</thead>

			<tbody>
				<s:if test="data.size()>0">
					<s:iterator value="data" var="map1">
						<tr>
							<td align="center">
								<a href="javascript:countBycityId('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="isFiber" />');">
									<s:property value="city_name" /> </a>
							</td>
							<td align="center">
								<a href="javascript:openHgw('1','<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="isFiber" />');">
									<s:property value="bindNum" /> </a>
							</td>
							<td align="center">
								<a href="javascript:openHgw('0','<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="isFiber" />');">
									<s:property value="allBingNum" /> </a>
							</td>
							<td align="center">
								<s:property value="percent" />
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tfoot>
						<tr>
							<td align="left">
								没有相关绑定信息
							</td>
						</tr>
					</tfoot>
				</s:else>
			</tbody>
			<tfoot>
				<tr>
					<td colspan='5'>
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
							style='cursor: hand'
							onclick="ToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>',
							                 '<s:property value="endtime1"/>','<s:property value="isFiber" />')">
					</td>
				</tr>
			</tfoot>
			<tr STYLE="display: none">
				<td colspan="4">
					<iframe id="childFrm" src=""></iframe>
				</td>
			</tr>
		</table>
	</body>
</html>
