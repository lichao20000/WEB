<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.*,com.linkage.litms.warn.*" %>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<html>
<head>
<title>�û�״̬����---��·����״̬��ϸ��Ϣ</title>
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
						�û���·������Ϣ</TH>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">�豸��Ϣ</td>
						<td ><%=device %></td>
						<td class=column align="left">״̬�ϱ�ʱ��</td>
						<td ><%=statusuptime %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">CPU����(%)</td>
						<td ><%=cpu_used %></td>
						<td class=column align="left">ʣ�������ڴ�(KB)</td>
						<td ><%=free_mem %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">���ý����ڴ�(KB)</td>
						<td ><%=swap_mem %></td>
						<td class=column align="left">root���ÿռ�(%)</td>
						<td ><%=root_space %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">boot���ÿռ�(%)</td>
						<td ><%=boot_space %></td>
						<td class=column align="left">var���ÿռ�(%)</td>
						<td ><%=var_space %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">www���ÿռ�(%)</td>
						<td ><%=www_space %></td>
						<td class=column align="left">database���ÿռ�(%)</td>
						<td ><%=db_space %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">΢������</td>
						<td ><%=epq_num %></td>
						<td class=column align="left">�ϴ�����ʱ��</td>
						<td ><%=restart_time %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">ֹͣģ��</td>
						<td ><%=stop_remark %></td>
						<td class=column align="left">����ģ��</td>
						<td ><%=reboot_remark %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">�ϴ���Ȩʱ��</td>
						<td ><%=last_auth_time %></td>
						<td class=column align="left">��Ȩ��</td>
						<td ><%=author_code %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">�豸�Ƿ��и澯</td>
						<td ><%=warntype %></td>
						<td class=column align="left">���һ�ε���ʱ��</td>
						<td ><%=lastoutlinetime %></td>
					</tr>
					
					<TR bgcolor="#FFFFFF"><TD HEIGHT=20 colspan=4>&nbsp;</TD></TR>
					
					<TR>
						<TH colspan="4" align="center">
						�û���Ϣ</TH>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">�ͻ�ID</td>
						<td ><%=customer_id %></td>
						<td class=column align="left">�ͻ�����</td>
						<td ><%=customer_name %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">����</td>
						<td ><%=city_name %></td>
						<td class=column align="left">��ַ</td>
						<td ><%=customer_address %></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">��ϵ��</td>
						<td ><%=linkman %></td>
						<td class=column align="left">��ϵ�绰</td>
						<td ><%=linkphone %></td>
					</tr>
					
					<TR>
						<TD colspan="4" align="right" class=foot><INPUT TYPE="button"
							value=" �� �� " class=btn onclick="javascript:window.close()">
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