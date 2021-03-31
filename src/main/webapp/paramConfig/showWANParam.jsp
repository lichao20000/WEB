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
	if("true".equals(flag_boolean)){
		flag_boolean_ = true;
	}else{
		flag_boolean_= false;
	}
	ParamInfoAct paramInfoAct = new ParamInfoAct(flag_boolean_);
	
	String device_id_first = request.getParameter("device_id");
	String[] str_WAN = paramInfoAct.getWAN(device_id_first);
	
	String pvc_type = "";
%>
<html>
<body>
<SPAN ID="child">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<%
	if(null!=str_WAN){
		for(int i=0;i<str_WAN.length;i++){
			%>
			<tr>
				<td >
					<TABLE id="tb1" border=0 cellspacing=1 cellpadding=2 width="100%">
						<TR bgcolor="#FFFFFF">
							<TH align="center">
								WAN：<%=str_WAN[i]%>
							</TH>
						</TR>
					</table>
				
					<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
						<tr bgcolor="#FFFFFF">
							<TH align="center" width="4%">
								索引
							</TH>
							<TH align="center" width="10%">
								VPI/VCI
							</TH>
							<TH align="center" width="10%">
								VLAN
							</TH>
							<TH align="center" width="6%">
								操作
							</TH>
							<TH align="center" width="70%"  bgcolor=#999999>
								<TABLE id="tb1" border=0 cellspacing=1 cellpadding=1 width="100%">
									<tr bgcolor="#FFFFFF">
										<TH align="center" width="8%">
											索引
										</TH>
										<TH align="center" width="28%">
											连接名称
										</TH>
										<TH align="center" width="16%">
											连接类型
										</TH>
										<TH align="center" width="16%">
											服务类型
										</TH>
										<TH align="center" width="12%">
											是否可用
										</TH>
										<TH align="center" width="20%">
											操作
										</TH>
									</tr>
								</TABLE>
							</TH>
						</tr>
			<%
			PvcObj pvcObj[] = paramInfoAct.getPvcObj(device_id_first,str_WAN[i]);
			if(null!=pvcObj){
				for(int j=0;j<pvcObj.length;j++){
					String temp_vpi_id = pvcObj[j].getVpi_id();
					String temp_vlan_id = pvcObj[j].getVlan_id();
					String temp_vci_id = pvcObj[j].getVci_id();
					String temp_wan_conn_id = pvcObj[j].getWan_conn_id();
					String temp_ppp_conn_num = pvcObj[j].getPpp_conn_num();
					String temp_ip_conn_num = pvcObj[j].getIp_conn_num();
					if(null==temp_vpi_id||"null".equals(temp_vpi_id)){
						temp_vpi_id = "";
					}
					if(null==temp_vlan_id||"null".equals(temp_vlan_id)){
						temp_vlan_id = "";
					}
					if(null==temp_vci_id||"null".equals(temp_vci_id)){
						temp_vci_id = "";
					}
					
					String temp_vpi_id_vci_id = temp_vpi_id +"/" + temp_vci_id;
					String div_WAN_PARAM_COON = "div_WAN_PARAM_COON" + str_WAN[i] + j;
					
					if(!("".equals(temp_vpi_id)&&"".equals(temp_vci_id))){
						pvc_type = "vpi/vci";
					}else if(!"".equals(temp_vlan_id)){
						pvc_type = "vlan_id";
					}else {
						pvc_type = "null";
					}
					
					%>
					<tr  bgcolor="#FFFFFF">
						<td width="4%" align="center" ><%=temp_wan_conn_id%></td>
						<td width="10%" align="center" ><%=temp_vpi_id_vci_id%></td>
						<td width="10%" align="center" ><%=temp_vlan_id%></td>
						<td width="16%" align="center" >
						<label style="cursor:hand;" onclick="delWan_conn(this,'<%=device_id_first%>|<%=str_WAN[i]%>|<%=temp_wan_conn_id%>')">删除</label>|
						<label style="cursor:hand;"
							 onclick="getPvc_conn('<%=device_id_first%>','<%=str_WAN[i]%>','<%=temp_wan_conn_id%>','<%=div_WAN_PARAM_COON%>','<%=temp_ppp_conn_num%>','<%=temp_ip_conn_num%>')">获取连接</label>
						</td>
						<td width="60%">
							<div id="<%=div_WAN_PARAM_COON%>">
								<span>点击左边【获取连接】查询</span>
							</div>
						</td>
					</tr>
					<%
				}
			}else{
				%>
				<tr bgcolor="#FFFFFF"><td colspan="4">pvc获取失败</td></tr>
				<%
			}
			%>
				<tr bgcolor="#FFFFFF">
					<td colspan="5" align="right">
						<label style="cursor:hand;" onclick="add('<%=device_id_first%>','<%=pvc_type%>','<%=str_WAN[i]%>')">新增</label>
					</td>
				</tr>
			</table>
			</td>
			</tr>
			<%
		}
	}else{
		%>
		<tr bgcolor="#FFFFFF"><td colspan="4">WAN节点不存在</td></tr>
		<%
	}
	
	paramInfoAct = null;
%>

</TABLE>
</SPAN>
<SCRIPT LANGUAGE="JavaScript">

parent.document.all("div_WAN_PARAM").innerHTML = child.innerHTML;

</SCRIPT>
</body>
</html>