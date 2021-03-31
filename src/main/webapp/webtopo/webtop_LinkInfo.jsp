<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%
	String link_id		= request.getParameter("linkid");
	String user_id		= request.getParameter("user_id");
	String vpn_auto_id	= null;
	//如果是-1时，则vpn_auto_id不属于任何用户、用户组
	if(!user_id.equals("-1")){
	    vpn_auto_id		= user_id.split("/")[2];
	}else{
	    vpn_auto_id		= user_id;
	}
	String from_title	= request.getParameter("from");
	String to_title		= request.getParameter("to");
	VpnScheduler vpnScheduler	= new VpnScheduler();
	RemoteDB.LinkInfo linkInfo	= vpnScheduler.getLinkInfo(vpn_auto_id,link_id);
	String from_id		= null;
	String to_id		= null;
	String from_port_name	= null;
	String to_port_name		= null;
	String from_port_index	= null;
	String to_port_index	= null;
	if(linkInfo != null){
		from_id			= linkInfo.from_id;
		to_id			= linkInfo.to_id;
		from_port_name	= linkInfo.from_port_name;
		to_port_name	= linkInfo.to_port_name;
		from_port_index	= linkInfo.from_port_index;
		to_port_index	= linkInfo.to_port_index;
	}
%>

<form name="frm" action="webtop_LinkInfoSave.jsp" method="post" target="subFrm" onsubmit="return checkForm()">
	<table width="100%" border="0" cellspacing="1" cellpadding="1" class="text" align="center">
		<tr>
			<td width="30%" valign="top">
				<table width="100%" border="0" cellspacing="1" cellpadding="1" bgcolor=#999999 class="text">
					<tr bgcolor=#ffffff>
						<td colspan="3"><span>链路:<%=from_title%> 到 <%=to_title%></span></td>
					</tr>				
					<tr>
						<th width="30" nowarp>
							<div align="center">&nbsp;</div>
						</th> 
						<th width="40%">
							<div align="center">网元1(<%=from_title%>)</div>
						</th> 
						<th width="40%">
							<div align="center">网元2(<%=to_title%>)</div>
						</th>
					</tr>
					<%
						if(linkInfo != null){
					%>
					<tr bgcolor=#ffffff>
	 					<td nowrap>端口索引</td>
	 					<td><input type="text" id="ifindex1" name="from_port_index" value="<%=from_port_index%>"></td>
	 					<td><input type="text" id="ifindex2" name="to_port_index" value="<%=to_port_index%>"></td>
	 				</tr>
					<tr bgcolor=#ffffff>
	 					<td nowrap>端口名称</td>
	 					<td><input type="text" id="ifname1" name="from_port_name" value="<%=from_port_name%>"></td>
	 					<td><input type="text" id="ifname2" name="to_port_name" value="<%=to_port_name%>"></td>
	 				</tr>
	 				<tr>
	 					<td colspan="3" class="blue_foot"><div align="center"><button type="submit" class="jianbian">保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;<button type=reset class="jianbian">重置</button></div></td>
	 				</tr>
					<%
						}else{
					%>
					<tr bgcolor=#ffffff>
	 					<td nowrap colspan=3>无数据!</td>
	 				</tr>
					<%
						}
					%>
						
				</table>
			</td>
		</tr>
	</table>
	<input type="hidden" name="link_id" value="<%=link_id%>">
	<input type="hidden" name="vpn_auto_id" value="<%=vpn_auto_id%>">
	<input type="hidden" name="from_id" value="<%=from_id%>">
	<input type="hidden" name="to_id" value="<%=to_id%>">
</form>
<iframe id="subFrm" name="subFrm" src="" scrolling="no" frameborder="0" style="display:none"></iframe>
<SCRIPT LANGUAGE="JavaScript">
<!--
function checkForm(){
	if(!confirm("确定要提交吗?")){
		return false;
	}
	return true;
}
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>