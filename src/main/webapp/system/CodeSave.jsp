<%--
Author		: yanhj
Date		: 2006-8-2
Desc		: 增加修改用户组.
--%>

<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String strSQL = null;
String strMsg=null;
String strAction = request.getParameter("action");
String code_id;

if(strAction.equals("delete")){	//删除操作
	code_id = request.getParameter("code_id");
	strSQL = "delete from tab_code where id="+ code_id;
}
else{
    String name = request.getParameter("name");
    String type = request.getParameter("type");
	String type_id = request.getParameter("type_id");
	if(type_id==null||type_id.equals(""))
		type_id = "0";
    if(strAction.equals("add")){	//增加操作
		//判断该代码类别中是否存在相同的代码索引号和代码值
		strSQL = "select * from tab_code where type='"+type+"' and (type_id="+type_id+" or name='"+name+"')";
		// teledb
		if (DBUtil.GetDB() == 3) {
			strSQL = "select type from tab_code where type='"+type+"' and (type_id="+type_id+" or name='"+name+"')";
		}
		PrepareSQL psql2 = new PrepareSQL(strSQL);
		psql2.getSQL();
		if(DataSetBean.getRecord(strSQL)!=null)
			strMsg = "代码类别"+type+"中已经存在代码类别索引号"+type_id+"或代码值"+name+"，请更换代码类别索引号或代码值。";
		else{
			long maxrole_id = DataSetBean.getMaxId("tab_code","id");
			strSQL = "insert into tab_code (id,pid,type,type_id,name) values ("+maxrole_id+",0,'"+type+"',"+type_id+",'"+name+"')";

			strSQL = StringUtils.replace(strSQL,",,",",null,");
			strSQL = StringUtils.replace(strSQL,",,",",null,");
			strSQL = StringUtils.replace(strSQL,",)",",null)");
		}
	}
	else{
		code_id = request.getParameter("hidcode_id");
		//判断该代码类别中是否存在相同的代码索引号和代码值
		//strSQL = "select * from tab_code where type='"+type+"' and id<>"+code_id+" and (type_id="+type_id+" or name='"+name+"')";
		//if(DataSetBean.getRecord(strSQL)!=null)
		//	strMsg = "代码类别"+type+"中已经存在代码索引号"+type_id+"或代码值"+name+"，请更换代码索引号或代码值。";
		//else{
			strSQL = "update tab_code set type='"+type+"',type_id="+type_id+",name='"+name+"' where id="+code_id;

			strSQL = StringUtils.replace(strSQL,"=,","=null,");
			strSQL = StringUtils.replace(strSQL,"= where","=null where");
		//}
	}
}

//out.print(strSQL);
if(strMsg==null && !strSQL.equals("")){
	int iCode = DataSetBean.executeUpdate(strSQL);
	if(iCode > 0){
		strMsg = "系统代码类型操作成功！";
	}
	else{
		strMsg = "系统代码类型操作失败，请返回重试或稍后再试！";
	}
}
%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ page import="com.linkage.commons.db.PrepareSQL" %>
<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	this.location = page;
}

function create(){
	document.all("childFrm").src = "createCodeXML.jsp";
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
				<TH align="center">操作提示信息</TH>
			</TR>
				<TR  height="50">
					<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
				</TR>
			
			<TR>
				<TD class=foot align="right">
				<!-- <INPUT TYPE="button" NAME="cmdJump" onclick="create()" value="生成新的XML文件" class=btn>
				&nbsp;&nbsp;&nbsp;&nbsp; -->
				<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('CodeList.jsp')" value=" 列 表 " class=btn>

				<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.back();" value=" 返 回 " class=btn>
				</TD>
			</TR>
		</TABLE>	  
	  </TD>
	</TR>
	<TR height="20" nowrap>
	  <TD>&nbsp;<IFRAME ID=childFrm SRC="createCodeXML.jsp" STYLE="display:none"></IFRAME></TD>
	</TR>
  </TABLE>
</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
create();
//-->
</SCRIPT>
