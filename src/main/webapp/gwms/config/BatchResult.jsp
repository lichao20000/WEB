<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>�������������ƽ��</title>
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
						<TH align="center">������ʾ</TH>
					</TR>
					<TR  height="50">
							<!-- �ӱ���ͨ����admin�û�������������Ϊ3�� -->
							<s:if test="%{instAreaName=='hb_lt' && taskNumFalg==1}">
								<TD align=center valign=middle class=column>���û������Ĳ������񳬹�������������������·�</TD>
							</s:if>
							<s:else>
						    	<TD align=center valign=middle class=column>���Զ������,��̨���ڽ��в���</TD>
						    </s:else>
					</TR>
					<TR>
						<TD class='green_foot' align="right">
						<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.go(-1);" value=" �� �� " class=btn>
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