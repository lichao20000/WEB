<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.common.database.Cursor, com.linkage.litms.common.database.DataSetBean,java.util.*"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%
request.setCharacterEncoding("GBK");
String strAction = request.getParameter("action");
String groupid = request.getParameter("groupid");
String strSQL = "select * from pping_group_conf where groupid="+groupid;
// teledb
if (DBUtil.GetDB() == 3) {
	strSQL = "select groupdesc, timeinterval, delaythreshold, timeout, isstat, warnmode, warnlevel, timeoutwarnlevel" +
			" from pping_group_conf where groupid="+groupid;
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSQL);
psql.getSQL();
String strData = "";
boolean isAdd = false;
String groupdesc = "";
String timeinterval = "";
String delaythreshold = "";
String timeout = "";
String isstat = "";
String warnmode = "";
String warnlevel = "";
String timeoutwarnlevel = "";
long area_id = user.getAreaId();

String selectGatherSql = "select gather_id,descr from tab_process_desc "
	   +"where gather_id in (select res_id from tab_gw_res_area where res_type=2 and area_id="+area_id+")";
Cursor cursor2 = DataSetBean.getCursor(selectGatherSql);
//Map fields2 = cursor2.getNext();
String gatherStr = FormUtil.creatSelectOption(cursor2,"gather_id","descr");
Map fields = DataSetBean.getRecord(strSQL);
if(fields == null){
	// ȡ��groupid
	long l_groupid = DataSetBean.getMaxId("pping_group_conf", "groupid");
	groupid = String.valueOf(l_groupid);
	isAdd = true;
}
else{
	groupdesc = (String)fields.get("groupdesc");
	timeinterval = (String)fields.get("timeinterval");
	delaythreshold = (String)fields.get("delaythreshold");
	timeout = (String)fields.get("timeout");
	isstat = (String)fields.get("isstat");
	warnmode = (String)fields.get("warnmode");
	warnlevel = (String)fields.get("warnlevel");
	timeoutwarnlevel = (String)fields.get("timeoutwarnlevel");
}
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function CheckForm() {
		var obj = document.frm;
		if(!IsNull(obj.timeinterval.value,"���ʱ����")){
			obj.timeinterval.focus();
			obj.timeinterval.select();
			return false;
		} else if(obj.timeinterval.value!="" && Trim(obj.timeinterval.value)==""){
			alert("���ʱ����ӦΪ����");
			obj.timeinterval.focus();
			obj.timeinterval.select();
			return false;
		} else if(obj.timeinterval.value!="" && !IsNumber(obj.timeinterval.value,"���ʱ����")){		
			obj.timeinterval.focus();
			obj.timeinterval.select();
			return false;
		} else if(!IsNull(obj.delaythreshold.value,"ʱ������ֵ")){
			obj.delaythreshold.focus();
			obj.delaythreshold.select();
			return false;
		} else if(obj.delaythreshold.value!="" && Trim(obj.delaythreshold.value)==""){
			alert("ʱ������ֵӦΪ����");
			obj.delaythreshold.focus();
			obj.delaythreshold.select();
			return false;
		} else if(obj.delaythreshold.value!="" && !IsNumber(obj.delaythreshold.value,"ʱ������ֵ")){		
			obj.delaythreshold.focus();
			obj.delaythreshold.select();
			return false;
		} else if(!IsNull(obj.timeout.value,"�ȴ���ʱֵ")){
			obj.timeout.focus();
			obj.timeout.select();
			return false;
		} else if(obj.timeout.value!="" && Trim(obj.timeout.value)==""){
			alert("�ȴ���ʱֵӦΪ����");
			obj.timeout.focus();
			obj.timeout.select();
			return false;
		} else if(obj.timeout.value!="" && !IsNumber(obj.timeout.value,"�ȴ���ʱֵ")){		
			obj.timeout.focus();
			obj.timeout.select();
			return false;
		} else if(obj.gather.value == "-1") {
			alert("��ѡ��ɼ���");
			obj.gather.focus();
			return false;
		} else {
			return true;
		}
	}

	function refreshPage() {
		window.close();
		//alert(parent);
		//alert(opener);
		//alert(opener.location);
		opener.location.reload();
	}

	function editBack() {
		window.history.go(-1);
		window.location.reload();
	}
//-->
</SCRIPT>


<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
<FORM NAME="frm" METHOD="post" ACTION="PowerPingConfigSave.jsp" onsubmit="return CheckForm()" target="childfrm">
    <TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" id="myTable">
		<TR>
			<TD bgcolor=#000000>
				
              <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			   <TR> 
                  <TD bgcolor="#ffffff" colspan="4">��'<font color="#FF0000">*</font>'�ı�������д��ѡ��</TD>
                </TR>
                <TR>
                  <TH colspan="4" align="center">���IP��</TH>
                </TR>
                <TR bgcolor="#FFFFFF"> 
				
                  <TD class=column align="right">IP����</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="groupid" maxlength=100 class=bk value="<%=groupid%>" readonly>
                    <font color="#FF0000">*</font></TD>
                  <TD class=column align="right">������</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="groupdesc" maxlength=100 class=bk value="<%=groupdesc%>">
                  </TD>
                </TR>
				<TR bgcolor="#FFFFFF"> 
				
                  <TD class=column align="right">���ʱ����</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="timeinterval" maxlength=100 class=bk value="<%=timeinterval%>">&nbsp;��&nbsp;<font color="#FF0000">*</font></TD>
                  <TD class=column align="right">ʱ������ֵ</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="delaythreshold" maxlength=100 class=bk value="<%=delaythreshold%>">&nbsp;����&nbsp;<font color="#FF0000">*</font>
                  </TD>
                </TR>
				<TR bgcolor="#FFFFFF">
				
					<TD class=column align="right">�ȴ���ʱֵ</TD>
					<TD><INPUT TYPE="text" NAME="timeout" maxlength=20 class=bk value="<%=timeout%>">&nbsp;����&nbsp;<font color="#FF0000">*</font>
					</TD>
					<TD class=column align="right">���澯ģʽ</TD>
					<TD>
					<SELECT name="warnmode" class=bk>
					<option value="-1" <% if (warnmode.equals("-1")) {%>selected<%}%>>==��ѡ��==</option>
					<option value="0" <% if (warnmode.equals("0")) {%>selected<%}%>>ֻ��һ��</option>
					<option value="1" <% if (warnmode.equals("1")) {%>selected<%}%>>��������</option>
					</SELECT>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF"> 
					<TD class=column align="right">ʱ�Ӹ澯�ȼ�</TD>
					<TD>
					<SELECT name="warnlevel" class=bk>
					<option value="-1" <% if (warnlevel.equals("-1")) {%>selected<%}%>>==��ѡ��==</option>
					<option value="0" <% if (warnlevel.equals("0")) {%>selected<%}%>>�Զ����</option>
					<option value="1" <% if (warnlevel.equals("1")) {%>selected<%}%>>������־</option>
					<option value="2" <% if (warnlevel.equals("2")) {%>selected<%}%>>��ʾ�澯</option>
					<option value="3" <% if (warnlevel.equals("3")) {%>selected<%}%>>һ��澯</option>
					<option value="4" <% if (warnlevel.equals("4")) {%>selected<%}%>>���ظ澯</option>
					<option value="5" <% if (warnlevel.equals("5")) {%>selected<%}%>>�����澯</option>
					</SELECT>
					</TD>
					<TD class=column align="right">��ʱ�澯�ȼ�</TD>
					<TD>
					<SELECT name="timeoutwarnlevel" class=bk>
					<option value="-1" <% if (timeoutwarnlevel.equals("-1")) {%>selected<%}%>>==��ѡ��==</option>
					<option value="0" <% if (timeoutwarnlevel.equals("0")) {%>selected<%}%>>�Զ����</option>
					<option value="1" <% if (timeoutwarnlevel.equals("1")) {%>selected<%}%>>������־</option>
					<option value="2" <% if (timeoutwarnlevel.equals("2")) {%>selected<%}%>>��ʾ�澯</option>
					<option value="3" <% if (timeoutwarnlevel.equals("3")) {%>selected<%}%>>һ��澯</option>
					<option value="4" <% if (timeoutwarnlevel.equals("4")) {%>selected<%}%>>���ظ澯</option>
					<option value="5" <% if (timeoutwarnlevel.equals("5")) {%>selected<%}%>>�����澯</option>
					</SELECT>
					</TD>
                </TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">�ɼ���</TD>
					<TD colspan="3">
					<SELECT name="gather" class=bk>
				      <%=gatherStr%>
				    </SELECT>&nbsp;<font color="#FF0000">*</font>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="left" colspan="4"><INPUT type="checkbox" name="isstat" value="1" <%if (isstat.equals("1")) {%>checked<%}%>>�Ƿ����ɱ���</TD>
				</TR>
                <TR> 
                  <TD colspan="4" align="right" class=foot> 
					<% if (isAdd) {%>
					<INPUT TYPE="submit" value=" �� �� " class="jianbian">
                    &nbsp;&nbsp;
					<INPUT TYPE="button" value=" �� �� " onclick="javascript:window.close()" class="jianbian">
					<INPUT TYPE="hidden" name="action" value="add">
					<%} else {%>
					<INPUT TYPE="submit" value=" �� �� " class="jianbian">
                    &nbsp;&nbsp;
					<INPUT TYPE="button" value=" �� �� " onclick="editBack()" class="jianbian">
					<INPUT TYPE="hidden" name="action" value="edit">
					<%}%>
                  </TD>
                </TR>
              </TABLE>
			</TD>
		</TR>
	</TABLE>
	</FORM>
  </TD></TR>
</TABLE>
<iframe id="childfrm" name="childfrm" align="center" style="display:none"></iframe>