<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<jsp:useBean id="roleManage" scope="request" class="com.linkage.litms.system.dbimpl.RoleManagerSyb"/>
<%--
	zhaixf(3412) 2008-05-14
	req:NJLC_HG-BUG-ZHOUMF-20080508-001
--%>
<%
request.setCharacterEncoding("GBK");
String username = curUser.getUser().getAccount();
String shortName = LipossGlobals.getLipossProperty("InstArea.ShortName");
boolean isRole = false;
if("ah_dx".equals(shortName)){
	isRole = true;
}
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--

function Init(){
	/* var strXSLSrc = "viewdata.xsl";  
	objXSLDoc = new ActiveXObject("Microsoft.XMLDOM");
	objXSLDoc.async = false;
	objXSLDoc.load( strXSLSrc );
	objXMLDoc = new ActiveXObject("Microsoft.XMLDOM");
	objXMLDoc.async = false;
	var strXMLSrc = idGroupList.XMLSrc;
	var xmlDoc = objXMLDoc.load( strXMLSrc );
	var sHtml = objXMLDoc.transformNode( objXSLDoc );
	idGroupList.innerHTML = sHtml; */
	<%
	request.setCharacterEncoding("GBK");
	Cursor cursor= roleManage.getAllRolesByRolePid(Integer.parseInt(String.valueOf(user.getRoleId())));
	int count = cursor.getRecordSize();
	Map fields = cursor.getNext();
	%>

}

function ExecCMD(type){
	var oSel = document.all("role_list");
	if(oSel == null) {
		alert("没有可操作的数据");
		return;
	}
	var value = getSelectValue(oSel);
	if(value != -1){
		var v = value.split("|||")[0];
		var f = value.split("|||")[1];
		var p = value.split("|||")[2];
		
		var curtUser = '<%=username%>';
		var role = '<%=isRole%>';

		switch(type){
			case 2:
			  if("1"==p || "1" == f){
				/*var page = "../Js/warning.htm";
				var vReturnValue = window.showModalDialog(page,"","dialogHeight: 217px; dialogWidth: 400px; dialogTop: px; dialogLeft: px; edge: Raised; center: Yes; help: No; resizable: No; status: No;scroll:No;");
				if(vReturnValue){
					//To Add your code
					this.location = "roleDelete_Menutree.jsp?role_id="+ v;
				}*/
                if(!confirm("您确实要永久地删除该条数据吗？")){
                  return ;
                }
                //To Add your code
                this.location = "roleDelete_Menutree.jsp?role_id="+ v;
			  }else{
				  if(role && curtUser == "admin"){
					  this.location = "droitEdit_Menutree.jsp?tmp_treeitemid=<%=temp_TreeItemId%>&role_id="+ v+"&opt=edit";
				  }else{
					  alert("您不具有该角色的删除权限");
				  }
			  }
				break;
			case 1:
			  if("1" == f){
				this.location = "droitEdit_Menutree.jsp?tmp_treeitemid=<%=temp_TreeItemId%>&role_id="+ v+"&opt=edit";
				//alert("droitEdit_Menutree.jsp?tmp_treeitemid=<%=temp_TreeItemId%>&role_id="+ v);
			  }else{
				  
				  if(role && curtUser == "admin"){
					  this.location = "droitEdit_Menutree.jsp?tmp_treeitemid=<%=temp_TreeItemId%>&role_id="+ v+"&opt=edit";
				  }else{
					  alert("您不具有该角色的修改权限");
				  }
			  }
				break;
			case 0:
				// To Add your code
				this.location = "droitEdit_Menutree.jsp?tmp_treeitemid=<%=temp_TreeItemId%>&role_id="+ v +"&opt=view";
				break;
		}
	}
	else{
		alert("请选择要操作的行");
	}
}


function getSelectValue(o){
	if(typeof(o[0]) != "object"){
		return o.value;
	}

	for(var i=0; i<o.length; i++){
		if(o[i].checked){
			return o[i].value;
		}
	}

	return -1;
}

//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<FORM NAME="frm">
<BR>
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align='center'>
<TR>
	<TD>
		<TABLE width="100%"height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
			<TR>
				<TD width="162" align="center" class="title_bigwhite">角色列表</TD>
				<TD ><img src="../images/attention.gif" width=15 height=12>&nbsp;角色一但被删除，则属于这个角色的用户和动作将会被剔除</TD>
			</TR>
		</TABLE>
	</TD>
</TR>
<TR>
	<TD bgcolor=#999999>
		  <TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="100%" ONSELECTSTART="return false;">
			<TR>
				<TH colspan="1" align="center">角色列表</TH>
			</TR>
			<TR>
			  <TD bgcolor="#999999" id="idGroupList" XMLSrc="role_xml.jsp">
			  <TABLE cellSpacing=1 cellPadding=3 width="100%" border=0><TBODY>
<%
	if(fields != null){
	%>
<TR bgColor=#ffffff>
<TD colSpan=4>角色列表 </TD></TR>
<TR bgColor=#e1eeee>
<TD class=dataHead width=30>
<TD class=dataHead>角色名称 </TD>
<TD class=dataHead>描述 </TD>
<TD class=dataHead>是否可编辑 </TD></TR>
<%
	int i = 0;
	String flg = "";
	while(fields != null){
	if (!(fields.get("role_id").equals(user.getRoleId()))) {
			
			if(fields.get("acc_oid").equals(String.valueOf(user.getId()))){
				flg = "1";
			}
	%>
<TR bgColor=<%=(i % 2) == 0 ? "#FFFFFF" : "#F6F6F6"%>>
<TD align=center><INPUT type=radio value=<%=fields.get("role_id") + "|||" + flg + "|||" + String.valueOf(user.getRoleId())%> name=role_list> </TD>
<TD><%=String.valueOf(fields.get("role_name")).replaceAll("&", "&amp;")%> </TD>
<TD><%=String.valueOf(fields.get("role_desc")).replaceAll("&", "&amp;")%> </TD>
<TD><%="1".equals(flg)? "是":"否" %></TD></TR>
<%
	i++;
	flg = "";
	}
	fields = cursor.getNext();
	}}%>
</TBODY></TABLE>
			  </TD>
			</TR>
			<TR>
			  <TD>
				<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
				<TR>
					<TD align='right'  class='green_foot'>
						<INPUT TYPE="button" NAME="cmdEdit" value=" 查 看 " onclick="ExecCMD(0)" class="btn">
						&nbsp;&nbsp;
						<INPUT TYPE="button" NAME="cmdEdit" value=" 编 辑 " onclick="ExecCMD(1)" class="btn">
						&nbsp;&nbsp;
						<INPUT TYPE="button" NAME="cmdEdit" value=" 删 除 " onclick="ExecCMD(2)" class="btn">
						&nbsp;
						<!-- <INPUT TYPE="button" NAME="cmdEdit" value=" 查看角色内用户 " onclick="ExecCMD(0)" class="btn">&nbsp;
						<INPUT TYPE="button" NAME="cmdEdit" value=" 查看角色权限 " onclick="ExecCMD(3)" class="btn"> -->
					</TD>
				</TR>
				</TABLE>
			  </TD>
			</TR>
		  </TABLE>
	</TD>
</TR>
</TABLE>


</FORM>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
Init();
//-->
</SCRIPT>