<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸��Ϣ��ѯ</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids = [ "dataForm" ];

	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide = "yes";

	function dyniframesize() {
		var dyniframe = new Array();
		for ( var i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block";
					//����û����������NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//����û����������IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
				}
			}
			//�����趨�Ĳ���������֧��iframe�����������ʾ����
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i]);
				tempobj.style.display = "block";
			}
		}
	}
	$(function() {
		//setValue();
		dyniframesize();
	});
	$(window).resize(function() {
		dyniframesize();
	});

	function query() {
		$("button[@name='button']").attr("disabled", true);
		var cityId = $.trim($("select[@name='city_id']").val());
		document.frm.submit();

	}
</script>
</head>

<body>
	<form name="frm" id="frm" method="post"
		action="<s:url value='/gtms/stb/resource/DeviceMessageQuery!querymessage.action'/>"
		target="dataForm">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								�豸��Ϣ��ѯ</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" />�豸�ͺſ���ģ��ƥ��</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">�豸��Ϣ��ѯ��ѯ</th>
						</tr>
						<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
							<TD align="right" class=column width="15%">OUI��</TD>
							<TD align="left" width="35%"><INPUT TYPE="text"
								NAME="oui"  id="oui" maxlength=30 class=bk size=20></TD>
							<TD align="right" class=column width="15%">�豸�ͺţ�</TD>
							<TD align="left" width="35%"><INPUT TYPE="text"
								NAME="device_model"  id="device_model" maxlength=30 class=bk size=20></TD>
						<TR>
							<td colspan="4" align="right" class="foot" width="100%">
								<button onclick="query()">&nbsp;�� ѯ&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr>
				<td><iframe id="dataForm" name="dataForm" height="0"
						frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
