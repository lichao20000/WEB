<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript">
		function resultConvert(res)
		{
			switch(res)
			{
				case 0:
					document.write("��");
					break;
				case 1:
					document.write("��");
					break;
				default:
					document.write("");
			}
		}
		</script>
	</head>
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					����ǰ��λ�ã������޸��鲥����������ϸ��Ϣ
				</TD>
			</TR>
		</TABLE>
		<br>
		<table class="querytable" width="98%" align="center">
			<tr>
				<td colspan="4" class="title_1">
					�����޸��鲥����������ϸ��Ϣ
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">
					��������
				</td>
				<TD width="85%"  >
					<s:property value="taskDetailMap.task_name" />
				</td>
			</tr>	
			<tr>
				<TD class="title_2" align="center" width="15%">
					������
				</td>
				<TD width="85%"  >
					<s:property value="taskDetailMap.acc_loginname" />
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">
					����ʱ��
				</td>
				<TD width="85%"  >
					<s:property value="taskDetailMap.add_time" />
				</td>
			</tr>
			<tr STYLE="display: none">
					<td colspan="4">
						<iframe id="childFrm" src=""></iframe>
					</td>
		   </tr>	
		</table>
		<br>
		<br>
	</body>