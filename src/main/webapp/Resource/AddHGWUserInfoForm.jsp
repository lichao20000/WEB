<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Date"%>
<%@page import="com.linkage.litms.common.util.*,com.linkage.litms.LipossGlobals"%>
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />


<SCRIPT LANGUAGE="JavaScript" id="idParentFun">
var m_bShow_pe; 
var m_bShow_ce;
var selected_value;
var selected_value_ce;
</SCRIPT>


<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
	String curCity_ID = curUser.getCityId();
/**
	String curGather_ID = "-1";
	Cursor cursorTmp = DataSetBean
			.getCursor("select gather_id,descr from tab_process_desc where city_id = '"
			+ curCity_ID + "'");
	Map fieldTmp = cursorTmp.getNext();
	if (fieldTmp != null) {
		curGather_ID = (String) fieldTmp.get("gather_id");
	}
*/
	request.setCharacterEncoding("GBK");
	//��ȡ����װ����ShortName
	String shortName = LipossGlobals.getLipossProperty("InstArea.ShortName");
	if (shortName == null) shortName = "";

	String strCityList = HGWUserInfoAct.getCityListSelf(false,curCity_ID, "",
			request);
	String servTypeList = HGWUserInfoAct.getServTypeList(1);
	String strOfficeList = HGWUserInfoAct.getOfficeList(false, "", "");
	String strZoneList = HGWUserInfoAct.getZoneList(false, "", "");

	String strGatherList = DeviceAct.getGatherList(session, "", "",
			false);
	//�豸�б�
	//String strDeviceList = HGWUserInfoAct.getDeviceByArea(false,"","",request);
	Cursor cursor_device = HGWUserInfoAct.getDeviceByArea(request);
	Map fields_device = cursor_device.getNext();

	//�����б�
	//String strVendorList = DeviceAct.getVendorList(true, "", "");
	//ҵ���б�
	//������еķ�����Ϣ
	Cursor cursor_service = HGWUserInfoAct.getServiceInfo();
	Map fields_service = cursor_service.getNext();

	Date curDtae = new Date();
	//String dealdate = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",curDtae.getTime()/1000);
	//String longtime = new DateTimeUtil(curDtae.getTime()).getLongDate();
	String dealdate = new DateTimeUtil(curDtae.getTime()).getDate();
	String time=new DateTimeUtil(curDtae.getTime()).getTime();
	
	//String curTime = StringUtils.formatDate("yyyy-MM-dd",new Date().getTime()/1000);	
	
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function check(){
		document.all("childframe").src = "checkDevice.jsp?oui="+document.frm.oui.value+"&vender="+document.frm.vender.value;
		
		if (document.frm.device_id.value == ""){
			if (!confirm("�ü�ͥ�����豸�����ڣ��Ƿ��������")){
				alert("����ȡ��!");
				obj.oui.focus();
				return false;
			}
			else{
				CheckForm();
			}
		}
	}

	function CheckForm() {
		var obj = document.frm;
		
		//document.all("childFrm").src = "checkUserName.jsp?username="+document.frm.username.value;
	    
		//var retValue   = window.showModalDialog("./checkDevice.jsp?oui="+document.frm.oui.value+"&vender="+document.frm.vender.value,window,"dialogLeft:1px;dialogTop:1px;dialogHeight:0px;dialogWidth:0px");
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
		}else if(obj.vpiid.value == ""){
			 alert("������VPI");
		     obj.vpiid.focus();
		     return false;
		}else if(obj.vciid.value == ""){
			 alert("������VCI");
		     obj.vciid.focus();
		     return false;
		} else if(obj.vciid.value!="" && !IsNumber(obj.vciid.value,"VCI")){
			obj.vciid.focus();
			obj.vciid.select();
			return false;
		} else if(obj.vpiid.value!="" && !IsNumber(obj.vpiid.value,"VPI")){
			obj.vpiid.focus();
			obj.vpiid.select();
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
		} else if(obj.bandwidth.value!="" && Trim(obj.bandwidth.value)==""){
			alert("����ӦΪ����");
			obj.bandwidth.focus();
			obj.bandwidth.select();
			return false;
		} else if(obj.bandwidth.value!="" && !IsNumber(obj.bandwidth.value,"����")){		
			obj.bandwidth.focus();
			obj.bandwidth.select();
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
		} else if(obj.userline.value!="" && Trim(obj.userline.value)==""){
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
		} else if(obj.upwidth.value!="" && !IsNumber(obj.upwidth.value,"���г�ŵ����")){		
			obj.upwidth.focus();
			obj.upwidth.select();
			return false;
		} else if(obj.max_user_number.value!="" && Trim(obj.max_user_number.value)==""){
			alert("�����û�������ӦΪ����");
			obj.max_user_number.focus();
			obj.max_user_number.select();
			return false;
		} else if(obj.max_user_number.value!="" && !IsNumber(obj.max_user_number.value,"�����û�������")){		
			obj.upwidth.focus();
			obj.upwidth.select();
			return false;
		} else {
			obj.hidOpenDate.value = DateToDes(obj.opendate.value);
			obj.hidOnlineDate.value = DateToDes(obj.onlinedate.value);
			obj.hidPauseDate.value = DateToDes(obj.pausedate.value);
			obj.hidCloseDate.value = DateToDes(obj.closedate.value);
			obj.hidUpdateTime.value = DateToDes(obj.updatetime.value);
			
			obj.hidMoveDate.value = DateToDes(obj.movedate.value);
			obj.hidDealDate.value = DateToDes_long(obj.dealdate.value,obj.time.value);	
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

	function SearchUName() {
		var f = document.frm;
		var username = f.username.value;
		if (username == "")	{
			f.username.focus();
			return false;
		} else {
			send_request('checkUserName.jsp?username='+username);
		}
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
				<TABLE width="95%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite">
										�û���Դ
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										��'
										<font color="#FF0000">*</font>'�ı�������д��ѡ��
									</td>
								</tr>
							</table>
						</td>
					</tr>

					<TR>
						<TD bgcolor=#999999>

							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR>
									<TH colspan="4" align="center">
										��ͥ�����û����
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
											value="" onChange="SearchUName()">
										&nbsp;
										<font color="#FF0000">*</font>
										<span id="isOK"></span>
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
									<TD colspan="3">
										<SELECT name="wan_type" class=bk>
											<option value="-1">
												==��ѡ��==
											</option>
											<option value="1" selected>
												�Ž�
											</option>
											<option value="2">
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
											value="">
										&nbsp;
										<font color="#FF0000">*</font>
									</TD>
									<TD class=column align="left">
										������֤
									</TD>
									<TD>
										<INPUT TYPE="password" NAME="validate_passwd" maxlength=100
											class=bk value="">
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
											value="8">
										&nbsp;
										<font color="#FF0000">*</font>
									</TD>
									<TD class=column align="left">
										VCI
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="vciid" maxlength=15 class=bk
											value="35">
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
										&nbsp; ���к�:&nbsp;
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
										&nbsp;
										
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
										<%=strOfficeList%>
										&nbsp;
									</TD>
									<TD class=column align="left">
										С����ʶ
									</TD>
									<TD>
										<%=strZoneList%>
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
									<TD class=column align="left">
										�û�ʵ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="realname" maxlength=20 class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										�Ա�
									</TD>
									<TD>
										<SELECT NAME="sex" class=bk>
											<option value="��">
												��
											</option>
											<option value="Ů">
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
										<INPUT TYPE="text" NAME="linkman" class=bk>
										&nbsp;
									</TD>


									<TD class=column align="left">
										��ϵ�绰
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="linkphone" class=bk>
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
									<TD class=column align="left">
										��ϵ�˵�ַ
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="linkaddress" class=bk>
										&nbsp;
									</TD>

									<TD class=column align="left">
										��ϵ���ֻ�
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="mobile" class=bk>
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
									<TD class=column align="left">
										��ϵ��email
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="email" class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										����
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="bandwidth" maxlength=15 class=bk
											value="2048">
										(bps)
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
									<TD class=column align="left">
										Adsl�󶨵绰
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="phonenumber" maxlength=15 class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										��������ʱ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="dealdate" class=bk
											value="<%=dealdate%>">
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
											value="2048">
										(bps)
									</TD>
									<TD class=column align="left">
										���г�ŵ����
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="upwidth" maxlength=15 class=bk
											value="512">
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
										<INPUT TYPE="text" NAME="cotno" maxlength=100 class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										���ڵ���id
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="cableid" maxlength=15 class=bk>
									</TD>

								</TR>




								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										�Ʒ����
									</TD>
									<TD>
										<SELECT name="bill_type_id" onChange="" class=bk>
											<option value="0">
												��ʱ��
											</option>
											<option value="1">
												����
											</option>
											<option value="2">
												����
											</option>
											<option value="3">
												������
											</option>
										</SELECT>
										&nbsp;
									</TD>
									<TD class=column align="left">
										���¼Ʒ����
									</TD>
									<TD>
										<SELECT name="next_bill_type_id" onChange="" class=bk>
											<option value="0">
												��ʱ��
											</option>
											<option value="1">
												����
											</option>
											<option value="2">
												����
											</option>
											<option value="3">
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
											<option value="1" selected>
												����
											</option>
											<option value="2">
												��ͣ
											</option>
											<option value="3">
												����
											</option>
										</select>
										&nbsp;
									<TD class=column align="left">
										����ʱ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="opendate" class=bk>
										<INPUT TYPE="button" value="��" class=btn
											onclick="showCalendar('day',event)">
										<INPUT TYPE="hidden" NAME="hidOpenDate" class=bk>
										&nbsp;

									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										�ͻ�����
									</TD>
									<TD>
										<SELECT name="cust_type_id" onChange="" class=bk>
											<option value="0">
												��˾�ͻ�
											</option>
											<option value="1">
												���ɿͻ�
											</option>
											<option value="2" selected>
												���˿ͻ�
											</option>
										</SELECT>
										&nbsp;
									</TD>
									<TD class=column align="left">
										�û�����
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="user_type_id" maxlength=20 class=bk>
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										֤������
									</TD>
									<TD>
										<SELECT name="cred_type_id" onChange="" class=bk>
											<option value="0">
												==��ѡ��==
											</option>
											<option value="1">
												����
											</option>
											<option value="2" selected>
												���֤
											</option>
											<option value="3">
												����֤
											</option>
											<option value="4">
												����֤
											</option>
										</SELECT>
										&nbsp;
									</TD>
									<TD class=column align="left">
										֤������
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="credno" maxlength=30 class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										��װסַ
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="address" maxlength=20 class=bk>
									</TD>
									<TD class=column align="left">
										�������ʱ�ʶ
									</TD>
									<TD>
										<SELECT name="access_kind_id" onChange="" class=bk>
											<option value="0">
												��ҵ
											</option>
											<option value="1">
												����
											</option>
											<option value="2" selected>
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
										<SELECT name="bindtype" onChange="" class=bk>
											<option value="0">
												ADSL��
											</option>
											<option value="1">
												IPTV��
											</option>
											<option value="2">
												VOIP��
											</option>
										</SELECT>
										&nbsp;
									</TD>
									<TD class=column align="left">
										�������
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="virtualnum" maxlength=20 class=bk>
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										��������
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="numcharacter" maxlength=20 class=bk>
									</TD>
									<TD class=column align="left">
										���뷽ʽ��ʶ
									</TD>
									<TD>
										<SELECT name="access_style_id" onChange="" class=bk>
											<option value="0">
												ADSL����
											</option>
											<option value="1">
												LAN����
											</option>
											<option value="2">
												���˽���
											</option>
										</SELECT>
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										��֤��־
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="aut_flag" maxlength=20 class=bk>
									</TD>
									<TD class=column align="left">
										���ܷ������ͱ��뼯
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="service_set" maxlength=30 class=bk>
									</TD>

								</TR>

								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										��λ�ͻ���ҵ���
									</TD>
									<TD>
										<SELECT name="trade_id" onChange="" class=bk>
											<option value="0">
												������ҵ
											</option>
											<option value="1">
												������ҵ
											</option>
											<option value="2">
												������ҵ
											</option>
											<option value="3">
												������ҵ
											</option>
										</SELECT>
										&nbsp;
									</TD>
									<TD class=column align="left">
										����Ӫҵִ��ע���
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="licenceregno" class=bk>
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										����ְҵ����
									</TD>
									<TD>
										<SELECT name="occupation_id" onChange="" class=bk>
											<option value="0">
												������Ա
											</option>
											<option value="1">
												����Ա
											</option>
											<option value="2">
												������
											</option>
											<option value="3">
												�ϰ�
											</option>
											<option value="4">
												����ְҵ��
											</option>
											<option value="5">
												����ְҵ��
											</option>
										</SELECT>
									</TD>
									<TD class=column align="left">
										�����̶ȴ���
									</TD>
									<TD>
										<SELECT name="education_id" onChange="" class=bk>
											<option value="0">
												����
											</option>
											<option value="1">
												��ר
											</option>
											<option value="2">
												��ѧ����
											</option>
											<option value="3">
												˶ʿ
											</option>
											<option value="4">
												��ʿ
											</option>
											<option value="5">
												����
											</option>
										</SELECT>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										VIP����
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="vipcardno" class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										��ϵ�����֤��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="linkman_credno" maxlength=20 class=bk>
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										������
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="agent" class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										���������֤��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="agent_credno" class=bk>
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										��������ϵ�绰
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="agentphone" class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										ADSL�豸��Դ
									</TD>
									<TD>
										<SELECT name="adsl_res" onChange="" class=bk>
											<option value="0">
												��Ӫ���ṩ
											</option>
											<option value="1">
												���˹���
											</option>
										</SELECT>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										ADSL����֤��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="adsl_card" class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										ADSL�豸�ͺ�
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="adsl_dev" class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										ADSL�豸���
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="adsl_ser" maxlength=15 class=bk>
									</TD>
									<TD class=column align="left">
										�Ƿ��ά
									</TD>
									<TD>
										<INPUT TYPE="radio" NAME="isrepair" value="0" checked>
										��&nbsp;&nbsp;
										<INPUT TYPE="radio" NAME="isrepair" value="1">
										��
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										Э����
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="contractno" class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										�û�IP��ַ
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="ipaddress" maxlength=15 class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										����IP��ַ������
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="overipnum" maxlength=15 class=bk>
									</TD>
									<TD class=column align="left">
										����
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="ipmask" maxlength=15 class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										����
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="gateway" maxlength=15 class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										MAC��ַ
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="macaddress" maxlength=15 class=bk>
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										�����û�������
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="max_user_number" maxlength=15
											class=bk>
									</TD>
									<TD class=column align="left">
										�����豸ip
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="device_ip" maxlength=15 class=bk>
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										�����豸���ܺ�
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="device_shelf" maxlength=15 class=bk>
										&nbsp;
									</TD>

									<TD class=column align="left">
										�����豸���
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="device_frame" maxlength=15 class=bk>
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										�����豸��λ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="device_slot" maxlength=15 class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										�����豸�˿�
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="device_port" maxlength=15 class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										BAS�����豸���
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="basdevice_id" maxlength=15 class=bk>
									</TD>
									<TD class=column align="left">
										BAS�����豸ip
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="basdevice_ip" maxlength=15 class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										BAS�����豸���ܺ�
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="basdevice_shelf" maxlength=15
											class=bk>
									</TD>
									<TD class=column align="left">
										BAS�����豸���
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="basdevice_frame" maxlength=15
											class=bk>
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
										<INPUT TYPE="text" NAME="basdevice_port" maxlength=15 class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										VlanID��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="vlanid" maxlength=15 class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										������
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="workid" maxlength=15 class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										��ͨʱ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="onlinedate" class=bk value="<%= dealdate%>">
										<INPUT TYPE="button" value="��" class=btn
											onclick="showCalendar('day',event)">
										<INPUT TYPE="hidden" NAME="hidOnlineDate" class=bk>
									</TD>

									<TD class=column align="left">
										��ͣʱ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="pausedate" class=bk>
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
										<INPUT TYPE="text" NAME="closedate" class=bk>
										<INPUT TYPE="button" value="��" class=btn
											onclick="showCalendar('day',event)">
										<INPUT TYPE="hidden" NAME="hidCloseDate" class=bk>
									</TD>
									<TD class=column align="left">
										����ʱ��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="updatetime" class=bk value="<%= dealdate%>">
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
										<INPUT TYPE="text" NAME="staff_id" maxlength=15 class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										��ע
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="remark" maxlength=15 class=bk>
									</TD>

								</TR>

								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										ADSL����
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="adsl_hl" maxlength=15 class=bk>
									</TD>
									<TD class=column align="left">
										����Э��
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="opmode" maxlength=15 class=bk>
									</TD>

								</TR>

								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										�û���·
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="userline" maxlength=15 class=bk>
									</TD>
									<TD class=column align="left">
										Dslam�豸���
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="dslamserialno" maxlength=15 class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										�ƶ�����
									</TD>
									<TD colspan="3">
										<INPUT TYPE="text" NAME="movedate" class=bk>
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
										<INPUT TYPE="hidden" name="action" value="add">

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
<iframe id="childFrm" style="display:none"></iframe>
<iframe id="childFrm1" style="display:none"></iframe>
<script language="javascript">
/**
show();
function show() {
	var shortName = '';
	var tr = document.getElementById("wanType");
	if (tr != null) {
		if (tr.isShow == shortName) {
			tr.style.display = "";
		}
	}
}

*/

function showVal(obj) {
	//alert(obj.value);
}
var vNow = new Date();
var y0  = vNow.getYear();
var m0  = vNow.getMonth()+1;
var d0  = vNow.getDate();


document.frm.opendate.value = y0 + "-" + m0 + "-" + d0;
//document.frm.hidOpenDate.value = "<%=curDtae.getTime() / 1000%>";

function selDev()
{
	var retValue = window.showModalDialog("./selDeviceList.jsp?oui="+document.frm.oui.value+"&vender="+document.frm.vender.value + "&gw_type=1",window,"dialogHeight:600px;dialogWidth:800px");
	
	if (typeof(retValue) != 'undefined'){
		document.frm.some_device.value = retValue;
		var temp = retValue.split('/');
		document.frm.oui.value = temp[0];
		document.frm.vender.value = temp[1];
	}
	
	//window.open("./selDeviceList.jsp?queryStr="+document.frm.some_device.value);
}
</script>
<br>
<%@ include file="../foot.jsp"%>