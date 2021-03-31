<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
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
	 //������:		��ʼ��������ready��
	 //����  :	��
	 //����  :	��ʼ�����棨DOM��ʼ��֮��
	 //����ֵ:		
	 //˵��  :	
	 //����  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	$(function() {
		var queryType = "<s:property value="queryType"/>";
		var queryResultType = "<s:property value="queryResultType"/>";
		if ("" != queryType && null != queryType) {
			$("input[@name='queryType']").val(queryType);
		}
		if ("" != queryResultType && null != queryResultType) {
			$("input[@name='queryResultType']").val(queryResultType);
		}
		$("input[@name='startLastTime']").val("");
		$("input[@name='endLastTime']").val("");
	});

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
	 //������:		queryChange
	 //����  :	change 1:�򵥲�ѯ��2:�߼���ѯ
	 //����  :	���ݴ���Ĳ���������ʾ�Ľ���
	 //����ֵ:		��������
	 //˵��  :	
	 //����  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	function queryDevice() 
	{
		var username = trim($("input[@name='username']").val());
		var servAccount = trim($("input[@name='servAccount']").val());
		var deviceSerialnumber = trim($("input[@name='deviceSerialnumber']").val());
		var loopbackIp = trim($("input[@name='loopbackIp']").val());
		if ("" != username) {
			$("input[@name='username']").val(username);
		}
		if ("" != deviceSerialnumber && deviceSerialnumber.length < 6) {
			alert("�������������6λ��");
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
		document.selectForm.submit();
	}

	function reset() {
		document.selectForm.username.value = "";
		document.selectForm.deviceSerialnumber.value = "";
		document.selectForm.servAccount.value = "";
		document.selectForm.cityId.value = "00";
		document.selectForm.loopbackIp.value = "";
		document.selectForm.addressingType.value = "-1";
		document.selectForm.startLastTime.value = "";
		document.selectForm.endLastTime.value = "";
		document.selectForm.status.value = "-1";
		document.selectForm.cpeMac.value = "";
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
		action="<s:url value="/gtms/stb/resource/gwDeviceQueryStb!zeroqueryDeviceList.action"/>"
		target="dataForm">
		
		<input type="hidden" name="queryType" value="2" />
		<input type="hidden" name="queryResultType" value="none" />
		<input type="hidden" name="type" value="<s:property value='type'/>" />
		<input type="hidden" name="instArea" value="<s:property value='instArea'/>" />
		
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>"width=24> 
					<s:if test="type==0">
            			����ǰ��λ�ã������в�ѯͳͳ��
            		</s:if>
            		<s:else>
            			����ǰ��λ�ã������в�ѯͳͳ��(��ɾ��)
            		</s:else>
            	</TD>
			</TR>
		</TABLE>
		
		<TABLE width="98%" class="querytable" align="center">
			<tr>
				<th colspan="4" id="thTitle" class="title_1">��������Ϣ��ѯ</th>
			</tr>
			
			<TR id="tr21" STYLE="display:">
				<TD width="10%" class="title_2">�����˺�</TD>
				<TD width="40%">
					<input type="text" name="username" value="" size="20" maxlength="50" class="bk" />
				</TD>
				<TD width="10%" class="title_2">�豸���к�</TD>
				<TD width="40%">
					<input type="text" name="deviceSerialnumber" value="" size="20" maxlength="40" class="bk" />
				</TD>
			</TR>

			<TR id="tr22" STYLE="display:">
				<TD width="10%" class="title_2">ҵ���˺�</TD>
				<TD width="40%"><input type="text" name="servAccount" value=""
					size="20" maxlength="40" class="bk" /></TD>
				<TD width="10%" class="title_2">�豸����</TD>
				<TD width="40%">
					<select name="cityId">
						<option value="">==��ѡ��==</option>
						<s:iterator value="cityList">
							<option value="<s:property value="city_id"/>">
								<s:property value="city_name" />
							</option>
						</s:iterator>
					</select>
				</TD>
			</TR>

			<TR id="tr22" STYLE="display:">
				<TD width="10%" class="title_2">���һ���ϱ�IP</TD>
				<TD width="40%">
					<input type="text" name="loopbackIp" value="" size="20" maxlength="40" class="bk" />
				</TD>
				<TD width="10%" class="title_2">��������</TD>
				<TD width="40%">
					<select name="addressingType" id="addressingType" class="bk">
						<option value="-1">===��ѡ��===</option>
						<option value="PPPoE">PPPoE</option>
						<option value="DHCP">DHCP</option>
						<option value="LAN">LAN</option>
					</select>
				</TD>
			</TR>

			<TR id="tr23" STYLE="display:">
				<TD width="10%" class="title_2">�״��ϱ�ʱ��</TD>
				<TD width="40%">
					<input type="text" name="startLastTime"	readonly class=bk value="<s:property value="startLastTime" />">
					<img name="shortDateimg"
					onClick="WdatePicker({el:document.selectForm.startLastTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
					src="../../../images/dateButton.png" width="13" height="12" border="0" alt="ѡ��">
					&nbsp;~&nbsp; 
					<input type="text" name="endLastTime" readonly class=bk value="<s:property value="endLastTime" />">
					<img name="shortDateimg"
					onClick="WdatePicker({el:document.selectForm.endLastTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
					src="../../../images/dateButton.png" width="13" height="12" border="0" alt="ѡ��">
				</TD>
				<TD width="10%" class="title_2">�豸MAC��ַ</TD>
				<TD width="40%">
					<input type="text" name="cpeMac" value="" size="23" maxlength="23" class="bk" />
				</TD>
			</TR>
			<TR id="biaotou1"  STYLE="display:">
				<TD width="10%" class="title_2">���÷�ʽ</TD>
				<TD width="40%">
					<s:select list="statusList" name="status"
						headerKey="-1" headerValue="===��ѡ��===" listKey="status_id"
						listValue="status_name" value="status_id" cssClass="bk"
						theme="simple">
					</s:select>
				</TD>
			</TR>
			<tr align="right">
				<td colspan="4" class="foot" align="right">
					<div class="right">
						<input type="button" onclick="javascript:queryDevice()" align="right" class=jianbian
							name="queryButton" value=" �� ѯ " />
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