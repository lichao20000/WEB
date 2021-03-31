<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript">
function queryDetail(upResult)
{
	var taskID = $("input[@name=taskID]").val();
	var page = "<s:url value='/gwms/resource/batchHttpTestMana!getTestSpeedTaskResult.action'/>?taskId=" + taskID + "&upResult=" + upResult; 
	window.open(page,"","left=20,top=20,width=1000,height=600,resizable=no,scrollbars=yes");
}
</script>

<table width="100%" class="listtable">
	<thead>
		<tr>
			<th align="center">��        ��</th>
			<ms:inArea areaCode="jl_lt" notInMode="true">
			<th align="center">���ٳɹ�</th>
			<th align="center">����ʧ��</th>
			<th align="center">δ����</th>
			</ms:inArea>
			<ms:inArea areaCode="jl_lt" notInMode="false">
			<th align="center">����ָ���·��ɹ�</th>
			<th align="center">����ָ���·�ʧ��</th>
			<th align="center">����ָ��δ�·�</th>
			<th align="center">���ٳɹ�</th>
			</ms:inArea>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td align="center">
				<a href="javascript:void(0);" onclick="queryDetail('all');">
					<s:property value="taskResultMap.all" />
				</a>
			</td>
			<td align="center">
				<a href="javascript:void(0);" onclick="queryDetail(1);">
					<s:property value="taskResultMap.succ" />
				</a>
			</td>
			<td align="center">
				<a href="javascript:void(0);" onclick="queryDetail(2);">
					<s:property value="taskResultMap.fail" />
				</a>
			</td>
			<td align="center">
				<a href="javascript:void(0);" onclick="queryDetail(0);">
					<s:property value="taskResultMap.undo" />
				</a>
				<input type="hidden" name="taskID" value="<s:property value='taskId' />"/>
			</td>
			<ms:inArea areaCode="jl_lt" notInMode="false">
			<td align="center">
				<a href="javascript:void(0);" onclick="queryDetail(3);">
					<s:property value="taskResultMap.dosucc" />
				</a>
			</td>
			</ms:inArea>
		</tr>
	</tbody>
	<tfoot>
		<tr bgcolor="#FFFFFF">
			<ms:inArea areaCode="jl_lt" notInMode="true">
				<td colspan="4" align="right">
					<button name="back" onclick="javascript:CloseDetail();">�ر�</button>
				</td>
			</ms:inArea>
			<ms:inArea areaCode="jl_lt" notInMode="false">
				<td colspan="5" align="right">
					<button name="back" onclick="javascript:CloseDetail();">�ر�</button>
				</td>
			</ms:inArea>
		</tr>
	</tfoot>
</table>
