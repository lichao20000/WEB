<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<head>
<title>批量配置任务定制结果</title>
</head>
<body>
<%@ include file="../../toolbar.jsp"%>
<BR>
<BR>
<TABLE border=0 cellspacing=0 cellpadding=0 width="80%" align="center">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 >
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">操作提示</TH>
					</TR>
						<TR  height="50">
							<TD align=center valign=middle class=column>策略定制<s:property value="res"/>,<s:property value="desc"/></TD>
						</TR>
					<TR>
						<TD class='green_foot' align="right">
						<INPUT TYPE="button" NAME="cmdBack" onclick="history.go(-1)" value=" 返 回 " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
</body>
</html>