
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page
	import="com.linkage.litms.common.database.Cursor,com.linkage.litms.resource.FileSevice,com.linkage.litms.common.util.DateTimeUtil,com.linkage.litms.Global"%>
<%@page import="java.util.Map,java.util.List"%>

<jsp:useBean id="DeviceAct" scope="request"	class="com.linkage.litms.resource.DeviceAct" />
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript">
<!--
	function setPPPParam(index, path, device_id) {
		pppConnectionType = document.all("pppConnectionType_"+index).value;
		pppLanInterface = document.all("pppLanInterface_"+index).value;
		pppServiceList = document.all("pppServiceList_"+index).value;
		pppEnable = document.all("pppEnable_"+index).value;
		pppUsername = document.all("pppUsername_"+index).value;
		pppPassword = document.all("pppPassword_"+index).value;
		
		page="ConfigNamePwRun.jsp?path="+path+"&device_id="+device_id+"&pppConnectionType="
		+pppConnectionType+"&&pppLanInterface="+pppLanInterface+"&&pppServiceList="
		+pppServiceList+"&&pppEnable="+pppEnable+"&&pppUsername="+pppUsername
		+"&&pppPassword="+pppPassword+"&refresh="+Math.random();
      	document.all("childFrm").src = page;
	}
	
	function setIPParam(index, path, device_id) {
		ipConnectionType = document.all("ipConnectionType_"+index).value;
		ipLanInterface = document.all("ipLanInterface_"+index).value;
		ipServiceList = document.all("ipServiceList_"+index).value;
		ipEnable = document.all("ipEnable_"+index).value;
		ipAddressType = document.all("ipAddressType_"+index).value;
		ipExternalIPAddress = document.all("ipExternalIPAddress_"+index).value;
		ipSubnetMask = document.all("ipSubnetMask_"+index).value;
		ipDefaultGateway = document.all("ipDefaultGateway_"+index).value;
		ipDNSServers = document.all("ipDNSServers_"+index).value;
		
		
		page="ConfigNamePwRun2.jsp?path="+path+"&device_id="+device_id+"&ipConnectionType="
		+ipConnectionType+"&&ipLanInterface="+ipLanInterface+"&&ipServiceList="
		+ipServiceList+"&&ipEnable="+ipEnable+"&&ipAddressType="
		+ipAddressType+"&&ipExternalIPAddress="+ipExternalIPAddress
		+"&&ipSubnetMask="+ipSubnetMask+"&ipDefaultGateway="+ipDefaultGateway
		+"&ipDNSServers="+ipDNSServers+"&refresh="+Math.random();
      	document.all("childFrm1").src = page;
	}
	
	
//-->
</SCRIPT>
  
<%
	request.setCharacterEncoding("GBK");
	String device_id_ = request.getParameter("device_id");
	String PPPpath = null;
	String IPpath = null;
	
	HashMap deviceInfo= DeviceAct.getDeviceInfo(device_id_);
	String gather_id = (String)deviceInfo.get("gather_id");
	String devicetype_id = (String)deviceInfo.get("devicetype_id");
	String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
	FileSevice fs = new FileSevice();
	
	
	fs.getAllNamePw(device_id_, gather_id, ior);
	//PPPoE用户
	//Map pppoeMap = fs.getpppoeMap();
	//IP用户
	//Map ipMap = fs.getipMap();
	//Map pppoeMap = new HashMap();
	
	//Map ipMap = new HashMap();

	//ArrayList pppoeList = new ArrayList();//fs.getpppoeList();
	//ArrayList ipList = new ArrayList();//fs.getipList();
	Map pppoeMap = new HashMap();
	Map ipMap = new HashMap();
	/*
	//
	pppoeMap.put(""+0, "AAA|1.1.2.");
	pppoeMap.put(""+1, "BBB|1.1.1.");
	pppoeMap.put(""+2, "CCC|1.1.1.");
	pppoeMap.put(""+3, "DDD|1.1.1.");
	pppoeMap.put(""+4, "EEE|1.1.1.");
	pppoeMap.put(""+5, "FFF|1.1.1.");
	pppoeMap.put(""+6, "");
	pppoeMap.put(""+7, "");
	pppoeMap.put(""+8, "");
	
	
	
	//IP用户
	//
	ipMap.put(""+0, "AAA|1.1.3.");
	ipMap.put(""+1, "BBB|1.1.1.");
	ipMap.put(""+2, "CCC|1.1.1.");
	ipMap.put(""+3, "DDD|1.1.1.");
	ipMap.put(""+4, "EEE|1.1.1.");
	ipMap.put(""+5, "FFF|1.1.1.");
	ipMap.put(""+6, "GGG|1.1.1.");
	ipMap.put(""+7, "HHH|1.1.1.");
	ipMap.put(""+8, "III|1.1.1.");
	
	pppoeList.add(pppoeMap);
	
	pppoeMap2.put(""+0, "111|1.1.2.");
	pppoeMap2.put(""+1, "222|1.1.1.");
	pppoeMap2.put(""+2, "333|1.1.1.");
	pppoeMap2.put(""+3, "444|1.1.1.");
	pppoeMap2.put(""+4, "555|1.1.1.");
	pppoeMap2.put(""+5, "666|1.1.1.");
	pppoeMap2.put(""+6, "");
	pppoeMap2.put(""+7, "");
	pppoeMap2.put(""+8, "");
	pppoeList.add(pppoeMap2);
	
	ipList.add(ipMap);
	*/
	
	
	ArrayList pppoeList = fs.getpppoeList();
	ArrayList ipList = fs.getipList();
	
	
	
	String strDataPPPOE = "";
	String strDataIP = "";

	if (pppoeList.size() == 0) {
		strDataPPPOE += "<TR align=left><TD class=column colspan=11>设备无法连接或设备无此结点！</TD></TR>";
	} else {
		for (int i =0; i < pppoeList.size(); i++) {
			pppoeMap = (Map)pppoeList.get(i);
			PPPpath = ((String)pppoeMap.get(""+0)).split("\\|")[1];
			
			strDataPPPOE += "<TR align=left>";
			strDataPPPOE+= "<TD class=column>"+ (i+1) +"</TD>";
			strDataPPPOE+= "<TD class=column><input type=\"text\" size= 10 class=bk name=pppConnectionType_"+i+" value=\""+ ((String)pppoeMap.get(""+0)).split("\\|")[0] + "\"></TD>";
			strDataPPPOE+= "<TD class=column><input type=\"text\" size= 10 class=bk name=pppLanInterface_"+i+" value=\""+ ((String)pppoeMap.get(""+1)).split("\\|")[0] + "\"></TD>";
			strDataPPPOE+= "<TD class=column><input type=\"text\" size= 10 class=bk name=pppServiceList_"+i+" value=\""+ ((String)pppoeMap.get(""+2)).split("\\|")[0] + "\"></TD>";
			strDataPPPOE+= "<TD class=column><input type=\"text\" size= 10 class=bk name=pppEnable_"+i+" value=\""+ ((String)pppoeMap.get(""+3)).split("\\|")[0] + "\"></TD>";
			strDataPPPOE+= "<TD class=column><input type=\"text\" size= 10 class=bk name=pppUsername_"+i+" value=\""+ ((String)pppoeMap.get(""+4)).split("\\|")[0] + "\"></TD>";
			strDataPPPOE+= "<TD class=column><input type=\"text\" size= 10 class=bk name=pppPassword_"+i+" value=\""+ ((String)pppoeMap.get(""+5)).split("\\|")[0] + "\"></TD>";
			
			strDataPPPOE+= "<TD class=column>"+pppoeMap.get(""+6) +"</TD>";
			strDataPPPOE+= "<TD class=column>"+pppoeMap.get(""+7) +"</TD>";
			strDataPPPOE+= "<TD class=column>"+pppoeMap.get(""+8) +"</TD>";
			strDataPPPOE += "<TD class=column><input type=button value='更 新' onclick='setPPPParam("+i+",\""+PPPpath+"\",\""+device_id_+"\")'></TD>";
			strDataPPPOE += "</TR>";
		}
	}
	String temp = "";
	String temp8 = "";
	if (ipList.size() == 0) {
		strDataIP += "<TR align=left><TD class=column colspan=11>设备无法连接或设备无此结点！</TD></TR>";
	} else {
		for (int i =0; i < ipList.size(); i++) {
			ipMap = (Map)ipList.get(i);
			IPpath = ((String)ipMap.get(""+0)).split("\\|")[1];
			strDataIP += "<TR align=left>";
			strDataIP+= "<TD class=column>"+ (i+1) +"</TD>";
			if (null == ((String)ipMap.get(""+0))) {
				strDataIP+= "<TD class=column><input type=\"text\" size= 10 name=ipConnectionType_"+i+"  value=\"\"></TD>";
			} else {
				strDataIP+= "<TD class=column><input type=\"text\" size= 10 name=ipConnectionType_"+i+"  value=\""+ ((String)ipMap.get(""+0)).split("\\|")[0] + "\"></TD>";
			}
			if (null == ((String)ipMap.get(""+1))) {
				strDataIP+= "<TD class=column><input type=\"text\" size= 10 name=ipLanInterface_"+i+"  value=\"\"></TD>";
			} else {
				strDataIP+= "<TD class=column><input type=\"text\" size= 10 name=ipLanInterface_"+i+" value=\""+ ((String)ipMap.get(""+1)).split("\\|")[0] + "\"></TD>";
			}
			
			if (null == ((String)ipMap.get(""+2))) {
				strDataIP+= "<TD class=column><input type=\"text\" size= 10 name=ipServiceList_"+i+"  value=\"\"></TD>";
			} else {
				strDataIP+= "<TD class=column><input type=\"text\" size= 10 name=ipServiceList_"+i+" value=\""+ ((String)ipMap.get(""+2)).split("\\|")[0] + "\"></TD>";
			}
			
			if (null == ((String)ipMap.get(""+3))) {
				strDataIP+= "<TD class=column><input type=\"text\" size= 10 name=ipEnable_"+i+"  value=\"\"></TD>";
			} else {
				strDataIP+= "<TD class=column><input type=\"text\" size= 10 name=ipEnable_"+i+" value=\""+ ((String)ipMap.get(""+3)).split("\\|")[0] + "\"></TD>";
			}
			
			//strDataIP+= "<TD class=column><input type=\"text\" size= 10 name=ipLanInterface_"+i+" value=\"\"></TD>";
			
			
			if (null == (String)ipMap.get(""+4)) {
				temp = "";
			} else {
				temp =  (String)ipMap.get(""+4);
			}
			strDataIP+= "<TD class=column><input type=\"text\" size= 10 name=ipAddressType_"+i+" value=\""+ temp + "\"></TD>";
			strDataIP+= "<TD class=column><input type=\"text\" size= 10 name=ipExternalIPAddress_"+i+" value=\""+ ((String)ipMap.get(""+5)).split("\\|")[0] + "\"></TD>";
			strDataIP+= "<TD class=column><input type=\"text\" size= 10 name=ipSubnetMask_"+i+" value=\""+ ((String)ipMap.get(""+6)).split("\\|")[0] + "\"></TD>";
			strDataIP+= "<TD class=column><input type=\"text\" size= 10 name=ipDefaultGateway_"+i+" value=\""+ ((String)ipMap.get(""+7)).split("\\|")[0] + "\"></TD>";
			
			if (null == (String)ipMap.get(""+8)) {
				strDataIP+= "<TD class=column><input type=\"text\" size= 10 readonly name=ipDNSServers_"+i+" value=\""+ temp8 + "\"></TD>";
			} else {
				//temp8 =  (String)ipMap.get(""+8);
				strDataIP+= "<TD class=column><input type=\"text\" size= 10 readonly name=ipDNSServers_"+i+" value=\""+ ((String)ipMap.get(""+8)).split("\\|")[0] + "\"></TD>";
			}
			
			
			strDataIP += "<TD class=column><input type=button value='更 新' onclick='setIPParam("+i+",\""+IPpath+"\",\""+device_id_+"\")'></TD>";
			strDataIP += "</TR>";
		}
	}
	
	
	
	
	
	
%>

<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">

</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%"
					id="ssidTable">
					<TR>
						<TH colspan="11">PPPoE用户信息</TH>
					</TR>
					<TR>
						<TH width="2%" nowrap>编号</TH>
						<TH width="10%" nowrap>连接类型</TH>
						<TH width="10%" nowrap>接口</TH>
						<TH width="10%" nowrap>服务列表</TH>
						<TH width="10%" nowrap>是否启用</TH>
						<TH width="10%" nowrap>用户名</TH>
						<TH width="10%" nowrap>密码</TH>
						<TH width="10%" nowrap></TH>
						<TH width="10%" nowrap></TH>
						<TH width="10%" nowrap></TH>
						<TH width="10%" nowrap>操作</TH>
					</TR>
					<%=strDataPPPOE%>
					
					<TR>
						<TH colspan="11">静态IP用户信息</TH>
					</TR>
					
					<TR>
						<TH nowrap>编号</TH>
						<TH nowrap>连接类型</TH>
						<TH nowrap>接口</TH>
						<TH nowrap>服务列表</TH>
						<TH nowrap>是否启用</TH>
						<TH nowrap>地址类型</TH>
						<TH nowrap>IP</TH>
						<TH nowrap>子网掩码</TH>
						<TH nowrap>默认网关</TH>
						<TH nowrap>WANAccessType</TH>
						<TH nowrap>操作</TH>
					</TR>
					<%=strDataIP%>
					
				</TABLE>
				</TD>
			</TR>
		</TABLE>

		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm NAME="childFrm" SRC="" STYLE="display:none"></IFRAME> &nbsp;</TD>
		<TD HEIGHT=20><IFRAME ID=childFrm1 NAME="childFrm1" SRC="" STYLE="display:none"></IFRAME> &nbsp;</TD>
	</TR>
</TABLE>

<%@ include file="../foot.jsp"%>

<%
	DeviceAct = null;
	//CLEAR
	pppoeMap = null;
	ipMap = null;
%>







