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


function ListToExcel(cityId, numType, starttime1, endtime1) {
	var page="<s:url value='/gwms/report/queryBatchRestartDevice!getBatchRestartDevDetailExcel.action'/>?"+ "cityId=" + cityId +
		"&numType=" + numType + "&starttime1=" + starttime1 + "&endtime1=" + endtime1;
	document.all("childFrm").src=page;
}

</script>

<table class="listtable">
	<caption>
		集团光宽批量重启详情
	</caption>
	<thead>
		<tr>
			<th>
				设备厂商
			</th>
			<th>
				设备型号
			</th>
			<th>
				软件版本
			</th>
			<th>
				属地
			</th>
			<th>
				设备重启原因
			</th>
			<th>
				设备序列号
			</th>
			<th>
				重启前指标
			</th>
			<th>
				重启后指标
			</th>
			<th>
				设备IP
			</th>
			<th>
				LOID
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="batchRestartDevDetailList.size()>0">
			<s:iterator value="batchRestartDevDetailList">
				<tr>
					<td>
						<s:property value="vendorName" />
					</td>
					<td>
						<s:property value="deviceModelName" />
					</td>
					<td>
						<s:property value="softwareVersion" />
					</td>
					<td>
						<s:property value="cityName" />
					</td>
					<td>
						<s:property value="rebootReason" />
					</td>
					<td>
						<s:property value="deviceSerialnumber" />
					</td>
					<td>
						<s:property value="beforeReStartNum" />
					</td>
					<td>
						<s:property value="afterReStartNum" />
					</td>
					<td>
						<s:property value="loopbackIp" />
					</td>
					<td>
						<s:property value="loid" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="6">
					系统没有相关的设备信息!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="10">
                <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
                     style='cursor: hand'
                     onclick="ListToExcel('<s:property value="cityId"/>', '<s:property value="numType"/>', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>')">
                <span style="float: right;">
                    [ 统计总数 : <s:property value='totalRowCount_splitPage'/> ]&nbsp;
                    <lk:pages
						url="/gwms/report/queryBatchRestartDevice!queryBatchRestartDevDetailList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
			</td>
		</tr>


		<TR>
			<TD align="center" colspan="10">
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
