<%@ include file="../timelater.jsp"%>
<%@ page
	import="com.linkage.litms.common.database.*,java.util.List,java.util.ArrayList"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<meta http-equiv="x-ua-compatible" content="IE=7" >
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
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
	String cityList1=ServiceAct.getCityId(cursor);
	String strVendorId = ServiceAct.getVendorHtmlNXDXE8C(vendorId,gw_type);
	String strModelId = ServiceAct.getModelHtmlNXDXE8C(vendorId, modelId,gw_type);
	String strDeviceTypeId = ServiceAct.getDeviceTypeHtmlNXDXE8C(modelId,deviceTypeId,gw_type);
	String is_normal=request.getParameter("is_normal");
	String isBind = request.getParameter("isBind");
	/* JLDX-REQ-20180606-JIANGHAO6-002 增加在用情况 */
	String use_state = request.getParameter("use_state");
	
	String rela_dev_type_id = request.getParameter("rela_dev_type_id");
	int count = cursor.getRecordSize() + 4;
%>

<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<SCRIPT LANGUAGE="JavaScript">
<%-- $(function(){
	var rela_dev_type_id=<%=rela_dev_type_id%>;
	var is_normal=<%=is_normal%>; 
	var mySelect = document.getElementById("is_normal");
	var mySelect =$("#is_normal");
	if(!!mySelect){
		for (var i = 0; i < mySelect.length; i++) {
			if (mySelect.options[i].value == is_normal){
				mySelect.options[i].selected=true;   
			}
		}
	}
	$("select[@name='rela_dev_type_id']").val(rela_dev_type_id);

}); --%>

function checkform(){
	var instArea = $('#instArea').val();
	var start_Time = document.form1.start_Time.value;
	var end_Time = document.form1.end_Time.value;
	var use_state = document.form1.use_state.value;
	if(""!=start_Time && null!=start_Time){
		document.form1.startTime.value = strTime2Second(start_Time+" 0:0:0");
	}
	if(""!=end_Time && null!=end_Time){
		document.form1.endTime.value = strTime2Second(end_Time+" 23:59:59");
	}
	if("jx_dx"==instArea)
	{
		var recent_start_Time = document.form1.recent_start_Time.value;
		var recent_end_Time = document.form1.recent_end_Time.value;
		if(""!=recent_start_Time && null!=recent_start_Time){
			document.form1.recentStartTime.value = strTime2Second(recent_start_Time+" 0:0:0");
		}else{
			document.form1.recentStartTime.value="";
		}
		if(""!=recent_end_Time && null!=recent_end_Time){
			document.form1.recentEndTime.value = strTime2Second(recent_end_Time+" 23:59:59");
		}else{
			document.form1.recentEndTime.value="";
		}
	}
	var deviceTypeId = $("select[@name='deviceTypeId']").val();
	$("#trData").show();
	$("#QueryData").html("<font style='display:block; text-align:center;font-size:20px' >正在查询，请稍等....</font>");
    $("#queryButGrey").attr("disabled", true);
    var form = document.getElementById("form1");
    document.form1.action="DeviceStatByEditionListNXDXE8C.jsp?devicetypeid='"+deviceTypeId+"'";
  	document.form1.submit();
}

function change_select(str){
	var gw_type= document.form1.gw_type.value;
	if("deviceModel"==str){
		var ajax="";
		var vendorId = $("select[@name='vendorId']").val();
		if("-1"==vendorId){
			$("select[@name='modelId']").html("<option value='-1'>==请先选择厂商==</option>");
			$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			return;
		}else if("2"==vendorId){
			ajax="34$HG8120C-GPON#100$HG8145C-GPON";
		}
		else if("10"==vendorId){
			ajax="13$F660";
		}
		parseMessage(ajax,$("select[@name='modelId']"));
		$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
	}
	if("deviceType"==str){
		var url = "<s:url value='/gwms/share/gwDeviceQuery!getDevicetype.action'/>";
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

$(function(){
	dyniframesize();
});
$(function(){
	var instArea = $('#instArea').val();
	var gw_type=$('#gw_type').val();
	if("jx_dx"==instArea)
	{
	$('#isEsurfing').show();
	}
	if("jl_dx"==instArea && gw_type==4)
	{
	//$("input[@id='chbx_isMactchSQL']").css("display","");
	$('#city').css("display","");
	$("td[@id='rela_dev_type']").css("display","none");
	
	$("td[@id='start']").css("display","");
	$("td[@id='showend']").css("display","");
	$("td[@id='show']").css("display","none");
	$("td[@id='show1']").css("display","none");
	$("td[@id='show2']").css("display","");
	/* $("td[@id='normal']").css("display","none);
	$("td[@id='start']").css("display","none);
	$("td[@id='end']").css("display","none); */
	//S('#rela_dev_type').css("display","none");
	}
	if("jl_dx"==instArea && gw_type==1)
	{
	//$("input[@id='chbx_isMactchSQL']").css("display","");
	$('#city').css("display","none");
	$("td[@id='rela_dev_type']").css("display","");
	
	$("td[@id='start']").css("display","");
	$("td[@id='showend']").css("display","");
	$("td[@id='show']").css("display","");
	$("td[@id='show1']").css("display","");
	$("td[@id='show2']").css("display","");
	
	/* $("td[@id='normal']").css("display","none);
	$("td[@id='start']").css("display","none);
	$("td[@id='end']").css("display","none); */
	//S('#rela_dev_type').css("display","none");
	}
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
<form name="form1" action="" id="form1" method="post" target="dataForm" >
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
						<TD width="164" align="center" class="title_bigwhite">按版本统计</TD>
						<td></td>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD>
				<TABLE width="1150px" height="30" border="0" cellspacing=1
					cellpadding=2 >
					<TR>
						<td class=column bgcolor="#FFFFFF">厂商 <select id="vendorId" name="vendorId"
							class="bk" onchange="change_select('deviceModel')"><%=strVendorId%></select></td>
						<td class=column bgcolor="#FFFFFF">型号<select id="modelId" name="modelId" class="bk"
							onchange="change_select('deviceType')"><%=strModelId%></select>
							</td>
						<td class=column bgcolor="#FFFFFF">版本<select name="deviceTypeId" id="deviceTypeId" class="bk"><%=strDeviceTypeId%></select></td>
					<td class=column bgcolor="#FFFFFF" colspan="3" name="rela_dev_type" id="rela_dev_type">	设备类型<select name="rela_dev_type_id" class="bk"  >
							<option value="-1">==请选择==</option>
							<option value="1">e8-b</option>
							<option value="2">e8-c</option>
							<ms:inArea areaCode="jl_dx" notInMode="false">
							<option value="7">悦me</option> 
							 </ms:inArea>
						</select></td>
                    <td class=column bgcolor="#FFFFFF" style="display:none;" id="show2">	
							 在用情况<select name="use_state" id="use_state" class="bk"  >
							<option value="-1">==请选择==</option>
							<option value="0">未用</option>
							<option value="1">在用</option>
							<option value="2">全部</option>
						</select>
						</td>
					</TR>
					<TR >
					
	<!--  			 <td class=column bgcolor="#FFFFFF">接入方式<select name="access_style_relay_id" class="bk">
							<option value="-1">==请选择==</option>
							<option value="1">ADSL</option>
							<option value="2">LAN</option>
							<option value="3">EPON光纤</option>
						</select></td>
						
						-->	
						<td class=column bgcolor="#FFFFFF" id="start">开始时间 <input type="text"
							name="start_Time" value='<%=start_Time%>' size="12" readonly
							class=bk> <input type="hidden" name="startTime"
							value='<%=startTime%>' readonly class=bk> <!-- 
							<img name="shortDateimg" onclick="new WdatePicker(document.form1.start_Time,'%Y-%M-%D',true,'whyGreen')"
								src="../images/search.gif" width="15" height="12" border="0" alt="选择"/>
							 --> <img name="shortDateimg"
							onClick="WdatePicker({el:document.form1.start_Time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
							src="../images/dateButton.png" width="15" height="12" border="0"
							alt="选择" /> (YYYY-MM-DD) </td>
                            <td class=column bgcolor="#FFFFFF" id="showend">结束时间 <input
							type="text" name="end_Time" value='<%=end_Time%>' size="12"
							readonly class=bk> <input type="hidden" name="endTime"
							value='<%=endTime%>' readonly class=bk> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.form1.end_Time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
							src="../images/dateButton.png" width="15" height="12" border="0"
							alt="选择" /> (YYYY-MM-DD)
							</td>
							<td class=column bgcolor="#FFFFFF" style="display: none" name="city" id="city">属地<select name="city_id" id="city_id" class="bk"><%=cityList1%></select></td>
							 <ms:inArea areaCode="cq_dx" notInMode="true">
							 <td class=column bgcolor="#FFFFFF" colspan="1" id="show">	
							 是否正规<select name="is_normal" id="is_normal" class="bk"  >
							<option value="-1">==请选择==</option>
							<option value="1">是</option>
							<option value="0">否</option>
						</select>
						</td>
						</ms:inArea>
						<td class=column bgcolor="#FFFFFF" style="display:none;" id="isEsurfing">是否天翼网关
						<select name="is_esurfing" id="is_esurfing" class="bk">
						<option value="">==请选择==</option>
						<option value="1">天翼网关</option>
						</select>
						</td>
						<!--  <td class=column bgcolor="#FFFFFF" style="display:none;" id="show1"></td>-->
						<td class=column bgcolor="#FFFFFF" colspan="3" style="display:none;" id="show1"></td>
					
						<td class=column bgcolor="#FFFFFF" ><input type="submit" name="btn0" id="queryButGrey" 
							value=" 查 询 " class="jianbian" onclick="javascript:checkform()"></td>
						<%-- </s:if> --%>
						
					</TR>
					<ms:inArea areaCode="jx_dx" notInMode="false">
						<tr>
						<td class=column bgcolor="#FFFFFF" >是否支持IPV6
							<select name="ip_type" id="ip_type" class="bk"  >
								<option value="">==请选择==</option>
								<option value="1">是</option>
								<option value="0">否</option>
							</select>
						</td>
						<td class=column bgcolor="#FFFFFF"  >是否支持千兆
							<select name="gbbroadband" id="gbbroadband" class="bk"  >
								<option value="">==请选择==</option>
								<option value="1">是</option>
								<option value="2">否</option>
							</select>
						</td>
						<td class=column bgcolor="#FFFFFF"  >是否支持测速
							<select name="is_speedTest" id="is_speedTest" class="bk"  >
								<option value="">==请选择==</option>
								<option value="1">是</option>
								<option value="0">否</option>
							</select>
						</td>
						<td class=column bgcolor="#FFFFFF" colspan="1" >是否有效用户
							<select name="is_ableUser" id="is_ableUser" class="bk"  >
								<option value="">==请选择==</option>
								<option value="1">是</option>
								<option value="0">否</option>
							</select>
						</td>
						</tr>
						
						<tr>
						<TD class=column bgcolor="#FFFFFF" colspan="4">最近上报时间
							<input type="text" name="recent_start_Time" value='<%=recent_start_Time%>' readonly  class=bk>
							<input type="hidden" name="recentStartTime" value='<%=recentStartTime%>' readonly class=bk>
							<img name="shortDateimg" onClick="WdatePicker({el:document.form1.recent_start_Time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../images/dateButton.png" width="13" height="12" border="0" alt="选择">&nbsp;~&nbsp; 
							<input type="text" name="recent_end_Time" value='<%=recent_end_Time%>' readonly class=bk>
							<input type="hidden" name="recentEndTime" value='<%=recentEndTime%>' readonly class=bk>
							<img name="shortDateimg" onClick="WdatePicker({el:document.form1.recent_end_Time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../images/dateButton.png" width="13" height="12" border="0" alt="选择">
						</TD>
						</tr>
						
					</ms:inArea>
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
	ServiceAct = null;
%>