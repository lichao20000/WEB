<%@ page contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>


<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/CheckForm.js'/>"></SCRIPT>
<script LANGUAGE="JavaScript"
	src="<s:url value='/Js/jsDate/WdatePicker.js'/>"></script>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value='/Js/jquery.blockUI.js'/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">
<!--//
var deviceId = "<s:property value='deviceId'/>";
//parent.unblock();

function getStatus(){
	document.all("tr002").style.display = "none"; 
	document.all("div_config").style.display = ""; 
	$("div[@id='div_config']").css("background-color","#33CC00");	
	document.all("div_config").innerHTML = "<font size=2>正在获取FTP服务信息，请等待...</font>";
	//parent.block();
	var url = "<s:url value='/bbms/config/ftpConfig!getFTP.action'/>";
	$.post(url,{
		deviceId:deviceId
	},function(ajax){
	var s=ajax.split(";");
	document.all("div_config").style.display = "";
	if (s[0]!="-1") {
		if(s[0]=="1"){
			$("input[@name='ftpenable'][@value=1]").attr("checked",true);
		}
		if(s[0]=="0"){
			$("input[@name='ftpenable'][@value=0]").attr("checked",true);
		}	 
		$("input[@name='ftpenable_h']").val(s[0]);
		$("input[@name='ftpUserName']").val(s[1]);
		//$("input[@name='ftpPassword']").val(s[2]);
        $("input[@name='ftpPort']").val(s[3]);           
		//parent.dyniframesize();
		//parent.unblock(); 
	}else{
		$("div[@id='div_config']").css("background-color","red");
		document.all("div_config").innerHTML = "<font size=2>获取FTP服务信息失败!"+s[1]+"</font>";
		//parent.dyniframesize();
		//parent.unblock();
	}                     
	});

    setTimeout("clearResult()", 5000); 		    
}

function ExecMod(){
	if(checkForm() == false) return false;
	//parent.block();
	var ftpenable = $("input[@name='ftpenable'][@checked]").val();
    var ftpenable_h = $.trim($("input[@name='ftpenable_h']").val());
    if(ftpenable=="0"&&ftpenable_h=="0"){
		alert("设备FTP已经是关闭的，不用重新设置关闭");
		return false;
    }
    var ftpUserName = $("input[@name='ftpUserName']").val();
    var ftpPort = $("input[@name='ftpPort']").val();
    document.all("div_config").style.display = ""; 
	$("div[@id='div_config']").css("background-color","#33CC00");                            
	document.all("div_config").innerHTML = "<font size=2>正在配置设备FTP服务信息，请耐心等待....</font>";
	var url = "<s:url value='/bbms/config/ftpConfig!ftpConfigSave.action'/>";
	$.post(url,{
		deviceId:deviceId,
		ftpenable:ftpenable,
		ftpUserName:ftpUserName,
		ftpPort:ftpPort
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

function clearResult(){
	document.all("div_config").style.display = "none";
}

function checkForm(){
	var ftpenable = $("input[@name='ftpenable'][@checked]").val();
	var ftpUserName = $.trim($("input[@name='ftpUserName']").val());
    var ftpPort = $.trim($("input[@name='ftpPort']").val());
    if(ftpenable=="1"){
    	if(IsAccount(ftpUserName,"FTP用户名")==false)return false;
		if(ftpPort.length<=0){
    		alert("请输入FTP端口（默认为21）！");
    		$("input[@name='ftpPort']").focus();
    		return false;
		}
    	if(IsNumber2(ftpPort,"FTP端口")==false){
    		$("input[@name='ftpPort']").focus();
    		return false;
    	}
    }   
    return true;
}
//-->
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
						<TH align="center">
							FTP服务管理
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
												<TD align="right" width="20%" class="column">
													服务状态
												</TD>
												<TD>
													<input type="radio" name="ftpenable" checked id="rd1"
														value="1">

													开启
													<input type="radio" name="ftpenable" id="rd2" value="0">

													关闭
													<input type="text" name="ftpenable_h" value="">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD class=column align="right">
													FTP用户名
												</TD>
												<TD>
													<input type="text" name="ftpUserName" id="ftpUserName"
														class="bk" value="">
													<font color="red">*</font>
												</TD>
											</TR>
											<!-- <TR bgcolor="#FFFFFF">
												<TD class=column align="right">
													FTP用户名
												</TD>
												<TD>
													<input type="text" name="ftpPassword" id="ftpPassword"
														class="bk" value="">
												</TD>
											</TR> -->
											<TR bgcolor="#FFFFFF">
												<TD align="right" class="column">
													FTP端口
												</TD>
												<TD>
													<input type="text" name="ftpPort" id="ftpPort" class="bk"
														value="">
													<font color="red">* 默认21</font>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="2" align="right" class="green_foot">
													<button onclick="ExecMod()" class="jianbian">
														设 置
													</button>
													&nbsp;&nbsp;
													<button onclick="getStatus()" class="jianbian">
														获 取
													</button>
												</TD>
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
</TABLE>