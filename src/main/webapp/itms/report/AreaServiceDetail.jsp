<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript">

function ListToExcel(cityId, flag, starttime, endtime) {
	var page="<s:url value='/itms/report/batchRestartResult!getDetailExcel.action'/>?"
		+ "cityId=" + cityId 
		+ "&flag=" +flag
		+ "&starttime=" +starttime
		+ "&endtime=" +endtime
	document.all("childFrm").src=page;
}

</script>

<table class="listtable">
	<caption>
		设备资源类表
	</caption>
	<thead>
		<tr>
			<th>
				厂商
			</th>
			<th>
				型号
			</th>
			<th>
				软件版本
			</th>
			<th>
				属地
			</th>
			<th>
				设备序列号
			</th>
			<th>
				loid
			</th>
			<th>
				重启时间
			</th>
			<th>
				重启结果
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="detailResultList.size()>0">
			<s:iterator value="detailResultList">
				<tr>
					<td>
						<s:property value="vendor_name" />
					</td>
					<td>
						<s:property value="device_model" />
					</td>
					<td>
						<s:property value="softwareversion" />
					</td>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="device_serialnumber" />
					</td>
					<td>
						<s:property value="loid" />
					</td>
					<td>
						<s:property value="restart_time" />
					</td>
					<td>
						<s:property value="restart_status" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7>
					系统没有相关的设备信息!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="9">
				<span style="float: right;"> 
				<lk:pages url="/itms/report/batchRestartResult!getDetail.action" styleClass="" showType=""
						isGoTo="true" changeNum="true" /> </span>
				<s:if test='#session.isReport=="1"'>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="cityId"/>','<s:property value="flag"/>'
						,'<s:property value="starttime"/>','<s:property value="endtime"/>')">
				</s:if>

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
		<td colspan="9">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>

<%@ include file="../../foot.jsp"%>
