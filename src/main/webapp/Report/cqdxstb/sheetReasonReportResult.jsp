<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
	
<script>
$(function() {
		parent.dyniframesize();
		$("#querybtn",parent.document).attr("disabled","");
});
	
var deviceType;
var startOpenDate;
var endOpenDate;
var servTypeId;

function getDetail(city_id, open_status)
{
	var page = "<s:url value='/itms/report/sheetStatsReport!getDetailReport.action'/>?cityId="+city_id+"&openStatus="+open_status;
	
	// ��һ����ȡ��ҳ��ļ�����ѯ����
	getCondition();	
	page += "&deviceType=" + deviceType + "&startOpenDate=" + startOpenDate + "&endOpenDate=" + endOpenDate + "&servTypeId=" + servTypeId;
	window.open(page,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}

function ToExcel()
{
	var form = parent.document.getElementsByName("selectForm")[0];
	form.action = "<s:url value='/itms/report/sheetStatsReport!getExcel.action'/>";
	form.target = "_self";
	form.submit();
}

// ��ȡ��ҳ��Ĳ�ѯ����
function getCondition()
{
	deviceType = window.parent.document.getElementsByName("deviceType")[0].value;
	startOpenDate = window.parent.document.getElementsByName("startOpenDate")[0].value;
	endOpenDate = window.parent.document.getElementsByName("endOpenDate")[0].value;
	servTypeId = window.parent.document.getElementsByName("servTypeId")[0].value;
}
</script>
<table class="listtable">
	<caption>
		��ѯ���
	</caption>
	<thead>
		<tr>
			<th>
				����/ԭ��
			</th>
			<th>
				�����ɹ�
			</th>
			<th>
				LOID����Ϊ��
			</th>
			<th>
				work_asgn_idΪ��
			</th>
			<th>
				�޶�Ӧ���û���Ϣ
			</th>
			<th>
				Order_RemarkΪ��
			</th>
			<th>
				�������Ϊ��
			</th>
			<th>
				��������Ϊ��
			</th>
			<th>
				ҵ���˺Ż�ҵ������Ϊ��
			</th>
			<th>
				�豸IDΪ��
			</th>
			<th>
				STBIDΪ�ջ��32λ
			</th>
			<th>
				�豸��������Ϊ��
			</th>
			<th>
				���ŷ�ʽΪ��
			</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="statsReportList">
			<tr>
				<td>
					<s:property value="city_name"  />
				</td>
				<td>
					<s:property value="r0" />
				</td>
				<td>
					<s:property value="r03" />
				</td>
				<td>
					<s:property value="r02" />
				</td>
				<td>
					<s:property value="r25" />
				</td>
				<td>
					<s:property value="r32" />
				</td>
				<td >
					<s:property value="r33" />
				</td>
				<td >
					<s:property value="r34" />
				</td>
				<td >
					<s:property value="r45" />
				</td>
				<td >
					<s:property value="r47" />
				</td>
				<td >
					<s:property value="r48" />
				</td>
				<td >
					<s:property value="r49" />
				</td>
				<td >
					<s:property value="r50" />
				</td>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="13">
				<%-- <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand'
					onclick="ToExcel()"> --%>
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="13">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>