<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>

<link href="<s:url value="/css/uploadAndParse.css"/>" rel="stylesheet"
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

</script>
<div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0"
		class="it_table">
		<tbody>
			<tr>
				<th>属地</th>
				<th>用户LOID</th>
				<th>工单号</th>
				<th>ITMS设备</th>
				<th>ITMS设备规格</th>
				<th>核销设备</th>
				<th>核销设备规格</th>
				<th>核销方式</th>
				<th>核销是否一致</th>
				<th>核销日期</th>
			</tr>
			<s:if test="DevVerificationList != null ">
				<s:if test="DevVerificationList.size() > 0">
					<s:iterator value="DevVerificationList">
						<tr>
							<td><s:property value="city_id" /></td>
							<td><s:property value="username" /></td>
							<td><s:property value="bill_no" /></td>
							<td><s:property value="itms_serial" /></td>
							<td><s:property value="spec_name_itms" /></td>
							<td><s:property value="term_serial" /></td>
							<td><s:property value="spec_name_term" /></td>
							<td><s:property value="use_type" /></td>
							<td><s:property value="is_match" /></td>
							<td><s:property value="use_time" /></td>

						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=11 align=left>没有查询到相关数据！</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11 align=left>没有查询到相关数据！</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="11" align="right">[ 统计总数 : <s:property value='queryCount'/> ]&nbsp;<lk:pages
						url="/itms/resource/devVerificationDisList!queryDevVerification.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		</tfoot>
	</table>
</div>
