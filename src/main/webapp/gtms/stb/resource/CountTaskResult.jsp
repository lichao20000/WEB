<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<script type="text/javascript">
function queryDetail(upResult)
{
	var taskID = $("input[@name=taskID]").val();
	var page = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!queryUpRecordByTaskId.action'/>?taskId=" + taskID + "&upResult=" + upResult; 
	window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}
</script>
<table width="100%" class="listtable">

	<thead>
		<tr>
			<%if("1".equals(LipossGlobals.getLipossProperty("isStbDownload"))){%>
			<th align="center">未执行</th>
			<th align="center">软件升级成功</th>
			<th align="center">软件升级失败</th>
			<%}else{ %>
			<th align="center">更新服务器地址修改成功</th>
			<th align="center">软件升级成功</th>
			<ms:inArea areaCode="hn_lt" notInMode="true">
			<th align="center">更新服务器地址修改失败</th>
			</ms:inArea>
			<ms:inArea areaCode="hn_lt" notInMode="false">
			<th align="center">未操作</th>
			<th align="center">不支持</th>
			</ms:inArea>
			<%} %>
			<%-- <ms:inArea areaCode="sx_lt">
				<th align="center">软件升级失败</th>
			</ms:inArea> --%>
		</tr>
	</thead>
	<tbody>
		<ms:inArea areaCode="hn_lt,sx_lt" notInMode="true">
		<tr>
			<td align="center">
				<a href="javascript:void(0);" onclick="queryDetail(1);"><s:property value="taskResultMap.updatesucc" /></a>
			</td>
			<td align="center">
				<a href="javascript:void(0);" onclick="queryDetail(2);"><s:property value="taskResultMap.softupsucc" /></a>
			</td>
			<td align="center">
				<a href="javascript:void(0);" onclick="queryDetail(-1);"><s:property value="taskResultMap.updatefailed" /></a>
				<input type="hidden" name="taskID" value="<s:property value='taskId' />"/>
			</td>
		</tr>
		</ms:inArea>
		<%if("1".equals(LipossGlobals.getLipossProperty("isStbDownload"))){%>
			<td align="center">
				<a href="javascript:void(0);" onclick="queryDetail(0);"><s:property value="taskResultMap.undo" /></a>
			</td>
			<td align="center">
				<a href="javascript:void(0);" onclick="queryDetail(1);"><s:property value="taskResultMap.updatesucc" /></a>
			</td>
			<td align="center">
				<a href="javascript:void(0);" onclick="queryDetail(-1);"><s:property value="taskResultMap.updatefailed" /></a>
				<input type="hidden" name="taskID" value="<s:property value='taskId' />"/>
			</td>
		<%} %>
		<ms:inArea areaCode="hn_lt" notInMode="false">
		<tr>
			<td align="center">
				<s:property value="taskResultMap.updatesucc" />
			</td>
			<td align="center">
				<s:property value="taskResultMap.softupsucc" />
			</td>
			<td align="center">
				<s:property value="taskResultMap.updatefailed" />
			</td>
			<td align="center">
				<s:property value="taskResultMap.unsupport" />
			</td>
		</tr>
		</ms:inArea>
	</tbody>
	<tfoot>
		<tr bgcolor="#FFFFFF">
			<ms:inArea areaCode="sx_lt">
				<td colspan="4" align="right">
					<button name="back" onclick="javascript:CloseDetail();">
						关闭
					</button>
				</td>
			</ms:inArea>
			<ms:inArea areaCode="sx_lt" notInMode="true">
				<td colspan="3" align="right">
					<button name="back" onclick="javascript:CloseDetail();">
						关闭
					</button>
				</td>
			</ms:inArea>
		</tr>
	</tfoot>
</table>
