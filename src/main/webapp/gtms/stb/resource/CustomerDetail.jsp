<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�û�ҵ������</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
	function deviceDetail(deviceId) {
		var page = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!queryZeroDeviceDetail.action'/>?"
				+ "deviceId=" + deviceId;
		window
				.open(
						page,
						"",
						"left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
</script>
</head>
<body>
	<TABLE width="98%" class="querytable" align="center">
		<TR>
			<TH colspan="5" class="title_1">�û���<s:property
					value="customerMap.servAccount" />����ϸ��Ϣ
			</TH>
		</TR>
		<TR height="20">
			<TD class="title_3" colspan=5>�û�������Ϣ</TD>
		</TR>
		<TR>
			<TD class="title_2">ҵ���ʺ�</TD>
			<TD colspan=2 width="25%"><s:property value="customerMap.servAccount" /></TD>
			<TD class="title_2">�����ʺ�</TD>
			<TD width="40%"><s:property value="customerMap.pppoeUser" /></TD>
		</TR>
		<TR>
			<TD class="title_2">�û�����ʺ�</TD>
			<TD colspan=2><s:property value="customerMap.cust_account" /></TD>
			<TD class="title_2">����</TD>
			<TD><s:property value="customerMap.cityName" /></TD>
		</TR>
		<TR>
			<TD class="title_2">��������</TD>
			<TD colspan=2><s:property value="customerMap.addressingType" /></TD>
			<TD class="title_2">����ʱ��</TD>
			<TD><s:property value="customerMap.openUserdate" /></TD>
		</TR>
		<TR>
			<TD class="title_2">��ͨ״̬</TD>
			<TD colspan=2><s:property value="customerMap.userStatus" /></TD>
			<s:if test="instAreaName=='nx_dx'">
				<TD class="title_2">MAC��ַ</TD>
				<TD><s:property value="customerMap.mac" /></TD>
			</s:if>
			<s:if test="instAreaName=='sd_lt'">
				<TD class="title_2">���뷽ʽ</TD>
				<TD><s:property value="customerMap.stbuptyle" /></TD>
			</s:if>
			<s:else>
				<TD class="title_2"></TD>
				<TD></TD>
			</s:else>
		</TR>
		<TR height="20">
			<TD class="title_3" colspan=5>��������Ϣ</TD>
		</TR>
		<TR>
			<TD class="title_2">�����г���</TD>
			<TD colspan=2 width=140><s:property value="customerMap.vendor_name" /></TD>
			<TD class="title_2">�������ͺ�</TD>
			<TD><s:property value="customerMap.device_model" /></TD>
		</TR>
		<TR>
			<TD class="title_2">���������к�</TD>
			<TD colspan=2 width=140><s:property value="customerMap.deviceSN" /></TD>
			<TD class="title_2">������IP</TD>
			<TD><s:property value="customerMap.loopback_ip" /></TD>
		</TR>
		<TR>
			<TD class="title_2">������MAC��ַ</TD>
			<TD colspan=2 width=140><s:property value="customerMap.cpe_mac" /></TD>
			<TD class="title_2">�����·�ʱ��</TD>
			<TD><s:property value="customerMap.end_time" /></TD>
		</TR>

		<s:if test="instAreaName=='sd_lt'">
		<TR colspan=5>
			<TABLE width="98%" class="querytable" align="center" >
				<TR height="20">
					<TD class="title_3" colspan=7>��������</TD>
				</TR>
				<TR>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">IPTV��������ʺ�</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">IPTV�˺�</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">�����Ĭ������1</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">�����Ĭ������2</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">���뷽ʽ</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">MAC��ַ</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">STBҵ����뷽ʽ</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">LOID</TD>
				</TR>
				<s:if test="zeroInfoList.size>0">
					<s:iterator var="list" value="zeroInfoList">
						<TR>
							<td style="text-align:center;"><s:property value="#list.pppoe_user" /></td>
							<td style="text-align:center;"><s:property value="#list.serv_account" /></td>
							<td style="text-align:center;"><s:property value="#list.browser_url1" /></td>
							<td style="text-align:center;"><s:property value="#list.browser_url12" /></td>
							<td style="text-align:center;"><s:property value="#list.stbuptyle" /></td>
							<td style="text-align:center;"><s:property value="#list.cpe_mac" /></td>
							<td style="text-align:center;"><s:property value="#list.addressing_type" /></td>
							<td style="text-align:center;"><s:property value="#list.username" /></td>
						</TR>
					</s:iterator>
				</s:if>
				<s:else>
					<TR>
						<s:if test="%{instAreaName=='sd_lt'}">
							<TD colspan=8>
								<div style="text-align: center">��ѯ������</div>
							</TD>
						</s:if>
						<s:else>
							<TD colspan=8>
								<div style="text-align: center">��ѯ������</div>
							</TD>
						</s:else>
					</TR>
				</s:else>
			</TABLE>
		</TR>
		</s:if>
		<s:else>
		<s:if test="instAreaName!='hb_lt'">
		<TR height="20">
			<TD class="title_3" colspan=5>������������Ϣ</TD>
		</TR>
		<TR>
			<TD class="title_2">ҵ���ʺ�</TD>
			<TD class="title_2" colspan="2">ʱ��</TD>
			<TD class="title_2" colspan="2">���������ý��</TD>
		</TR>
		<s:if test="zeroInfoList.size>0">
			<s:iterator var="list" value="zeroInfoList">
				<tr>
					<td>
						<a onclick="deviceDetail('<s:property value="#list.device_id" />')" style="cursor: hand;color: blue;">
							<s:property value="#list.serv_account" />
						</a>
					</td>
					<td colspan="2"><s:property value="#list.fail_time" /></td>
					<td colspan="2"><s:property value="#list.reason_desc" />(����ֵ[<s:property value="#list.return_value" />])</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="5">
					<div style="text-align: center">��ѯ������</div>
				</td>
			</tr>
		</s:else>
		</s:if>
		</s:else>
		<TR>
			<TD colspan="5" class=foot>
				<div align="center">
					<button onclick="javascript:window.close();">�� ��</button>
				</div>
			</TD>
		</TR>
	</TABLE>
</body>
