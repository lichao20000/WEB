<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>������ѯ��Ϣ</title>
<%
	/**
	 * ������ѯ��Ϣ
	 * 
	 * @author gaoyi
	 * @version 4.0.0
	 * @since 2013-08-02
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

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>������ѯ��Ϣ</caption>
	<thead>
		<tr>
			<th>LOID</th>
			<th>ҵ������</th>
			<th>��������</th>
			<th>�ص����</th>
			<%--<th>�豸���к�</th>--%>
			<th>����</th>
			<th>ִ��ʱ��</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="orderList!=null">
			<s:if test="orderList.size()>0">
				<s:iterator value="orderList">
						<tr>
							<td><s:property value="user_name" /></td>
							<td><s:property value="product_spec_id" /></td>
							<td><s:property value="type" /></td>
							<td><s:property value="result" /></td>
							<%--<td><s:property value="device_serialnumber" /></td>--%>
							<td><s:property value="city_name" /></td>
							<td><s:property value="receive_date" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=7>ϵͳû�иù�����Ϣ!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="7" align="right">
		 	<lk:pages
				url="/itms/service/orderInfo!getOrderInfo.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot>
</table>
</body>
</html>