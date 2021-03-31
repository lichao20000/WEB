<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>社会化管控设备查询</title>
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
		alert("请至少输入最后6位设备序列号进行查询！");
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
//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids = [ "dataForm" ];

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide = "yes";

function dyniframesize() {
	var dyniframe = new Array();
	for (var i = 0; i < iframeids.length; i++) {
		if (document.getElementById) {
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document
					.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera) {
				dyniframe[i].style.display = "block";
				//如果用户的浏览器是NetScape
				if (dyniframe[i].contentDocument
						&& dyniframe[i].contentDocument.body.offsetHeight)
					dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
				//如果用户的浏览器是IE
				else if (dyniframe[i].Document
						&& dyniframe[i].Document.body.scrollHeight)
					dyniframe[i].height = dyniframe[i].document.body.scrollHeight;
			}
		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
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
						<TD width="164" align="center" class="title_bigwhite">设备查询</TD>
						<td>&nbsp; <img
							src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12"> &nbsp; 社会化管控设备列表,可根据设备序列号查询指定设备。
						</td>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr>
						<th colspan="4">社会化管控设备查询</th>
					</tr>
					<TR bgcolor=#ffffff>
						<td class="column" width='15%' align="right">设备序列号：</td>
						<td width='35%' align="left"><input
							name="device_serialnumber" type="text" class='bk'
							value="<s:property value='device_serialnumber'/>"></td>

					</TR>



					<TR>
						<td class="green_foot" colspan="4" align="right"><input
							class="btn" name="button" type="button" onclick="do_test();"
							value=" 查 询 "> <INPUT CLASS="btn" TYPE="button"
							value=" 重 置 " onclick="resetFrm()"></TD>
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