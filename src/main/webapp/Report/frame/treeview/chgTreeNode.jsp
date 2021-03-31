<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ page import="com.linkage.commons.db.PrepareSQL" %>
<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%
request.setCharacterEncoding("GBK");

Map field = null;
String report_name = null;
String p_report_name = null;
String pid = null;
Cursor cursor = null;
Map fields = null;
String strSQL = null;
ArrayList role_list = new ArrayList();
role_list.clear();

String rp_id = request.getParameter("id");
int layer = Integer.parseInt(request.getParameter("layer"));

strSQL = "select * from tab_report_role where report_id = "+ rp_id;
// teledb
if (DBUtil.GetDB() == 3) {
    strSQL = "select role_id from tab_report_role where report_id = "+ rp_id;
}
PrepareSQL psql = new PrepareSQL(strSQL);
psql.getSQL();
cursor = DataSetBean.getCursor(strSQL);
fields = cursor.getNext();
while(fields != null){
	role_list.add(fields.get("role_id"));
	
	fields = cursor.getNext();
}

strSQL = "select * from tab_role where role_id<>1";
// teledb
if (DBUtil.GetDB() == 3) {
    strSQL = "select role_id, role_name from tab_role where role_id<>1";
}
PrepareSQL psql2 = new PrepareSQL(strSQL);
psql2.getSQL();
cursor = DataSetBean.getCursor(strSQL);
fields = cursor.getNext();
String roleStr="";
int i=1;

while(fields != null){
	roleStr += "<input type=checkbox name=role_id value="+ fields.get("role_id")+" "+ ((role_list.contains(fields.get("role_id")))?"checked":"") +">&nbsp;"+fields.get("role_name");
	if(i%4==0) roleStr += "<br>";
	i++;
	fields = cursor.getNext();
}

role_list.clear();
role_list = null;

strSQL = "select * from tab_report_manager where id = "+ rp_id;
// teledb
if (DBUtil.GetDB() == 3) {
    strSQL = "select report_name, sqlvalue from tab_report_manager where id = "+ rp_id;
}
PrepareSQL psql3 = new PrepareSQL(strSQL);
psql3.getSQL();
field = DataSetBean.getRecord(strSQL);
report_name = String.valueOf(field.get("report_name"));
String sqlvalue = String.valueOf(field.get("sqlvalue"));


field = null;
cursor = null;
fields = null;
%>
<HTML>
<HEAD>
<TITLE> <%=LipossGlobals.getLipossName()%> </TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<link rel="stylesheet" href="../../../css/css_blue.css" type="text/css">
</HEAD>
<BODY scrolling="no">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<!-- 主程序 start -->
	<form name=frm action="rp_nodesave.jsp"  method="post" target="childFrm" onsubmit="return checkElmenets();">
	    <TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
          <TR><TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
		  <TR height=30>
			<TD class="curstarttab"><div align=center><font color=blue>修改报表节点属性</font></div>
                  </TD>
		  </TR>
		</TABLE>
	  </TD></TR>
	  <TR><TD bgcolor=#999999>
	          <TABLE border=0 cellspacing=1 cellpadding=6 width="100%" id="idTable">
                <TR> 
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" width="20%" class=column3> 
                    <div align="right">报表名称</div>
                  </TD>
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" class=column3 width="80%"> 
                    <input type=text name="report_name" class="bk" size="30" value="<%=report_name%>">
                    &nbsp;<font color="#FF0000">*</font>
                  </TD>
                </TR>
                <TR> 
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" class=column3 width="20%"> 
                    <div align="right">报表链接</div>
                  </TD>
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" class=column3 width="80%"> 
					<input type=text name="sqlvalue" class="bk" size="30" value="<%=sqlvalue%>">
                    &nbsp;<font color="#FF0000">*</font>
                  </TD>
                </TR>                
                <TR> 
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" class=column3 width="20%"> 
                    <div align="right">浏览权限</div>
                  </TD>
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" class=column3 width="80%"> 
                    <%=roleStr%>
                  </TD>
                </TR>
                <TR> 
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" class=column3  colspan=2> 
                    <div align="center">
                      <input type=submit name=add class="btn" value="修 改">
					  &nbsp;&nbsp;&nbsp;&nbsp;
                      <input type=button name=cancel class="btn" value="取 消" onclick="javascript:window.close();">
                    </div>
                  </TD>
                </TR>

              </TABLE>
	  </TD></TR>
	  <TR><TD HEIGHT=10>&nbsp;</TD></TR>

	</TABLE>
	<input type=hidden name=report_stat value="2">
	<input type=hidden name=id value="<%=rp_id%>">
	</form>
	<!-- 主程序 end -->
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<iframe id="childFrm" name = "childFrm" width="0" height="0"></iframe>
</body></html>
<script language=javascript>

function checkElmenets(){
	var obj = document.frm;
	
	if(KillSpace(obj.report_name.value).length == 0){
		alert("报表名称不能为空");
		obj.report_name.focus();
		return false;
	}
	
	if(KillSpace(obj.sqlvalue.value).length == 0){
		alert("报表链接地址不能为空");
		obj.sqlvalue.focus();
		return false;
	}
	/*
	if(KillSpace(obj.gr_name.value).length == 0){
		alert("角色权限不能为空");
		obj.gr_name.focus();
		return false;
	}
	*/
	
	return true;
}

function KillSpace(x){
	while((x.length > 0) && (x.charAt(0) == ' '))
		x = x.substring(1,x.length)
	while((x.length>0) && (x.charAt(x.length-1) ==' '))
		x = x.substring(0,x.length-1)
	return x
}

//flag -1失败；+1成功；0有重复值
function doString(flag){
	if(flag == -1){
		alert("保存节点操作失败");
		window.close();
	}else if(flag == 0){
		alert("修改节点与原有节点内容重复，操作失败");
		window.close();
	}else if(flag == 1){
		alert("修改节点成功");
		opener.document.location.reload();
		window.close();
	}
}
</script>
