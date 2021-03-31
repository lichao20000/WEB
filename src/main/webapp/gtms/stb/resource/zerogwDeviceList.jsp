<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����û������б�</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
	$(function(){
		parent.dyniframesize();
	});
	function deviceDetail(deviceId){
		window.open("<s:url value='/gtms/stb/resource/gwDeviceQueryStb!queryZeroDeviceDetail.action'/>?deviceId="+deviceId,"","left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
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
		var loopbackIp=parent.document.selectForm.loopbackIp.value;
		var startLastTime=parent.document.selectForm.startLastTime.value;
		var endLastTime=parent.document.selectForm.endLastTime.value;
		var addressingType=parent.document.selectForm.addressingType.value;
		var status=parent.document.selectForm.status.value;
		var cpeMac=parent.document.selectForm.cpeMac.value;
		
		var form = parent.document.getElementById("selectForm");
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
	    form.submit();
		form.action=temp
		form.target=target;
	}
</SCRIPT>

</head>
<body>
<table class="listtable" width="98%" align="center">
	<thead>
		<tr>
		<th align="center">�豸����</th>
		<th align="center">�豸�ͺ�</th>
		<th align="center">����汾</th>
		<th align="center">����</th>
		<th align="center">�豸���к�</th>
		<th align="center">ҵ���ʺ�</th>
		<th align="center">MAC</th>
		<%if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
		 <th align="center">�豸IP(IPV4��ַ)</th>
		 <%} else {%>
		 <th align="center">�豸IP</th>
		 <%} %>
		 <%if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
		 <th align="center">�豸IP(IPV6��ַ)</th>
		 <%}%>
		 <%if(!"hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
		 <th align="center">��������</th>
		 <%} %>
		 <th align="center">�״��ϱ�ʱ��</th>
		 <%if(!"nx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) && 
				 !"jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
		 <th align="center">�豸״̬</th>
		  <%} %>
		<s:if test="'none'==queryResultType">
			<th align="center">����</th>
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
				<td><s:property value="serv_account"/></td>
				<td><s:property value="cpe_mac"/></td> 
				<td><s:property value="loopback_ip"/></td>
				<%if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
				<td><s:property value="loopback_ip_six"/></td>
				<%} %>
				<%if(!"hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
				<td><s:property value="addressing_type"/></td>
				<%} %>
				<td><s:property value="complete_time"/></td> 
				<%if(!"nx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) && 
						!"jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
				<td><s:property value="status"/></td> 
				<%} %>
				<s:if test="'none'==queryResultType">
					<td align="center" nowrap="nowrap">
						<label onclick="javascript:deviceDetail(<s:property value='device_id'/>);">
							<IMG SRC="<s:url value="/images/view.gif"/>" BORDER='0' ALT='��ϸ��Ϣ' style='cursor:hand'>
						</label>
						<label onclick="javascript:testOnlineStatus(<s:property value='device_id'/>);">
							<IMG SRC="<s:url value="/images/alarm_grey.gif"/>" BORDER='0' ALT='�������״̬' style='cursor:hand'>
						</label>
					</td>
				</s:if>
			</tr>
		</s:iterator>
		<!-- <tr bgcolor="#FFFFFF">
			<td colspan="12" align="left" nowrap="nowrap">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr> -->
	</tbody>
	
	<tfoot>
		<tr bgcolor="#FFFFFF">
		 	<td colspan="12" align="right" nowrap="nowrap">
				<lk:pages url="/gtms/stb/resource/gwDeviceQueryStb!zeroqueryDeviceList.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
		<tr STYLE="display: none">
			<td nowrap="nowrap">
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>
	</tfoot>
</table>
</body>