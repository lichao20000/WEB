<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />

<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css" />
<title>���������������ҳ��</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
	function showBlue() {
		var loid = document.getElementById("loid").value;
		var device = document.getElementById("device").value;
		$("div[@id='div_update']").html("");
		$("div[@id='div_update']").css("display", "");
		if (device.length < 6 && loid.length == 0) {
			alert("�豸���к�����6λ��");
		}
		if (("4381LO00410272" == loid && device.length== 0) || (device== "D382C48555FFFE87C"&& loid.length == 0)||(device== "D382C48555FFFE87C" &&loid == "4381LO00410272")) {
			document.all.blue1.style.display = '';
			$("div[@id='div_update']").display = '';
		} else {
			document.all.blue1.style.display = 'none';
			$("div[@id='div_update']").html("��ǰ������ѯ���豸�����ڣ�");
		}

	}
</script>
</head>
<body>
	<table width="100%" border="0" cellspacing="1" cellpadding="2"
		bgcolor="#999999">
		<tr>
			<td colspan="4" align="center" class="green_title">�豸��ѯ</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td width="15%" align="right">LOID��</td>
			<td width="35%" align="left"><input id="loid" type="text"
				class="bk"/></td>
			<td width="15%" align="right">�豸���кţ�</td>
			<td width="35%" align="left"><input id="device" type="text"
				class="bk"/> <font color="red">*�����������λ</font></td>

		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4" align="right"><input type="button" value="��ѯ"
				onclick="showBlue()"/></td>
		</tr>
	</table>

	<div id="space" style="display: none"
		style="width: 100%; z-index: 1; height:50px; top: 100px"></div>

	<table id="blue1" border=0 cellspacing=1 cellpadding=2 width="100%"
		align="center" style="display:none">
		<tr>
			<td colspan="4" align="center" class="green_title">��Ƶ������Ϣ</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4" height="10px"></td>
		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4">
				<table border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center" bgcolor="#ffffff">
					<tr bgcolor="#ffffff" class="green_title">
						<td colspan="4" align="left">���������</td>
					</tr>
					<tr>
						<td colspan="4">
							<table border=0 cellspacing=1 cellpadding=2 width="100%"
								bgcolor="#999999">
								<tr align="center" bgcolor="#ffffff">
									<td class=column5 align="center">PVC/VLAN����</td>
									<td class=column5 align="center">��������</td>
									<td class=column5 align="center">�󶨶˿�</td>
									<td class=column5 align="center">����״̬</td>
									<td class=column5 align="center">DNS(IPV4)</td>
									<td class=column5 align="center">IP��ַ(IPV4)</td>
									<td class=column5 align="center">DNS(IPV6)</td>
									<td class=column5 align="center">IP��ַ(IPV6)</td>
									<td class=column5 align="center">PPPoE�˺�</td>
									<td class=column5 align="center">����ʧ�ܴ�����</td>
								</tr>
								<tr align="center" bgcolor="#ffffff">
									<td align="center">41</td>
									<td align="center">�Ž�</td>
									<td align="center">LAN1</td>
									<td align="center">connected</td>
									<td align="center">123.166.138.87</td>
									<td align="center">123.166.138.87</td>
									<td align="center">-</td>
									<td align="center">-</td>
									<td align="center">n0436ztc5094660</td>
									<td align="center">ERROE_NONE</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4" height="10px"></td>
		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4">
				<table  border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center" bgcolor="#ffffff">
					<tr bgcolor="#ffffff" class="green_title">
						<td colspan="4" align="left">IPTV��</td>
					</tr>
					<tr>
						<td colspan="4">
							<table border=0 cellspacing=1 cellpadding=2 width="100%"
								bgcolor="#999999">
								<tr align="center" bgcolor="#ffffff">
									<td class=column5 align="center">PVC/VLAN</td>
									<td class=column5 align="center">�鲥VLAN</td>
									<td class=column5 align="center">����״̬</td>
									<td class=column5 align="center">��������</td>
									<td class=column5 align="center">�󶨶˿�</td>
									<td class=column5 align="center">�¹��豸����</td>
									<td class=column5 align="center">�¹��豸����״̬</td>
								</tr>
								<tr align="center" bgcolor="#ffffff">
									<td align="center">43</td>
									<td align="center">-</td>
									<td align="center">connected</td>
									<td align="center">·��</td>
									<td align="center">PPPoE_Bridge</td>
									<td align="center">3</td>
									<td align="center">connected</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4" height="10px"></td>
		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4">
				<table   border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center" bgcolor="#ffffff">
					<tr bgcolor="#ffffff" class="green_title">
						<td colspan="4" align="left">VOIP��</td>
					</tr>
					<tr>
						<td colspan="4">
							<table border=0 cellspacing=1 cellpadding=2 width="100%"
								bgcolor="#999999">
								<tr align="center" bgcolor="#ffffff">
									<td class=column5 align="center">�����˿�</td>
									<td class=column5 align="center">Э������</td>
									<td class=column5 align="center">PVC/VLAN</td>
									<td class=column5 align="center">DHCP��ַ</td>
									<td class=column5 align="center">��������</td>
									<td class=column5 align="center">����״̬</td>
									<td class=column5 align="center">��������ַ</td>
									<td class=column5 align="center">���÷�����</td>
									<td class=column5 align="center">ע��״̬</td>
									<td class=column5 align="center">ע���˺�</td>
									<td class=column5 align="center">�¹��豸����</td>
									<td class=column5 align="center">�¹��豸����״̬</td>
								</tr>
								<tr align="center" bgcolor="#ffffff">
									<td align="center">1</td>
									<td align="center">H.248</td>
									<td align="center">45</td>
									<td align="center">172.50.228.4</td>
									<td align="center">IP_Routed</td>
									<td align="center">connected</td>
									<td align="center">192.168.1.100</td>
									<td align="center">192.168.1.101</td>
									<td align="center">-</td>
									<td align="center">A</td>
									<td align="center">2</td>
									<td align="center">connected</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>

		<tr bgcolor="#ffffff">
			<td colspan="4" height="10px"></td>
		</tr>
		<tr>
			<td colspan="4">
				<table border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center" bgcolor="#999999">
					<tr>
						<td colspan="4" align="center" class="green_title">�û���Ϊ������Ϣ</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td colspan="4" align="left">���ҵ��</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td align="center" width="25%" class="column">�û�����ҵ�����ͣ�</td>
						<td align="left" width="25%">���ҵ��</td>
						<td align="center" width="25%" class="column">����ʱ�� ��</td>
						<td align="left" width="25%">2016/10/18 19:40</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td align="center" width="25%" class="column">�����豸IP��</td>
						<td align="left" width="25%">192.168.1.1</td>
						<td align="center" width="25%" class="column">ʹ��ʱ��(min) ��</td>
						<td align="left" width="25%">90</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td colspan="4" align="left">IPTV��</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td align="center" width="25%" class="column">�û�����ҵ�����ͣ�</td>
						<td align="left" width="25%">IPTV</td>
						<td align="center" width="25%" class="column">����ʱ�� ��</td>
						<td align="left" width="25%">2016/10/18 19:40</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td align="center" width="25%" class="column">�����豸IP��</td>
						<td align="left" width="25%">192.168.1.1</td>
						<td align="center" width="25%" class="column">ʹ��ʱ��(min) ��</td>
						<td align="left" width="25%">90</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td colspan="4" align="left">VOIP��</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td align="center" width="25%" class="column">�û�����ҵ�����ͣ�</td>
						<td align="left" width="25%">VOIP</td>
						<td align="center" width="25%" class="column">����ʱ�� ��</td>
						<td align="left" width="25%">2016/10/18 19:40</td>

					</tr>
					<tr bgcolor="#ffffff">
						<td align="center" width="25%" class="column">�����豸IP��</td>
						<td align="left" width="25%">192.168.1.1</td>
						<td align="center" width="25%" class="column">ʹ��ʱ��(min)��</td>
						<td align="left" width="25%">90</td>
					</tr>
				</table>
			</td>
		</tr>

	</table>
	<div id="div_update" style="display: none"
		style="width: 100%; z-index: 1; top: 100px"></div>
</body>
</html>