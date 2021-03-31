<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<script type="text/javascript">
function queryDetail(upResult,importtype)
{
	var taskID = $("input[@name=taskID]").val();
	var page = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!queryUpRecordByTaskId_import.action'/>?taskId=" + taskID +'&taskImportType='+importtype; 
	if(upResult != '2'){
		page = page + "&upResult=" + upResult;
	}
	window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}
</script>
<table width="100%" class="listtable">

	<thead>
		<tr>
			<th align="center">总数</th>
			<th align="center">成功数</th>
			<th align="center">失败数</th>
			<th align="center">未做数</th>
		</tr>
	</thead>
	<tbody>
		 <tr>
		    <td align="center">
				<a href="javascript:void(0);" onclick="queryDetail(2,'<s:property value="taskResultMap.importtype"/>');">
				   <s:property value="taskResultMap.total" />
				</a>
			</td>
		    <td align="center">
				<a href="javascript:void(0);" onclick="queryDetail(1,'<s:property value="taskResultMap.importtype"/>');">
				<s:property value="taskResultMap.updatesucc" /></a>
			</td>
			<td align="center">
				<a href="javascript:void(0);" onclick="queryDetail(-1,'<s:property value="taskResultMap.importtype"/>');">
				<s:property value="taskResultMap.updatefailed" /></a>
				<input type="hidden" name="taskID" value="<s:property value='taskId' />"/>
			</td>
			<td align="center">
				<a href="javascript:void(0);" onclick="queryDetail(0,'<s:property value="taskResultMap.importtype"/>');">
				<s:property value="taskResultMap.undo" />
				</a>
			</td>
		 </tr>
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
