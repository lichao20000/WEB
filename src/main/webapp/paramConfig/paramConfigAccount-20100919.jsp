<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<jsp:useBean id="deviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%
	//�ɼ���
	String gatherList = deviceAct.getGatherList(session, "", "", true);

	//�豸����
	String strVendorList = deviceAct.getVendorList(true, "", "");
%>
<SCRIPT LANGUAGE="JavaScript">
function selectAll(elmID){
		t_obj = document.all(elmID);
		if(!t_obj) return;
		obj = event.srcElement;
		if(obj.checked){
			if(typeof(t_obj) == "object" ) {
				if(typeof(t_obj.length) != "undefined") {
					for(var i=0; i<t_obj.length; i++){
						t_obj[i].checked = true;
					}
				} else {
					t_obj.checked = true;
				}
			}
		
		}else{
			if(typeof(t_obj) == "object" ) {
				if(typeof(t_obj.length) != "undefined") {
					for(var i=0; i<t_obj.length; i++){
						t_obj[i].checked = false;
					}
				} else {
					t_obj.checked = false;
				}
			}
		}
	}

function CheckForm()
{
  var hguser=document.all("hguser").value;
    var telephone= document.all("telephone").value;
    var checkradios = document.all("checkType");
    var checkType = "";
    for(var i=0;i<checkradios.length;i++)
    {
      if(checkradios[i].checked)
	  {
	    checkType = checkradios[i].value;
	    break;
	  }
    }
	if(checkType==0&&document.frm.gather_id.value == -1){
		alert("��ѡ��ɼ��㣡");
		document.frm.gather_id.focus();
		return false;
  	}
  	 if(checkType==0&&document.frm.vendor_id.value == -1){
		alert("��ѡ���̣�");
		document.frm.vendor_id.focus();
		return false;
 	}
 	if(checkType==0&&document.frm.devicetype_id.value == -1){
		alert("��ѡ���豸�ͺţ�");
		document.frm.devicetype_id.focus();
		return false;
 	}
 	if(checkType==0&&document.frm.softwareversion.value == -1){
			alert("��ѡ���豸�汾��");
			document.frm.softwareversion.focus();
			return false;
		}
		
	if(checkType==1&&""==hguser&&""==telephone)
	{
	  alert("����д�û�����绰���룡");
	  document.all("hguser").focus();
	  return false;
	}
 var oselect = document.all("device_id");
 if(oselect == null)
 {
	alert("��ѡ���豸��");
	return false;
 }
 var num = 0;
 if(typeof(oselect.length)=="undefined")
 {
	if(oselect.checked)
	{
		num = 1;
	}
 }
 else
 {
	for(var i=0;i<oselect.length;i++)
	{
		if(oselect[i].checked)
		{
		  num++;
		}
	}

 }
 if(num ==0)
 {
	alert("��ѡ���豸��");
	return false;
 }
 
 
 return true;
}




function showChild(param)
{
	var page ="";
	if(param == "gather_id")
	{
		document.all("div_vendor").innerHTML = "<%=strVendorList%>";
	}
	if(param == "device_model_id"){

		    page = "showDeviceVersion.jsp?vendor_id="+document.frm.vendor_id.value + "&device_model_id="+encodeURIComponent(document.frm.device_model_id.value);
			document.all("childFrm2").src = page;	
		}
	if(param == "softwareversion")
	{

		//document.frm.device.checked = false;
		//softwareversion�������valueΪ devicetype_id
		//devicetype_id�������valueΪ device_model
		page = "showDeviceofuser.jsp?gather_id="+document.frm.gather_id.value+"&vendor_id="+document.frm.vendor_id.value+"&devicetype_id="+document.frm.softwareversion.value;
		document.all("childFrm").src = page;
	}
	if(param == "vendor_id")
	{  
		page = "showDevicetype.jsp?vendor_id="+document.frm.vendor_id.value;
		document.all("childFrm1").src = page;	
	}
}

function relateDevice()
{
   var hguser=document.all("hguser").value;
   var telephone= document.all("telephone").value;
  // var checkradios = document.all("checkType");
   
  // var checkType = "";
  // for(var i=0;i<checkradios.length;i++)
 //  {
  //   if(checkradios[i].checked)
//	 {
//	   checkType = checkradios[i].value;
//	   break;
//	 }
 //  }
   //alert("hguser:"+hguser+"   telephone:"+telephone+"   checkType:"+checkType);
   if(""==hguser&&""==telephone)
   {
      alert("����д�û�����绰���룡");
      document.all("hguser").focus();
      return false;
   }
  
      var page="";
      page="showDeviceofuser.jsp?hguser="+hguser+"&telephone="+telephone+"&refresh="+Math.random();
      document.all("childFrm1").src = page;
 
   
}

function relateDevicebyOUI(){
    var oui=document.all("oui").value;
    var flg="getoui";
    var page="";
      page="showDeviceofuser.jsp?oui="+oui+"&flag=" + flg +"&refresh="+Math.random();
      document.all("childFrm1").src = page;
}

</SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" border="0">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="frm" METHOD="post" ACTION="showDeviceofuser.jsp"
			onsubmit="return CheckForm()">
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="text">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd" border="0">
					<tr>
						<td width="162" align="center" class="title_bigwhite">����ʵ������
						</td>
						<td><img src="../images/attention_2.gif" width="15"
							height="12"> ��ѡ����豸���������ʺ�ά����</td>

					</tr>
				</table>
				</td>
			</tr>
			<tr>

				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">

					<TR bgcolor="#FFFFFF"��>
						<TH colspan="4" align="center">�����豸�汾��ѯ</TH>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr1" STYLE="display: ">
						<TD align="right" width="20%">�ɼ���:</TD>
						<TD align="left" width="30%"><%=gatherList%></TD>
						<TD align="right" width="20%">����:</TD>
						<TD align="left" width="30%">
						<div id="div_vendor"><select name="vendor" class="bk">
							<option value="-1">--����ѡ��ɼ���--</option>
						</select></div>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr2" STYLE="display: ">
						<TD align="right" width="20%">�豸�ͺ�:</TD>
						<TD width="30%">
						<div id="div_devicetype"><select class="bk">
							<option value="-1">--����ѡ����--</option>
						</select></div>
						</TD>
						<TD align="right" width="20%">�豸�汾:</TD>
						<TD width="30%">
						<div id="div_deviceversion"><select name="device_version"
							class="bk">
							<option value="-1">--����ѡ���豸�ͺ�--</option>
						</select></div>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF"��>
						<TH colspan="4" align="center">�����û�����绰�����ѯ</TH>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr3">
						<TD align="right" width="20%">�û���:</TD>
						<TD width="30%"><input type="text" name="hguser" class="bk"
							value=""></TD>
						<TD align="right" width="20%">�û��绰����:</TD>
						<TD width="30%"><input type="text" name="telephone"
							class="bk" value=""> <input type="button" class=btn
							value=" �� ѯ " onclick="relateDevice()"></TD>
					</TR>
					<TR bgcolor="#FFFFFF"��>
						<TH colspan="4" align="center">���ݳ���OUIģ����ѯ</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="right" width="20%">OUI:</TD>
						<TD width="30%" colspan=3><input type="text" name="oui"
							class="bk" value=""> <input type="button" class=btn
							value=" �� ѯ " onclick="relateDevicebyOUI()">(������������ѯȫ���豸)</TD>
					</TR>
					<TR bgcolor="#FFFFFF"��>
						<TH colspan="4" align="center">�豸��ϸ��Ϣ</TH>
					</TR>

				</TABLE>
				</TD>

			</tr>
			<TR bgcolor="#FFFFFF">
				<TD colspan="4"><!-- 	<div id="div_device" style="width:100%; height:100px; z-index:1; top: 100px; overflow:scroll"> -->
				<div id="div_device"><!--<span>�밴������ѯ��</span>-->
				</div>
				</TD>
			</TR>

			<TR>
				<TD colspan="4" align="right" CLASS="green_foot">
				<INPUT TYPE="hidden" name="action" value="account"> 
				<INPUT TYPE="hidden" name="service_id" value="4">
				</TD>
			</TR>
		</table>
		</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>&nbsp; 
		<IFRAME ID=childFrm SRC=""	STYLE="display: none"></IFRAME> 
		<IFRAME ID=childFrm1 SRC=""	STYLE="display: none"></IFRAME> 
		<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>

<br>
<%@ include file="../foot.jsp"%>
