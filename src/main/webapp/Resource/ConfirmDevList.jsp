<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<jsp:useBean id="HGWUserInfoAct" scope="request" class="com.linkage.litms.resource.HGWUserInfoAct" />
<SCRIPT LANGUAGE="JavaScript">
<!--
var flag = true;
//-->
</SCRIPT>
<%
request.setCharacterEncoding("GBK");

ArrayList list = new ArrayList();
list.clear();
//所有确认设备列表
list = HGWUserInfoAct.getConfirmDevCursor(request);
//取得所有OUI和厂商名对应的MAP
Map ouiMap = HGWUserInfoAct.getOUIDevMap();
//取得所有当前用户下属地对应的MAP
Map cityMap = HGWUserInfoAct.getCityMap(request);
String strData = "";
String rtnMsg = "";
String strBar = String.valueOf(list.get(0));
Cursor cursor = (Cursor)list.get(1);
StringBuffer confirmDevLine = new StringBuffer();
StringBuffer allCDLines = new StringBuffer();

Map fields = cursor.getNext();
if(fields == null){
	strData = "<TR><TD class=column COLSPAN=8 HEIGHT=30>没有检索到确认设备!</TD></TR>";
}
else{
	while(fields != null){
		strData += "<TR>";
		strData += "<TD class=column1>"+ ouiMap.get((String)fields.get("oui")) + "</TD>";
		confirmDevLine.append((String)fields.get("oui")+"^");
		strData += "<TD class=column2>"+ (String)fields.get("device_serialnumber") + "</TD>";
		confirmDevLine.append((String)fields.get("device_serialnumber")+"^");
		strData += "<TD class=column1>"+ cityMap.get((String)fields.get("city_id")) + "</TD>";
		confirmDevLine.append((String)fields.get("city_id"));
		String urlEdit = "AddConfirmDev.jsp?action=update&oui="+ (String)fields.get("oui") 
		+"&device_serialnumber="+URLEncoder.encode((String)fields.get("device_serialnumber"))
		+"&city_id="+(String)fields.get("city_id");
		
		String urlDel = "ConfirmDevSave.jsp?action=delete&oui="
			+(String)fields.get("oui")+"&device_serialnumber="+URLEncoder.encode((String)fields.get("device_serialnumber"));
//		String urlEditEncode = response.encodeURL(URLEncoder.encode(urlEdit,"GBK"));
//		String urlDelEncode = response.encodeURL(URLEncoder.encode(urlDel,"GBK"));
		strData += "<TD class=column1><A HREF="+urlEdit+">编辑</A> | <A HREF="+urlDel+ " onclick='return delWarn();'>删除</A></TD>";
		strData += "</TR>";
		allCDLines.append(confirmDevLine+",");
		
		fields = cursor.getNext();
	}
	strData += "<TR><TD class=column COLSPAN=8 align=right>" + strBar + "</TD></TR>";
}

fields = null;
list.clear();
list = null;
ouiMap.clear();
ouiMap = null;
cityMap.clear();
cityMap = null;
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
var allCDLines = "<%= allCDLines.toString()%>"
function go(){
	v = document.all("txtpage").value;
	if(parseInt(v) && parseInt(v)>0){
		this.location = "HGWUserInfoList.jsp?offset="+ ((eval(v)-1)*15+1);
	}
}
//删除时警告
function delWarn(){
	if(confirm("真的要删除该确认设备吗？\n本操作所删除的不能恢复！！！")){
		return true;
	}
	else{
		return false;
	}
}

var show = 0;
//根据特定条件搜索
function showSearch(){
	if(show == 0){
		show = 1;
		document.all("Button_1").value = "隐藏确认设备";
		document.all("searchLayer").style.display="";
	}else{
		show = 0;
		document.all("Button_1").value = "检索确认设备";
		document.all("searchLayer").style.display="none";
	}
}

//查看用户相关的信息
function GoContent(user_id, gather_id){
	var strpage="HGWUserRelatedInfo.jsp?user_id=" + user_id + "&gather_id=" + gather_id;
	window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}
//检查特定用户时的FORM
function CheckFormForm(){
	var adslflag,accountflag;
	if(KillSpace(document.frm.username.value).length != 0){
		accountflag = true;
	}
	if(KillSpace(document.frm.realname.value).length != 0){
		adslflag = true;
	}
	if(accountflag || adslflag){
		return true;
	}else{
		alert("请输入查询条件");
		document.frm.username.focus();
		document.frm.username.select();
		return false;		
	}
}

//导出文件成EXCEL
function ToExcel() {
	var page="HGWUserInfoToExcel.jsp?title='导出到excel'&filename=filename";
	document.all("childFrm").src=page;
	//window.open(page);
}

//导入csv文件
function openUploadWin() {
//	var fvalue = document.all.file1.value;
//	fvalue = fvalue.replace(/\\/g,'/');

//	var page = "ConfirmDevList.jsp?fvalue="+fvalue;
	var page = "ConfirmDevUploadForm.jsp";
	window.open(page);
	//document.all("childFrm").src=page;
}

//导出CSV文件
function exportCSV() {
}
//-->
</SCRIPT>
<link rel="stylesheet" href="../css/listview.css" type="text/css">
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<tr>
	<td>
	<table width="95%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd" align="center">
		<tr>
			<td width="162" align="center" class="title_bigwhite">
				确认设备资源
			</td>
		</tr>
	</table>
	</td>
</tr>
<TR><TD>
<FORM NAME="frm" METHOD="post" ACTION="ConfirmDevList.jsp">
<TABLE id="searchLayer" width="95%" border=0 cellspacing=0 cellpadding=0 align="center" style="display:none">
		<TR>
			<TD bgcolor=#000000>
			  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR> 
                  <TH colspan="4" align="center">查询用户信息</TH>
                </TR>
                <TR bgcolor="#FFFFFF"> 
                  <TD class=column>厂商</TD>
                  <TD>
                    <INPUT TYPE="text" NAME="v_name" class=bk>
                  </TD>
                  <TD class=column width=180>设备序列号</TD>
                  <TD>
                    <INPUT TYPE="text" NAME="device_serialnumber" class=bk>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF">
                  <TD align=left colspan=8> 
                    <font color="red">注:如都为空,则检索全部!</font>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF">
                  <TD class=foot align=right colspan=8> 
                    <input type="submit" name="submit" value=" 查 询 " class=btn>
                  </TD>
                </TR>
              </TABLE>
			</TD>
		</TR>
	</TABLE>
</FORM>
<FORM>
<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
	<TR>
		<TD bgcolor=#000000>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">
				<TR>
					<TD bgcolor="#ffffff" colspan="9" >
					该系统现有以下确认设备&nbsp;<a href="AddConfirmDev.jsp">增加确认设备</a>
					&nbsp;&nbsp;<input type="button" name="Button_1" value="检索确认设备" class="btn" onclick="showSearch()">
					</TD>
				</TR>
				<TR>
					<TH>厂商</TH>
					<TH>设备序列号</TH>
					<TH>属地</TH>
					<TH width=150>操作</TH>
				</TR>
				<%=strData%>
				<TR>
                  <TD colspan="8" align="right" class=foot> 
					<INPUT TYPE="button" value=" 导入文件 " class=btn onclick="openUploadWin()">
					&nbsp;&nbsp;
					<INPUT TYPE="button" value=" 导出Excel " class=btn onclick="initialize('outTable',1,0)">
					&nbsp;&nbsp;
					<!-- <INPUT TYPE="button" value=" 导出CSV " class=btn onclick="exportCSV()">
					&nbsp;&nbsp; -->
                  </TD>
                </TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm name=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>

