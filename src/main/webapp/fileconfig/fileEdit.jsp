<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags" %>
<jsp:useBean id="fileManage" scope="request" class="com.linkage.litms.filemanage.FileManage"/>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage" />
<%
    //版本文件信息      
    Map fields = (Map)fileManage.getFileMap(request);
    
    String softwarefile_id ="";
	String filename = "";
	String vendor_id = "";
	String device_model_id = "";
	String devicetype_id = "";
	String filesize = "";
	String filestatus ="";
	String server_name = "";
	//String server_tomcat = "";
	String server_path = "";
	String remark = "";
	String cityList = "";
	String serviceList = "";
	if(fields != null){
		
	    softwarefile_id = (String)fields.get("softwarefile_id");
	    
		filename = (String)fields.get("softwarefile_name");
		device_model_id = (String)fields.get("device_model_id");
		devicetype_id = (String)fields.get("devicetype_id");
		filesize = (String)fields.get("softwarefile_size");
		filestatus = (String)fields.get("softwarefile_status");
		server_name = (String)fields.get("server_name");
		//server_tomcat = (String)fields.get("tomcat_url");
		server_path = (String)fields.get("server_dir");
		remark = (String)fields.get("softwarefile_description");
		cityList = (String)fields.get("citylist");
		serviceList = (String)fields.get("servicelist");
	}
	
	String sql_vendor_id = "select vendor_id from gw_device_model  where device_model_id ='" + device_model_id + "'";
	Cursor cursor_oui = DataSetBean.getCursor(sql_vendor_id);
	Map fields_oui = cursor_oui.getNext();
	if(null!=fields_oui){
		vendor_id = (String)fields_oui.get("vendor_id");
	}
	
	//厂商下拉框
	String strVendorList = versionManage.getStrVendorList(true,vendor_id,"");
	//设备型号下拉框
	String devicemodelidlist = versionManage.getDevicemodelidlist(device_model_id,true,vendor_id);
	//软件版本型号下拉框
	String deviceTypeList = versionManage.getDeviceTypeList(devicetype_id,false,device_model_id);
	

%>

<style type="text/css">
<!--
select {
	position:relative;
	font-size:12px;
	width:160px;
	line-height:18px;border:0px;
	color:#909993;
}
-->
</style>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
	function checkform(){
		var filetype=0;

		for(var i=0;i< document.frm.filestatus.length;i++)
		{
			if(document.frm.filestatus[i].checked)
			{
				filetype=document.frm.filestatus[i].value;
				break;
			}
		}	
		document.frm.file_status.value=filetype;
		if(!IsNull(document.frm.filename.value,"文件名")){
			document.frm.filename.focus();
			document.frm.filename.select();
			return false;
		}
		if(document.frm.devicetype_id.value==-1){
			alert("请选择设备型号！");
			document.frm.devicetype_id.focus();
			return false;		
		}
		
		var citys="";
		var services="";
		for (var i=0;i<document.frm.elements.length;i++)
   	 	{
			var e = document.frm.elements[i];
			if (e.name == 'city' && e.checked==true)
			{
				citys=citys+","+document.frm.elements[i].value;
			}
			if (e.name == 'service' && e.checked==true)
			{
				services=services+","+document.frm.elements[i].value;
			}
   		}
    	citys = citys + ",";
    	services = services + ",";
	
		document.frm.cityList.value = citys;
		document.frm.serviceList.value = services;
		
		return true;
	}
	function GoList(page){
		this.location = page;
	}
	function showChild(param){
		if(param == "vendor_id"){
			page = "showDeviceModel.jsp?vendor_id="+document.frm.vendor_id.value;
			document.all("childFrm").src = page;		
		}
		if(param == "device_model_id"){
			page = "showSoftwareVersion.jsp?device_model_id="+document.frm.device_model_id.value;
			document.all("childFrm1").src = page;		
		}
	}
	function ChkAllCity()
	{
		for (var i=0;i<frm.elements.length;i++)
		{
		var e = frm.elements[i];
		if (e.name != 'filestatus' && e.name!="cityAll" && e.name!="service" && e.name!="serviceAll")
		   e.checked = frm.cityAll.checked;
		}
	}
	
	function ChkAllService()
	{
		for (var i=0;i<frm.elements.length;i++)
		{
		var e = frm.elements[i];
		if (e.name != 'filestatus' && e.name!="serviceAll" && e.name!="city" && e.name!="cityAll")
		   e.checked = frm.serviceAll.checked;
		}
	}
</SCRIPT>

<FORM NAME="frm" METHOD="post" ACTION="fileSave.jsp" onsubmit="return checkform()">

<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="98%" BORDER="0" align="center">
<TR>
	<TD valign=top>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
		<TR>
			<TD HEIGHT=20>&nbsp;</TD>
		</TR>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							版本文件管理
						</td>
						<td>&nbsp;
							<img src="../images/attention_2.gif" width="15" height="12">
								&nbsp;上传版本文件到版本文件服务器。带<font color="red">*</font>的必须选择或输入.
						</td>
					</tr>
				</table>
			</td>
		</tr>		
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>修改版本文件</B></TH>
				</TR>
				<tr bgcolor="#FFFFFF">
				<TD class=column align="right" width="15%" nowrap><font color="red">*</font>&nbsp;设备厂商</TD>
					<TD width="50%">
						<%=strVendorList%>
					</TD>
					<TD class=column align="right" width="15%" nowrap><font color="red">*</font>&nbsp;设备型号</TD>
					<TD width="30%">
						<div id="div_device_model_id">
							<%=devicemodelidlist%>
						</div>
					</TD>
					</tr>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap><font color="red">*</font>&nbsp;目标版本</TD>
					<TD >
					<div id="div_devicetype">
						<%=deviceTypeList%>
					</div>
					</TD>
					<TD class=column align="right" >状态</TD>
					<%
						String strCheck1 = "";
						String strCheck2 = "";
						if(filestatus.equals("1")){
							strCheck1 = "checked";
						}else{
							strCheck2 = "checked";
						}					
					%>
					<TD >
						<input type="radio"  name="filestatus" value="1" <%=strCheck1%>>已审核
						<input type="radio"  name="filestatus" value="2" <%=strCheck2%>>未审核
						<input type="hidden" name="file_status" value="">
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap><font color="red">*</font>&nbsp;名称</TD>
					<TD >
						<INPUT TYPE="text" NAME="filename" class="bk" value="<%=filename%>" readOnly>&nbsp;
					</TD>
					<TD class=column align="right" ><font color="red">*</font>&nbsp;文件大小</TD>
					<TD>
						<input type="text" name="file_size" value="<%=filesize%>" class="bk" readOnly >&nbsp;(Byte)				
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" >
					<TD class=column align="right" nowrap><font color="red">*</font>&nbsp;存储路径</TD>
					<TD colspan="3" nowrap>
						<input type="text" size="80" name="filePath" class="bk" value="<%=server_name%>|<%=server_path%>" readOnly>
					</TD>
				</TR>
					<ms:inArea areaCode="sx_lt" notInMode="true">
						<tr bgcolor="#FFFFFF">
							<td class=column>
								<div align="center">请选择属地<br><input type=checkbox name=cityAll
																	onclick="javascript:ChkAllCity();">全选
								</div>
							</td>
							<td>
								<%=com.linkage.litms.netcutover.CommonForm.getCityCheckBox(cityList)%>
								<input type="hidden" name="cityList" value="">
							</td>
							<td class=column>
								<div align="center">请选择业务<br><input type=checkbox name=serviceAll
																	onclick="javascript:ChkAllService();">全选
								</div>
							</td>
							<td>
								<%=com.linkage.litms.filemanage.FileManage.getServiceCheckBox(serviceList)%>
								<input type="hidden" name="serviceList" value="">
							</td>
						</tr>
					</ms:inArea>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">描述</TD>
					<TD colspan="3">
						<textarea name="remark" cols="70" class="bk" rows="4"><%=remark%></textarea>
					</TD>
				</TR>
				<TR >
					<TD colspan="4" align="right" class='green_foot'>
						<INPUT TYPE="submit" name="save" value=" 保 存 " class=btn>&nbsp;&nbsp;
						<INPUT TYPE="reset" name="reset" value=" 重 写 " class=btn>
						<INPUT TYPE="hidden" name="action" value="update">
						<input TYPE="hidden" name="softwarefile_id" value="<%=softwarefile_id%>">
						<iframe id="uploadFrm" STYLE="display:none" name="uploadFrm" FRAMEBORDER=0 SCROLLING=NO src="FileUpload.jsp?type=office"  height="25" width=600>
						<IFRAME ID="childFrm" SRC="" STYLE="display:none"></IFRAME>						
						</iframe>
						<IFRAME ID="childFrm1" SRC="" STYLE="display:none"></IFRAME>
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
		<TR><TD></TD></TR>
	</TABLE>
	</TD>
</TR>
</TABLE>

</FORM>
<%@ include file="../foot.jsp"%>
