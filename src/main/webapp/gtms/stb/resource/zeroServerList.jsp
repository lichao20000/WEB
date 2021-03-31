<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒运营画面服务器列表</title>

<link href="<s:url value="/css/css_blue.css"/>" rel="stylesheet" type="text/css">

<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryCheckForm-linkage.js"/>"></script>
<lk:res/>

<SCRIPT LANGUAGE="JavaScript">
	$(function(){
		parent.dyniframesize();
	});
	
	function edit(serverId, serverName, serverUrl, accessUser, accessPasswd, fileType){
	
		window.parent.document.getElementsByName("serverId")[0].value = parent.trim(serverId);	
		window.parent.document.getElementsByName("serverNameAdd")[0].value = parent.trim(serverName);
		window.parent.document.getElementsByName("accessUserAdd")[0].value = parent.trim(accessUser);	
	    window.parent.document.getElementsByName("accessPasswdAdd")[0].value = parent.trim(accessPasswd);	
		window.parent.document.getElementsByName("serverUrlAdd")[0].value = parent.trim(serverUrl);
		window.parent.document.getElementsByName("fileTypeAdd")[0].value = parent.trim(fileType);	
		
		window.parent.document.getElementsByName("Action")[0].value = "upd";
		window.parent.document.getElementById("actLabel").innerHTML="编辑";
		
		parent.readFlag(true);
		
		//parent.disableLabel(true);
		parent.showAddPart(true);
		parent.disabledButton(false);
	}
	
	/** 删除 初始化帐号 */
	function dele(serverId){
		if(!delWarn())
			return;
		var url = "<s:url value='/gtms/stb/resource/serverManage!deleServer.action'/>";
		
		$.post(url,{
			serverId:serverId
		},function(ajax){
			
			alert(ajax);
			
			if(ajax.indexOf("成功") != -1){
				// 普通方式提交
				var form = window.parent.document.getElementById("selectForm");
				form.action = "<s:url value='/gtms/stb/resource/serverManage!serverQueryList.action'/>";
				parent.reset();
				form.submit();
			}
		});
		
		parent.showAddPart(false);
	}
	
	function delWarn(){
		if(confirm("真的要删除该帐号吗？\n本操作所删除的不能恢复！")){
			return true;
		}else{
			return false;
		}
	}
</SCRIPT>

</head>
<body>
<table class="listtable" width="100%" align="center">
	<thead>
		<tr>
		<th align="center">服务器名称</th>
		<th align="center">用户名</th>
		<!--<th align="center">密码</th>  -->
		<th align="center">服务器类型</th>
		<th align="center">服务器URL</th>
		<th align="center">操作</th>
		</tr>
	</thead>
	<tbody>
	<s:if test="serverList != null">
		<s:if test="serverList.size()>0">
			<s:iterator value="serverList">
				<tr bgcolor="#FFFFFF">
					<td><s:property value="serverName"/></td>
					<td><s:property value="accessUser"/></td>
					<!-- <td><s:property value="accessPasswd"/></td> -->
					
					<td><s:property value="fileTypeValue"/></td>
					<td><s:property value="serverUrl"/></td>
					<td align="center" nowrap="nowrap">
						<label onclick="javascript:edit('<s:property value="serverId"/>','<s:property value="serverName"/>','<s:property value="serverUrl"/>','<s:property value="accessUser"/>','<s:property value="accessPasswd"/>','<s:property value="fileType"/>');">
							<IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='修改' style='cursor:hand'>
						</label>
						<label onclick="javascript:dele('<s:property value="serverId"/>');">
							<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0' ALT='删除' style='cursor:hand'>
						</label>
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr bgcolor="#FFFFFF">
				<td colspan=6>
					没检索到相关数据！
				</td>
			</tr>
		</s:else>
	</s:if>
	<s:else>
		<tr bgcolor="#FFFFFF">
			<td colspan=6>
				没检索到相关数据！
			</td>
		</tr>
	</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="6" align="right" class=column>
				<lk:pages url="/gtms/stb/resource/serverManage!serverQueryList.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>