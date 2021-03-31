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
		�豸�б�
	</caption>
	<thead>
		<tr>
			<th>
				������Id
			</th>
			<th>
				����������
			</th>
			<th>
				��������
			</th>
			<th>
				������д�С
			</th>
			<th>
				���ȶ��д�С
			</th>
			<th>
				���ȼ�����
			</th>
			<th>
				���м�����
			</th>
			<th>
				���м�����
			</th>
			<th>
				ʱ��
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
					ϵͳû����ص��û���Ϣ!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="9" align="right" height="15">[ ͳ������ : <s:property value='queryCount'/> ]&nbsp;<lk:pages
						url="/itms/report/queryMq!queryMqDetail.action" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>


		<TR>
			<TD align="center" colspan="9">
				<button onclick="javascript:window.close();">
					&nbsp;�� ��&nbsp;
				</button>
			</TD>
		</TR>
	</tfoot>
</table>

<%@ include file="/foot.jsp"%>
