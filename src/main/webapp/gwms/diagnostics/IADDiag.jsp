<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../timelater.jsp"%>
<html>
	<head>
		<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<title>IAD���</title>
		<script type="text/javascript">
<!--//


var device_id = '<s:property value="deviceId"/>';
var gw_type = '1';   // Ĭ��ΪITMS

//pingDNS��ַ
function iadDiag(){
	var iserv = $("select[@name='iserv']").val();
	parent.block();
	$("div[@id='divIAD']").html("");
	var diagUrl = '<s:url value="/gwms/diagnostics/iaddiag!iadDiag.action"/>';
	//��ѯ
	$.post(diagUrl,{
		deviceId:device_id,
		gw_type:gw_type,
		iserv:iserv
	},function(ajax){
		var s = ajax.split(",");
		if(s[0]=="0"){
			$("div[@id='divIAD']").append("<font clor=green>VOIPע����Ͻ��:ע��ɹ�</font>");
		}else if(s[0]=="1"){
			$("div[@id='divIAD']").append("<font clor=red>VOIPע����Ͻ��:ע��ʧ�ܣ�      ʧ��ԭ��"+s[1]+"!</font>");
		}else if(s[0]=="-1"){
			$("div[@id='divIAD']").append("<font color=red>VOIPע����Ͻ��:"+s[1]+"</font>");
		}else{
			$("div[@id='divIAD']").append("<font color=red>VOIPע����Ͻ��:���ʧ�ܣ����ؽ������ȷ��</font>");
		}
		$("tr[@id='result']").show();
		parent.dyniframesize();
		parent.unblock();
	});
}


//-->
</script>
	</head>
	<body>
		<form name="frm" action="#">
			<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
				align="center">
				<TR>
					<TD bgcolor=#999999>
						<table border=0 cellspacing=1 cellpadding=1 width="100%"
							id="myTable">
							<tr height=25>
								<td class="green_title" colspan="2">
									VOIPע�����
								</td>
							</tr>
							<tr height=25 bgcolor=#ffffff>
								<td class="column" width="25%" align="right">
									ע���������
								</td>
								<td>
									<SELECT name="iserv">
										<option value="1">
											���÷�����
										</option>
										<option value="2">
											���÷�����
										</option>
									</SELECT>
								</td>
							</tr>
							<tr height=25 bgcolor=#ffffff>
								<td align="right" colspan="2">
									<input type="button" value="�� ��" onclick="iadDiag()"
										class="jianbian">
									&nbsp;
								</td>
							</tr>
							<tr height=25 bgcolor=#ffffff style="display: none" id="result">
								<td align="center" colspan="2">
									<div id="divIAD"></div>
								</td>
							</tr>
						</table>
					</TD>
				</TR>
			</TABLE>
		</form>
	</body>
</html>