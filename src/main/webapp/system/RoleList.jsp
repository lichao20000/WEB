<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function Init(){
	var strXSLSrc = "viewdata.xsl";  
	objXSLDoc = new ActiveXObject("Microsoft.XMLDOM");
	objXSLDoc.async = false;
	objXSLDoc.load( strXSLSrc );
	objXMLDoc = new ActiveXObject("Microsoft.XMLDOM");
	objXMLDoc.async = false;
	var strXMLSrc = idGroupList.XMLSrc;
	objXMLDoc.load( strXMLSrc );
	var sHtml = objXMLDoc.transformNode( objXSLDoc );
	idGroupList.innerHTML = sHtml;
}

function ExecCMD(type){
	var oSel = document.all("role_list");
	if(oSel == null) {
		alert("没有可操作的数据");
		return;
	}
	var v    = getSelectValue(oSel);
	if(v != -1){
		switch(type){
			case 2:
				var page = "../Js/warning.htm";
				var vReturnValue = window.showModalDialog(page,"","dialogHeight: 217px; dialogWidth: 400px; dialogTop: px; dialogLeft: px; edge: Raised; center: Yes; help: No; resizable: No; status: No;scroll:No;");
				if(vReturnValue){
					//To Add your code
					this.location = "RoleSave.jsp?action=delete&role_id="+ v;
				}
				break;
			case 1:
				this.location = "EditRoleForm.jsp?role_id="+ v;
				break;
			case 0:
				// To Add your code
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
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR><TD valign=top>
  <TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="80%" align="center" ONSELECTSTART="return false;">
	<TR height="10" nowrap><TD></TD></TR>
	<TR>
	  <TD>
		<table border=0 cellpadding=0 cellspacing=0 width="100%" align=center class="GG">
		<tr height=24>
		<td valign=middle width=30 align=center nowrap><img src="images/attention.gif" width=15 height=12></td>
		<td>角色一但被删除，则属于这个角色的用户和动作将会被剔除</td>
		</tr>
		</table>
	  </TD>
	</TR>
	<TR height="10" nowrap>
	  <TD></TD>
	</TR>
	<TR>
	  <TD bgcolor="#999999" id="idGroupList" XMLSrc="role_xml.jsp"></TD>
	</TR>
	<TR height="20" nowrap>
	  <TD></TD>
	</TR>
	<TR>
	  <TD>
		<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
		<TR>
			<TD>
				<INPUT TYPE="button" NAME="cmdEdit" value=" 编 辑 " onclick="ExecCMD(1)" class="btn">
				&nbsp;&nbsp;
				<INPUT TYPE="button" NAME="cmdEdit" value=" 删 除 " onclick="ExecCMD(2)" class="btn">
			</TD>
			<TD align=right>&nbsp;
				<!-- <INPUT TYPE="button" NAME="cmdEdit" value=" 查看角色内用户 " onclick="ExecCMD(0)" class="btn">&nbsp;
				<INPUT TYPE="button" NAME="cmdEdit" value=" 查看角色权限 " onclick="ExecCMD(3)" class="btn"> -->
			</TD>
		</TR>
		</TABLE>
	  </TD>
	</TR>
  </TABLE>
</TD></TR>
</TABLE>
</FORM>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
Init();
//-->
</SCRIPT>