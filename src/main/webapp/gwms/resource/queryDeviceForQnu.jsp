<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��ữ�ܿ��豸��ѯ</title>
<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
function do_test() {
	
	var device_serialnumber = $.trim(document.frm.device_serialnumber.value);
	if(device_serialnumber.length<6&&device_serialnumber.length>0){
		alert("�������������6λ�豸���кŽ��в�ѯ��");
		document.frm.device_serialnumber.focus();
		return false;
	}
	
	
	$("input[@name='button']").attr("disabled", true); 
	
	frm.submit();
}

//reset
function resetFrm() {
	document.frm.device_serialnumber.value="";
	
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

</script>
</head>
<body>
<form name="frm"
	action="<s:url value='/gwms/resource/queryDeviceForQnu!queryDevice.action"'/>"
	method="POST" target="dataForm">

	<table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td height="20"></td>
		</tr>
		<TR>
			<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">�豸��ѯ</TD>
						<td>&nbsp; <img
							src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12"> &nbsp; ��ữ�ܿ��豸�б�,�ɸ����豸���кŲ�ѯָ���豸��
						</td>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr>
						<th colspan="4">��ữ�ܿ��豸��ѯ</th>
					</tr>
					<TR bgcolor=#ffffff>
						<td class="column" width='15%' align="right">�豸���кţ�</td>
						<td width='35%' align="left"><input
							name="device_serialnumber" type="text" class='bk'
							value="<s:property value='device_serialnumber'/>"></td>

					</TR>



					<TR>
						<td class="green_foot" colspan="4" align="right"><input
							class="btn" name="button" type="button" onclick="do_test();"
							value=" �� ѯ "> <INPUT CLASS="btn" TYPE="button"
							value=" �� �� " onclick="resetFrm()"></TD>
					</TR>

				</TABLE>
			</TD>
		</TR>
		<tr>
			<td bgcolor=#ffffff>&nbsp;</td>
		</tr>
		<tr>
			<td><iframe id="dataForm" name="dataForm" height="0"
					frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
		</tr>
	</table>
</form>
</body>
<%@ include file="../foot.jsp"%>
</html>