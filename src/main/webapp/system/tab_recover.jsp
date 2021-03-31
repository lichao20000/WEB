<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.Cursor"%>
<%@ page import="com.linkage.litms.common.util.*"%>
<%@ page import="com.linkage.litms.common.database.QueryPage"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%
request.setCharacterEncoding("GBK");
curUser = (UserRes) session.getAttribute("curUser");
String account = curUser.getUser().getAccount();

String _sql = "select tab_name,'('+tab_name_zh+')' + tab_name as tab_name_zh from tab_table_sys";
// oracle
if (DBUtil.GetDB() == 1) {
	_sql = "select tab_name, '(' || tab_name_zh || ')' || tab_name as tab_name_zh from tab_table_sys";
}
// teledb
if (DBUtil.GetDB() == 3) {
	_sql = "select tab_name, concat('(', tab_name_zh, ')', tab_name) as tab_name_zh from tab_table_sys";
}
Cursor cursor = DataSetBean.getCursor(_sql);

String tab_box = FormUtil.createListBox(cursor,"tab_name","tab_name_zh",true,"","");

%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript">

function showChild(param){
	if(param == "tab_name"){
		var page = "show_tablename.jsp?table_name=" + document.frm.tab_name.value;
		document.all("childFrm").src = page;
	}
}
function CheckForm(){
	if(document.frm.tab_name.value == "-1"){
		alert("��ѡ��ָ�������");
		return false;
	}

	var oselect = document.all("tab_name_bk");
 	if(oselect == null){
		alert("��ѡ����Ҫ�ָ��ı�");
		return false;
	 }
	var num = 0;
	if(typeof(oselect.length)=="undefined"){
		if(oselect.checked){
			num = 1;
		}
 	}else{
		for(var i=0;i<oselect.length;i++){
			if(oselect[i].checked){
			  num++;
			}
		}

 	}
 	if(num ==0){
		alert("��ѡ����Ҫ�ָ��ı�");
		return false;
	}
}

</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<FORM NAME="frm" METHOD="post" ACTION="tab_recoverSave.jsp" onsubmit="return CheckForm()">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR>
	<TD>
		<table width="98%" align=center  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
		<tr>
			<td width="162" align="center" class="title_bigwhite">
				���ݻָ�
			</td>
		</tr>
		</table>
	</TD>
</TR>
<TR><TD>
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center">���ݻָ�</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">����</TD>
						<TD colspan="1">
							<%=tab_box%>
							&nbsp;<font color="#FF0000">*</font>
						</TD>
						<TD align=right class=column>������Ա</TD>
						<TD colspan="1"><INPUT name="acc_loginname" type="text" class="bk" value="<%=account%>" size="20" readonly>
							&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR BGCOLOR=#ffffff > 
					  <TD align=right class=column>��ʷ����</TD>
					  <TD colspan="3">
							<div id="div_table" style="width:95%; height:100px; z-index:1; top: 100px; overflow:scroll">
									<span>��ѡ����Ҫ�ָ��ı�</span>
							</div>
					  </TD>
					</TR>
					<TR >
						<TD colspan="4" align="right" CLASS=green_foot>						
							<INPUT TYPE="hidden" name="action" value="add">
							<INPUT TYPE="submit" value=" �� �� " class=btn>&nbsp;&nbsp;
							<INPUT TYPE="reset" value=" �� д " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<BR>
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR bgcolor="#FFFFFF">
						<TD width="100%">
							<font color=red>˵��:</font>
							<br>
								<font color=blue>*</font> 
								ѡ����Ҫ�ָ��ı�Ȼ��ѡ��ָ����ļ������Ա���н׶��Իָ���
								<br>
								<font color=blue>*</font>
								�����ָ����ļ�����ڱ��ݵ�Ŀ¼�£��Ա���лָ���
								<br>
							<br>

						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>

</TD></TR>
</FORM>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm name="childFrm" SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
