<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.*"%>
<%@ page import="com.linkage.litms.common.filter.*"%>
<jsp:useBean id="areaManage" scope="request" class="com.linkage.litms.system.dbimpl.AreaManageSyb"/>
<jsp:useBean id="group" scope="request" class="com.linkage.litms.system.Group"/>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<%
request.setCharacterEncoding("GBK");
/*
String strSQL = "select city_id,city_name from tab_city order by city_id";
Cursor cursor = DataSetBean.getCursor(strSQL);
String strCityList = FormUtil.createListBox(cursor, "city_id","city_name",false,"","per_city");
*/
//String strCityList = DeviceAct.getCityListSelf(false,"","per_city",request);

String InstArea = LipossGlobals.getLipossProperty("InstArea.ShortName");

String city_id = curUser.getCityId();
SelectCityFilter CityFilter = new SelectCityFilter(request);
String strCityList = CityFilter.getAllSubCitiesBox(city_id, false,city_id, "per_city", true); 
//Ĭ������
String defPwd = "";
if("cq_dx".equals(InstArea)){
	defPwd = "1q2w3e$R%T";
}

// 20200506 ɽ����ͨ �������������û���ֻ��һ������Ĭ����д�Ǹ�
String defaultAreaId = "";
String defaultAreaName = "";
String defaultGroupOid = "";
String defaultGroupName = "";
if("sx_lt".equals(InstArea)){
	// ����
	long userAreaId = user.getAreaId();
	List<Map> areaList = null;
	if(user.getAccount().equals("admin")){//���ж���ʾ
		areaList = areaManage.getAreaAll();
	}else{
		areaList = areaManage.getAreaById(Integer.parseInt(String.valueOf(userAreaId)));
	}
	if(areaList.size() == 1){
		defaultAreaId = String.valueOf(areaList.get(0).get("area_id"));
		defaultAreaName = String.valueOf(areaList.get(0).get("area_name"));
	}
	
	// �����û���
	List<Map> groupList = new ArrayList<Map>();
	Cursor cursor = group.getGroupsAll();
	Map fields = cursor.getNext();
	if(fields != null){
		while(fields != null){
			groupList.add(fields);
			fields = cursor.getNext();
		}
	}
	if(groupList.size() == 1){
		defaultGroupOid = String.valueOf(groupList.get(0).get("group_oid"));
		defaultGroupName = String.valueOf(groupList.get(0).get("group_name"));
	}
	
}



%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<style>
NOBR.BT
{
	padding-right: 6px;
	padding-left: 6px;
	padding-top: 2px;
	border-top: #ffffff 1px solid;
	border-bottom: #ffffff 1px solid;
	border-right: #ffffff 1px solid;
	border-left: #ffffff 1px solid;
	cursor:hand;
}

NOBR.BTOver{
	padding-right: 6px;
	padding-left: 6px;
	padding-top: 2px;
	border-top: #316AC5 1px solid;
	border-bottom: #316AC5 1px solid;
	border-right: #316AC5 1px solid;
	border-left: #316AC5 1px solid;
	cursor:hand;
	background-color: #C1D2EE;
}
</style>
<SCRIPT LANGUAGE="JavaScript" src="../Js/code.js"></SCRIPT>
<script type="text/javascript" src="../Js/jquery.js"></script>

<SCRIPT LANGUAGE="JavaScript">
<!--
var model="<%=model%>";
var InstArea="<%=InstArea%>";


function IsAccountS(strValue,strMsg){
	var bz = true;
	var i=0;
	var j=0;
	if(IsNull(strValue,strMsg)){
		 for(var i=0;i<strValue.length;i++){
			 var ch=strValue.substring(i,i+1);
			 if (i == 0 && ch == '_')
			 {
				 alert(strMsg+"�������»��߿�ͷ");
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


function CheckForm(){    
	
	var obj = document.frm;
	if(InstArea=="cq_dx")
	{
		var cityIds=$("input[@name='cityIds']").val();
		if(cityIds=="")
			{
			 alert('��ѡ�����أ�');
			    return false;
			}
	}else if(obj.per_city.value=='-1'){
    alert('��ѡ�����أ�');
    return false;
	}
	
	//�ж������Ƿ���������ַ�
	var pwdptn = new RegExp("([\u4E00-\u9FA5]|[\uFE30-\uFFA0])+");

	if('hb_lt' == InstArea )
	{
	    if(!IsAccountS(obj.acc_loginname.value,'��¼�˺�')){
    		obj.acc_loginname.focus();
    		obj.acc_loginname.select();
    		return false;
    	}
	}
	else
	{
	    if(!IsAccount(obj.acc_loginname.value,'��¼�˺�')){
            obj.acc_loginname.focus();
            obj.acc_loginname.select();
            return false;
        }
	}
	
	if(pwdptn.test(obj.acc_password.value)){
		alert("�������ݲ��ܰ��������ַ�");
		obj.acc_password.focus();
		obj.acc_password.select();
		return false;
	}	
	
	if(!IsPassword(obj.acc_password.value,'��¼����',obj.acc_loginname.value,InstArea)){
		obj.acc_password.focus();
		obj.acc_password.select();
		return false;
	}
	
	if(!IsNull(obj.area_name.value,'��������')){
		obj.acc_password.focus();
		obj.acc_password.select();
		return false;
	}
	
	if(!IsNull(obj.per_name.value,'����'))
	{
		return false;
	}
	/*
	else if(model=="/model_vip" && !IsNull(obj.customname.value,'������ͻ�'))
	{
		return false;
	}
	*/
	
	if(!IsNull(obj.gr_oid.value,'��ɫ')){
		//obj.gr_oid.focus();
		//obj.gr_oid.select();
		return false;
	}
	
	if(!IsNull(obj.gg_oid.value,'�û���')){
		//obj.gg_oid.focus();
		//obj.gg_oid.select();
		return false;
	}
	
	if(Trim(obj.per_email.value)!="" && !IsMail(obj.per_email.value,'E-Mail��ַ')){
		obj.per_email.focus();
		obj.per_email.select();
		return false;
	}
	
	if ("ah_dx" == InstArea && obj.per_dep_oid.value == 0) {
		alert("�������Ų���Ϊ��");
		return false;
	}
	
	if(!IsNull(obj.per_mobile.value,'�ֻ�����')){
		return false;
	}
	
	if(Trim(obj.per_mobile.value)!="" && obj.per_mobile.value.length!=11){
		alert("�ֻ�����ӦΪ11λ");
		obj.per_mobile.focus();
		obj.per_mobile.select();
		return false;
	}
	
	if(Trim(obj.acc_validatemonth.value)!=""&&!IsNumber(obj.acc_validatemonth.value,"������Ч��"))
	{
	    obj.acc_validatemonth.focus();
	    obj.acc_validatemonth.select();
	    return false;
	}
	
	if(Trim(obj.login_error.value)!="" && !IsNumber(obj.login_error.value,'��¼ʧ�ܴ���'))
	{
	    obj.login_error.focus();
	    obj.login_error.select();
	    return false;
	}
	
	if(Trim(obj.lock_time.value)!="" && !IsNumber(obj.lock_time.value,'��¼ʧ������ʱ��'))
	{
	    obj.lock_time.focus();
	    obj.lock_time.select();
	    return false;
	}
	
	if(Trim(obj.login_free.value)!="" && !IsNumber(obj.login_free.value,'��¼�ɿ���ʱ��'))
	{
	    obj.login_free.focus();
	    obj.login_free.select();
	    return false;
	}
	
	if(Trim(obj.min_passwdlength.value)!="" && !IsNumber(obj.min_passwdlength.value,'������С����'))
	{
	    obj.min_passwdlength.focus();
	    obj.min_passwdlength.select();
	    return false;
	}
	
	if(InstArea != "nx_lt" && !IsNull(obj.per_searchcode.value,'���֤��'))	
	{
	    return false;
	}	
	
	if(InstArea != "nx_lt" && Trim(obj.per_searchcode.value)!="" && obj.per_searchcode.value.length!=18){
		alert("���֤��ӦΪ18λ");
		obj.per_mobile.focus();
		obj.per_mobile.select();
		return false;
	}
	
	if(Trim(obj.user_ips.value)!=""&&!checkIPValue(obj.user_ips.value,'�û���¼IP'))	
	{
	    alert("������û�IP��ʽ����ȷ")
	    obj.user_ips.focus();
	    obj.user_ips.value="";
	    return false;
	}
	
	if(Trim(obj.login_error.value) > 5)
	{
		alert("��¼ʧ�ܴ������ܳ���5");
	    obj.login_error.focus();
		obj.login_error.select();
	    return false;
	}
	
	if(Trim(obj.acc_validatemonth.value) > 3)
	{
		alert("������Ч�ڲ��ܳ���3����");
	    obj.acc_validatemonth.focus();
		obj.acc_validatemonth.select();
	    return false;
	}

	/*obj.per_gender.value = getRadioValue(obj.tmpper_gender);
	getDateTime(obj);*/
	return true;
}

function getRadioValue(o){
	if(o.length >1){
		for(var i=0;i<o.length;i++){
			if(o[i].checked){
				return o[i].value;
			}
		}
		return -1;
	}
	else{
		if(o.checked) return o.value;
		else return -1;
	}
}

function getDateTime(o){
	o1 = o.year;
	o2 = o.month;
	o3 = o.day;
	if(o1.value != "" && o2.value != "0" && o3.value != "0"){
		o.per_birthdate.value = o1.value+"-"+o2.value+"-"+o3.value;
	}
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<BR>
<FORM NAME="frm" METHOD="post" ACTION="UserSave.jsp" onsubmit="return CheckForm()">
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="98%" border=0 align="center">
<TR>
	<TD>
		<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">
					ϵͳ����Ա����
				</td>
				<td>
					<img src="../images/attention_2.gif" width="15" height="12">
					&nbsp;&nbsp;ϵͳ����Ա���,��&nbsp;<font color="#FF0000">*</font>&nbsp;����ѡ�����д
				</TD>
			</TR>
		</TABLE>
	</TD>
</TR>

<TR>
	<TD>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<input type="hidden" name="cityIds" id="cityIds" value=""/>
				<TR>
					<TH  colspan="4" align="center"><B>�½�����Ա</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width='15%'>��¼�˺�</TD>
					<TD  width='35%'>
						<INPUT TYPE="text" NAME="acc_loginname" class="bk">&nbsp;<font color="#FF0000">*</font>
					</TD width='15%'>
					<TD class=column align="right">��¼����</TD>
					<TD width='35%'>
						<INPUT TYPE="password" NAME="acc_password" class="bk" value="<%=defPwd%>">&nbsp;<font color="#FF0000">*</font>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">��    ��</TD>
					<TD>
						<INPUT TYPE="text" NAME="per_name" class="bk" onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"  onpaste="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"oncontextmenu ="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')" "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
						<font color="#FF0000">*��֧�����ġ�Ӣ�ġ�����</font>
					</TD>
					<%
						if("cq_dx".equals(InstArea)){
					%>
					<TD style="cursor:pointer" class=column align="right" onclick="test()"><u>����</u></TD>
					<TD>
					</TD>
					<%} else{%>
					<TD class=column align="right">��    ��</TD>
					<TD>
						<%=strCityList%>
					</TD>
					<%
					}
					%>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">��¼ʧ�ܴ���</TD>
					<TD>
						<INPUT TYPE="text" NAME="login_error" value="5" class="bk">
					</TD>
					<TD class=column align="right">��¼ʧ������ʱ��</TD>
					<TD>
					   <INPUT TYPE="text" NAME="lock_time" value="1800" class="bk">(��λ����)
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">��¼�ɿ���ʱ��</TD>
					<TD>
						<INPUT TYPE="text" NAME="login_free" class="bk">(��λ����)
					</TD>
					<TD class=column align="right">��С���볤��</TD>
					<TD>
					   <INPUT TYPE="text" NAME="min_passwdlength" value="8" class="bk">
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
				    <TD class=column align="right">������Ч��</TD>
					<TD>
					   <INPUT TYPE="text" NAME="acc_validatemonth" value="3" class="bk">(��λ����)
					</TD>
					<%
						if("ah_dx".equals(InstArea) || "jx_dx".equals(InstArea)){
					%>
					<TD class=column align="right">�û���¼IP</TD>
				    <TD>
						<INPUT TYPE="text" NAME="user_ips" class="bk">(�Զ��Ÿ�����������д���λ�IP)
					</TD>
					<%
						}else{
					%>
				    <TD class=column align="right" rowspan=2>�û���¼IP</TD>
				    <TD rowspan=2>
						<INPUT TYPE="text" NAME="user_ips" class="bk"><br>(�Զ��Ÿ�����������д���λ�IP)
					</TD>
					<%
						}
					%>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">��������</TD>
					<TD>
						<INPUT TYPE="text" NAME="area_name" id="area_name" value="" readOnly class="bk"  onclick=areaSelect()>&nbsp;<font color="#FF0000">*</font>
						<INPUT TYPE="hidden" NAME="area_id" id="area_id" value="-1"><nobr class="BT" onmouseover="this.className='BTOver'" onmouseout="this.className='BT'" onclick="areaSelect();"><IMG SRC="images/search.gif" WIDTH="15" HEIGHT="12" BORDER="0" ALT="���ҽ�ɫ" valign="middle">&nbsp;ѡ��</nobr>
					</TD>
					<%if("ah_dx".equals(InstArea) || "jx_dx".equals(InstArea)){%>
					<TD class=column align="right">�Ƿ�Ψһ��¼</TD>
					<TD>
						<INPUT TYPE="radio" NAME="isUnique" value="1" checked>&nbsp;��
						<INPUT TYPE="radio" NAME="isUnique" value="0">&nbsp;��
					</TD>
					<%}%>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">������ɫ</TD>
					<TD>
						<INPUT TYPE="text" NAME="gr_name" maxlength=200 class=bk size=20 readonly><INPUT TYPE="hidden" NAME="gr_oid">&nbsp;<font color="#FF0000">*</font>
						<nobr class="BT" onmouseover="this.className='BTOver'" onmouseout="this.className='BT'" onclick="SelectRole();"><IMG SRC="images/search.gif" WIDTH="15" HEIGHT="12" BORDER="0" ALT="���ҽ�ɫ" valign="middle">&nbsp;����</nobr>
					</TD>
					<TD class=column align="right">�����û���</TD>
					<TD>
						<INPUT TYPE="text" NAME="gg_name" id="gg_name" maxlength=200 class=bk size=20 readonly>
						<INPUT TYPE="hidden" NAME="gg_oid" id="gg_oid">&nbsp;<font color="#FF0000">*</font>
						<nobr class="BT" onmouseover="this.className='BTOver'" onmouseout="this.className='BT'" onclick="SelectGroup();"><IMG SRC="images/search.gif" WIDTH="15" HEIGHT="12" BORDER="0" ALT="�����û���" valign="middle">&nbsp;����</nobr>
					</TD>
				</TR>
				<TR class="green_title" onclick="EC('Eaddresses',this)">
					<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
						<TR><TD>
							<IMG SRC="images/onlineaddress.gif" WIDTH="15" HEIGHT="12" BORDER="0" ALT="">&nbsp;��ϵ��ַ 
						</TD>
						<TD align="right">
							<IMG SRC="images/up_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="" >&nbsp;
						</TD>
						</TR>
						</TABLE>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" class="Eaddresses" style="display:">
					<TD class=column align="right">E-Mail��ַ</TD>
					<TD>
						<INPUT TYPE="text" NAME="per_email" class="bk">
					</TD>
					<TD class=column align="right">���ʼ�ʱ�ƺ�</TD>
					<TD>
						<INPUT TYPE="text" NAME="per_title" class="bk" onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"  onpaste="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"oncontextmenu ="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')" "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" class="Eaddresses" style="display:">
					<TD class=column align="right">��������</TD>
					<TD>
						<%
						if (LipossGlobals.inArea("ah_dx")) {
						%>
							<select name='per_dep_oid' class=bk>
							<option value='0'>==��ѡ��==</option>
							<%
							String depsql = "select depart_id,depart_name from tab_department";
							Cursor cursor = DataSetBean.getCursor(depsql);
							Map depfields = cursor.getNext();
							while (depfields != null) {
								%>
									<option value='<%=depfields.get("depart_id")%>'><%=depfields.get("depart_name")%></option>
								<%
								depfields = cursor.getNext();
							}
							%>
							</select>&nbsp;<font color="#FF0000">*</font>
							<%
						}
						else {
						%>
							<SCRIPT LANGUAGE="JavaScript">
							<!--
								c_select("per_dep_oid","DEPARTMENT",true);
							//-->
							</SCRIPT>
						<%}%>
					</TD>
					<%if("cq_dx".equals(InstArea)){%>
					<TD class=column align="right">�ֹ�˾</TD>
					<TD>
						<INPUT TYPE="text" NAME="per_jobtitle" class="bk">
					</TD>
				</TR>
				<%}else{ %>
				<TD class=column align="right">ְ��</TD>
					<TD>
						<INPUT TYPE="text" NAME="per_jobtitle" class="bk" onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"  onpaste="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"oncontextmenu ="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')" "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
					</TD>
					</TR>
					<%} %>
				</TR>
				<TR class="yellow_title" onclick="EC('Phone',this);">
					<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
						<TR><TD>
							<IMG SRC="images/phone.gif" WIDTH="15" HEIGHT="12" BORDER="0" ALT="">&nbsp;�绰���� 
						</TD>
						<TD align="right">
							<IMG SRC="images/down_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;
						</TD>
						</TR>
						</TABLE>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" class="Phone" style="display:none">
					<TD class=column align="right">�ֻ�</TD>
					<TD colspan = "3">
						<INPUT TYPE="text" NAME="per_mobile" class="bk"  onkeyup="value=value.replace(/[^0-9]/g,'')"onpaste="value=value.replace(/[^0-9]/g,'')" oncontextmenu ="value=value.replace(/[^0-9]/g,'')">&nbsp;<font color="#FF0000">*</font>
					</TD>
				</TR>
				<TR class="yellow_title" onclick="EC('Personal',this);">
					<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
						<TR><TD>
							<IMG SRC="images/persoinfo.gif" WIDTH="15" HEIGHT="12" BORDER="0" ALT="">&nbsp;���� 
						</TD>
						<TD align="right">
							<IMG SRC="images/down_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;
						</TD>
						</TR>
						</TABLE>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" class="Personal" style="display:none">
					<TD class=column align="right">������</TD>
					<TD>
						<INPUT TYPE="text" NAME="per_lastname" class="bk" onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"  onpaste="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"oncontextmenu ="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')" "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
					</TD>
					<TD class=column align="right">�Ա�</TD>
					<TD>
						<INPUT TYPE="radio" NAME="tmpper_gender" value="��" checked>&nbsp;��
						<INPUT TYPE="radio" NAME="tmpper_gender" value="Ů">&nbsp;Ů
						<INPUT TYPE="hidden" NAME="per_gender">
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" class="Personal" style="display:none">
					<TD class=column align="right">���� </TD>
					<TD colspan="3">
						<input type="text" name="year" value="" size="5" maxlength="4"  class="bk" > �꣨���磬1978��
						<select name="month"  class="bk" >
						<option value="0">�·�
						<option value="1">1 ��
						<option value="2">2 ��
						<option value="3">3 ��
						<option value="4">4 ��
						<option value="5">5 ��
						<option value="6">6 ��
						<option value="7">7 ��
						<option value="8">8 ��
						<option value="9">9 ��
						<option value="10">10 ��
						<option value="11">11 ��
						<option value="12">12 ��
						</select><nobr>
						<select name="day"  class="bk">
						<option value="0">����
						<option value="1">1 ��
						<option value="2">2 ��
						<option value="3">3 ��
						<option value="4">4 ��
						<option value="5">5 ��
						<option value="6">6 ��
						<option value="7">7 ��
						<option value="8">8 ��
						<option value="9">9 ��
						<option value="10">10 ��
						<option value="11">11 ��
						<option value="12">12 ��
						<option value="13">13 ��
						<option value="14">14 ��
						<option value="15">15 ��
						<option value="16">16 ��
						<option value="17">17 ��
						<option value="18">18 ��
						<option value="19">19 ��
						<option value="20">20 ��
						<option value="21">21 ��
						<option value="22">22 ��
						<option value="23">23 ��
						<option value="24">24 ��
						<option value="25">25 ��
						<option value="26">26 ��
						<option value="27">27 ��
						<option value="28">28 ��
						<option value="29">29 ��
						<option value="30">30 ��
						<option value="31">31 ��
						</select>
						<INPUT TYPE="hidden" NAME="per_birthdate">
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" class="Personal" style="display:none">
					<TD class=column align="right">���֤��</TD>
					<TD>
						<INPUT TYPE="text" NAME="per_searchcode" class="bk">&nbsp;
						<%if(!"nx_lt".equals(InstArea)){ %>
						    <font color="#FF0000">*</font>
						<%} %>
					</TD>
					<TD class=column align="right" >����</TD>
					<TD><INPUT TYPE="text" NAME="per_remark" maxlength=100 class=bk size=20 onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"  onpaste="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"oncontextmenu ="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')" "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"></TD>
				</TR>
				<TR height="23">
					<TD colspan="4" align="right" class=green_foot>
						<INPUT TYPE="submit" value=" �� �� " >&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="action" value="add">
						<INPUT TYPE="reset" value=" �� д " class=btn>
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
		<TR height="23" nowrap><TD></TD></TR>
	</TABLE>
	
	</TD>
</TR>
</TABLE>
</FORM>

<script>
function checkIPValue(IPValue)
{
  var IPs = IPValue.split(",");
  var length = IPs.length;
  for(var i=0;i<length;i++)
  {
     if(!isIP(IPs[i]))
     {
       return false;
     }
  }
  
  return true;  
}

function isIP(strValue){
   if(Trim(strValue).length>0)
   {
        var length ;
        var IPs = strValue.split(".");
        length = IPs.length;
        if(length>4||length<3)
        {
           return false;
        }
        for(var i=0;i<length;i++)
        {           
           if(i<3&&!chkIP(IPs[i]))
           {
            return false;
           }
           
           if(length==4&&Trim(IPs[3]).length>0&&!chkIP(IPs[3]))
           {
             return false;
           }           
        }
        
        return true;
   }
   else
   {
      return false;
   }
}


function chkIP(strIP)
{
	if(!IsNumber1(strIP)) return false;
	if(parseInt(strIP)>255){		
		return false;
	}
	return true;
}


function IsNumber1(strValue){

	var bz = true;

	if(Trim(strValue).length>0){

		for(var i=0;i<strValue.length;i++){

			var ch=strValue.substring(i,i+1);

			if(ch<'0'||ch>'9'){

				alert(strMsg+'????????')

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


function EC(leaf,obj){
	pobj = obj.offsetParent;
	//oTRs = pobj.getElementsByTagName("TR");
	oTRs = $("TR");
	var m_bShow; 
	for(var i=0; i<oTRs.length; i++){
		var s = oTRs[i].className;
		if(oTRs[i].className == leaf){
			m_bShow = (oTRs[i].style.display=="none");
			oTRs[i].style.display = m_bShow?"":"none";
		}
	}
	sobj = obj.getElementsByTagName("IMG");
	if(m_bShow) {
		sobj[1].src = "images/up_enabled.gif";
		obj.className="green_title";
	}
	else{
		sobj[1].src = "images/down_enabled.gif";
		obj.className="yellow_title";
	}
}
var has_showModalDialog = !!window.showModalDialog;
function SelectRole(){
	var page = "RolePicker.jsp?refresh="+Math.random();
	var vReturnValue = window.showModalDialog(page,"","dialogHeight: 470px; dialogWidth: 540px; dialogTop: px; dialogLeft: px; edge: Raised; center: Yes; help: No; resizable: No; status: No;scroll:No;");
	var obj = document.frm;
	if(!has_showModalDialog) return;
	if(typeof(vReturnValue) == "object"){
		obj.gr_name.value = vReturnValue.name;
		obj.gr_oid.value  = vReturnValue.oid
	}
}

function deviceResult2(vReturnValue){
	var obj = document.frm;
	obj.gr_name.value = vReturnValue.name;
	obj.gr_oid.value  = vReturnValue.oid
  	console.log(vReturnValue);
}

function SelectGroup(){
	var page = "GroupPicker.jsp?refresh="+Math.random();
	var vReturnValue = window.showModalDialog(page,"","dialogHeight: 470px; dialogWidth: 540px; dialogTop: px; dialogLeft: px; edge: Raised; center: Yes; help: No; resizable: No; status: No;scroll:No;");
	var obj = document.frm;
	if(typeof(vReturnValue) == "object"){
		obj.gg_name.value = vReturnValue.name;
		obj.gg_oid.value  = vReturnValue.oid
	}
}

function deviceResult1(vReturnValue){
	var obj = document.frm;
	obj.gg_name.value = vReturnValue.name;
	obj.gg_oid.value  = vReturnValue.oid
  	console.log(vReturnValue);
}

function deviceResult()
{
	var s= $("input[@name='cityIds']").val();
	alert(s);
}
function areaSelect(){
	//var area_pid = document.all("area_id").value;
	var width = 360;
	var page = "./AreaSelect.jsp?area_pid=<%=user.getAreaId()%>&width="+ width;
	window.open(page,"","left=20,top=20,width="+ width +",height=450,resizable=no,scrollbars=no");	
	
}
function SelUser()
{
	var page="../webtopo/SelVpnUser.jsp";
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,width=500,height=300";
	window.open(page,"ѡ���ͻ�",otherpra);
}

function setUserProperty(customid,customname)
{
	frm.customid.value=customid;
	frm.customname.value=customname;	
}
function test()
{
	var url = "../gtms/stb/resource/cityAll.jsp";
	var	width=1200;    
	var height=1200; 
	var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');    
	$("input[@name='cityIds']").val(returnVal);
}

//20200506 ɽ����ͨ �������������û���ֻ��һ������Ĭ����д�Ǹ�
var defaultAreaId='<%=defaultAreaId%>'
var defaultAreaName='<%=defaultAreaName%>'
var defaultGroupOid='<%=defaultGroupOid%>'
var defaultGroupName='<%=defaultGroupName%>'
if(defaultAreaId != ''){
	$("#area_id").val(defaultAreaId);
}
if(defaultAreaName != ''){
	$("#area_name").val(defaultAreaName);
}
if(defaultGroupOid != ''){
	$("#gg_oid").val(defaultGroupOid);
}
if(defaultGroupName != ''){
	$("#gg_name").val(defaultGroupName);
}

</script>


<%@ include file="../foot.jsp"%>

