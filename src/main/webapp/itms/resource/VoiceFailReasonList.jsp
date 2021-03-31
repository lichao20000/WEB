<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>����ע�����ԭ��ͳ��</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

</head>
<body>
<table class="listtable" id="listTable">
	<caption>����ע�����ԭ��ͳ�� </caption>
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
		<s:if test="voicefailMap!=null">
			<s:if test="voicefailMap.size()>0">
				<s:iterator value="voicefailMap">
						<tr>
							<td>
							
								<s:property value="city_name" />
							</td>
							<td>
								<a href="javascript:openCity('<s:property value="city_id"/>','1')">
								<s:property value="oneNum" />
								</a>
							</td>
							<td>
							
								<a href="javascript:openCity('<s:property value="city_id"/>','2')">
								<s:property value="twoNum" />
								</a>
							</td>
							<td>
								<a href="javascript:openCity('<s:property value="city_id"/>','3')">
								<s:property value="threeNum" />
								</a>
							</td>
							<td>
								<a href="javascript:openCity('<s:property value="city_id"/>','4')">
								<s:property value="fourNum" />
								</a>
							</td>
							<td>
								<a href="javascript:openCity('<s:property value="city_id"/>','5')">
								<s:property value="fiveNum" />
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
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>

		<tr>
			<td colspan="6" align="right"><IMG SRC="/itms/images/excel.gif"
				BORDER='0' ALT='�����б�' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>