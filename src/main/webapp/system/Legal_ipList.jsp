<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page errorPage="../dgError.jsp" %>
<%
request.setCharacterEncoding("GBK");

QueryPage qryp = new QueryPage();
String strSQL = "select legal_ip,ip_pin,ip_desc from snmp_conf_legal_ip order by legal_ip";
String stroffset = request.getParameter("offset");
int pagelen = 15;
int offset;
if(stroffset == null) offset = 1;
else offset = Integer.parseInt(stroffset);
qryp.initPage(strSQL,offset,pagelen);
String strBar = qryp.getPageBar();

Cursor cursor = DataSetBean.getCursor(strSQL,offset,pagelen);
session.putValue("legal_ip_data",cursor);
%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.common.database.QueryPage"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
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
	var oSel = document.all("legal_ip_list");
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
					this.location = "Legal_ipSave.jsp?action=delete&legal_ip="+ v;
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
	if(!IsIPAddr(obj.legal_ip.value)){
		obj.legal_ip.focus();
		obj.legal_ip.select();
		return false;
	}
	else if(!IsNull(obj.ip_pin.value,'校验字节串')){
		obj.ip_pin.focus();
		obj.ip_pin.select();
		return false;
	}
	else{
		return true;
	}	
}

function Add(){
	document.frm.reset();
	document.frm.legal_ip.readOnly = false;
	document.frm.action.value="add";
	idLabel.innerHTML="";
	actLabel.innerHTML = '添加';
}

function Edit(v){
	document.frm.reset();
	document.frm.action.value="update";
	actLabel.innerHTML = '编辑';
	var oNodeXml = objXMLDoc.selectNodes("//Row");
	var cur_oNodeXml;
	for(var i=0;i<oNodeXml.length;i++){
		if(oNodeXml.item(i).getAttribute("primarykey") == v){
			cur_oNodeXml = oNodeXml.item(i);
			break;
		}
	}
	oNodeXml = cur_oNodeXml.childNodes;
	idLabel.innerHTML = "〖"+oNodeXml.item(0).getAttribute("value")+"〗";
	document.frm.legal_ip.value = oNodeXml.item(0).getAttribute("value");
	document.frm.legal_ip.readOnly = true;
	document.frm.ip_pin.value = oNodeXml.item(1).getAttribute("value");
	document.frm.ip_desc.value = oNodeXml.item(2).getAttribute("value");
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
		<td>是用于限制可访问SnmpConf的客户IP地址</td>
		</tr>
		</table>
	  </TD>
	</TR>
	<TR height="10" nowrap>
	  <TD></TD>
	</TR>
	<TR>
	  <TD bgcolor="#999999" id="idList" XMLSrc="legal_ip_xml.jsp">
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
		<FORM NAME="frm" METHOD="post" ACTION="Legal_ipSave.jsp" onsubmit="return CheckForm()">
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#99999>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center" colspan=4><B><SPAN id="actLabel">增加</SPAN><SPAN id="idLabel"></SPAN>合法SnmpConf的客户IP</B></TH>
					</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >IP地址</TD>
							<TD><INPUT TYPE="text" NAME="legal_ip" class=bk size=20 ></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >校验字节串</TD>
							<TD><INPUT TYPE="text" NAME="ip_pin" class=bk size=20></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >描述</TD>
							<TD colspan=3><INPUT TYPE="text" NAME="ip_desc" maxlength=30 class=bk size=20></TD>
						</TR>
						<TR>
							<TD colspan="4" align="right" class=foot>
								<INPUT TYPE="submit" value=" 保 存 " class=btn>&nbsp;&nbsp;
								<INPUT TYPE="hidden" name="action" value="add">
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