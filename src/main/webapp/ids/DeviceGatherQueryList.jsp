<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
	</head>
	<script type="text/javascript">
		function DetailDevice(device_id){
			var strpage = "../Resource/DeviceShow.jsp?device_id=" + device_id;
			window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
		}
	</script>
	<body>
		<table class="listtable" id="listTable">
					<thead>
						<tr>
							<th>设备采集时间</th>
							<th>设备id</th>
							<th>路由拨号信息</th>
							<th>语音注册信息</th>
						</tr>
					</thead>
					<tbody>
						<s:if test="dataList!=null">
							<s:if test="dataList.size()>0">
								<s:iterator value="dataList">
										<tr>
											<td align="center"><s:property value="gather_time" /></td>
											<td align="center">
												<a href="javascript:DetailDevice('<s:property value="device_id"/>')">
													<s:property value="device_id" />
												</a>
											</td>
											<td align="center"><s:property value="last_conn_error" /></td>
											<td align="center"><s:property value="regist_result" /></td>
										</tr>
								</s:iterator>
							</s:if>
							<s:else>
								<tr>
									<td colspan=7>没有设备采集信息!</td>
								</tr>
							</s:else>
						</s:if>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="4" align="right">
						 	<lk:pages
								url="/ids/deviceGatherQuery!getDeviceInfo.action"
								styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
							</tr>
					</tfoot>
				</table>
	</body>
	
</html>