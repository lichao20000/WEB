<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%
request.setCharacterEncoding("GBK");
//String filename = request.getParameter("filename");
int iCode = DeviceAct.doBatchFromCvs(request);
String strMsg = null;;
if(iCode == -2){
	strMsg = "֪ͨ��̨��ȡ���IDʧ�ܣ������ԣ�";
}else if(iCode == -1){
	strMsg = "��ȡ�ϴ���CVS�ļ��������ݣ�";
}else{
	strMsg = "�Ѿ��ɹ�����" + iCode + "̨�豸��";
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
						<TH align="center">�����豸��Ϣ��ʾ</TH>
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
DeviceAct = null;
%>
<%@ include file="../foot.jsp"%>