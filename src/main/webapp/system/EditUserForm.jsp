<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.filter.SelectCityFilter"%>
<%@ page import="com.linkage.litms.common.database.*,java.util.*"%>
<%@ page import="com.linkage.litms.common.util.*"%>
<%
	request.setCharacterEncoding("GBK");
	String InstArea = LipossGlobals.getLipossProperty("InstArea.ShortName");
	String acc_oid = "";
	if(!"nx_dx".equals(InstArea) && !"hb_lt".equals(InstArea) && !"sx_lt".equals(InstArea)){
		DesUtils des = new DesUtils();
		acc_oid = des.decrypt(request.getParameter("acc_oid"));
	}else{
		acc_oid = request.getParameter("acc_oid");
	}

	//�õ���ǰ�û���acc_oid,���ж��Ƿ�acc_oid�Ƿ����¼�û���acc_oid���
	String userAcc_Oid = String.valueOf(user.getId());
	//�˴����������⣬������acc_oidΪselect��valueֵ��1,2,3��������������acc_oid���ݲ��޸�ֻ����ʡ���ж�
	boolean flag = false;
	if ("nmg_dx".equals(InstArea))
	{
		flag = false;
	}
	else
	{
		flag = acc_oid.equals(userAcc_Oid) ? true : false;
	}
	if ("admin".equals(user.getAccount()))
	{
		flag = false;
	}
	String strSQL = "select b.acc_loginname,b.acc_password,b.loginmaxtrynum,b.lockedtime,b.maxidletimebeforelogout,b.passwordminlenth,b.acc_login_ip,b.acc_validatemonth,b.is_unique,a.* from tab_persons a,tab_accounts b where a.per_acc_oid=b.acc_oid and a.per_acc_oid="
			+ acc_oid + " order by a.per_acc_oid";
	Map fields = DataSetBean.getRecord(strSQL);
	//String strCustomSel="<TD class=column colspan=2></TD>";
	SelectCityFilter city = new SelectCityFilter(request);
	Cursor cursor = city.getAllSubCitySelf(user.getCityId());
	String strCityList = FormUtil.createListBox(cursor, "city_id", "city_name",
			false, (String) fields.get("per_city"), "per_city");
	String cityid = (String) fields.get("per_city");
	strSQL = "select area_id from tab_acc_area where acc_oid=" + acc_oid;
	Map areaMap = DataSetBean.getRecord(strSQL);
	strSQL = "select area_name from tab_area where area_id="
			+ areaMap.get("area_id");
	Map AreaNameMap = DataSetBean.getRecord(strSQL);
	Map cityMap = CommonMap.getCityMap();
%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.common.util.FormUtil"%>
<%@ page import="sun.misc.BASE64Decoder" %>
<%@ page import="java.security.SecureRandom" %>
<%@ page import="javax.crypto.spec.DESKeySpec" %>
<%@ page import="javax.crypto.SecretKeyFactory" %>
<%@ page import="javax.crypto.SecretKey" %>
<%@ page import="javax.crypto.Cipher" %>
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
var InstArea = "<%=InstArea%>";
var cityid="<%=cityid%>";
function CheckForm(){
	var obj = document.frm;
	if(!IsAccount(obj.acc_loginname.value,'��¼�˺�')){
		obj.acc_loginname.focus();
		obj.acc_loginname.select();
		return false;
	}	
	
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
	
	if(!IsNull(obj.per_name.value,'����'))
	{
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
	
	if(Trim(obj.acc_validatemonth.value)!=""&&!IsNumber(obj.acc_validatemonth.value,"������Ч��"))
	{
	    obj.acc_validatemonth.focus();
	    obj.acc_validatemonth.select();
	    return false;
	}
	
	if(Trim(obj.user_ips.value)!=""&&!checkIPValue(obj.user_ips.value,'�û���¼IP'))	
	{
	    alert("������û�IP��ʽ����ȷ")
	    obj.user_ips.focus();
	    obj.user_ips.value="";
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
	

	if(Trim(obj.per_mobile.value)!="" && !IsNumber(obj.per_mobile.value,'�ֻ�����')){
		obj.per_mobile.focus();
		obj.per_mobile.select();
		return false;
	}
	
	if(Trim(obj.per_mobile.value)!="" && obj.per_mobile.value.length!=11){
		alert("�ֻ�����ӦΪ11λ");
		obj.per_mobile.focus();
		obj.per_mobile.select();
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
	//alert(obj.gg_oid.value);
	return true;
}

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

function setRadioValue(o,v){
	if(o.length >1){
		for(var i=0;i<o.length;i++){
			if(o[i].value == v){
				o[i].checked = true;
				break;
			}
		}
	}
	else{
		if(o[i].value == v) o[i].checked = true;
	}
}

// ���Ƿ�Ψһ��¼(is_unique)����ѡ��ֵ
function setIsUniqueValue(o, v){
	if(o.length >1){
		for(var i=0;i<o.length;i++){
			if(o[i].value == v){
				o[i].checked = true;
				break;
			}
		}
	}
	else{
		if(o[i].value == v) o[i].checked = true;
	}
}

function setDateTime(o,v){
	if(v=="") return;
	else v = v.substring(0,10);

	v1 = v.substring(0,4);
	p1 = v.indexOf("-");
	p2 = v.lastIndexOf("-");
	v2 = v.substring((p1+1),p2);
	v3 = v.substring((p2+1),v.length);

	o.year.value = v1;
	o.month.options[parseInt(v2)].selected = true;
	o.day[parseInt(v3)].selected = true;
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

function initUser(){
	var obj = document.frm;
	setRadioValue(obj.tmpper_gender,obj.per_gender.value);
	setDateTime(obj,obj.per_birthdate.value);
	// ���յ���
	if("ah_dx" == InstArea || "jx_dx" == InstArea ){
		setIsUniqueValue(obj.isUnique, obj.tempis_unique.value); // ���Ƿ�Ψһ��¼(is_unique)�ֶ�����ֵ  add by zhangchy 2013-01-05
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
}
function test()
{
	var url = "../gtms/stb/resource/cityAll.jsp?cityid="+cityid+"&jktest=1";
	var	width=1200;    
	var height=1200; 
	var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');    
	$("input[@name='cityIds']").val(returnVal);
}
function SelectGroup(){
  var page = "GroupPicker.jsp?refresh="+Math.random();
  var vReturnValue = window.showModalDialog(page,"","dialogHeight: 470px; dialogWidth: 540px; dialogTop: px; dialogLeft: px; edge: Raised; center: Yes; help: No; resizable: No; status: No;scroll:No;");
  var obj = document.frm;
  if(!has_showModalDialog) return;
  if(typeof(vReturnValue) == "object"){
    obj.gg_name.value = vReturnValue.name;
    obj.gg_oid.value  = vReturnValue.oid
  }
}
function deviceResult1(vReturnValue){
  var obj = document.frm;
  obj.gg_name.value = vReturnValue.name;
  obj.gg_oid.value  = vReturnValue.oid
}

window.onload=initUser;
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<BR>
<FORM NAME="frm" METHOD="post" ACTION="UserSave.jsp" onsubmit="return CheckForm()">
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="98%" border=0 align="center">
<input type="hidden" name="cityIds" id="cityIds" value="<%=fields.get("per_city")%>"/>
<TR>
	<TD>
		<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">
					ϵͳ����Ա����
				</td>
				<td>
					<img src="../images/attention_2.gif" width="15" height="12">
					&nbsp;&nbsp;ϵͳ����Ա�༭,��&nbsp;<font color="#FF0000">*</font>&nbsp;����ѡ�����д
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
				<TR>
					<TH  colspan="4" align="center"><B>�༭��<%=fields.get("per_name")%>������Ա</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width='15%'>��¼�˺�</TD>
					<TD  width='35%'>
						<INPUT TYPE="text" NAME="acc_loginname" class="bk" value="<%=fields.get("acc_loginname")%>" readonly>&nbsp;<font color="#FF0000">*</font>
					</TD>
					<TD class=column align="right" width='15%'>����</TD>
					<TD  width='35%'>
					<%
						if (!flag)
						{
					%>
<%-- 						<INPUT TYPE="text" NAME="per_name" class="bk" value="<%=fields.get("per_name")%>">&nbsp;<font color="#FF0000">*</font> --%>
						<INPUT TYPE="text" NAME="per_name" class="bk" value="<%=fields.get("per_name")%>" onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"  onpaste="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"oncontextmenu ="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')" "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">&nbsp;<font color="#FF0000" >*��֧�����ġ�Ӣ�ġ�����</font>
					<%
						}
						else
						{
					%>
						<INPUT TYPE="text" NAME="per_name" class="bk" value="<%=fields.get("per_name")%>" onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"  onpaste="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"oncontextmenu ="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')" "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" readonly>
					<%
						}
					%>
					</TD>														
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">��¼ʧ�ܴ���</TD>
					<TD>
					<%
						if (!flag)
						{
					%>
						<INPUT TYPE="text" NAME="login_error" class="bk" value="<%=fields.get("loginmaxtrynum")%>">
					<%
						}
						else
						{
					%>
						<INPUT TYPE="text" NAME="login_error" class="bk" value="<%=fields.get("loginmaxtrynum")%>" readonly>
					<%
						}
					%>
					</TD>
					<TD class=column align="right">��¼ʧ������ʱ��</TD>
					<TD>
					<%
						if (!flag)
						{
					%>
						<INPUT TYPE="text" NAME="lock_time" class="bk" value="<%=fields.get("lockedtime")%>">
					<%
						}
						else
						{
					%>
						<INPUT TYPE="text" NAME="lock_time" class="bk" value="<%=fields.get("lockedtime")%>" readonly>
					<%
						}
					%>(��λ����)
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">��¼�ɿ���ʱ��</TD>
					<TD>
					<%
						if (!flag)
						{
					%>
						<INPUT TYPE="text" NAME="login_free" class="bk" value="<%=fields.get("maxidletimebeforelogout")%>">
					<%
						}
						else
						{
					%>
						<INPUT TYPE="text" NAME="login_free" class="bk" value="<%=fields.get("maxidletimebeforelogout")%>" readonly>
					<%
						}
					%>(��λ����)
					</TD>
					<TD class=column align="right">��С���볤��</TD>
					<TD>
					<%
						if (!flag)
						{
					%>
						<INPUT TYPE="text" NAME="min_passwdlength" class="bk" value="<%=fields.get("passwordminlenth")%>">
					<%
						}
						else
						{
					%>
						<INPUT TYPE="text" NAME="min_passwdlength" class="bk" value="<%=fields.get("passwordminlenth")%>" readonly>
					<%
						}
					%>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
				    <TD class=column align="right">������Ч��</TD>
				    <TD>
				    <%
				    	if (!flag)
				    	{
				    %>
					<INPUT TYPE="text" NAME="acc_validatemonth" class="bk" value="<%=fields.get("acc_validatemonth")%>">(��λ����)
					<%
						}
						else
						{
					%>
					<INPUT TYPE="text" NAME="acc_validatemonth" class="bk" value="<%=fields.get("acc_validatemonth")%>" readonly>(��λ����)
					<%
						}
					%>
					</TD>
				    <TD class=column align="right" >�û���¼IP</TD>
				    <TD  >
				    <%
				    	if (!flag)
				    	{
				    %>
						<INPUT TYPE="text" NAME="user_ips" class="bk" value="<%=fields.get("acc_login_ip")%>">&nbsp;(�Զ��Ÿ�����������д���λ�IP)
					<%
							}
							else
							{
						%>
						<INPUT TYPE="text" NAME="user_ips" class="bk" value="<%=fields.get("acc_login_ip")%>" readonly>&nbsp;(�Զ��Ÿ�����������д���λ�IP)
					<%
							}
						%>
					</TD>
				</TR>						
				<TR bgcolor="#FFFFFF">	
				 <%
					 	if ("cq_dx".equals(InstArea))
					 	{
					 %>				
					<TD style="cursor:pointer" class=column align="right" onclick="test()"><u>����</u></TD>
					<TD class=column align="right" ></TD>
					<%
						}
						else
						{
					%>
					<TD class=column align="right">����</TD>
					<TD>					
					<%
											if (!flag)
												{
										%>
						<%=strCityList%>&nbsp;&nbsp;				
					<%
										}
											else
											{
									%>
						<INPUT TYPE="text" NAME="city_name" class="bk" value="<%=cityMap.get(fields.get("per_city"))%>" readonly>&nbsp;
						<INPUT TYPE="hidden" NAME="per_city" class="bk" value="<%=fields.get("per_city")%>">
					<%
						}
					%>
					</TD>
					<%
						}
					%> 
					<TD class=column align="right">������</TD>
					<TD>
					<%
						if (!flag)
						{
					%>
						<INPUT TYPE="text" NAME="area_name" value="<%=((AreaNameMap != null) ? (AreaNameMap.get("area_name")) : "")%>" class="bk" style="cursor:hand" onclick=areaSelect()><font color="#FF0000">*</font>
					<%
						}
						else
						{
					%>
						<INPUT TYPE="text" NAME="area_name" value="<%=((AreaNameMap != null) ? (AreaNameMap.get("area_name")) : "")%>" class="bk" readonly><font color="#FF0000">*</font>
					<%
						}
					%>
						<INPUT TYPE="hidden" NAME="area_id" value=<%=areaMap.get("area_id")%> class="bk">						
					</TD>
				</TR>					
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">������ɫ</TD>
					<TD>
					<%
						strSQL = "select role_id,role_name from tab_role where role_id in (select role_id from tab_acc_role where acc_oid="
								+ fields.get("per_acc_oid") + ")";
						cursor = DataSetBean.getCursor(strSQL);
						Map fields2 = cursor.getNext();
						String r_names = null , r_oids = null;
						while (fields2 != null)
						{
							if (r_names == null)
							{
								r_names = (String) fields2.get("role_name");
								r_oids = (String) fields2.get("role_id");
							}
							else
							{
								r_names += "," + (String) fields2.get("role_name");
								r_oids += "," + (String) fields2.get("role_id");
							}
							fields2 = cursor.getNext();
						}
						r_names = (r_names == null) ? "" : r_names;
						r_oids = (r_oids == null) ? "" : r_oids;
					%>
						<INPUT TYPE="text" NAME="gr_name" maxlength=200 class=bk size=20 readonly value="<%=r_names%>"><INPUT TYPE="hidden" NAME="gr_oid" value="<%=r_oids%>">&nbsp;<font color="#FF0000">*</font>
					<%
						if (!flag)
						{
					%>
						<nobr class="BT" onmouseover="this.className='BTOver'" onmouseout="this.className='BT'" onclick="SelectRole();"><IMG SRC="images/search.gif" WIDTH="15" HEIGHT="12" BORDER="0" ALT="���ҽ�ɫ" valign="middle">&nbsp;����</nobr>
					<%
						}
					%>
					</TD>
					<TD class=column align="right">�����û���</TD>
					<TD>
					<%
						strSQL = "select group_oid,group_name from tab_group where group_oid in (select group_oid from tab_acc_group where acc_oid="
								+ fields.get("per_acc_oid") + ")";
						cursor = DataSetBean.getCursor(strSQL);
						fields2 = cursor.getNext();
						String g_names = null , g_oids = null;
						while (fields2 != null)
						{
							if (g_names == null)
							{
								g_names = (String) fields2.get("group_name");
								g_oids = (String) fields2.get("group_oid");
							}
							else
							{
								g_names += "," + (String) fields2.get("group_name");
								g_oids += "," + (String) fields2.get("group_oid");
							}
							fields2 = cursor.getNext();
						}
						g_names = (g_names == null) ? "" : g_names;
						g_oids = (g_oids == null) ? "" : g_oids;
					%>
						<INPUT TYPE="text" NAME="gg_name" maxlength=200 class=bk size=20 readonly value="<%=g_names%>"><INPUT TYPE="hidden" NAME="gg_oid" value="<%=g_oids%>">&nbsp;<font color="#FF0000">*</font>
					<%
						if (!flag)
						{
					%>
						<nobr class="BT" onmouseover="this.className='BTOver'" onmouseout="this.className='BT'" onclick="SelectGroup();"><IMG SRC="images/search.gif" WIDTH="15" HEIGHT="12" BORDER="0" ALT="�����û���" valign="middle">&nbsp;����</nobr>
					<%
						}
					%>
					</TD>
				</TR>
			<%-- 	<%if("cq_dx".equals(InstArea)){%>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">�ֹ�˾</TD>
					<TD>
						<INPUT TYPE="hidden" NAME="per_jobtitle" value="<%=fields.get("per_jobtitle")%>">
					</TD>
					<TD class=column align="right"><br></TD>
					<TD><br></TD>
				</TR>
				<%} %> --%>
				<%
					if ("ah_dx".equals(InstArea) || "jx_dx".equals(InstArea))
					{
				%>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">�Ƿ�Ψһ��¼</TD>
					<TD>
						<INPUT TYPE="radio" NAME="isUnique" value="1">&nbsp;��
						<INPUT TYPE="radio" NAME="isUnique" value="0">&nbsp;��
						<INPUT TYPE="hidden" NAME="tempis_unique" value="<%=fields.get("is_unique")%>">
					</TD>
					<TD class=column align="right"><br></TD>
					<TD><br></TD>
				</TR>
				<%
					}
				%>
				<TR style="cursor:hand;background-color:#A0C6E5" onclick="EC('Eaddresses',this);">
					<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
						<TR><TD>
							<IMG SRC="images/onlineaddress.gif" WIDTH="15" HEIGHT="12" BORDER="0" ALT="">&nbsp;������ַ 
						</TD>
						<TD  align="right" >
							<IMG SRC="images/up_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="" >&nbsp;
						</TD>
						</TR>
						</TABLE>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" leaf="Eaddresses" style="display:">
					<TD class=column align="right">E-Mail��ַ</TD>
					<TD>
						<INPUT TYPE="text" NAME="per_email" class="bk" value="<%=fields.get("per_email")%>">
					</TD>
					<TD class=column align="right">���ʼ�ʱ�ƺ�</TD>
					<TD>
						<INPUT TYPE="text" NAME="per_title" class="bk" onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"  onpaste="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"oncontextmenu ="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')" "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" value="<%=fields.get("per_title")%>">
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" leaf="Eaddresses" style="display:">
					<TD class=column align="right">��������</TD>
					<TD>
						<%
							String tmp = (String) fields.get("per_dep_oid");
							String t = tmp.equals("") ? "0" : tmp;
							if (LipossGlobals.inArea("ah_dx")) {
								%>
								<select name='per_dep_oid' class=bk>
								<option value='0'>==��ѡ��==</option>
								<%
								String depsql = "select depart_id,depart_name from tab_department";
								cursor = DataSetBean.getCursor(depsql);
								Map depfields = cursor.getNext();
								while (depfields != null) {
									if (fields.get("per_dep_oid") != null && 
											fields.get("per_dep_oid").equals(depfields.get("depart_id"))) {
										%>
										<option value='<%=depfields.get("depart_id")%>' selected><%=depfields.get("depart_name")%></option>
										<%
									}
									else {%>
										<option value='<%=depfields.get("depart_id")%>'><%=depfields.get("depart_name")%></option>
									<%}
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
							c_select("per_dep_oid","DEPARTMENT",true,<%=t%>);
						//-->
						</SCRIPT>
						<% }%>
					</TD>
					 <%
					 	if (!"cq_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")))
					 	{
					 %>
					<TD class=column align="right">ְ��</TD>
					<%
						}
						else
						{
					%>
						<TD class=column align="right">�ֹ�˾</TD>
					<%
						}
					%>
					<TD>
						<INPUT TYPE="text" NAME="per_jobtitle" class="bk" onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"  onpaste="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"oncontextmenu ="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')" "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" value="<%=fields.get("per_jobtitle")%>">
					</TD>
				</TR>
				<TR style="cursor:hand;background-color:#cccccc" onclick="EC('Phone',this);">
					<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
						<TR><TD>
							<IMG SRC="images/phone.gif" WIDTH="15" HEIGHT="12" BORDER="0" ALT="">&nbsp;�绰���� 
						</TD>
						<TD  align="right" >
							<IMG SRC="images/down_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;
						</TD>
						</TR>
						</TABLE>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" class="Phone" style="display:none">
					<TD class=column align="right">�ֻ�</TD>
					<TD colspan="3">
						<INPUT TYPE="text" NAME="per_mobile" class="bk" value="<%=fields.get("per_mobile")%>"  
						onkeyup="value=value.replace(/[^0-9]/g,'')"onpaste="value=value.replace(/[^0-9]/g,'')" oncontextmenu ="value=value.replace(/[^0-9]/g,'')">&nbsp;<font color="#FF0000">*</font>
					</TD>
				</TR>
				<TR style="cursor:hand;background-color:#cccccc" onclick="EC('Personal',this);">
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
						<INPUT TYPE="text" NAME="per_lastname" class="bk" onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"  onpaste="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"oncontextmenu ="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')" "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" value="<%=fields.get("per_lastname")%>">
					</TD>
					<TD class=column align="right">�Ա�</TD>
					<TD>
						<INPUT TYPE="radio" NAME="tmpper_gender" value="��">&nbsp;��
						<INPUT TYPE="radio" NAME="tmpper_gender" value="Ů">&nbsp;Ů
						<INPUT TYPE="hidden" NAME="per_gender" value="<%=fields.get("per_gender")%>">
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" class="Personal" style="display:none">
					<TD class=column align="right">���� </TD>
					<TD colspan="3">
						<input type="text" name="year" value="" size="5" maxlength="4"> �꣨���磬1978��
						<select name="month" >
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
						<select name="day" >
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
						<INPUT TYPE="hidden" NAME="per_birthdate" value="<%=fields.get("per_birthdate")%>">
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" class="Personal" style="display:none">
					<TD class=column align="right">���֤��</TD>
					<TD>
						<INPUT TYPE="text" NAME="per_searchcode" class="bk" value="<%=fields.get("per_searchcode")%>">&nbsp;
						<%if(!"nx_lt".equals(InstArea)){ %>
						   <font color="#FF0000">*</font>
						<%} %>
					</TD>
					<TD class=column align="right" >����</TD>
					<TD><INPUT TYPE="text" NAME="per_remark" maxlength=100 class=bk size=20 onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"  onpaste="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')"oncontextmenu ="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')" "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" value="<%=fields.get("per_remark")%>"></TD>
				</TR>
				<TR height="23">
					<TD colspan="4" align="right" class=foot>
						<INPUT TYPE="submit" value=" �� �� " class=btn>&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="action" value="update">
						<INPUT TYPE="hidden" NAME="acc_oid" value="<%=acc_oid%>">
						<INPUT TYPE="reset" value=" �� д " class=btn>
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
		<TR height="23" nowrap><TD></TD></TR>

	</TABLE>
	</FORM>
	</TD>
</TR>
</TABLE>
<script>
function areaSelect(){
	//var area_pid = document.all("area_id").value;
	var page = "./AreaSelect.jsp?area_pid=<%=user.getAreaId()%>";
	window.open(page,"","left=20,top=20,width=360,height=450,resizable=no,scrollbars=no");	
	
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

</script>
<%@ include file="../foot.jsp"%>

