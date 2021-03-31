<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>

<link rel="stylesheet" href="<s:url value="/css3/mq_table.css"/>"
	type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css"></link>

<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript">
	$(function() {
		parent.document.getElementById("queryButton").disabled = false;
		parent.dyniframesize();
	});
	function getDetail(mqId,topicName,gathertime,starttime,endtime){
		var page="<s:url value='/itms/report/queryMq!getMqDetail.action'/>?"
			+ "mqId=" + mqId 
			+ "&topicName=" +topicName
			+ "&gathertime=" +gathertime
			+ "&starttime=" +starttime
			+ "&endtime=" +endtime;
		window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	} 
</script>
<div >
	<table class="listtable">
	<caption >
		统计结果
	</caption>
	<thead>
			<tr>
				<th>主题名称</th>
				<th>消费者数量</th>
				<th>消息入列数</th>
				<th>消息出列数</th>
				<th>统计时间</th>
			</tr>
			</thead>
			<tbody>
			<s:if test="queryMqList != null ">
				<s:if test="queryMqList.size() > 0">
					<s:iterator value="queryMqList">
						<tr align="center">
						<td><a
							href="javascript:getDetail('<s:property value="mqId"/>','<s:property value="topicName"/>','<s:property value="gathertime"/>','<s:property value="starttime"/>','<s:property value="endtime"/>');">
							<s:property value="topicName" /> </a></td>
							<td><s:property value="consumer_num" /></td>
							<td><s:property value="message_enqueued" /></td>
							<td><s:property value="message_dequeued" /></td>
							<td><s:property value="showtime" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=5 align=left>没有查询到相关数据！</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=5 align=left>没有查询到相关数据！</td>
				</tr>
			</s:else>
			</tbody>
			<tfoot>
			<tr >
				<td colspan="5" align="right" height="15">[ 统计总数 : <s:property value='queryCount'/> ]&nbsp;<lk:pages
						url="/itms/report/queryMq!getMqListByMq.action" showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
			</tfoot>
	</table>
</div>
