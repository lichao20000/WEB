<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�ն�����״̬ͳ�����</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>�ն�����״̬ͳ����� </caption>
	<thead>
		<tr>
			<th>����</th>
			<th>�ն���</th>
		</tr>
	</thead>
	<tbody>
			<s:if test="deviceTempList.size()>0">
				<s:iterator value="deviceTempList">
						<tr>
							<td>
								<s:property value="cityName" />
							</td>
							<td>
								<a href="javascript:downFile('<s:property value="city_id"/>','1');">
									<s:property value="device_count" />
								</a>
							</td>
						</tr>
				</s:iterator>
		</s:if>
		<s:else>
				<tr>
					<td colspan=2>ϵͳ��û�в�ѯ����Ҫ����Ϣ!</td>
				</tr>
		</s:else>
	</tbody>
	
</table>
</body>
</html>