<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.Date"%>
<%@page import="com.linkage.litms.common.util.*"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.blockUI.js"></SCRIPT>
<%--
	��ҵ���أ�BBMS��
--%>
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
<SCRIPT LANGUAGE="JavaScript">
var m_bShow_pe; 
var m_bShow_ce;
var selected_value;
var selected_value_ce;
var time = new Date();
</SCRIPT>

<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
	request.setCharacterEncoding("GBK");

	//���������������ͺͲ������ͣ�1Ϊ��ͥ���أ�2Ϊ��ҵ���أ�1Ϊ����(�û����Ӳ˵�)��2Ϊ�༭(�û��б�����)
	//11,12,21,22 ��Ϊ����
	String gwOptType = request.getParameter("gwOptType");

	String customer_id = request.getParameter("customer_id");
	//*************************�����������ǡ��༭����ʱ��Ҫȡ����Ӧ�û���Ϣ********************

	String showtype = request.getParameter("showtype");
	String oui = "";
	String device_serialnumber = "";

	String username = "";
	String passwd = "";
	String user_type_id = "";
	String access_style_id = "1";
	String realname = "";

	//��̬IP��ʽ����ʱ
	String ipaddress = "";
	String ipmask = "";
	String gateway = "";
	String adsl_ser = "";

	String user_state = "";
	String staff_id = "";
	String vpiid = "";
	String vciid = "";
	String opmode = "";
	String selectServTypeId = "";
	String selectedENamesId = "";
	String wan_type = "1";
	Date curDtae = new Date();

	String hidOpendate = "";
	String hidOnlinedate = "";
	String hidPausedate = "";
	String hidClosedate = "";
	String hidUpdatetime = "";
	String hidDealdate = "";

	String opendate = "";
	String onlinedate = "";
	String pausedate = "";
	String closedate = "";
	String updatetime = "";
	String dealdate = "";

	String openTime = "";
	String onlineTime = "";
	String closeTime = "";
	String pauseTime = "";
	String upTime = "";
	String time = "";

	String maxattdnrate = "2048";
	String upwidth = "512";
	String lan_num = "0";
	String ssid_num = "0";
	String work_model = "-1";

	String user_id = "";
	String gather_id = "";
	String device_id = "";


	//������ʽѡ��
	String select1 = "selected";
	String select2 = "";
	String select3 = "";
	String select4 = "";
	//ר������
	String vlanid = "46";

	Map fields = null;

	if ("12".equals(gwOptType) || "22".equals(gwOptType)) {

		user_id = request.getParameter("user_id");
		gather_id = request.getParameter("gather_id");

		String strSQL = "select * from tab_hgwcustomer where user_id="
				+ user_id + " and gather_id='" + gather_id + "'";

		if ("22".equals(gwOptType)) {
			strSQL = "select * from tab_egwcustomer where user_id="
					+ user_id;
		}


		fields = DataSetBean.getRecord(strSQL);

		if (fields != null) {
			//���û�пͻ�ID���ݹ������������ݿ������е�
			if(null == customer_id || customer_id.isEmpty() || "null".equals(customer_id)){
				customer_id = (String) fields.get("customer_id");
			}
			hidOpendate = (String) fields.get("opendate");
			hidOnlinedate = (String) fields.get("onlinedate");
			hidPausedate = (String) fields.get("pausedate");
			hidClosedate = (String) fields.get("closedate");
			//hidUpdatetime = (String) fields.get("updatetime");
			hidDealdate = (String) fields.get("dealdate");

			if ((hidOpendate != null) && (hidOpendate.length() != 0)
					&& (!"0".equals(hidOpendate))) {
				opendate = new DateTimeUtil(
						Long.parseLong(hidOpendate) * 1000).getDate();
				openTime = new DateTimeUtil(
						Long.parseLong(hidOpendate) * 1000).getTime();
			}
			if ((hidOnlinedate != null)
					&& (hidOnlinedate.length() != 0)
					&& (!"0".equals(hidOnlinedate))) {
				onlinedate = new DateTimeUtil(Long
						.parseLong(hidOnlinedate) * 1000).getDate();
				onlineTime = new DateTimeUtil(Long
						.parseLong(hidOnlinedate) * 1000).getTime();
			}
			if ((hidPausedate != null) && (hidPausedate.length() != 0)
					&& (!"0".equals(hidPausedate))) {
				pausedate = new DateTimeUtil(Long
						.parseLong(hidPausedate) * 1000).getDate();
				pauseTime = new DateTimeUtil(Long
						.parseLong(hidPausedate) * 1000).getTime();
			}
			if ((hidClosedate != null) && (hidClosedate.length() != 0)
					&& (!"0".equals(hidClosedate))) {
				closedate = new DateTimeUtil(Long
						.parseLong(hidClosedate) * 1000).getDate();
				closeTime = new DateTimeUtil(Long
						.parseLong(hidClosedate) * 1000).getTime();
			}
			if ((hidDealdate != null) && (hidDealdate.length() != 0)
					&& (!"0".equals(hidDealdate))) {
				dealdate = new DateTimeUtil(
						Long.parseLong(hidDealdate) * 1000).getDate();
				time = new DateTimeUtil(
						Long.parseLong(hidDealdate) * 1000).getTime();
			}

			device_id = (String) fields.get("device_id".toLowerCase());
			username = (String) fields.get("username".toLowerCase());
			passwd = (String) fields.get("passwd".toLowerCase());
			user_type_id = (String) fields.get("user_type_id"
					.toLowerCase());
			access_style_id = (String) fields.get("access_style_id"
					.toLowerCase());
			realname = (String) fields.get("realname".toLowerCase());

			user_state = (String) fields
					.get("user_state".toLowerCase());
			vpiid = (String) fields.get("vpiid".toLowerCase());
			vciid = (String) fields.get("vciid".toLowerCase());

			opmode = (String) fields.get("opmode".toLowerCase());
			wan_type = (String) fields.get("wan_type".toLowerCase());
			if (wan_type.equals("1")) {
				select1 = "selected";
			} else if (wan_type.equals("2")) {
				select2 = "selected";
			} else if (wan_type.equals("3")) {
				select3 = "selected";
			} else if (wan_type.equals("4")) {
				select4 = "selected";
			}

			ipaddress = (String) fields.get("ipaddress".toLowerCase());
			ipmask = (String) fields.get("ipmask".toLowerCase());
			gateway = (String) fields.get("gateway".toLowerCase());
			adsl_ser = (String) fields.get("adsl_ser".toLowerCase());//DNS��ַ

			staff_id = (String) fields.get("staff_id".toLowerCase());
			selectServTypeId = (String) fields.get("serv_type_id");
			//EGWר���ֶ�
			selectedENamesId = (String) fields.get("customer_id");


			//����
			maxattdnrate = (String) fields.get("maxattdnrate");
			upwidth = (String) fields.get("upwidth");
			if (maxattdnrate == null || "".equals(maxattdnrate)) {
				maxattdnrate = "2048";
			}
			if (upwidth == null || "".equals(upwidth)) {
				upwidth = "512";
			}
			//lan_num
			lan_num = (String) fields.get("lan_num");
			if (lan_num == null || "".equals(lan_num)) {
				lan_num = "0";
			}
			//ssid_num
			ssid_num = (String) fields.get("ssid_num");
			if (ssid_num == null || "".equals(ssid_num)) {
				ssid_num = "0";
			}
			//work_model
			work_model = (String) fields.get("work_model");
			if (work_model == null || "".equals(work_model)) {
				work_model = "-1";
			}

			//�豸��Ϣ
			oui = (String) fields.get("oui");
			device_serialnumber = (String) fields
					.get("device_serialnumber");

			vlanid = (String) fields.get("vlanid");
		}
	} else {
		//����û�ʱʱ���Ĭ��ֵΪ��ǰʱ��
		curDtae = new Date();

		opendate = new DateTimeUtil(curDtae.getTime()).getDate();
		openTime = new DateTimeUtil(curDtae.getTime()).getTime();

		onlinedate = new DateTimeUtil(curDtae.getTime()).getDate();
		onlineTime = new DateTimeUtil(curDtae.getTime()).getTime();

		dealdate = new DateTimeUtil(curDtae.getTime()).getDate();
		time = new DateTimeUtil(curDtae.getTime()).getTime();

	}
	updatetime = new DateTimeUtil(curDtae.getTime()).getDate();
	upTime = new DateTimeUtil(curDtae.getTime()).getTime();

	//��������������������������������������������������������������������������������������������������������������������������������������������������������������������������//
	//Ա������Ĭ��Ϊ��ǰ�û��˺�
	if (staff_id == null || "".equals(staff_id)) {
		staff_id = String.valueOf(curUser.getUser().getId());
	}

	//**************�����б�չʾ�����û��ֵ��ʾ�գ������ֵ����ʾ��ֵ******************************

	//��ȡҵ�����������б�
	String servTypeList = "";
	if ("11".equals(gwOptType)) {
		servTypeList = HGWUserInfoAct.getServTypeNameList(1,
				selectServTypeId);
	} else if ("21".equals(gwOptType)) {
		servTypeList = HGWUserInfoAct.getServTypeNameList(5,
				selectServTypeId);
		/**
		if("yn_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
			servTypeList = HGWUserInfoAct.getServTypeNameList(5,
					selectServTypeId);
		}else{
			servTypeList = HGWUserInfoAct.getServTypeNameList(15,
					selectServTypeId);
		}**/
	} else {
		//���Ա༭״̬�����ҳ��ʱ��ҵ�����Ͳ��ɱ༭����ʾ�б�Ψһ
		servTypeList = HGWUserInfoAct
				.getServTypeNameList(selectServTypeId);
	}

	//************************************************************************************************//

	//*******************�������ֶ�PVC��vlanidһ��Ĭ��ֵ***************//
	if ("".equals(vlanid))
		vlanid = "46";
	if ("".equals(vpiid)) {
		vpiid = "8";
		vciid = "35";
	}

	//
	
	UserRes userRes = (UserRes) request.getSession().getAttribute("curUser");
	String cityId = userRes.getCityId();

%>

<SCRIPT LANGUAGE="JavaScript">
<!--
	//var usernameflag = 1;
//-----------------ajax----------------------------------------
  var request = false;
  var cityId = '<%= cityId%>';
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
        	//closeMsgDlg();
			} else
			alert("status is " + request.status);
		}
	}
//---------------------------------------------------------------
    
//�����û�������ajax
   function send_request(url) {
     request.open("GET", url, true);
     request.onreadystatechange = updatePage;
     request.send(null);
   }

	function updatePage() {
     if (request.readyState == 4) {
       if (request.status == 200) {
        document.all.isOK.innerHTML = request.responseText;
       } else
         alert("status is " + request.status);
     }
   }
//�����豸����ajax
   function send_requestD(url) {
     request.open("GET", url, true);
     request.onreadystatechange = updatePageD;
     request.send(null);
   }

	function updatePageD() {
     if (request.readyState == 4) {
       if (request.status == 200) {
        document.all.isOKD.innerHTML = request.responseText;
       } else
         alert("status is " + request.status);
     }
   }


	function CheckForm() {
		var obj = document.frm;
		var gwOptType = obj.gwOptType.value;
		if(document.all.isOK.innerHTML != ""){
			alert("���û�����ע���ҵ��,�����ύ��");
			obj.username.focus();
			return false;
		}
		
		if(!IsNull(obj.username.value,"�û��ʻ�")){
			obj.username.focus();
			obj.username.select();
			return false;
		} else if(!IsNull(obj.passwd.value,"�û�����")){
			obj.passwd.focus();
			obj.passwd.select();
			return false;
		} else if (obj.e_id.value == "-1"){
			alert("��ѡ��ͻ�");
			return false;
		} else if (obj.some_service.value == "-1") {
			alert("��ѡ��ҵ�����ͣ�");
			return false;
		} else if (obj.wan_type.value == "-1") {
			alert("��ѡ���������ͣ�");
			return false;
//		} else if(obj.wan_type.value == "3" && !IsIPAddr(obj.ipaddress.value)){
			//��ѡ��̬IP��ʽʱ�����IP��ַ
//			return false;
		}else if(obj.access_style_id.value == ""){
			alert("��ѡ�񶩵�����");
			return false;
		}else if(!IsNumber(obj.maxattdnrate.value,"������пɴ�����")){
			//alert("VCI��ӦΪ����");
			obj.maxattdnrate.focus();
			obj.maxattdnrate.select();
			return false;
		}
		else if(!IsNumber(obj.upwidth.value,"���г�ŵ����")){
			//alert("VCI��ӦΪ����");
			obj.upwidth.focus();
			obj.upwidth.select();
			return false;
		}
		else if(!IsNumber(obj.lan_num.value,"���LAN����")){
			//alert("VCI��ӦΪ����");
			obj.lan_num.focus();
			obj.lan_num.select();
			return false;
		}
		else if(!IsNumber(obj.ssid_num.value,"SSID����")){
			//alert("VCI��ӦΪ����");
			obj.ssid_num.focus();
			obj.ssid_num.select();
			return false;
		}else if(obj.access_style_id.value == "1" || obj.access_style_id.value == "2" 
			|| obj.access_style_id.value == "3"){
			if(obj.vpiid.value == ""){
			 alert("������VPI");
		     obj.vpiid.focus();
		     return false;
			}else if(obj.vciid.value == ""){
			 alert("������VCI");
		     obj.vciid.focus();
		     return false;
			} else if(!IsNumber(obj.vciid.value,"VCI")){
			//alert("VCI��ӦΪ����");
			obj.vciid.focus();
			obj.vciid.select();
			return false;
			} else if(!IsNumber(obj.vpiid.value,"VPI")){
			//alert("VPI��ӦΪ����");
			obj.vpiid.focus();
			obj.vpiid.select();
			return false;
			}
		}else if (obj.access_style_id.value == "4" || obj.access_style_id.value == "5"){
			if (obj.vlanid.value == ""){
				alert("������vlanid��");
				obj.vlanid.focus();
				return false;
			}
		}
			//����ʱ��
			obj.hidOpenDate.value = DateToDes_long(obj.opendate.value, obj.openTime.value);
			//��ͨʱ��
			obj.hidOnlineDate.value = DateToDes_long(obj.onlinedate.value, obj.onlineTime.value);
			//��ͣʱ��
			obj.hidPauseDate.value = DateToDes_long(obj.pausedate.value, obj.pauseTime.value);
			//����ʱ��
			obj.hidCloseDate.value = DateToDes_long(obj.closedate.value, obj.closeTime.value);
			//updatetimeΪ��������
			obj.hidUpdateTime.value = DateToDes_long(obj.updatetime.value, obj.upTime.value);
			//��������ʱ��
			obj.hidDealDate.value = DateToDes_long(obj.dealdate.value,obj.time.value);

		
		//javabean����Ҫ���ֶ����жϲ�����ʽ
		if(gwOptType == "12" || gwOptType == "22"){
			obj.action.value = "update";
		}else{
			obj.action.value = "add";
			//Ĭ��Ϊ���ֹ���ӡ�
			obj.user_type_id.value = "3";
		}
		$("input[@name='saveBtn']").attr("disabled", true);
		$("input[@name='resetBtn']").attr("disabled", true);
		$("input[@name='srch']").attr("disabled", true);
		$("input[@name='clearBtn']").attr("disabled", true);		
		obj.submit();
		//alert(document.forms[0].action.value);
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

	function SearchUName(time) {
		var f = document.frm;
		var username = f.username.value;
		var gwOptType = f.gwOptType.value;
		var user_id = f.user_id.value;
		if (username == "")	{
			//if(gwOptType=="11" || gwOptType == "21")
			//	usernameflag = 0;
			f.username.focus();
			return false;
		} else {
			send_request('checkUserName.jsp?username='+username
						+'&gwOptType='+gwOptType
						+'&user_id='+user_id
						+'&time='+time
						+'&cityId=' + cityId);
		}
	}
	//����sΪҵ������
function showVal(s){
	SearchUName(new Date());

	wanTp(s.value);
}

function wanTp(v){
	var ss = "<SELECT name='wan_type' class=bk onchange = 'ShowIPAddr()'>";
	ss += "<option value='-1'>==��ѡ��==</option>";
	ss += "<option value='1' <%=select1%>>�Ž�</option>";
	ss += "<option value='2' <%=select2%>>·��</option>";
//	if(v == "60" || v == ""){
		//����ҵ������ѡ��������ʽ
		ss += "<option value='3' <%=select3%>>��̬IP</option>";
//	}
	ss += "<option value='4' <%=select4%>>DHCP</option>";
	ss += "</SELECT>&nbsp;<font color='red'>*</font>&nbsp;&nbsp;";
	typeSpan.innerHTML = ss;
}

//��ѡ��̬ IP��ʽʱ��Ҫ������IP��ַ
function ShowIPAddr(){
	//alert(ths.value);
	var wan_type = document.frm.wan_type.value;
	if(wan_type == "3"){
		ipaddrSpan.style.display = "";
		ipaddrSpan2.style.display = "";
		
		vlanSpan.style.display = "";
		vlantSpan.style.display = "";
		pvcSpan.style.display = "none";
		pvctSpan.style.display = "none";
	}else{
		if(wan_type == "4"){
			vlanSpan.style.display = "";
			vlantSpan.style.display = "";
			pvcSpan.style.display = "none";
			pvctSpan.style.display = "none";
		}else{
			vlanSpan.style.display = "none";
			vlantSpan.style.display = "none";
			pvcSpan.style.display = "";
			pvctSpan.style.display = "";
		
		}
		ipaddrSpan.style.display = "none";
		ipaddrSpan2.style.display = "none";
	}
}

//ѡ���豸
function selDev()
{
	window.open("./selectEGWDevice.jsp?time="+Math.random(),"win","height:480,width:650");
}

function showContent(tp){
	tpv = tp.value;
	if(tpv == "1"){
		vlanSpan.style.display = "none";
		vlantSpan.style.display = "none";
		pvcSpan.style.display = "";
		pvctSpan.style.display = "";
	}else{
		vlanSpan.style.display = "";
		vlantSpan.style.display = "";
		pvcSpan.style.display = "none";
		pvctSpan.style.display = "none";
	}
}

function clearDev(){
	var frm = document.frm;
	frm.device_serialnumber.value = "";
	frm.oui.value = "";
}

//
function findCustomer(){
	var cust = document.frm.custName.value;
	if (cust != "" && cust != null) {
		//showMsgDlg("���ڲ�ѯ���Եȡ�");
		var obj = document.all.customer_nameDiv;
		var url = "findCustomer.jsp?type=name&value=" + cust + "&tt="+new Date();
		sendRequest("get",url,obj);
	}
}

function showMsgDlg(strMsg){
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
	document.all.txtLoading.innerText=strMsg;
}
function closeMsgDlg(){
	PendingMessage.style.display="none";
}
//-->
</script>

<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle">
			<span id=txtLoading style="font-size:14px;font-family: ����"></span>
		</td>
	</tr>
</table>
</center>
</div>

<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" id="myTable">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="frm" METHOD="post" action="EGWUserInfoSave.jsp"><input
			type="hidden" name="gwOptType" value="<%= gwOptType %>"> <input
			type="hidden" name="device_id" value="<%=device_id%>"> <input
			type="hidden" name="user_id" value="<%= user_id%>"> <input
			type="hidden" name="user_type_id" value="<%= user_type_id%>">
		<input type="hidden" name="showtype" value="<%= showtype%>"> <input
			type="hidden" name="gather_id" value="<%=gather_id %>"> <input
			type="hidden" name="customer_id" value="<%= customer_id%>">
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">�û���Դ</td>
						<td><img src="../images/attention_2.gif" width="15"
							height="12"> ��' <font color="#FF0000">*</font>'�ı�������д��ѡ��</td>
					</tr>
				</table>
				</td>
			</tr>

			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center">
						<%
							//������ʾ̧ͷ����
							String title = "";
							if ("21".equals(gwOptType)) {
								title = "��ҵ�����û�����";
							} else if ("22".equals(gwOptType)) {
								title = "��ҵ�����û��༭(" + username + ")";
							} else if ("12".equals(gwOptType)) {
								title = "��ͥ�����û��༭(" + username + ")";
							} else {
								title = "��ͥ�����û�����";
							}
						%> <%=title%></TH>
					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>ҵ������</TD>
						<TD><%=servTypeList%> &nbsp; <font color="red">*</font>&nbsp;&nbsp;
						</TD>
						<TD class=column align="right">��������</TD>
						<TD><SELECT name="access_style_id" class=bk>
							<option>��ѡ��</option>
							<option value="1" <% if ("1".equals(access_style_id)) {%>
								selected <%}%>>ADSL</option>
							<option value="2" <% if ("2".equals(access_style_id)) {%>
								selected <%}%>>��ͨLAN</option>
							<option value="3" <% if ("3".equals(access_style_id)) {%>
								selected <%}%>>��ͨ����</option>
							<option value="4" <% if ("4".equals(access_style_id)) {%>
								selected <%}%>>ר��LAN</option>
							<option value="5" <% if ("5".equals(access_style_id)) {%>
								selected <%}%>>ר�߹���</option>
						</SELECT> &nbsp;<font color='#FF0000'>*</font></TD>
					</TR>

					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>��������</TD>
						<TD id='typeSpan'><SELECT name='wan_type' class=bk
							onchange='ShowIPAddr()'>
							<option value="-1">==��ѡ��==</option>
							<option value='1' <%=select1%>>�Ž�</option>
							<option value='2' <%=select2%>>·��</option>
							<option value='3' <%=select3%>>��̬IP</option>
						</SELECT>&nbsp;<font color='red'>*</font> &nbsp;&nbsp;</TD>
						
						<TD id="pvcSpan" class=column align='right' style="display:none" nowrap>VPI/VCI</TD>
						<TD id="pvctSpan" style="display:none">
							<INPUT TYPE='text' NAME='vpiid' value='<%=vpiid%>' size=3 class=bk>&nbsp;
							<INPUT TYPE='text' NAME='vciid' value='<%=vciid%>' size=3 class=bk>
							&nbsp;<font color='#FF0000'>*</font></TD>
						
						<TD id="vlanSpan" class=column align='right' style="display:none" nowrap>VlanID��</TD>
						<TD id="vlantSpan" style="display:none">
							<INPUT TYPE='text' NAME='vlanid' value='<%=vlanid%>' size=10 class=bk>
							&nbsp;<font color='#FF0000'>*</font>&nbsp;����ѡ����������</TD>
					</TR>

					<TR bgcolor="#FFFFFF" id="userPasswd">
						<TD class=column align="right" nowrap>�û��ʻ�</TD>
						<TD>
						<%
							if ("12".equals(gwOptType) || "22".equals(gwOptType)) {
						%> <INPUT TYPE="text" NAME="username" maxlength=50 class=bk
							value="<%=username%>" readonly='readonly'> <%
 							} else {
						 %> <INPUT TYPE="text" NAME="username" maxlength=50 class=bk
							value="<%=username%>" onblur="SearchUName(new Date())">
						<%
							}
						%> &nbsp; <font color="#FF0000">*</font> <span id="isOK"></span>&nbsp;����̬IP,��ר�ߺţ�</TD>
						<TD class=column align="right" nowrap>�û�����</TD>
						<TD><INPUT TYPE="text" NAME="passwd" maxlength=50 class=bk
							value="<%=passwd%>"> &nbsp; <font color="#FF0000">*</font>&nbsp;����̬IP,��ר�ߺţ�
						</TD>
					</TR>
					
					<tr bgcolor="#FFFFFF">
						<td class=column align="right" nowrap>�ͻ���ѯ</td>
						<!-- onchange="findCustomer()"  -->
						<td name="srchtd"><input type="text" name="custName" class=bk>&nbsp;
						<input type="button" name="srch" value="ģ����ѯ" onclick="findCustomer()" style="border: 1px solid #999999"></td>
						
						<td class=column align="right" nowrap>�ͻ�����</td>
						<td><div id="customer_nameDiv"></div></td>
					</tr>
					
					<TR id="ipaddrSpan" bgcolor="#FFFFFF" style="display: none">
						<TD class=column align='right' nowrap>IP��ַ</TD>
						<TD><input type='text' name='ipaddress' class=bk
							value=<%= ipaddress%>>&nbsp;</TD>
						<TD class=column align='right' nowrap>����</TD>
						<TD><INPUT TYPE='text' NAME='ipmask' class=bk
							value='<%=ipmask%>'></TD>
					</TR>
					<TR id="ipaddrSpan2" bgcolor="#FFFFFF" style="display: none">
						<TD class=column align='right' nowrap>����</TD>
						<TD><input type='text' name='gateway' class=bk
							value=<%= gateway%>></TD>
						<TD class=column align='right' nowrap>DNS��ַ</TD>
						<TD><INPUT TYPE='text' NAME='adsl_ser' class=bk
							value='<%=adsl_ser%>'></TD>
					</TR>
					
					
					
					<TR bgcolor="#FFFFFF">
						<TD colspan="4" height="20"></TD>
					</TR>
					<%if ("12".equals(gwOptType) || "22".equals(gwOptType)) { %>
					<tr bgcolor="#FFFFFF" >
						<TD class=column align="right" nowrap>�����豸</TD>
						<TD colspan=3 name="clearBtntd"><input type="hidden" name="some_device"
							value="">&nbsp;<input type="text" name="oui"
							class=bk size="12" value="<%= oui %>" readonly>&nbsp;-&nbsp;<input
							type="text" name="device_serialnumber" class=bk size="32"
							value="<%= device_serialnumber %>" readonly>&nbsp; <IMG
							STYLE="display:none" 
							onClick="selDev()" SRC="../images/search.gif" WIDTH="15"
							HEIGHT="12" BORDER="0" ALT="ѡ��">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="hidden" name="clearBtn" value="����豸" onclick="clearDev()" style="border: 1px solid #999999">
							<!-- <a href="javascript:clearDev()">����豸</a></TD> -->
					</tr>
					<%} %>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�û�״̬����</TD>
						<TD>
							<INPUT TYPE="text" NAME="user_state_dis" disabled=true class=bk size=13>
							<INPUT TYPE="hidden" NAME="user_state" readonly=true size=13>
							<!-- <select name="user_state" class=bk >
								<option value="1" <% if ("1".equals(user_state)) {%> selected
									<%}%>>����</option>
								<option value="2" <% if ("2".equals(user_state)) {%> selected
									<%}%>>��ͣ</option>
								<option value="3" <% if ("3".equals(user_state)) {%> selected
									<%}%>>����</option>
							</select> &nbsp; -->
						
						</TD>
						<TD class=column align="right">����״̬</TD>
						<TD><SELECT name="opmode" onChange="" class=bk>
							<option value="0" <% if (opmode.equals("0")) {%> selected <%}%>>
							δ����</option>
							<option value="1" <% if (opmode.equals("1")) {%> selected <%}%>>
							�ѿ���</option>
						</SELECT> &nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">����ģʽ</td>
						<td><select name="work_model" class=bk>
							<option value="-1">==��ѡ��==</option>
							<option value="1">==��==</option>
							<option value="2">==��==</option>
							<option value="3">==���ؾ���==</option>
						</select></td>
						<TD class=column align="right">����ʱ��</TD>
						<TD><INPUT TYPE="text" NAME="onlinedate" class=bk
							value="<%= onlinedate%>" size=13 readonly> <INPUT
							TYPE="button" value="��" class=btn onclick="showCalendar('day',event)">
						<input type="text" name="onlineTime" value="<%=onlineTime%>"
							class=bk size=13> <INPUT TYPE="hidden"
							NAME="hidOnlineDate" class=bk></TD>
					</TR>
					<TR bgcolor="#FFFFFF">

						<TD class=column align="right">��������ʱ��</TD>
						<TD><INPUT TYPE="text" NAME="dealdate" class=bk size=13
							value="<%=dealdate%>" readonly> <INPUT TYPE="button"
							value="��" class=btn onclick="showCalendar('day',event)"> <input
							type="text" name="time" value="<%=time%>" class=bk size=13>
						<INPUT TYPE="hidden" NAME="hidDealDate" class=bk></TD>
						<TD class=column align="right">����ʱ��</TD>
						<TD><INPUT TYPE="text" NAME="opendate" class=bk
							value="<%=opendate %>" size=13 readonly> <INPUT
							TYPE="button" value="��" class=btn onclick="showCalendar('day',event)">
						<input type="text" name="openTime" value="<%=openTime%>" class=bk
							size=13> <INPUT TYPE="hidden" NAME="hidOpenDate" class=bk>
						&nbsp;</TD>
					</TR>

					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">��ͣʱ��</TD>
						<TD><INPUT TYPE="text" NAME="pausedate" class=bk
							value="<%=pausedate %>" size=13 readonly> <INPUT
							TYPE="button" value="��" class=btn onclick="showCalendar('day',event)">
						<input type="text" name="pauseTime" value="<%=pauseTime%>"
							class=bk size=13> <INPUT TYPE="hidden"
							NAME="hidPauseDate" class=bk></TD>
						<TD class=column align="right">����ʱ��</TD>
						<TD><INPUT TYPE="text" NAME="closedate" class=bk
							value="<%=closedate %>" size=13 readonly> <INPUT
							TYPE="button" value="��" class=btn onclick="showCalendar('day',event)">
						<input type="text" name="closeTime" value="<%=closeTime%>"
							class=bk size=13> <INPUT TYPE="hidden"
							NAME="hidCloseDate" class=bk> <INPUT TYPE="hidden"
							NAME="updatetime" class=bk value="<%= updatetime%>" size=13>
						<input type="hidden" name="upTime" value="<%=upTime%>" class=bk
							size=13> <INPUT TYPE="hidden" NAME="hidUpdateTime"
							class=bk> <INPUT type="hidden" name="staff_id"
							value="<%=staff_id%>"></TD>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td class=column align="right">���LAN����</td>
						<td><input type="text" name="lan_num" value="<%=lan_num %>"
							class=bk maxlength="10">������</td>
						<td class=column align="right">SSID����</td>
						<td><input type="text" name="ssid_num" value="<%=ssid_num %>"
							class=bk maxlength="10">������</td>
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">��������</TD>
						<TD><INPUT TYPE="text" NAME="maxattdnrate" maxlength=10
							class=bk value="<%=maxattdnrate %>">(bps)</TD>
						<TD class=column align="right">��������</TD>
						<TD><INPUT TYPE="text" NAME="upwidth" maxlength=10 class=bk
							value="<%=upwidth %>">(bps)</TD>
					</TR>

					<TR>
						<TD colspan="4" align="right" class="green_foot" name="saveBtntd"><INPUT
							TYPE="button" name="saveBtn" value=" �� �� " class="btn" onclick="CheckForm()" id="saveBtn">
						&nbsp;&nbsp; <INPUT TYPE="reset" value=" �� д " class="btn" name="resetBtn">
						<INPUT TYPE="hidden" name="action" value=""></TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</FORM>
		</TD>
	</TR>
</TABLE>
<script LANGUAGE="JavaScript">
<!--
	
	//����ҵ�����ͣ�����������ʽ
	var servtype = "<%=selectServTypeId%>";

	wanTp(servtype);
	//�����������ͣ��Ƿ����IP��ַ����
   	var wtype = "<%= wan_type%>";
   	
   	ShowIPAddr(wtype);

	//��ʼ������ģʽѡ��
	document.frm.work_model.value = "<%=work_model%>";
	
	//�༭��ʱ����ؿͻ�����
    var custId = "<%=selectedENamesId%>";
	sendRequest("get","findCustomer.jsp?type=id&value=" + custId,document.all.customer_nameDiv);
	
	//�����û�״̬����
	setUser_state();
	
	//�����û�״̬����	
	function setUser_state(){
		var user_state = "<%=user_state%>";
		var user_state_ch;
		var user_state_sub = user_state;
		if("1"==user_state){
			user_state_ch = "����";
		}else if("2"==user_state){
			user_state_ch = "��ͣ";
		}else if("3"==user_state){
			user_state_ch = "����";
		}else{
			user_state_ch = "����";
			user_state_sub = "1";
		}
		document.frm.user_state_dis.value = user_state_ch;
		document.frm.user_state.value = user_state_sub;
	}
//-->
</script>

<br>
<%@ include file="../foot.jsp"%>
