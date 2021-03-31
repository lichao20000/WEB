<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.*"%>

<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />

<%
	request.setCharacterEncoding("GBK");

	String userGather_ID = "-1";
	String curCity_ID = curUser.getCityId();
	Cursor cursorTmp = DataSetBean
			.getCursor("select gather_id,descr from tab_process_desc where city_id = '"
					+ curCity_ID + "'");
	Map fieldTmp = cursorTmp.getNext();
	if (fieldTmp != null) {
		userGather_ID = (String) fieldTmp.get("gather_id");
	}

	String curGather_ID = request.getParameter("gather_id");
	if (curGather_ID == null) {
		curGather_ID = userGather_ID;
	}

	String add_gather_id = DeviceAct.getGatherList(session,
			userGather_ID, "add_gather_id", false);

	String stroffset = request.getParameter("offset");
	String strData = "";
	int offset;
	int pagelen = 15;

	if (stroffset == null)
		offset = 1;
	else
		offset = Integer.parseInt(stroffset);

	String sql = "select a.gather_id as gather_id,descr,time from gw_pm_config_time  a left join tab_process_desc b on a.gather_id=b.gather_id order by a.gather_id";
	QueryPage qryp = new QueryPage();
	qryp.initPage(sql, offset, pagelen);
	String strBar = qryp.getPageBar();
	Cursor cursor = DataSetBean.getCursor(sql, offset, pagelen);
	Map fields = cursor.getNext();

	if (fields == null) {
		strData = "<TR><TD CLASS=column COLSPAN=3 HEIGHT=30 align=center>系统没有此采集点的业务性能采集时间</TD></TR>";
	} else {
		while (fields != null) {

			strData += "<TR>";

			strData += "<TD CLASS=column align=center>"
					+ (String) fields.get("descr") + "</TD>";
			strData += "<TD CLASS=column align=center>"
					+ (String) fields.get("time") + "</TD>";
			strData += "<TD align=center CLASS=column> <A HREF=updateOperPerformForm.jsp?gather_name="
					+ (String) fields.get("descr")
					+ "&gather_id="
					+ (String) fields.get("gather_id")
					+ " onclick='return Edit(this.href);'>编辑</A> | <A HREF=operPerformSave.jsp?action=delete&gather_id="
					+ (String) fields.get("gather_id")
					+ " onclick='return delWarn(this.href);'>删除</A></TD>";
			strData += "</TR>";
			fields = cursor.getNext();
		}
		strData += "<TR BGCOLOR=#FFFFFF><TD COLSPAN=5 align=right>"
				+ strBar + "</TD></TR>";

	}

	//clear
	fields = null;
	cursor = null;
	strBar = null;
%>

<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" src="../Js/CheckForm.js"></SCRIPT>
<style>
span {
	position: static;
	border: 0;
}
</style>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<tr>
		<td>
		<table width="98%" height="30" border="0" cellspacing="0"
			cellpadding="2" class="green_gargtd" align="center">
			<tr>
				<td width="162" align="center" class="title_bigwhite">业务性能</td>
			</tr>
		</table>
		</td>
	</tr>
	<TR>
		<TD>
		<FORM NAME="frm" METHOD="post" ACTION="operPerformSave.jsp"
			target="childFrm">
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan=3>业务性能配置</TH>
					</TR>
					<TR>
						<TD bgcolor="#ffffff" align=right colspan=3><a href="#"
							onClick="document.location.reload()" class="css3">刷新</a>&nbsp;&nbsp;<A
							HREF='javascript:Add();'>增加</A>&nbsp;</TD>
					</TR>
					<TR>
						<TH>采集点</TH>
						<TH>采集时间</TH>
						<TH>操作</TH>
					</TR>
					<%=strData%>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		<BR>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center"><SPAN id="actLabel">添加</SPAN><SPAN
							id="hostLabel"></SPAN>业务性能配置</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">采集点：</TD>
						<TD class=column><SPAN id="addgatherLabel"
							name="addgatherLabel" style="display: none1"><%=add_gather_id%></SPAN><SPAN
							id="gatherLabel" style="display: none"></SPAN><input
							type="hidden" name="hid_gather_id" value="">&nbsp;<font
							color="#FF0000">*</font></TD>
						<TD class=column align="right">采集时间：</TD>
						<TD class=column><INPUT TYPE="text" NAME="gather_time_h"
							maxlength=2 class=bk size=2>:<INPUT TYPE="text"
							NAME="gather_time_m" maxlength=2 class=bk size=2>&nbsp;<font
							color="#FF0000">*</font></TD>
					</TR>
					<TR class=green_foot>
						<TD colspan="4" align="right"><INPUT TYPE="button"
							value=" 保 存 " class=btn onclick="javascript:CheckForm();">&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="action" value="add"> <INPUT
							TYPE="reset" value=" 重 写 " class=btn></TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm name=childFrm SRC=""
			STYLE="display: none"></IFRAME></TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var offset = <%=offset%>;

function Add(){
	document.frm.reset();
	document.frm.action.value="add";
	actLabel.innerHTML = '添加';
	hostLabel.innerHTML = '';
	
	document.all.addgatherLabel.style.display="";
	
	document.all.gatherLabel.style.display="none";

}

function Edit(page){
	
	
	document.all("childFrm").src = page;
	
	return false;
}

function delWarn(page){
	if(confirm("真的要删除该配置吗？\n本操作所删除的不能恢复！！！")){
		document.all("childFrm").src = page;
	}
	
	return false;
}

function CheckForm(){
	var action = document.frm.action.value;
	var gather_time_h = document.frm.gather_time_h.value;
	var gather_time_m = document.frm.gather_time_m.value;
	
	if(action=="add"){
		if(document.all("add_gather_id").value==-1){
			alert("请选择采集点");
			return false;
		}
	}

	if(IsNull(gather_time_h,"采集小时为空")){
		if(!isNaN(gather_time_h)&&gather_time_h>=0&&gather_time_h<=23&&gather_time_h.indexOf(".")==-1&&gather_time_h.indexOf("-")==-1){
			if(gather_time_h.length<2){
			  document.frm.gather_time_h.value = "0"+gather_time_h;
			  //return true;
			}
		}else{
			alert("采集小时需为一个小于23的正整数");
			return false;
		}
	}else{
		return false;
	}
	
	if(IsNull(gather_time_m,"采集分钟为空")){
		if(!isNaN(gather_time_m)&&gather_time_m>=0&&gather_time_m<=59&&gather_time_m.indexOf(".")==-1&&gather_time_m.indexOf("-")==-1){
			if(gather_time_m.length<2){
			  document.frm.gather_time_m.value = "0"+gather_time_m;
			  //return true;
			}
		}else{
			alert("采集分钟需为一个小于59的正整数");
			return false;
		}
	}else{
		return false;
	}

	frm.submit();
}


function showChild(param)
{
	var page ="";
	if(param == "gather_id")
	{
		page = "?gather_id="+document.all("gather_id").value+"&offset="+offset+"&refresh="+Math.random();
		location.href=page;
	}
	else if(param == "add_gather_id")
	{
		page = "getHostIPList.jsp?gather_id="+document.all.add_gather_id.value+"&refresh="+Math.random();
		document.all("childFrm").src = page;
	}
}
//-->
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--

//-->
</SCRIPT>