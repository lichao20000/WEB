<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<title>IPTV��4K�������豸ͳ��</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	dyniframesize();
});

function query(data_type,queryTime)
{
	if("exp"!=data_type){
		isShowButton(false);
		isCountDesc("����ͳ�ƣ����Ե�....");
	}
	
	$("input[@name='data_type']").val(data_type);
	$("input[@name='queryTime']").val(queryTime);
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/gtms/stb/resource/stbDeviceCount!stbN4KCount.action'/>";
	form.submit();
}

function isShowButton(tag){
	if(tag){
		$("button[@name='queryButton']").attr("disabled", false);
	}else{
		$("button[@name='queryButton']").attr("disabled", true);
	}
}

function isCountDesc(str){
	$("tr[@id='trData']").show();
    $("div[@id='QueryData']").html(str);
}

</SCRIPT>
</head>

<body>
<form name="mainForm" action="" target="dataForm">
	<input type='hidden' name="data_type" value="" />
	<input type='hidden' name="queryTime" value="" />
	<TABLE width="98%" align="center" class="querytable">
		<TR>
			<TD >
				<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
	            	����ǰ��λ�ã�IPTV��4K�������豸ͳ��
			</TD>
		</TR>
	</TABLE>
	<TABLE width="98%" class="querytable" align="center">
		<tr>
			<th id="thTitle" class="title_1">ȫʡ��4K�����зֲ�����</th>
		</tr>
		<tr align="right">
			<td class="foot" align="right">
				<div class="right">
					<button name="queryButton" align="right" onclick="javascript:query('query','')"> ͳ �� </button>
				</div>
			</td>
		</tr>
	</TABLE>
	<br>
	<div id="resultData">
		<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
	</div>
</form>
<table>
	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				����ͳ�ƣ����Ե�....
			</div>
		</td>
	</tr>
</table>

</body>
<SCRIPT LANGUAGE="JavaScript">
//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["dataForm"]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block"
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
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    		tempobj.style.display="block"
		}
	}
}

</SCRIPT>
</html>