<%@ include file="../timelater.jsp"%>
<%@ page
	import="com.linkage.litms.common.database.*,java.util.List,java.util.ArrayList"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<meta http-equiv="x-ua-compatible" content="IE=7" >
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<jsp:useBean id="ServiceActAhdx" scope="request"
	class="com.linkage.litms.resource.ServiceActAhdx" />
<%@ page contentType="text/html;charset=GBK"%>

<%
	List cityList = new ArrayList();
	Cursor cursor = ServiceActAhdx.getCityList(request);
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
	String deviceTypeId = request.getParameter("deviceTypeId");
	String gw_type = request.getParameter("gw_type");
	String strVendorId = ServiceActAhdx.getVendorHtml(vendorId,gw_type);
	String strModelId = ServiceActAhdx.getModelHtml(vendorId, modelId,gw_type);
	String strDeviceTypeId = ServiceActAhdx.getDeviceTypeHtml(modelId,
			deviceTypeId,gw_type);
	String is_normal=request.getParameter("is_normal");
	String isBind = request.getParameter("isBind");
	
	String rela_dev_type_id = request.getParameter("rela_dev_type_id");
	int count = cursor.getRecordSize() + 4;
%>

<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">
function doSubmit(){
	var start_Time = document.frm.start_Time.value;
	var end_Time = document.frm.end_Time.value;
	if(""!=start_Time && null!=start_Time){
		document.frm.startTime.value = strTime2Second(start_Time+" 0:0:0");
	}
	if(""!=end_Time && null!=end_Time){
		document.frm.endTime.value = strTime2Second(end_Time+" 23:59:59");
	}
	var deviceTypeId = $("select[@name='deviceTypeId']").val();
	$("#trData").show();
	$("#QueryData").html("<font style='display:block; text-align:center;font-size:20px' >正在查询，请稍等....</font>");
	document.frm.action="DeviceStatByEditionListAHDX.jsp?devicetypeid='"+deviceTypeId+"'";
	document.frm.submit();
}

function change_select(str){
	if("deviceModel"==str){
		var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
		var vendorId = $("select[@name='vendorId']").val();
		if("-1"==vendorId){
			$("select[@name='modelId']").html("<option value='-1'>==请先选择厂商==</option>");
			$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			return;
		}
		$.post(url,{
			gwShare_vendorId:vendorId
		},function(ajax){
			parseMessage(ajax,$("select[@name='modelId']"));
			$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
		});
	}
	if("deviceType"==str){
		var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
		var vendorId = $("select[@name='vendorId']").val();
		var deviceModelId = $("select[@name='modelId']").val();
		if("-1"==deviceModelId){
			$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			return;
		}
		$.post(url,{
			gwShare_vendorId:vendorId,
			gwShare_deviceModelId:deviceModelId
		},function(ajax){
			parseMessage(ajax,$("select[@name='deviceTypeId']"));
		});
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

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
   			dyniframe[i].style.display="block"
   			//如果用户的浏览器是NetScape
   			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
    				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
    			//如果用户的浏览器是IE
   			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
    				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
 			 }
 		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
  		tempobj.style.display="block"
		}
	}
}

$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 
</SCRIPT>
<form name="frm" action="" method="post" target="dataForm" >
<input type="hidden" name="gw_type" value='<%=gw_type%>' />
<input type="hidden" name="isBind" value='<%=isBind%>' />
	
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
						<TD width="164" align="center" class="title_bigwhite">按版本统计</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD>
				<TABLE width="100%" height="30" border="0" cellspacing=1
					cellpadding=2 bgcolor=#999999>
					<TR>
						<td class=column bgcolor="#FFFFFF">厂商 <select id="vendorId" name="vendorId"
							class="bk" onchange="change_select('deviceModel')"><%=strVendorId%></select></td>
						<td class=column bgcolor="#FFFFFF">型号<select id="modelId" name="modelId" class="bk"
							onchange="change_select('deviceType')"><%=strModelId%></select>
							</td>
						<td class=column bgcolor="#FFFFFF">版本<select name="deviceTypeId" class="bk"><%=strDeviceTypeId%></select></td>
					</TR>
					<TR>
						<td class=column bgcolor="#FFFFFF">开始时间 <input type="text"
							name="start_Time" value='<%=start_Time%>' size="12" readonly
							class=bk> <input type="hidden" name="startTime"
							value='<%=startTime%>' readonly class=bk> <!-- 
							<img name="shortDateimg" onclick="new WdatePicker(document.frm.start_Time,'%Y-%M-%D',true,'whyGreen')"
								src="../images/search.gif" width="15" height="12" border="0" alt="选择"/>
							 --> <img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.start_Time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
							src="../images/dateButton.png" width="15" height="12" border="0"
							alt="选择" /> (YYYY-MM-DD) </td>
                            <td class=column bgcolor="#FFFFFF">结束时间 <input
							type="text" name="end_Time" value='<%=end_Time%>' size="12"
							readonly class=bk> <input type="hidden" name="endTime"
							value='<%=endTime%>' readonly class=bk> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.end_Time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
							src="../images/dateButton.png" width="15" height="12" border="0"
							alt="选择" /> (YYYY-MM-DD)
							</td>
							 <td class=column bgcolor="#FFFFFF" colspan="2">	
						<input type="button" name="btn0" id="queryButGrey"
							value=" 过 滤 " class="jianbian" onclick="javascript:doSubmit()">
						</td>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<tr>
				<td height="20"></td>
			</tr>
			<tr id="trData" style="display: none">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
						正在统计，请稍等....
					</div>
				</td>
			</tr>
			<TR>
				<TD bgcolor=#999999>
					<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="yes" width="100%" src="">
					</iframe>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</form>
<%@ include file="../foot.jsp"%>
<%
ServiceActAhdx = null;
%>