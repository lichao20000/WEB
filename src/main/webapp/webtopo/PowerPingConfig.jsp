<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.common.database.Cursor, com.linkage.litms.common.database.DataSetBean,java.util.*"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%@ include file="../timelater.jsp"%>
<SCRIPT LANGUAGE="JavaScript" id="idParentFun">
</SCRIPT>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
	request.setCharacterEncoding("GBK");
//	String strIP = request.getParameter("strIP");

	String strData = "";
	String warnmode = "";
	String warnlevel = "";
	String timeoutwarnlevel = "";
	String isstat = "";
	String groupid = "";
//	HashMap isstatMap=new HashMap();

	//�ڹ������в��Ҹ��豸��ع���
	// 0 ����Ҫͳ��
    // 1 ��Ҫͳ��
//    isstatMap.clear();
//    isstatMap.put("0", "����Ҫ");
//    isstatMap.put("1", "��Ҫ");
	String strSql = "select * from pping_group_conf order by groupid";
	// teledb
	if (DBUtil.GetDB() == 3) {
		strSql = "select groupid, warnmode, warnlevel, timeoutwarnlevel, isstat, groupdesc, timeinterval, delaythreshold, timeout" +
				" from pping_group_conf order by groupid";
	}
	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSql);
	psql.getSQL();
	Cursor cursor = DataSetBean.getCursor(strSql);
	Map fields = cursor.getNext();
	if(fields == null){
		strData = "<TR ><TD COLSPAN=10 HEIGHT=30 bgcolor=#FFFFFF>��û������PowerPing!</TD></TR>";
	}
	else{
		while(fields != null){
			groupid = (String)fields.get("groupid");
			warnmode = (String)fields.get("warnmode");
			warnlevel = (String)fields.get("warnlevel");
			timeoutwarnlevel = (String)fields.get("timeoutwarnlevel");
			isstat = (String)fields.get("isstat");
			if ("0".equals(warnmode)) {
				warnmode = "ֻ��һ��";
			} else if ("1".equals(warnmode)) {
				warnmode = "��������";
			} else {
				warnmode = "δ֪";
			}
			if ("0".equals(warnlevel)) {
				warnlevel = "����澯";
			} else if ("1".equals(warnlevel)) {
				warnlevel = "�¼��澯";
			} else if ("2".equals(warnlevel)) {
				warnlevel = "����澯";
			} else if ("3".equals(warnlevel)) {
				warnlevel = "��Ҫ�澯";
			} else if ("4".equals(warnlevel)) {
				warnlevel = "��Ҫ�澯";
			} else if ("5".equals(warnlevel)) {
				warnlevel = "���ظ澯";
			} else {
				warnlevel = "δ֪";
			}
			if ("0".equals(timeoutwarnlevel)) {
				timeoutwarnlevel = "����澯";
			} else if ("1".equals(timeoutwarnlevel)) {
				timeoutwarnlevel = "�¼��澯";
			} else if ("2".equals(timeoutwarnlevel)) {
				timeoutwarnlevel = "����澯";
			} else if ("3".equals(timeoutwarnlevel)) {
				timeoutwarnlevel = "��Ҫ�澯";
			} else if ("4".equals(timeoutwarnlevel)) {
				timeoutwarnlevel = "��Ҫ�澯";
			} else if ("5".equals(timeoutwarnlevel)) {
				timeoutwarnlevel = "���ظ澯";
			} else {
				timeoutwarnlevel = "δ֪";
			}
			if ("0".equals(isstat)) {
				isstat = "����Ҫ";
			} else if ("1".equals(isstat)) {
				isstat = "��Ҫ";
			}
			strData += "<TR bgcolor=#FFFFFF>";
			strData += "<TD class=column1>"+ groupid+ " </TD>";
			strData += "<TD class=column2>"+ (String)fields.get("groupdesc") + "</TD>";
			strData += "<TD class=column1>"+ (String)fields.get("timeinterval") + "</TD>";
			strData += "<TD class=column2>"+ (String)fields.get("delaythreshold") + "</TD>";
			strData += "<TD class=column1>"+ (String)fields.get("timeout") + "</TD>";
			strData += "<TD class=column2>"+ isstat + "</TD>";
			strData += "<TD class=column1>"+ warnmode + "</TD>";
			strData += "<TD class=column2>"+ warnlevel + "</TD>";
			strData += "<TD class=column1>"+ timeoutwarnlevel + "</TD>";
			strData += "<TD class=column2><input type='radio' name='operationRadio' value="+groupid+"></TD>";
			strData += "</TR>";
			fields = cursor.getNext();
		}
	}

%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	
	
//-->
</SCRIPT>

<TABLE width="90%" border="0" cellspacing="0" cellpadding="0" align="center" id="idLayerTable">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<TR>
		<TD valign="top" >
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" bgcolor="#000000">
			<TR class="blue_title"> 
			  <TD >ip��ID</TD>
			  <TD >������</TD>
			  <TD >���ʱ����</TD>
			  <TD >ʱ������ֵ</TD>
			  <TD >�ȴ���ʱֵ</TD>
			  <TD >�Ƿ���Ҫͳ��</TD>
			  <TD >���澯ģʽ</TD>
			  <TD >ʱ�Ӹ澯�ȼ�</TD>
			  <TD >��ʱ�澯�ȼ�</TD>
			  <TD >����</TD>
			</TR>
			 <%=strData%>
			 </TABLE>
		</TD>
	</TR>
	<BR>
	<TR>
		<TD bgcolor=#FFFFFF align=center>
			<INPUT TYPE="button" value=" ������ " class="jianbian"  onclick="addgroup()">&nbsp;&nbsp;
			<INPUT TYPE="button" value=" �༭�� " class="jianbian"  onclick="operationBtn('1')">&nbsp;&nbsp;
			<INPUT TYPE="button" value=" ɾ���� " class="jianbian" onclick="operationBtn('2')">&nbsp;&nbsp;
			<INPUT TYPE="button" value="�鿴IP������" class="jianbian"  onclick="operationBtn('3')">&nbsp;&nbsp;
			<INPUT TYPE="button" value="�鿴IP�б�" class="jianbian"  onclick="operationBtn('4')">&nbsp;&nbsp;
			<INPUT TYPE="button" value=" �� �� " class="jianbian"  onclick="javascript:window.close()">&nbsp;&nbsp;
		</TD>
	</TR>
</TABLE>
<SCRIPT LANGUAGE="JavaScript">
	parent.maxspeed.innerHTML = idLayerTable.innerHTML;	
</SCRIPT>