<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
  String gw_type = request.getParameter("gw_type"); 
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
//<!--
var gw_type = "<%=gw_type%>" ;
function checkForm(){
 	var oselect = document.all("device_id");
 	var did = "";
 	if(oselect == null){
		alert("��ѡ���豸��");
		return false;
	 }
	var num = 0;
	if(typeof(oselect.length)=="undefined"){
		if(oselect.checked){
			did = oselect.value;
			num = 1;
		}
 	}else{
		for(var i=0;i<oselect.length;i++){
			if(oselect[i].checked){
				did += oselect[i].value + ",";
				num++;
			}
		}
 	}
 	
 	if(num ==0){
		alert("��ѡ���豸��");
		return false;
	}

	var url = "getDeviceVersion.jsp";
	var pars = "device_id=" + did;
	  pars += "&gw_type="+gw_type;
	document.getElementById("divVersion").innerHTML = "����֪ͨ��̨��ȡ�汾����.....";
	var myAjax
		= new Ajax.Request(
						url,
						{method:"post",
						parameters:pars,
						onSuccess:getDeviceVersion,
						onFailure:showError
						});
}

//Ajax�ص���������ȡ�汾��Ϣ��չʾ	
function getDeviceVersion(response){
	//alert(response.responseText);
	document.getElementById("divVersion").innerHTML = response.responseText;
}
//Debug
function showError(response){
	//alert();
	document.getElementById("divVersion").innerHTML = "��ȡ����汾��ѯʧ�ܣ������ԣ�";
}

//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<jsp:include page="/share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="checkbox"/>
	<jsp:param name="jsFunctionName" value=""/>
</jsp:include>
<TABLE border=0 cellspacing=0 cellpadding=0 width="95%" align="center">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<tr><td>
	<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">����汾����</div>
				</td>
				<td><img src="../images/attention_2.gif" width="15" height="12">�汾ʵʱ��ѯ</td>
			</tr>
		</table>
</td></tr>
<TR><TD>
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR >
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=0 width="100%" id="outTable">
					<TR>
						<TH bgcolor="#ffffff" colspan="4" align="center">����汾��ѯ</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td colspan="4">
							<div id="selectDevice"><span>������....</span></div>
						</td>
					</TR>
					<TR class="green_foot">
						<TD  align="right" height="23" colspan=4>
							<input type=button name="queryBtn" value=" �� ѯ " onclick="checkForm();">
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>
<IFRAME name=childFrm SRC="" STYLE="display:none"></IFRAME>
<IFRAME name=childFrm1 SRC="" STYLE="display:none"></IFRAME>
&nbsp;</TD></TR>
<tr><td></td></tr>
<tr><td><DIV ID="divVersion" align=center></DIV>
<div id="_debug"></div></td></tr>
</TABLE>
<br>
<%@ include file="../foot.jsp"%>