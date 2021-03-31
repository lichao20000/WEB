<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.Map"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配置信息</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/faulttreatment/slide.js"/>"></script>
<link href="<s:url value="/css3/css.css"/>" rel="stylesheet" type="text/css" />
<script type="text/javascript">
function configDetailInfo(strategyId,deviceSN,servTypeId){
	servTypeId = servTypeId ? servTypeId : "";
	var page = "<s:url value='/inmp/bss/bssSheetServ!getConfigDetail.action'/>?strategyId="+strategyId+"&deviceSN="+deviceSN
			+ "&servTypeId="+servTypeId;
	window.open(page,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}
function configLog(deviceSN,deviceId,servTypeId,servstauts,wanType){
	var page = "<s:url value='/inmp/bss/bssSheetServ!getConfigLogInfo.action'/>?"
		+ "deviceSN=" + deviceSN
		+ "&deviceId=" + deviceId
		+ "&servTypeId=" + servTypeId
		+ "&servstauts=" + servstauts
		+ "&wanType=" + wanType;
	window.open(page,"","left=20,top=20,width=700,height=350,resizable=yes,scrollbars=yes");
}
function resetData(userId, deviceId, oui, deviceSN, servTypeId, servstauts) {
	if (deviceId == "") {
		alert("用户未绑定设备，请先绑定设备，再激活！");
		return;
	}
	if (confirm('重新激活是将该业务置为未做状态，确实要重置吗?')) {
		var url = "<s:url value='/inmp/bss/bssSheetServ!callPreProcess.action'/>";
		$.post(url, {
			userId : userId,
			deviceSN : deviceSN,
			deviceId : deviceId,
			servTypeId : servTypeId,
			servstauts : servstauts,
			oui : oui
		}, function(ajax) {
			if (ajax == "1") {
				alert("调预读成功！");
				query();
			} else if (ajax == "-1") {
				alert("参数为空！");
			} else if (ajax == "-2") {
				alert("调预读失败！");
			}
		});
		//$("td[@id='temp123']").html("<font color='red'>未做</font>");
		//$('#resultStr',window.parent.document).html("<font color='red'>02586588146重新激活成功！</font>");
	}
}
function resetData(userId, deviceId, oui, deviceSN, servTypeId, servstauts) {
	if (deviceId == "") {
		alert("用户未绑定设备，请先绑定设备，再激活！");
		return;
	}
	if (confirm('重新激活是将该业务置为未做状态，确实要重置吗?')) {
		var url = "<s:url value='/inmp/bss/bssSheetServ!callPreProcess.action'/>";
		$.post(url, {
			userId : userId,
			deviceSN : deviceSN,
			deviceId : deviceId,
			servTypeId : servTypeId,
			servstauts : servstauts,
			oui : oui
		}, function(ajax) {
			if (ajax == "1") {
				alert("调预读成功！");
				query();
			} else if (ajax == "-1") {
				alert("参数为空！");
			} else if (ajax == "-2") {
				alert("调预读失败！");
			}
		});
		//$("td[@id='temp123']").html("<font color='red'>未做</font>");
		//$('#resultStr',window.parent.document).html("<font color='red'>02586588146重新激活成功！</font>");
	}
}
function solutions(result_id,deviceSN){
	var url = "<s:url value='/inmp/bss/bssSheetServ!getSolution.action'/>?&deviceSN="+deviceSN+"&result_id="+result_id;
	/*
	var tempForm = document.createElement("fm");
	tempForm.id="tempForm1";    
	tempForm.method="post";    
	tempForm.action=url;
	
	var hideInput1 = document.createElement("input");
	hideInput1.type="hidden";    
	hideInput1.name= "solution"  
	hideInput1.value= solution;  
	tempForm.appendChild(hideInput1);
	tempForm.attachEvent("onsubmit",function(){ openWindow();});
 	tempForm.fireEvent("onsubmit");  
	tempForm.submit();  
	document.body.removeChild(tempForm);
	*/
	//var obj= window.showModelessDialog(url,"","dialogHeight=350px;dialogwidth=410px;dialogLeft=260;dialogTop=100;help=no;resizable=o;status=no;scrollbars=no;"); 
	window.open(url,"","left=50,top=50,width=600,height=450,resizable=yes,scrollbars=yes");
}
</script>
</head>
	<body>
	<a class="sta_close" href="javascript:configInfoClose()"></a>
				<div class="sta_tit">
				设备<s:property value="deviceSN" /> 配置信息
				</div>
			<s:if test="configInfoList!=null">
			<s:if test="configInfoList.size()>0">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					class="it_table">
					<tr>
						<th>业务名称</th>
						<th>执行时间</th>
						<th>下发时间</th>
						<th>工单执行状态</th>
						<th>结果描述</th>
						<th width="285">操作</th>
					</tr>
				<s:iterator value="configInfoList">
					<tr>
						<td><s:property value="serviceName" /></td>
						<td><s:property value="start_time" /></td>
						<td><s:property value="end_time"/></td>
						<td><s:property value="status" /></td>
						<td><s:property value="fault_reason" /></td>
						<td><a class="td_btn" href="javascript:configDetailInfo('<s:property value="id" />','<s:property value="deviceSN" />','<s:property value="servType_id" />')">详细信息</a>
						<a class="td_btn" href="javascript:configLog('<s:property value="deviceSN" />','<s:property value="device_id" />','<s:property value="servType_id" />','<s:property value="servstauts" />','<s:property value="wanType" />')">历史配置</a>
						<a class="td_btn" href="javascript:solutions('<s:property value="result_id" />','<s:property value="deviceSN" />')">处理意见</a></td>
					</tr>
		</s:iterator>
		</table>
		</s:if>
		<s:else>
				<tr>
					<td colspan=6>
						<s:if test='openstatus=="1"'>
							设备符合配置要求，不用下发配置!
						</s:if>
						<s:else>
							系统没有配置信息!
						</s:else>
					</td>
				</tr>
			</s:else>
		</s:if>
	</body>

</html>