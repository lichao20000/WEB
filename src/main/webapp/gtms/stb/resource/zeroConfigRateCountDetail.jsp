<%@page contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css"/>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css"/>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>�������豸��ϸ</title>
</head>
<body>

	<table width="100%" class="listtable" align="center"
		style="margin-top: 10px;">
		<caption>�豸��ϸ</caption>
		<thead>
			<tr>
				<th class="title_1">����</th>
				<th class="title_1">����</th>
				<th class="title_1">�ͺ�</th>
				<th class="title_1">����汾</th>
				<th class="title_1">�豸���к�</th>
				<th class="title_1">ҵ���˺�</th>
				<th class="title_1">ip��ַ</th>
				<th class="title_1">mac��ַ</th>
				<th class="title_1">��ʱ��</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="data.size()>0">
				<s:iterator var="list" value="data">
					<tr>
						<td><s:property value="#list.city_name" /></td>
						<td><s:property value="#list.vendor_name" /></td>
						<td><s:property value="#list.device_model" /></td>
						<td><s:property value="#list.softwareversion" /></td>
						<td><s:property value="#list.device_serialnumber" /></td>
						<td><s:property value="#list.serv_account" /></td>
						<td><s:property value="#list.loopback_ip" /></td>
						<td><s:property value="#list.cpe_mac" /></td>
						<td><s:property value="#list.bind_time" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan="9">
						<div style="text-align: center">��ѯ������</div>
					</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<s:if test="data.size()>0">
				<tr>

					<td class="foot" colspan="9">
						<div style="float: right;">
							<lk:pages url="/gtms/stb/resource/zeroConfigRateCount!queryZeroConfigDetail.action" isGoTo="true"/>
						</div>
					</td>
				</tr>
			</s:if>
		</tfoot>
		<tr STYLE="display: none">
			<td colspan="12">
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>
	</table>
</body>
</html>
