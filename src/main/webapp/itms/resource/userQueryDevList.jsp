<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">


<script type="text/javascript">
	function ListToExcel(cityId, startOpenDate, endOpenDate, deviceType,
			isActive, specName, moreinter, moreitv, morevoip, moretianyi,
			temval) {
		var page = "<s:url value='/itms/resource/UserQuery!queryUserQueryDevExcel.action'/>?"
				+ "city_id="
				+ cityId
				+ "&startOpenDate="
				+ startOpenDate
				+ "&endOpenDate="
				+ endOpenDate
				+ "&deviceType="
				+ deviceType
				+ "&isActive="
				+ isActive
				+ "&specName="
				+ specName
				+ "&moreinter="
				+ moreinter
				+ "&moreitv="
				+ moreitv
				+ "&morevoip="
				+ morevoip
				+ "&moretianyi="
				+ moretianyi
				+ "&temval=" + temval;
		document.all("childFrm").src = page;
	}
</script>

<table class="listtable">
	<caption>�û���ϸ��Ϣ</caption>
	<thead>
		<tr>
			<th>����</th>
			<th>LOID</th>
			<th>����˺�</th>
			<th>ITV�˺�</th>
			<th>VOIP�˺�</th>
			<th>����ʱ��</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deployDevList!=null">
			<s:if test="deployDevList.size()>0">
				<s:iterator value="deployDevList">
					<tr>
						<td><s:property value="city_name" /></td>
						<td><s:property value="loid" /></td>
						<td><s:property value="interusername" /></td>
						<td><s:property value="itvusername" /></td>
						<td><s:property value="voipusername" /></td>
						<td><s:property value="dealdate" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>�����������ܲ��𱨱���Ϣ</td>
				</tr>
			</s:else>

		</s:if>
		<s:else>
			<tr>
				<td colspan=6>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>

		<tr>
			<td colspan=6><span style="float: right;"> <lk:pages
						url="/itms/resource/UserQuery!queryUserQueryDev.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" />
			</span> <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
				style='cursor: hand'
				onclick="ListToExcel('<s:property value="city_id"/>','<s:property value="startOpenDate"/>','<s:property value="endOpenDate"/>','<s:property value="deviceType"/>','<s:property value="isActive"/>','<s:property value="specName"/>','<s:property value="moreinter"/>','<s:property value="moreitv"/>','<s:property value="morevoip"/>','<s:property value="moretianyi"/>','<s:property value="temval"/>')">
			</td>
		</tr>

		<TR>
			<TD align="center" colspan=6>
				<button onclick="javascript:window.close();">&nbsp;��
					��&nbsp;</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="6"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
