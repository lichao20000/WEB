<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>����LOID��ѯ</title>
	<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
		type="text/css">
	<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
		type="text/css">
	<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
	<script type="text/javascript"
        src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
	<script type="text/javascript"
		src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		document.selectForm.submit();
	}
	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids = [ "dataForm" ]

	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide = "yes"

	function dyniframesize() {
		var dyniframe = new Array()
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block"
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
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block"
			}
		}
	}

	$(function() {
		init();
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});

	function init() {
		if($("input[@name='startTime']").val()==''){
			$("input[@name='startTime']").val($.now("-", false) + " 00:00:00");
		}
		if($("input[@name='endTime']").val()==''){
			$("input[@name='endTime']").val($.now("-", false) + " 23:59:59");
		}
	}
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/gtms/config/setLoidAction!initSetLoid.action'/>"
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
								����LOID��ϸ��Ϣ</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">����LOID��ѯ</th>
						</tr>

						<TR>
							<TD class=column width="15%" align='right'>��ʼʱ��</TD>
							<TD width="35%"><input type="text" name="startTime" readonly
								class=bk value="<s:property value="startTime" />"> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��"></TD>
							<TD class=column width="15%" align='right'>����ʱ��</TD>
							<TD width="35%"><input type="text" name="endTime" readonly
								class=bk value="<s:property value="endTime" />"> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��"></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>LOID</TD>
							<TD width="35%"><input type="text" id="loid" name="loid" /></TD>
							<TD class=column width="15%" align='right'>�豸���к�</TD>
							<TD width="35%"><input type="text" id="deviceNumber"
								name="deviceNumber" /></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>״̬</TD>
							<TD width="35%"><select name="statu" class="bk">
									<option value="">==��ѡ��==</option>
									<option value="0">δ��</option>
									<option value="1">�ɹ�</option>
							</select></TD>
							<TD class=column width="15%" align='right'></TD>
							<TD width="35%"></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;��&nbsp;ѯ&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
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
<%@ include file="../../foot.jsp"%>