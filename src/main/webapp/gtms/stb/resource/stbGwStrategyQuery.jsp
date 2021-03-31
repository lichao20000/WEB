<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids = [ "dataForm" ]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide = "no"

function dyniframesize() {
	var dyniframe = new Array()
	for (i = 0; i < iframeids.length; i++) {
		if (document.getElementById) {
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document
					.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera) {
				dyniframe[i].style.display = "block"
				//如果用户的浏览器是NetScape
				if (dyniframe[i].contentDocument
						&& dyniframe[i].contentDocument.body.offsetHeight)
					dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
				//如果用户的浏览器是IE
				else if (dyniframe[i].Document
						&& dyniframe[i].Document.body.scrollHeight)
					dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
			}
		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
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
			alert("请选择设备！");
		}
	}
</SCRIPT>

<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24> 您当前的位置：单台设备策略结果查询
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

