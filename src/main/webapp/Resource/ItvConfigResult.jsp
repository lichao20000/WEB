<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<html>
<head>
<title>IPTV���������ƽ��</title>
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
						<TH align="center">IPTV�������ò�����ʾ��Ϣ</TH>
					</TR>
						<TR  height="50">
							<TD align=center valign=middle class=column>�������,��̨���ڽ��в���</TD>
						</TR>
					<TR>
						<TD class=foot align="right">
						<!-- 
                        <INPUT TYPE="button" NAME="cmdJump" onclick="GoList('./configiTV_strategy_search.jsp')" value=" IPTV���ò�ѯ " class=btn >
						 -->
						<INPUT TYPE="button" NAME="cmdBack" class=jianbian onclick="history.go(-1)" value=" �� �� ">
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