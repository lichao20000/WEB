
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
	
	<script type="text/javascript">
function ListToExcel(city_id,reason_id,startOpenDate,endOpenDate) {
	var page="<s:url value='/itms/resource/VoiceFailResonQuery!voiceFailReasonDevQueryExcel.action'/>?"
		+"city_id="+city_id
		+"&reason=" + reason_id 
		+"&startOpenDate="+startOpenDate
		+"&endOpenDate=" +endOpenDate;
	document.all("childFrm").src=page;
}
</script>

<table class="listtable">
	<caption>
		�����豸ͳ����ϸ��Ϣ
	</caption>
	<thead>
		<tr>
			<th>����</th>
			<th>LOID</th>
			<th>�ն����к�</th>
			<th>�ն��ͺ�</th>
			<th>�����˿��Ƿ�����</th>
			<th>�����˿ں���</th>
			<th>����ע��ɹ�״̬</th>
			<th>����ע��ʧ��ԭ��</th>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="voicefailDeviceMap.size()>0">
			<s:iterator value="voicefailDeviceMap">
				<tr>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="loid" />
					</td>
					<td>
						<s:property value="device_serialnumber" />
					</td>
					<td>
						<s:property value="device_type" />
					</td>
					<td>
						�˿�[<s:property value="line_id" />]:
						<s:property value="enabled" />
					</td>
					<td>
						<s:property value="voip_phone" />
					</td>
					<td>
						<s:property value="status" />
					</td>
					<td>
						<s:property value="reason" />
					</td>
					<td>
						<s:property value="add_time" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=9>
					û��������Ϣ
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan=9>
				<span style="float: right;"> <lk:pages
						url="/itms/resource/VoiceFailResonQuery!voiceFailReasonDevQueryInfo.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="city_id"/>','<s:property value="reason_id"/>','<s:property value="startOpenDate"/>','<s:property value="endOpenDate"/>')"> 
			</td>
		</tr>

		<TR>
			<TD align="center" colspan=9>
				<button onclick="javascript:window.close();">
					&nbsp;�� ��&nbsp;
				</button>
			</TD>
		</TR>
	</tfoot>
	
	<tr STYLE="display: none">
		<td colspan="8">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
