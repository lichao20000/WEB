<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>����ע�����ͳ�����</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>����ע�����ͳ����� </caption>
	<thead>
		<tr>
			<th>����</th>
			<th>IADģ�����</th>
			<th>����·�ɲ�ͨ</th>
			<th>���ʷ���������Ӧ</th>
			<th>�ʺš��������</th>
			<th>δ֪����</th>
		</tr>
	</thead>
	<tbody>
			<s:if test="voiceRegFailedReasonList.size()>0">
				<s:iterator value="voiceRegFailedReasonList">
						<tr>
							<td>
								<s:property value="cityName" />
							</td>
							<td>
								<a href="javascript:downFile('<s:property value="city_id"/>','1');">
									<s:property value="lad_error" />
								</a>
							</td>
							<td>
								<a href="javascript:downFile('<s:property value="city_id"/>','2');">
								<s:property value="route_error" />
								</a>
							</td>
							<td>
								<a href="javascript:downFile('<s:property value="city_id"/>','3');">
								<s:property value="server_error" />
								</a>
							</td>
							<td>
								<a href="javascript:downFile('<s:property value="city_id"/>','4');">
								<s:property value="account_error" />
								</a>
							</td>
							<td>
								<a href="javascript:downFile('<s:property value="city_id"/>','5');">
								<s:property value="unknown_error" />
								</a>
							</td>
						</tr>
				</s:iterator>
		</s:if>
		<s:else>
				<tr>
					<td colspan=6>ϵͳ��û�в�ѯ����Ҫ����Ϣ!</td>
				</tr>
			</s:else>
	</tbody>
	
</table>
</body>
</html>