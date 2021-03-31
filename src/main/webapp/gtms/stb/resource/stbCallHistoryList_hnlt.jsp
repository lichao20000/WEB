<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
function detailDevice(mac,login_time)
{
	var table=$("input[name=table]").val();
	var url = "<s:url value='/gtms/stb/resource/stbCallHistoryACT!detailDevice.action'/>"
					+ "?mac=" + mac
					+ "&login_time="+login_time
					+ "&table="+table;
	window.open(url, "","left=20,top=20,width=700,height=650,resizable=yes,scrollbars=yes");
}
</script>

<div>
<input type="hidden" name="table" readonly value="<s:property value='table'/>" />
<table class="listtable">
	<caption>��ѯ���</caption>
	<thead>
		<tr>
			<th width="10%">����ʱ��</th>
			<th width="8%">����IP</th>
			<th width="8%">��������</th>
			<th width="9%">������MAC</th>
			<th width="8%">��֤APK�汾</th>
			<th width="8%">���������</th>
			<th width="6%">����˶˿�</th>
			<th width="6%">����˰汾</th>
			<th width="11%">����ҵ���˺�</th>
			<th width="11%">����ҵ���˺�</th>
			<th width="6%">���ش�����</th>
			<th width="6%">����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data!=null && data.size()>0">
		<s:iterator value="data">
			<tr align="center">
				<td><s:property value="request_time" /></td>
				<td><s:property value="login_ip" /></td>
				<td><s:property value="event_id" /></td>
				<td><s:property value="mac" /></td>
				<td><s:property value="apk_version_name" /></td>
				<td><s:property value="server_host" /></td>
				<td><s:property value="server_port" /></td>
				<td><s:property value="server_version" /></td>
				<td><s:property value="request_username" /></td>
				<td><s:property value="result_username" /></td>
				<td><s:property value="result_code" /></td>
				<td>
					<a href="javascript:detailDevice('<s:property value="mac"/>',
														'<s:property value="login_time"/>')">
						��ϸ��Ϣ
					</a>
				</td>
			</tr>
		</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=12 align=left>û�в�ѯ���ϱ����ݣ�</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="12" align="right" height="15">
				[ ͳ������ : <s:property value='queryCount' /> ]&nbsp;
				<lk:pages url="/gtms/stb/resource/stbCallHistoryACT!query.action" showType=""
					isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
</table>
</div>
