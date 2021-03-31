<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>零配置机顶盒列表</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
	$(function(){
		parent.dyniframesize();
	});
	function ToExcel() {
		var total = $("#bind_total").val();
		var mainForm = parent.document.getElementById("selectForm");
		mainForm.action = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!DeviceQueryExcel.action' />";
		if(total > 100000){
			alert("导出超过10万条记录!");
			return;
			/* if(!delWarn()){
				return;
			}else{
				mainForm.submit();
			} */
		}else{
			mainForm.submit();
		}
		
		mainForm.action = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!zeroqueryDeviceList.action' />"

	}
		
	function delWarn(){
		if(confirm("导出超过10万条记录，尽量在夜间22:00之后操作，是否继续？")){
			return true;
		}
		else{
			return false;
		}
	}
		
	function deviceDetail(deviceId, gw_type){
		window.open("<s:url value='/gtms/stb/resource/gwDeviceQueryStb!queryZeroDeviceDetail.action'/>?deviceId="+deviceId+"&gw_type="+gw_type,"","left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	}
	function deviceSelect(thisRadio){
		thisRadio.checked = true;
		parent.document.selectForm.childDevice.value=thisRadio.value;
	}
	function testOnlineStatus(id, gw_type)
	{
		parent.block();
		var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!testOnlineStatus.action'/>";
		var result = "";
		$.post(url, {
			deviceId:parseInt(id),
			gw_type:gw_type
		}, function(ajax) {
			var flag = parseInt(ajax);
			if(flag == 1){
				result = "设备在线(实时)";
			}else if (flag == 0){
				result = "设备下线(实时)";
			}
			else if (flag == -1){
				result = "设备下线(实时)";
			}
			else if (flag == -2){
				result = "设备下线(实时)";
			}
			else if (flag == -3){
				result = "设备在线(实时)";
			}
			else if (flag == -4){
				result = "设备下线(实时)";
			}
			else {
				result = "设备下线(实时)";
			}
			parent.unblock();
			alert(result);
		});
	}
	function ToExcel1() {
		var username=parent.document.selectForm.username.value;
		var servAccount=parent.document.selectForm.servAccount.value;
		var deviceSerialnumber=parent.document.selectForm.deviceSerialnumber.value;
		var cityId=parent.document.selectForm.cityId.value;
		var loopbackIp=parent.document.selectForm.loopbackIp.value;
		var startLastTime=parent.document.selectForm.startLastTime.value;
		var endLastTime=parent.document.selectForm.endLastTime.value;
		var addressingType=parent.document.selectForm.addressingType.value;
		var status=parent.document.selectForm.status.value;
		var cpeMac=parent.document.selectForm.cpeMac.value;
		
		var form = parent.document.getElementById("selectForm");
				
		//alert(form.action);
		var temp = form.action;
		var target =  form.target;
		form.action = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!zerotoExcel.action'/>?"
			+ "username=" + username 
			+ "&servAccount=" +servAccount
			+ "&deviceSerialnumber=" +deviceSerialnumber
			+ "&cityId=" +cityId
			+ "&loopbackIp=" +loopbackIp
			+ "&addressingType=" +addressingType
			+ "&status=" +status
			+ "&cpeMac=" +cpeMac
			+ "&startLastTime=" +startLastTime
			+ "&endLastTime=" +endLastTime;
		form.target = "";
		//alert(form.action);
		form.submit();
		form.action=temp
		form.target=target;
		//alert(form.action);
	}



	function deviceDelete(deviceId, gw_type){
		if(!confirm("真的要删除该设备吗？\n本操作所删除的不能恢复！！！")){
			return false;
		}
		var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!delete.action'/>";
		$.post(url,{
			deviceId:deviceId,
			gw_type:gw_type
		},function(ajax){
		    alert(ajax);
		    location.reload();
		});
	}
</SCRIPT>

</head>
<body>
<input type="hidden" id="bind_total" value="<s:property value="total"/>" />
<table class="listtable" width="98%" align="center">
	<thead>
		<tr>
			<th align="center">设备厂商</th>
			<th align="center">设备型号</th>
			<th align="center" width="12%">软件版本</th>
			<th align="center" width="8%">属地</th>
			<th align="center" width="12%">设备序列号</th>
			<th align="center" width="5%">OUI</th>
			<th align="center">业务帐号</th>
			<s:if test="'nx_dx'==instArea">
				<th align="center">接入帐号</th>
			</s:if>
			<th align="center">MAC</th>
			<th align="center">设备IP</th>
			<s:if test="'hn_lt'!=instArea">
				<th align="center" width="5%">接入类型</th>
			</s:if>
			 <th align="center">首次上报时间</th>
			 <s:if test="'nx_dx'!=instArea">
			 <th align="center" width="6%">设备状态</th>
			 </s:if>
			<s:if test="'none'==queryResultType && 'hn_lt'!=instArea">
				<th align="center">操作</th>
			</s:if>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="deviceList">
			<tr bgcolor="#FFFFFF">
			    <td><s:property value="vendor_add"/></td>
				<td><s:property value="device_model"/></td>
				<td><s:property value="softwareversion"/></td>
				<td><s:property value="city_name"/></td>
				<td><s:property value="device_serialnumber"/></td>
				<td><s:property value="oui"/></td>
				<td><s:property value="serv_account"/></td>
				<s:if test="'nx_dx'==instArea">
					<td><s:property value="pppoe_user"/></td>
				</s:if>
				<td><s:property value="cpe_mac"/></td> 
				<td><s:property value="loopback_ip"/></td>
				<s:if test="'hn_lt'!=instArea">
				<td><s:property value="addressing_type"/></td>
				</s:if>
				<td><s:property value="complete_time"/></td> 
				<s:if test="'nx_dx'!=instArea">
				<td><s:property value="status"/></td> 
				</s:if>
				<s:if test="'none'==queryResultType && 'hn_lt'!=instArea">
					<td align="center" nowrap="nowrap">
						<label onclick="javascript:deviceDetail(<s:property value='device_id'/>, <s:property value='gw_type'/>);">
							<IMG SRC="<s:url value="/images/view.gif"/>" BORDER='0' ALT='详细信息' style='cursor:hand'>
						</label>
						<label onclick="javascript:deviceDelete(<s:property value='device_id'/>, <s:property value='gw_type'/>);">
							<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0' ALT='删除' style='cursor:hand'>
						</label>
						<label onclick="javascript:testOnlineStatus(<s:property value='device_id'/>, <s:property value='gw_type'/>);">
							<IMG SRC="<s:url value="/images/alarm_grey.gif"/>" BORDER='0' ALT='检测在线状态' style='cursor:hand'>
						</label>
					</td>
				</s:if>
			</tr>
		</s:iterator>
		<tr bgcolor="#FFFFFF">
		<s:if test="'nx_dx' == instArea">
			<td colspan="13" align="left" nowrap="nowrap">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel()">
			</td>
		</s:if>
		</tr>
	</tbody>
	
	<tfoot>
		<tr bgcolor="#FFFFFF">
			<s:if test="'nx_dx'==instArea">
				<td colspan="13" align="right" nowrap="nowrap">
					<lk:pages url="/gtms/stb/resource/gwDeviceQueryStb!zeroqueryDeviceList.action" isGoTo="true" />
				</td> 
			</s:if>
			<s:else>
				<td colspan="13" align="right" nowrap="nowrap">
					<lk:pages url="/gtms/stb/resource/gwDeviceQueryStb!zeroqueryDeviceList.action" isGoTo="true" />
				</td> 
			</s:else>
			 
		</tr>
		<tr STYLE="display: none">
			<td nowrap="nowrap">
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>
	</tfoot>
</table>
</body>