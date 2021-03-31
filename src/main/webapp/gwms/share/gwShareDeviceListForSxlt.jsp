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

//-->
</script>

<form name="frm" action="<s:url value='/gwms/resource/queryDevice!deviceOperateByDeviceIdForSxlt.action'/>" method="POST">
<input name="gw_type" type="hidden" value="<s:property value='gw_type'/>"/>
<input type="hidden" name="deviceId" value="<s:property value='deviceId'/>">
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
</form>
