<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
function ExecMod() {
	var softwareversion = trim($("select[@name='div_goal_softwareversion']").val());
	var filename = $("input[@name='gwShare_fileName']").val();
	if (-1 == softwareversion) {
		alert("��ѡ��Ŀ��汾��");
		return false;
	}
	if ("" == filename || null == filename) {
		alert("���ϴ��ļ���");
		return false;
	}
	
	var url = "<s:url value='/gtms/stb/resource/softupgradByImport!anlazye.action'/>";
	$("tr[@id='messageInfo']").css("display","");
	$.post(url, {
		div_goal_softwareversion : softwareversion,
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



<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD><IMG height=20 src="<s:url value='/images/bite_2.gif'/>"
			width=24> ����ǰ��λ�ã����������������������</TD>
	</TR>
</TABLE>
<br>
<table width="98%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<TR>
		<td><%@ include file="../share/gwShareDeviceQueryByImport.jsp"%>
		</td>
	</TR>


	<TR id="softwareversion">
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="">
				<input type="hidden" name=gwShare_fileName value="" />
				<table width="100%" class="querytable">
					<TR>
						<TH colspan="4" class="title_1">�汾����</TH>
					</TR>
					<TR>
						<TD align="right" width="15%">����������Է�ʽ��</TD>
						<TD width="30%"><SELECT name="softStrategy_type">
								<option value="4">�´����ӵ�ϵͳ</option>
						</SELECT></TD>
						<td nowrap class="title_2" width="15%"><font color="red">*</font>&nbsp;Ŀ��汾
						</TD>
						<TD width=""><s:select list="versionpathList"
								name="div_goal_softwareversion" headerKey="-1"
								headerValue="==��ѡ��==" listKey="id" listValue="version_path"
								cssClass="bk" theme="simple">
							</s:select></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD colspan="4" CLASS="foot">
							<div class="right">
								<button id="softUpgrade_btn" onclick="ExecMod()">�� ��</button>
							</div>
						</TD>
					</TR>
					<TR id="messageInfo" style="display: none">
						<TD style="background-color: #E1EEEE; height: 10" colspan="4">
							���ڷ��������Ժ�...</TD>
					</TR>
				</TABLE>
			</FORM>
		</TD>
	</TR>
</TABLE>

