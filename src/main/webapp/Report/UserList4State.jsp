<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="HGWUserInfoAct" scope="request" class="com.linkage.litms.resource.HGWUserInfoAct"/>
<jsp:useBean id="EGWUserInfoAct" scope="request" class="com.linkage.litms.resource.EGWUserInfoAct"/>
<%@ page import="com.linkage.litms.Global,java.util.*" %> 
<%@ page import="java.text.SimpleDateFormat" %>

<%
request.setCharacterEncoding("GBK");
ArrayList list = new ArrayList();
list.clear();

//ȡ���豸����
String strVendorList = HGWUserInfoAct.getVendorList(false,"","vendorName");
//ȡ���豸����
String strDevModelList = HGWUserInfoAct.getDevModelList(false,"","devModelName");
//ȡ���豸�汾
String strOsVersionList = HGWUserInfoAct.getOsVersionList(false,"","osVersionName");

String gw_type = request.getParameter("gw_type");
String startTime = request.getParameter("startTime");
String endTime = request.getParameter("endTime");
String accessType = request.getParameter("accessType");
String stroffset = request.getParameter("offset");
String tempCityId = request.getParameter("city_id");
if(null!=gw_type && "2".equals(gw_type)){
	//����HGW�û��б�
	list = EGWUserInfoAct.getStateUsers(request);
}else{
	//����HGW�û��б�
	list = HGWUserInfoAct.getStateUsers(request);
}



String strData = "";

String strBar = String.valueOf(list.get(0));
Cursor cursor = (Cursor)list.get(1);

Map fields = cursor.getNext();
if(fields == null){
	strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>û���û�!</TD></TR>";
}
else{

	String cust_type;
	String bindType = "";
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
		/**
		if(fields.get("device_serialnumber") == null || fields.get("device_serialnumber").equals(""))
			bindType = "��";
		else
			bindType = "��";
		**/
		//  �½����� XJDX-REQ-20120409-HUJG3-001
		if(null == fields.get("device_id")||"".equals(fields.get("device_id")))
			bindType = "��";
		else
			bindType = "��";
		
		if( fields.get("dealdate") != null && !fields.get("dealdate").equals("")
				&& !fields.get("dealdate").equals("0")){
			time.setTimeInMillis((Long.parseLong((String)fields.get("dealdate")))*1000);
			formatTime = df.format(time.getTime());
		}else{
			formatTime = "-";
		}
		
		String serv_type = (String)fields.get("serv_type_id");
		serv_type = (Global.Serv_type_Map.get(serv_type) == null)? "" : (String)Global.Serv_type_Map.get(serv_type);
		
		String user_type_id = (String)fields.get("user_type_id");
		String tmp = "BSS�û�";
		if(user_type_id != null && !user_type_id.equals("")){
			if(user_type_id.equals("1")){
				tmp = "�ֳ���װ";
			} else if(user_type_id.equals("2")){
				tmp = "BSS�û�";
			} else if(user_type_id.equals("4")){
				tmp = "BSSͬ��";
			}
		}
		
		String updateTime = "";
		if(fields.get("updatetime") != null && !(fields.get("updatetime").equals(""))
				&& !(fields.get("updatetime").equals("0"))){
			time.setTimeInMillis((Long.parseLong((String)fields.get("updatetime")))*1000);
			updateTime = df.format(time.getTime());
		}else{
			updateTime = "-";
		}
		
		String username = (String)fields.get("username");
		boolean  tempStr = false;
		String isFTTP = "��";
		if(username == null || "".equals(username)){
			username = "-";
		}else {
			tempStr = username.toUpperCase().endsWith("C");
			if(tempStr)
			 	isFTTP = "��";
		}
		
		String phonenumber = (String)fields.get("phonenumber");
		if(phonenumber == null || "".equals(phonenumber))
			phonenumber = "-";

	    strData += "<TR>";
		strData += "<TD class=column1 align='left'>"+ username + "</TD>";
		strData += "<TD class=column2 align='left'>"+ phonenumber + "</TD>";
		strData += "<TD class=column2 align='left'>"+ tmp + "</TD>";		
		strData += "<TD class=column1 align='left'>"+ bindType + "</TD>";
		strData += "<TD class=column1 align='left'>"+ isFTTP + "</TD>";  // <!-- �½����� XJDX-REQ-20120409-HUJG3-001-->
		strData += "<TD class=column2 align='left'>"+ formatTime + "</TD>";
		strData += "<TD class=column2 align='left'>"+ updateTime + "</TD>";
		
		strData += "<TD class=column1 align='right'><A HREF=\"javascript:GoContent(\'"
				+(String)fields.get("user_id")+"\',\'"+(String)fields.get("gather_id")
				+"\',\'"+gw_type
				+"\');\" TITLE=�鿴�û�"+(String)fields.get("realname")+"�������Ϣ>"
				+ "��ϸ��Ϣ</A></TD>";
	    strData += "</TR>";
	    fields = cursor.getNext();
	}
	strData += "<TR><TD class=column COLSPAN=10 align=right>" + strBar + "</TD></TR>";
}

fields = null;
list.clear();
list = null;
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var strVendorList = "<%= strVendorList%>";
var strDevModelList = "<%= strDevModelList%>";
var strOsVersionList = "<%= strOsVersionList%>";
//-->
</SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
<!--

//�鿴�û���ص���Ϣ
function GoContent(user_id, gather_id,gw_type){
	if("2"==gw_type){
		var strpage="../Resource/EGWUserRelatedInfo.jsp?user_id=" + user_id + "&gather_id=" + gather_id;
		window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
	}else{
		var strpage="../Resource/HGWUserRelatedInfo.jsp?user_id=" + user_id + "&gather_id=" + gather_id;
		window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
	}
}

//�����ļ���EXCEL
function ToExcel() {
	var page = "stateDevUser!toUserExcel.action?gw_type="+<%=gw_type%>;
	page = page+"&startTime="+<%=startTime%>;
	page = page+"&endTime="+<%=endTime%>;
	page = page+"&accessType="+'<%=accessType%>';
	page = page+"&offset="+<%=stroffset%>;
	page = page+"&city_id="+'<%=tempCityId%>';
	document.all("childFrm").src=page;
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>

<FORM NAME="delFrom" METHOD="post" ACTION="HGWUserInfoSave.jsp" onSubmit="return CheckFormDel()">
<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
	<TR>
		<TD bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH colspan="10">
						�û��б�
					</TH>
				</TR>
				<TR>
					<TD class="green_title2">�û��ʺ�</TD>
					<TD class="green_title2">�󶨵绰</TD>
					<TD class="green_title2">�û���Դ</TD>
				<!--<TD class="green_title2">�û�����</TD> -->
					<TD class="green_title2">�Ƿ���豸</TD>
					<TD class="green_title2">�Ƿ�FTTH�û�</TD>    // <!-- �½����� XJDX-REQ-20120409-HUJG3-001-->
					<TD class="green_title2">����ʱ��</TD>
					<TD class="green_title2">����ʱ��</TD>
					<TD width=100 class="green_title2">����</TD>
				</TR>
				<%=strData%>

				<TR>
                  <TD colspan="10" align="left" class=green_foot>
					&nbsp;&nbsp;
					<a href="javascript:ToExcel()">
						<img src="../images/excel.gif" border="0" width="16" height="16"></img>
					</a>
					<iframe id="childFrm" style="display:none"></iframe>
                 </TD>
                </TR>

			</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>
</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
