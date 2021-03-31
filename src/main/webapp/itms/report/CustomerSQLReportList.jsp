<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	function ToExcel(){
		parent.ToExcel();
		}
</script>
<table class="listtable">
	<caption>查询结果</caption>
	<s:if test="queryData.size()>0">
		<thead>

			<tr>
				<s:iterator value="queryData[0]" var="title">
					<th><s:property value="title" /></th>
				</s:iterator>
			</tr>
		</thead>
	<% int count=0; %>
		<tbody>

			<s:iterator value="queryData" var="valueList" status="st">
				<s:if test="#st.first">
				</s:if>
				<s:else>
					<tr>
						<s:iterator value="#valueList" var="value">
                            <%count++; %>
							<td align="center"><s:property value='value' /></td>
						</s:iterator>

					</tr>
				</s:else>
			</s:iterator>

		</tbody>
		<tfoot>
			<tr>
				<td colspan="<%=count %>" align="right">&nbsp;&nbsp;&nbsp;&nbsp; <lk:pages
						url="/itms/report/customerSQLReport!queryList.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
			</tr>

			<tr>
				<td colspan= "<%=count %>" align="right"><IMG SRC="/itms/images/excel.gif"
					BORDER='0' ALT='导出列表' style='cursor: hand' onclick="ToExcel()">
				</td>

			</tr>
		</tfoot>
	</s:if>
	<s:else>
		<tfoot>
			<tr>
				<td align="left">没有统计到相关数据，请检查定制的SQL是否异常</td>
			</tr>
		</tfoot>
	</s:else>
	<tr STYLE="display: none">
		<td colspan="9"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>


