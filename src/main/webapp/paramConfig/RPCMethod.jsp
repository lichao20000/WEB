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
		alert("请选择设备！");
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
									配置查询
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									查询选择的设备的参数列表。
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<TR>
						<TH colspan="4" align="center">
							设备查询
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF" >
						<td colspan="4">
							<div id="selectDevice"><span>加载中....</span></div>
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
													<INPUT TYPE="submit" value=" 测 试 " class=btn >
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
						RPC方法获取
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						设备『<%=rpcmethodMap != null ? rpcmethodMap.get("oui") : "" %>-<%= rpcmethodMap != null ? rpcmethodMap.get("SerialNumber") : ""%>』 的信息.
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH>获取结果</TH>
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
      					<TR><TD CLASS=column1 align=left>设备连接失败。</TD></TR>
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