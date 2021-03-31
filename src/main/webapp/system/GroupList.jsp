<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	
	QueryPage qryp = new QueryPage();
	String strSQL = "";
	
	String stroffset = request.getParameter("offset");
	int pagelen = 15;
	int offset;
	String query=request.getParameter("query");
	String content=request.getParameter("content");
	String s1="";
	String s2="";

	if(query==null || (query!=null && query.equals("all")))
	{
		s1="selected";
		s2="";
		content="";
	}
	else
	{
		s1="";
		s2="selected";
	}
	
	//判断是否是第一次进入页面
	if((stroffset==null && query==null) || (query!=null && query.equals("all")))
	{
		strSQL = "select  * from tab_group ";
		session.putValue("groupSQL",strSQL);
	}
	//判断是查询进入页面
	else if(query!=null && query.length()>0){
		strSQL = "select * from tab_group where "+query+" like '%"+content+"%' ";
		session.putValue("groupSQL",strSQL);
	}

	//通过点击下一页进入
	else if(stroffset !=null && query==null)
	{
		strSQL=(String)session.getValue("groupSQL");
	}

	//out.println(strSQL);
	if(stroffset == null) offset = 1;
	else offset = Integer.parseInt(stroffset);



	qryp.initPage(strSQL,offset,pagelen);
	String search="&query="+query+"&content="+content+"";
	String strBar = qryp.getPageBar(search);
%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.common.database.QueryPage"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function Init(){
	var strXSLSrc = "viewdata.xsl";  
	objXSLDoc = new ActiveXObject("Microsoft.XMLDOM");
	objXSLDoc.async = false;
	objXSLDoc.load( strXSLSrc );
	objXMLDoc = new ActiveXObject("Microsoft.XMLDOM");
	objXMLDoc.async = false;
	var strXMLSrc = idList.XMLSrc;
	objXMLDoc.load( strXMLSrc );
	var sHtml = objXMLDoc.transformNode( objXSLDoc );
	idList.innerHTML = sHtml;
}

function ExecCMD(type){
	var oSel = document.all("group_list");
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
					this.location = "GroupSave.jsp?action=delete&group_oid="+ v;
				}
				break;
			case 1:
				this.location = "EditGroupForm.jsp?group_oid="+ v;
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
		if(o.checked) return o.value;
		else return -1;
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
		<td>用户组一但被删除，则属于这个组的用户组也将被删除，属于组的用户将会剔除出这个组</td>
		</tr>
		</table>
	  </TD>
	</TR>
	<TR height="10" nowrap>
	  <TD></TD>
	</TR>
	<TR>
		<td bgcolor="#ffffff" border="1">请选择要查询的方式
		<select name="query" class="bk">
				<option value="all" <%=s1%>>所有用户组</option>
				<option value="group_name" <%=s2%>>用户组名称</option>				
			</select>&nbsp;&nbsp;&nbsp;查询的内容
		<input type="text" name="content" value="<%=content%>" class="bk">&nbsp;&nbsp;&nbsp;
		<input type="hidden" name="offset" value="<%=offset%>">
		<input type="button" name="ls" value=" 查 询 " class="btn" onclick="javascript:frm.offset.value=1;frm.submit();"></td>
	</Tr>
	<TR height="10" nowrap>
	  <TD></TD>
	</TR>

	<TR>
	  <TD bgcolor="#999999" id="idList" XMLSrc="group_xml.jsp?offset=<%=offset%>&pagelen=<%=pagelen%>"></TD>
	</TR>
	<TR height="22" nowrap>
	  <TD align=right><%=strBar%></TD>
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
				<!-- <INPUT TYPE="button" NAME="cmdEdit" value=" 查看组内用户 " onclick="ExecCMD(0)" class="btn"> -->
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
