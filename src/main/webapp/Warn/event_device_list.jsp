<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="qryp" scope="page" class="com.linkage.litms.common.database.QueryPage" />
<%-- 
 * 创建日期 2007-3-6
 * Administrator liuli
--%>
<%
request.setCharacterEncoding("GBK");

String str_device_model_id = request.getParameter("device_model_id");
String str_Mycheckbox1 = request.getParameter("Mycheckbox1");
String str_start_ip = request.getParameter("start_ip");
String str_over_ip = request.getParameter("over_ip");
String sql="";



String strData = "";
sql = "select loopback_ip,device_name from tab_gw_device where 1=1 and gather_id in('" +str_Mycheckbox1 + "')" ;

if(str_start_ip!=null&&!str_start_ip.equals("") && !str_start_ip.equals("-1")){
	sql+=" and loopback_ip>='"+str_start_ip+"'";
}
if(!str_over_ip.equals("") && str_over_ip!=null&&!str_over_ip.equals("-1")){
	sql+=" and loopback_ip<='"+str_over_ip+"'";	
}
if(str_device_model_id!=null && !str_device_model_id.equals("") && !str_device_model_id.equals("-1")){
	sql+=" and device_model_id='" + str_device_model_id + "'";	
}
String stroffset = request.getParameter("offset");
int pagelen = 15;
int offset;
if(stroffset == null) offset = 1;
else offset = Integer.parseInt(stroffset);
qryp.initPage(sql,offset,pagelen);
String strBar = qryp.getPageBar();   
Cursor cursor = DataSetBean.getCursor(sql,offset,pagelen);
Map fields = cursor.getNext();

    if(fields == null){
	strData = "<TR><TD COLSPAN=2 HEIGHT=30 CLASS=column>系统中没有定义相关设备信息</TD></TR>";
    }
    else{
	int i=1;
	while(fields != null){
		strData += "<TR>";	
		strData += "<TD CLASS=column1 align=center>"+ fields.get("device_name") + "</TD>";
		strData += "<TD CLASS=column2 align=center>"+ fields.get("loopback_ip") + "</TD>";
		strData += "</TR>";
		i++;
		fields = cursor.getNext();
	}
	strData += "<TR><TD COLSPAN=2 align=right CLASS=column>" + strBar + "</TD></TR>";
    }
    fields = null;
%>
<link rel="stylesheet" href="../css/css_blue.css" type="text/css">
<link rel="stylesheet" href="../css/tree.css" type="text/css">
<link rel="stylesheet" href="../css/tab.css" type="text/css">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
     <FORM NAME="frm"  METHOD="POST" ACTION="" onSubmit="return CheckForm();">
		<table width="80%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="blue_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">设备列表信息</div>
						</td>
						<td><img src="../images/attention_2.gif" width="15" height="12"> 注：对应告警事件的设备相关信息</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">
				<table width="100%" border=0 align="center" cellpadding="1"
					cellspacing="1" bgcolor="#999999" class="text">
					<tr bgcolor="#FFFFFF" class="blue_title">
						<td colspan="2">设备列表信息</td>
					</tr>
               <tr bgcolor="#FFFFFF">
            <td nowrap><div align="center" class="blue_title"> <span> 设备名称</span></div></td>
            <td nowrap><div align="center" class="blue_title"><span> 设备IP</span></div></td>
          </tr>
           <%out.println(strData);%>   
			</table>
			</td>
			</tr>
		</table>
	</FORM>
	</TD>
	</TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>