<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * �쳣�豸�б�
 * @author ��־��(5194) tel��1234567890123
 * @version 1.0
 * @since 2008-6-11 ����11:17:15
 *
 * ��Ȩ���Ͼ������Ƽ� ���ܿƼ���
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�쳣�豸����</title>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css/liulu.css"/>" type="text/css">
<style type="text/css">
.table,.table tr,.table td ,.table th{
	border-style: solid;
	border-width: 1px;
	border-color: black;
	border-collapse: collapse;
	border-spacing: 0px;
}

.table {
	width: 100%;
}
</style>
<script type="text/javascript">
$(function(){
	var d = new Date();
	var e= new Date(d.getTime()-24*3600000);
	if(($("input[@name='startDate']").val()=="")||($("input[@name='endDate']").val()==""))
	{
		$("input[@name='startDate']").val(e.getYear()+"-"+(e.getMonth()+1)+"-"+e.getDate()+" 00:00:00");
		$("input[@name='endDate']").val(d.getYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()+" 23:59:59");
	}
});
function doExpDev(status,device_id,exception_time)
{
if(status==0)
{
alert("���쳣�豸��û�д���");
return;
}
var url="<s:url value="/bbms/ExceptionDevice!expDev.action"/>?viewStaus="+status+"&device_id="+device_id+"&exception_time="+exception_time;
$.open(url,"400px","200px","100px","100px","false");
}
</script>
</head>
<body>
<table class="table">
	<form action="<s:url value="/bbms/ExceptionDevice!ExpDevList.action"/>" method="post" name="frm">
	<tr>
		<th colspan="4">�쳣�豸�б��ѯ</th>
	</tr>
	<tr>
		<td class="column">�쳣��ʼ����ʱ��</td>
		<td><input type="text" name="startDate" value="<s:property value="startDate"/>" readonly style="background-color: #DFDFFF;color: #6a6a6a;"/>
		<button onclick="new WdatePicker(document.frm.startDate,'%Y-%M-%D %h:%m:%s',true,'whyGreen');">��</button>
		</td>
		<td class="column">�쳣��������ʱ��</td>
		<td><input type="text" name="endDate" value="<s:property value="endDate"/>" readonly style="background-color: #DFDFFF;color: #6a6a6a;"/>
		<button onclick="new WdatePicker(document.frm.endDate,'%Y-%M-%D %h:%m:%s',true,'whyGreen');">��</button>
		</td>
	</tr>
	<tr><td class="column">����״̬</td><td colspan="3">
	<label for="ndeal">δ����</label><input type="radio" value="0" name="dealstatus" id="ndeal" <s:property value="dealstatus==0?'checked':''"/>/>
	<label for="ydeal">�Ѵ���</label><input type="radio" value="1" name="dealstatus" id="ydeal" <s:property value="dealstatus==1?'checked':''"/>/>
	<label for="adeal">ȫ��</label><input type="radio" value="2" name="dealstatus" id="adeal" <s:property value="dealstatus==2?'checked':''"/>/>
	</td></tr>
	<tr>
		<td colspan="4" align="right">
		<button type="submit" class="jianbian" style="margin-left:10px;">��ѯ</button>
		</td>
	</tr>
	</form>
</table>
<s:if test="expDevList!=null">
	<table class="table" style="margin-top:10px;">
		<tr>
			<th>�豸��Ϣ</th>
			<th>�쳣����ʱ��</th>
			<th>�쳣ԭ��</th>
			<th>ϵͳ����</th>
			<th>�豸����</th>
			<th>����״̬</th>
			<th>����</th>
		</tr>
		<s:iterator value="expDevList">
			<tr>
				<td><s:property value="device_info" /></td>
				<td><s:date name="exception_time" /></td>
				<td><s:property value="exceptionType" /></td>
				<td><s:property value="acs_config" /></td>
				<td><s:property value="cpe_config" /></td>
				<td><s:property value="status==0?'δ����':'�Ѵ���'" /></td>
				<td><a href="javascript://" onclick="doExpDev(3,'<s:property value="device_id"/>',<s:property value="exception_mark"/>);">������쳣</a>&nbsp;|&nbsp;<a
					href="javascript://" onclick="doExpDev(<s:property value="status"/>,'<s:property value="device_id"/>',<s:property value="exception_mark"/>);">�鿴������Ϣ</a></td>
			</tr>
		</s:iterator>
	</table>
</s:if>
</body>
</html>