<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript">

function ListToExcel(flag,cityId,starttime1,endtime1,vendorId,deviceModelId,deviceTypeId,accessType,usertype) {
	var page="<s:url value='/gwms/report/deviceBindWay!getUserExcel.action'/>?"
		+ "flag=" + flag
		+ "&cityId=" + cityId 
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1
		+ "&vendorId=" +vendorId
		+ "&deviceModelId=" +deviceModelId
		+ "&deviceTypeId=" +deviceTypeId
		+ "&accessType=" +accessType
		+ "&usertype=" +usertype;
	document.all("childFrm").src=page;
}

//�鿴itms�û���ص���Ϣ
function itmsUserInfo(user_id){
	var strpage="<s:url value='/Resource/HGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}
</script>

<table class="listtable">
	<caption>
		�û��б�
	</caption>
	<thead>
		<tr>
			<th>
				�û��ʺ�
			</th>
			<th>
				�� ��
			</th>
			<th>
				�û���Դ
			</th>
			<th>
				���豸
			</th>
			<th>
				����ʱ��
			</th>
			<th>
				�󶨷�ʽ
			</th>
			<th>
				����
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
						<s:property value="user_type" />
					</td>
					<td>
						<s:property value="device" />
					</td>
					<td>
						<s:property value="opendate" />
					</td>
					<td>
						<s:property value="bindtype" />
					</td>
					<td>
						<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="��ϸ��Ϣ"
							onclick="itmsUserInfo('<s:property value="user_id"/>')"
							style="cursor: hand">
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7>
					ϵͳû����ص��û���Ϣ!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="7">
				<span style="float: right;"> <lk:pages
						url="/gwms/report/deviceBindWay!getUserList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="flag"/>','<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="vendorId"/>','<s:property value="deviceModelId"/>','<s:property value="deviceTypeId"/>','<s:property value="accessType"/>','<s:property value="usertype"/>')">
				<s:if test='#session.isReport=="1"'>
					
				</s:if>

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

<%@ include file="/foot.jsp"%>
