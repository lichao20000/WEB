<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", -10);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���������ѯ</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
var content = $.trim($("select[@name='content']").val());
var city_name = $.trim($("select[@name='city_name']").val());
var loid = $.trim($("input[@name='loid']").val());
var net_account = $.trim($("select[@name='net_account']").val());
var itv_account = $.trim($("input[@name='itv_account']").val());
var voip_account = $.trim($("select[@name='voip_account']").val());
	function query() {
		
		if (document.all("fileNames").value == "") {
			alert("�����ϴ��ļ���");
			return;
		}
		isShow();
		//$("input[@name='fileNames']").val(fileP[fileP.length - 1]);
		document.selectForm.submit();
	}
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
		dyniframesize();
	});

	function isShow() {
		$("tr[@id='trData']").show();
	}

	function isNotShow() {
		$("tr[@id='trData']").hide();
	}
	
	$(window).resize(function() {
		dyniframesize();
	});

	function ToExcel() {
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/itms/report/exportUser!getUserInfoExcel.action'/>";
		mainForm.submit();
		mainForm.action = "<s:url value='/itms/report/exportUser!queryUserList.action'/>";
	}
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/itms/report/exportUser!queryUserList.action'/>"
		target="dataForm" method="POST" enctype="multipart/form-data">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								���������ѯ</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> ���������ѯ</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">
						<TR>
							<th colspan="4">�����ļ�</th>
						</tr>
						<TR>
							<TD colspan="2" class=column width="15%" align='right'>�ύ�ļ���</td>
							<td colspan="2" align="center"><input type="hidden"
								name="fileType" value=""> <input type="hidden"
								name="fileName" value=""> <input name="rowNum"
								type="hidden" value="2000">
								<div id="importUsername">
														<iframe id="ifa" name="user_loadForm" FRAMEBORDER=0
															SCROLLING=NO src="<s:url value="FileUpload.jsp"/>"
															height="20" width="100%"> </iframe>
														<input type="hidden" name="fileNames" value="" />
													</div>
								</td>
						</TR>
						<TR>
							<TD colspan="2" class=column width="15%" align='right'>ע�����</TD>
							<TD colspan="2"><font color="#7f9db9">
									1����Ҫ������ļ���ʽ����Excel���ı��ļ�����xls��txt��ʽ �� <br>
									2���ļ��ĵ�һ��Ϊ�����У��ֱ��С��û�LOID����������˺š�����ITV�˺š����������˺š��� <br>
									3���ļ�ֻ��һ�С� <br> 4���ļ�������Ҫ����2000�У�����Ӱ�����ܡ� <br>
							</font></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()" id="button" name="button">�� ѯ</button>
							</td>
						</TR>
					</table>
				</td>
			<tr id="trData" style="display: none;">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
						���ڲ�ѯ�����Ե�....</div>
				</td>
			</tr>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td height="80%"><iframe id="dataForm" name="dataForm"
						height="100%" frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
		</table>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>