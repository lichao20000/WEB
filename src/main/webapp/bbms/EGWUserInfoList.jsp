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

//所有用户列表
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
	strData = "<TR><TD class=column COLSPAN=9 HEIGHT=30>没有检索到家庭网关用户!</TD></TR>";
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
			cust_type = "公司客户";
		} else if ("1".equals((String)fields.get("cust_type_id"))) {
			cust_type = "网吧客户";
		} else if ("2".equals((String)fields.get("cust_type_id"))) {
			cust_type = "个人客户";
		} else{
			cust_type = "-";
		}
		
		if( fields.get("device_serialnumber") == null || fields.get("device_serialnumber").equals(""))
			bindType = "否";
		else
			bindType = "是";

		String user_type_id = (String)fields.get("user_type_id");
		String tmp = "";
		if(user_type_id != null && !user_type_id.equals("")){
			if(user_type_id.equals("1")){
				tmp = "现场安装";
			} else if(user_type_id.equals("2")){
				tmp = "BSS用户";
			} else {
				tmp = "手工添加";
			}
		}else{
			tmp = "手工添加";
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
					+">编辑</A> | <A HREF='#' onclick=\"delUser('"
					+(String)fields.get("user_id")+"','"+(String)fields.get("gather_id")
					+"')\">删除</A> | <A HREF=\"javascript:GoContent(\'"
					+(String)fields.get("user_id")+"\',\'"+(String)fields.get("gather_id")
					+"\');\" TITLE=查看用户"+(String)fields.get("realname")+"的相关信息>"
					+ "详细信息</A></TD>";
		}else if (user_state.equals("3")){
			strData += "<TD class=column1 align='right'>已销户 | "
					+"<A HREF='#' onclick=\"delUser('"
					+(String)fields.get("user_id")+"','"+(String)fields.get("gather_id")
					+"')\">删除</A> | <A HREF=\"javascript:GoContent(\'"
					+(String)fields.get("user_id")+"\',\'"+(String)fields.get("gather_id")
					+"\');\" TITLE=查看用户"+(String)fields.get("realname")+"的相关信息>"
					+ "详细信息</A></TD>";
		}else if (flg!=null){
			strData += "<TD class=column1 align='right'> <A HREF='#' onclick=\"delUser('"
					+(String)fields.get("user_id")+"','"+(String)fields.get("gather_id")
					+"')\">删除</A> | <A HREF=\"javascript:GoContent(\'"
					+(String)fields.get("user_id")+"\',\'"+(String)fields.get("gather_id")
					+"\');\" TITLE=查看用户"+(String)fields.get("realname")+"的相关信息>"
					+ "详细信息</A></TD>";
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
//新增
function addUser(){
	var strpage="../Resource/AddEGWUserInfoForm.jsp?gwOptType=21&showtype=1&oui=<%=oui%>&vender=<%=device_serialnumber%>&customer_id=<%=customer_id%>";
	window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}

//编辑
function editUser(user_id, gather_id){
	var strpage="../Resource/AddEGWUserInfoForm.jsp?gwOptType=22&showtype=1&oui=<%=oui%>&vender=<%=device_serialnumber%>&customer_id=<%=customer_id%>&user_id=" + user_id + "&gather_id=" + gather_id;
	window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}
//删除
function delUser(user_id, gather_id){
	if(confirm("确定要删除该用户和用户所对应的设备吗？\n本操作所删除的不能恢复！！！")){
		var strpage="../Resource/EGWUserInfoSave.jsp?gwOptType=21&showtype=1&action=delete&user_id=" + user_id + "&gather_id=" + gather_id;
		window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
	}
	else{
		return false;
	}
}

//查看用户相关的信息
function GoContent(user_id, gather_id){
	var strpage="../Resource/EGWUserRelatedInfo.jsp?user_id=" + user_id + "&gather_id=" + gather_id;
	window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}

//选择全部
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
			//alert("没有可选择的用户!");
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
			//alert("没有可选择的用户!");
		}
		flag = true;
	}
	
}
//检查删除时的FORM
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
			alert("请先选定用户!");
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
				alert("请先选定用户!");
				return false;
			}
		}
	} else {
		alert("没有可删除的用户!");
		return false;
	}
}
//导出文件成EXCEL
function ToExcel() {
	var page="../Resource/EGWUserInfoToExcel.jsp?title='导出到excel'&filename=filename";
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
						用户资源
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						当前客户关联用户列表				
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
						企业网关用户列表
					</TH>
				</TR>
				<TR>
					<TD align="left" class="green_title2"><input type='checkbox' name='CheckAllToDel' onclick='checkedAll()'></TD>
					<TD class="green_title2">用户帐户</TD>
					<TD class="green_title2">绑定电话</TD>
					<TD class="green_title2">用户类型</TD>
					<TD class="green_title2">用户来源</TD>
					<TD class="green_title2">客户类型</TD>
					<TD class="green_title2">是否绑定设备</TD>
					<TD class="green_title2">受理时间</TD>
					<TD class="green_title2">操作</TD>
				</TR>
				<%=strData%>
				<TR> 
                  <TD colspan="9" align="right" class=foot> 
                    <INPUT TYPE="button" value=" 新增 " class=btn onclick="addUser()">
                    <INPUT TYPE="submit" value=" 批量删除 " class=btn>
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

