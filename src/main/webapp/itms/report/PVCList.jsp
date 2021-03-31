<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript">

function ListToExcel(cityId,starttime1,endtime1,flag) {
	var page="<s:url value='/itms/report/PVCDeploymentReport!getHgwExcel.action'/>?"
		+ "cityId=" + cityId 
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1
		+ "&reform_flag=" +flag;
	document.all("childFrm").src=page;
}

</script>

<table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
	<tr>
		<td colspan="2">
			<table width="100%" border=0 cellspacing=1 cellpadding=2
				bgcolor=#999999 id=userTable>
				<tr>
					<th colspan="6">
						用户列表
					</th>
				</tr>
				<tr>
					<td class="green_title">
						宽带账号
					</td>
					<td class="green_title">
						属地
					</td>
					<td class="green_title">
						设备序列号
					</td>
					<td class="green_title">
						IPTV生效时间
					</td>
					<td class="green_title">
						BAS地址
					</td>
					<td class="green_title">
						VLAN值
					</td>
				</tr>
				<s:if test="hgwList.size()>0">
					<s:iterator value="hgwList">
						<tr bgcolor="#ffffff">
							<td class=column nowrap align="center">
								<s:property value="username" />
							</td>
							<td class=column nowrap align="center">
								<s:property value="city_name" />
							</td>
							<td class=column nowrap align="center">
								<s:property value="device_serialnumber" />
							</td>
							<td class=column nowrap align="center">
								<s:property value="completedate" />
							</td>
							<td class=column nowrap align="center">
								<s:property value="bas_ip" />
							</td>
							<td class=column nowrap align="center">
								<s:property value="vlanid" />
							</td>

						</tr>
					</s:iterator>
				</s:if>
				<s:else>

					<tr>
						<td colspan=7 align=left class=column>
							系统没有相关的PVC用户信息!
						</td>
					</tr>

				</s:else>
			</table>
		</td>
	</tr>
	<tr>
		<td class=column align="center" width="40">
			<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
				style='cursor: hand'
				onclick="ListToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="reform_flag"/>')">
		</td>
		<td align="right" class=column>
			<lk:pages url="/itms/report/PVCDeploymentReport!goPage.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" />
		</td>
	</tr>
	<tr>
		<td height="25" colspan="2">
		</td>
	</tr>
	<TR>
		<TD align="center" class=foot colspan="2">
			<INPUT TYPE="button" value=" 关 闭 " class=jianbian
				onclick="javascript:window.close();">
		</TD>
	</TR>
	<tr STYLE="display: none">
		<td colspan="2">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
