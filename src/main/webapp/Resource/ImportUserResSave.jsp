<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="HGWUserInfoAct" scope="request" class="com.linkage.litms.resource.HGWUserInfoAct" />
<%
request.setCharacterEncoding("GBK");
//////////////////////////////////////////////////
//String uploadFile = request.getParameter("uploadFile");
//HGWUserInfoAct.uploadUserFile(uploadFile);
//////////////////////////////////////////////////
String filename = request.getParameter("filename");
int iCode = HGWUserInfoAct.doBatchFromCvs(filename);
String strMsg = null;
if(iCode == -2){
	strMsg = "通知后台获取最大ID失败，请重试！";
}else if(iCode == -1){
	strMsg = "读取上传的CVS文件中无数据！";
}else{
	strMsg = "已经成功导入" + iCode + "个用户！";
}
%>
<%@ include file="../head.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">导入用户信息提示</TH>
					</TR>
					<TR height="50">
						<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
</TABLE>
<%
HGWUserInfoAct = null;
%>
<%@ include file="../foot.jsp"%>