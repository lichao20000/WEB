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
	window.open(page,"","left=20,top=20,width=1000,height=600,resizable=yes,scrollbars=yes");
}
</script>

<table width="100%" class="listtable">
	<thead>
		<tr>
			<th align="center">已执行次数</th>
			<th align="center">数            量</th>
		</tr>
	</thead>
	<s:if test="taskcountlist!=null">
        <s:if test="taskcountlist.size()>0">
                <tbody>
                        <s:iterator value="taskcountlist">
                                <tr>
                                    <td align="center"><s:property value="times" /></td>
                                    <td align="center">
                                    <a href="javascript:void(0);" onclick="queryDetail('<s:property value="times"/>');">
										<s:property value="num" />
									</a>
                                      <input type="hidden" name="taskID" value="<s:property value='taskId' />"/>
                                     </td>
                                </tr>
                        </s:iterator>
                </tbody>
        </s:if>
<s:else>
<tbody>
	<tr>
		<td colspan="2">
			<font color="red">没有数据</font>
		</td>
	</tr>
</tbody>
</s:else>
</s:if>
	<tfoot>
		<tr bgcolor="#FFFFFF">
			<td colspan="2" align="right">
				<button name="back" onclick="javascript:CloseDetail();">关闭</button>
			</td>
		</tr>
	</tfoot>
</table>
