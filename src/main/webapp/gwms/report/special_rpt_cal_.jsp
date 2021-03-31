<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>工单查询结果展现</title>

<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript">


</script>

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="99.9%" align="center">
		<tr bgcolor="#FFFFFF">
			<td class=column1 height="25">
				<strong>
					<s:if test='"1".equals(reportType)'>
						工单小时统计
					</s:if>
					<s:if test='"2".equals(reportType)'>
						工单日统计
					</s:if>
					<s:if test='"3".equals(reportType)'>
						工单周统计
					</s:if>
					<s:if test='"4".equals(reportType)'>
						工单月统计
					</s:if>
				</strong>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
					bgcolor="#000000">
					<tr>
						<th>属地</th>
						<s:if test='"none".equals(sunm1display)'>
						</s:if>
						<s:else>
							<th>开户</th>
						</s:else>
						<s:if test='"none".equals(sunm2display)'>
						</s:if>
						<s:else>
							<th>暂停</th>
						</s:else>
						<s:if test='"none".equals(sunm3display)'>
						</s:if>
						<s:else>
							<th>销户</th>
						</s:else>
						<s:if test='"none".equals(sunm4display)'>
						</s:if>
						<s:else>
							<th>复机</th>
						</s:else>
						<s:if test='"none".equals(sunm5display)'>
						</s:if>
						<s:else>
							<th>更改速率</th>
						</s:else>
						<s:if test='"none".equals(sunm6display)'>
						</s:if>
						<s:else>
							<th>更改账号</th>
						</s:else>
						<s:if test='"none".equals(sunm7display)'>
						</s:if>
						<s:else>
							<th>更换设备</th>
						</s:else>
						<s:if test='"none".equals(sunm8display)'>
						</s:if>
						<s:else>
							<th>更改IP</th>
						</s:else>				
						<th>统计工单总数</th>
						<th>配置成功工单数</th>
						<th>成功率(%)</th>
					</tr>
					<s:iterator value="reportResult">
						<tr bgcolor="#FFFFFF">
							<td class=column>
								<s:if test='"true".equals(haschild)'>
									<s:if test='"1".equals(reportType)'>
								 		<a href="javascript:queryData('1','<s:property value="city_id"/>','<s:property value="longData"/>');">
								 			<s:property value="city_name"/>
								 		</a>
								 	</s:if>
									<s:if test='"2".equals(reportType)'>
								 		<a href="javascript:queryData('2','<s:property value="city_id"/>','<s:property value="longData"/>');">
								 			<s:property value="city_name"/>
								 		</a>
								 	</s:if>
								 	<s:if test='"3".equals(reportType)'>
								 		<a href="javascript:queryData('3','<s:property value="city_id"/>','<s:property value="longData"/>');">
								 			<s:property value="city_name"/>
								 		</a>
								 	</s:if>
								 	<s:if test='"4".equals(reportType)'>
								 		<a href="javascript:queryData('4','<s:property value="city_id"/>','<s:property value="longData"/>');">
								 			<s:property value="city_name"/>
								 		</a>
								 	</s:if>
								 </s:if>
								 <s:else>
								 	<s:property value="city_name"/>
								 </s:else>
							</td>
							
							<s:if test='"none".equals(sunm1display)'>
							</s:if>
							<s:else>
								<td  bgcolor=#ffffff align=center><s:property value="sum1"/></td>
							</s:else>
							<s:if test='"none".equals(sunm2display)'>
							</s:if>
							<s:else>
								<td  bgcolor=#ffffff align=center><s:property value="sum2"/></td>
							</s:else>
							<s:if test='"none".equals(sunm3display)'>
							</s:if>
							<s:else>
								<td  bgcolor=#ffffff align=center><s:property value="sum3"/></td>
							</s:else>
							<s:if test='"none".equals(sunm4display)'>
							</s:if>
							<s:else>
								<td  bgcolor=#ffffff align=center><s:property value="sum4"/></td>
							</s:else>
							<s:if test='"none".equals(sunm5display)'>
							</s:if>
							<s:else>
								<td  bgcolor=#ffffff align=center><s:property value="sum5"/></td>
							</s:else>
							<s:if test='"none".equals(sunm6display)'>
							</s:if>
							<s:else>
								<td  bgcolor=#ffffff align=center><s:property value="sum6"/></td>
							</s:else>
							<s:if test='"none".equals(sunm7display)'>
							</s:if>
							<s:else>
								<td  bgcolor=#ffffff align=center><s:property value="sum7"/></td>
							</s:else>
							<s:if test='"none".equals(sunm8display)'>
							</s:if>
							<s:else>
								<td  bgcolor=#ffffff align=center><s:property value="sum8"/></td>
							</s:else>			
							
							<td  bgcolor=#ffffff align=center><s:property value="sum9"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="sum10"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="sum11"/></td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
					bgcolor="#000000">
					<tr bgcolor="#FFFFFF">
						<td class=column1 align="left" width="100">
						 	<a href="javascript:queryDataForPrint('<s:property value="reportType"/>','<s:property value="cityId"/>','<s:property value="longData"/>');">
					 			<img src="../../images/print.gif" width="16"  border="0" height="16"></img>
						 	</a>
						 	&nbsp;
						 	<a href="javascript:queryDataForExcel('<s:property value="reportType"/>','<s:property value="cityId"/>','<s:property value="longData"/>');">
					 			<img src="../../images/excel.gif" width="16"  border="0" height="16"></img>
						 	</a>
						 	&nbsp;
							<a href="javascript:queryDataForPdf('<s:property value="reportType"/>','<s:property value="cityId"/>','<s:property value="longData"/>');">
					 			<img src="../../images/pdf.gif" width="16"  border="0" height="16"></img>
						 	</a>
						</td>
						<td class=column1  colspan="12" align="right">
							<strong>
								<s:if test='"1".equals(reportType)'>
									统计日期：<s:property value="hourDataEnd"/>
								</s:if>
								<s:if test='"2".equals(reportType)'>
									统计日期：<s:property value="dayDataEnd"/>
								</s:if>
								<s:if test='"3".equals(reportType)'>
									统计截止时间：<s:property value="weekDataEnd"/>
								</s:if>
								<s:if test='"4".equals(reportType)'>
									统计截止时间：<s:property value="monthDataEnd"/>
								</s:if>
							</strong>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>