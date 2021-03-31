<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">


<script type="text/javascript">
	function ListToExcel(cityId,startOpenDate,endOpenDate,specName,operation,hiddenVal) {
		var page = "<s:url value='/itms/resource/terminalUserBindingQuery!queryTerminalUserBindingDevExcel.action'/>?"
				+ "city_id="+ cityId
				+ "&startOpenDate="+ startOpenDate
				+ "&endOpenDate="+ endOpenDate
				+ "&specName="+ specName
				+ "&operation="+operation
				+ "&hiddenVal="+hiddenVal;
		document.all("childFrm").src = page;
	}
</script>

<table class="listtable">
	<caption>终端用户绑定记录详细信息</caption>
	<thead>
		<tr>
			<th>属地</th>
			<th>用户LOID</th>
			<th>设备序列号</th>
			<s:if test="%{hiddenVal == 1}">
			<th>受理时间</th>
			</s:if>
			<s:if test="%{hiddenVal == 2}">
			<th>注册时间</th>
			</s:if>
			<!-- <th>终端规格 </th> -->
			<th>操作方式</th>
			<th>操作时间</th>
			<th>操作人</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deployDevList!=null">
			<s:if test="deployDevList.size()>0">
				<s:iterator value="deployDevList">
					<tr>
						<td><s:property value="city_name" /></td>
						<td><s:property value="username" /></td>
						<td><s:property value="device_serialnumber" /></td>
						<s:if test="%{hiddenVal == 1}">
						<td><s:property value="dealdate" /></td>
						</s:if>
						<s:if test="%{hiddenVal == 2}">
						<td><s:property value="complete_time" /></td>
						</s:if>
						<%-- <td><s:property value="gw_type" /></td> --%>
						<td><s:property value="oper_type" /></td>
						<td><s:property value="binddate" /></td>
						<td><s:property value="dealstaff" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=8>暂无新增功能部署报表信息</td>
				</tr>
			</s:else>
			
		</s:if>
		<s:else>
			<tr>
				<td colspan=8>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>

		<tr>
			<td colspan=8><span style="float: right;"> <lk:pages
						url="/itms/resource/terminalUserBindingQuery!queryTerminalUserBindingDev.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" />
			</span> <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
				style='cursor: hand'
				onclick="ListToExcel('<s:property value="city_id"/>',
				'<s:property value="startOpenDate"/>',
				'<s:property value="endOpenDate"/>',
				'<s:property value="specName"/>',
				'<s:property value="operation"/>',
				'<s:property value="hiddenVal"/>')">
			</td>
		</tr>

		<TR>
			<TD align="center" colspan=8>
				<button onclick="javascript:window.close();">&nbsp;关
					闭&nbsp;</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="8"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
