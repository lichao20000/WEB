<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>

<script type="text/javascript">


function toExcel(cityId) {
	var page="<s:url value='/gwms/report/MismathSpeed!toChangedExcel.action'/>?"+ "cityId=" + cityId;
	document.all("childFrm").src=page;
}

</script>

<table class="listtable">
	<caption>
		家庭网关终端速率不匹配已修改终端详情
	</caption>
	<thead>
		<tr>
			<th>分公司</th>
			<th>地市</th>
			<th>签约速率</th>
			<th>loid</th>
			<th>宽带账号</th>
			<th>型号</th>
			<th>硬件版本</th>
			<th>软件版本</th>
			<th>串码</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="devList.size()>0">
			<s:iterator value="devList">
				<tr>
					<td>
						<s:property value="city" />
					</td>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="downlink" />
					</td>
					<td>
						<s:property value="loid" />
					</td>
					<td>
						<s:property value="net_user" />
					</td>
					<td>
						<s:property value="device_model" />
					</td>
					<td>
						<s:property value="hardwareversion" />
					</td>
					<td>
						 <s:property value="softwareversion" />
					</td>
					<td>
						 <s:property value="oui_sn" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="9">
					系统没有相关的设备信息!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="9">
				<span style="float: right;"> <lk:pages
						url="/gwms/report/MismathSpeed!queryChangedDetail.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'
						onclick="toExcel('<s:property value="cityId"/>')">
			</td>
		</tr>


		<TR>
			<TD align="center" colspan="9">
				<button onclick="javascript:window.close();">
					&nbsp;关 闭&nbsp;
				</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="5">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>
