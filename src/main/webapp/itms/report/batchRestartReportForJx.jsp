<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒批量重启</title>
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

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block"
     			//如果用户的浏览器是NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
      			//如果用户的浏览器是IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
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
            	您当前的位置：机顶盒批量重启
		</TD>
	</TR>
</TABLE>
<TABLE width="98%" class="querytable" align="center">
	<tr><th colspan="4" id="thTitle" class="title_1">机顶盒批量重启</th></tr>
	<TR id="tr22" STYLE="display:">
		<TD width="10%"  class="title_2">开始时间</TD>
		<TD width="40%">
		  <input type="text" name="startTime" class='bk' readonly value="<s:property value='startTime'/>">
			<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="<s:url value='/images/dateButton.png'/>" width="15" height="12"
								border="0" alt="选择" />	
		</TD>
		<TD width="10%" class="title_2" >结束时间</TD>
		<TD width="40%">
			<input type="text" name="endTime" class='bk' readonly
								value="<s:property value='endTime'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="<s:url value='/images/dateButton.png'/>" width="15" height="12"
								border="0" alt="选择" />	
		</TD>
	</TR>
	<tr align="right">
		<td colspan="4" class="foot" align="right">
			<div class="right">
				<button align="right" onclick="javascript:query()"> 查 询 </button>
				<button align="right" onclick="javascript:reset()"> 重置 </button>
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
