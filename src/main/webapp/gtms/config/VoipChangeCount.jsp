<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script language="JavaScript">
	function ToExcel(cityId) {
		var page = "<s:url value='/gtms/config/voipChangeCount!getInfoExcel.action'/>";
		document.all("childFrm").src = page;
	}

	function openDev(cityId, status) {
		var page = "<s:url value='/gtms/config/voipChangeCount!getDev.action'/>?"
				+ "cityId=" + cityId + "&status=" + status;
		window.open(page,"","left=50,top=50,height=550,width=1000,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
</script>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>


<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>语音用户割接统计</th>
					<td><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table class="querytable">
				<tr>
					<th colspan=4>语音用户割接统计</th>
				</tr>

				<table class="listtable">
					<caption>统计结果</caption>
					<thead>
						<tr>
							<th rowspan="2">属地</th>
							<th rowspan="2">总配置数</th>
							<th rowspan="2">成功</th>
							<th rowspan="2">失败</th>
							<th rowspan="2">不在线</th>
						</tr>
					</thead>
					<tbody>
						<s:if test="data.size()>0">
							<s:iterator value="data">
								<tr>
									<td><a
										href="javascript:openDev('<s:property value="city_id"/>','');">
											<s:property value="city_name" />
									</a></td>
									<td><a
										href="javascript:openDev('<s:property value="city_id"/>','');">
											<s:property value="allup" />
									</a></td>
									<td><a
										href="javascript:openDev('<s:property value="city_id"/>','1');">
											<s:property value="successnum" />
									</a></td>
									<td><a
										href="javascript:openDev('<s:property value="city_id"/>','-1');">
											<s:property value="failnum" />
									</a></td>
									<td><a
										href="javascript:openDev('<s:property value="city_id"/>','0');">
											<s:property value="noupnum" />
									</a></td>
								</tr>
							</s:iterator>
						</s:if>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="7"><IMG
								SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
								style='cursor: hand'
								onclick="ToExcel('<s:property value="cityId"/>')"></td>
						</tr>
					</tfoot>
					<tr STYLE="display: none">
						<td colspan="7"><iframe id="childFrm" src=""></iframe></td>
					</tr>

				</table>

			</table>
		</td>
	</tr>

</TABLE>

<%@ include file="/foot.jsp"%>
