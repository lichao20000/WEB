<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ���ѯ</title>
<%
	/**
	 * ԭʼ������ѯ
	 * 
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2010-09-08
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	function go(id,isHistory){
		var strpage = "<s:url value='/itms/service/operateByHistoryCQOldQuery!getOperateMessage.action'/>?bss_sheet_id="+id+"&isHistory="+isHistory;
		window.open(strpage,"","left=20,top=20,height=550,width=800,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
	}


	$(function() {
		parent.dyniframesize();
	});
	
	
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>ԭʼ������Ϣ<font color="red"> (ע�����͹���ʱ�밴��ǰ����˳������ִ��)</font></caption>
	<thead>
		<tr>
			<th>������ˮ��</th>
			<th>ҵ������</th>
			<th>��������</th>
			<th>�û��˺�</th>
			<th>�豸���к�</th>
			<th>�û��߼�ID</th>
			<th>ִ�����</th>
			<th>����ʱ��</th>
			<th>���ʱ��</th>
			<th>����</th>
			<!-- <th>��ʱʱ��</th> -->
			<th>�ͻ���</th>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="dataList!=null">
			<s:if test="dataList.size()>0">
				<s:iterator value="dataList">
						<tr>
							<td><s:property value="WORK_ASGN_ID" /></td>
							<td><s:property value="SERVICE_TYPE" /></td>
							<td><s:property value="SERVICE_OPT" /></td>
							<td><s:property value="ACCOUNT_NAME" /></td>
							<td><s:property value="SERIAL_NUMBER" /></td>
							<td><s:property value="LOID" /></td>
							<td> <font color="<s:property value='color'/>"><s:property value="STATUS" /></font> </td>
							<td><s:property value="ARRIVE_TIME" /></td>
							<td><s:property value="DEAL_TIME" /></td>
							<td><a href="javaScript:go('<s:property value='WORK_ASGN_ID'/>','<s:property value='isHistory'/>');" >����</a>  </td>
							<%-- <td><s:property value="DURING_TIME" />��</td> --%>
							<td><s:property value="CUSTOMER_ID" /></td>
							<td><s:property value="cityName" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=10>ϵͳû�и��û���ҵ����Ϣ!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=13>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	
	<%-- <tfoot>
		<tr>
			<td colspan="13" align="right">
		 	<lk:pages
				url="/itms/service/operateByHistoryCQOldQuery!getOperateByHistoryInfo.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot> --%>
</table>
	
</body>
</html>