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
<title>批量导出电话号码</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<%@ include file="../../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

// 下载模板
function getExcelTemplate(){
	$("form[@name='importFrm']").attr("action","queryVoipPhoneByLoid!downloadTemplate.action");
	$("form[@name='importFrm']").submit();
}

function doUpload(){
	
	var filePath = document.importFrm.file.value;
	if(filePath.replace(/(^\s*)|(\s*$)/g, "") == ""){
		alert("请先选择文件！");
		$("input[@name='file']").focus();
		return false;
	}
	
	var filet = "";
	 
	filet = filePath.split(".");
	if(filet.length < 2){
		alert("浏览的文件格式不正确！");
		$("input[@name='file']").focus();
		return false;
	}
	
	if("xls"!=filet[filet.length-1] && "xlsx"!=filet[filet.length-1]){
		alert("只支持Excel文件");
		$("input[@name='file']").focus();
		return false;
	}
	
	document.importFrm.upload.disabled = true;
	//parent.idWait.innerHTML = "正在上传解析......";
	//idWait.innerHTML = "正在上传解析......";
	
	$("form[@name='importFrm']").attr("action", "queryVoipPhoneByLoid!queryVoipPhoneByLoid.action");
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
						VOIP语音号码批量导出</td>
						<td nowrap><img src="../../images/attention_2.gif" width="15"
							height="12"> &nbsp;根据批量LOID导出VOIP语音电话号码，TXT文本中保存的是未能查询到语音电话号码的LOID</td>
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
								<TH colspan="4" align="center">电话号码批量导出</TH>
							</TR>
							<tr id="importUserQuery" bgcolor="#FFFFFF" style="display: ">
								<td width="10%" align="right">提交文件：</td>
								<td colspan="3">

								<form name="importFrm" action="queryVoipPhoneByLoid!queryVoipPhoneByLoid.action"
									method="POST" enctype="multipart/form-data">
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
								<input name="rowNum" type="hidden" value="100"><!-- 只解析Excel的前100行，不包括标题行 -->
								</form>
                              </td>
							</tr>
                            <tr bgcolor="#FFFFFF">
								<td align="right">注意事项：
								<td colspan="3"><font color="#7f9db9">
								1、需要导入的文件格式为Excel。 <br>
								2、文件的第一行为标题行，即Loid。 <br>
								3、文件只有一列。 <br>
								4、文件的行数不超过100行,如超过100行，只解析前100行数据。 <br>
								</font></td>
							</tr>
							<!-- 添加一个隐藏的占位栏，保持界面风格一致 -->
							<tr bgcolor="#FFFFFF">
								<td colspan="4" align="right" CLASS="green_foot">&nbsp;</td>
							</tr>
							<tr id="tr00" bgcolor="#FFFFFF" style="display: none" >
								<td colspan="4" ><%@ include file="/gtms/resource/QueryVoipPhoneByLoidResult.jsp"%></td>
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