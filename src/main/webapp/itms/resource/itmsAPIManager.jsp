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

<title>��������API�������</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	function getApiManagerList() {
		var servicenameZh = $.trim($("input[@name='servicenameZh']").val());
		var servicenameEn = $.trim($("input[@name='servicenameEn']").val());
		var functionDesc = $.trim($("input[@name='functionDesc']").val());
		var apiListName = $.trim($("input[@name='apiListName']").val());
		var classifyId = $.trim($("select[@name='classifyId']").val());
		var frm = document.getElementById("frm");
		frm.action = "<s:url value='/itms/resource/apiManager!getApiManagerList.action'/>";
		frm.submit();
	}
	function showAdd() {
		var strpage = "<s:url value='/itms/resource/apiManager!add.action'/>";
		window.open(strpage, "",
						"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
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

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>
<body>
	<form name="frm" id="frm" target="dataForm">
		<table width="100%" border="0" cellspacing="1" cellpadding="2"
			bgcolor="#999999">
			<tr>
				<td colspan="4" align="center" class="green_title">���APIȨ�޹���</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td width="15%" align="right" class=column>��������(����)��</td>
				<td width="35%" align="left"><input name="servicenameZh"
					type="text" class="bk" /></td>
				<td width="15%" align="right" class=column>��������(Ӣ��)��</td>
				<td width="35%" align="left"><input name="servicenameEn"
					type="text" class="bk" /></td>
			</tr>

			<tr bgcolor="#ffffff">
				<td class=column align="right">Ȩ�޷���</td>
				<td><s:select list="classifyList" name="classifyId"
						headerKey="-1" headerValue="ȫ��" listKey="id"
						listValue="classify_name" cssClass="select" style="width:100px;"></s:select></td>
				<td width="15%" align="right" class=column>����������</td>
				<td width="35%" align="left"><input name="functionDesc"
					type="text" class="bk" /></td>
			</tr>
			<tr bgcolor="#ffffff">
				<td colspan="4" align="right"><input type="button" value="����"
					onclick="showAdd()" /><input type="button" value="��ѯ"
					onclick="getApiManagerList()" /></td>
			</tr>

		</table>
	</form>
	<div class="content">
		<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
			scrolling="no" width="100%" src=""></iframe>
	</div>
</body>
<%@ include file="/foot.jsp"%>
</html>