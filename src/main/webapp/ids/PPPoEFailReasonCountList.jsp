<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>PPPOEʧ��ԭ��ͳ�Ʊ���</title>

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
	<caption>PPPOEʧ��ԭ��ͳ�� </caption>
	<thead>
		<tr>
			<th>����</th>
			<th>ERROR_ISP_TIME_OUT</th>
			<th>ERROR_AUTHENTICATION_FAILURE</th>
			<th>ERROR_ISP_DISCONNECT</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="pppoeCountList!=null">
			<s:if test="pppoeCountList.size()>0">
				<s:iterator value="pppoeCountList">
						<tr>
							<td>
								<s:property value="city_name" />
							</td>
							<td>
								<a href="javascript:openCity('<s:property value="city_id"/>','ERROR_ISP_TIME_OUT');">
									<s:property value="reason1" />
								</a>
							</td>
							<td>
								<a href="javascript:openCity('<s:property value="city_id"/>','ERROR_AUTHENTICATION_FAILURE');">
								<s:property value="reason2" />
								</a>
							</td>
							<td>
								<a href="javascript:openCity('<s:property value="city_id"/>','ERROR_ISP_DISCONNECT');">
								<s:property value="reason3" />
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
				BORDER=0  ALT="�����б�" style="cursor: hand" onclick="javaScript:ToExcel();">
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>