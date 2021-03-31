<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="../../Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript">
$(function(){
	parent.dyniframesize();
});

function DetailDevice(device_id){
	var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
</script>

<table width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
					<TR>
						<TH colspan="4" align="center">
						查询结果</TH>
					</TR>
	<tr>
		<td>
		<table width="100%" border=0 cellspacing=1 cellpadding=2
			bgcolor=#999999 id=userTable>
				<tr>
					<th>接收时间</th>
					<th>来源IP</th>
					<th>格式版本</th>
					<th>内容描述</th>
				</tr>
				<s:if test="data.size()>0">
					<s:iterator value="data">
						<tr bgcolor="#ffffff">
							<td class=column nowrap align="center"><s:property
								value="receive_time" /></td>
							<td class=column nowrap align="center"><s:property
								value="source_ip" /></td>
							<td class=column nowrap align="center"><s:property
								value="format_version" /></td>
							<td class=column nowrap align="center"><s:property
								value="content_desc" /></td>
						</tr>
					</s:iterator>
					<tr>
						<td align="right" colspan="5" class=column><lk:pages
							url="/bbms/report/SyslogQuery!goPageQuerySysLog.action"
							styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
					</tr>
				</s:if>
				<s:else>
					<tr>
						<td colspan="5" align=left class=column>系统没有相关的日志信息!</td>
					</tr>
				</s:else>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan="8" style="display: none"><IMG
			SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
			style='cursor: hand'
			onclick="ToExcel('<s:property value="queryType" />','<s:property value="deviceSN" />','<s:property value="loopbackIp" />','<s:property value="userName" />','<s:property value="customerName" />','<s:property value="linkphone" />','<s:property value="reportType" />','<s:property value="stat_time" />')">
		</td>
	</tr>
	<tr STYLE="display: none">
		<td><iframe id="childFrm" src=""></iframe></td>
	</tr>

</table>

<%@ include file="../foot.jsp"%>
