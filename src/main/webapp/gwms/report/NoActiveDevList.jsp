<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<table class="listtable">
	<caption>
		统计结果
	</caption>
	<thead>
		<tr>
			<th width="50%">
				属地
			</th>
			<th width="50%">
				不活跃设备数
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td>
						<a
							href="javascript:countBycityId('<s:property value="city_id"/>','<s:property value="activetime"/>');">
							<s:property value="city_name" /> </a>
					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','<s:property value="activetime1"/>');">
							<s:property value="total" /> </a>
					</td>					
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="2">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="ToExcel('<s:property value="cityId"/>','<s:property value="activetime"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="2">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


