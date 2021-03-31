<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%String absPath=request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>总数展示页面</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript">
	function SUMExcel(add_time,task_name)
	{

	var page="<s:url value='/itms/resource/LogRestartQuery!sunExcel.action'/>?"								
		+"&add_time="+add_time
		+"&task_name="+task_name;
	document.all("childFrm").src=page;
	}
	function Detailed(add_time,task_name)
	{
		var page="<s:url value='/itms/resource/LogRestartQuery!Detailed.action'/>?"								
			+"&add_time="+add_time
			+"&task_name="+task_name;
		document.all("childFrm").src=page;
	}
	function chang()
	{
		$("tr[@id='biaotou']").css("display","");
		$("tr[@id='antian']").css("display","");
		$("tr[@id='fenye']").css("display","");
		$("tr[@id='biaotou1']").css("display","none");
		$("tr[@id='mingx']").css("display","none");
		$("tr[@id='fenye1']").css("display","none");
	}
	function chang1()
	{
		$("tr[@id='biaotou']").css("display","none");
		$("tr[@id='antian']").css("display","none");
		$("tr[@id='fenye']").css("display","none");
		$("tr[@id='biaotou1']").css("display","");
		$("tr[@id='mingx']").css("display","");
		$("tr[@id='fenye1']").css("display","");
	}
	</script>

</head>
<body>
<form action="">
		<input type="hidden" name="add_time" id="add_time"
			value='<s:property value="add_time"/>'>
<input type="hidden" name="task_name" id="task_name" value='<s:property value="task_name"/>'>

<table class="listtable" id="listTable">
&nbsp;
<br>
<br>
<caption >查询结果</caption>

	<thead>
	<tr align="left">
<td >
<input type="radio" value="2" name="bindState" onclick="chang1()" 
								<s:property value='"2".equals(bindState)?"checked":""'/>>
							明细数据
							<input type="radio" value="1" name="bindState" checked="checked"
							onclick="chang()"	<s:property value='"1".equals(bindState)?"checked":""'/>>
							按天统计&nbsp;
</td>
</tr>
		<tr id="biaotou" style="display: ">	
			<th>时间</th>
			<th>重启成功数</th>
			<th>未执行数</th>		
			<th>未成功数</th>
			<th>总数</th>			
		</tr>
		<tr  id="biaotou1" style="display: none">
			<th>属地</th>
			<th>厂家</th>
			<th>型号</th>
			<th>设备序列号</th>
			<th>LOID</th>
			<th>软件版本</th>
			<th>硬件版本</th>
			<th>特别版本</th>
			<th>执行时间</th>
			<th>失败原因</th>
		</tr>
	</thead>
	
<s:if test="data!=null">	
<s:if test="data.size()>0">		
				<s:iterator value="data">
						<tr id="antian" style="display: ">
							<td >							
								<s:property value="update_time" />
							</td>
							<td >
								<s:property value="doneNum"/>
							</td>
							<td >
									<s:property value="unDoneNum" />
							</td>
							<td >
								<s:property value="failNum"/>
							</td>
							<td >
								<s:property value="totalNum"/>
							</td>
						</tr>
						<tr id="mingx" style="display: none">
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
								<s:property value="device_serialnumber" />		
							</td>
							<td >
									<s:property value="username" />		
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
							<td >							
								<s:property value="update_time" />
							</td>
							<td>
								<s:property value="fail_desc"/>
							</td>
						</tr>
				</s:iterator>
				</s:if>
				<s:else>
				<tr>
					<td colspan="5">系统中没有查询出需要的信息!</td>
				</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan="5">系统中没有查询出需要的信息!</td>
				</tr>
			</s:else>	
<tr id="fenye" style="display: ">		
<td colspan="2" align="left">
<img alt="导出表格" src="<%=absPath%>/images/excel.gif" border="0" style='cursor: hand' onclick="SUMExcel('<s:property value="add_time"/>','<s:property value="task_name"/>')"> 
</td>
<td colspan="3" align="right">
<lk:pages url="itms/resource/QueryBatchModifyTerminal!sumQuery.action"
styleClass="" showType="" isGoTo="true" changeNum="true"/>
</td>
</tr>

<tr id="fenye1" style="display:none">
<td colspan="2" align="left">
<img alt="导出表格" src="<%=absPath%>/images/excel.gif" border="0" style='cursor: hand' onclick="Detailed('<s:property value="add_time"/>','<s:property value="task_name"/>')"> 
</td>
<td colspan="8" align="right">
<lk:pages url="itms/resource/QueryBatchModifyTerminal!sumQuery.action"
styleClass="" showType="" isGoTo="true" changeNum="true"/>
</td>
</tr>
<tr STYLE="display: none">
	<td colspan="7"><iframe id="childFrm" src=""></iframe></td>
</tr>
</table>
</form>
</body>
</html>