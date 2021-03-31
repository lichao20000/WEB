<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="../../css/inmp/css3/c_table.css" type="text/css">
<link rel="stylesheet" href="../../css/inmp/css3/global.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/inmp/jquery.js"></SCRIPT>
<script type="text/javascript"
	src="../../Js/inmp/My97DatePicker/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/inmp/edittable.js"></SCRIPT>
<script type="text/javascript">

function ListToExcel(cityId,starttime1,endtime1,userTypeId,userline,isChkBind,usertype,is_active) {
	var page="<s:url value='/inmp/report/bindWay!getHgwExcel2.action'/>?"
		+ "cityId=" + cityId 
		+ "&usertype=" +usertype
		+ "&is_active=" +is_active
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1
		+ "&userTypeId=" +userTypeId
		+ "&userline=" +userline
		+ "&isChkBind=" +isChkBind;
	document.all("childFrm").src=page;
}

//查看itms用户相关的信息
function itmsUserInfo(user_id){
	var strpage="<s:url value='/inmp/resource/HGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
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
				绑定方式
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
						<s:property value="bindtype" />
					</td>
					<td>
						<IMG SRC="<s:url value="../../images/inmp/view.gif"/>" BORDER="0" ALT="详细信息"
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
						url="/inmp/report/bindWay!getHgw2.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
						<IMG SRC="<s:url value="../../images/inmp/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="userTypeId"/>','<s:property value="userline"/>','<s:property value="isChkBind"/>','<s:property value="usertype"/>','<s:property value="is_active"/>')">
				<s:if test='#session.isReport=="1"'>
					
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

<%@ include file="../foot.jsp"%>
