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
	//��������  �ɹ���1 ʧ�ܣ� 0
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
//ʹѡ�еĲ�����ɫ
function setTdColor(obj){
	if(oldTdObj != null){
		oldTdObj.className="";
	}
	obj.className="trOver";
	oldTdObj = obj;
}

//ѡ��DLS����
function selDLS(obj)
{
  document.all("childFrm5").src ="../webtopo/dlsParam.jsp?device_id="+device_id;
  setTdColor(obj);
}

//ѡ��PPOE��IP��ATM����
function selPPPOE(obj)
{
  document.all("childFrm5").src ="../webtopo/pppoeAndIpAndAtmParam.jsp?device_id="+device_id;
  setTdColor(obj);
}

//ѡ��LAN����
function selLAN(obj)
{
  document.all("childFrm5").src ="../webtopo/lanParam.jsp?device_id="+device_id;
  setTdColor(obj);
}

//ѡ��WLAN����
function selWLAN(obj)
{
  document.all("childFrm5").src ="../webtopo/wlanParam.jsp?device_id="+device_id;
  setTdColor(obj);
}

function CheckForm(){
	if(!deviceSelectedCheck()){
		alert("��ѡ���豸��");
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
									���ò�ѯ
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									���ò����б�
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
													<INPUT TYPE="submit" value=" �� ȡ " class=btn >
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
			��������
		</td>
		<td>
			<img src="../images/attention_2.gif" width="15" height="12">
			������ѡ��鿴�������		
		</td>
		<td align =center>
		   �豸����״̬��
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
		      <td onclick="selDLS(this)" width="15%" height=60><span ><a href="javascript://" >&nbsp;&nbsp;DSL����</a></span></td>
		   </tr>
		   <tr bgcolor="#ffffff" >
		      <td onclick="selPPPOE(this)" nowrap height=60><span><a href="javascript://" >&nbsp;&nbsp;PPPOE��IP��ATM����</a></span></td>
	       </tr>
		   <tr bgcolor="#ffffff" >
		      <td onclick="selLAN(this)" nowrap height=60><span><a href="javascript://" >&nbsp;&nbsp;LAN����</a></span></td>
		   </tr>
		   <tr bgcolor="#ffffff" >
		      <td onclick="selWLAN(this)" nowrap height=60><span><a href="javascript://" >&nbsp;&nbsp;WLAN����</a></span></td>
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