<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%@ page import="com.linkage.litms.common.database.Cursor" %>
<%@ page import="com.linkage.litms.resource.*" %>
<%@ page import ="com.linkage.litms.common.util.DateTimeUtil"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />

<%
//String strGatherList = DeviceAct.getGatherList(session, "", "",false);

DeviceAct act = new DeviceAct();
String strVendorList = act.getVendorList(false,"","vendor_id");

String start_time = new DateTimeUtil().getDate();
String end_time = new DateTimeUtil().getDate();

%>

<script language="javascript">

function DateToDes(v){
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
		pos = v.indexOf(" ");
		if(pos != -1){
			d = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}

		dt = new Date(m+"/"+d+"/"+y+" "+v);

		var s  = dt.getTime();
		return s/1000;
	}
	else
		return 0;
}

function query()
{
	if(!IsNull(document.frm.start_time.value,"起始时间")){
		document.frm.start_time.focus();
		document.frm.start_time.select();
		return false;
	}
	else if(!IsNull(document.frm.end_time.value,"结束时间")){
		document.frm.end_time.focus();
		document.frm.end_time.select();
		return false;
	}
	else{
		document.frm.hidstart.value=document.frm.start_time.value + " " + document.frm.start_ms.value;
		document.frm.hidend.value=document.frm.end_time.value + " " + document.frm.end_ms.value;
		document.frm.submit();
		return true;
	}
}
</script>
<%@ include file="../head.jsp"%>
<form name="frm" action="historyWarn_result.jsp" method="POST">
	<table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td height=20></td>
		</tr>
		<TR>
			<TD colspan="2">
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							事件管理
						</TD>
						<td><img src="../images/attention_2.gif" width="15" height="12"> 历史事件查询, 带 <font color="#FF0000">*</font> 的选项必须选择或输入</td>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2 bgcolor=#999999>
					<TR><TH colspan="4">历史查询</TR>
					<!-- <tr bgcolor=#ffffff>
						<td class=column align=right bgcolor=#ffffff>采集点</td>
						<td colspan="3">
							<%//=strGatherList %>
						</td>
					</tr> -->
					<TR BGCOLOR=#ffffff>
    					<TD class=column align=right width=13%>开始时间</TD>
      					<TD class=column nowrap width=37%>
							<input type="text" name="start_time" id="start_time" class=bk value="<%=start_time%>" readonly class="form_kuang">
							<input type="button" value="▼" class=btn onClick="showCalendar('day',event)" name="button2">
							<input type="text" name="start_ms" id="start_ms" class=bk value="00:00:00" class="form_kuang" size="8" maxlength="8">
							<input type="hidden" name="hidstart" value="">
							<font color="#FF0000">*</font>
						</TD>
     					<TD class=column align=right width=13%>结束时间
						</TD>
      					<TD nowrap>
        					<input type="text" name="end_time" id="end_time" class=bk value="<%=end_time%>" readonly class="form_kuang">
							<input type="button" value="▼" class=btn onClick="showCalendar('day',event)" name="button2">
							<input type="text" name="end_ms" id="end_ms" class=bk  value="23:59:59" class="form_kuang" size="8" maxlength="8">
							<input type="hidden" name="hidend" value="">
							<font color="#FF0000">*</font>
						</TD>
    				</TR>
    				<tr bgcolor=#ffffff>
    					<td class=column align=right>厂商选择</td>
    					<TD><%=strVendorList %></TD>
    					<td class=column align=right>设备序列号</td>
    					<td><input type="text" name="device_serialnumber" class=bk size="16" maxlength="64">  支持模糊匹配</td>
    				</tr>
    				<tr bgcolor=#ffffff class=green_foot>
    					<td align="right" colspan="4"><input type="button" onclick="query()" value=" 查 询 "></td>
    				</tr>
				</table>
			</td>
		</tr>
	</table>
</form>
<br>
<br>
<%@ include file="../foot.jsp"%>