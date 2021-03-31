<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.*,com.linkage.litms.warn.*" %>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<html>
<head>
<title>用户状态管理---线路连接状态详细信息</title>
</head>
<%
	String device_id = request.getParameter("device_id");
	String device = request.getParameter("device");
	Map resultMap = UserWarnAct.getDetailLinkStat(device_id);
	String boot_space = (String)resultMap.get("boot_space");
	String root_space = (String)resultMap.get("root_space");
	String var_space = (String)resultMap.get("var_space");
	String www_space = (String)resultMap.get("www_space");
	String db_space = (String)resultMap.get("db_space");
	String epq_num = (String)resultMap.get("epq_num");
	String free_mem = (String)resultMap.get("free_mem");
	String swap_mem = (String)resultMap.get("swap_mem");
	String reboot_remark = (String)resultMap.get("reboot_remark");
	String stop_remark = (String)resultMap.get("stop_remark");
	String restart_time = (String)resultMap.get("restart_time");
	String last_auth_time = (String)resultMap.get("last_auth_time");
	String author_code = (String)resultMap.get("author_code");
	String statusuptime = (String)resultMap.get("statusuptime");
	String cpu_used = (String)resultMap.get("cpu_used");
	String warntype = (String)resultMap.get("warntype");
	String lastoutlinetime = (String)resultMap.get("lastoutlinetime");
	
	String customer_id = "";
	String customer_name = "";
	String city_name = "";
	String customer_address = "";
	String linkphone = "";
	String linkman = "";
	Map customerMap = UserWarnAct.getCustomerinfo(device_id);
	if(customerMap != null){
		customer_id = (String)customerMap.get("customer_id");
		customer_name = (String)customerMap.get("customer_name");
		city_name = (String)customerMap.get("city_name");
		customer_address = (String)customerMap.get("customer_address");
		linkphone = (String)customerMap.get("linkphone");
		linkman = (String)customerMap.get("linkman");
	}


%>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#000000>

				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center">
						用户线路连接信息</TH>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">设备信息</td>
						<td ><%=device %></td>
						<td class=column align="left">状态上报时刻</td>
						<td ><%=statusuptime %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">CPU负载(%)</td>
						<td ><%=cpu_used %></td>
						<td class=column align="left">剩余物理内存(KB)</td>
						<td ><%=free_mem %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">已用交换内存(KB)</td>
						<td ><%=swap_mem %></td>
						<td class=column align="left">root可用空间(%)</td>
						<td ><%=root_space %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">boot可用空间(%)</td>
						<td ><%=boot_space %></td>
						<td class=column align="left">var可用空间(%)</td>
						<td ><%=var_space %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">www可用空间(%)</td>
						<td ><%=www_space %></td>
						<td class=column align="left">database可用空间(%)</td>
						<td ><%=db_space %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">微机数量</td>
						<td ><%=epq_num %></td>
						<td class=column align="left">上次重启时刻</td>
						<td ><%=restart_time %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">停止模块</td>
						<td ><%=stop_remark %></td>
						<td class=column align="left">重启模块</td>
						<td ><%=reboot_remark %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">上次授权时刻</td>
						<td ><%=last_auth_time %></td>
						<td class=column align="left">授权码</td>
						<td ><%=author_code %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">设备是否有告警</td>
						<td ><%=warntype %></td>
						<td class=column align="left">最后一次掉线时间</td>
						<td ><%=lastoutlinetime %></td>
					</tr>
					
					<TR bgcolor="#FFFFFF"><TD HEIGHT=20 colspan=4>&nbsp;</TD></TR>
					
					<TR>
						<TH colspan="4" align="center">
						用户信息</TH>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">客户ID</td>
						<td ><%=customer_id %></td>
						<td class=column align="left">客户名称</td>
						<td ><%=customer_name %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">属地</td>
						<td ><%=city_name %></td>
						<td class=column align="left">地址</td>
						<td ><%=customer_address %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">联系人</td>
						<td ><%=linkman %></td>
						<td class=column align="left">联系电话</td>
						<td ><%=linkphone %></td>
					</tr>
					
					<TR>
						<TD colspan="4" align="right" class=foot><INPUT TYPE="button"
							value=" 关 闭 " class=btn onclick="javascript:window.close()">
						&nbsp;&nbsp;</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<tr><td height="20"></td></tr>
</TABLE>
</body>
</html>
<%@ include file="../foot.jsp"%>