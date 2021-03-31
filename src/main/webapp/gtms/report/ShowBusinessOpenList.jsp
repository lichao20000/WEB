<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>�û�ҵ���б�</title>
		
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"	type="text/css">
		<script language="JavaScript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script language="JavaScript" src="<s:url value="/Js/edittable.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
		<script type="text/javascript">
			function ListToExcel(openStatus,cityId,start,end,parentCityId,selectTypeId) {
				var page="<s:url value='/gtms/report/businessOpen!getUserExcel.action'/>?"
					+ "openStatus=" + openStatus
					+ "&cityId=" + cityId 
					+ "&start=" +start
					+ "&end=" +end
					+ "&parentCityId=" +parentCityId
					+ "&selectTypeId=" +selectTypeId;
				document.all("childFrm").src=page;
			}
		</script>
	</head>
	
	<body>
		<table class="listtable">
			<caption>�û�ҵ���б�</caption>
			<thead>
				<tr>
					<th>�߼�SN</th>
					<th>����</th>
					<th>ҵ��</th>
					<th>ҵ��ͨ״̬</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="businessOpenUserList.size()>0">
					<s:iterator value="businessOpenUserList">
						<tr>
							<td align="center"><s:property value="username" /></td>
							<td align="center"><s:property value="cityName" /></td>
							<td align="center"><s:property value="servTypeName" /></td>
							<td align="center">
								<s:if test='openStatus=="1"'>�ѿ�ͨ</s:if>
								<s:elseif test='openStatus=="0"'>δ��ͨ</s:elseif>
								<s:elseif test='openStatus=="-1"'>��ͨʧ��</s:elseif>
								<s:else>δ֪</s:else>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=9>ϵͳû����ص��û�ҵ����Ϣ!</td>
					</tr>
				</s:else>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="9">
						<span style="float: right;"> 
							<lk:pages url="/gtms/report/businessOpen!getUserList.action" styleClass=""
								showType="" isGoTo="true" changeNum="true" />
						</span>
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
							style='cursor: hand'
							onclick="ListToExcel('<s:property value="openStatus"/>','<s:property value="cityId"/>','<s:property value="start"/>','<s:property value="end"/>','<s:property value="parentCityId"/>','<s:property value="selectTypeId"/>')">
						
					</td>
				</tr>
		
				<TR>
					<TD align="center" colspan="9">
						<button onclick="javascript:window.close();">&nbsp;�� ��&nbsp;</button>
					</TD>
				</TR>
			</tfoot>
		
			<tr STYLE="display: none">
				<td colspan="9"><iframe id="childFrm" src=""></iframe></td>
			</tr>
		</table>
	</body>
	<%@ include file="/foot.jsp"%>
</html>