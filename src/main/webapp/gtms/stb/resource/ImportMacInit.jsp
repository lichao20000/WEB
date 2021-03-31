<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>mac批量输入界面</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.blockUI.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

$(function(){
	
	closeMsgDlg()
	
	var retResult = '<s:property value="retResult" />';
	var fileName = '<s:property value="fileName" />';
	
	if("" != retResult){
		
		if(retResult == '1'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>所导入的文件 "+fileName+" 不符合要求，请按照注意事项上传文件或者点击下载模板</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '-1'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>文件 "+fileName+" 中厂商（要求不可为空），请确认！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '-2'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>文件 "+fileName+" 中产品型号（要求不可以为空），请确认！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '-6'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>文件 "+fileName+" 中设备MAC始（要求不可以为空），请确认！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '6'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>文件 "+fileName+" 未能导入，其中包含字段“设备MAC始”格式错误，请确认！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '-7'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>文件 "+fileName+" 中设备MAC终（要求不可以为空），请确认！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '7'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>文件 "+fileName+" 未能导入，其中包含字段“设备MAC终”格式错误，请确认！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		
		if(retResult == '-4'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>文件 "+fileName+" 中的厂商必须为（华为、中兴、创维、海信、长虹、UT、其他）中厂商，请确认！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '0'){
			alert("导入成功！");
			document.getElementById("actLabel").innerHTML = "<font color='red'>文件 "+fileName+" 导入成功！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
	
		if(retResult == '3'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>您上传的 "+fileName+" 为空，请确认！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '5'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>解析 "+fileName+" 出错！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
	}
});

//下载模板
function getExcelTemplate(){
	$("form[@name='importFrm']").attr("action","importMac!downloadTemplate.action");
	$("form[@name='importFrm']").submit();
}


//数据分析
function doUpload(){
	
	
	
	$("tr[@id='tr01']").css("display","none");

	var filePath = document.importFrm.file.value;
	
	if(filePath.replace(/(^\s*)|(\s*$)/g, "") == ""){
		alert("请先选择文件！");
		$("input[@name='file']").focus();
		return false;
	}
	
	var filet = "";
	var fileP = "";
	 
	filet = filePath.split(".");
	if(filet.length < 2){
		$("input[@name='file']").focus();
		return false;
	}
	
	if("xls"!=filet[filet.length-1] && "XLS"!=filet[filet.length-1]){
				alert("只支持Excel2003");
				$("input[@name='file']").focus();
				return false;
	}else{
				$("input[@name='fileType']").val("xls");
	}
	
	
	<%--
	if("xls"!=filet[filet.length-1] && "XLS"!=filet[filet.length-1] && 
	   "txt"!=filet[filet.length-1] && "TXT"!=filet[filet.length-1]){
		alert("只支持Excel2003或者TXT文本");
		$("input[@name='file']").focus();
		return false;
	}else{
		if("xls" == filet[filet.length-1] || "XLS" == filet[filet.length-1]){
			$("input[@name='fileType']").val("xls");
		}else if("txt" == filet[filet.length-1] || "TXT" == filet[filet.length-1]){
			$("input[@name='fileType']").val("txt");
		}
	}
	--%>
	
	
	fileP = filePath.split("\\");
	
	$("input[@name='fileName']").val(fileP[fileP.length-1]);
	document.importFrm.upload.disabled = true;
	
	showMsgDlg();
	$("form[@name='importFrm']").attr("action","importMac!readUploadFile.action");
	$("form[@name='importFrm']").submit();
}

//初始化的时候调用
	function showMsgDlg(){
			w = document.body.clientWidth;
			h = document.body.clientHeight;
			
			l = (w-250)/2;
			t = h/2-200;
			PendingMessage.style.left = l;
			PendingMessage.style.top  = t;
			PendingMessage.style.display="";
	}
	
	//完成数据，隐藏页面
	function closeMsgDlg(){
			PendingMessage.style.display="none";
	}


</SCRIPT>
</head>
<body>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:300;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;filter:alpha(opacity=80);display:none">
		<center>
				<table border="0">
					<tr>
						<td valign="middle"><img src="<s:url value='/images/cursor_hourglas.gif'/>"  
							border="0" WIDTH="30" HEIGHT="30"></td>
						<td>&nbsp;&nbsp;</td>
						<td valign="middle"><span id=txtLoading
							style="font-size:14px;font-family: 宋体">请稍等,正在解析文档······</span></td>
					</tr>
				</table>
		</center>
</div>

<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD >
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
            	您当前的位置：mac批量模板导入界面
		</TD>
	</TR>
</TABLE>
<TABLE width="98%" class="querytable" align="center">
	<tr><th colspan="4" id="thTitle" class="title_1">批量导入MAC数据</th></tr>
	<TR id="tr21" STYLE="display:">
		<TD width="10%"  class="title_2">提交文件</TD>
		<TD width="90%" colspan="3" align="center">
			<form name="importFrm" action="importMac!readUploadFile.action"
									method="POST" enctype="multipart/form-data">
					<input type="hidden" name="fileType" value="" >
					<input type="hidden" name="fileName" value="" >
					<input name="rowNum" type="hidden" value="20000"><!-- 只解析Excel的前20000行，不包括标题行 -->
					<table>
						<tr>
							<td>
								<input type="file" size="60" name="file" />
								<input type="button" value=" 提 交 "  name="upload" onclick="doUpload()" />
								&nbsp;&nbsp;
								<a href="javascript:void(0);" onClick="getExcelTemplate();"><font color='red'>点击下载模板</font></a>
								&nbsp;&nbsp;
							</td>
						</tr>
					</table>
			</form>
		</TD>
	</TR>
	<TR  STYLE="display:">
								<td   align="right" class="title_2">模板说明：
								<td colspan="3" valign="middle" >
								<font color="#7f9db9"> 
								<ol type="1">
								<li>需要导入的文件格式为Excel2003,即xls;</li>
								<li>Excel2003格式以每列区分，文件只有八列列,即订单号、包装箱号、厂商、供货方式、设备MAC始，设备MAC终、设备序列号;</li>
								<li>其中：订单号、包装箱号、供货方式、设备序列号<font color="red">可以为空</font>,厂商、产品型号、设备MAC始、设备MAC终为<font color="red">必填项</font>;</li>
								<!-- JXDX-ITV-REQ-20170622-WUWF-001(ITV终端管理平台MAC批量导入设备厂家规则去除) -->
								<!-- <li>厂商的选择范围（华为、中兴、创维、海信、长虹、UT、乐视、文广、其他）;</li> -->
								<li>设备MAC地址为6位16进制数,导入时需将分号去掉,格式为：000A29FAEC7D;</li>
								<li>文件的行数最多不超过2万行,如超过2万行，只解析前2万行数据;</li>
								</ol>
								</font></td>
						
	</TR>
	
	<!-- 添加一个隐藏的占位栏，保持界面风格一致 -->
	<TR class="right">
								<td colspan="4" align="right" CLASS="green_foot">&nbsp;</td>
	</TR>
	<tr id="tr00" class="right" style="display: none" >
	</TR>
	<TR id="tr01" style="display: none" class="right">
								<td align="right" class="title_2"  width="10%">导入结果：</td>
								<td id="td02" colspan="3" height="20" width="90%"><SPAN id="actLabel"></SPAN></td>
	</TR>
</TABLE>
<br>
</body>
<br>
<br>
