<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<title>�������������·�</title>
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
		alert("����ѡ���ļ���");
		return false;
	}
	//��ť���µ�ʱ��Ѱ�ť�û� add by zhangcong 2011-06-07
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
						�������������·�
						</td>
						<td nowrap>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"height="12"> &nbsp;�������������·�
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
							<TH colspan="4" align="center">����������������</TH>
						</TR>
						<tr id="importUserQuery" bgcolor="#FFFFFF" style="display: ">
							<td align="right">�ύ�ļ���</td>
							<td colspan="3">
							<form name="importFrm" id="form"
								action=""
								method="POST" enctype="multipart/form-data">
							<table>
								<tr>
								<td>
								<input type="file" size="60" name="file" />
								<input type="button" value=" �� ��  " class=jianbian name="upload" onclick="doUpload()" />
								</td>
								<td></td>
								</tr>
							</table>
							</form>
							</td>
						</tr>
						<tr id="importUserQuery2" style="display: " bgcolor="#FFFFFF">
							<td align="right">ע�����
							<td colspan="3"><font color="#7f9db9">
							1����Ҫ������ļ���ʽΪExcel�� <br>
							2���ļ�ֻ��Ϊһҳ��<br>
							3���ļ�������������100��,�糬��100�У�ֻ����ǰ100�����ݡ�<br>
							</font></td>
						</tr>
						<tr style="display: " bgcolor="#FFFFFF">
							<td colspan="4">
							<INPUT TYPE="button" value="�������ģ��" class=jianbian onclick="getExcelTemplate();">
							</td>
						</tr>
						<!-- ���һ�����ص�ռλ�������ֽ�����һ�� -->
						<tr id="space" style="display: " bgcolor="#FFFFFF">
							<td colspan="4" align="right" CLASS="green_foot">&nbsp;</td>
						</tr>
						<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
							<th colspan="4" align="center" id="1">�ļ��������</th>
						</tr>
						<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
							<td align="right">�·������ɹ���</td>
							<td colspan="3"><s:property value="successNum" /></td>
						</tr>
						<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
							<td align="right">�·�����ʧ����</td>
							<td colspan="3"><s:property value="failNum" /></td>
						</tr>
						<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
							<td align="right">���ݸ�ʽ������</td>
							<td colspan="3"><s:property value="formatErrorNum" /></td>
						</tr>
						<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
							<td align="right">�ܼ�¼��</td>
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