<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids = [ "dataForm" ]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide = "no"

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

$(window).resize(function() {
	dyniframesize();
});

	$(function() {
		gwShare_setGaoji();
	});
	
	function deviceResult(returnVal) 
	{
		for ( var i = 0; i < returnVal[2].length; i++) {
			deviceId = returnVal[2][i][0];
		}
		
		if (deviceId != "") 
		{
			$("input[name=device_id]").val(deviceId);
			var frm = document.getElementById("frm");
			frm.action = "<s:url value='/gtms/stb/resource/stbSetParamACT!getStrategyResult.action'/>";
			frm.submit();
		} else {
			alert("��ѡ���豸��");
		}
	}
</SCRIPT>

<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24> ����ǰ��λ�ã���̨�豸���Խ����ѯ
		</TD>
	</TR>
</TABLE>
<br>

<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
	<TR>
		<td>
			<%@ include file="../share/gwShareDeviceQuery.jsp"%>
		</td>
	</TR>
</TABLE>
</br>

<form name="frm" id="frm" target="dataForm">
<input type="hidden" name="device_id" value="" >
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<iframe id="dataForm" name="dataForm" height="500" frameborder="0" scrolling="no" width="100%" src=""></iframe>
		</td>
	</tr>
</table>
</form>

