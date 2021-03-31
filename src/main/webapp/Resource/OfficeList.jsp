<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>

<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<jsp:useBean id="office" scope="request" class="com.linkage.litms.resource.OfficeAct"/>
<%
request.setCharacterEncoding("GBK");
//获取属地信息
String city_id = user.getCityId();

List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
String m_CityIdQuery = StringUtils.weave(cityIdList);
/**
String CitySQL = "select * from tab_city where city_id in( " + m_CityIdQuery + ")";

Cursor cityCursor = DataSetBean.getCursor(CitySQL);
String cityStr = FormUtil.createListBox(cityCursor, "city_id",
		"city_name", true, "", "");
*/
String strClr = "";
String strData = "";
int offset;
ArrayList list = new ArrayList();
list.clear();
list = office.getOfficeList(request);

String stroffset = request.getParameter("offset");
int pagelen = 15;
if(stroffset == null) offset = 1;
else offset = Integer.parseInt(stroffset);

String strBar = String.valueOf(list.get(0));
Cursor cursor = (Cursor)list.get(1);
Map fields = cursor.getNext();

if(fields == null){
	strData = "<TR><TD CLASS=column COLSPAN=5 HEIGHT=30>系统没有局向资源</TD></TR>";
}
else{
	int i=1;
	while(fields != null){
		if((i%2)==0) strClr="#e7e7e7";
		else strClr = "#FFFFFF";

		strData += "<TR>";
		strData += "<TD CLASS=column1>"+ (String)fields.get("office_id") + "</TD>";
		strData += "<TD CLASS=column2>"+ (String)fields.get("office_name") + "</TD>";
		strData += "<TD CLASS=column1  align='right'>"+ (String)fields.get("staff_id") + "</TD>";
		//strData += "<TD>"+ (String)fields.get("remark") + "</TD>";
		strData += "<TD CLASS=column2  align='center'> <A HREF=UpdateOfficeForm.jsp?office_id="+ (String)fields.get("office_id") +" onclick='return Edit(this.href);'>编辑</A> | <A HREF=OfficeSave.jsp?action=delete&office_id="+ (String)fields.get("office_id") +" onclick='return delWarn();'>删除</A></TD>";
		strData += "</TR>";
		i++;
		fields = cursor.getNext();
	}
	strData += "<TR BGCOLOR=#FFFFFF><TD COLSPAN=4 align=right class=green_foot>" + strBar + "</TD></TR>";
}

//clear
fields = null;
cursor = null;
list.clear();
strBar = null;

%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" src="../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
function Add(){
	document.frm.reset();
	document.frm.action.value="add";
	actLabel.innerHTML = '添加';
	document.all("officeLabel").innerHTML = "";
}

function Edit(page){
	document.all("childFrm").src = page;
	return false;
}

function delWarn(){
	if(confirm("真的要删除该局向吗？\n本操作所删除的不能恢复！！！")){
		return true;
	}
	else{
		return false;
	}
}

function CheckForm(){
	var obj = document.frm;
	if(!IsNull(obj.office_name.value,'局向名称')){
		obj.office_name.focus();
		obj.office_name.select();
		return false;
	}
//	if(obj.city_id.value == "-1")
//	{
//		alert("用户属地不能为空！");
//		obj.city_id.focus();
//		return false;
//	}
//	else{
//		return true;
//	}	
}

//-->
</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<tr>
		<td>
		<table width="98%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd" align="center">
			<tr>
				<td width="162" align="center" class="title_bigwhite">
					基础资源
				</td>
				<td>
					<img src="../images/attention_2.gif" width="15" height="12">
					带'<font color="#FF0000">*</font>'的表单必须填写或选择
				</td>
				<td align="right">
					<A HREF='javascript:Add();'>增加&nbsp;&nbsp;</A>
				</td>
			</tr>
		</table>
		</td>
	</tr>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="OfficeSave.jsp" onsubmit="return CheckForm()">
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" >局向资源</TH>
					</TR>
					<TR>
						<!-- <TD>设备编号</TD> -->
						<TD class=green_title2>局向标识</TD>
						<TD class=green_title2>局向名称</TD>
						<TD class=green_title2>操作员ID</TD>
						<TD class=green_title2>操作</TD>
					</TR>
					<%=strData%>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<BR>
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center"><SPAN id="actLabel">添加</SPAN><SPAN id="officeLabel"></SPAN>局向资源</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">局向标识</TD>
						<input type="hidden" name="office_id_old" value="">
						<TD colspan=3><INPUT TYPE="text" NAME="office_id" maxlength=20 class=bk size=20 >&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >局向名称</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="office_name" maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<!-- 
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >属地
							<input type="hidden" name="citylist" value=""></TD>
							<TD colspan=3></TD>
						</TR>
					-->
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >操作员ID</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="staff_id" maxlength=30 class=bk size=20 value="<%=user.getAccount()%>"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >备注</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="remark" maxlength=50 class=bk size=50></TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class=green_foot>
							<INPUT TYPE="submit" value=" 保 存 " class=btn>&nbsp;&nbsp;
							<INPUT TYPE="hidden" name="action" value="add">
							<INPUT TYPE="reset" value=" 重 写 " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>