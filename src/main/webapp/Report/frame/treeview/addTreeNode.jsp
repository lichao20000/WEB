<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ page import="com.linkage.commons.db.PrepareSQL" %>
<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String rp_id = request.getParameter("id");
String rootid = request.getParameter("rootid");
int layer = Integer.parseInt(request.getParameter("layer"))+1;

String strSQL = "select * from tab_role";
// teledb
if (DBUtil.GetDB() == 3) {
    strSQL = "select role_id, role_name from tab_role";
}
PrepareSQL psql = new PrepareSQL(strSQL);
psql.getSQL();
Cursor cursor = DataSetBean.getCursor(strSQL);
Map fields = cursor.getNext();
String roleStr="";
int i=1;
while(fields != null){
	roleStr += "<input type=checkbox name=role_id value="+ fields.get("role_id")+">&nbsp;"+fields.get("role_name");
	if(i%4==0) roleStr += "<br>";
	i++;
	fields = cursor.getNext();
}

cursor = null;
fields = null;
%>
<HTML>
<HEAD>
<TITLE> <%=LipossGlobals.getLipossName()%> </TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<link rel="stylesheet" href="../../../css/css_blue.css" type="text/css">
<LINK REL="stylesheet" HREF="tab.css" TYPE="text/css">

</HEAD>
<BODY scrolling="no" onload = showInfo()>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<!-- ������ start -->
	<form name=frm action="rp_nodesave.jsp"  method="post" target="childFrm" onsubmit="return checkElmenets();">
	    <TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
          <TR><TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
		  <TR height=30>
			<TD class="curstarttab"><div align=center><font color=blue>��������ڵ�</font></div>
                  </TD>
		  </TR>
		</TABLE>
	  </TD></TR>
	  <TR><TD bgcolor=#999999>
	          <TABLE border=0 cellspacing=1 cellpadding=6 width="100%" id="idTable">
                <TR> 
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" width="20%" class=column3> 
                    <div align="right">��������</div>
                  </TD>
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" class=column3 width="80%"> 
                    <input type=text name="report_name" class="bk" size="30">
                    &nbsp;<font color="#FF0000">*</font>
                  </TD>
                </TR>
                <TR> 
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" class=column3 width="20%"> 
                    <div align="right">��������</div>
                  </TD>
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" class=column3 width="80%"> 
                    <input type=text name="sqlvalue" class="bk" size="30">
                    &nbsp;<font color="#FF0000">*</font>
                  </TD>
                </TR>
                <TR> 
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" class=column3 width="20%"> 
                    <div align="right">���Ȩ��</div>
                  </TD>
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" class=column3 width="80%"> 
                    <%=roleStr%>
                  </TD>
                </TR>
                <TR> 
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" class=column3  colspan=2> 
                    <div align="center">
                      <input type=submit name=add class="btn" value="�� ��">
					  &nbsp;&nbsp;&nbsp;&nbsp;
                      <input type=button name=cancel class="btn" value="ȡ ��" onclick="javascript:window.close();">
                    </div>
                  </TD>
                </TR>

              </TABLE>
	  </TD></TR>
	  <TR><TD HEIGHT=10>&nbsp;</TD></TR>

	</TABLE>
	<input type=hidden name=hidpath value="">
	<input type=hidden name=rootid value="<%=rootid%>">
	<input type=hidden name=hidparentid value="<%=rp_id%>">
	<input type=hidden name=hidlayer value="<%=layer%>">
	<input type=hidden name=report_stat value="3">
	</form>
	<!-- ������ end -->
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<iframe id="childFrm" name = "childFrm" width="0" height="0"></iframe>
</body></html>
<script language=javascript>
function showInfo(){
	document.all("hidpath").value = opener.document.all("rp_path").value;
}

function demo(){
	alert(document.all("hidpath").value);
}

function checkElmenets(){
	var obj = document.frm;
	
	if(KillSpace(obj.report_name.value).length == 0){
		alert("�������Ʋ���Ϊ��");
		obj.report_name.focus();
		return false;
	}
	
	if(KillSpace(obj.sqlvalue.value).length == 0){
		alert("�������ӵ�ַ����Ϊ��");
		obj.sqlvalue.focus();
		return false;
	}
	/*
	if(KillSpace(obj.gr_name.value).length == 0){
		alert("��ɫȨ�޲���Ϊ��");
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

//flag -1ʧ�ܣ�+1�ɹ���0���ظ�ֵ
function doString(flag){
	if(flag == -1){
		alert("�����½ڵ����ʧ��");
		window.close();
	}else if(flag == 0){
		alert("�½ڵ���ԭ�нڵ������ظ�������ʧ��");
		window.close();
	}else if(flag == 1){
		alert("�½ڵ㴴���ɹ�");
		opener.document.location.reload();
		window.close();
	}
}
</script>
