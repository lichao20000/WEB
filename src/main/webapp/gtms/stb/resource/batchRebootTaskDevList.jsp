<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>

<title>��������������-�豸��ϸ</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
function toExcel()
{
	var taskId = $("input[name=taskId]").val();
	var cityId = $("input[name=cityId]").val();
	var status = $("input[name=status]").val();
	var url = "<s:url value='/gtms/stb/resource/batchReboot!exportDevList.action'/>"
				+ "?taskId="+taskId
				+ "&cityId="+cityId
				+ "&status="+status;
    window.location.href=url;
}
</SCRIPT>
<div>
<input type="hidden" name="taskId" value="<s:property value='taskId'/>">
<input type="hidden" name="cityId" value="<s:property value='cityId'/>">
<input type="hidden" name="status" value="<s:property value='status'/>">
<table class="listtable">
	<caption>ͳ�ƽ��</caption>
	<thead>
		<tr>
			<th width="8%">����</th>
			<th width="8%">APK�汾</th>
			<th width="10%">�豸���к�</th>
			<th width="10%">ҵ���˺�</th>
			<th width="8%">MAC��ַ</th>
			<th width="6%">����</th>
			<th width="8%">�ͺ�</th>
			<th width="9%">Ӳ���汾</th>
			<th width="9%">����汾</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data!=null && data.size()>0">
			<s:iterator value="data">
				<tr align="center">
					<td><s:property value="cityName"/></td>
					<td><s:property value="version"/></td>
					<td><s:property value="device_serialnumber"/></td>
					<td><s:property value="serv_account"/></td>
					<td><s:property value="mac"/></td>
					<td><s:property value="vendorName"/></td>
					<td><s:property value="deviceModel"/></td>
					<td><s:property value="hardwareversion"/></td>
					<td><s:property value="softwareversion"/></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=9 align=left>û�в�ѯ��������ݣ�</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td align="left">
				<img src="<s:url value='/images/excel.gif'/>" border='0' alt='�����б�'
							style='cursor: hand' onclick="toExcel()" align=left />
			</td>
			<td colspan="8" align="right" height="15">
				<lk:pages url="/gtms/stb/resource/batchReboot!queryDevList.action"
						styleClass="" showType="" isGoTo="true" changeNum="false" />
			</td>
		</tr>
	</tfoot>
</table>
</div>
