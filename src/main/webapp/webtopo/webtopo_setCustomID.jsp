<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

String title = request.getParameter("title");
String obj_id = request.getParameter("obj_id");


%>
<script language="JavaScript">
<!--

var isCall = 0;
var iTimerID;

function CallPro()
{
	if (parseInt(isCall,10) == 1)
	{	
		window.alert("������ͻ���Ϣ�ɹ�!");
		window.clearInterval(iTimerID);	
		window.close();
	}
	else if(parseInt(isCall,10) == -1)
	{
		window.alert("������ͻ���Ϣʧ�ܣ������¹���!");
		isCall=0;
		window.clearInterval(iTimerID);	
	}
}
function CheckForm()
{	
	//alert(frm.customid.value);
	if(frm.customid.value=="")
	{
		window.alert("��ѡ��������ͻ�");
		return ;
	}
	
	var page = "webtopo_SaveDevCustomInfo.jsp?obj_id=" + frm.obj_id.value + "&customid=" + frm.customid.value ;
	//alert(page);
	document.all("childFrm").src=page;
	iTimerID = window.setInterval("CallPro()", 500);
}

function SelUser()
{
	var page="SelVpnUser.jsp";
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,width=500,height=300";
	window.open(page,"ѡ���ͻ�",otherpra);
}

function setUserProperty(customid,customname)
{
	frm.customid.value=customid;
	frm.customname.value=customname;
	
}

//-->
</script>
<%@ include file="../head.jsp"%>
<title>�����ͻ�</title>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
    <FORM NAME="frm" METHOD="post">
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH colspan="2" align="center">�����ͻ���Ϣ</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="30%">��Ԫ����</TD>
					<TD>
						<input type="text" name="devname" value="<%=title%>">
						<input type="hidden" name="obj_id" value="<%=obj_id%>">
					</TD>					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="30%">������ͻ�</TD>
					<TD>
						<input type="hidden" name="customid" >
						<input type="text" class=bkreadOnly name="customname" readonly >

						<input type="button" name="seluser" onclick="javascript:SelUser();" value="..." class="jianbian">
					</TD>					
				</TR>
				<TR>
					<TD colspan="2" align="center" class=foot>
						<INPUT TYPE="button" value=" �� �� " onclick="javascript:CheckForm();" class=btn>&nbsp;&nbsp;
						<INPUT TYPE="button" value=" �� �� " class=btn onclick="javascript:window.close();">						
					</TD>
				</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
    </FORM>
<TR><TD>&nbsp;</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>