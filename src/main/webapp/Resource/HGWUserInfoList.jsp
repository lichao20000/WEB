<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.module.gwms.dao.tabquery.CityDAO"%>
<%@page import="com.linkage.module.gwms.util.StringUtil"%>
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
<%@ page
	import="java.util.ArrayList,com.linkage.litms.Global,com.linkage.litms.common.database.*,java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%--
	zhaixf(3412) 2008-05-12
	req:NJLC_HG-BUG-ZHOUMF-20080508-001
--%>
<SCRIPT LANGUAGE="JavaScript">

var flag = true;


</SCRIPT>

<%
	request.setCharacterEncoding("GBK");
	String username_r = request.getParameter("username");
	String usernameType_r = request.getParameter("usernameType");
	String starttime_r = request.getParameter("starttime");
	String endtime_r = request.getParameter("endtime");
	String queryFlag = request.getParameter("queryFlag");//�Ƿ���ʾ�б�
	String isFirst= request.getParameter("flg");
	String isJs = LipossGlobals.getLipossProperty("InstArea.ShortName");
	username_r = username_r == null ? "" : username_r;
	starttime_r = starttime_r == null ? "" : starttime_r;
	endtime_r = endtime_r == null ? "" : endtime_r;
	String gw_type = request.getParameter("gw_type");
	System.out.println("gw_type:"+gw_type);
	//flg
	String flg = request.getParameter("flg");
	//opt���������б��ǲ�������edit�� or ��view��
	String opt = request.getParameter("opt");
	//String rtnMsg = "";
	//ȡ���豸����
	//String strVendorList = "";
	//ȡ���豸����
	//String strDevModelList = "";
	//ȡ���豸�汾
	//String strOsVersionList = "";
	//ȡ���û���
	//String strDomain = "";
	StringBuffer strData = new StringBuffer();
	if ("query".equals(queryFlag))
	{
		ArrayList list = new ArrayList();
		list.clear();
		//������еķ�����Ϣ
		//Cursor cursor_service = HGWUserInfoAct.getServiceInfo();
		//Map fields_service = cursor_service.getNext();
		//ȡ���豸����
		//strVendorList = HGWUserInfoAct.getVendorList(false,"","vendorName");
		//ȡ���豸����
		//strDevModelList = HGWUserInfoAct.getDevModelList(false,"","devModelName");
		//ȡ���豸�汾
		//strOsVersionList = HGWUserInfoAct.getOsVersionList(false,"","osVersionName");
		//ȡ���û���
		//strDomain = HGWUserInfoAct.getUserDomains();
		//����HGW�û��б�
		list = HGWUserInfoAct.getHGWUsersCursor(request);
		Map cityMap = CityDAO.getCityIdCityNameMap();
		Map<String, String> userTypeMap = HGWUserInfoAct.getUserType();
		String strBar = String.valueOf(list.get(0));
		Cursor cursor = (Cursor) list.get(1);
		//if (list.size()==3 && list.get(2) != null) {
		//	rtnMsg = (String)list.get(2);
		//}
		Map fields = cursor.getNext();
		if (fields == null)
		{
			strData
			.append("<TR><TD class=column COLSPAN=10 HEIGHT=30>û�м������û�!</TD></TR>");
		}
		else
		{
			String cust_type;
			String os = "";
			Calendar time = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String opendate = "";
			while (fields != null)
			{
		cust_type = "";
		opendate = "";
		if ("0".equals((String) fields.get("cust_type_id")))
		{
			cust_type = "��˾�ͻ�";
		}
		else if ("1".equals((String) fields.get("cust_type_id")))
		{
			cust_type = "���ɿͻ�";
		}
		else if ("2".equals((String) fields.get("cust_type_id")))
		{
			cust_type = "���˿ͻ�";
		}
		else
		{
			cust_type = "-";
		}
		if (fields.get("device_serialnumber") == null
				|| fields.get("device_serialnumber").equals(""))
		{
			os = "-";
		}
		else
		{
			os = fields.get("oui") + "-"
			+ fields.get("device_serialnumber");
		}
		//		String gather_tmp = (String)fields.get("gather_id");
		//		if (gather_tmp == null || "".equals(gather_tmp) || "-1".equals(gather_tmp)){
		//	gather_tmp = "";
		//		}
		if (fields.get("opendate") != null
				&& !fields.get("opendate").equals("")
				&& !fields.get("opendate").equals("0"))
		{
			time.setTimeInMillis((Long.parseLong((String) fields
			.get("opendate"))) * 1000);
			opendate = df.format(time.getTime());
		}
		else
		{
			opendate = "-";
		}
		String user_state = (String) fields.get("user_state");
		String serv_type = (String) fields.get("serv_type_id");
		serv_type = (Global.Serv_type_Map.get(serv_type) == null) ? ""
				: (String) Global.Serv_type_Map.get(serv_type);
		String user_type_id = (String) fields.get("user_type_id");
		String tmp = "�ֹ����";
		if (false == StringUtil.IsEmpty(user_type_id))
		{
			tmp = userTypeMap.get(user_type_id);
			if (true == StringUtil.IsEmpty(tmp))
			{
				tmp = "����";
			}
		}
		//String onlinedate = "";
		//if(fields.get("onlinedate") != null && !(fields.get("onlinedate").equals(""))
		//		&& !(fields.get("onlinedate").equals("0"))){
		//	time.setTimeInMillis((Long.parseLong((String)fields.get("onlinedate")))*1000);
		//	onlinedate = df.format(time.getTime());
		//}else{
		//	onlinedate = "-";
		//}
		String opmode = "";
		if ("1".equals(fields.get("opmode")))
		{
			opmode = "<IMG SRC='../images/check.gif' BORDER='0' ALT='�ѿ���' style='cursor:hand'>";
		}
		else
		{
			opmode = "<IMG SRC='../images/button_s.gif' BORDER='0' ALT='δ����' style='cursor:hand'>";
		}
		String username = (String) fields.get("username");
		if (username == null || "".equals(username))
			username = "-";
		String city = (String) fields.get("city_id");
		if (city == null || "".equals(city))
		{
			city = "-";
		}
		else
		{
			city = (String) cityMap.get(city);
		}
		strData.append("<TR>");
		//strData.append("<TD class=column1 ><input type='checkbox' name='isCheckedToDel' value='"+(String)fields.get("user_id")+"|"+(String)fields.get("gather_id")+"'></TD>");
		strData.append("<TD class=column1 align='center'>" + username
				+ "</TD>");
		strData.append("<TD class=column2 align='center'>" + city
				+ "</TD>");
		strData.append("<TD class=column2 align='center'>" + tmp
				+ "</TD>");
		//strData += "<TD class=column1 align='left'>"+ serv_type + "</TD>";
		//strData += "<TD class=column2 align=\"center\">"+ cust_type + "</TD>";
		strData
				.append("<TD class=column1 align='center'>" + os
				+ "</TD>");
		strData.append("<TD class=column2 align='center'>" + opendate
				+ "</TD>");
		if(!"sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
			strData.append("<TD class=column2 align='center'>" + opmode
					+ "</TD>");
		}
		if ("edit".equals(opt))
		{
			if (user_state.equals("1") || user_state.equals("2"))
			{
				strData
				.append("<TD class=column1 align='center'>"
				//+ "<A HREF=AddGWUserInfoForm.jsp?user_id="+ (String)fields.get("user_id") 
				//+"&gather_id="+(String)fields.get("gather_id") 
				//+"&gwOptType=12"
				+ "<A HREF='../gwms/resource/hgwcustManage.action?userId="
				+ fields.get("user_id")
				+"&gw_type="+gw_type
				+ "'"
				+ "><IMG SRC='../images/edit.gif' BORDER='0' ALT='�༭' style='cursor:hand'></A>  <A HREF=GwUserInfoSave.jsp?gwOptType=11&action=delete&user_id="
				+ (String) fields.get("user_id")
				+ "&gather_id="
				+ (String) fields.get("gather_id")
				+ " onclick='return delWarn();'><IMG SRC='../images/del.gif' BORDER='0' ALT='ɾ��' style='cursor:hand'></A>  <A HREF=\"javascript:GoContent(\'"
				+ (String) fields.get("user_id")
			//	+ "\',\'"
			//	+ (String) fields.get("access_style_id")
				+ "\',\'"
				+ (String) fields.get("gather_id")
				+ "\');\" TITLE=�鿴�û�"
				+ (String) fields.get("realname")
				+ "�������Ϣ>"
				+ "<IMG SRC='../images/view.gif' BORDER='0' ALT='��ϸ��Ϣ' style='cursor:hand'></A></TD>");
			}
			else if (user_state.equals("3"))
			{
				strData
				.append("<TD class=column1 align='center'>������ | "
				+ "<A HREF=GwUserInfoSave.jsp?gwOptType=11&action=delete&user_id="
				+ (String) fields.get("user_id")
				+ "&gather_id="
				+ (String) fields.get("gather_id")
				+ " onclick='return delWarn();'><IMG SRC='../images/del.gif' BORDER='0' ALT='ɾ��' style='cursor:hand'></A> | <A HREF=\"javascript:GoContent(\'"
				+ (String) fields.get("user_id")
			//	+ "\',\'"
			//	+ (String) fields.get("access_style_id")
				+ "\',\'"
				+ (String) fields.get("gather_id")
				+ "\');\" TITLE=�鿴�û�"
				+ (String) fields.get("realname")
				+ "�������Ϣ>"
				+ "<IMG SRC='../images/view.gif' BORDER='0' ALT='��ϸ��Ϣ' style='cursor:hand'></A></TD>");
			}
			else if (flg != null)
			{
				strData
				.append("<TD class=column1 align='center'> <A HREF=GwUserInfoSave.jsp?gwOptType=11&action=delete&user_id="
				+ (String) fields.get("user_id")
				+ "&gather_id="
				+ (String) fields.get("gather_id")
				+ " onclick='return delWarn();'><IMG SRC='../images/del.gif' BORDER='0' ALT='ɾ��' style='cursor:hand'></A> | <A HREF=\"javascript:GoContent(\'"
				+ (String) fields.get("user_id")
			//	+ "\',\'"
			//	+ (String) fields.get("access_style_id")
				+ "\',\'"
				+ (String) fields.get("gather_id")
				+ "\');\" TITLE=�鿴�û�"
				+ (String) fields.get("realname")
				+ "�������Ϣ>"
				+ "<IMG SRC='../images/view.gif' BORDER='0' ALT='��ϸ��Ϣ' style='cursor:hand'></A></TD>");
			}
		}
		else
		{
			strData
			.append("<TD class=column1 align='center'><A HREF=\"javascript:GoContent(\'"
					+ (String) fields.get("user_id")
				//	+ "\',\'"
				//	+ (String) fields.get("access_style_id")
					+ "\',\'"
					+ (String) fields.get("gather_id")
					+ "\');\" TITLE=�鿴�û�"
					+ (String) fields.get("realname")
					+ "�������Ϣ>"
					+ "<IMG SRC='../images/view.gif' BORDER='0' ALT='��ϸ��Ϣ' style='cursor:hand'></A></TD>");
		}
		strData.append("</TR>");
		fields = cursor.getNext();
			}
			strData.append("<TR><TD class=column COLSPAN=10 align=right>"
			+ strBar + "</TD></TR>");
		}
		fields = null;
		list.clear();
		list = null;
	}
%>
<%@ include file="../head.jsp"%>
<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/jQeuryExtend-linkage.js"></script>
<script type="text/javascript"
			src="../Js/My97DatePicker/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
 $(function() {
	//��ʼ��ʱ��
	//init();
	var queryFlag = "<%=queryFlag%>";
	var isFirst =<%=isFirst%>;
	var isJs= "<%=isJs%>";
    if(isFirst=='search' && isJs=='js_dx'){
	$("input[@name='button']").attr("disabled", false); 
}

	
	if("query"==queryFlag){
		$("form[@name='delFrom']").show();
	}else{
		$("form[@name='delFrom']").hide();
	}
	var usernameType_r = "<%=usernameType_r%>";
	if(usernameType_r==""||usernameType_r=="null"){
		usernameType_r = "2";
	}
	$("select[@name='usernameType']").attr("value",usernameType_r);
});
        //��ʼ��ʱ��
function init() {
	$("input[@name='starttime']").val($.now("-",false)+" 00:00:00");
	$("input[@name='endtime']").val($.now("-",true));
}


//var strVendorList = "<%//= strVendorList%>";
//var strDomain = "<%//= strDomain%>";
//var strDevModelList = "<%//= strDevModelList%>";
//var strOsVersionList = "<%//= strOsVersionList%>";

//-->
</SCRIPT>

<SCRIPT LANGUAGE="JavaScript">


//function go(){
//	v = document.all("txtpage").value;
//	if(parseInt(v) && parseInt(v)>0){
	//	this.location = "HGWUserInfoList.jsp?offset="+ ((eval(v)-1)*15+1)
						+ "&opt=<%//=opt%>";
//	}
//}

//ɾ��ʱ����
function delWarn(){
	if(confirm("���Ҫɾ���ü�ͥ�����û��͸��û�����Ӧ���豸��\n��������ɾ���Ĳ��ָܻ�������")){
		return true;
	}
	else{
		return false;
	}
}
/*
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
*/
//�鿴�û���ص���Ϣ
function GoContent(user_id,gather_id){
	
	//var strpage="HGWUserRelatedInfo.jsp?user_id=" + user_id +"&access_style_id=" + access_style_id+ "&gather_id=" + gather_id;
	
	var strpage="HGWUserRelatedInfo.jsp?user_id=" + user_id + "&gather_id=" + gather_id;
	
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}
/*
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

//���ϴ��ļ�����
function openUploadWin() {
	var strpage="HGWUserFileUploadForm.jsp";
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}
*/
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
	var page="HGWUserInfoToExcel.jsp?title='����'&starttime=" + document.frm.starttime.value + "&endtime=" + document.frm.endtime.value + "&username=" + document.frm.username.value;
//	alert(page)
	document.all("childFrm").src=page;
	//window.open(page);
}

//reset
function resetFrm() {
	document.frm.starttime.value="";
	document.frm.endtime.value="";
	document.frm.username.value="";
}
/*
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
*/
//-->

/**
 * add by chenjie(67371) 2011-3-17
 * ��ѯ�ύǰ���������жϣ����������*���������в�ѯ
 */
function toSubmit()
{
	var form = $("form[@name='frm']");
	var input = $("input[@name='username']");
	var input_value = trim(input.val());
	input.val(input_value);
	// У���ֵ
	if(input.val() == "")
	{
		alert("���������*�Ĳ�ѯ����!");
		input.focus();
		input.select();
	}
	else
	{
		$("input[@name='button']").attr("disabled", true);
		form.submit();
	}
}

/*LTrim(string):ȥ����ߵĿո�*/
function LTrim(str){
    var whitespace = new String("�� \t\n\r");
    var s = new String(str);   

    if (whitespace.indexOf(s.charAt(0)) != -1){
        var j=0, i = s.length;
        while (j < i && whitespace.indexOf(s.charAt(j)) != -1){
            j++;
        }
        s = s.substring(j, i);
    }
    return s;
}

/*RTrim(string):ȥ���ұߵĿո�*/
function RTrim(str){
    var whitespace = new String("�� \t\n\r");
    var s = new String(str);
 
    if (whitespace.indexOf(s.charAt(s.length-1)) != -1){
        var i = s.length - 1;
        while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1){
            i--;
        }
        s = s.substring(0, i+1);
    }
    return s;
}

/*Trim(string):ȥ���ַ������ߵĿո�*/
function trim(str){
    return RTrim(LTrim(str)).toString();
}

</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post"
				ACTION="HGWUserInfoList.jsp?flg='search'&opt=<%=opt%>" >
				<TABLE id="searchLayer" width="98%" border=0 cellspacing=0
					cellpadding=0 align="center" style="display: ">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite">
										�û���ѯ
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										�����û���Դ,��ѯʱ��Ϊ����ʱ��.
										<!-- &nbsp;&nbsp;
						<input type="hidden" name="Button_1" value="�����û�" class="btn" onclick="showSearch()">
					&nbsp;&nbsp;
					<input type="hidden" name="Button_2" value="ͳ���û�" class="btn" onclick="showStatSearch()">
						 -->
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
										�û���ѯ
									</TH>
								</TR>
								<TR>
									<td class="column" width='15%' align="right">
										��ʼʱ��
									</td>
									<td class="column" width='35%' align="left">
										<input type="text" name="starttime" readonly
											value="<%=starttime_r%>" class=bk>
										<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
											src="../images/dateButton.png" width="15" height="12"
											border="0" alt="ѡ��">
									</td>
									<td class="column" width='15%' align="right">
										����ʱ��
									</td>
									<td class="column" width='35%' align="left">
										<input type="text" name="endtime" readonly
											value="<%=endtime_r%>" class=bk>
										<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
											src="../images/dateButton.png" width="15" height="12"
											border="0" alt="ѡ��">
									</td>
								</TR>
								<TR bgcolor="#FFFFFF">
									<td class="column" width='15%' align="right">
										<SELECT name="usernameType" class="bk">
											<option value="6">
												�豸���к�
											</option>
											<option value="1">
												<ms:inArea areaCode="sx_lt">
													Ψһ��ʶ
												</ms:inArea>
												<ms:inArea areaCode="sx_lt" notInMode="true">
													LOID
												</ms:inArea>
											</option>
											<option value="2" selected>
												��������˺�
											</option>
											<option value="3">
												IPTV����˺�
											</option>
											<option value="4">
												VoIP��֤����
											</option>
											<option value="5">
												VoIP�绰����
											</option>
										</SELECT>
									</TD>
									<TD colspan="3">
										<INPUT TYPE="text" NAME="username" class=bk
											value=<%=username_r%>>
										<font color="red">*</font>
									</TD>
								</TR>
								<tr style="display: none">
								<td colspan="4"><input type="text" value='<%=gw_type%>' name="gw_type" ></td></tr>
							
							
								<TR bgcolor="#FFFFFF">
									<TD class=green_foot align=right colspan=8>
										<input type="button" name="button" value=" �� ѯ " class=btn onclick="toSubmit()">
										<input type="hidden" name="queryFlag" value="query" />
										<INPUT CLASS="btn" TYPE="button" value=" �� �� "
											onclick="resetFrm()">
										<s:if test='#session.isReport=="1"'>
											<!-- <INPUT TYPE="button" value=" �� �� " class=btn
												onclick="ToExcel()">  -->
										</s:if>
										<input type="hidden" name="searchForUsers" value="queryUsers">
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</FORM>

			<!-- <FORM NAME="statForm" METHOD="post" ACTION="HGWUserInfoList.jsp?flg='sub'&opt=<%//=opt%>" onSubmit="return CheckStatForm()">
<TABLE id="statLayer" width="98%" border=0 cellspacing=0 cellpadding=0 align="center" style="display:none">
		<TR>
			<TD bgcolor=#999999>
			  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR> 
                  <TH colspan="4" align="center">ͳ���û���Ϣ</TH>
                </TR>
                <TR bgcolor="#FFFFFF">
                  <TD class=column>ҵ��</TD>
                  <TD> 
                  	<%	//String servTypeNameList = HGWUserInfoAct.getServTypeNameList(1); %>
					<%//=servTypeNameList%>
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
 -->
			<FORM NAME="delFrom" METHOD="post" ACTION="HGWUserInfoSave.jsp"
				onSubmit="return CheckFormDel()" style="display: none">
				<TABLE width="98%" border=0 cellspacing=0 cellpadding=0
					align="center">


					<%
					//if(rtnMsg.length() > 0) {
					%>
					<!-- <TR>
			<TD height="15">
			<font color=red>ͳ���û�������<%//= rtnMsg%></font>
			</TD>
			</TR> -->
					<%
					//}
					%>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR>
									<!-- <TH><input type='checkbox' name='CheckAllToDel' onclick='checkedAll()'></TH> -->
									<TH>
										�û��ʺ�
									</TH>
									<TH>
										��&nbsp;&nbsp;��
									</TH>
									<TH>
										�û���Դ
									</TH>
									<TH>
										���豸
									</TH>
									<TH>
										����ʱ��
									</TH>
									<ms:inArea areaCode="sx_lt" notInMode="true">
									<TH>
										����״̬
									</TH>
									</ms:inArea>
									<TH width=100>
										����
									</TH>
								</TR>
								<%=strData%>
								<!--<TR> 
                  <TD colspan="10" align="left" class=green_foot> 
					
                  	<!-- <INPUT TYPE="button" value=" �����ļ� " class=btn onclick="openUploadWin()">
					<INPUT TYPE="button" value=" �����ļ�" class=btn onclick="ToExcel()">
					&nbsp;&nbsp;
                    <!-- <INPUT TYPE="submit" value=" ����ɾ��" class=btn> 
                    <INPUT TYPE="hidden" name="action" value="deleteBatch">
                  </TD>
                </TR>-->
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</FORM>
		</TD>
	</TR>

	<TR>
		<TD HEIGHT=20>
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=10>
			<IFRAME ID=deleChildFrm SRC="" STYLE="display: none"></IFRAME>
			&nbsp;
		</TD>
	</TR>
</TABLE>

<SCRIPT LANGUAGE="JavaScript">
/*
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
*/
//	document.onload = showStatSearch();
</SCRIPT>
<%@ include file="../foot.jsp"%>

