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
<title>批量参数配置</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<%@ include file="../../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
	$(function() {
		var retResult = '<s:property value="excuteResult" />';

		if ("" != retResult && null != retResult) {
			document.getElementById("actLabel").innerHTML = "<font color='red'> "
					+ retResult + "</font>";
			$("tr[@id='tr00']").css("display", "none");
			$("tr[@id='tr01']").css("display", "");
		}
	});

	// 下载模板
	function getExcelTemplate() {
		$("form[@name='importFrm']").attr("action",
				"batchModifyMGCIP!download.action");
		$("form[@name='importFrm']").submit();
	}

	function doUpload() {
		// 隐藏
		$("tr[@id='tr01']").css("display", "none");

		var filePath = document.importFrm.file.value;
		if (filePath.replace(/(^\s*)|(\s*$)/g, "") == "") {
			alert("请先选择文件！");
			$("input[@name='file']").focus();
			return false;
		}

		var filet = "";

		filet = filePath.split(".");
		if (filet.length < 2) {
			alert("浏览的文件格式不正确！");
			$("input[@name='file']").focus();
			return false;
		}

		if ("xls" != filet[filet.length - 1]
				&& "XLS" != filet[filet.length - 1]) {
			alert("只支持Excel2003或者Excel2007文件");
			$("input[@name='file']").focus();
			return false;
		}

		document.importFrm.upload.disabled = true;
		$("tr[@id='tr00']").css("display", "");

		$("form[@name='importFrm']").attr("action",
				"batchModifyMGCIP!uploadAndParse.action");
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
										批量业务下发</td>
									<td nowrap><img src="../../images/attention_2.gif"
										width="15" height="12">
										&nbsp;根据excel表格提供的用户业务数据，批量修改并下发到终端。</td>
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
												<TH colspan="4" align="center">导入需要修改的用户业务数据</TH>
											</TR>
											<tr id="importUserQuery" bgcolor="#FFFFFF" style="display:">
												<td width="10%" align="right">提交文件：</td>
												<td colspan="3">

													<form name="importFrm"
														action=""
														method="POST" enctype="multipart/form-data">
														<table>
															<tr>
																<td><input type="file" size="60" name="file" /> <input
																	type="button" value=" 提 交 " class=jianbian
																	name="upload" onclick="doUpload()" /> &nbsp;&nbsp; <a
																	href="javascript:void(0);"
																	onClick="getExcelTemplate();"><font color='red'>点击下载模板</font></a>
																	&nbsp;&nbsp;</td>
															</tr>
														</table>
													</form>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td align="right">注意事项：
												<td colspan="3"><font color="#7f9db9">
														1、需要导入的文件格式为Excel2003/2007文件，即xls格式 。 <br>
														2、此功能谨慎使用，操作后不可恢复。 <br> 3、文件的行数最多不超过1000行。 <br>
												</font></td>
											</tr>
											<!-- 添加一个隐藏的占位栏，保持界面风格一致 -->
											<tr bgcolor="#FFFFFF">
												<td colspan="4" align="right" CLASS="green_foot">&nbsp;</td>
											</tr>
											<tr id="tr00" bgcolor="#FFFFFF" style="display: none">
												<td colspan="4">正在上传解析，请稍等....</td>
											</tr>
											<tr id="tr01" style="display: none" bgcolor="#FFFFFF">
												<td align="right">导入结果：</td>
												<td id="td02" colspan="3" height="20"><SPAN
													id="actLabel"></SPAN></td>
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