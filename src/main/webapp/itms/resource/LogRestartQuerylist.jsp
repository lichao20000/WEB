<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
    <%@ taglib prefix="s" uri="/struts-tags"%>
    <%@ taglib prefix="lk" uri="/linkage"%>
    <%String absPath=request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ITMS系统批量重启查询</title>
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
	 function opendoneNum(add_time,task_name,doneNum,type)
	{		
	var url="<s:url value='/itms/resource/LogRestartQuery!success.action'/>?"
	+"&add_time="+add_time
	+"&task_name="+task_name
	+"&doneNum="+doneNum
	+"&type="+type;		
	window.open(url,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	 
	 
	function openfailNum(failNum,add_time,task_name,type)
	{
		
		var url="<s:url value='/itms/resource/LogRestartQuery!file.action'/>?"
			+"&failNum="+failNum
			+"&add_time="+add_time
			+"&task_name="+task_name
			+"&type="+type;
		window.open(url, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	function openunDoneNum(unDoneNum,add_time,task_name,type)
	{
		
		var url="<s:url value='/itms/resource/LogRestartQuery!unDoneNum.action'/>?"
		+"&add_time="+add_time
		+"&task_name="+task_name
		+"&unDoneNum="+unDoneNum
		+"&type="+type;
		window.open(url, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
		
	function ToExcel()
	{
		var mainForm = window.parent.document.getElementById("frm");
		mainForm.action="<s:url value='/itms/resource/LogRestartQuery!toExcel.action'/>";
		mainForm.submit();
		mainForm.action="<s:url value='/itms/resource/LogRestartQuery!countITMS.action'/>";
	} 
	
	function OPEN(add_time,task_name)
	{		
				
		var url="<s:url value='/itms/resource/LogRestartQuery!sumQuery.action'/>?"								
				+"&add_time="+add_time
				+"&task_name="+task_name;
		window.open(url, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	</script>

</head>
<body>
<form name="frm" id="form">
<table class="listtable" id="listTable">
<caption>查询结果</caption>
	<thead>
		<tr>
			<th>导入日期</th>
			<th>批次</th>
			<th>重启成功数</th>
			<!-- <th>ITMS接收时间</th> -->
			<th>重启失败数</th>
			<th>未执行数</th>
			<th>总数</th>
		</tr>
	</thead>

<s:if test="data!=null">	
<s:if test="data.size()>0">		
				<s:iterator value="data">
						<tr>
							<td >							
								<s:property value="add_time" />
							</td>
							<td >
								<s:property value="task_name"/>
							</td>
							<td >
								<a href="javascript:opendoneNum('<s:property value="add_time"/>','<s:property value="task_name"/>','<s:property value="doneNum"/>',1);">
									<s:property value="doneNum" />
								</a>
							</td>
							<td >
								<a href="javascript:openfailNum('<s:property value="failNum" />','<s:property value="add_time"/>','<s:property value="task_name"/>',-1);">
								<s:property value="failNum"/>
								</a>
							</td>
							<td >
								<a href="javascript:openunDoneNum('<s:property value="unDoneNum" />','<s:property value="add_time"/>','<s:property value="task_name"/>',3);">
								<s:property value="unDoneNum"/>
								</a>
							</td>
							<td>
								<a href="javascript:OPEN('<s:property value="add_time"/>','<s:property value="task_name"/>');">
								<s:property value="totalNum"/>
								</a>
								
							</td>
						</tr>
				</s:iterator>
				</s:if>
				<s:else>
				<tr>
					<td colspan=2>系统中没有查询出需要的信息!</td>
				</tr>
			</s:else>	
			</s:if>
			<s:else>
				<tr>
					<td colspan=2>系统中没有查询出需要的信息!</td>
				</tr>
			</s:else>		
<table>
<tr>
<td colspan="6" align="left">
<img alt="导出表格" src="<%=absPath%>/images/excel.gif" border="0" style='cursor: hand' onclick="ToExcel()"> 
</td>
<td colspan="6" align="right">
<lk:pages url="itms/resource/LogRestartQuery!countITMS.action"
styleClass="" showType="" isGoTo="true" changeNum="true"/>
</td>
</tr>
</table>
</table>
</form>
</body>
</html>