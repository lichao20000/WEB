<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<html>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#999999">
					<tr bgcolor="#FFFFFF">
						<th width="20%">�û��˺�</th>
						<th width="25%">�豸���к�</th>
						<th width="10%">�Ƿ��</th>
						<th width="15%">�󶨷�ʽ</th>
						<th width="10%">�Ƿ���۵�</th>
						<th width="20%">�۵����ؽ��</th>
					</tr>
					<s:if test="reportList==null">
						<tr bgcolor="#FFFFFF">
							<td colspan=6>���û�������</td>
						</tr>
					</s:if>
					<s:else>
						<s:iterator value="reportList">
							<tr bgcolor="#FFFFFF">
								<td align=center><s:property value="username"/></td>
								<td align=center><s:property value="device_serialnumber"/></td>
								<td align=center><s:property value="bindState"/></td>
								<td align=center><s:property value="bindType"/></td>
								<td align=center><s:property value="dispatch"/></td>
								<td align=center><s:property value="dispatchResult"/></td>
							</tr>
						</s:iterator>
					</s:else>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
<script language="javascript">
	parent.dyniframesize();
</script>