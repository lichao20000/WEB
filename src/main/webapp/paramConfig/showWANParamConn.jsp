<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.paramConfig.ConnObj"%>
<%@ page import="com.linkage.litms.paramConfig.PvcObj"%>
<%@ page import="com.linkage.litms.paramConfig.NodeObj"%>
<%@ page import="com.linkage.litms.paramConfig.ParamInfoAct"%>

<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<%
	request.setCharacterEncoding("GBK");
	String flag_boolean = request.getParameter("flag_boolean");
	boolean flag_boolean_ = true;
	if ("true".equals(flag_boolean)) {
		flag_boolean_ = true;
	} else {
		flag_boolean_ = false;
	}
	ParamInfoAct paramInfoAct = new ParamInfoAct(flag_boolean_);

	String device_id_first = request.getParameter("device_id");
	String wan_id_first = request.getParameter("wan_id");
	String wan_conn_id_first = request.getParameter("wan_conn_id");
	String div_flag = request.getParameter("div_flag");
	String ppp_conn_num_first = request.getParameter("ppp_conn_num");
	String ip_conn_num_first = request.getParameter("ip_conn_num");
	
	
%>
<html>
<body>
<SPAN ID="child1">
<TABLE id="tb1" border=0 cellspacing=1 cellpadding=1 width="100%">
	<%
	ConnObj connObj[] = paramInfoAct.getConnectionObj(device_id_first,wan_id_first,wan_conn_id_first,ppp_conn_num_first,ip_conn_num_first);
	
	if (null != connObj) {
		for (int k = 0; k < connObj.length; k++) {

			String device_id = "";
			String wan_id = "";
			String wan_conn_id = "";
			String wan_conn_sess_id = "";
			String sess_type = "";
			String gather_time = "";
			String enable = "";
			String name = "";
			String conn_type = "";
			String serv_list = "";
			String bind_port = "";
			String username = "";
			String password = "";
			String ip_type = "";
			String ip = "";
			String mask = "";
			String gateway = "";
			String dns_enab = "";
			String dns = "";

			String enable_temp_ = "";

			device_id = connObj[k].getDevice_id();
			wan_id = connObj[k].getWan_id();
			wan_conn_id = connObj[k].getWan_conn_id();
			wan_conn_sess_id = connObj[k].getWan_conn_sess_id();
			sess_type = connObj[k].getSess_type();
			gather_time = connObj[k].getGather_time();

			String enable_str = "enable";
			String name_str = "name";
			String conn_type_str = "conn_type";
			String serv_list_str = "serv_list";
			String bind_port_str = "bind_port";
			String username_str = "username";
			String password_str = "password";
			String ip_type_str = "ip_type";
			String ip_str = "ip";
			String mask_str = "mask";
			String gateway_str = "gateway";
			String dns_enab_str = "dns_enab";
			String dns_str = "dns";

			NodeObj[] conn_PvcObj = connObj[k].getNodeObj();

			if (null != conn_PvcObj && !"".equals(conn_PvcObj)) {
				for (int m = 0; m < conn_PvcObj.length; m++) {
					if (enable_str.equals(conn_PvcObj[m].getName())) {
						enable = conn_PvcObj[m].getValue();
					} else if (name_str.equals(conn_PvcObj[m].getName())) {
						name = conn_PvcObj[m].getValue();
					} else if (conn_type_str.equals(conn_PvcObj[m].getName())) {
						conn_type = conn_PvcObj[m].getValue();
					} else if (serv_list_str.equals(conn_PvcObj[m]
					.getName())) {
						serv_list = conn_PvcObj[m].getValue();
					} else if (bind_port_str.equals(conn_PvcObj[m].getName())) {
						bind_port = conn_PvcObj[m].getValue();
					} else if (username_str.equals(conn_PvcObj[m].getName())) {
						username = conn_PvcObj[m].getValue();
					} else if (password_str.equals(conn_PvcObj[m].getName())) {
						password = conn_PvcObj[m].getValue();
					} else if (ip_type_str.equals(conn_PvcObj[m].getName())) {
						ip_type = conn_PvcObj[m].getValue();
					} else if (ip_str.equals(conn_PvcObj[m].getName())) {
						ip = conn_PvcObj[m].getValue();
					} else if (mask_str.equals(conn_PvcObj[m].getName())) {
						mask = conn_PvcObj[m].getValue();
					} else if (gateway_str.equals(conn_PvcObj[m].getName())) {
						gateway = conn_PvcObj[m].getValue();
					} else if (dns_enab_str.equals(conn_PvcObj[m].getName())) {
						dns_enab = conn_PvcObj[m].getValue();
					} else if (dns_str.equals(conn_PvcObj[m].getName())) {
						dns = conn_PvcObj[m].getValue();
					}
				}
			}

			if ("1".equals(enable)) {
				enable_temp_ = "可用";
			} else {
				enable_temp_ = "不可用";
			}
		%>
		<tr bgcolor="#FFFFFF">
			<td width="8%" align="center"><%=wan_conn_sess_id%></td>
			<td width="28%" align="center"><%=name%></td>
			<td width="16" align="center"><%=conn_type%></td>
			<td width="16%" align="center"><%=serv_list%></td>
			<td width="12%" align="center"><%=enable_temp_%></td>
			<td align="center" width="20%">
			<label style="cursor:hand;"
				onclick="edit('<%=device_id_first%>','<%=wan_id_first%>','<%=wan_conn_id_first%>','<%=wan_conn_sess_id%>','<%=sess_type%>','<%=gather_time%>','<%=enable%>','<%=name%>','<%=conn_type%>','<%=serv_list%>','<%=bind_port%>','<%=username%>','<%=password%>','<%=ip_type%>','<%=ip%>','<%=mask%>','<%=gateway%>','<%=dns_enab%>','<%=dns%>')" >编辑</label>|
			<label style="cursor:hand;"
				onclick="delWan_conn_session(this,'<%=device_id_first%>|<%=wan_id_first%>|<%=wan_conn_id_first%>|<%=sess_type%>|<%=wan_conn_sess_id%>')">删除</label>
			</td>
		</tr>
		<%
		}
	} else {
	%>
	<tr bgcolor="#FFFFFF">
		<td colspan="6">pvc连接获取失败</td>
	</tr>
	<%
}
	
	paramInfoAct = null;
%>

</TABLE>
</SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("<%=div_flag%>").innerHTML = child1.innerHTML;
</SCRIPT>
</body>
</html>
