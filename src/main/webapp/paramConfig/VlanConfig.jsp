<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
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

var did;

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
									VLAN 配置。
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
					<TR bgcolor="#FFFFFF" >
						<td colspan="4">
							<div id="selectDevice"><span>加载中....</span></div>
						</td>
					</TR>
					<TR>
						<TH colspan="4" align="center">
							VLAN 配置
						</TH>
					</TR>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF" id="ntp_type" style="display:">
											    <TD align="right" width="15%">
													配置方式:
												</TD>
												<TD colspan=3 width="85%">
													<input type="radio" name="type" id="rd1" class=btn value="1" checked><label for="rd1">TR069</label>&nbsp;
													<input type="radio" name="type" id="rd2" class=btn value="2"><label for="rd2">SNMP</label>
												</TD>
											</TR>
											<tr bgcolor="#FFFFFF"><td colspan="4" height="20"></td></tr>
											<tr bgcolor="#FFFFFF" id="divVlanCount" style="display:none">
												<td align="right">配置的vlan数量</td>
												<td colspan="3"><input type="text" name="vlanCount" value="" readOnly></td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<table id="para" width="100%" border=0 cellspacing=1 cellpadding=2 style="display:none">
													</table>
												</td>
											</tr>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<INPUT TYPE="button" id="bt_set" style="display:" onclick="ExecMod();" value=" 设 置 " class=btn >
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
	function getStatus() {
     		if(CheckForm()){
		        page = "VlanConfigStatus.jsp?device_id=" +did 
		        	+ "&type=" + (document.getElementById("rd1").checked ? "1" : "2")
		        	+ "&refresh=" + new Date().getTime();
				document.all("div_ping").innerHTML = "正在获取设备VLAN信息，请耐心等待....";
				document.getElementById("tr001").style.display = "";
				document.getElementById("tr002").style.display = "";
				document.all("childFrm").src = page;
			}else{
				return false;
			}
	}

     function ExecMod(){
     		if(CheckForm()){
			//if (true) {
				var vlanIdx = "";
		        var VLANID = "";
		        var VLanInterface = "";
		        var VlanName = "";
		        var IPInterfaceIPAddress = "";
		        var IPInterfaceSubnetMask = "";
		        if (true) {
		        //生成参数
		        if (document.frm.vlan_idx != null){
		        	if (typeof(document.frm.vlan_idx.value) == 'undefined'){
		        		for (var i=0;i<document.frm.vlan_idx.length;i++){
							vlanIdx += document.frm.vlan_idx[i].value + "|";
		        			VLANID += document.frm.vlan_id[i].value + "|";
		        			VLanInterface += document.frm.VLan_Interface[i].value + "|";
		        			VlanName += document.frm.vlan_name[i].value + "|";
		        			IPInterfaceIPAddress += document.frm.vlan_ip[i].value + "|";
		        			IPInterfaceSubnetMask += document.frm.vlan_mask[i].value + "|";
		        		}
		        	}
		        	else{
						vlanIdx += document.frm.vlan_idx.value + "|";
		        		VLANID = document.frm.vlan_id.value + "|";
		        		VLanInterface = document.frm.VLan_Interface.value + "|";
		        		VlanName = document.frm.vlan_name.value + "|";
		        		IPInterfaceIPAddress = document.frm.vlan_ip.value + "|";
		        		IPInterfaceSubnetMask = document.frm.vlan_mask.value + "|";
		        	}
		        }
		        else{
		        	alert('没有可配置的vlan！');
		        	return false;
		        }
		        }
		        page = "VlanConfigSave.jsp?device_id=" +did 
		        	+ "&type=" + (document.getElementById("rd1").checked ? "1" : "2")
		        	+ "&vlanIdx=" + vlanIdx
		        	+ "&VLANID=" + VLANID
		        	+ "&VLanInterface=" + VLanInterface
		        	+ "&VlanName=" + VlanName
		        	+ "&IPInterfaceIPAddress=" + IPInterfaceIPAddress
		        	+ "&IPInterfaceSubnetMask=" + IPInterfaceSubnetMask
		        	+ "&refresh=" + new Date().getTime();
		        
		        page2 = "VlanConfigSave.jsp?device_id=26"
		        	+ "&type=" + "1"
		        	+ "&vlanIdx=" + "22|33"
		        	+ "&VLANID=" + "**"
		        	+ "&VLanInterface=" + "**"
		        	+ "&VlanName=" + "**"
		        	+ "&IPInterfaceIPAddress=" + "**"
		        	+ "&IPInterfaceSubnetMask=" + "**"
		        	+ "&refresh=" + new Date().getTime();
				//alert(page);
				document.all("div_ping").innerHTML = "正在配置设备VLAN信息，请耐心等待....";
				document.all("childFrm").src = page;
			}else{
				return false;
			}	
    }
    
    function setParaHtml(htmlStr){
    	document.all("para").style.display = "";
    	$("#para").html("<TR bgcolor='#FFFFFF'><th class='column'>索引</th><th class='column'>vlan ID</th><th class='column'>VLAN绑定端口列表</th><th class='column'>vlan名称</th><th class='column'>VLAN IP地址</th><th class='column'>VLAN 子网掩码</th></TR>"+htmlStr);
    }
//-->
</SCRIPT>