<%@  page  language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<%--
	/**
	 * �淶�汾��ѯ�б�ҳ��
	 *
	 * @author ����(����) Tel:�绰
	 * @version 1.0
	 * @since ${date} ${time}
	 * 
	 * <br>��Ȩ���Ͼ������Ƽ� ���ܿƼ���
	 * 
	 */
 --%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�淶�汾��ѯ</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	
	$(function(){
		parent.dyniframesize();
	});

	function updateStatus(upTaskName,upStatus){
		if (confirm("��ȷ���޸Ĵ�����״̬!")) {
			if("��" == upStatus){
			upStatus ="1";
		}else{
			upStatus ="0";
		}
		var url = "<s:url value='/gwms/resource/batchSoftUpACT!updateStatus.action'/>";
		$.post(url,{
		upTaskName:upTaskName,
		upStatus:upStatus
	},function(ajax){
		if("1" == ajax){
			alert("�޸ĳɹ�");
			window.parent.query();
		}else if("0" == ajax){
			alert("�޸�ʧ��");
		}
	});
		}
		
}

</script>

</head>

<body >
<table class="listtable">
	<caption>��ѯ���</caption>
	<thead>
		<tr>
			<th>��������</th>
			<th>�����豸��</th>
			<th>�ɹ���</th>
			<th>δ����</th>
			<th>�Ƿ�ִ����</th>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="taskList!=null">
			<s:if test="taskList.size()>0">
				<s:iterator value="taskList">
					<tr>
						<td align="center"><s:property value="task_name" /></td>
						<td align="center"><s:property value="totalNum" /></td>
						<td align="center"><s:property value="doneNum" /></td>
						<td align="center"><s:property value="unDoneNum"/></td>
						<td align="center"><s:property value="status"/></td>
						<td align="center"><a href="javascript:updateStatus('<s:property value="task_name" />','<s:property value="status" />')">�޸�</a></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>û�в�ѯ����ذ汾��</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>û�в�ѯ����ذ汾��</td>
			</tr>
		</s:else>
		<tfoot>
			<tr>
				<td colspan="6" align="right">
					<lk:pages url="/gwms/resource/batchSoftUpACT!queryList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
		</tfoot>
	</tbody>

	

</table>
</body>
</html>