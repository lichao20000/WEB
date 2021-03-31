<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.filter.SelectCityFilter"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.linkage.litms.netcutover.SheetList"%>
<%@page import="java.util.Date"%>
<jsp:useBean id="sheetManage" scope="request"
	class="com.linkage.litms.netcutover.SheetManage" />
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
<%
	request.setCharacterEncoding("GBK");

	SimpleDateFormat sdt = new SimpleDateFormat("yyyy-M-d");
	String today = sdt.format(new Date());

	String flag = request.getParameter("flag");

	String serviceList = HGWUserInfoAct.getEgwServTypeList();

	SheetList sheet = new SheetList();
	String strDeviceModelList = sheet.getDeviceModelList();
	//----------------------属地过滤 add by YYS 2006-9-26 ----------------
	// 获取属地数据
	String city_id = curUser.getCityId();
	SelectCityFilter City = new SelectCityFilter(request);
	String city_name = City.getNameByCity_id(city_id);
	String strCityList = City.getSelfAndNextLayer(true, city_id, "");
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--

//----------------------属地过滤 add by YYS 2006-9-26 ----------------
var user_city_id = "<%=city_id%>";//用户所属属地编号
var city_id = "<%=city_id%>";//用户所选中的属地编号
function showChild(parname,event){
	var  event = window.event || event;
	var  obj  = window.event ? event.srcElement:event.currentTarget;
	//var obj = event.srcElement;
        
	if(parname=='city_id'){
		city_id = obj.options[obj.selectedIndex].value;
		if(city_id!=-1){
			if(user_city_id != city_id){
				document.all("childFrm").src = "cityFilter.jsp?city_id="+city_id;
			}else{
				document.all("my_city<%=city_id%>").innerHTML = "";
				document.all("city_name").innerHTML = "<font color='red'><%=city_name%></font>";
			}
			document.all("hid_city_id").value = city_id;
		}else
			alert("请选择一个有效的属地");
	}
}
//--------------------------------------------------------------------

function DateToDes(v,type){
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
		if(type == "start")
			dt = new Date(m+"/"+d+"/"+y);
		else
			dt = new Date(m+"/"+d+"/"+y);

		var s  = dt.getTime();
		return s/1000;
	}
	else
		return 0;
}


function checkAll(){

	var s = document.all.end.value + " " + document.all.endTime.value;	
	var d = s.replace("\-","\/");
	t =  new Date(d);
	
	//document.frm.tableTime.value = t.getTime();
	m = parseInt(t.getMonth()) + 1;
	if (m < 10)
		document.all.tableTime.value = t.getYear() + "0" + m;
	else 
		document.all.tableTime.value = "" + t.getYear() + m;
	var vStart;
	var vEnd;

	var obj = document.frm;

	if(!IsNull(obj.start.value,'开始时间')){
		obj.start.focus();
		obj.start.select();
		return false;
	} else if (!IsNull(obj.startTime.value,'开始时间')) {
		obj.startTime.focus();
		obj.startTime.select();
		return false;
	} else if (!IsNull(obj.end.value,'结束时间')) {
		obj.end.focus();
		obj.end.select();
		return false;
	} else if (!IsNull(obj.endTime.value,'结束时间')) {
		obj.endTime.focus();
		obj.endTime.select();
		return false;
	} 
	
	vStart = DateToDes(obj.start.value,"start");
	vEnd = DateToDes(obj.end.value,"end");
	var dt1 = new Date(vStart * 1000);
	var dt2 = new Date(vEnd * 1000);
	if (dt1.getYear() != dt2.getYear()) {
		alert("开始时间和结束时间必须为同一年!");
		return false;
	} else if (dt1.getMonth() != dt2.getMonth()) {
		alert("开始时间和结束时间必须为同一月!");
		return false;
	}
	return true;
}

function showVal(obj) {
	var serv_type_id = obj.value;
	document.all("childFrm").src = "jt_Work_handForm_list.jsp?serv_type_id=" + serv_type_id + "&refresh=" + new Date().getTime();
}


//-->
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/JSCalendar.js"></SCRIPT>
<link rel="stylesheet" href="../css/JSCalendar.css" type="text/css">
<style>
span {
	position: static;
	border: 0;
}
</style>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align=center>
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<tr>
		<td>
		<table width="100%" height="30" border="0" cellspacing="0"
			cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">工单管理</td>
			</tr>
		</table>
		</td>
	</tr>

	<TR>
		<TD>
		<form name="frm" id="sheet_frm" method="post" action="sheet_SearchList.jsp"
		 target="childFrm2">
		<table id="searchLayer" width="100%" border=0 cellspacing=0
			cellpadding=0 align="center">
			<tr>
				<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr>
						<th colspan="4" align="center">工单查询</th>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column width="148">工单编号</td>
						<td width="286"><input type="text" name="sheet_id" class="bk"></td>
						<td class=column>业务帐号</td>
						<td><input type="text" name="username" class="bk"></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class="column">设备型号</td>
						<td><%=strDeviceModelList%></td>
						<td class=column>设备序列号</td>
						<td><input type="text" name="device_serialnumber" class="bk">&nbsp;模糊匹配</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<TD class=column>业务类型</TD>
						<TD ><%=serviceList%></TD>
						<td class=column>操作类型</td>
						<td><select name="oper_type" class="bk">
							<option value="-1" selected>==请选择==</option>
						</select></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column width="148">工单状态</td>
						<td colspan="3" width="286"><select name="sheet_status" class=bk>
							<option value="-1">==请选择==</option>
							<option value="0">--系统未知错误--</option>
							<option value="1">--执行成功--</option>
							<option value="-1">--设备连接不上--</option>
							<option value="-2">--设备没有响应--</option>
							<option value="-3">--系统没有工单--</option>
							<option value="-4">--系统没有设备--</option>
							<option value="-5">--设备正被操作--</option>
							<option value="-6">--设备正被操作--</option>
							<option value="-7">--系统参数错误--</option>
							<option value="other">--其他--</option>
						</select></td>
						<!-- <td class=column width=109>来自何处</td>
						<td width="338""><select name="sheet_source" class=bk>
							<option value="-1">==请选择==</option>
							<option value="0">--操作界面--</option>
							<option value="1">--BSS--</option>
						</select></td> -->
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD class=column>请选择筛选属地</TD>
						<TD colspan="3"><span id="initCityFilter"><%=strCityList%>
						<span id='my_city<%=city_id%>'></span></span> &nbsp;当前所选属地为:<span
							id=city_name><font color='red'><%=city_name%></font></span>
						&nbsp;包含所有下级属地: <input type="radio" name='ifcontainChild'
							value='1' checked>是 <input type="radio"
							name='ifcontainChild' value='0'>否 <input type="hidden"
							name='hid_city_id' value=<%=city_id%>></TD>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td class=column width="148">开始时间</td>
						<td width="286" colspan="3"><input type="text" name="start"
							value=<%=today%> class=bk readonly> <input type="button"
							value="▼" class=jianbian onClick="showCalendar('day',event)"
							name="button232"> <input type="text" name="startTime"
							class="bk" size="10" value="00:00:00"></td>
						<input type="hidden" name="start_time" value="">
					<tr bgcolor="#FFFFFF">
						<td class=column width=109>结束时间</td>
						<td width="338" colspan="3"><input type="text" name=end
							value="<%=today%>" class=bk readonly> <input
							type="button" value="▼" class=jianbian
							onClick="showCalendar('day',event)" name="button2222"> <input
							type="text" name="endTime" class="bk" size="10" value="23:59:59">
						<INPUT TYPE="hidden" NAME="tableTime"> <input
							type="hidden" name="end_time" value=""></td>
					</tr>
					<!----------------------------->
					<tr bgcolor="#FFFFFF">
						<td class=foot align=right colspan=4><input type="hidden"
							name="flag" value="<%=flag%>"> <input type="button" value=" 查 询 " 
							onclick="doQuery()" class=btn></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</form>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><div id="showList"></div></TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>&nbsp;</TD>
	</TR>
</TABLE>
<table width="100%" border="0" cellspacing="0"
	cellpadding="0">
	<TR>
		<TD nowrap><IFRAME name=childFrm2 frameborder="no" border="0"
			marginwidth="0" marginheight="0" scrolling="no"
			allowtransparency="yes" STYLE="display: none"></IFRAME></TD>
	</TR>
</table>
<SCRIPT LANGUAGE="JavaScript">
	showVal(document.all("some_service"));
	function goPage(offset) {
		var form=document.all("frm");
		form.action="sheet_SearchList.jsp?offset="+offset;
		form.submit();
		//window.location.href="sheet_SearchList.jsp?offset="+offset;
	}
	function doQuery(){
		if(checkAll()){
			var form=document.all("frm");
			form.action="sheet_SearchList.jsp?offset=1";
			form.submit();
		}else{
			return;
		}
	}
	function doDbClick(o){
		parames = o.parames;
		arrParame = parames.split(",");
		sheet_id = arrParame[0];
		receive_time = arrParame[1];
		gather_id = arrParame[2];
		page = "sheet_detail.jsp?sheet_id="+ sheet_id + "&receive_time="+ receive_time;
		window.open(page,new Date().getTime(),"left=20,top=20,width=500,height=420,resizable=no,scrollbars=no");
	}
</SCRIPT>

<%@ include file="../foot.jsp"%>
