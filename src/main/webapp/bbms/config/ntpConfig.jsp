<%--
NTP配置
Author: 翟晓峰
Version: 1.0.0
Date: 2009-10-20
--%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="<s:url value='../../Js/jquery.js'/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.blockUI.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
var deviceId = '<s:property value="deviceId"/>';
//采集NTP
function gatherNtp(){
	parent.block();
	$("div[@id='div_config']").html("");
	$("select[@name='timeZone']").val("");
	$("input[@name='ntpServer1']").val("");
	$("input[@name='ntpServer2']").val("");
	$("span[@id='span_timezone']").html();
	var type = $("input[@name='type'][@checked]").val();
	var url = '<s:url value='/bbms/config/ntpConfig!gatherNtp.action'/>'; 
	$.post(url,{
		deviceId:deviceId,
		configType:type
	},function(ajax){
		//alert(ajax);
		var arrayAjax = ajax.split("|");
		if('-1' == arrayAjax[0]){
			$("div[@id='div_config']").html("<font color=red>&nbsp;获取失败:" + arrayAjax[1] + "</font>");
		}else{
			if('0' == arrayAjax[1]){
				//snmp方式
			}else{
				$("select[@name='timeZone']").val(arrayAjax[1]);
				$("span[@id='span_timezone']").html("采集值：" + arrayAjax[1]);
			}
			$("input[@name='ntpServer1']").val(arrayAjax[2]);
			$("input[@name='ntpServer2']").val(arrayAjax[3]);
			if('1' == arrayAjax[4]){
				$("input[@id='id_status1']").attr("checked", true);
			}else{
				//alert(arrayAjax[4]);
				$("input[@id='id_status2']").attr("checked", true);
			}
			$("div[@id='div_config']").html("<font color=green>&nbsp;获取成功</font>");
		}
		parent.unblock();
	});
}
//配置NTP
function configNtp(){
	
	var type = $("input[@name='type'][@checked]").val();
	//var status = $("input[@name='status'][@checked]").val();
	var status = '1';
	var timeZone = $("select[@name='timeZone']").val();
	var ntpServer1 = $("input[@name='ntpServer1']").val();
	var ntpServer2 = $("input[@name='ntpServer2']").val();

	var url = '<s:url value='/bbms/config/ntpConfig!configNtp.action'/>'; 
	
	if(!IsNull(ntpServer1.trim(), "主NTP服务器地址")){
		return;
	}
	parent.block();
	$("div[@id='div_config']").html("");
	$.post(url,{
		deviceId:deviceId,
		ntpServer1:ntpServer1,
		ntpServer2:ntpServer2,
		timeZone:timeZone,
		enable:status,
		configType:type
	},function(ajax){
		//alert(ajax);
		var arrayAjax = ajax.split("|");
		if('-1' == arrayAjax[0]){
			$("div[@id='div_config']").html("<font color=red>&nbsp;配置失败:" + arrayAjax[1] + "</font>");
		}else{
			$("div[@id='div_config']").html("<font color=green>&nbsp;" + arrayAjax[1] + "</font>");
			var url = "<s:url value='/servStrategy/ServStrategy!getStrategy.action'/>";
			var strategyId = arrayAjax[2];
			$.post(url,{
           		strategyId:strategyId
            },function(ajax){
          	   	$("div[@id='div_strategy']").html("");
				$("div[@id='div_strategy']").append(ajax);
				parent.dyniframesize();
            });			
			document.getElementById("tr002").style.display = "";
		}
		parent.dyniframesize();
		parent.unblock();
	});
}

//改变enable
function changeStatus(enable){
	if('1' == enable.value){
		$("tr[@id='id_ntp1']").show();
		$("tr[@id='id_ntp2']").show();
		$("tr[@id='id_timezone']").show();
	}else{
		$("tr[@id='id_ntp1']").hide();
		$("tr[@id='id_ntp2']").hide();
		$("tr[@id='id_timezone']").hide();
	}
}
//改变配置方式
function chgConfigType(type){
	if('1' == type.value){
		if('1' == $("input[@name='status'][@checked]").val()){
			$("tr[@id='id_timezone']").show();
		}
	}else{
		$("tr[@id='id_timezone']").hide();
	}
}
</SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="###">
				<input type="hidden" name="strDevice" value="">
				<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR>
												<TH colspan="4" bgcolor=#999999>
													NTP/时区配置
												</TH>
											</TR>
											
											<TR bgcolor="#FFFFFF" id="ntp_type" style="display:none">
											    <TD align="right" width="25%" class=column>
													配置方式
												</TD>
												<TD colspan=3>
													<input type="radio" name="type" onclick="chgConfigType(this)" id="rd1" class=btn value="1" checked><label for="rd1">TR069</label>&nbsp;
													
													<input type="radio" name="type" onclick="chgConfigType(this)" id="rd2" class=btn value="2"><label for="rd2">SNMP</label>
													 
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="ntp_status" style="display:none">
											  <TD align="right" class=column>
													服务状态
												</TD>
												<TD colspan=3>
													<input type="radio" name="status" id="id_status1" value="1" checked onclick="changeStatus(this);" class=btn><label for="rd3">开启</label>&nbsp;
													<input type="radio" name="status" id="id_status2" value="0" onclick="changeStatus(this);" class=btn><label for="rd4">关闭</label>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="id_ntp1" >
											    <TD align="right" class=column>
													主NTP服务器地址
												</TD>
												<TD colspan=3>
													<input type="text" name="ntpServer1" class=bk
													size=30 maxlength=50>
													<span id="iscisco"></span>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="id_ntp2" >
											    <TD align="right" class=column>
													备NTP服务器地址
												</TD>
												<TD colspan=3>
													<input type="text" name="ntpServer2" class=bk
													size=30 maxlength=50>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="id_timezone">
											    <TD align="right" class=column>
													时区(不支持SNMP)
												</TD>
												<TD colspan=3>
													<select name="timeZone" class="bk">
														<option value="+08:00" selected>+08:00(Beijing, Chongquing, Hong Kong, Urumqi)</option>
													</select>
													&nbsp;&nbsp;<span id="span_timezone"></span>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<button onclick="configNtp();">设 置</button>&nbsp;&nbsp;
													<button onclick="gatherNtp();">获 取</button>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TH colspan="4">操作结果</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
												<td colspan="4" valign="top" class=column>
												<div id="div_config" style="width: 100%; height: 20px; z-index: 1; top: 100px;">
													
												</div>
												</td>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr002" style="display: none">
												<td colspan="4" valign="top" class=column>
													<div id="div_strategy"
														style="width: 100%; z-index: 1; top: 100px">
													</div>
												</td>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME name="iframe_result" ID=childFrm SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>