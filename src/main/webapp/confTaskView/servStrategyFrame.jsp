<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<script type="text/javascript">

var f1 = "<s:url value="/servStrategy/ServStrategy!startQuery.action"/>?task_id="+"<s:property value="task_id"/>";
var f2 = "<s:url value="/servStrategy/ServStrategy.action"/>?task_id="+"<s:property value="task_id"/>";

</script>
</head>
<frameset rows="30%,70%" border="0">
	<frame name="searchForm" src=""/>
	<frame name="dataForm" src=""/>
</frameset>
<script type="text/javascript">

document.all("searchForm").src = f1;
document.all("dataForm").src = f2;

</script>
</html>