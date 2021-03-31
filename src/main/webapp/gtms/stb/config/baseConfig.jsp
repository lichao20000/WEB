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
			alert("��ѡ����뷽ʽ");
		    return false;
		}
		/**
		if(addressingType=="PPPoE"){
			if(PPPoEID == ""){
		        alert("������PPPoE�˺�");
		        $("input[@name='PPPoEID']").focus();
		        return false;
			}
			if(PPPoEPassword == ""){
		        alert("������PPPoE����");
		        $("input[@name='PPPoEPassword']").focus();
		        return false;
			}
		//}else if(addressingType == "DHCP"){
			if(DHCPID == ""){
		        alert("������DHCP�˺�");
		        $("input[@name='DHCPID']").focus();
		        return false;
			}
			if(DHCPPassword == ""){
		        alert("������DHCP����");
		        $("input[@name='DHCPPassword']").focus();
		        return false;
			}
		**/
		//}else 
		if(addressingType=="Static"){
		/**
			if(IPAddress == ""){
		        alert("������IP��ַ");
		        $("input[@name='IPAddress']").focus();
		        return false;
			}
			**/ 
			if(!IsIPAddr2(IPAddress,"IP��ַ")){
				$("input[@name='IPAddress']").focus();
				return false;
			}
			/**
			if(subnetMask == ""){
		        alert("��������������");
		        $("input[@name='subnetMask']").focus();
		        return false;
			}
			**/
			if(!IsIPAddr2(subnetMask,"��������")){
		        $("input[@name='subnetMask']").focus();
		        return false;
			}
			/**
			if(defaultGateway == ""){
		        alert("����������");
		        $("input[@name='defaultGateway']").focus();
		        return false;
			}
			**/
			if(!IsIPAddr2(defaultGateway,"����")){
		        $("input[@name='defaultGateway']").focus();
		       return false;
			}
			/**
			if(DNSServers == ""){
		        alert("������DNS������");
		        $("input[@name='DNSServers']").focus();
		        return false;
			}
			**/
			if(!IsIPAddr2(DNSServers,"DNS������")){
		       $("input[@name='defaultGateway']").focus();
		       return false;
			}
		}
		if(userID == ""){
	        alert("������ҵ���˺�");
	        $("input[@name='userID']").focus();
	        return false;
		}
		if(userPassword == ""){
	        alert("������ҵ������");
	        $("input[@name='userPassword']").focus();
	        return false;
		}
		if(authURL == ""){
	        alert("��������֤��������ַ");
	        $("input[@name='authURL']").focus();
	        return false;
		}
		/**
		if(autoUpdateServer == ""){
	        alert("��������·�������ַ");
	        $("input[@name='autoUpdateServer']").focus();
	        return false;
		}
		**/
	    return true;
	}
	function chkIPArea(strIP)
	{
	if(!IsNumber(strIP,"IP��ַ")) return false;
	if(parseInt(strIP)>255){
		alert("IP��ַ��" + strIP + "����255");
		return false;
	}
	return true;
	} 
	function IsNull(strValue,strMsg){
		if(Trim(strValue).length>0) return true;
		else{
			alert(strMsg+'����Ϊ��');
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
					alert(msg + "��ʽ������");
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
				alert(msg + "��ʽ������");
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
				alert(strMsg+'ӦΪ����');
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
	   // alert("ip1������ip2");
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
			if(!confirm("ȷ��Ҫ�ѽ��뷽ʽ��"+addressingTypebk+"��Ϊ"+addressingType+"��\n�������п���ʧ�ܣ�����")){
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
			alert("����û�иĶ��������ã�");
			return false;
		}
		$("tr[@id='tr_config']").show();        
        $("div[@id='div_config']").html("���������豸������Ϣ�������ĵȴ�....");                
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
  		     	$("div[@id='div_config']").html("���������豸������Ϣ�������ĵȴ�....");
				getStrategyById(s[2]);
			}
		});
    }
    
    
    // ȷ��DHCP ���� PPoE ���ʺŸ������Ƿ��иı�
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
				�����л���������Ϣ
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
				������������
			</TD>
			<TD width="30%" colspan="3">
				<input type="radio" name="intType" value="DHCP" checked="checked">
				����&nbsp;&nbsp;
				<input type="radio" name="intType" value="DHCP">
				����&nbsp;&nbsp;
			</TD>
		</TR>
		<TR>
			<TD class="title_2">
				���뷽ʽ
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
				DHCP�˺�
			</TD>
			<TD width="35%">
				<input type="text" name="DHCPID" id="DHCPID" class="bk"
					value="<s:property value="DHCPID"/>">
				<input type="hidden" name="DHCPIDbk"
					value="<s:property value="DHCPID"/>">
			</TD>
			<TD class="title_2" width="15%">
				DHCP����
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
				PPPoE�˺�
			</TD>
			<TD width="35%">
				<input type="text" name="PPPoEID" id="PPPoEID" class="bk"
					value="<s:property value="PPPoEID"/>">
				<input type="hidden" name="PPPoEIDbk"
					value="<s:property value="PPPoEID"/>">
			</TD>
			<TD class="title_2" width="15%">
				PPPoE����
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
				<strong>IP��ַ��Ϣ</strong>
			</Td>
		</tr>
		<TR id="tr_lan_2" style="display:none">
			<TD class="title_2">
				IP��ַ
			</TD>
			<TD>
				<input type="text" name="IPAddress" id="IPAddress" class="bk"
					value="<s:property value="IPAddress"/>">
				<input type="hidden" name="IPAddressbk"
					value="<s:property value="IPAddress"/>">
			</TD>
			<TD class="title_2" >
				��������
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
				����
			</TD>
			<TD>
				<input type="text" name="defaultGateway" id="defaultGateway"
					class="bk" value="<s:property value="defaultGateway"/>">
				<input type="hidden" name="defaultGatewaybk"
					value="<s:property value="defaultGateway"/>">
			</TD>
			<TD class="title_2">
				DNS������
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
				<strong>��Ƶ��ʽ������ģʽ</strong>
			</Td>
		</tr>
		<tr>
			<TD class="title_2">
				��Ƶ��ʽ
			</TD>
			<TD>
				<input type="text" name="compositeVideo" id="compositeVideo" class="bk"  readonly="readonly"
					value="<s:property value="compositeVideo"/>">
			</TD>
			<TD class="title_2">
				����ģʽ
			</TD>
			<TD>
				<input type="text" name="aspectRatio" id="aspectRatio" class="bk"  readonly="readonly"
					value="<s:property value="aspectRatio"/>">
			</TD>
		</tr>
		 -->
		<tr>
			<Td colspan="4" class="title_3">
				<strong>������Ϣ</strong>
			</Td>
		</tr>
			
		<TR>
			<TD class="title_2">
				ҵ���˺�
			</TD>
			<TD>
				<input type="text" name="userID" id="userID" class="bk"
					value="<s:property value="userID"/>">
				<input type="hidden" name="userIDbk"
					value="<s:property value="userID"/>">
			</TD>
			<TD class="title_2">
				ҵ������
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
							һ������ ����
						</button>
					</s:if>
					<s:else>
						<button onclick="resPass()">
							һ������ ����
						</button>
					</s:else> -->
			</TD>
		</TR>
		<TR>
			<TD class="title_2">
				��֤��������ַ
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
				���·�������ַ
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
				���ܵ�ַ
			</TD>
			<TD colspan="3">
				<input type="text" name="networkAddress" id="networkAddress"
					class="bk" value="<s:property value="networkAddress"/>" size="50"
					readonly="readonly">
			</TD>
		</TR>
		
		<TR>
			<TD class="title_2">
				�ɼ�ʱ��:
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
							д������
						</button>
					</s:if>
					<s:else>
						 <s:if test='"0"==isHistory'>
							<button onclick="ExecMod()">
								д������
							</button>
						</s:if>
						<s:else>
							<button onclick="ExecMod()" disabled="disabled">
								д������
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
	
