<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>������ͼ���ò��Բ�ѯ</title>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">

function query(){
	configInfoClose();
	bssSheetClose();
	var startOpenDate=$("input[@name='startOpenDate']").val();
	var endOpenDate=$("input[@name='endOpenDate']").val();
	var username=$("input[@name='username']").val();
	if(username==null||username=="")
		{
		$("input[@name='username']").focus();
		alert("������LOID���û��ʺ���Ϣ���в�ѯ");
		return false;
		}
	else if(startOpenDate>endOpenDate)
		{
		alert("��ʼʱ�䲻�ܴ��ڽ���ʱ�䣡");
		return false;
		}
	else
		{
	document.selectForm.submit();
		}
}

function configInfoClose(){
	$("td[@id='configInfoEm']").hide();
	$("td[@id='configInfo']").hide();
}

function configDetailClose(){
	$("td[@id='bssSheetDetail']").hide();
}

function bssSheetClose(){
	$("td[@id='bssSheetInfo']").hide();
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
	for (var i=0; i<iframeids.length; i++)
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
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i]);
    		tempobj.style.display="block";
		}
	}
}

$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
});

</script>
</head>

<body>
	<form id="form" name="selectForm" action="<s:url value='/itms/resource/SoundGraphsQuery!query.action'/>"
		target="dataForm">
		<input type="hidden" name="selectType" value="0" /> <input
			type="hidden" name="gw_type" value='<s:property value="gw_type" />' />
		<input type="hidden" name="netServUp"
			value='<s:property value="netServUp" />' /> <input type="hidden"
			name="isRealtimeQuery" value='<s:property value="isRealtimeQuery" />' />
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table width="100%" height="30" border="0" cellspacing="0"
						cellpadding="0" class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								������ͼ���ò��Բ�ѯ</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> ��ʼʱ�䡢����ʱ��Ϊ����ʱ��</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">������������ͼ���ò��Բ�ѯ</th>
						</tr>

						<TR>
							<TD class=column width="15%" align='right'><SELECT
								name="usernameType">
									<option value="1">LOID</option>
									<option value="2">��������˺�</option>
									<option value="3">IPTV����˺�</option>
									<option value="4">VoIP��֤����</option>
									<option value="5">VoIP�绰����</option>
							</SELECT></TD>
							<TD width="35%"><input type="text" name="username" size="20"
								maxlength="30" class=bk /></TD>
							<TD class=column width="15%" align='right'>��ͨ״̬<%--  <s:property
									value="gwType" /> --%>
							</TD>
							<TD width="35%"><SELECT name="openstatus">
									<option value="">==��ѡ��==</option>
									<option value="1">==�ɹ�==</option>
									<option value="10000">==δ��==</option>
									<option value="-1">==ʧ��==</option>
							</SELECT></TD>

						</TR>

						<TR>
							<TD class=column width="15%" align='right'>��ͨʱ��</TD>
							<TD width="35%"><input type="text" name="startOpenDate"
								readonly class=bk value="<s:property value="startOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/inmp/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��"> &nbsp; <font color="red"> *</font></TD>
							<TD class=column width="15%" align='right'>����ʱ��</TD>
							<TD width="35%"><input type="text" name="endOpenDate"
								readonly class=bk value="<s:property value="endOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/inmp/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��"> &nbsp; <font color="red"> *</font></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;�� ѯ&nbsp;</button>
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
				<td height="25" id="configInfoEm" style="display: none"></td>
			</tr>
			<tr>
				<td id="configInfo"></td>
			</tr>
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>

			<tr>
				<td id="bssSheetDetail"></td>
			</tr>


		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="/foot.jsp"%>