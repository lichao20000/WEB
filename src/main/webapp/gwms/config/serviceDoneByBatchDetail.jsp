<%@  page  language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	

	
	function ToExcel() {
		var task_id='<s:property value="task_id" />';
		var serv_type='<s:property value="servTypeId" />';
		var page="<s:url value='/gwms/config/serviceManSheet!queryTaskDetailExcel.action'/>"+"?task_id="+task_id+"&&servTypeId="+encodeURI(encodeURI(serv_type));
		document.all("childFrm").src=page;
	}

	
	function deviceDetail(type,countNum,city_id){
		var task_id='<s:property value="task_id" />';
		var url = "<s:url value='/gwms/config/serviceManSheet!queryDevList.action?countNum='/>"+countNum+"&&type="+type+"&&city_id="+city_id+"&&task_id="+task_id;
		window.open(url ,"","left=140,top=80,width=1000,height=600,resizable=yes,scrollbars=yes");
	}

</script>

<table class="listtable">
	<caption>查询结果</caption>
	<thead>
		<tr>
			<th>业务类型</th>
			<th>属地</th>
			<th>总数</th>
			<th>成功数</th>
			<th>失败数</th>
			<th>未做数</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="taskList!=null">
			<s:if test="taskList.size()>0">
				<s:iterator value="taskList">
					<tr>
						<s:if test="servTypeNme==null || servTypeNme==''">
							<td align="center">全部</td>
						</s:if>
						<s:else>
							<td align="center"><s:property value="servTypeNme" /></td>
						</s:else>
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
					<td colspan=6>没有查询到相关信息！</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>没有查询到相关信息！</td>
			</tr>
		</s:else>
		<tfoot>
			<tr>
				<td colspan="6" align="right">
					<lk:pages url="/gwms/config/serviceManSheet!queryTaskDetail.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr> 
			<%--<tr>
				<td colspan="10" align="left">
					<IMG
					SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel()">导出
				</td>
			</tr>--%>
			<tr STYLE="display: none">
				<td>
					<iframe id="childFrm" src=""></iframe>
				</td>
			</tr>
		</tfoot>
	</tbody>

</table>
