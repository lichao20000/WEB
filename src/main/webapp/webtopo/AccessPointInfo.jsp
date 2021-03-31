<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.common.database.Cursor, com.linkage.litms.common.database.DataSetBean,java.util.*"%>

<%@ include file="../timelater.jsp"%>
<SCRIPT LANGUAGE="JavaScript" id="idParentFun">
var str_link_auto_id;
var str_vpn_auto_id;
var str_vpn_link_id;
var str_vpn_id;
var m_bShow_pe; 
var m_bShow_ce;
var selected_value;
var selected_value_auto;
var selected_value_ce;
var selected_value_ce_auto;
</SCRIPT>


<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<IFRAME id="childFrm" name="childFrm" style="display:none"></IFRAME>
<IFRAME id="childfrm1" name="childfrm1" style="display:none"></IFRAME>
<IFRAME id="childfrm2" name="childfrm2" style="display:none"></IFRAME>
<IFRAME id="childfrm3" name="childfrm3" style="display:none"></IFRAME>

<%
	String link_auto_id = request.getParameter("link_auto_id");
	String vpn_link_id = "0";
	String vpn_auto_id = "0";
	String vpn_id = "0";
	String strSql="select link_auto_id,vpn_link_id,vpn_auto_id from vpn_auto_link where link_auto_id="+ link_auto_id;
	Cursor cursor = DataSetBean.getCursor(strSql);
	Map fields = cursor.getNext();
	if (fields==null) {
	} else {
		vpn_link_id = (String)fields.get("vpn_link_id");
		vpn_auto_id = (String)fields.get("vpn_auto_id");

		if (vpn_auto_id.equals("0")) {
			%>
			<SCRIPT LANGUAGE="JavaScript">
			<!--
				document.all("childfrm1").src="AccessInfo.jsp?info=1";
			//-->
			</SCRIPT>
			<%
		} else {
			String strSQL="select vpn_id from vpn_auto_customer where vpn_auto_id="+ vpn_auto_id;
			Map fields2 = DataSetBean.getRecord(strSQL);
			//用户信息没有关联
			if(fields2 == null||((String)fields2.get("vpn_id")).equals("0")){
			%>
			<SCRIPT LANGUAGE="JavaScript">
			<!--
			document.all("childfrm1").src="./AccessgetUnrelateUserList.jsp?vpn_auto_id=<%=vpn_auto_id%>"+"&tt="+new Date().getTime();
			//-->
			</SCRIPT>
			<%
			}
			else{//用户信息已关联
				vpn_id = (String)fields2.get("vpn_id");
			%>
			<SCRIPT LANGUAGE="JavaScript">
			<!--
				document.all("childfrm1").src="./AccessgetUserInfo.jsp?vpn_id=<%=vpn_id%>"+"&tt="+new Date().getTime();
			//-->
			</SCRIPT>
			<%
			}
		}
	}
	if (!vpn_id.equals("0")) {
		if(vpn_link_id.equals("0")){
		%>
			<SCRIPT LANGUAGE="JavaScript">
			<!--
				str_link_auto_id = <%=link_auto_id%>;
				str_vpn_id = <%=vpn_id%>;				document.all("childfrm2").src="./AccessUnlinkInfo.jsp?link_auto_id="+str_link_auto_id+"&vpn_id="+str_vpn_id+"&tt="+new Date().getTime();
			//-->
			</SCRIPT>
		<%
		}
		else {
		%>
			<SCRIPT LANGUAGE="JavaScript">
			<!--
				str_vpn_link_id = <%=vpn_link_id%>;
				str_vpn_id = <%=vpn_id%>;	document.all("childfrm2").src="./AccessUpdateVPNLinkInfo.jsp?vpn_link_id="+str_vpn_link_id+"&vpn_id="+str_vpn_id+"&tt="+new Date().getTime();
			//-->
			</SCRIPT>
		<%
		}
	} else {
		%>
		<SCRIPT LANGUAGE="JavaScript">
		<!--
			document.all("childfrm2").src="AccessInfo.jsp?info=2";
		//-->
		</SCRIPT>
		<%
	}
	%>
	<SCRIPT LANGUAGE="JavaScript">
	<!--
	str_link_auto_id = <%=link_auto_id%>;
	document.all("childfrm3").src="./AccessUpdateAutoVPNLinkInfo.jsp?link_auto_id="+str_link_auto_id+"&tt="+new Date().getTime();
	//-->
	</SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
<!--
	function conn_vpn_auto_link(link_auto_id,vpn_link_id,vpn_id) {
		var vDate = new Date().getTime();
		page = "./AccessUpdateUnlinkInfo.jsp?vpn_link_id="+vpn_link_id+"&link_auto_id="+link_auto_id+"&time="+vDate;
		document.all("childfrm2").src=page;
	}
	function reconn_vpn_auto_link(link_auto_id,vpn_link_id,vpn_id) {
		page = "./AccessUnlinkInfo.jsp?link_auto_id="+link_auto_id+"&vpn_link_id="+vpn_link_id+"&vpn_id="+vpn_id;
		document.all("childfrm2").src=page;
	}
	
//-->
</SCRIPT>
<form method=post action="AccessVPNUserInfoUpdate.jsp" name="frm" onsubmit="return CheckForm()" target="childFrm">
<TABLE width="90%" border="0" cellspacing="0" cellpadding="0" align="center" id="tab1">
	<TR>
	<TD>
	<DIV id="userinfo" style="overflow:auto;display:none;">
	</DIV>
	</TD>
	</TR>
</TABLE>
</form>

<TABLE width="90%" border="0" cellspacing="0" cellpadding="0" align="center" id="tab3">
	<TR>
	<TD>
	<DIV id="autolinkinfo" style="overflow:auto;display:none;">
	</DIV>
	</TD>
	</TR>
</TABLE>
<BR>

<TABLE width="90%" border="0" cellspacing="0" cellpadding="0" align="center" id="tab2">
	<TR>
	<TD>
	<DIV id="linkInfo" style="overflow:auto;display:none;">
	</DIV>
	</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<script language="JavaScript">
<!--
function go(v){
	if(v==1){
		var page="AccessgetUnrelateUserList.jsp?vpn_auto_id=<%=vpn_auto_id%>"+"&tt="+new Date().getTime();
		document.all("childFrm").src=page;
	}else if (v==2){	
		var page="AccessUnlinkInfo.jsp?link_auto_id=<%=link_auto_id%>"+"&vpn_auto_id=<%=vpn_auto_id%>"+"&tt="+new Date().getTime();
		document.all("childfrm2").src=page;
	} else{
		var page="AccessgetUserInfo.jsp?vpn_id=<%=vpn_id%>"+"&tt="+new Date().getTime();
		document.all("childFrm").src=page;	
	}
}

function CheckForm() {
	var obj = document.frm;
	if(!IsNull(obj.ext_vpn_id.value,"客户编码")){
		obj.ext_vpn_id.focus();
		obj.ext_vpn_id.select();
		return false;
	} else if(!IsNull(obj.username.value,"客户名称")){
		obj.username.focus();
		obj.username.select();
		return false;
	} else if(!IsNull(obj.vpn_name.value,"VPN名称")){
		obj.vpn_name.focus();
		obj.vpn_name.select();
		return false;
	} else if(!IsNull(obj.linkman.value,"联系人")){
		obj.linkman.focus();
		obj.linkman.select();
		return false;
	} else if(obj.cred_type_id.value!="" && Trim(obj.cred_type_id.value)==""){
		alert("证件类型应为数字");
		obj.cred_type_id.focus();
		obj.cred_type_id.select();
		return false;
	} else if(obj.cred_type_id.value!="" && !IsNumber(obj.cred_type_id.value,"证件类型")){		
		obj.cred_type_id.focus();
		obj.cred_type_id.select();
		return false;
	} else if(obj.user_state.value!="" && Trim(obj.user_state.value)==""){
		alert("用户状态编码应为数字");
		obj.user_state.focus();
		obj.user_state.select();
		return false;
	} else if(obj.user_state.value!="" && !IsNumber(obj.user_state.value,"用户状态编码")){		
		obj.user_state.focus();
		obj.user_state.select();
		return false;
	} else if(!IsNull(obj.complete_time.value,"实际完成时间")){		
		obj.complete_time.focus();
		obj.complete_time.select();
		return false;
	} else {
		document.frm.hidcomplete_time.value = DateToDes(document.frm.complete_time.value);
		return true;
	}	
}

function DateToDes(v){
	//alert("DateToDes in");
	if(v != ""){
		pos = v.indexOf("-");
		if(pos != -1 && pos == 4){
			y = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		pos = v.indexOf("-");
		if(pos != -1){
			m = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		if(v.length>0)
			d = parseInt(v);

		dt = new Date(m+"/"+d+"/"+y);
		var s  = dt.getTime();
		
		return s/1000;
	}
	else
		return 0;
}

//用户信息呈现
function showLinkInfo(v){
	str_vpn_id = <%=vpn_id%>;
	alert("gsj-"+v);
	//新增用户信息时
	if(v=="null"){
		//var vpn_auto_id = document.all.vpn_auto_id.value;
		var page="./AccessUpdateVPNLinkInfo.jsp?vpn_link_id="+str_vpn_link_id+"&vpn_id="+str_vpn_id+"&tt="+new Date().getTime();
	}
	else{
		var page="./AccessUpdateVPNLinkInfo.jsp?vpn_link_id="+v+"&vpn_id="+str_vpn_id+"&tt="+new Date().getTime();
		alert("gsj1-"+page);
	}
	document.all("childfrm2").src=page;
}

//用户信息呈现
function showUserInfo(v){
	//新增用户信息时
	if(v=="null"){
		var vpn_auto_id = document.all.vpn_auto_id.value;
		var page="getUserInfo.jsp?&vpn_auto_id="+vpn_auto_id+"&tt="+new Date().getTime();
	}
	else{
		var page="getUserInfo.jsp?vpn_id="+v+"&tt="+new Date().getTime();		
	}
	document.all("childfrm1").src=page;
	window.location.reload();
}

//待关联用户翻页
function getUserList(offset){
	var page="AccessgetUnrelateUserList.jsp?offset="+offset+"&vpn_auto_id=<%=vpn_auto_id%>"+"&tt="+new Date().getTime();
	document.all("childfrm1").src=page;
}

//关联用户
function relateUser(){
	//确保只有一条的情况
	var relateRadioObj = document.getElementsByName("relateRadio");
	//var vpn_auto_id = document.all.vpn_auto_id.value;
	var flag = true;
	var checkedID;
	
	for(i = 0; i < relateRadioObj.length; i++){
	 if(relateRadioObj[i].checked==true){
		checkedID = relateRadioObj[i].value;
		flag = false;
		break;
	 }
	}
	
	if(flag){
		alert("请选择或新增一个用户信息");
		return;
	}
	
	var page="AccessVPNUserInfoUpdate.jsp?action=relate&vpn_id="+checkedID+"&vpn_auto_id=<%=vpn_auto_id%>"+"&tt="+new Date().getTime();
	document.all("childfrm1").src=page;
}

//重新关联用户
function reRelateUser(){
	go(1);
}

//重新关联链路
function reRelateLink(){
	go(2);
}

//添加用户
function addUser(){
	var vpn_auto_id = document.all.vpn_auto_id.value;
	var page="AccessVPNUserInfoForm.jsp"+"?tt="+new Date().getTime()+"&vpn_auto_id="+vpn_auto_id;
	document.all("childFrm").src=page;
}

//添加链路
function addLink(){
	var vpn_id = <%=vpn_id%>;
	var link_auto_id = <%=link_auto_id%>;
	var page="AccessAddVPNLinkInfo.jsp"+"?vpn_id="+vpn_id+"&link_auto_id="+link_auto_id+"&tt="+new Date().getTime();
	document.all("childfrm2").src=page;
}

function selectObjectCmd_pe(flag) {
	var fuc = document.getElementById;
	if(flag == "1") {
		var obj = document.frm3;
	} else {
		var obj = document.frm2;
	}
	
	var sel_val=obj.pe_port_proto.value;
	//var obj=document.all.frm3.pe_port_proto;

	//alert(fuc('Dot1Q_ISL'));

	//var object_cmd_val= obj.options[obj.selectedIndex].value;
	if(sel_val != null){
		if (sel_val == 1 || sel_val == 2) {
			fuc('Dot1Q_ISL').style.display="";
			fuc('ATM_aal5_1').style.display="none";
			fuc('ATM_aal5_2').style.display="none";
			fuc('ATM_aal5_3').style.display="none";
			fuc('FR').style.display="none";
			if (flag == "1") {
				selected_value_auto = 1;
			} else if (flag == "2") {
				selected_value = 1;
			}
			
		} else if (sel_val == 7) {
			fuc("Dot1Q_ISL").style.display="none";
			fuc("ATM_aal5_1").style.display="";
			fuc("ATM_aal5_2").style.display="";
			fuc("ATM_aal5_3").style.display="";
			fuc("FR").style.display="none";
			if (flag == "1") {
				selected_value_auto = 2;
			} else if (flag == "2") {
				selected_value = 2;
			}
		} else if (sel_val == 3 || sel_val == 4) {
			fuc("Dot1Q_ISL").style.display="none";
			fuc("ATM_aal5_1").style.display="none";
			fuc("ATM_aal5_2").style.display="none";
			fuc("ATM_aal5_3").style.display="none";
			fuc("FR").style.display="";
			if (flag == "1") {
				selected_value_auto = 3;
			} else if (flag == "2") {
				selected_value = 3;
			}
		} else {
			fuc("Dot1Q_ISL").style.display="none";
			fuc("ATM_aal5_1").style.display="none";
			fuc("ATM_aal5_2").style.display="none";
			fuc("ATM_aal5_3").style.display="none";
			fuc("FR").style.display="none";
		}
		
	}
}

function selectObjectCmd_ce(flag_ce) {
	var fuc = document.getElementById;
	if(flag_ce == 1) {
		var obj = document.frm3;
	} else {
		var obj = document.frm2;
	}
	var sel_val=obj.ce_port_proto.value;
	//var obj_ce=document.all.ce_port_proto;
	//var object_cmd_val_ce= obj_ce.options[obj_ce.selectedIndex].value;
	if(sel_val != null){
		if (sel_val == 1 || sel_val == 2) {
			fuc("Dot1Q_ISL_CE").style.display="";
			fuc("ATM_aal5_1_CE").style.display="none";
			fuc("ATM_aal5_2_CE").style.display="none";
			fuc("ATM_aal5_3_CE").style.display="none";
			fuc("FR_CE").style.display="none";
			if (flag_ce == "1") {
				selected_value_ce_auto = 1;
			} else if (flag_ce == "2") {
				selected_value_ce = 1;
			}

		} else if (sel_val == 7) {
			fuc("Dot1Q_ISL_CE").style.display="none";
			fuc("ATM_aal5_1_CE").style.display="";
			fuc("ATM_aal5_2_CE").style.display="";
			fuc("ATM_aal5_3_CE").style.display="";
			fuc("FR_CE").style.display="none";
			if (flag_ce == "1") {
				selected_value_ce_auto = 2;
			} else if (flag_ce == "2") {
				selected_value_ce = 2;
			}
		} else if (sel_val == 3 || sel_val == 4) {
			fuc("Dot1Q_ISL_CE").style.display="none";
			fuc("ATM_aal5_1_CE").style.display="none";
			fuc("ATM_aal5_2_CE").style.display="none";
			fuc("ATM_aal5_3_CE").style.display="none";
			fuc("FR_CE").style.display="";
			if (flag_ce == "1") {
				selected_value_ce_auto = 3;
			} else if (flag_ce == "2") {
				selected_value_ce = 3;
			}
		} else {
			fuc("Dot1Q_ISL_CE").style.display="none";
			fuc("ATM_aal5_1_CE").style.display="none";
			fuc("ATM_aal5_2_CE").style.display="none";
			fuc("ATM_aal5_3_CE").style.display="none";
			fuc("FR_CE").style.display="none";
		}
	}
}

function ce_managed_Selected() {

	var obj_ce_managed=document.all.ce_managed;
	var val_ce_managed= obj_ce_managed.options[obj_ce_managed.selectedIndex].value;
	if(val_ce_managed != null){
		if (val_ce_managed == "0") {
			
		} else if (val_ce_managed == "1") {

		} else {

		}
	}
}

function CheckForm2() {
	var obj = document.frm2;
	if(!IsNull(obj.ext_vpn_link_id.value,"VPN链路统一标识")){
		obj.ext_vpn_link_id.focus();
		obj.ext_vpn_link_id.select();
		return false;
	} else if(!IsNull(obj.subname.value,"VPN用户接入子用户名称")){
		obj.subname.focus();
		obj.subname.select();
		return false;
	} else if(!IsNull(obj.userid.value,"VPN用户接入子用户账号")){
		obj.userid.focus();
		obj.userid.select();
		return false;
	} else if(!IsNull(obj.linkman.value,"VPN用户接入子用户联系人")){
		obj.linkman.focus();
		obj.linkman.select();
		return false;
	} else if (obj.city_id.value == -1) {
		alert("请选择属地标识");
		obj.city_id.focus();
		return false;
	} else if (obj.office_id.value == -1) {
		alert("请选择局向标识");
		obj.office_id.focus();
		return false;
	} else if (obj.zone_id.value == -1) {
		alert("请选择小区标识");
		obj.zone_id.focus();
		return false;
	} else if(!IsNull(obj.linkman.value,"联系人")){
		obj.linkman.focus();
		obj.linkman.select();
		return false;
	} else if(!IsNull(obj.address.value,"接入地点")){
		obj.address.focus();
		obj.address.select();
		return false;
	} else if(!IsNull(obj.accessip.value,"接入设备管理IP")){
		obj.accessip.focus();
		obj.accessip.select();
		return false;
	} else if(!IsNull(obj.pe_name.value,"PE设备名称")){
		obj.pe_name.focus();
		obj.pe_name.select();
		return false;
	} else if(!IsNull(obj.perouterip.value,"PE路由器管理ip")){
		obj.perouterip.focus();
		obj.perouterip.select();
		return false;
	} else if(!IsNull(obj.pe_port.value,"接口数据")){
		obj.pe_port.focus();
		obj.pe_port.select();
		return false;
	} else if(!IsNull(obj.pe_port_fsp.value,"PE(子)接口所在槽位号")){
		obj.pe_port_fsp.focus();
		obj.pe_port_fsp.select();
		return false;
	} else if (obj.pe_port_proto.value==0) {
		alert("请选择PE(子)接口封装协议");
		obj.pe_port_proto.focus();
		return false;
	} else if(!IsNull(obj.peportip.value,"PE(子)界面ip")){
		obj.peportip.focus();
		obj.peportip.select();
		return false;
	} else if(!IsNull(obj.peport_bandwidth.value,"PE(子)界面带宽(bps)")){
		obj.peport_bandwidth.focus();
		obj.peport_bandwidth.select();
		return false;
	} else if(obj.cred_type_id.value!="" && Trim(obj.cred_type_id.value)==""){
		alert("用户状态编码应为数字");
		obj.cred_type_id.focus();
		obj.cred_type_id.select();
		return false;
	} else if(obj.cred_type_id.value!="" && !IsNumber(obj.cred_type_id.value,"VPN用户接入子用户证件类型")){		
		obj.cred_type_id.focus();
		obj.cred_type_id.select();
		return false;
	} else if(obj.user_state.value!="" && Trim(obj.user_state.value)==""){
		alert("VPN用户接入子用户证件类型应为数字");
		obj.user_state.focus();
		obj.user_state.select();
		return false;
	} else if(obj.user_state.value!="" && !IsNumber(obj.user_state.value,"用户状态编码")){		
		obj.user_state.focus();
		obj.user_state.select();
		return false;
	} else if(obj.frame.value!="" && Trim(obj.frame.value)==""){
		alert("VPN用户接入子用户在接入设备上对应的框应为数字");
		obj.frame.focus();
		obj.frame.select();
		return false;
	} else if(obj.frame.value!="" && !IsNumber(obj.frame.value,"VPN用户接入子用户在接入设备上对应的框")){		
		obj.frame.focus();
		obj.frame.select();
		return false;
	} else if(obj.slot.value!="" && Trim(obj.slot.value)==""){
		alert("VPN用户接入子用户在接入设备上对应的槽应为数字");
		obj.slot.focus();
		obj.slot.select();
		return false;
	} else if(obj.slot.value!="" && !IsNumber(obj.slot.value,"VPN用户接入子用户在接入设备上对应的槽")){		
		obj.slot.focus();
		obj.slot.select();
		return false;
	} else if(obj.port.value!="" && Trim(obj.port.value)==""){
		alert("VPN用户接入子用户在接入设备上对应的端口应为数字");
		obj.port.focus();
		obj.port.select();
		return false;
	} else if(obj.port.value!="" && !IsNumber(obj.port.value,"VPN用户接入子用户在接入设备上对应的端口")){		
		obj.port.focus();
		obj.port.select();
		return false;
	} else if(obj.speed.value!="" && Trim(obj.speed.value)==""){
		alert("VPN用户接入子用户接入速率应为数字");
		obj.speed.focus();
		obj.speed.select();
		return false;
	} else if(obj.speed.value!="" && !IsNumber(obj.speed.value,"VPN用户接入子用户接入速率")){		
		obj.speed.focus();
		obj.speed.select();
		return false;
	} else if(obj.ifindex.value!="" && Trim(obj.ifindex.value)==""){
		alert("VPN用户接入子用户在接入设备上对应端口的端口索引应为数字");
		obj.ifindex.focus();
		obj.ifindex.select();
		return false;
	} else if(obj.ifindex.value!="" && !IsNumber(obj.ifindex.value,"VPN用户接入子用户在接入设备上对应端口的端口索引")){		
		obj.ifindex.focus();
		obj.ifindex.select();
		return false;
	} else if(obj.ifprivateindex.value!="" && Trim(obj.ifprivateindex.value)==""){
		alert("接入子用户在接入设备上对应端口的私有索引应为数字");
		obj.ifprivateindex.focus();
		obj.ifprivateindex.select();
		return false;
	} else if(obj.ifprivateindex.value!="" && !IsNumber(obj.ifprivateindex.value,"接入子用户在接入设备上对应端口的私有索引")){		
		obj.ifprivateindex.focus();
		obj.ifprivateindex.select();
		return false;
	} else if(obj.peport_bandwidth.value!="" && Trim(obj.peport_bandwidth.value)==""){
		alert("PE（子）接口带宽应为数字");
		obj.peport_bandwidth.focus();
		obj.peport_bandwidth.select();
		return false;
	} else if(obj.peport_bandwidth.value!="" && !IsNumber(obj.peport_bandwidth.value,"PE（子）接口带宽")){		
		obj.peport_bandwidth.focus();
		obj.peport_bandwidth.select();
		return false;
	} else if(obj.pe_vlan_id.value!="" && Trim(obj.pe_vlan_id.value)==""){
		alert("Pe_vlan_id应为数字");
		obj.pe_vlan_id.focus();
		obj.pe_vlan_id.select();
		return false;
	} else if(obj.pe_vlan_id.value!="" && !IsNumber(obj.pe_vlan_id.value,"Pe_vlan_id")){		
		obj.pe_vlan_id.focus();
		obj.pe_vlan_id.select();
		return false;
	} else if(obj.pe_vcd.value!="" && Trim(obj.pe_vcd.value)==""){
		alert("Pe_vcd应为数字");
		obj.pe_vcd.focus();
		obj.pe_vcd.select();
		return false;
	} else if(obj.pe_vcd.value!="" && !IsNumber(obj.pe_vcd.value,"Pe_vcd")){		
		obj.pe_vcd.focus();
		obj.pe_vcd.select();
		return false;
	} else if(obj.pe_vpi.value!="" && Trim(obj.pe_vpi.value)==""){
		alert("pe_vpi应为数字");
		obj.pe_vpi.focus();
		obj.pe_vpi.select();
		return false;
	} else if(obj.pe_vpi.value!="" && !IsNumber(obj.pe_vpi.value,"pe_vpi")){		
		obj.pe_vpi.focus();
		obj.pe_vpi.select();
		return false;
	} else if(obj.pe_vci.value!="" && Trim(obj.pe_vci.value)==""){
		alert("pe_vci应为数字");
		obj.pe_vci.focus();
		obj.pe_vci.select();
		return false;
	} else if(obj.pe_vci.value!="" && !IsNumber(obj.pe_vci.value,"pe_vci")){		
		obj.pe_vci.focus();
		obj.pe_vci.select();
		return false;
	} else if(obj.pe_dlci.value!="" && Trim(obj.pe_dlci.value)==""){
		alert("pe_dlci应为数字");
		obj.pe_dlci.focus();
		obj.pe_dlci.select();
		return false;
	} else if(obj.pe_dlci.value!="" && !IsNumber(obj.pe_dlci.value,"pe_dlci")){		
		obj.pe_dlci.focus();
		obj.pe_dlci.select();
		return false;
	}  else if(obj.bridgegroup.value!="" && Trim(obj.bridgegroup.value)==""){
		alert("桥接组应为数字");
		obj.bridgegroup.focus();
		obj.bridgegroup.select();
		return false;
	} else if(obj.bridgegroup.value!="" && !IsNumber(obj.bridgegroup.value,"桥接组")){		
		obj.bridgegroup.focus();
		obj.bridgegroup.select();
		return false;
	} else {
		if (m_bShow_pe==true) {
			if(!IsNull(obj.pe_name_bak.value,"备Pe设备名称")){
				obj.pe_name_bak.focus();
				obj.pe_name_bak.select();
				return false;
			} else if(!IsNull(obj.perouterip_bak.value,"备PE路由器管理ip")){
				obj.perouterip_bak.focus();
				obj.perouterip_bak.select();
				return false;
			} else if(!IsNull(obj.pe_port_bak.value,"备pe(子)接口名称")){
				obj.pe_port_bak.focus();
				obj.pe_port_bak.select();
				return false;
			} else if(!IsNull(obj.pe_port_fsp_bak.value,"备PE(子)接口所在槽位号")){
				obj.pe_port_fsp_bak.focus();
				obj.pe_port_fsp_bak.select();
				return false;
			} else if(!IsNull(obj.pe_port_proto_bak.value,"备PE(子)接口封装协议")){
				obj.pe_port_proto_bak.focus();
				obj.pe_port_proto_bak.select();
				return false;
			} else if(!IsNull(obj.peportip_bak.value,"备PE(子)接口ip")){
				obj.peportip_bak.focus();
				obj.peportip_bak.select();
				return false;
			} else if(!IsNull(obj.peport_bandwidth_bak.value,"备PE（子）接口带宽")){
				obj.peport_bandwidth_bak.focus();
				obj.peport_bandwidth_bak.select();
				return false;
			} else if(!IsNull(obj.pe_vlan_id_bak.value,"pe_vlan_id_bak")){
				obj.pe_vlan_id_bak.focus();
				obj.pe_vlan_id_bak.select();
				return false;
			} else if(!IsNull(obj.pe_vcd_bak.value,"pe_vcd_bak")){
				obj.pe_vcd_bak.focus();
				obj.pe_vcd_bak.select();
				return false;
			} else if(!IsNull(obj.pe_vpi_bak.value,"pe_vpi_bak")){
				obj.pe_vpi_bak.focus();
				obj.pe_vpi_bak.select();
				return false;
			} else if(!IsNull(obj.pe_vci_bak.value,"pe_vci_bak")){
				obj.pe_vci_bak.focus();
				obj.pe_vci_bak.select();
				return false;
			} else if(!IsNull(obj.pe_dlci_bak.value,"pe_dlci_bak")){
				obj.pe_dlci_bak.focus();
				obj.pe_dlci_bak.select();
				return false;
			} else if(obj.peport_bandwidth_bak.value!="" && Trim(obj.peport_bandwidth_bak.value)==""){
				alert("备PE（子）接口带宽应为数字");
				obj.peport_bandwidth_bak.focus();
				obj.peport_bandwidth_bak.select();
				return false;
			} else if(obj.peport_bandwidth_bak.value!="" && !IsNumber(obj.peport_bandwidth_bak.value,"备PE（子）接口带宽")){		
				obj.peport_bandwidth_bak.focus();
				obj.peport_bandwidth_bak.select();
				return false;
			} else if(obj.pe_vlan_id_bak.value!="" && Trim(obj.pe_vlan_id_bak.value)==""){
				alert("Pe_vlan_id_bak应为数字");
				obj.pe_vlan_id_bak.focus();
				obj.pe_vlan_id_bak.select();
				return false;
			} else if(obj.pe_vlan_id_bak.value!="" && !IsNumber(obj.pe_vlan_id_bak.value,"Pe_vlan_id_bak")){		
				obj.pe_vlan_id_bak.focus();
				obj.pe_vlan_id_bak.select();
				return false;
			} else if(obj.pe_vcd_bak.value!="" && Trim(obj.pe_vcd_bak.value)==""){
				alert("Pe_vcd_bak应为数字");
				obj.pe_vcd_bak.focus();
				obj.pe_vcd_bak.select();
				return false;
			} else if(obj.pe_vcd_bak.value!="" && !IsNumber(obj.pe_vcd_bak.value,"Pe_vcd_bak")){		
				obj.pe_vcd_bak.focus();
				obj.pe_vcd_bak.select();
				return false;
			} else if(obj.pe_vpi_bak.value!="" && Trim(obj.pe_vpi_bak.value)==""){
				alert("pe_vpi_bak应为数字");
				obj.pe_vpi_bak.focus();
				obj.pe_vpi_bak.select();
				return false;
			} else if(obj.pe_vpi_bak.value!="" && !IsNumber(obj.pe_vpi_bak.value,"pe_vpi_bak")){		
				obj.pe_vpi_bak.focus();
				obj.pe_vpi_bak.select();
				return false;
			} else if(obj.pe_vci_bak.value!="" && Trim(obj.pe_vci_bak.value)==""){
				alert("pe_vci_bak应为数字");
				obj.pe_vci_bak.focus();
				obj.pe_vci_bak.select();
				return false;
			} else if(obj.pe_vci_bak.value!="" && !IsNumber(obj.pe_vci_bak.value,"pe_vci")){		
				obj.pe_vci_bak.focus();
				obj.pe_vci_bak.select();
				return false;
			} else if(obj.pe_dlci_bak.value!="" && Trim(obj.pe_dlci_bak.value)==""){
				alert("pe_dlci_bak应为数字");
				obj.pe_dlci_bak.focus();
				obj.pe_dlci_bak.select();
				return false;
			} else if(obj.pe_dlci_bak.value!="" && !IsNumber(obj.pe_dlci_bak.value,"pe_dlci_bak")){		
				obj.pe_dlci_bak.focus();
				obj.pe_dlci_bak.select();
				return false;
			} else if(obj.ce_vlan_id.value!="" && Trim(obj.ce_vlan_id.value)==""){
				alert("ce_vlan_id应为数字");
				obj.ce_vlan_id.focus();
				obj.ce_vlan_id.select();
				return false;
			} else if(obj.ce_vlan_id.value!="" && !IsNumber(obj.ce_vlan_id.value,"ce_vlan_id")){		
				obj.ce_vlan_id.focus();
				obj.ce_vlan_id.select();
				return false;
			} else if(obj.ce_vcd.value!="" && Trim(obj.ce_vcd.value)==""){
				alert("ce_vcd应为数字");
				obj.ce_vcd.focus();
				obj.ce_vcd.select();
				return false;
			} else if(obj.ce_vcd.value!="" && !IsNumber(obj.ce_vcd.value,"ce_vcd")){		
				obj.ce_vcd.focus();
				obj.ce_vcd.select();
				return false;
			} else if(obj.ce_vpi.value!="" && Trim(obj.ce_vpi.value)==""){
				alert("ce_vpi应为数字");
				obj.ce_vpi.focus();
				obj.ce_vpi.select();
				return false;
			} else if(obj.ce_vpi.value!="" && !IsNumber(obj.ce_vpi.value,"ce_vpi")){		
				obj.ce_vpi.focus();
				obj.ce_vpi.select();
				return false;
			} else if(obj.ce_vci.value!="" && Trim(obj.ce_vci.value)==""){
				alert("ce_vci应为数字");
				obj.ce_vci.focus();
				obj.ce_vci.select();
				return false;
			} else if(obj.ce_vci.value!="" && !IsNumber(obj.ce_vci.value,"ce_vci")){		
				obj.ce_vci.focus();
				obj.ce_vci.select();
				return false;
			} else if(obj.ce_dlci.value!="" && Trim(obj.ce_dlci.value)==""){
				alert("ce_dlci应为数字");
				obj.ce_dlci.focus();
				obj.ce_dlci.select();
				return false;
			} else if(obj.ce_dlci.value!="" && !IsNumber(obj.ce_dlci.value,"ce_dlci")){		
				obj.ce_dlci.focus();
				obj.ce_dlci.select();
				return false;
			}
		}
		if (m_bShow_ce==true) {
		if(!IsNull(obj.ce_name.value,"CE名称")){
			obj.ce_name.focus();
			obj.ce_name.select();
			return false;
		} else if(!IsNull(obj.ce_ip.value,"CE管理地址")){
			obj.ce_ip.focus();
			obj.ce_ip.select();
			return false;
		} else if(!IsNull(obj.ce_port.value,"CE（子）接口名称")){
			obj.ce_port.focus();
			obj.ce_port.select();
			return false;
		} else if(!IsNull(obj.ce_port_fsp.value,"CE（子）接口所在槽位号")){
			obj.ce_port_fsp.focus();
			obj.ce_port_fsp.select();
			return false;
		} else if (obj.ce_port_proto.value==0) {
			alert("请选择CE(子)接口封装协议");
			obj.ce_port_proto.focus();
			return false;
		} else if(!IsNull(obj.ce_port_ip.value,"CE（子）接口地址")){
			obj.ce_port_ip.focus();
			obj.ce_port_ip.select();
			return false;
		} else if (selected_value_ce == 1) {
			if(!IsNull(obj.ce_vlan_id.value,"ce_vlan_id")){
				obj.ce_vlan_id.focus();
				obj.ce_vlan_id.select();
				return false;
			}
		} else if (selected_value_ce == 2) {
			if(!IsNull(obj.ce_vcd.value,"ce_vcd")){
				obj.ce_vcd.focus();
				obj.ce_vcd.select();
				return false;
			} else if(!IsNull(obj.ce_vpi.value,"ce_vpi")){
				obj.ce_vpi.focus();
				obj.ce_vpi.select();
				return false;
			} else if(!IsNull(obj.ce_vci.value,"ce_vci")){
				obj.ce_vci.focus();
				obj.ce_vci.select();
				return false;
			}

		} else if (selected_value_ce == 3) {
				if(!IsNull(obj.ce_dlci.value,"ce_dlci")){
				obj.ce_dlci.focus();
				obj.ce_dlci.select();
				return false;
			} 
		} else if(obj.peport_bandwidth_bak.value!="" && Trim(obj.peport_bandwidth_bak.value)==""){
			alert("备PE（子）接口带宽应为数字");
			obj.peport_bandwidth_bak.focus();
			obj.peport_bandwidth_bak.select();
			return false;
			} else if(obj.peport_bandwidth_bak.value!="" && !IsNumber(obj.peport_bandwidth_bak.value,"备PE（子）接口带宽")){		
				obj.peport_bandwidth_bak.focus();
				obj.peport_bandwidth_bak.select();
				return false;
			} else if(obj.pe_vlan_id_bak.value!="" && Trim(obj.pe_vlan_id_bak.value)==""){
				alert("Pe_vlan_id_bak应为数字");
				obj.pe_vlan_id_bak.focus();
				obj.pe_vlan_id_bak.select();
				return false;
			} else if(obj.pe_vlan_id_bak.value!="" && !IsNumber(obj.pe_vlan_id_bak.value,"Pe_vlan_id_bak")){		
				obj.pe_vlan_id_bak.focus();
				obj.pe_vlan_id_bak.select();
				return false;
			} else if(obj.pe_vcd_bak.value!="" && Trim(obj.pe_vcd_bak.value)==""){
				alert("Pe_vcd_bak应为数字");
				obj.pe_vcd_bak.focus();
				obj.pe_vcd_bak.select();
				return false;
			} else if(obj.pe_vcd_bak.value!="" && !IsNumber(obj.pe_vcd_bak.value,"Pe_vcd_bak")){		
				obj.pe_vcd_bak.focus();
				obj.pe_vcd_bak.select();
				return false;
			} else if(obj.pe_vpi_bak.value!="" && Trim(obj.pe_vpi_bak.value)==""){
				alert("pe_vpi_bak应为数字");
				obj.pe_vpi_bak.focus();
				obj.pe_vpi_bak.select();
				return false;
			} else if(obj.pe_vpi_bak.value!="" && !IsNumber(obj.pe_vpi_bak.value,"pe_vpi_bak")){		
				obj.pe_vpi_bak.focus();
				obj.pe_vpi_bak.select();
				return false;
			} else if(obj.pe_vci_bak.value!="" && Trim(obj.pe_vci_bak.value)==""){
				alert("pe_vci_bak应为数字");
				obj.pe_vci_bak.focus();
				obj.pe_vci_bak.select();
				return false;
			} else if(obj.pe_vci_bak.value!="" && !IsNumber(obj.pe_vci_bak.value,"pe_vci")){		
				obj.pe_vci_bak.focus();
				obj.pe_vci_bak.select();
				return false;
			} else if(obj.pe_dlci_bak.value!="" && Trim(obj.pe_dlci_bak.value)==""){
				alert("pe_dlci_bak应为数字");
				obj.pe_dlci_bak.focus();
				obj.pe_dlci_bak.select();
				return false;
			} else if(obj.pe_dlci_bak.value!="" && !IsNumber(obj.pe_dlci_bak.value,"pe_dlci_bak")){		
				obj.pe_dlci_bak.focus();
				obj.pe_dlci_bak.select();
				return false;
			} 
	}  
	if (selected_value == 1) {
		if(!IsNull(obj.pe_vlan_id.value,"pe_vlan_id")){
			obj.pe_vlan_id.focus();
			obj.pe_vlan_id.select();
			return false;
		}
	} else if (selected_value == 2) {
		if(!IsNull(obj.pe_vpi.value,"pe_vpi")){
			obj.pe_vpi.focus();
			obj.pe_vpi.select();
			return false;
		} else if(!IsNull(obj.pe_vci.value,"pe_vci")){
			obj.pe_vci.focus();
			obj.pe_vci.select();
			return false;
		}

	} else if (selected_value == 3) {
			if(!IsNull(obj.pe_dlci.value,"pe_dlci")){
				obj.pe_dlci.focus();
				obj.pe_dlci.select();
				return false;
			}
	} else {
		document.frm2.hidClosetime.value = DateToDes(document.frm2.closetime.value);
		document.frm2.hidOpentime.value = DateToDes(document.frm2.opentime.value);
		return true;
	}
	}
}
	
function EC(leaf,obj){
	pobj = obj.offsetParent;
	oTRs = pobj.getElementsByTagName("TR");

	for(var i=0; i<oTRs.length; i++){
		if(oTRs[i].leaf == leaf && leaf == "pe"){
			m_bShow_pe = (oTRs[i].style.display=="none");
			oTRs[i].style.display = m_bShow_pe?"":"none";
		} else if(oTRs[i].leaf == leaf && leaf == "ce"){
			m_bShow_ce = (oTRs[i].style.display=="none");
			oTRs[i].style.display = m_bShow_ce?"":"none";
		}
	}
	var sobj = obj.getElementsByTagName("IMG");
	
	if(m_bShow_pe) {
		sobj[0].src = "images/up_enabled.gif";
		obj.style.backgroundColor = "#A0C6E5";
	} else if(m_bShow_ce) {
		sobj[0].src = "images/up_enabled.gif";
		obj.style.backgroundColor = "#A0C6E5";
	}
	else if(!m_bShow_pe && leaf == "pe") {
		sobj[0].src = "images/down_enabled.gif";
		obj.style.backgroundColor = "#cccccc";
	} else if (!m_bShow_ce && leaf == "ce") {
		sobj[0].src = "images/down_enabled.gif";
		obj.style.backgroundColor = "#cccccc";
	}
}
	function CheckForm3() {
		var obj = document.frm3;
		if(!IsNull(obj.vrf_name.value,"Vrf name")){
			obj.vrf_name.focus();
			obj.vrf_name.select();
			return false;
		} else if(!IsNull(obj.rd.value,"RD")){
			obj.rd.focus();
			obj.rd.select();
			return false;
		} else if(!IsNull(obj.export_rt.value,"Export RT")){
			obj.export_rt.focus();
			obj.export_rt.select();
			return false;
		} else if(!IsNull(obj.import_rt.value,"Import RT")){
			obj.import_rt.focus();
			obj.import_rt.select();
			return false;
		} else if(!IsNull(obj.pe_device_id.value,"PE路由器设备编码")){
			obj.pe_device_id.focus();
			obj.pe_device_id.select();
			return false;
		} else if(!IsNull(obj.pe_port.value,"接口数据")){
			obj.pe_port.focus();
			obj.pe_port.select();
			return false;
		} else if(!IsNull(obj.pe_port_fsp.value,"PE(子)接口所在槽位号")){
			obj.pe_port_fsp.focus();
			obj.pe_port_fsp.select();
			return false;
		} else if(obj.maxroute.value!="" && Trim(obj.maxroute.value)==""){
			alert("maxroute应为数字");
			obj.maxroute.focus();
			obj.maxroute.select();
			return false;
		} else if(obj.maxroute.value!="" && !IsNumber(obj.maxroute.value,"maxroute")){		
			obj.maxroute.focus();
			obj.maxroute.select();
			return false;
		} else if(obj.maxroutethred.value!="" && Trim(obj.maxroutethred.value)==""){
			alert("maxroutethred应为数字");
			obj.maxroutethred.focus();
			obj.maxroutethred.select();
			return false;
		} else if(obj.maxroutethred.value!="" && !IsNumber(obj.maxroutethred.value,"maxroutethred")){		obj.maxroutethred.focus();
			obj.maxroutethred.select();
			return false;
		} else if(obj.peport_bandwidth.value!="" && Trim(obj.peport_bandwidth.value)==""){
			alert("PE（子）接口带宽应为数字");
			obj.peport_bandwidth.focus();
			obj.peport_bandwidth.select();
			return false;
		} else if(obj.peport_bandwidth.value!="" && !IsNumber(obj.peport_bandwidth.value,"PE（子）接口带宽")){		
			obj.peport_bandwidth.focus();
			obj.peport_bandwidth.select();
			return false;
		} else if(obj.pe_vlan_id.value!="" && Trim(obj.pe_vlan_id.value)==""){
			alert("Pe_vlan_id应为数字");
			obj.pe_vlan_id.focus();
			obj.pe_vlan_id.select();
			return false;
		} else if(obj.pe_vlan_id.value!="" && !IsNumber(obj.pe_vlan_id.value,"Pe_vlan_id")){		
			obj.pe_vlan_id.focus();
			obj.pe_vlan_id.select();
			return false;
		} else if(obj.pe_vcd.value!="" && Trim(obj.pe_vcd.value)==""){
			alert("Pe_vcd应为数字");
			obj.pe_vcd.focus();
			obj.pe_vcd.select();
			return false;
		} else if(obj.pe_vcd.value!="" && !IsNumber(obj.pe_vcd.value,"Pe_vcd")){		
			obj.pe_vcd.focus();
			obj.pe_vcd.select();
			return false;
		} else if(obj.pe_vpi.value!="" && Trim(obj.pe_vpi.value)==""){
			alert("pe_vpi应为数字");
			obj.pe_vpi.focus();
			obj.pe_vpi.select();
			return false;
		} else if(obj.pe_vpi.value!="" && !IsNumber(obj.pe_vpi.value,"pe_vpi")){		
			obj.pe_vpi.focus();
			obj.pe_vpi.select();
			return false;
		} else if(obj.pe_vci.value!="" && Trim(obj.pe_vci.value)==""){
			alert("pe_vci应为数字");
			obj.pe_vci.focus();
			obj.pe_vci.select();
			return false;
		} else if(obj.pe_vci.value!="" && !IsNumber(obj.pe_vci.value,"pe_vci")){		
			obj.pe_vci.focus();
			obj.pe_vci.select();
			return false;
		} else if(obj.pe_dlci.value!="" && Trim(obj.pe_dlci.value)==""){
			alert("pe_dlci应为数字");
			obj.pe_dlci.focus();
			obj.pe_dlci.select();
			return false;
		} else if(obj.pe_dlci.value!="" && !IsNumber(obj.pe_dlci.value,"pe_dlci")){		
			obj.pe_dlci.focus();
			obj.pe_dlci.select();
			return false;
		}  else if(obj.bridgegroup.value!="" && Trim(obj.bridgegroup.value)==""){
			alert("桥接组应为数字");
			obj.bridgegroup.focus();
			obj.bridgegroup.select();
			return false;
		} else if(obj.bridgegroup.value!="" && !IsNumber(obj.bridgegroup.value,"桥接组")){		
			obj.bridgegroup.focus();
			obj.bridgegroup.select();
			return false;
		} else {
			if (m_bShow_pe==true) {
				if(!IsNull(obj.pe_name_bak.value,"备Pe设备名称")){
					obj.pe_name_bak.focus();
					obj.pe_name_bak.select();
					return false;
				} else if(!IsNull(obj.perouterip_bak.value,"备PE路由器管理ip")){
					obj.perouterip_bak.focus();
					obj.perouterip_bak.select();
					return false;
				} else if(!IsNull(obj.pe_port_bak.value,"备pe(子)接口名称")){
					obj.pe_port_bak.focus();
					obj.pe_port_bak.select();
					return false;
				} else if(!IsNull(obj.pe_port_fsp_bak.value,"备PE(子)接口所在槽位号")){
					obj.pe_port_fsp_bak.focus();
					obj.pe_port_fsp_bak.select();
					return false;
				} else if(!IsNull(obj.pe_port_proto_bak.value,"备PE(子)接口封装协议")){
					obj.pe_port_proto_bak.focus();
					obj.pe_port_proto_bak.select();
					return false;
				} else if(!IsNull(obj.pe_device_id_bak.value,"备PE(子)接口ip")){
					obj.peportip_bak.focus();
					obj.peportip_bak.select();
					return false;
				} else if(!IsNull(obj.peport_bandwidth_bak.value,"备PE（子）接口带宽")){
					obj.peport_bandwidth_bak.focus();
					obj.peport_bandwidth_bak.select();
					return false;
				} else if(!IsNull(obj.pe_vlan_id_bak.value,"pe_vlan_id_bak")){
					obj.pe_vlan_id_bak.focus();
					obj.pe_vlan_id_bak.select();
					return false;
				} else if(!IsNull(obj.pe_vcd_bak.value,"pe_vcd_bak")){
					obj.pe_vcd_bak.focus();
					obj.pe_vcd_bak.select();
					return false;
				} else if(!IsNull(obj.pe_vpi_bak.value,"pe_vpi_bak")){
					obj.pe_vpi_bak.focus();
					obj.pe_vpi_bak.select();
					return false;
				} else if(!IsNull(obj.pe_vci_bak.value,"pe_vci_bak")){
					obj.pe_vci_bak.focus();
					obj.pe_vci_bak.select();
					return false;
				} else if(!IsNull(obj.pe_dlci_bak.value,"pe_dlci_bak")){
					obj.pe_dlci_bak.focus();
					obj.pe_dlci_bak.select();
					return false;
				} else if(obj.peport_bandwidth_bak.value!="" && Trim(obj.peport_bandwidth_bak.value)==""){
					alert("备PE（子）接口带宽应为数字");
					obj.peport_bandwidth_bak.focus();
					obj.peport_bandwidth_bak.select();
					return false;
				} else if(obj.peport_bandwidth_bak.value!="" && !IsNumber(obj.peport_bandwidth_bak.value,"备PE（子）接口带宽")){		
					obj.peport_bandwidth_bak.focus();
					obj.peport_bandwidth_bak.select();
					return false;
				} else if(obj.pe_vlan_id_bak.value!="" && Trim(obj.pe_vlan_id_bak.value)==""){
					alert("Pe_vlan_id_bak应为数字");
					obj.pe_vlan_id_bak.focus();
					obj.pe_vlan_id_bak.select();
					return false;
				} else if(obj.pe_vlan_id_bak.value!="" && !IsNumber(obj.pe_vlan_id_bak.value,"Pe_vlan_id_bak")){		
					obj.pe_vlan_id_bak.focus();
					obj.pe_vlan_id_bak.select();
					return false;
				} else if(obj.pe_vcd_bak.value!="" && Trim(obj.pe_vcd_bak.value)==""){
					alert("Pe_vcd_bak应为数字");
					obj.pe_vcd_bak.focus();
					obj.pe_vcd_bak.select();
					return false;
				} else if(obj.pe_vcd_bak.value!="" && !IsNumber(obj.pe_vcd_bak.value,"Pe_vcd_bak")){		
					obj.pe_vcd_bak.focus();
					obj.pe_vcd_bak.select();
					return false;
				} else if(obj.pe_vpi_bak.value!="" && Trim(obj.pe_vpi_bak.value)==""){
					alert("pe_vpi_bak应为数字");
					obj.pe_vpi_bak.focus();
					obj.pe_vpi_bak.select();
					return false;
				} else if(obj.pe_vpi_bak.value!="" && !IsNumber(obj.pe_vpi_bak.value,"pe_vpi_bak")){		
					obj.pe_vpi_bak.focus();
					obj.pe_vpi_bak.select();
					return false;
				} else if(obj.pe_vci_bak.value!="" && Trim(obj.pe_vci_bak.value)==""){
					alert("pe_vci_bak应为数字");
					obj.pe_vci_bak.focus();
					obj.pe_vci_bak.select();
					return false;
				} else if(obj.pe_vci_bak.value!="" && !IsNumber(obj.pe_vci_bak.value,"pe_vci")){		
					obj.pe_vci_bak.focus();
					obj.pe_vci_bak.select();
					return false;
				} else if(obj.pe_dlci_bak.value!="" && Trim(obj.pe_dlci_bak.value)==""){
					alert("pe_dlci_bak应为数字");
					obj.pe_dlci_bak.focus();
					obj.pe_dlci_bak.select();
					return false;
				} else if(obj.pe_dlci_bak.value!="" && !IsNumber(obj.pe_dlci_bak.value,"pe_dlci_bak")){		
					obj.pe_dlci_bak.focus();
					obj.pe_dlci_bak.select();
					return false;
				} 
			}
			if (m_bShow_ce==true&&obj.ce_managed.value=="1") {
			if(!IsNull(obj.ce_name.value,"CE名称")){
				obj.ce_name.focus();
				obj.ce_name.select();
				return false;
			} else if(!IsNull(obj.ce_ip.value,"CE管理地址")){
				obj.ce_ip.focus();
				obj.ce_ip.select();
				return false;
			} else if(!IsNull(obj.ce_port.value,"CE（子）接口名称")){
				obj.ce_port.focus();
				obj.ce_port.select();
				return false;
			} else if(!IsNull(obj.ce_port_fsp.value,"CE（子）接口所在槽位号")){
				obj.ce_port_fsp.focus();
				obj.ce_port_fsp.select();
				return false;
			} else if (obj.ce_port_proto.value==0) {
				alert("请选择CE(子)接口封装协议");
				obj.ce_port_proto.focus();
				return false;
			} else if(!IsNull(obj.ce_port_ip.value,"CE（子）接口地址")){
				obj.ce_port_ip.focus();
				obj.ce_port_ip.select();
				return false;
			} else if (selected_value_ce_auto == 1) {
				if(!IsNull(obj.ce_vlan_id.value,"ce_vlan_id")){
					obj.ce_vlan_id.focus();
					obj.ce_vlan_id.select();
					return false;
				}
			} else if (selected_value_ce_auto == 2) {
				if(!IsNull(obj.ce_vcd.value,"ce_vcd")){
					obj.ce_vcd.focus();
					obj.ce_vcd.select();
					return false;
				} else if(!IsNull(obj.ce_vpi.value,"ce_vpi")){
					obj.ce_vpi.focus();
					obj.ce_vpi.select();
					return false;
				} else if(!IsNull(obj.ce_vci.value,"ce_vci")){
					obj.ce_vci.focus();
					obj.ce_vci.select();
					return false;
				}

			} else if (selected_value_ce_auto == 3) {
					if(!IsNull(obj.ce_dlci.value,"ce_dlci")){
					obj.ce_dlci.focus();
					obj.ce_dlci.select();
					return false;
				} 
			} else if(obj.ce_vlan_id.value!="" && Trim(obj.ce_vlan_id.value)==""){
					alert("ce_vlan_id应为数字");
					obj.ce_vlan_id.focus();
					obj.ce_vlan_id.select();
					return false;
				} else if(obj.ce_vlan_id.value!="" && !IsNumber(obj.ce_vlan_id.value,"ce_vlan_id")){		
					obj.ce_vlan_id.focus();
					obj.ce_vlan_id.select();
					return false;
				} else if(obj.ce_vcd.value!="" && Trim(obj.ce_vcd.value)==""){
					alert("ce_vcd应为数字");
					obj.ce_vcd.focus();
					obj.ce_vcd.select();
					return false;
				} else if(obj.ce_vcd.value!="" && !IsNumber(obj.ce_vcd.value,"ce_vcd")){		
					obj.ce_vcd.focus();
					obj.ce_vcd.select();
					return false;
				} else if(obj.ce_vpi.value!="" && Trim(obj.ce_vpi.value)==""){
					alert("ce_vpi应为数字");
					obj.ce_vpi.focus();
					obj.ce_vpi.select();
					return false;
				} else if(obj.ce_vpi.value!="" && !IsNumber(obj.ce_vpi.value,"ce_vpi")){		
					obj.ce_vpi.focus();
					obj.ce_vpi.select();
					return false;
				} else if(obj.ce_vci.value!="" && Trim(obj.ce_vci.value)==""){
					alert("ce_vci应为数字");
					obj.ce_vci.focus();
					obj.ce_vci.select();
					return false;
				} else if(obj.ce_vci.value!="" && !IsNumber(obj.ce_vci.value,"ce_vci")){		
					obj.ce_vci.focus();
					obj.ce_vci.select();
					return false;
				} else if(obj.ce_dlci.value!="" && Trim(obj.ce_dlci.value)==""){
					alert("ce_dlci应为数字");
					obj.ce_dlci.focus();
					obj.ce_dlci.select();
					return false;
				} else if(obj.ce_dlci.value!="" && !IsNumber(obj.ce_dlci.value,"ce_dlci")){		
					obj.ce_dlci.focus();
					obj.ce_dlci.select();
					return false;
				}
		}  
		if (selected_value_auto == 1) {
			if(!IsNull(obj.pe_vlan_id.value,"pe_vlan_id")){
				obj.pe_vlan_id.focus();
				obj.pe_vlan_id.select();
				return false;
			}
		} else if (selected_value_auto == 2) {
			if(!IsNull(obj.pe_vpi.value,"pe_vpi")){
				obj.pe_vpi.focus();
				obj.pe_vpi.select();
				return false;
			} else if(!IsNull(obj.pe_vci.value,"pe_vci")){
				obj.pe_vci.focus();
				obj.pe_vci.select();
				return false;
			}

		} else if (selected_value_auto == 3) {
				if(!IsNull(obj.pe_dlci.value,"pe_dlci")){
					obj.pe_dlci.focus();
					obj.pe_dlci.select();
					return false;
				}
		} else {
				return true;
			}
		}
	}
//-->
</script>
