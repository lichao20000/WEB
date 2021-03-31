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
	document.all("div_config").innerHTML = "<font size=2>���ڻ�ȡFTP������Ϣ����ȴ�...</font>";
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
		document.all("div_config").innerHTML = "<font size=2>��ȡTR069��Ϣʧ��!"+s[1]+"</font>";
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
	document.all("div_config").innerHTML = "<font size=2>���������豸TR069��Ϣ�������ĵȴ�....</font>";
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
	    	alert("��������ȷ��TR-069����URL��ַ��");
	    	$("input[@name='acsurl']").focus();
	    	return false;
    	}
    	if(acsurl.indexOf("*")>0){
			alert("��������ȷ��TR-069����URL��ַ��");
			$("input[@name='acsurl']").focus();
			return false;
		}
		if(IsAccount(userName,"ACS�û���")==false)return false;
		if(IsAccount(password,"ACS�û�����")==false)return false;
		if(IsAccount(cpeUsername,"CPE�û���")==false)return false;
		if(IsAccount(cpePasswd,"CPE�û�����")==false)return false;
		if(periodicInformEnable=="1"){
			if(IsNumber(periodicInformInterval,"�ϱ�����")==false)return false;
	    	if(periodicInformInterval<60){
	    		alert("���������60���ϱ����ڣ��룩��");
				$("input[@name='periodicInformInterval']").focus();
				return false;
	    	}
	    }
    }
    if(manageFlag=="1"){
	    if(periodicInformEnable=="1"){
	    	if(IsNumber(periodicInformInterval,"�ϱ�����")==false)return false;
	    	if(periodicInformInterval<60){
	    		alert("���������60���ϱ����ڣ��룩��");
				$("input[@name='periodicInformInterval']").focus();
				return false;
	    	}
	    }
    }
    if(manageFlag=="2"){
    	if(acsurl.length<=0){
	    	alert("��������ȷ��TR-069����URL��ַ��");
	    	$("input[@name='acsurl']").focus();
	    	return false;
    	}
    	if(acsurl.indexOf("*")>0){
			alert("��������ȷ��TR-069����URL��ַ��");
			$("input[@name='acsurl']").focus();
			return false;
		}
    }
    if(manageFlag=="3"){
    	if(acsurl.length<=0){
	    	alert("��������ȷ��TR-069����URL��ַ��");
	    	$("input[@name='acsurl']").focus();
	    	return false;
    	}
    	if(acsurl.indexOf("*")>0){
			alert("��������ȷ��TR-069����URL��ַ��");
			$("input[@name='acsurl']").focus();
			return false;
		}
		if(IsAccount(userName,"ACS�û���")==false)return false;
		if(IsAccount(password,"ACS�û�����")==false)return false;
		if(IsAccount(cpeUsername,"CPE�û���")==false)return false;
		if(IsAccount(cpePasswd,"CPE�û�����")==false)return false;
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
							TR-069����
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
													������
												</TD>
												<TD colspan="3">
													<input type="radio" name="manageFlag" checked id="rd1"
														value="0" onchange="change()" onclick="change()">
													TR-069ȫ������
													<input type="radio" name="manageFlag" id="rd2" value="1"
														onchange="change()" onclick="change()">
													ֻ�����ϱ����ڲ���
													<input type="radio" name="manageFlag" id="rd3" value="2"
														onchange="change()" onclick="change()">
													ֻ����TR-069��ַ
													<input type="radio" name="manageFlag" id="rd4" value="3"
														onchange="change()" onclick="change()">
													ֻ����TR-069��ַ�������Ϣ
												</TD>
											</TR>
											<TR id="manage_url" style="display: " bgcolor="#FFFFFF">
												<TD class=column align="right" width="15%">
													TR-069����URL
												</TD>
												<TD colspan="3">
													<input type="text" name="acsurl" id="acsurl" class="bk"
														value="http://*:*/ACS-server/ACS" size="50">
													<font color="red">* </font>
												</TD>
											</TR>
											<TR id="manage_acs" style="display: " bgcolor="#FFFFFF">
												<TD class=column align="right" width="15%">
													ACS�û���
												</TD>
												<TD>
													<input type="text" name="userName" id="userName "
														class="bk" value="">
													<font color="red">* </font>Ĭ��navigater
												</TD>
												<TD class=column align="right" width="15%">
													ACS�û�����
												</TD>
												<TD>
													<input type="text" name="password" id="password" class="bk"
														value="">
													<font color="red">* </font>Ĭ��navigater
												</TD>
											</TR>
											<TR id="manage_acs" style="display: " bgcolor="#FFFFFF">
												<TD class=column align="right" width="15%">
													CPE�û���
												</TD>
												<TD>
													<input type="text" name="cpeUsername" id="cpeUsername"
														class="bk" value="">
													<font color="red">* </font>Ĭ��bbms
												</TD>
												<TD class=column align="right" width="15%">
													CPE�û�����
												</TD>
												<TD>
													<input type="text" name="cpePasswd" id="cpePasswd"
														class="bk" value="">
													<font color="red">* </font>Ĭ��bbms
												</TD>
											</TR>
											<TR id="manage_period" style="display: " bgcolor="#FFFFFF">
												<TD align="right" class="column" width="15%">
													�����ϱ�����
												</TD>
												<TD colspan="3">
													<input type="radio" name="periodicInformEnable" checked
														id="rd5" value="1">
													����
													<input type="radio" name="periodicInformEnable" id="rd6"
														value="0">
													�ر�
													<input type="text" name="periodicInformEnable_h" value="">
												</TD>
											</TR>
											<TR id="manage_period" style="display: " bgcolor="#FFFFFF">
												<TD align="right" class="column" width="15%">
													�ϱ����ڣ��룩
												</TD>
												<TD colspan="3">
													<input type="text" name="periodicInformInterval"
														id="periodicInformInterval" class="bk" value="">
													<font color="red">* </font>����60
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="foot">
													<button onclick="ExecMod();">
														�� ��
													</button>
													&nbsp;&nbsp;
													<button onclick="getStatus();">
														�� ȡ
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