<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%
request.setCharacterEncoding("GBK");

String strSQL = "select * from tab_group where group_oid="+request.getParameter("group_oid");
// teledb
if (DBUtil.GetDB() == 3) {
	strSQL = "select group_poid from tab_group where group_oid="+request.getParameter("group_oid");
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSQL);
psql.getSQL();
Map m_GroupPoidMap = DataSetBean.getRecord(strSQL);

strSQL = "select group_oid,group_rootid,group_name from tab_group";
Cursor cursor = DataSetBean.getCursor(strSQL);
Map fields = cursor.getNext();
StringBuffer sb = new StringBuffer(200);
sb.append("<select name='group_poid' class='bk'>");
sb.append("<option value='0' rootvalue='0'>请选择父层用户组</option>");
if(fields != null){
	while(fields != null){
		sb.append("<option value='").append(fields.get("group_oid")).append("'"); 
		sb.append(" rootvalue='").append(fields.get("group_rootid")).append("'");
		
		if(m_GroupPoidMap.get("group_poid").equals(fields.get("group_oid"))){
			sb.append(" selected>");			
		}else{
			sb.append(" >");	
		}
		
		sb.append(fields.get("group_name")).append("</option>");

		fields = cursor.getNext();
	}
}
sb.append("</select>");
String p_groupStr = sb.toString();
sb = null;
m_GroupPoidMap = null;

strSQL = "select * from tab_group where group_oid="+request.getParameter("group_oid");
// teledb
if (DBUtil.GetDB() == 3) {
	strSQL = "select group_name, group_oid, group_desc, group_poid from tab_group where group_oid="+request.getParameter("group_oid");
}
fields = DataSetBean.getRecord(strSQL);

%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<style>
SPAN.BT
{
	padding-right: 6px;
	padding-left: 6px;
	padding-top: 2px;
	border-top: #ffffff 1px solid;
	border-bottom: #ffffff 1px solid;
	border-right: #ffffff 1px solid;
	border-left: #ffffff 1px solid;
	cursor:hand;
}

SPAN.BTOver{
	padding-right: 6px;
	padding-left: 6px;
	padding-top: 2px;
	border-top: #316AC5 1px solid;
	border-bottom: #316AC5 1px solid;
	border-right: #316AC5 1px solid;
	border-left: #316AC5 1px solid;
	cursor:hand;
	background-color: #C1D2EE;
}
</style>
<SCRIPT LANGUAGE="JavaScript">
<!--
function CheckForm(){
	var obj = document.frm;
	if(!IsNull(obj.group_name.value,'组名称')){
		obj.group_name.focus();
		obj.group_name.select();
		return false;
	}
	//else if(!IsNull(obj.ga_name.value,'组范围')){
	//	return false;
	//}
	else{
		getGroupRootID();
		return true;
	}	
}

function SelectArea(){
	var page = "DomainPicker.jsp";
	var uObject = new Object();
	var obj = document.frm;
	uObject.name = obj.ga_name.value;
	var vReturnValue = window.showModalDialog(page,uObject,"dialogHeight: 470px; dialogWidth: 540px; dialogTop: px; dialogLeft: px; edge: Raised; center: Yes; help: No; resizable: No; status: No;scroll:No;");
	
	if(typeof(vReturnValue) == "object"){
		obj.ga_name.value = vReturnValue.name;
		obj.ga_oid.value  = vReturnValue.oid;
	}
}

function getGroupRootID(){
	var o = document.frm.group_poid;

	for(var i=0; i<o.length;i++){
		if(o[i].value == o.value){
			document.frm.group_rootid.value= o[i].rootvalue;
			break;
		}
	}
}

function init(v1,v2){
	var o = document.frm.group_poid;

	for(var i=o.length-1;i>=0;i--){
		if(o[i].value == v2){
			o.remove(i);
		}
	}

	for(var i=0; i<o.length;i++){
		if(o[i].value == v1){
			o[i].selected = true;
			break;
		}
	}
}

//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD valign=top>
	<FORM NAME="frm" METHOD="post" ACTION="GroupSave.jsp" onsubmit="return CheckForm()">
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR height="10" nowrap><TD></TD></TR>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="2" align="center"><B>编辑〖<%=fields.get("group_name")%>〗用户组</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">选择父组</TD>
					<TD><%=p_groupStr%><INPUT TYPE="hidden" name="group_rootid"></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >组名称</TD>
					<TD><INPUT TYPE="text" NAME="group_name" maxlength=25 class=bk size=20 value="<%=fields.get("group_name")%>">&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<%
				strSQL = "select domain_id,domain_name from tab_domain where domain_id in (select domain_id from tab_group_domain where group_oid="+fields.get("group_oid")+")";
				cursor = DataSetBean.getCursor(strSQL);
				Map fields2 = cursor.getNext();
				String per_names="",acc_oids="";
				while(fields2!=null){
					if(per_names.equals("")){
						per_names = (String)fields2.get("domain_name");
						acc_oids  = (String)fields2.get("domain_id");
					}
					else{
						per_names += ","+(String)fields2.get("domain_name");
						acc_oids  += ","+(String)fields2.get("domain_id");						
					}

					fields2 = cursor.getNext();
				}
				%>
				 <TR bgcolor="#FFFFFF">
					<TD class=column align="right" >组范围</TD>
					<TD><nobr><INPUT TYPE="text" NAME="ga_name" maxlength=255 class=bk size=80 readonly value="<%=per_names%>"><INPUT TYPE="hidden" NAME="ga_oid" value="<%=acc_oids%>">
					<SPAN class="BT" onmouseover="this.className='BTOver'" onmouseout="this.className='BT'" onclick="SelectArea();"><IMG SRC="images/search.gif" WIDTH="15" HEIGHT="12" BORDER="0" ALT="查找设备组" valign="middle">&nbsp;查找</SPAN>
					</nobr>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >组描述</TD>
					<TD><INPUT TYPE="text" NAME="group_desc" maxlength=40 class=bk size=40 value="<%=fields.get("group_desc")%>"></TD>
				</TR>
				<TR>
					<TD colspan="2" align="center" class=foot>
						<INPUT TYPE="submit" value=" 修 改 " class=btn>&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="action" value="update">
						<INPUT TYPE="hidden" name="group_oid" value="<%=fields.get("group_oid")%>">
						<INPUT TYPE="reset" value=" 重 写 " class=btn>
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
	</TABLE>
	</FORM>
	</TD>
</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
init('<%=fields.get("group_poid")%>','<%=fields.get("group_oid")%>');
//-->
</SCRIPT>
