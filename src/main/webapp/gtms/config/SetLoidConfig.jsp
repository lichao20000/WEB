<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>批量回填设备LOID</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
</head>
<body>
	<s:form action="/gtms/config/setLoidAction!doConfig.action"
		method="post" enctype="multipart/form-data" name="batchexform"
		target="dataForm">
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
											批量回填设备LOID</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td>
								<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
									align="center">
									<tr>
										<td bgcolor=#999999 colspan="2">
											<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
												<TR>
													<TH colspan="2" align="center">批量回填设备LOID</TH>
												</TR>
											</TABLE>
										</td>
									</tr>
									<tr>
										<td class="foot" align="center" width="15%">请选择文件</td>
										<td width="85%" class="foot"><s:file label="上传"
												theme="simple" name="upload"></s:file><font color="red">*</font>
											xls格式文档 <input type="hidden" name="fileName" value="" />
											<input type="hidden" name="fileType" value="" /></td>
									</tr>
									<tr>
										<td CLASS="green_foot" align="right">注意事项</td>
										<td colspan="" CLASS="green_foot">
											1、需要导入的文件格式限于Excel即xls格式和txt格式 。 <br>
											2、文件的第一行为标题行，【设备序列号】、【LOID】。 <br> 3、文件只有2列。 <br>
											4、文件行数不要太多，以免影响性能。最多为5000行。
										</td>
									</tr>
									<TR>
										<TD colspan="2" align="right" class="foot">
											<button type="button" id="exeButton" name="exeButton"
												onclick="doExecute();" class=btn>&nbsp;配&nbsp;置
												&nbsp;</button>
										</TD>
									</TR>
								</TABLE>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td bgcolor=#999999><iframe id="dataForm" name="dataForm"
									height="0" frameborder="0" scrolling="no" width="100%" src="">
								</iframe></td>
						</tr>
					</table>
				</TD>
			</TR>
		</TABLE>
	</s:form>
</body>
<script type="text/javascript">
	var deviceId = "";
	$(function() {
		gwShare_setGaoji();
	});
	function doExecute() {
		var myFile = $("input[@name='upload']").val();
		if ("" == myFile) {
			alert("请选择文件!");
			return false;
		}
		var filet = myFile.split(".");
		if (filet.length < 2) {
			alert("请选择文件!");
			return false;
		}
		
		if("xls" == filet[filet.length-1] || "XLS" == filet[filet.length-1]){
			$("input[@name='fileType']").val("xls");
		}else if("txt" == filet[filet.length-1] || "TXT" == filet[filet.length-1]){
			$("input[@name='fileType']").val("txt");
		}
		
		if ("xls" == filet[filet.length - 1]||"XLS" == filet[filet.length-1]||"txt" == filet[filet.length-1] || "TXT" == filet[filet.length-1]) {
			var file = myFile.split("\\");
			var fileName = file[file.length - 1];
			$("input[@name='fileName']").attr("value", fileName);
			$("form[@name='batchexform']").submit();
		} else {
			alert("仅支持后缀为xls或txt的文件");
			return false;
		}
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
</script>
</html>
<%@ include file="../../foot.jsp"%>