<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ���ѯ</title>
<%
	/**
	 * e8-cҵ���ѯ
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
	$(function() {
		parent.dyniframesize();
	});
	
	function ToExcel(){
			parent.ToExcel();
	}
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>�ֹ�����������ѯ��־��Ϣ</caption>
	<thead>
		<tr>
			<th>��������loid</th>
			<th>�󶨵��豸���к�</th>
			<th>����</th>
			<th>��������</th>
			<th>��������</th>
			<th>����ִ�н��</th>
			<th>����ʱ��</th>
			<th>������</th>
			<th>�����˵�¼ip</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="operateList!=null">
			<s:if test="operateList.size()>0">
				<s:iterator value="operateList">
						<tr>
							<td><s:property value="user_Name" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="city_name" /></td>
							<td><s:property value="serv_type" /></td>
							<td><s:property value="oper_type" /></td>
							<td><s:property value="result_id" /></td>
							<td><s:property value="oper_time" /></td>
							<td><s:property value="acc_loginname" /></td>
							<td><s:property value="occ_ip" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9>ϵͳû�и��û���ҵ����Ϣ!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=9>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="9" align="right">
		 	<lk:pages
				url="/gtms/service/operateByHandQuery!getOperateByHandInfo.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>

		<tr>
			<td colspan="9" align="right"><IMG SRC="/itms/images/excel.gif"
				BORDER='0' ALT='�����б�' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>