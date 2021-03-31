<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>


<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>

<script type="text/javascript">

var _gw_type = "<%=request.getParameter("gw_type")%>";

function deviceResult(url){
	$("#tr_deviceInfo1").html("");
	$("tr[@id='tr_deviceInfo']").show();
	$("div[@id='div_deviceInfo']").html("正在查询，请稍等....");
	$.post(url,{},function(ajax){
   	    $("div[@id='div_deviceInfo']").html("");
   		$("div[@id='div_deviceInfo']").append(ajax);
   	});
}

function ToExcel() {
	var page="../../gwms/resource/queryDevice!getInfoExcelDevice.action?"
		+ "&starttime=" + document.frm.starttime.value
		+ "&endtime=" + document.frm.endtime.value
		+ "&timeType=" + document.frm.timeType.value
		+ "&device_serialnumber=" + $.trim(document.frm.device_serialnumber.value)
		+ "&loopback_ip=" + document.frm.loopback_ip.value;
	document.all("childFrm").src=page;
	//window.open(page);
}

function EditDevice(device_id){
	var strpage = "../../Resource/AddDeviceForm.jsp?_action=update&device_id=" + device_id;
	window.location.href=strpage;
}
function DetailDevice(device_id){
	var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

function refresh(){
	window.location.href=window.location.href;
}

function refreshDev(device_id,deviceSN,oui){
	var url = '<s:url value='/gwms/resource/refDelDev!refresh.action'/>';
	$.post(url,{
		deviceId:device_id,
		deviceSN:deviceSN,
		oui:oui
	},function(ajax){	
	    alert(ajax);
	    // frm.submit();
	});
}
//参数类型
var gwShare_queryField_arr = $("input[@name='gwShare_queryField']");
for(var i = 0; i < gwShare_queryField_arr.length; i++){
	var item = gwShare_queryField_arr[i];
	if(item.value ==  gwShare_queryField){
		item.checked = true;
	}
}

function delDev(device_id,cpe_allocatedstatus,oui,deviceSN){
	if(!confirm("真的要删除该网络设备吗？\n本操作所删除的不能恢复！！！")){
		return false;
	}
	
	var url = '<s:url value='/gwms/resource/refDelDev!delete.action'/>';
	var tmp = '<s:property value="gw_type" />'; // modify by chenjie 原来这地方写死是'1',现在根据gw_type判断
	
	$.post(url,{
		deviceId:device_id,
		cpe_allocatedstatus:cpe_allocatedstatus,
		oui:oui,
		deviceSN:deviceSN,
		gw_type:tmp 
	},function(result){
		if(result){
			if(result.indexOf('删除成功') === -1){
				alert(result);
			}else{
				var tr_deviceId = "#device_id" + device_id;
				$(tr_deviceId).hide();
			}
		}else{
			alert('删除失败:服务器异常');
		}
	});
}

</script>

<table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
	<tr>
		<td height="20"></td>
	</tr>
	<TR>
		<TD>
		<TABLE width="100%" height="30" border="0" cellspacing="0"
			cellpadding="0" class="green_gargtd">
			<TR>
				<TD width="164" align="center" class="title_bigwhite">设备资源</TD>
				<td>&nbsp; <img src="<s:url value="/images/attention_2.gif"/>"
					width="15" height="12"> &nbsp; 设备操作列表,选择时间类型确定所要查询的时间。</td>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<tr>
		<td colspan="4">
			<%@ include file="/gwms/share/gwShareDeviceQueryForSxlt.jsp"%>
		</td>
	</TR>
	<!-- 查询时显示 -->
	<tr id="tr_deviceInfo" style="display: none">
		<TD>
			<div id="div_deviceInfo" style="width: 100%; z-index: 1; top: 100px; text-align: center;">
				正在查询，请稍等....
			</div>
		</td>
	</tr>
	<!-- 跳转页面后显示 -->
	<tr id="tr_deviceInfo1">
		<td>		
		<s:if test="deviceList != null">
		  <table width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
			<tr>
				<td bgcolor=#ffffff>&nbsp;</td>
			</tr>
		
			<s:if test="deviceList==null">
				<!-- 第一次入采集，不做任何查询 -->
			</s:if>
			<s:else>
				<s:if test="deviceList.size()>0">
					<tr>
						<td>
						<table width="100%" border=0 cellspacing=1 cellpadding=2
							bgcolor=#999999 id=userTable>
							<tr>
								<td class="green_title">属地</td>
								<td class="green_title">设备厂商</td>
								<td class="green_title">型号</td>
								<td class="green_title">软件版本</td>
								<td class="green_title">OUI</td>
								<td class="green_title">设备序列号</td>
								<td class="green_title">域名或IP</td>
								<td class="green_title">上报时间</td>
								<td class="green_title" align='center' width="10%">操作</td>
							</tr>
							<s:iterator value="deviceList">
								<tr bgcolor="#ffffff" id="device_id<s:property value="device_id" />">
									<s:if test="gw_type == 1">
										<td class=column nowrap align="center"><s:property
												value="city_name" /></td>
										<td class=column nowrap align="center"><s:property
												value="vendor_add" /></td>
										<td class=column nowrap align="center"><s:property
												value="device_model" /></td>
										<td class=column nowrap align="center"><s:property
												value="softwareversion" /></td>
										<td class=column nowrap align="center"><s:property
											value="oui" /></td>
										<td class=column nowrap align="center"><s:property
												value="device_serialnumber" /></td>
										<td class=column nowrap align="center"><s:property
												value="loopback_ip" /></td>
										<td class=column nowrap align="center"><s:property
												value="complete_time" /></td>
										<td class=column nowrap align="center"> 
											<%-- <IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='编辑'
											onclick="EditDevice('<s:property value="device_id"/>')"
											style='cursor: hand'> --%>
											<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="详细信息" title="详细信息"
												onclick="DetailDevice('<s:property value="device_id"/>')"
												style="cursor: hand"> 
											<IMG SRC="<s:url value="/images/refresh.png"/>" BORDER="0" ALT="刷新" title="刷新"
												onclick="refreshDev('<s:property value="device_id"/>','<s:property value="device_serialnumber"/>','<s:property value="oui"/>')"
												style="cursor: hand"> 
											<s:if test="#session.curUser.user.areaId==1">
											 <IMG SRC="<s:url value="/images/del.gif"/>" BORDER="0" ALT="删除" title="删除"
												onclick="delDev('<s:property value="device_id"/>','<s:property value="cpe_allocatedstatus"/>','<s:property value="oui"/>','<s:property value="device_serialnumber"/>')"
												style="cursor: hand">
											</s:if>
										</td>
									</s:if>
									<s:else>
										<td class=column nowrap align="center"><s:property
												value="city_name" /></td>
										<td class=column nowrap align="center"><s:property
												value="vendor_add" /></td>
										<td class=column nowrap align="center"><s:property
												value="device_model" /></td>
										<td class=column nowrap align="center"><s:property
												value="softwareversion" /></td>
										<td class=column nowrap align="center"><s:property
												value="device_serialnumber" /></td>
										<td class=column nowrap align="center"><s:property
												value="loopback_ip" /></td>
										<td class=column nowrap align="center"><s:property
												value="complete_time" /></td>
										<td class=column nowrap align="center">
											<%-- <IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='编辑'
												onclick="EditDevice('<s:property value="device_id"/>')"
												style='cursor: hand'> --%>
											<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="详细信息" title="详细信息"
												onclick="DetailDevice('<s:property value="device_id"/>')"
												style="cursor: hand"> 
											<IMG SRC="<s:url value="/images/refresh.png"/>" BORDER="0" ALT="刷新" title="刷新"
												onclick="refreshDev('<s:property value="device_id"/>','<s:property value="device_serialnumber"/>','<s:property value="oui"/>')"
												style="cursor: hand">
											<s:if test="#session.curUser.user.areaId==1">
											<IMG SRC="<s:url value="/images/del.gif"/>" BORDER="0" ALT="删除" title="删除"
												onclick="delDev('<s:property value="device_id"/>','<s:property value="cpe_allocatedstatus"/>','<s:property value="oui"/>','<s:property value="device_serialnumber"/>')"
												style="cursor: hand"> 
											</s:if>
										</td>
									</s:else>
								</tr>
							</s:iterator>
						</table>
						</td>
					</tr>
				</s:if>
		
				<s:else>
					<tr>
						<td>
						<table width="100%" border=0 cellspacing=1 cellpadding=2
							bgcolor=#999999 id=userTable>
							<tr>
								<td class="green_title">属地</td>
								<td class="green_title">设备厂商</td>
								<td class="green_title">型号</td>
								<td class="green_title">软件版本</td>
								<td class="green_title">设备序列号</td>
								<td class="green_title">域名或IP</td>
								<td class="green_title">上报时间</td>
								<td class="green_title" align='center' width="10%">操作</td>
							</tr>
							<tr>
								<td colspan=8 align=left class=column>系统没有相关的设备信息!</td>
							</tr>
						</table>
						</td>
					</tr>
				</s:else>
		
				<s:if test="deviceList.size()>0">
					<tr>
						<td align="right">
							<lk:pages url="/gwms/share/gwDeviceQuery!goPageDeviceListForSxlt.action"
								styleClass="" showType="" isGoTo="true" changeNum="true" />
						</td>
					</tr>
				</s:if>
			</s:else>
		
			<tr STYLE="display: none">
				<s:if test="gw_type == 1">
					<td><iframe id="childFrm" src=""></iframe></td>
				</s:if>
				<s:else>
					<td><iframe id="childFrm" src=""></iframe></td>
				</s:else>
			</tr>
		</table>
		</s:if>
		</td>
	</tr>
	
</table>

<%@ include file="../foot.jsp"%>
<script>
// 跳转页面传值
// 简单查询
var gwShare_queryType = "<s:property value='gwShare_queryType' />"; // 查询类型
var gwShare_queryField = "<s:property value='gwShare_queryField' />";// 查询参数
var gwShare_queryParam = "<s:property value='gwShare_queryParam' />";// 查询参数值
var timeType = "<s:property value='timeType' />";// 时间类型
var starttime = "<s:property value='startTime' />";// 开始时间
var endtime = "<s:property value='endTime' />";// 结束时间
var gw_type = "<s:property value='gw_type' />";// 开始时间
// 高级查询
var gwShare_cityId = "<s:property value='gwShare_cityId' />";// 属地
var gwShare_onlineStatus = "<s:property value='gwShare_onlineStatus' />";// 在线状态
var gwShare_vendorId = "<s:property value='gwShare_vendorId' />";// 厂商
var gwShare_deviceModelId = "<s:property value='gwShare_deviceModelId' />";// 型号
var gwShare_devicetypeId = "<s:property value='gwShare_devicetypeId' />";// 版本
var gwShare_bindType = "<s:property value='gwShare_bindType' />";// 绑定状态
var gwShare_deviceSerialnumber = "<s:property value='gwShare_deviceSerialnumber' />";// 设备序列号

//简单查询
if(gwShare_queryType == 1){
	gwShare_queryChange('1');
	//参数类型
	var gwShare_queryField_arr = $("input[@name='gwShare_queryField']");
	for(var i = 0; i < gwShare_queryField_arr.length; i++){
		var item = gwShare_queryField_arr[i];
		if(item.value ==  gwShare_queryField){
			item.checked = true;
		}
	}
	// 参数
	$("input[@name='gwShare_queryParam']").val(gwShare_queryParam);
	// 开始时间
	$("input[@name='starttime']").val(starttime);
	// 结束时间
	$("input[@name='endtime']").val(endtime);
	// 查询时间类型
	$("select[@name='timeType']").val(timeType);
}else if(gwShare_queryType == 2){
	gwShare_queryChange('2');
	// 属地
	$("select[@name='gwShare_cityId']").val(gwShare_cityId);
	// 在线状态
	$("select[@name='gwShare_onlineStatus']").val(gwShare_onlineStatus);
	// 厂商
	$("select[@name='gwShare_vendorId']").val(gwShare_vendorId);
	// 型号
	$("select[@name='gwShare_deviceModelId']").val(gwShare_deviceModelId);
	// 版本
	$("select[@name='gwShare_devicetypeId']").val(gwShare_devicetypeId);
	// 绑定状态
	$("select[@name='gwShare_bindType']").val(gwShare_bindType);
	// 设备序列号
	$("input[@name='gwShare_deviceSerialnumber']").val(gwShare_deviceSerialnumber);
}else{
	$(function(){
		/* $("input[@name='gwShare_jiadan']").css("display","none");
		$("input[@name='gwShare_gaoji']").css("display","none"); */
		$("input[@name='gwShare_import']").css("display","none");
		$("input[@name='gwShare_up_import']").css("display","none");
		
		gwShare_setGaoji();
	});
}


</script>