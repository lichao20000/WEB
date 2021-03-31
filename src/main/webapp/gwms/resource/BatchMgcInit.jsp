<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
	/**
	 * 该jsp的描述信息
	 *
	 * @author wuchao(工号) tel：
	 * @version 1.0
	 * @since 2011-9-21 下午02:42:14
	 * 
	 * <br>版权：南京联创科技 网管科技部
	 * 
	 */
 --%>
<html>
<head>
<title>批量MGC管理</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<%@ include file="../../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
	$(function() {
		var isResult = '<s:property value="isResult" />';
		if (isResult == '1') {
			$("tr[@name='tr_result']").show();
		} else {
			$("tr[@name='tr_result']").hide();
		}
	});

	function doUpload() {
		var username = document.importFrm.file.value;
		if (!username) {
			alert("请先选择文件！");
			return false;
		}
		//按钮按下的时候把按钮置灰
		document.importFrm.upload.disabled = true;
		$("form[@name='importFrm']").attr("action",
				"batchMgcACT!doUpload.action");
		document.importFrm.submit();
	}

	function getExcelTemplate() {
		$("form[@name='importFrm']").attr("action",
				"batchMgcACT!downloadTemplate.action");
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
										批量MGC管理</td>
									<td nowrap><img src="../../images/attention_2.gif"
										width="15" height="12"> &nbsp;批量MGC管理</td>
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
												<TH colspan="4" align="center">批量MGC管理</TH>
											</TR>
											<tr id="importUserQuery" bgcolor="#FFFFFF">
												<td align="right">提交文件：</td>
												<td colspan="3">
													<form name="importFrm" id="form"
														action="batchMgcACT!doUpload.action" method="POST"
														enctype="multipart/form-data">
														<table>
															<tr>
																<td><input type="file" size="60" name="file" /> <input
																	type="button" value=" 提 交  " class=jianbian
																	name="upload" onclick="doUpload()" /></td>
															</tr>
														</table>
													</form>
												</td>
											</tr>
											<tr id="importUserQuery2" bgcolor="#FFFFFF">
												<td align="right">注意事项：
												<td colspan="3"><font color="#7f9db9">
														1、需要导入的文件格式为Excel。 <br> 2、文件只能为一页。<br>
														3、为了考虑程序及时响应，建议文件不超过100行<br>
												</font></td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td colspan="4"><INPUT TYPE="button" value="点击下载模版"
													class=jianbian onclick="getExcelTemplate();">
												</td>
											</tr>
										</table>
										<s:if test="failedList != null && failedList.size() == 0">
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<font color="red">文件导入成功</font>
												</td>
											</tr>
										</s:if>
										<s:if test="failedList != null && failedList.size() > 0">
											<table border=0 cellspacing=1 cellpadding=2 width="100%">
												<tr bgcolor="#FFFFFF">
													<th align="center">错误行</th>
													<th align="center">逻辑ID</th>
													<th align="center">错误原因</th>
												</tr>
												<s:iterator value="failedList">
													<tr bgcolor="#FFFFFF">
														<td><s:property value="failedLine" /></td>
														<td><s:property value="username" /></td>
														<td><s:property value="failedCause" /></td>
													</tr>
												</s:iterator>
											</table>
										</s:if>
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