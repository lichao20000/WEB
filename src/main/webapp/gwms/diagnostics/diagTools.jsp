<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../timelater.jsp"%>
<%
request.setCharacterEncoding("GBK");
String InstArea = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<html>
	<head>
		<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
		<title>诊断工具</title>
<script type="text/javascript">
<!--//

//业务类型，上网
var serv_type_id = "10";
var device_id = '<s:property value="deviceId"/>';
var user_id = '<s:property value="userId"/>';
var gw_type= '<s:property value="gw_type"/>' ;
var InstArea="<%=InstArea%>";

//pingDNS地址
function ping(){
	var ping_addr = $("input[@name='pingAddr']").val();
	var package_size = $("input[@name='packageSize']").val();
	var ping_times = $("input[@name='pingTimes']").val();
	if(false == IsNull(ping_addr,"ping目的地址")){
		return false;
	}
	if(false == IsNull(package_size,"包大小")){
		return false;
	}
	if(false == IsNumber(package_size,"包大小")){
		return false;
	}
	if(false == IsNull(ping_times,"ping次数")){
		return false;
	}
	if(false == IsNumber(ping_times,"ping次数")){
		return false;
	}
	if(ping_times > 10){
		alert("ping次数不能超过20");
		return false;
	}
	parent.block();
	$("div[@id='divPing']").html("");
	var diagUrl = '<s:url value="/gwms/diagnostics/diagTools!ping.action"/>';
	//查询
	$.post(diagUrl,{
		deviceId:device_id,
		pingAddr:ping_addr,
		packageSize:package_size,
		gw_type:gw_type,
		times:ping_times
	},function(ajaxMesg){
		//alert(ajaxMesg);
		$("div[@id='divPing']").append(ajaxMesg);
		parent.dyniframesize();
		parent.unblock();
	});
}

//重启
function reboot(){
	parent.block();
	$("div[@id='divReboot']").html("");
	var diagUrl = '<s:url value="/gwms/diagnostics/diagTools!reboot.action"/>';
	//查询
	$.post(diagUrl,{
		deviceId:device_id,
		gw_type:gw_type
	},function(ajaxMesg){
		//alert(ajaxMesg);
		$("div[@id='divReboot']").append(ajaxMesg);
		parent.dyniframesize();
		parent.unblock();
	});
}

//恢复出厂设置
function factoryReset(){
	parent.block();
	$("div[@id='divReset']").html("");
	var diagUrl = '<s:url value="/gwms/diagnostics/diagTools!factoryReset.action"/>';
	//查询
	$.post(diagUrl,{
		deviceId:device_id,
		gw_type:gw_type
	},function(ajaxMesg){
		//alert(ajaxMesg);
		$("div[@id='divReset']").append(ajaxMesg);
		parent.dyniframesize();
		parent.unblock();
	});
}

//上传日志
function upLogFile(obj){
	parent.block();
	$("div[@id='divUpLogFile']").html("");
	var diagUrl = '<s:url value="/gwms/diagnostics/diagTools!upLogFile.action"/>';
	var link = '<s:url value="/NetCutover/WorkSheetView_device.jsp"/>'
	var ajx = '';
	$.post(diagUrl,{
		deviceId:device_id,
		gw_type:gw_type
	},function(ajaxMesg){
		//alert(ajaxMesg);
		if('1' == ajaxMesg){
			ajx = " 调用成功, <a href='" + link + "' target='_blank'>查看工单执行情况</a>";
		}else{
			ajx = " <font color=red>调用后台失败</font>";
		}
		$("div[@id='divUpLogFile']").append(ajx);
		parent.dyniframesize();
		parent.unblock();
	});
}

//码流开启
function openStream(){
	var diagUrl = '<s:url value="/gwms/diagnostics/diagTools!openStream.action"/>';
	$.post(diagUrl,{
		deviceId:device_id,
		gw_type:gw_type
	},function(ajaxMesg){
		alert(ajaxMesg);
	});
}

//码流关闭
function closeStream(){
	var diagUrl = '<s:url value="/gwms/diagnostics/diagTools!closeStream.action"/>';
	$.post(diagUrl,{
		deviceId:device_id,
		gw_type:gw_type
	},function(ajaxMesg){
		alert(ajaxMesg);
	});
}

//清除交互内容
function clearStream(){
	var diagUrl = '<s:url value="/gwms/diagnostics/diagTools!clearStream.action"/>';
	$.post(diagUrl,{
		deviceId:device_id
	},function(ajaxMesg){
		alert(ajaxMesg);
	});
}

//查看交互内容
function showInterStream(){
	var page = '<s:url value="/gwms/diagnostics/diagTools!showInterStream.action"/>';
	page += "?deviceId=" + device_id;
	var height = 600;
	var width = 800;
	var left = (screen.width-width)/2;
	var top  = (screen.height-height)/2;
	var w = window.open(page,"ss","left="+left+",top="+top+",width="+width+",height="+height+",resizable=yes,scrollbars=yes,toolbar=no");
}

//查看历史ping信息
function showPing(){
	var page = '<s:url value="/gwms/report/pingResult.action"/>';
	page += "?deviceId=" + device_id;
	var height = 600;
	var width = 800;
	var left = (screen.width-width)/2;
	var top  = (screen.height-height)/2;
	var w = window.open(page,"ss","left="+left+",top="+top+",width="+width+",height="+height+",resizable=yes,scrollbars=yes,toolbar=no");

}
//-->
</script>
	</head>
	<body>
		<form name="frm" action="diagTools">
			<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
				align="center">
				<TR>
					<TD bgcolor=#999999>
						<table border=0 cellspacing=1 cellpadding=1 width="100%"
							id="myTable">
							<%
							if(!"sx_lt".equals(InstArea)){
							%>
							<tr CLASS="green_title" height=25>
								<td width="20%">
									PING测试
								</td>
								<td align="right">
									<input type="button" value="历史ping操作查看" onclick="showPing()">
									&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;
									<input type="button" value="执 行" onclick="ping()">
									&nbsp;
								</td>
							</tr>
							<%}%>
							<%
							if(!"sx_lt".equals(InstArea)){
							%>
							<tr bgcolor=#ffffff height=25>
								<td colspan="2" bgcolor=#999999>
									<table width="100%" border=0 cellspacing=0 cellpadding=0
										align="center">
										<tr bgcolor=#ffffff>
											<td class=column align="right" width="20%">
												目的地址&nbsp;
											</td>
											<td>
												&nbsp;
												<input type="text" name="pingAddr">
												&nbsp;&nbsp;
												<font color="red">域名或IP</font>
											</td>
											<td class=column align="right">
												包大小&nbsp;
											</td>
											<td>
												&nbsp;
												<input type="text" name="packageSize" value="200" size=15>
											</td>
											<td class=column align="right">
												次数&nbsp;
											</td>
											<td>
												&nbsp;
												<input type="text" name="pingTimes" value="5" size=15>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr height=25 bgcolor=#ffffff>
								<td class=column align="right">
									PING测试结果&nbsp;
								</td>
								<td bgcolor=#ffffff>
									&nbsp;
									<div id="divPing"></div>
								</td>
							</tr>
							<tr CLASS="" height=25 bgcolor=#ffffff>
								<td>
									远程重启
								</td>
								<td align="right">
									<input type="button" value="执 行" onclick="reboot()">
									&nbsp;
								</td>
							</tr>
							<tr height=25 bgcolor=#ffffff>
								<td class=column align="right">
									远程重启结果&nbsp;
								</td>
								<td bgcolor=#ffffff>
									&nbsp;
									<div id="divReboot"></div>
								</td>
							</tr>

							<tr CLASS="" height=25 bgcolor=#ffffff>
								<td>
									恢复出厂设置
								</td>
								<td align="right">
									<input type="button" value="执 行" onclick="factoryReset()">
									&nbsp;
								</td>
							</tr>
							<tr height=25 bgcolor=#ffffff>
								<td class=column align="right">
									恢复出厂设置结果&nbsp;
								</td>
								<td bgcolor=#ffffff>
									&nbsp;
									<div id="divReset"></div>
								</td>
							</tr>

							<tr CLASS="" height=25 bgcolor=#ffffff>
								<td>
									上传日志文件
								</td>
								<td align="right">
									<input type="button" value="执 行" onclick="upLogFile()">
									&nbsp;
								</td>
							</tr>
							<tr height=25 bgcolor=#ffffff>
								<td class=column align="right">
									上传日志文件结果&nbsp;
								</td>
								<td bgcolor=#ffffff>
									&nbsp;
									<div id="divUpLogFile"></div>
								</td>
							</tr>
							<%}%>

							<tr CLASS="" height=25 bgcolor=#ffffff>
								<td>
									码流分析工具
								</td>
								<td align="right">
									<input type="button" value="开 启" onclick="openStream()">
									&nbsp;&nbsp;
									<input type="button" value="关 闭" onclick="closeStream()">
									&nbsp;&nbsp;
								</td>
							</tr>
							<tr height=25 bgcolor=#ffffff>
								<td align="right" class=column>
									码流分析结果&nbsp;
								</td>
								<td>
									<input type="button" onclick="showInterStream()"
										value="查看ACS与设备交互内容">
									&nbsp;&nbsp;
									<input type="button" onclick="clearStream()" value="清除记录">
									<!-- &nbsp&nbsp<a href='#' onclick='showInterStream()'>查看ACS与设备交互内容</a>
							&nbsp&nbsp&nbsp&nbsp<a href='#' onclick='clearStream()'>清除记录</a> -->
								</td>
							</tr>

						</table>
					</TD>
				</TR>
			</TABLE>
		</form>
	</body>
</html>