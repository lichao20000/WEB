<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�������б�</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
	$(function(){
		parent.dyniframesize();
	});
	function deviceDetail(deviceId){
		window.open("<s:url value='/gtms/stb/resource/gwDeviceQueryStb!queryDeviceDetail.action'/>?deviceId="+deviceId,"","left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	}
	function deviceDelete(deviceId){
		if(!confirm("���Ҫɾ�����豸��\n��������ɾ���Ĳ��ָܻ�������")){
			return false;
		}
		var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!delete.action'/>";
		$.post(url,{
			deviceId:deviceId
		},function(ajax){
		    alert(ajax);
		    location.reload();
		});
	}
	function deviceEdit(deviceId){
		window.open("<s:url value='/gtms/stb/resource/gwDeviceQueryStb!editInit.action'/>?deviceId="+deviceId,"","left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	}
	function deviceXinneng(deviceId){
		window.open("<s:url value='/gtms/stb/resource/gwDeviceQueryStb!xinneng.action'/>?deviceId="+deviceId,"","left=80,top=80,width=800,height=450,resizable=yes,scrollbars=yes");
	}
	function deviceSelect(thisRadio){
		thisRadio.checked = true;
		parent.document.selectForm.childDevice.value=thisRadio.value;
	}
	function testOnlineStatus(id)
	{
		parent.block();
		var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!testOnlineStatus.action'/>";
		var result = "";
		$.post(url, {
			deviceId : parseInt(id)
		}, function(ajax) {
			var flag = parseInt(ajax);
			if(flag == 1){
				result = "�豸����(ʵʱ)";
			}else if (flag == 0){
				result = "�豸����(ʵʱ)";
			}
			else if (flag == -1){
				result = "�豸����(ʵʱ)";
			}
			else if (flag == -2){
				result = "�豸����(ʵʱ)";
			}
			else if (flag == -3){
				result = "�豸����(ʵʱ)";
			}
			else if (flag == -4){
				result = "�豸����(ʵʱ)";
			}
			else {
				result = "�豸����(ʵʱ)";
			}
			parent.unblock();
			alert(result);
		});
	}
	function ToExcel() {
		var username=parent.document.selectForm.username.value;
		var servAccount=parent.document.selectForm.servAccount.value;
		var deviceSerialnumber=parent.document.selectForm.deviceSerialnumber.value;
		var cityId=parent.document.selectForm.cityId.value;
		var deviceIp=parent.document.selectForm.deviceIp.value;
		var startLastTime=parent.document.selectForm.startLastTime.value;
		var endLastTime=parent.document.selectForm.endLastTime.value;
		
		var form = parent.document.getElementById("selectForm");
		//alert(form.action);
		var temp = form.action;
		var target =  form.target;
		form.action = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!toExcel.action'/>?"
			+ "username=" + username 
			+ "&servAccount=" +servAccount
			+ "&deviceSerialnumber=" +deviceSerialnumber
			+ "&cityId=" +cityId
			+ "&deviceIp=" +deviceIp
			+ "&startLastTime=" +startLastTime
			+ "&endLastTime=" +endLastTime;
		form.target = "";
		//alert(form.action);
		form.submit();
		form.action=temp
		form.target=target;
		//alert(form.action);
	}
</SCRIPT>

</head>
<body>
<table class="listtable" width="98%" align="center">
	<thead>
		<!--<s:if test="'none'!=queryResultType">
			<th align="center">����</th>
		</s:if>-->
		<tr>
		<th align="center">�豸����</th>
		<th align="center">�豸�ͺ�</th>
		<th align="center">����汾</th>
		<th align="center">����</th>
		<th align="center">�豸���к�</th>
		<th align="center">ҵ���˺�</th>
		<!-- <th align="center">����״̬</th>
		<th align="center">�Ƿ�ɹ���</th>  -->
		 <th align="center">�豸IP</th>
		 <th align="center">�������ʱ��</th>
		<s:if test="'none'==queryResultType">
			<th align="center">����</th>
		</s:if>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="deviceList">
			<tr bgcolor="#FFFFFF">
				<!--<s:if test="'none'!=queryResultType">
					<td >
						<input type="<s:property value="queryResultType"/>" 
							value="<s:property value="device_id"/>|<s:property value="oui"/>|<s:property value="device_serialnumber"/>|<s:property value="loopback_ip"/>|<s:property value="city_id"/>|<s:property value="city_name"/>" onclick="javascript:deviceSelect(this);"/>ѡ��</td>
				</s:if>-->
				<td ><s:property value="vendor_add"/></td>
				<td ><s:property value="device_model"/></td>
				<td ><s:property value="softwareversion"/></td>
				<td ><s:property value="city_name"/></td>
				<td ><s:property value="device_serialnumber"/></td>
				<td ><s:property value="serv_account"/></td>
				<!--  <td ><s:property value="online_status"/></td>
				<td ><s:property value="inform_stat"/></td>-->
				<td ><s:property value="loopback_ip"/></td> 
				<td ><s:property value="last_time"/></td> 
				<s:if test="'none'==queryResultType">
					<td align="center">
						<label onclick="javascript:deviceDetail(<s:property value='device_id'/>);">
							<IMG SRC="<s:url value="/images/view.gif"/>" BORDER='0' ALT='��ϸ��Ϣ' style='cursor:hand'>
						</label>
						<s:if test='"1"==infoQueryType'>
							<label onclick="javascript:deviceDelete(<s:property value='device_id'/>);">
								<IMG SRC="<s:url value="images/del.gif"/>" BORDER='0' ALT='ɾ��' style='cursor:hand'>
							</label>
						</s:if>
						<!-- <label onclick="javascript:deviceEdit(<s:property value='device_id'/>);">
							<IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='�༭' style='cursor:hand'>
						</label> 
						<label onclick="javascript:deviceXinneng(<s:property value='device_id'/>);">
							<IMG SRC="<s:url value="/images/ico_1.gif"/>" BORDER='0' ALT='������Ϣ' style='cursor:hand'>
						</label>-->
						<label onclick="javascript:testOnlineStatus(<s:property value='device_id'/>);">
							<s:if test='"����"==online_status'>
								<IMG SRC="<s:url value="/images/alarm_grey1.png"/>" BORDER='0' ALT='�������״̬' style='cursor:hand'>
							</s:if>
							<s:else>
								<IMG SRC="<s:url value="/images/alarm_grey.gif"/>" BORDER='0' ALT='�������״̬' style='cursor:hand'>
							</s:else>
						</label>
						<!-- 
						<a href="javascript:deviceDetail(<s:property value='device_id'/>);">
							<IMG SRC="<s:url value="/images/view.gif"/>" BORDER='0' ALT='��ϸ��Ϣ' style='cursor:hand'>
						</a>
						 -->
					</td>
				</s:if>
			</tr>
		</s:iterator>
		<tr bgcolor="#FFFFFF">
			<td colspan="9" align="left">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
	</tbody>
	
	<tfoot>
		<tr bgcolor="#FFFFFF">
			
			<td colspan="9" align="right">
				<lk:pages url="/gtms/stb/resource/gwDeviceQueryStb!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
		<tr STYLE="display: none">
			<td>
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>
	</tfoot>
</table>
</body>