<%@  page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�·������б�</title>

	<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
	<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
	<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
	<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">

	$(function(){
		parent.dyniframesize();
	});

	function del(task_id){
		var  bln1   =   window.confirm("ȷ��Ҫɾ��������(����Ǽ���)");  
	    if(bln1 != true) {
	    	return;
	    }
	    
		var url = "<s:url value='/gwms/config/serviceManSheet!del.action'/>";
		$.post(url,{
			task_id:task_id
		},function(ajax){
			if("1" == ajax){
				alert("ɾ���ɹ�");
				window.parent.query();
			}else if("0" == ajax||"" == ajax){
				alert("ɾ��ʧ��");
			}
		});
	}
	
	
	function detail(task_id,serv_type){
		
		var url = "<s:url value='/gwms/config/serviceManSheet!queryTaskDetail.action?task_id='/>"+task_id+"&&servTypeId="+encodeURI(encodeURI(serv_type));
		window.open(url ,"","left=80,top=80,width=800,height=600,resizable=yes,scrollbars=yes");

	}
	

	

</script>

</head>

<body >
<table class="listtable">
	<caption>��ѯ���</caption>
	<thead>
		<tr>
			<th>ҵ������</th>
			<th>״̬</th>
			<th>����ʱ��</th>
			<th>����</th>
			<th>�ɹ���</th>
			<th>ʧ����</th>
			<th>δ����</th>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="taskList!=null">
			<s:if test="taskList.size()>0">
				<s:iterator value="taskList">
					<tr>
						<s:if test="servTypeNme==null || servTypeNme==''">
							<td align="center">ȫ��</td>
						</s:if>
						<s:else>
						<td align="center"><s:property value="servTypeNme" /></td>
						</s:else>
						<s:if test="status==1">
							<td align="center">����</td>
						</s:if>
						<s:else>
							<td align="center">��ͣ</td>
						</s:else>
						<td align="center"><s:property value="add_time" /></td>
						<td align="center"><s:property value="totalNum" /></td>
						<td align="center"><s:property value="succNum" /></td>
						<td align="center"><s:property value="failNum" /></td>
						<td align="center"><s:property value="unDoneNum"/></td>
						<td align="center">
						<a href="javascript:del('<s:property value="task_id" />')">ɾ��</a>
						<a href="javascript:detail('<s:property value="task_id" />','<s:property value="servTypeId" />')">����</a>
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=8>û�в�ѯ�������Ϣ��</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=8>û�в�ѯ�������Ϣ��</td>
			</tr>
		</s:else>
	</tbody>

	<tfoot>
			<tr>
				<td colspan="8" align="right">
					<lk:pages url="/gwms/config/serviceManSheet!queryList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
		</tfoot>
</table>
</body>
</html>