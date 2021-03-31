<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.Cursor"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil" %>
<%@ page import="com.linkage.litms.paramConfig.warnAction" %>
<%@ page import="com.linkage.litms.common.util.CommonMap" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%
int total = 0;
String sql = "select * from tab_alarm where 1=1 ";

// teledb
if (DBUtil.GetDB() == 3) {
	sql = "select mlevel, type, device_id, id, occurtime, status, description from tab_alarm where 1=1 ";
}

//告警类型
String warnType = request.getParameter("warnType");
if (warnType != null && !"".equals(warnType) && !"-1".equals(warnType)){
	sql += " and type = " + warnType;
}

//告警等级
String warnLevel = request.getParameter("warnLevel");
if (warnLevel != null && !"".equals(warnLevel) && !"-1".equals(warnLevel)){
	sql += " and mlevel = " + warnLevel;
}

//告警状态
String warnStatus = request.getParameter("warnStatus");
if (warnStatus != null && !"".equals(warnStatus) && !"-1".equals(warnStatus)){
	sql += " and status = " + warnStatus;
}

sql += " order by occurtime desc";

com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
psql.getSQL();
//获取最近的200条记录
Cursor cursor=DataSetBean.getCursor(sql,200);
Map fields=cursor.getNext();

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
	window.showModalDialog("./showWarnDetail.jsp?id="+id+"&device_id="+device_id+"&time="+time+"&refresh="+(new Date()).getTime());
}

function refreshAlarm(){
	var warnType = document.frm.warnType.value;
	var warnLevel = document.frm.warnLevel.value;
	var warnStatus = document.frm.warnStatus.value;
	
	window.location.reload("./showLastWarn.jsp?warnType="+warnType+"&warnLevel="+warnLevel+"&warnStatus="+warnStatus);
}

function reloadForm(){
	window.location.reload();
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
<form name="frm" method="post" action="changeStatus.jsp" target="childFrm1">
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
						<TD width="164" align="center" class="title_bigwhite" nowrap>
							告警管理
						</TD>
						<td align=right nowrap>告警类型</td>
    					<td>
    						<select name="warnType" id="warnType" class=bk onchange="refreshAlarm()">
    							<option value="-1">--所有类型--</option>
    							<option value="1">设备告警</option>
    							<option value="2">服务质量告警</option>
    							<option value="3">通信告警</option>
    							<option value="4">处理失败告警</option>
    							<option value="5">网管系统产生的告警</option>
    						</select>
    					</td>
						<td align=right nowrap>告警级别</td>
    					<td>
    						<select name="warnLevel" id="warnLevel" class=bk onchange="refreshAlarm()">
    							<option value="-1">--所有告警--</option>
    							<option value="1">严重告警</option>
    							<option value="2">主要告警</option>
    							<option value="3">次要告警</option>
    							<option value="4">警告告警</option>
    							<option value="5">事件告警</option>
    							<option value="6">清除告警</option>
    						</select>
    					</td>
    					<td align=right nowrap>告警状态</td>
    					<td>
    						<select name="warnStatus" id="warnStatus" class=bk onchange="refreshAlarm()">
    							<option value="-1">--所有状态--</option>
    							<option value="1">未确认未清除告警</option>
    							<option value="2">已确认未清除告警</option>
    							<option value="3">未确认但已清除告警</option>
    							<option value="4">已确认并已清除告警</option>
    						</select>
    					</td>
    					<td nowrap>
							<a href="#" onclick="window.location.reload()">刷新</a>
						</td>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2 bgcolor=#999999>
					<TR><TH colspan="10">实时查询</TH></TR>
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
						total++;
						
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
						<td width="20%" class=column align=left nowrap><textarea rows="3" class="input-textarea" style="width:100%"><%=fields.get("description")%></textarea></td>
						<td width="10%" class=column align=left nowrap><font <%=style%>><%=warnAct.getWarnStatus(fields.get("status"))%></font></td>
						<td width="10%" class=column align=center nowrap>
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
					}
					else{%>
					<tr  class=column><td colspan="10">没有查询到数据</td></tr>
					<%} %>
					<tr >
						<td colspan="5"  align="left" class="green_foot"  height="6"><a href="#" onclick="confirmBatch()">批量确认</a><a href="#" onclick="cleanBatch()" >  批量清除</a><a href="#" onclick="canelConfirmBatch()">  批量取消确认</a></td>
						<td colspan="5" align="right" class="green_foot"  height="6">共返回<%=total %>条数据</td>
					</tr>
				</table>
			</td>
		</tr>
		<TR style="display:none">
			<TD>
				<IFRAME name="childFrm" ID="childFrm" SRC="" STYLE="display:none"></IFRAME>
			</TD>
		</TR>
		<tr><td></td></tr>
	</table>
	<iframe name="childFrm1" id="childFrm1" style="display:none"></iframe>
</form>

<script language="javascript">
var tmp1 = "<%=warnType%>";
var tmp2 = "<%=warnLevel%>";
var tmp3 = "<%=warnStatus%>";

if (tmp1 != "" && tmp1 != "null"){
	document.frm.warnType.value = tmp1;
}
if (tmp2 != "" && tmp2 != "null"){
	document.frm.warnLevel.value = tmp2;
}
if (tmp3 != "" && tmp3 != "null"){
	document.frm.warnStatus.value = tmp3;
}
</script>
<%@ include file="../foot.jsp"%>