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
<title>���������绰����</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<%@ include file="../../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

// ����ģ��
function getExcelTemplate(){
	$("form[@name='importFrm']").attr("action","queryVoipPhoneByLoid!downloadTemplate.action");
	$("form[@name='importFrm']").submit();
}

function doUpload(){
	
	var filePath = document.importFrm.file.value;
	if(filePath.replace(/(^\s*)|(\s*$)/g, "") == ""){
		alert("����ѡ���ļ���");
		$("input[@name='file']").focus();
		return false;
	}
	
	var filet = "";
	 
	filet = filePath.split(".");
	if(filet.length < 2){
		alert("������ļ���ʽ����ȷ��");
		$("input[@name='file']").focus();
		return false;
	}
	
	if("xls"!=filet[filet.length-1] && "xlsx"!=filet[filet.length-1]){
		alert("ֻ֧��Excel�ļ�");
		$("input[@name='file']").focus();
		return false;
	}
	
	document.importFrm.upload.disabled = true;
	//parent.idWait.innerHTML = "�����ϴ�����......";
	//idWait.innerHTML = "�����ϴ�����......";
	
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
						VOIP����������������</td>
						<td nowrap><img src="../../images/attention_2.gif" width="15"
							height="12"> &nbsp;��������LOID����VOIP�����绰���룬TXT�ı��б������δ�ܲ�ѯ�������绰�����LOID</td>
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
								<TH colspan="4" align="center">�绰������������</TH>
							</TR>
							<tr id="importUserQuery" bgcolor="#FFFFFF" style="display: ">
								<td width="10%" align="right">�ύ�ļ���</td>
								<td colspan="3">

								<form name="importFrm" action="queryVoipPhoneByLoid!queryVoipPhoneByLoid.action"
									method="POST" enctype="multipart/form-data">
								<table>
									<tr>
										<td>
											<input type="file" size="60" name="file" />
											<input type="button" value=" �� �� " class=jianbian name="upload" onclick="doUpload()" />
											&nbsp;&nbsp;
											<a href="javascript:void(0);" onClick="getExcelTemplate();"><font color='red'>�������ģ��</font></a>
											&nbsp;&nbsp;
										</td>
									</tr>
								</table>
								<input name="rowNum" type="hidden" value="100"><!-- ֻ����Excel��ǰ100�У������������� -->
								</form>
                              </td>
							</tr>
                            <tr bgcolor="#FFFFFF">
								<td align="right">ע�����
								<td colspan="3"><font color="#7f9db9">
								1����Ҫ������ļ���ʽΪExcel�� <br>
								2���ļ��ĵ�һ��Ϊ�����У���Loid�� <br>
								3���ļ�ֻ��һ�С� <br>
								4���ļ�������������100��,�糬��100�У�ֻ����ǰ100�����ݡ� <br>
								</font></td>
							</tr>
							<!-- ���һ�����ص�ռλ�������ֽ�����һ�� -->
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