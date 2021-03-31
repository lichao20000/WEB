<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript">
function templateDetail(id)
{     
	window.open("<s:url value='/itms/resource/servTemplate!queryDetail.action' />?id="+id,"","height=800,width=1500,status=yes,toolbar=no,menubar=no,location=no");
}
function resultConvert(res)
{
	switch(res)
	{
		case 0:
			document.write("ʧЧ");
			break;
		case 1:
			document.write("��Ч");
			break;
		default:
			document.write("");
	}
}

function dateConvert(dat)
{
	var d = new Date(dat*1000);
	var year = d.getFullYear();
	var month = d.getMonth()+1;
	var day = d.getDate();
	var h = d.getHours();
	var m = d.getMinutes();
	var s = d.getSeconds();
	
	document.write(year+"/"+month+"/"+day+" "+h+":" + m+":"+s);
}
</script>
</head>

<body>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			���������������
		</TD>
	</TR>
</TABLE>
<br>
<table class="querytable" width="98%" align="center">
	<tr>
		<!-- <td bgcolor=#999999 colspan="4" class="title_1"><div align="center">���������·���������</div></td> -->
		<td colspan="4" class="title_1">���������������</td>
	</tr>
	<TR>
		<TD class="title_2" align="center" width="15%">��������</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.task_name" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">���Ʒ�ʽ</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.querytypeDesc" />
		</TD>
	</TR>
	
	<s:if test="taskResultMap.querytype==2">
		<TR>
			<TD class="title_2" align="center" width="15%">����</TD>
			<TD width="85%" colspan="3">
				<s:property value="taskResultMap.vendor_id" />
			</TD>
		</TR>
		<TR>
			<TD class="title_2" align="center" width="15%">�ͺ�</TD>
			<TD width="85%" colspan="3">
				<s:property value="taskResultMap.device_model_id" />
			</TD>
		</TR>
		<TR>
			<TD class="title_2" align="center" width="15%">�汾</TD>
			<TD width="85%" colspan="3">
				<s:property value="taskResultMap.version" />
			</TD>
		</TR>
		<TR>
			<TD class="title_2" align="center" width="15%">����</TD>
			<TD width="85%" colspan="3">
				<s:property value="taskResultMap.city_name" />
			</TD>
		</TR>
		<TR>
			<TD class="title_2" align="center" width="15%">�Ѱ�</TD>
			<TD width="85%" colspan="3">
				<s:property value="taskResultMap.isbind" />
			</TD>
		</TR>
	</s:if>
	<s:elseif test="taskResultMap.querytype==1">
		<TR>
			<TD class="title_2" align="center" width="15%">��ѯ����</TD>
			<TD width="85%" colspan="3">
				<s:property value="taskResultMap.queryfield" />
			</TD>
		</TR>
		<TR>
			<TD class="title_2" align="center" width="15%">����ֵ</TD>
			<TD width="85%" colspan="3">
				<s:property value="taskResultMap.queryparam" />
			</TD>
		</TR>
	</s:elseif>
	<s:elseif test="taskResultMap.querytype==3">
		<TR>
			<TD class="title_2" align="center" width="15%">�ļ���</TD>
			<TD width="85%" colspan="3">
				<s:property value="taskResultMap.filename" />
			</TD>
		</TR>
	</s:elseif>
	
	<TR>
		<TD class="title_2" align="center" width="15%">��������</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.type" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">������ʼ����</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.start_date" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">������������</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.end_date" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">������ʼʱ��</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.start_time" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">��������ʱ��</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.end_time" />
		</TD>
	</TR>
	<tr>
		<TD class="title_2" align="center" width="15%">����ʱ��</td>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.set_time" />
		</TD>
	</tr>
	<tr>
		<TD class="title_2" align="center" width="15%">����ʱ��</td>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.update_time" />
		</TD>
	</tr>
</table>
<br>
<br>
</body>