<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@ page import="com.linkage.litms.common.util.StringUtils"%>
<%@ page import="com.linkage.litms.performance.GeneralNetPerf"%>
<%@ page import="java.util.Map"%>

<%
	request.setCharacterEncoding("GBK");
	Cursor cursor;
	Map fields;
	//获取页面传递的参数
	String start = request.getParameter("hidstart");
	String hour = request.getParameter("hour");
	String expressionid = request.getParameter("expressionid");
	String device_id = request.getParameter("ip");
	String[] arr_iid = request.getParameterValues("iid");
	String sortcolumn = "avgvalue";
	String strday = request.getParameter("start");
	String stype = request.getParameter("SearchType");

	

	//根据报表类型计算时间
	int iStart = 0;
	int iEnd = 0;
	int type = Integer.parseInt(stype);
	switch (type) {
	case 1: {//小时
		int iHour = Integer.parseInt(hour);
		iStart = Integer.parseInt(start) + iHour * 3600;
		iEnd = iStart + 3600;
		break;
	}
	case 2: {//日
		iStart = Integer.parseInt(start);
		iEnd = iStart + 3600 * 24;
		break;
	}
	case 3: {//周
		iStart = Integer.parseInt(start);
		DateTimeUtil du = new DateTimeUtil(((long) iStart) * 1000);
		du = new DateTimeUtil(du.getFirstDayOfWeek("US"));
		//本周一
		iStart = Integer.valueOf(du.getLongTime() + "").intValue();
		du = new DateTimeUtil(du.getLastDayOfWeek("US"));
		//下周一
		iEnd = Integer.valueOf(du.getLongTime() + 3600 * 24 + "")
		.intValue();
		break;
	}
	case 4: {//月
		iStart = Integer.parseInt(start);
		// 获得当前月的第一天
		iStart = Integer.valueOf(
		StringUtils.getNowMonthDay(iStart) + "").intValue();
		// 获得后一个月的第一天
		iEnd = Integer.valueOf(
		(StringUtils.getNextMonthDay(iStart)) + "").intValue();
		break;
	}

	}

	//执行SQL，获取数据
	GeneralNetPerf netPM = new GeneralNetPerf(iStart, iEnd, type);
	cursor = netPM.getAllDeviceGeneralTxtData(request);
	fields = cursor.getNext();

	String _unit = "";
	if (fields != null) {
		_unit = (String) fields.get("unit");
	}
%>
<DIV  id="idLayer" width="100%">

<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
	<TR>
		<TD bgcolor=#999999 width="100%">
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outData">
				<TR>					
					<TH>
						设备信息
					</TH>
					<TH>
						性能类型
					</TH>					
					<TH>
						索引描述
					</TH>
					<TH>
						最大值(<%=_unit%>)
					</TH>
					<TH>
						最小值(<%=_unit%>)
					</TH>
					<TH>
						平均值(<%=_unit%>)
					</TH>
				</TR>
				<%
					//打印数据
					if (fields != null) {
						String _DeviceId = "";
						String _DeviceName = "";
						String _DeviceIp = "";
						String _ExpId = "";
						String _ExpDescr = "";
						boolean flag = true;

						String _IndexId = "";
						String _IndexDesc = "";
						String _Index = "";
						String _Maxvalue = "";
						String _Minvalue = "";
						String _Avgvalue = "";
						
						//性能类型
						String _class1 = "";

						String strClr = "";
						while (fields != null) {
							//开始了一个新的设备 重新读取设备信息
							if (!_DeviceId.equals(fields.get("device_id")) || !_class1.equals(fields.get("class1"))) {
								_DeviceId = (String) fields.get("device_id");
								_DeviceName = (String) fields.get("device_name");
								_DeviceIp = (String) fields.get("loopback_ip");
								_ExpId = (String) fields.get("expressionid");
								

								flag = true;
								if (strClr.equals("#e7e7e7"))
									strClr = "#FFFFFF";
								else
									strClr = "#e7e7e7";
							} else {
								flag = false;
							}
							
							_class1 = (String) fields.get("class1");
							
							_ExpDescr = (String) fields.get("descr");
							_IndexId = (String) fields.get("id");
							_Index = (String) fields.get("indexid");
							_IndexDesc = (String) fields.get("indexDesc".toLowerCase());
							_Maxvalue = StringUtils.formatNumber((String) fields
							.get("maxvalue"), 2);
							_Minvalue = StringUtils.formatNumber((String) fields
							.get("minvalue"), 2);
							_Avgvalue = StringUtils.formatNumber((String) fields
							.get("avgvalue"), 2);

							out.println("<tbody><TR bgcolor=" + strClr
							+ " align=center >");							
							//同一设备不重复显示设备信息
							out.println("<TD>" + (flag ? _DeviceIp + "/" + _DeviceName : "") + "</TD>");
							
							if (flag){
								out.println("<TD>" + _ExpDescr + "</TD>");
							}
							else{
								out.println("<TD></TD>");
							}					
							
							out.println("<TD>" + _IndexDesc + "</TD>");
							out.println("<TD>" + _Maxvalue + "</TD>");
							out.println("<TD>" + _Minvalue + "</TD>");
							out.println("<TD>" + _Avgvalue + "</TD>");
							out.println("</TR></tbody>");

							fields = cursor.getNext();
						}
				%>
				<TR class=blue_trOut onmouseout="className='blue_trOut'" bgColor=#ffffff>
					<TD colspan=6 class=green_foot align=right>
						<!-- <INPUT TYPE="button" name="selectAllBtn"
							onclick="checkAll('btn')" value=" 全选 " class="jianbian" >
						&nbsp;&nbsp;&nbsp;
						<INPUT TYPE="button" onclick="DoMrtgAct('deviceId')" value="生成MRTG图" class="jianbian"> -->
						<INPUT TYPE="button" onclick="initialize('outData',0,1)" value=" 导出数据报表 " class="jianbian">
						<input type="hidden" name="url" value="">&nbsp;&nbsp;&nbsp;
					</TD>
				</TR>
				<%
						} else {
						out.println("<TR bgcolor=#ffffff>");
						out.println("<TD class=column  colspan=6 align=center  width=\"100%\">查询无数据</TD>");
						out.println("</TR>");
					}
				%>
			</TABLE>
			
		</TD>
	</TR>
</TABLE>

</DIV>

<SCRIPT LANGUAGE="JavaScript">
<!--
if(typeof(parent.idLayerView) == "object"){
	parent.idLayerView.innerHTML = idLayer.innerHTML;
}
//-->
</SCRIPT>
