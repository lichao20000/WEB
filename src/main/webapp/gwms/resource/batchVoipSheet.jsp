<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<title>批量语音工单下发</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<%@ include file="../../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

$(function(){
	var isResult = "<s:property value="isResult" />";
	if(isResult=='1'){
		$("tr[@name='tr_result']").show();
	}else{
		$("tr[@name='tr_result']").hide();
	}
});



function doUpload(){
	$("form[@name='importFrm']").attr("action", "batchVoipSheetAction!analyticSheet.action");
	var username=document.importFrm.file.value;
	if(username.replace(/(^\s*)|(\s*$)/g, "") == ""){
		alert("请先选择文件！");
		return false;
	}
	//按钮按下的时候把按钮置灰 add by zhangcong 2011-06-07
	document.importFrm.upload.disabled = true;
	document.importFrm.submit();
}



function getExcelTemplate(){
	$("form[@name='importFrm']").attr("action", "batchVoipSheetAction!downloadTemplate.action");
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
		<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite" nowrap>
						批量语音工单下发
						</td>
						<td nowrap>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"height="12"> &nbsp;批量语音工单下发
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>
				<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
					<TR>
					<TD bgcolor=#999999>
					<table border=0 cellspacing=1 cellpadding=2 width="100%" style="table-layout: fixed;">
						<TR>
							<TH colspan="4" align="center">解析批量语音工单</TH>
						</TR>
						<tr id="importUserQuery" bgcolor="#FFFFFF" style="display: ">
							<td align="right">提交文件：</td>
							<td colspan="3">
							<form name="importFrm" id="form"
								action=""
								method="POST" enctype="multipart/form-data">
							<table>
								<tr>
								<td>
								<input type="file" size="60" name="file" />
								<input type="button" value=" 提 交  " class=jianbian name="upload" onclick="doUpload()" />
								</td>
								<td></td>
								</tr>
							</table>
							</form>
							</td>
						</tr>
						<tr id="importUserQuery2" style="display: " bgcolor="#FFFFFF">
							<td align="right">注意事项：
							<td colspan="3"><font color="#7f9db9">
							1、需要导入的文件格式为Excel。 <br>
							2、文件只能为一页。<br>
							3、文件的行数不超过100行,如超过100行，只解析前100行数据。<br>
							</font></td>
						</tr>
						<tr style="display: " bgcolor="#FFFFFF">
							<td colspan="4">
							<INPUT TYPE="button" value="点击下载模版" class=jianbian onclick="getExcelTemplate();">
							</td>
						</tr>
						<!-- 添加一个隐藏的占位栏，保持界面风格一致 -->
						<tr id="space" style="display: " bgcolor="#FFFFFF">
							<td colspan="4" align="right" CLASS="green_foot">&nbsp;</td>
						</tr>
						<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
							<th colspan="4" align="center" id="1">文件解析结果</th>
						</tr>
						<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
							<td align="right">下发工单成功数</td>
							<td colspan="3"><s:property value="successNum" /></td>
						</tr>
						<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
							<td align="right">下发工单失败数</td>
							<td colspan="3"><s:property value="failNum" /></td>
						</tr>
						<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
							<td align="right">数据格式错误数</td>
							<td colspan="3"><s:property value="formatErrorNum" /></td>
						</tr>
						<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
							<td align="right">总记录数</td>
							<td colspan="3"><s:property value="totalNum" /></td>
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
<!--  </form>-->
</body>
<%@ include file="../../foot.jsp"%>
</html>