<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor,com.linkage.litms.common.util.DateTimeUtil"%>
<%@page import="java.util.*"%>

<jsp:useBean id="StrategyBean" scope="request" class="com.linkage.litms.paramConfig.StrategyBean" />
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>


<%@ include file="../head.jsp"%>

<%
	request.setCharacterEncoding("GBK");
	List list = StrategyBean.searchStrategyInfo(request);
	String strBar = String.valueOf(list.get(0));
	Cursor cursor = (Cursor) list.get(1);
	Map fields = cursor.getNext();
	String strData = "";
	String resultMsg = "";
	String status_str = "";
	Map<String, String> statusMap = new HashMap<String, String>();
	statusMap.put("0","等待执行");
	statusMap.put("1","预读PVC");
	statusMap.put("2","预读绑定端口");
	statusMap.put("3","预读无线");
	statusMap.put("4","业务下发");
	statusMap.put("100","策略完成");
	
	Map<String, String> businessStatusMap = new HashMap<String, String>();
	businessStatusMap.put("0","未做");
	businessStatusMap.put("1","开通成功");
	businessStatusMap.put("-1","开通失败");
	
	//Map<String, String> resultIDMap = new HashMap<String, String>();
	//resultIDMap.put("0","系统未知错误");
	//resultIDMap.put("1","执行成功");
	//resultIDMap.put("2","执行成功");
	//resultIDMap.put("3","执行成功");
	//resultIDMap.put("4","执行成功");
	//resultIDMap.put("-1","设备连接不上");
	//resultIDMap.put("-2","设备没有响应");
	//resultIDMap.put("-3","系统没有工单");
	//resultIDMap.put("-4","系统没有设备");
	//resultIDMap.put("-5","系统没有模板");
	//resultIDMap.put("-6","设备正被操作");
	//resultIDMap.put("-7","系统参数错误");
	
	
	if (null != fields) {
		String time = null;
		String start_time = null;
		String end_time = null;
		
		DateTimeUtil dateTimeUtil = null;
		while (null != fields) {
			String device_serialnumber = (String) fields.get("device_serialnumber");
			String status = (String) fields.get("status");

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
				status_str = statusMap.get(status);
			} else if("1".equals((String)fields.get("result_id"))) {
				resultMsg = "成功";
				status_str = statusMap.get(status) + "(" + resultMsg + ")";
			} else if("2".equals((String)fields.get("result_id"))) {
				resultMsg = "";
				status_str = statusMap.get(status);
			} else {
				resultMsg = "失败";
				status_str = statusMap.get(status) + "(" + resultMsg + ")";
			}
			
			strData += "<TR align='center'>";
			strData += "<TD class=column><a href='#' onclick=\"detailMsg('"+fields.get("username")+"')\">" + fields.get("username")
			+ "</a></TD>";
			strData += "<TD class=column>" + fields.get("oui") + "</TD>";
			strData += "<TD class=column>" + device_serialnumber + "</TD>";
			strData += "<TD class=column>" + status_str +"</TD>";
			//strData += "<TD class=column>" + resultMsg + "</TD>";
			strData += "<TD class=column>" + time + "</TD>";
			strData += "<TD class=column>" + start_time + "</TD>";
			strData += "<TD class=column>" + end_time + "</TD>";

			strData += "</TR>";
			fields = cursor.getNext();
		}
		strData += "<TR><TD class=column COLSPAN=10 align=right nowrap><input type='button' value=' 刷 新 ' onclick='javascript:window.location.reload();'></TD></TR>";
		strData += "<TR><TD class=column COLSPAN=10 align=right nowrap>" + strBar + "</TD></TR>";
	} else {
		strData = "<TR align=left><TD class=column colspan=10>系统没有符合条件的设备!</TD></TR>";
	}
%>

<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
	function detailMsg(username) {
		window.showModalDialog('configiTV_strategy_detail.jsp?username='+username+"&t="+new Date().getTime(),window,'dialogHeight:500px;dialogWidth:800px');
	}
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
					<TR>
						<TH nowrap>用户宽带账号</TH>
						<TH nowrap>设备OUI</TH>
						<TH nowrap>设备序列号</TH>
						<TH nowrap>策略状态(结果)</TH>
						
						<TH nowrap>定制时间</TH>
						<TH nowrap>开始执行时间</TH>
						<TH nowrap>结束执行时间</TH>
						
					</TR>
					<%=strData%>
					
				</TABLE>
				</TD>
			</TR>
		</TABLE>

		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm NAME="childFrm" SRC=""
			STYLE="display: none"></IFRAME> &nbsp;</TD>
	</TR>
</TABLE>

<%@ include file="../foot.jsp"%>

<%
	//DeviceAct = null;
	//CLEAR
	fields = null;
	cursor = null;
%>








