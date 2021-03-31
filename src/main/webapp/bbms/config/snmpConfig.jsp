<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="../../css/css_green.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="../../Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.blockUI.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
//是否展示V3版本的特殊字段
 var deviceId = <%=(String) request.getParameter("deviceId")%>;
function snmpVersionChange(){
    var snmp_version = $("input[@name='snmp_version'][@checked]").val();
	if(snmp_version=="V3"){
		$("tr[@name='snmpv3']").show();
		$("tr[@name='notsnmpv3']").hide();
		chgSecurityLevel();
		parent.dyniframesize();
        parent.unblock();
	}else{
		$("tr[@name='snmpv3']").hide();
		$("tr[@name='notsnmpv3']").show();	
		parent.dyniframesize();
        parent.unblock();	
	}
}
//安全级别选择
function chgSecurityLevel(){
    var security_level = $("select[@name='security_level']").val();
	if('1' == security_level){
		$("tr[@id='AuthPriv']").hide();
		$("tr[@id='AuthNoPriv']").hide();
		parent.dyniframesize();
	}
    if('2' == security_level){
		$("tr[@id='AuthPriv']").hide();
		$("tr[@id='AuthNoPriv']").show();
		parent.dyniframesize();
	}
	if('3' == security_level){
		$("tr[@id='AuthPriv']").show();
		$("tr[@id='AuthNoPriv']").show();
		parent.dyniframesize();
	}
	if('-1' == security_level){
		$("tr[@id='AuthPriv']").show();
		$("tr[@id='AuthNoPriv']").show();
		parent.dyniframesize();
	}
}
//SNMP 版本字段检查
	function checkSNMPVersion(){
		var v = $("input[@name='snmp_version'][@checked]").val()
		if(v == "V3"){
			// V3
			if(document.frm.security_username.value == ""){
				alert("请输入鉴权用户");
				document.frm.security_username.focus();
				return false;
			}
			//if(document.frm.engine_id.value == ""){
			//	alert("请输入SNMP引擎");
			//	document.frm.engine_id.focus();
			//	return false;
			//}
			var security_level = $("select[@name='security_level']").val();
			if(security_level == "-1"){
				alert("请选择安全级别");
				return false;
			}
			if(security_level == "2"){
				if(document.frm.auth_protocol.value == "-1"){
				alert("请选择认证协议");
				document.frm.auth_protocol.focus();
				return false;
			    }
			    if(document.frm.auth_passwd.value == ""){
				alert("请输入认证密钥");
				document.frm.auth_passwd.focus();
				return false;
			    }
			}
			if(security_level == "3"){
				if(document.frm.auth_protocol.value == "-1"){
				alert("请选择认证协议");
				document.frm.auth_protocol.focus();
				return false;
			    }
			    if(document.frm.auth_passwd.value == ""){
				alert("请输入认证密钥");
				document.frm.auth_passwd.focus();
				return false;
			    }
			    if(document.frm.privacy_protocol.value == "-1"){
				alert("请选择加密协议");
				return false;
			    }			
			    if(document.frm.privacy_passwd.value == ""){
				alert("请输入加密密码");
				document.frm.privacy_passwd.focus();
				return false;
			    }
			}			
		}else{
			// V1  V2
			if(document.frm.snmp_r_passwd.value == ""){
				alert("请输入读口令");
				document.frm.snmp_r_passwd.focus();
				return false;
			}
			if(document.frm.snmp_w_passwd.value == ""){
				alert("请输入写口令");
				document.frm.snmp_w_passwd.focus();
				return false;
			}			
		}
		return true;
	}
	function getStatus() {
				document.all("div_config").style.display = ""; 
				$("div[@id='div_config']").css("background-color","#33CC00");	
     		    document.all("div_config").innerHTML = "<font size=2>正在获取SNMP配置信息，请等待...</font>";
     		    parent.block();
		        var url = "<s:url value='/bbms/config/snmpConfig.action'/>";
		        $.post(url,{
	               deviceId:deviceId
	            },function(ajax){
	               var s=ajax.split(";");
	               document.all("div_config").style.display = "";
		           if (s[0]!="-1") {
		               if(s[0]=="1"){
		               $("input[@name='is_enable'][@value=1]").attr("checked",true);
		               }
		               if(s[0]=="0"){
		               $("input[@name='is_enable'][@value=0]").attr("checked",true);
		               }	 
			           $("input[@name='is_enable_h']").val(s[0]);
			           $("input[@name='security_username']").val(s[2]);
                       //$("input[@name='engine_id']").val(s[4]);
                       if(s[6]=="1"||s[6]=="2"||s[6]=="3"){
                           $("select[@name='security_level']").attr("value",s[6]);
                           
                       }else{
                           $("select[@name='security_level']").attr("value",'-1');
                           
                       }
			           if(s[1]=="v1"||s[1]=="0"||s[1]=="V1"){
		               $("input[@name='snmp_version'][@value=V1]").attr("checked",true);
		               snmpVersionChange();
		               }
			           if(s[1]=="v2"||s[1]=="V2"){
		               $("input[@name='snmp_version'][@value=V2]").attr("checked",true);
		               snmpVersionChange();
		               }
		               if(s[1]=="v3"||s[1]=="1"||s[1]=="V3"){
		               $("input[@name='snmp_version'][@value=V3]").attr("checked",true);
		               snmpVersionChange();		               
		               }
			           
			           
                       if(s[7]=="MD5"||s[7]=="SHA-1"){
                           $("select[@name='auth_protocol']").attr("value",s[7]);
                       }else{
                           $("select[@name='auth_protocol']").attr("value",'-1');
                       }  
                                        
                       $("input[@name='auth_passwd']").val(s[8]);
                       if(s[9]=="DES"||s[9]=="IDEA"||s[9]=="AES128"||s[9]=="AES192"||s[9]=="AES256"){
                           $("select[@name='privacy_protocol']").attr("value",s[9]);
                       }else{
                           $("select[@name='privacy_protocol']").attr("value",'-1');
                       }   
                       $("input[@name='privacy_passwd']").val(s[10]);
                       $("input[@name='snmp_r_passwd']").val(s[11]);
                       $("input[@name='snmp_w_passwd']").val(s[12]);  
 
                       $("div[@id='div_config']").css("background-color","#33CC00"); 
                       document.all("div_config").innerHTML = "<font size=2>获取SNMP配置信息成功!</font>"; 
                       
						parent.dyniframesize();
						parent.unblock(); 
                   }else{
                  		 $("div[@id='div_config']").css("background-color","red");
                       document.all("div_config").innerHTML = "<font size=2>获取SNMP配置信息失败!"+s[1]+"</font>";
                       parent.dyniframesize();
                       parent.unblock();
                   }                     
	            });
				
				document.getElementById("tr002").style.display = "";
                setTimeout("clearResult()", 5000); 
			    
	}
function clearResult() {
	document.all("div_config").style.display = "none";
}

     function ExecMod(){
  		if(checkSNMPVersion() == false) return false;
		parent.block();
        var is_enable = $("input[@name='is_enable'][@checked]").val();
       
        var is_enable_h = document.all("rd3").value;
        if(is_enable=="0"&&is_enable_h=="0"){
	        alert("设备SNMP已经是关闭的，不用重新设置关闭");
	        return false;
        }
        var snmp_version = $("input[@name='snmp_version'][@checked]").val();
        
        var security_username = document.all("security_username").value;		            
     //  var engine_id = document.all("engine_id").value;
        var security_level = document.all("security_level").value;
        var auth_protocol = document.all("auth_protocol").value;
        var auth_passwd = document.all("auth_passwd").value;
        var privacy_protocol = document.all("privacy_protocol").value;
        var privacy_passwd = document.all("privacy_passwd").value;
        var snmp_r_passwd = document.all("snmp_r_passwd").value;
        var snmp_w_passwd = document.all("snmp_w_passwd").value;   
        document.all("div_config").style.display = "";    		            
		$("div[@id='div_config']").css("background-color","#33CC00");                            
		document.all("div_config").innerHTML = "<font size=2>正在配置设备SNMP访问配置信息，请耐心等待....</font>";
		var url = "<s:url value='/bbms/config/snmpConfig!SNMPConfigSave.action'/>";
		$.post(url,{
              deviceId:deviceId,
              is_enable:is_enable,
              snmp_version:snmp_version,
              security_username:security_username,
              security_level:security_level,
              auth_protocol:auth_protocol,
              auth_passwd:auth_passwd,
              privacy_protocol:privacy_protocol,
              privacy_passwd:privacy_passwd,
              snmp_r_passwd:snmp_r_passwd,
              snmp_w_passwd:snmp_w_passwd
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
					parent.dyniframesize();
	            });			
				parent.dyniframesize();
		 		parent.unblock();
			}
           parent.dyniframesize();
           parent.unblock();
            });
		document.getElementById("tr002").style.display = "";
		parent.dyniframesize();
	    setTimeout("clearResult()", 5000);
				
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
			<FORM NAME="frm" METHOD="post" ACTION="#"
				onsubmit="return CheckForm()">
				<div id="filepath" style="display: none"></div>
				<input type="hidden" name="strDevice" value="">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
					<!-- <tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite" nowrap>
										参数实例管理
									</td>
									<td nowrap>
										<img src="../../images/attention_2.gif" width="15" height="12">
										配置终端的时间服务器。
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<TR>
						<TH colspan="4" align="center">
							设备查询
						</TH>
					</TR> 
					<TR bgcolor="#FFFFFF" id="tr1" STYLE="display: ">
						<td colspan="4">
							<div id="selectDevice">
								<span>加载中....</span>
							</div>
						</td>
					</TR>-->
					<TH colspan="4" align="center">
						SNMP 访问配置
					</TH>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF" id="ntp_status" style="display: ">
												<TD align="right" width="20%" class="column">
													SNMP使能开关
												</TD>
												<TD colspan=3>
													<input type="radio" name="is_enable" checked id="rd1"
														class=btn value="1">

													开启
													<input type="radio" name="is_enable" id="rd2" class=btn
														value="0">

													关闭
													<input type="hidden" name="is_enable_h" id="rd3">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" name="snmpv" style="display: ">
												<TD class=column align="right">
													协议版本
												</TD>
												<TD id="snmp_version" colspan=3>
													<input type="radio" name="snmp_version"
														id="snmp_version_v1" checked value="V1"
														onclick="snmpVersionChange();">
													V1
													<input type="radio" name="snmp_version"
														id="snmp_version_v2" value="V2"
														onclick="snmpVersionChange();">
													V2
													<input type="radio" name="snmp_version"
														id="snmp_version_v3" value="V3"
														onclick="snmpVersionChange();">
													V3
													<font color="#FF0000">*</font>
												</TD>
											</TR>
											<!-- SNMPVersion == 1 (V3) -->
											<TR bgcolor="#FFFFFF" name="snmpv3" style="display: none">
												<TD align="right" class="column">
													鉴权用户
												</TD>
												<TD width="30%">
													<input type="text" name="security_username"
														id="security_username" class="bk" value="">
													<font color="#FF0000">*</font>
												</TD>
												<!-- <TD align="right" class=column>
													SNMP引擎
												</TD>
												<TD width="30%">
													<input type="text" name="engine_id" id="engine_id"
														class="bk" value="">
														<font color="#FF0000">*</font>
												</TD> -->
												<TD align="right" class=column width="20%">
													安全级别
												</TD>
												<TD>
													<select class="bk" name="security_level"
														id="security_level" onchange="chgSecurityLevel()">
														<option value="-1" selected>
															==请选择==
														<option value="1">
															noAuthNoPriv
														<option value="2">
															AuthNoPriv
														<option value="3">
															AuthPriv
													</select>
													<font color="#FF0000">*</font>
												</TD>
											</TR>
											<!-- <TR bgcolor="#FFFFFF" name="snmpv3" style="display: none"
												class=column >

											 <TD align="right" class=column>
													SNMP端口
												</TD>
												<TD>
													<input type="text" name="snmp_udp" id="snmp_udp" class="bk"
														value="161">
													<font color="#FF0000">*</font>
												</TD>
                                            </TR> -->
											<TR bgcolor="#FFFFFF" name="snmpv3" style="display: none"
												class=column id="AuthNoPriv">

												<TD class=column align="right">
													认证协议
												</TD>
												<TD>
													<select class="bk" name="auth_protocol" id="auth_protocol">
														<option value="-1" selected>
															==请选择==
														<option value="MD5">
															MD5
														<option value="SHA-1">
															SHA-1
													</select>
													<font name="state1" color="#FF0000">*</font>
												</TD>
												<TD class=column align="right">
													认证密钥
												</TD>
												<TD>
													<input type="text" name="auth_passwd" id="auth_passwd"
														class="bk" value="">
													<font name="state1" color="#FF0000">*</font>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" name="snmpv3" style="display: none"
												id="AuthPriv">

												<TD class=column align="right">
													加密协议
												</TD>
												<TD>
													<select class="bk" name="privacy_protocol"
														id="privacy_protocol">
														<option value="-1" selected>
															==请选择==
														<option value="DES">
															DES
														<option value="IDEA">
															IDEA
														<option value="AES128">
															AES128
														<option value="AES192">
															AES192
														<option value="AES256">
															AES256
													</select>
													<font name="state2" color="#FF0000">*</font>
												</TD>
												<TD class=column align="right">
													加密密码
												</TD>
												<TD>
													<input type="text" name="privacy_passwd"
														id="privacy_passwd" class="bk" value="">
													<font name="state2" color="#FF0000">*</font>
												</TD>
											</TR>
											<!-- snmp相关 -->
											<TR bgcolor="#FFFFFF" name="notsnmpv3">
												<TD class=column align="right" width="20%">
													SNMP读口令
												</TD>
												<TD width="30%">
													<input type="text" name="snmp_r_passwd" id="snmp_r_passwd"
														class="bk" value="">
													<font color="#FF0000">*</font>
												</TD>
												<TD class=column align="right" width="20%">
													SNMP写口令
												</TD>
												<TD width="30%">
													<input type="text" name="snmp_w_passwd" id="snmp_w_passwd"
														class="bk" value="">
													<font color="#FF0000">*</font>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<button onclick="ExecMod();">
														设 置
													</button>
													&nbsp;&nbsp;
													<button onclick="getStatus();">
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
	<!--  <TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>  -->
</TABLE>