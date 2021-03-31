<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%@ page
	import="com.linkage.litms.common.database.*,java.util.*,com.linkage.litms.common.util.*"%>

<%
	String oui = request.getParameter("oui");
	String serialnumber = request.getParameter("serialnumber");
	String gw_type = request.getParameter("gw_type");
	
	String tab_name = "tab_hgwcustomer";
	if(gw_type.equals("2")){
		tab_name = "tab_egwcustomer";
	}
	
	String sql = " select a.username,b.serv_type_name,b.serv_type_desc from "+ tab_name +" a,tab_gw_serv_type b"
			+ " where a.device_serialnumber='"+ serialnumber + "' and a.oui='" + oui + "' and a.user_state in('1','2') and a.serv_type_id=b.serv_type_id";

	Cursor cursor = DataSetBean.getCursor(sql);
	
	Map fields = cursor.getNext();


%>
<%@ include file="../head.jsp"%>
<HTML>
<BODY>
<TABLE width="95%" height="30"  border="0" cellpadding="0" cellspacing="0" class="green_gargtd" align="center">
<TR>
  <TD width="162" align="center" class="title_bigwhite">已开通的业务</TD>
</TR>
</TABLE>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
              <TR> 
              	<TH class=column nowrap>用户帐号</TH>
                <TH class=column nowrap>业务名称</TH>
                <TH class=column nowrap>业务描述</TH>
              </TR>
              <%             
              	if(fields == null ){  
               %>
               <TR> 
                <TD class=column colspan="5" nowrap>设备尚未开通任何业务!</TD>
              </TR>
              <%
              } else {
              
              		while(fields != null){
						
						out.println("<TR>");
						out.println("<TD width='20%' class=column>" + fields.get("username") + "</TD>");
						out.println("<TD width='30%' class=column>" + fields.get("serv_type_name") + "</TD>");
						out.println("<TD class=column>" + fields.get("serv_type_desc") + "</TD>");
						out.println("</TR>");
						
						fields = cursor.getNext();
  			
              		}           
              }

               %>
			
              <TR> 
                <TD colspan=7 class="green_foot" align=right> 
                  <INPUT TYPE="button" value=" 关 闭 " onclick="javascript:window.close();" class=jianbian>
                </TD>
              </TR>
            </TABLE>
		  </TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
</BODY>
</HTML>
<%@ include file="../foot.jsp"%>