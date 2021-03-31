<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
    <%@ taglib prefix="s" uri="/struts-tags"%>
    <%@ taglib prefix="lk" uri="/linkage"%>
     <%String absPath=request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>重启成功明细数据</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
	<script type="text/javascript">
	function successexcel(add_time,task_name,doneNum,type)
	{
		
		var url="<s:url value='/itms/resource/LogRestartQuery!successExcel.action'/>?"
			+"&add_time="+add_time
			+"&task_name="+task_name
			+"&doneNum="+doneNum
			+"&type="+type;
		document.all("childFrm").src=url;
	}

	</script>

</head>
<body>
<form action="">
<input type="hidden" name="add_time" id="add_time" value='<s:property value="add_time"/>'>
<input type="hidden" name="task_name" id="task_name" value='<s:property value="task_name"/>'>
<input type="hidden" name="doneNum" id="doneNum" value='<s:property value="doneNum"/>'>
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
			<th>执行时间</th>
		</tr>
	</thead>

<s:if test="data!=null">	
<s:if test="data.size()>0">		
				<s:iterator value="data">
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
							<td>
								<s:property value="update_time"/>
								</td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan="7">系统中没有查询出需要的信息!</td>
				</tr>
			</s:else>	
			</s:if>
			
			<s:else>
				<tr>
					<td colspan="7">系统中没有查询出需要的信息!</td>
				</tr>
			</s:else>		
<table>
<tr>
<td colspan="7" align="left">
<img alt="导出表格" src="<%=absPath%>/images/excel.gif" border="0" style='cursor: hand' onclick="successexcel('<s:property value="add_time"/>','<s:property value="task_name"/>','<s:property value="doneNum"/>',
							   '<s:property value="type" />')"> 
</td>
<td colspan="7" align="right">
<lk:pages url="itms/resource/LogRestartQuery!success.action"
styleClass="" showType="" isGoTo="true" changeNum="true"/>
</td>
</tr>
</table>
<tr STYLE="display: none">
	<td colspan="7"><iframe id="childFrm" src=""></iframe></td>
</tr>
</table>
</body>
</html>