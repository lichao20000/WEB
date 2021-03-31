<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.system.UserRes"%>
<%@page import="com.linkage.litms.system.User"%>
<%@page import="com.linkage.module.gtms.stb.utils.ResTool"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%
request.setCharacterEncoding("GBK");
UserRes curUser = (UserRes) session.getAttribute("curUser");
String city_id = curUser.getCityId();
// SelectCityFilter CityFilter = new SelectCityFilter(request);
 // String strCityList = CityFilter.getAllSubCitiesBox(city_id, false,city_id, "per_city", true); 
  //System.out.println("####strCityList="+strCityList);
User user = curUser.getUser();

List<Map> cityList = ResTool.getSubCityList(city_id, -1, true);

%>
<html>
<head>
<link href="<s:url value='/css/base.css'/>" rel="stylesheet" type="text/css" />
<link href="<s:url value='/css/scene.css'/>" rel="stylesheet" type="text/css" />
<title>日志管理</title>
<lk:res />
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
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<script language="JavaScript">
$(document).ready(function(){
	var startTime = '<s:property value="logObj.starttime" />';
	var endTime = '<s:property value="logObj.endtime" />';
	
	$("input[name='logObj.starttime']").attr("value", startTime);
	if('' != endTime){
		$("input[name='logObj.endtime']").attr("value", endTime);
	}

	var selectOperType = '<s:property value="logObj.operType" />';
	$("#selectOperType").val(selectOperType);
	
});

function checkForm(){
	$("input[name='logObj.operType']").val($("#selectOperType").val());

	$("input[name='logObj.area_id']").val($("input[name='area_id']").val());
	$("input[name='logObj.role_id']").val($("input[name='gr_oid']").val());
	$("form[name='frm']").submit();
}


function exportLog()
{
	var item = document.getElementsByName("logObj.itemSelect")[0].value;
	var url = "<s:url value='/gtms/system/logManage!excelLog.action'/>";
	window.open(url+"?logObj.account="+$("input[name=logObj.account]").val()+"&logObj.itemSelect="+item+"&logObj.starttime="+$("input[name=logObj.starttime]").val()+"&logObj.endtime="+$("input[name=logObj.endtime]").val()+"&logObj.area_id=" + $("input[name=area_id]").val()+"&logObj.role_id="+$("input[name=gr_oid]").val());
}

function areaSelect(){
	//var area_pid = document.all("area_id").value;
	var width = 360;
	var page = "../../system/AreaSelect.jsp?area_pid=<%=user.getAreaId()%>&width="+ width;
	window.open(page,"","left=20,top=20,width="+ width +",height=450,resizable=no,scrollbars=no");	
	
}

function SelectRole(){
	var page = "../../system/RolePicker.jsp?refresh="+Math.random();
	var vReturnValue = window.showModalDialog(page,"","dialogHeight: 500px; dialogWidth: 540px; dialogTop: px; dialogLeft: px; edge: Raised; center: Yes; help: No; resizable: No; status: No;scroll:No;");
	var obj = document.frm;
	if(typeof(vReturnValue) == "object"){
		obj.gr_name.value = vReturnValue.name;
		obj.gr_oid.value  = vReturnValue.oid
	}
}

function reSetArea()
{
	$("input[name='area_id']").val("");
	$("input[name='area_name']").val("");
}

function reSetRole()
{
	$("input[name='gr_oid']").val("");
	$("input[name='gr_name']").val("");
}
</script>

</head>
<body>
<form id="frm" name="frm" method="post"
	action="<s:url value='/gtms/system/logManage!queryLog.action'/>">
	<input type="hidden" name="logObj.area_id" value="" />
	<input type="hidden" name="logObj.role_id" value="" />
<table class="querytable" align="center" width="98%" style="margin-top: 15px">
	<tr>
		<td colspan="4" class="title_1">功能使用统计</td>
	</tr>
	<tr>
		<td class="title_2">账号：</td>
		<td><label> <input type="text" name="logObj.account" value="<s:property value="logObj.account" />"
			id="textfield" /> </label></td>
		<td class="title_2">模块：</td>
		<td><select name="logObj.itemSelect" size="1" id="item">
			<option value="">请选择</option>
			<s:iterator value="itemList" status="status">
				<option value="<s:property value="item_id"/>"><s:property
					value="item_name" /></option>
			</s:iterator>
		</select>
		</td>
	</tr>
	<tr>
		<td class="title_2">登录域：</td>
		<td>
			<INPUT TYPE="text" NAME="area_name" value="<s:property value="area_name" />" readOnly class="bk"  onclick=areaSelect()>
			<INPUT TYPE="hidden" NAME="area_id" value='<s:property value="logObj.area_id" />'><nobr class="BT" onmouseover="this.className='BTOver'" onmouseout="this.className='BT'" onclick="areaSelect();">
			<IMG SRC="<s:url value='/images/search.gif' />" WIDTH="15" HEIGHT="12" BORDER="0" ALT="查找角色" valign="middle">&nbsp;选择</nobr>
			<button onclick="reSetArea();">重置</button>
		</td>
		<td class="title_2">角色：</td>
		<td>
			<INPUT TYPE="text" NAME="gr_name" maxlength=200 class=bk size=20 value="<s:property value="gr_name" />" readonly><INPUT TYPE="hidden" NAME="gr_oid" value='<s:property value="logObj.role_id" />'>
			<nobr class="BT" onmouseover="this.className='BTOver'" onmouseout="this.className='BT'" onclick="SelectRole();">
			<IMG SRC="<s:url value='/images/search.gif"'/> WIDTH="15" HEIGHT="12" BORDER="0" ALT="查找角色" valign="middle">&nbsp;查找</nobr>
			<button onclick="reSetRole();">重置</button>
		</td>
	</tr>
	<%--<tr>
		<td class="title_2">操作类型：</td>
		<td><select name="selectOperType" size="1" id="selectOperType">
			<option value="">请选择</option>
			<option value="1">查询</option>
			<option value="2">添加</option>
			<option value="3">修改</option>
			<option value="4">删除</option>
		</select>
			<input type="hidden" name="logObj.operType">
		</td>
		
		<td class="title_2">操作内容：</td>
		<td><label><input type="text" name="logObj.operContent" value="<s:property value="logObj.operContent" />"
			id="textfield" /> </label></td>
	</tr>
	<tr>
		<td class="title_2">IP地址：</td>
		<td><label><input type="text" name="logObj.ipAddr" value="<s:property value="logObj.ipAddr" />"
			id="textfield" /> </label></td>
		<td class="title_2">主机名：</td>
		<td><label><input type="text" name="logObj.hostname" value="<s:property value="logObj.hostname" />"
			id="textfield" /> </label></td>
	</tr>--%>
	<tr>
		<td class="title_2">开始时间：</td>
		<td><label>
		<lk:date id="logObj.starttime" name="logObj.starttime" type="all" defaultDate="" /></label>
		</td>
		<td class="title_2">结束时间：</td>
		<td><label><lk:date id="logObj.endtime" name="logObj.endtime" type="all" defaultDate="" /></label></td>
	</tr>
	<tr>
		<td class="foot" colspan="4">
			<div class="right">
			<button name="query" onclick="checkForm()">查询</button>
			<button name="excelLog" onclick="exportLog()">导出</button>
			</div>
		</td>
	</tr>
</table>

<table class="listtable" width="98%" align="center" style="margin-top: 15px">
	<thead>
	<tr>
		<th>时间</th>
		<th>账号</th>
		<th>角色</th>
		<th>属地</th>
		<th>模块</th>
		<%--  <th>操作类型</th>
		<th>主机名</th>
		<th>IP地址</th>
		<th>操作内容</th>--%>
	</tr>
	</thead>
	<s:if test="logList!=null&&logList.size()>0">
	<s:iterator value="logList" status="status">
		<tr>
			<td><s:property value="logtime" /></td>
			<td><s:property value="account" /></td>
			<td><s:property value="role_name" /></td>
			<td><s:property value="city_name" /></td>
			<td><s:property value="item" /></td>
			<%--<td><s:property value="operType" /></td>
			<td><s:property value="hostname" /></td>
			<td><s:property value="ipAddr" /></td>
			<td><s:property value="operContent" /></td>--%>
		</tr>
	</s:iterator>
	</s:if>
	<s:else>
		<tr>
			<td colspan="15">
				无符合条件记录
			</td>
		</tr>
	</s:else>
	<tfoot>
	<tr>
		<td colspan="7">
			 <div class="right">
			 <lk:pages url="/gtms/system/logManage!queryLog.action" styleClass="" showType="" isGoTo="true" changeNum="true" />
			 </div>
		</td>
	</tr>
	</tfoot>
</table>
</form>
</body>
</html>