<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="DeviceCountByEditionACT" scope="request"
	class="com.linkage.module.gtms.stb.report.act.DeviceCountByEditionACT" />

<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<%
	String cityId = request.getParameter("cityId");
	String vendorId = request.getParameter("vendorId");
	if(vendorId==null||"".equals(vendorId)){
		vendorId ="-1";
	}
	String modelId = request.getParameter("modelId");
	if(modelId==null||"".equals(modelId)){
		modelId ="-1";
	}
	String deviceTypeId = request.getParameter("deviceTypeId");
	if(deviceTypeId==null||"".equals(deviceTypeId)){
		deviceTypeId ="-1";
	}
	String queryFlag = request.getParameter("queryFlag");
%>
<SCRIPT LANGUAGE="JavaScript">
var isQuery = 0 ;
function detail(city_id,vendor_id,deviceModel_id,devicetype_id, gw_type,noChildCity){
	var page="<s:url value='/gtms/stb/resource/gwDeviceQueryStb!queryDeviceList.action'/>?cityId="+city_id+"&devicetypeId="+devicetype_id + "&queryType=2&noChildCity="+noChildCity
	+"&vendorId="+vendor_id
	+"&deviceModelId="+deviceModel_id;
	//alert(page);
	window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}


$(function(){
	deviceSelect_change_select("city","<%=cityId%>");
	var vendorId = "<%=vendorId%>";
	deviceSelect_change_select("vendor",vendorId);
	if(vendorId!="-1"){
		var modelId = "<%=modelId%>";
		setTimeout('deviceSelect_change_select("deviceModel","<%=modelId%>")', 1000);
		//deviceSelect_change_select("deviceModel",modelId);
		if(modelId!="-1"){
			setTimeout('deviceSelect_change_select("devicetype","<%=deviceTypeId%>")', 2000);
		}
	}
	
});

/*------------------------------------------------------------------------------
//函数名:		deviceSelect_change_select
//参数  :	type 
	            vendor      加载设备厂商
	            deviceModel 加载设备型号
	            devicetype  加载设备版本
//功能  :	加载页面项（厂商、型号级联）
//返回值:		
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function deviceSelect_change_select(type,selectvalue){
	switch (type){
		case "city":
			var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!getCityNextChild.action'/>";
			$.post(url,{
			},function(ajax){
				deviceSelect_parseMessage(ajax,$("select[@name='cityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!getVendor.action'/>";
			$.post(url,{
			},function(ajax){
				deviceSelect_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
				$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!getDeviceModel.action'/>";
			var vendorId = $("select[@name='vendorId']").val();
			$.post(url,{
				vendorId:vendorId
			},function(ajax){
				deviceSelect_parseMessage(ajax,$("select[@name='modelId']"),selectvalue);
				$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!getDevicetype.action'/>";
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='modelId']").val();
			$.post(url,{
				vendorId:vendorId,
				deviceModelId:deviceModelId
			},function(ajax){
				deviceSelect_parseMessage(ajax,$("select[@name='deviceTypeId']"),selectvalue);
			});
			break;
		default:
			alert("未知查询选项！");
			break;
	}
}

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
function deviceSelect_parseMessage(ajax,field,selectvalue){
	var flag = true;
	if(""==ajax){
		return;
	}
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	field.html("");	
	if(selectvalue=="-1"){
		field.append("<option value='-1' selected>==请选择==</option>");
	}else{
		field.append("<option value='-1'>==请选择==</option>");
	}
	for(var i=0;i<lineData.length;i++){
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if(selectvalue==xValue){
			flag = false;
			//根据每组value和text标记的值创建一个option对象
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
		}else{
			//根据每组value和text标记的值创建一个option对象
			option = "<option value='"+xValue+"'>=="+xText+"==</option>";
		}
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

function strTime2Second(dateStr){
	
	var temp = dateStr.split(' ')
	var date = temp[0].split('-');   
    var time = temp[1].split(':'); 
	
	var reqReplyDate = new Date();
	reqReplyDate.setYear(date[0]);
    reqReplyDate.setMonth(date[1]-1);
    reqReplyDate.setDate(date[2]);
    reqReplyDate.setHours(time[0]);
    reqReplyDate.setMinutes(time[1]);
    reqReplyDate.setSeconds(time[2]);

	return Math.floor(reqReplyDate.getTime()/1000);
}

//-->
</SCRIPT>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			您当前的位置：按版本统计
		</TD>
	</TR>
</TABLE>
<form name="form1" action="DeviceCountByEdition.jsp" method="post">
	<TABLE width="98%" class="querytable" align="center">
		<TR>
			<TH class="title_1">
				按版本统计
			</TH>
		</TR>

		<TR>
			<td>
				厂商
				<select name="vendorId" class="bk"
					onchange="deviceSelect_change_select('deviceModel','-1')"
					style="width: 200px">
					<option value="-1">
						==请选择==
					</option>
				</select>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 型号
				<select name="modelId" class="bk"
					onchange="deviceSelect_change_select('devicetype','-1')"
					style="width: 200px">
					<option value="-1">
						==请先选择厂商==
					</option>
				</select>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 版本
				<select name="deviceTypeId" class="bk" " style="width: 200px">
					<option value="-1">
						==请先选择设备型号==
					</option>
				</select>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 属地
				<select name="cityId" class="bk" style="width: 100px">
					<option value="-1">
						==请选择==
					</option>
				</select>
				<input type="hidden" name="queryFlag" value="query" />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="submit" name="btn0" value=" 过 滤 " class="jianbian"
					onclick="javascript:checkform()">
			</td>
		</TR>
		<tr>
			<td height="20">
			</td>
		</tr>
		<TR>
			<TD>
				<TABLE class='listtable' width='100%'>
							<%
								if ("query".equals(queryFlag))
	{
							%>
						  	 <%=DeviceCountByEditionACT.getHtmlDeviceByEdition(request)%>
						    <%}else{}%>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</form>
