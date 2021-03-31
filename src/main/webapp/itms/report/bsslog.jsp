<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ģ�⹤��������־</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/itms/report/bsslogQuery!bsslogQuery.action' />";
		mainForm.submit();
	}

	function ToExcel(loid, bussinessacount, startOpenDate, operationuser,
			bssaccount) {
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/itms/report/bsslogQuery!getExcel.action'/>?"
				+ "loid="
				+ loid
				+ "&bussinessacount="
				+ bussinessacount
				+ "&startOpenDate="
				+ startOpenDate
				+ "&operationuser="
				+ operationuser;
		+"&bssaccount=" + bssaccount;
		mainForm.submit();
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
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>

<body>
	<form id="selectForm" name="selectForm" action="" target="dataForm">
		<table>
			<input name="id" type="hidden" value="">
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								ģ�⹤��������־</td>
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
							<th colspan="4">ģ�⹤��������־</th>
						</tr>

						<TR>
							<TD class="column" align="right" width="15%">LOID</TD>
							<TD width="35%"><INPUT TYPE="text" NAME="loid" maxlength=50
								class=bk value=""></TD>
							<TD class=column width="15%" align='right'>ҵ������</TD>
							<TD width="35%"><SELECT name="bussinessacount" class=bk>
									<option value="">==��ѡ��==</option>
									<option value="10">���</option>
									<option value="11">IPTV</option>
									<option value="14">VOIP</option>
							</SELECT></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>ҵ���ʺ�</TD>
							<TD width="35%"><INPUT TYPE="text" NAME="bssaccount"
								maxlength=50 class=bk value=""></TD>
							<TD class=column width="15%" align='right'>����ʱ��</TD>
							<TD width="35%"><input type="text" name="startOpenDate"
								readonly class=bk value="<s:property value="startOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��"></TD>
						</TR>
						<TR>
							<TD class="column" align="right" width="15%">������</TD>
							<TD width="35%"><INPUT TYPE="text" NAME="operationuser"
								maxlength=50 class=bk value=""></TD>
							<TD class="column" align="right" width="15%"></TD>
							<TD width="35%"></TD>
						</TR>

						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;��&nbspѯ&nbsp;</button>
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
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>