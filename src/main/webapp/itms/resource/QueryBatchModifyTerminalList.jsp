<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
 <%@ taglib prefix="s" uri="/struts-tags"%>
   <%@ taglib prefix="lk" uri="/linkage"%>
   <%String absPath=request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ITMS系统批量重启查询</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
$(function() {
	parent.dyniframesize();
});
 function opendoneNum(task_name,add_time,type)
{		
var url="<s:url value='/itms/resource/QueryBatchModifyTerminal!doneNumQuery.action'/>?"
+"&add_time="+add_time
+"&task_name="+task_name
+"&type="+type;		
window.open(url,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
 
 
function  openfailNum(add_time,task_name,type)
{
	var url="<s:url value='/itms/resource/QueryBatchModifyTerminal!failNumQuery.action'/>?"
		+"&add_time="+add_time
		+"&task_name="+task_name
		+"&type="+type;
	window.open(url, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
function openunDoneNum(add_time,task_name,type)
{
	var url="<s:url value='/itms/resource/QueryBatchModifyTerminal!unDoneNumQuery.action'/>?"
	+"&add_time="+add_time
	+"&task_name="+task_name
	+"&type="+type;
	window.open(url, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
	

function OPEN(add_time,task_name)
{		
	var url="<s:url value='/itms/resource/QueryBatchModifyTerminal!sumQuery.action'/>?"								
			+"&add_time="+add_time
			+"&task_name="+task_name;
	window.open(url, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
</script>
<title>显示页面</title>
</head>
<body>
<form name="frm" id="form">
<table class="listtable" id="listTable">
<caption>查询结果</caption>
	<thead>
		<tr>
			<th>批量导入时间</th>
			<th>下发成功数</th>
			<th>未触发数</th>
			<th>未成功数</th>
			<th>总数</th>
		</tr>
	</thead>
	<s:if test="date!=null">
	<s:if test="date.size()>0">			
				<s:iterator value="date">
						<tr>
							<td >							
								<s:property value="add_time" />
							</td>
							<td >
								<a href="javascript:opendoneNum('<s:property value="task_name"/>','<s:property value="add_time" />',1);">
									<s:property value="doneNum" />
								</a>
							</td>
							<td >
								<a href="javascript:openunDoneNum('<s:property value="add_time"/>','<s:property value="task_name"/>',0);">
								<s:property value="unDoneNum"/>
								</a>
							</td>
							<td >
							<a href="javascript:openfailNum('<s:property value="add_time"/>','<s:property value="task_name"/>',-1);">
							<s:property value="failNum"/>
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
					<td colspan="2">系统中没有查询出需要的信息!</td>
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
<td colspan="6" align="right">
<lk:pages url="itms/resource/QueryBatchModifyTerminal!query.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
</td>
</tr>
</table>
</table>
</form>
</body>
</html>