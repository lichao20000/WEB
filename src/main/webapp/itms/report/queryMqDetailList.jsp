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
			src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript">
	$(function() {
		parent.document.getElementById("queryButton").disabled = false;
		parent.dyniframesize();
	});
</script>

<br>
<table class="listtable" >
	<caption>
		设备列表
	</caption>
	<thead>
		<tr>
			<th>
				订阅者Id
			</th>
			<th>
				订阅者名称
			</th>
			<th>
				订阅主题
			</th>
			<th>
				挂起队列大小
			</th>
			<th>
				调度队列大小
			</th>
			<th>
				调度计数器
			</th>
			<th>
				入列计数器
			</th>
			<th>
				出列计数器
			</th>
			<th>
				时间
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="mqDetailList.size()>0">
			<s:iterator value="mqDetailList">
				<tr align="center">
					<td>
						<s:property value="client_id" />
					</td>
					<td>
						<s:property value="subscripion_name" />
					</td>
					<td>
						<s:property value="destination_topic" />
					</td>
					<td>
						<s:property value="pending_queue_size" />
					</td>
					<td>
						<s:property value="dispatched_queue_size" />
					</td>
					<td>
						<s:property value="dispatched_counter" />
					</td>
					<td>
						<s:property value="enqueue_counter" />
					</td>
					<td>
						<s:property value="dequeue_counter" />
					</td>
					<td>
						<s:property value="gathertime" />
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
			<td colspan="9" align="right" height="15">[ 统计总数 : <s:property value='queryCount'/> ]&nbsp;<lk:pages
						url="/itms/report/queryMq!queryMqDetail.action" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>


		<TR>
			<TD align="center" colspan="9">
				<button onclick="javascript:window.close();">
					&nbsp;关 闭&nbsp;
				</button>
			</TD>
		</TR>
	</tfoot>
</table>

<%@ include file="/foot.jsp"%>
