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
<title>������������(���ط���)</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	

	
	function ToExcel() {
		var task_id='<s:property value="task_id" />';
		var task_name='<s:property value="task_name" />';
		var page="<s:url value='/gtms/config/stackRefreshTools!queryTaskGyCityExcel.action'/>"+"?task_id="+task_id+"&&task_name="+task_name;
		document.all("childFrm").src=page;
	}
	
	
	
	
	/* function detail(type,task_id){
		var url = "<s:url value='/gwms/resource/batchSoftUpACT!queryTaskGyCity.action'/>";
		$.post(url,{
			task_id:task_id,
			task_name:task_name
		},function(ajax){
			if("1" == ajax){
				alert("�޸ĳɹ�");
				window.parent.query();
			}else if("0" == ajax){
				alert("�޸�ʧ��");
			}
		});
	} */
	
	function deviceDetail(type,countNum,city_id){
		var task_id='<s:property value="task_id" />';
		var url = "<s:url value='/gtms/config/stackRefreshTools!queryDevList4CQ.action?countNum='/>"+countNum+"&&type="+type+"&&city_id="+city_id+"&&task_id="+task_id;
		window.open(url ,"","left=140,top=80,width=1000,height=600,resizable=yes,scrollbars=yes");
	}

</script>

</head>

<body >
<table class="listtable">
	<caption>��ѯ���</caption>
	<thead>
		<tr>
			<th>��������</th>
			<th>����</th>
			<th>����</th>
			<th>�ɹ���</th>
			<th>ʧ����</th>
			<th>δ����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="taskByCityList!=null">
			<s:if test="taskByCityList.size()>0">
				<s:iterator value="taskByCityList">
					<tr>
						<td align="center"><s:property value="task_name" /></td>
						<td align="center"><s:property value="city_name" /></td>
						<td align="center"><a class='green_link' href="#" onclick="deviceDetail('total','<s:property value="totalNum" />','<s:property value="city_id" />')"><s:property value="totalNum" /></a></td>
						<td align="center"><a class='green_link' href="#" onclick="deviceDetail('succ','<s:property value="succNum" />','<s:property value="city_id" />')"><s:property value="succNum" /></a></td>
						<td align="center"><a class='green_link' href="#" onclick="deviceDetail('fail','<s:property value="failNum" />','<s:property value="city_id" />')"><s:property value="failNum" /></a></td>
						<td align="center"><a class='green_link' href="#" onclick="deviceDetail('unDone','<s:property value="unDoneNum" />','<s:property value="city_id" />')"><s:property value="unDoneNum"/></a></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>û�в�ѯ�������Ϣ��</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>û�в�ѯ�������Ϣ��</td>
			</tr>
		</s:else>
		<tfoot>
			<tr>
				<td colspan="6" align="right">
					<lk:pages url="/gtms/config/stackRefreshTools!queryTaskGyCity.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr> 
			<tr>
				<td colspan="10" align="left">
					<IMG
					SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="ToExcel()">����
				</td>
			</tr>
			<tr STYLE="display: none">
				<td>
					<iframe id="childFrm" src=""></iframe>
				</td>
			</tr>
		</tfoot>
	</tbody>

	

</table>
</body>
</html>