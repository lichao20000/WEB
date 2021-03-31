<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>批量配置任务定制结果</title>
<script type="text/javascript">
function goBack(){
	window.location.href = "../resource/BatchHttpTest.jsp"
}
</script>
</head>
<body>
<%@ include file="../../../toolbar.jsp"%>
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
							<!-- 河北联通，非admin用户测速任务限制为3个 -->
							<s:if test="%{instAreaName=='hb_lt' && taskNumFalg==1}">
								<TD align=center valign=middle class=column>此用户创建的测速任务超过三个，请清理任务后下发</TD>
							</s:if>
							<s:else>
						    	<TD align=center valign=middle class=column>策略定制完成,后台正在进行操作</TD>
						    </s:else>
					</TR>
					<TR>
						<TD class='green_foot' align="right">
						<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.go(-1);" value=" 返 回 " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../../foot.jsp"%>
</body>
</html>