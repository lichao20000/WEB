<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>语音业务按协议统计</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript">
	// 导出列表数据
	function queryDetailForExcel(gw_type,cityId,dataCityId,protocol){
		var url = "<s:url value='/gwms/report/voipCountReportACT!queryDetailForExcel.action'/>";
		document.detailForm.action = url+"?gw_type="+gw_type+"&cityId="+cityId+"&protocol="+protocol+"&dataCityId="+dataCityId;
		document.detailForm.method = "post";
		document.detailForm.submit();
	}
</script>

</head>
	<body>
		<form name="detailForm" action="" method="post">
			<table class="listtable" width="98%" align="center">
				<thead>
					<tr>
						<th align="center">地市</th>
						<th align="center">区县</th>
						<th align="center">LOID</th>
						<th align="center">语音类型</th>
						<th align="center">语音号码</th>
						<th align="center">语音口</th>
						<th align="center">语音VLAN</th>
						<th align="center">IP</th>
						<th align="center">掩码</th>
						<th align="center">网关</th>
						<th align="center">MGC1</th>
						<th align="center">MGC2</th>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="detailList">
						<tr bgcolor="#FFFFFF">
							<td><s:property value="scity_name"/></td>
						    <td><s:property value="city_name"/></td>
							<td><s:property value="loid"/></td>
							<td><s:property value="protocol"/></td>
							<td><s:property value="voip_phone"/></td>
							<td><s:property value="voip_port"/></td>
							<td><s:property value="vlanid"/></td>
							<td><s:property value="ipaddress"/></td> 
							<td><s:property value="ipmask"/></td>
							<td><s:property value="gateway"/></td>
							<td><s:property value="prox_serv"/></td>
							<td><s:property value="stand_prox_serv"/></td>
						</tr>
					</s:iterator>
				</tbody>
				
				<tfoot>
					<tr bgcolor="#FFFFFF">
							
							<td align="left" width="10%">
							 	<a href="javascript:queryDetailForExcel('<s:property value="gw_type"/>',
																	 '<s:property value="cityId"/>',
																	 '<s:property value="dataCityId"/>',
																	 '<s:property value="protocol"/>'
																	 );">
						 			<img src="<s:url value="/images/excel.gif"/>"  border="0" width="16" height="16"></img>
							 	</a>
							</td>
							<td colspan="11" align="right" nowrap="nowrap">
								<lk:pages url="/gwms/report/voipCountReportACT!querydetailList.action" styleClass="" showType="" isGoTo="true" changeNum="false"/>
							</td> 
					</tr>
					<tr STYLE="display: none">
						<td nowrap="nowrap">
							<iframe id="childFrm" src=""></iframe>
						</td>
					</tr>
				</tfoot>
			</table>
		</form>
	</body>
</html>