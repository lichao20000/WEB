<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>工单执行结果</title>
</head>
<body>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">工单执行结果提示信息</TH>
					</TR>
						<TR  height="50">
							<TD align=center valign=middle class=column>定制完成,后台正在进行操作</TD>
						</TR>
					<TR>
						<TD class=foot align="right">
						<a href='<s:property value="goback"/>'>==返回==</a>| <a href='<s:property value="gobefore"/>'>==查看工单结果==</a>
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