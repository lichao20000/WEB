<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.Cursor"%>
<%@ page import="com.linkage.litms.common.database.QueryPage"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%
request.setCharacterEncoding("GBK");
curUser = (UserRes) session.getAttribute("curUser");
String account = curUser.getUser().getAccount();

Map dataMap = new HashMap();

dataMap.put("0","�豸����");
dataMap.put("1","�û�����");
dataMap.put("2","��־����");
dataMap.put("3","�澯����");
dataMap.put("4","��������");


String _sql = "select * from tab_table_sys";
// teledb
if (DBUtil.GetDB() == 3) {
	_sql = "select tab_id, data_type, tab_name_zh, tab_name from tab_table_sys";
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(_sql);
psql.getSQL();
Cursor cursor = DataSetBean.getCursor(_sql);
Map fields = cursor.getNext();
%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript">
<!--

function CheckForm(){
	var oselect = document.all("tab_id");
 	if(oselect == null){
		alert("��ѡ����Ҫ���ݵı�");
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
		alert("��ѡ����Ҫ���ݵı�");
		return false;
	}
}
function selectAll(elmID){
	t_obj = document.all(elmID);
	if(!t_obj) return;
	obj = event.srcElement;
	if(obj.checked){
		if(typeof(t_obj) == "object" ) {
			if(typeof(t_obj.length) != "undefined") {
				for(var i=0; i<t_obj.length; i++){
					t_obj[i].checked = true;
				}
			} else {
				t_obj.checked = true;
			}
		}
	
	}else{
		if(typeof(t_obj) == "object" ) {
			if(typeof(t_obj.length) != "undefined") {
				for(var i=0; i<t_obj.length; i++){
					t_obj[i].checked = false;
				}
			} else {
				t_obj.checked = false;
			}
		}
	}
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<FORM NAME="frm" METHOD="post" ACTION="tab_backupSave.jsp" onsubmit="return CheckForm()">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<table width="98%" align=center  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
		<tr>
			<td width="162" align="center" class="title_bigwhite">
				���ݱ���
			</td>
		</tr>
	</table></TD>
</TR>
<TR><TD>
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center">���ݱ���</TH>
					</TR>
					<TR BGCOLOR=#ffffff > 
					  <TD align=right class=column>����·��</TD>
					  <TD colspan="1"><INPUT name="backup_path" type="text" class="bk" value="/bak/" size="30"></TD>
					  <TD align=right class=column>������Ա</TD>
					  <TD colspan="1"><INPUT name="acc_loginname" type="text" class="bk" value="<%=account%>" size="20" readonly>
						&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right"><INPUT TYPE="checkbox" onclick="selectAll('tab_id')" name="table"></TD>
						<TD colspan="3">
						<% 
							
							int i = 1;
							if(fields != null){
								while (fields != null){

									out.print("<input type='checkbox' id='tab_id' name='tab_id' value='" + fields.get("tab_id") + "'>��" + dataMap.get(fields.get("data_type")) + "��" + fields.get("tab_name_zh") + "(" + fields.get("tab_name") + ")" );

									if(i % 2 == 0){
										out.print("<BR>");
									}
									i++;
									fields = cursor.getNext();
								}																					
							} else {

								out.print("���ȴ���ά�����");
							}
						
						%>

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
								ѡ����Ҫ���ݵı����ݺ�ı���ڱ���·�����Ա�鿴���ݡ�
								<br>
								<font color=blue>*</font>
								���ݺ���ļ����������¸�ʽ��tab_name_������ʱ����(���磺tab_gw_device_20070521081120)��
								<br>&nbsp;&nbsp;table_nameָԭ���ݱ�ı���������Ϊ��ǰʱʱ��;
								<br>
							<br>

						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>

	<%
		dataMap = null;
		fields = null;

	%>

</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm name="childFrm" SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
</FORM>

<%@ include file="../foot.jsp"%>
