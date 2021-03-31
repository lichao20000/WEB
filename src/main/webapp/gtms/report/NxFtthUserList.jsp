 <%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>


<%--
	/**
	 * 规范版本查询页面
	 *
	 * @author 姓名(工号) Tel:电话
	 * @version 1.0
	 * @since ${date} ${time}
	 * 
	 * <br>版权：南京联创科技 网管科技部
	 * 
	 */
 --%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>宁夏FTTH用户列表</title>
		
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"	type="text/css">
		<script language="JavaScript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script language="JavaScript" src="<s:url value="/Js/edittable.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
		
		<script type="text/javascript">
			function ListToExcel(bindFlag,cityId,starttime1,endtime1) {
				var page="<s:url value='/gtms/report/nxFtthBindReport!getUserExcel.action'/>?"
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
	</head>
	
	<body>
		<table class="listtable">
			<caption>用户列表</caption>
			<thead>
				<tr>
					<th>用户帐号</th>
					<th>设备厂商</th>
					<th>设备型号</th>
					<th>属 地</th>
					<th>用户来源</th>
					<th>绑定设备</th>
					<th>开户时间</th>
					<th>绑定方式</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="FtthUserList.size()>0">
					<s:iterator value="FtthUserList">
						<tr>
							<td><s:property value="username" /></td>
							<td><s:property value="vendor_add" /></td>
							<td><s:property value="device_model" /></td>
							<td><s:property value="city_name" /></td>
							<td><s:property value="user_type" /></td>
							<td><s:property value="device" /></td>
							<td><s:property value="opendate" /></td>
							<td><s:property value="bindtype" /></td>
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
						<td colspan=9>系统没有相关的用户信息!</td>
					</tr>
				</s:else>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="9">
						<span style="float: right;"> 
							<lk:pages url="/gtms/report/nxFtthBindReport!getUserList.action" styleClass=""
								showType="" isGoTo="true" changeNum="true" />
						</span>
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
							style='cursor: hand'
							onclick="ListToExcel('<s:property value="bindFlag"/>','<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>')">
						
						<s:if test='#session.isReport=="1"'>
							
						</s:if>
					</td>
				</tr>
		
				<TR>
					<TD align="center" colspan="9">
						<button onclick="javascript:window.close();">&nbsp;关 闭&nbsp;</button>
					</TD>
				</TR>
			</tfoot>
		
			<tr STYLE="display: none">
				<td colspan="9"><iframe id="childFrm" src=""></iframe></td>
			</tr>
		</table>
	</body>
	<%@ include file="/foot.jsp"%>
</html>