<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%@ page import="com.linkage.litms.common.database.Cursor" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil" %>
<%@ page import="com.linkage.litms.common.database.QueryPage" %>
<%@ page import="com.linkage.litms.paramConfig.warnAction" %>
<%@ page import="com.linkage.litms.common.util.CommonMap" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%
String start_time = request.getParameter("hidstart");
String end_time = request.getParameter("hidend");

//格式化时间
DateTimeUtil dateTime = new DateTimeUtil(start_time);
long s_time = dateTime.getLongTime();
dateTime = new DateTimeUtil(end_time);
long e_time = dateTime.getLongTime();

String useflag = request.getParameter("useflag");
String device_id = request.getParameter("device_id");
String gather_id = request.getParameter("gather_id");
String vendor_id = request.getParameter("vendor_id");
String devicetype_id = request.getParameter("devicetype_id");
String softwareversion = request.getParameter("softwareversion");

String warnID = request.getParameter("warnID");
String warnType = request.getParameter("warnType");
String warnLevel = request.getParameter("warnLevel");
String warnStatus = request.getParameter("warnStatus");

//sql语句的组装
String sql = "";

if ("1".equals(useflag)){
	// teledb
	if (DBUtil.GetDB() == 3) {
		sql += "select a.mlevel, a.type, a.device_id, a.occurtime, a.id, a.status, a.description" +
				" from tab_alarm a where a.occurtime > "
				+ s_time + " and a.occurtime <" + e_time;
	}
	else {
		sql += "select a.* from tab_alarm a where a.occurtime > "
				+ s_time + " and a.occurtime <" + e_time;
	}
}
else{
	// teledb
	if (DBUtil.GetDB() == 3) {
		sql += "select a.mlevel, a.type, a.device_id, a.occurtime, a.id, a.status, a.description" +
				" from tab_alarm a where 1=1 " ;
	}
	else{
		sql += "select a.* from tab_alarm a where 1=1 " ;
	}
}

String sqlDevice = "";

//若选择了具体设备则不需要厂商、版本等条件
if (device_id != null && !"".equals(device_id)){
	sql += " and a.device_id = '" + device_id + "'";
}
else{
	if (gather_id != null && !"".equals(gather_id) && !"-1".equals(gather_id)){
		sqlDevice += " and b.gather_id = '" + gather_id + "'";
	}
	
	if (vendor_id != null && !"".equals(vendor_id) && !"-1".equals(vendor_id)){
		sqlDevice += " and b.oui = '" + vendor_id + "'";
	}
	
	if (devicetype_id != null && !"".equals(devicetype_id) && !"-1".equals(devicetype_id)){
		sqlDevice += " and c.device_model = '" + devicetype_id + "'";
	}
	
	if (softwareversion != null && !"".equals(softwareversion) && !"-1".equals(softwareversion)){
		sqlDevice += " and c.softwareversion = '" + softwareversion + "'";
	}
	
	if (!"".equals(sqlDevice)){
		sql += " and a.device_id in (select b.device_id from tab_gw_device b,tab_devicetype_info c where b.devicetype_id = c.devicetype_id" + sqlDevice + ")";
	}
}

if ((warnID != null) && (!"".equals(warnID)) && (!"-1".equals(warnID))){
	sql += " and a.id = " + warnID;
}

if ((warnType != null) && (!"".equals(warnType)) && (!"-1".equals(warnType))){
	sql += " and a.type = " + warnType;
}

if ((warnLevel != null) && (!"".equals(warnLevel)) && (!"-1".equals(warnLevel))){
	sql += " and a.mlevel = " + warnLevel;
}

if ((warnStatus != null) && (!"".equals(warnStatus)) && (!"-1".equals(warnStatus))){
	sql += " and a.status = " + warnStatus;
}

//Cursor cursor1 = DataSetBean.getCursor(sql);
//int total = cursor1.getRecordSize();

if (device_id == null){
	device_id = "";
}

//翻页时的查询条件
String search = "&hidstart=" + start_time + "&warnID=" + warnID + "&warnType=" + warnType 
				+ "&warnLevel=" + warnLevel + "&warnStatus=" + warnStatus
				+ "&hidend=" + end_time + "&device_id=" + device_id + "&gather_id=" + gather_id
				+ "&vendor_id=" + vendor_id + "&devicetype_id=" + devicetype_id
				+ "&softwareversion=" + softwareversion + "&useflag=" + useflag;

String stroffset = request.getParameter("offset");
int offset=1;
int pagelen = 20;
if (stroffset != null){
	offset = Integer.parseInt(stroffset);
}

if (stroffset == null)
	offset = 1;
else
	offset = Integer.parseInt(stroffset);

com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
psql.getSQL();
//生成页码等信息
QueryPage qryp = new QueryPage();
qryp.initPage(sql, offset, pagelen);
String strBar = qryp.getPageBar(search);

//查询告警信息
Cursor cursor = DataSetBean.getCursor(sql,offset,pagelen);
Map fields = cursor.getNext();

warnAction warnAct = new warnAction();

Map deviceNameMap = CommonMap.getDeviceNameMap();
%>

<script language="javascript">
function confirmWarn(id, device_id, time, status){

	if (status == 2 || status ==4){
		alert('该告警已经确认过!');
	}
	else if (status == 1 || status ==3){
		var page="changeStatus.jsp?id="+id+"&device_id="+device_id+"&time="+time+"&action=confirm&status="+status+"&refresh="+(new Date()).getTime();
		document.all("childFrm").src = page;
	}
	else{
		alert('未知告警状态无法确认!');
	}
}

function cleanWarn(id, device_id, time, status){

	if (status == 3 || status == 4){
		alert('该告警已经清除过!');
	}
	else if (status == 1 || status == 2){
		var page="changeStatus.jsp?id="+id+"&device_id="+device_id+"&time="+time+"&action=clean&status="+status+"&refresh="+(new Date()).getTime();
		document.all("childFrm").src = page;
	}
	else{
		alert('未知告警状态无法清除!');
	}
}

function cancelConfirmWarn(id, device_id, time, status){
	if (status == 1 || status == 3){
		alert('该告警还未确认!');
	}
	else if (status == 2 || status == 4){
		var page="changeStatus.jsp?id="+id+"&device_id="+device_id+"&time="+time+"&action=cancelConfirm&status="+status+"&refresh="+(new Date()).getTime();
		document.all("childFrm").src = page;
	}
	else{
		alert('未知告警状态无法操作!');
	}
}

function showDetail(id, device_id, time, status){
	window.showModalDialog("./showWarnDetail.jsp?id="+id+"&device_id="+device_id+"&time="+time);
}

function reloadForm(){
	window.location.reload("./historyWarn.jsp?offset=<%=offset%><%=search%>");
}

function confirmBatch(){
	document.frm.isBatch.value="1";
	document.frm.action.value="confirm";
	
	var obj = document.frm.checkWarm;
	var tmpList = "";
	for (var i=0;i<obj.length;i++){
		if (obj[i].checked){
			if (tmpList == ""){
				tmpList = obj[i].value;
			}
			else{
				tmpList = tmpList + "#" + obj[i].value;
			}
		}
	}
	
	if (tmpList == ""){
		alert("请选择需要确认的告警");
		return false;
	}
	
	document.frm.warmList.value = tmpList;
	
	document.frm.submit();
}

function cleanBatch(){
	document.frm.isBatch.value="1";
	document.frm.action.value="clean";
	
	var obj = document.frm.checkWarm;
	var tmpList = "";
	for (var i=0;i<obj.length;i++){
		if (obj[i].checked){
			if (tmpList == ""){
				tmpList = obj[i].value;
			}
			else{
				tmpList = tmpList + "#" + obj[i].value;
			}
		}
	}
	
	if (tmpList == ""){
		alert("请选择需要清除的告警");
		return false;
	}
	
	document.frm.warmList.value = tmpList;
	
	document.frm.submit();
}

function canelConfirmBatch(){
	document.frm.isBatch.value="1";
	document.frm.action.value="cancelConfirm";
	
	var obj = document.frm.checkWarm;
	var tmpList = "";
	for (var i=0;i<obj.length;i++){
		if (obj[i].checked){
			if (tmpList == ""){
				tmpList = obj[i].value;
			}
			else{
				tmpList = tmpList + "#" + obj[i].value;
			}
		}
	}
	
	if (tmpList == ""){
		alert("请选择需要清除的告警");
		return false;
	}
	
	document.frm.warmList.value = tmpList;
	
	document.frm.submit();
}


function checkAllWarm(){
	
	var obj = document.frm.checkWarm;
	if (document.frm.checkAll.checked){
		for (var i=0;i<obj.length;i++){
			obj[i].checked = true;
		}
		document.frm.checkAll2.checked = true;
	}
	else{
		for (var i=0;i<obj.length;i++){
			obj[i].checked = false;
		}
		document.frm.checkAll2.checked = false;
	}
}
function checkAllWarm2(){
	
	var obj = document.frm.checkWarm;
	if (document.frm.checkAll2.checked){
		for (var i=0;i<obj.length;i++){
			obj[i].checked = true;
		}
		document.frm.checkAll.checked = true;
	}
	else{
		for (var i=0;i<obj.length;i++){
			obj[i].checked = false;
		}
		document.frm.checkAll.checked = false;
	}
}
</script>

<%@ include file="../head.jsp"%>
<form name="frm" action="changeStatus.jsp" method="POST" target="childFrm1">
	<input type="hidden" name="offset" value="1">
	<input type="hidden" name="isBatch" value="0">
	<input type="hidden" name="warmList" value="">
	<input type="hidden" name="action" value="">
	<table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td height="20"></td>
		</tr>
		<TR>
			<TD colspan="2">
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							历史告警列表
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2 bgcolor=#999999>
					<TR><TH colspan="10">实时告警</TH></TR>
					<tr>
						<TD class="green_title2"><input type="checkbox" name="checkAll" onclick="checkAllWarm()"></TD>
						<TD class="green_title2">编号</TD>
						<TD class="green_title2">告警源</TD>
						<TD class="green_title2">告警时间</TD>
						<TD class="green_title2">告警级别</TD>
						<TD class="green_title2">告警类型</TD>
						<TD class="green_title2">告警名称</TD>
						<TD class="green_title2">告警描述</TD>
						<TD class="green_title2">告警状态</TD>
						<TD class="green_title2">操作</TD>
					</tr>
					<%
					if (cursor.getRecordSize() !=0){
					while (fields != null){ 
						String level = (String)fields.get("mlevel");
						String style = "color=black";
						if ("1".equals(level)){
							style = "color=#FF0000";
						}
						else if ("2".equals(level)){
							style = "color=#FFB4B2";
						}
						else if ("3".equals(level)){
							style = "color=#FFC351";
						}
						else if ("4".equals(level)){
							style = "color=#FFEBB5";
						}
						else if ("5".equals(level)){
							style = "color=#E1ECFB";
						}
						else{
							style = "color=black";
						}
						
						String type = (String)fields.get("type");
						String warnOrigin = (String)fields.get("device_id");
						if ("1".equals(type)){
							if (deviceNameMap.get(warnOrigin) != null){
								warnOrigin = (String)deviceNameMap.get(warnOrigin);
							}
							else{
								warnOrigin = "未知设备";
							}
						}
						
						String time = (String)request.getParameter("occurtime");
						if ("0".equals(time)){
							time = (new DateTimeUtil(0)).getLongDate();
						}
						else{
							time = (new DateTimeUtil(Long.parseLong((String)fields.get("occurtime"))*1000)).getLongDate();
						}
						
						String checkWarm = (String)fields.get("id")
											+ "," + (String)fields.get("device_id")
											+ "," + (String)fields.get("occurtime")
											+ "," + (String)fields.get("status");
					%>
					<tr bgcolor=#ffffff>
						<td width="5%" class=column align=left><input type="checkbox" name="checkWarm" value="<%=checkWarm %>"></td>
						<td width="10%" class=column align=left nowrap><font <%=style%>><%=fields.get("id")%></font></td>
						<td width="10%" class=column align=left><font <%=style%>><%=warnOrigin%></font></td>
						<td width="10%" class=column align=left nowrap><font <%=style%>><%=(new DateTimeUtil(Long.parseLong((String)fields.get("occurtime"))*1000)).getLongDate()%></font></td>
						<td width="10%" class=column align=left nowrap><font <%=style%>><%=warnAct.getWarnLevel(fields.get("mlevel"))%></font></td>
						<td width="10%" class=column align=left nowrap><font <%=style%>><%=warnAct.getWarnType(type)%></font></td>
						<td width="10%" class=column align=left nowrap><font <%=style%>><%=warnAct.getWarnName(fields.get("id"))%></font></td>
						<td width="20%" class=column align=left nowrap><textarea rows="3" rows="2" class="input-textarea" style="width:100%"><%=fields.get("description")%></textarea></td>
						<td width="10%" class=column align=left nowrap><font <%=style%>><%=warnAct.getWarnStatus(fields.get("status"))%></font></td>
						<td width="10%" class=column align=left nowrap>
							<a href="#" onclick="confirmWarn('<%=fields.get("id")%>','<%=fields.get("device_id")%>','<%=fields.get("occurtime")%>','<%=fields.get("status")%>')">确认</a><BR>
							<a href="#" onclick="cleanWarn('<%=fields.get("id")%>','<%=fields.get("device_id")%>','<%=fields.get("occurtime")%>','<%=fields.get("status")%>')">清除</a><BR>
							<a href="#" onclick="cancelConfirmWarn('<%=fields.get("id")%>','<%=fields.get("device_id")%>','<%=fields.get("occurtime")%>','<%=fields.get("status")%>')">取消确认</a><BR>
							<a href="#" onclick="showDetail('<%=fields.get("id")%>','<%=fields.get("device_id")%>','<%=fields.get("occurtime")%>','<%=fields.get("status")%>')">告警信息</a>
						</td>
					</tr>
					<%
						fields = cursor.getNext();
					} 
					%>
					<tr>
						<TD class="green_title2"><input type="checkbox" name="checkAll2" onclick="checkAllWarm2()"></TD>
						<TD class="green_title2">编号</TD>
						<TD class="green_title2">告警源</TD>
						<TD class="green_title2">告警时间</TD>
						<TD class="green_title2">告警级别</TD>
						<TD class="green_title2">告警类型</TD>
						<TD class="green_title2">告警名称</TD>
						<TD class="green_title2">告警描述</TD>
						<TD class="green_title2">告警状态</TD>
						<TD class="green_title2">操作</TD>
					</tr>

					<%
					} else{%>
					<tr><td colspan="10" class=column>没有查询到数据</td></tr>
					<%} %>
					<tr >
						<td colspan="5"  align="left" class="green_foot"  height="6"><a href="#" onclick="confirmBatch()">批量确认</a><a href="#" onclick="cleanBatch()">  批量清除</a><a href="#" onclick="canelConfirmBatch()">  批量取消确认</a></td>
						<td colspan="5" align=right  class="green_foot"  height="6"><%=strBar %></td>
					</tr>
					<TR style="display:none">
						<TD>
							<IFRAME ID="childFrm" SRC="" STYLE="display:none"></IFRAME>
						</TD>
					</TR>
				</table>
			</td>
		</tr>
	</table>
	<iframe name="childFrm1" id="childFrm1" style="display:none"></iframe>
</form>
<BR><BR>
<%@ include file="../foot.jsp"%>

