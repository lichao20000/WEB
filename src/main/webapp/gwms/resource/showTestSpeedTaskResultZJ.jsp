<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="s" uri="/struts-tags"%>
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
			<ms:inArea areaCode="hb_lt" notInMode="true">
				<th align="center" width="6%">����</th>
			</ms:inArea>
			<ms:inArea areaCode="hb_lt" notInMode="false">
				<th align="center" width="6%">����</th>
			</ms:inArea>
				
				<th align="center" width="6%">����</th>
				<th align="center" width="6%">�ͺ�</th>
				<th align="center" width="6%">����汾</th>
				<th align="center" width="8%">�豸���к�</th>
			<%-- <ms:inArea areaCode="ah_lt" notInMode="true">
				<th align="center" width="6%">����˺�</th>
			</ms:inArea> --%>
				<th align="center" width="6%">LOID</th>
				<ms:inArea areaCode="zj_lt" notInMode="false">
					<th align="center" width="6%">����˺�</th>
				</ms:inArea>
				<th align="center" width="6%">����</th>
				<th align="center" width="6%">����IP</th>
				<th align="center" width="5%">ƽ������</th>
				<th align="center" width="5%">ǩԼ����</th>
				<th align="center" width="5%">��ǰ����</th>
				<th align="center" width="5%">�������</th>
				<th align="center" width="5%">��ʼʱ��</th>
				<th align="center" width="5%">����ʱ��</th>
				<th align="center" width="8%">�������</th>
				
			<%-- <ms:inArea areaCode="hb_lt" notInMode="false">
				<th align="center" width="6%">����</th>
			</ms:inArea>
			<ms:inArea areaCode="jl_lt" notInMode="false">
				<th align="center" width="6%">ǩԼ����</th>
			</ms:inArea>
			
			<ms:inArea areaCode="hb_lt" notInMode="true">
				<th align="center" width="12%">���ý��</th>
			</ms:inArea>
			<ms:inArea areaCode="hb_lt" notInMode="false">
				<th align="center" width="10%">���ý��</th>
			</ms:inArea>
			
			<ms:inArea areaCode="hb_lt" notInMode="true">
				<th align="center" width="12%">����ʱ��</th>
			</ms:inArea>
			<ms:inArea areaCode="hb_lt" notInMode="false">
				<th align="center" width="10%">����ʱ��</th>
			</ms:inArea> --%>
			</tr>
		</thead>
	    <s:if test="taskResultList != null && taskResultList.size()>0">
	    <tbody>
			<s:iterator value="taskResultList">
			<tr>
				<td align="center"><s:property value="cityName" /></td>
	            <td align="center"><s:property value="vendorName" /></td>
	            <td align="center"><s:property value="deviceModel" /></td>
	            <td align="center"><s:property value="deviceTypeName" /></td>
	            <td align="center"><s:property value="deviceSerialNumber" /></td>
	         <%-- <ms:inArea areaCode="ah_lt" notInMode="true">
	            <td align="center"><s:property value="pppoe_name" /></td>
	         </ms:inArea> --%>
	         	 <td align="center"><s:property value="loid" /></td>
	         	 <ms:inArea areaCode="zj_lt" notInMode="false">
		            <td align="center"><s:property value="username" /></td>
		         </ms:inArea>
		         <td align="center"><s:property value="times" /></td>
		         <td align="center"><s:property value="pppoeip" /></td>
		         <td align="center"><s:property value="aspeed" /></td>
		         <td align="center"><s:property value="bspeed" /></td>
		         <td align="center"><s:property value="cspeed" /></td>
		         <td align="center"><s:property value="maxspeed" /></td>
		         <td align="center"><s:property value="starttime" /></td>
		         <td align="center"><s:property value="endtime" /></td>
		         <td align="center"><s:property value="result_desc" /></td>
	        <%-- <ms:inArea areaCode="hb_lt,jl_lt" notInMode="false">
				<td align="center"><s:property value="rate" /></td>
			</ms:inArea>
	            <td align="center"><s:property value="result" /></td>
	            <td align="center"><s:property value="update_time" /></td> --%>
			</tr>
			</s:iterator>
		</tbody>
		</s:if>
		<s:else>
			<tbody>
				<tr>
					<td colspan="20">û�в�ѯ������豸��</td>
				</tr>
			</tbody>
		</s:else>
		<tfoot>
			<tr>
				<td colspan="1" align="left" class=column>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand' onclick="ToExcel()">
				</td>
			<ms:inArea areaCode="hb_lt,jl_lt" notInMode="false">
				<td colspan="8" align="right" class=column>
					<lk:pages url="/gwms/resource/batchHttpTestMana!getTestSpeedTaskResult.action"
						styleClass="" showType="" isGoTo="true" changeNum="true"/>
				</td>
			</ms:inArea>
			<ms:inArea areaCode="hb_lt,jl_lt" notInMode="true">
				<td colspan="15" align="right" class=column>
					<lk:pages url="/gwms/resource/batchHttpTestMana!getTestSpeedTaskResult.action"
						styleClass="" showType="" isGoTo="true" changeNum="true"/>
				</td>
			</ms:inArea>
			</tr>
			<tr STYLE="display: none">
				<td colspan="15">
					<iframe id="childFrm" name="childFrm" src=""></iframe>
				</td>
			</tr>
		</tfoot>
	</table>
	<div id="divDetail"
		style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none"></div>
</form>
</body>
