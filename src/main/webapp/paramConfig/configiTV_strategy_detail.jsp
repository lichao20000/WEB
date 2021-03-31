<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@page import="java.util.*"%>

<jsp:useBean id="StrategyBean" scope="request" class="com.linkage.litms.paramConfig.StrategyBean" />
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%@ include file="../head.jsp"%>

<%
	request.setCharacterEncoding("GBK");
	
	Map fields = StrategyBean.detailStrategyInfo(request);

	Map customerFields = StrategyBean.detailCustomerInfo(request);
	
	Map<String, String> statusMap = new HashMap<String, String>();
	statusMap.put("0","等待执行");
	statusMap.put("1","预读PVC");
	statusMap.put("2","预读绑定端口");
	statusMap.put("3","预读无线");
	statusMap.put("4","业务下发");
	statusMap.put("100","策略完成");
	
	Map<String, String> typeMap = new HashMap<String, String>();
	typeMap.put("0","立即执行");
	typeMap.put("1","第一次连到系统");
	typeMap.put("2","周期上报");
	typeMap.put("3","重新启动");
	typeMap.put("4","下次连到系统");
	
	Map<String, String> businessStatusMap = new HashMap<String, String>();
	businessStatusMap.put("0","未做");
	businessStatusMap.put("1","开通成功");
	businessStatusMap.put("-1","开通失败");
	
	Map<String, String> resultIDMap = new HashMap<String, String>();
	resultIDMap.put("0","系统未知错误");
	resultIDMap.put("1","策略执行成功");
	resultIDMap.put("-1","设备连接不上");
	resultIDMap.put("-2","设备没有响应");
	resultIDMap.put("-3","系统没有工单");
	resultIDMap.put("-4","系统没有设备");
	resultIDMap.put("-5","系统没有模板");
	resultIDMap.put("-6","设备正被操作");
	resultIDMap.put("-7","系统参数错误");
	
	String time = "";
	String start_time = "";
	String end_time = "";
	String device_serialnumber = "";
	String status = "";
	String type = "";
	String resultMsg = "";
	if (null != fields) {

		DateTimeUtil dateTimeUtil = null;
		device_serialnumber = (String) fields.get("device_serialnumber");
		status = (String) fields.get("status");

		time = (String) fields.get("time");
		start_time = (String) fields.get("start_time");
		end_time = (String) fields.get("end_time");
		
		if (time != null && !time.equals("")) {
			dateTimeUtil = new DateTimeUtil(Long.parseLong(time) * 1000);
			time = dateTimeUtil.getLongDate();
			dateTimeUtil = null;
		}
		if (start_time != null && !start_time.equals("")) {
			dateTimeUtil = new DateTimeUtil(Long.parseLong(start_time) * 1000);
			start_time = dateTimeUtil.getLongDate();
			dateTimeUtil = null;
		}
		if (end_time != null && !end_time.equals("")) {
			dateTimeUtil = new DateTimeUtil(Long.parseLong(end_time) * 1000);
			end_time = dateTimeUtil.getLongDate();
			dateTimeUtil = null;
		}
		
		if ("0".equals((String)fields.get("result_id")) && "0".equals(status)) {
			resultMsg = "";
		} else {
			resultMsg = resultIDMap.get((String)fields.get("result_id"));
		}
		
		//if ("1".equals((String)fields.get("result_id")) && "100".equals(status)) {
		//	resultMsg = "执行成功";
		//} else if ("1".equals(status)) {
		//	resultMsg = "";
		//} else {
		//	resultMsg = "执行失败";
		//}
		
	} else {
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
						<TH colspan="10">IPTV策略配置信息</TH>
					</TR>
					<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right" width="20%">业务类型：</TD>
					<TD width="30%">IPTV桥接开户</TD>
					<TD class=column align="right" width="20%">绑定端口：</TD>
					<TD width="30%">LAN2口和WLAN2口</TD>
					</TR>
					<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right" width="20%">用户宽带账号：</TD>
					<TD width="30%"><%=fields.get("username")%></TD>
					<TD class=column align="right" width="20%">设备OUI：</TD>
					<TD width="30%"><%=fields.get("oui")%></TD>
					</TR>
					<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right" width="20%">设备序列号：</TD>
					<TD width="30%"><%=device_serialnumber%></TD>
					<TD class=column align="right" width="20%">策略状态：</TD>
					<TD width="20%"><%=statusMap.get(status)%></TD>
					</TR>
					<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right" width="20%">策略执行结果：</TD>
					<TD width="30%"><%=resultMsg%></TD>
					<TD class=column align="right" width="20%">策略执行结果描述：</TD>
					<TD width="20%"><%=fields.get("result_desc")%></TD>
					</TR>
					<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right" width="20%">PVC：</TD>
					<TD width="30%">PVC:<%=customerFields.get("vpiid")%>/<%=customerFields.get("vciid")%></TD>
					<TD class=column align="right" width="20%">开通状态：</TD>
					<TD width="20%"><%=businessStatusMap.get(customerFields.get("open_status"))%></TD>
					</TR>
					<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right" width="20%">策略方式：</TD>
					<TD width="30%"><%=typeMap.get(fields.get("type"))%></TD>
					<TD class=column align="right" width="20%">定制时间：</TD>
					<TD width="30%"><%=time%></TD>
					</TR>
					<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right" width="20%">开始执行时间：</TD>
					<TD width="30%"><%=start_time%></TD>
					<TD class=column align="right" width="20%">结束执行时间：</TD>
					<TD width="30%"><%=end_time%></TD>
					
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>

		</TD>
	</TR>
</TABLE>

<%@ include file="../foot.jsp"%>

<%
	//DeviceAct = null;
	//CLEAR
	customerFields = null;
	fields = null;
	statusMap = null;
	typeMap = null;
	businessStatusMap = null;
%>








