<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ page import="com.linkage.commons.db.PrepareSQL" %>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%
	request.setCharacterEncoding("GBK");
String str_vpn_id = request.getParameter("vpn_id");
String vpn_auto_id = request.getParameter("vpn_auto_id");

String strSQL = "";
String Str_cust_level = "";
String longtime_date = "";
String vpn_name = "";
String ext_vpn_id = "";
String username = "";
String vpn_type_id = "0";
String topo_type = "0";
String linkman = "";
String address = "";
String user_state = "";
String linkphone = "";
String mobile = "";
String email = "";
String remark1 = "";
String cred_type_id = "";
String cardno = "";
if(str_vpn_id==null){
	strSQL = "select * from vpn_customer where vpn_id=(select vpn_id from vpn_auto_customer where vpn_auto_id="+ vpn_auto_id+")";
	// teledb
	if (DBUtil.GetDB() == 3) {
		strSQL = "select cust_level_id, complete_time, vpn_name, ext_vpn_id, username, vpn_type_id, topo_type, " +
				"linkman, address, user_state, linkphone, mobile, email, remark1, cred_type_id, cardno" +
				" from vpn_customer where vpn_id=(select vpn_id from vpn_auto_customer where vpn_auto_id="+ vpn_auto_id+")";
	}
}else{
	strSQL = "select * from vpn_customer where vpn_id="+ str_vpn_id;
	// teledb
	if (DBUtil.GetDB() == 3) {
		strSQL = "select cust_level_id, complete_time, vpn_name, ext_vpn_id, username, vpn_type_id, topo_type," +
				" linkman, address, user_state, linkphone, mobile, email, remark1, cred_type_id, cardno" +
				" from vpn_customer where vpn_id="+ str_vpn_id;
	}
}
PrepareSQL psql = new PrepareSQL(strSQL);
Map fields = DataSetBean.getRecord(psql.getSQL());
if (fields != null) {
	Str_cust_level = (String)fields.get("cust_level_id");
	if ((String)fields.get("complete_time") != null && !"".equals((String)fields.get("complete_time"))) {
		longtime_date= new DateTimeUtils(Long.parseLong((String)fields.get("complete_time")) * 1000).getDate();
	} else {
		longtime_date = "";
	}
	vpn_name = (String)fields.get("vpn_name".toLowerCase());
	ext_vpn_id = (String)fields.get("ext_vpn_id".toLowerCase());
	username = (String)fields.get("username".toLowerCase());
	vpn_type_id = (String)fields.get("vpn_type_id".toLowerCase());
	topo_type = (String)fields.get("topo_type".toLowerCase());
	linkman = (String)fields.get("linkman".toLowerCase());
	address = (String)fields.get("address".toLowerCase());
	user_state = (String)fields.get("user_state".toLowerCase());
	linkphone = (String)fields.get("linkphone".toLowerCase());
	mobile = (String)fields.get("mobile".toLowerCase());
	email = (String)fields.get("email".toLowerCase());
	remark1 = (String)fields.get("remark1".toLowerCase());
	cred_type_id = (String)fields.get("cred_type_id".toLowerCase());
	cardno = (String)fields.get("cardno".toLowerCase());
}
%>
<SCRIPT LANGUAGE="JavaScript" src="../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" src="Js/calendar.js"></SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
   <TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" id='myTable'>
		<TR>
			<TD bgcolor=#000000>
				
              <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR> 
                  <TD bgcolor="#ffffff" colspan="4">带'<font color="#FF0000">*</font>'的表单必须填写或选择</TD>
                </TR>
                <TR>
                  <TH colspan="4" align="center">查看/修改〖<%=vpn_name%>〗VPN用户</TH>
                </TR>
                <TR bgcolor="#FFFFFF"> 
				
                  <TD class=column align="right">客户编码</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="ext_vpn_id" maxlength=100 class=bk value="<%=ext_vpn_id%>">
                    <font color="#FF0000">*</font></TD>
                  <TD class=column align="right">客户名称</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="username" maxlength=100 class=bk value="<%=username%>">&nbsp;<font color="#FF0000">*</font>
                    </TD>
                </TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">VPN名称</TD>
					<TD><INPUT TYPE="text" NAME="vpn_name" maxlength=20 class=bk value="<%=vpn_name%>">&nbsp;<font color="#FF0000">*</font></TD>
					<TD class=column align="right">客户等级</TD>
					<TD>
					<SELECT name="cust_level_id" class=bk>
					<option value="1" <% if (Str_cust_level.equals("1")) {%>selected<%}%>>钻石</option>
					<option value="2" <% if (Str_cust_level.equals("2")) {%>selected<%}%>>白金</option>
					<option value="3" <% if (Str_cust_level.equals("3")) {%>selected<%}%>>金</option>
					<option value="4" <% if (Str_cust_level.equals("4")) {%>selected<%}%>>银</option>
					<option value="5" <% if (Str_cust_level.equals("5")) {%>selected<%}%>>铜</option>
					</SELECT>&nbsp;<font color="#FF0000">*</font>
					</TD>
				</TR>

				<TR bgcolor="#FFFFFF"> 
				
				  <TD class=column align="right">客户类型</TD>
				  <TD>
				 <%
				 
				 String strsql = "select cust_type_id,cust_type_name from vpn_customer_type";
					Cursor cursor1 = DataSetBean.getCursor(strsql); 
					Map fields1 = cursor1.getNext();
					String old = (String)fields1.get("cust_type_name".toLowerCase());
					String selected = "";
					if(fields1 == null){
						out.println("<SELECT name=cust_type_name class=bk><option value='0'>没有客户类型</option></SELECT>");
					}
					else{
						out.println("<SELECT name=cust_type_id class=bk>");
						//out.println("<OPTION VALUE=0>====请选择====</OPTION>");
						while(fields1 != null){
							if(old.equals((String)fields1.get("cust_type_name".toLowerCase()))) {
								selected = "selected";
							}
							else{
								selected = "";
							}
							out.println("<option value='"+fields1.get("cust_type_id".toLowerCase())+"' "+ selected +">"+fields1.get("cust_type_name".toLowerCase())+"</option>");
							fields1 = cursor1.getNext();
						}
						out.println("</select>");
					}
				 %>
				  <font color="#FF0000">*</font>
				  </TD>
					<TD class=column align="right">用户类型</TD>
					<TD>
					<SELECT name="vpn_type_id" class=bk>
					<option value="1" <%if (Integer.parseInt(vpn_type_id) == 1){%> selected<%}%>>三层MPLS VPN用户</option>
					<option value="2" <%if (Integer.parseInt(vpn_type_id) == 2){%> selected<%}%>>二层MPLS VPN用户</option>
					</SELECT>&nbsp;<font color="#FF0000">*</font>&nbsp;
				  </TD>
                </TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">拓扑类型</TD>
					<TD>
					<SELECT name="topo_type" class=bk>
					<option value="1" <% if(Integer.parseInt(topo_type) == 1){%> selected<%}%>>网状</option>
					<option value="2" <% if(Integer.parseInt(topo_type) == 2){%> selected<%}%>>星形</option>
					<option value="3" <% if(Integer.parseInt(topo_type) == 3){%> selected<%}%>>混合</option>
					</SELECT>&nbsp;<font color="#FF0000">*</font>&nbsp;
					</TD>
					<TD class=column align="right">联系人(客户经理)</TD>
					<TD><INPUT TYPE="text" NAME="linkman" maxlength=100 class=bk value="<%=linkman%>">&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
				
					<TD class=column align="right">证件类型</TD>
					<TD>
					<SELECT name="cred_type_id" class=bk>
					<option value="0" <% if(cred_type_id.equals("0")){%> selected<%}%>>其他</option>
					<option value="1" <% if(cred_type_id.equals("1")){%> selected<%}%>>身份证</option>
					<option value="2" <% if(cred_type_id.equals("2")){%> selected<%}%>>军官证</option>
					<option value="3" <% if(cred_type_id.equals("3")){%> selected<%}%>>工作证</option>
					</SELECT>
					</TD>
					<TD class=column align="right">证件号码</TD>
					<TD><INPUT TYPE="text" NAME="cardno" maxlength=30 class=bk value="<%=cardno%>"></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
				
					<TD class=column align="right">单位地址</TD>
					<TD><INPUT TYPE="text" NAME="address" maxlength=15 class=bk value="<%=address%>"></TD>
					<TD class=column align="right">用户状态编码</TD>
					<TD>
					<SELECT name="user_state" class=bk>
					<option value="0" <% if(user_state.equals("0")){%> selected<%}%>>开户</option>
					<option value="1" <% if(user_state.equals("1")){%> selected<%}%>>消户</option>
					<option value="2" <% if(user_state.equals("2")){%> selected<%}%>>暂停</option>
					</SELECT>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF"> 
				
				  <TD class=column align="right">联系电话号码</TD>
				  <TD><INPUT TYPE="text" NAME="linkphone" maxlength=15 class=bk value="<%=linkphone%>"></TD>
                  <TD class=column align="right">手机号码</TD>
				  <TD><INPUT TYPE="text" NAME="mobile" maxlength=10 class=bk value="<%=mobile%>"></TD>
                </TR>
				<TR bgcolor="#FFFFFF">
				
					<TD class=column align="right">Email联系方式</TD>
					<TD><INPUT TYPE="text" NAME="email" maxlength=30 class=bk value="<%=email%>"></TD>
					<TD class=column align="right">实际完成时间</TD>
					<TD><INPUT TYPE="text" NAME="complete_time" maxlength=10 value="<%=longtime_date%>" class=bk>
					<INPUT TYPE="button" value="▼" class=btn onclick="showCalendar('day',event)"><INPUT TYPE="hidden" NAME="hidcomplete_time" class=bk>&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
				
					<TD class=column align="right">备注</TD>
					<TD colspan="3"><INPUT TYPE="text" NAME="remark1" maxlength=50 size=50 class=bk value="<%=remark1%>">
					</TD>
				</TR>
                <TR>
					<TD colspan="4" align="center" class=foot>
						<INPUT TYPE="submit" value=" 修 改 " class=btn>&nbsp;&nbsp;
						<INPUT TYPE="button" value=" 关 闭 " class=btn onclick="javascript:window.close();">&nbsp;&nbsp;
						<INPUT TYPE="button" value=" 重新关联用户信息 " class=btn onclick='reRelateUser();'>
						<INPUT TYPE="hidden" name="action" value="edit">
						<INPUT TYPE="hidden" name="vpn_id" value="<%=str_vpn_id%>">
					</TD>
				</TR>
              </TABLE>
			</TD>
		</TR>
	</TABLE>
  </TD></TR>
</TABLE>
<script language="JavaScript">
<!--
//alert(parent.document.all("userinfo").innerHTML);
//alert(document.all.myTable.outerHTML);
parent.document.all("userinfo").style.display="";
parent.document.all("userinfo").innerHTML=document.all.myTable.outerHTML;
//-->
</script>