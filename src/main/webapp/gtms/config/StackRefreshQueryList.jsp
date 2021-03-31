<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.closeMessageInfo();
		$("#tdData",parent.document).show();
		$("#button",parent.document).attr('disabled',false);
		parent.dyniframesize();
	});
	
</script>
</head>
<body>
	<s:if test='code=="1"'>
		
		<table class="listtable" id="listTable">
			<caption>查询结果</caption>
			<thead>
				<tr>
					<th>参数节点路径</th>
					<th>参数值</th>
				</tr>
			</thead>
			<tbody>
						<s:iterator value="httpMap" status="index">
							<tr>
								<td width="80%"><s:property value="key" />
									<s:if test="key.contains('X_CT-COM_IPMode')">
										<font color="red">(参数值：1:IPV4、2:IPV6、3:IPV6和IPV4)</font>
									</s:if>
									<s:else>
										<font color="red">(参数值：1:打开、0:关闭)</font>
									</s:else>
								
								</td>
								<td width="20%"><s:property value="value" /></td>
							</tr>
						</s:iterator>
					
			</tbody>
		</table>
		
	</s:if>	
	<s:elseif test='code=="0"'>
		<div align="left"  style="background-color: #E1EEEE;height: 20">
			<s:property value="httpMap['msg']" />
		</div>
	</s:elseif>
</body>
</html>