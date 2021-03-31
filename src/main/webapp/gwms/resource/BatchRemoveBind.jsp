<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", -10);
	String taskId = request.getParameter("taskId");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>批量解绑</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		if (document.all("fileNames").value == "") {
			alert("请先上传文件！");
			return;
		}
		isShow();
		var fileName = $("input[@name='fileNames']").val();
		var url = '<s:url value="/gwms/resource/batchRemoveBind!fileLineTest.action"/>';
		$.post(url,{
			filename : fileName
		},function(ajax){
			if(ajax != ""){
				isNotShow();
           		alert(ajax);
           		return false;
           	}
           	doRemoveBind();
		});
		
		
	}
	function doRemoveBind(){
		var fileName = $("input[@name='fileNames']").val();
		var url = '<s:url value="/gwms/resource/batchRemoveBind!removeBind.action" />';
		$.post(url,{
			filename : fileName
		},function(ajax){
			document.getElementsByName("taskId")[0].value =ajax;
			document.selectForm.submit();
		}
		);
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

	function isShow() {
		$("tr[@id='trData']").show();
	}

	function isNotShow() {
		$("tr[@id='trData']").hide();
	}
	
	$(window).resize(function() {
		dyniframesize();
	});

</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/gwms/resource/batchRemoveBind!getResultList.action'/>"
		target="dataForm" method="POST" enctype="multipart/form-data">
		<input type="hidden" name="taskId" value="" />
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								批量解绑</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> 批量解绑</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">
						<TR>
							<th colspan="4">导入文件</th>
						</tr>
						<TR>
							<TD colspan="2" class=column width="15%" align='right'>提交文件：</td>
							<td colspan="2" align="center"><input type="hidden"
								name="fileType" value=""> <input type="hidden"
								name="fileName" value=""> <input name="rowNum"
								type="hidden" value="2000">
								<div id="importUsername">
														<iframe id="ifa" name="user_loadForm" FRAMEBORDER=0
															SCROLLING=NO src="<s:url value="FileUpload.jsp"/>"
															height="20" width="100%"> </iframe>
														<input type="hidden" name="fileNames" value="" />
													</div>
								</td>
						</TR>
						<TR>
							<TD colspan="2" class=column width="15%" align='right'>注意事项：</TD>
							<TD colspan="2"><font color="#7f9db9">
									1、需要导入的文件格式限于文本文件，即txt格式 。 <br>
									2、文件的第一行为标题行，【用户LOID】。 <br>
									3、文件只有一列。 <br> 4、文件行数不要超过100行。 <br>
							</font></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()" id="button" name="button">执行</button>
							</td>
						</TR>
					</table>
				</td>
			<tr id="trData" style="display: none;">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
						正在执行，请稍等....</div>
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
<%@ include file="../../foot.jsp"%>