<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>批量导出设备相关信息</title>
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/inmp/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/inmp/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/inmp/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		isShow();
		$("button[@name='button']").attr("disabled", true);
		var filePath = document.selectForm.file.value;
		if (filePath.replace(/(^\s*)|(\s*$)/g, "") == "") {
			alert("请先选择文件！");
			isNotShow();
			buttonShow();
			$("input[@name='file']").focus();
			return false;
		}
		var filet = "";
		var fileP = "";
		filet = filePath.split(".");
		if (filet.length < 2) {
			alert("浏览的文件格式不正确！");
			isNotShow();
			buttonShow();
			$("input[@name='file']").focus();
			return false;
		}
		if ("xls" != filet[filet.length - 1]
				&& "XLS" != filet[filet.length - 1]
				&& "txt" != filet[filet.length - 1]
				&& "TXT" != filet[filet.length - 1]) {
			alert("只支持Excel2003或者TXT文本");
			isNotShow();
			buttonShow();
			$("input[@name='file']").focus();
			return false;
		} else {
			if ("xls" == filet[filet.length - 1]
					|| "XLS" == filet[filet.length - 1]) {
				$("input[@name='fileType']").val("xls");
			} else if ("txt" == filet[filet.length - 1]
					|| "TXT" == filet[filet.length - 1]) {
				$("input[@name='fileType']").val("txt");
			}
		}
		fileP = filePath.split("\\");
		var myDate = new Date();
		var times = myDate.getTime();
		$("input[@name='fileName']").val(fileP[fileP.length - 1] + times);
		document.selectForm.submit();
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
		for ( var i = 0; i < iframeids.length; i++) {
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
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
				}
			}
			//根据设定的参数来处理不支持iframe的浏览器的显示问题
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i]);
				tempobj.style.display = "block";
			}
		}
	}

	$(function() {
		dyniframesize();
	});
	
	function buttonShow(){
		$("button[@name='button']").attr("disabled", false);
	}

	function isShow() {
		$("tr[@id='trData']").show();
	}

	function isNotShow() {
		$("tr[@id='trData']").hide();
	}
	
	$(window).resize(function() {
		dyniframesize();
	});

	function ToExcel() {
		var filePath = document.selectForm.file.value;
		if (filePath.replace(/(^\s*)|(\s*$)/g, "") == "") {
			alert("请先选择文件！");
			$("input[@name='file']").focus();
			return false;
		}
		var filet = "";
		var fileP = "";
		filet = filePath.split(".");
		if (filet.length < 2) {
			alert("浏览的文件格式不正确！");
			$("input[@name='file']").focus();
			return false;
		}
		if ("xls" != filet[filet.length - 1]
				&& "XLS" != filet[filet.length - 1]
				&& "txt" != filet[filet.length - 1]
				&& "TXT" != filet[filet.length - 1]) {
			alert("只支持Excel2003或者TXT文本");
			$("input[@name='file']").focus();
			return false;
		} else {
			if ("xls" == filet[filet.length - 1]
					|| "XLS" == filet[filet.length - 1]) {
				$("input[@name='fileType']").val("xls");
			} else if ("txt" == filet[filet.length - 1]
					|| "TXT" == filet[filet.length - 1]) {
				$("input[@name='fileType']").val("txt");
			}
		}
		fileP = filePath.split("\\");
		//$("input[@name='fileName']").val(fileP[fileP.length - 1]);
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/inmp/resource/importQuery!getExcel.action'/>";
		mainForm.submit();
		mainForm.action = "<s:url value='/inmp/resource/importQuery!readUploadFile.action'/>";
	}
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/inmp/resource/importQuery!readUploadFile.action'/>"
		target="dataForm" method="POST" enctype="multipart/form-data">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="back_blue">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								批量导出设备相关信息</td>
							<td><img src="<s:url value="/images/inmp/attention_2.gif"/>"
								width="15" height="12" /> 批量导出设备相关信息</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">
						<TR>
							<th colspan="5">导入文件</th>
						</tr>
						<TR>
							<TD colspan="2" class=column width="15%" align='right'>提交文件：</td>
							<td colspan="3" align="center"><input type="hidden"
								name="fileType" value=""> <input type="hidden"
								name="fileName" value=""> <input name="rowNum"
								type="hidden" value="2000"> <input type="hidden"
								name="curPage_splitPage" value=""> <input type="hidden"
								name="numPage_splitPage" value=""> <input type="file" size="60"
								name="file" /></td>
						</TR>
						<TR>
							<TD colspan="2" class=column width="15%" align='right'>注意事项：</TD>
							<TD colspan="2"><font color="#7f9db9">
									1、需要导入的文件格式限于Excel、文本文件，即xls、txt格式 。 <br><br>
									2、文件的第一行为标题行，分别有【用户LOID】、【设备序列号】、【宽带账号】、【ITV账号】、【语音账号】。 <br><br>
									3、文件只有一列。 <br><br> 
									4、文件行数不要超过2000行，以免影响性能。 <br><br>
									5、样例如右图。<br>
							</font></TD>
							<TD class=column width="30%" align='left'><img src="<s:url value="/images/example3.jpg"/>" align="left" width="200px" height="150px"/></TD>
						</TR>
						<TR>
							<td colspan="5" align="right" class=foot>
								<button onclick="query()" id="button" name="button">查 询</button>
							</td>
						</TR>
					</table>
				</td>
			<tr id="trData" style="display: none;">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
						正在查询，请稍等....</div>
				</td>
			</tr>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td height="80%"><iframe id="dataForm" name="dataForm"
						height="100%" frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
		</table>
	</form>
</body>
</html>
<%@ include file="../foot.jsp"%>