<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/inmp/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/inmp/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/inmp/edittable.js"/>"></SCRIPT>
<script type="text/javascript">

function ListToExcel(isItv,cityId,starttime1,endtime1,flag,isNew) {
	var prod_spec_id='<s:property value="prodSpecId" />';
	var page="<s:url value='/inmp/report/PVCReport!getHgwExcel.action'/>?"
		+ "cityId=" + cityId 
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1
		+ "&reform_flag=" +flag
		+ "&isNew=" + isNew
		+ "&isItv=" + isItv
		+ "&prodSpecId=" + prod_spec_id;
	document.all("childFrm").src=page;
}

</script>

<table class="listtable">
	<caption>
		�û��б�
	</caption>
	<thead>
		<tr>
			<th>
				����˺�
			</th>
			<th>
				����
			</th>
			<th>
				�ͻ�ID
			</th>
			<th>
				�ͻ�����
			</th>
			<th>
				���з�ʽ
			</th>
			<th>
				IPTV��Чʱ��
			</th>
			<th>
				BAS��ַ
			</th>
			<th>
				VLANֵ
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="hgwList.size()>0">
			<s:iterator value="hgwList">
				<tr>
					<td>
						<s:property value="username" />
					</td>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="customer_id" />
					</td>
					<td>
						<s:property value="customer_name" />
					</td>
					<td>
						<s:property value="prod_spec_name" />
					</td>
					<td>
						<s:property value="completedate" />
					</td>
					<td>
						<s:property value="bas_ip" />
					</td>
					<td>
						<s:property value="vlanid" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7>
					ϵͳû����ص�PVC�û���Ϣ!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="8">
				<span style="float: right;"> <lk:pages
						url="/inmp/report/PVCReport!getHgw.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
					<IMG SRC="<s:url value="/images/inmp/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="isItv"/>','<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="reform_flag"/>','<s:property value="isNew"/>')">
			</td>
		</tr>


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

<%@ include file="../foot.jsp"%>
