<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
function relate()
{
	var obj =event.srcElement;
	//alert(typeof(obj));
	if(obj.options[obj.selectedIndex].value=="1")
	{
		tr1.style.display="none";
		tr2.style.display="none";
		tr3.style.display="none";
		tr4.style.display="none";
		tr5.style.display="none";
	}
	else if(obj.options[obj.selectedIndex].value=="2")
	{
		tr1.style.display="";
		tr2.style.display="";
		tr3.style.display="";
		tr4.style.display="none";
		tr5.style.display="none";
	}
	else if(obj.options[obj.selectedIndex].value=="3")
	{
		tr1.style.display="";
		tr2.style.display="";
		tr3.style.display="";
		tr4.style.display="";
		tr5.style.display="";
	}
}

function checkForm()
{
  var resultObj = new Object();
  var snmpV3Obj =document.all("snmpV3");
  var snmpV3=0;
  for(var i=0;i<snmpV3Obj.length;i++)
  {	  
	  if(snmpV3Obj[i].checked)
	  {
		snmpV3=snmpV3Obj[i].value;
		break;
	  }
  }

 //需要v3
 if(snmpV3=="0")
 {
	  var obj=document.all("security_level");
	  if(obj.options[obj.selectedIndex].value=="1")
	 {
		resultObj.text1="1||null||null||null||null||null";
		resultObj.text2="noAuthNoPriv||||||||||";
	 }
	 else if(obj.options[obj.selectedIndex].value=="2")
	{
		if(!IsNull(document.all("security_username").value,"鉴权用户")
			||!IsNull(document.all("auth_passwd").value,"鉴权密钥"))		
		{
			return false;
		}
		var auth_protocolObj = document.all("auth_protocol");
		resultObj.text1="2||"+document.all("security_username").value+"||"+auth_protocolObj.options[auth_protocolObj.selectedIndex].value
			+"||"+document.all("auth_passwd").value+"||null||null";
    	resultObj.text2="AuthNoPriv||"+document.all("security_username").value+"||"+auth_protocolObj.options[auth_protocolObj.selectedIndex].text+"||"
		+document.all("auth_passwd").value+"||||";
	}
	else if(obj.options[obj.selectedIndex].value=="3")
	{
		if(!IsNull(document.all("security_username").value,"鉴权用户")
			||!IsNull(document.all("auth_passwd").value,"鉴权密钥")
			||!IsNull(document.all("privacy_passwd").value,"私钥"))
		{
			return false;
		}
		var auth_protocolObj = document.all("auth_protocol");
		var privacy_protocolObj=document.all("privacy_protocol");
		resultObj.text1="3||"+document.all("security_username").value+"||"+auth_protocolObj.options[auth_protocolObj.selectedIndex].value
			+"||"+document.all("auth_passwd").value+"||"+privacy_protocolObj.options[privacy_protocolObj.selectedIndex].value+"||"
			+document.all("privacy_passwd").value;
    	resultObj.text2="AuthPriv||"+document.all("security_username").value+"||"
		+auth_protocolObj.options[auth_protocolObj.selectedIndex].text+"||"+document.all("auth_passwd").value+"||"
		+privacy_protocolObj.options[privacy_protocolObj.selectedIndex].text+"||"+document.all("privacy_passwd").value;
	}
 }
 else
 {
	 if(!IsNull(document.all("snmp_r_passwd").value,"v1/V2读口令"))
	 {
		 return false;
	 }
	 resultObj.text1="null||null||null||null||null||null";
	 resultObj.text2="||||||||||";
 }

 if(document.all("snmp_r_passwd").value=="")
 {
    resultObj.text1+="||null";
	resultObj.text2+="||";
 }
 else 
 {
	 resultObj.text1+="||"+document.all("snmp_r_passwd").value;
     resultObj.text2+="||"+document.all("snmp_r_passwd").value;
 } 
  window.returnValue=resultObj;
  window.close();
  return true;
}


function isV3()
{
	var obj =document.all("snmpV3");
	for(var i=0;i<obj.length;i++)
	{
		if(obj[i].checked&&obj[i].value=="0")
		{
            tr0.style.display="";
			tr1.style.display="";
			tr2.style.display="";
			tr3.style.display="";
			tr4.style.display="none";
			tr5.style.display="none";
			break;
		}
		else if(obj[i].checked&&obj[i].value=="1")
		{
			tr0.style.display="none";
			tr1.style.display="none";
			tr2.style.display="none";
			tr3.style.display="none";
			tr4.style.display="none";
			tr5.style.display="none";
			break;
		}		
	}
}
</SCRIPT>
<TABLE border=0 cellspacing=1 cellpadding=2 width="95%" bgcolor=#999999>
	<TR>
		<TH colspan=2>添加设备口令</TH>
	</TR>	
	<TR bgcolor="#FFFFFF">
		<TD align=right width="20%">v3版本:</TD>
		<TD align=left width="80%"><input type="radio" name="snmpV3" value="0" checked onclick="isV3()">需要&nbsp;&nbsp;<input type="radio" name="snmpV3" value="1" onclick="isV3()">不需要</TD>
	</TR>
	<TR bgcolor="#FFFFFF" id="tr0">
		<TD align=right width="20%">安全级别:</TD>
		<TD align=left width="80%">
			<select name="security_level" onchange="relate()">
				<option value="1">noAuthNoPriv</option>
				<option value="2" selected>AuthNoPriv</option>
				<option value="3">AuthPriv</option>
			</select>
		</TD>		
	</TR>
    <TR bgcolor="#FFFFFF" id="tr1" style="display:">
		<TD align=right width="20%">鉴权用户:</TD>
		<TD align=left width="80%"><input type="text" name="security_username" value="SMEGWMANAGER"></TD>		
	</TR>
	<TR bgcolor="#FFFFFF" id="tr2" style="display:">
		<TD align=right width="20%">鉴权协议:</TD>
		<TD align=left width="80%">
			<select name="auth_protocol">
				<option value="MD5" selected>MD5</option>
				<option value="SHA-1">SHA-1</option>
			</select>
		</TD>		
	</TR>
	<TR bgcolor="#FFFFFF" id="tr3" style="display:">
		<TD align=right width="20%">鉴权密钥:</TD>
		<TD align=left width="80%"><input type="text" name="auth_passwd" value="TelecomSNMP"></TD>		
	</TR>		
	<TR bgcolor="#FFFFFF" id="tr4" style="display:none">
		<TD align=right width="20%">加解密协议:</TD>
		<TD align=left width="80%">
			<select name="privacy_protocol">
				<option value="DES">DES</option>
				<option value="IDEA">IDEA</option>
				<option value="AES128">AES128</option>
				<option value="AES192">AES192</option>
				<option value="AES256">AES256</option>				
			</select>
		</TD>		
	</TR>	
	<TR bgcolor="#FFFFFF" id="tr5" style="display:none">
		<TD align=right width="20%">私钥:</TD>
		<TD align=left width="80%"><input type="text" name="privacy_passwd" value="TelecomSNMP"></TD>		
	</TR>
	<TR bgcolor="#FFFFFF">
		<TD align=right width="20%">v1/V2读口令:</TD>
		<TD align=left width="80%"><input type="text" name="snmp_r_passwd" value=""></TD>		
	</TR>
	<TR bgcolor="#FFFFFF">
		<TD colspan=2 class=foot align=right>
			<input type="button" name="ok" value="确定" onclick=" return checkForm();" >&nbsp;&nbsp;
			<input type="button" name="ok" value="取消" onclick="window.close();" >
		</TD>
	</TR>
</TABLE>