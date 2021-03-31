<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>查询结果</title>
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

	function GoContent(digit_map) {
		var url="<s:url value='/itms/resource/SoundGraphsQuery!number.action'/>?"
			+"&digit_map="+digit_map;
		window.open(url, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>查询结果</caption>
		<thead>
			<tr>
				<th>业务名称</th>
				<th>执行时间</th>
				<th>下发完成时间</th>
				<th>策略状态</th>
				<th>策略结果</th>
				<th>结果描述</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<s:if test="userid !=null">
		<s:if test="userid.size()>0">
		<s:if test="userid.size()==1">
		<s:if test="DeviceId!=null">
		<s:if test="DeviceId.size()>0">
		<s:if test="DeviceId.size()==1">
		<s:iterator value="DeviceId">
		<s:if test="device_id!=null">
		<s:if test="digit_map!=null">
		<s:if test="Date != null ">
			<s:if test="Date.size() > 0">
				<s:iterator value="Date">
					<tr>
						<td>
							<s:property value="business_name" />
						</td>
						<td>
							<s:property value="start_time" />
						</td>
						<td>
							<s:property value="time" />
						</td>
						<td>
							<s:property value="status" />
						</td>
						<s:if test="result_id==1">
						<td>
							成功
						</td>
						</s:if>
						<s:elseif test="result_id==10000">
						<td>
							未做
						</td>
						</s:elseif>
						<s:else>
						<td>
							失败
						</td>
						</s:else>
						<td>
							<s:property value="result_desc" />
						</td>
						<td align="center">
							<a href="javascript:GoContent('<s:property value='digit_map' />')">更多信息</a>
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=7 align=left> 没有查询到相关数据！ </td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
				<tr>
					<td colspan=7 align=left> 没有查询到相关数据！ </td>
				</tr>
		</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left> 无虚拟网语音业务！ </td>
			</tr>
		</s:else> 
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left> 该用户未绑定设备！ </td>
			</tr>
		</s:else>
		</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left> 该用户是多语音用户,请通过VOIP认证号码或VOIP电话号码查询！ </td>
			</tr>
		</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left>无虚拟网语音业务！ </td>
			</tr>
		</s:else>
		
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left> 无虚拟网语音业务！ </td>
			</tr>
		</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left> 账号对应多个用户，请根据LOID查询！ </td>
			</tr>
		</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left> 输入的账号不存在！ </td>
			</tr>
		</s:else>
		
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left> 输入的账号不存在！ </td>
			</tr>
		</s:else>
		
		<tfoot>
		<tr>
			<td colspan="7" align="right">
		 	<lk:pages
				url="/itms/resource/SoundGraphsQuery!query.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot>
	</tbody>
	</table>
</body>
</html>