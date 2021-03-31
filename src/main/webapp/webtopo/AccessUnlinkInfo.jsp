<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.common.database.Cursor, com.linkage.litms.common.database.DataSetBean,java.util.*"%>

<%@ include file="../timelater.jsp"%>
<SCRIPT LANGUAGE="JavaScript" id="idParentFun">
var link_auto_id;
var vpn_link_id;
var vpn_id_js;
</SCRIPT>

<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
	String strData2 = "";
	String strData_new = "";
	String vpn_id = request.getParameter("vpn_id");
	String link_auto_id = request.getParameter("link_auto_id");
	String vpn_auto_id = request.getParameter("vpn_auto_id");
	String strsql = "select vpn_id from vpn_auto_customer where vpn_auto_id="+vpn_auto_id;
	Cursor cursor = DataSetBean.getCursor(strsql);
	Map fields = cursor.getNext();
	if(fields == null){
	}
	else{
		while(fields != null){
			vpn_id=(String)fields.get("vpn_id");
			fields = cursor.getNext();
		}
	}
	String strSQL = 
        "select vpn_link_id,vpn_id,ext_vpn_link_id,subname,pe_name from vpn_link where vpn_link_id not in (select distinct vpn_link_id from vpn_auto_link where link_auto_id = "+link_auto_id+") and vpn_id="+vpn_id;
	Cursor cursor2 = DataSetBean.getCursor(strSQL);
	Map fields2 = cursor2.getNext();
	if(fields2 == null){
		strData_new = "<TR ><TD COLSPAN=6 HEIGHT=30 bgcolor=#FFFFFF><INPUT TYPE='button' value='新增链路' class=btn onclick='addLink()'></TD></TR>";
	}
	else{
		while(fields2 != null){
			strData2 += "<TR bgcolor=#FFFFFF>";
			strData2 += "<TD>"+ (String)fields2.get("vpn_id") + "</TD>";
			strData2 += "<TD>"+ (String)fields2.get("ext_vpn_link_id") + "</TD>";
			strData2 += "<TD>"+ (String)fields2.get("subname") + "</TD>";
			strData2 += "<TD>"+ (String)fields2.get("pe_name") + "</TD>";
			String vpn_link_id=(String)fields2.get("vpn_link_id");
			vpn_id=(String)fields2.get("vpn_id");
			strData_new = "<TR ><TD COLSPAN=6 HEIGHT=30 bgcolor=#FFFFFF><INPUT TYPE='button' value='新增链路' class=btn onclick='addLink()'></TD></TR>";
			%>
			<SCRIPT LANGUAGE="JavaScript">
			<!--
			link_auto_id=<%=link_auto_id%>;
			vpn_link_id=<%=vpn_link_id%>;
			vpn_id_js=<%=vpn_id%>;
			//-->
			</SCRIPT>
			<%
			strData2 += "<TD class=column2><INPUT TYPE='button' value='关联链路信息' class=btn onclick=' conn_vpn_auto_link("+link_auto_id+","+vpn_link_id+","+vpn_id+")'></TD>";
			strData2 += "</TR>";
			fields2 = cursor2.getNext();
		}
	}
%>

<TABLE border=1 cellspacing=0 cellpadding=0 width="100%" id="myTable_linkInfo">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR>
		<TD width="100%" height="250" valign="top" >
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" bgcolor="#000000">
			<TR>
				<TD bgcolor="#ffffff" colspan="7"><font color="red">此接入点对象暂未关联链路信息，可选择某未关联链路信息与之关联或新增</font></TD>
			</TR>
			<%=strData_new%>
			<TR class="blue_title">
			  <TH>VPN用户标识</TH>
			  <TH>链路统一标识</TH>
			  <TH>VPN用户接入子用户名称</TH>
			  <TH>PE设备名称</TH>
			  <TH width=150>操作</TH>
			</TR>
			<%= strData2%>
			 </TABLE>
		</TD>
</TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.document.all("linkInfo").style.display="";
parent.document.all("linkInfo").innerHTML = document.all("myTable_linkInfo").innerHTML;

//-->
</SCRIPT>