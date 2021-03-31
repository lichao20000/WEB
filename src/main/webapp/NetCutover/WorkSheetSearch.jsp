<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
<%@page import="com.linkage.litms.common.filter.SelectCityFilter"%>


<SCRIPT LANGUAGE="JavaScript" SRC="../Js/JSCalendar.js"></SCRIPT>
<link rel="stylesheet" href="../css/JSCalendar.css" type="text/css">
<%
	request.setCharacterEncoding("GBK");
	SimpleDateFormat sdt = new SimpleDateFormat("yyyy-M-d");
	String today = sdt.format(new Date());

	// 获取属地数据
	String city_id = curUser.getCityId();
	SelectCityFilter City = new SelectCityFilter(request);
	//String servTypeList = HGWUserInfoAct.getServTypeList(0);//getEgwServTypeList();
	String city_name = City.getNameByCity_id(city_id);
	String strCityList = City.getSelfAndNextLayer(false, city_id, "cityId");
%>
<SCRIPT LANGUAGE="JavaScript">

var user_city_id = "<%=city_id%>";//用户所属属地编号
var city_id = "<%=city_id%>";//用户所选中的属地编号
function showChild(parname){
	var obj = event.srcElement;
        
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
	var obj = document.frm;
	var serviceType = obj.some_service.value;
	var cityId = obj.cityId.value;
	var oper_type = obj.oper_type.value;
	var username = obj.username.value;
	var s = document.all.end.value + " " + document.all.endTime.value;	
	var d = s.replace("\-","\/");
	t =  new Date(d);
	m = parseInt(t.getMonth()) + 1;
	if (m < 10)
		document.all.tableTime.value = t.getYear() + "0" + m;
	else 
		document.all.tableTime.value = "" + t.getYear() + m;
	var vStart;
	var vEnd;


	if(serviceType == "-1"){
		alert("请选择业务类型  ");
		return;
	}
  

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
		alert("开始时间和结束时间必须为同一年的同一个月!");
		return false;
	} else if (dt1.getMonth() != dt2.getMonth()) {
		alert("开始时间和结束时间必须为同一月!");
		return false;
	}
	//document.all("childFrmList").innerHTML = "正在查询中………………";
	document.all("childFrmList").src = "WorkSheetSearchResult.jsp?some_service="+serviceType
											+"&city_id="+cityId
											+"&oper_type="+oper_type
											+"&username="+username
											+"&start="+obj.start.value
											+"&end="+obj.end.value
											+"&startTime="+obj.startTime.value
											+"&endTime="+obj.endTime.value;
											
}


function showVal(obj) {
	var serv_type_id = obj.value;
	//document.all("childFrm").src = "jt_Work_handForm_list.jsp?serv_type_id=" + serv_type_id + "&refresh=" + new Date().getTime();
}
//-->
</SCRIPT>
<form name="frm" method="post" >
<TABLE border=0 cellspacing=0 cellpadding=0 width="95%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						工单管理
					</td>
				</tr>
			</table>
		</td>
	</tr>
	
	<TR>
		<TD>
		  <table id="searchLayer" width="100%" border=0 cellspacing=0
			cellpadding=0 align="center">
			<tr>
				<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr>
						<th colspan="6" align="center">BSS工单查询</th>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column >LOID</td>
						<td colspan=5><input type="text" name="username" class="bk"></td>
						
					</tr>
					<tr bgcolor="#FFFFFF">
						<TD class=column>属地</TD>
						<TD ><%=strCityList%></TD>
						<TD class=column>业务类型</TD>
					<TD>
				<select name="some_service" class="bk">
								<option value="-1" selected>==请选择==</option>
								<option value="22">==宽带==</option>
								<option value="11">==IPTV==</option>
								<option value="15">==VOIP==</option>
								<option value="20">==用户工单==</option>
							</select>
					</TD>
						<td class=column>操作类型</td>
					<td>
							<select name="oper_type" class="bk">
								<option value="-1" selected>==请选择==</option>
								<option value="1">==开户==</option>
								<option value="3">==销户==</option>
								<option value="6">==更改帐号==</option>
								<option value="8">==移机==</option>
							</select>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column width="90">开始时间</td>
						<td ><input type="text"
							name="start" value=<%=today%> class=bk size="20" readonly> <input
							type="button" value="▼" class=jianbian
							onClick="showCalendar('day',event)" name="button232"> <input
							type="text" name="startTime" class="bk" size="10"
							value="00:00:00"></td>

						<td class=column width="90">结束时间</td>
						<td colspan="3"><input type="text"
							name=end value="<%=today%>" class=bk size="20" readonly> <input
							type="button" value="▼" class=jianbian
							onClick="showCalendar('day',event)" name="button2222"> <input
							type="text" name="endTime" class="bk" size="10" value="23:59:59">
						<INPUT TYPE="hidden" NAME="tableTime"></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=foot align=right colspan=6>
							<input type="button" name="submit" value=" 查 询 " class=btn onClick="checkAll()"></td>
					</tr>
				</table>
				</td>
			  </tr>
		   </table>
		</TD>   
	</TR>
	<!-- 查询结果 -->
	<TR>
		<TD>
			<div ID=childList ></div>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD>
	</TR>
	<TR>
		<TD><IFRAME ID=childFrmList SRC="" STYLE="display:none"></IFRAME>&nbsp;
		</TD>
	</TR>
</TABLE>
</form>
<SCRIPT LANGUAGE="JavaScript">
<!--
function doDbClick(o){

	sheet_id = o.value;
	page = "bbsSheet_detail.jsp?sheet_id="+ sheet_id;

	window.open(page,"","left=20,top=20,width=600,height=400,resizable=yes,scrollbars=yes");
}

showVal(document.all("some_service"));
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>