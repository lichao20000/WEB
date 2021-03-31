<%@page contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>失败原因统计</title>
<lk:res />
<script type="text/javascript">
	$(function() {
		parent.showIframe();
		var h = $("body").attr("scrollHeight");
		parent.setDataSize(h + 50);
	});

	function openHgw(cityId, starttime1, endtime1, failReasonId) {
		var page = "<s:url value='/gtms/stb/zeroconf/zeroFailReasonQuery!zeroqueryDeviceList.action'/>?"
				+ "dto.cityId="
				+ cityId
				+ "&dto.beginTime="
				+ starttime1
				+ "&dto.endTime="
				+ endtime1
				+ "&dto.failReasonId="
				+ failReasonId;
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
				<th class="title_1" align="center">失败原因</th>
				<s:if test="data.size()>0">
					<s:iterator value="data">
						<th><s:property value="city_name" /></th>
					</s:iterator>
				</s:if>
				<s:subset source="data" start="0" count="1">
					<s:iterator>
						<th><s:property value="xiaoji" /></th>
					</s:iterator>
				</s:subset>
			</tr>
		</thead>
		<s:if test="data.size()>0">
			<tr>
				<td>成功</td>
				<s:iterator value="data">
					<td><a
						href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','0');"><s:property
								value="zerofailsuccess" /></td>
				</s:iterator>
				<s:subset source="data" start="0" count="1">
					<s:iterator>
						<td><s:property value="azerofailsuccess" /></td>
					</s:iterator>
				</s:subset>
			</tr>
		</s:if>
		<s:if test="data.size()>0">
			<tr>
				<td>E8-C终端未上报该机顶盒MAC</td>
				<s:iterator value="data">
					<td><a
						href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','1');"><s:property
								value="e8cnoupmac" /></td>
				</s:iterator>
				<s:subset source="data" start="0" count="1">
					<s:iterator>
						<td><s:property value="ae8cnoupmac" /></td>
					</s:iterator>
				</s:subset>
			</tr>
		</s:if>
		<s:if test="data.size()>0">
			<tr>
				<td>E8-C上报机顶盒MAC异常（含绑定多个机顶盒MAC）</td>
				<s:iterator value="data">
					<td><a
						href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','2');"><s:property
								value="e8cupmacexception" /></td>
				</s:iterator>
				<s:subset source="data" start="0" count="1">
					<s:iterator>
						<td><s:property value="ae8cupmacexception" /></td>
					</s:iterator>
				</s:subset>
			</tr>
		</s:if>
		<s:if test="data.size()>0">
			<tr>
				<td>IPTV账号不匹配</td>
				<s:iterator value="data">
					<td><a
						href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','3');"><s:property
								value="iptvaccountnomatch" /></td>
				</s:iterator>
				<s:subset source="data" start="0" count="1">
					<s:iterator>
						<td><s:property value="aiptvaccountnomatch" /></td>
					</s:iterator>
				</s:subset>
			</tr>
		</s:if>
		<s:if test="data.size()>0">
			<tr>
				<td>AAA查询不到宽带账号拨号信息</td>
				<s:iterator value="data">
					<td><a
						href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','4');"><s:property
								value="aaanotfindaccount" /></td>
				</s:iterator>
				<s:subset source="data" start="0" count="1">
					<s:iterator>
						<td><s:property value="aaaanotfindaccount" /></td>
					</s:iterator>
				</s:subset>
			</tr>
		</s:if>
		<s:if test="data.size()>0">
			<tr>
				<td>AAA反馈宽带账号信息匹配失败</td>
				<s:iterator value="data">
					<td><a
						href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="dto.beginTime"/>','<s:property value="dto.endTime"/>','5');"><s:property
								value="aaabackinfoerror" /></td>
				</s:iterator>
				<s:subset source="data" start="0" count="1">
					<s:iterator>
						<td><s:property value="aaaabackinfoerror" /></td>
					</s:iterator>
				</s:subset>
			</tr>
		</s:if>
		<s:if test="data.size()>0">
			<tr>
				<td>小计</td>
				<s:iterator value="data">
					<td><s:property value="allnum" /></td>
				</s:iterator>
				<s:subset source="data" start="0" count="1">
					<s:iterator>
						<td><s:property value="allCount" /></td>
					</s:iterator>
				</s:subset>
			</tr>
		</s:if>
	</table>
</body>