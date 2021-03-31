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

function ListToExcel(cityId,starttime1,endtime1,wanAccessTypeId,forbidNet,typeId) {
	var page="<s:url value='/itms/report/iTVCount!getHgwExcel.action'/>?"
		+ "&cityId=" + cityId
		+ "&starttime1=" + starttime1
		+ "&endtime1=" + endtime1
		+ "&wanAccessTypeId=" + wanAccessTypeId
		+ "&forbidNet=" + forbidNet
		+ "&typeId=" + typeId;
	document.all("childFrm").src=page;
}

</script>

<table class="listtable">
	<caption>
		iTV用户列表
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
				终端类型
			</th>
			<th>
				套餐类型
			</th>
			<th>
				接入方式
			</th>
			<th>
				是否非纯
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
						<s:property value="type_name" />
					</td>
					<td>
						<s:property value="serv_package_name" />
					</td>
					<td>
						<s:property value="prod_spec_name" />
					</td>
					<td>
						<s:property value="forbid_net" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>
					系统没有相关的用户信息!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan=6>
				<span style="float: right;"> <lk:pages
						url="/itms/report/iTVCount!getHgw.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
				<s:if test='#session.isReport=="1"'>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="wanAccessTypeId"/>','<s:property value="forbidNet"/>','<s:property value="typeId"/>')">
				</s:if>

			</td>
		</tr>


		<TR>
			<TD align="center" colspan=6>
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
