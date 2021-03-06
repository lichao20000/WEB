<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript">

function ListToExcel(bindFlag,cityId,starttime1,endtime1) {
	var page="<s:url value='/itms/report/netAccSpeedAction!getExcel.action'/>?"
		+ "bindFlag=" + bindFlag
		+ "&cityId=" + cityId 
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1;
	document.all("childFrm").src=page;
}

//查看itms用户相关的信息
function itmsUserInfo(user_id){
	var strpage="<s:url value='/Resource/HGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}
</script>

<table class="listtable">
	<caption>
		用户列表
	</caption>
	<thead>
		<tr>
				<th>
					LOID
				</th>
				<th>
					厂家
				</th>
				<th>
					型号
				</th>
				<th>
					硬件版本
				</th>
				<th>
					软件版本
				</th>
				<th>
					属地
				</th>
				<th>
					测速时间
				</th>
				<th>
					签约速率
				</th>
				<th>
					测速速率
				</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="FTTHUserList.size()>0">
			<s:iterator value="FTTHUserList">
				<tr>
					<td>
						<s:property value="username" />
					</td>
					<td>
						<s:property value="vendor_add" />
					</td>
					<td>
						<s:property value="device_model" />
					</td>
					<td>
						<s:property value="softversion" />
					</td>
					<td>
						<s:property value="hardwareversion" />
					</td>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="test_time" />
					</td>
					<td>
						<s:property value="downlink" />
					</td>
					<td>
						<s:property value="realspeed" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=9>
					系统没有相关的用户信息!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="9">
				<span style="float: right;"> <lk:pages
						url="/itms/report/netAccSpeedAction!getList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="bindFlag"/>','<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>')">
				<s:if test='#session.isReport=="1"'>
					
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

<%@ include file="/foot.jsp"%>
