<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">

<script type="text/javascript">
function ListToExcel(cityId,endOpenDate) {
	var page="<s:url value='/itms/resource/PonNetWork!getDegraContextExcel.action'/>?"
		+ "city_id=" + cityId 
		+ "&endOpenDate=" +endOpenDate;
	document.all("childFrm").src=page;
}
</script>

<table class="listtable">
	<caption>
		E8C终端光路劣化数详细列表
	</caption>
	<thead>
		<tr>
			<th>区域</th>
			<th>子区域</th>
			<th>LOID</th>
			<th>设备序列号</th>
			<th>发送光功率</th>
			<th>接收光功率</th>
			<th>接收地址</th>
			<th>OLT名称</th>
			<th>OLTIP</th>
			<th>PON端口</th>
			<th>ONTID</th>
			<th>出现次数</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="devList.size()>0">
			<s:iterator value="devList">
				<tr>
					<td>
						<s:property value="area_name" />
					</td>
					<td>
						<s:property value="subarea_name" />
					</td>
					<td>
						<s:property value="loid" />
					</td>
					<td>
						<s:property value="device_serialnumber" />
					</td>
					<td>
						<s:property value="tx_power" />
					</td>
					<td>
						<s:property value="rx_power" />
					</td>
					<td>
						<s:property value="linkaddress" />
					</td>
					<td>
						<s:property value="olt_name" />
					</td>
					<td>
						<s:property value="olt_ip" />
					</td>
					<td>
						<s:property value="pon_id" />
					</td>
					<td>
						<s:property value="ont_id" />
					</td>
					<td>
						<s:property value="count_num" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=12>
					系统没有相关的设备信息!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan=12>
				<span style="float: right;"> <lk:pages
						url="/itms/resource/PonNetWork!getDegraContext.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="city_id"/>','<s:property value="endOpenDate"/>')"> 
			</td>
		</tr>


		<TR>
			<TD align="center" colspan=12>
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

<%@ include file="/foot.jsp"%>
