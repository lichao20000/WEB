<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="paramTreeObject" scope="request" class="com.linkage.litms.paramConfig.ParamTreeObject"/>
<%
    request.setCharacterEncoding("GBK");
    
	String para_name = request.getParameter("param_name");
	
	String gather_id = request.getParameter("gather_id");
	
	String device_id = request.getParameter("device_id");
	
	String ior  = request.getParameter("ior");
	
	String gw_type = request.getParameter("gw_type");
	
	Map upMap = paramTreeObject.upParaValue(para_name,ior,device_id,gather_id);
	
	String strHTML = paramTreeObject.getUpParaHTML(upMap);


%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">

</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
    <FORM METHOD="POST" ACTION="../../dynamicMenu/inside_gonggaoxinxi1broadsava.jsp" NAME="frm" onSubmit="return CheckForm();">
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">	    
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						参数属性列表
					</td>
					<td>
						<img src="../../images/inmp/attention_2.gif" width="15" height="12">
						上报的参数实例属性值。
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH width="60%" nowrap>参数名称</TH>
						<TH width="10%" nowrap>通知方式</TH>
						<TH width="30%" nowrap>存取列表</TH>
					</TR>
					<%=strHTML%>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD colspan=2 align=right hight="20" >
				&nbsp;               				
			</TD>
		</TR>		
		<TR>
			<TD colspan=2 align=right>
				<input type="button" name="close" value=" 关 闭 " onclick="javascript:window.close();" class="btn">               				
			</TD>
		</TR>
	</TABLE>
	</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
</body>
<%@ include file="../foot.jsp"%>