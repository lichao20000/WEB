<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������Ϣ��ѯ</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.blockUI.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
	/** add by chenjie 2011.4.22 **/
	function block() {
		$.blockUI({
			overlayCSS : {
				backgroundColor : '#CCCCCC',
				opacity : 0.6
			},
			message : "<font size=3>���ڲ��������Ժ�...</font>"
		});
	}

	function unblock() {
		$.unblockUI();
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
	 //������:		queryChange
	 //����  :	change 1:�򵥲�ѯ��2:�߼���ѯ
	 //����  :	���ݴ���Ĳ���������ʾ�Ľ���
	 //����ֵ:		��������
	 //˵��  :	
	 //����  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	function queryDevice() 
	{
		var servAccount = trim($("input[@name='servAccount']").val());
		var deviceSerialnumber = trim($("input[@name='deviceSerialnumber']").val());
		document.selectForm.submit();
	}

	function reset() {
		document.selectForm.deviceSerialnumber.value = "";
		document.selectForm.servAccount.value = "";
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
	<form name="selectForm" method="post" action="<s:url value='/gtms/stb/resource/stbUpgradeBlackList!queryBlackDeviceList.action'/>"
		target="dataForm">
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD><IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
            	����ǰ��λ�ã������к�������ѯ
            	</TD>
			</TR>
		</TABLE>
		<TABLE width="98%" class="querytable" align="center">
			<tr>
				<th colspan="4" id="thTitle" class="title_1">�����к�������ѯ</th>
			</tr>
			<TR id="tr21" STYLE="display:">
				<TD width="10%" class="title_2">ҵ���˺�</TD>
				<TD width="40%">
					<input type="text" name="servAccount" value=""
						size="20" maxlength="40" class="bk" />
				</TD>
				<TD width="10%" class="title_2">�豸���к�</TD>
				<TD width="40%">
					<input type="text" name="deviceSerialnumber"
						value="" size="20" maxlength="40" class="bk" />
				</TD>
			</TR>
			
			<tr align="right">
				<td colspan="4" class="foot" align="right">
					<div class="right">
						<button onclick="javascript:queryDevice()" align="right" class=jianbian> �� ѯ </button>
					</div>
				</td>
			</tr>
		</TABLE>
		<br>
		<div>
			<iframe id="dataForm" name="dataForm" height="100%" frameborder="0"
				scrolling="no" width="100%" src=""></iframe>
		</div>
	</form>
</body>
<br>
<br>