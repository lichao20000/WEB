<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!-- ������ͨ���ٳɹ��豸��ϸ  JLLT-REQ-RMS-20200224-JH001(������ͨ�������ٽ��ͳ�ƹ�������) -->
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
	if(!confirm("���������������Ͼŵ�֮��ִ�У�����Ӱ��ϵͳ���ܡ�")){
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
			����ǰ��λ�ã���������������
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
				<th align="center" width="8%">����</th>
				<th align="center" width="8%">����</th>
				<th align="center" width="8%">�ͺ�</th>
				<th align="center" width="8%">����汾</th>
				<th align="center" width="8%">�豸���к�</th>
				<th align="center" width="8%">����˺�</th>
				<th align="center" width="8%">ǩԼ����</th>
				<th align="center" width="8%">�����ն�</th>
				<th align="center" width="8%">ƽ����������</th>
				<th align="center" width="8%">�Ƿ���</th>
				<th align="center" width="8%">��ʼʱ��</th>
				<th align="center" width="8%">����ʱ��</th>
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
					<td colspan="12">û�в�ѯ������豸��</td>
				</tr>
			</tbody>
		</s:else>
		<tfoot>
			<tr>
				<td align="left" class=column>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
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
