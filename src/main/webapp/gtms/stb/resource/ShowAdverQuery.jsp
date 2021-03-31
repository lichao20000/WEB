<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������·������ѯ</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/jquery.blockUI.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">


	/*------------------------------------------------------------------------------
	 //������:		deviceSelect_change_select
	 //����  :	type 
	 vendor      �����豸����
	 deviceModel �����豸�ͺ�
	 devicetype  �����豸�汾
	 //����  :	����ҳ������̡��ͺż�����
	 //����ֵ:		
	 //˵��  :	
	 //����  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	function gwShare_change_select(type, selectvalue) {
		switch (type) {
		case "deviceModel":
			var url = "<s:url value='/gtms/stb/resource/showAdver!getDeviceModel.action'/>";
			var vendorId = $("select[@name='vendorId']").val();
			if ("-1" == vendorId) {
				$("select[@name='device_model']").html(
						"<option value='-1'>==����ѡ����==</option>");
				$("select[@name='device_version']").html(
						"<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url, {
				vendorId : vendorId
			}, function(ajax) {
				gwShare_parseMessage(ajax, $("select[@name='device_model']"),
						selectvalue);
				$("select[@name='device_version']").html(
						"<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value='/gtms/stb/resource/showAdver!getSoftVersion.action'/>";
			var deviceModelId = $("select[@name='device_model']").val();
			if ("-1" == deviceModelId) {
				$("select[@name='gwShare_devicetypeId']").html(
						"<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url, {
				device_model : deviceModelId
			}, function(ajax) {
				gwShare_parseMessage(ajax, $("select[@name='device_version']"),
						selectvalue);
			});
			break;
		default:
			alert("δ֪��ѯѡ�");
			break;
		}
	}

	//������ѯ�豸�ͺŷ���ֵ�ķ���
	function gwShare_parseMessage(ajax, field, selectvalue) {
		var flag = true;
		if ("" == ajax) {
			return;
		}
		var lineData = ajax.split("#");
		if (!typeof (lineData) || !typeof (lineData.length)) {
			return false;
		}
		field.html("");
		option = "<option value='-1' selected>==��ѡ��==</option>";
		field.append(option);
		for (var i = 0; i < lineData.length; i++) {
			var oneElement = lineData[i].split("$");
			var xValue = oneElement[0];
			var xText = oneElement[1];
			if (selectvalue == xValue) {
				flag = false;
				//����ÿ��value��text��ǵ�ֵ����һ��option����
				option = "<option value='"+xValue+"' selected>==" + xText
						+ "==</option>";
			} else {
				//����ÿ��value��text��ǵ�ֵ����һ��option����
				option = "<option value='"+xValue+"'>==" + xText
						+ "==</option>";
			}
			try {
				field.append(option);
			} catch (e) {
				alert("�豸�ͺż���ʧ�ܣ�");
			}
		}
		if (flag) {
			field.attr("value", "-1");
		}
	}

	/*------------------------------------------------------------------------------
	 //������:		checkip
	 //����  :	str �������ַ���
	 //����  :	���ݴ���Ĳ����ж��Ƿ�Ϊ�Ϸ���IP��ַ
	 //����ֵ:		true false
	 //˵��  :	
	 //����  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	function checkip(str) {
		var pattern = /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
		return pattern.test(str);
	}

	/*------------------------------------------------------------------------------
	 //������:		trim
	 //����  :	str �������ַ���
	 //����  :	���ݴ���Ĳ�������ȥ�����ҵĿո�
	 //����ֵ:		trim��str��
	 //˵��  :	
	 //����  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	function trim(str) {
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}

	/*------------------------------------------------------------------------------
	 //������:		queryAdverList
	 //����  :	���ݴ���Ĳ�����ѯ����·����
	 //����ֵ:		��������
	 //˵��  :	
	 //����  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	function queryAdverList() {
		var deviceSerialnumber = trim($("input[@name='deviceSerialnumber']")
				.val());
		var loopbackIp = trim($("input[@name='loopbackIp']").val());
		var servAccount = trim($("input[@name='servAccount']").val());
		if ("" != servAccount) {
			$("input[@name='servAccount']").val(servAccount);
		}
		if ("" != deviceSerialnumber && deviceSerialnumber.length < 6) {
			alert("�豸���к��������������6λ��");
			return false;
		} else {
			$("input[@name='deviceSerialnumber']").val(deviceSerialnumber);
		}
		if ("" != loopbackIp && !checkip(loopbackIp)) {
			alert("��������ȷ��IP��ַ��");
			return false;
		} else {
			$("input[@name='loopbackIp']").val(loopbackIp);
		}
		document.selectForm.queryButton.disabled = true;
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
		for (var i = 0; i < iframeids.length; i++) {
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
						dyniframe[i].height = dyniframe[i].document.body.scrollHeight;
				}
			}
			//�����趨�Ĳ���������֧��iframe�����������ʾ����
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block";
			}
		}
	}
	$(function() {
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</SCRIPT>
</head>
<body>
	<form name="selectForm" method="post"
		action="<s:url value='/gtms/stb/resource/showAdver!adverResultList.action'/>" target="dataForm">
		<table width="98%" align="center" class="querytable">
			<tr>
				<td><img height=20 src="<s:url value='/images/bite_2.gif'/>"
					width=24> ����ǰ��λ�ã���������·������ѯ</td>
			</tr>
		</table>
		<table width="98%" class="querytable" align="center">
			<tr>
				<th colspan="4" id="thTitle" class="title_1">��������·������ѯ</th>
			</tr>
			<tr id="tr21" STYLE="display:">
				<td width="10%" class="title_2">����</TD>
				<td width="40%"><s:select list="vendorList" name="vendorId"
						headerKey="-1" headerValue="==��ѡ��==" listKey="vendor_id"
						listValue="vendor_add" value="vendorId" cssClass="bk"
						onchange="gwShare_change_select('deviceModel','-1');"
						theme="simple">
					</s:select></td>

				<td width="10%" class="title_2">�ͺ�</td>
				<td width="40%"><select name="device_model" class="bk"
					onchange="gwShare_change_select('devicetype','-1')">
						<option value="-1">����ѡ����</option>
				</select></td>
			</tr>

			<tr id="tr22" STYLE="display:">
				<td width="10%" class="title_2" align="center">�汾</td>
				<td width="40%"><select name="device_version" class="bk">
						<option value="-1">����ѡ���豸�ͺ�</option>
				</select></td>

				<td width="10%" class="title_2">�豸���к�</td>
				<td width="40%"><input type="text" name="deviceSerialnumber" value=""
					size="20" maxlength="40" class="bk" /></td>
			</tr>

			<tr id="tr22" STYLE="display:">
				<td width="10%" class="title_2">���һ���ϱ�IP</td>
				<td width="40%"><input type="text" name="loopbackIp" value=""
					size="20" maxlength="40" class="bk" /></td>
				<td width="10%" class="title_2">�豸MAC��ַ</td>
				<td width="40%"><input type="text" name="cpeMac" value=""
					size="20" maxlength="20" class="bk" /></td>
			</tr>

			<tr id="tr22" STYLE="display:">
				<td width="10%" class="title_2">ҵ���˺�</td>
				<td width="40%"><input type="text" name="servAccount" value=""
					size="20" maxlength="40" class="bk" /></td>
				<td width="10%" class="title_2">�·�״̬</td>
				<td width="40%"><select name="faultType"
					id="addressingType" class="bk">
						<option value="-1">===��ѡ��===</option>
						<option value="success">�ɹ�,��Ҫ����</option>
						<option value="fail">ʧ��</option>
						<option value="notIssued">δ�·�</option>
				</select></td>
			</tr>
			<tr id="tr23" STYLE="display:">
				<td width="10%" class="title_2">��������</td>
				<td width="40%"><input type="text" name="taskName" value=""
					size="23" maxlength="23" class="bk" /></td>
			</tr>
			<tr align="right">
				<td colspan="4" class="foot" align="right">
					<div class="right">
						<button name="queryButton" align="right"
							onclick="return queryAdverList()">�� ѯ</button>
					</div>
				</td>
			</tr>
		</table>
		<br>
		<div>
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src=""></iframe>
		</div>
	</form>
</body>
<br>
<br>