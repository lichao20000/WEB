<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Ӫ��֧�ű���</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		$("td[@id='sheetContent']").hide();
		var custManagerId = $.trim($("select[@name='custManagerId']").val());
		if (custManagerId == "-1") {
			alert("��ѡ��ͻ�����");
			return false;
		}
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

					{
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
					}

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
		//setValue();
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>

<body>
<form name="selectForm"
	action="<s:url value='/gtms/blocTest/sellSupportCustomize!queryReport.action'/>"
	target="dataForm"><input type="hidden" name="selectType"
	value="0" />
<table>
	<tr>
		<td HEIGHT=20>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<table class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">Ӫ��֧�ű����ѯ</td>
				<td><img src="<s:url value="/images/attention_2.gif"/>"
					width="15" height="12" /> ��ѯӪ��֧�ű������</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table class="querytable">

			<TR>
				<th colspan="4">Ӫ��֧�ű���</th>
			</tr>

			<TR bgcolor="#FFFFFF">
				<td align="right" class=column width="15%">�ͻ�����</td>
				<td align="left" width="35%"><s:select list="custManagerList"
					name="custManagerId" headerKey="-1" headerValue="==��ѡ��=="
					listKey="cust_manager_id" listValue="cust_manager_name"
					cssClass="bk"></s:select></td>

			
			</TR>
			
			<TR>
				<td colspan="4" align="right" class=foot>
				<button onclick="query()">&nbsp;�� ѯ&nbsp;</button>
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

		<!-- <td id="sheetList"></td> -->
	</tr>
	<tr>
		<td height="25"></td>
	</tr>
	<tr>
		<td id="sheetContent"></td>
	</tr>
</table>
<br>
</form>
</body>
</html>