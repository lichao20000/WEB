<%--
@auth: alex(yanhj@lianchuang.com)
@date: 2008-5-21
@desc: ��������״̬
--%>

<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

lookProcess lkProcess = new lookProcess();


//String sqlStr = "select * from tab_oss_proc_conf";
//Cursor cursor = DataSetBean.getCursor(sqlStr);
Cursor cursor = lkProcess.getAllProcess();
Map fields = cursor.getNext();
int num = cursor.getRecordSize();
String[] pNames = new String[num];
String[] pRealNames = new String[num];
String[] pDescs = new String[num];
String[] proc_level = new String[num];
String[] proc_run = new String[num];
String[] proc_port = new String[num];

String javaname = null;
String pname = null;
for(int i=0;i<num;i++) {
	pname = (String)fields.get("proc_name".toLowerCase());
	javaname = (String)fields.get("java_proc_key".toLowerCase());
	pNames[i] = pname;
	if(null!=javaname && !"".equals(javaname)) {
		pRealNames[i] = javaname;
	} else {
		pRealNames[i] = pname;
	}
	pDescs[i] = (String)fields.get("proc_desc".toLowerCase());
	proc_level[i] = (String)fields.get("proc_level".toLowerCase());
	proc_run[i] = (String)fields.get("reserved1".toLowerCase());
	proc_port[i] = (String)fields.get("reserved2".toLowerCase());

	fields = cursor.getNext();
}

//Vector list = lkProcess.getProcessStatus(pRealNames);
String[] pidList = lkProcess.getProcessStatusCorba(pRealNames);

String tmp = "";
String strClr;
String strData = "";

if (pidList != null){
for(int i=0;i<pidList.length;i++){
	if((i%2)==0) strClr="#e7e7e7";
	else strClr = "#FFFFFF";

	strData += "<TR bgcolor='"+strClr+"'>";
	strData += "<TD>"+ pNames[i] + "</TD>";
	
	if (pidList[i] != null && !"".equals(pidList[i]) && !"-1".equals(pidList[i])){
		tmp = pidList[i];
	}
	else{
		tmp = "";
	}
	
	if("".equals(tmp)){
		strData += "<TD>&nbsp;</TD>";
		strData += "<TD><font color=red><b>down</b></font></TD>";
	}
	else{
		strData += "<TD>"+ tmp + "</TD>";
		strData += "<TD><font color=green><b>up</b></font></TD>";
	}
	strData += "<TD>"+ pDescs[i] + "</TD>";
	strData += "<TD>"+ proc_level[i] + "</TD>";
	strData += "<TD>"+ proc_run[i] + "</TD>";
	strData += "<TD>"+ proc_port[i] + "</TD>";
	strData += "</TR>";
}
}
%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.common.lookProcess"%>
<%@page import="java.util.Vector"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function Refresh(){
	n = Math.round(Math.random()*1000);
	this.location = "?number="+ n;
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%">
<TR>
		<TD height="20">&nbsp;</TD>
</TR>
	<TR>
			<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							���̼��
						</TD>
						<TD>
							<img src="../images/attention_2.gif" width="15" height="12">
							&nbsp;&nbsp;��������״̬
						</TD>
						<TD align="right">
							<a href="javascript:Refresh();">ˢ��</a>
						</TD>
					</TR>
				</TABLE>
			</TD>
	</TR>
<TR><TD>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH>��������</TH>
						<TH>PID</TH>
						<TH>����״̬</TH>
						<TH>��������</TH>
						<TH>���̼���</TH>
						<TH>��������</TH>
						<TH>���̶˿�</TH>
					</TR>
					<%out.println(strData);%>
					<TR>
						<TH>��������</TH>
						<TH>PID</TH>
						<TH>����״̬</TH>
						<TH>��������</TH>
						<TH>���̼���</TH>
						<TH>��������</TH>
						<TH>���̶˿�</TH>
					</TR>
					<TR bgcolor='#FFFFFF'>
						<TD colspan="7" >
						<B>��������״̬</B><BR>
						<I>PID:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</I>�������е�ID��;<BR>
						<I>���̼���:&nbsp;</I>��������ʱ�Ⱥ�˳�򣬴���ֵԽС������Խ�ߣ������������������Ϊ1,����ֵ���μ�;<BR>
						<I>��������:&nbsp;</I>������������������������ģ��Ŀ¼�����������<BR>
						<I>���̶˿�:&nbsp;</I>��������ʱ�Ķ˿ں�,������ֵ��ѡ:�ޡ������������ֵ��<BR>
						</TD>
					</TR>

				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20></TD></TR>
</TABLE>
<BR>
<%@ include file="../foot.jsp"%>
