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
		alert("请上传文件！");
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
									批量导入改造后E8C设备
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
						批量导入改造后E8C设备分析
					</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post" ACTION="">
							<TABLE width="100%" class="querytable">
								<TR bgcolor="#FFFFFF">
									<TD colspan="4" CLASS="foot">
										<div class="right">
											<button id="softUpgrade_btn" onclick="ExecMod()">解析文件</button>
										</div>
									</TD>
								</TR>
								<TR id="messageInfo" style="display: none">
									<TD style="background-color: #E1EEEE; height: 10" colspan="4">
										正在分析，请稍后...</TD>
								</TR>
							</TABLE>
						</FORM>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
</TABLE>

