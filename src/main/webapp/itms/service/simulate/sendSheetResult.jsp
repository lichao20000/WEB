<%--
模拟BSS工单 执行结果
Author: Jason
Version: 1.0.0
Date: 2009-10-16
--%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<html>
<head>
<title>模拟工单执行结果</title>
<script type="text/javascript">

<!--//
	function GoList(){
		this.location = "simulateSheet.jsp";
	}
//-->
</script>
</head>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">BSS模拟工单操作提示信息</TH>
					</TR>
						<TR  height="50">
							<TD align=center valign=middle class=column>工单执行结果 <s:property value="ajax"/></TD>
						</TR>
					<TR>
						<TD class=foot align="right">
							<!-- <INPUT TYPE="button" NAME="cmdBack" onclick="GoList();" value=" 模拟BSS工单 " class="btn"> -->
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../../../foot.jsp"%>
</body>
</html>