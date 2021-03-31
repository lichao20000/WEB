<%--
FileName	: IpLineSave.jsp
Author		: Dolphin
Date			: 2002-9-28
Note			: 属地增加、修改、删除操作
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="city" scope="request" class="com.linkage.module.gwms.dao.tabquery.CityDAO"/>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="java.io.PrintWriter"%>
<%
request.setCharacterEncoding("GBK");
//DataSetBean db = new DataSetBean();
String strMsg = city.cityActDo(request);

String area = LipossGlobals.getLipossProperty("InstArea.ShortName");

//山西联通
if(area.equals("sx_lt")){
	PrintWriter pw = response.getWriter();
	pw.print(strMsg);
	pw.flush();
	pw.close();
	return;
}

/*
String strSQL;
String strMsg=null;
String strAction = request.getParameter("action");

if(strAction.equals("delete")){	//删除操作
	String str_city_id = request.getParameter("city_id");
	strSQL = "delete from tab_city where city_id='"+ str_city_id+"'";
}
else{
	String city_id_old = request.getParameter("city_id_old");
	String str_city_id = request.getParameter("city_id");
	String str_city_name = request.getParameter("city_name");
	//String str_sno = request.getParameter("sno");
	String str_staff_id = request.getParameter("staff_id");
	String str_remark = request.getParameter("remark");

	if(strAction.equals("add")){	//增加操作
		//判断是否重复
		strSQL = "select * from tab_city where city_id='"+str_city_id+"' or city_name='"+str_city_name+"'";
		if(DataSetBean.getRecord(strSQL)!=null)
			strMsg = "属地编号\""+str_city_id+"\"或属地名称\""+str_city_name+"\"已经存在，请换一个属地编号或属地名称。";
		else{
			strSQL = "insert into tab_city (city_id,city_name,staff_id,remark) values ('"+ str_city_id +"','"+ str_city_name +"','"+ str_staff_id +"','"+ str_remark +"')";
			strSQL = StringUtils.replace(strSQL,",,",",null,");
			strSQL = StringUtils.replace(strSQL,",,",",null,");
			strSQL = StringUtils.replace(strSQL,",)",",null)");
		}
	}
	else{	//修改操作
		//判断是否重复
		strSQL = "select * from tab_city where (city_id='"+str_city_id+"' or city_name='"+str_city_name+"') and city_id<>'"+city_id_old+"'";
		if(DataSetBean.getRecord(strSQL)!=null)
			strMsg = "属地编号\""+str_city_id+"\"或属地名称\""+str_city_name+"\"已经存在，请换一个属地编号或属地名称。";
		else{
			strSQL = "update tab_city set city_id='"+str_city_id+"',city_name='"+ str_city_name +"',staff_id='"+ str_staff_id +"',remark='"+ str_remark +"' where city_id='" + city_id_old+"'";
			strSQL = StringUtils.replace(strSQL,"=,","=null,");
			strSQL = StringUtils.replace(strSQL,"= where","=null where");
		}
	}
}

if(strMsg==null && !strSQL.equals("")){
	int iCode = DataSetBean.executeUpdate(strSQL);
	if(iCode > 0){
		strMsg = "属地资源操作成功！";
	}
	else{
		strMsg = "属地资源操作失败，请返回重试或稍后再试！";
	}
}
*/
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	this.location = page;
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">属地资源操作提示信息</TH>
					</TR>
						<TR height="50">
							<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
						</TR>
					<TR>
						<TD class=foot align="right">
						<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('CityList.jsp')" value=" 列 表 " class=btn>

						<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.go(-1);" value=" 返 回 " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>