<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />

<%
	request.setCharacterEncoding("GBK");
	String strCityList = DeviceAct.getCityListSelf(false, "", "", request);
	String strVendorList = DeviceAct.getVendorList(true, "", "");
	String gw_type = request.getParameter("gw_type");
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
function showChild(parname){
	if(parname=="vendor_id"){
	    var type=1;		
		var o = $("vendor_id");
		var id = o.options[o.selectedIndex].value;
		var url = "getDeviceChild.jsp";
		var pars = "vendor_id=" + id;
		pars +="&type="+type;
		var myAjax
			= new Ajax.Request(
							url,
							{method:"post",parameters:pars,
							onComplete:function(req){getData(type,req);},
							onFailure:showError}						
						   );
		$("sp_DeviceVersion").innerHTML = "";
	}
    if(parname=="device_model_id")
    {
        var type=2;
		o =$("device_model_id");
		id=o.options[o.selectedIndex].value;
		var url = "getDeviceChild.jsp";
		pars ="device_model_id="+id;
		pars +="&type="+type;
		var myAjax
			= new Ajax.Request(
							url,
							{method:"post",parameters:pars,
							onComplete:function(req){getData(type,req);},
							onFailure:showError}						
						   );
    }
}


function getData(type ,request)
{
 //alert("wp!");
 if(type==1)
 {
    $("sp_DeviceModel").innerHTML = " �豸�ͺţ�" + request.responseText;
 }
 else if(type==2)
 {
    $("sp_DeviceVersion").innerHTML = " �豸�汾��" + request.responseText;
 }
}
function getDeviceModel(request){
	$("sp_DeviceModel").innerHTML = " �豸�ͺţ�" + request.responseText;
}
//Debug
function showError(request){
	alert(request.responseText);
}
//���ݲ�ѯ���ͳ�ʼ��ѡ���
function changeType(type){
	
	$("sp_DeviceModel").innerHTML = "";
	$("sp_DeviceVersion").innerHTML = "";
	$("vendor_id").value = "-1";
	
}
//-->
</SCRIPT>

<%@ include file="../toolbar.jsp"%>
<form name="queryFrm" method="POST" action="userDevListData.jsp" target="dataFrame">
<table width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
	<tr>
		<td height="20"></td>
	</tr>
	<TR>
		<TD colspan="2">
		<TABLE width="100%" height="30" border="0" cellspacing="0"
			cellpadding="0" class="green_gargtd">
			<TR>
				<TD width="164" align="center" class="title_bigwhite">�豸��Դ</TD>
				<td>
					<img src="../images/attention_2.gif" width="15" height="12">
					�豸�ʺŶ�Ӧ��ϵ��ѯ
				</td>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<TR>
		<TD bgcolor=#999999>
		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">
			<TR class=column>
				<TH colspan="4" align="center">�豸�ʺŲ�ѯ</TH>
			</TR>
			<TR>
				<TD class="column" align="right" height="23" width="15%">�豸���к�</TD>
				<TD bgcolor="#ffffff" align="left" height="23" width="35%">
					<input type="text" value="" name="device_serial" class="bk">
				</TD>
				<TD class="column" align="right" height="23" width="15%">�豸IP��ַ</TD>
				<TD bgcolor="#ffffff" align="left" height="23" width="35%">
					<input type="text" value="" name="loopback_ip" class="bk">
				</TD>
			</TR>

			<TR>
				<TD class="column" align="right" height="23" width=120>����</TD>
				<TD bgcolor="#ffffff" align="left" height="23"><%=strCityList%></TD>
				<TD class="column" align="right" height="23">����״̬</TD>
				<TD bgcolor="#ffffff" align="left" height="23">
					<select name="status" class="bk">
					<option value="-1">==��ѡ��==</option>
					<option value="0">����</option>
					<option value="1">����</option>
					<option value="2">δ֪</option>
				</select></TD>
			</TR>
			<TR>
				<TD class="column" align="right" height="23">����</TD>
				<TD bgcolor="#ffffff" align="left" height="23" nowrap colspan=3>
				<div>
					<span><%=strVendorList%></span> &nbsp;
					<span id="sp_DeviceModel"></span> &nbsp;
					<span id="sp_DeviceVersion"></span>
				</div>
				</TD>
			</TR>
			<TR>
				<TD class="column" align="right" height="23" colspan=4>
					<input type="submit" name="queryBtn" value=" �� ѯ " class="btn">
					<INPUT TYPE="hidden" name="gw_type" value=<%=gw_type%>>
				</TD>
			</TR>
		</table>
		</TD>
	</TR>
</table>
</form>
</html>