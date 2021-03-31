<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<title>机顶盒按EPG/APK版本统计-设备详细</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<div>
<table class="listtable">
	<caption>统计结果</caption>
	<thead>
		<tr>
			<th width="8%">属地</th>
			<s:if test="'apk'==queryType">
				<th width="8%">APK版本</th>
			</s:if>
			<s:else>
				<th width="8%">EPG版本</th>
			</s:else>
			<th width="10%">设备序列号</th>
			<th width="10%">业务账号</th>
			<th width="8%">MAC地址</th>
			<th width="6%">厂商</th>
			<th width="8%">型号</th>
			<th width="9%">硬件版本</th>
			<th width="9%">软件版本</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data!=null && data.size()>0">
			<s:iterator value="data">
				<tr align="center">
					<td><s:property value="cityName"/></td>
					<td><s:property value="version"/></td>
					<td><s:property value="deviceSerialnumber"/></td>
					<td><s:property value="servAccount"/></td>
					<td><s:property value="cpeMac"/></td>
					<td><s:property value="vendorName"/></td>
					<td><s:property value="deviceModel"/></td>
					<td><s:property value="hardwareversion"/></td>
					<td><s:property value="softwareversion"/></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=9 align=left>没有查询到相关数据！</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="9" align="right" height="15">
				[ 统计总数 : <s:property value='queryCount'/> ]&nbsp;
				<lk:pages url="/gtms/stb/resource/stbCountACT!getStbDeviceList.action" 
					showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
</table>
</div>
