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
			<th class=column1 height="25" align="center"><strong>�鲥����ҵ����Ϣ����</strong>
			</th>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center" bgcolor="#999999" style="overflow-y: auto">
					<tr bgcolor="#FFFFFF">
						<th>����</th>
						<th>LOID</th>
						<th>����˺�</th>
						<th>���VLAN</th>
						<th>����·�״̬</th>
                        <th>ITV�˺�</th>
                        <th>itvVlan</th>
                        <th>itv�·�״̬</th>
                        <th>vpdn�˺�</th>
                        <th>vpdnVlan</th>
                        <th>vpdn�·�״̬</th>
                        <th>��������</th>
                        <th>����Vlan</th>
                        <th>�����·�״̬</th>
                        <th>����Э������</th>
					</tr>
                    <s:iterator value="businessList">
                        <tr bgcolor="#FFFFFF">
                            <td width="7%" style = "word-break: break-all">
                                <s:property value="city_name" />
                            </td>
                            <td width="7%" style = "word-break: break-all" >
                                <s:property value="username" />
                            </td>
                            <td width="7%" style = "word-break: break-all">
                                <s:property value="netAccount" />
                            </td>
                            <td width="7%" style = "word-break: break-all">
                                <s:property value="netVlan" />
                            </td>
                            <td width="7%" style = "word-break: break-all">
                                <s:property value="netOpenStatus" />
                            </td>
                            <td width="7%" style = "word-break: break-all">
                                <s:property value="itvAccount" />
                            </td>
                            <td width="7%" style = "word-break: break-all">
                                <s:property value="itvVlan" />
                            </td>
                            <td width="6%"  style = "word-break: break-all">
                                <s:property value="itvOpenStatus" />
                            </td>
                            <td width="7%" style = "word-break: break-all">
                                <s:property value="vpdnAccount" />
                            </td>
                            <td width="7%" style = "word-break: break-all">
                                <s:property value="vpdnVlan" />
                            </td>
                            <td width="6%"  style = "word-break: break-all">
                                <s:property value="vpdnOpenStatus" />
                            </td>
                            <td width="7%" style = "word-break: break-all">
                                <s:property value="voipPhone" />
                            </td>
                            <td width="7%" style = "word-break: break-all">
                                <s:property value="voipVlan" />
                            </td>
                            <td width="6%"  style = "word-break: break-all">
                                <s:property value="voipOpenStatus" />
                            </td>
                            <td width="5%"  style = "word-break: break-all">
                                <s:property value="voipProtocol" />
                            </td>
                        </tr>
                    </s:iterator>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
