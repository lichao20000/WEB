<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%--
	zhaixf(3412) 2008-04-22
	JSDX_ITMS-BUG-YHJ-20080421-001
--%>
<%--
	zhaixf(3412) 2008-05-12
	req:NJLC_HG-BUG-ZHOUMF-20080508-001
--%>
<jsp:useBean id="EGWUserInfoAct" scope="request" class="com.linkage.litms.resource.EGWUserInfoAct"/>
<%@ page import="com.linkage.litms.*" %> 
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>

<%
request.setCharacterEncoding("GBK");
ArrayList list = new ArrayList();
list.clear();

String oui = request.getParameter("oui");
String device_serialnumber = request.getParameter("device_serialnumber");
String customer_id = request.getParameter("customer_id");

//�����û��б�
list = EGWUserInfoAct.getEGWUsersList(request);

//flg
String flg = request.getParameter("flg");

String strData = "";
String rtnMsg = "";
String strBar = String.valueOf(list.get(0));
Cursor cursor = (Cursor)list.get(1);
if (list.size()==3 && list.get(2) != null) {
	rtnMsg = (String)list.get(2);
}

Map fields = cursor.getNext();
if(fields == null){
	strData = "<TR><TD class=column COLSPAN=9 HEIGHT=30>û�м�������ͥ�����û�!</TD></TR>";
}
else{
	String bindType = "";
	String cust_type = "";
	Calendar time=Calendar.getInstance(); 
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	String formatTime = "";
    while(fields != null){
		cust_type = "";
		formatTime = "";
		if ("0".equals((String)fields.get("cust_type_id"))) {
			cust_type = "��˾�ͻ�";
		} else if ("1".equals((String)fields.get("cust_type_id"))) {
			cust_type = "���ɿͻ�";
		} else if ("2".equals((String)fields.get("cust_type_id"))) {
			cust_type = "���˿ͻ�";
		} else{
			cust_type = "-";
		}
		
		if( fields.get("device_serialnumber") == null || fields.get("device_serialnumber").equals(""))
			bindType = "��";
		else
			bindType = "��";

		String user_type_id = (String)fields.get("user_type_id");
		String tmp = "";
		if(user_type_id != null && !user_type_id.equals("")){
			if(user_type_id.equals("1")){
				tmp = "�ֳ���װ";
			} else if(user_type_id.equals("2")){
				tmp = "BSS�û�";
			} else {
				tmp = "�ֹ����";
			}
		}else{
			tmp = "�ֹ����";
		}
		
		if(fields.get("dealdate") != null && !(fields.get("dealdate").equals(""))
				&& !fields.get("dealdate").equals("0")){
			time.setTimeInMillis((Long.parseLong((String)fields.get("dealdate")))*1000);
			formatTime = df.format(time.getTime());
		}else{
			formatTime = "-";
		}
		
		String user_state = (String)fields.get("user_state");

		String username = (String)fields.get("username");
		if(username == null || "".equals(username))
			username = "-";
		
		String phonenumber = (String)fields.get("phonenumber");
		if(phonenumber == null || "".equals(phonenumber))
			phonenumber = "-";
		
		String serv_type = (String)fields.get("serv_type_id");
		serv_type = (String)Global.Serv_type_Map.get(serv_type);
		if(serv_type == null || "".equals(serv_type))
				serv_type = "-";

		
	    strData += "<TR>";
		strData += "<TD class=column1 ><input type='checkbox' name='isCheckedToDel' value='"+(String)fields.get("user_id")+"|"+(String)fields.get("gather_id")+"'></TD>";
		strData += "<TD class=column1 align='left'>"+ username + "</TD>";
		strData += "<TD class=column2 align='left'>"+ phonenumber + "</TD>";
		strData += "<TD class=column1 align='left'>"+ tmp + "</TD>";
		strData += "<TD class=column1 align='left'>"+ serv_type + "</TD>";
		strData += "<TD class=column2 align='left'>"+ cust_type + "</TD>";
		strData += "<TD class=column1 align='left'>"+ bindType + "</TD>";
		strData += "<TD class=column2 align='left'>"+ formatTime + "</TD>";

		if(user_state.equals("1")||user_state.equals("2")){
			strData += "<TD class=column1 align='right'><A HREF='#' onclick=\"editUser('"
					+(String)fields.get("user_id")+"','"+(String)fields.get("gather_id")+"')\""
					+">�༭</A> | <A HREF='#' onclick=\"delUser('"
					+(String)fields.get("user_id")+"','"+(String)fields.get("gather_id")
					+"')\">ɾ��</A> | <A HREF=\"javascript:GoContent(\'"
					+(String)fields.get("user_id")+"\',\'"+(String)fields.get("gather_id")
					+"\');\" TITLE=�鿴�û�"+(String)fields.get("realname")+"�������Ϣ>"
					+ "��ϸ��Ϣ</A></TD>";
		}else if (user_state.equals("3")){
			strData += "<TD class=column1 align='right'>������ | "
					+"<A HREF='#' onclick=\"delUser('"
					+(String)fields.get("user_id")+"','"+(String)fields.get("gather_id")
					+"')\">ɾ��</A> | <A HREF=\"javascript:GoContent(\'"
					+(String)fields.get("user_id")+"\',\'"+(String)fields.get("gather_id")
					+"\');\" TITLE=�鿴�û�"+(String)fields.get("realname")+"�������Ϣ>"
					+ "��ϸ��Ϣ</A></TD>";
		}else if (flg!=null){
			strData += "<TD class=column1 align='right'> <A HREF='#' onclick=\"delUser('"
					+(String)fields.get("user_id")+"','"+(String)fields.get("gather_id")
					+"')\">ɾ��</A> | <A HREF=\"javascript:GoContent(\'"
					+(String)fields.get("user_id")+"\',\'"+(String)fields.get("gather_id")
					+"\');\" TITLE=�鿴�û�"+(String)fields.get("realname")+"�������Ϣ>"
					+ "��ϸ��Ϣ</A></TD>";
		}
		strData += "</TR>";
		fields = cursor.getNext();
	}
	strData += "<TR><TD class=column COLSPAN=9 align=right>" + strBar + "</TD></TR>";
}

fields = null;
list.clear();
list = null;
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var flag = true;

function go(){
	v = document.all("txtpage").value;
	if(parseInt(v) && parseInt(v)>0){
		this.location = "EGWUserInfoList.jsp?offset="+ ((eval(v)-1)*15+1);
	}
}
//����
function addUser(){
	var strpage="../Resource/AddEGWUserInfoForm.jsp?gwOptType=21&showtype=1&oui=<%=oui%>&vender=<%=device_serialnumber%>&customer_id=<%=customer_id%>";
	window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}

//�༭
function editUser(user_id, gather_id){
	var strpage="../Resource/AddEGWUserInfoForm.jsp?gwOptType=22&showtype=1&oui=<%=oui%>&vender=<%=device_serialnumber%>&customer_id=<%=customer_id%>&user_id=" + user_id + "&gather_id=" + gather_id;
	window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}
//ɾ��
function delUser(user_id, gather_id){
	if(confirm("ȷ��Ҫɾ�����û����û�����Ӧ���豸��\n��������ɾ���Ĳ��ָܻ�������")){
		var strpage="../Resource/EGWUserInfoSave.jsp?gwOptType=21&showtype=1&action=delete&user_id=" + user_id + "&gather_id=" + gather_id;
		window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
	}
	else{
		return false;
	}
}

//�鿴�û���ص���Ϣ
function GoContent(user_id, gather_id){
	var strpage="../Resource/EGWUserRelatedInfo.jsp?user_id=" + user_id + "&gather_id=" + gather_id;
	window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}

//ѡ��ȫ��
function checkedAll(){
	var oSelect = document.all("isCheckedToDel");
	if (flag) {
		if(oSelect != null && typeof(oSelect) == "object" ) {
			if(typeof(oSelect.length) != "undefined") {
				for(var i=0; i<oSelect.length; i++) {
					oSelect[i].checked = true;
				}
			}
			else {
				oSelect.checked  = true;
			}
		} else {
			//alert("û�п�ѡ����û�!");
		}
		flag = false;
	} else {
	if(oSelect != null && typeof(oSelect) == "object" ) {
			if(typeof(oSelect.length) != "undefined") {
				for(var i=0; i<oSelect.length; i++) {
					oSelect[i].checked = false;
				}
			}
			else {
				oSelect.checked  = false;
			}
		} else {
			//alert("û�п�ѡ����û�!");
		}
		flag = true;
	}
	
}
//���ɾ��ʱ��FORM
function CheckFormDel() {
	var oSelect = document.all("isCheckedToDel");
	if(oSelect != null && typeof(oSelect) == "object" ) {
		if(typeof(oSelect.length) != "undefined") {
			for(var i=0; i<oSelect.length; i++) {
				if(oSelect[i].checked) {
					if (!delWarn()) {
						return false;
					} else {
						return true;
					}
				}
			}
			alert("����ѡ���û�!");
			return false;
		}
		else {
			if(oSelect.checked) {
				if (!delWarn()) {
					return false;
				} else {
					return true;
				}
			} else {
				alert("����ѡ���û�!");
				return false;
			}
		}
	} else {
		alert("û�п�ɾ�����û�!");
		return false;
	}
}
//�����ļ���EXCEL
function ToExcel() {
	var page="../Resource/EGWUserInfoToExcel.jsp?title='������excel'&filename=filename";
	document.all("childFrm").src=page;
	//window.open(page);
}

//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
<FORM NAME="delFrom" METHOD="post" ACTION="../Resource/EGWUserInfoSave.jsp" onSubmit="return CheckFormDel()">
<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
	
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						�û���Դ
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						��ǰ�ͻ������û��б�				
					</td>
					<td align="right">
						
					</td>
				</tr>
			</table>
			</td>
		</tr>
	<TR>
		<TD bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH colspan="9">
						��ҵ�����û��б�
					</TH>
				</TR>
				<TR>
					<TD align="left" class="green_title2"><input type='checkbox' name='CheckAllToDel' onclick='checkedAll()'></TD>
					<TD class="green_title2">�û��ʻ�</TD>
					<TD class="green_title2">�󶨵绰</TD>
					<TD class="green_title2">�û�����</TD>
					<TD class="green_title2">�û���Դ</TD>
					<TD class="green_title2">�ͻ�����</TD>
					<TD class="green_title2">�Ƿ���豸</TD>
					<TD class="green_title2">����ʱ��</TD>
					<TD class="green_title2">����</TD>
				</TR>
				<%=strData%>
				<TR> 
                  <TD colspan="9" align="right" class=foot> 
                    <INPUT TYPE="button" value=" ���� " class=btn onclick="addUser()">
                    <INPUT TYPE="submit" value=" ����ɾ�� " class=btn>
                    <INPUT TYPE="hidden" name="action" value="deleteBatch">
                  </TD>
                </TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>

