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
	function go(id,receive_date,gw_type){
		var strpage = "<s:url value='/itms/service/operateByHistoryQuery!getOperateMessage.action'/>?bss_sheet_id="+id+"&receive_date="+receive_date+"&gw_type="+gw_type;
		window.open(strpage,"","left=20,top=20,height=450,width=700,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
	}


	$(function() {
		parent.dyniframesize();
	});
	
	
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>ԭʼ������Ϣ</caption>
	<thead>
		<tr>
			<ms:inArea areaCode="sx_lt" notInMode="true">
			<th>loid</th>
			</ms:inArea>
			<ms:inArea areaCode="sx_lt" notInMode="false">
			<th>Ψһ��ʶ</th>
			</ms:inArea>
			<ms:inArea areaCode="cq_dx" notInMode="false">
			<th>���ӵ���</th>
			</ms:inArea>
			<th>����</th>
			<th>����ʱ��</th>
			<th>ҵ������</th>
			<th>ҵ���˺�</th>
			<th>��������</th>
			<th>ִ�н��</th>
			<ms:inArea areaCode="cq_dx" notInMode="true">
			<th>�������</th>
			</ms:inArea>
			<ms:inArea areaCode="cq_dx" notInMode="false">
			<th>�������</th>
			</ms:inArea>
			<th>������Դ</th>
			<ms:inArea areaCode="ah_lt" notInMode="false">
			<th>״̬</th>
			</ms:inArea>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="dataList!=null">
			<s:if test="dataList.size()>0">
				<s:iterator value="dataList">
						<tr>
							<td><s:property value="username" /></td>
							<ms:inArea areaCode="cq_dx" notInMode="false">
								<td><s:property value="bss_sheet_id" /></td>
							</ms:inArea>
							<td><s:property value="cityName" /></td>
							<td><s:property value="receive_date" /></td>
							<td><s:property value="servType" /></td>
							<td><s:property value="servUsername" /></td>
							<td><s:property value="resultType" /></td>
							<td> <font color="<s:property value='color'/>"><s:property value="resultId" /></font> </td>
							<ms:inArea areaCode="cq_dx" notInMode="true">
							<td><s:property value="returnt_context" /></td>
							</ms:inArea>
							<ms:inArea areaCode="cq_dx" notInMode="false">
							<td><s:property value="returnt_context_short" /></td>
							</ms:inArea>
							<td><s:property value="remark" /></td>
							<ms:inArea areaCode="ah_lt" notInMode="false">
							<td><s:property value="statusDesc" /></td>
							</ms:inArea>
							<ms:inArea areaCode="cq_dx" notInMode="true">
							<td><a href="javaScript:go('<s:property value='bss_sheet_id'/>','','<s:property value="gw_type" />');" >��������</a>  </td>
							</ms:inArea>
							<ms:inArea areaCode="cq_dx" notInMode="false">
							<td><a href="javaScript:go('<s:property value='bss_sheet_id'/>','<s:property value="receive_date_long" />','<s:property value="gw_type" />');" >��������</a>  </td>
							</ms:inArea>
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
				<td colspan=10>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<ms:inArea areaCode="cq_dx" notInMode="true">
		<tr>
			<td colspan="10" align="right">
		 	<lk:pages
				url="/itms/service/operateByHistoryQuery!getOperateByHistoryInfoStb.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
		</ms:inArea>
		<ms:inArea areaCode="cq_dx" notInMode="false">
		<tr>
			<td colspan="11" align="right">
		 	<lk:pages
				url="/itms/service/operateByHistoryQuery!getOperateByHistoryInfoStb.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
		</ms:inArea>
	</tfoot>
</table>
	
</body>
</html>