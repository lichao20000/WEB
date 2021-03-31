<%@ page contentType="text/html;charset=gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
		type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
	var deviceId = '<s:property value="deviceId"/>';
	init();
	function init(){
		var addressingType = '<s:property value="addressingType"/>';
		if(addressingType=="PPPoE"||addressingType=="PPPOE"){
			$("input[@name='addressingType'][@value=PPPoE]").attr("checked",true);
			$("input[@name='addressingTypebk']").val("PPPoE");
		}else if(addressingType=="DHCP"){
			$("input[@name='addressingType'][@value=DHCP]").attr("checked",true);
			$("input[@name='addressingTypebk']").val("DHCP");
		}else if(addressingType=="LAN"||addressingType=="Static"){
			$("input[@name='addressingType'][@value=Static]").attr("checked",true);
			$("input[@name='addressingTypebk']").val("Static");
			$("#tr_lan_1").show();
			$("#tr_lan_2").show();
			$("#tr_lan_3").show();
		}
		
	} 
	function changeAddType(val){
		if(val.value =="Static"){
			$("#tr_lan_1").show();
			$("#tr_lan_2").show();
			$("#tr_lan_3").show();
		}else{
			$("#tr_lan_1").hide();
			$("#tr_lan_2").hide();
			$("#tr_lan_3").hide();
		}
	}
	function checkForm(){
	          
		var PPPoEID = $("input[@name='PPPoEID']").val();
		var PPPoEPassword = $("input[@name='PPPoEPassword']").val();
		var DHCPID = $("input[@name='DHCPID']").val();
		var DHCPPassword = $("input[@name='DHCPPassword']").val();
		var IPAddress = $("input[@name='IPAddress']").val();
		var subnetMask = $("input[@name='subnetMask']").val();
		var defaultGateway = $("input[@name='defaultGateway']").val();
		var DNSServers = $("input[@name='DNSServers']").val();
		var userID = $("input[@name='userID']").val();
		var userPassword = $("input[@name='userPassword']").val();
		var authURL = $("input[@name='authURL']").val();
		var autoUpdateServer = $("input[@name='autoUpdateServer']").val();
		
		var addressingType = $("input[@name='addressingType'][@checked]").val();
		if(addressingType==""||addressingType==undefined){
			alert("请选择接入方式");
		    return false;
		}
		/**
		if(addressingType=="PPPoE"){
			if(PPPoEID == ""){
		        alert("请输入PPPoE账号");
		        $("input[@name='PPPoEID']").focus();
		        return false;
			}
			if(PPPoEPassword == ""){
		        alert("请输入PPPoE密码");
		        $("input[@name='PPPoEPassword']").focus();
		        return false;
			}
		//}else if(addressingType == "DHCP"){
			if(DHCPID == ""){
		        alert("请输入DHCP账号");
		        $("input[@name='DHCPID']").focus();
		        return false;
			}
			if(DHCPPassword == ""){
		        alert("请输入DHCP密码");
		        $("input[@name='DHCPPassword']").focus();
		        return false;
			}
		**/
		//}else 
		if(addressingType=="Static"){
		/**
			if(IPAddress == ""){
		        alert("请输入IP地址");
		        $("input[@name='IPAddress']").focus();
		        return false;
			}
			**/ 
			if(!IsIPAddr2(IPAddress,"IP地址")){
				$("input[@name='IPAddress']").focus();
				return false;
			}
			/**
			if(subnetMask == ""){
		        alert("请输入子网掩码");
		        $("input[@name='subnetMask']").focus();
		        return false;
			}
			**/
			if(!IsIPAddr2(subnetMask,"子网掩码")){
		        $("input[@name='subnetMask']").focus();
		        return false;
			}
			/**
			if(defaultGateway == ""){
		        alert("请输入网关");
		        $("input[@name='defaultGateway']").focus();
		        return false;
			}
			**/
			if(!IsIPAddr2(defaultGateway,"网关")){
		        $("input[@name='defaultGateway']").focus();
		       return false;
			}
			/**
			if(DNSServers == ""){
		        alert("请输入DNS服务器");
		        $("input[@name='DNSServers']").focus();
		        return false;
			}
			**/
			if(!IsIPAddr2(DNSServers,"DNS服务器")){
		       $("input[@name='defaultGateway']").focus();
		       return false;
			}
		}
		if(userID == ""){
	        alert("请输入业务账号");
	        $("input[@name='userID']").focus();
	        return false;
		}
		if(userPassword == ""){
	        alert("请输入业务密码");
	        $("input[@name='userPassword']").focus();
	        return false;
		}
		if(authURL == ""){
	        alert("请输入认证服务器地址");
	        $("input[@name='authURL']").focus();
	        return false;
		}
		/**
		if(autoUpdateServer == ""){
	        alert("请输入更新服务器地址");
	        $("input[@name='autoUpdateServer']").focus();
	        return false;
		}
		**/
	    return true;
	}
	function chkIPArea(strIP)
	{
	if(!IsNumber(strIP,"IP地址")) return false;
	if(parseInt(strIP)>255){
		alert("IP地址中" + strIP + "大于255");
		return false;
	}
	return true;
	} 
	function IsNull(strValue,strMsg){
		if(Trim(strValue).length>0) return true;
		else{
			alert(strMsg+'不能为空');
			return false;
		}
	} 
	function IsIPAddr2(strValue,msg){
		if(IsNull(strValue,msg)){
			var pos;
			var tmpStr;
			var v = strValue;
			var i=0;
			var bz=true;
			while(bz){
				pos = v.indexOf(".");
				if(i != 3 && pos == -1){
					alert(msg + "格式不符合");
					return false;
				}
				if(pos == -1){pos = v.length;bz=false;}
				tmpStr = v.substring(0,pos);
				if(!chkIPArea(tmpStr)) return false;
				v = v.substring(pos+1,v.length);
				i=i+1;
			}
			if(i=4) return true;
			else{
				alert(msg + "格式不符合");
				return false;			
			}
		}
	}  
	function Trim(strValue){
		var v = strValue;
		var i = 0;
		while(i<v.length){
		  if(v.substring(i,i+1)!=' '){
			v = v.substring(i,v.length);
			break;
		  }
		  i = i + 1;
		}
		i = v.length;
		while(i>0){
		  if(v.substring(i-1,i)!=' '){
		    v = v.substring(0,i);
			break;
		  }	
		  i = i - 1;
		}
		return v;
}  
function IsNumber(strValue,strMsg){
	var bz = true;
	if(IsNull(strValue,strMsg)){
		for(var i=0;i<strValue.length;i++){
			var ch=strValue.substring(i,i+1);
			if(ch<'0'||ch>'9'){
				alert(strMsg+'应为数字');
				bz = false;
				break;
			}
		}
	}
	else{
		bz = false;
	}
	if(bz){return true;}
	else{return false;}
}
	function checkIpAd(ip1,ip2){
	    var a = ip1.split(".");
	    var b = ip2.split(".");
	    if(a[0]!=b[0]||a[1]!=b[1]||a[2]!=b[2]){
	    //alert("ip1:"+a[0]+"."+a[1]+"."+a[2]+"."+a[3]);
	    //alert("ip2:"+b[0]+"."+b[1]+"."+b[2]+"."+b[3]);
	   // alert("ip1不等于ip2");
	                return false;
	        }
	        return true;
	}
	
	function ExecMod(){
		if(checkForm() == false) return false;
		var PPPoEID = $("input[@name='PPPoEID']").val();
		var PPPoEIDbk = $("input[@name='PPPoEIDbk']").val();
		var PPPoEPassword = $("input[@name='PPPoEPassword']").val();
		var PPPoEPasswordbk = $("input[@name='PPPoEPasswordbk']").val();
		var DHCPID = $("input[@name='DHCPID']").val();
		var DHCPIDbk = $("input[@name='DHCPIDbk']").val();
		var DHCPPassword = $("input[@name='DHCPPassword']").val();
		var DHCPPasswordbk = $("input[@name='DHCPPasswordbk']").val();
		var IPAddress = $("input[@name='IPAddress']").val();
		var IPAddressbk = $("input[@name='IPAddressbk']").val();
		var subnetMask = $("input[@name='subnetMask']").val();
		var subnetMaskbk = $("input[@name='subnetMaskbk']").val();
		var defaultGateway = $("input[@name='defaultGateway']").val();
		var defaultGatewaybk = $("input[@name='defaultGatewaybk']").val();
		var DNSServers = $("input[@name='DNSServers']").val();
		var DNSServersbk = $("input[@name='DNSServersbk']").val();
		var userID = $("input[@name='userID']").val();
		var userIDbk = $("input[@name='userIDbk']").val();
		var userPassword = $("input[@name='userPassword']").val();
		var userPasswordbk = $("input[@name='userPasswordbk']").val();
		var authURL = $("input[@name='authURL']").val();
		var authURLbk = $("input[@name='authURLbk']").val();
		var autoUpdateServer = $("input[@name='autoUpdateServer']").val();
		var autoUpdateServerbk = $("input[@name='autoUpdateServerbk']").val();
		var addressingType = $("input[@name='addressingType'][@checked]").val();
		var addressingTypebk = $("input[@name='addressingTypebk']").val();
		
		if(addressingType!=addressingTypebk){
			if(!confirm("确定要把接入方式从"+addressingTypebk+"改为"+addressingType+"吗？\n本操作有可能失败！！！")){
				return false;
			}
		}
		
		if(checkAccount() == true && 
		   subnetMask==subnetMaskbk &&
		   defaultGateway==defaultGatewaybk &&
		   DNSServers==DNSServersbk &&
		   userID==userIDbk &&
		   userPassword==userPasswordbk &&
		   authURL==authURLbk &&
		   autoUpdateServer==autoUpdateServerbk &&
		   addressingType==addressingTypebk &&
		   IPAddress==IPAddressbk)
		{
			alert("参数没有改动不需配置！");
			return false;
		}
		$("tr[@id='tr_config']").show();        
        $("div[@id='div_config']").html("正在配置设备配置信息，请耐心等待....");                
		var url = "<s:url value='/gtms/stb/config/baseConfig!baseConfig.action'/>";
		$.post(url,{
			PPPoEID:PPPoEID,
			PPPoEIDbk:PPPoEIDbk,
			PPPoEPassword:PPPoEPassword,
			PPPoEPasswordbk:PPPoEPasswordbk,
			DHCPID:DHCPID,
			DHCPIDbk:DHCPIDbk,
			DHCPPassword:DHCPPassword,
			DHCPPasswordbk:DHCPPasswordbk,
			IPAddress:IPAddress,
			IPAddressbk:IPAddressbk,
			subnetMask:subnetMask,
			subnetMaskbk:subnetMaskbk,
			defaultGateway:defaultGateway,
			defaultGatewaybk:defaultGatewaybk,
			DNSServers:DNSServers,
			DNSServersbk:DNSServersbk,
			userID:userID,
			userIDbk:userIDbk,
			userPassword:userPassword,
			userPasswordbk:userPasswordbk,
			authURL:authURL,
			authURLbk:authURLbk,
			autoUpdateServer:autoUpdateServer,
			autoUpdateServerbk:autoUpdateServerbk,
			addressingType:addressingType,
			addressingTypebk:addressingTypebk,
			deviceId:deviceId
		},function(ajax){
			var s = ajax.split(";");
			if(s[0]=="-1"){
				alert(s[1]);
				$("div[@id='div_config']").html("<font color=red>"+s[1]+"</font>");   
			}       
			if(s[0]=="1"){
  		     	$("div[@id='div_config']").html("正在配置设备配置信息，请耐心等待....");
				getStrategyById(s[2]);
			}
		});
    }
    
    
    // 确定DHCP 或者 PPoE 的帐号跟密码是否有改变
    function checkAccount(){
    	var PPPoEID = $("input[@name='PPPoEID']").val();
		var PPPoEIDbk = $("input[@name='PPPoEIDbk']").val();
		var PPPoEPassword = $("input[@name='PPPoEPassword']").val();
		var PPPoEPasswordbk = $("input[@name='PPPoEPasswordbk']").val();
		var DHCPID = $("input[@name='DHCPID']").val();
		var DHCPIDbk = $("input[@name='DHCPIDbk']").val();
		var DHCPPassword = $("input[@name='DHCPPassword']").val();
		var DHCPPasswordbk = $("input[@name='DHCPPasswordbk']").val();
		
		var addressingType = $("input[@name='addressingType'][@checked]").val();
		
		if(DHCPID == DHCPIDbk && DHCPPassword == DHCPPasswordbk && PPPoEID == PPPoEIDbk && PPPoEPassword == PPPoEPasswordbk){
			return true ;
		}else{
			return false;
		}
    }
	    
	function getStrategyById(strategyId){
		var url = "<s:url value='/gtms/stb/config/baseConfig!getStrategyById.action'/>";
		$.post(url,{
			strategyId:strategyId
		},function(ajax){
			var s = ajax.split(";");
			if(s[0]=="1"){
				alert(s[1]);
				$("div[@id='div_config']").html("<font color=green>"+s[1]+"</font>");  
			}else if(s[0]=="-1"){
				alert(s[1]);
				$("div[@id='div_config']").html("<font color=red>"+s[1]+"</font>");  
			}else{
				setTimeout('getStrategyById("'+strategyId+'")', 1000);
			}
		});				
	}

	function resPass() {
		var newPassword = "";
		var newPassword = $("input[@name='userPassword']").val();
        var url = "<s:url value='/gtms/stb/config/baseConfig!resPass.action'/>";
		$.post(url, {
			deviceId:deviceId,
			newPassword:newPassword
		}, function(ajax) {
			alert(ajax);
		});

	}
	
  </SCRIPT>
	<TABLE class="querytable" width="100%">
		<tr>
			<Td colspan="4" class="title_1">
				机顶盒基本配置信息
			</Td>
		</tr>
		<s:if test='errorMessage!=null&&errorMessage!=""'>
			<tr id="error" style="display: ">
				<td colspan="4">
					<div align="center">
						<font color="red"><s:property value="errorMessage" /> </font>
					</div>
				</td>
			</tr>
		</s:if>
		<TR style="display: none">
			<TD class="title_2" width="20%">
				网络连接类型
			</TD>
			<TD width="30%" colspan="3">
				<input type="radio" name="intType" value="DHCP" checked="checked">
				有线&nbsp;&nbsp;
				<input type="radio" name="intType" value="DHCP">
				无线&nbsp;&nbsp;
			</TD>
		</TR>
		<TR>
			<TD class="title_2">
				接入方式
			</TD>
			<TD colspan=3>
				<input type="radio" name="addressingType" value="PPPoE" onclick="changeAddType(this)">
				PPPoE&nbsp;&nbsp;
				<input type="radio" name="addressingType" value="DHCP" onclick="changeAddType(this)">
				DHCP&nbsp;&nbsp;
				<input type="radio" name="addressingType" value="Static" onclick="changeAddType(this)">
				LAN(Static)
				<input type="hidden" name="addressingTypebk"
					value="<s:property value="addressingType"/>">
			</TD>
		</TR>
		<tr >
			<Td colspan="4" class="title_3">
				<strong>DHCP</strong>
			</Td>
		</tr>
		<TR >
			<TD class="title_2" width="15%">
				DHCP账号
			</TD>
			<TD width="35%">
				<input type="text" name="DHCPID" id="DHCPID" class="bk"
					value="<s:property value="DHCPID"/>">
				<input type="hidden" name="DHCPIDbk"
					value="<s:property value="DHCPID"/>">
			</TD>
			<TD class="title_2" width="15%">
				DHCP密码
			</TD>
			<TD width="35%">
			<s:if test='pwd=="1"'>
				<input type="text" name="DHCPPassword" id="DHCPPassword" class="bk"
					value="<s:property value="DHCPPassword"/>">
			</s:if>
			<s:else>
				<input type="password" name="DHCPPassword" id="DHCPPassword" class="bk"
						value="<s:property value="DHCPPassword"/>">
			</s:else>
				
				<input type="hidden" name="DHCPPasswordbk"
					value="<s:property value="DHCPPassword"/>">
			</TD>
		</TR>
		<tr >
			<Td colspan="4" class="title_3">
				<strong>PPPoE</strong>
			</Td>
		</tr>
		<TR >
			<TD class="title_2" width="15%">
				PPPoE账号
			</TD>
			<TD width="35%">
				<input type="text" name="PPPoEID" id="PPPoEID" class="bk"
					value="<s:property value="PPPoEID"/>">
				<input type="hidden" name="PPPoEIDbk"
					value="<s:property value="PPPoEID"/>">
			</TD>
			<TD class="title_2" width="15%">
				PPPoE密码
			</TD>
			<TD width="35%">
			<s:if test='pwd=="1"'>
				<input type="text" name="PPPoEPassword" id="PPPoEPassword" class="bk"
					value="<s:property value="PPPoEPassword"/>">
			</s:if>
			<s:else>
				<input type="password" name="PPPoEPassword" id="PPPoEPassword" class="bk"
					value="<s:property value="PPPoEPassword"/>">
			</s:else>
				<input type="hidden" name="PPPoEPasswordbk"
					value="<s:property value="PPPoEPassword"/>">
			</TD>
		</TR>

		<tr id="tr_lan_1" style="display:none">
			<Td colspan="4" class="title_3">
				<strong>IP地址信息</strong>
			</Td>
		</tr>
		<TR id="tr_lan_2" style="display:none">
			<TD class="title_2">
				IP地址
			</TD>
			<TD>
				<input type="text" name="IPAddress" id="IPAddress" class="bk"
					value="<s:property value="IPAddress"/>">
				<input type="hidden" name="IPAddressbk"
					value="<s:property value="IPAddress"/>">
			</TD>
			<TD class="title_2" >
				子网掩码
			</TD>
			<TD>
				<input type="text" name="subnetMask" id="subnetMask" class="bk"
					value="<s:property value="subnetMask"/>">
				<input type="hidden" name="subnetMaskbk"
					value="<s:property value="subnetMask"/>">
			</TD>
		</TR>
	
		<TR id="tr_lan_3" style="display:none">
			<TD class="title_2">
				网关
			</TD>
			<TD>
				<input type="text" name="defaultGateway" id="defaultGateway"
					class="bk" value="<s:property value="defaultGateway"/>">
				<input type="hidden" name="defaultGatewaybk"
					value="<s:property value="defaultGateway"/>">
			</TD>
			<TD class="title_2">
				DNS服务器
			</TD>
			<TD>
				<input type="text" name="DNSServers" class="bk" id="DNSServers"
					value="<s:property value="DNSServers"/>">
				<input type="hidden" name="DNSServersbk"
					value="<s:property value="DNSServers"/>">
			</TD>
		</TR>
		<!-- 
		<tr>
			<Td colspan="4" class="title_3">
				<strong>视频制式与屏显模式</strong>
			</Td>
		</tr>
		<tr>
			<TD class="title_2">
				视频制式
			</TD>
			<TD>
				<input type="text" name="compositeVideo" id="compositeVideo" class="bk"  readonly="readonly"
					value="<s:property value="compositeVideo"/>">
			</TD>
			<TD class="title_2">
				屏显模式
			</TD>
			<TD>
				<input type="text" name="aspectRatio" id="aspectRatio" class="bk"  readonly="readonly"
					value="<s:property value="aspectRatio"/>">
			</TD>
		</tr>
		 -->
		<tr>
			<Td colspan="4" class="title_3">
				<strong>配置信息</strong>
			</Td>
		</tr>
			
		<TR>
			<TD class="title_2">
				业务账号
			</TD>
			<TD>
				<input type="text" name="userID" id="userID" class="bk"
					value="<s:property value="userID"/>">
				<input type="hidden" name="userIDbk"
					value="<s:property value="userID"/>">
			</TD>
			<TD class="title_2">
				业务密码
			</TD>
			<TD>
				<s:if test='pwd=="1"'>
					<input type="text" name="userPassword" id="userPassword" class="bk"
						value="<s:property value="userPassword"/>">
				</s:if>
				<s:else>
					<input type="password" name="userPassword" id="userPassword" class="bk"
						value="<s:property value="userPassword"/>">
				</s:else>
				
				
				<input type="hidden" name="userPasswordbk"
					value="<s:property value="userPassword"/>">
					<!-- 
					<s:if test='errorMessage!=null&&errorMessage!=""'>
						<button onclick="resPass()" disabled="disabled">
							一键重置 密码
						</button>
					</s:if>
					<s:else>
						<button onclick="resPass()">
							一键重置 密码
						</button>
					</s:else> -->
			</TD>
		</TR>
		<TR>
			<TD class="title_2">
				认证服务器地址
			</TD>
			<TD colspan="3">
				<input type="text" name="authURL" id="authURL" class="bk"
					value="<s:property value="authURL"/>" size="50">
				<input type="hidden" name="authURLbk"
					value="<s:property value="authURL"/>" size="50">
			</TD>
		</TR>
		<TR>
			<TD class="title_2">
				更新服务器地址
			</TD>
			<TD colspan="3">
				<input type="text" name="autoUpdateServer" id="autoUpdateServer"
					class="bk" value="<s:property value="autoUpdateServer"/>" size="50">
				<input type="hidden" name="autoUpdateServerbk"
					value="<s:property value="autoUpdateServer"/>" size="50">
			</TD>
		</TR>
		<TR>
			<TD class="title_2">
				网管地址
			</TD>
			<TD colspan="3">
				<input type="text" name="networkAddress" id="networkAddress"
					class="bk" value="<s:property value="networkAddress"/>" size="50"
					readonly="readonly">
			</TD>
		</TR>
		
		<TR>
			<TD class="title_2">
				采集时间:
			</TD>
			<TD colspan="3">
				<s:property value="time"/>
			</TD>
		</TR>
		
		
		
		<tr align="right">
			<td colspan="4" class="foot" align="right">
				<div class="right">
					<s:if test='errorMessage!=null&&errorMessage!=""'>
						<button onclick="ExecMod()" disabled="disabled">
							写入配置
						</button>
					</s:if>
					<s:else>
						 <s:if test='"0"==isHistory'>
							<button onclick="ExecMod()">
								写入配置
							</button>
						</s:if>
						<s:else>
							<button onclick="ExecMod()" disabled="disabled">
								写入配置
							</button>
						</s:else>
					</s:else>
				</div>
			</td>
		</tr>
		<TR style="display: none" id="tr_config">
			<td colspan="4" valign="top" class=column>
				<div id="div_config" style="width: 100%; z-index: 1; top: 100px">
				</div>
			</td>
		</TR>
	</TABLE>
	
