<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

  <link rel="stylesheet" href="../../css/css_green.css" type="text/css">
<%@ include file="../head.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<TABLE width="80%" border=0 cellspacing=0 cellpadding=0
				align="center">
				<TR>
					<TD bgcolor=#999999>
						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR>
								<TH align="center" colspan="2">
									现场安装提示信息
								</TH>
							</TR>
							<TR height="50">
								<TD align=center valign=middle class=column>
									绑定状态：<s:property value="message"/>
								</TD>
							</TR>
							<tr>
								<td class=column colspan="2" align="right">
									<input type="button" name="back" value=" 返 回 " onclick="history.go(-1)">
								</td>
							</tr>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
</TABLE>
<BR><BR>
<%@ include file="../foot.jsp"%>
