<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../timelater.jsp"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<title>型号网口详情</title>
<script language="javascript">
</script>
</head>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<body>

	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" id="idTable">
	  <TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr CLASS=green_title2>
						<td width="50%">厂商</td>
						<td width="50%">型号</td>
					</tr>
					<TR bgcolor="#FFFFFF">
						<td class=column1 nowrap><s:property value="vendorId"/></td>
						<td class=column1 nowrap><s:property value="deviceModelName"/></td>
					</TR>
					<tr CLASS=green_title2>
						<td width="50%">网口</td>
						<td width="50%">网口速率</td>
					</tr>
				    <s:iterator value="deviceModelList">
						<TR bgcolor="#FFFFFF">
							<td class=column1 nowrap><s:property value="port"/></td>
							<td class=column1 nowrap><s:property value="rate"/>M/S</td>
						</TR>
					</s:iterator>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
<%@ include file="../foot.jsp"%>
</body>
</html>
