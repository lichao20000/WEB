<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
	/**
	 * ��jsp��������Ϣ
	 *
	 * @author wuchao(����) tel��
	 * @version 1.0
	 * @since 2011-9-21 ����02:42:14
	 * 
	 * <br>��Ȩ���Ͼ������Ƽ� ���ܿƼ���
	 * 
	 */
 --%>
<html>
<head>
<title>����MGC����</title>
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
			alert("����ѡ���ļ���");
			return false;
		}
		//��ť���µ�ʱ��Ѱ�ť�û�
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
										����MGC����</td>
									<td nowrap><img src="../../images/attention_2.gif"
										width="15" height="12"> &nbsp;����MGC����</td>
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
												<TH colspan="4" align="center">����MGC����</TH>
											</TR>
											<tr id="importUserQuery" bgcolor="#FFFFFF">
												<td align="right">�ύ�ļ���</td>
												<td colspan="3">
													<form name="importFrm" id="form"
														action="batchMgcACT!doUpload.action" method="POST"
														enctype="multipart/form-data">
														<table>
															<tr>
																<td><input type="file" size="60" name="file" /> <input
																	type="button" value=" �� ��  " class=jianbian
																	name="upload" onclick="doUpload()" /></td>
															</tr>
														</table>
													</form>
												</td>
											</tr>
											<tr id="importUserQuery2" bgcolor="#FFFFFF">
												<td align="right">ע�����
												<td colspan="3"><font color="#7f9db9">
														1����Ҫ������ļ���ʽΪExcel�� <br> 2���ļ�ֻ��Ϊһҳ��<br>
														3��Ϊ�˿��ǳ���ʱ��Ӧ�������ļ�������100��<br>
												</font></td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td colspan="4"><INPUT TYPE="button" value="�������ģ��"
													class=jianbian onclick="getExcelTemplate();">
												</td>
											</tr>
										</table>
										<s:if test="failedList != null && failedList.size() == 0">
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<font color="red">�ļ�����ɹ�</font>
												</td>
											</tr>
										</s:if>
										<s:if test="failedList != null && failedList.size() > 0">
											<table border=0 cellspacing=1 cellpadding=2 width="100%">
												<tr bgcolor="#FFFFFF">
													<th align="center">������</th>
													<th align="center">�߼�ID</th>
													<th align="center">����ԭ��</th>
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