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

<title>��������APIȨ�޹���</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">

	function getApiPluginList() {
		var classifyName = $.trim($("input[@name='classifyName']").val());
		var creator = $.trim($("input[@name='creator']").val());
		//document.getElementById("queryButton").disabled = true;
		var frm = document.getElementById("frm");
		frm.action = "<s:url value='/itms/resource/apiPlugin!getApiPluginList.action'/>";
		frm.submit();
	}
	function showAdd(){
		var strpage = "itmsAPIPluginAdd.jsp";
		window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
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
				<td colspan="4" align="center" class="green_title">���APIȨ�޷���</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td width="15%" align="right" class=column>Ȩ�޷������ƣ�</td>
				<td width="35%" align="left"><input name="classifyName"
					type="text" class="bk" /></td>
				<td width="15%" align="right" class=column>�����ߣ�</td>
				<td width="35%" align="left"><input name="creator" type="text"
					class="bk" /></td>
			</tr>
			<tr bgcolor=#ffffff>
				<td class=column align="right" width="15%">��ʼʱ�䣺</td>
				<td width="35%"><lk:date id="starttime" name="starttime"
						defaultDate="%{starttime}" /></td>
				<td class=column align="right" width="15%">����ʱ�䣺</td>
				<td width="35%"><lk:date id="endtime" name="endtime"
						defaultDate="%{endtime}" /></td>
			</tr>
			<tr bgcolor="#ffffff">
				<td class=column align="right">����״̬</td>
				<td colspan="3"><select name='status' class="select"
					style="width: 85px;">
						<option value="1" selected>��Ч</option>
						<option value="2">ʧЧ</option>
				</select></td>
			</tr>
			<tr bgcolor="#ffffff">
				<td colspan="4" align="right"><input type="button" value="����"
					onclick="showAdd()" /><input type="button" value="��ѯ"
					onclick="getApiPluginList()" /></td>
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