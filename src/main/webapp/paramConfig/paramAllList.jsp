<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page import="com.linkage.litms.resource.FileSevice"%>

<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
int int_flag  = 0;

if (device_id != null) {
	//测试连接  成功：1 失败： 0
	FileSevice  fileSevice = new  FileSevice();
	int_flag  = fileSevice.testConnection(request);
}

%>
<jsp:include page="/share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="radio"/>
	<jsp:param name="jsFunctionName" value=""/>
</jsp:include>
<SCRIPT LANGUAGE="JavaScript">

var device_id ="<%=device_id%>";
var oldTdObj = null;
//使选中的参数变色
function setTdColor(obj){
	if(oldTdObj != null){
		oldTdObj.className="";
	}
	obj.className="trOver";
	oldTdObj = obj;
}

//选择DLS参数
function selDLS(obj)
{
  document.all("childFrm5").src ="../webtopo/dlsParam.jsp?device_id="+device_id;
  setTdColor(obj);
}

//选择PPOE、IP、ATM参数
function selPPPOE(obj)
{
  document.all("childFrm5").src ="../webtopo/pppoeAndIpAndAtmParam.jsp?device_id="+device_id;
  setTdColor(obj);
}

//选择LAN参数
function selLAN(obj)
{
  document.all("childFrm5").src ="../webtopo/lanParam.jsp?device_id="+device_id;
  setTdColor(obj);
}

//选择WLAN参数
function selWLAN(obj)
{
  document.all("childFrm5").src ="../webtopo/wlanParam.jsp?device_id="+device_id;
  setTdColor(obj);
}

function CheckForm(){
	if(!deviceSelectedCheck()){
		alert("请选择设备！");
		return false;
	}
}

</SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="paramAllList.jsp" onsubmit="return CheckForm()">
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
									常用参数列表。
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
													<INPUT TYPE="submit" value=" 获 取 " class=btn >
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
				
<table style="display:'<%=device_id != null ? "" : "none"%>'" width="98%" height="30" border="0" cellspacing="0" align="center" cellpadding="0" class="green_gargtd">
	<tr>
		<td width="162" align="center" class="title_bigwhite">
			常见参数
		</td>
		<td>
			<img src="../images/attention_2.gif" width="15" height="12">
			您可以选择查看具体参数		
		</td>
		<td align =center>
		   设备连接状态：
		   <%if(int_flag==1) {%>
			<img src="../images/gaojin_green.gif" width="15" height="12">
			<%}else{%>
			<img src="../images/gaojin_red.gif" width="15" height="12">
			<%} %>
		</td>
	</tr>
</table>
<table style="display:'<%=device_id != null ? "" : "none"%>'" width="98%" border=0 cellspacing=1 cellpadding=0 align="center" bgcolor="#999999" class=text>
	<tr>
	  <td>
		<table width="100%" border=0 cellspacing=1 cellpadding=0 align="center">
		  <tr bgcolor="#ffffff" >
		      <td onclick="selDLS(this)" width="15%" height=60><span ><a href="javascript://" >&nbsp;&nbsp;DSL参数</a></span></td>
		   </tr>
		   <tr bgcolor="#ffffff" >
		      <td onclick="selPPPOE(this)" nowrap height=60><span><a href="javascript://" >&nbsp;&nbsp;PPPOE、IP、ATM参数</a></span></td>
	       </tr>
		   <tr bgcolor="#ffffff" >
		      <td onclick="selLAN(this)" nowrap height=60><span><a href="javascript://" >&nbsp;&nbsp;LAN参数</a></span></td>
		   </tr>
		   <tr bgcolor="#ffffff" >
		      <td onclick="selWLAN(this)" nowrap height=60><span><a href="javascript://" >&nbsp;&nbsp;WLAN参数</a></span></td>
		   </tr>
		 </table>
	  </td>
	  <td height=100%>
	    <iframe id="childFrm5" name="childFrm5" frameborder="0" width="100%" height="100%"></iframe>
	  </td> 
	</tr>
</table>
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