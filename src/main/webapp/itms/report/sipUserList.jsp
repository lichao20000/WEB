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

function ListToExcel(cityId,starttime1,endtime1,userTypeId,userline,isChkBind) {
	var page="<s:url value='/itms/report/sipUserInfo!getSipUserInfoExcel.action'/>?"
		+ "cityId=" + cityId 
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1
		
	document.all("childFrm").src=page;
}

</script>

<table class="listtable">
	<caption>
		用户列表
	</caption>
	<thead>
		<tr>
			<th>
				逻辑ID
			</th>
			<th>
				属 地
			</th>
			<th>
				协议类型
			</th>
			<th>
				VoIP认证账号
			</th>
			<th>
				业务电话号码
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="hgwList.size()>0">
			<s:iterator value="hgwList">
				<tr>
					<td>
						<s:property value="username" />
					</td>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="protocol" />
					</td>
					<td>
						<s:property value="voip_username" />
					</td>
					<td>
						<s:property value="voip_phone" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7>
					系统没有相关的用户信息!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="7">
				<span style="float: right;"> <lk:pages
						url="/itms/report/bindWay!getHgw.action" styleClass="" showType=""
						isGoTo="true" changeNum="true" /> </span>
				<s:if test='#session.isReport=="1"'>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="userTypeId"/>','<s:property value="userline"/>','<s:property value="isChkBind"/>')">
				</s:if>

			</td>
		</tr>


		<TR>
			<TD align="center" colspan="7">
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
