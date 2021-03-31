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
		alert("请选择目标版本！");
		return false;
	}
	if ("" == filename || null == filename) {
		alert("请上传文件！");
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



<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD><IMG height=20 src="<s:url value='/images/bite_2.gif'/>"
			width=24> 您当前的位置：机顶盒批量导入软件升级</TD>
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
						<TH colspan="4" class="title_1">版本升级</TH>
					</TR>
					<TR>
						<TD align="right" width="15%">软件升级策略方式：</TD>
						<TD width="30%"><SELECT name="softStrategy_type">
								<option value="4">下次连接到系统</option>
						</SELECT></TD>
						<td nowrap class="title_2" width="15%"><font color="red">*</font>&nbsp;目标版本
						</TD>
						<TD width=""><s:select list="versionpathList"
								name="div_goal_softwareversion" headerKey="-1"
								headerValue="==请选择==" listKey="id" listValue="version_path"
								cssClass="bk" theme="simple">
							</s:select></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD colspan="4" CLASS="foot">
							<div class="right">
								<button id="softUpgrade_btn" onclick="ExecMod()">升 级</button>
							</div>
						</TD>
					</TR>
					<TR id="messageInfo" style="display: none">
						<TD style="background-color: #E1EEEE; height: 10" colspan="4">
							正在分析，请稍后...</TD>
					</TR>
				</TABLE>
			</FORM>
		</TD>
	</TR>
</TABLE>

