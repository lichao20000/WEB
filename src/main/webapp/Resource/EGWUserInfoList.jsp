<%@ include file="../timelater.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=GBK"%>
<%--
	zhaixf(3412) 2008-04-22
	JSDX_ITMS-BUG-YHJ-20080421-001
--%>
<%--
	zhaixf(3412) 2008-05-12
	req:NJLC_HG-BUG-ZHOUMF-20080508-001
--%>
<jsp:useBean id="HGWUserInfoAct" scope="request" class="com.linkage.litms.resource.HGWUserInfoAct"/>
<jsp:useBean id="EGWUserInfoAct" scope="request" class="com.linkage.litms.resource.EGWUserInfoAct"/>
<%@ page import="java.util.ArrayList,com.linkage.litms.common.database.*,com.linkage.litms.*,com.linkage.litms.resource.DeviceAct" %> 
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<SCRIPT LANGUAGE="JavaScript">
<!--
var flag = true;
//-->
</SCRIPT>
<%
request.setCharacterEncoding("GBK");
		
ArrayList list = new ArrayList();
list.clear();
//获得所有的服务信息
//Cursor cursor_service = HGWUserInfoAct.getServiceInfo();
//Map fields_service = cursor_service.getNext();
//取得设备厂商
String strVendorList = HGWUserInfoAct.getVendorList(false,"","vendorName");
//取得设备类型
String strDevModelList = HGWUserInfoAct.getDevModelList(false,"","devModelName");
//取得设备版本
String strOsVersionList = HGWUserInfoAct.getOsVersionList(false,"","osVersionName");

//取得用户域
String strDomain = HGWUserInfoAct.getUserDomains();

//所有用户列表
list = EGWUserInfoAct.getEGWUsersCursor(request);


//flg
String flg = request.getParameter("flg");
//opt用于区分列表还是操作：‘edit’ or ‘view’
String opt = request.getParameter("opt");

String strData = "";
String rtnMsg = "";
String strBar = String.valueOf(list.get(0));
Cursor cursor = (Cursor)list.get(1);
if (list.size()==3 && list.get(2) != null) {
	rtnMsg = (String)list.get(2);
}

Map fields = cursor.getNext();
if(fields == null){
	strData = "<TR><TD class=column COLSPAN=8 HEIGHT=30>没有检索到企业网关用户!</TD></TR>";
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
		
		if( fields.get("device_id") == null || fields.get("device_id").equals(""))
			bindType = "否";
		else
			bindType = "是";

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
		
		String customer_name = (String)fields.get("customer_name");
		if(customer_name == null || "".equals(customer_name))
			customer_name = "";
		// 在从数据库中 统计 业务属地对应的数据
		DeviceAct devAct = new DeviceAct();
		// 属地MAP 当前用户看到本身及其下属地市
		HashMap cityMap = devAct.getCityMap_All();
		
		String cityId = (String)fields.get("city_id");
		String cityName = (String)cityMap.get(cityId);
		if(cityName == null || "".equals(cityName))
			cityName = "-";

		String phonenumber = (String)fields.get("phonenumber");
		if(phonenumber == null || "".equals(phonenumber))
			phonenumber = "-";
		
		if(fields.get("device_serialnumber") == null || fields.get("device_serialnumber").equals(""))
			bindType = "否";
		else
			bindType = "是";
				
	    strData += "<TR>";
		strData += "<TD class=column1 ><input type='checkbox' name='isCheckedToDel' value='"+(String)fields.get("user_id")+"|"+(String)fields.get("gather_id")+"'></TD>";
		strData += "<TD class=column1 align='left'>"+ username + "</TD>";
		strData += "<TD class=column2 align='left'>"+ customer_name + "</TD>";
		strData += "<TD class=column1 align='left'>"+ cityName + "</TD>";
		strData += "<TD class=column1 align='left'>"+ bindType + "</TD>";
		strData += "<TD class=column2 align='left'>"+ formatTime + "</TD>";
		
	  if("edit".equals(opt)){

		if(user_state.equals("1")||user_state.equals("2")){
		    
			strData += "<TD class=column1 align='right'><A HREF=AddEGWUserInfoForm.jsp?user_id="+ (String)fields.get("user_id") 
					+"&gather_id="+(String)fields.get("gather_id") 
					+"&gwOptType=22"
					+">编辑</A> | <A HREF=EGWUserInfoSave.jsp?gwOptType=21&action=delete&user_id="
					+(String)fields.get("user_id")+"&gather_id="+(String)fields.get("gather_id")
					+ " onclick='return delWarn();'>删除</A> | <A HREF=\"javascript:GoContent(\'"
					+(String)fields.get("user_id")+"\',\'"+(String)fields.get("gather_id")
					+"\');\" TITLE=查看用户"+(String)fields.get("realname")+"的相关信息>"
					+ "详细信息</A></TD>";
		}else if (user_state.equals("3")){
			strData += "<TD class=column1 align='right'>已销户 | "
					+"<A HREF=EGWUserInfoSave.jsp?gwOptType=21&action=delete&user_id="
					+(String)fields.get("user_id")+"&gather_id="+(String)fields.get("gather_id")
					+ " onclick='return delWarn();'>删除</A> | <A HREF=\"javascript:GoContent(\'"
					+(String)fields.get("user_id")+"\',\'"+(String)fields.get("gather_id")
					+"\');\" TITLE=查看用户"+(String)fields.get("realname")+"的相关信息>"
					+ "详细信息</A></TD>";
		}else if (flg!=null){
			strData += "<TD class=column1 align='right'> <A HREF=EGWUserInfoSave.jsp?gwOptType=21&action=delete&user_id="
					+(String)fields.get("user_id")+"&gather_id="+(String)fields.get("gather_id")
					+ " onclick='return delWarn();'>删除</A> | <A HREF=\"javascript:GoContent(\'"
					+(String)fields.get("user_id")+"\',\'"+(String)fields.get("gather_id")
					+"\');\" TITLE=查看用户"+(String)fields.get("realname")+"的相关信息>"
					+ "详细信息</A></TD>";
		}
	  }else{
			strData += "<TD class=column1 align='center'><A HREF=\"javascript:GoContent(\'"
				+(String)fields.get("user_id")+"\',\'"+(String)fields.get("gather_id")
				+"\');\" TITLE=查看用户"+(String)fields.get("realname")+"的相关信息>"
				+ "详细信息</A></TD>";
		}
		strData += "</TR>";
		fields = cursor.getNext();
	}
	strData += "<TR><TD class=column COLSPAN=8 align=right>" + strBar + "</TD></TR>";
}

fields = null;
list.clear();
list = null;
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var strVendorList = "<%= strVendorList%>";
var strDomain = "<%= strDomain%>";
var strDevModelList = "<%= strDevModelList%>";
var strOsVersionList = "<%= strOsVersionList%>";
var opt = "<%=opt%>";
//-->
</SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
<!--
function go(){
	v = document.all("txtpage").value;
	if(parseInt(v) && parseInt(v)>0){
		this.location = "EGWUserInfoList.jsp?offset="+ ((eval(v)-1)*15+1)
							+ "&opt="+<%=opt%>;
	}
}
//删除时警告
function delWarn(){
	if(confirm("确定要删除该用户和用户所对应的设备吗？\n本操作所删除的不能恢复！！！")){
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
		document.all("Button_1").value = "隐藏检索";
		document.all("searchLayer").style.display="";
	}else{
		show = 0;
		document.all("Button_1").value = "检索用户";
		document.all("searchLayer").style.display="none";
	}
}

//查看用户相关的信息
function GoContent(user_id, gather_id){
	var strpage="EGWUserRelatedInfo.jsp?user_id=" + user_id + "&gather_id=" + gather_id;
	window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}
//检查特定用户时的FORM
function CheckFormForm(){
	var adslflag,accountflag;
	if(KillSpace(document.frm.username.value).length != 0){
		accountflag = true;
	}
	if(KillSpace(document.frm.phonenumber.value).length != 0){
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
//检查统计时的FORM
function CheckStatForm() {
	if (document.all("some_service").value < 0) {
		alert("请先选择服务!");
		document.all("some_service").focus();
		return false;
	} else if (document.all("dev_id") != null && document.all("dev_id").value == "") {
		alert("请先输入设备ID!");
		document.all("dev_id").select();
		document.all("dev_id").focus();
		return false;
	} else if (document.all("vendorName") != null && document.all("vendorName").value < 0) {
		alert("请先选择用户厂商!");
		document.all("vendorName").focus();
		return false;
	} else if (document.all("devModelName") != null && document.all("devModelName").value < 0) {
		alert("请先选择用户类型!");
		document.all("devModelName").focus();
		return false;
	} else if (document.all("osVersionName") != null && document.all("osVersionName").value < 0) {
		alert("请先选择用户版本!");
		document.all("osVersionName").focus();
		return false;
	} else if (document.all("user_domain") != null && document.all("user_domain").value < 0) {
		alert("请先选择用户域!");
		document.all("user_domain").focus();
		return false;
	} else if (document.all("dev_ip") != null && document.all("dev_ip").value == "") {
		alert("请先输入设备IP!");
		document.all("dev_ip").select();
		document.all("dev_ip").focus();
		return false;
	} else {
//		statTable.style.display = "";
//		statTable.innerHTML = "正在载入数据......";
		return true;
	}
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
	var page="EGWUserInfoToExcel.jsp?title='导出到excel'&filename=filename";
	document.all("childFrm").src=page;
	//window.open(page);
}

function TypeChange_1() {
	var index=document.all.group_type_1.selectedIndex;
	switch(index) {
		//未选择的情况
		case 0: {
			document.all("filterCon_1").innerHTML = "";
			break;
		}
		//设备ID
		case 1: {
			document.all("filterCon_1").innerHTML = "<input type='text' name='dev_id' value=''>";
			break;
		}
		//设备厂商
		case 2: {
			document.all("filterCon_1").innerHTML = strVendorList;
			break;
		}
		//设备类型
		case 3: {
			document.all("filterCon_1").innerHTML = strDevModelList;
			break;
		}
		//设备版本
		case 4: {
			document.all("filterCon_1").innerHTML = strOsVersionList;
			break;
		}
	}
}

function TypeChange_2() {
	var index=document.all.group_type_2.selectedIndex;
	switch(index) {
		//未选择的情况
		case 0: {
			document.all("filterCon_2").innerHTML = "";
			break;
		}
		//上线状态
		case 1: {
			document.all("filterCon_2").innerHTML = "<input type='radio' name='CPE_CurrentStatus' value='1' checked>在线&nbsp;<input type='radio' name='CPE_CurrentStatus' value='0'>下线&nbsp;";
			break;
		}
		//管理域
		case 2: {
			document.all("filterCon_2").innerHTML = strDomain;
			break;
		}
		//设备IP
		case 3: {
			document.all("filterCon_2").innerHTML = "<input type='text' name='dev_ip' value=''>";
			break;
		}
	}
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
<FORM NAME="frm" METHOD="post" ACTION="EGWUserInfoList.jsp?flg='search'&opt=<%=opt%>">
<TABLE id="searchLayer" width="95%" border=0 cellspacing=0 cellpadding=0 align="center" style="display:none">
		<TR>
			<TD bgcolor=#999999>
			  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR> 
                  <TH colspan="4" align="center">查询用户信息</TH>
                </TR>
                <TR bgcolor="#FFFFFF"> 
                  <TD class=column>用户帐户</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="username" class=bk>
                  </TD>
                  <TD class=column width=180>联系电话</TD>
                  <TD>
                    <INPUT TYPE="text" NAME="phonenumber" class=bk>
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
                    <input type="hidden" name="searchForUsers" value="queryUsers">
                  </TD>
                </TR>
              </TABLE>
			</TD>
		</TR>
	</TABLE>
</FORM>

<FORM NAME="statForm" METHOD="post" ACTION="EGWUserInfoList.jsp?flg='sub'&opt=<%=opt%>" onSubmit="return CheckStatForm()">
<TABLE id="statLayer" width="95%" border=0 cellspacing=0 cellpadding=0 align="center" style="display:none">
		<TR>
			<TD bgcolor=#999999>
			  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR> 
                  <TH colspan="4" align="center">统计用户信息</TH>
                </TR>
                <TR bgcolor="#FFFFFF">
                  <TD class=column>业务</TD>
                  <TD> 
                  	<%	String servTypeNameList = HGWUserInfoAct.getServTypeNameList(5); %>
					<%=servTypeNameList%>
                  </TD>
                  <TD class=column>是否开通</TD>
                  <TD>
                    <input type="radio" value="1" name="radioBtn" checked>开通&nbsp;
				    <input type="radio" value="0" name="radioBtn">未开通&nbsp;&nbsp;
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF"> 
                  <TD class=column>过滤条件:
                  <SELECT NAME="group_type_1" class=bk onchange="javascript:TypeChange_1();">
			     	 <option value="0">==请选择==</option>
                     <option value="1">设备ID</option>
                     <option value="2">设备厂商</option>
                     <option value="3">设备类型</option>
                     <option value="4">软件版本</option>
                  </SELECT>&nbsp;&nbsp;
                  </TD>
                  <TD>
                  <DIV id="filterCon_1"></DIV>
                  </TD>
                  <TD class=column>过滤条件:
                  <SELECT NAME="group_type_2" class=bk onchange="javascript:TypeChange_2();">
			     	 <option value="0">==请选择==</option>
                     <option value="1">上线状态</option>
                     <option value="2">管理域</option>
                     <option value="3">设备IP</option>
                  </SELECT>&nbsp;&nbsp;
                  </TD>
                  <TD>
                     <DIV id="filterCon_2"></DIV>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF">
                  <TD class=foot align=right colspan=8> 
                    <input type="submit" name="submit" value=" 统 计 " class=btn>
                    <input type="hidden" name="searchForUsers" value="statUsers">
                  </TD>
                </TR>
              </TABLE>
			</TD>
		</TR>
	</TABLE>
</FORM>

<FORM NAME="delFrom" METHOD="post" ACTION="EGWUserInfoSave.jsp" onSubmit="return CheckFormDel()">
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
						企业网关用户资源列表,可对用户资源进行检索和统计						
					</td>
					<td align="right">
						
					</td>
				</tr>
			</table>
			</td>
		</tr>
				
		<%
		if(rtnMsg.length() > 0) {
		%>
			<TR>
			<TD height="15">
			<font color=red>统计用户条件：<%= rtnMsg%></font>
			</TD>
			</TR>
		<%
		}
		%>
	<TR>
		<TD bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH colspan="8">
						企业网关用户列表
					</TH>
				</TR>
				<TR>
					<TD align="left" class="green_title2"><input type='checkbox' name='CheckAllToDel' onclick='checkedAll()'></TD>
					<TD class="green_title2">用户帐户</TD>
					<TD class="green_title2">客户名称</TD>
					<TD class="green_title2">属地</TD>
					<TD class="green_title2">是否绑定设备</TD>
					<TD class="green_title2">受理时间</TD>
					<TD class="green_title2">操作</TD>
				</TR>
				<%=strData%>
				<TR> 
                  <TD colspan="8" align="right" class=foot> 
					<input type="button" name="Button_1" value="检索用户" class="btn" onclick="showSearch()">
					&nbsp;&nbsp;
					<!-- 
					<input type="button" name="Button_2" value="统计用户" class="btn" onclick="showStatSearch()">
					&nbsp;&nbsp;
					 -->
					 <s:if test='#session.isReport=="1"'>
					<INPUT TYPE="button" value=" 导出文件" class=btn onclick="ToExcel()">
					</s:if>
					&nbsp;&nbsp;
					<% if ("edit".equals(opt)){%>
					<!-- 屏蔽批量删除 -->
                    <!--  <INPUT TYPE="submit" value=" 批量删除" class=btn>
                    <INPUT TYPE="hidden" name="action" value="deleteBatch">-->
                    <%} %>
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

<SCRIPT LANGUAGE="JavaScript">
<!--
var statShow = 0;
function showStatSearch(){
	if(statShow == 0){
		statShow = 1;
		document.all("Button_2").value = "隐藏统计";
		document.all("statLayer").style.display="";
	}else{
		statShow = 0;
		document.all("Button_2").value = "统计用户";
		document.all("statLayer").style.display="none";
	}
}
//	document.onload = showStatSearch();
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>

