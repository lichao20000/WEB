<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ page import="com.linkage.commons.db.PrepareSQL" %>
<%
	request.setCharacterEncoding("GBK");
	
	// ������õ���SNMPЭ����豸

	
	//�豸���� ��ͥOR��ҵ����
	String gw_type= request.getParameter("gw_type");
	
	
//	int GwProtocol = LipossGlobals.getGwProtocol();	
//	if(GwProtocol == 2 && gw_type.equals("2")){
//		response.sendRedirect("AddDeviceForm_snmp.jsp");
//	}
	
	Cursor cursor = null;
	Map fields = null;
	//��ȡ�豸��Ϣ,�༭�豸
	String device_serialnumber = "";//�豸���к�
	String devicetype_id = "";//�豸�ͺ�id
	String cpe_mac = "";//�豸MAC��ַ
	String loopback_ip = "";
	String device_model_id = "";
	//String cpe_currentupdatetime = "";//�豸�������ʱ��
	String software_version = ""; //����汾
	String hardware_version = ""; //Ӳ���汾
	//String cpe_currentstatus = "";//�豸��ǰע��״̬
	//String cpe_operationinfo = "";//�豸��������ʷ��Ϣ
	String vendorId = "";//����oui
	String oui_name = "";
	String device_area_id = "";
	String device_addr = "";
	String gather_id = "";
	String city_id = "";
	String remark = "";
	String device_id_ex = "";
	String buy_time = "";
	String staff_id = user.getAccount();
	String res_pro_id = "";
	String service_year = "";
	String complete_time = "";
	String manage_staff = "";
	String device_name = "";
	String maxenvelopes = "1";
	String retrycount = "0";
	String port = "1";
	String path = "";
	String cpe_username = "itms";
	String cpe_passwd = "itms";
	String acs_username = "hgw";
	String acs_passwd = "hgw";
	String selectOffId = "";
	String selectZoneId = "";
	
	
	//sgw_security������ֶ�
	String snmp_r_passwd = "";
	String snmp_w_passwd = "";
	String snmp_version = "v2";//Э��汾 v1,v2,v3
	String security_username="SMEGWMANAGER";//��Ȩ�û�
	String engine_id = "";//SNMP����
	String context_name = ""; // ContextName
	int security_level = 2; //��ȫ����,1:noAuthNoPriv   2:AuthNoPriv   3:AuthPriv , Ĭ��2
	String auth_protocol = "MD5";//��ȨЭ��
	String auth_passwd = "TelecomSNMP";//��Ȩ��Կ,Ĭ��TelecomSNMP
	String privacy_protocol = "-1";//�ӽ���Э��  DES��IDEA��AES128��AES192��AES256
	String privacy_passwd = "";//˽Կ
	String temp = "";
	String device_type = "";
	//SNMP ����ֶ�
	String snmp_udp = "161";//snmp�˿�
	//SNMP ����ֶ�
	String selected1 = "";
	String selected2 = "";
	String selected3 = "";
	String selected4 = "";
	//�ͻ�id
	String customer_id = "";
	//�����ַ
	String device_url = "";
	
	String device_id = request.getParameter("device_id");
	String strHead = "��������豸";
	if (device_id != null) {
		fields = DeviceAct.getSingleDeviceInfo(request);
		if (fields != null) {
			vendorId = (String) fields.get("vendor_id");
			device_serialnumber = (String) fields
					.get("device_serialnumber");
			device_id_ex = (String) fields.get("device_id_ex");
			cpe_mac = (String) fields.get("cpe_mac");
			loopback_ip = (String) fields.get("loopback_ip");
			//cpe_currentupdatetime = (String)fields.get("cpe_currentupdatetime");
			//cpe_currentstatus = (String)fields.get("cpe_currentstatus");
			//cpe_operationinfo = (String)fields.get("cpe_operationinfo");
			devicetype_id = (String) fields.get("devicetype_id");
			device_model_id = (String) fields.get("device_model_id");
			
			vendorId = oui_name = (String) fields.get("vendor_id");
			//device_area_id = (String)fields.get("area_id");
			device_addr = (String) fields.get("device_addr");
			city_id = (String) fields.get("city_id");
			remark = (String) fields.get("remark");
			buy_time = (String) fields.get("buy_time");
			if (null != buy_time && !"".equals(buy_time)) {
				buy_time = new DateTimeUtil(
						Long.parseLong(buy_time) * 1000).getDate();
			}
			//staff_id = (String)fields.get("staff_id");
			res_pro_id = (String) fields.get("res_pro_id");

			service_year = (String) fields.get("service_year");
			complete_time = (String) fields.get("complete_time");
			if (null != complete_time && !"".equals(complete_time)) {
				complete_time = new DateTimeUtil(Long
						.parseLong(complete_time) * 1000).getDate();
			}
			gather_id = (String) fields.get("gather_id");
			manage_staff = (String) fields.get("manage_staff");

			device_name = (String) fields.get("device_name");
			maxenvelopes = (String) fields.get("maxenvelopes");
			retrycount = (String) fields.get("retrycount");
			port = (String) fields.get("cr_port");
			path = (String) fields.get("cr_path");
			gw_type = (String) fields.get("gw_type");
			acs_username = (String) fields.get("acs_username");
			acs_passwd = (String) fields.get("acs_passwd");
			cpe_username = (String) fields.get("cpe_username");
			cpe_passwd = (String) fields.get("cpe_passwd");
			snmp_udp = (String) fields.get("snmp_udp");
			selectOffId = (String) fields.get("office_id");
			selectZoneId = (String) fields.get("zone_id");
			device_type = (String) fields.get("device_type");
			customer_id = (String) fields.get("customer_id");
			device_url = (String) fields.get("device_url");
		}
		String sql2 = "select * from sgw_security where device_id='" + device_id + "'";
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql2 = "select snmp_r_passwd, snmp_w_passwd, snmp_version, security_username, engine_id, context_name, " +
					"security_level, auth_protocol, auth_passwd, privacy_protocol, privacy_passwd " +
					" from sgw_security where device_id='" + device_id + "'";
		}
		PrepareSQL psql = new PrepareSQL(sql2);
		psql.getSQL();
		Map fields1 = DataSetBean.getRecord(sql2);
		if( fields1 != null && "2".equals(gw_type)){
			
			snmp_r_passwd = (String)fields1.get("snmp_r_passwd");
			snmp_w_passwd = (String)fields1.get("snmp_w_passwd");
			snmp_version = (String)fields1.get("snmp_version");
			security_username=(String)fields1.get("security_username");
			engine_id = (String)fields1.get("engine_id");
			context_name = (String)fields1.get("context_name");
			security_level = Integer.parseInt((String)fields1.get("security_level"));
			temp = (String)fields1.get("auth_protocol");
			if(temp.trim().length()>0 && !temp.equals("null") && !temp.equals("NULL"))
				auth_protocol = temp;
			auth_passwd = (String)fields1.get("auth_passwd");
			temp = (String)fields1.get("privacy_protocol");
			if(temp.trim().length()>0 && !temp.equals("null") && !temp.equals("NULL"))
				privacy_protocol = temp;
			privacy_passwd = (String)fields1.get("privacy_passwd");
		}
	}
	if (service_year == null || "".equals(service_year))
		//service_year = "0";
		service_year = "1";
	/*
	String curCity_ID = curUser.getCityId();
	String curGather_ID = "-1";
	Cursor cursorTmp = DataSetBean.getCursor("select gather_id,descr from tab_process_desc where city_id = '" + curCity_ID +"'");
	Map fieldTmp = cursorTmp.getNext();
	if (fieldTmp != null){
		curGather_ID = (String)fieldTmp.get("gather_id");
	}
	 */
	String strCityList, strVendorList, strDeviceModel, strGroupList, strUserList, strGatherList, strDeviceVersion;
	//if(LipossGlobals.isXJDX()){
		strCityList = DeviceAct.getCityListAll(false, city_id, "", request);
	//}else{
	//	strCityList = DeviceAct
	//	.getCityListSelf(false, city_id, "", request);
	//}
	if (device_id != null) {
		//strCityList = DeviceAct.getCityListSelf(false, curCity_ID, "", request);
		strVendorList = DeviceAct.getVendorList2(true, vendorId, "");
		//strDeviceModel = DeviceAct.getDeviceModelByVendorID(request, device_model_id, "");
		strDeviceModel = DeviceAct.getDeviceModelByVendorID2(request, devicetype_id, "");
	} else {
		//strCityList = DeviceAct.getCityListSelf(false, curCity_ID, "", request);
		strVendorList = DeviceAct.getVendorList(true, vendorId, "");
		//strDeviceModel = DeviceAct.getDeviceModelByVendorID(request, device_model_id, "");
		strDeviceModel = DeviceAct.getDeviceModelByVendorID(request, devicetype_id, "");
	}
	
	//strDeviceVersion = DeviceAct.getDeviceVersionByVendorID(request,
	//		devicetype_id, "");
	
	//strGroupList = DeviceAct.getGroupList(true, "", "");
	strUserList = DeviceAct.getUserOfGroupList(false, manage_staff,
			"per_acc_oid");
	strGatherList = DeviceAct.getGatherList(session, gather_id, "",
			false);
	//strGatherList = DeviceAct.getGatherList(session,curGather_ID,"",false);

	String action = request.getParameter("_action");
	action = action == null ? "add" : "update";
	boolean idEdit = false;//�Ǳ༭��������
	String innerHtml = "";
	if (action.equals("add")) {
		buy_time = complete_time = new DateTimeUtil().getDate();
		/*Date date = new Date();
		buy_time = complete_time = "" + (date.getTime() / 1000);
		date = null;*/
		
//		if(gw_type.equals("2") && GwProtocol == 0){
//			innerHtml = "ȷ����ӵ��豸����<input type=\"radio\" name=\"dev_type\" value=\"1\" onclick=\"changeDev(this);\" checked>TR069�豸"
//								 + "<input type=\"radio\" name=\"dev_type\" value=\"2\" onclick=\"changeDev(this);\">SNMP�豸";
//		}else{
			innerHtml = "��'<font color=\"#FF0000\">*</font>'�ı�������д��ѡ��";
//		}
	} else {
		idEdit = true;
		innerHtml = "��'<font color=\"#FF0000\">*</font>'�ı�������д��ѡ��";
		strHead = "�༭�����豸";
	}
	
	//��ȡ���������б�
	//String strOfficeList = "";
	//if(selectOffId == null || "".equals(selectOffId)){
	//	strOfficeList = DeviceAct.getOfficeList1(false, "", "");
	//}else{
	//	strOfficeList = DeviceAct.getOfficeList1(false, selectOffId, "");
	//}
	
	//��ȡС�������б�
	//String strZoneList = "";
	//if(selectZoneId == null || "".equals(selectZoneId)){
	//	strZoneList = DeviceAct.getZoneList1(false, "", "");
	//}else{
	//	strZoneList = DeviceAct.getZoneList1(false, selectZoneId, "");
	//}
	
	//��ҵ����
	//String entNamesList = HGWUserInfoAct.getENamesList_BBMS(customer_id);
	
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	if(<%=idEdit %>){
		setVersion("<%=snmp_version %>");
		$("#security_level").val("<%=security_level %>");
		$("#auth_protocol").val("<%=auth_protocol %>");
		$("#privacy_protocol").val("<%=privacy_protocol %>");
		setStates("<%=security_level %>");
		if("<%=snmp_version %>"=="v3"){
			snmpVersionChange(true);
		}else{
			snmpVersionChange(false);
		}
	}
});
function CheckForm(){
	var obj = document.frm;
	var gw_type = <%= gw_type%>;
	//alert(gw_type);
	
	if(!IsNull(obj.device_serialnumber.value,'�豸���к�')){
		obj.device_serialnumber.focus();
		return false;
	}
	if(obj.city_id.value=="-1"){
		alert("��ѡ������");
		obj.city_id.focus()
		return false;
	}
	if(!IsNull(obj.maxenvelopes.value,"������Envelopes")){
		obj.maxenvelopes.focus();
		return false;
	}
	if(obj.vendor_id.value=="-1"){
		alert("��ѡ���豸����")
		obj.vendor_id.focus()
		return false;
	}
	if(obj.devicetype_id.value=="-1"){
		alert("��ѡ���豸�ͺ�");
		obj.devicetype_id.focus();
		return false;
	}
	if(!IsNull(obj.staff_id.value,"Ա������")){
		obj.staff_id.focus();
		obj.staff_id.select();
		return false;
	}
	if(!IsNull(obj.port.value,"�˿�")){
		obj.port.focus();
		obj.port.select();
		return false;
	}
	if(obj.gather_id.value=="-1"){
		alert("��ѡ��ɼ���");
		obj.gather_id.focus()
		return false;
	}
	if(!IsNull(obj.buy_time.value,"����ʱ��")){
		obj.buy_time.focus();
		obj.buy_time.select();
		return false;
	}
	if(!IsNull(obj.complete_time.value,"���һ��ά����ʼʱ��")){
		obj.complete_time.focus();
		obj.complete_time.select();
		return false;
	}
	if(obj.complete_time.value < obj.buy_time.value){
		alert("����ʱ���������ά��ʱ��");
		obj.buy_time.focus();
		obj.buy_time.select();
		return false;
	}
	/*
	else if(!IsNull(obj.retrycount.value,"�豸���Դ���")){
		obj.retrycount.focus();
		obj.retrycount.select();
		return false;
	}
	*/
	
	//�������ҵ���أ��ż���SNMP��ز���
	if (gw_type == 2) {
		var snmp_r_passwd = $("#snmp_r_passwd");
		var snmp_w_passwd = $("#snmp_w_passwd");
		var snmp_udp = $("#snmp_udp");
		if(!IsNull(snmp_udp.val(),"SNMP�˿�")){
			snmp_udp.focus();
			return false;
		}
	
		//�����ֶ���֤
		var version = $("input[@name='snmp_version'][@type='radio'][@checked]").val();
		var security_level = $("#security_level").val();
		if(version=="v3"){
			if(security_level==1){
				
			}else{
				var security_username = $("#security_username");
				var engine_id = $("#engine_id");
				var context_name = $("#context_name");
				var auth_protocol = $("#auth_protocol");
				var auth_passwd = $("#auth_passwd");
				if(!IsNull(security_username.val(),"��Ȩ�û�")){
					security_username.focus();
					return false;
				}
				if(auth_protocol.val()=="-1"){
					alert("��ѡ���ȨЭ��");
					auth_protocol.focus();
					return false;
				}
				if(!IsNull(auth_passwd.val(),"��Ȩ��Կ")){
					auth_passwd.focus();
					return false;
				}
				if(security_level==3){
					var privacy_protocol = $("#privacy_protocol");//�ӽ���Э��
					var privacy_passwd = $("#privacy_passwd");//˽Կ
					if(privacy_protocol.val()=="-1"){
						alert("��ѡ��ӽ���Э��");
						privacy_protocol.focus();
						return false;
					}
					if(!IsNull(privacy_passwd.val(),"˽Կ")){
						privacy_passwd.focus();
						return false;
					}
				}
			}
		}else{
			if(!IsNull(snmp_r_passwd.val(),"SNMP������")){
				snmp_r_passwd.focus();
				return false;
			}
			if(!IsNull(snmp_w_passwd.val(),"SNMPд����")){
				snmp_w_passwd.focus();
				return false;
			}
		}
	}
	
	$("input[@name='hidden_buy_time']").val(DateToDes($("input[@name='buy_time']").val()));
	$("input[@name='hidden_complete_time']").val(DateToDes($("input[@name='complete_time']").val()));
	return true;
		
}

function changeDev(param){	
	if(param.value == 2){
		this.location="AddDeviceForm_snmp.jsp";
	}
}
function showChild(parname){
	/*
	if(parname=='city_id'){
		var pid = document.all(parname).value; 
			document.all("childFrm").src= "getOfficeList.jsp?pid=" + pid;
	}
	*/
	
	if(parname=="devicetype_id"){
		var pid = document.all(parname).value; 
		document.all("childFrm").src= "getSnmpSecurityInfo.jsp?device_model_id=" + pid + "&refresh="+(new Date()).getTime();
	}
	
	if(parname=="vendor_id"){	
		var o = $("select[@name='vendor_id']");
		var id = o.val();	
		//var o = $("vendor_id");
		//var id = o.options[o.selectedIndex].value;
		//document.all("childFrm").src = "getVendorDeviceModel.jsp?vendor_id="+ id;
		var url = "getVendorDeviceModel.jsp";
		var pars = "vendor_id=" + id;
		var myAjax
			= new Ajax.Request(
							url,
							{method:"post",parameters:pars,onComplete:getDeviceModel,onFailure:showError}						
						   );
	}else if(parname=="group_oid"){
		var o = document.all("group_oid");
		var id = o.options[o.selectedIndex].value;
		document.all("childFrm").src = "getGroup_oid.jsp?group_oid="+ id;
	}
}
function getDeviceModel(request){
	//$("sp_DeviceModel").innerHTML = request.responseText;
	$("#sp_DeviceModel").html(request.responseText);
	//$("#sp_DeviceVersion").html(request.responseText);
	
}
//Debug
function showError(request){
	//alert(request.responseText);
	$("#debug").html(request.responseText);
}
function resetForm(){
	document.all("frm").reset();
}
function goList(){
	window.location.href="../gwms/resource/queryDevice!deviceOperate.action";
}
function DateToDes(v){
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
function GetDate(second){
	//js�н��ַ���ת��������
	second = eval(second*1000);
	var vDate = new Date(second);
	var y  = vDate.getFullYear();
	var m  = vDate.getMonth()+1;
	var d  = vDate.getDate();

	return y + "-" + m + "-" + d;
}
//--------------------------------------//
//�Ƿ�չʾV3�汾�������ֶ�
function snmpVersionChange(flag){
	if(flag==true){
		$("tr[@name='snmpv3']").show();
		$("tr[@name='notsnmpv3']").hide();
	}else{
		$("tr[@name='snmpv3']").hide();
		$("tr[@name='notsnmpv3']").show();
	}
}
//����snmp�汾
function setVersion(v){
	$("#snmp_version_"+v).attr("checked",true);
}
//�����Ƿ����
function setStates(v){
	if(v==1){
		$("font[@name='state1']").hide();
		states1(false);
		$("font[@name='state2']").hide();
		states2(false);
	}else if(v==2){
		$("font[@name='state1']").show();
		states1(true);
		$("font[@name='state2']").hide();
		states2(false);
	}else if(v==3){
		$("font[@name='state1']").show();
		states1(true);
		$("font[@name='state2']").show();
		states2(true);
	}
}
// �ı�state1���Ƿ����״̬flag=true ����,��֮������
function states1(flag){
	if(flag==true){
		//����
		$("#auth_protocol").attr("disabled","");
		$("#auth_passwd").attr("disabled","");
	}else{
		//������,����ҵ�
		$("#auth_protocol").attr("disabled","true");
		$("#auth_passwd").attr("disabled","true");
	}
}
// �ı�state2���Ƿ����״̬flag=true ����,��֮������
function states2(flag){
	if(flag==true){
		//����
		$("#privacy_protocol").attr("disabled","");
		$("#privacy_passwd").attr("disabled","");
	}else{
		//������,����ҵ�
		$("#privacy_protocol").attr("disabled","true");
		$("#privacy_passwd").attr("disabled","true");
	}
}



  var request = false;
   try {
     request = new XMLHttpRequest();
   } catch (trymicrosoft) {
     try {
       request = new ActiveXObject("Msxml2.XMLHTTP");
       
     } catch (othermicrosoft) {
       try {
         request = new ActiveXObject("Microsoft.XMLHTTP");
       } catch (failed) {
         request = false;
       }  
     }
   }
   if (!request)
     alert("Error initializing XMLHttpRequest!");
     
     
     
   //ajaxһ��ͨ�÷���
	function sendRequest(method,url,object){
		request.open(method, url, true);
		request.onreadystatechange = function(){refreshPage(object);};
		request.send(null);
	}
	function refreshPage(object){
		if (request.readyState == 4) {
    		if (request.status == 200) {
        	object.innerHTML = request.responseText;
			} else
			alert("status is " + request.status);
		}
	}
	
function findCustomer(){
	var cust = document.frm.custName.value;
	var obj = document.all.customer_nameDiv;
	var url = "findCustomer.jsp?type=name&value=" + cust;
	sendRequest("get",url,obj);
}

//--------------------------------------//
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="frm" METHOD="post" ACTION="DeviceSave.jsp" onSubmit="return CheckForm();" target="childFrm">
		<input type="hidden" name="old_device_serialnumber" value="<%=device_serialnumber%>">
		<input type="hidden" name="old_device_name" value="<%=device_name%>">
		<input type="hidden" name="old_city_id" value="<%=city_id%>">
		<input type="hidden" name="old_office_id" value="<%=selectOffId%>">
		<input type="hidden" name="old_zone_id" value="<%=selectZoneId%>">
		<input type="hidden" name="old_vendor_id" value="<%=vendorId%>">
		<input type="hidden" name="old_devicetype_id" value="<%=devicetype_id%>">
		<input type="hidden" name="old_snmp_version" value="<%=snmp_version%>">
		<input type="hidden" name="old_device_type" value="<%=device_type%>">
		<input type="hidden" name="old_snmp_r_passwd" value="<%=snmp_r_passwd%>">
		<input type="hidden" name="old_snmp_w_passwd" value="<%=snmp_w_passwd%>">
		<input type="hidden" name="old_snmp_udp" value="<%=snmp_udp%>">
		<input type="hidden" name="old_port" value="<%=port%>">
		<input type="hidden" name="old_loopback_ip" value="<%=loopback_ip%>">
		<input type="hidden" name="old_cpe_mac" value="<%=cpe_mac%>">
		<input type="hidden" name="old_path" value="<%=path%>">
		<input type="hidden" name="old_device_addr" value="<%=device_addr%>">
		<input type="hidden" name="old_service_year" value="<%=service_year%>">
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="162" align="center" class="title_bigwhite">�豸��Դ</TD>
						<TD>
							<IMG src="../../images/inmp/attention_2.gif" width="15"	height="12"><%=innerHtml%>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>

			<TR>
				<TD bgcolor=#999999>

				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR class=column>
						<TH colspan="4" align="center"><%=strHead%></TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" height="17" width="13%" nowrap>�豸���к�</TD>
						<TD class="column" width="30%">
						<input type="text" name="device_serialnumber" size="30" value="<%=device_serialnumber%>"
							class="bk" readonly>&nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" height="17" width="13%">����</TD>
						<TD height="23" class="column"><%=strCityList%>&nbsp;<font
							color="#FF0000">*</font>
					</TR>

					
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" height="17" width="13%">�豸����</TD>
						<TD class="column" width="29%" nowrap>
						<div><span><%=strVendorList%></span>&nbsp;<font color="#FF0000">*</font>
						</div>
						</TD>
						<TD class=column align="right" height="17" width="13%">�豸�ͺ�(�汾)</TD>
						<TD class="column" width="29%" nowrap>
						<div><span id="sp_DeviceModel"><%=strDeviceModel%></span> <font
							color="#FF0000">*</font></div>
						</TD>
					</TR>
					
					
					<% 
						if ("2".equals(gw_type)) {
							//�������ҵ����
					%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" height="17" width="13%">�豸����</TD>
						<TD class="column" width="29%" nowrap>
							<select class="bk" name="device_type">
								<% if (device_type.equals("Navigator1-1")) { 
										selected1 = "selected" ;
									} else if (device_type.equals("Navigator1-2")) { 
										selected2 = "selected" ;
									} else if (device_type.equals("Navigator2-1")) { 
										selected3 = "selected" ;
									} else if (device_type.equals("Navigator2-2")) { 
										selected4 = "selected" ;
									}
								%>
								<option value="Navigator1-1" <%= selected1%>>Navigator1-1
								<option value="Navigator1-2" <%= selected2%>>Navigator1-2
								<option value="Navigator2-1" <%= selected3%>>Navigator2-1
								<option value="Navigator2-2" <%= selected4%>>Navigator2-2
							</select>
							<font color="#FF0000">*</font>
						</TD>
						<TD class=column align="right" height="17" width="13%"></TD>
						<TD class="column" width="29%" nowrap>
						
						</TD>
					</TR>
					
					<TR class="green_title">
						<TD colspan="4">
							<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR><TD align="center">
								SNMP�����Ϣ 
							</TD>
							</TR>
							</TABLE>
						</TD>
					</TR>
					
					<!-- ������ʼ -->
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" height="17" width="13%">Э��汾</TD>
						<TD class="column" height="17" id="snmp_version">
							<input type="radio" name="snmp_version" id="snmp_version_v1" checked value="v1" onclick="snmpVersionChange(false);">V1
							<input type="radio" name="snmp_version" id="snmp_version_v2" value="v2" onclick="snmpVersionChange(false);">V2
							<input type="radio" name="snmp_version" id="snmp_version_v3" value="v3" onclick="snmpVersionChange(true);">V3
							<font color="#FF0000">*</font>
						</TD>
						<TD class=column align="right" height="17" width="13%"></TD>
						<TD class="column" height="17">
							
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" name="snmpv3" style="display:none">
						<TD class=column align="right" height="17" width="13%">��Ȩ�û�</TD>
						<TD class="column" height="17">
							<input type="text" name="security_username" id="security_username" class="bk" value="<%=security_username %>">
							<font color="#FF0000">*</font>
						</TD>
						<TD class=column align="right" height="17" width="13%">SNMP����</TD>
						<TD class="column" height="17">
							<input type="text" name="engine_id" id="engine_id" class="bk" value="<%=engine_id %>">
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" name="snmpv3" style="display:none">
						
						<TD class=column align="right" height="17" width="13%">SNMP�˿�</TD>
						<TD class="column" height="17">
							<input type="text" name="snmp_udp" id="snmp_udp" class="bk" value="<%=snmp_udp %>" readonly>
							<font color="#FF0000">*</font>
						</TD>
						
						<TD class=column align="right" height="17" width="13%">��ȫ����</TD>
						<TD class="column" height="17">
							<select class="bk" name="security_level" id="security_level" onchange="setStates(this.value);">
								<option value="1">noAuthNoPriv
								<option value="2" selected>AuthNoPriv
								<option value="3">AuthPriv
							</select>
							<font color="#FF0000">*</font>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" name="snmpv3" style="display:none">
						<TD class=column align="right" height="17" width="13%">��ȨЭ��</TD>
						<TD class=column>
							<select class="bk" name="auth_protocol" id="auth_protocol" >
								<option value="-1" >==��ѡ��==
								<option value="MD5" selected>MD5
								<option value="SHA-1">SHA-1
							</select>
							<font name="state1" color="#FF0000">*</font>
						</TD>
						<TD class=column align="right" height="17" width="13%">��Ȩ��Կ</TD>
						<TD class="column" height="17">
							<input type="text" name="auth_passwd" id="auth_passwd" class="bk" value="<%=auth_passwd %>">
							<font name="state1" color="#FF0000">*</font>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" name="snmpv3" style="display:none">
						<TD class=column align="right" height="17" width="13%">�ӽ���Э��</TD>
						<TD class=column>
							<select class="bk" name="privacy_protocol" id="privacy_protocol" disabled="true">
								<option value="-1" selected>==��ѡ��==
								<option value="DES">DES
								<option value="IDEA">IDEA
								<option value="AES128">AES128
								<option value="AES192">AES192
								<option value="AES256">AES256
							</select>
							<font name="state2" color="#FF0000">*</font>
						</TD>
						<TD class=column align="right" height="17" width="13%">˽Կ</TD>
						<TD class="column" height="17">
							<input type="text" name="privacy_passwd" id="privacy_passwd" class="bk" value="<%=privacy_passwd %>" disabled="true">
							<font name="state2" color="#FF0000">*</font>
						</TD>
					</TR>
					<!-- snmp��� -->
					<TR bgcolor="#FFFFFF"  name="notsnmpv3">
						<TD class=column align="right" height="17" width="13%">SNMP������</TD>
						<TD class="column" height="17">
							<input type="text" name="snmp_r_passwd" id="snmp_r_passwd" class="bk" value="<%=snmp_r_passwd %>">
							<font color="#FF0000">*</font>
						</TD>
						<TD class=column align="right" height="17" width="13%">SNMPд����</TD>
						<TD class="column" height="17">
							<input type="text" name="snmp_w_passwd" id="snmp_w_passwd" class="bk" value="<%=snmp_w_passwd %>">
							<font color="#FF0000">*</font>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" style="display:none">
						<TD class=column align="right" height="17" width="13%">ContextName</TD>
						<TD class=column colspan="3">
							<input type="text" name="context_name" id="context_name" class="bk" value="<%=context_name %>">
						</TD>
					</TR>
					<!-- snmp��� -->
					<!-- �������� -->
					<%		
						}
					%>								
					
					<TR class="green_title">
						<TD colspan="4">
							<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR><TD align="center">
								�豸������Ϣ
							</TD>
							</TR>
							</TABLE>
						</TD>
					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" height="17" width="13%">�ɼ���</TD>
						<TD class="column" width="29%"><span><%=strGatherList%></span>&nbsp;<font
							color="#FF0000">*</font></TD>
							
						<input type="hidden" name="old_loopback_ip" value="<%=loopback_ip%>">
						<TD class=column align="right" height="17" width="13%">�豸IP��ַ</TD>
						<TD class="column" width="29%"><input type="text"
							name="loopback_ip" value="<%=loopback_ip%>" maxlength=15 class="bk">
						</TD>
					</TR>

					<TR bgcolor="#FFFFFF" style="display: none">
						<TD class=column align="right" height="17" width="13%">�豸����</TD>
						<TD class="column">
							<input type="text" name="device_name" class="bk" size="30" value="<%=device_name%>">
						</TD>
						<TD class=column align="right" height="17" width="13%">������</TD>
						<TD class="column" width="29%"><input type="text"
							name="maxenvelopes" class="bk" value="<%=maxenvelopes%>">&nbsp;
						<font color="#FF0000">*</font></TD>
					</TR>
					<!-- 
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">
							�����ʶ
						</TD>
						<TD class=column>
							<%//strOfficeList%>
							&nbsp;
						</TD>
						<TD class=column align="right">
							С����ʶ
						</TD>
						<TD class=column>
							<%//strZoneList%>
							&nbsp;
						</TD>
					</TR>
					 -->
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" height="17" width="13%">�豸MAC��</TD>
						<TD class="column" width="29%"><input type="text"
							name="cpe_mac" value="<%=cpe_mac%>" class="bk"></TD>
							
						<TD class=column align="right" height="17" width="13%">
						<div align="right">��������·��</div>
						</TD>
						<TD class="column" width="29%">
							<input type="text" name="path" class="bk" value="<%=path%>" readonly>
							<input type="hidden" name="retrycount" class="bk" value="<%=retrycount%>">
						</TD>
					</TR>
					
					<TR bgcolor="#FFFFFF" style="display:none">
						<TD class=column align="right" height="17" width="13%" nowrap>�豸����������</TD>
						<TD class="column" width="29%" nowrap><span id=strUserList><%=strUserList%></span>
						</TD>
						<TD class=column align="right" height="17" width="13%">���̱���</TD>
						<TD class="column" width="29%"><input type="text"
							name="res_pro_id" class="bk" value="<%=res_pro_id%>"></TD>
					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" height="17" width="13%">ά������</TD>
						<TD class="column" width="29%">
							<select name="service_year" class="bk">
								<option value="0">0��</option>
								<option value="1">1��</option>
								<option value="2">2��</option>
								<option value="3">3��</option>
								<option value="4">4��</option>
								<option value="5">5��</option>
								<option value="6">6��</option>
								<option value="7">7��</option>
								<option value="8">8��</option>
								<option value="9">9��</option>
								<option value="10">10��</option>
							</select>	
						</TD>
						<TD class=column align="right" height="17" width="13%">������</TD>
						<TD class="column" width="29%"><input type="text" readonly
							name="staff_id" class="bk" value="<%=staff_id%>"></TD>
					</TR>

					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" height="17" width="13%">�豸�˿�</TD>
						<TD class="column" width="29%" nowrap><input type="text"
							name="port" class="bk" value="<%=port%>" readonly>&nbsp;<font
							color="#FF0000">*</font></TD>
						<TD class=column align="right" height="17" width="13%">�豸��ϸ��ַ</TD>
						<TD class="column" width="29%"><input type="text"
							name="device_addr" value="<%=device_addr%>" class="bk"></TD>
					</TR>

					<TR bgcolor="#FFFFFF" style="display: none">
						<TD class=column align="right" height="17" width="13%">Ӳ���汾</TD>
						<TD class="column" width="29%"><input type="text"
							name="hardwareVersion" value="<%=hardware_version%>" class="bk">
						</TD>
						<TD class=column align="right" height="17" width="13%">����汾</TD>
						<TD class="column" width="29%"><input type="text"
							name="softwareVersion" value="<%=software_version%>" class="bk">
						</TD>
					</TR>
					
					
					<TR bgcolor="#FFFFFF" style="display:none">
						<TD class=column align="right" height="17" width="13%">ACS�����豸�û���</TD>
						<TD class="column" width="29%">
							<font size="2" color="blue"><%=acs_username%></font>
							<input type="hidden" name="acs_username" value="<%=acs_username%>" class="bk"></TD>
						<TD class=column align="right" height="17" width="13%">ACS�����豸����</TD>
						<TD class="column" width="29%" nowrap>
							<font size="2" color="blue"><%=acs_passwd%></font>
							<input type="hidden" name="acs_passwd" class="bk" value="<%=acs_passwd%>">
						</TD>
					</TR>

					<TR bgcolor="#FFFFFF" style="display:none">
						<TD class=column align="right" height="17" width="13%">�豸����ACS�û���</TD>
						<TD class="column" width="29%">
							<font size="2" color="blue"><%=cpe_username%></font>
							<input type="hidden" name="cpe_username" value="<%=cpe_username%>" class="bk"></TD>
						<TD class=column align="right" height="17" width="13%">�豸����ACS����</TD>
						<TD class="column" width="29%" nowrap>
							<font size="2" color="blue"><%=cpe_passwd%></font>
							<input type="hidden" name="cpe_passwd" class="bk" value="<%=cpe_passwd%>">
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
					<TD class=column align="right" height="17" width="13%">����ʱ��</TD>
						<TD class="column" width="29%">
							<input type="text"
							name="buy_time" class="bk" value="<%=buy_time%>" readOnly>
							<input type="button" value="��" class=btn onClick="showCalendar('day',event)" name="button2">
							<font color="#FF0000">*</font> 
							<input type="hidden" name="hidden_buy_time" class="bk">
						</TD>
						<TD class=column align="right" height="17" width="13%">���һ��ά��ʱ��</TD>
						<TD class="column" width="29%" nowrap>
							<input type="text" name="complete_time" class="bk" value="<%=complete_time%>" readOnly> 
							<input type="button" value="��" class=btn onClick="showCalendar('day',event)" name="button">
							&nbsp;<font color="#FF0000">*</font> 
							<input type="hidden" name="hidden_complete_time" class="bk">
						</TD>
					</TR>
					
					<TR bgcolor="#FFFFFF" style="display:none">
						<TD class=column align="right" height="17" width="13%">��ע</TD>
						<TD class="column" width="29%" nowrap colspan="3">
							<input type="text" name="remark" maxlength=255 class="bk" size=40 value="<%=remark%>">
						</TD>
					</TR>
					<%if ("2".equals(gw_type)){ %>
					<tr bgcolor="#FFFFFF" style="display:none">
						<td class=column align="right" nowrap>�ͻ����Ʋ�ѯ</td>
						<td class="column" nowrap>
						<input type="text" name="custName" class=bk>&nbsp;
						<input type="button" name="srch" value="ģ����ѯ" onclick="findCustomer()" style="border: 1px solid #999999"></td>
						
						<td class=column align="right" nowrap>�ͻ�����</td>
						<td class="column" nowrap>
						<div id="customer_nameDiv"></div></td>
					</tr>
					<TR bgcolor="#FFFFFF">
						<!-- <TD class=column align="right" nowrap>��ҵ����</TD>
						<TD class="column" nowrap></TD> -->
						<TD class=column align="right" nowrap>�����ַ</TD>
						<TD class="column" nowrap><input type="text" name="device_url" class="bk" value="<%=device_url %>"></TD>
						<TD class="column" nowrap></TD>
						<TD class="column" nowrap></TD>
					</TR>
					<%} %>
					<TR>
						<TD colspan="4" align="center" class=green_foot>
							<INPUT TYPE="submit" value=" �� �� " class=btn> &nbsp;&nbsp; 
							<INPUT TYPE="reset" value=" �� д " class=btn> 
							<INPUT TYPE="hidden" name="_action" value="<%=action%>"> 
							<INPUT TYPE="hidden" name="device_id" value="<%=device_id%>">
							<INPUT TYPE="hidden" name="devicetype_id" value="<%=devicetype_id%>">
							<INPUT TYPE="hidden" name="gw_type" value="<%=gw_type%>">
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</FORM>
	<TR>
		<TD>&nbsp;</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME name=childFrm SRC="" STYLE="display:none"></IFRAME>
		&nbsp;</TD>
	</TR>
</TABLE>
<div id="debug"></div>
<SCRIPT LANGUAGE="JavaScript">
<!--
	 
	 document.getElementsByName("service_year")[0].value="<%=service_year%>";
	 
	//ҳ���ʱ��ʱ���ʼ��
	var gw_type = <%= gw_type%>;
	if(gw_type == 2){
		var custId = "<%=customer_id%>";
		sendRequest("get","findCustomer.jsp?type=id&value=" + custId,document.all.customer_nameDiv);
	}

	
	//$("buy_time").value = GetDate($("hidden_buy_time").value);
	//$("complete_time").value = GetDate($("hidden_complete_time").value);
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>
<%
	DeviceAct = null;
	cursor = null;
	fields = null;
	//�༭��ʱ����ؿͻ�����
%>