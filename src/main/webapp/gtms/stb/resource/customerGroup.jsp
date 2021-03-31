<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>用户分组类型管理</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.blockUI.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
	/** add by chenjie 2011.4.22 **/
	function block() {
		$.blockUI({
			overlayCSS : {
				backgroundColor : '#CCCCCC',
				opacity : 0.6
			},
			message : "<font size=3>正在操作，请稍后...</font>"
		});
	}

	function unblock() {
		$.unblockUI();
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
	
	/**
	* 查询数据
	*/
	function queryData() {
		var groupName = $.trim($("#groupName").val());
		/*if (!groupName) {
			alert("请输入用户分组名称查询！");
			return false;
		}*/
		$("#selectForm").attr("action", "<s:url value='/gtms/stb/resource/customerGroupACT!queryDataList.action'/>");
		$("#selectForm").submit();
	}
	
	function editData(groupId) {
		var url="<s:url value='/gtms/stb/resource/customerGroupACT!queryData.action'/>?groupId="+groupId;
		window.showModalDialog(url, window,'dialogWidth=800px;dialogHeight=450px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised'); 
	}

	function addData() {
		var url="<s:url value='/gtms/stb/resource/customerGroupACT!queryData.action'/>";
		window.showModalDialog(url, window,'dialogWidth=800px;dialogHeight=450px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
	}
</SCRIPT>
</head>
<body>
	<form name="selectForm" id="selectForm" method="post" action="" target="dataForm">
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD><IMG height=20 src="<s:url value='/images/bite_2.gif'/>"
					width=24>您当前的位置：用户分组类型管理
            </TD>
			</TR>
		</TABLE>
		<TABLE width="98%" class="querytable" align="center">
			<tr>
				<th colspan="2" id="thTitle" class="title_1">用户分组类型管理</th>
			</tr>

			<TR>
				<TD width="10%" class="title_2">用户分组名称：</TD>
				<TD width="40%"><input type="text" name="groupName" id="groupName" value="" size="20" maxlength="40" class="bk" /></TD>
			</TR>
			
			<tr align="right">
				<td colspan="2" class="foot" align="right">
					<div class="right">
						<input type="button" onclick="javascript:queryData()" align="right" class=jianbian
							value=" 查 询 " />
						<input type="button" onclick="javascript:addData()" align="right" class=jianbian
							value=" 新增 " />
					</div>
				</td>
			</tr>
		</TABLE>
		<br>
		<div>
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src=""></iframe>
		</div>
	</form>
</body>
<br>
<br>