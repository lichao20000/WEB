<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.paramConfig.WlanObj"%>
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
	
	WlanObj wlanObj[] = paramInfoAct.getWlanObj(device_id_first);
	
%>
<html>
<body>
<SPAN ID="child">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<%
	if(null!=wlanObj){
		String temp_lan_id	    	 = "";
		String temp_lan_wlan_id 	 = "";
		String temp_gather_time      = "";
		String temp_ap_enable        = "";

		String temp_ssid         	 = "";
		String temp_enable       	 = "";
		String temp_hide         	 = "";
		String temp_beacontype  	 = "";
		String temp_radio_enable 	 = "";
		String temp_channel      	 = "";
		String temp_standard     	 = "";
		
		String temp_wep_key    		 = "";
		String temp_wpa_key   		 = "";
		
		String temp_enable_is_	     = "";
		String temp_hide_is_  	     = "";
		String temp_radio_enable_is_ = "";
		
		String temp_ApEnable = wlanObj[0].getApEnable();
		String temp_PowerLevel = wlanObj[0].getPowerLevel();
		String temp_PowerValue = wlanObj[0].getPowerValue();
		
		if("1".equals(temp_ApEnable)){
			temp_ApEnable = "开启";
		}else{
			temp_ApEnable = "未开";
		}
		
		%>
		<tr>
			<td >
				<TABLE id="tb1" border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR bgcolor="#FFFFFF">
						<TH align="center">WLAN开关：<%=temp_ApEnable%></TH>
						<TH align="center">功率级别：<%=temp_PowerLevel%></TH>
						<TH align="center">发射功率：<%=temp_PowerValue%></TH>
					</TR>
				</table>
				<TABLE width="100%" border=0 cellspacing=1 cellpadding=2 align="center">
					<tr bgcolor="#FFFFFF">
						<!--  <TH align="center" width="4%">索引</TH>-->
						<TH align="center" >LAN编号</TH>
						<TH align="center" >wan连接ID</TH>
						<TH align="center" >SSID</TH>
						<TH align="center" >是否可用</TH>
						<TH align="center" >是否隐藏</TH>
						<TH align="center" >加密安全模式</TH>
						<TH align="center" >是否广播</TH>
						<TH align="center" >操作</TH>
					</tr>
					<%
					for(int i=0;i<wlanObj.length;i++){
						
						temp_lan_id	      = wlanObj[i].getLanId();
						temp_lan_wlan_id  = wlanObj[i].getLanWlanId();
						temp_gather_time  = wlanObj[i].getGatherTime();
						temp_ap_enable    = wlanObj[i].getApEnable();
						temp_ssid         = wlanObj[i].getSsid();
						temp_enable       = wlanObj[i].getEnable();
						temp_hide         = wlanObj[i].getHide();
						temp_beacontype   = wlanObj[i].getBeacontType();
						temp_radio_enable = wlanObj[i].getRadioEnable();
						temp_channel      = wlanObj[i].getChannel();
						temp_standard     = wlanObj[i].getStandard();
						
						temp_wep_key     = wlanObj[i].getWepKey();
						temp_wpa_key     = wlanObj[i].getWpaKey();
						
						temp_enable_is_ = "";
						temp_hide_is_   = "";
						temp_radio_enable_is_   = "";
						
						if("null".equals(temp_lan_id) || null==temp_lan_id){
							temp_lan_id = "";
						}
						if("null".equals(temp_lan_wlan_id) || null==temp_lan_wlan_id){
							temp_lan_wlan_id = "";
						}
						if("null".equals(temp_gather_time) || null==temp_gather_time){
							temp_gather_time = "";
						}
						if("null".equals(temp_ap_enable) || null==temp_ap_enable){
							temp_ap_enable = "";
						}
						if("null".equals(temp_ssid) || null==temp_ssid){
							temp_ssid = "";
						}
						if("null".equals(temp_enable) || null==temp_enable){
							temp_enable = "";
						}else{
							if("1".equals(temp_enable)){
								temp_enable_is_ = "可用";
							}else{
								temp_enable_is_ = "不可用";
							}
						}
						if("null".equals(temp_hide) || null==temp_hide){
							temp_hide = "";
						}else{
							if("1".equals(temp_hide)){
								temp_hide_is_ = "是";
							}else{
								temp_hide_is_ = "否";
							}
						}
						if("null".equals(temp_beacontype) || null==temp_beacontype){
							temp_beacontype = "";
						}
						if("null".equals(temp_radio_enable) || null==temp_radio_enable){
							temp_radio_enable = "";
						}else{
							if("1".equals(temp_radio_enable)){
								temp_radio_enable_is_ = "是";
							}else{
								temp_radio_enable_is_ = "否";
							}
						}
						if("null".equals(temp_channel) || null==temp_channel){
							temp_channel = "";
						}
						if("null".equals(temp_standard) || null==temp_standard){
							temp_standard = "";
						}
						if("null".equals(temp_wep_key) || null==temp_wep_key){
							temp_wep_key = "";
						}
						if("null".equals(temp_wpa_key) || null==temp_wpa_key){
							temp_wpa_key = "";
						}
					
					%>
					<tr  bgcolor="#FFFFFF">
						<td align="center" ><%=temp_lan_id%></td>
						<td align="center" ><%=temp_lan_wlan_id%></td>
						<td align="center" ><%=temp_ssid%></td>
						<td align="center" ><%=temp_enable_is_%></td>
						<td align="center" ><%=temp_hide_is_%></td>
						<td align="center" ><%=temp_beacontype%></td>
						<td align="center" ><%=temp_radio_enable_is_%></td>
						<td align="center" >
							<label style="cursor:hand;"
							onclick="edit('<%=device_id_first%>','<%=temp_lan_id%>','<%=temp_lan_wlan_id%>','<%=temp_gather_time%>','<%=temp_ap_enable%>','<%=temp_PowerLevel%>','<%=temp_enable%>','<%=temp_ssid%>','<%=temp_hide%>','<%=temp_beacontype%>','<%=temp_wep_key%>','<%=temp_wpa_key%>')">编辑</label>|
							<label style="cursor:hand;"
							 onclick="delWlan_conn(this,'<%=device_id_first%>|<%=temp_lan_id%>|<%=temp_lan_wlan_id%>')">删除</label>
						</td>
					</tr>
					<% } %>
					<tr  bgcolor="#FFFFFF">
						<td align="right" colspan="8"><label style="cursor:hand;"
							 onclick="add('<%=device_id_first%>')">新增</label></td>
					</tr>
				</table>
			</td>
		</tr>
		<%
		}else{
		%>
		<tr bgcolor="#FFFFFF"><td colspan="4">WLAN节点不存在</td></tr>
		<%
	}
	paramInfoAct = null;
%>

</TABLE>
</SPAN>
<SCRIPT LANGUAGE="JavaScript">
parent.document.all("div_WLAN_PARAM").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>