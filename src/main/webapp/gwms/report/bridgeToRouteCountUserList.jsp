<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript">

function ListToExcel(cityId,starttime1,endtime1,sessionType) {
	var page="<s:url value='/gwms/report/iTVCount!getHgwExcel.action'/>?"
		+ "&cityId=" + cityId
		+ "&starttime1=" + starttime1
		+ "&endtime1=" + endtime1
		+ "&sessionType=" + sessionType
	document.all("childFrm").src=page;
}

</script>

<table class="listtable">
	<caption>
		不活跃设备列表
	</caption>
	<thead>
		<tr>
			<th>
				属地
			</th>
			<th>
				宽带账号
			</th>
			<th>
				设备序列号
			</th>
			<th>
				工作模式
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="hgwList.size()>0">
			<s:iterator value="hgwList">
				<tr>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="username" />
					</td>
					<td>
						<s:property value="device" />
					</td>
					<td>
						<s:property value="sessionType" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=4>
					系统没有相关的用户信息!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan=4>
				<span style="float: right;"> <lk:pages
						url="/gwms/report/bridgeToRoute!getHgw.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="ListToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="sessionType"/>')">
			</td>
		</tr>


		<TR>
			<TD align="center" colspan=4>
				<button onclick="javascript:window.close();">
					&nbsp;关 闭&nbsp;
				</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="4">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
