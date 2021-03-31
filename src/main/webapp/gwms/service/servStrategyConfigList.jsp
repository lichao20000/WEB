<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ�����ò�����ͼ</title>
<%
	/**
	 * ҵ�����ò�����ͼ
   	 *
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2008-12-18
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});

	function resetData(deviceId,taskId,tempId){
		if(confirm('ȷʵҪ������?')){
			var url = "<s:url value="/gwms/service/servStrategyView!resetData.action"/>";
			$.post(url,{
				deviceId:deviceId,
				taskId:taskId,
				id:tempId
		    },function(mesg){
		    	alert(mesg);
		    	document.location.reload();
		    });
		}else{
			//return false;
		}
	}

	function cancelData(deviceId,taskId,tempId){
		if(confirm('ȷʵҪȡ����?')){
			var url = "<s:url value="/gwms/service/servStrategyView!cancelData.action"/>";
			$.post(url,{
				deviceId:deviceId,
				taskId:taskId,
				id:tempId
		    },function(mesg){
		    	alert(mesg);
		    	document.location.reload();
		    });
		}else{
			//return false;
		}
	}
</script>

</head>

<body>
	<table class="listtable">
		<caption>
			ͳ�ƽ��
		</caption>
		<thead>
			<tr>
				<th rowspan="2" width="5%">����</th>
				<th colspan="2">�豸��Ϣ</th>
				<th colspan="5">������Ϣ</th>
			</tr>
			<tr>
				<th >�豸���к�</th>
				<th >�û��˺�</th>
				<th>ҵ������</th>
				<th>����״̬</th>
				<th>����ʱ��</th>
				<th>ִ��ʱ��</th>
				<th>���Խ��</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="servStrategyList" var="servStrategyInfo" status="status">
				<tr>
					<td align="center" rowspan="<s:property value="listSize"/>">
					<a href="javascript:resetData('<s:property value="deviceId"/>',
												  '<s:property value="taskId"/>',
												  '<s:property value="id"/>' )">����</a>
					<br>
					<a href="javascript:cancelData('<s:property value="deviceId"/>',
												  '<s:property value="taskId"/>',
												  '<s:property value="id"/>')">ȡ��</a></td>
					<td rowspan="<s:property value="listSize"/>"><s:property value="deviceSerialnumber"/></td>
					<td rowspan="<s:property value="listSize"/>"><s:property value="username"/></td>
					<s:iterator value="list" var="infoList">
							<td ><s:property value="service_name"/></td>
							<td ><s:if test="status!=null">
							<s:property value="status"/>
							</s:if></td>
							<td ><s:property value="time"/></td>
							<td ><s:property value="start_time"/></td>
							<td ><s:property value="result_id"/></td>
						</tr>
					</s:iterator>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="8" align="right">
					<lk:pages url="/gwms/service/servStrategyView!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
				</td>
			</tr>
		</tfoot>
	</table>
</body>
</html>
