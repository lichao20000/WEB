<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>


<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/CheckForm.js'/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value='/Js/jsDate/WdatePicker.js'/>"></script>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value='/Js/jquery.blockUI.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
var deviceId = "<s:property value='deviceId'/>";
//parent.unblock();

function change(){
	var manageFlag = $("input[@name='manageFlag'][@checked]").val();
	if(manageFlag=="0"){
		$("tr[@id='manage_url']").show();
		$("tr[@id='manage_acs']").show();
		$("tr[@id='manage_period']").show();
	}
	if(manageFlag=="1"){
		$("tr[@id='manage_url']").hide();
		$("tr[@id='manage_acs']").hide();
		$("tr[@id='manage_period']").show();
	}
	if(manageFlag=="2"){
		$("tr[@id='manage_url']").show();
		$("tr[@id='manage_acs']").hide();
		$("tr[@id='manage_period']").hide();
	}
	if(manageFlag=="3"){
		$("tr[@id='manage_url']").show();
		$("tr[@id='manage_acs']").show();
		$("tr[@id='manage_period']").hide();
	}

}

function getStatus() {
	$("tr[@id='tr002']").hide();
	document.all("div_config").style.display = ""; 
	$("div[@id='div_config']").css("background-color","#33CC00");	
	document.all("div_config").innerHTML = "<font size=2>正在获取FTP服务信息，请等待...</font>";
	//parent.block();
	var url = "<s:url value='/bbms/config/tr069Config!getTR069.action'/>";
	$.post(url,{
		deviceId:deviceId
	},function(ajax){
	var s=ajax.split(";");
	document.all("div_config").style.display = "";
	if (s[0]!="-1") { 
		$("input[@name='acsurl']").val(s[1]);
		$("input[@name='userName']").val(s[2]);
		$("input[@name='password']").val(s[3]);
        $("input[@name='cpeUsername']").val(s[4]);   
        $("input[@name='cpePasswd']").val(s[5]);    
        if(s[6]=="1"){
			$("input[@name='periodicInformEnable'][@value=1]").attr("checked",true);
		}
		if(s[6]=="0"){
			$("input[@name='periodicInformEnable'][@value=0]").attr("checked",true);
		}
        $("input[@name='periodicInformInterval']").val(s[7]);               
		//parent.dyniframesize();
		//parent.unblock(); 
	}else{
		$("div[@id='div_config']").css("background-color","red");
		document.all("div_config").innerHTML = "<font size=2>获取TR069信息失败!"+s[1]+"</font>";
		//parent.dyniframesize();
		//parent.unblock();
	}                     
	});

    setTimeout("clearResult()", 5000); 		    
}

function ExecMod(){
	if(checkForm() == false) return false;
	//parent.block();
	var manageFlag = $("input[@name='manageFlag'][@checked]").val();
    var acsurl = $.trim($("input[@name='acsurl']").val());
    var userName = $.trim($("input[@name='userName']").val());
    var password = $.trim($("input[@name='password']").val());
    var cpeUsername = $.trim($("input[@name='cpeUsername']").val());
    var cpePasswd = $.trim($("input[@name='cpePasswd']").val());
    var periodicInformEnable = $("input[@name='periodicInformEnable'][@checked]").val();
    var periodicInformInterval = $.trim($("input[@name='periodicInformInterval']").val());
    document.all("div_config").style.display = ""; 
	$("div[@id='div_config']").css("background-color","#33CC00");                            
	document.all("div_config").innerHTML = "<font size=2>正在配置设备TR069信息，请耐心等待....</font>";
	var url = "<s:url value='/bbms/config/tr069Config!TR069ConfigSave.action'/>";
	$.post(url,{
		manageFlag:manageFlag,
		acsurl:acsurl,
		userName:userName,
		password:password,
		cpeUsername:cpeUsername,
		cpePasswd:cpePasswd,
		periodicInformEnable:periodicInformEnable,
		periodicInformInterval:periodicInformInterval,
		deviceId:deviceId
	},function(ajax){
		var s = ajax.split(";");
		if(s[0]=="-1"){
			$("div[@id='div_config']").css("background-color","red");
			document.all("div_config").innerHTML = "<font size=2>"+s[1]+"</font>";
		}
		if(s[0]=="1"){
			$("div[@id='div_config']").css("background-color","#33CC00");
			document.all("div_config").innerHTML = "<font size=2>"+s[1]+"</font>";				
			var url = "<s:url value='/servStrategy/ServStrategy!getStrategy.action'/>";
			var strategyId = s[2];
			$.post(url,{
	        	strategyId:strategyId
	        },function(ajax){
          	   	$("div[@id='div_strategy']").html("");
				$("div[@id='div_strategy']").append(ajax);
				//parent.dyniframesize();
			});			
				//parent.dyniframesize();
		 		//parent.unblock();
			}
           //parent.dyniframesize();
           //parent.unblock();
		});
		document.getElementById("tr002").style.display = "";
		//parent.dyniframesize();
	    setTimeout("clearResult()", 5000); 		    
    }

function clearResult() {
	document.all("div_config").style.display = "none";
}

function checkForm(){
	var manageFlag = $("input[@name='manageFlag'][@checked]").val();
    var acsurl = $.trim($("input[@name='acsurl']").val());
    var userName = $.trim($("input[@name='userName']").val());
    var password = $.trim($("input[@name='password']").val());
    var cpeUsername = $.trim($("input[@name='cpeUsername']").val());
    var cpePasswd = $.trim($("input[@name='cpePasswd']").val());
    var periodicInformEnable = $("input[@name='periodicInformEnable'][@checked]").val();
    var periodicInformInterval = $.trim($("input[@name='periodicInformInterval']").val());
    if(manageFlag=="0"){
    	if(acsurl.length<=0){
	    	alert("请输入正确的TR-069管理URL地址！");
	    	$("input[@name='acsurl']").focus();
	    	return false;
    	}
    	if(acsurl.indexOf("*")>0){
			alert("请输入正确的TR-069管理URL地址！");
			$("input[@name='acsurl']").focus();
			return false;
		}
		if(IsAccount(userName,"ACS用户名")==false)return false;
		if(IsAccount(password,"ACS用户密码")==false)return false;
		if(IsAccount(cpeUsername,"CPE用户名")==false)return false;
		if(IsAccount(cpePasswd,"CPE用户密码")==false)return false;
		if(periodicInformEnable=="1"){
			if(IsNumber(periodicInformInterval,"上报周期")==false)return false;
	    	if(periodicInformInterval<60){
	    		alert("请输入大于60的上报周期（秒）！");
				$("input[@name='periodicInformInterval']").focus();
				return false;
	    	}
	    }
    }
    if(manageFlag=="1"){
	    if(periodicInformEnable=="1"){
	    	if(IsNumber(periodicInformInterval,"上报周期")==false)return false;
	    	if(periodicInformInterval<60){
	    		alert("请输入大于60的上报周期（秒）！");
				$("input[@name='periodicInformInterval']").focus();
				return false;
	    	}
	    }
    }
    if(manageFlag=="2"){
    	if(acsurl.length<=0){
	    	alert("请输入正确的TR-069管理URL地址！");
	    	$("input[@name='acsurl']").focus();
	    	return false;
    	}
    	if(acsurl.indexOf("*")>0){
			alert("请输入正确的TR-069管理URL地址！");
			$("input[@name='acsurl']").focus();
			return false;
		}
    }
    if(manageFlag=="3"){
    	if(acsurl.length<=0){
	    	alert("请输入正确的TR-069管理URL地址！");
	    	$("input[@name='acsurl']").focus();
	    	return false;
    	}
    	if(acsurl.indexOf("*")>0){
			alert("请输入正确的TR-069管理URL地址！");
			$("input[@name='acsurl']").focus();
			return false;
		}
		if(IsAccount(userName,"ACS用户名")==false)return false;
		if(IsAccount(password,"ACS用户密码")==false)return false;
		if(IsAccount(cpeUsername,"CPE用户名")==false)return false;
		if(IsAccount(cpePasswd,"CPE用户密码")==false)return false;
    }
    return true;
}

</SCRIPT>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD align=right>
			<div id="div_config"
				style="width: 23%; display: none; background-color: #33CC00">
			</div>
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="#">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
					<tr>
						<TH align="center" colspan="4">
							TR-069配置
						</TH>
					</tr>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%" class="column">
													配置项
												</TD>
												<TD colspan="3">
													<input type="radio" name="manageFlag" checked id="rd1"
														value="0" onchange="change()" onclick="change()">
													TR-069全部参数
													<input type="radio" name="manageFlag" id="rd2" value="1"
														onchange="change()" onclick="change()">
													只配置上报周期参数
													<input type="radio" name="manageFlag" id="rd3" value="2"
														onchange="change()" onclick="change()">
													只配置TR-069地址
													<input type="radio" name="manageFlag" id="rd4" value="3"
														onchange="change()" onclick="change()">
													只配置TR-069地址及相关信息
												</TD>
											</TR>
											<TR id="manage_url" style="display: " bgcolor="#FFFFFF">
												<TD class=column align="right" width="15%">
													TR-069管理URL
												</TD>
												<TD colspan="3">
													<input type="text" name="acsurl" id="acsurl" class="bk"
														value="http://*:*/ACS-server/ACS" size="50">
													<font color="red">* </font>
												</TD>
											</TR>
											<TR id="manage_acs" style="display: " bgcolor="#FFFFFF">
												<TD class=column align="right" width="15%">
													ACS用户名
												</TD>
												<TD>
													<input type="text" name="userName" id="userName "
														class="bk" value="">
													<font color="red">* </font>默认navigater
												</TD>
												<TD class=column align="right" width="15%">
													ACS用户密码
												</TD>
												<TD>
													<input type="text" name="password" id="password" class="bk"
														value="">
													<font color="red">* </font>默认navigater
												</TD>
											</TR>
											<TR id="manage_acs" style="display: " bgcolor="#FFFFFF">
												<TD class=column align="right" width="15%">
													CPE用户名
												</TD>
												<TD>
													<input type="text" name="cpeUsername" id="cpeUsername"
														class="bk" value="">
													<font color="red">* </font>默认bbms
												</TD>
												<TD class=column align="right" width="15%">
													CPE用户密码
												</TD>
												<TD>
													<input type="text" name="cpePasswd" id="cpePasswd"
														class="bk" value="">
													<font color="red">* </font>默认bbms
												</TD>
											</TR>
											<TR id="manage_period" style="display: " bgcolor="#FFFFFF">
												<TD align="right" class="column" width="15%">
													周期上报开关
												</TD>
												<TD colspan="3">
													<input type="radio" name="periodicInformEnable" checked
														id="rd5" value="1">
													开启
													<input type="radio" name="periodicInformEnable" id="rd6"
														value="0">
													关闭
													<input type="text" name="periodicInformEnable_h" value="">
												</TD>
											</TR>
											<TR id="manage_period" style="display: " bgcolor="#FFFFFF">
												<TD align="right" class="column" width="15%">
													上报周期（秒）
												</TD>
												<TD colspan="3">
													<input type="text" name="periodicInformInterval"
														id="periodicInformInterval" class="bk" value="">
													<font color="red">* </font>大于60
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="foot">
													<button onclick="ExecMod();">
														设 置
													</button>
													&nbsp;&nbsp;
													<button onclick="getStatus();">
														获 取
													</button>
												</TD>
											</TR>
											<TR id="tr002" style="display: none" bgcolor="#FFFFFF">
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
</TABLE>