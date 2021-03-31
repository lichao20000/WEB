<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ���ѯ</title>
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
	function changeOpenDate(userId,loid) {
		$('#editUserOpenDateDiv', window.parent.document).show();
		window.parent.document.getElementsByName("userId")[0].value = userId;
		window.parent.document.getElementsByName("loidNow")[0].value = loid;
	}

</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>BSS�û���Ϣ</caption>
		<thead>
			<tr>
				<th>LOID</th>
				<th>����</th>
				<th>�豸���к�</th>
				<th>BSS��ͨʱ��</th>
				<th>BSS����ʱ��</th>
				<th>��ͨ״̬</th>
				<th>����</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="bssSheetInfoList!=null">
				<s:if test="bssSheetInfoList.size()>0">
					<s:iterator value="bssSheetInfoList">
							<tr>
								<td><s:property value="username" /></td>
								<td><s:property value="cityName" /></td>
								<td><s:property value="device_serialnumber" /></td>
								<td><s:property value="openDate" /></td>
								<td><s:property value="dealDate" /></td>
								<s:if test='open_status == "1"'>
								<td>�ɹ�</td>
								</s:if>
								<s:elseif test='open_status == "0"'>
									<td><font color="#008000">δ��</font></td>
								</s:elseif>
								<s:else>
									<td><font color="red">ʧ��</font></td>
								</s:else>
								<td>
									<s:if test='isEditEnable == "true"'>
										<a href="javascript:changeOpenDate('<s:property value="user_id" />','<s:property value="username" />')">�޸Ŀ���ʱ��</a>
									</s:if>
									</td>
							</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=7>ϵͳû�и��û�����Ϣ!</td>
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
		</tfoot>
	</table>
</body>

</html>