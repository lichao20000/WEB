<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!-- 吉林联通测速成功设备详细  JLLT-REQ-RMS-20200224-JH001(吉林联通批量测速结果统计功能需求) -->
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />

<script language="JavaScript">
function ToExcel() 
{
	if(!confirm("导出操作请在晚上九点之后执行，否则将影响系统性能。")){
		return;
	}
	var mainForm = document.getElementById("selectForm");
	mainForm.action = "<s:url value='/gwms/resource/batchHttpTestMana!getTaskExcel.action'/>";
	mainForm.submit();
	mainForm.action = "";
}
</script>
</head>

<body>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			您当前的位置：测速任务结果界面
		</TD>
	</TR>
</TABLE>
<br>
<form id="selectForm" name="selectForm" action="" target="childFrm">
	<input type="hidden" name="upResult" value='<s:property value="upResult"/>'/>
	<input type="hidden" name="taskId" value='<s:property value="taskId"/>'/>
	<table width="98%" class="listtable" align="center">
		<thead>
			<tr>
				<th align="center" width="8%">属地</th>
				<th align="center" width="8%">厂商</th>
				<th align="center" width="8%">型号</th>
				<th align="center" width="8%">软件版本</th>
				<th align="center" width="8%">设备序列号</th>
				<th align="center" width="8%">宽带账号</th>
				<th align="center" width="8%">签约带宽</th>
				<th align="center" width="8%">测速终端</th>
				<th align="center" width="8%">平均下载速率</th>
				<th align="center" width="8%">是否达标</th>
				<th align="center" width="8%">开始时间</th>
				<th align="center" width="8%">结束时间</th>
			</tr>
		</thead>
	    <s:if test="taskResultList!=null && taskResultList.size()>0">
	    <tbody>
			<s:iterator value="taskResultList">
			<tr>
				<td align="center"><s:property value="cityName" /></td>
	            <td align="center"><s:property value="vendorName" /></td>
	            <td align="center"><s:property value="deviceModel" /></td>
	            <td align="center"><s:property value="deviceTypeName" /></td>
	            <td align="center"><s:property value="deviceSerialNumber" /></td>
	            <td align="center"><s:property value="pppoe_name" /></td>
				<td align="center"><s:property value="rate" /></td>
	            <td align="center"><s:property value="speed_dev" /></td>
	            <td align="center"><s:property value="average_speed" /></td>
	            <td align="center"><s:property value="is_sure" /></td>
	            <td align="center"><s:property value="start_time" /></td>
	            <td align="center"><s:property value="end_time" /></td>
			</tr>
			</s:iterator>
		</tbody>
		</s:if>
		<s:else>
			<tbody>
				<tr>
					<td colspan="12">没有查询到相关设备！</td>
				</tr>
			</tbody>
		</s:else>
		<tfoot>
			<tr>
				<td align="left" class=column>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand' onclick="ToExcel()">
				</td>
				<td colspan="11" align="right" class=column>
					<lk:pages url="/gwms/resource/batchHttpTestMana!getTestSpeedTaskResult.action"
						styleClass="" showType="" isGoTo="true" changeNum="true"/>
				</td>
			</tr>
			<tr STYLE="display: none">
				<td colspan="4">
					<iframe id="childFrm" name="childFrm" src=""></iframe>
				</td>
			</tr>
		</tfoot>
	</table>
	<div id="divDetail"
		style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none"></div>
</form>
</body>
