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
	strMsg = "֪ͨ��̨��ȡ���IDʧ�ܣ������ԣ�";
}else if(iCode == -1){
	strMsg = "��ȡ�ϴ���CVS�ļ��������ݣ�";
}else{
	strMsg = "�Ѿ��ɹ�����" + iCode + "���û���";
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
						<TH align="center">�����û���Ϣ��ʾ</TH>
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