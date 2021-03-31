<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<html>
<head>
<title>IPTV批量任务定制结果</title>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
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
						<TH align="center">IPTV批量配置操作提示信息</TH>
					</TR>
						<TR  height="50">
							<TD align=center valign=middle class=column>定制完成,后台正在进行操作</TD>
						</TR>
					<TR>
						<TD class=foot align="right">
						<!-- 
                        <INPUT TYPE="button" NAME="cmdJump" onclick="GoList('./configiTV_strategy_search.jsp')" value=" IPTV配置查询 " class=btn >
						 -->
						<INPUT TYPE="button" NAME="cmdBack" class=jianbian onclick="history.go(-1)" value=" 返 回 ">
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