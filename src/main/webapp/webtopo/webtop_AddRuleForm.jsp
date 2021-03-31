<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	String name = request.getParameter("name");
	String desc = request.getParameter("desc");
	String text = request.getParameter("text");
	String result = request.getParameter("result");
	name = name == null ? "" : name;
	desc = desc == null ? "" : desc;
	text = text == null ? "" : text;
	result = result == null ? "" : result;
	
	String script = "setTextValue('" + name + "','" + desc + "','" + text + "','" + result + "');";
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function doClose(){
	  if(checkValue()){
		  var returnObj = new Object();
		  returnObj.text = document.all("ruler_content").value;
		  returnObj.name = document.all("ruler_name").value;
		  returnObj.desc = document.all("ruler_desc").value;
		  var _result = document.all("rule_result");
		  returnObj.result = _result.value;
		  window.returnValue = returnObj;
		  window.close();
	  }
}
function setTextValue(name,desc,text,result){
	document.all("ruler_name").value = name;
	document.all("ruler_desc").value = desc;
	document.all("ruler_content").value = text;
	if(result != "")
		document.all("rule_result").value = result;
}
function checkValue(){
	var obj = document.frm;
	if(!IsNull(obj.ruler_name.value,'规则名称')){
		obj.ruler_name.focus();
		obj.ruler_name.select();
		return false;
	}else if(!IsNull(obj.ruler_content.value,'规则定义')){
		obj.ruler_content.focus();
		obj.ruler_content.select();
		return false;
	}else if(!IsNull(obj.ruler_desc.value,'规则描述')){
		obj.ruler_desc.focus();
		obj.ruler_desc.select();
		return false;
	}
	return true;
}
function OnEditContent()
{
	var winattr="resizable:no;scroll:yes;dialogHeight:600px;dialogWidth:810px;status:no;help:no"
	var StringPara="",pos1=0,pos2=0;
	StringPara=window.showModalDialog("./webtop_RulerContentForm.jsp",window,winattr);
	if(StringPara!=null)
	{
		if(StringPara!=""){
			document.frm.ruler_content.value=trim(StringPara);
		}
	}
}
function trim(str){
	var ch;
	var value = "";
	for(var i=0;i<str.length;i++){
		ch = str.charAt(i);
		if(ch != '^' && ch != '(' && ch != ')'){
			value += ch;
		}
	}
	return value
}
/*
function CheckForm(){
	var obj = document.frm;
	if(!IsNull(obj.ruler_name.value,'规则名称')){
		obj.ruler_name.focus();
		obj.ruler_name.select();
		return false;
	}else if(!IsNull(obj.ruler_name.value,'规则定义')){
		obj.ruler_content.focus();
		obj.ruler_content.select();
		return false;
	}else if(!IsNull(obj.ruler_desc.value,'规则描述')){
		obj.ruler_desc.focus();
		obj.ruler_desc.select();
		return false;
	}
	else if(obj.user_uniformid.value=='0'){
		alert("请选择大客户");
		return false;
	}
	
	else if(!IsNull(obj.ruler_time.value,'作用时间')){
		obj.ruler_time.focus();
		obj.ruler_time.select();
		return false;
	}
	else{
		//alert(obj.ipname.value);
		if(obj.ipname.value!='') {
			obj.is_ip_set.value="1";
			if(!IsIPAddr(obj.ipname.value)) {
				obj.ipname.focus();
				obj.ipname.select();
				return false;
			}
			else return true;
		}
		//if(obj.user_uniformid.value!='0') {obj.is_user_set.value="1";obj.is_ip_set.value="1";}
		return true;
	}
 
}
*/
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
  <FORM NAME="frm" METHOD="post">
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
	  <TR>
		<TD bgcolor=#000000>
		  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<TR>
			  <TD bgcolor="#ffffff" colspan="4">带'<font color="#FF0000">*</font>'的表单必须填写或选择</TD>
			</TR>
			<TR>
				<TH colspan="4" align="center" nowrap>添加规则</TH>
			</TR>
			<TR bgcolor="#FFFFFF" nowrap>
				<TD class=column align="right" nowrap>规则名称</TD>
				<TD><INPUT TYPE="text" NAME="ruler_name" maxlength=100 class=bk>&nbsp;<font color="#FF0000">*</font></TD>
				<TD class=column align="right" nowrap>规则描述</TD>
				<TD><INPUT TYPE="text" NAME="ruler_desc" maxlength=255 class=bk></TD>
			</TR>
			<TR bgcolor="#FFFFFF" nowrap>
				<TD class=column align="right" nowrap>规则定义</TD>
				<TD colspan="3"  align="left" noWrap>
				  <TEXTAREA name="ruler_content" cols="65" readonly></TEXTAREA>
				   &nbsp;&nbsp;
				  <INPUT name="EditRuler" type="button" value="编辑规则" onClick="OnEditContent();"  class=jianbian>&nbsp;<font color="#FF0000">*</font>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF" nowrap>
				<TD class=column align="right" nowrap>当满足上述规则时</TD>
				<TD colspan="3"  align="left" noWrap>
				  <select name="rule_result">
						<option value="继续过滤" selected>继续过滤</option>	
				  		<option value="立即过滤">立即过滤</option>
					</select>
				</TD>
			</TR>
			<TR>
			<TR>
				<TD colspan="4" align="center" class=foot><INPUT TYPE="button" value=" 关 闭 " class=jianbian onclick="doClose()"></TD>
			</TR>
		  </TABLE>
	    </TD>
	  </TR>
	</TABLE>
  </FORM>
<TR><TD>&nbsp;</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
&nbsp;</TD></TR>
</TABLE>
<SCRIPT LANGUAGE=JavaScript>
	<%=script%>
</SCRIPT>
<%@ include file="../foot.jsp"%>