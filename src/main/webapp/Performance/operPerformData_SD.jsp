<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@ page import="com.linkage.litms.common.util.StringUtils"%>
<%@ page import="com.linkage.litms.performance.GeneralOperPerf"%>

<%
	request.setCharacterEncoding("GBK");
	Cursor cursor;
	Map fields;
	//获取页面传递的参数
	String start = request.getParameter("hidstart");
	String strTime = request.getParameter("start");	
	String searchType = request.getParameter("SearchType");

	

	//根据报表类型计算时间
	int iStart = 0;
	int iEnd = 0;
	int type = Integer.parseInt(searchType);
	switch (type) {
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
	GeneralOperPerf operPM = new GeneralOperPerf(iStart, iEnd,type);
	cursor = operPM.getAllDeviceGeneralTxtData(request);
	fields = cursor.getNext();
%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<DIV id="idLayer" width="100%">
<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align=center>
	<TR>
		<TD bgcolor=#999999 width="100%">
		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outData">
			<TR>

				<TH>设备名称</TH>
				<TH>用户帐号</TH>
				<TH>oid描述</TH>
				<TH>统计时间</TH>
				<TH id="maxtitle">最大值</TH>
				<TH id="mintitle">最小值</TH>
				<TH id="valuetitle">平均值</TH>
			</TR>
			<%
				//打印数据
				if (fields != null) {
					String _unit = "";
					String _DeviceId = "";
					String _DeviceName = "";
					String _UserName = "";
					int oidType = 1;
					String _OidDesc = "";
					boolean flag = true;
					int gatherTime = 0;
					String gatherDate = "";

					String maxValue = "0.0";
					String minValue = "0.0";
					String avgValue = "0.0";

					//性能类型
					String _oidtype = "";
					String _deviceid="";

					String strClr = "";
					while (fields != null) {
						flag = true;
						_DeviceId = (String) fields.get("device_id");
						_unit = (String) fields.get("unit");
						_DeviceName = (String) fields.get("device_name");
						_UserName = (String) fields.get("username");
						oidType = Integer.parseInt((String) fields.get("oid_type"));
						_OidDesc = (String) fields.get("oid_desc");
						gatherDate = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",
								Long.parseLong((String) fields.get("gathertime")));
						if (type == 2) {
							avgValue = StringUtils.formatNumber((String) fields
									.get("oid_value"), 2);
							maxValue = " ";
							minValue = " ";
						} else if (type == 3 || type == 4) {
							avgValue = StringUtils.formatNumber((String) fields
									.get("avg_value"), 2);
							maxValue = StringUtils.formatNumber((String) fields
									.get("max_value"), 2);
							minValue = StringUtils.formatNumber((String) fields
									.get("min_value"), 2);
						}

						//同一个设备
						if(_deviceid.equals((String) fields.get("device_id")))
						{
							flag = false;
							_deviceid =(String) fields.get("device_id");
						}
						
						if (strClr.equals("#e7e7e7"))
							strClr = "#FFFFFF";
						else
							strClr = "#e7e7e7";

						//_class1 = (String) fields.get("class1");

						out.println("<tbody><TR bgcolor=" + strClr
								+ " align=center >");

						//同一设备不重复显示设备信息
						out.println("<TD>" + (flag ? _DeviceName : "") + "</TD>");
						out.println("<TD>" + _UserName + "</TD>");
						out.println("<TD align='left'>" + _OidDesc + "</TD>");
						out.println("<TD>" + gatherDate + "</TD>");
						out.println("<TD id='max' name='max' align='right'>" + maxValue + _unit
								+ "</TD>");
						out.println("<TD id='min' name='min' align='right'>" + minValue + _unit
								+ "</TD>");
						out.println("<TD align='right'>" + avgValue + _unit + "</TD>");
						out.println("</TR></tbody>");

						fields = cursor.getNext();
					}
			%>

			<%
				} else {
					out.println("<TR bgcolor=#ffffff>");
					out
							.println("<TD class=column  colspan=8 align=center  width=\"100%\">查询无数据</TD>");
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
var type = <%=type%>;
var maxtitle = document.getElementById("maxtitle");
var mintitle = document.getElementById("mintitle");
var valuetitle = document.getElementById("valuetitle");

var maxValueObjs = document.getElementsByName("max");
var minValueObjs = document.getElementsByName("min");

if(type==2){
 maxtitle.style.display="none";
 mintitle.style.display="none";
 valuetitle.innerHTML="值";
 for(var i=0;i<maxValueObjs.length;i++){
   maxValueObjs[i].style.display="none";
 }
 
 for(var j=0;j<minValueObjs.length;j++){
   minValueObjs[j].style.display="none";
 }

}else{
 maxtitle.style.display="";
 mintitle.style.display="";
 valuetitle.innerHTML="平均值";
 for(var i=0;i<maxValueObjs.length;i++){
   maxValueObjs[i].style.display="";
 }
 
 for(var j=0;j<minValueObjs.length;j++){
   minValueObjs[j].style.display="";
 }


}


if(typeof(parent.idLayerView) == "object"){
	parent.idLayerView.innerHTML = idLayer.innerHTML;
}
//-->
</SCRIPT>
