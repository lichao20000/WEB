<%--
FileName	: rpt_lgSave.jsp
Author		: smf
Date			: 2004-12-8
Note			: ���ӡ��޸ġ�ɾ�����ñ�
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String strSQL="";
String strMsg="";
String strAction = request.getParameter("action");
Cursor cursor=null;
int iCode=-1;

String tab_name=request.getParameter("tab_name");
String isclear = request.getParameter("isclear");
String date_type = request.getParameter("date_type");
String date_value = request.getParameter("date_value");
String remark =request.getParameter("remark");
String sys=request.getParameter("sys");
String stroffset=request.getParameter("offset");
	
if(isclear==null)
{
	isclear="0";
}
if(strAction.compareTo("update")==0)
{
	strSQL="update tab_data_clear set isclear="+isclear+" where tab_name='"+tab_name+"'";
}
else
{
	strSQL="select tab_name from tab_data_clear where tab_name='"+tab_name+"'";
	cursor=DataSetBean.getCursor(strSQL);
	strSQL="";
	int recordCount=cursor.getRecordSize();

	//������ʱ�������
	if(recordCount>0)
	{
		strSQL = "update tab_data_clear set isclear="+isclear+", date_type="+date_type+", date_value="+date_value+",remark='"+remark+"' where tab_name='"+tab_name+"'";
		strSQL = StringUtils.replace(strSQL,"=,","=null,");
		strSQL = StringUtils.replace(strSQL,"= where","=null where");
		
	}
	else
	{
		strSQL = "insert into tab_data_clear(tab_name,isclear,date_type,date_value,remark) values('"+tab_name+"',"+isclear+","+date_type+","+date_value+",'"+remark+"')";
					
		strSQL  = StringUtils.replace(strSQL,",,",",null,");
		strSQL  = StringUtils.replace(strSQL,",,",",null,");
		strSQL  = StringUtils.replace(strSQL,",)",",null)");
				
	}
}




//out.println(strSQL);
if(!strSQL.equals("")){
		iCode = DataSetBean.executeUpdate(strSQL);
	}	





if(iCode >=0 ){
	strMsg = "���ñ�����ɹ���";
	}
else{
		strMsg = "���ñ����ʧ�ܣ��뷵�����Ի��Ժ����ԣ�";
	}
%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	this.location = page;
}
//-->
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
						<TH align="center">���ò�����ʾ��Ϣ</TH>
					</TR>
						<TR  height="50">
							<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
						</TR>
					<TR>
						<TD class=foot align="right">
						<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('tab_clear.jsp?sys=<%=sys%>&offset=<%=stroffset%>')" value=" �� �� " class=btn>

						<!--<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.go(-1);" value=" �� �� " class=btn>-->
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