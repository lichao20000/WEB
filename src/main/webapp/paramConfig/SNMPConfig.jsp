<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
request.setCharacterEncoding("GBK");

%>

<jsp:include page="/share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="radio"/>
	<jsp:param name="jsFunctionName" value=""/>
</jsp:include>

<SCRIPT LANGUAGE="JavaScript">

var did = "";;

function CheckForm(){
	
	if(!deviceSelectedCheck()){
		alert("请选择设备！");
		return false;
	}

	var oSelect = document.all("device_id");
	
	if(oSelect !=null ) {
		for(var i=0; i<oSelect.length; i++) {
			if(oSelect[i].checked) {
				did = oSelect[i].value;
			}
		}
	}
	if(oSelect.checked){
		did = oSelect.value;
	}
	return true;
}

</SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="paramAllList.jsp" onsubmit="return CheckForm()">
			<div id="filepath" style="display:none"></div>
				<input type="hidden" name="strDevice" value="">
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									参数实例管理
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
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
					<TR bgcolor="#FFFFFF" id="tr1" STYLE="display:">
						<td colspan="4">
							<div id="selectDevice"><span>加载中....</span></div>
						</td>
					</TR>
					<TH colspan="4" align="center">
							SNMP 访问配置
					</TH>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF" id="ntp_type" style="display:">
											    <TD align="right" width="15%">
													配置方式
												</TD>
												<TD colspan=3 width="85%">
													<input type="radio" name="type" id="rd1" class=btn value="1" checked><label for="rd1">TR069</label>&nbsp;
													<input type="radio" name="type" id="rd2" class=btn value="2"><label for="rd2">SNMP</label>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<td align="right">协议版本</td>
												<td colspan="3">
												<input type="radio" name="SNMPVersion" id="SNMPVersionV1" value="1" onclick="snmpVersionChange(false);"><label for="SNMPVersionV1">V1</label>&nbsp;
												<input type="radio" name="SNMPVersion" id="SNMPVersionV2" value="2" onclick="snmpVersionChange(false);"><label for="SNMPVersionV2">V2</label>&nbsp;
												<input type="radio" name="SNMPVersion" id="SNMPVersionV3" checked value="3" onclick="snmpVersionChange(true);"><label for="SNMPVersionV3">V3</label>
												</td>
											</TR>
											<!-- SNMPVersion == 1 (V3) -->
											<TR bgcolor="#FFFFFF" name="snmpv3">
												<td align="right" width="15%">鉴权用户</td>
												<td width="35%"><input type="text" name="SecurityUserName"></td>
												<td align="right" width="15%">鉴权密钥</td>
												<td width="35%"><input type="text" name="AuthPasswd"></td>
											</TR>
											<TR bgcolor="#FFFFFF" name="snmpv3">
												<td align="right">私钥</td>
												<td colspan="3"><input type="text" name="PrivacyPasswd"></td>
											</TR>
											
											<!-- SNMPVersion == 0 (v1/v2) -->
											<TR bgcolor="#FFFFFF" name="notsnmpv3" style="display:none" >
												<td align="right" width="15%">读口令</td>
												<td width="35%"><input type="text" name="ReadOnlyCommunity"></td>
												<td align="right" width="15%">写口令</td>
												<td width="35%"><input type="text" name="ReadWriteCommunity"></td>
											</TR>
											<TR bgcolor="#FFFFFF" name="notsnmpv3" style="display:none">
												<td align="right">Trap口令</td>
												<td colspan="3"><input type="text" name="TrapCommunity"></td>
											</TR>
											
											
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<INPUT TYPE="button" id="bt_set" onclick="ExecMod();" value=" 设 置 " class=btn >
													<INPUT TYPE="button" id="bt_get" onclick="getStatus();" value=" 获 取 " class=btn >
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr001" style="display:none">
												<TH colspan="4">操作结果</TH>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr002" style="display:none">
												<td colspan="4" valign="top" class=column>
												<div id="div_ping"
													style="width: 100%; height: 120px; z-index: 1; top: 100px;"></div>
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
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	//是否展示V3版本的特殊字段
	function snmpVersionChange(flag){
		if(flag==true){
			$("tr[@name='snmpv3']").show();
			$("tr[@name='notsnmpv3']").hide();
		}else{
			$("tr[@name='snmpv3']").hide();
			$("tr[@name='notsnmpv3']").show();
		}
	}
	//SNMP 版本字段检查
	function checkSNMPVersion(){
		var v = $("input[@name='SNMPVersion'][@checked]").val()
		if(v == "3"){
			// V3
			if(document.frm.SecurityUserName.value == ""){
				alert("请输入鉴权用户");
				document.frm.SecurityUserName.focus();
				return false;
			}
			if(document.frm.AuthPasswd.value == ""){
				alert("请输入鉴权密钥");
				document.frm.AuthPasswd.focus();
				return false;
			}
			if(document.frm.PrivacyPasswd.value == ""){
				alert("请输入私钥");
				document.frm.PrivacyPasswd.focus();
				return false;
			}
		}else{
			// V1  V2
			if(document.frm.ReadOnlyCommunity.value == ""){
				alert("请输入读口令");
				document.frm.ReadOnlyCommunity.focus();
				return false;
			}
			if(document.frm.ReadWriteCommunity.value == ""){
				alert("请输入写口令");
				document.frm.ReadWriteCommunity.focus();
				return false;
			}
			if(document.frm.TrapCommunity.value == ""){
				alert("请输入Trap口令");
				document.frm.TrapCommunity.focus();
				return false;
			}
			
		}
		return true;
	}
	function getStatus() {
     		if(CheckForm()){
		        page = "SNMPConfigStatus.jsp?device_id=" +did 
		        	+ "&type=" + (document.getElementById("rd1").checked ? "1" : "2")
		        	+ "&refresh=" + new Date().getTime();
				document.all("div_ping").innerHTML = "正在获取设备SNMP访问配置信息，请耐心等待....";
				document.getElementById("tr001").style.display = "";
				document.getElementById("tr002").style.display = "";
				document.all("childFrm").src = page;
			}else{
				return false;
			}
	}

     function ExecMod(){
     		if(CheckForm()){
     			if(checkSNMPVersion() == false) return false;
     			
		        page = "SNMPConfigSave.jsp?device_id=" +did 
		        	+ "&type=" + (document.getElementById("rd1").checked ? "1" : "2")
		        	+ "&SNMPVersion=" + $("input[@name='SNMPVersion'][@checked]").val()
		        	+ "&SecurityUserName=" + document.frm.SecurityUserName.value 
		        	+ "&AuthPasswd=" + document.frm.AuthPasswd.value 
		        	+ "&PrivacyPasswd=" + document.frm.PrivacyPasswd.value 
		        	+ "&ReadOnlyCommunity=" + document.frm.ReadOnlyCommunity.value 
		        	+ "&ReadWriteCommunity=" + document.frm.ReadWriteCommunity.value 
		        	+ "&TrapCommunity=" + document.frm.TrapCommunity.value 
		        	+ "&refresh=" + new Date().getTime();
				document.all("div_ping").innerHTML = "正在配置设备SNMP访问配置信息，请耐心等待....";
				document.getElementById("tr001").style.display = "";
				document.getElementById("tr002").style.display = "";
				document.all("childFrm").src = page;
			}else{
				return false;
			}	
    }
//-->
</SCRIPT>