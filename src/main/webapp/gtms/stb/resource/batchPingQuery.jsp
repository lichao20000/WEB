<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���ؼ��</title>
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


	/**
	* ��ѯ����
	*/
	function queryData() {
		var deviceIp = $.trim($("#deviceIp").val());
		if ("" != deviceIp && !checkip(deviceIp)) {
			alert("��������ȷ��IP��ַ��");
			return false;
		}
		$("#selectForm").attr("action", "<s:url value='/gtms/stb/resource/batchPingAction!queryDataList.action'/>");
		$("#selectForm").submit();
	}
	
	/**
	* ִ��ping����
	*/
	function pingCmd(){
		if(!confirm("ִ�й��̿���Ҫ�����ӣ�ȷ��ִ�нű���")){
			return false;
		}
		block();
		var url = '<s:url value='/gtms/stb/resource/batchPingAction!pingCmd.action'/>';
		$.post(url, {}, function(ajax){
		    alert(ajax);
		    unblock();
		    //location.reload();
		});
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
	<form name="selectForm" id="selectForm" method="post" action="" target="dataForm">
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD><IMG height=20 src="<s:url value='/images/bite_2.gif'/>"
					width=24>����ǰ��λ�ã����ؼ��
            </TD>
			</TR>
		</TABLE>
		<TABLE width="98%" class="querytable" align="center">
			<tr>
				<th colspan="4" id="thTitle" class="title_1">���ؼ��</th>
			</tr>

			<TR>
				<TD width="10%" class="title_2">�豸IP</TD>
				<TD width="40%"><input type="text" name="deviceIp" id="deviceIp" value="" size="20" maxlength="40" class="bk" /></TD>
				<TD width="10%" class="title_2">�豸����</TD>
				<TD width="40%"><select name="cityId" style="width:105px;">
						<option value="">==��ѡ��==</option>
						<s:iterator value="cityList">
							<option value="<s:property value="city_id"/>">
								<s:property value="city_name" />
							</option>
						</s:iterator>
				</select></TD>
			</TR>
			<TR bgcolor=#ffffff>
				<td class="column" width='15%' align="right">
					��ʼʱ��
				</td>
				<td width='35%' align="left">
					<input type="text" name="starttime" class='bk' readonly
						value="<s:property value='starttime'/>">
					<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��">
				</td>
				<td class="column" width='15%' align="right">
					����ʱ��
				</td>
				<td width='35%' align="left">
					<input type="text" name="endtime" class='bk' readonly
						value="<s:property value='endtime'/>">
					<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��">
				</td>
			</TR>
			<TR>
				<TD width="10%" class="title_2">���</TD>
				<TD width="40%">
					<select name="result" style="width:105px;">
						<option value="">ȫ��</option>
						<option value="0">ʧ��</option>
						<option value="1">�ɹ�</option>
					</select>
				</TD>
				<TD width="10%" class="title_2"></TD>
				<TD width="40%"></TD>
			</TR>

			
			<tr align="right">
				<td colspan="4" class="foot" align="right">
					<div class="right">
						<input type="button" onclick="javascript:queryData()" align="right" class=jianbian
							value=" �� ѯ " />
						<input type="button" onclick="javascript:pingCmd()" align="right" class=jianbian
							value=" ִ�нű� " />
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