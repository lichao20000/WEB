<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<script type="text/javascript">
    var starttime = "<s:property value='starttime'/>";
    var endtime = "<s:property value='endtime'/>";
	$(function() {
		parent.showIframe();
		var h = $("body").attr("scrollHeight");
		parent.setDataSize(h + 50);
	});

	function zeroConfigDetail(bindState,bindWay,cityId){
		var url = "<s:url value='/gtms/stb/resource/zeroConfigRateCount!queryZeroConfigDetail.action'/>?bindState=" + bindState +
		"&bindWay="+bindWay +"&cityId="+cityId + "&starttime=" + starttime + "&endtime=" + endtime;
		window.open(url, "","left=80,top=80,width=1200,height=400,resizable=yes,scrollbars=yes");
	}
</script>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title></title>

		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
	</head>
	<body>
		<table class="listtable">
			<caption>
				统计结果
			</caption>
			<thead>
				<tr>
					<th>属地 </th>
					<th>机顶盒安装总数</th>
					<th>零配置总数</th>
					<th>零配置安装成功数</th>
					<th>零配置安装失败数</th>
					<th>其他绑定数</th>
					<th>零配置率</th>
					<th>零配置成功率</th>
					<th>爱运维总数</th>
					<th colspan="2">
					<div style="border-bottom: 1px solid #aca899;">E8-C查询MAC</div>
					<div>
					<div style="border-right: 1px solid #aca899;width: 50%;float: left;">匹配成功数</div>
					<div style="width: 50%;float: left;">匹配成功率</div>
					</div>
					</th>
					<th colspan="2">
					<div style="border-bottom: 1px solid #aca899;">通过IP查询AAA宽带账号</div>
					<div>
					<div style="border-right: 1px solid #aca899;width: 50%;float: left;">匹配成功数</div>
					<div style="width: 50%;float: left;">匹配成功率</div>
					</div>
					</th>
					<th colspan="2">
					<div style="border-bottom: 1px solid #aca899;">输入宽带账号自助安装</div>
					<div>
					<div style="border-right: 1px solid #aca899;width: 50%;float: left;">匹配成功数</div>
					<div style="width: 50%;float: left;">匹配成功率</div>
					</div>
					</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="data.size()>0">
					<s:iterator value="data" var="map1">
						<tr>
							<td align="center">
							<s:property value="cityName" />
							</td>
							<td align="center" style="width: 7%;">
							<a href="javascript:zeroConfigDetail(1,6,'<s:property value="cityId" />');">
							<s:property value="bindNumTal" /></a>
							</td>
							<td align="center">
							<a href="javascript:zeroConfigDetail(0,7,'<s:property value="cityId" />');">
							<s:property value="zeroBindNumTal" /></a>

							</td>
							<td align="center" style="width: 7%;">
							<a href="javascript:zeroConfigDetail(1,7,'<s:property value="cityId" />');">
							<s:property value="successBindNumTal" /></a>
							</td>
							<td align="center" style="width: 7%;">
							<a href="javascript:zeroConfigDetail(-1,7,'<s:property value="cityId" />');">
							<s:property value="failBindNumTal" /></a>
							</td>
							<td align="center">
							<a href="javascript:zeroConfigDetail(1,3,'<s:property value="cityId" />');">
							<s:property value="zeroFailOtherTal" /></a>
							</td>
							<td align="center">
							<s:property value="zeroBindRate" />
							</td>
							<td align="center">
							<s:property value="successRate" />
							</td>
							<td align="center" style="width: 7%;">
							<a href="javascript:zeroConfigDetail(1,5,'<s:property value="cityId" />');">
							<s:property value="imiNumTal" /></a>
							</td>
							<td align="center" style="width: 7%;">
							<a href="javascript:zeroConfigDetail(1,0,'<s:property value="cityId" />');">
							<s:property value="macSuccTal" /></a>
							</td>
							<td align="center" style="width: 7%;">
							<s:property value="macRate" />
							</td>
							<td align="center" style="width: 7%;">
							<a href="javascript:zeroConfigDetail(1,1,'<s:property value="cityId" />');">
							<s:property value="ipSuccTal" /></a>
							</td>
							<td align="center" style="width: 7%;">
							<s:property value="ipRate" />
							</td>
							<td align="center" style="width: 7%;">
							<a href="javascript:zeroConfigDetail(1,2,'<s:property value="cityId" />');">
							<s:property value="itvSuccTal" /></a>
							</td>
							<td align="center" style="width: 7%;">
							<s:property value="itvRate" />
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tfoot>
						<tr>
							<td align="left">
								没有相关信息
							</td>
						</tr>
					</tfoot>
				</s:else>
			</tbody>
			<tfoot>
				<tr>
					<td colspan='15'>
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
							style='cursor: hand'
							onclick="ToExcel('<s:property value="start"/>','<s:property value="end"/>')"/>
					</td>
				</tr>
			</tfoot>
			<tr STYLE="display: none">
				<td colspan="12">
					<iframe id="childFrm" src=""></iframe>
				</td>
			</tr>
		</table>
	</body>
</html>
