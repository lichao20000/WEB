<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务查询</title>
<%
	/**
	 * 原始工单查询
	 * 
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2010-09-08
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	function go(id,receive_date,gw_type){
		var strpage = "<s:url value='/itms/service/operateByHistoryQuery!getOperateMessage.action'/>?bss_sheet_id="+id+"&receive_date="+receive_date+"&gw_type="+gw_type;
		window.open(strpage,"","left=20,top=20,height=450,width=700,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
	}


	$(function() {
		parent.dyniframesize();
	});
	
	
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>原始工单信息</caption>
	<thead>
		<tr>
			<ms:inArea areaCode="sx_lt" notInMode="true">
			<th>loid</th>
			</ms:inArea>
			<ms:inArea areaCode="sx_lt" notInMode="false">
			<th>唯一标识</th>
			</ms:inArea>
			<ms:inArea areaCode="cq_dx" notInMode="false">
			<th>电子单号</th>
			</ms:inArea>
			<th>属地</th>
			<th>受理时间</th>
			<th>业务类型</th>
			<th>业务账号</th>
			<th>操作类型</th>
			<th>执行结果</th>
			<ms:inArea areaCode="cq_dx" notInMode="true">
			<th>结果描述</th>
			</ms:inArea>
			<ms:inArea areaCode="cq_dx" notInMode="false">
			<th>结果描述</th>
			</ms:inArea>
			<th>工单来源</th>
			<ms:inArea areaCode="ah_lt" notInMode="false">
			<th>状态</th>
			</ms:inArea>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="dataList!=null">
			<s:if test="dataList.size()>0">
				<s:iterator value="dataList">
						<tr>
							<td><s:property value="username" /></td>
							<ms:inArea areaCode="cq_dx" notInMode="false">
								<td><s:property value="bss_sheet_id" /></td>
							</ms:inArea>
							<td><s:property value="cityName" /></td>
							<td><s:property value="receive_date" /></td>
							<td><s:property value="servType" /></td>
							<td><s:property value="servUsername" /></td>
							<td><s:property value="resultType" /></td>
							<td> <font color="<s:property value='color'/>"><s:property value="resultId" /></font> </td>
							<ms:inArea areaCode="cq_dx" notInMode="true">
							<td><s:property value="returnt_context" /></td>
							</ms:inArea>
							<ms:inArea areaCode="cq_dx" notInMode="false">
							<td><s:property value="returnt_context_short" /></td>
							</ms:inArea>
							<td><s:property value="remark" /></td>
							<ms:inArea areaCode="ah_lt" notInMode="false">
							<td><s:property value="statusDesc" /></td>
							</ms:inArea>
							<ms:inArea areaCode="cq_dx" notInMode="true">
							<td><a href="javaScript:go('<s:property value='bss_sheet_id'/>','','<s:property value="gw_type" />');" >工单内容</a>  </td>
							</ms:inArea>
							<ms:inArea areaCode="cq_dx" notInMode="false">
							<td><a href="javaScript:go('<s:property value='bss_sheet_id'/>','<s:property value="receive_date_long" />','<s:property value="gw_type" />');" >工单内容</a>  </td>
							</ms:inArea>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=10>系统没有该用户的业务信息!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=10>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<ms:inArea areaCode="cq_dx" notInMode="true">
		<tr>
			<td colspan="10" align="right">
		 	<lk:pages
				url="/itms/service/operateByHistoryQuery!getOperateByHistoryInfoStb.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
		</ms:inArea>
		<ms:inArea areaCode="cq_dx" notInMode="false">
		<tr>
			<td colspan="11" align="right">
		 	<lk:pages
				url="/itms/service/operateByHistoryQuery!getOperateByHistoryInfoStb.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
		</ms:inArea>
	</tfoot>
</table>
	
</body>
</html>