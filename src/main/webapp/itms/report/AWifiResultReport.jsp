<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>

<script language="JavaScript">
var gw_type = '<s:property value="gw_type"/>';
var isSoftUp = '<s:property value="isSoftUp"/>';
var awifi_type = '<s:property value="awifi_type"/>';

// 厂商
var vendorId = $.trim($("select[@name='vendor']").val());
// 型号
var deviceModelId = $.trim($("select[@name='device_model']").val());
var strategy = "batch";

function doQuery(){
	var cityId = $.trim($("select[@name='cityId']").val());
	var starttime = $.trim($("input[@name='starttime']").val());
    var endtime = $.trim($("input[@name='endtime']").val());
    
 // 厂商
    var vendorIds = $.trim($("select[@name='vendor']").val());
    // 型号
    var deviceModelIds = $.trim($("select[@name='device_model']").val());
    
    if(cityId == "-1"){
         alert("请选择属地");
         return false;
    }
    $("tr[@id='trData']").show();
    $("button[@name='button']").attr("disabled", true);
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    var url = '<s:url value='/itms/report/aWifiResult.action'/>'; 
	$.post(url,{
		cityId:cityId,
		starttime:starttime,
		endtime:endtime,
		gw_type:gw_type,
		awifi_type:awifi_type,
		vendorId:vendorIds,
		deviceModelId:deviceModelIds,
		isSoftUp:isSoftUp,
		strategy:strategy
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("button[@name='button']").attr("disabled", false);
	});
}

function countBycityId(cityId,starttime1,endtime1){
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    alert("countBycityId()"+awifi_type);
    var url = '<s:url value='/itms/report/aWifiResult.action'/>'; 
	$.post(url,{
		cityId:cityId,
		starttime1:starttime1,
		endtime1:endtime1,
		gw_type:gw_type,
		awifi_type:awifi_type,
		vendorId:vendorId,
		deviceModelId:deviceModelId,
		isSoftUp:isSoftUp,
		strategy:strategy
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

function ToExcel(cityId,starttime1,endtime1) {
	var page="<s:url value='/itms/report/aWifiResult!getExcel.action'/>?"
		+ "cityId=" + cityId
		+ "&starttime1=" + starttime1
		+ "&endtime1=" + endtime1
		+ "&isSoftUp=" + isSoftUp
		+ "&gw_type=" +gw_type
		+ "&awifi_type=" +awifi_type
		+ "&vendorId="+vendorId
		+ "&deviceModelId="+deviceModelId
		+ "&strategy="+strategy;
	document.all("childFrm").src=page;
}

function openDev(cityId,starttime1,endtime1,status,resultId,isMgr){
	var page="<s:url value='/itms/report/aWifiResult!getDev.action'/>?"
		+ "cityId=" + cityId 
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1
		+ "&status=" +status
		+ "&resultId=" +resultId
		+ "&isMgr=" +isMgr
		+ "&isSoftUp=" + isSoftUp
		+ "&gw_type=" +gw_type
		+ "&awifi_type=" +awifi_type
		+ "&vendorId="+vendorId
		+ "&deviceModelId="+deviceModelId
		+ "&strategy="+strategy;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}  

// 加载厂商
$(function(){
	gwShare_change_select("vendor",-1);
});


// COPY from \code\web\ailk-itms-web\src\main\webapp\itms\resource\DeviceType_Info_util.jsp
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
function gwShare_change_select(type,selectvalue){
	switch (type){
		case "city":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value="/itms/report/aWifiResult!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendor']"),selectvalue);
				gwShare_parseMessage(ajax,$("select[@name='vendor_add']"),selectvalue);
				//$("select[@name='vendor']").html("<option value='-1'>==请先选择设备厂商==</option>");
				//$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/itms/report/aWifiResult!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendor']").val();
			if("-1"==vendorId){
				$("select[@name='device_model']").html("<option value='-1'>==请先选择设备厂商==</option>");
				//$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				//$("select[@name='device_model']").html("<option value='-1'>==请先选择设备型号==</option>");
				gwShare_parseMessage(ajax,$("select[@name='device_model']"),selectvalue);
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue);
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

</script>

<br>
<TABLE>
	<!--  
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						awifi业务开通情况统计
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						
					</td>
				</tr>
			</table>
		</td>
	</tr>
	-->
	<tr>
		<td>
			<table class="green_gargtd">
				<s:if test="awifi_type==1">
					<tr>
						<th>awifi业务开通情况统计</th>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						</td>
					</tr>
				</s:if>
				<s:elseif test="awifi_type==2">
					<tr>
						<th>校园业务开通情况统计</th>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						</td>
					</tr>
				</s:elseif>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<input type='hidden' name="awifi_type" value="1" />
				<table class="querytable">
					<tr>
						<s:if test="awifi_type==1">
							<th colspan=4>awifi业务开通情况统计</th>
						</s:if>
						<s:elseif test="awifi_type==2">
							<th colspan=4>校园网业务开通情况统计</th>
						</s:elseif>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							开始时间
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>">
							<img name="shortDateimg"
									onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="选择" />
						</td>
						<td class=column align=center width="15%">
							结束时间
						</td>
						<td>
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>">
							<img name="shortDateimg"
									onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="选择" />					
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							属 地
						</td>
						<td>
							<s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="请选择属地" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk"></s:select>
						</td>
						<TD class=column align=center width="15%">设备厂商</TD>
						<TD>
							<select name="vendor" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
							</select>
						</TD>
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD class=column align=center width="15%">设备型号</TD>
						<TD  colspan="3">
							<select name="device_model" class="bk"><option value="-1">==请选择厂商==</option></select>
						</TD>
					</TR>
					
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()" name="button">&nbsp;统 计&nbsp;</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				正在统计，请稍等....
			</div>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
