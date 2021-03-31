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

function ListToExcel(cityId,starttime1,endtime1,userTypeId,userline,isChkBind,is_active) {
	var page="<s:url value='/itms/report/bindWay!getHgwExcel.action'/>?"
		+ "cityId=" + cityId 
		+ "is_active=" + is_active 
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1
		+ "&userTypeId=" +userTypeId
		+ "&userline=" +userline
		+ "&isChkBind=" +isChkBind;
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
				用户帐号
			</th>
			<th>
				属 地
			</th>
			<th>
				用户来源
			</th>
			<th>
				绑定设备
			</th>
			<th>
				开户时间
			</th>
			<th>
				竣工状态
			</th>
			<th>
				操作
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
						<s:property value="user_type" />
					</td>
					<td>
						<s:property value="device" />
					</td>
					<td>
						<s:property value="opendate" />
					</td>
					<td>
						<s:if test='opmode=="1"'>
							<IMG SRC="<s:url value='/images/check.gif'/>" BORDER="0"
								ALT="已竣工" style="cursor: hand">
						</s:if>
						<s:else>
							<IMG SRC="<s:url value='/images/button_s.gif'/>" BORDER="0"
								ALT="未竣工" style="cursor: hand">
						</s:else>
					</td>
					<td>
						<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="详细信息"
							onclick="itmsUserInfo('<s:property value="user_id"/>')"
							style="cursor: hand">
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
						onclick="ListToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="userTypeId"/>','<s:property value="userline"/>','<s:property value="isChkBind"/>','<s:property value="is_active"/>')">
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
