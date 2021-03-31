<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/CheckForm.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
	
	var gw_type = "<%=request.getParameter("gw_type")%>";

	$(function(){
		gwShare_setGaoji();
		
		var isGoPage = '<s:property value="isGoPage"/>';
	 	if(isGoPage != 1){
	 		$("#chooseDevHtml").html("");
		}else{
			// 页面跳转
			$("#goPageForStbDeviceTraceRouteTest").show().siblings("span").html("");
		}
	});
	
	// 山西联通不显示中间设备选择页面
	function deviceInfo(url){
		$.post(url,{},function(ajax){
			$("#chooseDevHtml").html("");
	   		$("#chooseDev").html("");
	   		$("#chooseDev").append(ajax);
	   		// 页面跳转
	   		$("#goPageForStbDeviceTraceRouteTest").show().siblings("span").html("");
	   	});
	}
	
	function deviceResult(returnVal){
		for(var i=0;i<returnVal[2].length;i++){
			$("input[@name='deviceId']").val(returnVal[2][i][0]);
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[2][i][1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2][i][2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[2][i][5]);
		}
	}
	/*------------------------------------------------------------------------------
	//函数名:		queryChange
	//参数  :	change 1:简单查询、2:高级查询
	//功能  :	根据传入的参数调整显示的界面
	//返回值:		调整界面
	//说明  :	
	//描述  :	Create 2009-12-25 of By qxq
	------------------------------------------------------------------------------*/
	function queryDevice(){
		var	width=800;    
		var height=500;    
		var url="<s:url value="/gtms/stb/resource/gwDeviceQuery!queryDeviceList.action"/>?queryResultType=radio";
		var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=no;edge=sunken');    
		if(typeof(returnVal)=='undefined'){
			return;
		}else{
			$("input[@name='deviceId']").val(returnVal[0]);
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[5]);
		}
	}

function getStatus() {
	if(CheckForm()){
		var deviceId = $("input[@name='deviceId']").val();
		var maxHopCount = $("input[@name='maxHopCount']").val();
		var timeout = $("input[@name='timeout']").val();
		var hostIp =  $("input[@name='hostIp']").val();
		$('#traceRoute_btn').attr("disabled",true);
		
 		$("div[@id='div_ping']").html("正在TRACE ROUTE检测，请稍后...");
		var url = "<s:url value='/gtms/stb/diagnostic/pingInfo!traceRouteDiag.action'/>";
		$.post(url,{
			deviceId:deviceId,
			hostIp:hostIp,
			maxHopCount:maxHopCount,
			timeout:timeout,
			gw_type:gw_type
		},function(ajax){
		    $("div[@id='div_ping']").html("");
			$("div[@id='div_ping']").append(ajax);
			$('#traceRoute_btn').attr("disabled",false);
		});	
	}else{
		return false;
	}
}

function CheckForm(){
	var obj = document.frm;
	if(obj.deviceId.value==""){
		alert("请查询设备！");
		return false;
    }
	if(!IsNull(obj.hostIp.value,'测试主机')){
		obj.hostIp.focus();
		return false;
	}
	if(!IsIPAddr(obj.hostIp.value)){
		obj.hostIp.focus();
		return false;
	}
	if(!IsNull(obj.maxHopCount.value,'最大跳数')){
		obj.maxHopCount.focus();
		return false;
	}
	if(!IsNull(obj.timeout.value,'超时时间')){
		obj.timeout.focus();
		return false;
	}
	return true;					
}
function changeIP(obj)
{
	$("input[name=hostIp]").val(obj.value);
}
</SCRIPT>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			您当前的位置：TRACE ROUTE检测
		</TD>
	</TR>
</TABLE>
<TABLE width="98%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<TR >
		<td>
			<ms:inArea areaCode="sx_lt" notInMode="false">
				<%@ include file="/gtms/stb/share/gwShareDeviceQueryForSxlt.jsp"%>
			</ms:inArea>
			<ms:inArea areaCode="sx_lt" notInMode="true">
				<%@ include file="/gtms/stb/share/gwShareDeviceQuery.jsp"%>
			</ms:inArea>
		</td>
	</TR>
	<tr>
		<td id="chooseDev"></td>
	</tr>
	
	<ms:inArea areaCode="sx_lt" notInMode="false">
	<tr id="chooseDevHtml">
		<td>
			<%@ include file="/gtms/stb/share/gwShareDeviceListForSxlt.jsp"%>
		</td>
	</tr>
	</ms:inArea>
	
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="">
				<TABLE width="100%" class="querytable">
					<input type="hidden" value="" name="deviceId" />
					<TR id="trDeviceResult" style="display: none">
						<td nowrap class=title_2 width="15%">设备序列号</td>
						<td id="tdDeviceSn" width="35%"></td>
						<td nowrap class=title_2 width="15%">属地</td>
						<td id="tdDeviceCityName" width="35%"></td>
					</tr>
					<TR>
						<Td colspan="4" class="title_1">TRACE ROUTE检测</Td>
					</TR>
					<TR>
						<td nowrap class=title_2 width="15%">最大跳数</td>
						<td colspan="1" width="35%">
							<input type="text" name="maxHopCount" class="bk" value="30" />
						</td>
						<td nowrap class=title_2>超时时间(s)</td>
						<td colspan="1">
							<input type="text" name="timeout" class="bk" value="1" />
						</td>
					</TR>
					<TR>
						<td nowrap class=title_2 width="15%">目的IP</td>
						<td colspan="1" width="35%">
							<input type="text" maxlength="15" name="hostIp" class="bk"/>
							<ms:inArea areaCode="sx_lt" notInMode="false">
								<s:select onchange="changeIP(this)" list="IpMap"
										  headerKey="-1" headerValue="请选择" listKey="ip_value"
										  listValue="ip_name" cssClass="bk">
								</s:select>
							</ms:inArea>
							<ms:inArea areaCode="sx_lt" notInMode="true">
								<select onchange="changeIP(this)">
									<option value="">--请选择--</option>
									<option value="10.112.7.160">中兴ITV平台</option>
									<option value="10.112.7.160">华为ITV平台</option>
									<option value="172.0.10.172">升级服务器</option>
									<option value="10.128.0.7">网管服务器</option>
								</select>
							</ms:inArea>
						</td>
					</TR>
					<TR>
						<TD colspan="4" align="right" class="foot">
							<div align="right">
								<button type="button" id="traceRoute_btn" onclick="getStatus();">
									检 测
								</button>
							</div>
						</TD>
					</TR>
					<TR id="tr001" style="display: ">
						<TH colspan="4">操作结果</TH>
					</TR>
					<TR>
						<td colspan="4" valign="top" >
							<div id="div_ping"
								style="width:100%; height:120px; z-index:1; top: 100px;"></div>
						</td>
					</TR>
				</TABLE>
			</FORM>
		</TD>
	</TR>
</TABLE>
<script>
var gwShare_queryType = '<s:property value="gwShare_queryType"/>';
var gwShare_queryField = '<s:property value="gwShare_queryField"/>';
var gwShare_queryParam = '<s:property value="gwShare_queryParam"/>';
gwShare_queryChange(gwShare_queryType);
//参数类型
if(gwShare_queryType == 1){
	var gwShare_queryField_arr = $("input[@name='gwShare_queryField']");
	for(var i = 0; i < gwShare_queryField_arr.length; i++){
		var item = gwShare_queryField_arr[i];
		if(item.value ==  gwShare_queryField){
			item.checked = true;
		}
	}
	//参数
	$("input[@name='gwShare_queryParam']").val(gwShare_queryParam);
}
</script>