<%@ include file="../timelater.jsp"%>
<%@ page
	import="com.linkage.litms.common.database.*,java.util.List,java.util.ArrayList"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<jsp:useBean id="ServiceAct" scope="request"
	class="com.linkage.litms.resource.ServiceAct" />
<%@ page contentType="text/html;charset=GBK"%>


<%
	List cityList = new ArrayList();
	Cursor cursor = ServiceAct.getCityList(request);
	String startTime = request.getParameter("startTime");
	if (null == startTime || "null".equals(startTime)) {
		startTime = "";
	}
	String endTime = request.getParameter("endTime");
	if (null == endTime || "null".equals(endTime)) {
		endTime = "";
	}
	String start_Time = request.getParameter("start_Time");
	if (null == start_Time || "null".equals(start_Time)) {
		start_Time = "";
	}
	String end_Time = request.getParameter("end_Time");
	if (null == end_Time || "null".equals(end_Time)) {
		end_Time = "";
	}
	
	// rela_dev_type_id
	String vendorId = request.getParameter("vendorId"); 
	String modelId = request.getParameter("modelId");
	String cityId = request.getParameter("cityId");
	String deviceTypeId = request.getParameter("deviceTypeId");
	String strCityId = ServiceAct.getCityHtml(request,cityId);
	String strVendorId = ServiceAct.getVendorHtml(vendorId);
	String strModelId = ServiceAct.getModelHtml(vendorId, modelId);
	String gw_type = request.getParameter("gw_type");
	String countTime = request.getParameter("countTime");
	
	int count = cursor.getRecordSize() + 4;
%>

<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

/*------------------------------------------------------------------------------
//函数名:		deviceSelect_parseMessage
//参数  :	ajax 
            	类似于XXX$XXX#XXX$XXX
            field
            	需要加载的jquery对象
//功能  :	解析ajax返回参数
//返回值:		
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
//解析查询设备型号返回值的方法
function parseMessage(ajax,field){
	var flag = true;
	if(""==ajax){
		return;
	}
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	field.html("");
	option = "<option value='-1' selected>==请选择==</option>";
	field.append(option);
	for(var i=0;i<lineData.length;i++){
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
		try{
			field.append(option);
		}catch(e){
			alert("设备型号检索失败！");
		}
	}
	if(flag){
		field.attr("value","-1");
	}
}

</SCRIPT>
<%@ include file="../head.jsp"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<form name="form1" action="DeviceStatByModelDetail.jsp"><input
	type="hidden" name="gw_type" value='<%=gw_type%>' />
	<input type="hidden" name="countTime" id="countTime" value='<%=countTime%>'/>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">按型号统计(明细)</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD>
				<TABLE width="100%" height="30" border="0" cellspacing=1
					cellpadding=2 bgcolor=#999999>
					<TR>
						<td class=column bgcolor="#FFFFFF">设备资源明细如下</td>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<%
					if(start_Time != null){
					%>
					<%=ServiceAct.getHtmlDeviceListByModel(request)%>
					<TR id="exportId">
						<TD class=column colspan="<%=count%>" height="30"><a
							href="DeviceListByModel_prt.jsp?isPrt=1&gw_type=<%=gw_type%>&countTime=<%=countTime%>&endTime=<%=endTime%>&vendorId=<%=vendorId%>&modelId=<%=modelId%>&cityId=<%=cityId%>"
							alt="导出当前页数据到Excel中">&nbsp;&nbsp;&nbsp;导出到Excel</a></TD>
					</TR>
					<%
					}
					%>
					
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</form>
<%@ include file="../foot.jsp"%>
<%
	ServiceAct = null;
%>