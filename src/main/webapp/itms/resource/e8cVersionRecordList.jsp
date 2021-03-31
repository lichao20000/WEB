<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>e8-c规范版本查询列表</title>
<link href="<s:url value="/css3/css_s.css"/>" rel="stylesheet"
	type="text/css" />
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});

	$(function(){
		$("input[@name='button']",parent.document).attr("disabled", false);
		$("input[@name='button']",parent.document).css("backgroundColor","#0066ff");
		$("#QueryData",parent.document).html("");
	});
</script>
</head>
<body>
	<div class="content">
		<div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="it_table">
				<tr>
				   <th>设备厂商</th>
				   <th>设备型号</th>
				   <th>软件版本</th>
				   <th>操作类型</th>
				   <th>操作人员</th>
				   <th>操作时间</th>
				   <th>文件名称</th>

				</tr>
				<s:if test="rlist!=null">
					<s:if test="rlist.size()>0">
						<s:iterator value="rlist">
							<tr>
								<td><s:property value="vendor_name"/></td>
								<td><s:property value="device_model"/></td>
								<td><s:property value="software_version"/></td>
								<td><s:property value="operType"/></td>
								<td><s:property value="operUsername"/></td>
								<td><s:property value="time"/></td>
								<td><s:property value="fileName"/></td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
					<tr>
						<td colspan="9" align="left">
							<font color="red">没有相关规范版本记录信息!</font>
						</td>
					</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td colspan="9" align="left">
							<font color="red">系统没有匹配到相应信息!</font>
						</td>
					</tr>
				</s:else>
				<tr>
					<td colspan="9" align="right">
						<lk:pages
							url="/itms/resource/E8cVersionQuery!getFileOperRecordList.action"
							styleClass="" showType="" isGoTo="true" changeNum="true" />
					</td>
				</tr>
				<tr STYLE="display: none">
					<td colspan="2">
						<iframe id="childFrm" name="childFrm" src=""></iframe>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
<!-- <div id="wall" class="wall" style="width:2500;height:590; margin: -1000;display: none" ></div> -->
</html>