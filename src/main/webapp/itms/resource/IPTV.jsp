<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />

<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css" />
<title>IPTV业务质量检测页面</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
	function showBlue() {
		var txt = document.getElementById("iptv").value;
		//var classifyName = $.trim($("input[@name='classifyName']").val());
		$("div[@id='div_update']").html("");
		$("div[@id='div_update']").css("display", "");
		if (txt.length == 0) {
			alert("请输入完整IPTV业务账号！");
		}
		if ("n0436ztc5094660" == txt) {
			document.all.blue1.style.display = '';
		} else {
			document.all.blue1.style.display = 'none';
			$("div[@id='div_update']").html("当前条件查询的设备不存在！");
		}

	}
</script>
</head>
<body>
	<table width="100%" border="0" cellspacing="1" cellpadding="2"
		bgcolor="#999999">
		<tr>
			<td colspan="4" align="center" class="green_title">设备查询</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td width="15%" align="right">IPTV业务账号</td>
			<td align="left" colspan="3"><input id="iptv" type="text"
				class="bk"/><font color="red">*输入完整IPTV业务账号</font></td>

		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4" align="right"><input type="button" value="查询"
				onclick="showBlue()"/></td>
		</tr>
	</table>

	<table id="blue1" border=0 cellspacing=1 cellpadding=2 width="100%"
		align="center" bgcolor="#999999" style="display: none">
		<tr>
			<td colspan="4" align="center" class="green_title">视频质量信息</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">频道地址：</td>
			<td align="left" width="25%">239.160.1.2</td>
			<td align="center" width="25%" class="column">EPG访问次数：</td>
			<td align="left" width="25%">2</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">最大EPG响应时延(ms)：</td>
			<td align="left" width="25%">1000</td>
			<td align="center" width="25%" class="column">平均EPG响应时延(ms)：</td>
			<td align="left" width="25%">300</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">最大EPG加载时延(ms)：</td>
			<td align="left" width="25%">2000</td>
			<td align="center" width="25%" class="column">平均EPG加载时延(ms)：</td>
			<td align="left" width="25%">500</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">最大EPG认证时延(ms)：</td>
			<td align="left" width="25%">500</td>
			<td align="center" width="25%" class="column">平均EPG认证时延(ms)：</td>
			<td align="left" width="25%">100</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">视频加载时长：</td>
			<td align="left" width="25%">5000</td>
			<td align="center" width="25%" class="column">MOS值：</td>
			<td align="left" width="25%">4.84</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">VSTQ值：</td>
			<td align="left" width="25%">50</td>
			<td align="center" width="25%" class="column">丢包率(%)：</td>
			<td align="left" width="25%">0</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">平均抖动(微妙)：</td>
			<td align="left" colspan="3">100</td>
		</tr>

		<tr style="" bgcolor="#ffffff">
			<td align="center" width="25%"></td>
			<td align="center" width="25%" class="column">平均时长：</td>
			<td align="center" width="25%" class="column">失败次数</td>
			<td align="center" width="25%" class="column">请求次数</td>
		</tr>
		<tr style="" bgcolor="#ffffff">
			<td align="center" width="25%" class="column">直播业务切换</td>
			<td align="center" width="25%">500</td>
			<td align="center" width="25%">0</td>
			<td align="center" width="25%">2</td>
		</tr>
		<tr style="" bgcolor="#ffffff">
			<td align="center" width="25%" class="column">点播业务切换</td>
			<td align="center" width="25%">1000</td>
			<td align="center" width="25%">0</td>
			<td align="center" width="25%">0</td>
		</tr>
		<tr style="" bgcolor="#ffffff">
			<td align="center" width="25%" class="column">回看业务切换</td>
			<td align="center" width="25%">500</td>
			<td align="center" width="25%">0</td>
			<td align="center" width="25%">10</td>
		</tr>

		<tr bgcolor="#ffffff">
			<td colspan="4" height="10px"></td>
		</tr>
		<tr>
			<td colspan="4" align="center" class="green_title">网络质量信息</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">收到的包：</td>
			<td align="left" width="25%">50000</td>
			<td align="center" width="25%" class="column">丢包数 ：</td>
			<td align="left" width="25%">0</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">丢包率(经验值<0.1%)：</td>
			<td align="left" width="25%">0%</td>
			<td align="center" width="25%" class="column">媒体丢包率(经验值<2%)：</td>
			<td align="left" width="25%">0%</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">最小DF(ms)：</td>
			<td align="left" width="25%">0</td>
			<td align="center" width="25%" class="column">最大DF(ms)：</td>
			<td align="left" width="25%">0</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">平均DF(经验值<50ms)：</td>
			<td align="left" width="25%">0</td>
			<td align="center" width="25%" class="column">抖动(经验值<50us)：</td>
			<td align="left" width="25%">0</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4" height="10px"></td>
		</tr>

		<tr>
			<td colspan="4" align="center" class="green_title">用户行为分析信息</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">最新观看频道名称：</td>
			<td align="left" width="25%">CCTV1</td>
			<td align="center" width="25%" class="column">频道访问次数 ：</td>
			<td align="left" width="25%">20</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">最新观看频道内容：</td>
			<td align="left" width="25%">焦点访谈</td>
			<td align="center" width="25%" class="column">访问时间：</td>
			<td align="left" width="25%">2016/10/18 19:40</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="25%" class="column">观看最多频道：</td>
			<td align="left" width="25%" colspan="3">CCTV5</td>
		</tr>


	</table>
	<div id="div_update" style="display: none"
		style="width: 100%; z-index: 1; top: 100px"></div>
</body>
</html>