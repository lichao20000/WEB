<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�û��������͹���</title>
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
	
	/**
	* ��ѯ����
	*/
	function queryData() {
		var groupName = $.trim($("#groupName").val());
		/*if (!groupName) {
			alert("�������û��������Ʋ�ѯ��");
			return false;
		}*/
		$("#selectForm").attr("action", "<s:url value='/gtms/stb/resource/customerGroupACT!queryDataList.action'/>");
		$("#selectForm").submit();
	}
	
	function editData(groupId) {
		var url="<s:url value='/gtms/stb/resource/customerGroupACT!queryData.action'/>?groupId="+groupId;
		window.showModalDialog(url, window,'dialogWidth=800px;dialogHeight=450px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised'); 
	}

	function addData() {
		var url="<s:url value='/gtms/stb/resource/customerGroupACT!queryData.action'/>";
		window.showModalDialog(url, window,'dialogWidth=800px;dialogHeight=450px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
	}
</SCRIPT>
</head>
<body>
	<form name="selectForm" id="selectForm" method="post" action="" target="dataForm">
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD><IMG height=20 src="<s:url value='/images/bite_2.gif'/>"
					width=24>����ǰ��λ�ã��û��������͹���
            </TD>
			</TR>
		</TABLE>
		<TABLE width="98%" class="querytable" align="center">
			<tr>
				<th colspan="2" id="thTitle" class="title_1">�û��������͹���</th>
			</tr>

			<TR>
				<TD width="10%" class="title_2">�û��������ƣ�</TD>
				<TD width="40%"><input type="text" name="groupName" id="groupName" value="" size="20" maxlength="40" class="bk" /></TD>
			</TR>
			
			<tr align="right">
				<td colspan="2" class="foot" align="right">
					<div class="right">
						<input type="button" onclick="javascript:queryData()" align="right" class=jianbian
							value=" �� ѯ " />
						<input type="button" onclick="javascript:addData()" align="right" class=jianbian
							value=" ���� " />
					</div>
				</td>
			</tr>
		</TABLE>
		<br>
		<div>
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src=""></iframe>
		</div>
	</form>
</body>
<br>
<br>