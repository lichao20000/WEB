<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">


</script>

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center" style="margin-top: 50px" >
		<tr>
			<th class=column1 height="25" align="center"><strong>�鲥�����豸��Ϣ����</strong>
			</th>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center" bgcolor="#999999"  style="overflow-y: auto" >
					<tr bgcolor="#FFFFFF">
						<th>����</th>
						<th>LOID</th>
						<th>���豸���к�</th>
						<th>�豸ע��ϵͳʱ��</th>
						<th>�豸�������ʱ��</th>
                        <th>����</th>
                        <th>�ͺ�</th>
                        <th>����汾</th>
                        <th>Ӳ���汾</th>
					</tr>
					<s:iterator value="devList">
						<tr bgcolor="#FFFFFF">
							<td width="11%">
								<s:property value="city_name" />
							</td>
							<td width="11%">
								<s:property value="username" />
							</td>
							<td width="11%">
								<s:property value="device_serialnumber" />
							</td>
							<td width="11%">
								<s:property value="complete_time" />
							</td>
							<td width="11%">
								<s:property value="last_time" />
							</td>
							<td width="11%">
                            	<s:property value="vendor_name" />
                            </td>
                            <td width="11%">
                                <s:property value="device_model" />
                            </td>
							<td width="11%">
								<s:property value="softwareversion" />
							</td>
							<td width="12%">
								<s:property value="hardwareversion" />
							</td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
