<%--
Author		: yanhj
Date		: 2006-8-2
Desc		: 增加修改用户组.
--%>

<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page errorPage="../dgError.jsp" %>
<jsp:useBean id="code" scope="request" class="com.linkage.litms.system.CodeList"/>
<%
request.setCharacterEncoding("GBK");
String stroffset = request.getParameter("offset");
int pagelen = 15;
int offset;
if(stroffset == null) offset = 1;
else offset = Integer.parseInt(stroffset);

String strBar = String.valueOf(code.getCodeList(request).get(0));
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var objXMLDoc;
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
	var oSel = document.all("code_list");
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
					this.location = "CodeSave.jsp?action=delete&code_id="+ v;
				}
				break;
			case 1:
				Edit(v);
				break;
			case 0:
				Add();
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


function CheckForm(){
	var obj = document.frm;
	if(!IsNull(obj.type.value,'代码类别')){
		obj.type.focus();
		obj.type.select();
		return false;
	}
	if(!IsNull(obj.type_id.value,'代码类别索引号')){
		obj.type_id.focus();
		obj.type_id.select();
		return false;
	}
	if(!IsNumber(obj.type_id.value,'代码类别索引号')){
		obj.type_id.focus();
		obj.type_id.select();
		return false;
	}
	else if(!IsNull(obj.name.value,'代码值')){
		obj.name.focus();
		obj.name.select();
		return false;
	}
	else{
		return true;
	}	
}

function Add(){
	document.frm.reset();
	document.frm.action.value="add";
	codeLabel.innerHTML="";
	actLabel.innerHTML = '添加';
}

function Edit(v){
	document.frm.reset();
	document.frm.action.value="update";
	actLabel.innerHTML = '编辑';
	document.frm.hidcode_id.value= v;
	var v1,v2;
	var oNodeXml = objXMLDoc.selectNodes("//Row");
	var cur_oNodeXml;
	for(var i=0;i<oNodeXml.length;i++){
		if(oNodeXml.item(i).getAttribute("primarykey") == v){
			cur_oNodeXml = oNodeXml.item(i);
			break;
		}
	}
	oNodeXml = cur_oNodeXml.childNodes;
	codeLabel.innerHTML = "〖"+oNodeXml.item(2).getAttribute("value")+"〗";
	document.frm.name.value = oNodeXml.item(2).getAttribute("value");
	document.frm.type_id.value = oNodeXml.item(1).getAttribute("value");
	document.frm.type.value = oNodeXml.item(0).getAttribute("value");
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR><TD valign=top>
  <TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="75%" align="center" >
	<TR height="10" nowrap><TD></TD></TR>
	<TR>
	  <TD>
		<table border=0 cellpadding=0 cellspacing=0 width="100%" align=center class="GG" ONSELECTSTART="return false;">
		<tr height=24>
		<td valign=middle width=30 align=center nowrap><img src="images/attention.gif" width=15 height=12></td>
		<td>表单默认是增加代码</td>
		</tr>
		</table>
	  </TD>
	</TR>
	<TR height="10" nowrap>
	  <TD></TD>
	</TR>
	<TR>
	  <TD bgcolor="#999999" id="idList" XMLSrc="code_xml.jsp?offset=<%=offset%>&pagelen=<%=pagelen%>">
	  </TD>
	</TR>
	<TR height="10" nowrap>
	  <TD></TD>
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
				<INPUT TYPE="button" NAME="cmdEdit" value=" 增 加 " onclick="Add()" class="btn">
			</TD>
		</TR>
		</TABLE>
	  </TD>
	</TR>
	<TR height="20" nowrap>
	  <TD></TD>
	</TR>
	<TR>
	  <TD>
		<FORM NAME="frm" METHOD="post" ACTION="CodeSave.jsp" onsubmit="return CheckForm()">
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#99999>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center" colspan=4><B><SPAN id="actLabel" style="border:0">增加</SPAN><SPAN id="codeLabel"  style="border:0"></SPAN>代码</B></TH>
					</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >代码类别</TD>
							<TD><INPUT TYPE="text" NAME="type" class=bk size=20></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >代码类别索引号</TD>
							<TD><INPUT TYPE="text" NAME="type_id" class=bk size=20></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >代码值</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="name" maxlength=30 class=bk size=20></TD>
						</TR>
						<TR>
							<TD colspan="4" align="right" class=foot>
								<INPUT TYPE="submit" value=" 保 存 " class=btn>&nbsp;&nbsp;
								<INPUT TYPE="hidden" name="action" value="add">
								<INPUT TYPE="hidden" name="hidcode_id">
								<INPUT TYPE="reset" value=" 重 写 " class=btn>
							</TD>
						</TR>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
		</form>
	  </TD>
	</TR>
  </TABLE>
</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
Init();
//-->
</SCRIPT>