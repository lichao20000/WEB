<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�û���Ϣ����</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
	<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>

<script type="text/javascript">	

</script>
</head>

<body>
<form name="frm" id="frm" method="post" action="" target="dataForm">

		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">��ϸ��Ϣ</th>
						</tr>
						<!-- <TR bgcolor="#FFFFFF">
							<TD class=column align="right">��Ȩ�˺�</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="authUser"
								maxlength=30 class=bk size=20>&nbsp;<font
								color="#FF0000">*</font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">��Ȩ����</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="authPwd" maxlength=30
								class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
						</TR> -->
						<s:if test="date!=null">
				<s:if test="date.size()>0">
					<s:iterator value="date">
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >����ʱ��</TD>
								<TD colspan=3 disabled="disabled"><s:property value="dealDate" /></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >ҵ��ƽ̨</TD>
								<TD colspan=3 disabled="disabled"><s:property value="platformType" /></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >�û�����</TD>
								<TD colspan="3" disabled="disabled"><s:property value="userGroupID" /></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >�ֻ�����</TD>
								<TD colspan="3" disabled="disabled"><s:property value="iptvBindPhone" /></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column  align='right' >����</TD>
								<TD colspan="3" disabled="disabled"><s:property value="city_name" /></TD>
						</TR>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >ҵ���˺�</TD>
							<TD colspan="3" disabled="disabled"><s:property value="servaccount" /></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >ҵ������</TD>
								<TD colspan="3" disabled="disabled"><s:property value="servpwd" /></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >MAC��ַ</TD>
								<TD colspan="3" disabled="disabled"><s:property value="MAC" /></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >���뷽ʽ</TD>
								<TD colspan=3 disabled="disabled"><s:property value="stbaccessStyle" /></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">�����˺�</TD>
								<TD colspan="3" disabled="disabled"><s:property value="stbuser" /></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right">��������</TD>
								<TD colspan="3" disabled="disabled"><s:property value="stbpwd" /></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >���з�ʽ</TD>
								<TD colspan="3" disabled="disabled"><s:property value="stbuptyle" /></TD>
						</TR>
						<TR>
							<td colspan="4" align="c" class="foot" width="100%">
									<button onclick="javascript:window.close();">&nbsp;�� ��&nbsp;</button>
							</td>
						</TR>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=4>ϵͳû�и��û���ҵ����Ϣ!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=4>ϵͳû�д��û�!</td>
				</tr>
			</s:else>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
