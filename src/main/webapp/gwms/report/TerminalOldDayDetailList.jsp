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

<!--��һ���Ͼ��ն������������-->
function ListToExcel(cityId) {
	var page="<s:url value='/gwms/report/terminalNoMatchReport!getOldDayCompleteDevDetailExcel.action'/>?"+ "cityId=" + cityId;
	document.all("childFrm").src=page;
}

</script>

<table class="listtable">
	<caption>
		��������豸�б�
	</caption>
	<thead>
		<tr>
			<th>
				Ӫҵ��
			</th>
			<th>
				LOID
			</th>
			<th>
				����˺�
			</th>
			<th>
				����
			</th>
			<th>
				�ͺ�
			</th>
			<th>
				�������ʱ��
			</th>
			<th>
				��ע
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="oldDevDetailList.size()>0">
			<s:iterator value="oldDevDetailList">
				<tr>
					<td>
						<s:property value="cityName" />
					</td>
					<td>
						<s:property value="loid" />
					</td>
					<td>
						<s:property value="username" />
					</td>
					<td>
						<s:property value="vendorName" />
					</td>
					<td>
						<s:property value="deviceModelName" />
					</td>
					<td>
						<s:property value="lastOnlineTime" />
					</td>
					<td>
						<s:property value="remark" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="7">
					ϵͳû����ص��豸��Ϣ!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="7">
				<span style="float: right;"> <lk:pages
						url="/gwms/report/terminalNoMatchReport!queryOldDayCompleteDevDetailList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="cityId"/>')">
			</td>
		</tr>


		<TR>
			<TD align="center" colspan="7">
				<button onclick="javascript:window.close();">
					&nbsp;�� ��&nbsp;
				</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="5">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>
