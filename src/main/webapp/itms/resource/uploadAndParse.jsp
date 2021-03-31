<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ include file="/toolbar.jsp"%>
<html>
<head>
<title>主设备厂商数据上传</title>
<link href="<s:url value="/css/uploadAndParse.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT type="text/javascript">
	//规避ie版本较低时, onclick触发input file时,拒绝访问(IE的一种安全机制)
	function add() 
	{
		document.getElementById("filePath").style.posLeft=event.x-document.getElementById("filePath").offsetWidth/2;
	}

	function change() {
		var filePath = document.getElementById("filePath").value;
		document.getElementById("filePathshow").value = filePath;
		document.getElementById("uploadButton").disabled = false;
		document.getElementById("it_stips").style.display="none";
		document.getElementById("it_ok").style.display="none";
	}

	function upload() {
		var filePath = document.getElementById("filePath").value;
		if(""==filePath){
			alert("请先选择文件！");
			return;
		}
		if(filePath.length<4 || ("txt"!= filePath.substr(filePath.length-3,3) && "xls"!= filePath.substr(filePath.length-3,3))){
			document.getElementById("it_stips").style.display="";
			document.getElementById("it_ok").style.display="none";
			return false;
		}
		var fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
		if("案例模板.txt"==fileName || "案例模板.xls"==fileName){
			alert("请修改文件名,不能以案例模板为名!");
			return;
		}
		document.getElementById("fileName").value =fileName;
		document.getElementById("it_stips").style.display="none";
		var url = "<s:url value='/itms/resource/devVendorDataUpload!uploadAndParse.action'/>";
		document.mainForm.action = url;
		document.mainForm.submit();
		parent.document.getElementById("uploadButton").disabled = true;
	}

	function cancel() {
		document.getElementById("mainForm").reset();
		document.getElementById("it_stips").style.display="none";
		document.getElementById("it_ok").style.display="none";
		document.getElementById("uploadButton").disabled = false;
	}

	function downloadcase() {
		var url = "<s:url value='/itms/resource/devVendorDataUpload!download.action'/>";
		document.getElementById("mainForm").action = url;
		document.getElementById("mainForm").submit();
		document.getElementById("mainForm").reset();
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
</head>

<body>
	<div class="it_main">
		<h1 class="it_webtt">主设备厂商数据上传</h1>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
			target="dataForm" enctype="multipart/form-data">
			<div class="file_search">
				<div class="input_line">
					<input type="file" name="filePath" id="filePath" onchange=change() style="position:absolute;filter:alpha(opacity=0);width:40px; height:40px" hidefocus />
					 <input type="hidden" name="fileName" id="fileName" value="" />
					 <input id="filePathshow" type="text" class="it_ipt file_ipt" disabled="disabled" /><input type="button" class="file_btn" value="添加文件" onmouseover="add();" />
				</div>
				<div class="notice">注意事项：文件不大于2000M，格式限于xls、txt格式&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
				<div id="download" >
					<input type="radio" width="22%" name="caseDownload" value="0" onclick="downloadcase()">xls案例下载 </input>
					<input type="radio" width="22%" name="caseDownload" value="1" onclick="downloadcase()">txt案例下载</input>
				</div>
				<div class="it_stips" id ="it_stips" style="display: none">文件格式错误！</div>
				<div class="it_stips it_ok" id ="it_ok" style="display: none">上传成功！</div>
				<div class="foot_btn">
					<a href="javascript:void(0);" id="uploadButton" onclick="upload()" class="putin">上传</a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" onclick="cancel()" class="cancel">取消</a>
				</div>
			</div>
		</FORM>
	</div>
	<div>
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src=""></iframe>
	</div>
</body>
<%@ include file="/foot.jsp"%>
</html>
