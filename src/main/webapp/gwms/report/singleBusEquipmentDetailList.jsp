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


function ListToExcel(cityId, servTypeId, starttime1, endtime1, totalRowCount) {
    if(parseInt(totalRowCount) > 100000) {
        alert("数据量太大不支持导出");
        return;
    }
	var page="<s:url value='/gwms/report/singleBusEquipment!getSingleBusEquipmentDetailExcel.action'/>?"+ "cityId=" + cityId +
		"&servTypeId=" + servTypeId  + "&starttime1=" + starttime1 + "&endtime1=" + endtime1;
	document.all("childFrm").src=page;
}

</script>

<table class="listtable">
	<caption>
		单个业务统计设备资源报表详情
	</caption>
	<thead>
		<tr>
			<th>
				LOID
			</th>
			<th>
				属地
			</th>
			<th>
				用户账号
			</th>
			<th>
				设备序列号
			</th>
			<th>
				设备厂商
			</th>
			<th>
				设备型号
			</th>
			<th>
				硬件版本
			</th>
			<th>
				软件版本
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="singleBusEquipmentDetailList.size()>0">
			<s:iterator value="singleBusEquipmentDetailList">
				<tr>
					<td>
						<s:property value="loid" />
					</td>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="username" />
					</td>
					<td>
						<s:property value="device_name" />
					</td>
					<td>
						<s:property value="vendor_add" />
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
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="20">
					系统没有相关的设备资源信息!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="20">
                <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
                     style='cursor: hand'
                     onclick="ListToExcel('<s:property value="cityId"/>', '<s:property value="servTypeId"/>', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', <s:property value='totalRowCount_splitPage'/>)">
                <span style="float: right;">
                    [ 统计总数 : <s:property value='totalRowCount_splitPage'/> ]&nbsp;
                    <lk:pages
						url="/gwms/report/singleBusEquipment!querySingleBusEquipmentDetailList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
			</td>
		</tr>


		<TR>
			<TD align="center" colspan="20">
				<button onclick="javascript:window.close();">
					&nbsp;关 闭&nbsp;
				</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="20">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>
