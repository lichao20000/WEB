<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.*"%>
<%
request.setCharacterEncoding("GBK");
RPCMethod  RPCMethod = new  RPCMethod();

DevRPC[] devRPCArr  = RPCMethod.GetRPCMethods(request);

%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@page import="ACS.DevRPC"%>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
    <FORM METHOD="POST" ACTION="" NAME="frm" onSubmit="return CheckForm();">
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">	    
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						RPC������ȡ
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						�豸��<%=devRPCArr[0].OUI %>-<%= devRPCArr[0].SerialNumber %>�� ����Ϣ.
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH>��ȡ���</TH>
					</TR>
					<%
					  if ((devRPCArr != null) && (devRPCArr[0].rpcArr != null)){
							for (int i=0;i<devRPCArr[0].rpcArr.length;i++){
					%>
     							<TR><TD CLASS=column1 align=left><%=devRPCArr[0].rpcArr[i]%></TD></TR>
  					<%
  						  	}
   					  }else{
   					%>
      					<TR><TD CLASS=column1 align=left>�豸����ʧ�ܡ�</TD></TR>
  					<%
  					  } 
  					%>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD colspan=2 align=right hight="20" >
				&nbsp;               				
			</TD>
		</TR>		
		<TR>
			<TD colspan=2 align=right>
				<input type="button" name="close" value=" �� �� " onclick="javascript:window.close();" class="btn">               				
			</TD>
		</TR>
	</TABLE>
	</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
</body>
<%@ include file="../foot.jsp"%>