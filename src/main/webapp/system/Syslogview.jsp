<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	
	QueryPage qryp = new QueryPage();
	String flag = request.getParameter("flag");
	String stroffset = request.getParameter("offset");
	
	SimpleDateFormat sdt = new SimpleDateFormat("yyyy-M-d");
	String today = sdt.format(new Date());
	
	String menu_name_str = null;
	menu_name_str="<SELECT NAME='menu_name' class=bk><option value='0'>=��ѡ��=</option>";
	String sqlStr = "select distinct(menu_name) from tab_oper_logs";
	Cursor cursor = DataSetBean.getCursor(sqlStr);
	Map fields = cursor.getNext();
	while(fields!=null) {
		menu_name_str=menu_name_str+"<option value='"+fields.get("menu_name".toLowerCase())+"'>"+fields.get("menu_name".toLowerCase())+"</option>";
		fields = cursor.getNext();
	}
	menu_name_str=menu_name_str+"</select>";
	
	String style = "";
	if("1".equals(flag) || null!=stroffset){
		style = "style='display:none'";
	} else {
		style = "";
	}
	
%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.linkage.litms.common.database.QueryPage"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<FORM NAME="frm" METHOD="post" ACTION="Syslogview.jsp" onSubmit="return CheckFormForm()">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
   <TABLE id="searchLayer" width="98%" border=0 cellspacing=0 cellpadding=0 align="center" <%=style %>>
		<TR>
			<TD bgcolor=#999999>
			  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR> 
                  <TH colspan="4" align="center">WEB������־��ѯ</TH>
                </TR>
                <TR bgcolor="#FFFFFF"> 
                  <TD class=column>�����û�</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="account" class=bk>
                  </TD>
                  <TD class=column width=180>����ģ��</TD>
                  <TD> 
                    <%=menu_name_str%>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF"> 
                  <TD class=column>��ʼʱ��</TD>
                  <TD>
                  	<input type="text" name="sday" class=bk value="<%=today%>">
                    <input type="button" value="��" class=btn onClick="showCalendar('day',event)" name="button1">
                    <input type="text" name="stime" class="bk" size="10"  value="00:00:00">
                  </TD>
                  <TD class=column width=180>����ʱ��</TD>
                  <TD>
                  	<input type="text" name="eday" class=bk value="<%=today%>">
                    <input type="button" value="��" class=btn onClick="showCalendar('day',event)" name="button2">
                    <input type="text" name="etime" class="bk" size="10"  value="23:59:59">
				  </TD>
                </TR>
                <TR bgcolor="#FFFFFF"> 
                  <TD class=foot align=right colspan=4> 
                  	<input type="hidden" name="flag" value="1">
                    <input type="submit" name="submit" value=" �� ѯ " class=btn>
                  </TD>
                </TR>
              </TABLE>
			</TD>
		</TR>
	</TABLE>
</TABLE>

</FORM>
	
	<%
	
	int pagelen = 15;
	int offset=1;
	String logSql = "";
	String strData = "";
	
	if(null!=flag && "1".equals(flag)) {
		String account = request.getParameter("account");
		String menu_name = request.getParameter("menu_name");
		String sday = request.getParameter("sday");
		String eday = request.getParameter("eday");
		if(null!=sday && !"".equals(sday)) {
			sday+=" "+request.getParameter("stime");
		}
		if(null!=eday && !"".equals(sday)) {
			eday+=" "+request.getParameter("etime");
		}
		
		logSql = "select * from tab_oper_logs where sysid=1";
		// teledb
		if (DBUtil.GetDB() == 3) {
			logSql = "select oper_time, staff_id, menu_name, oper_name, remark from tab_oper_logs where sysid=1";
		}
		if(null!=account && !"".equals(account)) {
			logSql+=" and staff_id='"+account+"'";
		}
		if(null!=menu_name && !"0".equals(menu_name)) {
			logSql+=" and menu_name='"+menu_name+"'";
		}
		if(null!=sday && !"".equals(sday)){
			logSql+=" and oper_time>='"+sday+"'";
		}
		if(null!=eday && !"".equals(eday)){
			logSql+=" and oper_time<='"+eday+"'";
		}
		
		logSql+=" order by oper_time desc";
		session.setAttribute("logSql",logSql); 
		stroffset = "1";
	}
	
	if(null!=stroffset) {
		offset = Integer.parseInt(stroffset);
		logSql = String.valueOf(session.getAttribute("logSql"));
		com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(logSql);
		psql.getSQL();
		qryp.initPage(logSql,offset,pagelen);
		String strBar = qryp.getPageBar();
		
		cursor = DataSetBean.getCursor(logSql,offset,pagelen);
		fields = cursor.getNext();
		
		if(null==fields) {
			strData = "<TR ><TD class=column COLSPAN=9 HEIGHT=30>û�з�����������־</TD></TR>";
		} else {
			while(null!=fields) {
				strData+="<TR>";
				strData+="<TD class=column1>"+fields.get("oper_time".toLowerCase())+"</TD>";
				strData+="<TD class=column1>"+fields.get("staff_id".toLowerCase())+"</TD>";
				strData+="<TD class=column1>"+fields.get("menu_name".toLowerCase())+"</TD>";
				strData+="<TD class=column1>"+fields.get("oper_name".toLowerCase())+"</TD>";
				strData+="<TD class=column1>"+fields.get("remark".toLowerCase())+"</TD>";
				strData+="</TR>";
				fields = cursor.getNext();
			}
			strData += "<TR><TD class=column COLSPAN=5 align=right>" + strBar + "</TD></TR>";
		}
%>

<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
            <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
              <TR>
                <TD bgcolor="#ffffff" colspan="5" >
                  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td width="50%">��־�б�<input type="button" name="Button" value="�����ض���־" class="btn" onClick="showSearch()"></td>
                    </tr>
                  </table>
                </TD>
              </TR>
              <TR>
                <TH width="20%">����ʱ��</TH>
                <TH width="10%">�����û�</TH>
                <TH width="20%">�˵�����</TH>
                <TH width="10%">��������</TH>
                <TH width="40%">��ע</TH>
              </TR>
              <%=strData%>
            </TABLE>
			</TD>
		</TR>
	</TABLE>
<%
	}
	%>
<%@ include file="../foot.jsp"%>
<script language="javascript">

//show
//show == 0		����
//show == 1		��ʾ
var show = 0;

function showSearch(){
	if(show == 0){
		show = 1;
		document.all("Button").value = "������־������";
		document.all("searchLayer").style.display="";
	}else{
		show = 0;
		document.all("Button").value = "�����ض���־";
		document.all("searchLayer").style.display="none";
	}
}



</SCRIPT>