<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��ͼ�·����ò�ѯ���</title>
<%
	/**
	 * e8-cҵ���ѯ
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

<script type="text/javascript">
	
	$(function(){
		parent.dyniframesize();
	});

</script>

</head>

<body>
<table class="listtable">
	<caption>��ѯ���</caption>
	<thead>
     <tr>
			<th>����</th>
			<th>�豸���к�</th>
			<th>����</th>
			<th>�ͺ�</th>
            <th>�·���ʼʱ��</th>
			<th>�·�����ʱ��</th>
			<th>�·�״̬</th>
			<th>�·���ͼģ������</th>
			<th>�Ƿ�ʧЧ</th>
			<th>��������</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="list!=null">
			<s:if test="list.size()>0">
				<s:iterator value="list">
					<tr>
					<td><s:property value="city_name" /></td>
						<td><s:property value="device_serialnumber" /></td>
						<td><s:property value="vendor_name" /></td>
						<td><s:property value="device_model" /></td>
					    <td><s:property value="starttime" /></td>
						<td><s:property value="endtime" /></td>
						<td><s:property value="result_id" /></td>
						<td><s:property value="map_name" /></td>
						<td><s:property value="enable" /></td>
						<td><s:property value="task_name" /></td>
				<!-- 	<td><a
							href="javascript:delDevice('<s:property value="devicetype_id" />')">ɾ��</a>
						
						</td>
						 -->
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=10>û�в�ѯ�������Ϣ��</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=10>û�в�ѯ�������Ϣ��</td>
			</tr>
		</s:else>
	</tbody>

<tfoot>
		<tr>
			<td colspan="10" align="right"><lk:pages
				url="/itms/config/digitDeviceACT!query.action" styleClass=""
				showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot>

</table>
</body>
<script>

</script>
</html>