<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.ArrayList,com.linkage.litms.common.database.*"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<jsp:useBean id="AdslAct" scope="request" class="com.linkage.litms.resource.AdslAct"/>

<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>

<%
request.setCharacterEncoding("GBK");
String strData = "";
Map city_Map = CityDAO.getCityIdCityNameMap();
ArrayList list = new ArrayList();
list.clear();
list = AdslAct.getAdslUserInfoList(request);
String 	strCityList = DeviceAct.getCityListSelf(false, "", "", request);
String strBar = String.valueOf(list.get(0));
Cursor cursor = (Cursor)list.get(1);
Map fields = cursor.getNext();

Map dev_idToIp = com.linkage.litms.common.util.CommonMap.getSnmpDeviceIPMap();
if(fields == null){
	strData = "<TR><TD class=column COLSPAN=6 HEIGHT=30>��ϵͳû���û���Դ</TD></TR>";
}
else{
	int i=1;
	String city_id = null;
	String city_name = null;
	while(fields != null){
	    String tempIP = (String)dev_idToIp.get(fields.get("device_id"));
	    city_id = (String)fields.get("city_id");
		city_name = (String)city_Map.get(city_id);
		city_name = city_name == null ? "&nbsp;" : city_name;
		strData += "<TR>";
		strData += "<TD class=column1>"+ (String)fields.get("username") + "</TD>";
		strData += "<TD class=column1>"+ (tempIP==null?"":tempIP) + "</TD>";
		strData += "<TD class=column2>"+ (String)fields.get("linkphone") + "</TD>";
		strData += "<TD class=column2>"+ city_name + "</TD>";
		strData += "<TD class=column2>"+ (String)fields.get("realname") + "</TD>";
		strData += "<TD class=column2 align='center'><A HREF=UpdateRadiusCustomerForm.jsp?user_id="+ (String)fields.get("user_id") +"&username="+(String)fields.get("username")+">�༭</A> | <A HREF=RadiusCustomerSave.jsp?action=delete&user_id="+ (String)fields.get("user_id") +"&username="+(String)fields.get("username")+" onclick='return delWarn();'>ɾ��</A></TD>";
		strData += "</TR>";
		i++;
		fields = cursor.getNext();
	}
	strData += "<TR><TD class=column COLSPAN=6 align=right>" + strBar + "</TD></TR>";
}

fields = null;
cursor = null;

list.clear();
list = null;

%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript">
<!--
function go(){
	v = document.all("txtpage").value;
	if(parseInt(v) && parseInt(v)>0){
		this.location = "RadiusCustomerList.jsp?offset="+ ((eval(v)-1)*15+1);
	}
}

function delWarn(){
	if(confirm("���Ҫɾ�����û���Դ��\n��������ɾ���Ĳ��ָܻ�������")){
		return true;
	}
	else{
		return false;
	}
}

function GoContent(device_id){
	var strpage="DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

function QueryByUserName(){
       var name=document.all.username.value;
       var cit=document.all.city_id.value;
      // alert(cit);
       this.location = "RadiusCustomerList.jsp?username="+name+"&city_id="+cit+"";
}
 

function CheckFormForm(){
	var adslflag,accountflag,dslamipflag,user_stateflag;
//alert(KillSpace(document.frm.account.value).length);
	if(KillSpace(document.frm.account.value).length != 0){
		accountflag = true;
	}

	if(KillSpace(document.frm.adslphone.value).length != 0){
		adslflag = true;
	}
	
	if(KillSpace(document.frm.dslamip.value).length != 0){
		dslamipflag = true;
	}	
	
	if(document.frm.user_state.value != "-1"){
		user_stateflag = true
	}
	
	if(adslflag || accountflag || dslamipflag||user_stateflag){
		return true;
	}else{
		alert("�������ѯ����");
		document.frm.account.focus();
		document.frm.account.select();
		return false;		
	}
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
   <FORM NAME="frm" METHOD="post" ACTION="RadiusCustomerList.jsp" onSubmit="return CheckFormForm()">
  </FORM>
 
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
				<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="162" align="center" class="title_bigwhite">�û���Դ</TD>
						<td>
						�ʺţ�<input type="text" name="username" class=bk value="">
						���أ�<%=strCityList%>
						<input type="button" name="query" onClick="QueryByUserName()" value="��ѯ">
						<td>
						<TD align="right">
								<a href="AddRadiusCustomerForm.jsp">����ADSL�û�</a>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD bgcolor=#000000>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
						<TR>
							<TH bgcolor="#ffffff" colspan="6" >
								ADSL�ͻ���Դ�б�
							</TH>
						</TR>
						<TR> 
							<TH>�û��˺�</TH>
							<TH>�豸����</TH>
							<TH>��ϵ�绰</TH>
							<TH>����</TH>
							<TH>�û�ʵ��</TH>
							<TH width=150>����</TH>
						</TR>
						<%=strData%>
					</TABLE>
				</TD>
			</TR>
		</TABLE>

</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>

<script language="javascript">

//show
//show == 0		����
//show == 1		��ʾ
var show = 0;

function showSearch(){
	if(show == 0){
		show = 1;
		document.all("Button").value = "�����û�������";
		document.all("searchLayer").style.display="";
	}else{
		show = 0;
		document.all("Button").value = "�����ض��û�";
		document.all("searchLayer").style.display="none";
	}
}

document.onload = showSearch;

</SCRIPT>