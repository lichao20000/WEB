<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.Map"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>故障处理</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/faulttreatment/slide.js"/>"></script>
<link href="<s:url value="/css3/css.css"/>" rel="stylesheet"
	type="text/css" />
<script type="text/javascript">
	function DetailDevices(username,device_serialnumber)
{
	$("div[@id='tempList']").show();
		$("div[@id='tempList']").html("正在查询，请稍后...");
	var url = "<s:url value='/itms/resource/faultTreadtMent!queryUserAndDeviceInfoList.action'/>"; 
	$.post(url, {
			usernamereturn : username,
			device_serialnumberreturn : device_serialnumber
		}, function(ajax) {
			$("div[@id='tempList']").hide();
			$("div[@id='QueryDataList']").html("");
			$("div[@id='QueryDataList']").html(ajax);
		});
	}
</script>
</head>
<body>
	<input type="hidden" name="tempusername" value='' />
	<s:if test="msg=='false'">
		<div class="it_stips" id="nouser">系统没有此用户！</div>
	</s:if>
	<s:else>
	<div class="content">
		<div>
			<table width="97%" border="0" cellspacing="0" cellpadding="0" id="it_table" align="center"
				class="it_table">
				<tr>
					<th>用户账号</th>
					<th>属地</th>
					<th>用户来源</th>
					<th>绑定设备</th>
					<th>开户时间</th>
					<th>操作</th>
				</tr>
				<s:if test="showList!=null">
					<s:if test="showList.size()>0">
						<s:iterator value="showList" status="u">
							<tr>
								<td><s:property value="username" /></td>
								<td><s:property value="city_id" /></td>
								<td><s:property value="user_type_id" /></td>
								<td><s:property value="device_serialnumber" /></td>
								<td><s:property value="opendate" /></td>
								<td><a href="javascript:DetailDevices('<s:property value="username" />','<s:property value="device_serialnumber" />');" class="itta_more">查看详情</a></td>
							</tr>
						</s:iterator>
					</s:if>
				</s:if>
			</table>
			<input type="hidden" name="username"
				value='<s:property value="userMap.username" />' /> <input
				type="hidden" name="user_id"
				value='<s:property value="userMap.user_id" />' /> <input
				type="hidden" name="device_id"
				value='<s:property value="userMap.city_id" />' />
		</div>
		<div id="nullArea" style="width:200px;height:50px;"></div>
		<div id="tempList" class="it_main" style="display: none; text-align: center;color:#0066FF;font-size:20px;"></div>
		<div class="it_main">
		<div id="QueryDataList" style="width: 100%; height:auto; z-index: 1; top: 100px;">
		</div>
	</div>

	</div>
	</s:else>
</body>
</html>
