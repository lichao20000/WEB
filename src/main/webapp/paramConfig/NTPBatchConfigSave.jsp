<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: 帐号增加、修改、删除操作
--%>
<html>
<%@ page import="java.util.*,com.linkage.litms.common.util.Encoder,com.linkage.litms.system.dbimpl.LogItem"%>
<jsp:useBean id="configMgr" scope="request" class="com.linkage.litms.paramConfig.ConfigureManager"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	//批量的设备ID
	String strDevice = request.getParameter("device_id");
	String[] device_list = strDevice.split(",");
	String device_id = "";
	String device_name = "";
	// 主NTP服务器地址或域名
	String main_ntp_server = request.getParameter("main_ntp_server");
	// 备NTP服务器地址或域名
	String second_ntp_server = request.getParameter("second_ntp_server");
	// 服务状态
	String status = request.getParameter("status");
	// 配置方式
	String type = request.getParameter("type");
	// 配置方式的描述
	String typeDesc = "1".equals(type) ? "tr069" : "snmp";
	// 成功率阈值
	String successpercentStr = request.getParameter("successpercent");
	int successpercent = 0;
	if("".equals(successpercentStr))
		successpercent = 0;
	else
		successpercent = Integer.parseInt(successpercentStr);
	// 重试次数(目前没有用)
	String repeatnum = request.getParameter("repeatnum");
	
	// 用来统计成功次数
	int successNum = 0;
	// 采集出来的成功率
	int curSuccessPercent = 0;
	
	String strlist = "";
	String tempStr = "";
	Map map = null;
	
	
	// 批量配置的设备数量
	int deviceNum = device_list.length;
	//------------先采集成功率------------
	
	for (int i=0;i < deviceNum;i++){
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			device_name = (String)fields.get("device_name");
			map = configMgr.getNTPStatus(device_id,type);
			String main_server = "";
			String second_server = "";
			if (null != map && 0 < map.size()) {
				successNum ++;
			}
		}
	}
	// 当前采集到的成功率
	curSuccessPercent = ( successNum * 100 ) / deviceNum;
	
	//------------------------
	// 采集到的成功率低于阈值则不进行配置,大于才走配置流程
	if(curSuccessPercent >= successpercent){
		
		
		String msg_ = "配置方式：" + typeDesc + "；" + ("1".equals(status) ? ("设置NTP状态：开启；设置主NTP服务器地址或域名：" + main_ntp_server + "；设置备NTP服务器地址或域名：" + second_ntp_server) : "设置NTP状态：并闭");
		msg_ = Encoder.toISO(msg_);
		
		strlist = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>"
					+ "<tr>"
					+ "<td bgcolor='#FFFFFF' width='30%' nowrap>"
					+ "设备名称"
					+ "</td><td bgcolor='#FFFFFF' width='70%' nowrap>"
					+ "返回结果"
					+ "</td></tr>";
		
		
		
		for (int i=0;i < deviceNum;i++){
			device_id = device_list[i];
			Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
			Map fields = cursor.getNext();
			
			if (fields != null){
				device_name = (String)fields.get("device_name");
				String result_ = "失败";
				map = configMgr.NTPConfigure(device_id,status,type,main_ntp_server,second_ntp_server);
				//Map map = null;
				//192.168.232.23
				//configMgr.testCreate("145","1.3.6.1.4.1.25506.8.22.2.1.1.1.36.172.32.42.6.3",2,"6");
				
				if(map==null || 0 == map.size()) {
					strlist += "<tr class='blue_foot'>";
					strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
					strlist += device_name;
					strlist += "</td>";
					strlist += "<td bgcolor='#FFFFFF' width='70%' nowrap>";
					strlist += "设置失败";
					strlist += "</td></tr>";
				}else{
					Set set = map.keySet();
					Iterator iterator = set.iterator();
					String name = null;
					String value = null;	
					while(iterator.hasNext()){
						name = (String)iterator.next();
						value = (String)map.get(name);
						if ((value != null && value.indexOf("配置成功") != -1) || "启用成功".equals(value) || "禁用成功".equals(value)) {
							result_ = "成功";
						}
						strlist += "<tr class='blue_foot'>";
						strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
						strlist += name;
						strlist += "</td><td bgcolor='#FFFFFF' width='70%'>";
						strlist += value;
						strlist += "</td></tr>";
						
					}
				}
				
				LogItem.getInstance().writeItemLog_other(request, 1, device_id, msg_, Encoder.toISO(result_), "NTP配置");
			
			}
		}
		
		strlist += "</table>";
	}else{
		strlist = "当前成功率 "+curSuccessPercent+"% 小于阈值 "+successpercent+"% ,不进行配置";
	}
	

%>

<body>
<SPAN ID="child"><%=strlist%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_ping").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>
