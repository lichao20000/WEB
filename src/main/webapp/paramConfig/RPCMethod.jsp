<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page import="com.linkage.litms.resource.RPCMethod,ACS.DevRpc"%>

<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
Map rpcmethodMap = null;
if (device_id != null) {
	
	RPCMethod  RPCMethod = new  RPCMethod();
	rpcmethodMap  = RPCMethod.GetRPCMethods(request);
}
%>
<SCRIPT LANGUAGE="JavaScript">

function CheckForm(){
	if(!deviceSelectedCheck()){
		alert("��ѡ���豸��");
		return false;
	}
}

</SCRIPT>
<jsp:include page="/share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="radio"/>
	<jsp:param name="jsFunctionName" value=""/>
</jsp:include>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="RPCMethod.jsp" onsubmit="return CheckForm()">
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									���ò�ѯ
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									��ѯѡ����豸�Ĳ����б�
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<TR>
						<TH colspan="4" align="center">
							�豸��ѯ
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF" >
						<td colspan="4">
							<div id="selectDevice"><span>������....</span></div>
						</td>
					</TR>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">												
													<INPUT TYPE="submit" value=" �� �� " class=btn >
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
				
	<TABLE style="display:'<%=device_id != null ? "" : "none"%>'" width="98%" border=0 cellspacing=0 cellpadding=0 align="center">	    
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						RPC������ȡ
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						�豸��<%=rpcmethodMap != null ? rpcmethodMap.get("oui") : "" %>-<%= rpcmethodMap != null ? rpcmethodMap.get("SerialNumber") : ""%>�� ����Ϣ.
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
					  if ((rpcmethodMap != null) && (rpcmethodMap.get("rpcArr") != null)){
						  	String[] rpcArr = (String[])rpcmethodMap.get("rpcArr");
							for (int i=0;i<rpcArr.length;i++){
					%>
     							<TR><TD CLASS=column1 align=left><%=rpcArr[i]%></TD></TR>
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
	</TABLE>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>