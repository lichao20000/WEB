<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
function ExecMod() {
	var filename = $("input[@name='gwShare_fileName']").val();
	if ("" == filename || null == filename) {
		alert("���ϴ��ļ���");
		return false;
	}
	
	var url = "<s:url value='/gwms/resource/DeviceE8CImportACT!analyse.action'/>";
	$("tr[@id='messageInfo']").css("display","");
	$.post(url, {
		gwShare_fileName :filename	
	}, function(ajax) {
		$("tr[@id='messageInfo']").css("display","none");
		alert(ajax);
	});
}

function trim(str) {
	return str.replace(/(^\s*)|(\s*$)/g, "");
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



<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									������������E8C�豸
								</td>
								<td nowrap>
									<img src="<s:url value='/images/attention_2.gif'/>" width="15" height="12">
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="../share/gwShareDeviceQueryByImport.jsp"%>
					</td>
				</TR>
				<TR>
					<TH colspan="4" align="center">
						������������E8C�豸����
					</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post" ACTION="">
							<TABLE width="100%" class="querytable">
								<TR bgcolor="#FFFFFF">
									<TD colspan="4" CLASS="foot">
										<div class="right">
											<button id="softUpgrade_btn" onclick="ExecMod()">�����ļ�</button>
										</div>
									</TD>
								</TR>
								<TR id="messageInfo" style="display: none">
									<TD style="background-color: #E1EEEE; height: 10" colspan="4">
										���ڷ��������Ժ�...</TD>
								</TR>
							</TABLE>
						</FORM>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
</TABLE>

