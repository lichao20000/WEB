<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.common.database.Cursor, com.linkage.litms.common.database.DataSetBean,java.util.*"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%
request.setCharacterEncoding("GBK");
String strAction = request.getParameter("action");
String groupid = request.getParameter("groupid");
String strSQL = "select * from pping_group_conf where groupid="+groupid;
// teledb
if (DBUtil.GetDB() == 3) {
	strSQL = "select groupdesc, timeinterval, delaythreshold, timeout, isstat, warnmode, warnlevel, timeoutwarnlevel" +
			" from pping_group_conf where groupid="+groupid;
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSQL);
psql.getSQL();
String strData = "";
boolean isAdd = false;
String groupdesc = "";
String timeinterval = "";
String delaythreshold = "";
String timeout = "";
String isstat = "";
String warnmode = "";
String warnlevel = "";
String timeoutwarnlevel = "";
long area_id = user.getAreaId();

String selectGatherSql = "select gather_id,descr from tab_process_desc "
	   +"where gather_id in (select res_id from tab_gw_res_area where res_type=2 and area_id="+area_id+")";
Cursor cursor2 = DataSetBean.getCursor(selectGatherSql);
//Map fields2 = cursor2.getNext();
String gatherStr = FormUtil.creatSelectOption(cursor2,"gather_id","descr");
Map fields = DataSetBean.getRecord(strSQL);
if(fields == null){
	// 取得groupid
	long l_groupid = DataSetBean.getMaxId("pping_group_conf", "groupid");
	groupid = String.valueOf(l_groupid);
	isAdd = true;
}
else{
	groupdesc = (String)fields.get("groupdesc");
	timeinterval = (String)fields.get("timeinterval");
	delaythreshold = (String)fields.get("delaythreshold");
	timeout = (String)fields.get("timeout");
	isstat = (String)fields.get("isstat");
	warnmode = (String)fields.get("warnmode");
	warnlevel = (String)fields.get("warnlevel");
	timeoutwarnlevel = (String)fields.get("timeoutwarnlevel");
}
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function CheckForm() {
		var obj = document.frm;
		if(!IsNull(obj.timeinterval.value,"检测时间间隔")){
			obj.timeinterval.focus();
			obj.timeinterval.select();
			return false;
		} else if(obj.timeinterval.value!="" && Trim(obj.timeinterval.value)==""){
			alert("检测时间间隔应为数字");
			obj.timeinterval.focus();
			obj.timeinterval.select();
			return false;
		} else if(obj.timeinterval.value!="" && !IsNumber(obj.timeinterval.value,"检测时间间隔")){		
			obj.timeinterval.focus();
			obj.timeinterval.select();
			return false;
		} else if(!IsNull(obj.delaythreshold.value,"时延门限值")){
			obj.delaythreshold.focus();
			obj.delaythreshold.select();
			return false;
		} else if(obj.delaythreshold.value!="" && Trim(obj.delaythreshold.value)==""){
			alert("时延门限值应为数字");
			obj.delaythreshold.focus();
			obj.delaythreshold.select();
			return false;
		} else if(obj.delaythreshold.value!="" && !IsNumber(obj.delaythreshold.value,"时延门限值")){		
			obj.delaythreshold.focus();
			obj.delaythreshold.select();
			return false;
		} else if(!IsNull(obj.timeout.value,"等待超时值")){
			obj.timeout.focus();
			obj.timeout.select();
			return false;
		} else if(obj.timeout.value!="" && Trim(obj.timeout.value)==""){
			alert("等待超时值应为数字");
			obj.timeout.focus();
			obj.timeout.select();
			return false;
		} else if(obj.timeout.value!="" && !IsNumber(obj.timeout.value,"等待超时值")){		
			obj.timeout.focus();
			obj.timeout.select();
			return false;
		} else if(obj.gather.value == "-1") {
			alert("请选择采集点");
			obj.gather.focus();
			return false;
		} else {
			return true;
		}
	}

	function refreshPage() {
		window.close();
		//alert(parent);
		//alert(opener);
		//alert(opener.location);
		opener.location.reload();
	}

	function editBack() {
		window.history.go(-1);
		window.location.reload();
	}
//-->
</SCRIPT>


<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
<FORM NAME="frm" METHOD="post" ACTION="PowerPingConfigSave.jsp" onsubmit="return CheckForm()" target="childfrm">
    <TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" id="myTable">
		<TR>
			<TD bgcolor=#000000>
				
              <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			   <TR> 
                  <TD bgcolor="#ffffff" colspan="4">带'<font color="#FF0000">*</font>'的表单必须填写或选择</TD>
                </TR>
                <TR>
                  <TH colspan="4" align="center">添加IP组</TH>
                </TR>
                <TR bgcolor="#FFFFFF"> 
				
                  <TD class=column align="right">IP组编号</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="groupid" maxlength=100 class=bk value="<%=groupid%>" readonly>
                    <font color="#FF0000">*</font></TD>
                  <TD class=column align="right">组描述</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="groupdesc" maxlength=100 class=bk value="<%=groupdesc%>">
                  </TD>
                </TR>
				<TR bgcolor="#FFFFFF"> 
				
                  <TD class=column align="right">检测时间间隔</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="timeinterval" maxlength=100 class=bk value="<%=timeinterval%>">&nbsp;秒&nbsp;<font color="#FF0000">*</font></TD>
                  <TD class=column align="right">时延门限值</TD>
                  <TD> 
                    <INPUT TYPE="text" NAME="delaythreshold" maxlength=100 class=bk value="<%=delaythreshold%>">&nbsp;毫秒&nbsp;<font color="#FF0000">*</font>
                  </TD>
                </TR>
				<TR bgcolor="#FFFFFF">
				
					<TD class=column align="right">等待超时值</TD>
					<TD><INPUT TYPE="text" NAME="timeout" maxlength=20 class=bk value="<%=timeout%>">&nbsp;毫秒&nbsp;<font color="#FF0000">*</font>
					</TD>
					<TD class=column align="right">发告警模式</TD>
					<TD>
					<SELECT name="warnmode" class=bk>
					<option value="-1" <% if (warnmode.equals("-1")) {%>selected<%}%>>==请选择==</option>
					<option value="0" <% if (warnmode.equals("0")) {%>selected<%}%>>只发一次</option>
					<option value="1" <% if (warnmode.equals("1")) {%>selected<%}%>>连续发送</option>
					</SELECT>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF"> 
					<TD class=column align="right">时延告警等级</TD>
					<TD>
					<SELECT name="warnlevel" class=bk>
					<option value="-1" <% if (warnlevel.equals("-1")) {%>selected<%}%>>==请选择==</option>
					<option value="0" <% if (warnlevel.equals("0")) {%>selected<%}%>>自动清除</option>
					<option value="1" <% if (warnlevel.equals("1")) {%>selected<%}%>>正常日志</option>
					<option value="2" <% if (warnlevel.equals("2")) {%>selected<%}%>>提示告警</option>
					<option value="3" <% if (warnlevel.equals("3")) {%>selected<%}%>>一般告警</option>
					<option value="4" <% if (warnlevel.equals("4")) {%>selected<%}%>>严重告警</option>
					<option value="5" <% if (warnlevel.equals("5")) {%>selected<%}%>>紧急告警</option>
					</SELECT>
					</TD>
					<TD class=column align="right">超时告警等级</TD>
					<TD>
					<SELECT name="timeoutwarnlevel" class=bk>
					<option value="-1" <% if (timeoutwarnlevel.equals("-1")) {%>selected<%}%>>==请选择==</option>
					<option value="0" <% if (timeoutwarnlevel.equals("0")) {%>selected<%}%>>自动清除</option>
					<option value="1" <% if (timeoutwarnlevel.equals("1")) {%>selected<%}%>>正常日志</option>
					<option value="2" <% if (timeoutwarnlevel.equals("2")) {%>selected<%}%>>提示告警</option>
					<option value="3" <% if (timeoutwarnlevel.equals("3")) {%>selected<%}%>>一般告警</option>
					<option value="4" <% if (timeoutwarnlevel.equals("4")) {%>selected<%}%>>严重告警</option>
					<option value="5" <% if (timeoutwarnlevel.equals("5")) {%>selected<%}%>>紧急告警</option>
					</SELECT>
					</TD>
                </TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">采集点</TD>
					<TD colspan="3">
					<SELECT name="gather" class=bk>
				      <%=gatherStr%>
				    </SELECT>&nbsp;<font color="#FF0000">*</font>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="left" colspan="4"><INPUT type="checkbox" name="isstat" value="1" <%if (isstat.equals("1")) {%>checked<%}%>>是否生成报表</TD>
				</TR>
                <TR> 
                  <TD colspan="4" align="right" class=foot> 
					<% if (isAdd) {%>
					<INPUT TYPE="submit" value=" 保 存 " class="jianbian">
                    &nbsp;&nbsp;
					<INPUT TYPE="button" value=" 关 闭 " onclick="javascript:window.close()" class="jianbian">
					<INPUT TYPE="hidden" name="action" value="add">
					<%} else {%>
					<INPUT TYPE="submit" value=" 修 改 " class="jianbian">
                    &nbsp;&nbsp;
					<INPUT TYPE="button" value=" 返 回 " onclick="editBack()" class="jianbian">
					<INPUT TYPE="hidden" name="action" value="edit">
					<%}%>
                  </TD>
                </TR>
              </TABLE>
			</TD>
		</TR>
	</TABLE>
	</FORM>
  </TD></TR>
</TABLE>
<iframe id="childfrm" name="childfrm" align="center" style="display:none"></iframe>