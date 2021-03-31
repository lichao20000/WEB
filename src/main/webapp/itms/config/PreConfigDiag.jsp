<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>设备配置检查</title>
		<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
			type="text/css">
		<link rel="stylesheet" href="<s:url value='/css/css_blue.css'/>"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">

		var gw_type = '<%=request.getParameter("gw_type")%>';

		var deviceId="";
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
		$("tr[@id='tr_businessInfo']").hide();
		$("tr[@id='tr_title_businessInfo']").hide();
		$("tr[@id='tr_baseInfo']").show();
	    $("div[@id='div_baseInfo']").html("正在查询，请稍等....");
	    var url = '<s:url value='/itms/config/preConfigDiag!baseInfo.action'/>'; 
		$.post(url,{
			deviceId:deviceId,
			gw_type:gw_type
		},function(ajax){	
		    $("div[@id='div_baseInfo']").html("");
			$("div[@id='div_baseInfo']").append(ajax);
			$("tr[@id='tr_title_businessInfo']").show();
		});
	}
	
	function businessInfo(){
		if(deviceId == ""){
			alert("请先查询设备!");
			return false;
		}
		var onlinestatus1 = $("input[@name='onlinestatus1']").val();
		if(onlinestatus1=="设备与平台能正常交互"){
			$("tr[@id='tr_businessInfo']").show();
		    $("div[@id='div_businessInfo']").html("正在诊断，请稍等....");
		    var url = '<s:url value='/itms/config/preConfigDiag!businessInfo.action'/>'; 
			$.post(url,{
				deviceId:deviceId,
				gw_type:gw_type,
				cityId:cityId
			},function(ajax){	
			    $("div[@id='div_businessInfo']").html("");
				$("div[@id='div_businessInfo']").append(ajax);
			});
		}else{
			$("tr[@id='tr_businessInfo']").show();
			$("div[@id='div_businessInfo']").html("<font color=red>当前设备不在线，请确认设备是否开机，如果设备已开机，请确认网络管理通道是否通畅！</font>");
		}
		
	
	}
	
	function wideNetconfig(accessType){
		var url = "<s:url value='/itms/config/preConfigDiag!wideNetconfig.action'/>";
		$("div[@id='div_wideNetconfig']").html("正在重新下发配置，这个过程可能需要几分钟，请耐心等待....");
			$.post(url,{
				deviceId:deviceId,
				accessType:accessType,
				gw_type:gw_type,
				cityId:cityId
			},function(ajax){
				var s = ajax.split(";");
				if(s[0]=="1"){
					alert(s[1]);
					$("div[@id='div_wideNetconfig']").html("<font color=green>"+s[1]+"</font>");  
				}else if(s[0]=="-1"){
					alert(s[1]);
					$("div[@id='div_wideNetconfig']").html("<font color=red>"+s[1]+"</font>");  
				}else{
					alert(s[1]);
					$("div[@id='div_wideNetconfig']").html("<font color=red>"+s[1]+"</font>");  
				}
			});
	}
	
	
	function iptvconfig(accessType){
		var url = "<s:url value='/itms/config/preConfigDiag!iptvconfig.action'/>";
		$("div[@id='div_iptvconfig']").html("正在重新下发配置，这个过程可能需要几分钟，请耐心等待....");
			$.post(url,{
				deviceId:deviceId,
				accessType:accessType,
				cityId:cityId,
				deviceserialnumber:deviceserialnumber,
				gw_type:gw_type,
				oui:oui
			},function(ajax){
				var s = ajax.split(";");
				if(s[0]=="-1"){
					alert(s[1]);
					$("div[@id='div_iptvconfig']").html("<font color=red>"+s[1]+"</font>");   
				}       
				if(s[0]=="1"){
	  		     	$("div[@id='div_iptvconfig']").html("正在重新下发配置，这个过程可能需要几分钟，请耐心等待....");
					getStrategyById(s[2],'div_iptvconfig');
				}
			});
	}
	
	function voipconfig(accessType){
		var url = "<s:url value='/itms/config/preConfigDiag!voipconfig.action'/>";
		$("div[@id='div_ivoipconfig']").html("正在重新下发配置，这个过程可能需要几分钟，请耐心等待....");
			$.post(url,{
				deviceId:deviceId,
				accessType:accessType,
				cityId:cityId,
				gw_type:gw_type,
				deviceserialnumber:deviceserialnumber
			},function(ajax){
				var s = ajax.split(";");
				if(s[0]=="-1"){
					alert(s[1]);
					$("div[@id='div_voipconfig']").html("<font color=red>"+s[1]+"</font>");   
				}       
				if(s[0]=="1"){
	  		     	$("div[@id='div_voipconfig']").html("正在重新下发配置，这个过程可能需要几分钟，请耐心等待....");
					getHgwcustServById(s[2],'div_voipconfig');
				}
			});
	}
	
	function qosconfig(){
		var url = "<s:url value='/itms/config/preConfigDiag!qosconfig.action'/>";
		$("div[@id='div_qosconfig']").html("正在重新下发配置，这个过程可能需要几分钟，请耐心等待....");
			$.post(url,{
				deviceId:deviceId,
				gw_type:gw_type
			},function(ajax){
				var s = ajax.split(";");
				if(s[0]=="-1"){
					alert(s[1]);
					$("div[@id='div_qosconfig']").html("<font color=red>"+s[1]+"</font>");   
				}       
				if(s[0]=="1"){
	  		     	$("div[@id='div_qosconfig']").html("正在重新下发配置，这个过程可能需要几分钟，请耐心等待....");
					getStrategyById(s[2],'div_qosconfig');
				}
			});
	}
	
	
	function getStrategyById(strategyId,divName){
		var url = "<s:url value='/itms/config/preConfigDiag!getStrategyById.action'/>";
		$.post(url,{
			strategyId:strategyId
		},function(ajax){
			var s = ajax.split(";");
			if(s[0]=="1"){
				alert(s[1]);
				$("div[@id='"+divName+"']").html("<font color=green>"+s[1]+"</font>");  
			}else if(s[0]=="-1"){
				alert(s[1]);
				$("div[@id='"+divName+"']").html("<font color=red>"+s[1]+"</font>");  
			}else{
				//$("div[@id='"+divName+"']").html("<font color=red>"+s[1]+"</font>");  
				setTimeout('getStrategyById("'+strategyId+'","'+divName+'")', 10000);
			}
		});				
	}
	
	function getHgwcustServById(userId,divName){
		var url = "<s:url value='/itms/config/preConfigDiag!getHgwcustServById.action'/>";
		$.post(url,{
			userId:userId
		},function(ajax){
			var s = ajax.split(";");
			if(s[0]=="1"){
				alert(s[1]);
				$("div[@id='"+divName+"']").html("<font color=green>"+s[1]+"</font>");  
			}else if(s[0]=="-1"){
				alert(s[1]);
				$("div[@id='"+divName+"']").html("<font color=red>"+s[1]+"</font>");  
			}else{
				setTimeout('getHgwcustServById("'+userId+'","'+divName+'")', 10000);
			}
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
								设备配置检查
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
								【基本信息】
							</td>
						</tr>
					</table>
				</TD>
			</TR>
			<tr id=tr_baseInfo style="display: none">
				<TD>
					<div id="div_baseInfo" style="width: 100%; z-index: 1; top: 100px">
						正在查询，请稍等....
					</div>
				</td>
			</tr>
			<TR>
				<TD HEIGHT=20>
					&nbsp;
				</TD>
			</TR>
			<TR id=tr_title_businessInfo style="display: none">
				<TD>
					<table width="100%" height="30" border="0" cellspacing="0"
						cellpadding="0" class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								<a href="javascript:businessInfo();" style="CURSOR: hand;">
									【业务预配信息】 </a>
							</td>
						</tr>
					</table>
				</TD>
			</TR>
			<tr id=tr_businessInfo STYLE="display: none">
				<TD>
					<div id="div_businessInfo"
						style="width: 100%; z-index: 1; top: 100px">
						正在诊断，请稍等....
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
<%@ include file="../../foot.jsp"%>