<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.linkage.litms.common.filter.SelectCityFilter"%>
<%@ page import ="com.linkage.litms.netcutover.SheetList" %>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/JSCalendar.js"></SCRIPT>
<link rel="stylesheet" href="../css/JSCalendar.css" type="text/css">
<%
request.setCharacterEncoding("GBK");
SimpleDateFormat sdt = new SimpleDateFormat("yyyy-M-d");
String today = sdt.format(new Date());
//----------------------属地过滤 add by YYS 2006-9-26 ----------------
// 获取属地数据
String city_id = curUser.getCityId();
SelectCityFilter City = new SelectCityFilter(request);
String city_name = City.getNameByCity_id(city_id);
String strCityList = City.getSelfAndNextLayer(true, city_id, "");

SheetList sheet = new SheetList();
Cursor cursor = sheet.queryDdnsSheetList(request);
Map fields = cursor.getNext();
Map cityMap = com.linkage.litms.common.util.CommonMap.getCityMap();

%>
<SCRIPT LANGUAGE="JavaScript">
<!--
//----------------------属地过滤 add by YYS 2006-9-26 ----------------
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
		alert("开始时间和结束时间必须为同一年的同一个月!");
		return false;
	} else if (dt1.getMonth() != dt2.getMonth()) {
		alert("开始时间和结束时间必须为同一月!");
		return false;
	}
	document.frm.submit;
}
//-->
</SCRIPT>
<form name="frm" method="post" onSubmit="return checkAll()">
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
						<th colspan="4" align="center">域名工单查询</th>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column >域名账号</td>
						<td  class="foot"><input type="text" name="domainId" class="bk">
						&nbsp;<font color=red>*</font>fs.ctddns.cn，输入前缀fs</td>
						<td class="column">客户名称</td>
						<td class="foot"><input type = "text" name = "customername" class="bk"></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<TD class=column>请选择筛选属地</TD>
						<TD class=foot><span id="initCityFilter"><%=strCityList%> <span id='my_city<%=city_id%>'></span></span>
						&nbsp;&nbsp;是否包含子属地: 
						<input type="radio" name='ifcontainChild' value='1' checked>是 
						<input type="radio" name='ifcontainChild' value='0'>否
						<input type="hidden" name = 'hid_city_id' value=<%=city_id%>>
						</TD>
						<td class=column>工单状态</td>
						<td class="foot"><select name="result_spec" class=bk>
							<option value="-1">==请选择==</option>
							<option value="0">--执行成功--</option>
							<option value="1">--执行失败--</option>											
						</select></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column width="135">开始时间</td>
						<td class="foot"><input type="text"
							name="start" value=<%=today%> class=bk readonly> <input
							type="button" value="▼" class=jianbian
							onClick="showCalendar('day',event)" name="button232"> <input
							type="text" name="startTime" class="bk" size="10"
							value="00:00:00"></td>

						<td class=column width="135">结束时间</td>
						<td class="foot" ><input type="text"
							name=end value="<%=today%>" class=bk readonly> <input
							type="button" value="▼" class=jianbian
							onClick="showCalendar('day',event)" name="button2222"> <input
							type="text" name="endTime" class="bk" size="10" value="23:59:59">
						<INPUT TYPE="hidden" NAME="tableTime"></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=foot align=right colspan=4>
							<input type="submit" name="submit" value=" 查 询 " class=btn></td>
					</tr>
				</table>
				</td>
			  </tr>
			</table>
		</TD>
	</TR>

<!-- 查询结果 -->

			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr>
						<th width="" nowrap>工单编号</th>
						<th width="" nowrap>属地</th>
						<th width="" nowrap>客户名称</th>
						<th width="" nowrap>操作类型</th>
						<th width="" nowrap>域名</th>
						<th width="" nowrap>域名账号</th>
						<th width="" nowrap>域名密码</th>
						<th width="" nowrap>来单时间</th>
						<th nowrap>工单执行结果描述</th>
					</tr>
			<%
            if (fields != null) {
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String tmp;
                while (fields != null) {
					out.println("<tr value = " + fields.get("result_spec") + " bgcolor='#ffffff'>");
                    out.println("<td>" + fields.get("id")
                            + "</td>");
                    tmp = (String) fields.get("city_id");
                    out.println("<td>" + cityMap.get(tmp)
                            + "</td>");

                    out.println("<td>" + fields.get("customer_name")
                            + "</td>");
                    out.println("<td>" + fields.get("type")
                            + "</td>");
					out.println("<td>"
                            + fields.get("1")
                            + "</td>");
					out.println("<td>"
                            + fields.get("1").toString().substring(0, fields.get("1").toString().indexOf("."))
                            + "</td>");
					out.println("<td>"
                            + fields.get("2")
                            + "</td>");
					out.println("<td>"
                            + sdf.format(new Date(new Long((String)fields.get("receive_date"))))
                            + "</td>");
                    out.println("<td>" 
                    		+ fields.get("result_tail") 
                    		+ "</td>");
                    out.println("</tr>");

                    fields = cursor.getNext();
                }
            } else {
                out.println("<tr bgcolor='#ffffff' ><td align=center colspan=11>没有工单记录</td></tr>");
            }
        %>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</form>
<%@ include file="../foot.jsp"%>