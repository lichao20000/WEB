<%@ include file="../timelater.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=GBK"%>
<%--
	zhaixf(3412) 2008-04-22
	JSDX_ITMS-BUG-YHJ-20080421-001
--%>
<%--
	zhaixf(3412) 2008-05-12
	req:NJLC_HG-BUG-ZHOUMF-20080508-001
--%>
<jsp:useBean id="HGWUserInfoAct" scope="request" class="com.linkage.litms.resource.HGWUserInfoAct"/>
<jsp:useBean id="EGWUserInfoAct" scope="request" class="com.linkage.litms.resource.EGWUserInfoAct"/>
<%@ page import="java.util.ArrayList,com.linkage.litms.common.database.*,com.linkage.litms.*,com.linkage.litms.resource.DeviceAct" %> 
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<SCRIPT LANGUAGE="JavaScript">
<!--
var flag = true;
//-->
</SCRIPT>
<%
request.setCharacterEncoding("GBK");
		
ArrayList list = new ArrayList();
list.clear();
//������еķ�����Ϣ
//Cursor cursor_service = HGWUserInfoAct.getServiceInfo();
//Map fields_service = cursor_service.getNext();
//ȡ���豸����
String strVendorList = HGWUserInfoAct.getVendorList(false,"","vendorName");
//ȡ���豸����
String strDevModelList = HGWUserInfoAct.getDevModelList(false,"","devModelName");
//ȡ���豸�汾
String strOsVersionList = HGWUserInfoAct.getOsVersionList(false,"","osVersionName");

//ȡ���û���
String strDomain = HGWUserInfoAct.getUserDomains();

//�����û��б�
list = EGWUserInfoAct.getEGWUsersCursor(request);


//flg
String flg = request.getParameter("flg");
//opt���������б��ǲ�������edit�� or ��view��
String opt = request.getParameter("opt");

String strData = "";
String rtnMsg = "";
String strBar = String.valueOf(list.get(0));
Cursor cursor = (Cursor)list.get(1);
if (list.size()==3 && list.get(2) != null) {
	rtnMsg = (String)list.get(2);
}

Map fields = cursor.getNext();
if(fields == null){
	strData = "<TR><TD class=column COLSPAN=8 HEIGHT=30>û�м�������ҵ�����û�!</TD></TR>";
}
else{

	String bindType = "";
	String cust_type = "";
	Calendar time=Calendar.getInstance(); 
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	String formatTime = "";
    while(fields != null){
		cust_type = "";
		formatTime = "";
		if ("0".equals((String)fields.get("cust_type_id"))) {
			cust_type = "��˾�ͻ�";
		} else if ("1".equals((String)fields.get("cust_type_id"))) {
			cust_type = "���ɿͻ�";
		} else if ("2".equals((String)fields.get("cust_type_id"))) {
			cust_type = "���˿ͻ�";
		} else{
			cust_type = "-";
		}
		
		if( fields.get("device_id") == null || fields.get("device_id").equals(""))
			bindType = "��";
		else
			bindType = "��";

		if(fields.get("dealdate") != null && !(fields.get("dealdate").equals(""))
				&& !fields.get("dealdate").equals("0")){
			time.setTimeInMillis((Long.parseLong((String)fields.get("dealdate")))*1000);
			formatTime = df.format(time.getTime());
		}else{
			formatTime = "-";
		}
		
		String user_state = (String)fields.get("user_state");

		String username = (String)fields.get("username");
		if(username == null || "".equals(username))
			username = "-";
		
		String customer_name = (String)fields.get("customer_name");
		if(customer_name == null || "".equals(customer_name))
			customer_name = "";
		// �ڴ����ݿ��� ͳ�� ҵ�����ض�Ӧ������
		DeviceAct devAct = new DeviceAct();
		// ����MAP ��ǰ�û�������������������
		HashMap cityMap = devAct.getCityMap_All();
		
		String cityId = (String)fields.get("city_id");
		String cityName = (String)cityMap.get(cityId);
		if(cityName == null || "".equals(cityName))
			cityName = "-";

		String phonenumber = (String)fields.get("phonenumber");
		if(phonenumber == null || "".equals(phonenumber))
			phonenumber = "-";
		
		if(fields.get("device_serialnumber") == null || fields.get("device_serialnumber").equals(""))
			bindType = "��";
		else
			bindType = "��";
				
	    strData += "<TR>";
		strData += "<TD class=column1 ><input type='checkbox' name='isCheckedToDel' value='"+(String)fields.get("user_id")+"|"+(String)fields.get("gather_id")+"'></TD>";
		strData += "<TD class=column1 align='left'>"+ username + "</TD>";
		strData += "<TD class=column2 align='left'>"+ customer_name + "</TD>";
		strData += "<TD class=column1 align='left'>"+ cityName + "</TD>";
		strData += "<TD class=column1 align='left'>"+ bindType + "</TD>";
		strData += "<TD class=column2 align='left'>"+ formatTime + "</TD>";
		
	  if("edit".equals(opt)){

		if(user_state.equals("1")||user_state.equals("2")){
		    
			strData += "<TD class=column1 align='right'><A HREF=AddEGWUserInfoForm.jsp?user_id="+ (String)fields.get("user_id") 
					+"&gather_id="+(String)fields.get("gather_id") 
					+"&gwOptType=22"
					+">�༭</A> | <A HREF=EGWUserInfoSave.jsp?gwOptType=21&action=delete&user_id="
					+(String)fields.get("user_id")+"&gather_id="+(String)fields.get("gather_id")
					+ " onclick='return delWarn();'>ɾ��</A> | <A HREF=\"javascript:GoContent(\'"
					+(String)fields.get("user_id")+"\',\'"+(String)fields.get("gather_id")
					+"\');\" TITLE=�鿴�û�"+(String)fields.get("realname")+"�������Ϣ>"
					+ "��ϸ��Ϣ</A></TD>";
		}else if (user_state.equals("3")){
			strData += "<TD class=column1 align='right'>������ | "
					+"<A HREF=EGWUserInfoSave.jsp?gwOptType=21&action=delete&user_id="
					+(String)fields.get("user_id")+"&gather_id="+(String)fields.get("gather_id")
					+ " onclick='return delWarn();'>ɾ��</A> | <A HREF=\"javascript:GoContent(\'"
					+(String)fields.get("user_id")+"\',\'"+(String)fields.get("gather_id")
					+"\');\" TITLE=�鿴�û�"+(String)fields.get("realname")+"�������Ϣ>"
					+ "��ϸ��Ϣ</A></TD>";
		}else if (flg!=null){
			strData += "<TD class=column1 align='right'> <A HREF=EGWUserInfoSave.jsp?gwOptType=21&action=delete&user_id="
					+(String)fields.get("user_id")+"&gather_id="+(String)fields.get("gather_id")
					+ " onclick='return delWarn();'>ɾ��</A> | <A HREF=\"javascript:GoContent(\'"
					+(String)fields.get("user_id")+"\',\'"+(String)fields.get("gather_id")
					+"\');\" TITLE=�鿴�û�"+(String)fields.get("realname")+"�������Ϣ>"
					+ "��ϸ��Ϣ</A></TD>";
		}
	  }else{
			strData += "<TD class=column1 align='center'><A HREF=\"javascript:GoContent(\'"
				+(String)fields.get("user_id")+"\',\'"+(String)fields.get("gather_id")
				+"\');\" TITLE=�鿴�û�"+(String)fields.get("realname")+"�������Ϣ>"
				+ "��ϸ��Ϣ</A></TD>";
		}
		strData += "</TR>";
		fields = cursor.getNext();
	}
	strData += "<TR><TD class=column COLSPAN=8 align=right>" + strBar + "</TD></TR>";
}

fields = null;
list.clear();
list = null;
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var strVendorList = "<%= strVendorList%>";
var strDomain = "<%= strDomain%>";
var strDevModelList = "<%= strDevModelList%>";
var strOsVersionList = "<%= strOsVersionList%>";
var opt = "<%=opt%>";
//-->
</SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
<!--
function go(){
	v = document.all("txtpage").value;
	if(parseInt(v) && parseInt(v)>0){
		this.location = "EGWUserInfoList.jsp?offset="+ ((eval(v)-1)*15+1)
							+ "&opt="+<%=opt%>;
	}
}
//ɾ��ʱ����
function delWarn(){
	if(confirm("ȷ��Ҫɾ�����û����û�����Ӧ���豸��\n��������ɾ���Ĳ��ָܻ�������")){
		return true;
	}
	else{
		return false;
	}
}

var show = 0;
//�����ض���������
function showSearch(){
	if(show == 0){
		show = 1;
		document.all("Button_1").value = "���ؼ���";
		document.all("searchLayer").style.display="";
	}else{
		show = 0;
		document.all("Button_1").value = "�����û�";
		document.all("searchLayer").style.display="none";
	}
}

//�鿴�û���ص���Ϣ
function GoContent(user_id, gather_id){
	var strpage="EGWUserRelatedInfo.jsp?user_id=" + user_id + "&gather_id=" + gather_id;
	window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}
//����ض��û�ʱ��FORM
function CheckFormForm(){
	var adslflag,accountflag;
	if(KillSpace(document.frm.username.value).length != 0){
		accountflag = true;
	}
	if(KillSpace(document.frm.phonenumber.value).length != 0){
		adslflag = true;
	}
	if(accountflag || adslflag){
		return true;
	}else{
		alert("�������ѯ����");
		document.frm.username.focus();
		document.frm.username.select();
		return false;		
	}
}
//���ͳ��ʱ��FORM
function CheckStatForm() {
	if (document.all("some_service").value < 0) {
		alert("����ѡ�����!");
		document.all("some_service").focus();
		return false;
	} else if (document.all("dev_id") != null && document.all("dev_id").value == "") {
		alert("���������豸ID!");
		document.all("dev_id").select();
		document.all("dev_id").focus();
		return false;
	} else if (document.all("vendorName") != null && document.all("vendorName").value < 0) {
		alert("����ѡ���û�����!");
		document.all("vendorName").focus();
		return false;
	} else if (document.all("devModelName") != null && document.all("devModelName").value < 0) {
		alert("����ѡ���û�����!");
		document.all("devModelName").focus();
		return false;
	} else if (document.all("osVersionName") != null && document.all("osVersionName").value < 0) {
		alert("����ѡ���û��汾!");
		document.all("osVersionName").focus();
		return false;
	} else if (document.all("user_domain") != null && document.all("user_domain").value < 0) {
		alert("����ѡ���û���!");
		document.all("user_domain").focus();
		return false;
	} else if (document.all("dev_ip") != null && document.all("dev_ip").value == "") {
		alert("���������豸IP!");
		document.all("dev_ip").select();
		document.all("dev_ip").focus();
		return false;
	} else {
//		statTable.style.display = "";
//		statTable.innerHTML = "������������......";
		return true;
	}
}

//ѡ��ȫ��
function checkedAll(){
	var oSelect = document.all("isCheckedToDel");
	if (flag) {
		if(oSelect != null && typeof(oSelect) == "object" ) {
			if(typeof(oSelect.length) != "undefined") {
				for(var i=0; i<oSelect.length; i++) {
					oSelect[i].checked = true;
				}
			}
			else {
				oSelect.checked  = true;
			}
		} else {
			//alert("û�п�ѡ����û�!");
		}
		flag = false;
	} else {
	if(oSelect != null && typeof(oSelect) == "object" ) {
			if(typeof(oSelect.length) != "undefined") {
				for(var i=0; i<oSelect.length; i++) {
					oSelect[i].checked = false;
				}
			}
			else {
				oSelect.checked  = false;
			}
		} else {
			//alert("û�п�ѡ����û�!");
		}
		flag = true;
	}
	
}
//���ɾ��ʱ��FORM
function CheckFormDel() {
	var oSelect = document.all("isCheckedToDel");
	if(oSelect != null && typeof(oSelect) == "object" ) {
		if(typeof(oSelect.length) != "undefined") {
			for(var i=0; i<oSelect.length; i++) {
				if(oSelect[i].checked) {
					if (!delWarn()) {
						return false;
					} else {
						return true;
					}
				}
			}
			alert("����ѡ���û�!");
			return false;
		}
		else {
			if(oSelect.checked) {
				if (!delWarn()) {
					return false;
				} else {
					return true;
				}
			} else {
				alert("����ѡ���û�!");
				return false;
			}
		}
	} else {
		alert("û�п�ɾ�����û�!");
		return false;
	}
}
//�����ļ���EXCEL
function ToExcel() {
	var page="EGWUserInfoToExcel.jsp?title='������excel'&filename=filename";
	document.all("childFrm").src=page;
	//window.open(page);
}

function TypeChange_1() {
	var index=document.all.group_type_1.selectedIndex;
	switch(index) {
		//δѡ������
		case 0: {
			document.all("filterCon_1").innerHTML = "";
			break;
		}
		//�豸ID
		case 1: {
			document.all("filterCon_1").innerHTML = "<input type='text' name='dev_id' value=''>";
			break;
		}
		//�豸����
		case 2: {
			document.all("filterCon_1").innerHTML = strVendorList;
			break;
		}
		//�豸����
		case 3: {
			document.all("filterCon_1").innerHTML = strDevModelList;
			break;
		}
		//�豸�汾
		case 4: {
			document.all("filterCon_1").innerHTML = strOsVersionList;
			break;
		}
	}
}

function TypeChange_2() {
	var index=document.all.group_type_2.selectedIndex;
	switch(index) {
		//δѡ������
		case 0: {
			document.all("filterCon_2").innerHTML = "";
			break;
		}
		//����״̬
		case 1: {
			document.all("filterCon_2").innerHTML = "<input type='radio' name='CPE_CurrentStatus' value='1' checked>����&nbsp;<input type='radio' name='CPE_CurrentStatus' value='0'>����&nbsp;";
			break;
		}
		//������
		case 2: {
			document.all("filterCon_2").innerHTML = strDomain;
			break;
		}
		//�豸IP
		case 3: {
			document.all("filterCon_2").innerHTML = "<input type='text' name='dev_ip' value=''>";
			break;
		}
	}
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
<FORM NAME="frm" METHOD="post" ACTION="EGWUserInfoList.jsp?flg='search'&opt=<%=opt%>">
<TABLE id="searchLayer" width="95%" border=0 cellspacing=0 cellpadding=0 align="center" style="display:none">
		<TR>
			<TD bgcolor=#999999>
			  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR> 
                  <TH colspan="4" align="center">��ѯ�û���Ϣ</TH>
                </TR>
                <TR bgcolor="#FFFFFF"> 
                  <TD class=column>�û��ʻ�</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="username" class=bk>
                  </TD>
                  <TD class=column width=180>��ϵ�绰</TD>
                  <TD>
                    <INPUT TYPE="text" NAME="phonenumber" class=bk>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF">
                  <TD align=left colspan=8> 
                    <font color="red">ע:�綼Ϊ��,�����ȫ��!</font>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF">
                  <TD class=foot align=right colspan=8> 
                    <input type="submit" name="submit" value=" �� ѯ " class=btn>
                    <input type="hidden" name="searchForUsers" value="queryUsers">
                  </TD>
                </TR>
              </TABLE>
			</TD>
		</TR>
	</TABLE>
</FORM>

<FORM NAME="statForm" METHOD="post" ACTION="EGWUserInfoList.jsp?flg='sub'&opt=<%=opt%>" onSubmit="return CheckStatForm()">
<TABLE id="statLayer" width="95%" border=0 cellspacing=0 cellpadding=0 align="center" style="display:none">
		<TR>
			<TD bgcolor=#999999>
			  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR> 
                  <TH colspan="4" align="center">ͳ���û���Ϣ</TH>
                </TR>
                <TR bgcolor="#FFFFFF">
                  <TD class=column>ҵ��</TD>
                  <TD> 
                  	<%	String servTypeNameList = HGWUserInfoAct.getServTypeNameList(5); %>
					<%=servTypeNameList%>
                  </TD>
                  <TD class=column>�Ƿ�ͨ</TD>
                  <TD>
                    <input type="radio" value="1" name="radioBtn" checked>��ͨ&nbsp;
				    <input type="radio" value="0" name="radioBtn">δ��ͨ&nbsp;&nbsp;
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF"> 
                  <TD class=column>��������:
                  <SELECT NAME="group_type_1" class=bk onchange="javascript:TypeChange_1();">
			     	 <option value="0">==��ѡ��==</option>
                     <option value="1">�豸ID</option>
                     <option value="2">�豸����</option>
                     <option value="3">�豸����</option>
                     <option value="4">����汾</option>
                  </SELECT>&nbsp;&nbsp;
                  </TD>
                  <TD>
                  <DIV id="filterCon_1"></DIV>
                  </TD>
                  <TD class=column>��������:
                  <SELECT NAME="group_type_2" class=bk onchange="javascript:TypeChange_2();">
			     	 <option value="0">==��ѡ��==</option>
                     <option value="1">����״̬</option>
                     <option value="2">������</option>
                     <option value="3">�豸IP</option>
                  </SELECT>&nbsp;&nbsp;
                  </TD>
                  <TD>
                     <DIV id="filterCon_2"></DIV>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF">
                  <TD class=foot align=right colspan=8> 
                    <input type="submit" name="submit" value=" ͳ �� " class=btn>
                    <input type="hidden" name="searchForUsers" value="statUsers">
                  </TD>
                </TR>
              </TABLE>
			</TD>
		</TR>
	</TABLE>
</FORM>

<FORM NAME="delFrom" METHOD="post" ACTION="EGWUserInfoSave.jsp" onSubmit="return CheckFormDel()">
<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
	
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						�û���Դ
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						��ҵ�����û���Դ�б�,�ɶ��û���Դ���м�����ͳ��						
					</td>
					<td align="right">
						
					</td>
				</tr>
			</table>
			</td>
		</tr>
				
		<%
		if(rtnMsg.length() > 0) {
		%>
			<TR>
			<TD height="15">
			<font color=red>ͳ���û�������<%= rtnMsg%></font>
			</TD>
			</TR>
		<%
		}
		%>
	<TR>
		<TD bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH colspan="8">
						��ҵ�����û��б�
					</TH>
				</TR>
				<TR>
					<TD align="left" class="green_title2"><input type='checkbox' name='CheckAllToDel' onclick='checkedAll()'></TD>
					<TD class="green_title2">�û��ʻ�</TD>
					<TD class="green_title2">�ͻ�����</TD>
					<TD class="green_title2">����</TD>
					<TD class="green_title2">�Ƿ���豸</TD>
					<TD class="green_title2">����ʱ��</TD>
					<TD class="green_title2">����</TD>
				</TR>
				<%=strData%>
				<TR> 
                  <TD colspan="8" align="right" class=foot> 
					<input type="button" name="Button_1" value="�����û�" class="btn" onclick="showSearch()">
					&nbsp;&nbsp;
					<!-- 
					<input type="button" name="Button_2" value="ͳ���û�" class="btn" onclick="showStatSearch()">
					&nbsp;&nbsp;
					 -->
					 <s:if test='#session.isReport=="1"'>
					<INPUT TYPE="button" value=" �����ļ�" class=btn onclick="ToExcel()">
					</s:if>
					&nbsp;&nbsp;
					<% if ("edit".equals(opt)){%>
					<!-- ��������ɾ�� -->
                    <!--  <INPUT TYPE="submit" value=" ����ɾ��" class=btn>
                    <INPUT TYPE="hidden" name="action" value="deleteBatch">-->
                    <%} %>
                  </TD>
                </TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD></TR>
</TABLE>

<SCRIPT LANGUAGE="JavaScript">
<!--
var statShow = 0;
function showStatSearch(){
	if(statShow == 0){
		statShow = 1;
		document.all("Button_2").value = "����ͳ��";
		document.all("statLayer").style.display="";
	}else{
		statShow = 0;
		document.all("Button_2").value = "ͳ���û�";
		document.all("statLayer").style.display="none";
	}
}
//	document.onload = showStatSearch();
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>

