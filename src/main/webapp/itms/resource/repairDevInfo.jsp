<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>�ն˶�������</title>
<link href="<s:url value="/css/uploadAndParse.css"/>" rel="stylesheet"
	type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
</head>

<body>
	<div class="it_main">
		<div class="tanchu">
			<div class="yw_tit">�ն˶�������</div>
			<s:if test="devDetailMap != null ">
				<div class="content">
					<div class="itms_tit">
						<h2>�ն���Ϣ</h2>
					</div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						class="it_table">
						<tr>
							<th>�豸���к�</th>
							<th>���޳���</th>
							<th>��������</th>
							<th>����/����</th>
							<th>�豸�ͺ�</th>
							<th>Ӳ���汾</th>
							<th>����汾</th>
							<th>��������</th>
							<th>ʹ�õ���</th>
						</tr>
						<tr>
							<td><s:property value="devDetailMap.device_serialnumber" /></td>
							<td><s:property value="devDetailMap.repair_vendor" /></td>
							<td><s:property value="devDetailMap.send_city" /></td>
							<td><s:property value="devDetailMap.insurance_status" /></td>
							<td><s:property value="devDetailMap.device_model" /></td>
							<td><s:property value="devDetailMap.hardwareversion" /></td>
							<td><s:property value="devDetailMap.softwareversion" /></td>
							<td><s:property value="devDetailMap.city_id" /></td>
							<td><s:property value="devDetailMap.city_area" /></td>
						</tr>
					</table>
				</div>
				<div class="content">
					<div class="itms_tit">
						<h2>�ն�״̬</h2>
					</div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						class="it_table">
						<tr>
							<th>���޳�������</th>
							<th>��������</th>
							<th>���°�ʱ��</th>
							<th>�ն�״̬</th>
							<th>���½��ʱ��</th>
						</tr>
						<tr>
							<td><s:property value="devDetailMap.manufacture_date" /></td>
							<td><s:property value="devDetailMap.production_date" /></td>
							<td><s:property value="devDetailMap.recent_binddate" /></td>
							<td><s:property value="devDetailMap.bind_status" /></td>
							<td><s:property value="devDetailMap.recent_unbinddate" /></td>
						</tr>
						<tr>
							<th>3���ںϸ�</th>
							<th>��LOID</th>
							<th>LOID״̬</th>
							<th>����ҵ��</th>
							<th>��������</th>
						</tr>
						<tr>
							<td><s:property value="devDetailMap.qualified_status" /></td>
							<td><s:property value="devDetailMap.bind_loid" /></td>
							<td><s:property value="devDetailMap.loid_status" /></td>
							<td><s:property value="devDetailMap.add_business" /></td>
							<td><s:property value="devDetailMap.is_test" /></td>
						</tr>
					</table>
				</div>
			</s:if>
			<s:else>
				<div class="content">
					<div class="itms_tit">
						<h2>δ��ѯ����Ӧ�ն˵�����Ϣ</h2>
					</div>
				</div>
			</s:else>
		</div>
	</div>
</body>
<%@ include file="/foot.jsp"%>
</html>
