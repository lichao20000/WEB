<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%
request.setCharacterEncoding("gbk");
String gwType = request.getParameter("gw_type");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>查询单双栈结果</title>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/CheckFormForm.js"></SCRIPT>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">


function deviceResult(returnVal){
	$("td[@id='trDeviceResult']").css("display","");

	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	
	$("input[@name='device_id']").val(returnVal[2][0][0]);
	$("td[@id='tdDeviceSn']").append(returnVal[2][0][1]+" -"+returnVal[2][0][2]);
	$("td[@id='tdDeviceCityName']").append(returnVal[2][0][5]);	
}

function CheckForm(){
    var __device_id = $("input[@name='device_id']").val();
	if(__device_id == null || __device_id == ""){
		alert("请先查询设备!");
		return false;
	}
}



function query(){
	$("td[@id='messageInfo']").css("display","");
	$("td[@id='tdData']").css("display","none");
	$("input[@name='button']").attr("disabled", true);
	document.frm.submit();
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
</SCRIPT>
</head>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
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
									查询单双栈结果
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>
				<TR>
					<td id="trDeviceResult" style="display:none;">
						<FORM NAME="frm" METHOD="post" action="<s:url value='/gtms/config/StackRefreshQueryACT!queryStackRefreshDetail.action'/>"
							target="dataForm" onsubmit="return CheckForm()">
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" >
									<TR>
										<TD bgcolor=#999999>
												<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
														<TR bgcolor="#FFFFFF">
															<td nowrap align="right" class=column width="15%">
																设备属地
																<input type="hidden" name="device_id" id="device_id" value="" />
																<input type="hidden" name="gw_type" value="<%=gwType%>"/>
															</td>
															<td id="tdDeviceCityName" width="35%">
															</td>
															<td nowrap align="right" class=column width="15%">
																设备序列号
															</td>
															<td id="tdDeviceSn" width="35%">
															</td>
														</TR>
														<TR bgcolor="#FFFFFF">
															<TD colspan="5" align="right" class="green_foot">
																<INPUT TYPE="button" name="button" id="button" class=jianbian value= "查询"  onclick="javaScript:query()">
															</TD>
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
					<TD id="messageInfo"  style="display:none;background-color: #E1EEEE;height: 20" >
						后台正在查询，请耐心等待......		
					</TD>
				</TR>
				<TR>
					<TD id="tdData" style="display:none;" HEIGHT=20>
						<iframe id="dataForm" name="dataForm" height=0 frameborder="0" scrolling="no" width="100%" src=""></iframe>
					</TD>
				</TR>
			</table>
		</TD>
	</TR>
	
</TABLE>
</body>
</html>
<%@ include file="../../foot.jsp"%>