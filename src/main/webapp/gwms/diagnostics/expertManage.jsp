<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
<title> IPTV�޷�ʹ����� </title>
<script type="text/javascript">
<!--//
function editExpert(_id, _faultDesc, _suggest){
	$("input[@name='expertId']").val(_id);
	$("input[@name='faultDesc']").val(_faultDesc);
	$("input[@name='suggest']").val(_suggest);
}

function saveCheck(){
	var _id = $("input[@name='expertId']").val();
	var _faultDesc = $("input[@name='faultDesc']").val();
	var _suggest = $("input[@name='suggest']").val();

	if(false == IsNull(_id,"ר�ҽ���ID")){
		return false;
	}
	var diagUrl = '<s:url value="/gwms/diagnostics/expertManage!updateExpert.action"/>';
	//��ѯ
	$.post(diagUrl,{
		expertId:_id,
		faultDesc:encodeURIComponent(_faultDesc),
		suggest:encodeURIComponent(_suggest)
	},function(ajaxMesg){
		alert(ajaxMesg);
	});
}
//-->
</script>
</head>
<body>
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD height="20">
	</TD>
</TR>
<TR>
	<TD valign=top>
	<FORM NAME="frm" ACTION="">
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							ר�ҽ���
						</td>
						<td>
							<img src="../../images/attention_2.gif" width="15" height="12">
								�鿴������ר�ҽ���
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<table border="0" cellspacing="1" cellpadding="2" id="myTable" width="100%">
					<tr>
						<th width="10%">ר�ҽ���ID</th>
						<th width="25%">��������</th>
						<th width="25%">ר�ҽ���</th>
						<th width="30%">˵��</th>
						<th width="10%">����</th>
					</tr>
						<s:iterator value="expertList">
							<tr>
								<td class=column align="center">
									<s:property value="id" escapeHtml="false"/>
								</td>
								<td class="column" >
									<s:property value="ex_fault_desc" />
								</td>
								<td class="column" >
									<s:property value="ex_suggest" />
								</td>
								<td class="column" >
									<s:property value="ex_desc" escapeHtml="false"/>
								</td>
								<td class="column" align="center">
									<a href="#foot" onclick="editExpert('<s:property value="id"/>',
									'<s:property value="ex_fault_desc"/>','<s:property value="ex_suggest"/>')">�༭</a>
								</td>
							</tr>
						</s:iterator>
				</table>
			</TD>
		</TR>
		<TR>
			<TD>
				<br>
			</TD>
		</TR>
		<TR>
			<TD bgcolor=#999999>
				<table border="0" cellspacing="1" cellpadding="2" id="myTable" width="100%">
					<tr>
						<th colspan="2">ר�ҽ���༭</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td width="25%" align="right">ר�ҽ���ID</td>
						<td width="75%" class=column>
							<input type="text" name="expertId" readonly size="15">
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td align="right">��������</td>
						<td class=column>
							<input type="text" name="faultDesc" size="80">
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td align="right">ר�ҽ���</td>
						<td class=column>
							<input type="text" name="suggest" size="80">
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan="2" align="right">
							<input type="button" value="�� ��" name="foot" onclick="saveCheck()">&nbsp;&nbsp;
						</td>
					</tr>
				</table>
			</TD>
		</TR>
	</TABLE>
	</FORM>
	</TD>
</TR>
</TABLE>
</body>
</html>
