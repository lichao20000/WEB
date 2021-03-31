<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>HTTP下载业务质量测试</title>
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
		alert("请先查询设备!");
		return false;
	}
	return true;
}

/*------------------------------------------------------------------------------
//函数名:		trim
//参数  :	str 待检查的字符串
//功能  :	根据传入的参数进行去掉左右的空格
//返回值:		trim（str）
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function trim(str) {
	return str.replace(/(^\s*)|(\s*$)/g, "");
}


function queryHTTPService(){
	var username = trim($("input[@name='userName']").val());
	var password = trim($("input[@name='password']").val());
	var downLoadUrl =trim($("input[@name='downLoadUrl']").val());
	if(username == null || username == ''){
		alert("请输入测试用户名!");
		return false;
	}
	if(password == null || password == ''){
		alert("请输入密码!");
		return false;
	}
	if(downLoadUrl == null || downLoadUrl == ''){
		alert("请输入测试下载的url!");
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

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids = [ "dataForm" ]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide = "yes"

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
										HTTP下载业务质量检测</td>
									<td nowrap><img src="../images/attention_2.gif" width="15"
										height="12"> 单台HTTP下载业务质量检测</td>
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
														设备属地</td>
													<td id="tdDeviceCityName" width="35%"></td>
													<td nowrap align="right" class=column width="15%">
														设备序列号</td>
													<td id="tdDeviceSn" width="35%"></td>
													<input type="hidden" name="deviceSerialnumber"
														id="deviceSerialnumber" value="" />
													<input type="hidden" name="oui" id="oui" value="" />
												</TR>
												<TR bgcolor="#FFFFFF">
													<TD nowrap align="right" class=column width="15%">用户下载的URL</TD>
													<TD colspan="3"><input type="text" name=downLoadUrl
														value="http://117.71.32.12/speed1000.dat" size="30"
														class="bk"></TD>
												</TR>
												<TR bgcolor="#FFFFFF" id="testUserInfo">
													<TD nowrap align="right" class=column width="15%">测试用户名</TD>
													<TD width="35%"><input type="text" id="userName"
														name="userName" size="20" class="bk" /></TD>

													<TD nowrap align="right" class=column width="15%">密码</TD>
													<TD width="35%"><input type="password" id="password"
														name="password" size="20" class="bk" /></TD>
												</TR>
												<TR bgcolor="#FFFFFF">
													<TD colspan="5" align="right" class="green_foot"><INPUT
														TYPE="button" name="button" id="button" class=jianbian
														value=" 测   试" onclick="javaScript:queryHTTPService()"></TD>
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
							后台正在查询，HTTP下载质量指标，请耐心等待......</TD>
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