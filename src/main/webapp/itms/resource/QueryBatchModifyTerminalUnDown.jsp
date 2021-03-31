<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
    <%@ taglib prefix="s" uri="/struts-tags"%>
    <%@ taglib prefix="lk" uri="/linkage"%>
     <%String absPath=request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>为触发数展示页面</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
	<script type="text/javascript">
	function unDoneNumExcel(task_name,add_time,type)
	{
	
		var page="<s:url value='/itms/resource/QueryBatchModifyTerminal!unexecuteExcel.action'/>?"
			+"&task_name="+task_name
			+"&add_time="+add_time
			+"&type="+type;
		document.all("childFrm").src=page;
	}
	</script>

</head>
<body>
<form action="">
<input type="hidden" name="add_time" id="add_time" value='<s:property value="add_time"/>'>
<input type="hidden" name="task_name" id="task_name" value='<s:property value="task_name"/>'>
<input type="hidden" name="unDoneNum" id="unDoneNum" value='<s:property value="unDoneNum"/>'>
<input type="hidden" name="type" id="type" value='<s:property value="type"/>'>
</form>
<table class="listtable" id="listTable">
<caption>查询结果</caption>
	<thead>
		<tr>
			<th>属地</th>
			<th>厂家</th>
			<th>型号</th>
			<!-- <th>ITMS接收时间</th> -->
			<th>软件版本</th>
			<th>硬件版本</th>
			<th>特别版本</th>
		</tr>
	</thead>

<s:if test="date!=null">	
<s:if test="date.size()>0">		
				<s:iterator value="date">
						<tr>
							<td >							
								<s:property value="city_name" />
							</td>
							<td >
								<s:property value="vendor_name"/>
							</td>
							<td >
									<s:property value="device_model" />
							</td>
							<td >
								<s:property value="softwareversion"/>
							</td>
							<td >
								<s:property value="hardwareversion"/>
							</td>
							<td>
								<s:property value="specversion"/>
							</td>
						</tr>
				</s:iterator>
					</s:if>
				<s:else>
				<tr>
					<td colspan="8">系统中没有查询出需要的信息!</td>
				</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan="6">系统中没有查询出需要的信息!</td>
				</tr>
			</s:else>		
<table>
<tr>
<td colspan="6" align="left">
<img alt="导出表格" src="<%=absPath%>/images/excel.gif" border="0" style='cursor: hand' onclick="unDoneNumExcel( '<s:property value="task_name"/>','<s:property value="add_time"/>','<s:property value="type" />')"> 
</td>
<td colspan="6" align="right">
<lk:pages url="itms/resource/QueryBatchModifyTerminal!unDoneNumQuery.action" 
styleClass="" showType="" isGoTo="true" changeNum="true"/>
</td>
</tr>
</table>
<tr STYLE="display: none">
	<td colspan="6"><iframe id="childFrm" src=""></iframe></td>
</tr>
</table>
</body>
</html>