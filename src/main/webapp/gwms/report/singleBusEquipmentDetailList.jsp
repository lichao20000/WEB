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


function ListToExcel(cityId, servTypeId, starttime1, endtime1, totalRowCount) {
    if(parseInt(totalRowCount) > 100000) {
        alert("������̫��֧�ֵ���");
        return;
    }
	var page="<s:url value='/gwms/report/singleBusEquipment!getSingleBusEquipmentDetailExcel.action'/>?"+ "cityId=" + cityId +
		"&servTypeId=" + servTypeId  + "&starttime1=" + starttime1 + "&endtime1=" + endtime1;
	document.all("childFrm").src=page;
}

</script>

<table class="listtable">
	<caption>
		����ҵ��ͳ���豸��Դ��������
	</caption>
	<thead>
		<tr>
			<th>
				LOID
			</th>
			<th>
				����
			</th>
			<th>
				�û��˺�
			</th>
			<th>
				�豸���к�
			</th>
			<th>
				�豸����
			</th>
			<th>
				�豸�ͺ�
			</th>
			<th>
				Ӳ���汾
			</th>
			<th>
				����汾
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="singleBusEquipmentDetailList.size()>0">
			<s:iterator value="singleBusEquipmentDetailList">
				<tr>
					<td>
						<s:property value="loid" />
					</td>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="username" />
					</td>
					<td>
						<s:property value="device_name" />
					</td>
					<td>
						<s:property value="vendor_add" />
					</td>
					<td>
						<s:property value="device_model" />
					</td>
					<td>
						<s:property value="hardwareversion" />
					</td>
					<td>
						<s:property value="softwareversion" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="20">
					ϵͳû����ص��豸��Դ��Ϣ!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="20">
                <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
                     style='cursor: hand'
                     onclick="ListToExcel('<s:property value="cityId"/>', '<s:property value="servTypeId"/>', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', <s:property value='totalRowCount_splitPage'/>)">
                <span style="float: right;">
                    [ ͳ������ : <s:property value='totalRowCount_splitPage'/> ]&nbsp;
                    <lk:pages
						url="/gwms/report/singleBusEquipment!querySingleBusEquipmentDetailList.action" styleClass=""
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
