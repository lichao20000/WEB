<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ���ѯ</title>
<%
	/**
	 * e8-cҵ���ѯ
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2013-08-15
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

	<form id="selectForm" name="selectForm"
		action="<s:url value='/itms/resource/UserSpecTerminals!getTerminalSpecList.action'/>">
		<table class="listtable" id="listTable">
			<caption>�ն˹�����û����һ�±ȶ�ҵ����Ϣ</caption>
			<thead>
				<tr>
					<th width="25%">����</th>
					<th width="25%">�ܿ�����</th>
					<th width="25%"><s:property value="SpecTitle" /></th>
					<th width="25%">ռ��</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="list!=null">
					<s:if test="list.size()>0">
						<s:iterator value="list">
							<tr>
								<td align="center">
<%-- 								<a href="javascript:countBycityId('<s:property value="city_id"/>');"> --%>
							<s:property value="city_name" />
<!-- 							</a> -->
								</td>
								<td align="center">
 								<s:property value="totals" /> 
								</td>
								<td align="center"><a id="aa" href="javascript:doQuery('<s:property value="spec_id"/>','<s:property value="city_id"/>','<s:property value="startOpenDate"/>','<s:property value="endOpenDate"/>')"><s:property
											value="total" /></a></td>
								<td align="center">
 								<s:property value="pert"/>
								</td>
							</tr>
						</s:iterator>
					</s:if>
				</s:if>
					<s:else>
					</s:else>
			</tbody>
			<tfoot>
					<tr>
						<td colspan=4><IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0'
							ALT='�����б�' style='cursor: hand' onclick="ToCityExcel('<s:property value="spec_id"/>','<s:property value="city_id"/>','<s:property value="startOpenDate"/>','<s:property value="endOpenDate"/>')">
						</td>
					</tr>

		</tfoot>
		</table>
	</form>
</body>
<script>
</script>

</html>