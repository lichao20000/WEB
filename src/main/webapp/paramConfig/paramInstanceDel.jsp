<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%
String gather_id = request.getParameter("gather_id");
String vendor_id = request.getParameter("vendor_id");
String devicetype_id = request.getParameter("devicetype_id");
String softwareversion = request.getParameter("softwareversion");
String checkType = request.getParameter("checkType");
String hguser = request.getParameter("hguser");
if(null==hguser)
{
	hguser ="";
}
String telephone = request.getParameter("telephone");
if(null==telephone)
{
	telephone ="";
}

//�ɼ���
String gatherList = deviceAct.getGatherList(session, "", "", true);

//�豸����
String strVendorList = deviceAct.getVendorList(true, "", "");
%>
<SCRIPT LANGUAGE="JavaScript">

function CheckForm(){
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
 	if(oselect == null){
		alert("��ѡ���豸��");
		return false;
	 }
	var num = 0;
	if(typeof(oselect.length)=="undefined"){
		if(oselect.checked){
			num = 1;
		}
 	}else{
		for(var i=0;i<oselect.length;i++){
			if(oselect[i].checked){
			  num++;
			}
		}

 	}
 	if(num ==0){
		alert("��ѡ���豸��");
		return false;
	}
}

function showChild(param)
{
	var page ="";
	if(param == "gather_id")
	{
		document.all("div_vendor").innerHTML = "<%=strVendorList%>";
	}
//	if(param == "devicetype_id"){
	if(param == "device_model_id"){
		// document.frm.device.checked = false;
		page = "showDeviceVersion.jsp?vendor_id="+document.frm.vendor_id.value + "&device_model_id="+document.frm.device_model_id.value;
		document.all("childFrm2").src = page;
	}
	if(param == "softwareversion")
	{		
		page = "showDevice.jsp?gather_id="+document.frm.gather_id.value +"&vendor_id="+document.frm.vendor_id.value+ "&devicetype_id="+document.frm.softwareversion.value+"&flag=paramInstanceadd_Config&refresh="+Math.random();
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
   //alert("hguser:"+hguser+"   telephone:"+telephone+"   checkType:"+checkType);
   if(1==checkType&&""==hguser&&""==telephone)
   {
      alert("����д�û�����绰���룡");
      document.all("hguser").focus();
   }
   else if(1==checkType)
   {
      var page="";
      page="showDevice.jsp?hguser="+hguser+"&telephone="+telephone+"&flag=paramInstanceadd_Config&refresh="+Math.random();
      document.all("childFrm1").src = page;
   }
   
}


function ShowDialog(param)
{
  //�����û�����ѯ
  if(param==1)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="";
  }
  
  //�����豸�汾����ѯ
  if(param==0)
  {
     tr1.style.display="";
     tr2.style.display="";
     tr3.style.display="none";
  }
}

function selectHgw()
{
   var checkradios = document.all("checkType");
   for(var i=0;i<checkradios.length;i++)
   {
     if(checkradios[i].value==1)
     {
       checkradios[i].checked = true;
       break;
     }
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
			<FORM NAME="frm" METHOD="post" ACTION="getConfig_DelPara.jsp" onsubmit="return CheckForm()">
				<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite">
									����ʵ������
								</td>
								<td>
									<img src="../images/attention_2.gif" width="15" height="12">
									��ѡ����豸ɾ������ʵ�����á�
									<input type="radio" value="0" onclick="ShowDialog(this.value)" name="checkType" checked>�����豸�汾��ѯ&nbsp;&nbsp;
			                        <input type="radio" value="1" onclick="ShowDialog(this.value)" name="checkType">�����û���ѯ�豸
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR>
												<TH colspan="4" align="center">
													����ʵ����ȡ
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr1" STYLE="display:">
											    <TD align="right" width="20%">
													�ɼ���:
												</TD>
												<TD align="left" width="30%">
												    <%=gatherList%>
												</TD>
												<TD align="right" width="20%">
													����:
												</TD>
												<TD align="left" width="30%">
													<div id="div_vendor">
														<select name="vendor" class="bk">
															<option value="-1">
																--����ѡ��ɼ���--
															</option>
														</select>													
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr2" STYLE="display:">
											    <TD align="right" width="20%">
													�豸�ͺ�:
												</TD>
												<TD width="30%">
													<div id="div_devicetype">
														<select name=devicetype_id class="bk">
															<option value="-1">
																--����ѡ����--
															</option>
														</select>
													</div>
												</TD>
												<TD align="right" width="20%">
													�豸�汾:
												</TD>
												<TD width="30%">
													<div id="div_deviceversion">
														<select name="device_version" class="bk">
															<option value="-1">
																--����ѡ���豸�ͺ�--
															</option>
														</select>													
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr3" STYLE="display:none">
											    <TD align="right" width="20%">
													�û���:
												</TD>
												<TD width="30%">
													<input type="text" name="hguser" value="">
												</TD>
												<TD align="right" width="20%">
													�û��绰����:
												</TD>
												<TD width="30%">
													<input type="text" name="telephone" value="">
													<input type="button" class=btn value="��ѯ" onclick="relateDevice()">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right">
													�豸�б�:
													<br>
													<!-- 
													<INPUT TYPE="checkbox" onclick="selectAll('device_id')" name="device">
													ȫѡ
													-->
												</TD>
												<TD colspan="3">
													<div id="div_device" style="width:95%; height:100px; z-index:1; top: 100px; overflow:scroll">
														<span>��ѡ���豸��</span>
													</div>
												</TD>
											</TR>
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
<script language="javascript">
<%if ("0".equals(checkType)){%>
	document.frm.gather_id.value = "<%=gather_id%>";
	document.all("div_vendor").innerHTML = "<%=strVendorList%>";
	document.frm.vendor_id.value = "<%=vendor_id%>";
	
	page = "showDevicetype.jsp?vendor_id=<%=vendor_id%>&defaultValue=<%=devicetype_id%>";
	document.all("childFrm1").src = page;
	page = "showDeviceVersion.jsp?vendor_id=<%=vendor_id%>&devicetype_id=<%=devicetype_id%>&defaultValue=<%=softwareversion%>";
	document.all("childFrm2").src = page;
	page = "showDevice.jsp?gather_id=<%=gather_id%>&vendor_id=<%=vendor_id%>&devicetype_id=<%=softwareversion%>&flag=paramInstanceadd_Config&refresh="+Math.random();
	document.all("childFrm").src = page;
<%}else if("1".equals(checkType)){%>
    document.all("telephone").value="<%=telephone%>";
    document.all("hguser").value="<%=hguser%>";
    selectHgw();
    ShowDialog(1);
    relateDevice();  
<%}%>
</script>
<%@ include file="../foot.jsp"%>