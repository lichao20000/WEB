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
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="../../Js/inmp/jquery.js""></script>
<script type="text/javascript" src="../../Js/inmp/jQuerySplitPage-linkage.js"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});
</script>

</head>

<body>
	<table class="listtable">
		<caption>
			ͳ�ƽ��
		</caption>
		<thead>
			<tr>
				<th>��װ�ص�</th>
				<th>������</th>
				<th>�豸���к�</th>
				<th>�û��˺�</th>
				<th>��װʱ��</th>
				<th>������ʽ</th>
				<th>��������</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="rsList">
				<tr bgcolor="#FFFFFF">
					<td class=column1><s:property value="cityName"/></td>
					<td class=column1><s:property value="dealstaff"/></td>
					<td class=column1><s:property value="deviceSn"/></td>
					<td class=column1><s:property value="username"/></td>
					<td class=column1><s:property value="binddate"/></td>
					<td class=column1><s:property value="userline"/></td>
					<td class=column1><s:property value="operType"/></td>
				</tr>
			</s:iterator>
		</tbody>
		<tr>
			<td colspan="7" align="right">
				<lk:pages url="/inmp/resource/bindLogView!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
	</table>
</body>
</html>