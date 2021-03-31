<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<%
String isJs= LipossGlobals.getLipossProperty("InstArea.ShortName");
String startDealdate=request.getParameter("startDealdate1");
String endDealdate=request.getParameter("endDealdate1");
String type=request.getParameter("type");
%>

<script type="text/javascript">

function ListToExcel(cityId,isActive,starttime1,endtime1,type) {
	var page="<s:url value='/itms/report/E8CActiveReport!getCustomerListExcel.action'/>?"
		+ "&startDealdate1=" + starttime1
		+ "&endDealdate1=" + endtime1
		+ "&isActive=" + isActive
		+ "&type=" + type
		+ "&cityId=" + cityId;
	document.all("childFrm").src=page;
}

</script>
<title>�û���ϸ�б�</title>
<input type="hidden" name="startDealdate" value="<%=startDealdate %>" />
<input type="hidden" name="endDealdate" value="<%=endDealdate %>" />
<table class="listtable">
	<caption>
		�û��б�
	</caption>
	<thead>
		<tr>
			<th>
				�û��˺�
			</th>
			<th>
				����
			</th>
			<th>
				�û��Ƿ��Ծ
			</th>
			<th>
				�豸���к�
			</th>
			<th>
				����ʱ��
			</th>
			<th>
				�������ʱ��
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="list.size()>0">
			<s:iterator value="list">
				<tr>
					<td>
						<s:property value="LOID" />
					</td>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="active_name" />
					</td>
					<td>
						<s:property value="serial" />
					</td>
					<td>
						<s:property value="opendate" />
					</td>
					<td>
						<s:property value="last_time" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>
					ϵͳû����ص��û���Ϣ!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
	<s:if test="list!=null">
		<s:if test="list.size()>0">
		<tr>
			<td colspan="8">
				<span style="float: right;"> <lk:pages
						url="/itms/report/E8CActiveReport!goPage.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
					 <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="cityId"/>','<s:property value="isActive"/>','<s:property value="startDealdate1"/>','<s:property value="endDealdate1"/>','<s:property value="type"/>')">

			</td>
		</tr>
			</s:if>
			</s:if>

		<TR>
			<TD align="center" colspan="8">
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

<%@ include file="/foot.jsp"%>
