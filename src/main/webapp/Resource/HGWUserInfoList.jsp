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
	String queryFlag = request.getParameter("queryFlag");//是否显示列表
	String isFirst= request.getParameter("flg");
	String isJs = LipossGlobals.getLipossProperty("InstArea.ShortName");
	username_r = username_r == null ? "" : username_r;
	starttime_r = starttime_r == null ? "" : starttime_r;
	endtime_r = endtime_r == null ? "" : endtime_r;
	String gw_type = request.getParameter("gw_type");
	System.out.println("gw_type:"+gw_type);
	//flg
	String flg = request.getParameter("flg");
	//opt用于区分列表还是操作：‘edit’ or ‘view’
	String opt = request.getParameter("opt");
	//String rtnMsg = "";
	//取得设备厂商
	//String strVendorList = "";
	//取得设备类型
	//String strDevModelList = "";
	//取得设备版本
	//String strOsVersionList = "";
	//取得用户域
	//String strDomain = "";
	StringBuffer strData = new StringBuffer();
	if ("query".equals(queryFlag))
	{
		ArrayList list = new ArrayList();
		list.clear();
		//获得所有的服务信息
		//Cursor cursor_service = HGWUserInfoAct.getServiceInfo();
		//Map fields_service = cursor_service.getNext();
		//取得设备厂商
		//strVendorList = HGWUserInfoAct.getVendorList(false,"","vendorName");
		//取得设备类型
		//strDevModelList = HGWUserInfoAct.getDevModelList(false,"","devModelName");
		//取得设备版本
		//strOsVersionList = HGWUserInfoAct.getOsVersionList(false,"","osVersionName");
		//取得用户域
		//strDomain = HGWUserInfoAct.getUserDomains();
		//所有HGW用户列表
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
			.append("<TR><TD class=column COLSPAN=10 HEIGHT=30>没有检索到用户!</TD></TR>");
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
			cust_type = "公司客户";
		}
		else if ("1".equals((String) fields.get("cust_type_id")))
		{
			cust_type = "网吧客户";
		}
		else if ("2".equals((String) fields.get("cust_type_id")))
		{
			cust_type = "个人客户";
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
		String tmp = "手工添加";
		if (false == StringUtil.IsEmpty(user_type_id))
		{
			tmp = userTypeMap.get(user_type_id);
			if (true == StringUtil.IsEmpty(tmp))
			{
				tmp = "其他";
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
			opmode = "<IMG SRC='../images/check.gif' BORDER='0' ALT='已竣工' style='cursor:hand'>";
		}
		else
		{
			opmode = "<IMG SRC='../images/button_s.gif' BORDER='0' ALT='未竣工' style='cursor:hand'>";
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
				+ "><IMG SRC='../images/edit.gif' BORDER='0' ALT='编辑' style='cursor:hand'></A>  <A HREF=GwUserInfoSave.jsp?gwOptType=11&action=delete&user_id="
				+ (String) fields.get("user_id")
				+ "&gather_id="
				+ (String) fields.get("gather_id")
				+ " onclick='return delWarn();'><IMG SRC='../images/del.gif' BORDER='0' ALT='删除' style='cursor:hand'></A>  <A HREF=\"javascript:GoContent(\'"
				+ (String) fields.get("user_id")
			//	+ "\',\'"
			//	+ (String) fields.get("access_style_id")
				+ "\',\'"
				+ (String) fields.get("gather_id")
				+ "\');\" TITLE=查看用户"
				+ (String) fields.get("realname")
				+ "的相关信息>"
				+ "<IMG SRC='../images/view.gif' BORDER='0' ALT='详细信息' style='cursor:hand'></A></TD>");
			}
			else if (user_state.equals("3"))
			{
				strData
				.append("<TD class=column1 align='center'>已销户 | "
				+ "<A HREF=GwUserInfoSave.jsp?gwOptType=11&action=delete&user_id="
				+ (String) fields.get("user_id")
				+ "&gather_id="
				+ (String) fields.get("gather_id")
				+ " onclick='return delWarn();'><IMG SRC='../images/del.gif' BORDER='0' ALT='删除' style='cursor:hand'></A> | <A HREF=\"javascript:GoContent(\'"
				+ (String) fields.get("user_id")
			//	+ "\',\'"
			//	+ (String) fields.get("access_style_id")
				+ "\',\'"
				+ (String) fields.get("gather_id")
				+ "\');\" TITLE=查看用户"
				+ (String) fields.get("realname")
				+ "的相关信息>"
				+ "<IMG SRC='../images/view.gif' BORDER='0' ALT='详细信息' style='cursor:hand'></A></TD>");
			}
			else if (flg != null)
			{
				strData
				.append("<TD class=column1 align='center'> <A HREF=GwUserInfoSave.jsp?gwOptType=11&action=delete&user_id="
				+ (String) fields.get("user_id")
				+ "&gather_id="
				+ (String) fields.get("gather_id")
				+ " onclick='return delWarn();'><IMG SRC='../images/del.gif' BORDER='0' ALT='删除' style='cursor:hand'></A> | <A HREF=\"javascript:GoContent(\'"
				+ (String) fields.get("user_id")
			//	+ "\',\'"
			//	+ (String) fields.get("access_style_id")
				+ "\',\'"
				+ (String) fields.get("gather_id")
				+ "\');\" TITLE=查看用户"
				+ (String) fields.get("realname")
				+ "的相关信息>"
				+ "<IMG SRC='../images/view.gif' BORDER='0' ALT='详细信息' style='cursor:hand'></A></TD>");
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
					+ "\');\" TITLE=查看用户"
					+ (String) fields.get("realname")
					+ "的相关信息>"
					+ "<IMG SRC='../images/view.gif' BORDER='0' ALT='详细信息' style='cursor:hand'></A></TD>");
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
	//初始化时间
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
        //初始化时间
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

//删除时警告
function delWarn(){
	if(confirm("真的要删除该家庭网关用户和该用户所对应的设备吗？\n本操作所删除的不能恢复！！！")){
		return true;
	}
	else{
		return false;
	}
}
/*
var show = 0;
//根据特定条件搜索
function showSearch(){
	if(show == 0){
		show = 1;
		document.all("Button_1").value = "隐藏检索";
		document.all("searchLayer").style.display="";
	}else{
		show = 0;
		document.all("Button_1").value = "检索用户";
		document.all("searchLayer").style.display="none";
	}
}
*/
//查看用户相关的信息
function GoContent(user_id,gather_id){
	
	//var strpage="HGWUserRelatedInfo.jsp?user_id=" + user_id +"&access_style_id=" + access_style_id+ "&gather_id=" + gather_id;
	
	var strpage="HGWUserRelatedInfo.jsp?user_id=" + user_id + "&gather_id=" + gather_id;
	
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}
/*
//检查特定用户时的FORM
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
		alert("请输入查询条件");
		document.frm.username.focus();
		document.frm.username.select();
		return false;		
	}
}
//检查统计时的FORM

function CheckStatForm() {
	if (document.all("some_service").value < 0) {
		alert("请先选择服务!");
		document.all("some_service").focus();
		return false;
	} else if (document.all("dev_id") != null && document.all("dev_id").value == "") {
		alert("请先输入设备ID!");
		document.all("dev_id").select();
		document.all("dev_id").focus();
		return false;
	} else if (document.all("vendorName") != null && document.all("vendorName").value < 0) {
		alert("请先选择用户厂商!");
		document.all("vendorName").focus();
		return false;
	} else if (document.all("devModelName") != null && document.all("devModelName").value < 0) {
		alert("请先选择用户类型!");
		document.all("devModelName").focus();
		return false;
	} else if (document.all("osVersionName") != null && document.all("osVersionName").value < 0) {
		alert("请先选择用户版本!");
		document.all("osVersionName").focus();
		return false;
	} else if (document.all("user_domain") != null && document.all("user_domain").value < 0) {
		alert("请先选择用户域!");
		document.all("user_domain").focus();
		return false;
	} else if (document.all("dev_ip") != null && document.all("dev_ip").value == "") {
		alert("请先输入设备IP!");
		document.all("dev_ip").select();
		document.all("dev_ip").focus();
		return false;
	} else {
//		statTable.style.display = "";
//		statTable.innerHTML = "正在载入数据......";
		return true;
	}
}

//打开上传文件窗口
function openUploadWin() {
	var strpage="HGWUserFileUploadForm.jsp";
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}
*/
//选择全部
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
			//alert("没有可选择的用户!");
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
			//alert("没有可选择的用户!");
		}
		flag = true;
	}
	
}
//检查删除时的FORM
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
			alert("请先选定用户!");
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
				alert("请先选定用户!");
				return false;
			}
		}
	} else {
		alert("没有可删除的用户!");
		return false;
	}
}
//导出文件成EXCEL
function ToExcel() {
	var page="HGWUserInfoToExcel.jsp?title='导出'&starttime=" + document.frm.starttime.value + "&endtime=" + document.frm.endtime.value + "&username=" + document.frm.username.value;
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
		//未选择的情况
		case 0: {
			document.all("filterCon_1").innerHTML = "";
			break;
		}
		//设备ID
		case 1: {
			document.all("filterCon_1").innerHTML = "<input type='text' name='dev_id' value=''>";
			break;
		}
		//设备厂商
		case 2: {
			document.all("filterCon_1").innerHTML = strVendorList;
			break;
		}
		//设备类型
		case 3: {
			document.all("filterCon_1").innerHTML = strDevModelList;
			break;
		}
		//设备版本
		case 4: {
			document.all("filterCon_1").innerHTML = strOsVersionList;
			break;
		}
	}
}

function TypeChange_2() {
	var index=document.all.group_type_2.selectedIndex;
	switch(index) {
		//未选择的情况
		case 0: {
			document.all("filterCon_2").innerHTML = "";
			break;
		}
		//上线状态
		case 1: {
			document.all("filterCon_2").innerHTML = "<input type='radio' name='CPE_CurrentStatus' value='1' checked>在线&nbsp;<input type='radio' name='CPE_CurrentStatus' value='0'>下线&nbsp;";
			break;
		}
		//管理域
		case 2: {
			document.all("filterCon_2").innerHTML = strDomain;
			break;
		}
		//设备IP
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
 * 查询提交前作条件的判断，至少输入带*的条件进行查询
 */
function toSubmit()
{
	var form = $("form[@name='frm']");
	var input = $("input[@name='username']");
	var input_value = trim(input.val());
	input.val(input_value);
	// 校验空值
	if(input.val() == "")
	{
		alert("必须输入带*的查询条件!");
		input.focus();
		input.select();
	}
	else
	{
		$("input[@name='button']").attr("disabled", true);
		form.submit();
	}
}

/*LTrim(string):去除左边的空格*/
function LTrim(str){
    var whitespace = new String("　 \t\n\r");
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

/*RTrim(string):去除右边的空格*/
function RTrim(str){
    var whitespace = new String("　 \t\n\r");
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

/*Trim(string):去除字符串两边的空格*/
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
										用户查询
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										检索用户资源,查询时间为竣工时间.
										<!-- &nbsp;&nbsp;
						<input type="hidden" name="Button_1" value="检索用户" class="btn" onclick="showSearch()">
					&nbsp;&nbsp;
					<input type="hidden" name="Button_2" value="统计用户" class="btn" onclick="showStatSearch()">
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
										用户查询
									</TH>
								</TR>
								<TR>
									<td class="column" width='15%' align="right">
										开始时间
									</td>
									<td class="column" width='35%' align="left">
										<input type="text" name="starttime" readonly
											value="<%=starttime_r%>" class=bk>
										<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
											src="../images/dateButton.png" width="15" height="12"
											border="0" alt="选择">
									</td>
									<td class="column" width='15%' align="right">
										结束时间
									</td>
									<td class="column" width='35%' align="left">
										<input type="text" name="endtime" readonly
											value="<%=endtime_r%>" class=bk>
										<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
											src="../images/dateButton.png" width="15" height="12"
											border="0" alt="选择">
									</td>
								</TR>
								<TR bgcolor="#FFFFFF">
									<td class="column" width='15%' align="right">
										<SELECT name="usernameType" class="bk">
											<option value="6">
												设备序列号
											</option>
											<option value="1">
												<ms:inArea areaCode="sx_lt">
													唯一标识
												</ms:inArea>
												<ms:inArea areaCode="sx_lt" notInMode="true">
													LOID
												</ms:inArea>
											</option>
											<option value="2" selected>
												上网宽带账号
											</option>
											<option value="3">
												IPTV宽带账号
											</option>
											<option value="4">
												VoIP认证号码
											</option>
											<option value="5">
												VoIP电话号码
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
										<input type="button" name="button" value=" 查 询 " class=btn onclick="toSubmit()">
										<input type="hidden" name="queryFlag" value="query" />
										<INPUT CLASS="btn" TYPE="button" value=" 重 置 "
											onclick="resetFrm()">
										<s:if test='#session.isReport=="1"'>
											<!-- <INPUT TYPE="button" value=" 导 出 " class=btn
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
                  <TH colspan="4" align="center">统计用户信息</TH>
                </TR>
                <TR bgcolor="#FFFFFF">
                  <TD class=column>业务</TD>
                  <TD> 
                  	<%	//String servTypeNameList = HGWUserInfoAct.getServTypeNameList(1); %>
					<%//=servTypeNameList%>
                  </TD>
                  <TD class=column>是否开通</TD>
                  <TD>
                    <input type="radio" value="1" name="radioBtn" checked>开通&nbsp;
				    <input type="radio" value="0" name="radioBtn">未开通&nbsp;&nbsp;
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF"> 
                  <TD class=column>过滤条件:
                  <SELECT NAME="group_type_1" class=bk onchange="javascript:TypeChange_1();">
			     	 <option value="0">==请选择==</option>
                     <option value="1">设备ID</option>
                     <option value="2">设备厂商</option>
                     <option value="3">设备类型</option>
                     <option value="4">软件版本</option>
                  </SELECT>&nbsp;&nbsp;
                  </TD>
                  <TD>
                  <DIV id="filterCon_1"></DIV>
                  </TD>
                  <TD class=column>过滤条件:
                  <SELECT NAME="group_type_2" class=bk onchange="javascript:TypeChange_2();">
			     	 <option value="0">==请选择==</option>
                     <option value="1">上线状态</option>
                     <option value="2">管理域</option>
                     <option value="3">设备IP</option>
                  </SELECT>&nbsp;&nbsp;
                  </TD>
                  <TD>
                     <DIV id="filterCon_2"></DIV>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF">
                  <TD class=foot align=right colspan=8> 
                    <input type="submit" name="submit" value=" 统 计 " class=btn>
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
			<font color=red>统计用户条件：<%//= rtnMsg%></font>
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
										用户帐号
									</TH>
									<TH>
										属&nbsp;&nbsp;地
									</TH>
									<TH>
										用户来源
									</TH>
									<TH>
										绑定设备
									</TH>
									<TH>
										开户时间
									</TH>
									<ms:inArea areaCode="sx_lt" notInMode="true">
									<TH>
										竣工状态
									</TH>
									</ms:inArea>
									<TH width=100>
										操作
									</TH>
								</TR>
								<%=strData%>
								<!--<TR> 
                  <TD colspan="10" align="left" class=green_foot> 
					
                  	<!-- <INPUT TYPE="button" value=" 导入文件 " class=btn onclick="openUploadWin()">
					<INPUT TYPE="button" value=" 导出文件" class=btn onclick="ToExcel()">
					&nbsp;&nbsp;
                    <!-- <INPUT TYPE="submit" value=" 批量删除" class=btn> 
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
		document.all("Button_2").value = "隐藏统计";
		document.all("statLayer").style.display="";
	}else{
		statShow = 0;
		document.all("Button_2").value = "统计用户";
		document.all("statLayer").style.display="none";
	}
}
*/
//	document.onload = showStatSearch();
</SCRIPT>
<%@ include file="../foot.jsp"%>

