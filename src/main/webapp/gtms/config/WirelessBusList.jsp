<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<body>
<table border=0 cellspacing=1 cellpadding=1 width="100%"
			align="center" bgcolor="#999999"> 
	<thead>
		<TR>
			<TH colspan="6">
				<span style="float: right">
				<s:if test="awifi_type==3">
					<a href="#"
						onclick="doExecute(0)"><img
							src="<s:url value="/images/refresh.png" />" border="0" alt="刷新"></a>
				</s:if>
				<s:else>
					<a href="#"
						onclick="doExecute(<s:property value="flag"/> ,0)"><img
							src="<s:url value="/images/refresh.png" />" border="0" alt="刷新"></a>
				</s:else>
				&nbsp;&nbsp;</span> 操作结果
			</TH>
		</TR>
		<tr class="green_title2">
			<td width="15%">
				业务名称
			</td>
			<td width="15%">
				策略状态
			</td>
			<td width="15%">
				策略结果
			</td>
			<td width="15%">
				结果描述
			</td>
			<td width="20%">
				开始时间
			</td>
			<td width="20%">
				结束时间
			</td>
		</tr>
	</thead>
	<tbody style="background-color: #FFFFFF;height: 25px;">
		<s:if test="queryList.size()>0">
			<s:iterator value="queryList">
				<tr style="vertical-align:middle; text-align:center;">
					<td align="center">
						<s:property value="service_id"  />
					</td>
					<td align="center">
						<s:property value="status" /> 
					</td>
					<td align="center">
						<s:property value="result_id" /> 
					</td>
					<td align="center">
						<s:property value="result_desc" /> 
					</td>
					<td align="center">
						<s:property value="start_time" />
					</td>
					<td align="center">
						<s:property value="end_time" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6 >
					<font color="red">没有满足条件的数据！</font>
				</td>
			</tr>
		</s:else>
	</tbody>
</table>
</body>
