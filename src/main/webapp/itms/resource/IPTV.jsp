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
<title>IPTVҵ���������ҳ��</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
	function showBlue() {
		var txt = document.getElementById("iptv").value;
		//var classifyName = $.trim($("input[@name='classifyName']").val());
		$("div[@id='div_update']").html("");
		$("div[@id='div_update']").css("display", "");
		if (txt.length == 0) {
			alert("����������IPTVҵ���˺ţ�");
		}
		if ("n0436ztc5094660" == txt) {
			document.all.blue1.style.display = '';
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
			<td width="15%" align="right">IPTVҵ���˺�</td>
			<td align="left" colspan="3"><input id="iptv" type="text"
				class="bk"/><font color="red">*��������IPTVҵ���˺�</font></td>

		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4" align="right"><input type="button" value="��ѯ"
				onclick="showBlue()"/></td>
		</tr>
	</table>

	<table id="blue1" border=0 cellspacing=1 cellpadding=2 width="100%"
		align="center" bgcolor="#999999" style="display: none">
		<tr>
			<td colspan="4" align="center" class="green_title">��Ƶ������Ϣ</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">Ƶ����ַ��</td>
			<td align="left" width="25%">239.160.1.2</td>
			<td align="center" width="25%" class="column">EPG���ʴ�����</td>
			<td align="left" width="25%">2</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">���EPG��Ӧʱ��(ms)��</td>
			<td align="left" width="25%">1000</td>
			<td align="center" width="25%" class="column">ƽ��EPG��Ӧʱ��(ms)��</td>
			<td align="left" width="25%">300</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">���EPG����ʱ��(ms)��</td>
			<td align="left" width="25%">2000</td>
			<td align="center" width="25%" class="column">ƽ��EPG����ʱ��(ms)��</td>
			<td align="left" width="25%">500</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">���EPG��֤ʱ��(ms)��</td>
			<td align="left" width="25%">500</td>
			<td align="center" width="25%" class="column">ƽ��EPG��֤ʱ��(ms)��</td>
			<td align="left" width="25%">100</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">��Ƶ����ʱ����</td>
			<td align="left" width="25%">5000</td>
			<td align="center" width="25%" class="column">MOSֵ��</td>
			<td align="left" width="25%">4.84</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">VSTQֵ��</td>
			<td align="left" width="25%">50</td>
			<td align="center" width="25%" class="column">������(%)��</td>
			<td align="left" width="25%">0</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">ƽ������(΢��)��</td>
			<td align="left" colspan="3">100</td>
		</tr>

		<tr style="" bgcolor="#ffffff">
			<td align="center" width="25%"></td>
			<td align="center" width="25%" class="column">ƽ��ʱ����</td>
			<td align="center" width="25%" class="column">ʧ�ܴ���</td>
			<td align="center" width="25%" class="column">�������</td>
		</tr>
		<tr style="" bgcolor="#ffffff">
			<td align="center" width="25%" class="column">ֱ��ҵ���л�</td>
			<td align="center" width="25%">500</td>
			<td align="center" width="25%">0</td>
			<td align="center" width="25%">2</td>
		</tr>
		<tr style="" bgcolor="#ffffff">
			<td align="center" width="25%" class="column">�㲥ҵ���л�</td>
			<td align="center" width="25%">1000</td>
			<td align="center" width="25%">0</td>
			<td align="center" width="25%">0</td>
		</tr>
		<tr style="" bgcolor="#ffffff">
			<td align="center" width="25%" class="column">�ؿ�ҵ���л�</td>
			<td align="center" width="25%">500</td>
			<td align="center" width="25%">0</td>
			<td align="center" width="25%">10</td>
		</tr>

		<tr bgcolor="#ffffff">
			<td colspan="4" height="10px"></td>
		</tr>
		<tr>
			<td colspan="4" align="center" class="green_title">����������Ϣ</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">�յ��İ���</td>
			<td align="left" width="25%">50000</td>
			<td align="center" width="25%" class="column">������ ��</td>
			<td align="left" width="25%">0</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">������(����ֵ<0.1%)��</td>
			<td align="left" width="25%">0%</td>
			<td align="center" width="25%" class="column">ý�嶪����(����ֵ<2%)��</td>
			<td align="left" width="25%">0%</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">��СDF(ms)��</td>
			<td align="left" width="25%">0</td>
			<td align="center" width="25%" class="column">���DF(ms)��</td>
			<td align="left" width="25%">0</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">ƽ��DF(����ֵ<50ms)��</td>
			<td align="left" width="25%">0</td>
			<td align="center" width="25%" class="column">����(����ֵ<50us)��</td>
			<td align="left" width="25%">0</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4" height="10px"></td>
		</tr>

		<tr>
			<td colspan="4" align="center" class="green_title">�û���Ϊ������Ϣ</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">���¹ۿ�Ƶ�����ƣ�</td>
			<td align="left" width="25%">CCTV1</td>
			<td align="center" width="25%" class="column">Ƶ�����ʴ��� ��</td>
			<td align="left" width="25%">20</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">���¹ۿ�Ƶ�����ݣ�</td>
			<td align="left" width="25%">�����̸</td>
			<td align="center" width="25%" class="column">����ʱ�䣺</td>
			<td align="left" width="25%">2016/10/18 19:40</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">�ۿ����Ƶ����</td>
			<td align="left" width="25%" colspan="3">CCTV5</td>
		</tr>


	</table>
	<div id="div_update" style="display: none"
		style="width: 100%; z-index: 1; top: 100px"></div>
</body>
</html>