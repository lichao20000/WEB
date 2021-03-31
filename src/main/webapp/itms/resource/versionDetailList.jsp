<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�汾ͳ�Ʋ�ѯ</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	function ToExcel(vendor, devicemodel, versionSpecification, specName,
			deviceType, accessWay, voiceAgreement, zeroconf, mbbndwidth,
			ipvsix, temval, softwareversion) {
		var page = "<s:url value='/itms/resource/VersionQuery!queryDetailExcel.action' />?"
				+ "vendor="
				+ vendor
				+ "&devicemodel="
				+ devicemodel
				+ "&versionSpecification="
				+ versionSpecification
				+ "&specName="
				+ specName
				+ "&deviceType="
				+ deviceType
				+ "&accessWay="
				+ accessWay
				+ "&voiceAgreement="
				+ voiceAgreement
				+ "&zeroconf="
				+ zeroconf
				+ "&mbbndwidth="
				+ mbbndwidth
				+ "&ipvsix="
				+ ipvsix
				+ "&temval="
				+ temval
				+ "&softwareversion=" + softwareversion;
		document.all("childFrm").src = page;

	}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>�汾ͳ�Ʋ�ѯ</caption>
		<thead>
			<tr>
				<th>�豸���к�</th>
				<th>��LOID</th>
				<th>ע��ʱ��</th>
				<th>����</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="deployDevList!=null">
				<s:if test="deployDevList.size()>0">
					<s:iterator value="deployDevList">
						<tr>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="username" /></td>
							<td><s:property value="complete_time" /></td>
							<td><s:property value="city_name" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=4>�����������ܲ��𱨱���Ϣ</td>
					</tr>
				</s:else>

			</s:if>
			<s:else>
				<tr>
					<td colspan=4>ϵͳû�д��û�!</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="4" align="right"><lk:pages
						url="/itms/resource/VersionQuery!queryVersionDev.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
			<tr>
				<td colspan="4" align="right"><IMG
					SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand'
					onclick="ToExcel('<s:property value="vendor"/>',
					'<s:property value="devicemodel"/>',
					'<s:property value="versionSpecification"/>',
					'<s:property value="specName"/>',
					'<s:property value="deviceType"/>',
					'<s:property value="accessWay"/>',
					'<s:property value="voiceAgreement"/>',
					'<s:property value="zeroconf"/>',
					'<s:property value="mbbndwidth"/>',
					'<s:property value="ipvsix"/>',
					'<s:property value="temval"/>',
					'<s:property value="softwareversion"/>')"></td>
			</tr>
		</tfoot>
		<tr STYLE="display: none">
			<td colspan="4"><iframe id="childFrm" src=""></iframe></td>
		</tr>
	</table>
</body>
</html>