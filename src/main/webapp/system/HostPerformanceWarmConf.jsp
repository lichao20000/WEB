<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="com.linkage.litms.resource.*"%>
<%@page import="java.util.*"%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<jsp:useBean id="office" scope="request" class="com.linkage.litms.resource.OfficeAct"/>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%
request.setCharacterEncoding("GBK");

String userGather_ID = "-1";
String curCity_ID = curUser.getCityId();
Cursor cursorTmp = DataSetBean.getCursor("select gather_id,descr from tab_process_desc where city_id = '" + curCity_ID +"'");
Map fieldTmp = cursorTmp.getNext();
if (fieldTmp != null){
	userGather_ID = (String)fieldTmp.get("gather_id");
}

String curGather_ID = request.getParameter("gather_id");
if(curGather_ID == null){
	curGather_ID = userGather_ID;	
}
String strGatherList = DeviceAct.getGatherList(session,curGather_ID,"",true);
String add_gather_id = DeviceAct.getGatherList(session,userGather_ID,"add_gather_id",true);

String stroffset = request.getParameter("offset");
String strData = "";
int offset;
int pagelen = 15;

if (stroffset == null)
    offset = 1;
else
    offset = Integer.parseInt(stroffset);

String sql = "select * from tab_attrwarconf where gather_id='"+curGather_ID+"' order by hostip,attr_id";
// teledb
if (DBUtil.GetDB() == 3) {
	sql = "select hostip, gather_id, attr_id, warmvalue, s_warmvalue " +
			" from tab_attrwarconf where gather_id='"+curGather_ID+"' order by hostip,attr_id";
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
psql.getSQL();
QueryPage qryp = new QueryPage();
qryp.initPage(sql, offset, pagelen);
String strBar = qryp.getPageBar();
Cursor cursor = DataSetBean.getCursor(sql, offset, pagelen);
Map fields = cursor.getNext();

if(fields == null){
	strData = "<TR><TD CLASS=column COLSPAN=5 HEIGHT=30 align=center>ϵͳû�д˲ɼ������������</TD></TR>";
}
else{
	Map m = new HashMap();
	m.put("1","cpu");
	m.put("2","�ڴ�");
	m.put("3","�ļ�ϵͳ");
	
	String hostip = "";
	int i=0;
	while(fields != null){
		
		strData += "<TR>";		
		if(!hostip.equals(fields.get("hostip"))){
			strData += "<TD rowspan=3 CLASS=column>"+ (String)fields.get("hostip") + "</TD>";
			strData += "<TD rowspan=3 CLASS=column>"+ (String)fields.get("gather_id") + "</TD>";
			i=0;
		}

		strData += "<TD CLASS=column>"+ m.get(fields.get("attr_id")) + " : " + fields.get("warmvalue") + "%</TD>";
		strData += "<TD CLASS=column>"+ m.get(fields.get("attr_id")) + " : " + fields.get("s_warmvalue") + "%</TD>";
		if(i==2)
			i=0;
		else
			i++;
		
		if(!hostip.equals(fields.get("hostip"))){
			strData += "<TD align=center rowspan=3 CLASS=column> <A HREF=UpdateHostPerWarmForm.jsp?hostip="+ fields.get("hostip")+"&gather_id="+ fields.get("gather_id") +" onclick='return Edit(this.href);'>�༭</A> | <A HREF=HostPerWarmSave.jsp?action=delete&hostip="+ fields.get("hostip")+"&gather_id="+ fields.get("gather_id") +" onclick='return delWarn(this.href);'>ɾ��</A></TD>";
			hostip = (String)fields.get("hostip");
		}
		
		strData += "</TR>";		
		fields = cursor.getNext();
	}
	strData += "<TR BGCOLOR=#FFFFFF><TD COLSPAN=5 align=right>" + strBar + "</TD></TR>";
	
	m = null;
}

//clear
fields = null;
cursor = null;
strBar = null;

%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" src="../Js/CheckForm.js"></SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align='center'>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<tr>
		<td>
		<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd" align="center">
			<tr>
				<td width="162" align="center" class="title_bigwhite">
					�������ܷ�ֵ����
				</td>
				<td ><img
					src="../images/attention.gif" width=15 height=12>&nbsp;�������ܸ澯</td>
			</tr>
		</table>
		</td>
	</tr>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="HostPerWarmSave.jsp" target="childFrm">
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TD bgcolor="#ffffff" colspan="4"  align=center>�ɼ��㣺<%= strGatherList%></TD>
						<TD bgcolor="#ffffff" align=right ><a href="#" onClick="document.location.reload()" class="css3">ˢ��</a>&nbsp;&nbsp;<A HREF='javascript:Add();'>����</A>&nbsp;</TD>
					</TR>
					<TR>
						<TH>����IP</TH>
						<TH>�ɼ���</TH>
						<TH>��Ҫ��ֵ</TH>
						<TH>��Ҫ��ֵ</TH>
						<TH>����</TH>
					</TR>
					<%=strData%>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<BR>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center"><SPAN id="actLabel">���</SPAN><SPAN id="hostLabel"></SPAN>�������ܷ�ֵ</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�ɼ��㣺</TD>
						<TD class=column><SPAN id="addgatherLabel" name="addgatherLabel" style="display:none1"><%= add_gather_id%></SPAN><SPAN id="gatherLabel" style="display:none"></SPAN><input type="hidden" name="hid_gather_id" value="">&nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right">����IP��</TD>
						<TD class=column><SPAN id=divHostIP name=divHostIP style="display:none1"></SPAN><SPAN id="hostIPLabel" style="display:none"></SPAN><input type="hidden" name="hid_hostip" value="">&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >cpu��������Ҫ��ֵ��</TD>
						<TD class=column><INPUT TYPE="text" NAME="attr_id1" maxlength=10 class=bk size=10>&nbsp;<font color="#FF0000">(%)</font></TD>
						<TD class=column align="right" >cpu�����ʴ�Ҫ��ֵ��</TD>
						<TD class=column><INPUT TYPE="text" NAME="attr_id1_s" maxlength=10 class=bk size=10>&nbsp;<font color="#FF0000">(%)</font></TD>
					</TR>
					<TR>
						<TD class=column align="right" >�ڴ���������Ҫ��ֵ��</TD>
						<TD class=column><INPUT TYPE="text" NAME="attr_id2" maxlength=10 class=bk size=10>&nbsp;<font color="#FF0000">(%)</font></TD>
						<TD class=column align="right" >�ڴ������ʴ�Ҫ��ֵ��</TD>
						<TD class=column><INPUT TYPE="text" NAME="attr_id2_s" maxlength=10 class=bk size=10>&nbsp;<font color="#FF0000">(%)</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >�ļ�ϵͳʹ������Ҫ��ֵ��</TD>
						<TD class=column><INPUT TYPE="text" NAME="attr_id3" maxlength=10 class=bk size=10>&nbsp;<font color="#FF0000">(%)</font></TD>
						<TD class=column align="right" >�ļ�ϵͳʹ���ʴ�Ҫ��ֵ��</TD>
						<TD class=column><INPUT TYPE="text" NAME="attr_id3_s" maxlength=10 class=bk size=10>&nbsp;<font color="#FF0000">(%)</font></TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class=green_foot>
							<INPUT TYPE="button" value=" �� �� " class=btn onclick="javascript:CheckForm();">&nbsp;&nbsp;
							<INPUT TYPE="hidden" name="action" value="add">
							<INPUT TYPE="reset" value=" �� д " class=btn>
							<SPAN style="display:none"><INPUT TYPE="checkbox" NAME="allHost" value=1>��������Ӧ�õ��˲ɼ�������������</SPAN>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm name=childFrm SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var offset = <%=offset%>;

function Add(){
	document.frm.reset();
	document.frm.action.value="add";
	actLabel.innerHTML = '���';
	hostLabel.innerHTML = '';
	
	document.all.addgatherLabel.style.display="";
	document.all.divHostIP.style.display="";
	
	document.all.gatherLabel.style.display="none";
	document.all.hostIPLabel.style.display="none";
	document.frm.allHost.disabled=false;
	
	showChild("add_gather_id");
}

function Edit(page){
	document.all.frm.allHost.disabled=true;
	var gatherName = document.all.gather_id.options[document.all.gather_id.selectedIndex].text; 
	gatherName = gatherName.replace("--","");
	gatherName = gatherName.replace("--","");
	var srcpage = page+"&gather_name="+gatherName+"&refresh="+Math.random();
	
	document.all("childFrm").src = srcpage;
	
	return false;
}

function delWarn(page){
	if(confirm("���Ҫɾ����������\n��������ɾ���Ĳ��ָܻ�������")){
		document.all("childFrm").src = page;
	}
	
	return false;
}

function CheckForm(){
	var action = document.frm.action.value;
	var attr_id1 = document.frm.attr_id1.value;
	var attr_id2 = document.frm.attr_id2.value;
	var attr_id3 = document.frm.attr_id3.value;
	var attr_id1_s = document.frm.attr_id1_s.value;
	var attr_id2_s = document.frm.attr_id2_s.value;
	var attr_id3_s = document.frm.attr_id3_s.value;

	if(action=="add"){
		if(document.all("add_gather_id").value==-1||document.all("hostip").value==-1){
			alert("��ѡ��ɼ��������IP");
			return false;
		}
	}
	
	if(IsNull(attr_id1,"cpu��ֵ")){
		if(!isNaN(attr_id1)&&attr_id1>=0&&attr_id1<=100&&attr_id1.indexOf(".")==-1&&attr_id1.indexOf("-")==-1){
			
		}else{
			alert("cpu��ֵ��Ϊһ��С��100��������");
			return false;
		}
	}else{
		return false;
	}
	
	if(IsNull(attr_id1,"cpu��ֵ")){
		if(!isNaN(attr_id1)&&attr_id1>=0&&attr_id1<=100&&attr_id1.indexOf(".")==-1&&attr_id1.indexOf("-")==-1){
			
		}else{
			alert("cpu��ֵ��Ϊһ��С��100��������");
			return false;
		}
	}else{
		return false;
	}
	
	if(IsNull(attr_id2,"�ڴ淧ֵ")){
		if(!isNaN(attr_id2)&&attr_id2>=0&&attr_id2<=100&&attr_id2.indexOf(".")==-1&&attr_id2.indexOf("-")==-1){
			
		}else{
			alert("�ڴ淧ֵ��Ϊһ��С��100��������");
			return false;
		}
	}else{
		return false;
	}
	
	if(IsNull(attr_id3,"�ļ�ϵͳ��ֵ")){
		if(!isNaN(attr_id3)&&attr_id3>=0&&attr_id3<=100&&attr_id3.indexOf(".")==-1&&attr_id3.indexOf("-")==-1){
			
		}else{
			alert("�ļ�ϵͳ��ֵ��Ϊһ��С��100��������");
			return false;
		}
	}else{
		return false;
	}
	
	if(IsNull(attr_id1_s,"cpu��ֵ")){
		if(!isNaN(attr_id1_s) && attr_id1_s>=0 && attr_id1_s<=100 && attr_id1_s.indexOf(".")==-1 && attr_id1_s.indexOf("-")==-1){
			
		}else{
			alert("cpu��ֵ��Ϊһ��С��100��������");
			return false;
		}
	}else{
		return false;
	}
	
	if(IsNull(attr_id2_s,"�ڴ淧ֵ")){
		if(!isNaN(attr_id2_s) && attr_id2_s>=0 && attr_id2_s<=100 && attr_id2_s.indexOf(".")==-1 && attr_id2_s.indexOf("-")==-1){
			
		}else{
			alert("�ڴ淧ֵ��Ϊһ��С��100��������");
			return false;
		}
	}else{
		return false;
	}
	
	if(IsNull(attr_id3_s,"�ļ�ϵͳ��ֵ")){
		if(!isNaN(attr_id3_s) && attr_id3_s>=0 && attr_id3_s<=100 && attr_id3_s.indexOf(".")==-1 && attr_id3_s.indexOf("-")==-1){
			
		}else{
			alert("�ļ�ϵͳ��ֵ��Ϊһ��С��100��������");
			return false;
		}
	}else{
		return false;
	}
	
	if (eval(attr_id1) <= eval(attr_id1_s) || eval(attr_id2) <= eval(attr_id2_s) || eval(attr_id3) <= eval(attr_id3_s)){
		alert("��Ҫ��ֵ������ڴ�Ҫ��ֵ��");
		return false;
	}
	
	frm.submit();
}

function showChild(param)
{
	var page ="";
	if(param == "gather_id")
	{
		page = "?gather_id="+document.all("gather_id").value+"&offset="+offset+"&refresh="+Math.random();
		location.href=page;
	}
	else if(param == "add_gather_id")
	{
		page = "getHostIPList.jsp?gather_id="+document.all.add_gather_id.value+"&refresh="+Math.random();
		document.all("childFrm").src = page;
	}
}
//-->
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
if(document.frm.action.value=='add')
	showChild("add_gather_id");
//-->
</SCRIPT>