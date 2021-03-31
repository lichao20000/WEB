<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../timelater.jsp"%>
<html>
	<head>
		<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<title>IAD诊断</title>
		<script type="text/javascript">
<!--//


var device_id = '<s:property value="deviceId"/>';
var gw_type = '1';   // 默认为ITMS

//pingDNS地址
function iadDiag(){
	var iserv = $("select[@name='iserv']").val();
	parent.block();
	$("div[@id='divIAD']").html("");
	var diagUrl = '<s:url value="/gwms/diagnostics/iaddiag!iadDiag.action"/>';
	//查询
	$.post(diagUrl,{
		deviceId:device_id,
		gw_type:gw_type,
		iserv:iserv
	},function(ajax){
		var s = ajax.split(",");
		if(s[0]=="0"){
			$("div[@id='divIAD']").append("<font clor=green>VOIP注册诊断结果:注册成功</font>");
		}else if(s[0]=="1"){
			$("div[@id='divIAD']").append("<font clor=red>VOIP注册诊断结果:注册失败！      失败原因："+s[1]+"!</font>");
		}else if(s[0]=="-1"){
			$("div[@id='divIAD']").append("<font color=red>VOIP注册诊断结果:"+s[1]+"</font>");
		}else{
			$("div[@id='divIAD']").append("<font color=red>VOIP注册诊断结果:诊断失败，返回结果不正确！</font>");
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
									VOIP注册诊断
								</td>
							</tr>
							<tr height=25 bgcolor=#ffffff>
								<td class="column" width="25%" align="right">
									注册服务器：
								</td>
								<td>
									<SELECT name="iserv">
										<option value="1">
											主用服务器
										</option>
										<option value="2">
											备用服务器
										</option>
									</SELECT>
								</td>
							</tr>
							<tr height=25 bgcolor=#ffffff>
								<td align="right" colspan="2">
									<input type="button" value="诊 断" onclick="iadDiag()"
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