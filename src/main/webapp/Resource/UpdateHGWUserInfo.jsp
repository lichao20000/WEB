<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.litms.common.util.*,com.linkage.litms.LipossGlobals"%>
<%@page import="java.util.*"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />

<%@ include file="../timelater.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
var m_bShow_pe; 
var m_bShow_ce;
var selected_value;
var selected_value_ce;
</SCRIPT>


<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
	//��ȡ����װ����ShortName
	String shortName = LipossGlobals.getLipossProperty("InstArea.ShortName");
	if (shortName == null) shortName = "";

	String username = "";
	String passwd = "";
	String city_id = "";
	String cotno = "";
	String bill_type_id = "";
	String next_bill_type_id = "";
	String cust_type_id = "";
	String user_type_id = "";
	String bindtype = "";
	String virtualnum = "";
	//String numcharacter = "0";
	String access_style_id = "";
	String aut_flag = "";
	String service_set = "";
	String realname = "";
	String sex = "";
	String cred_type_id = "";
	String credno = "";
	String address = "0";
	//String office_id = "0";
	//String zone_id = "0";
	String access_kind_id = "0";
	String trade_id = "0";
	String licenceregno = "0";
	String occupation_id = "";
	String education_id = "";
	String vipcardno = "";
	String contractno = "";
	String linkman = "";
	String linkman_credno = "";
	String linkphone = "0";
	String linkaddress = "0";
	String mobile = "0";
	String email = "0";
	String agent = "0";
	String agent_credno = "0";
	String agentphone = "-1";
	String adsl_res = "";
	String adsl_card = "";
	String adsl_dev = "";
	String adsl_ser = "";
	String isrepair = "";
	String bandwidth = "";
	String ipaddress = "0";
	String overipnum = "0";
	String ipmask = "0";
	String gateway = "0";
	String macaddress = "0";
	//String device_id = "";
	String device_ip = "";
	String device_shelf = "0";
	String device_frame = "0";
	String device_slot = "";
	String device_port = "";
	String basdevice_id = "";
	String basdevice_ip = "0";
	String basdevice_shelf = "0";
	String basdevice_frame = "0";
	//String basdevice_slot = "0";
	String basdevice_port = "0";
	String vlanid = "";
	String workid = "";
	String user_state = "0";
	String staff_id = "0";
	String remark = "";
	String phonenumber = "";
	String cableid = "";
	String bwlevel = "0";
	String vpiid = "0";
	String vciid = "0";
	String adsl_hl = "0";
	String userline = "0";
	String dslamserialno = "";
	String opmode = "";
	String maxattdnrate = "0";
	String upwidth = "0";

	String opendate_date = "";
	String onlinedate_date = "";
	//String pausedate_date = "";
	String closedate_date = "";
	String updatetime_date = "";
	String movedate_date = "";
	String dealdate_date = "";
    String time = "";   
	String opendate = "0";
	String onlinedate = "0";
	String pausedate = "0";
	String closedate = "0";
	String updatetime = "0";
	String movedate = "0";
	String dealdate = "0";
	String max_user_number = "0";
	String service_id = "0";
	String wan_type = "1";
	String oui = "";
	String device_serialnumber = "";
	Date curDtae = new Date();
	String user_id = request.getParameter("user_id");
	String gather_id = request.getParameter("gather_id");

	String strSQL = "select * from tab_hgwcustomer where user_id="
			+ user_id + " and gather_id='" + gather_id + "'";
	Map fields = DataSetBean.getRecord(strSQL);
	if (fields != null) {
		opendate = (String) fields.get("opendate");
		onlinedate = (String) fields.get("onlinedate");
		pausedate = (String) fields.get("pausedate");
		closedate = (String) fields.get("closedate");
		updatetime = (String) fields.get("updatetime");
		movedate = (String) fields.get("movedate");
		dealdate = (String) fields.get("dealdate");

		if ((opendate != null) && (opendate.length() != 0)
		&& (!"0".equals(opendate))) {
			opendate_date = new DateTimeUtil(
			Long.parseLong(opendate) * 1000).getDate();
		}
		if ((onlinedate != null) && (onlinedate.length() != 0)
		&& (!"0".equals(onlinedate))) {
			onlinedate_date = new DateTimeUtil(Long
			.parseLong(onlinedate) * 1000).getDate();
		}
		if ((pausedate != null) && (pausedate.length() != 0)
		&& (!"0".equals(pausedate))) {
			//pausedate_date= new DateTimeUtil(Long.parseLong(pausedate) * 1000).getDate();
		}
		if ((closedate != null) && (closedate.length() != 0)
		&& (!"0".equals(closedate))) {
			closedate_date = new DateTimeUtil(
			Long.parseLong(closedate) * 1000).getDate();
		}
		if ((updatetime != null) && (updatetime.length() != 0)
		&& (!"0".equals(updatetime))) {
			updatetime_date = new DateTimeUtil(Long
			.parseLong(updatetime) * 1000).getDate();
		}
		if ((movedate != null) && (movedate.length() != 0)
		&& (!"0".equals(movedate))) {
			movedate_date = new DateTimeUtil(
			Long.parseLong(movedate) * 1000).getDate();
		}
		if ((dealdate != null) && (dealdate.length() != 0)
		&& (!"0".equals(dealdate))) {
			dealdate_date = new DateTimeUtil(
			Long.parseLong(dealdate) * 1000).getDate();
			time = new DateTimeUtil(
			Long.parseLong(dealdate) * 1000).getTime();

		}

		username = (String) fields.get("username".toLowerCase());
		passwd = (String) fields.get("passwd".toLowerCase());
		city_id = (String) fields.get("city_id".toLowerCase());
		cotno = (String) fields.get("cotno".toLowerCase());
		bill_type_id = (String) fields
		.get("bill_type_id".toLowerCase());
		next_bill_type_id = (String) fields.get("next_bill_type_id"
		.toLowerCase());
		cust_type_id = (String) fields
		.get("cust_type_id".toLowerCase());
		user_type_id = (String) fields
		.get("user_type_id".toLowerCase());
		bindtype = (String) fields.get("bindtype".toLowerCase());
		virtualnum = (String) fields.get("virtualnum".toLowerCase());
		//numcharacter = (String)fields.get("numcharacter".toLowerCase());
		access_style_id = (String) fields.get("access_style_id"
		.toLowerCase());
		aut_flag = (String) fields.get("aut_flag".toLowerCase());
		service_set = (String) fields.get("service_set".toLowerCase());
		realname = (String) fields.get("realname".toLowerCase());
		sex = (String) fields.get("sex".toLowerCase());
		cred_type_id = (String) fields
		.get("cred_type_id".toLowerCase());
		credno = (String) fields.get("credno".toLowerCase());
		address = (String) fields.get("address".toLowerCase());
		//office_id = (String)fields.get("office_id".toLowerCase());
		//zone_id = (String)fields.get("zone_id".toLowerCase());
		access_kind_id = (String) fields.get("access_kind_id"
		.toLowerCase());
		trade_id = (String) fields.get("trade_id".toLowerCase());
		licenceregno = (String) fields
		.get("licenceregno".toLowerCase());
		occupation_id = (String) fields.get("occupation_id"
		.toLowerCase());
		education_id = (String) fields
		.get("education_id".toLowerCase());
		vipcardno = (String) fields.get("vipcardno".toLowerCase());
		contractno = (String) fields.get("contractno".toLowerCase());
		linkman = (String) fields.get("linkman".toLowerCase());
		linkman_credno = (String) fields.get("linkman_credno"
		.toLowerCase());
		linkphone = (String) fields.get("linkphone".toLowerCase());
		linkaddress = (String) fields.get("linkaddress".toLowerCase());
		mobile = (String) fields.get("mobile".toLowerCase());
		email = (String) fields.get("email".toLowerCase());
		agent = (String) fields.get("agent".toLowerCase());
		agent_credno = (String) fields
		.get("agent_credno".toLowerCase());
		agentphone = (String) fields.get("agentphone".toLowerCase());
		adsl_res = (String) fields.get("adsl_res".toLowerCase());
		adsl_card = (String) fields.get("adsl_card".toLowerCase());
		adsl_dev = (String) fields.get("adsl_dev".toLowerCase());
		adsl_ser = (String) fields.get("adsl_ser".toLowerCase());
		isrepair = (String) fields.get("isrepair".toLowerCase());
		bandwidth = (String) fields.get("bandwidth".toLowerCase());
		ipaddress = (String) fields.get("ipaddress".toLowerCase());
		overipnum = (String) fields.get("overipnum".toLowerCase());
		ipmask = (String) fields.get("ipmask".toLowerCase());
		gateway = (String) fields.get("gateway".toLowerCase());
		macaddress = (String) fields.get("macaddress".toLowerCase());
		//device_id = (String)fields.get("device_id".toLowerCase());
		device_ip = (String) fields.get("device_ip".toLowerCase());
		device_shelf = (String) fields
		.get("device_shelf".toLowerCase());
		device_frame = (String) fields
		.get("device_frame".toLowerCase());
		device_slot = (String) fields.get("device_slot".toLowerCase());
		device_port = (String) fields.get("device_port".toLowerCase());
		basdevice_id = (String) fields
		.get("basdevice_id".toLowerCase());
		basdevice_ip = (String) fields
		.get("basdevice_ip".toLowerCase());
		basdevice_shelf = (String) fields.get("basdevice_shelf"
		.toLowerCase());
		basdevice_frame = (String) fields.get("basdevice_frame"
		.toLowerCase());
		//basdevice_slot = (String)fields.get("basdevice_slot".toLowerCase());
		basdevice_port = (String) fields.get("basdevice_port"
		.toLowerCase());
		vlanid = (String) fields.get("vlanid".toLowerCase());
		workid = (String) fields.get("workid".toLowerCase());
		user_state = (String) fields.get("user_state".toLowerCase());
		staff_id = (String) fields.get("staff_id".toLowerCase());
		remark = (String) fields.get("remark".toLowerCase());
		phonenumber = (String) fields.get("phonenumber".toLowerCase());
		cableid = (String) fields.get("cableid".toLowerCase());
		bwlevel = (String) fields.get("bwlevel".toLowerCase());
		vpiid = (String) fields.get("vpiid".toLowerCase());
		vciid = (String) fields.get("vciid".toLowerCase());
		adsl_hl = (String) fields.get("adsl_hl".toLowerCase());
		userline = (String) fields.get("userline".toLowerCase());
		dslamserialno = (String) fields.get("dslamserialno"
		.toLowerCase());
		opmode = (String) fields.get("opmode".toLowerCase());
		maxattdnrate = (String) fields
		.get("maxattdnrate".toLowerCase());
		upwidth = (String) fields.get("upwidth".toLowerCase());
		max_user_number = (String) fields.get("max_user_number"
		.toLowerCase());
		service_id = (String) fields.get("serv_type_id".toLowerCase());
		wan_type = (String) fields.get("wan_type".toLowerCase());
		oui = (String) fields.get("oui".toLowerCase());
		device_serialnumber = (String) fields.get("device_serialnumber"
		.toLowerCase());
	}

	String strGatherList = DeviceAct.getGatherList(session, gather_id,
			"", false);
	String strCityList = HGWUserInfoAct.getCityListSelf(false, city_id,
			"", request);
			
	String servTypeList = HGWUserInfoAct.getServTypeList(1);
	//�豸�б�
	Cursor cursor_device = HGWUserInfoAct.getDeviceByArea(request);
	Map fields_device = cursor_device.getNext();

	//������еķ�����Ϣ
	Cursor cursor_service = HGWUserInfoAct.getServiceInfo();
	Map fields_service = cursor_service.getNext();
	
//	Cursor cursor_tmp = DataSetBean.getCursor("select * from tab_service where flag = 1");
//	String service_list = FormUtil.createListBox(cursor_tmp,"service_id", "service_name", false, service_id, "some_service");
%>
<SCRIPT LANGUAGE="JavaScript">
<!--

	function CheckForm() {
		var obj = document.frm;
		
		//var retValue = window.showModalDialog("./checkDevice.jsp?oui="+document.frm.oui.value+"&vender="+document.frm.vender.value,window,"dialogLeft:1px;dialogTop:1px;dialogHeight:0px;dialogWidth:0px");
		
		//if (typeof(retValue) != 'undefined'){
		//	document.frm.device_id.value = retValue;
		//}
		
		//if (document.frm.device_id.value == ""){
		//	if (!confirm("�ü�ͥ�����豸�����ڣ��Ƿ��������")){
		//		alert("����ȡ��!");
		//		obj.oui.focus();
		//		return false;
		//	}
		//}
		
		if(!IsNull(obj.username.value,"�û��ʻ�")){
			obj.username.focus();
			obj.username.select();
			return false;
		} else if (obj.some_service.value == "-1") {
			alert("��ѡ��ҵ�����ͣ�");
			return false;
		} else if (obj.wan_type.value == "-1") {
			alert("��ѡ���������ͣ�");
			return false;
		} else if(!IsNull(obj.passwd.value,"�û�����")){
			obj.passwd.focus();
			obj.passwd.select();
			return false;
		} else if(!IsNull(obj.validate_passwd.value,"������֤")){
			obj.validate_passwd.focus();
			obj.validate_passwd.select();
			return false;
		} else if(obj.validate_passwd.value != obj.passwd.value){
			alert("�������֤���벻һ��!");
			obj.validate_passwd.focus();
			obj.validate_passwd.select();
			return false;
		} else if (obj.city_id.value == -1) {
			alert("��ѡ�����ر�ʶ");
			obj.city_id.focus();
			return false;
		}else if(obj.vpiid.value==""){
		     alert("������VPI");
		     obj.vpiid.focus();
		     return false;
		}else if(obj.vciid.value==""){
		     alert("������VCI");
		     obj.vciid.focus();
		     return false;
		} else if(obj.virtualnum.value!="" && Trim(obj.virtualnum.value)==""){
			alert("�������ӦΪ����");
			obj.virtualnum.focus();
			obj.virtualnum.select();
			return false;
		} else if(obj.virtualnum.value!="" && !IsNumber(obj.virtualnum.value,"�������")){		
			obj.virtualnum.focus();
			obj.virtualnum.select();
			return false;
		} else if(obj.overipnum.value!="" && Trim(obj.overipnum.value)==""){
			alert("����IP��ַ������ӦΪ����");
			obj.overipnum.focus();
			obj.overipnum.select();
			return false;
		} else if(obj.overipnum.value!="" && !IsNumber(obj.overipnum.value,"����IP��ַ������")){		
			obj.overipnum.focus();
			obj.overipnum.select();
			return false;
		} else if(obj.device_shelf.value!="" && Trim(obj.device_shelf.value)==""){
			alert("�����豸���ܺ�ӦΪ����");
			obj.device_shelf.focus();
			obj.device_shelf.select();
			return false;
		} else if(obj.device_shelf.value!="" && !IsNumber(obj.device_shelf.value,"�����豸���ܺ�")){		
			obj.device_shelf.focus();
			obj.device_shelf.select();
			return false;
		} else if(obj.device_frame.value!="" && Trim(obj.device_frame.value)==""){
			alert("�����豸���ӦΪ����");
			obj.device_frame.focus();
			obj.device_frame.select();
			return false;
		} else if(obj.device_frame.value!="" && !IsNumber(obj.device_frame.value,"�����豸���")){		
			obj.device_frame.focus();
			obj.device_frame.select();
			return false;
		} else if(obj.device_slot.value!="" && Trim(obj.device_slot.value)==""){
			alert("�����豸��λ��ӦΪ����");
			obj.device_slot.focus();
			obj.device_slot.select();
			return false;
		} else if(obj.device_slot.value!="" && !IsNumber(obj.device_slot.value,"�����豸��λ��")){		
			obj.device_slot.focus();
			obj.device_slot.select();
			return false;
		} else if(obj.device_port.value!="" && Trim(obj.device_port.value)==""){
			alert("�����豸�˿�ӦΪ����");
			obj.device_port.focus();
			obj.device_port.select();
			return false;
		} else if(obj.device_port.value!="" && !IsNumber(obj.device_port.value,"�����豸�˿�")){		
			obj.device_port.focus();
			obj.device_port.select();
			return false;
		} else if(obj.basdevice_shelf.value!="" && Trim(obj.basdevice_shelf.value)==""){
			alert("BAS�����豸���ܺ�ӦΪ����");
			obj.basdevice_shelf.focus();
			obj.basdevice_shelf.select();
			return false;
		} else if(obj.basdevice_shelf.value!="" && !IsNumber(obj.basdevice_shelf.value,"BAS�����豸���ܺ�")){		
			obj.basdevice_shelf.focus();
			obj.basdevice_shelf.select();
			return false;
		} else if(obj.basdevice_frame.value!="" && Trim(obj.basdevice_frame.value)==""){
			alert("BAS�����豸���ӦΪ����");
			obj.basdevice_frame.focus();
			obj.basdevice_frame.select();
			return false;
		} else if(obj.basdevice_frame.value!="" && !IsNumber(obj.basdevice_frame.value,"BAS�����豸���")){		
			obj.basdevice_frame.focus();
			obj.basdevice_frame.select();
			return false;
		} else if(obj.basdevice_slot.value!="" && Trim(obj.basdevice_slot.value)==""){
			alert("BAS�����豸��λ��ӦΪ����");
			obj.basdevice_slot.focus();
			obj.basdevice_slot.select();
			return false;
		} else if(obj.basdevice_slot.value!="" && !IsNumber(obj.basdevice_slot.value,"BAS�����豸��λ��")){		
			obj.basdevice_slot.focus();
			obj.basdevice_slot.select();
			return false;
		} else if(obj.basdevice_port.value!="" && Trim(obj.basdevice_port.value)==""){
			alert("BAS�����豸�˿�ӦΪ����");
			obj.basdevice_port.focus();
			obj.basdevice_port.select();
			return false;
		} else if(obj.basdevice_port.value!="" && !IsNumber(obj.basdevice_port.value,"BAS�����豸�˿�")){		
			obj.basdevice_port.focus();
			obj.basdevice_port.select();
			return false;
		} else if(obj.vlanid.value!="" && Trim(obj.vlanid.value)==""){
			alert("VlanID��ӦΪ����");
			obj.vlanid.focus();
			obj.vlanid.select();
			return false;
		} else if(obj.vlanid.value!="" && !IsNumber(obj.vlanid.value,"VlanID��")){		
			obj.vlanid.focus();
			obj.vlanid.select();
			return false;
		} else if(obj.bwlevel.value!="" && Trim(obj.bwlevel.value)==""){
			alert("����ϸ��ӦΪ����");
			obj.bwlevel.focus();
			obj.bwlevel.select();
			return false;
		} else if(obj.bwlevel.value!="" && !IsNumber(obj.bwlevel.value,"����ϸ��")){		
			obj.bwlevel.focus();
			obj.bwlevel.select();
			return false;
		} else if(obj.vciid.value!="" && Trim(obj.vciid.value)==""){
			alert("VciID��ӦΪ����");
			obj.vciid.focus();
			obj.vciid.select();
			return false;
		} else if(obj.vciid.value!="" && !IsNumber(obj.vciid.value,"VCI")){		
			obj.vciid.focus();
			obj.vciid.select();
			return false;
		}  else if(obj.userline.value!="" && Trim(obj.userline.value)==""){
			alert("�û���·ӦΪ����");
			obj.userline.focus();
			obj.userline.select();
			return false;
		} else if(obj.userline.value!="" && !IsNumber(obj.userline.value,"�û���·")){		
			obj.userline.focus();
			obj.userline.select();
			return false;
		} else if(obj.maxattdnrate.value!="" && Trim(obj.maxattdnrate.value)==""){
			alert("������пɴ�����ӦΪ����");
			obj.maxattdnrate.focus();
			obj.maxattdnrate.select();
			return false;
		} else if(obj.maxattdnrate.value!="" && !IsNumber(obj.maxattdnrate.value,"������пɴ�����")){		
			obj.maxattdnrate.focus();
			obj.maxattdnrate.select();
			return false;
		} else if(obj.upwidth.value!="" && Trim(obj.upwidth.value)==""){
			alert("���г�ŵ����ӦΪ����");
			obj.upwidth.focus();
			obj.upwidth.select();
			return false;
		} else if(obj.vciid.value!="" && !IsNumber(obj.vciid.value,"VCI")){
			obj.vciid.focus();
			obj.vciid.select();
			return false;
		} else if(obj.vpiid.value!="" && !IsNumber(obj.vpiid.value,"VPI")){
			obj.vpiid.focus();
			obj.vpiid.select();
			return false;
		} else if(obj.upwidth.value!="" && !IsNumber(obj.upwidth.value,"���г�ŵ����")){		
			obj.upwidth.focus();
			obj.upwidth.select();
			return false;
		} else {
			document.frm.hidOpenDate.value = DateToDes(document.frm.opendate.value);
			document.frm.hidOnlineDate.value = DateToDes(document.frm.onlinedate.value);
			document.frm.hidPauseDate.value = DateToDes(document.frm.pausedate.value);
			document.frm.hidCloseDate.value = DateToDes(document.frm.closedate.value);
			document.frm.hidUpdateTime.value = DateToDes(document.frm.updatetime.value);
			document.frm.hidMoveDate.value = DateToDes(document.frm.movedate.value);
			document.frm.hidDealDate.value = DateToDes_long(document.frm.dealdate.value,document.frm.time.value);
			
			obj.some_device.value = obj.oui.value + '/' + obj.vender.value;
			return true;
		}
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
//add by benyp
function DateToDes_long(v,t){
   
	if(v != ""){
	  if(t != ""){
	  //yyyy-mm-dd
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
	//hh:mm:ss	

	    h = parseInt(t.substring(0,2));
	    r = parseInt(t.substring(3,5));
	    s = parseInt(t.substring(6,8));
	
		dt = new Date(y,m-1,d,h,r,s);	
		var q  = dt.getTime()/1000;
		return q;
		}
		
		
		else{
		
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
	}
	else
		return 0;
}

function EC(leaf,obj){
	pobj = obj.offsetParent;
	oTRs = pobj.getElementsByTagName("TR");
	var m_bShow; 
	for(var i=0; i<oTRs.length; i++){
		if(oTRs[i].leaf == leaf){
			m_bShow = (oTRs[i].style.display=="none");
			oTRs[i].style.display = m_bShow?"":"none";
		}
	}
	sobj = obj.getElementsByTagName("IMG");
	if(m_bShow) {
		sobj[0].src = "images/up_enabled.gif";
		obj.className="yellow_title";
	}
	else{
		sobj[0].src = "images/down_enabled.gif";
		obj.className="green_title";
	}
}

//-->
</SCRIPT>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="HGWUserInfoSave.jsp"
				onsubmit="return CheckForm()">
				<input type="hidden" name="device_id" value="">
				<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD bgcolor=#999999>

							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR>
									<TD bgcolor="#ffffff" colspan="4">
										��'
										<font color="#FF0000">*</font>'�ı�������д��ѡ��
									</TD>
								</TR>
								<TR>
									<TH colspan="4" align="center">
										�༭��ͥ�����û���<%=username%>
										
										<%if(!realname.equals("")){%>(
										<%=realname%>)
										<%}%>
										������Ϣ
									</TH>
								</TR>
								<TR>
									<TD colspan="4" align="center" class=column>
										������
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									
									<TD class=column align="left">
										�û��ʻ�
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="username" maxlength=100 class=bk
											value="<%=username%>">
										&nbsp;
										<font color="#FF0000">*</font>
									</TD>
									<TD class=column align="left">
										ҵ������
									</TD>
									<TD>
										<%=servTypeList%>
										&nbsp;
										<font color="red">*</font>&nbsp;&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="left">
										��������
									</TD>
									<% 
										String select1 = "";
										String select2 = "";
										if(wan_type.equals("1")){
											select1 = "selected";
											select2 = "";
										} else {
											select1 = "";
											select2 = "selected";
										}
									
									%>
									
									<TD colspan="3">
										<SELECT name="wan_type" class=bk>
											<option value="-1">
												==��ѡ��==
											</option>
											<option value="1" <%=select1%>>
												�Ž�
											</option>
											<option value="2" <%=select2%>>
												·��
											</option>
										</SELECT>
										&nbsp;
										<font color="red">*</font>&nbsp;&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="left">
										�û�����
									</TD>
									<TD>
										<INPUT TYPE="password" NAME="passwd" maxlength=100 class=bk
											value="<%=passwd%>">
										&nbsp;
										<font color="#FF0000">*</font>
									</TD>
									<TD class=column align="left">
										������֤
									</TD>
									<TD>
										<INPUT TYPE="password" NAME="validate_passwd" maxlength=100
											class=bk value="<%=passwd%>">
										&nbsp;
										<font color="#FF0000">*</font>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="left">
										VPI
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="vpiid" maxlength=15 class=bk
											value="<%=vpiid%>">
										&nbsp;
										<font color="#FF0000">*</font>
									</TD>
									<TD class=column align="left">
										VCI
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="vciid" maxlength=15 class=bk
											value="<%=vciid%>">
										&nbsp;
										<font color="#FF0000">*</font>
									</TD>
								</TR>


								<TR class="green_title" onclick="EC('suggestedContent',this);">
									<TD colspan="4" >
										<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
										<TR class=column>
											<TD>
												<font size="2">������д��</font>
											</TD>
											<TD align="right">
												<IMG SRC="images/up_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="" >&nbsp;
											</TD>
										</TR>
										</TABLE>
									</TD>
								</TR>
								
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
									<TD class=column align="left">
										��ͥ�����豸(OUI)
										<input type="hidden" name="some_device" value="">
									</TD>
									<TD colspan=3>
										<input type="text" name="oui" class=bk size="10">
										&nbsp; --(���к�)--&nbsp;
										<input type="text" name="vender" class=bk size="35">
										&nbsp;
										<IMG onClick="selDev()" SRC="../images/search.gif" WIDTH="15"
											HEIGHT="12" BORDER="0" ALT="ѡ��">
										&nbsp;
									</TD>
								</TR>

								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
								    <TD class=column align="left">
										�ɼ���
									</TD>
									<TD>
										<%=strGatherList%>
										
									</TD>
									<TD class=column align="left">
										���ر���
									</TD>
									<TD>
										<%=strCityList%>
										&nbsp;
									</TD>
									

								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
								<TD class=column align="left">
										�����ʶ
									</TD>
									<TD>
										<%
											String strsql_2 = "select office_id,office_name from tab_office order by office_id";
											Cursor cursor_2 = DataSetBean.getCursor(strsql_2);
											Map fields_2 = cursor_2.getNext();
											String old_2 = (String) fields.get("office_id".toLowerCase());

											String selected_2 = "";
											if (fields_2 == null) {
												out
												.println("<SELECT name=office_id class=bk><option value='0'>û�о����ʶ</option></SELECT>");
											} else {
												out.println("<SELECT name=office_id class=bk>");
												//out.println("<OPTION VALUE=0>====��ѡ��====</OPTION>");
												while (fields_2 != null) {
													if (old_2.equals((String) fields_2.get("office_id"
													.toLowerCase()))) {
												selected_2 = "selected";
													} else {
												selected_2 = "";
													}
													out.println("<option value='"
													+ fields_2.get("office_id".toLowerCase()) + "' "
													+ selected_2 + ">"
													+ fields_2.get("office_name".toLowerCase())
													+ "</option>");
													fields_2 = cursor_2.getNext();
												}
												out.println("</select>");
											}
										%>
										&nbsp;
									</TD>
									<TD class=column align="left">
										С����ʶ
									</TD>
									<TD>
										<%
											String strsql_3 = "select zone_id,zone_name from tab_zone order by zone_id ";
											Cursor cursor_3 = DataSetBean.getCursor(strsql_3);
											Map fields_3 = cursor_3.getNext();
											String old_3 = (String) fields.get("zone_id".toLowerCase());
											String selected_3 = "";
											if (fields_3 == null) {
												out
												.println("<SELECT name=zone_id class=bk><option value='0'>û��С����ʶ</option></SELECT>");
											} else {
												out.println("<SELECT name=zone_id class=bk>");
												//out.println("<OPTION VALUE=0>====��ѡ��====</OPTION>");
												while (fields_3 != null) {
													if (old_3.equals((String) fields_3.get("zone_id"
													.toLowerCase()))) {
												selected_3 = "selected";
													} else {
												selected_3 = "";
													}
													out.println("<option value='"
													+ fields_3.get("zone_id".toLowerCase()) + "' "
													+ selected_3 + ">"
													+ fields_3.get("zone_name".toLowerCase())
													+ "</option>");
													fields_3 = cursor_3.getNext();
												}
												out.println("</select>");
											}
										%>
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
									<TD class=column align="left">
										�û�ʵ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="realname" maxlength=20 class=bk
											value="<%=realname%>">
										&nbsp;
									</TD>
									<TD class=column align="left">
										�Ա�
									</TD>
									<TD>
										<SELECT name="sex" class=bk>
											<option value="��" <% if (sex.equals("0")) {%> selected <%}%>>
												��
											</option>
											<option value="Ů" <% if (sex.equals("1")) {%> selected <%}%>>
												Ů
											</option>
										</SELECT>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
									<TD class=column align="left">
										��ϵ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="linkman" maxlength=10 class=bk
											value="<%=linkman%>">
										&nbsp;
									</TD>
									<TD class=column align="left">
										��ϵ�绰
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="linkphone" maxlength=10 class=bk
											value="<%=linkphone%>">
										&nbsp;
									</TD>


								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">

									<TD class=column align="left">
										��ϵ�˵�ַ
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="linkaddress" maxlength=10 class=bk
											value="<%=linkaddress%>">
										&nbsp;
									</TD>
									<TD class=column align="left">
										��ϵ���ֻ�
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="mobile" maxlength=10 class=bk
											value="<%=mobile%>">
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">

									<TD class=column align="left">
										��ϵ��email
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="email" maxlength=10 class=bk
											value="<%=email%>">
										&nbsp;
									</TD>
									<TD class=column align="left">
										����
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="bandwidth" maxlength=15 class=bk
											value="<%=bandwidth%>">
										(bps)
									</TD>									
								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
									<TD class=column align="left">
										Adsl�󶨵绰
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="phonenumber" maxlength=15 class=bk
											value="<%=phonenumber%>">
										&nbsp;
									</TD>
									<TD class=column align="left">
										��������ʱ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="dealdate" maxlength=20 class=bk
											value="<%=dealdate_date%>">
										<INPUT TYPE="button" value="��" class=btn
											onclick="showCalendar('day',event)">
									    <input type="text" name="time" value="<%=time%>" class=bk>	
										<INPUT TYPE="hidden" NAME="hidDealDate" class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
									<TD class=column align="left">
										������пɴ�����
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="maxattdnrate" maxlength=15 class=bk
											value="<%=maxattdnrate%>">
										(bps)
									</TD>
									<TD class=column align="left">
										���г�ŵ����
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="upwidth" maxlength=15 class=bk
											value="<%=upwidth%>">
										(bps)
									</TD>

								</TR>
								
								<TR class="green_title" onclick="EC('optionalContents',this);">
									<TD colspan="4">
										<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
										<TR>
										<TD>
											<font size="2">ѡ����д��</font> 
										</TD>
										<TD align="right">
											<IMG SRC="images/up_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="" >&nbsp;
										</TD>
										</TR>
										</TABLE>
									</TD>
								</TR>

								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										��ͬ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="cotno" maxlength=100 class=bk
											value="<%=cotno%>">
										&nbsp;
										
									</TD>
									<TD class=column align="left">
										���ڵ���id
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="cableid" maxlength=15 class=bk
											value="<%=cableid%>">
									</TD>

								</TR>



								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										�Ʒ����
									</TD>
									<TD>
										<SELECT name="bill_type_id" class=bk>
											<option value="0" <% if (bill_type_id.equals("0")) {%>
												selected <%}%>>
												��ʱ��
											</option>
											<option value="1" <% if (bill_type_id.equals("1")) {%>
												selected <%}%>>
												����
											</option>
											<option value="2" <% if (bill_type_id.equals("2")) {%>
												selected <%}%>>
												����
											</option>
											<option value="3" <% if (bill_type_id.equals("3")) {%>
												selected <%}%>>
												������
											</option>
										</SELECT>
										&nbsp;
									</TD>
									<TD class=column align="left">
										���¼Ʒ����
									</TD>
									<TD>
										<SELECT name="next_bill_type_id" class=bk>
											<option value="0" <% if (next_bill_type_id.equals("0")) {%>
												selected <%}%>>
												��ʱ��
											</option>
											<option value="1" <% if (next_bill_type_id.equals("1")) {%>
												selected <%}%>>
												����
											</option>
											<option value="2" <% if (next_bill_type_id.equals("2")) {%>
												selected <%}%>>
												����
											</option>
											<option value="3" <% if (next_bill_type_id.equals("3")) {%>
												selected <%}%>>
												������
											</option>
										</SELECT>
										&nbsp;
										
									</TD>

								</TR>


								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										�û�״̬����
									</TD>
									<TD>
										<select name="user_state" class=bk>
											<option value="-1">
												===��ѡ��=
											</option>
											<option value="1" <% if ("1".equals(user_state)) {%> selected
												<%}%>>
												����
											</option>
											<option value="2" <% if ("2".equals(user_state)) {%> selected
												<%}%>>
												��ͣ
											</option>
											<option value="3" <% if ("3".equals(user_state)) {%> selected
												<%}%>>
												����
											</option>
										</select>
										&nbsp;
										
									</TD>

									<TD class=column align="left">
										����ʱ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="opendate" maxlength=10 class=bk
											value="<%=opendate_date%>">
										<INPUT TYPE="button" value="��" class=btn
											onclick="showCalendar('day',event)">
										<INPUT TYPE="hidden" NAME="hidOpenDate" class=bk>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										�ͻ�����
									</TD>
									<TD>
										<SELECT name="cust_type_id" class=bk>
											<option value="0" <% if (cust_type_id.equals("0")) {%>
												selected <%}%>>
												��˾�ͻ�
											</option>
											<option value="1" <% if (cust_type_id.equals("1")) {%>
												selected <%}%>>
												���ɿͻ�
											</option>
											<option value="2" <% if (cust_type_id.equals("2")) {%>
												selected <%}%>>
												���˿ͻ�
											</option>
										</SELECT>
										&nbsp;
									</TD>
									<TD class=column align="left">
										�û�����
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="user_type_id" maxlength=20 class=bk
											value="<%=user_type_id%>">
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										֤������
									</TD>
									<TD>
										<SELECT name="cred_type_id" class=bk>
											<option value="0" <% if (cred_type_id.equals("0")) {%>
												selected <%}%>>
												==��ѡ��==
											</option>
											<option value="1" <% if (cred_type_id.equals("1")) {%>
												selected <%}%>>
												����
											</option>
											<option value="2" <% if (cred_type_id.equals("2")) {%>
												selected <%}%>>
												���֤
											</option>
											<option value="3" <% if (cred_type_id.equals("3")) {%>
												selected <%}%>>
												����֤
											</option>
											<option value="4" <% if (cred_type_id.equals("4")) {%>
												selected <%}%>>
												����֤
											</option>
										</SELECT>
										&nbsp;
									</TD>
									<TD class=column align="left">
										֤������
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="credno" maxlength=30 class=bk
											value="<%=credno%>">
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										��װסַ
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="address" maxlength=20 class=bk
											value="<%=address%>">
									</TD>
									<TD class=column align="left">
										�������ʱ�ʶ
									</TD>
									<TD>
										<SELECT name=access_kind_id class=bk>
											<option value="0" <% if (access_kind_id.equals("0")) {%>
												selected <%}%>>
												��ҵ
											</option>
											<option value="1" <% if (access_kind_id.equals("1")) {%>
												selected <%}%>>
												����
											</option>
											<option value="2" <% if (access_kind_id.equals("2")) {%>
												selected <%}%>>
												����
											</option>
										</SELECT>
										&nbsp;
									</TD>
								</TR>




								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										������
									</TD>
									<TD>
										<SELECT name="bindtype" class=bk>
											<option value="0" <% if (bindtype.equals("0")) {%> selected
												<%}%>>
												ADSL��
											</option>
											<option value="1" <% if (bindtype.equals("1")) {%> selected
												<%}%>>
												IPTV��
											</option>
											<option value="2" <% if (bindtype.equals("2")) {%> selected
												<%}%>>
												VOIP��
											</option>
										</SELECT>
										&nbsp;
									</TD>
									<TD class=column align="left">
										�������
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="virtualnum" maxlength=20 class=bk
											value="<%=virtualnum%>">
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										��������
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="numcharacter" maxlength=20 class=bk
											value="<%=user_type_id%>">
									</TD>
									<TD class=column align="left">
										���뷽ʽ��ʶ
									</TD>
									<TD>
										<SELECT name="access_style_id" class=bk>
											<option value="0" <% if ("0".equals(access_style_id)) {%>
												selected <%}%>>
												ADSL����
											</option>
											<option value="1" <% if ("1".equals(access_style_id)) {%>
												selected <%}%>>
												LAN����
											</option>
											<option value="2" <% if ("2".equals(access_style_id)) {%>
												selected <%}%>>
												���˽���
											</option>
										</SELECT>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										��֤��־
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="aut_flag" maxlength=20 class=bk
											value="<%=aut_flag%>">
									</TD>
									<TD class=column align="left">
										���ܷ������ͱ��뼯
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="service_set" maxlength=30 class=bk
											value="<%=service_set%>">
									</TD>

								</TR>

								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										��λ�ͻ���ҵ���
									</TD>
									<TD>
										<SELECT name=trade_id class=bk>
											<option value="0" <% if (trade_id.equals("0")) {%> selected
												<%}%>>
												������ҵ
											</option>
											<option value="1" <% if (trade_id.equals("1")) {%> selected
												<%}%>>
												������ҵ
											</option>
											<option value="2" <% if (trade_id.equals("2")) {%> selected
												<%}%>>
												������ҵ
											</option>
											<option value="3" <% if (trade_id.equals("3")) {%> selected
												<%}%>>
												������ҵ
											</option>
										</SELECT>
										&nbsp;
									</TD>
									<TD class=column align="left">
										����Ӫҵִ��ע���
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="licenceregno" maxlength=10 class=bk
											value="<%=licenceregno%>">
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										����ְҵ
									</TD>
									<TD>
										<SELECT name=occupation_id class=bk>
											<option value="0" <% if (occupation_id.equals("0")) {%>
												selected <%}%>>
												������Ա
											</option>
											<option value="1" <% if (occupation_id.equals("1")) {%>
												selected <%}%>>
												����Ա
											</option>
											<option value="2" <% if (occupation_id.equals("2")) {%>
												selected <%}%>>
												������
											</option>
											<option value="3" <% if (occupation_id.equals("3")) {%>
												selected <%}%>>
												�ϰ�
											</option>
											<option value="4" <% if (occupation_id.equals("4")) {%>
												selected <%}%>>
												����ְҵ��
											</option>
											<option value="5" <% if (occupation_id.equals("5")) {%>
												selected <%}%>>
												����ְҵ��
											</option>
										</SELECT>
										&nbsp;
									</TD>
									<TD class=column align="left">
										�����̶�
									</TD>
									<TD>
										<SELECT name=education_id class=bk>
											<option value="0" <% if (education_id.equals("0")) {%>
												selected <%}%>>
												����
											</option>
											<option value="1" <% if (education_id.equals("1")) {%>
												selected <%}%>>
												��ר
											</option>
											<option value="2" <% if (education_id.equals("2")) {%>
												selected <%}%>>
												��ѧ����
											</option>
											<option value="3" <% if (education_id.equals("3")) {%>
												selected <%}%>>
												˶ʿ
											</option>
											<option value="4" <% if (education_id.equals("4")) {%>
												selected <%}%>>
												��ʿ
											</option>
											<option value="5" <% if (education_id.equals("5")) {%>
												selected <%}%>>
												����
											</option>
										</SELECT>
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										VIP����
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="vipcardno" maxlength=10 class=bk
											value="<%=vipcardno%>">
										&nbsp;
									</TD>
									<TD class=column align="left">
										��ϵ�����֤��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="linkman_credno" maxlength=10 class=bk
											value="<%=linkman_credno%>">
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										������
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="agent" maxlength=10 class=bk
											value="<%=agent%>">
										&nbsp;
									</TD>
									<TD class=column align="left">
										���������֤��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="agent_credno" maxlength=10 class=bk
											value="<%=agent_credno%>">
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										��������ϵ�绰
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="agentphone" maxlength=10 class=bk
											value="<%=agentphone%>">
										&nbsp;
									</TD>
									<TD class=column align="left">
										ADSL�豸��Դ
									</TD>
									<TD>
										<SELECT name=adsl_res class=bk>
											<option value="0" <% if (adsl_res.equals("0")) {%> selected
												<%}%>>
												��Ӫ���ṩ
											</option>
											<option value="1" <% if (adsl_res.equals("1")) {%> selected
												<%}%>>
												���˹���
											</option>
										</SELECT>
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										ADSL����֤��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="adsl_card" maxlength=10 class=bk
											value="<%=adsl_card%>">
										&nbsp;
									</TD>
									<TD class=column align="left">
										ADSL�豸�ͺ�
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="adsl_dev" maxlength=10 class=bk
											value="<%=adsl_dev%>">
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										ADSL�豸���
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="adsl_ser" maxlength=15 class=bk
											value="<%=adsl_ser%>">
									</TD>
									<TD class=column align="left">
										�Ƿ��ά
									</TD>
									<TD>
										<INPUT TYPE="radio" NAME="isrepair" value="0"
											<% if (isrepair.equals("0")) {%> checked <%}%>>
										��&nbsp;&nbsp;
										<INPUT TYPE="radio" NAME="isrepair" value="1"
											<% if (isrepair.equals("1")) {%> checked <%}%>>
										��
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										Э����
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="contractno" maxlength=10 class=bk
											value="<%=contractno%>">
										&nbsp;
									</TD>
									<TD class=column align="left">
										�û�IP��ַ
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="ipaddress" maxlength=15 class=bk
											value="<%=ipaddress%>">
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										����IP��ַ������
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="overipnum" maxlength=15 class=bk
											value="<%=overipnum%>">
									</TD>
									<TD class=column align="left">
										����
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="ipmask" maxlength=15 class=bk
											value="<%=ipmask%>">
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										����
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="gateway" maxlength=15 class=bk
											value="<%=gateway%>">
										&nbsp;
									</TD>
									<TD class=column align="left">
										MAC��ַ
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="macaddress" maxlength=15 class=bk
											value="<%=macaddress%>">
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										�����û�������
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="max_user_number" maxlength=15
											class=bk value="<%=max_user_number%>">
									</TD>
									<TD class=column align="left">
										�����豸ip
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="device_ip" maxlength=15 class=bk
											value="<%=device_ip%>">
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										�����豸���ܺ�
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="device_shelf" maxlength=15 class=bk
											value="<%=device_shelf%>">
										&nbsp;
									</TD>

									<TD class=column align="left">
										�����豸���
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="device_frame" maxlength=15 class=bk
											value="<%=device_frame%>">
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										�����豸��λ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="device_slot" maxlength=15 class=bk
											value="<%=device_slot%>">
										&nbsp;
									</TD>
									<TD class=column align="left">
										�����豸�˿�
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="device_port" maxlength=15 class=bk
											value="<%=device_port%>">
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										BAS�����豸���
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="basdevice_id" maxlength=15 class=bk
											value="<%=basdevice_id%>">
									</TD>
									<TD class=column align="left">
										BAS�����豸ip
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="basdevice_ip" maxlength=15 class=bk
											value="<%=basdevice_ip%>">
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										BAS�����豸���ܺ�
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="basdevice_shelf" maxlength=15
											class=bk value="<%=basdevice_shelf%>">
									</TD>
									<TD class=column align="left">
										BAS�����豸���
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="basdevice_frame" maxlength=15
											class=bk value="<%=basdevice_frame%>">
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										BAS�����豸��λ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="basdevice_slot" maxlength=15 class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										BAS�����豸�˿�
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="basdevice_port" maxlength=15 class=bk
											value="<%=basdevice_port%>">
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										VlanID��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="vlanid" maxlength=15 class=bk
											value="<%=vlanid%>">
									</TD>
									<TD class=column align="left">
										������
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="workid" maxlength=15 class=bk
											value="<%=workid%>">
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										��ͨʱ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="onlinedate" maxlength=10 class=bk
											value="<%=onlinedate_date%>">
										<INPUT TYPE="button" value="��" class=btn
											onclick="showCalendar('day',event)">
										<INPUT TYPE="hidden" NAME="hidOnlineDate" class=bk>
									</TD>

									<TD class=column align="left">
										��ͣʱ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="pausedate" maxlength=10 class=bk
											value="<%=closedate_date%>">
										<INPUT TYPE="button" value="��" class=btn
											onclick="showCalendar('day',event)">
										<INPUT TYPE="hidden" NAME="hidPauseDate" class=bk>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										����ʱ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="closedate" maxlength=10 class=bk
											value="<%=closedate_date%>">
										<INPUT TYPE="button" value="��" class=btn
											onclick="showCalendar('day',event)">
										<INPUT TYPE="hidden" NAME="hidCloseDate" class=bk>
									</TD>
									<TD class=column align="left">
										����ʱ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="updatetime" maxlength=10 class=bk
											value="<%=updatetime_date%>">
										<INPUT TYPE="button" value="��" class=btn
											onclick="showCalendar('day',event)">
										<INPUT TYPE="hidden" NAME="hidUpdateTime" class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										Ա������
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="staff_id" maxlength=15 class=bk
											value="<%=staff_id%>">
										&nbsp;
									</TD>
									<TD class=column align="left">
										��ע
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="remark" maxlength=15 class=bk
											value="<%=remark%>">
									</TD>

								</TR>

								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										ADSL����
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="adsl_hl" maxlength=15 class=bk
											value="<%=adsl_hl%>">
									</TD>
									<TD class=column align="left">
										����Э��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="opmode" maxlength=15 class=bk
											value="<%=opmode%>">
									</TD>
								</TR>

								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										�û���·
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="userline" maxlength=15 class=bk
											value="<%=userline%>">
									</TD>
									<TD class=column align="left">
										Dslam�豸���
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="dslamserialno" maxlength=15 class=bk
											value="<%=dslamserialno%>">
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										�ƶ�����
									</TD>
									<TD colspan="3">
										<INPUT TYPE="text" NAME="movedate" maxlength=10 class=bk
											value="<%=movedate_date%>">
										<INPUT TYPE="button" value="��" class=btn
											onclick="showCalendar('day',event)">
										<INPUT TYPE="hidden" NAME="hidMoveDate" class=bk>
									</TD>



								</TR>



								<TR>
									<TD colspan="4" align="right" class=foot>
										<INPUT TYPE="submit" value=" �� �� " class=btn>
										&nbsp;&nbsp;
										<INPUT TYPE="reset" value=" �� д " class=btn>
										<INPUT TYPE="hidden" name="action" value="update">
										<INPUT TYPE="hidden" name="user_id" value="<%=user_id%>">
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</FORM>
		</TD>
	</TR>
</TABLE>

<script language="javascript">
/**
show();
function show() {
	var shortName = '';
	var tr = document.getElementById("wanType");
	if (tr != null) {
		if (tr.isShow == shortName) {
			tr.style.display = "";
			document.forms[0].wan_type.value = '';
		}
	}
	var service_id = '<%=service_id%>';
	document.forms[0].some_service.value = service_id;
}
*/
function showVal(obj) {
	//alert(obj.value);
}

document.frm.oui.value = '<%=oui%>';
document.frm.vender.value = '<%=device_serialnumber%>';

document.frm.some_device.value = document.frm.oui.value+'/'+document.frm.vender.value;

function selDev()
{
	var retValue = window.showModalDialog("./selDeviceList.jsp?oui="+document.frm.oui.value+"&vender="+document.frm.vender.value +"&gw_type=1",window,"status:yes;resizable:yes;edge:raised;scroll:no;help:no;center:yes;dialogHeight:600px;dialogWidth:800px");
	
	if (typeof(retValue) != 'undefined'){
		document.frm.some_device.value = retValue;
		var temp = retValue.split('/');
		document.frm.oui.value = temp[0];
		document.frm.vender.value = temp[1];
	}
	
	//window.open("./selDeviceList.jsp?queryStr="+document.frm.some_device.value);
}
</script>
