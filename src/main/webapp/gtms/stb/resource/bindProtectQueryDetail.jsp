<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">

<table class="listtable">
	<caption>
		结果详情
	</caption>
	<s:if test="data.size()>0">
		<thead>

			<tr>
				<th>
					业务账号
				</th>
				<th>
					机顶盒MAC
				</th>
				<th>
					添加时间
				</th>
				<th>
					操作员
				</th>
				<th>
					备注
				</th>
			</tr>
		</thead>

		<tbody>
			<s:iterator value="data" var="map1">
				<tr>
					<td align="center">
						<s:property value="username" />
					</td>
					<td align="center">
						<s:property value="mac" />
					</td>
					<td align="center">
						<s:property value="addtime" />
					</td>
					<td align="center">
						<s:property value="per_name" />
					</td>
					<td align="center">
						<s:property value="remark" />
					</td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="5" align="right"></td>
			</tr>
		</tfoot>
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
	<tr STYLE="display: none">
		<td colspan="5">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


