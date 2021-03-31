<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.common.util.DateTimeUtil"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
request.setCharacterEncoding("GBK");
//采集点
String gatherList = DeviceAct.getGatherList(session, "", "", true);
String start_time = new DateTimeUtil().getDate();
String end_time = new DateTimeUtil().getDate();

%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var $E = document.all;
function showChild(param){
	var page = null;
	if(param == "gather_id"){//获取主机ip
		if($E("gather_id").value == -1){
			$E("divHostIP").innerHTML = "";
			return ;
		}
		page = "getHostIPList.jsp?gather_id=" + $E("gather_id").value;
	}
	
	else if(param == "hostip"){//获取实例
		return ;
		/*
		if($E("attr_id").value == -1){
			return ;
		}
		page = "getHostInstanceByHost.jsp?hostip=" + $E("hostip").value;
		page +=  "&attr_id=" + $E("attr_id").value;
		*/
	}
	
	else if(param == "attr_id"){
		SetQueryData("");
		return ;
		/*
		if($E("hostip") == null || $E("hostip").value == -1){
			return ;
		}
		page = "getHostInstanceByHost.jsp?hostip=" + $E("hostip").value;
		page +=  "&attr_id=" + $E("attr_id").value;
		*/
	}
	
	$E("childFrm").src = page;
}
function CheckForm(){
	if($E("start_time").value == "" || $E("end_time").value == ""){
		alert("请选择查询时间!");
		return false;
	}else if($E("gather_id").value == -1){
		alert("请选择采集点！");
		return false;
	}else if($E("hostip") != null && $E("hostip").value == -1){
		alert("请选择主机IP地址！");
		return false;
	}else if($E("attr_id").value == -1){
		alert("请选择主机属性！");
		return false;
	}
	/*
	else if($E("instance") != null){
		var instanceObj = $E("instance");
		if(typeof(instanceObj.length) == "undefined" && instanceObj.checked == false){
			alert("请选择主机实例!");
			return false;
		}else{
			var flag = false;
			for(var i=0;i<instanceObj.length;i++)
				if(instanceObj[i].checked) flag = true;
			if(!flag){
				alert("请选择主机实例!");
				return false;
			}
		}
	}
	*/
	return true;
}
function QueryInstance(){
	if(!CheckForm()) return ;

	var page = "QueryPerfmanceData.jsp";
	var param = "?hostip=" + $E("hostip").value;
	param += "&attr_id=" + $E("attr_id").value;
	param += "&start_time=" + $E("start_time").value + " " + $E("start_ms").value;
	param += "&end_time=" + $E("end_time").value + " " + $E("end_ms").value;
	param += "&gather_id=" + $E("gather_id").value;
	param += "&tt=" + new Date();
	$E("childFrm").src = page + param;
}
function SetQueryData(htmlData){
	$E("divQueryData").innerHTML = htmlData;
}
//-->
</SCRIPT>
<table width="100%" border="0" cellspacing="0" cellpadding="0" align=center>
  <tr><td HEIGHT=20>&nbsp;&nbsp;</td></tr>
  <TR><TD>
		<table width="95%" align=center  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">
					主机性能
				</td>
			</tr>
		</table>
	</TD></TR>
	<TR><TD>
		<TABLE border=0 cellspacing=1 cellpadding=2 width="95%" align=center valign=middle bgcolor="#999999">
			<TR>
				<TH colspan="4">
					主机性能历史查询
				</TH>
			</TR>
			<tr bgcolor="#FFFFFF">
					<td nowrap class=column align="right">
						起始时间
					</td>
					<td nowrap>
						<input type="text" name="start_time" id="start_time" value="<%=start_time%>" class=bk class="form_kuang" readOnly>
						<input type="button" value="▼" class=btn onClick="showCalendar('day',event)" name="button2">
						<input type="text" name="start_ms" id="start_ms" class=bk value="00:00:00" size="10">
					</td>
					<td class=column>
						<div align="center">结束时间</div>
					</td>
					<td nowrap>
						<input type="text" name="end_time" id="end_time"  value="<%=end_time%>" class=bk readOnly class="form_kuang">
						<input type="button" value="▼" class=btn onClick="showCalendar('day',event)" name="button2">
						<input type="text" name="end_ms" id="end_ms" class=bk  value="23:59:59" size="10">
					</td>
			</tr>	
			<tr bgcolor="#FFFFFF">
				<td nowrap class=column>
					<div align="right">采集点</div>
				</td>
				<td>
					<%=gatherList%>
				</td>
				<td nowrap class=column>
					<div align="right">主机IP</div>
				</td>
				<td>
					<div id="divHostIP"></div>
				</td>
			</tr>	
			<tr bgcolor="#FFFFFF">
					<td nowrap class=column>
						<div align="right">属性</div>
					</td>
					<td nowrap>
						<select name="attr_id" id="attr_id" onchange="showChild('attr_id')" class="bk">
							<option value=-1>==请选择==</option>
							<option value=1>  CPU  </option>
							<option value=2>内    存</option>
							<option value=3>文件系统</option>
							<option value=4>系统进程</option>
							<option value=5>系统并发数</option>
						</select>
					</td>
				<td nowrap colspan=2>&nbsp;</td>
			</tr>
			<tr bgcolor="#FFFFFF" style="display:none">
				<td nowrap class=column>
					<div align="right">实例名称</div>
				</td>
				<td colspan=3>
					<div id="divInstance"></div>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF" class=green_foot>
				<td nowrap align=right colspan=4>
					<input type=button value=" 查 询 " onclick="QueryInstance()">
				</td>
			</tr>
		</TABLE>
	
	</TD></TR>
	<TR><TD height="20">&nbsp;
</TD></TR>
	<TR><TD>
			<div id=divQueryData></div>
  </TD></TR>
</TABLE>

<BR>

<iframe id="childFrm" name="childFrm" style="display:none"></iframe>
<%@ include file="../foot.jsp"%>