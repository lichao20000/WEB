<%@ include file="../timelater.jsp"%>
<%@ page
	import="com.linkage.litms.common.database.*,java.util.List,java.util.ArrayList"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<meta http-equiv="x-ua-compatible" content="IE=7" >
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<jsp:useBean id="ServiceActHblt" scope="request"
	class="com.linkage.litms.resource.ServiceActHblt" />
<%@ page contentType="text/html;charset=GBK"%>

<%
	List cityList = new ArrayList();
	Cursor cursor = ServiceActHblt.getCityList(request);
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
	String recentStartTime = request.getParameter("recentStartTime");
	if (null == recentStartTime || "null".equals(recentStartTime)) {
		recentStartTime = "";
	}
	String recentEndTime = request.getParameter("recentEndTime");
	if (null == recentEndTime || "null".equals(recentEndTime)) {
		recentEndTime = "";
	}
	String recent_start_Time = request.getParameter("recent_start_Time");
	if (null == recent_start_Time || "null".equals(recent_start_Time)) {
		recent_start_Time = "";
	}
	String recent_end_Time = request.getParameter("recent_end_Time");
	if (null == recent_end_Time || "null".equals(recent_end_Time)) {
		recent_end_Time = "";
	}
	// rela_dev_type_id
	String vendorId = request.getParameter("vendorId"); 
	String modelId = request.getParameter("modelId");
	String deviceTypeId = request.getParameter("deviceTypeId");
	String gw_type = request.getParameter("gw_type");
	String sqlid = request.getParameter("sqlid");
		//os_jiank
	String cityList1=ServiceActHblt.getCityId(cursor);
	String strVendorId = ServiceActHblt.getVendorHtml(vendorId,gw_type);
	String strModelId = ServiceActHblt.getModelHtml(vendorId, modelId,gw_type);
	String strDeviceTypeId = ServiceActHblt.getDeviceTypeHtml(modelId,
			deviceTypeId,gw_type);
	String is_normal=request.getParameter("is_normal");
	String isBind = request.getParameter("isBind");
	String rela_dev_type_id = request.getParameter("rela_dev_type_id");
	int count = cursor.getRecordSize() + 4;
%>

<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
function checkform(){

	var start_Time = document.form1.start_Time.value;
	var end_Time = document.form1.end_Time.value;
	var recent_start_Time = document.form1.recent_start_Time.value;
	var recent_end_Time = document.form1.recent_end_Time.value;
	if(""!=start_Time && null!=start_Time){
		document.form1.startTime.value = strTime2Second(start_Time+" 0:0:0");
	}
	if(""!=end_Time && null!=end_Time){
		document.form1.endTime.value = strTime2Second(end_Time+" 23:59:59");
	}
	if(""!=recent_start_Time && null!=recent_start_Time){
		document.form1.recentStartTime.value = strTime2Second(recent_start_Time+" 0:0:0");
	}
	if(""!=recent_end_Time && null!=recent_end_Time){
		document.form1.recentEndTime.value = strTime2Second(recent_end_Time+" 23:59:59");
	}
	var deviceTypeId = $("select[@name='deviceTypeId']").val();
	$("#trData").show();
	$("#QueryData").html("<font style='display:block; text-align:center;font-size:20px' >正在查询，请稍等....</font>");
    $("#queryButGrey").attr("disabled", true);
    var city_id= $("select[name='city_id']").val();
    if("-1"==city_id)
    {
    	$("select[name='city_id']").val("00");
    }
    var form = document.getElementById("form1");
   // form.attr("action","DeviceStatByEditionList.jsp?devicetypeid='"+deviceTypeId+"'");
    form.action="DeviceStatByEditionListHblt.jsp?devicetypeid='"+deviceTypeId+"'";
    //  var url="DeviceStatByEditionList.jsp?devicetypeid='"+ +"'";
   // var form = document.getElementById("form1");
  	form.submit();
}

function change_select(str){
	var gw_type= document.form1.gw_type.value;
	if("deviceModel"==str){
		var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
		var vendorId = $("select[@name='vendorId']").val();
		if("-1"==vendorId){
			$("select[@name='modelId']").html("<option value='-1'>==请先选择厂商==</option>");
			$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			return;
		}
		$.post(url,{
			gw_type:gw_type,
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
			gw_type:gw_type,
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
function gwShare_change(type,selectvalue){
	switch (type){
		case "city":
			var url = "<s:url value="/gtms/stb/resource/stbReport!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='city_id']"),selectvalue);
			});
			break;
		case "cityid":
			var url = "<s:url value='/gtms/stb/resource/stbReport!getCityNext.action'/>";
			var cityId = $("select[@name='city_id']").val();
			if("-1"==cityId){
				$("select[@name='citynext']").html("<option value='-1'>==请先选择属地==</option>");
				break;
			}
			$.post(url,{
				citynext:cityId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='citynext']"),selectvalue);
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
function gwShare_parseMessage(ajax,field,selectvalue){
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

$(function(){
	dyniframesize();
	gwShare_change("city","-1");
});
$(function(){
	var instArea = $('#instArea').val();
	var gw_type=$('#gw_type').val();
});

$(window).resize(function(){
	dyniframesize();
}); 
function useQuery(){
	$("#queryButGrey").attr("disabled", false);
}
function IFrameReSizeWidth(iframename) {

　　var pTar = document.getElementById(iframename);

　　if (pTar) {  //ff

　　if (pTar.contentDocument && pTar.contentDocument.body.offsetWidth) {

　　pTar.width = pTar.contentDocument.body.offsetWidth;

　　}  //ie

　　else if (pTar.Document && pTar.Document.body.scrollWidth) {

　　pTar.width = pTar.Document.body.scrollWidth;

　　}

　　}
		
　　}
</SCRIPT>
<%@ include file="../head.jsp"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<form name="form1" action="" method="post" target="dataForm" >
<input type="hidden" name="gw_type"  id="gw_type" value='<%=gw_type%>' /> 
<input type="hidden" name="isBind" value='<%=isBind%>' />
<input type="hidden" name="sqlid" id="sqlid" value='<%=sqlid%>' />
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>							
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">机顶盒资源查询报表</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD>
				<TABLE width="100%" class="querytable" align="center">
					<TR id="gwShare_tr21" >
						<TD align="right" class="title_2" width="15%" name="city" id="city">属    地</TD>
						<TD>
							<select name="city_id" id="city_id" class="bk" onchange="gwShare_change('cityid','-1')" >
								<option value="-1">==请选择==</option>
							</select>
						</TD>
						<TD align="right" class="title_2" width="15%" >下级属地</TD>
						<TD align="left" width="35%">
							<select name="citynext" class="bk" >
								<option value="-1">==请先选择属地==</option>
							</select>
						</TD>
					</TR>					
					<TR id="gwShare_tr22" >
						<TD align="right" class="title_2" width="15%">厂    商</TD>
						<TD width="35%">
							<select id="vendorId" name="vendorId"
							class="bk" onchange="change_select('deviceModel')"><%=strVendorId%></select>
						</TD>
						<TD align="right" class="title_2" width="15%">型    号</TD>
						<TD align="left" width="35%">
							<select id="modelId" name="modelId" class="bk"
							onchange="change_select('deviceType')"><%=strModelId%></select>
						</TD>
					</TR>
					<TR id="gwShare_tr22" >
						<TD align="right" class="title_2" width="15%">版    本</TD>
						<TD width="35%">
							<select name="deviceTypeId" class="bk"><%=strDeviceTypeId%></select>
						</TD>
						<TD align="right" width="15%" class="title_2">首次上报时间</TD>
							
						<TD width="35%"><input type="text"
							name="start_Time" value='<%=start_Time%>' readonly
							class=bk> <input type="hidden" name="startTime"
							value='<%=startTime%>' readonly class=bk>
							<img name="shortDateimg"
							onClick="WdatePicker({el:document.form1.start_Time,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../images/dateButton.png" width="13" height="12" border="0"
							alt="选择">&nbsp;~&nbsp; <input
							type="text" name="end_Time" value='<%=end_Time%>'
							readonly class=bk> <input type="hidden" name="endTime"
							value='<%=endTime%>' readonly class=bk>
							<img name="shortDateimg"
							onClick="WdatePicker({el:document.form1.end_Time,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../images/dateButton.png" width="13" height="12" border="0"
							alt="选择">
						</TD>
					</TR>
					<TR >
						<TD align="right" width="15%" class="title_2">最近上报时间</TD>
						<TD width="35%"><input type="text"
							name="recent_start_Time" value='<%=recent_start_Time%>' readonly
							class=bk> <input type="hidden" name="recentStartTime"
							value='<%=recentStartTime%>' readonly class=bk>
							<img name="shortDateimg"
							onClick="WdatePicker({el:document.form1.recent_start_Time,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../images/dateButton.png" width="13" height="12" border="0"
							alt="选择">&nbsp;~&nbsp; <input
							type="text" name="recent_end_Time" value='<%=recent_end_Time%>'
							readonly class=bk> <input type="hidden" name="recentEndTime"
							value='<%=recentEndTime%>' readonly class=bk>
							<img name="shortDateimg"
							onClick="WdatePicker({el:document.form1.recent_end_Time,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../images/dateButton.png" width="13" height="12" border="0"
							alt="选择">
						</TD>
					</TR>
					<tr align="right">
					<td colspan="4" class="foot" align="right">
					<div class="right">
						<input type="submit" name="btn0" id="queryButGrey" 
							value=" 查 询 " class="jianbian" onclick="javascript:checkform()">
					</div>
				</td>
			</tr>
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
		<input type="hidden" id="instArea" value="<%=LipossGlobals.getLipossProperty("InstArea.ShortName") %>">
		</TD>
	</TR>
</TABLE>
</form>
<%@ include file="../foot.jsp"%>
<%
	ServiceActHblt = null;
%>