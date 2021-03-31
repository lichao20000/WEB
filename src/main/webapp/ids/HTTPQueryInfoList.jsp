<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ���ѯ</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

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
<s:if test='code=="0"'>
<table class="listtable" id="listTable">
	<caption>���Խ��</caption>
	<thead>
		<tr>
			<th>�豸���к�</th>
			<th>�����յ�ʱ��</th>
			<th>���俪ʼʱ��</th>
			<th>�������ʱ��</th>
			<th>�����ֽ�������������ͷ��</th>
			<th>�����ֽ���</th>
			<th>TCP����ʱ��</th>
			<th>TCP��Ӧʱ��</th>
			<%if("js_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
					<th>ƽ����������(Mbps)</th>
					<th>�����������(Mbps)</th>												
			<%
				}else{ 
			%>
				<th>��������(M/s)</th>
			<%
				}
			%>
		</tr>
	</thead>
		<tbody>
					
						<tr>
							<td><s:property value="httpMap['DevSn']" /></td>
							<td><s:property value="httpMap['RequestsReceivedTime']" /></td>
							<td><s:property value="httpMap['TransportStartTime']" /></td>
							<td><s:property value="httpMap['TransportEndTime']" /></td>
							<td><s:property value="httpMap['ReceiveByteContainHead']" /></td>
							<td><s:property value="httpMap['ReceiveByte']" /></td>
							<td><s:property value="httpMap['TCPRequestTime']" /></td>
							<td><s:property value="httpMap['TCPResponseTime']" /></td>
							<%if("js_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
								<td><s:property value="httpMap['AvgSampledValues']" /></td>
								<td><s:property value="httpMap['MaxSampledValues']" /></td>											
							<%
								}else{ 
							%>
								<td><s:property value="httpMap['downPert']" /></td>
							<%
								}
							%>
						</tr>
					
				
	</tbody>
</table>
</s:if>	
<s:elseif test='code=="1"'>
	<div align="left"  style="background-color: #E1EEEE;height: 20">
		<s:property value="httpMap['errMessage']" />
	</div>
</s:elseif>
</body>
</html>