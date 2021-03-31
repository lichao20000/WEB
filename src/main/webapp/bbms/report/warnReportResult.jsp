<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��ҵ����ά��Ԥ��</title>
<%
	/**
	 * ��ҵ����ά��Ԥ����ѯ���ҳ��
	 * 
	 * @author chenjie
	 * @since 2012-12-06
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
	$(function() {
		parent.dyniframesize();
	});


	function ToExcel(){
		parent.ToExcel();
	}

	function GoContent(user_id,gw_type){
	 	 //gw_type = window.parent.document.getElementsByName("gw_type")[0].value;
		 if(parseInt(gw_type)==2){
		 	var strpage="<s:url value='/Resource/EGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
		 }else{
			var strpage="<s:url value='/Resource/HGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
		 }
		 window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>��ҵ����ά��Ԥ����ѯ</caption>
	<thead>
		<tr>
			<th>�û���</th>
			<th>������</th>
			<th>�ͻ���ϵ�绰</th>
			<th>װ����ַ</th>
			<th>������Ʒ����</th>
			<th>װ������</th>
			<th>�豸����</th>
			<th>�豸����</th>
			<th>��������</th>
			<th>��ҵ����</th>
			<th>Ԥ��ԭ��</th>
			<th>Ԥ��ʱ��</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="warnReportList!=null">
			<s:if test="warnReportList.size()>0">
				<s:iterator value="warnReportList">
					<tr>
						<td><a
							href="javascript:GoContent('<s:property value="user_id" />',2);">
						<s:property value="username" /> </a></td>
						<td><s:property value="city_name" /></td>
						<td><s:property value="linkphone" /></td>
						<td><s:property value="customer_address" /></td>
						<td><s:property value="product_type" /></td>
						<td><s:property value="opendate" /></td>
						<td><s:property value="vendor_name" /></td>
						<td><s:property value="device_type" /></td>
						<td><s:property value="adsl_hl_str" /></td>
						<td><s:property value="industry" /></td>
						<td><s:property value="warning_reason" /></td>
						<td><s:property value="warning_date" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=12>ϵͳû�в�ѯ�������Ϣ��</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=12>ϵͳû�в�ѯ�������Ϣ��</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>

			<td colspan="12" align="right">
				&nbsp;&nbsp;&nbsp;&nbsp; <lk:pages
				url="/bbms/report/warnReportACT!queryWarnReport.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>

		<!-- 
		<tr>
			<td colspan="8" align="right"><IMG SRC="/itms/images/excel.gif"
				BORDER='0' ALT='�����б�' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
		 -->
	</tfoot>
</table>
</body>
</html>