<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<style type="text/css">
.bc {
	font-size: 9pt;
	color: #000000;
	text-decoration: none;
	font-weight: bold;
	line-height: 22px;
	text-indent: 115px;
	background-image: url("../../images/green_title_bg.jpg");
}
</style>
<SCRIPT LANGUAGE="JavaScript">
	function updateCity() {
		var deviceId = $("input[@name='deviceId']").val();
		var newCityId = $("#newCityId").val();
		//alert("Value: " + $("#newCityId").val());
		//alert(deviceId);
		if ($("#bindDevice").val() == "1") {
			alert("设备已绑定");
			$("input[@name='updateBtn']").attr("disabled", true);
			return false;
		}
		if (!confirm('请确认是否进行属地修正?')) {
			return false;
		}
		var url = "<s:url value='/gwms/resource/updateDevCityByIP!updateAhCity.action'/>";
		$("div[@id='div_blue']").html("");
		$.post(url, {
			deviceId : deviceId,
			newCityId : newCityId
		}, function(ajax) {
			$("div[@id='div_blue']").html("");
			$("div[@id='div_blue']").append(ajax);
		});
		document.all("tr_blue").style.display = "";

	}
</SCRIPT>
<TR style="display: none" id="blue_space">
	<TD>
		<div id="blue_space" style="width: 100%; z-index: 1; top: 100px">
		</div>
	</TD>
	<TD HEIGHT=30></TD>
</TR>

<table width="100%" border=0 cellspacing=1 cellpadding=2 align="center"
	bgcolor=#999999>

	<s:iterator value="deviceList" var="deviceList">
		<tr bgcolor="#FFFFFF">
			<td colspan="4" class="bc">设备序列号： <s:property
					value="device_serialnumber" /></td>
		</tr>
		<input type="hidden" name="deviceId"
			value=<s:property value="device_id"/> />

		<input type="hidden" id="bindDevice" name="bindDevice"
			value=<s:property value="cpe_allocatedstatus"/> />

		<tr bgcolor="#FFFFFF" align="center">
			<td width="25%">设备属地</td>
			<td width="25%"><s:property value="city_name" /></td>
			<td width="25%">变更后属地</td>
			<td width="25%"><s:select list="cityList" name="newCityId"
					listKey="city_id" listValue="city_name" value="1234"
					cssClass="select" style="width:100px;"></s:select></td>

		</tr>
		<tr>
			<td align="right" class="column" colspan="4"><input
				name="updateBtn" type="button" class=jianbian value=" 修  改 "
				onclick="updateCity()"></td>
		</tr>
	</s:iterator>
	<tr>

	</tr>
</table>

<TR style="display: none" id="tr_blue">
	<TD>
		<div id="div_blue" style="width: 100%; z-index: 1; top: 100px">
		</div>
	</TD>
</TR>
