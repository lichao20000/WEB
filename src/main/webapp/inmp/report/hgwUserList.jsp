<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/inmp/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/inmp/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/inmp/edittable.js"/>"></SCRIPT>
<%
String isJs= LipossGlobals.getLipossProperty("InstArea.ShortName");
%>

<script type="text/javascript">

function ListToExcel(cityId,starttime1,endtime1,flag,isNew) {
	var prod_spec_id='<s:property value="prodSpecId" />';
	var page="<s:url value='/inmp/report/PVCReport!getHgwExcel.action'/>?"
		+ "cityId=" + cityId 
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1
		+ "&reform_flag=" +flag
		+ "&isNew=" + isNew
		+ "&prodSpecId=" + prod_spec_id;
	document.all("childFrm").src=page;
}

</script>

<title>用户详细列表</title>

<table class="listtable">
	<caption>
		用户列表
	</caption>
	<thead>
		<tr>
			<th>
				属地
			</th>
			<th>
				用户名称
			</th>
			<th>
				受理时间
			</th>
			<% 
			if (!"nmg_dx".equals(isJs))
			{
			%>
			<th>
				竣工时间
			</th>
			<%} %>
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
						<s:property value="dealdate" />
					</td>
					<% 
					if (!"nmg_dx".equals(isJs))
					{
					%>
					<td>
						<s:property value="onlinedate" />
					</td>
					<%} %>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<% 
				if (!"nmg_dx".equals(isJs))
				{
				%>
				<td colspan=7>
				<%} else {%>
				<td colspan=6>
				<%} %>
					系统没有相关的用户信息!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="8">
				<span style="float: right;"> <lk:pages
						url="/inmp/report/getBindRateByCityid!getUser.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
					<!-- <IMG SRC="<s:url value="/images/inmp/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="reform_flag"/>','<s:property value="isNew"/>')"> -->

			</td>
		</tr>


		<TR>
			<TD align="center" colspan="8">
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

<%@ include file="../foot.jsp"%>
