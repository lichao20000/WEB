<%--
FileName	: GroupSave.jsp
Author		: yanhj
Date		: 2006-8-1
Desc		: �����޸��û���.
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%
request.setCharacterEncoding("GBK");
String strSQL = null;
String strMsg=null;
String strAction = request.getParameter("action");

if(strAction.equals("delete")){	//ɾ������
	String group_oid = request.getParameter("group_oid");
	strSQL = "delete from tab_group where group_oid="+ group_oid +" or group_poid="+ group_oid;
	strSQL += ";delete from tab_group_domain where group_oid="+ group_oid;
	//strSQL += ";delete from tab_group_city where group_oid="+ group_oid;
}
else{
	String group_poid = request.getParameter("group_poid");
	String group_rootid = request.getParameter("group_rootid");
	String group_name = request.getParameter("group_name");
	String group_desc = request.getParameter("group_desc");
	String acc_oids   = request.getParameter("ga_oid");
	String group_oid=null;
	
	if(!group_poid.equals("0") && group_rootid.equals("0")){
		group_rootid = group_poid;
	}

    if(strAction.equals("add")){	//���Ӳ���
		//�ж��Ƿ��Ѿ�����
		strSQL = "select * from tab_group where group_name='"+group_name+"'";
		// teledb
		if (DBUtil.GetDB() == 3) {
			strSQL = "select group_name from tab_group where group_name='"+group_name+"'";
		}
		if(DataSetBean.getRecord(strSQL)!=null)
			strMsg = "�û���"+group_name+"�Ѿ����ڣ��뻻һ�����ơ�";
		else{
			long maxgroup_oid = DataSetBean.getMaxId("tab_group","group_oid");
			strSQL = "insert into tab_group (group_oid,group_poid,group_rootid,group_name,group_desc) values ("+maxgroup_oid+","+group_poid+","+group_rootid+",'"+group_name+"','"+group_desc+"')";

			strSQL = StringUtils.replace(strSQL,",,",",null,");
			strSQL = StringUtils.replace(strSQL,",,",",null,");
			strSQL = StringUtils.replace(strSQL,",)",",null)");

			group_oid = String.valueOf(maxgroup_oid);
		}
	}
	else{
		group_oid = request.getParameter("group_oid");
		//�ж��Ƿ�������û���ͬ��
		strSQL = "select * from tab_group where group_name='"+group_name+"' and group_oid<>"+group_oid;
		// teledb
		if (DBUtil.GetDB() == 3) {
			strSQL = "select group_name from tab_group where group_name='"+group_name+"' and group_oid<>"+group_oid;
		}
		if(DataSetBean.getRecord(strSQL)!=null)
			strMsg = strMsg = "�û���"+group_name+"�Ѿ����ڣ��뻻һ�����ơ�";
		else{
			strSQL = "update tab_group set group_poid="+group_poid+",group_rootid="+group_rootid+",group_name='"+group_name+"',group_desc='"+group_desc+"' where group_oid="+group_oid;

			strSQL = StringUtils.replace(strSQL,"=,","=null,");
			strSQL = StringUtils.replace(strSQL,"= where","=null where");

			//strSQL += ";delete from tab_group_city where group_oid="+group_oid;
			strSQL += ";delete from tab_group_domain where group_oid="+group_oid;
		}
	}

	//String[] acc_oid_arr = acc_oids.split(",");
	//for(int i=0;i<acc_oid_arr.length;i++){
	//	strSQL += ";insert into tab_group_city (city_id,group_oid) values ("+acc_oid_arr[i]+","+group_oid+")";
	//}

	if(strMsg==null){
		String[] acc_oid_arr = acc_oids.split(",");
		for(int i=0;i<acc_oid_arr.length;i++){
			if(acc_oid_arr[i]!=null && !acc_oid_arr[i].trim().equals(""))
				strSQL += ";insert into tab_group_domain (domain_id,group_oid) values ('"+acc_oid_arr[i]+"',"+group_oid+")";
		}
	}
}

//out.print(strSQL);
if(strMsg==null && !strSQL.equals("")){
	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSQL);
	psql.getSQL();
	int iCode[] = DataSetBean.doBatch(strSQL);
	if(iCode!=null && iCode.length > 0){
		strMsg = "�û��鱣������ɹ���";
	}
	else{
		strMsg = "�û��鱣�����ʧ�ܣ��뷵�����Ի��Ժ����ԣ�";
	}
}
%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	this.location = page;
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR><TD valign=top>
  <TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" width="65%" align="center" ONSELECTSTART="return false;" nowrap>
	<TR height="10" nowrap><TD></TD></TR>
	<TR>
	  <TD bgcolor="#999999">
		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<TR>
				<TH align="center">������ʾ��Ϣ</TH>
			</TR>
				<TR  height="50">
					<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
				</TR>
			<TR>
				<TD class=foot align="right">
				<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('GroupList.jsp')" value=" �� �� " class=btn>

				<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.back();" value=" �� �� " class=btn>
				</TD>
			</TR>
		</TABLE>	  
	  </TD>
	</TR>
	<TR height="20" nowrap>
	  <TD></TD>
	</TR>
  </TABLE>
</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
