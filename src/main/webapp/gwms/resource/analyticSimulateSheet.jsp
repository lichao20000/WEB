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
<title>����ģ�ⷢ��BSS����</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<%@ include file="../../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
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
	
	var username=document.importFrm.file.value;
	if(username.replace(/(^\s*)|(\s*$)/g, "") == ""){
		alert("����ѡ���ļ���");
		return false;
	}
	//��ť���µ�ʱ��Ѱ�ť�û� add by zhangcong 2011-06-07
	document.importFrm.upload.disabled = true;
	//parent.idWait.innerHTML = "�����ϴ�����......";
	//idWait.innerHTML = "�����ϴ�����......";
	document.importFrm.submit();
}



function getExcelTemplate(){


//var mainForm = document.getElementById("form");
//mainForm.action="/itms/gwms/resource/analyticSimulateSheetACT!getExcelTemplate.action"
//mainForm.submit();
//mainForm.action="/itms/gwms/resource/analyticSimulateSheetACT!analyticSimulateSheet.action"

	$("form[@name='importFrm']").attr("action","analyticSimulateSheetACT!downloadTemplate.action");
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
						����ģ�ⷢ��BSS����</td>
						<td nowrap><img src="../../images/attention_2.gif" width="15"
							height="12"> &nbsp;����ģ�ⷢ��BSS����</td>
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
								<TH colspan="4" align="center">����ģ�ⷢ��BSS����</TH>
							</TR>
							<tr id="importUserQuery" bgcolor="#FFFFFF" style="display: ">
								<td align="right">�ύ�ļ���</td>
								<td colspan="3">
								<form name="importFrm" id="form"
									action="analyticSimulateSheetACT!analyticSimulateSheet.action"
									method="POST" enctype="multipart/form-data">
								<table>
									<tr>
										<td><input type="file" size="60" name="file" /> <!-- <input type="button" value="˵ ��" name="filedesc" onclick="parent.showDesc()" /> -->
										<input type="button" value=" �� ��  " class=jianbian
											name="upload" onclick="doUpload()" /></td>
										<td><input type="hidden" name="str" value="ssss" /></td>
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
								<td colspan="4"><INPUT TYPE="button" value="�������ģ��"
									class=jianbian onclick="getExcelTemplate();">
							</tr>
							<!-- ���һ�����ص�ռλ�������ֽ�����һ�� -->
							<tr id="space" style="display: " bgcolor="#FFFFFF">
								<td colspan="4" align="right" CLASS="green_foot">&nbsp;</td>
							</tr>
							<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<th colspan="4" align="center" id="1">�ļ��������</th>
							</tr>
							<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<td align="right">���Ϲ���ʧ�ܵ�LOID</td>
								<td colspan="3"><s:property value="inforSheetStr" /></td>
							</tr>
							<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<td align="right">��������ʧ�ܵ�LOID</td>
								<td colspan="3"><s:property value="netSheetStr" /></td>
							</tr>
							<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<td align="right">ITV����ʧ�ܵ�LOID</td>
								<td colspan="3"><s:property value="itvSheetStr" /></td>
							</tr>
							<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<td align="right">VOIPʧ�ܵ�LOID</td>
								<td colspan="3"><s:property value="voipSheetStr" /></td>
							</tr>
							<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<td align="right">�ɹ�������</td>
								<td colspan="3"><s:property value="successNum" /></td>
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