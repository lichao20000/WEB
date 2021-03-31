<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>

<script type="text/javascript">


function ListToExcel(cityId, numType, downlink, taskName, starttime1, endtime1) {
	var page="<s:url value='/gwms/report/queryBatchSpeedMeasure!getBatchSpeedMeasureDetailExcel.action'/>?"+ "cityId=" + cityId +
		"&numType=" + numType + "&downlink=" + downlink + "&taskNameEncode=" + encodeURI(encodeURI(taskName)) + "&starttime1=" + starttime1 + "&endtime1=" + endtime1;
	document.all("childFrm").src=page;
}

</script>

<table class="listtable">
	<caption>
		�������ٽ������
	</caption>
	<thead>
		<tr>
			<th>
				CITY_NAME
			</th>
			<th>
				LOID
			</th>
			<th>
				KD
			</th>
			<th>
				������ʽ
			</th>
			<th>
				DEVICE_NAME
			</th>
			<th>
				����ǩԼ����
			</th>
			<th>
				PON����
			</th>
			<th>
				����
			</th>
			<th>
				�ͺ�
			</th>
			<th>
				����汾
			</th>
			<th>
				Ӳ���汾
			</th>
			<th>
				������������
			</th>
			<th>
				�Ƿ�ǧ��è
			</th>
			<th>
				�Ƿ�֧�ֲ���
			</th>
			<th>
				����ʱ��
			</th>
			<th>
				���Խ��
			</th>
			<%--<th>
				���Խ��1
			</th>--%>
			<th>
				���ٽ��
			</th>
			<th>
				��������
			</th>
			<th>
				������
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="batchSpeedMeasureDetailList.size()>0">
			<s:iterator value="batchSpeedMeasureDetailList">
				<tr>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="loid" />
					</td>
					<td>
						<s:property value="KD" />
					</td>
					<td>
						<s:property value="onlineType" />
					</td>
					<td>
						<s:property value="device_name" />
					</td>
					<td>
						<s:property value="downlink" />
					</td>
					<td>
						<s:property value="PON_TYPE" />
					</td>
					<td>
						<s:property value="vendor_add" />
					</td>
					<td>
						<s:property value="device_model" />
					</td>
					<td>
						<s:property value="softwareversion" />
					</td>
					<td>
						<s:property value="hardwareversion" />
					</td>
					<td>
						<s:property value="device_version_type" />
					</td>
					<td>
						<s:property value="gbbroadband" />
					</td>
					<td>
						<s:property value="is_speedtest" />
					</td>
					<td>
						<s:property value="test_time" />
					</td>
					<%--<td>
						<s:property value="http_result" />
					</td>--%>
					<td>
						<s:property value="http_result_one" />
					</td>
					<td>
						<s:property value="total_down_pert_deal" />
					</td>
					<td>
						<s:property value="speedQuality" />
					</td>
					<td>
						<s:property value="accessStandard" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="20">
					ϵͳû����صĲ��ٽ����Ϣ!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="20">
                <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
                     style='cursor: hand'
                     onclick="ListToExcel('<s:property value="cityId"/>', '<s:property value="numType"/>', '<s:property value="downlink"/>', '<s:property value="taskName"/>', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>')">
                <span style="float: right;">
                    [ ͳ������ : <s:property value='totalRowCount_splitPage'/> ]&nbsp;
                    <lk:pages
						url="/gwms/report/queryBatchSpeedMeasure!queryBatchSpeedMeasureDetailList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
			</td>
		</tr>


		<TR>
			<TD align="center" colspan="20">
				<button onclick="javascript:window.close();">
					&nbsp;�� ��&nbsp;
				</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="20">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>
