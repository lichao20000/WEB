<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������������</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.blockUI.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
function query(){
	document.selectForm.action = "<s:url value='/itms/report/batchRestart!qryList.action'/>";
	document.selectForm.submit();
}

function init(){
	query();
}
function reset(){
	document.selectForm[0].reset;
}

function toExcel(){
	var startTime =  selectForm.startTime.value;
	var endTime =  selectForm.endTime.value;
	var page = "<s:url value='/itms/report/batchRestart!qryExcel.action'/>?"
			+ "&startTime="
			+ startTime
			+ "&endTime=" + endTime
	document.all("childFrm").src = page;
}

function detail(taskId,type) {
	var startTime =  selectForm.startTime.value;
	var endTime =  selectForm.endTime.value;
	var strpage = "<s:url value='/itms/report/batchRestart!qryDetail.action'/>?taskId="
			+ taskId
			+ "&type="+type
	        + "&startTime="
	        + startTime
			+ "&endTime=" + endTime
	window.open(strpage, "",
					"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

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
$(function(){
	dyniframesize();
	//init();
});

$(window).resize(function(){
	dyniframesize();
}); 

</SCRIPT>
</head>
<body>
<form name="selectForm" action="<s:url value="/itms/report/batchRestart!qryList.action"/>" target="dataForm">
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD >
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
            	����ǰ��λ�ã���������������
		</TD>
	</TR>
</TABLE>
<TABLE width="98%" class="querytable" align="center">
	<tr><th colspan="4" id="thTitle" class="title_1">��������������</th></tr>
	<TR id="tr22" STYLE="display:">
		<TD width="10%"  class="title_2">��ʼʱ��</TD>
		<TD width="40%">
		  <input type="text" name="startTime" class='bk' readonly value="<s:property value='startTime'/>">
			<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="<s:url value='/images/dateButton.png'/>" width="15" height="12"
								border="0" alt="ѡ��" />	
		</TD>
		<TD width="10%" class="title_2" >����ʱ��</TD>
		<TD width="40%">
			<input type="text" name="endTime" class='bk' readonly
								value="<s:property value='endTime'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="<s:url value='/images/dateButton.png'/>" width="15" height="12"
								border="0" alt="ѡ��" />	
		</TD>
	</TR>
	<tr align="right">
		<td colspan="4" class="foot" align="right">
			<div class="right">
				<button align="right" onclick="javascript:query()"> �� ѯ </button>
				<button align="right" onclick="javascript:reset()"> ���� </button>
			</div>
		</td>
	</tr>
</TABLE>
<br>
<div align="center"><iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="98%" src=""></iframe></div>
</form>
<iframe id="childFrm" name="childFrm" style="display: none"></iframe>
</body>
<br>
<br>
