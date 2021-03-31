<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>HTTP����ҵ����������</title>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/CheckFormForm.js"></SCRIPT>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

var area = "<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>";
var wanType=new Array();
function CheckForm(){
 	var __device_id = $("input[@name='deviceId']").val();

	if(__device_id == null || __device_id == ""){
		alert("���Ȳ�ѯ�豸!");
		return false;
	}
	return true;
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


function queryHTTPService(){
	var username = trim($("input[@name='userName']").val());
	var password = trim($("input[@name='password']").val());
	var downLoadUrl =trim($("input[@name='downLoadUrl']").val());
	if(username == null || username == ''){
		alert("����������û���!");
		return false;
	}
	if(password == null || password == ''){
		alert("����������!");
		return false;
	}
	if(downLoadUrl == null || downLoadUrl == ''){
		alert("������������ص�url!");
		return false;
	}
	$("input[@name='deviceSerialnumber']")
	$("td[@id='messageInfo']").css("display","");
	$("td[@id='tdData']").css("display","none");
	$("input[@name='button']").attr("disabled", true);
	document.frm.submit();
}

function init(){
	$("td[@id='messageInfo']").css("display","none");
	$("td[@id='trDeviceResult']").css("display","");
	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
}

function closeMessageInfo(){
	$("td[@id='messageInfo']").css("display","none");
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

$(function() {
	dyniframesize();
});

$(window).resize(function() {
	dyniframesize();
});

function deviceResult(callData){
	$("input[@name='gwShare_queryButton']").attr("disabled", false);
	
	init();
	var returnVal = callData.split("#");
	
	$("td[@id='tdDeviceSn']").append(returnVal[1]);
	$("input[@name='oui']").val(returnVal[2]);
	$("input[@name='deviceSerialnumber']").val(returnVal[1]);
	$("td[@id='tdDeviceCityName']").append(returnVal[6]);
	
	$("input[@name='userName']").val('');
	$("input[@name='password']").val('');
}

</SCRIPT>
</head>
<body>
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD HEIGHT=20>&nbsp;</TD>
		</TR>
		<TR>
			<TD>

				<table width="98%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite" nowrap>
										HTTP����ҵ���������</td>
									<td nowrap><img src="../images/attention_2.gif" width="15"
										height="12"> ��̨HTTP����ҵ���������</td>
								</tr>
							</table>
						</td>
					</tr>

					<TR bgcolor="#FFFFFF">
						<td colspan="4"><%@ include
								file="/ids/share/idsShareDeviceWAN.jsp"%>
						</td>
					</TR>
					<TR>
						<td id="trDeviceResult" style="display: none;">
							<FORM NAME="frm" METHOD="post"
								action="<s:url value='/ids/httpQualityCheck!httpQualityCheck.action'/>"
								target="dataForm" onsubmit="return CheckForm()">
								<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
									align="center">
									<TR>
										<TD bgcolor=#999999>
											<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
												<TR bgcolor="#FFFFFF">
													<td nowrap align="right" class=column width="15%">
														�豸����</td>
													<td id="tdDeviceCityName" width="35%"></td>
													<td nowrap align="right" class=column width="15%">
														�豸���к�</td>
													<td id="tdDeviceSn" width="35%"></td>
													<input type="hidden" name="deviceSerialnumber"
														id="deviceSerialnumber" value="" />
													<input type="hidden" name="oui" id="oui" value="" />
												</TR>
												<TR bgcolor="#FFFFFF">
													<TD nowrap align="right" class=column width="15%">�û����ص�URL</TD>
													<TD colspan="3"><input type="text" name=downLoadUrl
														value="http://117.71.32.12/speed1000.dat" size="30"
														class="bk"></TD>
												</TR>
												<TR bgcolor="#FFFFFF" id="testUserInfo">
													<TD nowrap align="right" class=column width="15%">�����û���</TD>
													<TD width="35%"><input type="text" id="userName"
														name="userName" size="20" class="bk" /></TD>

													<TD nowrap align="right" class=column width="15%">����</TD>
													<TD width="35%"><input type="password" id="password"
														name="password" size="20" class="bk" /></TD>
												</TR>
												<TR bgcolor="#FFFFFF">
													<TD colspan="5" align="right" class="green_foot"><INPUT
														TYPE="button" name="button" id="button" class=jianbian
														value=" ��   ��" onclick="javaScript:queryHTTPService()"></TD>
												</TR>
											</TABLE>
										</TD>
									</TR>
								</TABLE>
							</FORM>
						</td>
					</tr>

					<tr>
						<td height="20"></td>
					</tr>

					<TR bgcolor="#FFFFFF">
						<TD id="messageInfo"
							style="display: none; background-color: #E1EEEE; height: 20">
							��̨���ڲ�ѯ��HTTP��������ָ�꣬�����ĵȴ�......</TD>
					</TR>
					<TR>
						<TD id="tdData" style="display: none;" HEIGHT=20><iframe
								id="dataForm" name="dataForm" height=0 frameborder="0"
								scrolling="no" width="100%" src=""></iframe></TD>
					</TR>
				</table>
			</TD>
		</TR>

	</TABLE>
</body>
</html>
<%@ include file="../../foot.jsp"%>