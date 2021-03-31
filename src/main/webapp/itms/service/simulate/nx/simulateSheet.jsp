<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>BSSģ�⹤��</title>
<%%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	//��ʾ����������Ϣ
	function showSheet() {
		var _servTypeId = $("select[@name='servTypeId']");
		var _operateType = $("select[@name='operateType']");
		if ('-1' == _servTypeId.val()) {
			alert("��ѡ��ҵ������");
			_servTypeId.focus();
			return;
		}
		if ('-1' == _operateType.val()) {
			alert("��ѡ���������");
			_operateType.focus();
			return;
		}
		document.mainfrm.submit();
	}
	var hasUsername = 0;
	function checkUserInfo() {
		var _username = $("input[@name='username']").val();
		var url = "<s:url value='/itms/service/simulateNxNewSheet!checkUsername.action'/>";
		$.post(url, {
			username : _username
		}, function(ajax) {
			var relt = ajax.split("#");
			if (relt[0] != "1") {
				isHasUsername = 0;
				$("div[@id='usernameDiv']").html(
						"<font color=red>" + relt[1] + "</font>");
			} else {
				hasUsername = 1;
			}
		});
	}
	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids=["dataForm"];

	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide="yes";

	function dyniframesize() 
	{
		var dyniframe=new Array();
		for (i=0; i<iframeids.length; i++)
		{
			if (document.getElementById)
			{
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera)
				{
	     			dyniframe[i].style.display="block";
	     			//����û����������NetScape
	     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
	      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
	      			//����û����������IE
	     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
	      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
	   			 }
	   		}
			//�����趨�Ĳ�����������֧��iframe�����������ʾ����
			if ((document.all || document.getElementById) && iframehide=="no")
			{
				var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i]);
	    		tempobj.style.display="block";
			}
		}
	}

	$(function(){
		//setValue();
		dyniframesize();
	});

	$(window).resize(function(){
		dyniframesize();
	}); 
</script>
</head>

<body>
<form name="mainfrm"
	action="<s:url value='/itms/service/simulateNxNewSheet!showSheet.action'/>"
	target="dataForm">

<table>
	<tr>
		<td HEIGHT=20>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<table class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">BSSģ�⹤��</td>
				<td><img src="<s:url value="/images/attention_2.gif"/>"
					width="15" height="12" /> BSSģ�⹤��</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table class="querytable">

			<TR>
				<th colspan="4">BSSģ�⹤��</th>
			</tr>

			<TR bgcolor="#FFFFFF">
				<TD class=column align="right" width="20%">ҵ������</TD>
				<TD width="30%"><select name="servTypeId" class=bk>
					<option value="-1">==��ѡ��ҵ������==</option>
					<option value="20">��������</option>
					<option value="22">����ҵ��</option>
					<option value="21">IPTVҵ��</option>
					<!-- <option value="14">VOIPҵ��(SIP)</option> -->
					<option value="15">VOIPҵ��(H248)</option>
					<option value="14">SIP����</option>
					<!-- <option value="10">E8B����</option>
					<option value="11">E8B IPTV</option> -->
				</select>&nbsp; <font color="#FF0000">* </font></TD>
				<TD class=column align="right" width="20%">��������</TD>
				<TD width="30%"><select name="operateType" class=bk onchange="">
					<option value="-1">==��ѡ���������==</option>
					<option value="1">����</option>
					<option value="3">����</option>
				</select>&nbsp; <font color="#FF0000">* </font></TD>
			</TR>
			<TR>
				<td colspan="4" align="right" class=foot>
				<button onclick="showSheet()">&nbsp;ģ�⹤��&nbsp;</button>
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
</table>
<br>
</form>
</body>
</html>