<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
	/**
	 * ��jsp��������Ϣ
	 *
	 * @author zhangchy(����) tel��
	 * @version 1.0
	 * @since 2012-9-18 ����02:42:14
	 * 
	 * <br>��Ȩ���Ͼ������Ƽ� ���ܿƼ���
	 * 
	 */
 --%>
<html>
<head>
<title>������������</title>
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

	// ����ģ��
	function getExcelTemplate() {
		$("form[@name='importFrm']").attr("action",
				"batchModifyMGCIP!download.action");
		$("form[@name='importFrm']").submit();
	}

	function doUpload() {
		// ����
		$("tr[@id='tr01']").css("display", "none");

		var filePath = document.importFrm.file.value;
		if (filePath.replace(/(^\s*)|(\s*$)/g, "") == "") {
			alert("����ѡ���ļ���");
			$("input[@name='file']").focus();
			return false;
		}

		var filet = "";

		filet = filePath.split(".");
		if (filet.length < 2) {
			alert("������ļ���ʽ����ȷ��");
			$("input[@name='file']").focus();
			return false;
		}

		if ("xls" != filet[filet.length - 1]
				&& "XLS" != filet[filet.length - 1]) {
			alert("ֻ֧��Excel2003����Excel2007�ļ�");
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
										����ҵ���·�</td>
									<td nowrap><img src="../../images/attention_2.gif"
										width="15" height="12">
										&nbsp;����excel����ṩ���û�ҵ�����ݣ������޸Ĳ��·����նˡ�</td>
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
												<TH colspan="4" align="center">������Ҫ�޸ĵ��û�ҵ������</TH>
											</TR>
											<tr id="importUserQuery" bgcolor="#FFFFFF" style="display:">
												<td width="10%" align="right">�ύ�ļ���</td>
												<td colspan="3">

													<form name="importFrm"
														action=""
														method="POST" enctype="multipart/form-data">
														<table>
															<tr>
																<td><input type="file" size="60" name="file" /> <input
																	type="button" value=" �� �� " class=jianbian
																	name="upload" onclick="doUpload()" /> &nbsp;&nbsp; <a
																	href="javascript:void(0);"
																	onClick="getExcelTemplate();"><font color='red'>�������ģ��</font></a>
																	&nbsp;&nbsp;</td>
															</tr>
														</table>
													</form>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td align="right">ע�����
												<td colspan="3"><font color="#7f9db9">
														1����Ҫ������ļ���ʽΪExcel2003/2007�ļ�����xls��ʽ �� <br>
														2���˹��ܽ���ʹ�ã������󲻿ɻָ��� <br> 3���ļ���������಻����1000�С� <br>
												</font></td>
											</tr>
											<!-- ���һ�����ص�ռλ�������ֽ�����һ�� -->
											<tr bgcolor="#FFFFFF">
												<td colspan="4" align="right" CLASS="green_foot">&nbsp;</td>
											</tr>
											<tr id="tr00" bgcolor="#FFFFFF" style="display: none">
												<td colspan="4">�����ϴ����������Ե�....</td>
											</tr>
											<tr id="tr01" style="display: none" bgcolor="#FFFFFF">
												<td align="right">��������</td>
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