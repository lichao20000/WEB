<%@ include file="../timelater.jsp"%>
<jsp:useBean id="HGWUserInfoAct" scope="request" class="com.linkage.litms.resource.HGWUserInfoAct"/>
<jsp:useBean id="EGWUserInfoAct" scope="request" class="com.linkage.litms.resource.EGWUserInfoAct"/>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*" %>
<%
request.setCharacterEncoding("GBK");
String gwOptType = request.getParameter("gwOptType");
String action = request.getParameter("action");
//��������ʾҳ�棬 1����ʾ�ӿͻ�������������Ҫˢ�¸�ҳ��
String showtype = request.getParameter("showtype");
String strMsg="";
String strGWsrc ="";
if(action != null && action.equals("delete")){
	if(gwOptType.equals("11")){
		strMsg = HGWUserInfoAct.HGWUserInfoActExe(request);
		strGWsrc = "HGWUserInfoList.jsp?opt=edit";
	}else{
		strMsg = EGWUserInfoAct.EGWUserInfoActExe(request);
		strGWsrc = "EGWUserInfoList.jsp?opt=edit";
	}
}else{
	if("12".equals(gwOptType) || "11".equals(gwOptType)){
		strMsg = HGWUserInfoAct.HGWUserInfoActExe(request);
		strGWsrc = "HGWUserInfoList.jsp?opt=edit";
	}else{
		strMsg = EGWUserInfoAct.EGWUserInfoActExe(request);
		strGWsrc = "EGWUserInfoList.jsp?opt=edit";
	}
	//���ص������û����ҳ��
	if("12".equals(gwOptType) || "11".equals(gwOptType)){
		gwOptType = "11";
	}else{
		gwOptType = "21";
	}
}

//String vpn_id = request.getParameter("vpn_id_hid");
%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	this.location = page;
}

function closeWin(){
	window.opener.location.reload();
	window.close();
}
//-->
</SCRIPT>
<SCRIPT FOR=window EVENT=onunload>
closeWin()
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">���ؿͻ���Դ������ʾ��Ϣ</TH>
					</TR>
						<TR  height="50">
							<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
						</TR>
					<TR>
						<TD class=foot align="right">
						<%if ("1".equals(showtype)){ %>
						<INPUT TYPE="button" NAME="close" onclick="closeWin()" value=" �� �� " class=btn>
						<%}else{ %>
                        <INPUT TYPE="button" NAME="cmdJump" onclick="GoList('AddEGWUserInfoForm.jsp?gwOptType=<%= gwOptType%>')" value=" ������û� " class=btn >
						<INPUT TYPE="button" NAME="cmdBack" onclick="GoList('<%=strGWsrc %>')" value=" �û��б� " class=btn>
						<%} %>
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