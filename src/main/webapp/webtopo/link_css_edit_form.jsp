<%--
Author		: yanhj
Date		: 2006-10-13
Desc		: edit the css of link.
--%>

<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.MCDataSource"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%
	request.setCharacterEncoding("GBK");

	String fromTitle = request.getParameter("from");
	String toTitle = request.getParameter("to");
	String link_id = request.getParameter("link_id");
	String oIndex = request.getParameter("oIndex");

	String action = "add";
	fromTitle = fromTitle == null ? "" :fromTitle;
	toTitle = toTitle == null ? "" :toTitle;

	String link_color = "#7FFF7F";
	String link_weight = "2";
	
	if(null != link_id) {
		String sql = "select * from tab_line_css where link_id='" + link_id + "'";
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql = "select link_color, link_weight from tab_line_css where link_id='" + link_id + "'";
		}
		com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
		psql.getSQL();
		Map map = DataSetBean.getRecord(sql);
		if(map != null) {
			link_color = (String)map.get("link_color");
			link_weight = (String)map.get("link_weight");
			action = "upd";
		} else {
			action = "add";
			link_color = "#7FFF7F";
			link_weight = "2";
		}
	}


%>


<SCRIPT LANGUAGE="JavaScript">
<!--
var oIndex="<%=oIndex%>";
var link_color= "<%=link_color%>";
var link_weight="<%=link_weight%>";
//�ܷ�֪����·����
var flag = false;

var LinkObj = opener.arrLink[oIndex];
if(typeof(LinkObj)=="object"){
	flag = true;
}

function changeColor() {
	line.color = document.frm.link_color.value;	
}

function changeSize() {
	line.size = document.frm.link_weight.value;	
}

function checkForm() {
	var obj = document.frm;

	if(!IsNull(obj.link_weight.value,"��·����")) {
		obj.link_weight.focus();
		obj.link_weight.select();
	} else {
		changeSave();
		return true;
	}
}

function changeSave(){
	if(flag){
		alert("��·���³ɹ�");
		var weight = document.getElementById('link_weight').value;
		var color = document.getElementById('link_color').value;
		opener.arrLink[oIndex]._color = color;
		opener.arrLink[oIndex]._weight = weight;
		opener.arrLink[oIndex].view();
	}else{
		alert("��·����ʧ��");
	}
}
//-->
</SCRIPT>

<%@ include file="../head.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<!-- <TR><TD HEIGHT=20>&nbsp;</TD></TR> -->
<TR><TD>
<TABLE width="99%" height="30"  border="0" align="center" cellpadding="0" cellspacing="0" class="blue_gargtd">
  <TR>
	<TD width="162" align="center"  class="title_bigwhite"> ������·��ʽ </TD>
	<TD bordercolor="0" >&nbsp;&nbsp;<IMG height=12 src="../images/attention_2.gif" width=15>��'<font color="#FF0000">*</font>'�ı�������д��ѡ��.</TD>
  </TR>
</TABLE>
<TABLE width="99%" border=0 cellspacing=0 cellpadding=0 align="center" >
	<TR>
		<TD bgcolor=#999999 height='100%'>
			<FORM NAME="frm" METHOD=POST ACTION="link_css_edit_save.jsp"  onSubmit="return checkForm();" target="childFrm">
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" height='100%'>
				<TR>
					<TH class=column colspan=2>��·:<%=fromTitle%> �� <%=toTitle%></TH>
				</TR>
				<TR>
					<TD class=column align='right' width='40%'>��·����</TD>
					<TD class=column>
						<INPUT TYPE="text" NAME="link_weight" onchange="javascript:changeSize()">(px)&nbsp;<font color="#FF0000">*</font>
					</TD>
				</TR>
				<TR>
					<TD class=column align='right'>��·��ɫ</TD>
					<TD class=column>
						<SELECT NAME="link_color" onchange="javascript:changeColor()">
							<option value="#FF00FF">õ��</option>
							<option value="#800080">�Ϻ�</option>
							<option value="#0000FF">��ɫ</option>
							<option value="#339966">����</option>
							<option value="#7FFF7F" selected>ǳ��</option>
						</SELECT>&nbsp;<font color="#FF0000">*</font>
					</TD>
				</TR>
				<TR>
					<TD class=column align='right'>��·ʾ��</TD>
					<TD class=column align='left'><HR id=line color='green' width='200' size=2></TD>
				</TR>
				<TR>
					<TD class=blue_foot colspan=2 align=right>
						<INPUT NAME="link_id" TYPE="hidden"  value="<%=link_id%>">
						<INPUT NAME="action" TYPE="hidden"  value="<%=action%>">
						<INPUT NAME="save" CLASS="jianbian" TYPE="submit"  value="  �� �� " onClick="javascript:">
						<INPUT NAME="close" CLASS="jianbian" TYPE="button"  value=" �� �� " onClick="javascript:window.close()">
					</TD>
				</TR>
			</TABLE>
			</FORM>
		</TD>
	</TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm  name=childFrm  SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD></TR>
</TABLE>
</TD></TR>
</TABLE>

<SCRIPT LANGUAGE="JavaScript">
<!--
document.frm.link_color.value = link_color;
document.frm.link_weight.value = link_weight;
line.size = document.frm.link_weight.value;	
line.color = document.frm.link_color.value;	
//-->
</SCRIPT>

<%@ include file="../foot.jsp"%>
