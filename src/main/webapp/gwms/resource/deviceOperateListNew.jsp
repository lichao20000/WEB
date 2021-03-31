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
<%
String isJs= LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
var gw_type = "<s:property value='gw_type' />";

function ToExcel() {
	var page="../../gwms/resource/queryDevice!getInfoExcelDevice2.action?"
		+ "&starttime=" + document.frm.starttime.value
		+ "&endtime=" + document.frm.endtime.value
		+ "&timeType=" + document.frm.timeType.value
		+ "&device_serialnumber=" + $.trim(document.frm.device_serialnumber.value)
		+ "&loopback_ip=" + document.frm.loopback_ip.value;
	document.all("childFrm").src=page;
	//window.open(page);
}

function DelDevice(device_id){
	if(!confirm("真的要删除该网络设备吗？\n本操作所删除的不能恢复！！！")){
		return false;
	}
	var url = "../../Resource/DeviceSave.jsp";
	var pars = "device_id=" + device_id;
	pars += "&gw_type=" + <s:property value="infoType" />;
	pars += "&tt=" + new Date().getTime();
	pars += "&_action=delete";
	var gw_type = <s:property value="infoType" />;
	var deluser;
	if(confirm("是否需要删除设备对应的用户帐号")){
		deluser = true;
	}else{
		deluser = false;
	}
	var _action = "delete";
	$.post(url,{
		device_id:device_id,
		gw_type:gw_type,
		tt:new Date().getTime(),
		_action:_action,
		deluser:deluser
	},function(ajax){	
		eval(ajax);
	});
	return true;
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
function delDev(device_id,cpe_allocatedstatus,oui,deviceSN){
	if(!confirm("真的要删除该网络设备吗？\n本操作所删除的不能恢复！！！")){
		return false;
	}
	var url = '<s:url value='/gwms/resource/refDelDev!delete.action'/>';
	var tmp = '<s:property value="infoType" />'; // modify by chenjie 原来这地方写死是'1',现在根据gw_type判断
	$.post(url,{
		deviceId:device_id,
		cpe_allocatedstatus:cpe_allocatedstatus,
		oui:oui,
		deviceSN:deviceSN,
		gw_type:tmp 
	},function(ajax){	
	    alert(ajax);
	    frm.submit();
	});
}
//-->
</script>

<form name="frm" action="<s:url value='/gwms/resource/queryDevice!deviceOperateByDeviceId.action'/>" method="POST">
<input name="gw_type" type="hidden" value="<s:property value='gw_type'/>"/>
<input type="hidden" name="deviceId" value="<s:property value='deviceId'/>">
<table width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
	<tr>
		<td bgcolor=#ffffff>&nbsp;</td>
	</tr>

	<s:if test="data==null">
		<!-- 第一次入采集，不做任何查询 -->
	</s:if>
	<s:else>
		<s:if test="data.size()>0">
			<tr>
				<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					bgcolor=#999999 id=userTable>
					<tr>
						<s:iterator value="title" status="status">
							<td class="green_title"><s:property
								value="title[#status.index]" /></td>

						</s:iterator>
						<td class="green_title" align='center' width="10%">操作</td>
					</tr>
					<s:iterator value="data">
						<tr bgcolor="#ffffff">
							<s:if test="infoType == 1">
								<td class=column nowrap align="center"><s:property
									value="city_name" /></td>
								<td class=column nowrap align="center"><s:property
									value="vendor_add" /></td>
								<td class=column nowrap align="center"><s:property
									value="device_model" /></td>
								<%
                                    if ("nx_lt".equals(isJs))
                                    {
                                %>
                                <td class=column nowrap align="center"><s:property
                                    value="softwareversion" /></td>
                                <%
                                    }
                                %>
								<td class=column nowrap align="center"><s:property
									value="device" /></td>
								<td class=column nowrap align="center"><s:property
									value="device_id_ex" /></td>
								<td class=column nowrap align="center"><s:property
									value="loopback_ip" /></td>
								<td class=column nowrap align="center"><s:property
									value="complete_time" /></td>
								<td class=column nowrap align="center"> <IMG
									SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='编辑'
									onclick="EditDevice('<s:property value="device_id"/>')"
									style='cursor: hand'>
									<%
										if (!"sx_lt".equals(isJs))
										{
									%>
									<IMG
											SRC="<s:url value="/images/button_s.gif"/>" BORDER='0'
											ALT='不管理'
											onclick="DelDevice('<s:property value="device_id"/>')"
											style='cursor: hand'>
									<%
										}
									%>
									<IMG
									SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="详细信息"
									onclick="DetailDevice('<s:property value="device_id"/>')"
									style="cursor: hand"> <IMG
									SRC="<s:url value="/images/refresh.png"/>" BORDER="0" ALT="刷新"
									onclick="refreshDev('<s:property value="device_id"/>','<s:property value="device_serialnumber"/>','<s:property value="oui"/>')"
									style="cursor: hand"> 
									<%if (!"cq_dx".equals(isJs)){%>
									<s:if test="#session.curUser.user.areaId==1">
									 <IMG SRC="<s:url value="/images/del.gif"/>" BORDER="0" ALT="删除"
										onclick="delDev('<s:property value="device_id"/>','<s:property value="cpe_allocatedstatus"/>','<s:property value="oui"/>','<s:property value="device_serialnumber"/>')"
										style="cursor: hand">
									</s:if>
									<%}else{%>
									<IMG SRC="<s:url value="/images/del.gif"/>" BORDER="0" ALT="删除"
										onclick="delDev('<s:property value="device_id"/>','<s:property value="cpe_allocatedstatus"/>','<s:property value="oui"/>','<s:property value="device_serialnumber"/>')"
										style="cursor: hand">
									<%}%>
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
									value="device" /></td>
								<td class=column nowrap align="center"><s:property
									value="device_id_ex" /></td>
								<td class=column nowrap align="center"><s:property
									value="loopback_ip" /></td>
								<td class=column nowrap align="center"><s:property
									value="complete_time" /></td>
								<td class=column nowrap align="center"><IMG
									SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='编辑'
									onclick="EditDevice('<s:property value="device_id"/>')"
									style='cursor: hand'>
									<%
										if (!"sx_lt".equals(isJs))
										{
									%>
									<IMG
											SRC="<s:url value="/images/button_s.gif"/>" BORDER='0'
											ALT='不管理'
											onclick="DelDevice('<s:property value="device_id"/>')"
											style='cursor: hand'>
									<%
										}
									%>
									<IMG
									SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="详细信息"
									onclick="DetailDevice('<s:property value="device_id"/>')"
									style="cursor: hand"> <IMG
									SRC="<s:url value="/images/refresh.png"/>" BORDER="0" ALT="刷新"
									onclick="refreshDev('<s:property value="device_id"/>','<s:property value="device_serialnumber"/>','<s:property value="oui"/>')"
									style="cursor: hand">
									<%if (!"cq_dx".equals(isJs)){%>
									<s:if test="#session.curUser.user.areaId==1">
									<IMG SRC="<s:url value="/images/del.gif"/>" BORDER="0" ALT="删除"
										onclick="delDev('<s:property value="device_id"/>','<s:property value="cpe_allocatedstatus"/>','<s:property value="oui"/>','<s:property value="device_serialnumber"/>')"
										style="cursor: hand"> 
									</s:if>
									<%}else{%>
									<IMG SRC="<s:url value="/images/del.gif"/>" BORDER="0" ALT="删除"
										onclick="delDev('<s:property value="device_id"/>','<s:property value="cpe_allocatedstatus"/>','<s:property value="oui"/>','<s:property value="device_serialnumber"/>')"
										style="cursor: hand"> 
									<%}%>
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
						<s:iterator value="title" status="status">
							<td class="green_title"><s:property
								value="title[#status.index]" /></td>
						</s:iterator>
						<td class="green_title" align='center' width="10%">操作</td>
					</tr>
					<tr>
						<td colspan=8 align=left class=column>系统没有相关的设备信息!</td>
					</tr>
				</table>
				</td>
			</tr>
		</s:else>

		<s:if test="data.size()>0">
			<tr>
				<td align="right"><lk:pages
					url="/gwms/resource/queryDevice!gopageDeviceOperate.action"
					styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		</s:if>
	</s:else>

	<tr STYLE="display: none">
		<s:if test="infoType == 1">
			<td><iframe id="childFrm" src=""></iframe></td>
		</s:if>
		<s:else>
			<td><iframe id="childFrm" src=""></iframe></td>
		</s:else>
	</tr>
</table>
</form>
