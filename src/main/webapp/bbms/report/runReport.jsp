<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>运行报告</title>
		<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
			type="text/css">
		<link rel="stylesheet" href="<s:url value='/css/css_blue.css'/>"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

		<SCRIPT LANGUAGE="JavaScript">
		var deviceId="";
		var deviceIP="";
		var cityId="";
		var deviceserialnumber = "";
		var oui = "";
	$(function(){
		gwShare_setGaoji();
	});
	
	function CheckForm(){
		if(deviceId == ""){
			alert("请先查询设备!");
			return false;
		}
		return true;
	}
	
	function deviceResult(returnVal){
		
		deviceId = $.trim(returnVal[2][0][0]);
		cityId = $.trim(returnVal[2][0][4]);
		deviceserialnumber = $.trim(returnVal[2][0][2]);
		oui = $.trim(returnVal[2][0][1]);
		$("tr[@id='tr_baseInfo']").show();
	    $("div[@id='div_baseInfo']").html("正在查询，请稍等...");
	    var url = '<s:url value='/bbms/report/runReportQuery!baseInfo.action'/>'; 
		$.post(url,{
			deviceId:deviceId,
			deviceserialnumber:deviceserialnumber
		},function(ajax){	
		    $("div[@id='div_baseInfo']").html("");
			$("div[@id='div_baseInfo']").append(ajax);
			$("tr[@id='tr_title_netConfigInfo']").show();
		});
	}
	function netConfigInfo(){
		if(deviceId == ""){
			alert("请先查询设备!");
			return false;
		}	
		$("tr[@id='tr_netConfigInfo']").show();
	    $("div[@id='div_netConfigInfo']").html("正在查询，请稍等....");
		 var url = '<s:url value='/bbms/report/runReportQuery!queryGatherInfo.action'/>'; 
		 $.post(url,{
		 			deviceId : deviceId
		 		},
			 	function(ajax){
			 	$("div[@id='div_netConfigInfo']").html("");
				$("div[@id='div_netConfigInfo']").append(ajax);
			 });
	}
	
	
	</SCRIPT>

	</head>

	<body>

		<TABLE border=0 align="center" cellspacing=0 cellpadding=0 width="98%">
			<TR>
				<TD HEIGHT=20>
					&nbsp;
				</TD>
			</TR>
			<TR>
				<TD>
					<table width="100%" height="30" border="0" cellpadding="0"
						cellspacing="0" class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
							   	 BBMS运行报告信息
							</td>
							<td class="title_bigwhite">
								<img src="<s:url value="/images/attention_2.gif"/>" width="15"
									height="12" />
							</td>
						</tr>
					</table>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<td colspan="4">
					<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
				</td>
			</TR>
			<TR id=tr_baseInfo style="display: none">
				<TD>
					<table width="100%" height="30" border="0" cellspacing="0"
						cellpadding="0" class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								【网关信息】
							</td>
						</tr>
					</table>
				</TD>
			</TR>
			<tr id=tr_baseInfo style="display: none" bgcolor="#FFFFFF">
				<TD>
					<div id="div_baseInfo" style="width: 100%; z-index: 1; top: 100px">
						正在查询，请稍等....
					</div>
				</td>
			</tr>
				<TR id="tr_title_netConfigInfo" style="display: none">
				<TD>
					<table width="100%" height="30" border="0" cellspacing="0"
						cellpadding="0" class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								<a href="javascript:netConfigInfo();" style="CURSOR: hand;">
									【网关采集信息】 </a>
							</td>
						</tr>
					</table>
				</TD>
			</TR>
			<tr id="tr_netConfigInfo" STYLE="display: none">
				<TD>
					<div id="div_netConfigInfo"
						style="width: 100%; z-index: 1; top: 100px">
						正在查询，请稍等......
					</div>
				</TD>
			</tr>
			<TR>
				<TD HEIGHT=20>
					&nbsp;
				</TD>
			</TR>
		</TABLE>
	</body>
</html>
<%@ include file="../foot.jsp"%>