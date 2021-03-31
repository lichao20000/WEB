<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
	/**
	 * 该jsp的描述信息
	 *
	 * @author zhangchy(工号) tel：
	 * @version 1.0
	 * @since 2012-9-18 下午02:42:14
	 * 
	 * <br>版权：南京联创科技 网管科技部
	 * 
	 */
 --%>
<html>
<head>
<title>批量导入预置设备</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<%@ include file="../../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>

<%
	String gw_type = request.getParameter("gw_type"); 
%>

<SCRIPT LANGUAGE="JavaScript">

var gw_type = '<%=gw_type%>';

$(function(){
	var retResult = '<s:property value="retResult" />';
	var fileName = '<s:property value="fileName" />';
	
	if("" != retResult){
		if(retResult == '-6'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>文件 "+fileName+" 未能导入，其中包含字段“厂商”为空的记录，请确认！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '-5'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>文件 "+fileName+" 未能导入，其中包含字段“设备型号”为空的记录，请确认！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '-3'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>文件 "+fileName+" 未能导入，其中包含字段“属地”为空的记录，请确认！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '-2'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>文件 "+fileName+" 未能导入，其中包含字段“设备序列”号为空的记录，请确认！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '-1'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>文件 "+fileName+" 未能导入，其中包含字段“OUI”为空的记录，请确认！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '0'){
			alert("导入成功！");
			document.getElementById("actLabel").innerHTML = "<font color='red'>文件 "+fileName+" 导入成功！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '1'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>所导入的文件 "+fileName+" 不符合要求，请按照注意事项上传文件或者点击下载模板</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '2'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>文件 "+fileName+" 中存在非法属地的记录，请确认！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '3'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>您上传的 "+fileName+" 为空，请确认！</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '4'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>文件 "+fileName+" 没有成功上传，请重新上传！</font>";
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

// 下载模板
function getExcelTemplate(){
	$("form[@name='importFrm']").attr("action","importDeviceInit!downloadTemplate.action");
	$("form[@name='importFrm']").submit();
}

function doUpload(){
	// 隐藏
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
		alert("浏览的文件格式不正确！");
		$("input[@name='file']").focus();
		return false;
	}
	
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
	
	fileP = filePath.split("\\");
	$("input[@name='fileName']").val(fileP[fileP.length-1]);
	
	document.importFrm.upload.disabled = true;
	$("tr[@id='tr00']").css("display","");
	
	$("form[@name='importFrm']").attr("action", "importDeviceInit!readUploadFile.action?gw_type="+gw_type);
	$("form[@name='importFrm']").submit();
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
						设备资源导入
						</td>
						<td nowrap><img src="../../images/attention_2.gif" width="15"
							height="12"> &nbsp;为了管控所有新入网的设备，在此界面先导入需要采购的设备，然后设备自动上报时，没有在此导入的，一律不允许入网</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>
				<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD bgcolor=#999999>
						<table border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR>
								<TH colspan="4" align="center">导入需要采购的设备</TH>
							</TR>
							<tr id="importUserQuery" bgcolor="#FFFFFF" style="display: ">
								<td width="10%" align="right">提交文件：</td>
								<td colspan="3">

								<form name="importFrm" action="importDeviceInit!readUploadFile.action"
									method="POST" enctype="multipart/form-data">
									<input type="hidden" name="fileType" value="" >
									<input type="hidden" name="fileName" value="" >
									<input name="rowNum" type="hidden" value="20000"><!-- 只解析Excel的前20000行，不包括标题行 -->
								<table>
									<tr>
										<td>
											<input type="file" size="60" name="file" />
											<input type="button" value=" 提 交 " class=jianbian name="upload" onclick="doUpload()" />
											&nbsp;&nbsp;
											<a href="javascript:void(0);" onClick="getExcelTemplate();"><font color='red'>点击下载模板</font></a>
											&nbsp;&nbsp;
										</td>
									</tr>
								</table>
								</form>
                              </td>
							</tr>
                            <tr bgcolor="#FFFFFF">
								<td align="right">注意事项：
								<td colspan="3"><font color="#7f9db9">
								1、需要导入的文件格式为Excel2003、TXT文本文件，即xls、txt格式 。 <br> 
								2、文件的第一行为标题行，即OUI、设备序列号、属地、购买时间(YYYY-MM-DD)、设备MAC、厂商、设备型号、导入时间、设备串号，其中</font><font color='red'>OUI，设备序列号，厂商，设备型号为必填，其他项可填可不填</font><font color="#7f9db9">。 <br>
								3、Excel2003格式以每列区分，TXT文本格式中间以“|”隔开(不包含双引号)，具体字段分别为：OUI|设备序列号|属地|购买时间(YYYY-MM-DD)|设备MAC|厂商|设备型号|导入时间(YYYY-MM-DD)|设备串号 <br>
								4、文件只有九列，其中OUI，设备序列号，属地，厂商，设备型号为必填，购买时间，设备MAC，导入时间，设备串号可以为空，如果为空，那么分隔符“|”还是需要的，如：1880F5|373001880F5A00272|省中心|||||。 <br>
								5、文件的行数最多不超过2万行,如超过2万行，只解析前2万行数据。 <br>
								</font></td>
							</tr>
							<!-- 添加一个隐藏的占位栏，保持界面风格一致 -->
							<tr bgcolor="#FFFFFF">
								<td colspan="4" align="right" CLASS="green_foot">&nbsp;</td>
							</tr>
							<tr id="tr00" bgcolor="#FFFFFF" style="display: none" >
								<td colspan="4" >正在上传解析，请稍等....</td>
							</tr>
							<tr id="tr01" style="display: none" bgcolor="#FFFFFF">
								<td align="right">导入结果：</td>
								<td id="td02" colspan="3" height="20"><SPAN id="actLabel"></SPAN></td>
							</tr>
						</table>
						</td>
					</TR>
				</table>
				</td>
				<tr>
		</table>
		</TD>
	</TR>
	<tr>
		<td height=20></td>
	</tr>
</TABLE>

</body>
<%@ include file="../../foot.jsp"%>
</html>