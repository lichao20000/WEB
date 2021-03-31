<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript">
	function usernameMemUpdate(username){
		var url = "<s:url value="/gwms/sysConfig/resourceBindMemConfig!configMem.action"/>";
		$.post(url,{
			username:username,
			type:"user",
			operate:"set",
			gw_type:1
	    },function(mesg){
			alert(mesg);
	    });
	}
	function usernameMemDelete(username){
		var url = "<s:url value="/gwms/sysConfig/resourceBindMemConfig!configMem.action"/>";
		$.post(url,{
			username:username,
			type:"user",
			operate:"del",
			gw_type:1
	    },function(mesg){
			alert(mesg);
	    });
	}
</script>

</head>

<body>

<table class="listtable" id="listTable">
	<caption>DB与MEM比对结果</caption>
	<thead>
		<tr>
			<th>字段名</th>
			<th>DB信息</th>
			<th>MEM信息</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td align="center"></td>
			<td align="center">
				<s:if test="userDbMap==null">数据库不存在该用户</s:if>
			</td>
			<td align="center">
				<s:if test="userMemMap==null">缓存中不存在该用户</s:if>
			</td>
		</tr>
		<tr>
			<td align="center">userId</td>
			<td align="center">
				<s:if test="-65535==userDbMap.userId"></s:if>
				<s:else><s:property value="userDbMap.userId" /></s:else>
			</td>
			<td align="center">
				<s:if test="-65535==userMemMap.userId"></s:if>
				<s:else><s:property value="userMemMap.userId" /></s:else>
			</td>
		</tr>
		<tr>
			<td align="center">cityId</td>
			<td align="center"><s:property value="userDbMap.cityId" /></td>
			<td align="center"><s:property value="userMemMap.cityId" /></td>
		</tr>
		<tr>
			<td align="center">username</td>
			<td align="center"><s:property value="userDbMap.username" /></td>
			<td align="center"><s:property value="userMemMap.username" /></td>
		</tr>
		<tr>
			<td align="center">userline</td>
			<td align="center">
				<s:if test="-65535==userDbMap.userline"></s:if>
				<s:else><s:property value="userDbMap.userline" /></s:else>
			</td>
			<td align="center">
				<s:if test="-65535==userMemMap.userline"></s:if>
				<s:else><s:property value="userMemMap.userline" /></s:else>
			</td>
		</tr>
		<tr>
			<td align="center">isChkBind</td>
			<td align="center">
				<s:if test="-65535==userDbMap.isChkBind"></s:if>
				<s:else><s:property value="userDbMap.isChkBind" /></s:else>
			</td>
			<td align="center">
				<s:if test="-65535==userMemMap.isChkBind"></s:if>
				<s:else><s:property value="userMemMap.isChkBind" /></s:else>
			</td>
		</tr>
		<tr>
			<td align="center">deviceId</td>
			<td align="center"><s:property value="userDbMap.deviceId" /></td>
			<td align="center"><s:property value="userMemMap.deviceId" /></td>
		</tr>
		<tr>
			<td align="center">oui</td>
			<td align="center"><s:property value="userDbMap.oui" /></td>
			<td align="center"><s:property value="userMemMap.oui" /></td>
		</tr>
		<tr>
			<td align="center">deviceSerialnumber</td>
			<td align="center"><s:property value="userDbMap.deviceSerialnumber" /></td>
			<td align="center"><s:property value="userMemMap.deviceSerialnumber" /></td>
		</tr>
		<tr>
			<td align="center">type</td>
			<td align="center"><s:property value="userDbMap.type" /></td>
			<td align="center"><s:property value="userMemMap.type" /></td>
		</tr>
		
		<tr>
			<td align="center">操作</td>
			<td align="center"></td>
			<td align="center">
				<a href="javascript:usernameMemUpdate('<s:property value="username" />')">更新</a>
				<a href="javascript:usernameMemDelete('<s:property value="username" />')">删除</a>
			</td>
		</tr>
	</tbody>
</table>
</body>
</html>