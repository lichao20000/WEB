<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%
//采集点
String gatherList = deviceAct.getGatherList(session, "", "", true);

//设备厂商
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
  if(document.frm.gather_id.value == -1)
  {
	alert("请选择采集点！");
	document.frm.gather_id.focus();
	return false;
  }
 if(document.frm.devicetype_id.value == -1)
 {
	alert("请选择设备型号！");
	document.frm.devicetype_id.focus();
	return false;
 }
 var oselect = document.all("device_id");
 if(oselect == null)
 {
	alert("请选择设备！");
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
	alert("请选择设备！");
	return false;
 }
 
 var temp = document.all("instance").value;
 if(temp=="")
 {
  alert("请填写实例！");
  return false;
 }
 
 temp = document.all("paramvalue").value;
 if(temp=="")
 {
   alert("请填写参数值！");
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
	if(param == "devicetype_id")
	{
		document.frm.device.checked = false;
		//softwareversion下拉框的value为 devicetype_id
		//devicetype_id下拉框的value为 device_model
		page = "showDevice.jsp?gather_id="+document.frm.gather_id.value + "&devicetype_id="+document.frm.softwareversion.value;
		document.all("childFrm").src = page;
	}
	if(param == "vendor_id")
	{
		page = "showDevicetype.jsp?vendor_id="+document.frm.vendor_id.value;
		document.all("childFrm1").src = page;	
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
			<FORM NAME="frm" METHOD="post" ACTION="paramInstanceConfig_submit.jsp" onsubmit="return CheckForm()">
				<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite">
									参数实例管理
								</td>
								<td>
									<img src="../images/attention_2.gif" width="15" height="12">
									对选择的设备进行参数实例配置。
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
													参数实例增加及配置
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
											    <TD align="right" width="20%">
													采集点:
												</TD>
												<TD align="left" width="30%">
												    <%=gatherList%>
												</TD>
												<TD align="right" width="20%">
													厂商:
												</TD>
												<TD align="left" width="30%">
													<div id="div_vendor">
														<select name="vendor" class="bk">
															<option value="-1">
																--请先选择采集点--
															</option>
														</select>													
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
											    <TD align="right" width="20%">
													设备型号:
												</TD>
												<TD width="30%" colspan=3>
													<div id="div_devicetype">
														<select name="devicetype_id" class="bk">
															<option value="-1">
																--请先选择厂商--
															</option>
														</select>
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right">
													设备列表:
													<br>
													<INPUT TYPE="checkbox" onclick="selectAll('device_id')" name="device">
													全选
												</TD>
												<TD colspan="3">
													<div id="div_device" style="width:95%; height:100px; z-index:1; top: 100px; overflow:scroll">
														<span>请先选择设备型号！</span>
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
											    <TD align="right" width="20%">
											       实例:
											    </TD>
											    <TD align="left" width="30%">
											       <input type="text" name="instance" class=bk size=20 maxLength=255>&nbsp;<font color="#FF0000">*</font>
											    </TD>
											    <TD align="right" width="20%">
											       关键字:
											    </TD>
											    <TD align="left" width="30%">
											       <input type="text" name="keyword" class=bk size=20 maxLength=255>
											    </TD>
											</TR>
											<TR bgcolor="#FFFFFF">
											    <TD align="right" width="20%">
											       参数值:
											    </TD>
											    <TD align="left" width="30%" colspan=3>
											       <input type="text" name="paramvalue" class=bk size=20 maxLength=255>&nbsp;<font color="#FF0000">*</font>
											    </TD>											    
											</TR>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
													<INPUT TYPE="hidden" name="action" value="add">
													<INPUT TYPE="hidden" name="service_id" value="7">												
													<INPUT TYPE="submit" value=" 保 存 " class=btn>
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
		</TD>
	</TR>
</TABLE>