<%@ include file="../timelater.jsp" %>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%@ include file="../head.jsp" %>
<%@ include file="../toolbar.jsp"%>

<% 
	String isSuccess = request.getParameter("isSuccess");
	if ("1".equals(isSuccess)) {
%>
	<script type="text/javascript">
		alert("�û������ϴ��ɹ���");
	</script>
<%
	} else if ("0".equals(isSuccess)) {
%>
		<script type="text/javascript">
			alert("�û������ϴ�ʧ�ܣ�");
		</script>
<%		
	}else if ("lackDataErr".equals(isSuccess)) {
%>
		<script type="text/javascript">
			alert("�������ļ��У������ֶβ��������ϴ�ʧ�ܣ�");
		</script>
<%	
	}else if ("numErr".equals(isSuccess)) {
%>
		<script type="text/javascript">
			alert("�������ļ��У��������ն�ҵ��ID��������ҵ���͡� ����ȫ��Ϊ���֣��ϴ�ʧ�ܣ�");
		</script>
<%		
	}
%>
<% 
String path = "user.xls";
%>
<script language="javascript">
<!--
	function checkForm(){
		var file = document.importUserForm.file.value;
		
		if (file == ''){
			alert('��ѡ����Ҫ�����.xls�ļ���');
			return false;
		}
		
		if (file.indexOf('.xls') == -1){
			alert('��ѡ��.xls��ʽ���ļ���');
			return false;
		}
		
		document.importUserForm.submit();
	}
//-->
</script>

<table border="0" cellspacing="0" cellpadding="0" width="95%" align="center">
	<tr>
	<td height="20">&nbsp;</td>
	</tr>
	<tr>
	<td>
		<form name="importUserForm" action="<s:url value="/Resource/importUsersBBMS.action"/>" method="POST" enctype="multipart/form-data">
		<table border="0" cellspacing="0" cellpadding="0" align="center" width="100%">
			<tr>
				<td>
					<table border="0" cellspacing="0" cellpadding="0" height="30" width="100%" class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								�û���Դ
							</td>
							<td>
							<img src="../images/attention_2.gif" width="15" height="12">
								�ϴ��û��ļ���ϵͳ�С�
						</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
						<tr>
							<th colspan="4">�ϴ���ҵ�����û�</th>
						</tr>
						<tr bgcolor="#ffffff">
							<td align="right" width="10%">��ѡ���ļ�:</td>
							<td width="35%"><input type="file" name="file"></td>
							<td colspan="2" align="left">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" value=" �� �� " onclick="checkForm()">
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" bgcolor=#ffffff>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td height="20">&nbsp;</td>
										<td width="2%">&nbsp;</td>
										<td colspan="2"><font color="red">�ļ���ʽ��</font></td>
									</tr>
									<tr>
										<td height="15">&nbsp;</td>
										<td width="2%">1��</td>
										<td>�ļ�Ϊ.xls��ʽ</td>
									</tr>
									<tr>
										<td height="15">&nbsp;</td>
										<td width="2%" valign="top">2��</td>
										<td>�ļ��еĵ�һ�в���⣬���ֶ�Ϊ��<br>�����ն�ID�����룩���������򣨱��룩������˺ţ����룩�������ն�ҵ��ID�����룩���ն˰�װ��ַ�����룩����ҵ���ƣ����룩����ϵ�����������룩����ϵ�˵绰�����룩����ҵ���ͣ���ѡ���� ��ҵ��ģ����ѡ������ҵ��ַ����ѡ��</td>
									</tr>
									<tr>
										<tr>
										<td height="30">&nbsp;</td>
										<td width="2%"></td>
										<td><a href="<%= path%>">�������ģ��</a></td>
									</tr>
								</table>
							</td>
						</tr>
					</TABLE>
				</td>
			</tr>
		</table>
		</form>
	</td>
	</tr>
</table>
<%@ include file="../foot.jsp"%>












