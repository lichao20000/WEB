
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><%--
Author		: liuli
Date		: 2006-10-13
Desc		: 手工录入公告信息
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String strMsg = null;
String strSQL="";
String strAction = request.getParameter("action");
if(strAction.equals("delete")){
	//删除操作
	String Str_title= request.getParameter("id");
	strSQL = "delete from tab_broad_info where id="+Str_title;
}
else{
	String city_id_old = request.getParameter("city_id_old");
	String Str_title= request.getParameter("id");
	String Str_service_year = request.getParameter("service_year");
	String[] Str_kind = request.getParameterValues("kind");
	String Str_biaoti = request.getParameter("biaoti");
	String Str_content = request.getParameter("content");
	SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	String dateString = fmt.format(new Date());
	
	String Str_kinds = "";
	for(int i=0;i<Str_kind.length;i++){
		if(Str_kinds.equals(""))
			Str_kinds = Str_kind[i];
		else
			Str_kinds += "," + Str_kind[i];
	}
	
	if(strAction.equals("add")){	
		//增加操作
		//判断是否重复
		strSQL = "select  title from tab_broad_info where id="+Str_title;
		if(DataSetBean.getRecord(strSQL)!=null)
			strMsg = "标题\""+Str_biaoti+"\"已经存在，请换一个标题。";
		else{ 
			long id = DataSetBean.getMaxId("tab_broad_info","id") + 1;
			// strSQL = "insert into tab_broad_info (id,title,content,titletype,city_id,broad_time,acc_oid) values ("+ id +",'"+ Str_biaoti +"','"+ Str_content +"',"+ Str_service_year +",'"+ Str_kinds +"',"+"convert(datetime,'"+dateString+"'),'"+user.getId()+"')";
			if (DBUtil.GetDB() == 1) {
				// oracle
				strSQL = "insert into tab_broad_info (id,title,content,titletype,city_id,broad_time,acc_oid) values ("+ id +",'"+ Str_biaoti +"','"+ Str_content +"',"+ Str_service_year +",'"+ Str_kinds +"'," + "to_date('" + dateString + "','yyyy-mm-dd HH24:mi:ss'),'"+user.getId()+"')";
			}
			else if (DBUtil.GetDB() == 2) {
				// sybase
				strSQL = "insert into tab_broad_info (id,title,content,titletype,city_id,broad_time,acc_oid) values ("+ id +",'"+ Str_biaoti +"','"+ Str_content +"',"+ Str_service_year +",'"+ Str_kinds +"',"+"convert(datetime,'"+dateString+"'),'"+user.getId()+"')";
			}
			else if (DBUtil.GetDB() == 3) {
				// teledb
				strSQL = "insert into tab_broad_info (id,title,content,titletype,city_id,broad_time,acc_oid) values ("+ id +",'"+ Str_biaoti +"','"+ Str_content +"',"+ Str_service_year +",'"+ Str_kinds +"',"+"str_to_date('" + dateString + "','%Y-%m-%d %H:%i:%s'),'"+user.getId()+"')";
			}
			strSQL = StringUtils.replace(strSQL,",,",",null,");
			strSQL = StringUtils.replace(strSQL,",,",",null,");
			strSQL = StringUtils.replace(strSQL,",)",",null)");
		}
	}
	else{	
		//修改操作
		//判断是否重复
		strSQL = "select * from tab_broad_info where id="+Str_title;
		if(DataSetBean.getRecord(strSQL)!=null)
			strMsg = "标题\""+Str_biaoti+"\"已经存在，请换一个标题。";
		else{
			long id = DataSetBean.getMaxId("tab_broad_info","id") + 1;
			strSQL = "update tab_broad_info set id="+ id +",title='"+ Str_biaoti +"',content='"+ Str_content +"',titletype="+ Str_service_year +",city_id='"+ Str_kinds +"' where title='" + city_id_old+"'";
			strSQL = StringUtils.replace(strSQL,"=,","=null,");
			strSQL = StringUtils.replace(strSQL,"= where","=null where");
		}
	}
}

    if(strMsg==null || !strSQL.equals("")){
		com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSQL);
		psql.getSQL();
		int iCode = DataSetBean.executeUpdate(strSQL);
		if(iCode > 0){
			strMsg = "手工公告信息操作成功！";
		}
		else{
			strMsg = "手工公告信息操作失败，请返回重试或稍后再试！";
		}
    }

%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>	
		<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">公告操作提示信息</TH>
					</TR>
						<TR  height="50">
							<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
						</TR>
					    <TR>
						<TD class=foot align="right">
						<INPUT TYPE="button" NAME="cmdJump" onclick="javascript:document.location.href='inside_gonggaoxinxi1broad.jsp'" value=" 列 表 " class=btn>

						<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.go(-1);" value=" 返 回 " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>	
</TD></TR>
<TR><TD>
</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>