<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>查看voip认证密码</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript">
$(function(){
var userip = window.opener.document.getElementById("userip").value;
var voip_passwd = window.opener.document.getElementById("voip_passwd").value;
var voip_username = window.opener.document.getElementById("voip_username").value;
//$("input[@name='voip_username']").val(voip_username);
document.getElementById("voip_username").innerHTML=voip_username;
document.getElementById("voip_passwd").innerHTML=voip_passwd;
document.getElementById("userip").innerHTML=userip;
//$("input[@name='voip_passwd']").val(voip_passwd);
//$("input[@name='userip']").val(userip);
});
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>查看详细信息</caption>
	<thead>
		<tr>
			<th>用户账号</th>
			<th>用户密码</th>
			<th>ip地址</th>
			<!-- <th>ITMS接收时间</th> -->
		</tr>
	</thead>
	<tbody>
					<tr style="text-align: center;">
						<td id="voip_username"> 
						</td>
						<td id="voip_passwd"></td>
						<td id="userip"></td>
					</tr>
		
	</tbody>
</table>
</body>
</html>