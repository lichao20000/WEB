<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../timelater.jsp"%>

<jsp:useBean id="roleManage" scope="request" class="com.linkage.litms.system.dbimpl.RoleManagerSyb"/>

<%
	request.setCharacterEncoding("GBK");
	
	// ��ȡ��ǰ��¼�û��Ľ�ɫID���Լ��ӽ�ɫID add by zhangchy 2012-09-04 ----------begin--------
	StringBuffer sbRoleId = new StringBuffer();  // �洢��¼�û��Ľ�ɫID���ӽ�ɫID
	Cursor cursor1 = roleManage.getAllRolesByRolePid(Integer.parseInt(String.valueOf(user.getRoleId())));
	int count = cursor1.getRecordSize();
	Map fields1 = cursor1.getNext();
	while(fields1 != null){
		sbRoleId.append(fields1.get("role_id")).append(",");
		fields1 = cursor1.getNext();
	}
	sbRoleId.append(String.valueOf(user.getRoleId()));
	// add by zhangchy 2012-09-04  ----------------end-----------
	
	// �����SQLҲ���л������������������� where role_id in ("+sbRoleId.toString()+") ֻ�ܲ鿴��ǰ��¼��������ɫ�����ӽ�ɫ�Ľ�ɫ
	String mysql="select role_id,role_name from tab_role where role_id in ("+sbRoleId.toString()+") order by role_id";
	Cursor cursor=DataSetBean.getCursor(mysql);
	Map fields=cursor.getNext();
	String strData="";
	if(fields!=null)
	{
		while(fields!=null)
		{
			strData +="<tr style='cursor:hand;background:#F4F4FF' onmouseover=\"this.style.background='#CECECE';this.style.color='#006790';\" onmouseout=\"this.style.background='#F4F4FF';this.style.color='#000000';\" ondblclick=\"javascript:CallSetValue('"+fields.get("ROLE_ID".toLowerCase())+"','"+fields.get("ROLE_NAME".toLowerCase())+"')\" title=\"˫��ѡ��ý�ɫ\"><td class=column1 align=center>"+fields.get("ROLE_ID".toLowerCase())+"</td><td class=column1 align=center>"+fields.get("ROLE_NAME".toLowerCase())+"</td></tr>";
			fields=cursor.getNext();
		}
	}
	else
	{
		strData="<tr><td colspan=3 class=column1>û�л�ȡ���û���ɫ��Ϣ</td></tr>";
	}
%>

<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function CallSetValue(role_id,role_name)
	{
		opener.SetValue(role_id,role_name);
		window.close();	
	}
//-->
</SCRIPT>
<%@ include file="../head.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
						<TR>
							<TD bgcolor="#ffffff" colspan="3" >�û���ɫ�б�˫��ѡ��</TD>
						</TR>
						<TR>
							<TH>��ɫ���</TH>
							<TH>��ɫ����</TH>							
						</TR>
						<%=strData%>
						<TR>
							<td class=column1 colspan=3 align=right><input type="button" name="close" value=" �� �� " onclick="javascript:window.close();" class="btn"></td>
						</TR>

					</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD>
</TR>
</TABLE>
<%@ include file="../foot.jsp"%>