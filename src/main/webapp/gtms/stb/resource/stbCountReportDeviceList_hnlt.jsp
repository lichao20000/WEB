<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<title>�����а�EPG/APK�汾ͳ��-�豸��ϸ</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<div>
<table class="listtable">
	<caption>ͳ�ƽ��</caption>
	<thead>
		<tr>
			<th width="8%">����</th>
			<s:if test="'apk'==queryType">
				<th width="8%">APK�汾</th>
			</s:if>
			<s:else>
				<th width="8%">EPG�汾</th>
			</s:else>
			<th width="10%">�豸���к�</th>
			<th width="10%">ҵ���˺�</th>
			<th width="8%">MAC��ַ</th>
			<th width="6%">����</th>
			<th width="8%">�ͺ�</th>
			<th width="9%">Ӳ���汾</th>
			<th width="9%">����汾</th>
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
				<td colspan=9 align=left>û�в�ѯ��������ݣ�</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="9" align="right" height="15">
				[ ͳ������ : <s:property value='queryCount'/> ]&nbsp;
				<lk:pages url="/gtms/stb/resource/stbCountACT!getStbDeviceList.action" 
					showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
</table>
</div>
