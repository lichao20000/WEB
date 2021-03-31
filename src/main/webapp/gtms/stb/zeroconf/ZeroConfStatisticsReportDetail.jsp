<%@page contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>工单零配置开通统计</title>
<lk:res />
<script type="text/javascript">
	$(function() {
		parent.showIframe();
		var h = $("body").attr("scrollHeight");
		parent.setDataSize(h + 50);
	});

	function openHgw(cityId, starttime1, endtime1, userStatus, custAccessType) {
		var page = "<s:url value='/gtms/stb/zeroconf/zeroConfStatisticsReportQuery!listCustomer.action'/>?"
				+ "dto.cityId="
				+ cityId
				+ "&dto.beginTime="
				+ starttime1
				+ "&dto.endTime="
				+ endtime1
				+ "&dto.userStatus="
				+ userStatus
				+ "&dto.custAccessType=" + custAccessType;
		window
				.open(
						page,
						"",
						"left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}

	function openFailHgw(cityId, starttime1, endtime1, userStatus,
			custAccessType) {
		var page = "<s:url value='/gtms/stb/zeroconf/zeroConfStatisticsReportQuery!failListCustomer.action'/>?"
				+ "dto.cityId="
				+ cityId
				+ "&dto.beginTime="
				+ starttime1
				+ "&dto.endTime="
				+ endtime1
				+ "&dto.custAccessType="
				+ custAccessType;
		window
				.open(
						page,
						"",
						"left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
</script>
</head>
</head>
<body>
	<table width="100%" class="listtable" align="center"
		style="margin-top: 10px;">
		<thead>
			<tr>
				<th class="title_1" rowspan="3" align="center">地市</th>
				<th class="title_1" colspan="15" align="center" height="27">接入方式</th>
			</tr>
			<tr>
				<th class="title_1" colspan="5" align="center" height="27">ADSL</th>
				<th class="title_1" colspan="5" align="center" height="27">LAN</th>
				<th class="title_1" colspan="5" align="center" height="27">FTHH</th>
			</tr>
			<tr>
				<th class="title_1" align="center">工单总数</th>
				<th class="title_1" align="center">零配置开通总数</th>
				<th class="title_1" align="center">零配置失败数</th>
				<th class="title_1" align="center">未做工单数</th>
				<th class="title_1" align="center">零配置成功率</th>
				<th class="title_1" align="center">工单总数</th>
				<th class="title_1" align="center">零配置开通总数</th>
				<th class="title_1" align="center">零配置失败数</th>
				<th class="title_1" align="center">未做工单数</th>
				<th class="title_1" align="center">零配置成功率</th>
				<th class="title_1" align="center">工单总数</th>
				<th class="title_1" align="center">零配置开通总数</th>
				<th class="title_1" align="center">零配置失败数</th>
				<th class="title_1" align="center">未做工单数</th>
				<th class="title_1" align="center">零配置成功率</th>
			</tr>
		</thead>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td><s:property value="city_name" /></a></td>
					<td><a
						href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','','0');"><s:property
								value="adsl" /></td>
					<td><a
						href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','1','0');"><s:property
								value="adslsuccess" /></td>
					<td><a
						href="javascript:openFailHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','0,-1','0');"><s:property
								value="adslfail" /></td>
					<td><a
						href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','0','0');"><s:property
								value="adslno" /></td>
					<td><s:property value="adslpercent" /></td>
					<td><a
						href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','','2');"><s:property
								value="lan" /></td>
					<td><a
						href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','1','2');"><s:property
								value="lansuccess" /></td>
					<td><a
						href="javascript:openFailHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','0,-1','2');"><s:property
								value="lanfail" /></td>
					<td><a
						href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','0','2');"><s:property
								value="lanno" /></td>
					<td><s:property value="lanpercent" /></td>
					<td><a
						href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','','1,3');"><s:property
								value="fthh" /></td>
					<td><a
						href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','1','1,3');"><s:property
								value="fthhsuccess" /></td>
					<td><a
						href="javascript:openFailHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','0,-1','1,3');"><s:property
								value="fthhfail" /></td>
					<td><a
						href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','0','1,3');"><s:property
								value="fthhno" /></td>
					<td><s:property value="fthhpercent" /></td>
				</tr>
			</s:iterator>
			<s:subset source="data" start="0" count="1">
				<s:iterator>
					<tr>
						<td><s:property value="all" /></td>
						<td><s:property value="aadsl" /></td>
						<td><s:property value="aadslsuccess" /></td>
						<td><s:property value="aadslfail" /></td>
						<td><s:property value="aadslno" /></td>
						<td><s:property value="aaadslpercent" /></td>
						<td><s:property value="alan" /></td>
						<td><s:property value="alansuccess" /></td>
						<td><s:property value="alanfail" /></td>
						<td><s:property value="alanno" /></td>
						<td><s:property value="alanpercent" /></td>
						<td><s:property value="afthh" /></td>
						<td><s:property value="afthhsuccess" /></td>
						<td><s:property value="afthhfail" /></td>
						<td><s:property value="afthhno" /></td>
						<td><s:property value="afthhpercent" /></td>
					</tr>
				</s:iterator>
			</s:subset>
		</s:if>
	</table>
</body>