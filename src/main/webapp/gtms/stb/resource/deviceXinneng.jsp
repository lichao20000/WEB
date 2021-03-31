<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒性能信息</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

</SCRIPT>

</head>
<body>
<TABLE width="98%" class="querytable" align="center">
	<TR>
		<TH colspan="4" class="title_1">设备〖<s:property value="deviceDetailMap.device_serialnumber"/>〗性能信息</TH>
	</TR>
	<TR >
		<TD class="title_3"  colspan=4>网络质量信息
		<font color="red"><s:property value="deviceNetMap.error"/></font>
		</TD>
	</TR>
	<TR >
		<TD class="title_2" >收到的包</TD>
		<TD width="25%"><s:property value="deviceNetMap.packets_received"/>个</TD>
		<TD class="title_2" >丢包数</TD>
		<TD width="40%"><s:property value="deviceNetMap.packets_lost"/>个</TD>
	</TR>
	<TR >
		<TD class="title_2" >丢包率</TD>
		<TD width="25%"><s:property value="deviceNetMap.fraction_lost"/></TD>
		<TD class="title_2" >媒体丢包率</TD>
		<TD width="40%"><s:property value="deviceNetMap.pm_lost_rate"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >最小DF</TD>
		<TD width="25%"><s:property value="deviceNetMap.min_df"/>s</TD>
		<TD class="title_2" >最大DF</TD>
		<TD width="40%"><s:property value="deviceNetMap.max_df"/>s</TD>
	</TR>
	<TR >
		<TD class="title_2" >平均DF</TD>
		<TD width="25%"><s:property value="deviceNetMap.avg_df"/>s</TD>
		<TD class="title_2" >抖动</TD>
		<TD width="40%"><s:property value="deviceNetMap.dithering"/>μs</TD>
	</TR>
	<TR >
		<TD class="title_2" >总RAM</TD>
		<TD width="25%"><s:property value="deviceNetMap.phy_mem_size"/></TD>
		<TD class="title_2" >存储</TD>
		<TD width="40%"><s:property value="deviceNetMap.storage_size"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >码率</TD>
		<TD width="25%"><s:property value="deviceNetMap.bitrate"/>bps</TD>
		<TD class="title_2" ></TD>
		<TD width="40%"></TD>
	</TR>
	
	
	
	
	<TR >
		<TD class="title_3"  colspan=4>视频流信息
		<font color="red"><s:property value="deviceEpgMap.valuation"/></font>
		</TD>
	</TR>
	<TR >
		<TD class="title_2" >当前MOS值</TD>
		<TD width="25%"><s:property value="deviceEpgMap.mos"/></TD>
		<TD class="title_2" >MDI延迟因子</TD>
		<TD width="40%"><s:property value="deviceEpgMap.mdi_df"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >MDI丢包因子</TD>
		<TD width="25%"><s:property value="deviceEpgMap.mdi_mlr"/></TD>
		<TD class="title_2" ></TD>
		<TD width="40%"></TD>
	</TR>
	<TR >
		<TD class="title_3"  colspan=4>EPG交互信息
		<font color="red"><s:property value="deviceEpgMap.valuation"/></font>
		</TD>
	</TR>
	<TR >
		<TD class="title_2" >频道切换平均耗时</TD>
		<TD width="25%"><s:property value="deviceEpgMap.avgChannelChangeCost"/>ms</TD>
		<TD class="title_2" >组播请求平均耗时</TD>
		<TD width="40%"><s:property value="deviceEpgMap.avgMulticastRequestTime"/>ms</TD>
	</TR>
	<TR >
		<TD class="title_2" >直播业务切换平均时长</TD>
		<TD width="25%"><s:property value="deviceEpgMap.avgDirectSeedTime"/>ms</TD>
		<TD class="title_2" >直播业务切换失败次数/请求次数</TD>
		<TD width="40%"><s:property value="deviceEpgMap.directSeedFailTimes"/>/<s:property value="deviceEpgMap.directSeedReqTimes"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >点播业务切换平均时长</TD>
		<TD width="25%"><s:property value="deviceEpgMap.avgOrderProgTime"/>ms</TD>
		<TD class="title_2" >点播业务切换失败次数/请求次数</TD>
		<TD width="40%"><s:property value="deviceEpgMap.orderProgFailTimes"/>/<s:property value="deviceEpgMap.orderProgReqTimes"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >回看业务切换平均时长</TD>
		<TD width="25%"><s:property value="deviceEpgMap.avgLookBackTime"/>ms</TD>
		<TD class="title_2" >回看业务切换失败次数/请求次数</TD>
		<TD width="40%"><s:property value="deviceEpgMap.lookBackFailTimes"/>/<s:property value="deviceEpgMap.lookBackReqTimes"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >EPG服务器地址</TD>
		<TD width="25%"><s:property value="deviceEpgMap.epgServerAddr"/></TD>
		<TD class="title_2" ></TD>
		<TD width="40%"></TD>
	</TR>
	<TR>
		<TD colspan="4" class=foot>
			<div align="center">
				<button onclick="javascript:window.close();"> 关 闭 </button>
			</div>
		</TD>
	</TR>
</TABLE>
</body>